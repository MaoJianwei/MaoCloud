package org.mao.cloud.MaoCloud.Network.impl;

import io.netty.channel.socket.SocketChannel;
import io.netty.util.internal.ConcurrentSet;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.mao.cloud.MaoCloud.Network.base.NodeLink;
import org.mao.cloud.MaoCloud.Network.base.SubscriberRole;
import org.mao.cloud.MaoCloud.Network.api.NetworkService;
import org.mao.cloud.MaoCloud.Network.api.NetworkSubscriber;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mao on 2016/7/3.
 *
 * Beta, functions are moved to {@link MaoProtocolNetworkControllerImpl}.
 */
@Component(immediate = false)
@Service
public class NetworkManager implements NetworkService {



    private ConcurrentHashMap clientMap = new ConcurrentHashMap<String, NodeLink>(); //KEY: NAME
    private ConcurrentHashMap initClientMap = new ConcurrentHashMap<InetSocketAddress, SocketChannel>(); //KEY: InetSocketAddress

    private ConcurrentHashMap<SubscriberRole, ConcurrentSet<NetworkSubscriber>> subscriberMap = new ConcurrentHashMap();

    // -------------------------------






    // -------------------------------



    @Activate
    protected void activate(){

        subscriberMap.put(SubscriberRole.Monitor, new ConcurrentSet<NetworkSubscriber>());
        subscriberMap.put(SubscriberRole.Cluster, new ConcurrentSet<NetworkSubscriber>());
        subscriberMap.put(SubscriberRole.Service, new ConcurrentSet<NetworkSubscriber>());




    }


    @Deactivate
    protected void deactivate(){


    }


    /**
     *
     * @param role
     * @param subscriber
     * @return {@code false} if the subscriber already exists in the set
     */
    public boolean subscribe(SubscriberRole role, NetworkSubscriber subscriber){
        return subscriberMap.get(role).add(subscriber);
    }
    public boolean unsubscribe(SubscriberRole role, NetworkSubscriber subscriber){
        return subscriberMap.get(role).remove(subscriber);
    }
    public boolean sendMessage(String nodeName, Object data){
        return false;
    }


}
