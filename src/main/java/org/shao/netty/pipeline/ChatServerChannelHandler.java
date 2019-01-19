package org.shao.netty.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.shao.netty.chat.Session;
import org.shao.netty.chat.SessionUtil;
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

        // 1.拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 2.通过消息发送方的会话信息构造要发送的消息
//        ChatResponsePacket chatResponsePacket = new ChatResponsePacket();
//        chatResponsePacket.setMessage("服务端回复【" + chatRequestPacket.getMessage() + "】");

        ChatResponsePacket chatResponsePacket = new ChatResponsePacket();
        chatResponsePacket.setMessage(chatRequestPacket.getMessage());
        chatResponsePacket.setFromUserId(session.getUserId());
        chatResponsePacket.setFromUserName(session.getUserName());

        // 3.拿到消息接收方的 channel
        Channel toUserChannel = SessionUtil.getChannel(chatRequestPacket.getToUserId());

        // 4.将消息发送给消息接收方
        // 写数据 无需编码
//        ctx.channel().writeAndFlush(chatResponsePacket);
        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(chatResponsePacket);
        } else {
            System.err.println("[" + chatRequestPacket.getToUserId() + "] 不在线，发送失败!");
        }

    }
}
