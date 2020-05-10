package org.luncert.mx1.core.component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
  
  private ChannelHandlerContext ctx;
  
  private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
  
  public Session(ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }
  
  public SocketAddress getRemoteAddress() {
    return ctx.channel().remoteAddress();
  }
  
  public Channel getChannel() {
    return ctx.channel();
  }
  
  public void set(String key, Object value) {
    sessionStore.put(key, value);
  }
  
  @SuppressWarnings("unchecked")
  public <T> T get(String key) {
    return (T) sessionStore.get(key);
  }
}
