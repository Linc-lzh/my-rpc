package com.register;

import com.common.MyContants;
import com.register.model.ServerInfo;

import java.util.Arrays;
import java.util.List;

public class RegisterCenter {

    public static List<ServerInfo> getServerList(){
        return Arrays.asList(new ServerInfo("127.0.0.1", MyContants.SERVER_PORT, 90));
    }
}
