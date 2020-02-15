package com.greyeg.tajr.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.greyeg.tajr.R;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.jobs.ReminderUtilities;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_HOVER_PERMISSION = 1000;

    private boolean mPermissionsRequested = false;


    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ReminderUtilities.scheduleOrderReminder(getApplicationContext());
        Log.d("OVERLAYYY", "splash onCreate: ");
//        startService(new Intent(this, FloatLayout.class));

//        Intent startHoverIntent = new Intent(SplashActivity.this, MissedCallNoOrderService.class);
//        startService(startHoverIntent);
        // On Android M and above we need to ask the user for permission to display the Hover
        // menu within the "alert window" layer.  Use OverlayPermission to check for the permission
        // and to request it.
//        if (!mPermissionsRequested && !OverlayPermission.hasRuntimePermissionToDrawOverlay(this)) {
//            @SuppressWarnings("NewApi")
//            Intent myIntent = OverlayPermission.createIntentToRequestOverlayPermission(this);
//            startActivityForResult(myIntent, REQUEST_CODE_HOVER_PERMISSION);
//        }


        if (SessionManager.getInstance(getApplicationContext()).getIsLogin().equals("yes")){
           goTo(MainActivity.class);
        }else {
            goTo(LoginActivity.class);
        }
    }

    private void goTo(Class<?> tClass){

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setLocale(SharedHelper.getKey(getApplicationContext(),"lang"));
                Intent intent = new Intent(getApplicationContext(),tClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        },1000);

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_HOVER_PERMISSION == requestCode) {
            mPermissionsRequested = true;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
