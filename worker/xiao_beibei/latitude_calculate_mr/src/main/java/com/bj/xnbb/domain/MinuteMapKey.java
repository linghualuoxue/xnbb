package com.bj.xnbb.domain;

import com.bj.xnbb.common.Constant;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**21010000_0014_20160930_090037_930MHz_934MHz_25.00kHz_V_M
 * Created by XNBB on 2017/6/20.
 */
public class MinuteMapKey implements Writable{
    private String monitor_station;
    private String montor_no;
    private String time;
    private String startHz;
    private String endHz;
    private String step;
    private String polarization; //极化方式
    private String sign;         //标志

    public void write(DataOutput out) throws IOException {
       out.writeUTF( this.monitor_station );
       out.writeUTF( this.montor_no);
       out.writeUTF( this.time);
       out.writeUTF( this.startHz);
       out.writeUTF( this.endHz);
       out.writeUTF( this.step);
       out.writeUTF( this.polarization);
       out.writeUTF( this.sign);
    }
    public void readFields(DataInput in) throws IOException {

      this.monitor_station= in.readUTF();
      this.montor_no = in.readUTF();
      this.time = in.readUTF();
      this.startHz = in.readUTF();
      this.endHz = in.readUTF();
      this.step = in.readUTF();
      this.polarization = in.readUTF();
      this.sign = in.readUTF();
    }

    public String getMonitor_station() {
        return monitor_station;
    }

    public void setMonitor_station(String monitor_station) {
        this.monitor_station = monitor_station;
    }

    public String getMontor_no() {
        return montor_no;
    }

    public void setMontor_no(String montor_no) {
        this.montor_no = montor_no;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartHz() {
        return startHz;
    }

    public void setStartHz(String startHz) {
        this.startHz = startHz;
    }

    public String getEndHz() {
        return endHz;
    }

    public void setEndHz(String endHz) {
        this.endHz = endHz;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getPolarization() {
        return polarization;
    }

    public void setPolarization(String polarization) {
        this.polarization = polarization;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
       return  this.monitor_station
        +Constant.FILE_NAME_SPLIT+ this.montor_no
        +Constant.FILE_NAME_SPLIT+ this.time
        +Constant.FILE_NAME_SPLIT+ this.startHz
        +Constant.FILE_NAME_SPLIT+ this.endHz
        +Constant.FILE_NAME_SPLIT+ this.step
        +Constant.FILE_NAME_SPLIT+ this.polarization
        +Constant.FILE_NAME_SPLIT+ this.sign;
    }
}
