package com.maojianwei.MaoCloud.MaoCloud.Network.api;

import com.maojianwei.MaoCloud.MaoCloud.Network.base.MaoProtocolNode;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;

/**
 * Created by mao on 2016/11/6.
 */
public interface MaoProtocolListener {

    void nodeConnected(MaoProtocolNode node);
    void nodeUnconnected(MaoProtocolNode node);

    void processMessage(MPMessage msg);
}
