package com.yd.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {

    private final int localPort;

    public TestServer(int localPort, String remoteHost, int remotePort) {
        this.localPort = localPort;
    }

    public static void main(String[] args) throws InterruptedException {

        int port;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 6666;
        }
        new TestServer(8080, "127.0.0.1", 6666).start();
    }

    public void start() {

        ServerBootstrap bootStrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            //设置线程池
            bootStrap.group(bossGroup, workerGroup);

            //设置socket工厂
            bootStrap.channel(NioServerSocketChannel.class);

            //设置管道工厂
            bootStrap.childHandler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel ch) throws Exception {

                    ch.pipeline().addLast(new MessageDecoder(Contast.MAX_FRAME_LENGTH, Contast.LENGTH_FIELD_LENGTH, Contast.LENGTH_FIELD_OFFSET, Contast.LENGTH_ADJUSTMENT, Contast.INITIAL_BYTES_TO_STRIP, true));
                    ch.pipeline().addLast(new MessageEncode());
                    ch.pipeline().addLast(new TestServerHandler());
                }
            });

            //设置参数

            //serverScoketchannel的设置  2048等待缓存池大小，同一时间最多连接2048个客户端
            bootStrap.option(ChannelOption.SO_BACKLOG, Contast.MAX_CLINET);
            //socketchannel的设置，维持链接的活跃，清除一些死链接
            bootStrap.option(ChannelOption.SO_KEEPALIVE, true);
            //socketchannel的设置，关闭延迟发送
            bootStrap.option(ChannelOption.TCP_NODELAY, true);

            //绑定端口
            ChannelFuture future = bootStrap.bind(localPort);

            System.out.println("Server start listen at " + localPort + "\n");

            //等待服务端关闭 socket 的 channel
            Channel channel = future.channel();

            channel.closeFuture().sync();

        } catch (InterruptedException e) {


        } finally {
            //释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
