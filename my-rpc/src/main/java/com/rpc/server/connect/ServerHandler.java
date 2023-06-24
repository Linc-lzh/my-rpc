

package com.rpc.server.connect;

import com.common.MyContants;
import com.rpc.protocol.RpcProtocol;
import com.rpc.server.entity.User;
import com.rpc.server.service.UserService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;


public class ServerHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) ch.remoteAddress();
        String clientIp = socketAddress.getAddress().getHostAddress();

        logger.info("client connect to rpc server, client's ip is: " + clientIp);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) ch.remoteAddress();
        String clientIp = socketAddress.getAddress().getHostAddress();

        logger.info("client close the connection to rpc server, client's ip is: " + clientIp);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] recvData = (byte[]) msg;
        if (recvData.length == 0) {
            logger.warn("receive request from client, but the data length is 0");
            return;
        }

        logger.info("receive request from client, the data length is: " + recvData.length);

        //反序列化请求数据
        RpcProtocol rpcReq = new RpcProtocol();
        rpcReq.byteArrayToRpcHeader(recvData);

        if(rpcReq.getMagicNum() != MyContants.MAGIC_NUMBER){
            logger.warn("request msgic code error");
            return;
        }

        //解析请求，并调用处理方法
        int ret = -1;
        if(rpcReq.getClassNameLen() > 0){
            ret = executeServerMethod(rpcReq);

            //构造返回数据
            ByteBuf respData = constructRespData(rpcReq, ret);
            ctx.channel().writeAndFlush(respData);
        }
    }

    private static ByteBuf constructRespData(RpcProtocol rpcReq, int ret) {
        RpcProtocol rpcResp = new RpcProtocol();
        rpcResp.setClassNameLen(rpcReq.getClassNameLen());
        rpcResp.setClassName(rpcReq.getClassName());
        rpcResp.setMethodNameLen(rpcReq.getMethodNameLen());
        rpcResp.setMethodName(rpcReq.getMethodName());
        rpcResp.setVersion(rpcReq.getVersion());
        rpcResp.setMagicNum(rpcReq.getMagicNum());
        rpcResp.setBodyLen(Integer.BYTES);
        byte[] body = rpcResp.createUserRespTobyteArray(ret);
        rpcResp.setBody(body);
        ByteBuf respData = Unpooled.copiedBuffer(rpcResp.generateByteArray());
        return respData;
    }

    private static int executeServerMethod(RpcProtocol rpcReq) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        int ret;
        User user = User.byteArrayToUserInfo(rpcReq.getBody());
        String klassName = new String(rpcReq.getClassName());
        Class<?> klass = Class.forName(klassName);
        String methodName = new String(rpcReq.getMethodName());
        Method method = klass.getMethod(methodName, User.class);
        ret = (int)method.invoke(klass.newInstance(), user);
        return ret;
    }
}