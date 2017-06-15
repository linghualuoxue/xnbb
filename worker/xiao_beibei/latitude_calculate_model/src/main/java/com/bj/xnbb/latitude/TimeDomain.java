package com.bj.xnbb.latitude;

import com.bj.xnbb.common.Constant;

import java.io.*;

/**
 * Created by XNBB on 2017/6/15.
 */
public class TimeDomain {
    public static void main(String[] args) throws Exception{
        if (args != null && args.length > 0){
            for (String fileName : args) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                String context = null;
                context = br.readLine();

                String[] split = context.split(Constant.SEPARATOR);
                String time_hour = split[0].substring(0, 11);
                String time_minute = split[0].substring(0, 13);

                String[] frequency_pointers = split[6].split(" ");
                for (String pointer : frequency_pointers) {
                    float value = (Float.valueOf(pointer) / 10 - 170) - getNoisyPoint();


                }


            }
        }
    }


    /**
     * 获取噪点
     * @return
     */
    public static float getNoisyPoint(){
        return -100.00f;
    }
}
