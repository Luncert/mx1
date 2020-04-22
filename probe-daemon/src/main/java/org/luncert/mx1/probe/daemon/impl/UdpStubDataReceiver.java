package org.luncert.mx1.probe.daemon.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.probe.daemon.IStubDataReceiver;

import java.io.OutputStream;

@Slf4j
public class UdpStubDataReceiver implements IStubDataReceiver {
  
  private static final int DEFAULT_UDP_PORT = 44440;
  
  // handle io
  private EventLoopGroup bossGroup = new NioEventLoopGroup();
  
  private ChannelFuture channelFuture;
  
  @Override
  public void transport(OutputStream outputStream) {
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(bossGroup)
        .channel(NioDatagramChannel.class)
        .option(ChannelOption.SO_BROADCAST, true)
        .handler(new Handler());
    
    try {
      channelFuture = bootstrap.bind(DEFAULT_UDP_PORT).sync();
    } catch (InterruptedException e) {
      log.error("Exception on receive udp data.", e);
    }
  }
  
  private class Handler extends SimpleChannelInboundHandler<DatagramPacket> {
  
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
    
    }
  }
  
  // mask not an issue master
  @Override
  public void destroy() {
    if (channelFuture != null) {
      try {
        channelFuture.channel().closeFuture().wait();
      } catch (InterruptedException e) {
        log.error("Exception on release netty resources.", e);
      }
    }
  }
}
