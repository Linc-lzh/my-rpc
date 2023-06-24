package com.rpc;

import com.common.MyContants;
import com.rpc.client.connect.TcpClient;
import com.rpc.client.entity.User;
import com.rpc.client.service.UserService;
import com.rpc.protocol.RpcProtocol;
import com.rpc.util.ByteConverter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.rpc.util.RpcReference;
/**
 * Hello world!
 *
 */
public class RpcClient {
    public static void main(String[] args) throws Exception {

        Object proxyInstance = Proxy.newProxyInstance(RpcClient.class.getClassLoader(),
                new Class[]{UserService.class},
                new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                User userinfo = (User)args[0];
                if(method.isAnnotationPresent(RpcReference.class)) {
                    RpcReference annotation = method.getAnnotation(RpcReference.class);
                    //初始化客户端连接
                    TcpClient client = TcpClient.GetInstance();
                    try {
                        client.init();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("init rpc client error");
                    }

                    //构造请求数据
                    RpcProtocol rpcReq = new RpcProtocol();
                    byte[] classNameArr = annotation.className().getBytes();
                    rpcReq.setClassNameLen(classNameArr.length);
                    rpcReq.setClassName(classNameArr);

                    byte[] methodNameArr = annotation.methodName().getBytes();
                    rpcReq.setMethodNameLen(methodNameArr.length);
                    rpcReq.setMethodName(methodNameArr);

                    rpcReq.setVersion(MyContants.VERSION);

                    rpcReq.setMagicNum(MyContants.MAGIC_NUMBER);

                    byte[] body = userinfo.userInfoTobyteArray();
                    rpcReq.setBodyLen(body.length);
                    rpcReq.setBody(body);

                    //序列化
                    byte[] reqData = rpcReq.generateByteArray();

                    //发送请求
                    client.sendData(reqData);

                    //接收请求结果
                    byte[] recvData = client.recvData();

                    //反序列化结果
                    RpcProtocol rpcResp = new RpcProtocol();
                    rpcResp.byteArrayToRpcHeader(recvData);

                    int ret = ByteConverter.bytesToInt(rpcResp.getBody(), 0);
                    return ret;
                }

                return method.invoke(proxy, args);
            }
        });

        User user = new User();
        user.setAge((short) 26);
        user.setSex((short) 1);

        int ret = ((UserService)proxyInstance).addUser(user);
        if(ret == 0)
            System.out.println("调用远程服务创建用户成功！！！");
        else
            System.out.println("调用远程服务创建用户失败！！！");
    }
}
