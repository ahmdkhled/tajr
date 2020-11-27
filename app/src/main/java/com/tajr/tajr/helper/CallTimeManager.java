package com.tajr.tajr.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tajr.tajr.models.CallActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CallTimeManager {

    private static CallTimeManager callTimeManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String CALLS_KEY="calls_key";

    public static CallTimeManager getInstance(Context context) {
        return callTimeManager==null?callTimeManager=new CallTimeManager(context):callTimeManager;
    }

    private CallTimeManager(Context context) {
        sharedPreferences=context.getSharedPreferences("CALL_LOG",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void saveCallSession(String duration,String time){

        CallActivity callActivity=new CallActivity();
        callActivity.setDuration(duration);

        callActivity.setCall_stamp(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(time))));

        ArrayList<CallActivity> callActivities=getCallActivity();
        callActivities.add(callActivity);
        Gson gson=new Gson();
        String json=gson.toJson(callActivities,new TypeToken<ArrayList<CallActivity>>(){}.getType());

        editor.putString(CALLS_KEY,json);
        editor.apply();
    }

    public ArrayList<CallActivity> getCallActivity(){
        Gson gson=new Gson();
        String callsString=sharedPreferences.getString(CALLS_KEY,"");
        ArrayList<CallActivity> callActivity= gson.fromJson(callsString,new TypeToken<ArrayList<CallActivity>>(){}.getType());
        return callActivity==null?new ArrayList<CallActivity>():callActivity;
    }

    public void emptyCallsHistory(){
        editor.putString(CALLS_KEY,"");
        editor.apply();
    }


}
