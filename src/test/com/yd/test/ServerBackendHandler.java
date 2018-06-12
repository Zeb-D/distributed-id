package com.yd.test;

import io.netty.channel.*;

@SuppressWarnings("deprecation")
public class ServerBackendHandler extends ChannelInboundHandlerAdapter {

    private final Channel inboundChannel;

    public ServerBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    //当和目标服务器的通道连接建立时
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    //    System.out.println(">>>>>>>>>>ACTIVE>>>>>>>>>>");
    }


    /**
     * msg是从目标服务器返回的消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

    	ReceiveDataFormat receiveDataFormat = (ReceiveDataFormat)msg;
		String strMsg = receiveDataFormat.getStrMsgLength() + receiveDataFormat.receiveDateContent;
        System.out.println("Backend server returns message:" + ctx.channel().remoteAddress() + "\n" + strMsg + "\n");
        /**
         * 接收目标服务器发送来的数据并打印
         * 然后把数据写入代理服务器和客户端的通道里
         */
        //通过inboundChannel向客户端写入数据
  //      String resDataToClient = msg.toString();
        inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
          //          inboundChannel.close();
                } else {
                	System.out.println("error");
                    future.channel().close();
                }
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
  //      System.out.println(">>>>>>>>>>IN-ACTIVE>>>>>>>>>>");
          ServerFrontHandler.closeOnFlush(inboundChannel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ServerFrontHandler.closeOnFlush(ctx.channel());
    }
}
