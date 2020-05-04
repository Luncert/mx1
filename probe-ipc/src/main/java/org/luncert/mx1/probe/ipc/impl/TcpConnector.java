package org.luncert.mx1.probe.ipc.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.ipc.Connector;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class TcpConnector<E> implements Connector<E> {
  
  
  private EventLoopGroup bossGroup = new NioEventLoopGroup();
  
  private EventLoopGroup workerGroup;
  
  private TcpDataHandler nettyDataHandler;
  
  private SocketAddress serveAddr;
  
  private SocketAddress dest;
  
  private Channel channel;
  
  public TcpConnector() {
    nettyDataHandler = new TcpDataHandler();
  }
  
  public TcpConnector<E> addHandler(IpcDataHandler<E> handler) {
    nettyDataHandler.addHandler(handler);
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
  public IpcChannel<E> open() throws IOException {
    try {
      ChannelFuture channelFuture;
      if (dest != null) {
        Bootstrap bootstrap = new Bootstrap()
            .group(bossGroup)
            .channel(NioSocketChannel.class)
            .handler(nettyDataHandler);

        channelFuture = bootstrap.connect(dest);
      } else if (serveAddr != null) {
        workerGroup = new NioEventLoopGroup();
        
        ServerBootstrap bootstrap = new ServerBootstrap()
            .group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
              @Override
              public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast(new ObjectEncoder());
                pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
    
                pipeline.addLast(nettyDataHandler);
              }
            });
        
        channelFuture = bootstrap.bind(serveAddr);
      } else {
        throw new IOException("neither serving address or destination is provided.");
      }
      
      channel = channelFuture.sync().channel();
      
      return new TcpOutboundHandler();
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  }
  
  private class TcpDataHandler extends SimpleChannelInboundHandler<E> {
    
    private List<IpcDataHandler<E>> handlerList = new LinkedList<>();
    
    void addHandler(IpcDataHandler<E> handler) {
      handlerList.add(handler);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, E msg) {
      System.out.println("???" + msg);
      for (IpcDataHandler<E> handler : handlerList) {
        handler.onData(msg);
      }
    }
    
    // TODO: close
  }
  
  private class TcpOutboundHandler extends IpcChannel<E> {
  
    @Override
    public void write(E object) {
      channel.writeAndFlush(object);
    }
    
    @Override
    public void refresh() {
      channel.read();
    }
    
    @Override
    public void close() throws IOException {
      try {
        channel.close().sync();
    
        log.info("TCP connection destroyed.");
      } catch (InterruptedException e) {
        throw new IOException(e);
      } finally {
        if (workerGroup != null) {
          workerGroup.shutdownGracefully();
        }
        bossGroup.shutdownGracefully();
      }
    }
  }
}
