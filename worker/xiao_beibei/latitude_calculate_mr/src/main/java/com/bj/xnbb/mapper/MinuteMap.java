package com.bj.xnbb.mapper;

import com.bj.xnbb.common.Constant;
import com.bj.xnbb.domain.MinuteMapKey;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * Created by XNBB on 2017/6/20.
 */
public class MinuteMap extends Mapper<LongWritable,Text,Text,Text>{
    Text key = new Text();
    Text v = new Text();
    MinuteMapKey k = new MinuteMapKey();
    //21010000_0014_20160930_090037_930MHz_934MHz_25.00kHz_V_M
    @Override
    protected void map(LongWritable lw, Text value, Context context) throws IOException, InterruptedException {
        String path = ((FileSplit)context.getInputSplit()).getPath().getName();
        String[] valueSplit = value.toString().split(Constant.SEPARATOR);
        String[] fileNameSplit = path.split(Constant.FILE_NAME_SPLIT);

        k.setMonitor_station(fileNameSplit[0]);
        k.setMontor_no(fileNameSplit[1]);
        k.setTime(valueSplit[0].substring(0,12));
        k.setStartHz(fileNameSplit[4].substring(0,fileNameSplit[4].lastIndexOf("M")));
        k.setEndHz(fileNameSplit[5].substring(0,fileNameSplit[5].lastIndexOf("M")));

        //String s = String.valueOf((int)Float.parseFloat(fileNameSplit[6].substring(0,fileNameSplit[6].lastIndexOf("k"))));
        k.setStep(String.valueOf((int)Float.parseFloat(fileNameSplit[6].substring(0,fileNameSplit[6].lastIndexOf("k")))));
        k.setPolarization(fileNameSplit[7]);
        k.setSign(fileNameSplit[8]);
        key.set(k.toString());
        v.set(valueSplit[valueSplit.length-1]);
        context.write(key,v);

    }
}
