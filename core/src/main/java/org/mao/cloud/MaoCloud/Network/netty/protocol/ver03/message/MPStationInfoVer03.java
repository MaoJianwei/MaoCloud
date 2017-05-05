package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPStationInfo;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;

/**
 * Created by mao on 2017/5/5.
 */
public class MPStationInfoVer03 implements MPStationInfo {

//    private byte [] idHashValue;
//    public byte [] getHashValue(){
//        return idHashValue;
//    }
    private MPStationInfoVer03(){

    }

    //TODO - should be updated - considered CheckSum
    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPStationInfo.Reader{

//        private static final int SHA256_LENGTH = 32; //bytes

        @Override
        public MPStationInfo readFrom(ByteBuf msg) throws MPParseError{

//            int dataLength = msg.readInt();
//            if(dataLength < SHA256_LENGTH){
//                throw new MPErrorDataLength(dataLength, SHA256_LENGTH);
//            }
//
//            byte [] idHashValue = new byte [SHA256_LENGTH];
//            msg.readBytes(idHashValue);
            return new MPStationInfoVer03();
        }
    }

    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPStationInfo.Writer{

        MPStationInfoVer03 msg;
        ByteBuf data;

        private Writer(MPStationInfoVer03 msg){
            this.msg = msg;
        }

        @Override
        public void writeVersionTo(ByteBuf out){
            out.writeByte(msg.getVersion().get());
        }

        @Override
        public void writeTypeTo(ByteBuf out){
            out.writeByte(msg.getType().get());
        }

        @Override
        public int prepareData(){
            data = PooledByteBufAllocator.DEFAULT.heapBuffer();
//            data.writeBytes(msg.getHashValue());
            return data.readableBytes();
        }

        @Override
        public void writeDataTo(ByteBuf out){
            out.writeBytes(data);
            data.release();
            data = null;
        }
    }

    public Builder builder(){
        return new Builder();
    }
    public static class Builder implements MPStationInfo.Builder {

//        private String nodeName;
//        private String nodePassword;

        public Builder(){
//            nodeName = null;
//            nodePassword = null;
        }

//        public Builder setNodeName(String name){
//            nodeName = name;
//            return this;
//        }
//
//        public Builder setNodePassword(String password){
//            nodePassword = password;
//            return this;
//        }

        public MPStationInfoVer03 build() {
//            if (nodeName==null) {
//                throw new NullPointerException("Null Pointer: nodeName");
//            } else if (nodePassword == null) {
//                throw new NullPointerException("Null Pointer: nodePassword");
//            }

            try {
                return new MPStationInfoVer03();
            } catch(Exception e) {
                return null;
            }
        }

//        private byte[] namePasswordHash() throws NoSuchAlgorithmException{
//            return MessageDigest
//                    .getInstance("SHA-256")
//                    .digest((nodeName + nodePassword).getBytes());
//        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.NOTIFICATION_STATION_INFO;
    }
}
