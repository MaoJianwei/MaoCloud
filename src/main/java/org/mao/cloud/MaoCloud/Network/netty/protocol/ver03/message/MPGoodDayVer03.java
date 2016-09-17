package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPGoodDay;

/**
 * Created by mao on 2016/9/17.
 */
public class MPGoodDayVer03 implements MPGoodDay {

    public static final Reader READER = new Reader();


    private String cause;

    private MPGoodDayVer03(String cause){
        this.cause = cause;
    }


    public static class Reader implements MPMessageReader<MPGoodDay>{
        public MPGoodDay readFrom(ByteBuf msg){

            int dataLength = msg.readInt();
            String causeStr;
            if(dataLength > 0){
                byte [] cause = new byte [dataLength];
                msg.readBytes(cause);
                causeStr = new String(cause);
            }else{
                causeStr = "";
            }

            return new MPGoodDayVer03(causeStr);
        }
    }
}
