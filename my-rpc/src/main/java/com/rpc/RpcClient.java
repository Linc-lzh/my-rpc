package com.rpc;

import com.rpc.client.entity.User;
import com.rpc.client.service.UserService;

/**
 * Hello world!
 *
 */
public class RpcClient
{
    public static void main(String[] args) throws Exception {
        UserService proxyUserService = new UserService();

        User user = new User();
        user.setAge((short) 26);
        user.setSex((short) 1);


        int ret = proxyUserService.addUser(user);
        if(ret == 0)
            System.out.println("调用远程服务创建用户成功！！！");
        else
            System.out.println("调用远程服务创建用户失败！！！");
    }
}
