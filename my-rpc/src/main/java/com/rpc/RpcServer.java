package com.rpc;

import com.common.MyContants;
import com.rpc.server.connect.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServer {
    private static Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public static void main(String[] args) throws Exception {
        Thread tcpServerThread = new Thread("tcpServer") {
            public void  run() {
                TcpServer tcpServer = new TcpServer(MyContants.SERVER_PORT);
                try {
                    tcpServer.start();
                } catch (Exception e) {
                    logger.info("TcpServer start exception: " + e.getMessage());
                }
            }
        };
        tcpServerThread.start();
        tcpServerThread.join();
    }
}
