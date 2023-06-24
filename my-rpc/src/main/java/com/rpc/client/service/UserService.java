package com.rpc.client.service;

import com.rpc.client.entity.User;
import com.rpc.util.RpcReference;

public interface UserService {
    @RpcReference(className="com.rpc.server.service.UserService", methodName="addUser")
    int addUser (User userinfo) throws Exception;
}
