package com.arempter.s3proxy

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.logging.{ LogLevel, LoggingHandler }

class ProxyInitializer(host: String, port: Int) extends ChannelInitializer[SocketChannel] {

  override def initChannel(ch: SocketChannel): Unit = {
    ch.pipeline()
      .addLast(new HttpServerCodec())
      .addLast(
        new LoggingHandler(LogLevel.INFO),
        new ProxyFrontend(host, port)
      )
  }

}
