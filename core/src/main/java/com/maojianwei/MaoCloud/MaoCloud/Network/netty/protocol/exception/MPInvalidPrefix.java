package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.exception;

import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;

/**
 * Created by mao on 2016/9/17.
 */
@Deprecated
public class MPInvalidPrefix extends MPParseError {

    public MPInvalidPrefix(){
        super("Protocol prefix is invalid!");
    }
}
