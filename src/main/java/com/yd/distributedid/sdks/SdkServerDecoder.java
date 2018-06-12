package com.yd.distributedid.sdks;

import com.yd.distributedid.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yd
 */
public class SdkServerDecoder extends FixedLengthFrameDecoder {
    private static final Logger logger = LoggerFactory.getLogger(SdkServerDecoder.class);

    public SdkServerDecoder(int frameLength) {
        super(frameLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = null;
        try {
            buf = (ByteBuf) super.decode(ctx, in);
            if (buf == null) {
                return null;
            }
            return new SdkProto(buf.readInt(), buf.readLong(), buf.readLong());
        } catch (Exception e) {
            logger.error("decode exception, " + NettyUtil.parseRemoteAddr(ctx.channel()), e);
            NettyUtil.closeChannel(ctx.channel());
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
        return null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        logger.error("SdkServerDecoder channel [{}] error and will be closed", NettyUtil.parseRemoteAddr(channel), cause);
        NettyUtil.closeChannel(channel);
    }
}
