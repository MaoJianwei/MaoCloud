package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.base.type;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.constant.MPSubType;

/**
 * Created by mao on 2016/9/17.
 */
@Deprecated
public enum MPLinkControlTypeVer02 implements MPSubType {

    HELLO(0),
    GOODDAY(1),
    ECHO(2);

    private final int wireSubType;
    MPLinkControlTypeVer02(int wireSubType){this.wireSubType = wireSubType;}
}
