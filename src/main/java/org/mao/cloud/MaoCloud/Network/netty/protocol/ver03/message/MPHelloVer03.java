package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageWriter;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;

/**
 * Created by mao on 2016/9/17.
 */
public class MPHelloVer03 implements MPHello {

    private String idHashValue;
    public String getHashValue(){return idHashValue;}


    private MPHelloVer03(String hashValue){
        idHashValue = hashValue;
    }


    public static final Writer WRITER = new Writer();
    public static class Writer implements MPMessageWriter<MPHelloVer03>{}

    public static final Reader READER = new Reader();
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

    public static Builder builder(){return new Builder();}
    public static class Builder implements MPHello.Builder {

        private String nodeName;
        private String nodePassword;
//        private byte [] idHashValue;

        private Builder(){
            nodeName = null;
            nodePassword = null;
        }

        public Builder setNodeName(String name){
            nodeName = name;
            return this;
        }
        public Builder setNodePassword(String password){
            nodePassword = password;
            return this;
        }

        public MPHelloVer03 build() {
            if (nodeName==null) {
                throw new NullPointerException("Null Pointer: nodeName");
            } else if (nodePassword == null) {
                throw new NullPointerException("Null Pointer: nodePassword");
            }

            return new MPHelloVer03(namePasswdHash());
        }

        private String namePasswdHash(){
            // TODO - achieve this
            return nodeName + nodePassword;
        }
    }

    public MPMessageType getType(){return MPMessageType.HELLO;}
}
