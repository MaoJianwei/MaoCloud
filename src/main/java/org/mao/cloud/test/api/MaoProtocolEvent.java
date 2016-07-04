package org.mao.cloud.test.api;

/**
 * Created by mao on 2016/7/2.
 */
public class MaoProtocolEvent {

    public enum Type{
        CMD,
        DATA
    }

    public Type type;
    public String dataOrCmd;
}
