package org.luncert.mx1.probe.ipc.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.ipc.Connector;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class UdpConnector<E> implements Connector<E> {
  
  private static final int DEFAULT_UDP_PORT = 44440;
  
  private int port = DEFAULT_UDP_PORT;
  
  private int destPort = -1;
  
  private EventLoopGroup bossGroup = new NioEventLoopGroup();
  
  private Bootstrap bootstrap;
  
  private UdpDataHandler nettyDataHandler;
  
  private Channel channel;
  
  public UdpConnector() {
    bootstrap = new Bootstrap();
    nettyDataHandler = new UdpDataHandler();
    bootstrap.group(bossGroup)
        .channel(NioDatagramChannel.class)
        .option(ChannelOption.SO_BROADCAST, true)
        .handler(nettyDataHandler);
  }
  
  public UdpConnector port(int port) {
    this.port = port;
    return this;
  }
  
  public UdpConnector destination(int port) {
    this.destPort = port;
    return this;
  }
  
  public UdpConnector handler(IpcDataHandler handler) {
    nettyDataHandler.addHandler(handler);
    return this;
  }
  
  @Override
  public IpcChannel open() throws IOException {
    if (destPort == -1) {
      throw new IOException("config of destination port is missing");
    }
    
    try {
      channel = bootstrap.bind(port).sync().channel();
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
  
    log.info("Udp connector initialized");
  
    return new IpcOutboundChannel();
  }
  
  @Deprecated
  private class IpcOutboundChannel extends IpcChannel {
    
    // TODO: IpcChannel中完成了序列化，怎么把数据流转换成udp包？
    
    @Override
    public void write(Object object) throws IOException {
      try {
        channel.writeAndFlush(new DatagramPacket(
            Unpooled.copiedBuffer(object.toString(), CharsetUtil.UTF_8),
            new InetSocketAddress("localhost", destPort)
        )).sync();
      } catch (InterruptedException e) {
        throw new IOException(e);
      }
    }
  
    @Override
    public void close() throws IOException {
      try {
        channel.close().sync();
        
        log.info("UDP connection destroyed");
      } catch (InterruptedException e) {
        throw new IOException(e);
      } finally {
        bossGroup.shutdownGracefully();
      }
    }
  }
  
  private class UdpDataHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    
    private List<IpcDataHandler> handlerList = new LinkedList<>();
    
    void addHandler(IpcDataHandler handler) {
      this.handlerList.add(handler);
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
      ByteBuf buf = msg.copy().content();
      String req = buf.toString(Charset.defaultCharset());
      System.out.println(req);
    }
  }
}
