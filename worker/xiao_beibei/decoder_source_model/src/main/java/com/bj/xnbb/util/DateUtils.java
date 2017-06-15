package com.bj.xnbb.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**时间格式转化工具
 * Created by XNBB on 2017/6/14.
 */
public class DateUtils {

    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
    /**
     * 处理单个数字的时间
     * @param time
     * @return
     */
    public static String parseSingleTime(int time){
        return time<10?(0+String.valueOf(time)):String.valueOf(time);
    }

    /**
     * 获取时间戳，
     * @return  yyyyMMddHH
     */
    public static String getTimestamp(){
        Date date = new Date();
        return df.format(date);
    }

    public static void main(String[] args) {
        System.out.printf(getTimestamp());
    }

}
