package com.rpc.util;

import java.util.Random;

public class ToolUtil {

    //  num 表示生成的数组中1的个数 在数组中0表示抛弃请求 1表示接受请求
    public static byte[] randomGenerator(int limit, int num) {

        byte[] tempArray = new byte[limit];

        if (num <= 0) {
            for (int i = 0; i < limit; i++) {
                tempArray[i] = 0;
            }
            return tempArray;
        }
        if (num >= limit) {
            for (int i = 0; i < limit; i++) {
                tempArray[i] = 1;
            }
            return tempArray;
        }

        //在数组中随机填充num个1
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            int temp = Math.abs(random.nextInt()) % limit;
            while (tempArray[temp] == 1) {
                temp = Math.abs(random.nextInt()) % limit;
            }
            tempArray[temp] = 1;
        }
        return tempArray;
    }
}
