package org.mao.cloud.MaoCloud.Network.api;

import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;

/**
 * Created by mao on 2016/11/6.
 */
public interface MaoProtocolListener {

    void nodeConnected(MaoProtocolNode node);
    void nodeUnconnected(MaoProtocolNode node);

    void processMessage(MPMessage msg);
}
