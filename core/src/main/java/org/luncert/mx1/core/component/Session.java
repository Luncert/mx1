package org.luncert.mx1.core.component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

public class Session {
  
  private ChannelHandlerContext ctx;
  
  public Session(ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }
  
  public SocketAddress getRemoteAddress() {
    return ctx.channel().remoteAddress();
  }
  
  public Channel getChannel() {
    return ctx.channel();
  }
}
