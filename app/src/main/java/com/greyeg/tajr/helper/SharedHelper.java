package com.greyeg.tajr.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    private static void init(Context context){
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void putKey(Context context, String Key, String Value) {
        init(context);
        editor.putString(Key, Value);
        editor.commit();

    }

    public static void putKey(Context context, String Key, long Value) {
        init(context);
        editor.putLong(Key, Value);
        editor.apply();

    }

    public static void putKey(Context context, String Key, boolean Value) {
        init(context);
        editor.putBoolean(Key, Value);
        editor.apply();

    }

    public static String getKey(Context contextGetKey, String Key) {
        sharedPreferences = contextGetKey.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        String Value = sharedPreferences.getString(Key, "");
        return Value;

    }
    public static long getLongValue(Context context, String Key) {
        init(context);
        return sharedPreferences.getLong(Key, 0);

    }

    public static boolean getBooleanValue(Context context, String Key) {
        init(context);
        return sharedPreferences.getBoolean(Key, true);

    }


    public static void clearSharedPreferences(Context context)
    {
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

    }


}
