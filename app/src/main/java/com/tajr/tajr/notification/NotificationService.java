package com.tajr.tajr.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.tajr.tajr.R;

public class NotificationService extends BroadcastReceiver {

    private static final int NOTI_PRIMARY1 = 1100;
    private static final int NOTI_PRIMARY2 = 1101;
    private static final int NOTI_SECONDARY1 = 1200;
    private static final int NOTI_SECONDARY2 = 1201;

    private NotificationHelper noti;

    Context context;
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "noo", Toast.LENGTH_SHORT).show();


        Log.d("yyyyyyyyyyyyyy", "onReceive: ");
//        this.context = context;
//        noti = new NotificationHelper(context);
//        sendNotification(NOTI_SECONDARY1,"elam faisal");
//
//        if (isOnline(context)){
//            Toast.makeText(context, "conected", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(context, "noo", Toast.LENGTH_SHORT).show();
//        }
    }
    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send activity notifications.
     *
     * @param id The ID of the notification to create
     * @param title The title of the notification
     */
    public void sendNotification(int id, String title) {
        Notification.Builder nb = null;
        nb = noti.getNotification2(title, context.getString(R.string.work));
        noti.notify(id, nb);

    }


}
