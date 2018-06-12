package com.yd.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MessageDecoder extends LengthFieldBasedFrameDecoder { //ByteToMessageDecoder{ 

    //判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是 byte+byte = 1+1 = 2
    private static final int HEADER_SIZE = 2;

    private int length;
    private String body;

    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                          int lengthAdjustment, int initialBytesToStrip, boolean failFast) {

        super(maxFrameLength, lengthFieldOffset, lengthFieldLength,
                lengthAdjustment, initialBytesToStrip, failFast);
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        if (buffer == null) {
            return null;
        }
        if (buffer.readableBytes() < HEADER_SIZE) {
            return null;
        }

        byte lengthA = buffer.readByte();
        byte lengthB = buffer.readByte();
        byte lengthC = buffer.readByte();
        byte lengthD = buffer.readByte();

        String lentCode = Integer.toHexString(lengthA) + Integer.toHexString(lengthB)
                + Integer.toHexString(lengthC) + Integer.toHexString(lengthD);

        String strLength = Util.AsciiStringToString(lentCode);

        length = Util.hexStringToAlgorism(strLength);
        length *= 2;

        if (buffer.readableBytes() < length) {
            return null;
        }

        ByteBuf buf = buffer.readBytes(length);
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        body = new String(req, "UTF-8");

        ReceiveDataFormat customMsg = new ReceiveDataFormat(body.length(), body);
        customMsg.setStrMsgLength(strLength);
        return customMsg;
    }
}  


