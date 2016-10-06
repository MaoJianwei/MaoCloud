package org.mao.cloud.MaoCloud.Network.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolAgent;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolController;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolNetworkController;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mao on 2016/10/5.
 */
@Component(immediate = true)
@Service
public class MaoProtocolControllerImpl implements MaoProtocolController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private MaoProtocolAgent agent = new MaoProtocolNodeAgent();
    private MaoProtocolNetworkController networkController = new MaoProtocolNetworkControllerImpl(agent);

    private ConcurrentMap<String, MaoProtocolNode> connectedNodes = new ConcurrentHashMap<>();

    @Activate
    private void activate(){
        networkController.start();
    }

    @Deactivate
    private void deactivate(){
        networkController.stop();
    }

    private class MaoProtocolNodeAgent implements MaoProtocolAgent{

        @Override
        public boolean addConnectedNode(MaoProtocolNode node){
            connectedNodes.put(node.getAddress(), node);
            log.info("New Node is up: {}", node.getAddress());
            return true;
        }
    }
}
