package org.shao.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by hmh on 2019/1/15.
 */
public class NettyClient {

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                    }
                });

        /*ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 1000).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Connect Success");
            } else {
                System.out.println("Connect failed");
                // 重新连接
            }
        });*/
        ChannelFuture channelFuture = connect(bootstrap, "127.0.0.1", 1000, MAX_RETRY);

        Channel channel = channelFuture.channel();
        while (true) {
            channel.writeAndFlush(new Date() + ": hello world!");
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*bootstrap.connect("juejin.im", 80).addListeners(future -> {
           if (future.isSuccess()) {
               System.out.println("connect success");
           } else {
               System.out.println("connect failed");
           }
        });*/


    }

    private static ChannelFuture connect(Bootstrap bootstrap, String host, int port) {
        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Connect success");
            } else {
                System.out.println("Connect failed");
                connect(bootstrap, host, port);
            }
        });
        return channelFuture;
    }

    private static ChannelFuture connect(Bootstrap bootstrap, String host, int port, int retry) {
        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Connect success");
            } else if (retry == 0) {
                System.err.println("The number of retries has run out, abandon connection");
            } else {
                // 第几次重试
                int order = (MAX_RETRY - retry)+1;
                // 本次重试的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": Connect failed, the " + order + "th retry");

//                connect(bootstrap, host, port);
                bootstrap.config().group().schedule(() ->
                   connect(bootstrap, host, port, retry-1), delay, TimeUnit.SECONDS
                );
            }
        });
        return channelFuture;
    }


}
