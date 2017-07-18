package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.base;

/**
 * Created by mao on 2016/9/18.
 */
public enum MPMessageType {

    LINK_HELLO(0x00),
    LINK_GOODDAY(0x01),
    LINK_ECHO_REQUEST(0x02),
    LINK_ECHO_REPLY(0x03),

    // todo - need more identification
    CLUSTER_INTERNAL_DATA(0x30);

    private final int wireType;

    MPMessageType(int wireType){
        this.wireType = wireType;
    }
    public int get() {
        return wireType;
    }
}
