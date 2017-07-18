package com.maojianwei.MaoCloud.MaoCloud.Network.api;

import com.maojianwei.MaoCloud.MaoCloud.api.MaoCloudData;

/**
 * Created by mao on 2016/11/6.
 */
public interface MaoNetworkDataListener {

    void processData(MaoCloudData mcData);
}
