package org.shao.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.shao.netty.protocol.*;

import java.util.Date;

/**
 * Created by hmh on 2019/1/17.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodec.getInstance().decode(requestByteBuf);

        // 判断是否是登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            // 登录校验
            if (valid(loginRequestPacket)) {
                LoginUtil.markAsLogin(ctx.channel());
                // 校验成功
                System.out.println(new Date() + ": 登录成功!");
                loginResponsePacket.setSuccess(true);
            } else {
                // 校验失败
                System.out.println(new Date() + ": 登录失败!");
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setMsg("账号密码校验失败");
            }
            // 编码
            ByteBuf responseByteBuf = PacketCodec.getInstance().encode(ctx.alloc(), loginResponsePacket);
            // 写数据
            ctx.channel().writeAndFlush(responseByteBuf);
        } else if (packet instanceof ChatRequestPacket) {
            // 处理消息
            ChatRequestPacket chatRequestPacket = (ChatRequestPacket) packet;
            System.out.println(new Date() + ": 收到客户端消息: " + chatRequestPacket.getMessage());

            ChatResponsePacket chatResponsePacket = new ChatResponsePacket();
            chatResponsePacket.setMessage("服务端回复【" + chatRequestPacket.getMessage() + "】");

            ByteBuf responseByteBuf = PacketCodec.getInstance().encode(ctx.alloc(), chatResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }

}
