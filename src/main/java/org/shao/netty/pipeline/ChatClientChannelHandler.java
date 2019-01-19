package org.shao.netty.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.shao.netty.protocol.ChatResponsePacket;

/**
 * Created by hmh on 2019/1/19.
 */
public class ChatClientChannelHandler extends SimpleChannelInboundHandler<ChatResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatResponsePacket chatResponsePacket) throws Exception {
//        System.out.println(new Date() + ": 收到服务端的消息: " + chatResponsePacket.getMessage());
        String fromUserId = chatResponsePacket.getFromUserId();
        String fromUserName = chatResponsePacket.getFromUserName();
        System.out.println(fromUserId + ":" + fromUserName + " -> " + chatResponsePacket.getMessage());
    }
}
