package org.mao.cloud.MaoCloud.Foundation.intf;

import org.mao.cloud.MaoCloud.Foundation.base.SubscriberRole;

/**
 * Created by mao on 2016/7/3.
 */
public interface NetworkService {

    boolean subscribe(SubscriberRole role, NetworkSubscriber subscriber);

    boolean unsubscribe(SubscriberRole role, NetworkSubscriber subscriber);


    //getNodeInfo(nodename);
    boolean sendMessage(String nodeName, Object data);

}
