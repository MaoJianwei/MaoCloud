package com.maojianwei.MaoCloud.MaoCloud.base.communicate.subtype;

/**
 * Main Type.
 *
 * Created by mao on 2016/11/6.
 */
public enum MaoCloudDataType {

    LINK_CONTROL(0),
    MONITOR(1),
    CLUSTER(2),
    INTERNAL_SERVICE(3),
    EXTERNAL_SERVICE(4),
    ANNOUNCE(5);

    private final int mainType;
    MaoCloudDataType(int mainType) {
        this.mainType = mainType;
    }
    public int get(){
        return mainType;
    }
}
