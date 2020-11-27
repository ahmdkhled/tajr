package com.tajr.tajr.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tajr.tajr.models.Activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TimeCalculator {

    private static TimeCalculator timeCalculator;
    private static Timer timer;
    private static boolean running = false;
    private Context context;
    private static long seconds;
    private static final String APP_KEY = "app_time";
    private static final String ACTIVITY_KEY = "activity_time";
    public static final String PM = "PM";
    public static final String APP = "APP";
    public static final String CALCULATING = "calculating";
    public static final String READY_TO_UPLOAD = "ready_to_upload";
    public static final String UPLOADING = "uploading";
    public static final String FAILED_TO_UPLOAD = "failed_to_upload";


    public static TimeCalculator getInstance(Context context) {

        return timeCalculator == null ? new TimeCalculator(context) : timeCalculator;
    }


    private TimeCalculator(Context context) {
        this.context = context;
    }


    public void startTimer(Activity activity) {
        if (running) return;
        timer = new Timer();
        Log.d("TIMERCALCC", "timer started: ");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                activity.setValue(seconds);
                updateActivity(activity);
                Log.d("TIMERCALCCzz", "time " + activity.getValue());

            }
        }, 0, 1000);

        running = true;

    }

    public void stopTimer() {
        Log.d("TIMERCALCCzz", "timer stopped: ");
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        running = false;
        seconds = 0;

    }

    public boolean isRunning() {
        return running;
    }

    public void setStatus(String source, String status) {
        ArrayList<Activity> activityList = getWorkTimeActivity();
        Log.d("TIMERCALCCzz", "setStatus: count " + activityList.size());
        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i).getType().equals(source)
                    && activityList.get(i).getStatus().equals(CALCULATING)) {

                activityList.get(i).setStatus(status);
                Log.d("TIMERCALCCzz", "setStatus: " + activityList.get(i));
            }
        }
        Gson gson = new Gson();
        String jsonStr = gson.toJson(activityList, new TypeToken<ArrayList<Activity>>() {
        }.getType());
        SharedHelper.putKey(context, ACTIVITY_KEY, jsonStr);
    }


    public ArrayList<Activity> getWorkTimeActivity() {
        Gson gson = new Gson();
        String activityString = SharedHelper.getKey(context, ACTIVITY_KEY);
        ArrayList<Activity> activity =
                gson.fromJson(activityString, new TypeToken<ArrayList<Activity>>() {
                }.getType());
        return activity == null ? new ArrayList<Activity>() : activity;
    }


    public void updateActivity(Activity activity) {
        ArrayList<Activity> activityList = getWorkTimeActivity();
        int index = activityList.indexOf(activity);
        if (index == -1) {
            activityList.add(activity);
        } else {
            activityList.set(index, activity);

        }

        Gson gson = new Gson();
        String jsonStr = gson.toJson(activityList, new TypeToken<ArrayList<Activity>>() {
        }.getType());
        SharedHelper.putKey(context, ACTIVITY_KEY, jsonStr);

        Log.d("TIMERCALCCzz", index + " updateActivity: " + getWorkTimeActivity().get(0).getValue());


    }

    public ArrayList<Activity> getIdleWorkTime() {
        ArrayList<Activity> activityList = getWorkTimeActivity();
        ArrayList<Activity> idleActivityList = new ArrayList<>();
        for (int i = 0; i < activityList.size(); i++) {
            if (!activityList.get(i).getStatus().equals(CALCULATING)) {
                idleActivityList.add(activityList.get(i));
            }
        }
        return idleActivityList;
    }

    public void clear(ArrayList<Activity> uploadedActivity) {
        ArrayList<Activity> activityList = getWorkTimeActivity();
        activityList.removeAll(uploadedActivity);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(activityList, new TypeToken<ArrayList<Activity>>() {
        }.getType());
        SharedHelper.putKey(context, ACTIVITY_KEY, jsonStr);
    }


    public void clear() {
        SharedHelper.putKey(context,ACTIVITY_KEY,"");

    }
}