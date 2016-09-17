package org.mao.cloud.MaoCloud.Network.netty.protocol;

import io.netty.buffer.ByteBuf;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessageReader;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPInvalidPrefix;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPParseError;
import org.mao.cloud.MaoCloud.Network.netty.protocol.exception.MPUnsupportedVersion;
import org.mao.cloud.MaoCloud.Network.netty.protocol.ver03.base.MPFactoryVer03;

/**
 * Created by mao on 2016/9/17.
 */
public final class MPFactories {

    private static final GeneralReader GENERAL_READER = new GeneralReader();

    public static MPMessageReader<MPMessage> getGeneralReader(){
        return GENERAL_READER;
    }

    private static class GeneralReader implements MPMessageReader<MPMessage> {

        private static final String PROTOCOL_PREFIX = "MAOCLOUD";

        public MPMessage readFrom(ByteBuf msg) throws MPParseError{

            if(!checkProtocolValid(msg))
                throw new MPInvalidPrefix();

            MPFactory factory;
            byte version = msg.readByte();
            switch (version){
                case 0:
                    factory = MPFactoryVer03.INSTANCE;
                    break;
                default:
                    throw new MPUnsupportedVersion(version);
            }

            return factory.getReader().readFrom(msg);
        }

        /**
         * Check Protocol Prefix: MAOCLOUD
         * @return
         */
        private boolean checkProtocolValid(ByteBuf msg){
            byte [] prefix = new byte[8];
            msg.readBytes(prefix);
            String prefixStr = new String(prefix);
            return  prefixStr.equals(PROTOCOL_PREFIX) ? true : false;
        }
    }
}
