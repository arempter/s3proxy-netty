package com.arempter.s3proxy

import com.typesafe.config.ConfigFactory
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.{ LogLevel, LoggingHandler }

object Proxy extends App {

  val conf = ConfigFactory.load()
  val localPort = conf.getConfig("proxy.server").getInt("port")
  val s3Host = conf.getConfig("s3.server").getString("host")
  val s3Port = conf.getConfig("s3.server").getInt("port")

  val bossGroup = new NioEventLoopGroup(4)
  val workerGroup = new NioEventLoopGroup()

  try {
    new ServerBootstrap()
      .group(bossGroup, workerGroup)
      .channel(classOf[NioServerSocketChannel])
      .handler(new LoggingHandler(LogLevel.INFO))
      .childHandler(new ProxyInitializer(s3Host, s3Port))
      .childOption[java.lang.Boolean](ChannelOption.AUTO_READ, false)
      .bind(localPort).sync().channel().closeFuture().sync()

  } finally {
    bossGroup.shutdownGracefully()
    workerGroup.shutdownGracefully()
  }

}
