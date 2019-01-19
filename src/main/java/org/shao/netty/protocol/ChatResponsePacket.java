package org.shao.netty.protocol;

import lombok.Data;

/**
 * Created by hmh on 2019/1/17.
 */
@Data
public class ChatResponsePacket extends Packet {

    /**
     * 谁发送的
     */
    private String fromUserId;
    private String fromUserName;

    private String message;

    public ChatResponsePacket() {
    }

    public ChatResponsePacket(String fromUserId, String fromUserName, String message) {
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.message = message;
    }

    @Override
    protected Byte getCommand() {
        return Command.CHAT_RESPONSE;
    }
}
