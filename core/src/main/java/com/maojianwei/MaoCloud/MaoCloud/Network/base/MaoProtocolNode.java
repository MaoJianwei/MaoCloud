package com.maojianwei.MaoCloud.MaoCloud.Network.base;

import com.maojianwei.MaoCloud.MaoCloud.Network.api.MaoProtocolAgent;
import com.maojianwei.MaoCloud.util.IpUtil;
import io.netty.channel.Channel;

import java.net.InetAddress;

/**
 * Created by mao on 2016/10/5.
 */
public class MaoProtocolNode {

    private MaoProtocolAgent agent;
    private String address;
    private Channel channel;

    public MaoProtocolNode(Channel channel, MaoProtocolAgent agent){
        this.agent = agent;
        this.channel = channel;
        this.address = channel.remoteAddress().toString().split(":")[0].replace("/","");
    }

    public String getChannelInfo(){
        return channel.toString();
    }

    public InetAddress getAddressInet(){
        return IpUtil.strToInet(address);
    }
    public String getAddressStr(){
        return address;
    }


    public boolean announceConnected(){
        return agent.addConnectedNode(this);
    }
    public boolean announceDisConnected(){
        return agent.removeConnectedNode(this);
    }
}
