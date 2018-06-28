package com.yd.distributedid.http;

import com.yd.distributedid.core.SnowFlake;
import com.yd.distributedid.core.BaseServer;
import com.yd.distributedid.util.GlobalConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * Http服务器，使用Netty中的Http协议栈，
 * 实现中支持多条请求路径，对于不存在的请求路径返回404状态码
 * 如：http://localhost:8099/getTime
 * @author Yd
 */
public class HttpServer extends BaseServer {

    private SnowFlake snowFlake;


    public HttpServer(SnowFlake snowFlake) {
        this.snowFlake = snowFlake;
        this.port = GlobalConfig.HTTP_PORT;
    }

    @Override
    public void init() {
        super.init();
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)//是否启用心跳保活机制,在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
                .option(ChannelOption.TCP_NODELAY, true)//TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                .option(ChannelOption.SO_BACKLOG, 1024)//BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defLoopGroup,
                                new HttpRequestDecoder(),       //请求解码器
                                new HttpObjectAggregator(65536),//将多个消息转换成单一的消息对象
                                new HttpResponseEncoder(),      // 响应编码器
                                new ChunkedWriteHandler(),//目的是支持异步大文件传输（）
                                new HttpServerHandler(snowFlake)//自定义处理器
                        );
                    }
                });

    }

    @Override
    public void start() {
        try {
            cf = b.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) cf.channel().localAddress();
            logger.info("HttpServer start success, port is:{}", addr.getPort());
        } catch (InterruptedException e) {
            logger.error("HttpServer start fail,", e);
        }
    }

}
