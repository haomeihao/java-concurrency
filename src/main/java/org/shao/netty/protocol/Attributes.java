package org.shao.netty.protocol;

import io.netty.util.AttributeKey;

/**
 * Created by hmh on 2019/1/17.
 */
public interface Attributes {

    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

}
