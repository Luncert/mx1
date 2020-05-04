package org.luncert.mx1.probe.ipc.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.ipc.Connector;
import org.luncert.mx1.probe.ipc.IpcChannel;
import org.luncert.mx1.probe.ipc.IpcDataHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class UdpConnector implements Connector {
  
  private static final int DEFAULT_UDP_PORT = 44440;
  
  private int port = DEFAULT_UDP_PORT;
  
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
  
  public UdpConnector handler(IpcDataHandler handler) {
    nettyDataHandler.addHandler(handler);
    return this;
  }
  
  @Override
  public IpcChannel connect() throws IOException {
    try {
      ChannelFuture channelFuture = bootstrap.bind(port).sync();
      channel = channelFuture.channel();
      
      log.info("Udp connector initialized.");
      
      return new IpcOutboundChannel();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private class IpcOutboundChannel implements IpcChannel {
  
    @Override
    public void write(Object object) throws IOException {
      channel.write(object);
    }
    
    @Override
    public void close() throws IOException {
      try {
        channel.closeFuture().await();
        
        log.info("Udp connector destroyed.");
      } catch (InterruptedException e) {
        e.printStackTrace();
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
