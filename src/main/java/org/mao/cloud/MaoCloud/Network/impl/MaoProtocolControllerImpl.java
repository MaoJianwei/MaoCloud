package org.mao.cloud.MaoCloud.Network.impl;

import io.netty.util.internal.ConcurrentSet;
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

import java.net.InetAddress;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
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

    private ConcurrentSet<String> configuredNodeSet = new ConcurrentSet<>();
    private ConcurrentLinkedQueue<String> unConnectedNodes = new ConcurrentLinkedQueue<>();
    private ConcurrentSet<String> connectingNodes = new ConcurrentSet<>();
    private ConcurrentMap<String, MaoProtocolNode> connectedNodes = new ConcurrentHashMap<>();

    @Activate
    private void activate() {

        log.info("Init...");

        configuredNodeSet.add("10.103.89.180");
        configuredNodeSet.add("10.103.89.116");

        Set<String> localAddresses = new HashSet<>();
        try {
            InetAddress[] ips = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
            for (InetAddress ip : ips) {
                localAddresses.add(ip.getHostAddress());
            }
        } catch (Exception e) {
            log.warn("Can not gain local IP, because: {}", e.getMessage());
        }

        configuredNodeSet.stream()
                .filter(address -> !localAddresses.contains(address))
                .forEach(address -> unConnectedNodes.offer(address));

        log.info("All unConnectedNodes is: {}", unConnectedNodes.toString());

        networkController.start();

        log.info("activate finish !");
    }

    @Deactivate
    private void deactivate() {
        log.info("Deactivating...");
        networkController.stop();
        log.info("deactivate OK !");
    }

    private class MaoProtocolNodeAgent implements MaoProtocolAgent {

        private final Logger log = LoggerFactory.getLogger(getClass());

        @Override
        public boolean addConnectedNode(MaoProtocolNode node) {
            connectingNodes.remove(node.getAddress());
            connectedNodes.put(node.getAddress(), node);

            log.info("New Node is up: {}", node.getAddress());
            return true;
        }

        @Override
        public String getOneUnConnectedNode() {
            String nodeIp = null;
            if(!unConnectedNodes.isEmpty()) {
                nodeIp = unConnectedNodes.poll();
                connectingNodes.add(nodeIp);
            }
            log.info("poll a unConnected node: {}", nodeIp);
            return nodeIp;
        }
    }
}
