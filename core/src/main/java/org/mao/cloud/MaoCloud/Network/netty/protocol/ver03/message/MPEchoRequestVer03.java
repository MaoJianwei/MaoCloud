package org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.message;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPMessageType;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;

/**
 * Created by mao on 2016/9/17.
 */
public class MPEchoRequestVer03 implements MPEchoRequest {

    private static final int ECHO_REQUEST_LENGTH = 0;

    private MPEchoRequestVer03(){
        ;
    }

    private static final Reader READER = new Reader();
    public static Reader reader(){
        return READER;
    }
    public static class Reader implements MPEchoRequest.Reader{
        public MPEchoRequest readFrom(ByteBuf msg) throws MPParseError{
            return new MPEchoRequestVer03();
        }
    }

    public Writer writer(){
        return new Writer(this);
    }
    static class Writer implements MPEchoRequest.Writer{

        MPEchoRequestVer03 msg;
        private Writer(MPEchoRequestVer03 msg){
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
            return ECHO_REQUEST_LENGTH;
        }

        @Override
        public void writeDataTo(ByteBuf out){
            ;
        }
    }

    public Builder builder(){
        return new Builder();
    }
    public static class Builder implements MPEchoRequest.Builder {

        public Builder(){
        }

        public MPEchoRequestVer03 build() {
            return new MPEchoRequestVer03();
        }
    }


    public MPVersion getVersion(){
        return MPVersion.MP_03;
    }
    public MPMessageType getType(){
        return MPMessageType.LINK_ECHO_REQUEST;
    }
}
