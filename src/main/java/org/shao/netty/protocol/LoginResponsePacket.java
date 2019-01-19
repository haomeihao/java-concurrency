package org.shao.netty.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * Created by hmh on 2019/1/17.
 */
@Data
@ToString
public class LoginResponsePacket extends Packet {

    private Boolean success;

    private String msg;

    private String userId;
    private String userName;

    @Override
    protected Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
