package com.yd.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    public static void main(String[] args) {
        //服务类
        ServerBootstrap bootstrap = new ServerBootstrap();
        final NioEventLoopGroup boss = new NioEventLoopGroup();
        final NioEventLoopGroup worker = new NioEventLoopGroup();

        try {

            //设置线程池
            bootstrap.group(boss, worker);

            //设置socket工厂
            bootstrap.channel(NioServerSocketChannel.class);

            //设置管道工厂
            bootstrap.childHandler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel ch) throws Exception {

                    ch.pipeline().addLast(new MessageDecoder(8));
                    ch.pipeline().addLast(new MessageEncoder());
                    ch.pipeline().addLast(new ServerHandler());
                }
            });

            //设置参数

            //serverScoketchannel的设置  2048等待缓存池大小，同一时间最多连接2048个客户端
            bootstrap.option(ChannelOption.SO_BACKLOG, 2048);
            //socketchannel的设置，维持链接的活跃，清除一些死链接
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            //socketchannel的设置，关闭延迟发送
            bootstrap.option(ChannelOption.TCP_NODELAY, true);

            //绑定端口
            ChannelFuture future = bootstrap.bind(7788);

            System.out.println("服务器开启！");

            //等待服务端关闭 socket 的 channel
            Channel channel = future.channel();

            channel.closeFuture().sync();

        } catch (InterruptedException e) {
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                //释放资源
                boss.shutdownGracefully();
                worker.shutdownGracefully();
                System.exit(0);
            }
        });

    }

}
