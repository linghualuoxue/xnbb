package com.bj.xnbb.bootstrap;

import com.bj.xnbb.mapper.HourMap;
import com.bj.xnbb.mapper.MinuteMap;
import com.bj.xnbb.reducer.HourReducer;
import com.bj.xnbb.reducer.MinuteReducer;
import com.bj.xnbb.util.HdfsUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;

/**
 * Created by XNBB on 2017/6/20.
 */
public class MinuteMain {
    public static void main(String[] args)throws Exception{

        if (args==null || args.length<3)return;//如果参数小于3则不执行代码
        HdfsUtils.deleteFolder(new File(args[1]));
        HdfsUtils.deleteFolder(new File(args[2]));
        HdfsUtils.deleteFolder(new File(args[1]));
        HdfsUtils.deleteFolder(new File(args[2]));
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(MinuteMain.class);
        job.setJobName("minute_analysis");

        job.setMapperClass(MinuteMap.class);
        job.setReducerClass(MinuteReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        Job job2 = Job.getInstance(conf);
        job2.setJarByClass(MinuteMain.class);
        job2.setMapperClass(HourMap.class);
        job2.setReducerClass(HourReducer.class);

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(Text.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job2,new Path(args[1]));
        FileOutputFormat.setOutputPath(job2,new Path(args[2]));

        job.waitForCompletion(true);
        job2.waitForCompletion(true);
    }
}
