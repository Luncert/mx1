package org.luncert.mx1.probe.daemon;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import org.luncert.mx1.commons.util.MarshallingCodeCFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;

public class CentralService implements Closeable {
  
  private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  
  @Getter
  private Channel channel;
  
  public void connect(SocketAddress centralAddress, ChannelInboundHandler inboundHandler)
      throws IOException {
    if (centralAddress == null) {
      throw new NullPointerException("central address cannot be null");
    }
    
    Bootstrap bootstrap = new Bootstrap()
        .group(bossGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) {
            ch.pipeline()
                .addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) // outbound handler
                .addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) // inbound handler
                .addLast(inboundHandler);
          }
        });
  
    ChannelFuture channelFuture = bootstrap.connect(centralAddress);
    try {
      channelFuture.sync();
    } catch (InterruptedException e) {
      throw new IOException(e);
    }
    
    channel = channelFuture.channel();
  }
  
  @Override
  public void close() {
    bossGroup.shutdownGracefully();
  }
}
