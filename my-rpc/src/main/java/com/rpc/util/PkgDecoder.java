package com.rpc.util;

import com.rpc.protocol.RpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PkgDecoder extends ByteToMessageDecoder
{
    private Logger logger = LoggerFactory.getLogger(PkgDecoder.class);

    public PkgDecoder(){ }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception
    {
        if (buffer.readableBytes() < RpcProtocol.HEAD_LEN) {
            return; //未读完足够的字节流，缓存后继续读
        }

        byte[] intBuf = new byte[4];
        buffer.getBytes(buffer.readerIndex() + RpcProtocol.HEAD_LEN - 4, intBuf);    // ImHeader的bodyLen在第68位到71为, int类型
        int messageLen = ByteConverter.bytesToIntBigEndian(intBuf);

        if (buffer.readableBytes() < messageLen) {
            return; //未读完足够的字节流，缓存后继续读
        }

        byte[] bytesReady = new byte[messageLen];
        buffer.readBytes(bytesReady);
        out.add(bytesReady);
    }
}
