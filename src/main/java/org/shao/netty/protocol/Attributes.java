package org.shao.netty.protocol;

import io.netty.util.AttributeKey;
import org.shao.netty.chat.Session;

/**
 * Created by hmh on 2019/1/17.
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");

}
