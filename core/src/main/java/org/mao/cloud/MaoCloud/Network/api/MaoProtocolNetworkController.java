package org.mao.cloud.MaoCloud.Network.api;

import io.netty.channel.Channel;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;

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
