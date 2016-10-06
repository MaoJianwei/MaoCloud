package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPErrorDataLength;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mao on 2016/9/17.
 */
public class MPHelloVer03 implements MPHello {

    private byte [] idHashValue;
    public byte [] getHashValue(){
        return idHashValue;
    }


    private MPHelloVer03(byte [] idHashValue){
        this.idHashValue = idHashValue;
    }


    //TODO - should be updated - considered CheckSum
    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPHello.Reader{

        private static final int SHA256_LENGTH = 32; //bytes

        @Override
        public MPHello readFrom(ByteBuf msg) throws MPParseError{

            int dataLength = msg.readInt();
            if(dataLength < SHA256_LENGTH){
                throw new MPErrorDataLength(dataLength, SHA256_LENGTH);
            }

            byte [] idHashValue = new byte [SHA256_LENGTH];
            msg.readBytes(idHashValue);
            return new MPHelloVer03(idHashValue);
        }
    }


    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPHello.Writer{

        MPHelloVer03 msg;
        ByteBuf data;

        private Writer(MPHelloVer03 msg){
            this.msg = msg;
        }

        @Override
        public void writeVersion(ByteBuf out){
            out.writeByte(msg.getVersion().get());
        }

        @Override
        public void writeType(ByteBuf out){
            out.writeByte(msg.getType().get());
        }

        @Override
        public int prepareData(){
            data = PooledByteBufAllocator.DEFAULT.heapBuffer();
            data.writeBytes(msg.getHashValue());
            return data.readableBytes();
        }

        @Override
        public void writeData(ByteBuf out){
            out.writeBytes(data);
        }
    }

    public Builder builder(){
        return new Builder();
    }
    public static class Builder implements MPHello.Builder {

        private String nodeName;
        private String nodePassword;

        public Builder(){
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

            try {
                return new MPHelloVer03(namePasswordHash());
            } catch(Exception e) {
                return null;
            }
        }

        private byte[] namePasswordHash() throws NoSuchAlgorithmException{
                return MessageDigest
                        .getInstance("SHA-256")
                        .digest((nodeName + nodePassword).getBytes());
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.HELLO;
    }
}
