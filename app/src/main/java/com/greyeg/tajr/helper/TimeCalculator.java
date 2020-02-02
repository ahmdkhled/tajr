package com.greyeg.tajr.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.greyeg.tajr.models.Activity;
import com.greyeg.tajr.models.CallActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TimeCalculator {

    private static TimeCalculator timeCalculator;
    private static Timer timer;
    private static boolean running=false;
    private Context context;
    private static long seconds;
    private static final String APP_KEY ="app_time";
    private static final String ACTIVITY_KEY ="activity_time";



    public static TimeCalculator getInstance(Context context) {

        return timeCalculator==null?new TimeCalculator(context):timeCalculator;
    }



    private TimeCalculator(Context context) {
        this.context=context;
    }


    public  void startTimer(Activity activity){
        if (running) return;
        timer=new Timer();
        Log.d("TIMERCALCC", "timer started: ");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                activity.setValue(seconds);
                updateActivity(activity);
                Log.d("TIMERCALCCzz", "time "+activity.getValue());

            }
        },0,1000);

        running=true;

    }

    public  void stopTimer(){
        Log.d("TIMERCALCCzz", "timer stopped: ");
        if (timer!=null){
            timer.cancel();
            timer.purge();
            timer=null;
        }
        running=false;
        seconds=0;
    }

    public boolean isRunning() {
        return running;
    }


    private void saveAppTime(long seconds){
        SharedHelper.putKey(context, APP_KEY,seconds);
    }

    public long getAppWorkTime(){
        return SharedHelper.getLongValue(context, APP_KEY);
    }

    public ArrayList<Activity> getWorkTimeActivity(){
        Gson gson=new Gson();
        String activityString=SharedHelper.getKey(context,ACTIVITY_KEY);
        ArrayList<Activity> activity=
                gson.fromJson(activityString,new TypeToken<ArrayList<Activity>>(){}.getType());
        return activity==null?new ArrayList<Activity>():activity;
    }


    public void updateActivity(Activity activity){
        ArrayList<Activity> activityList=getWorkTimeActivity();
        int index=activityList.indexOf(activity);
        if (index==-1){
            activityList.add(activity);
        }else{
            activityList.set(index,activity);

        }

        Gson gson=new Gson();
        String jsonStr=gson.toJson(activityList,new TypeToken<ArrayList<Activity>>(){}.getType());
        SharedHelper.putKey(context,ACTIVITY_KEY,jsonStr);

        Log.d("TIMERCALCCzz", index+" updateActivity: "+getWorkTimeActivity().get(0).getValue());


    }

    public void clear() {
        SharedHelper.putKey(context, ACTIVITY_KEY,"");
    }
}
