package org.mao.cloud.MaoCloud.Network.intf;

import org.mao.cloud.MaoCloud.Network.base.SubscriberRole;

/**
 * Created by mao on 2016/7/3.
 */
public interface NetworkService {

    boolean subscribe(SubscriberRole role, NetworkSubscriber subscriber);

    boolean unsubscribe(SubscriberRole role, NetworkSubscriber subscriber);


    boolean sendMessage(String nodeName, Object data);

}
