package com.bj.xnbb.latitude;

import com.bj.xnbb.common.Constant;

import java.io.*;
import java.util.*;

/**
 * Created by XNBB on 2017/6/15.
 */
public class TimeDomain {
    public static void main(String[] args) throws Exception{

        if (args != null && args.length > 0){
            for (String fileName : args) {
                List<String> minuteList = new ArrayList<String>();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                String context = null;

                Map<String,Map<Integer, float[]>> hourMap = new TreeMap<String, Map<Integer, float[]>>();

                Map<Integer,float[]> minuteMap = new HashMap<Integer,float[]>();//每分钟频点对应的占用率

                List<int[]> frameList = new ArrayList<int[]>();//存储每贞的对应数据

                String lastMinute = "";
                int i = 0;//帧数计数器
                while((context = br.readLine())!=null){

                    String[] split = context.split(Constant.SEPARATOR);
                    String time_hour = split[0].substring(0, 10);
                    String time_minute = split[0].substring(0, 12);
                    lastMinute = time_minute;
                    if(minuteList.contains(time_minute)){
                        //包含，就是同一分钟内的数据
                        i++; //一分钟的帧数增加1

                        String[] frequency_pointers = split[6].split(" ");
                        for (int j = 0; j < frequency_pointers.length-1; j++) {
                            float value = (float) (getNoisyPoint() + (Float.valueOf(frequency_pointers[i]) / 10.0f - Constant.FIELD_STRENGTH_CONSTANT));
                                minuteMap.get(j)[0] += value>= Constant.THRESHOLD_5 ? 1:0;
                                minuteMap.get(j)[1] += value>= Constant.THRESHOLD_10 ? 1:0;
                                minuteMap.get(j)[2] += value>= Constant.THRESHOLD_20 ? 1:0;
                                minuteMap.get(j)[3] += value>= Constant.THRESHOLD_30 ? 1:0;
                        }
                    }else{
                        //不包含，就是开启下一分钟的数据
                        minuteList.add(time_minute);
                        if (i>0){
                            for (float[] ints : minuteMap.values()) {
                                ints[0] =  ints[0]/i;
                                ints[1] = ints[1]/i;
                                ints[2] = ints[2]/i;
                                ints[3] = ints[3]/i;
                            }
                            hourMap.put(time_minute,minuteMap);
                            i=0;
                            minuteMap = new HashMap<Integer,float[]>();
                        }

                        String[] frequency_pointers = split[6].split(" ");
                        for (int j = 0; j < frequency_pointers.length-1; j++) {
                            float value = (float) (getNoisyPoint() + (Float.valueOf(frequency_pointers[i]) / 10.0f - Constant.FIELD_STRENGTH_CONSTANT));
                                minuteMap.put(j,new float[4]);
                                minuteMap.get(j)[0] = value>= Constant.THRESHOLD_5 ? 1:0;
                                minuteMap.get(j)[1]= value>= Constant.THRESHOLD_10 ? 1:0;
                                minuteMap.get(j)[2]= value>= Constant.THRESHOLD_20 ? 1:0;
                                minuteMap.get(j)[3]= value>= Constant.THRESHOLD_30 ? 1:0;
                        }
                        i++;
                    }

                }
                //如果i还大于0则还有数据没有处理
                if (i>0){
                    for (float[] ints : minuteMap.values()) {
                        ints[0] =  ints[0]/i;
                        ints[1] = ints[1]/i;
                        ints[2] = ints[2]/i;
                        ints[3] = ints[3]/i;
                    }
                    hourMap.put(lastMinute,minuteMap);
                }

                //计算小时的占用率-----start

                //计算小时的占用率总和
                List<String> hourList_sum = new ArrayList<String>();
                Map<String, TreeMap<Integer,float[]>> occupy_hour_sum= new TreeMap<String, TreeMap<Integer,float[]>>();  //小时内每个频率点的占用率
                //Map<Ineger,float[]>
                int count=0;//分钟计数器
                for (Map.Entry<String, Map<Integer, float[]>> entry : hourMap.entrySet()) {
                    String minute_ = entry.getKey();    //每一分钟的数据
                    String hour_ = minute_.substring(0, minute_.length() - 2);
                    int length = entry.getValue().size();

                    if(hourList_sum.contains(hour_)){
                        Map<Integer, float[]> values = entry.getValue();
                        for (int m = 0; m < length-1; m++) {
                            occupy_hour_sum.get(hour_).get(m)[0] += values.get(m)[0];
                            occupy_hour_sum.get(hour_).get(m)[1] += values.get(m)[1];
                            occupy_hour_sum.get(hour_).get(m)[2] += values.get(m)[2];
                            occupy_hour_sum.get(hour_).get(m)[3] += values.get(m)[3];
                        }
                           count++;

                    }else{
                            //计算上一小时的时间域占用
                        if (count>0){
                            for (TreeMap<Integer, float[]> integerTreeMap : occupy_hour_sum.values()) {
                                for (int s = 0; s < integerTreeMap.size()-1; s++) {
                                    integerTreeMap.get(s)[0]=integerTreeMap.get(s)[0]/count;
                                    integerTreeMap.get(s)[1]=integerTreeMap.get(s)[1]/count;
                                    integerTreeMap.get(s)[2]=integerTreeMap.get(s)[2]/count;
                                    integerTreeMap.get(s)[3]=integerTreeMap.get(s)[3]/count;
                                }
                            }
                            count = 0;

                        }
                       //新的小时开始
                        Map<Integer, float[]> values = entry.getValue();
                        TreeMap valueMap =null;
                        for (int m = 0; m < length-1; m++) {
                            valueMap = new TreeMap<Integer,float[]>();
                            float[] floats = new float[4];
                            floats[0] = values.get(m)[0];
                            floats[1] = values.get(m)[1];
                            floats[2] = values.get(m)[2];
                            floats[3] = values.get(m)[3];
                            valueMap.put(m,floats);
                        }
                        occupy_hour_sum.put(hour_,valueMap);
                        count++;
                    }

                }

                if(count>0){
                    for (TreeMap<Integer, float[]> integerTreeMap : occupy_hour_sum.values()) {
                        for (int s = 0; s < integerTreeMap.size()-1; s++) {
                            integerTreeMap.get(s)[0]=integerTreeMap.get(s)[0]/count;
                            integerTreeMap.get(s)[1]=integerTreeMap.get(s)[1]/count;
                            integerTreeMap.get(s)[2]=integerTreeMap.get(s)[2]/count;
                            integerTreeMap.get(s)[3]=integerTreeMap.get(s)[3]/count;
                        }
                    }
                }


            }
        }
    }


    /**
     * 获取噪点
     * @return
     */
    public static int getNoisyPoint(){
        return 100;
    }
}
