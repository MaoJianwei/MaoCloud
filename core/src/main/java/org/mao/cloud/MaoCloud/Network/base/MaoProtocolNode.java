package org.mao.cloud.MaoCloud.Network.base;

import io.netty.channel.Channel;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolAgent;

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

    public String getAddress(){
        return address;
    }




    public boolean announceConnected(){
        return agent.addConnectedNode(this);
    }

}
