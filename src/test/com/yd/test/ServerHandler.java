package com.yd.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Object>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		ReceiveDataFormat receiveDataFormat = (ReceiveDataFormat)msg;

		System.out.println("ServerReceived：服务端获取信息：" + receiveDataFormat.receiveDateContent);
		ctx.writeAndFlush("Server to Clent：：" + receiveDataFormat.receiveDateContent);
	}
}
