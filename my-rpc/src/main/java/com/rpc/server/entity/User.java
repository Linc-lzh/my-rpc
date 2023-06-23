package com.rpc.server.entity;

import com.rpc.util.ByteConverter;

public class User {
    private long uid;
    private short age;
    private short sex;

    public long getUid() {
        return uid;
    }

    public User setUid(long uid) {
        this.uid = uid;
        return this;
    }

    public short getAge() {
        return age;
    }

    public User setAge(short age) {
        this.age = age;
        return this;
    }

    public short getSex() {
        return sex;
    }

    public User setSex(short sex) {
        this.sex = sex;
        return this;
    }

    public static User byteArrayToUserInfo(byte[] data) {
        User user = new User();
        int index = 0;

        user.setUid(ByteConverter.bytesToLong(data, index));
        index += Long.BYTES;

        user.setAge(ByteConverter.bytesToShort(data, index));
        index += Short.BYTES;

        user.setSex(ByteConverter.bytesToShort(data, index));
        index += Short.BYTES;
        return user;
    }

    public byte[] userInfoTobyteArray()
    {
        byte[] data = new byte[Long.BYTES + Short.BYTES + Short.BYTES];
        int index = 0;
        System.arraycopy(ByteConverter.longToBytes(this.getUid()), 0, data, index, Long.BYTES);
        index += Long.BYTES;
        System.arraycopy(ByteConverter.shortToBytes(this.getAge()), 0, data, index, Short.BYTES);
        index += Short.BYTES;
        System.arraycopy(ByteConverter.shortToBytes(this.getSex()), 0, data, index, Short.BYTES);
        return data;
    }
}