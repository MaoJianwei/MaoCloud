package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.exception;

import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;

/**
 * Created by mao on 2016/9/17.
 */
public class MPErrorType extends MPParseError {

    public MPErrorType(int version, int mainType, int subType, boolean isMainType){
        super("Error Message "+ (isMainType ? "Main" : "Sub") +" Type, version:"+ version + " MainType:" + mainType + " SubType:" + subType );
    }

}
