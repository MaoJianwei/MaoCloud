package org.mao.cloud.MaoCloud.Network.cli;

import org.apache.karaf.shell.commands.Command;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolControllerAdmin;
import org.mao.cloud.MaoCloud.Network.base.MaoProtocolNode;
import org.mao.cloud.cli.base.AbstractShellCommand;

import java.util.List;

/**
 * Created by mao on 2016/10/16.
 */
@Command(scope = "maocloud", name = "list-network-peer",
        description = "List all network infos of node peers.")
public class ListNetworkPeer extends AbstractShellCommand{

    @Override
    protected void execute(){
        MaoProtocolControllerAdmin controllerAdmin = getService(MaoProtocolControllerAdmin.class);
        List<MaoProtocolNode> nodes = controllerAdmin.getAllConnectedNodes();
        nodes.forEach(node->{
            print(node.getChannelInfo());
        });
    }
}
