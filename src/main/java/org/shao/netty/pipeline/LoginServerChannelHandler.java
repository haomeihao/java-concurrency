package org.shao.netty.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.shao.netty.protocol.LoginRequestPacket;
import org.shao.netty.protocol.LoginResponsePacket;
import org.shao.netty.protocol.LoginUtil;

import java.util.Date;

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
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + ": 登录成功!");
            loginResponsePacket.setSuccess(true);
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
}
