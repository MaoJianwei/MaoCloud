package org.mao.cloud.MaoCloud.Network.api;

import org.mao.cloud.MaoCloud.api.MaoCloudData;

/**
 * Created by mao on 2016/11/6.
 */
public interface MaoNetworkDataListener {

    void processData(MaoCloudData mcData);
}
