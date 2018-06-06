package com.yd.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Yd on  2018-06-04
 * @description
 **/
public class MessageEncoder extends MessageToByteEncoder<ReceiveDataFormat> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ReceiveDataFormat data, ByteBuf out) throws Exception {
        out.writeInt(data.recevieDataLength);
        out.writeInt(data.receiveDateContent);
    }
}
