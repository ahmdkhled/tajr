package com.tajr.tajr.records;

/**
 * Created by VS00481543 on 01-11-2017.
 */

public class CallDetails {

    private int serial;
    private String num;
    private String time;
    private String date;
    private String uploaded;
    private String duration;

    public CallDetails(){

    }

    public CallDetails(int serial, String num, String time, String date)
    {
        this.serial=serial;
        this.num=num;
        //this.name=name;
        this.time=time;
        this.date=date;
    }

    public CallDetails(int serial, String num, String time, String date, String uploaded) {
        this.serial = serial;
        this.num = num;
        this.uploaded = uploaded;
        this.time = time;
        this.date = date;
    }

    public CallDetails(int serial, String num, String time, String date, String uploaded, String duration) {
        this.serial = serial;
        this.num = num;
        this.time = time;
        this.date = date;
        this.uploaded = uploaded;
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getSerial()
    {
        return serial;
    }

    public void setSerial(int serial)
    {
        this.serial=serial;
    }

    public String getNum()
    {
        return num;
    }

    public void setNum(String num)
    {
        this.num=num;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /* public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }*/

    public String getTime1()
    {
        return time;
    }

    public void setTime1(String time)
    {
        this.time=time;
    }

    public String getDate1()
    {
        return date;
    }

    public void setDate1(String date)
    {
        this.date=date;
    }
}
