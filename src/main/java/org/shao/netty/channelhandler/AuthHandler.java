package org.shao.netty.channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.shao.netty.protocol.LoginUtil;

/**
 * Created by hmh on 2019/1/19.
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean hasLogin = LoginUtil.hasLogin(ctx.channel());
        if (!hasLogin) {
            System.out.println("无登录验证，强制关闭连接!");
            ctx.channel().close();
        } else {
            // 一行代码实现逻辑的删除
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接被关闭!");

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端连接被注销!");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        boolean hasLogin = LoginUtil.hasLogin(ctx.channel());
        if (hasLogin) {
            System.out.println("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
        }
    }

}
