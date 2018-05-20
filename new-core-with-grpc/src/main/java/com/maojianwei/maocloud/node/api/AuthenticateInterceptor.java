package com.maojianwei.maocloud.node.api;

import com.maojianwei.maocloud.grpc.node.NodeManagement.NodeLoginResult.LoginResult;
import io.grpc.ServerInterceptor;

/**
 * Should follow Singleton Pattern.
 */
public interface AuthenticateInterceptor extends ServerInterceptor {

    LoginResult validateLogin(String loginKey);
    void setMyCredential(String keySha256);
}
