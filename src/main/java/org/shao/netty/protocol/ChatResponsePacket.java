package org.shao.netty.protocol;

import lombok.Data;

/**
 * Created by hmh on 2019/1/17.
 */
@Data
public class ChatResponsePacket extends Packet {

    private String message;

    @Override
    protected Byte getCommand() {
        return Command.CHAT_RESPONSE;
    }
}
