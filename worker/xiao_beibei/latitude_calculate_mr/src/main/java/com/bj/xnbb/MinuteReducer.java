package com.bj.xnbb;

import com.bj.xnbb.common.Constant;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**21010000_0014_20160930_930MHz_934_25.00_V_M
 * Created by XNBB on 2017/6/20.
 */
public class MinuteReducer extends Reducer<Text,Text,Text,Text> {
     Text text =  new Text();
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
         int i = 0;
         int sum = getCount(values);//获取一分钟内总共有多少条数据

        String[] fileNameSplits = key.toString().split(Constant.FILE_NAME_SPLIT);

        float startHz = Float.parseFloat(fileNameSplits[3]);//开始频率
        float endHz = Float.parseFloat(fileNameSplits[4]);//结束频率
        float step = Float.parseFloat(fileNameSplits[5]);//步进
        BigDecimal sumBig = new BigDecimal(sum);
        ArrayList<BigDecimal[]> valueList = new ArrayList<BigDecimal[]>();//放每分钟每个频点对应的几个值
        Map<Integer,int[]> minuteMap = getMinuteMap(fileNameSplits);//每分钟频点对应的占用率

        StringBuilder sb = new StringBuilder();//拼接字符串
        Map<Float, Integer> noizeMap = null;//对应的噪点
        for (Text value : values) {//每一条数据的处理
            String[] frequency_point_array = value.toString().split(" ");
            List<Float> pointerList = new ArrayList<Float>();
            for (String pointer : frequency_point_array) {
                pointerList.add(Float.valueOf(pointer));
            }
            if(i==0){
                float[] noiseLine = getNoiseLine(pointerList);
                noizeMap = getLine(pointerList, noiseLine[0], noiseLine[1], startHz, endHz, step);
            }
            getPointerList(startHz, step, minuteMap, noizeMap, pointerList);
            for (int[] ints : minuteMap.values()) {
                BigDecimal[] bigArr = new BigDecimal[4];
                bigArr[0] = new BigDecimal(ints[0]).divide(sumBig).setScale(2,BigDecimal.ROUND_HALF_UP);
                bigArr[1] = new BigDecimal(ints[2]).divide(sumBig).setScale(2,BigDecimal.ROUND_HALF_UP);
                bigArr[2] = new BigDecimal(ints[3]).divide(sumBig).setScale(2,BigDecimal.ROUND_HALF_UP);
                bigArr[3] = new BigDecimal(ints[4]).divide(sumBig).setScale(2,BigDecimal.ROUND_HALF_UP);
                valueList.add(bigArr);
            }
            i++;
        }

