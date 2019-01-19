package org.shao.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by hmh on 2019/1/17.
 */
public class SecondClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Thread.sleep(1000);
        System.out.println(new Date() + ": (一)客户端写出数据");
        // 1.获取数据
//        ByteBuf byteBuf = getByteBuf(ctx);
        // 2.写数据
//        ctx.channel().writeAndFlush(byteBuf);
        for (int i = 0; i < 1000; i++) {
            ByteBuf byteBuf = getByteBuf(ctx);
            ctx.channel().writeAndFlush(byteBuf);
        }
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        byte[] bytes = "你好，闪电侠!".getBytes(Charset.forName("utf-8"));
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        return buffer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Thread.sleep(1000);
        System.out.println(new Date() + ": (四)客户端读到数据 -> " + byteBuf.toString(Charset.forName("utf-8")));
    }

}
