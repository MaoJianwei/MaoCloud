package org.mao.cloud.test.api;

import org.mao.cloud.MaoCloud.Network.netty.api.MaoCloudProtocol;

/**
 * Created by mao on 2016/7/1.
 */
public class MaoProtocol {

    private String protocolPrefix;
    private short packetLen;
    private boolean isSYN;
    private boolean isFIN;
    private boolean isCMD;
    private boolean isDATA;
    private String dataOrCmd;

    private MaoProtocol(String protocolPrefix,
                        short packetLen,
                        boolean isSYN,
                        boolean isFIN,
                        boolean isCMD,
                        boolean isDATA,
                        String dataOrCmd) {
        this.protocolPrefix = protocolPrefix;
        this.packetLen = packetLen;
        this.isSYN = isSYN;
        this.isFIN = isFIN;
        this.isCMD = isCMD;
        this.isDATA = isDATA;
        this.dataOrCmd = dataOrCmd;
    }

    public boolean checkValid(){
        if(!protocolPrefix.equals("MAO Link"))
            return false;
        if(packetLen < 0)
            return false;
        if(dataOrCmd.length() != packetLen)
            return false;

        if (isDATA && (!isSYN) && (!isFIN) && (!isCMD)){

        } else if (isSYN && (!isFIN) && (!isDATA)){

        } else if (isFIN && (!isSYN) && (!isDATA)){

        } else {
            return false;
        }

        return true;
    }


    public String getProtocolPrefix(){
        return this.protocolPrefix;
    }
    public short getPacketLen(){
        return this.packetLen;
    }
    public boolean getSYN(){
        return this.isSYN;
    }
    public boolean getFIN(){
        return this.isFIN;
    }
    public boolean getCMD(){
        return this.isCMD;
    }
    public boolean getDATA(){
        return this.isDATA;
    }
    public String getDataOrCmd(){
        return this.dataOrCmd;
    }





    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private String protocolPrefix = "";
        private short packetLen = 0;
        private boolean isSYN = false;
        private boolean isFIN = false;
        private boolean isCMD = false;
        private boolean isDATA = false;
        private String dataOrCmd = "";

        protected Builder(){}

        public MaoCloudProtocol build(){
            return new MaoCloudProtocol(protocolPrefix, packetLen, isSYN, isFIN, isCMD, isDATA, dataOrCmd);
        }

        public Builder setProtocolPrefix(String protocolPrefix){
            this.protocolPrefix = protocolPrefix;
            return this;
        }

        public Builder setPacketLen(short packetLen){
            this.packetLen = packetLen;
            return this;
        }

        public Builder setSYN(boolean isSYN){
            this.isSYN = isSYN;
            return this;
        }

        public Builder setFIN(boolean isFIN){
            this.isFIN = isFIN;
            return this;
        }

        public Builder setCMD(boolean isCMD){
            this.isCMD = isCMD;
            return this;
        }

        public Builder setDATA(boolean isDATA){
            this.isDATA = isDATA;
            return this;
        }

        public Builder setDataOrCmd(String dataOrCmd){
            this.dataOrCmd = dataOrCmd;
            return this;
        }
    }
}
