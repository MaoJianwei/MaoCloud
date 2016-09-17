package org.mao.cloud.MaoCloud.Network.netty.protocol.api.base;

/**
 * Created by mao on 2016/9/17.
 */
public abstract class MPParseError extends Exception {
    private static final long serialVersionUID = 1L;

    public MPParseError(String errorInfo){
        super(errorInfo);
    }
}
