package org.mao.cloud.MaoCloud.Network.netty.protocol.base;

/**
 * Created by mao on 2016/9/18.
 */
public enum MPVersion {

    MP_03(0);

    private int wireVersion;
    MPVersion(int wireVersion){this.wireVersion = wireVersion;}
    public int getWireVersion(){return wireVersion;}
}
