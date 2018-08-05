package com.arempter.s3proxy

import io.netty.buffer.Unpooled
import io.netty.channel._

class ProxyBackend(inboundChannel: Channel) extends ChannelInboundHandlerAdapter {

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    ctx.read()
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Object): Unit = {
    inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener {
      override def operationComplete(future: ChannelFuture): Unit = {
        if (future.isSuccess) {
          ctx.channel().read()
        } else {
          future.channel().close()
        }
      }
    })
  }

  override def channelInactive(ctx: ChannelHandlerContext): Unit = {
    closeOnFlush(inboundChannel)
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
