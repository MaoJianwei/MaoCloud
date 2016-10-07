package org.mao.cloud.MaoCloud.Network.netty.protocol.base;

/**
 * Created by mao on 2016/9/18.
 */
public enum MPMessageType {

    HELLO(0x00),
    GOODDAY(0x01),
    ECHO_REQUEST(0x02),
    ECHO_REPLY(0x03);

    private final int wireType;

    MPMessageType(int wireType){this.wireType = wireType;}
    public int get(){return wireType;}
}