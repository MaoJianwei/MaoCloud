package com.maojianwei.MaoCloud.MaoCloud.Network.api;

import io.netty.channel.Channel;
import com.maojianwei.MaoCloud.MaoCloud.Network.base.MaoProtocolNode;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;

import java.net.InetAddress;
import java.util.Set;

/**
 * Created by mao on 2016/10/5.
 */
public interface MaoProtocolNetworkController {

    void start(Set<String> unconnectedNodes);
    void stop();

    void clientReportNodeDown(InetAddress nodeAddr);

    MaoProtocolNode createMaoProtocolNode(Channel channel);

    MPFactory getMapProtocolFactory03();
}
