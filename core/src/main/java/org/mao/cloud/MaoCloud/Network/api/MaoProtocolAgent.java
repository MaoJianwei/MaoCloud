package org.mao.cloud.MaoCloud.Network.api;

import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * Created by mao on 2016/10/5.
 */
public interface MaoProtocolAgent {

    //Used by both of server and client roles.
    boolean addConnectedNode(MaoProtocolNode node);
    boolean removeConnectedNode(MaoProtocolNode node);

    void processMessage(MPMessage msg);


    Inet4Address getLocalIpv4();
    Inet6Address getLocalIpv6();
}
