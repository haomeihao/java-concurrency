package org.shao.netty.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.shao.netty.chat.Session;
import org.shao.netty.chat.SessionUtil;
import org.shao.netty.protocol.LoginRequestPacket;
import org.shao.netty.protocol.LoginResponsePacket;
import org.shao.netty.protocol.LoginUtil;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hmh on 2019/1/19.
 */
public class LoginServerChannelHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setVersion(loginRequestPacket.getVersion());
        // 登录校验
        if (valid(loginRequestPacket)) {
            // 校验成功
            System.out.println(new Date() +": "+ ctx.channel().toString() + ": 登录成功!");
            LoginUtil.markAsLogin(ctx.channel());

            String userId = UUID.randomUUID().toString().replace("-","");
            loginResponsePacket.setUserId(userId);
            loginResponsePacket.setUserName(loginRequestPacket.getUsername());
            loginResponsePacket.setSuccess(true);

            // session中绑定userId->channel
            Session session = new Session(userId, loginRequestPacket.getUsername());
            SessionUtil.bindSession(session, ctx.channel());
        } else {
            // 校验失败
            System.out.println(new Date() + ": 登录失败!");
            loginResponsePacket.setSuccess(false);
            loginResponsePacket.setMsg("账号密码校验失败");
        }
        // 写数据 无需编码
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接被关闭!");
        SessionUtil.unbindSession(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接被注销!");
    }

}
