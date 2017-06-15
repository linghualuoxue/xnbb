package com.bj.xnbb.bootstrap;
import com.bj.xnbb.common.*;
import com.bj.xnbb.handler.*;
import com.bj.xnbb.util.DateUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * Created by XNBB on 2017/6/14.
 */
public class BootStrap {
    public static void main(String[] args)throws Exception {

        InputStream is = BootStrap.class.getClassLoader().getResourceAsStream("config.properties");
        Properties pp = new Properties();
        pp.load(is);
        ExecutorService threadPool =  Executors.newFixedThreadPool(Integer.valueOf(pp.getProperty(CollectorConstant.CORE_ZISE)));

        /**
         * parseFile 处理文件
         */
        if(args!=null){
            for (String arg : args) {
              File file =   new File(arg);
              if(file.exists()){
                   if(file.isDirectory()){
                        //文件夹的处理
                        String targetDirector = file.getAbsolutePath()
                                + CollectorConstant.POSTFIX
                                + DateUtils.getTimestamp();

                        File tFile = new File(targetDirector);
                        if(!tFile.exists()){
                            tFile.mkdirs();
                        }
                        String[] files = file.list();
                       for (String fileName : files) {
                           //目标文件
                          String targetFile =  targetDirector+File.separator+
                           fileName.substring(0,fileName.indexOf(CollectorConstant.FILE_NAME_SPLIT
                           )-1);
                           //移动站的处理
                           if(fileName.contains(CollectorConstant.MOBILE_STATION)){
                               threadPool.execute(new ParseMobileStationData(new File(fileName),targetFile));
                           }
                           //固定站的处理
                           if(fileName.contains(CollectorConstant.FIXATION_STATION)){
                               threadPool.execute(new ParseFixStationData(new File(fileName),targetFile));
                           }
                       }

                   }else{
                       //文件的处理
                     String fileName =   file.getName();
                     String absolutePath = file.getAbsolutePath();
                     //移动站的处理
                     if(fileName.contains(CollectorConstant.MOBILE_STATION)){
                           String newFile = absolutePath.substring(0,absolutePath.lastIndexOf(CollectorConstant.FILE_NAME_SPLIT)-1);
                           threadPool.execute(new ParseMobileStationData(file,newFile));
                     }
                     //固定站的处理
                     if(fileName.contains(CollectorConstant.FIXATION_STATION)){
                         String newFile = absolutePath.substring(0,absolutePath.lastIndexOf(CollectorConstant.FILE_NAME_SPLIT)-1);
                         threadPool.execute(new ParseFixStationData(new File(fileName),newFile));
                     }
                   }
                  // String path =    file.getAbsolutePath();

              }
            }
        }

    }
}
