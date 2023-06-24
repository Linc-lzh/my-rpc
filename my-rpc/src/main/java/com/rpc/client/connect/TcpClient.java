package com.rpc.client.connect;

import com.common.MyContants;
import com.register.RegisterCenter;
import com.register.model.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.rpc.util.ToolUtil;

public class TcpClient {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static int MAX_PACKAGE_SIZE = 1024 * 4;
    private static String SERVER_IP = "127.0.0.1";
    private static TcpClient instance = null;

    private static ConcurrentHashMap<String, SocketClient> socketClients = new ConcurrentHashMap<>();
    private boolean isInit = false;
    private AtomicInteger count = new AtomicInteger();
    //private ChannelFuture channelFuture = null;


    private final static int CONNECT_TIMEOUT_MILLIS = 2000;

    //private Bootstrap bootstrap = new Bootstrap();
    public TcpClient() {}

    public static TcpClient GetInstance() {
        if (instance==null){
            synchronized (TcpClient.class){
                if (instance==null){
                    instance = new TcpClient();
                }
            }
        }
        return instance;
    }


    public void init() throws Exception{
        if(!isInit) {
            SocketClient client = new SocketClient();
            for (ServerInfo serverInfo : RegisterCenter.getServerList()) {
                client.setChannel(SocketChannel.open(new InetSocketAddress(SERVER_IP, MyContants.SERVER_PORT)));
                client.getChannel().configureBlocking(true);
                client.setWeightArrays(ToolUtil.randomGenerator(100, serverInfo.getWeight()));
                socketClients.put(serverInfo.getHost() + ":" + serverInfo.getPort(),
                        client);
            }

        }
        isInit = true;
    }



    public boolean sendData(byte[] data){
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.put(data);
        byteBuffer.flip();
        int ret = 0;
        try {
            SocketClient socketClient = socketClients.get("127.0.0.1:58885");
            byte[] weightArrays = socketClient.getWeightArrays();
            if (weightArrays[count.get()] == 1) {
                ret = socketClient.getChannel().write(byteBuffer);
            }

            count.getAndIncrement();
            count.compareAndSet(weightArrays.length - 1, 0);
            if(ret == 0)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public byte[] recvData()  {
        ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_PACKAGE_SIZE);
        try {
            SocketClient socketClient = socketClients.get("127.0.0.1:58885");
            int rs = socketClient.getChannel().read(byteBuffer);
            byte[] bytes = new byte[rs];
            byteBuffer.flip();
            byteBuffer.get(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
