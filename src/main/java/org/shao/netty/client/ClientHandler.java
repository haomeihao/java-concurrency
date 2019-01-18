package org.shao.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.shao.netty.protocol.*;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hmh on 2019/1/17.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        // 编码
        ByteBuf byteBuf = PacketCodec.getInstance().encode(ctx.alloc(), loginRequestPacket);

        // 写数据
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodec.getInstance().decode(byteBuf);
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (loginResponsePacket.getSuccess()) {
                LoginUtil.markAsLogin(ctx.channel());
                System.out.println(new Date() + ": 客户端登录成功");
            } else {
                System.err.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getMsg());
            }
        } else if (packet instanceof ChatResponsePacket) {
            ChatResponsePacket chatResponsePacket = (ChatResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + chatResponsePacket.getMessage());
        } else {
            System.err.println(new Date() + ": 响应类型错误");
        }
    }
}
