package com.rpc.server.service;

import com.rpc.server.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public int addUser(User userinfo){
        logger.debug("create user success, uid=" + userinfo.getUid());
        return 1;
    }
}
