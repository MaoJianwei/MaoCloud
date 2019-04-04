package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.base;

/**
 * Created by mao on 2016/9/17.
 */
public enum MPVersion {

    MP_03(3);

    private final int wireVersion;
    MPVersion(int wireVersion) {
        this.wireVersion = wireVersion;
    }
    public int get() {
        return wireVersion;
    }
}
