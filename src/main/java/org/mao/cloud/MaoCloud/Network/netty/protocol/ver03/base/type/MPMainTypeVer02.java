package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.base.type;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.constant.MPMainType;

/**
 * Created by mao on 2016/9/17.
 */
@Deprecated
public enum MPMainTypeVer02 implements MPMainType {

    LINK_CONTROL(0);

    private final int wireMainType;
    MPMainTypeVer02(int wireMainType){this.wireMainType = wireMainType;}
}
