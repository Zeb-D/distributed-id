package com.yd.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    public final int BASE_LENGTH = 4 + 4;

    public MessageDecoder(int frameLength) {
        super(frameLength, 0, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = null;
        try {
            buf = (ByteBuf) super.decode(ctx, in);
            if (buf == null) {
                return null;
            }
            int length = buf.readInt();
            int content = buf.readInt();
            return new ReceiveDataFormat(length, content);
        } catch (Exception e) {
            ctx.channel().close();
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
        return null;
    }
}  


