package org.mao.cloud.MaoCloud.Network.netty.protocol.api.constant;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPConst;

/**
 * Created by mao on 2016/9/17.
 */
@Deprecated
public enum MPVersion implements MPConst{

    VERSION_0_2(0);

    private final int wireVersion;

    MPVersion(int wireVersion){this.wireVersion = wireVersion;}
}
