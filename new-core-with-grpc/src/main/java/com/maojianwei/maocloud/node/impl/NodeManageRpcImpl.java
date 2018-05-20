package com.maojianwei.maocloud.node.impl;


import com.maojianwei.maocloud.grpc.common.Common.None;
import com.maojianwei.maocloud.grpc.node.NodeManageGrpc;
import com.maojianwei.maocloud.grpc.node.NodeManagement.MaoEchoReply;
import com.maojianwei.maocloud.grpc.node.NodeManagement.MaoEchoRequest;
import com.maojianwei.maocloud.grpc.node.NodeManagement.NodeLogin;
import com.maojianwei.maocloud.grpc.node.NodeManagement.NodeLoginResult;
import com.maojianwei.maocloud.grpc.node.NodeManagement.NodeLoginResult.LoginResult;
import com.maojianwei.maocloud.grpc.node.NodeManagement.NodeNetInfo;
import com.maojianwei.maocloud.node.api.AuthenticateInterceptor;
import com.maojianwei.maocloud.node.api.NodeConnectorService;
import com.maojianwei.maocloud.node.api.NodeManageRpc;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

public class NodeManageRpcImpl extends NodeManageGrpc.NodeManageImplBase implements NodeManageRpc{

    private static final None NONE = None.newBuilder().build();
    private volatile static NodeManageRpcImpl me;

    private AuthenticateInterceptor authenticateInterceptor;
    private NodeConnectorService nodeConnectorService;

    public static NodeManageRpcImpl getNodeManageService() {
        if (me == null) {
            synchronized(NodeManageRpcImpl.class) {
                me = new NodeManageRpcImpl();
            }
        }
        ArrayList a  = new ArrayList();
        a.add()
        return me;
    }

    @Override
    public void login(NodeLogin request, StreamObserver<NodeLoginResult> responseObserver) {
        LoginResult result = authenticateInterceptor.validateLogin(request.getCredentialKey());



        responseObserver.onNext(
                NodeLoginResult.newBuilder().setResult(result).setOk(result.equals(LoginResult.OK)).build());
        responseObserver.onCompleted();
    }

    @Override
    public void bidirectionalEcho(MaoEchoRequest request, StreamObserver<MaoEchoReply> responseObserver) {

        MaoEchoReply reply = MaoEchoReply.newBuilder().build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void addNode(NodeNetInfo request, StreamObserver<None> responseObserver) {

        //todo - request

        responseObserver.onNext(NONE);
        responseObserver.onCompleted();
    }
}
