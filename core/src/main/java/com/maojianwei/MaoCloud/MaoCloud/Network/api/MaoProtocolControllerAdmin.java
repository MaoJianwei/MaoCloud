package com.maojianwei.MaoCloud.MaoCloud.Network.api;

import com.maojianwei.MaoCloud.MaoCloud.Network.base.MaoProtocolNode;

import java.util.List;

/**
 * Created by mao on 2016/10/16.
 */
public interface MaoProtocolControllerAdmin {

    List<String> getAllUnConnectedNodes();
    List<MaoProtocolNode> getAllConnectedNodes();
}
