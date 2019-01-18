package org.shao;

import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;
import org.shao.netty.protocol.*;

/**
 * Created by hmh on 2019/1/17.
 */
public class PacketCodecTest extends JucApplicationTests {

    @Test
    public void encode() {
        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setVersion((byte)1);
        loginRequestPacket.setUserId("123");
        loginRequestPacket.setUsername("admin");
        loginRequestPacket.setPassword("admin123");

        ByteBuf byteBuf = PacketCodec.getInstance().encode(loginRequestPacket);
        Packet decodedPacket = PacketCodec.getInstance().decode(byteBuf);

        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket),
                serializer.serialize(decodedPacket));
    }

}
