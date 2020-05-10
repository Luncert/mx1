package org.luncert.mx1.probe.ipc.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.commons.data.NetURL;
import org.luncert.mx1.commons.util.MarshallingCodeCFactory;
import org.luncert.mx1.probe.ipc.Connector;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class TcpConnector<E> implements Connector<E> {
  
  // single thread for handling establishing of connection
  private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  
  private EventLoopGroup workerGroup;
  
  private SocketAddress serveAddr;
  
  private SocketAddress dest;
  
  private Channel nettyChannel;
  
  private volatile Channel remoteChannel;
  
  private IpcChannel tcpChannel = new TcpChannel();
  
  private List<IpcDataHandler<E>> handlerList = new LinkedList<>();
  
  public TcpConnector() {
  }
  
  public TcpConnector<E> addHandler(IpcDataHandler<E> handler) {
    handlerList.add(handler);
    return this;
  }
  
  public TcpConnector<E> bind(SocketAddress serveAddr) {
    if (dest != null) {
      throw new UnsupportedOperationException("destination has been provided");
    }
    
    this.serveAddr = serveAddr;
    return this;
  }
  
  public TcpConnector<E> destination(SocketAddress dest) {
    if (serveAddr != null) {
      throw new UnsupportedOperationException("serving address has been provided");
    }
    
    this.dest = dest;
    return this;
  }
  
  @Override
  public IpcChannel open() throws IOException {
    try {
      ChannelFuture channelFuture;
      if (dest != null) {
        Bootstrap bootstrap = new Bootstrap()
            .group(bossGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new NettyInitializer());
  
        channelFuture = bootstrap.connect(dest);
      } else if (serveAddr != null) {
        workerGroup = new NioEventLoopGroup();
        
        ServerBootstrap bootstrap = new ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG,128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new NettyInitializer());
        
        channelFuture = bootstrap.bind(serveAddr);
      } else {
        throw new IOException("neither serving address or destination is provided");
      }
  
      nettyChannel = channelFuture.sync().channel();

      if (channelFuture.isSuccess()) {
        log.debug("TCP connection opened");
      }
      
      return tcpChannel;
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
  
  private class NettyInitializer extends ChannelInitializer<SocketChannel> {
  
    @Override
    public void initChannel(SocketChannel ch) {
      ch.pipeline()
          // TooLongFrameException: default frame length limit: 4096
          // ref:
          // https://www.jianshu.com/p/a0a51fd79f62
          // https://docs.jboss.org/netty/3.1/api/org/jboss/netty/handler/codec/frame/LengthFieldBasedFrameDecoder.html
          // https://stackoverflow.com/questions/53147024/netty-tcp-client-server-file-transfer-exception-toolongframeexception
          //.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4))
          //.addLast("frameEncoder", new LengthFieldPrepender(4))
          .addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) // outbound handler
          .addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) // inbound handler, max frame length: 1024 * 1024
          .addLast(new TcpDataHandler());
      // create new TcpDataHandler for each connection
    }
  }
  
  private class TcpDataHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object rawMsg) throws IOException {
      E msg = (E) rawMsg;
      for (IpcDataHandler<E> handler : handlerList) {
        handler.onData(tcpChannel, msg);
      }
  
      ctx.fireChannelRead(rawMsg);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
      remoteChannel = ctx.channel();
  
      for (IpcDataHandler<E> handler : handlerList) {
        handler.onOpen(tcpChannel);
      }
      
      ctx.fireChannelActive();
    }
  
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
      remoteChannel = null;
  
      for (IpcDataHandler<E> handler : handlerList) {
        handler.onClose();
      }
      
      ctx.fireChannelInactive();
    }
  
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      // NOTE: If remote is closed when server is reading data, it will throw following exception:
      // An existing connection was forcibly closed by the remote host.
      ctx.fireExceptionCaught(cause);
    }
  }
  
  private class TcpChannel extends IpcChannel {
  
    @Override
    public synchronized void write(Object object) throws IOException {
      checkRemoteAvailability();
  
      // NOTE: check whether obj and its fields are all serializable
      ChannelFuture future = remoteChannel.writeAndFlush(object);
      
      // TODO: figure out the reason why receiver cannot receive data
      //  if I delete following part.
      try {
        future.sync();
      } catch (InterruptedException e) {
        throw new IOException(e);
      }
      
      if (!future.isSuccess()) {
        throw new IOException("failed to write object");
      }
    }
    
    @Override
    public void sync() throws IOException {
      try {
        // channel.close doesn't mean to close the tcp server,
        // but just request to close this channel and block current thread,
        // the channelFuture will be notified once the server is down
        nettyChannel.closeFuture().sync();
    
        log.debug("TCP connection closed");
      } catch (InterruptedException e) {
        throw new IOException(e);
      }
    }
    
    @Override
    public NetURL getRemoteAddress() throws IOException {
      checkRemoteAvailability();
      
      InetSocketAddress socketAddress = (InetSocketAddress) remoteChannel.remoteAddress();
      return new NetURL("tcp", socketAddress.getHostString(), socketAddress.getPort());
    }
    
    private void checkRemoteAvailability() throws IOException {
      if (remoteChannel == null) {
        throw new IOException("remote is not available");
      }
    }
    
    @Override
    public void close() {
      bossGroup.shutdownGracefully();
      if (workerGroup != null) {
        workerGroup.shutdownGracefully();
      }
    }
  }
}
