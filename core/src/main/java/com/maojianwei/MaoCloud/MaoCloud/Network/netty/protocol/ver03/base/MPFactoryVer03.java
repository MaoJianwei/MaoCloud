package com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.ver03.base;

import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPGoodDay;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.ver03.message.MPEchoReplyVer03;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.ver03.message.MPEchoRequestVer03;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.ver03.message.MPGoodDayVer03;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPEchoReply;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import com.maojianwei.MaoCloud.MaoCloud.Network.netty.protocol.ver03.message.MPHelloVer03;

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
    public MPGoodDay.Builder buildGoodDay(){
        return new MPGoodDayVer03.Builder();
    }
}
