package org.shao.netty.protocol;

/**
 * Created by hmh on 2019/1/17.
 */
public interface Command {

    byte LOGIN_REQUEST = 1;

    byte LOGIN_RESPONSE = 2;

    byte CHAT_REQUEST = 3;

    byte CHAT_RESPONSE = 4;

}
