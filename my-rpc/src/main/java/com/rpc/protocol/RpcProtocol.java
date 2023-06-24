package com.rpc.protocol;

import com.rpc.util.ByteConverter;

import java.io.*;

public class RpcProtocol {
    private int version;
    private int classNameLen;
    private byte[] className;
    private int methodNameLen;
    private byte[] methodName;
    private int magicNum;
    private int bodyLen = 0;
    private byte[] body;
    private int messageLen = 0;
    final public static int HEAD_LEN = 24;

    public int getMessageLen() {
        return messageLen;
    }

    public RpcProtocol setMessageLen(int messageLen) {
        this.messageLen = messageLen;
        return this;
    }

    public byte[] getBody() {
        return body;
    }

    public RpcProtocol setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public RpcProtocol setVersion(int version) {
        this.version = version;
        return this;
    }

    public int getClassNameLen() {
        return classNameLen;
    }

    public RpcProtocol setClassNameLen(int classNameLen) {
        this.classNameLen = classNameLen;
        return this;
    }

    public byte[] getClassName() {
        return className;
    }

    public RpcProtocol setClassName(byte[] className) {
        this.className = className;
        return this;
    }

    public int getMethodNameLen() {
        return methodNameLen;
    }

    public RpcProtocol setMethodNameLen(int methodNameLen) {
        this.methodNameLen = methodNameLen;
        return this;
    }

    public byte[] getMethodName() {
        return methodName;
    }

    public RpcProtocol setMethodName(byte[] methodName) {
        this.methodName = methodName;
        return this;
    }

    public int getMagicNum() {
        return magicNum;
    }

    public RpcProtocol setMagicNum(int magicNum) {
        this.magicNum = magicNum;
        return this;
    }

    public int getBodyLen() {
        return bodyLen;
    }

    public RpcProtocol setBodyLen(int bodyLen) {
        this.bodyLen = bodyLen;
        return this;
    }


    public byte[] generateByteArray() {
        this.messageLen = HEAD_LEN + classNameLen + methodNameLen + bodyLen;
        byte[] data = new byte[this.messageLen];
        int index = 0;
        System.arraycopy(ByteConverter.intToBytes(version), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;

        System.arraycopy(ByteConverter.intToBytes(classNameLen), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;

        System.arraycopy(ByteConverter.intToBytes(methodNameLen), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;

        System.arraycopy(ByteConverter.intToBytes(magicNum), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;

        System.arraycopy(ByteConverter.intToBytes(bodyLen), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;

        System.arraycopy(ByteConverter.intToBytes(messageLen), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;

        System.arraycopy(className, 0, data, index, classNameLen);
        index += classNameLen;

        System.arraycopy(methodName, 0, data, index, methodNameLen);
        index += methodNameLen;

        System.arraycopy(body, 0, data, index, body.length);
        return data;
    }

    public RpcProtocol byteArrayToRpcHeader(byte[] data)
    {
        int index = 0;
        this.setVersion(ByteConverter.bytesToInt(data, index));
        index += Integer.BYTES;

        this.setClassNameLen(ByteConverter.bytesToInt(data, index));
        index += Integer.BYTES;

        this.setMethodNameLen(ByteConverter.bytesToInt(data, index));
        index += Integer.BYTES;

        this.setMagicNum(ByteConverter.bytesToInt(data, index));
        index += Integer.BYTES;

        this.setBodyLen(ByteConverter.bytesToInt(data, index));
        index += Integer.BYTES;

        this.setMessageLen(ByteConverter.bytesToInt(data, index));
        index += Integer.BYTES;

        this.className = new byte[this.classNameLen];
        System.arraycopy(data, index, this.className, 0, this.classNameLen);
        index += this.classNameLen;

        this.methodName = new byte[this.methodNameLen];
        System.arraycopy(data, index, this.methodName, 0, this.methodNameLen);
        index += this.methodNameLen;

        this.body = new byte[this.bodyLen];
        System.arraycopy(data, index, this.body, 0, this.bodyLen);

        return this;
    }

    public static Object bytes2Object(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object obj = oi.readObject();
        bi.close();
        oi.close();
        return obj;
    }

    public static byte[] object2Bytes(Serializable obj) throws Exception {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        bo.close();
        oo.close();
        return bo.toByteArray();
    }

    public byte[] createUserRespTobyteArray(int result)
    {
        byte[] data = new byte[Integer.BYTES];
        int index = 0;
        System.arraycopy(ByteConverter.intToBytes(result), 0, data, index, Integer.BYTES);
        return data;
    }
}
