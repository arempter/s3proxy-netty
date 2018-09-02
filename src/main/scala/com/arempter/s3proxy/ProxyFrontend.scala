package com.arempter.s3proxy

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel._

class ProxyFrontend(host: String, port: Int) extends SimpleChannelInboundHandler[Object] {

  var outboundCh: Channel = _

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    val inboundCh = ctx.channel()

    val b = new Bootstrap()
      .group(inboundCh.eventLoop())
      .channel(ctx.channel().getClass)
      .handler(new ProxyBackend(inboundCh))
      .option[java.lang.Boolean](ChannelOption.AUTO_READ, false)

    val f = b.connect(host, port)
    outboundCh = f.channel()

    f.addListener(new ChannelFutureListener {
      override def operationComplete(future: ChannelFuture): Unit = {
        if (future.isSuccess) {
          inboundCh.read()
        } else {
          inboundCh.close()
        }
      }
    })
  }

  override def channelRead0(ctx: ChannelHandlerContext, msg: Object): Unit = {
    if (outboundCh.isActive) {
      outboundCh.writeAndFlush(msg).addListener(new ChannelFutureListener {
        override def operationComplete(future: ChannelFuture): Unit = {
          if (future.isSuccess) {
            ctx.channel().read()
          } else {
            future.channel().close()
          }
        }
      })
    }
  }

  override def channelInactive(ctx: ChannelHandlerContext): Unit = {
    if (outboundCh != null) {
      closeOnFlush(outboundCh)
    }
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
    closeOnFlush(ctx.channel())
  }

  def closeOnFlush(ch: Channel) = {
    if (ch.isActive) {
      ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
    }
  }

}
