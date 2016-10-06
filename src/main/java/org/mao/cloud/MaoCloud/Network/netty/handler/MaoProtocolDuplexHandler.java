package org.mao.cloud.MaoCloud.Network.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.impl.MaoProtocolNetworkControllerImpl;
import org.mao.cloud.MaoCloud.Network.netty.protocol.MPFactories;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;

import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.END;
import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.ENDING;
import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.WAIT_HELLO;

/**
 * Created by mao on 2016/9/17.
 */
public class MaoProtocolDuplexHandler extends ChannelDuplexHandler {

    private MaoProtocolNode maoProtocolNode;
    private MaoProtocolNetworkControllerImpl controller;
    private MaoProtocolState state;

    public MaoProtocolDuplexHandler(MaoProtocolNetworkControllerImpl controller){
        this.controller = controller;
        state = MaoProtocolState.INIT;
    }


    private void setState(MaoProtocolState newState){
        this.state = newState;

    }
    enum MaoProtocolState {

        /**
         * Before TCP channel is Active.
         */
        INIT{

        },


        WAIT_HELLO{
            @Override
            void processHelloMessage(ChannelHandlerContext ctx, MPHello mpHello){

                if(mpHello.getVersion().get() >= MPVersion.MP_03.get()){
                    MPFactory factory = MPFactories.getFactory(MPVersion.MP_03);
                    MPHello hello = factory.buildHello()
                            .setNodeName("MaoTestA")
                            .setNodePassword("123456789")
                            .build();
                    ctx.writeAndFlush(hello);
                }


                //TODO - below is hello world Test
                ChannelHandler channelHandler = ctx.handler();
                MaoProtocolDuplexHandler h = (MaoProtocolDuplexHandler) channelHandler;

                h.maoProtocolNode = h.controller.getMaoProtocolNode(ctx.channel().remoteAddress().toString());
                h.maoProtocolNode.announceConnected();
                h.setState(ACTIVE);
            };
        },
        ACTIVE{

        },
        GOODDAY,
        ENDING,
        END;


        private void processMPMessage(ChannelHandlerContext ctx, MPMessage mpMessage){
            switch(mpMessage.getType()){
                case HELLO:
                    processHelloMessage(ctx, (MPHello) mpMessage);
                    break;
                case ECHO_REQUEST:
                    processEchoRequestMessage();
                    break;
                case ECHO_REPLY:
                    processEchoReplyMessage();
                    break;
                case GOODDAY:
                    processGoodDayMessage();
                    break;
                default:
                    //TODO - throw
            }
        }

        void processHelloMessage(ChannelHandlerContext ctx, MPHello mpHello){

        };
        void processEchoRequestMessage(){

            //TODO - send EchoReply

            //get version
            MPVersion version;
            MPFactories.

        };
        void processEchoReplyMessage(){
            ;
        };
        void processGoodDayMessage(){

        };
    }




    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        setState(WAIT_HELLO);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        setState(ENDING);

        //TODO - Release resource

        setState(END);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MPMessage mpMessage = (MPMessage) msg;
        state.processMPMessage(ctx, mpMessage);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        //IdleTimeout
        //...
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //MPParseError
        //...
    }
}
