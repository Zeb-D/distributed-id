package com.yd.test;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@SuppressWarnings("deprecation")
public class TestServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ReceiveDataFormat receiveDataFormat = (ReceiveDataFormat) msg;
        String strMsg = receiveDataFormat.getStrMsgLength() + receiveDataFormat.receiveDateContent;

        System.out.println("From Server : IP: " + ctx.channel().remoteAddress().toString() + "\n" + strMsg);

        ctx.channel().writeAndFlush(msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        closeOnFlush(ctx.channel());
    }

    protected void messageReceived(ChannelHandlerContext arg0, Object arg1)
            throws Exception {


    }

}
