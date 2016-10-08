package org.mao.cloud.MaoCloud.Network.netty.protocol.exception;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;

/**
 * Created by mao on 2016/9/17.
 */
public class MPUnsupportedVersion extends MPParseError {

    public MPUnsupportedVersion(int version){
        super("Unsupported wire version: " + version);
    }
}
