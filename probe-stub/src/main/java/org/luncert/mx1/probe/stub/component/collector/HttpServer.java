package org.luncert.mx1.probe.stub.component.collector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public final class HttpServer {
  
  private static final int MAX_CONTENT_LENGTH = 1024 * 1024; // 1MB
  
  private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  
  private EventLoopGroup workerGroup = new NioEventLoopGroup();
  
  private int port;
  
  private HttpRequestDispatcher dispatcher;
  
  public HttpServer(int port, HttpRequestDispatcher dispatcher) {
    this.port = port;
    this.dispatcher = dispatcher;
  }
  
  public void start() {
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workerGroup)
        .handler(new LoggingHandler(LogLevel.DEBUG))
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>(){
          @Override
          protected void initChannel(SocketChannel ch) {
            ch.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH))
                .addLast(new HttpRequestHandler());
          }
        });
    
    try {
      ChannelFuture future = bootstrap.bind(new InetSocketAddress("localhost", port)).sync();
      assert future.isSuccess();
    } catch (InterruptedException | AssertionError e) {
      log.error("Failed to start HttpServer.", e);
    }
  }
  
  public void close() {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }
  
  private class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
  
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
      HttpResponse response = dispatcher.handle(msg);
      ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
  }
}
