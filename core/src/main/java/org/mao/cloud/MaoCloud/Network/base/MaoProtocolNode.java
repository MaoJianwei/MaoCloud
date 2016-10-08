package org.mao.cloud.MaoCloud.Network.base;

import org.mao.cloud.MaoCloud.Network.api.MaoProtocolAgent;

/**
 * Created by mao on 2016/10/5.
 */
public class MaoProtocolNode {

    private String address;
    private MaoProtocolAgent agent;

    public  MaoProtocolNode(String address, MaoProtocolAgent agent){
        this.address = address;
        this.agent = agent;
    }

    public String getAddress(){
        return address;
    }




    public boolean announceConnected(){
        return agent.addConnectedNode(this);
    }

}
