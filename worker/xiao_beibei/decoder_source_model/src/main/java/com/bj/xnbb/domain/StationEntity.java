package com.bj.xnbb.domain;

/**移动站的实体类
 * Created by XNBB on 2017/6/14.
 */
public class StationEntity {

    private String fileName;//文件名称
    private String title;//记录头
    private String seria;//编号
    private String yarn;//年
    private String month;//月
    private String day;//日
    private String hour;//时
    private String minute;//分钟
    private String second;//秒
    private String millisecond;//毫秒
    private String speed;
    private double longitude;//精度
    private double dimension;//纬度
    private int hanging;//挂高
    private int startkHz;//开始频率
    private int step;//步进
    private int frequencyCount;//频率点数
    private String frequency;//频率



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeria() {
        return seria;
    }

    public void setSeria(String seria) {
        this.seria = seria;
    }

    public String getYarn() {
        return yarn;
    }

    public void setYarn(String yarn) {
        this.yarn = yarn;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(String millisecond) {
        this.millisecond = millisecond;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDimension() {
        return dimension;
    }

    public void setDimension(double dimension) {
        this.dimension = dimension;
    }

    public int getHanging() {
        return hanging;
    }

    public void setHanging(int hanging) {
        this.hanging = hanging;
    }

    public int getStartkHz() {
        return startkHz;
    }

    public void setStartkHz(int startkHz) {
        this.startkHz = startkHz;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getFrequencyCount() {
        return frequencyCount;
    }

    public void setFrequencyCount(int frequencyCount) {
        this.frequencyCount = frequencyCount;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return
              //  "\001"+title+
              //  "\001"+seria+
                yarn+month+day+hour+minute+second+millisecond+
                "\001"+(speed==null?"speed":speed)+
                "\001"+longitude+
                "\001"+dimension+
                "\001"+hanging+
                // "\001"+startkHz+
                // "\001"+step+
                "\001"+frequencyCount+
                "\001"+frequency;
    }

}
