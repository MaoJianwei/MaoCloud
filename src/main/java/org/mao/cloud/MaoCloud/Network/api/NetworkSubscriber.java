package org.mao.cloud.MaoCloud.Network.api;

import java.util.concurrent.Callable;

/**
 * Created by mao on 2016/7/3.
 */
public interface NetworkSubscriber {

    boolean addMsgTask(Callable task);

}
