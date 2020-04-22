package org.luncert.mx1.probe.daemon;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.Future;

public class NettyEngine {

  private EventLoopGroup bossGroup, workerGroup;
  private ServerBootstrap bootstrap;
  private int port;

  public NettyEngine(int port, ChannelInitializer<SocketChannel> initializer) {
    this.port = port;
    bossGroup = new NioEventLoopGroup();
    workerGroup = new NioEventLoopGroup();
    bootstrap = new ServerBootstrap()
        .group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(initializer);
  }

  /**
   * 启动并阻塞
   */
  public void run_forever() {
    try {
      ChannelFuture f = bootstrap.bind(port).sync();
      // Runtime.getRuntime().addShutdownHook(new Thread(() -> clear()));
      f.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
      clear();
    }
  }

  /**
   * 提交任务到netty主事件循环组执行
   * @param task
   * @return Future
   */
  public Future<?> submit(Runnable task) {
    return bossGroup.submit(task);
  }

  /**
   * 释放netty资源
   * @throws InterruptedException
   */
  private void clear() {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }

}