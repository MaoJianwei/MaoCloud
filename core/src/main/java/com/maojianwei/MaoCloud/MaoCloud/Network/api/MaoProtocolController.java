package com.maojianwei.MaoCloud.MaoCloud.Network.api;

/**
 * Created by mao on 2016/10/5.
 */
public interface MaoProtocolController {

    void addListener(MaoProtocolListener listener);
    void removeListener(MaoProtocolListener listener);
}
