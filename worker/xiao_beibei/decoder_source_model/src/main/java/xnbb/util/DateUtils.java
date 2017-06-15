package xnbb.util;

/**时间格式转化工具
 * Created by XNBB on 2017/6/14.
 */
public class DateUtils {

    public static String parseSingleTime(int time){
        return time<10?(0+String.valueOf(time)):String.valueOf(time);
    }
}
