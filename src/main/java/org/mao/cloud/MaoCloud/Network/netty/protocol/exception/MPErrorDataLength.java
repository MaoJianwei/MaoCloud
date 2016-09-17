package org.mao.cloud.MaoCloud.Network.netty.protocol.exception;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;

/**
 * Created by mao on 2016/9/17.
 */
public class MPErrorDataLength extends MPParseError {

    public MPErrorDataLength(int wrongLen, int rightLen){
        super("Error data length:" + wrongLen + " should be:" + rightLen);
    }
}
