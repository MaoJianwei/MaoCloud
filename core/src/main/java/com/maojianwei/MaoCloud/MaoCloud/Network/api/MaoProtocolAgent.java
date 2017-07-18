package com.maojianwei.MaoCloud.MaoCloud.Network.api;

import com.maojianwei.MaoCloud.MaoCloud.Network.base.MaoProtocolNode;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;

import java.net.Inet4Address;
import java.net.Inet6Address;

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
