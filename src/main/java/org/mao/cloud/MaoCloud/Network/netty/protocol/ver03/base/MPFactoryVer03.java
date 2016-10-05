package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.base;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;

/**
 * Created by mao on 2016/9/17.
 */
public class MPFactoryVer03 implements MPFactory {

    public static final MPFactoryVer03 INSTANCE = new MPFactoryVer03();

    public MPMessageReader<MPMessage> getReader(){
        return MPMessageVer03.READER;
    }
}