        for (BigDecimal[] bigDecimals : valueList) {
            sb.append(bigDecimals[0].toString()+"_"+bigDecimals[1].toString()+"_"+bigDecimals[0].toString()+"_"+bigDecimals[1].toString()).append(" ");
        }
        text.set(sb.substring(0,sb.length()));
        context.write(key,text);
    }

    /**
     *
     * @param startHz
     * @param step
     * @param minuteMap
     * @param noizeMap
     * @param pointerList
     */
    private void getPointerList(float startHz, float step, Map<Integer, int[]> minuteMap, Map<Float, Integer> noizeMap, List<Float> pointerList) {
        for (int j = 0; j < pointerList.size(); j++) {
            float currentHz = startHz+j*step;
            float v = (Float.valueOf(pointerList.get(j)) / 10.0f - Constant.FIELD_STRENGTH_CONSTANT-noizeMap.get(currentHz));
            minuteMap.get(j)[0] += v>= Constant.THRESHOLD_5 ? 1:0;
            minuteMap.get(j)[1] += v>= Constant.THRESHOLD_10 ? 1:0;
            minuteMap.get(j)[2] += v>= Constant.THRESHOLD_20 ? 1:0;
            minuteMap.get(j)[3] += v>= Constant.THRESHOLD_30 ? 1:0;
        }
    }

    /**
     * 获取每分钟总条数
     * @param values
     * @return
     */
    public static int getCount(Iterable<Text> values){
        int sum=0;
        for (Text value : values) {
            sum++;
        }
        return sum;
    }

    /**
     * 获取装每个频率四个门限值的map
     * @param fileNameSplits
     * @return
     */
    public static Map<Integer,int[]> getMinuteMap(String[] fileNameSplits){
        Map<Integer,int[]> minuteMap = new HashMap<Integer,int[]>();//每分钟频点对应的占用率
        for (int j = 0; j < fileNameSplits.length; j++) {
            minuteMap.put(j,new int[4]);
        }
        return minuteMap;
    }

    public static float[] getNoiseLine(List<Float> frequencyList){

        // -10 -20 -30 -40 -50 -60 -70 -80 -90 -100 -110
        //10组list
        Map<String, ArrayList<Float>> listMap = new HashMap<String, ArrayList<Float>>();
        for (int i = 1; i <= 10; i++) {
            listMap.put("list"+i,new ArrayList<Float>());
        }
        for (Float element : frequencyList) {
            if (-110 <= element && element < -100) {
                listMap.get("list10").add(element);
                continue;
            } else if (-100 <= element && element < -90) {
                listMap.get("list9").add(element);
                continue;
            } else if (-90 <= element && element < -80) {
                listMap.get("list8").add(element);
                continue;
            } else if (-80 <= element && element < -70) {
                listMap.get("list7").add(element);
                continue;
            } else if (-70 <= element && element < -60) {
                listMap.get("list6").add(element);
                continue;
            } else if (-60 <= element && element < -50) {
                listMap.get("list5").add(element);
                continue;
            } else if (-50 <= element && element < -40) {
                listMap.get("list4").add(element);
                continue;
            } else if (-40 <= element && element < -30) {
                listMap.get("list3").add(element);
                continue;
            } else if (-30 <= element && element < -20) {
                listMap.get("list2").add(element);
                continue;
            } else if (-20 <= element && element < -10) {
                listMap.get("list1").add(element);
                continue;
            }
        }
        List<Integer> sortList = new ArrayList<Integer>();
        for (int i = 1; i <= listMap.size(); i++) {
            Collections.sort(listMap.get("list"+i));
            sortList.add(listMap.get("list"+i).size());
        }
        Collections.sort(sortList);
        float maxfrequency=0f;
        float minfrequency=0f;
        for (int i = 1; i < listMap.size(); i++) {
            if(listMap.get("list"+i).size()==sortList.get(sortList.size()-1)){
                String maxList = ("list"+i);
                minfrequency= listMap.get(maxList).get(0);
                maxfrequency= listMap.get(maxList).get(listMap.get(maxList).size()-1);
                break;
            }
        }
        return new float[]{minfrequency,maxfrequency};
    }

    /**
     * 获取噪点
     * @param frequencyList 每分钟第一帧的数据
     * @param lowLine 计算出来的最低值
     * @param maxLine  计算出来的最高值
     * @param startHz   开始频率
     * @param endHz     结束频率
     * @param step      步进
     */

    public static Map<Float,Integer> getLine(List<Float> frequencyList,float lowLine,float maxLine,float startHz,float endHz,float step){
        int everySize = frequencyList.size()/ Constant.COPIES;
        ArrayList<ArrayList<float[]>> totalList = new ArrayList<ArrayList<float[]>>();
        for (int i = 0; i < Constant.COPIES; i++) {
            totalList.add(new ArrayList<float[]>());
        }
        int j=0;
        for (int i = 0; i <= frequencyList.size()-1; i++) {
            float h = startHz+step*i;
            if(i%everySize!=0){

                totalList.get(j).add(new float[]{h,frequencyList.get(i)});
            }else{
                if (i!=0 && (frequencyList.size()-1-i)>=everySize){
                    j++;
                }
                totalList.get(j).add(new float[]{h,frequencyList.get(i)});
            }
        }

        //float[] minPointerArray = new float[Constant.COPIES];
        ArrayList<float[]> tenPointer = new ArrayList<float[]>();
        tenPointer.add(new float[]{startHz,frequencyList.get(0)});//将第一点放入统计list中
        int m=0;
        for (ArrayList<float[]> floats : totalList) {
            Collections.sort(floats, new Comparator<float[]>() {
                public int compare(float[] o1, float[] o2) {
                    if (o1[1]>o2[1])return 1;
                    return -1;
                }
            });
            float[] minPointer = floats.get(0);  //获取每个统计list中最低的坐标点
            if(minPointer[1]<lowLine){
                tenPointer.add(new float[]{minPointer[0],lowLine});
            }else{
                if(minPointer[1] < maxLine){
                    tenPointer.add(minPointer);
                }else{
                    tenPointer.add(new float[]{minPointer[0],maxLine});
                }
            }
            m++;
        }
        tenPointer.add(new float[]{endHz,frequencyList.get(frequencyList.size()-1)});//将最后一个点放入统计list中

        //获取每个频率点对应的噪点
        Map<Float,Integer> noizePointer = new HashMap<Float,Integer>();
        float currentHz=startHz;//当前频率
        for (int i = 1; i <= tenPointer.size()-1; i++) {
            if(i==11){

                System.out.printf("");
            }
            float[] m1 = tenPointer.get(i-1);
            float[] m2 = tenPointer.get(i);
            try {
                float n = Float.parseFloat(Constant.format2.format((m2[1]-m1[1])/(m2[0]-m1[0])));
                float t = Float.parseFloat(Constant.format2.format(m2[1]-m2[0]*n));


                while (currentHz<=tenPointer.get(i)[0]) {
                    noizePointer.put(currentHz, Math.round(currentHz * n + t));
                    currentHz = Float.parseFloat(Constant.format3.format(currentHz+step));
                }
            } catch (NumberFormatException e) {
                System.out.printf("i:"+i);
                e.printStackTrace();
            }
        }
        return noizePointer;

    }
}
