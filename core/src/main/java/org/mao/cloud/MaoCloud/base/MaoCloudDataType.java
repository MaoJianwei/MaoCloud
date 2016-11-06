package org.mao.cloud.MaoCloud.base;

/**
 * Created by mao on 2016/11/6.
 */
public enum MaoCloudDataType {
    LINK_CONTROL(0),
    MONITOR(1),
    CLUSTER(2),
    INTERNAL_SERVICE(3),
    EXTERNAL_SERVICE(4),
    ANNOUNCE(5);

    int mainType;

    MaoCloudDataType(int mainType) {
        this.mainType = mainType;
    }
}
