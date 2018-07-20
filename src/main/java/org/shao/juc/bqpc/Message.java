package org.shao.juc.bqpc;

import lombok.Getter;

/**
 * 消息
 * Created by hmh on 2018/7/20.
 * @author hmh
 */
@Getter
public class Message {

    private String message;

    public Message(String message) {
        this.message = message;
    }
}
