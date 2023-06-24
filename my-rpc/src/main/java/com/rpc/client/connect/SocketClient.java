package com.rpc.client.connect;

import java.nio.channels.SocketChannel;

public class SocketClient {
    private SocketChannel channel = null;
    private byte[] weightArrays = null;

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public byte[] getWeightArrays() {
        return weightArrays;
    }

    public void setWeightArrays(byte[] weightArrays) {
        this.weightArrays = weightArrays;
    }
}
