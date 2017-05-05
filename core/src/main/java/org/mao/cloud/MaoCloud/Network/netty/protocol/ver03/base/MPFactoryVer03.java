package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.base;

import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoReply;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPStationInfo;
import org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message.MPEchoReplyVer03;
import org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message.MPEchoRequestVer03;
import org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message.MPHelloVer03;
import org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message.MPStationInfoVer03;

/**
 * Created by mao on 2016/9/17.
 */
public class MPFactoryVer03 implements MPFactory {

    public static final MPFactoryVer03 INSTANCE = new MPFactoryVer03();

    public MPMessageReader<MPMessage> getReader(){
        return MPMessageVer03.READER;
    }


    @Override
    public MPHello.Builder buildHello(){
        return new MPHelloVer03.Builder();
    }

    @Override
    public MPEchoRequest.Builder buildEchoRequest(){
        return new MPEchoRequestVer03.Builder();
    }

    @Override
    public MPEchoReply.Builder buildEchoReply(){
        return new MPEchoReplyVer03.Builder();
    }

    @Override
    public MPStationInfo.Builder buildStationInfo() {
        return new MPStationInfoVer03.Builder();
    }
}
