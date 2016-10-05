package org.mao.cloud.MaoCloud.Network.netty.protocol.api.base;

import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;

/**
 * Created by mao on 2016/9/16.
 */
public interface MPMessage {
    MPMessageType getType();


    interface Builder{
        MPMessage build();
    }

}
