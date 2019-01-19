package org.shao.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.shao.netty.Constant;
import org.shao.netty.pipeline.ChatClientChannelHandler;
import org.shao.netty.pipeline.LoginClientChannelHandler;
import org.shao.netty.pipeline.PacketDecoder;
import org.shao.netty.pipeline.PacketEncoder;
import org.shao.netty.protocol.ChatRequestPacket;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by hmh on 2019/1/17.
 */
public class NettyClientExample {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                // 表示连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 表示是否开启 TCP 底层心跳机制，true 为开启
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 表示是否开始 Nagle 算法，true 表示关闭，false 表示开启，通俗地说，
                // 如果要求高实时性，有数据发送时就马上发送，就设置为 true 关闭，
                // 如果需要减少发送次数减少网络交互，就设置为 false 开启
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
//                        socketChannel.pipeline().addLast(new ClientHandler());

//                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
//                        socketChannel.pipeline().addLast(new Spliter());
//                        socketChannel.pipeline().addLast(new LifeCyCleTestHandler());
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginClientChannelHandler());
                        socketChannel.pipeline().addLast(new ChatClientChannelHandler());
                        socketChannel.pipeline().addLast(new PacketEncoder());

//                        socketChannel.pipeline().addLast(new SecondClientHandler());
                    }
                });

        connect(bootstrap, Constant.HOST, Constant.PORT, Constant.MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("Connect success");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("The number of retries has run out, abandon connection");
            } else {
                // 第几次重试
                int order = (Constant.MAX_RETRY - retry) + 1;
                // 本次重试的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": Connect failed, the " + order + "th retry");

                bootstrap.config().group().schedule(() ->
                        connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS
                );
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
//                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();

                    ChatRequestPacket chatRequestPacket = new ChatRequestPacket();
                    chatRequestPacket.setMessage(line);
//                    ByteBuf byteBuf = PacketCodec.getInstance().encode(channel.alloc(), chatRequestPacket);
//                    channel.writeAndFlush(byteBuf);

                    channel.writeAndFlush(chatRequestPacket);
//                }
            }
        }).start();
    }

}
