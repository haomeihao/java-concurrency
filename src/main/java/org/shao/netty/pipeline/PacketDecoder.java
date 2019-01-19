package org.shao.netty.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.shao.netty.protocol.PacketCodec;

import java.util.List;

/**
 * Created by hmh on 2019/1/19.
 */
public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        out.add(PacketCodec.getInstance().decode(byteBuf));
    }
}
