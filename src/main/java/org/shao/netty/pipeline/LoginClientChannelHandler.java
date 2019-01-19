package org.shao.netty.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.shao.netty.protocol.ChatRequestPacket;
import org.shao.netty.protocol.LoginRequestPacket;
import org.shao.netty.protocol.LoginResponsePacket;
import org.shao.netty.protocol.LoginUtil;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hmh on 2019/1/19.
 */
public class LoginClientChannelHandler extends SimpleChannelInboundHandler<LoginResponsePacket>{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        // 写数据 无需编码
        ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.getSuccess()) {
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + ": 客户端登录成功");

            ChatRequestPacket chatRequestPacket = new ChatRequestPacket();
            chatRequestPacket.setMessage("你好，测试Netty拆包!");
            for (int i = 0; i < 1000; i++) {
                ctx.channel().writeAndFlush(chatRequestPacket);
            }
        } else {
            System.err.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getMsg());
        }
    }
}