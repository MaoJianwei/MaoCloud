package com.maojianwei.maocloud.node.impl;

import com.maojianwei.maocloud.grpc.node.NodeManagement;
import com.maojianwei.maocloud.grpc.node.NodeManagement.NodeLoginResult.LoginResult;
import com.maojianwei.maocloud.node.api.AuthenticateInterceptor;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;

import java.util.HashSet;
import java.util.Set;

public final class AuthenticateInterceptorImpl implements AuthenticateInterceptor {

    private static final Set<String> validCredentialSha256 = new HashSet<>();
    private static String myCredential;

    private volatile static AuthenticateInterceptor me;

    private AuthenticateInterceptorImpl() {
    }

    public static AuthenticateInterceptor getAuthenticateInterceptor() {
        if (me == null) {
            synchronized (AuthenticateInterceptorImpl.class) {
                if(me == null) {
                    me = new AuthenticateInterceptorImpl();
                }
            }
        }
        return me;
    }

    public void setMyCredential(String keySha256) {
        myCredential = keySha256;
    }

    public LoginResult validateLogin(String loginKey) {
        return loginKey.equals(myCredential) ? LoginResult.OK : LoginResult.INVALID_ACCOUNT;
    }


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {



        return null;
    }
}
