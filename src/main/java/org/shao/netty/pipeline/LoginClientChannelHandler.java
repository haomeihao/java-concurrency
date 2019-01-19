package org.shao.netty.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.shao.netty.chat.Session;
import org.shao.netty.chat.SessionUtil;
import org.shao.netty.protocol.ChatRequestPacket;
import org.shao.netty.protocol.LoginRequestPacket;
import org.shao.netty.protocol.LoginResponsePacket;
import org.shao.netty.protocol.LoginUtil;

import java.util.Date;

/**
 * Created by hmh on 2019/1/19.
 */
public class LoginClientChannelHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
//        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("flash");
        loginRequestPacket.setPassword("pwd");

        // 写数据 无需编码
//        ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.getSuccess()) {
            System.out.println(new Date() + ": " + ctx.channel().toString() + ": 客户端登录成功");
            System.out.println(loginResponsePacket.getUserId() + ": " + loginResponsePacket.getUserName());
            LoginUtil.markAsLogin(ctx.channel());

            ChatRequestPacket chatRequestPacket = new ChatRequestPacket();
            chatRequestPacket.setMessage("你好，测试Netty拆包!");
            for (int i = 0; i < 1; i++) {
//                ctx.channel().writeAndFlush(chatRequestPacket);
            }

            // session中绑定userId->channel
            Session session = new Session(loginResponsePacket.getUserId(), loginResponsePacket.getUserName());
            SessionUtil.bindSession(session, ctx.channel());
        } else {
            System.err.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getMsg());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被关闭!");
        SessionUtil.unbindSession(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被注销!");
    }

}
