package org.mao.cloud.MaoCloud.Network.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolNetworkController;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPFactory;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoReply;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPEchoRequest;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.message.MPHello;
import org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.END;
import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.ENDING;
import static org.mao.cloud.MaoCloud.Network.netty.handler.MaoProtocolDuplexHandler.MaoProtocolState.WAIT_HELLO;
import static org.mao.cloud.MaoCloud.Network.netty.protocol.base.MPVersion.MP_03;

/**
 * Created by mao on 2016/9/17.
 */
public class MaoProtocolDuplexHandler extends ChannelDuplexHandler {

    private static final Logger log = LoggerFactory.getLogger(MaoProtocolDuplexHandler.class);

    // protocol nodes' Controller
    private MaoProtocolNetworkController controller;

    // my protocol node Representation
    private MaoProtocolNode maoProtocolNode;

    private final boolean isRoleClient;
    private MaoProtocolState state;
    private Channel channel;

    private MPVersion mpVersion;
    private MPFactory mpFactory;

    public MaoProtocolDuplexHandler(MaoProtocolNetworkController controller, boolean isRoleClient){
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
                MaoProtocolDuplexHandler h = ((MaoProtocolDuplexHandler)ctx.handler());

                log.info("ready to process a MPHello, state:{} Type: {}, Version: {}, idHashValue: {}",
                        h.getState(),
                        mpHello.getType(),
                        mpHello.getVersion(),
                        mpHello.getHashValue());

                if(h.isRoleClient){
                    log.info("My role is Client, will init myself...");
                    if (mpHello.getVersion().get() == MP_03.get()){
                        h.mpVersion = MP_03;
                        h.mpFactory = h.controller.getMapProtocolFactory03();
                    }
                } else {
                    log.info("My role is Server, ready to send hello as a reply.");
                    if (mpHello.getVersion().get() == MP_03.get()) {

                        h.mpVersion = MP_03;
                        h.mpFactory = h.controller.getMapProtocolFactory03();

                        log.info("will generate a hello as a reply.");
                        MPHello hello = h.mpFactory.buildHello()
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
                log.info("got a new MaoProtocolNode representation, {}",
                        h.maoProtocolNode.getAddressStr());

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
                    processEchoRequestMessage(ctx, (MPEchoRequest) mpMessage);
                    break;
                case ECHO_REPLY:
                    processEchoReplyMessage(ctx, (MPEchoReply) mpMessage);
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
        }
        void processEchoRequestMessage(ChannelHandlerContext ctx, MPEchoRequest mpEchoRequest){
            MaoProtocolDuplexHandler h = ((MaoProtocolDuplexHandler)ctx.handler());

            log.info("receive Echo Request, Type: {}, Version: {}",
                    mpEchoRequest.getType(),
                    mpEchoRequest.getVersion());

            log.info("echo Reply is building...");
            MPEchoReply mpEchoReply = h.mpFactory.buildEchoReply().build();

            log.info("echo Reply is sending...");
            ctx.writeAndFlush(mpEchoReply);
            log.info("echo Reply has been sent");
        }
        void processEchoReplyMessage(ChannelHandlerContext ctx, MPEchoReply mpEchoReply){
            log.info("receive Echo Reply, Type: {}, Version: {}",
                    mpEchoReply.getType(),
                    mpEchoReply.getVersion());
        }
        void processGoodDayMessage(){
            log.warn("go into default processGoodDayMessage()");
        };




        void sendHelloMessage(ChannelHandlerContext ctx){
            MaoProtocolDuplexHandler h = ((MaoProtocolDuplexHandler)ctx.handler());

            MPHello hello = h.controller.getMapProtocolFactory03()
                    .buildHello()
                    .setNodeName("MaoTestB")
                    .setNodePassword("987654321")
                    .build();

            log.info("sending MPHello, Type:{}, Version:{}, idHashValue:{}",
                    hello.getType(),
                    hello.getVersion(),
                    hello.getHashValue());
            ctx.writeAndFlush(hello);
            log.info("sent MPHello, Type:{}, Version:{}, idHashValue:{}",
                    hello.getType(),
                    hello.getVersion(),
                    hello.getHashValue());
        }

        void sendEchoRequestMessage(ChannelHandlerContext ctx){

            MaoProtocolDuplexHandler h = ((MaoProtocolDuplexHandler)ctx.handler());

            log.info("echo Request is building...");
            MPEchoRequest echoRequest = h.mpFactory.buildEchoRequest().build();

            log.info("echo Request is sending...");
            ctx.writeAndFlush(echoRequest);
            log.info("echo Request has been sent");
        }
    }




    // --- channel functions ---

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channel = ctx.channel();
        log.info("go into channelActive, state: {}, channel's string: {}",
                state,
                channel.toString());
        setState(WAIT_HELLO);

        if(isRoleClient) {
            state.sendHelloMessage(ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {

        log.info("go into channelInactive, state: {}", state);

        setState(ENDING);
        //TODO - Release resource

        log.info("{} will announce disconnected, state: {}", maoProtocolNode.getAddressStr(), state);
        maoProtocolNode.announceDisConnected();

        // TODO: 2016/10/20 try to reconnect just while error occur.
        if(isRoleClient){
            log.info("{} will report clientNodeDown, state: {}", maoProtocolNode.getAddressStr(), state);
            controller.clientReportNodeDown(this.maoProtocolNode.getAddressInet());
        }

        setState(END);
        log.info("{} release over, good day! state: {}", maoProtocolNode.getAddressStr(), state);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("channel read, object class: {}", msg.getClass());
        MPMessage mpMessage = (MPMessage) msg;
        log.info("channel read, MPMessage Type:{}, Version:{}",
                mpMessage.getType(),
                mpMessage.getVersion());

        log.info("Will process MPMessage, state: {}", state);

        state.processMPMessage(ctx, mpMessage);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        if(evt instanceof IdleStateEvent){
            log.info("Idle, State: {}, Channel: {}, Object: {}",
                    ((IdleStateEvent) evt).state(),
                    ctx.channel().toString(),
                    evt.toString());

            state.sendEchoRequestMessage(ctx);
        } else {
            log.warn("UnHandled! go into userEventTriggered(), channel: {}, Event: {}",
                    ctx.channel().toString(),
                    evt.toString());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //MPParseError
        //...
        if(cause instanceof ReadTimeoutException){
            log.warn("Read Timeout! channel will be closing. channel: {}", ctx.channel().toString());

            //channelInactive() will do release job.
            //we can do more to cut off the link gracefully.
            ctx.close();
        } else {
            log.error("UnHandled! go into exceptionCaught(), channel: {}, Throwable: {}",
                    ctx.channel().toString(),
                    cause.getMessage());
        }
    }
}
