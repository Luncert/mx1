package org.luncert.mx1.core.component;

import io.netty.channel.ChannelHandlerContext;

public class Session {
  
  private ChannelHandlerContext ctx;
  
  public Session(ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }
}
