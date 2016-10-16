package org.mao.cloud.cli.base;

/**
 * Created by mao on 2016/10/16.
 */
public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException(String errMsg){
        super(errMsg);
    }
}
