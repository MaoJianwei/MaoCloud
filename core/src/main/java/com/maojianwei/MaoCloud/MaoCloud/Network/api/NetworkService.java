package com.maojianwei.MaoCloud.MaoCloud.Network.api;

import com.maojianwei.MaoCloud.MaoCloud.api.MaoCloudData;

import java.util.List;

/**
 * Created by mao on 2016/7/3.
 */
public interface NetworkService {

    void addDataListener(MaoNetworkDataListener dataListener);
    void removeDataListener(MaoNetworkDataListener dataListener);
    
    // TODO: 2016/11/6 nodes is ClusterManager's abstraction
    void sendMessage(List nodes, MaoCloudData mcData);
}
