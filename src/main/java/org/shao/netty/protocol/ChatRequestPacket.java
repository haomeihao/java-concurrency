package org.shao.netty.protocol;

import lombok.Data;

/**
 * Created by hmh on 2019/1/17.
 */
@Data
public class ChatRequestPacket extends Packet {

    /**
     * 发送给谁
     */
    private String toUserId;
    private String toUserName;

    private String message;

    public ChatRequestPacket() {
    }

    public ChatRequestPacket(String toUserId, String message) {
        this.toUserId = toUserId;
        this.message = message;
    }

    public ChatRequestPacket(String toUserId, String toUserName, String message) {
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.message = message;
    }

    @Override
    protected Byte getCommand() {
        return Command.CHAT_REQUEST;
    }

}
