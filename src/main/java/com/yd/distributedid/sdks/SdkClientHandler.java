package com.yd.distributedid.sdks;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author Yd on  2018-05-30
 * @description
 **/
public class SdkClientHandler extends ChannelInboundHandlerAdapter {

    // 连接成功后，向server发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送SdkProto协议的消息

        SdkProto protocol = new SdkProto(11, 22);

        ctx.writeAndFlush(protocol);
    }

    // 接收server端的消息，并打印出来
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        try {
            // 用于获取客户端发来的数据信息
            SdkProto body = (SdkProto) msg;
            System.out.println("Client接受的服务端的信息 :" + body.toString());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }
}
