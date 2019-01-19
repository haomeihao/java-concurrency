package org.shao.netty.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.shao.netty.protocol.ChatRequestPacket;
import org.shao.netty.protocol.ChatResponsePacket;

import java.util.Date;

/**
 * Created by hmh on 2019/1/19.
 */
public class ChatServerChannelHandler extends SimpleChannelInboundHandler<ChatRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestPacket chatRequestPacket) throws Exception {
        System.out.println(new Date() + ": 收到客户端消息: " + chatRequestPacket.getMessage());

        ChatResponsePacket chatResponsePacket = new ChatResponsePacket();
        chatResponsePacket.setMessage("服务端回复【" + chatRequestPacket.getMessage() + "】");

        // 写数据 无需编码
        ctx.channel().writeAndFlush(chatResponsePacket);
    }
}
