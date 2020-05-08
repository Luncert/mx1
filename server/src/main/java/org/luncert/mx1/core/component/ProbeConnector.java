package org.luncert.mx1.core.component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.luncert.mx1.core.util.MarshallingCodeCFactory;
import org.luncert.mx1.probe.commons.data.NetURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Scope("singleton")
//@Component
public final class ProbeConnector {
  
  private static final NetURL DEFAULT_BINDING = new NetURL("tcp://localhost:43211");
  
  private EventLoopGroup bossGroup = new NioEventLoopGroup();
  
  private EventLoopGroup workerGroup = new NioEventLoopGroup();
  
  private Map<ChannelHandlerContext, Session> sessionCache = new ConcurrentHashMap<>();
  
  @Autowired
  private ProbeDataHandler probeDataHandler;
  
  @Value("${mx1.binding}")
  private String binding;
  
  @PostConstruct
  public void start() throws InterruptedException {
    NetURL url;
    if (binding == null) {
      url = DEFAULT_BINDING;
      log.info("Starting ProbeConnector on default binding {}.", url);
    } else {
      url = new NetURL(binding);
      log.info("Starting ProbeConnector on {}.", url);
    }
    
    ServerBootstrap bootstrap = new ServerBootstrap()
        .group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG,128)
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) {
            ch.pipeline()
                .addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) // outbound handler
                .addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) // inbound handler
                .addLast(new TcpDataHandler());
          }
        });
    
    ChannelFuture channelFuture = bootstrap.bind(
        new InetSocketAddress(url.getHost(), url.getPort()));
  
    // FIXME: we can get a channel from the channelFuture,
    //  if we write something through this channel, is that a broadcasting?
    channelFuture.sync();
  
    if (channelFuture.isSuccess()) {
      log.info("ProbeConnector is up, data handler is {}.", probeDataHandler);
    }
  }
  
  private class TcpDataHandler extends ChannelInboundHandlerAdapter {
  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      sessionCache.computeIfAbsent(ctx, Session::new);
      ctx.fireChannelActive();
    }
  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      sessionCache.remove(ctx);
      ctx.fireChannelInactive();
    }
  
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
      Session session = sessionCache.get(ctx);
      probeDataHandler.handle(session, msg);
      ctx.fireChannelRead(msg);
    }
  }
  
  @PreDestroy
  public void stop() {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
    log.info("ProbeConnector stopped.");
  }
}
