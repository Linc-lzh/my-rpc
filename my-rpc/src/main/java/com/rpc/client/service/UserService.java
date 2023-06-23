package com.rpc.client.service;

import com.common.MyContants;
import com.common.RpcCommand;
import com.rpc.client.connect.TcpClient;
import com.rpc.client.entity.User;
import com.rpc.protocol.RpcProtocol;
import com.rpc.util.ByteConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public int addUser (User userinfo) throws Exception {
        //初始化客户端连接
        TcpClient client = TcpClient.GetInstance();
        try {
            client.init();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("init rpc client error");
        }

        //构造请求数据
        RpcProtocol rpcReq = new RpcProtocol();
        rpcReq.setCmd(RpcCommand.CMD_CREATE_USER);
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
}
