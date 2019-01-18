package org.shao.netty.protocol;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * Created by hmh on 2019/1/17.
 */
public class LoginUtil {
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> attribute = channel.attr(Attributes.LOGIN);
        return attribute.get() != null;
    }
}
