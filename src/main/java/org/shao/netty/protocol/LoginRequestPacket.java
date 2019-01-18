package org.shao.netty.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * Created by hmh on 2019/1/17.
 */
@Data
@ToString
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST ;
    }
}
