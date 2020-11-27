package com.tajr.tajr.helper;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenHelper {

    private static final String WIDTH="width";
    private static final String HEIGHT="height";

    public static void saveScreenDimensions(Context context, Activity activity){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        SharedHelper.putKey(context,WIDTH,String.valueOf(displaymetrics.widthPixels));
        SharedHelper.putKey(context,HEIGHT,String.valueOf(displaymetrics.heightPixels));

        //Log.d("SCREEEEEENw", "screen: "+displaymetrics.widthPixels+" => "+displaymetrics.heightPixels);
    }

    public static int[] getScreenDimensions(Context context){
        int[] dimensions=new int[2];
        dimensions[0]=Integer.valueOf(SharedHelper.getKey(context,WIDTH));
        dimensions[1]=Integer.valueOf(SharedHelper.getKey(context,HEIGHT));
        return dimensions;
    }

}
