package org.mao.cloud.MaoCloud.Network.cli;

import org.apache.karaf.shell.commands.Command;
import org.mao.cloud.MaoCloud.Network.api.MaoProtocolControllerAdmin;
import org.mao.cloud.cli.base.AbstractShellCommand;

/**
 * Created by mao on 2016/10/20.
 */
@Command(scope = "maocloud", name = "list-network-offline-nodes")
public class ListNetworkOfflineNodes extends AbstractShellCommand {

    @Override
    public void execute(){
        MaoProtocolControllerAdmin admin = getService(MaoProtocolControllerAdmin.class);

        admin.getAllUnConnectedNodes().forEach(node -> print(node));
    }
}
