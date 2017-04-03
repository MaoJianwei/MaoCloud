package org.mao.cloud.MaoCloud.Network.impl;

import io.netty.util.internal.ConcurrentSet;
import org.apache.felix.scr.annotations.*;
import org.mao.cloud.MaoCloud.Network.api.*;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.MaoCloud.Network.netty.protocol.api.base.MPMessage;
import org.mao.cloud.MaoCloud.api.MaoCloudData;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mao on 2016/7/3.
 *
 * Beta, functions are moved to {@link MaoProtocolNetworkControllerImpl}.
 */
@Component(immediate = false)
@Service
public class NetworkManager implements NetworkService {

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY)
    protected MaoProtocolController maoProtocolController;


    private ConcurrentMap<Integer, MaoProtocolNode> maoProtocolNodes = new ConcurrentHashMap();
    private Set<MaoNetworkDataListener> maoNetworkDataListeners = new ConcurrentSet();

    private InternalMPListener internalMPListener = new InternalMPListener();

    @Activate
    protected void activate(){
        maoProtocolController.addListener(internalMPListener);
    }

    @Deactivate
    protected void deactivate(){
        maoProtocolController.removeListener(internalMPListener);
    }



    @Override
    public void addDataListener(MaoNetworkDataListener dataListener) {
        maoNetworkDataListeners.add(dataListener);
    }

    @Override
    public void removeDataListener(MaoNetworkDataListener dataListener) {
        maoNetworkDataListeners.remove(dataListener);
    }

    @Override
    public void sendMessage(List nodes, MaoCloudData mcData) {
        // TODO: 2016/11/6
    }


    private class InternalMPListener implements MaoProtocolListener {

        @Override
        public void nodeConnected(MaoProtocolNode node) {

            Integer nodeId = Integer.valueOf(node.getAddressStr().replace(".",""));

            maoProtocolNodes.put(nodeId, node);
        }

        @Override
        public void nodeUnconnected(MaoProtocolNode node) {
            // TODO: 2016/11/6
        }

        @Override
        public void processMessage(MPMessage msg) {
            // TODO: 2016/11/6 parse data to MaoCloudData
            for (MaoNetworkDataListener l : maoNetworkDataListeners) {
                // l.processData();
            }
        }
    }
}
