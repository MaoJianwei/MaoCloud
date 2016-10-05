package org.mao.cloud.MaoCloud.Network.netty.protocol.api.message;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;

/**
 * Created by mao on 2016/9/17.
 */
public interface MPHello extends MPMessage {

    interface Builder extends MPMessage.Builder {
        MPHello build();
    }
}
