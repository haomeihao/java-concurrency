package org.shao.netty.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by hmh on 2019/1/17.
 */
public class PacketCodec {

    public static final int MAGIC_NUMBER = 0x12345678;
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;
    static {
        packetTypeMap = new HashMap<>();
        // TODO 注意不要遗漏
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.CHAT_REQUEST, ChatRequestPacket.class);
        packetTypeMap.put(Command.CHAT_RESPONSE, ChatResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    // 私有构造函数
    private PacketCodec() {
    }

    // 静态的工厂方法
    public static PacketCodec getInstance() {
        return PacketCodec.Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private PacketCodec singleton;

        // JVM保证这个方法绝对只调用一次
        Singleton() {
            singleton = new PacketCodec();
        }

        public PacketCodec getInstance() {
            return singleton;
        }
    }

    public ByteBuf encode(Packet packet) {
        return encode(ByteBufAllocator.DEFAULT, packet);
    }

    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        // 1. 创建 ByteBuf 对象
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        return encode(byteBuf, packet);
    }

    public ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        // 2. 序列化 Java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);
        // 3. 实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);
        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }

    public static void main(String[] args) {
        System.out.println(PacketCodec.getInstance().hashCode());
        System.out.println(PacketCodec.getInstance().hashCode());
        System.out.println(PacketCodec.getInstance().hashCode());
    }

}
