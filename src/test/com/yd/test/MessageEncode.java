package com.yd.test;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncode extends MessageToByteEncoder<ReceiveDataFormat>{

	@Override
	protected void encode(ChannelHandlerContext arg0, ReceiveDataFormat recvMsg,
			ByteBuf outBuff) throws Exception {

		if (recvMsg == null) {
			 throw new Exception("msg is null");  
		}
		
		String strData = recvMsg.receiveDateContent;
		String strLength = recvMsg.getStrMsgLength();
		
		byte[] byteData   = strData.getBytes(Charset.forName("utf-8"));
		byte[] byteLength = strLength.getBytes(Charset.forName("utf-8"));
		
		outBuff.writeBytes(byteLength);
		outBuff.writeBytes(byteData);
	}

}
