package com.greyeg.tajr;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.multidex.MultiDex;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.greyeg.tajr.helper.SharedHelper;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {

    private Locale locale = null;
    private static Context mContext;



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
            Configuration config = new Configuration(newConfig);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;



        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("gemy/Changa-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Fresco.initialize(this);



        if (SharedHelper.getKey(getApplicationContext(), "lang").equals("en")) {

            setLocale("en");

        }else if (SharedHelper.getKey(getApplicationContext(), "lang").equals("ar")) {

            setLocale("ar");

        }

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(this, LoginActivity.class);
//        startActivity(refresh);
//        finish();
    }




    public static Context getContext() {
        return mContext;
    }
}
