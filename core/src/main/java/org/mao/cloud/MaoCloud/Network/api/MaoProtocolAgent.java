package org.mao.cloud.MaoCloud.Network.api;

import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * Created by mao on 2016/10/5.
 */
public interface MaoProtocolAgent {
    boolean addConnectedNode(MaoProtocolNode node);
    InetAddress getOneUnConnectedNode();


    Inet4Address getLocalIpv4();
    Inet6Address getLocalIpv6();
}
