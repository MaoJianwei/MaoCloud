package org.mao.cloud.MaoCloud.Network.netty.handler;

import io.netty.channel.Channel;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.END;
import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.ENDING;
import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.WAIT_HELLO;

/**
 * Created by mao on 2016/9/17.
 */
public class MaoProtocolDuplexHandler extends ChannelDuplexHandler {

    private static final Logger log = LoggerFactory.getLogger(MaoProtocolDuplexHandler.class);


    private final boolean isRoleClient;

    private MaoProtocolNetworkControllerImpl controller;

    private MaoProtocolNode maoProtocolNode;
    private MaoProtocolState state;
    private Channel channel;

    public MaoProtocolDuplexHandler(MaoProtocolNetworkControllerImpl controller, boolean isRoleClient){
        this.controller = controller;
        this.isRoleClient = isRoleClient;
        state = MaoProtocolState.INIT;
    }

    private MaoProtocolState getState(){
        return this.state;
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

                //TODO - below is hello world Test
                ChannelHandler channelHandler = ctx.handler();
                MaoProtocolDuplexHandler h = (MaoProtocolDuplexHandler) channelHandler;

                log.info("ready to process a MPHello, state:{} Type: {}, Version: {}, idHashValue: {}",
                        h.getState(),
                        mpHello.getType(),
                        mpHello.getVersion(),
                        mpHello.getHashValue());



                if(!h.isRoleClient) {
                    log.info("My role is Server, ready to send hello as a reply.");
                    if (mpHello.getVersion().get() >= MPVersion.MP_03.get()) {

                        log.info("will generate a hello as a reply.");
                        MPFactory factory = MPFactories.getFactory(MPVersion.MP_03);
                        MPHello hello = factory.buildHello()
                                .setNodeName("MaoTestA")
                                .setNodePassword("123456789")
                                .build();

                        log.info("will writeAndFlush a hello as a reply, Type: {}, Version: {}, idHashValue: {}",
                                hello.getType(),
                                hello.getVersion(),
                                hello.getHashValue());
                        ctx.writeAndFlush(hello);
                    } else {
                        log.warn("version is not match to anyone!");
                    }
                }

                log.info("will get new MaoProtocolNode representation...");
                h.maoProtocolNode = h.controller.getMaoProtocolNode(ctx.channel());
                log.info("got a new MaoProtocolNode representation, {}", h.maoProtocolNode.getAddress());

                log.info("ready to announce maoProtocolNode connected...");
                h.maoProtocolNode.announceConnected();
                log.info("announce maoProtocolNode connected, finished");

                h.setState(ACTIVE);
                log.info("state should go to ACTIVE, state: {}", h.getState());
            };
        },
        ACTIVE{

        },
        GOODDAY,
        ENDING,
        END;


        private void processMPMessage(ChannelHandlerContext ctx, MPMessage mpMessage){

            log.info("ready to process a MPMessage, state:{} Type: {}, Version: {}",
                    ((MaoProtocolDuplexHandler)ctx.handler()).getState(),
                    mpMessage.getType(),
                    mpMessage.getVersion());

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
                    log.warn("go across all case to default in processMPMessage(), channel's String: {}",
                            ctx.channel().toString());
            }
        }

        void processHelloMessage(ChannelHandlerContext ctx, MPHello mpHello){
            log.warn("go into default processHelloMessage()");
        };
        void processEchoRequestMessage(){
            log.warn("go into default processEchoRequestMessage()");

            //TODO - send EchoReply

            //get version
            //MPVersion version;
            //MPFactories.

        };
        void processEchoReplyMessage(){
            log.warn("go into default processEchoReplyMessage()");
        };
        void processGoodDayMessage(){
            log.warn("go into default processGoodDayMessage()");
        };
    }






    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channel = ctx.channel();
        log.info("go into channelActive, state: {}, channel's string: {}",
                state,
                channel.toString());
        setState(WAIT_HELLO);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        log.info("go into channelInactive, state: {}", state);

        setState(ENDING);

        //TODO - Release resource

        setState(END);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("channel read, object class: {}", msg.getClass());
        MPMessage mpMessage = (MPMessage) msg;
        log.info("channel read, MPMessage Type:{}, Version:{}",
                ((MPMessage) msg).getType(),
                ((MPMessage) msg).getVersion());

        log.info("Will process MPMessage, state: {}", state);

        state.processMPMessage(ctx, mpMessage);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        //IdleTimeout
        //...
        log.warn("go into userEventTriggered(), ChannelHandlerContext: {}, Object: {}",
                ctx.channel().toString(),
                evt.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //MPParseError
        //...
        log.warn("go into exceptionCaught(), ChannelHandlerContext: {}, Throwable: {}",
                ctx.channel().toString(),
                cause.getMessage());
    }
}
