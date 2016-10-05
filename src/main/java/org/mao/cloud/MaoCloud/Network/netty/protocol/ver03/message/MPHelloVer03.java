package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;

/**
 * Created by mao on 2016/9/17.
 */
public class MPHelloVer03 implements MPHello {

    public static final Reader READER = new Reader();

    public MPMessageType getType(){return MPMessageType.HELLO;}


    private byte [] idHashValue;

    private MPHelloVer03(byte [] idHashValue){
        this.idHashValue = idHashValue;
    }


    public static class Reader implements MPMessageReader<MPHello>{

        private static final int SHA256_LENGTH = 32;//bytes
        public MPHello readFrom(ByteBuf msg) throws MPParseError{

            int dataLength = msg.readInt();
            if(dataLength != SHA256_LENGTH){
                throw new MPErrorDataLength(dataLength, SHA256_LENGTH);
            }

            byte [] idHashValue = new byte [SHA256_LENGTH];
            msg.readBytes(idHashValue);
            return new MPHelloVer03(idHashValue);
        }
    }

    public class Builder implements MPHello.Builder {

        private byte [] idHashValue;

        private Builder(){
            idHashValue = null;
        }

        public Builder setHashValue(byte [] hashValue){
            idHashValue = hashValue;
            return this;
        }

        public MPHelloVer03 build() {
            if(idHashValue==null){
                throw new NullPointerException("Null Pointer: idHashValue");
            }
            return new MPHelloVer03(idHashValue);
        }
    }
}
