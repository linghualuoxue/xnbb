package com.bj.xnbb.handler;

import com.bj.xnbb.common.CollectorConstant;
import com.bj.xnbb.util.*;
import com.bj.xnbb.domain.*;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**移动站的处理
 * Created by XNBB on 2017/6/14.
 */
public class ParseMobileStationData implements Runnable {

    static final Logger logger = Logger.getLogger(ParseMobileStationData.class);

    static DecimalFormat df   =new  java.text.DecimalFormat("#.000000");//精度格式

    private File file;

    private String targetFielName;

    public ParseMobileStationData(File file,String tartgetFileName) {
        this.targetFielName = tartgetFileName;
        this.file = file;
    }

    public void run() {
        try {
            if(file.exists()){
                this.handler(file);
            }

        } catch (Exception e) {
            logger.error("解析失败,失败文件是："+file.getName());
        }

    }

    public void handler(File file)throws Exception{
        logger.info("开始解析文件:"+file.getName());

        FileInputStream stream = null;
        List<StationEntity> entityList = null;
        try {
            stream = new FileInputStream(file);
            entityList = parseData(file, stream);
        } catch (IOException e) {
           logger.error("解析文件失败，文件："+file.getName(),e.getCause());
        } finally {
            if(stream!=null)stream.close();
        }
        writeToTargetFIle(file, entityList);
    }

    /**
     * 向目标文件写数据
     * @param entityList
     * @throws IOException
     */
    private void writeToTargetFIle(File file, List<StationEntity> entityList) throws IOException {
//        String fileName = file.getName();
//        String newFileName = targetFielName+File.separator
//                +fileName.substring(0,fileName.lastIndexOf(CollectorConstant.FILE_NAME_SPLIT)-1)
//                +CollectorConstant.POSTFIX;

        File outFile = new File(targetFielName);
        if(!outFile.exists()){
            outFile.createNewFile();
        }else{
            //删除文件
            outFile.delete();
            outFile.createNewFile();
        }

        BufferedWriter bufferredWriter = null;
        try {
            bufferredWriter = new BufferedWriter(new FileWriter(outFile,true));

            for (StationEntity entity : entityList) {
                bufferredWriter.write(entity.toString());
                bufferredWriter.newLine();
                bufferredWriter.flush();
            }
        } catch (IOException e) {
            logger.error("写文件失败,文件："+outFile,e.getCause());
        } finally {
            if(bufferredWriter!=null){
                bufferredWriter.close();
            }
        }
    }

    /**
     * 解析读入的文件
     * @param file
     * @param stream
     * @return
     * @throws IOException
     */
    private List<StationEntity> parseData(File file, FileInputStream stream) throws IOException {
        List<StationEntity> entityList = new ArrayList<StationEntity>();

        String fileName =  file.getName();
        String[] lines = fileName.split(CollectorConstant.POSTFIX);

        int startHz = Integer.valueOf(lines[4].substring(0,lines[4].indexOf("M")));
        int step = Integer.valueOf(lines[6].substring(0, lines[6].indexOf(".")));

        while(stream.available()>0){

            StationEntity entity = new StationEntity();
            entity.setFileName(file.getName());

            int len = 4;//读几个字节
            byte[] b = new byte[len];
            stream.read(b);
            String title = BytesConversionUtils.bytesToHexString(b);
            entity.setTitle(title);

            len = 2;//读几个字节
            b = new byte[len];
            stream.read(b);
            int seria = BytesConversionUtils.getShort(b,0);
            System.out.printf("编号:"+seria+"\n");
            entity.setSeria(String.valueOf(seria));

            stream.skip(1); //跳过之前的字节数

            len = 2;//读几个字节
            b = new byte[len];
            stream.read(b);
            int yarn = BytesConversionUtils.byte2int(b);
            System.out.printf("年:"+yarn+"\n");
            entity.setYarn(String.valueOf(yarn));

            len=1;
            b = new byte[len];
            stream.read(b);
            int month = BytesConversionUtils.getShortOne(b,0);
            System.out.printf("月:"+month+"\n");
            entity.setMonth(DateUtils.parseSingleTime(month));

            len=1;
            b = new byte[len];
            stream.read(b);
            int day = BytesConversionUtils.getShortOne(b,0);
            System.out.printf("日:"+day+"\n");
            entity.setDay(DateUtils.parseSingleTime(day));


            len=1;
            b = new byte[len];
            stream.read(b);
            int hour = BytesConversionUtils.getShortOne(b,0);
            System.out.printf("时:"+hour+"\n");
            entity.setHour(DateUtils.parseSingleTime(hour));

            len=1;
            b = new byte[len];
            stream.read(b);
            int minute = BytesConversionUtils.getShortOne(b,0);
            System.out.printf("分:"+minute+"\n");
            entity.setMinute(DateUtils.parseSingleTime(minute));

            len=1;
            b = new byte[len];
            stream.read(b);
            int second = BytesConversionUtils.getShortOne(b,0);
            System.out.printf("秒:"+second+"\n");
            entity.setSecond(DateUtils.parseSingleTime(second));

            len=2;
            b = new byte[len];
            stream.read(b);
            int millisecond = BytesConversionUtils.getShort(b,0);
            System.out.printf("毫秒:"+millisecond+"\n");
            entity.setMillisecond(DateUtils.parseSingleTime(millisecond));

            len=8;
            b = new byte[len];
            stream.read(b);
            double longitude = BytesConversionUtils.getDouble(b,0);
            System.out.printf("精度:"+df.format(longitude)+" 度\n");
            entity.setLongitude(longitude);

            len=8;
            b = new byte[len];
            stream.read(b);
            double dimension = BytesConversionUtils.getDouble(b,0);
            System.out.printf("维度:"+df.format(dimension)+" 度\n");
            entity.setDimension(dimension);

            len=4;
            b = new byte[len];
            stream.read(b);
            int hanging = BytesConversionUtils.byte2int4(b);
            System.out.printf("挂高:"+hanging+" m\n");
            entity.setHanging(hanging);

//            len=8;
//            b = new byte[len];
//            stream.read(b);
//            long startkHz = BytesConversionUtils.getLong8(b,0);
//            System.out.printf("开始频率:"+startkHz/1000000+" MHz\n");
//            entity.setStartkHz(startkHz/1000000);

            stream.skip(8);
            entity.setStartkHz(startHz);

//            len=4;
//            b = new byte[len];
//            stream.read(b);
//            float step = BytesConversionUtils.getFloat(b);
//            System.out.printf("步进:"+step/1000+" kHz\n");
//            entity.setStep(step/1000);

            stream.skip(4);
            entity.setStep(step);

            len=4;
            b = new byte[len];
            stream.read(b);
            int frequencyCount = BytesConversionUtils.byte2int4(b);
            System.out.printf("频率点数:"+frequencyCount+"\n");
            entity.setFrequencyCount(frequencyCount);

            StringBuilder sb = new StringBuilder();
            for ( int i = 0; i < frequencyCount; i++) {
                b = new byte[2];
                stream.read(b);
                int frequency = BytesConversionUtils.getShort(b,0);
                sb.append(frequency).append(" ");
                //System.out.printf("帧："+frequency);
            }
            entity.setFrequency(sb.substring(0,sb.length()-1));
           logger.info("文件解析完成,内容为："+entity.toString());
            entityList.add(entity);
    }
        return entityList;
    }
}
