package org.shao.netty.chat;

import lombok.Data;

/**
 * Created by hmh on 2019/1/19.
 */
@Data
public class Session {
    private String userId;
    private String userName;

    public Session() {
    }

    public Session(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
