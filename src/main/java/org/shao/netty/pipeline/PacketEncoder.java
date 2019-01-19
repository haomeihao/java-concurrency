package org.shao.netty.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.shao.netty.protocol.Packet;
import org.shao.netty.protocol.PacketCodec;

/**
 * Created by hmh on 2019/1/19.
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf) throws Exception {
        PacketCodec.getInstance().encode(byteBuf, packet);
    }
}
