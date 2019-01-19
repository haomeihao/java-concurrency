package org.shao.netty.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.shao.netty.protocol.PacketCodec;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by hmh on 2019/1/19.
 */
public class Spliter extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_OFFSET = 7;
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        int readerIndex = in.readerIndex();
        int magicNumber = in.getInt(readerIndex);
        if (PacketCodec.MAGIC_NUMBER != magicNumber) {
            Channel channel = ctx.channel();
            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
            InetSocketAddress localAddress = (InetSocketAddress) channel.localAddress();
            System.out.println(new Date() + ":【非法连接】remote:" + remoteAddress.toString() + ", local:" + localAddress.toString()
                    + " -> " + in.toString(Charset.forName("utf-8")));
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }

}
