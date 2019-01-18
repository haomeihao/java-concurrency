package org.shao.netty.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by hmh on 2019/1/17.
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    @JSONField(serialize = false, deserialize = false)
    private Byte version = 1;

    /**
     * 获取指令
     */
    protected abstract Byte getCommand();

}
