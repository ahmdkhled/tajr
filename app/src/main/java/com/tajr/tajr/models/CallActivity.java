package com.tajr.tajr.models;

public class CallActivity {

    private String history_line;
    private String call_stamp;
    private String duration;


    public CallActivity(String history_line, String call_stamp, String duration) {
        this.history_line = history_line;
        this.call_stamp = call_stamp;
        this.duration = duration;
    }

    public CallActivity() {
    }

    public String getHistory_line() {
        return history_line;
    }

    public void setHistory_line(String history_line) {
        this.history_line = history_line;
    }

    public String getCall_stamp() {
        return call_stamp;
    }

    public void setCall_stamp(String call_stamp) {
        this.call_stamp = call_stamp;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


}
