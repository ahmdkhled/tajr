package com.greyeg.tajr.helper;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCalculator {

    private static TimeCalculator timeCalculator;
    private static Timer timer;
    private static boolean running=false;
    private Context context;
    private static long seconds;
    private static final String TIME_KEY="pm_time";



    public static TimeCalculator getInstance(Context context) {

        return timeCalculator==null?new TimeCalculator(context):timeCalculator;
    }


    private TimeCalculator(Context context) {
        this.context=context;
    }


    public  void startTimer(){
        if (running) return;
        timer=new Timer();
        Log.d("TIMERCALCC", "timer started: ");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds=SharedHelper.getLongValue(context,TIME_KEY)+1;
                saveTime(seconds);
                Log.d("TIMERCALCC", "time "+seconds);

            }
        },0,1000);

        running=true;

    }

    public  void stopTimer(){
        Log.d("TIMERCALCC", "timer stopped: ");
        if (timer!=null){
            timer.cancel();
            timer.purge();
            timer=null;
        }
        running=false;
    }

    public boolean isRunning() {
        return running;
    }

    private void saveTime(long seconds){
        SharedHelper.putKey(context,TIME_KEY,seconds);
    }

    public long getWorkTime(){
        return SharedHelper.getLongValue(context,TIME_KEY);
    }
    public void clearWorkTime(){
         SharedHelper.putKey(context,TIME_KEY,0);
    }

}
