package com.tajr.tajr.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SubscriptionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.tajr.tajr.R;
import com.tajr.tajr.helper.SharedHelper;
import com.tajr.tajr.helper.font.RobotoTextView;
import com.rafakob.drawme.DrawMeRelativeLayout;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.sim_view)
    DrawMeRelativeLayout simView;
    @BindView(R.id.sim_num)
    TextView simNum;
    @BindView(R.id.lang_view)
    DrawMeRelativeLayout langView;
    @BindView(R.id.bubble_container)
    ConstraintLayout bubble_view;
    @BindView(R.id.lang_name)
    TextView langName;
    @BindView(R.id.bubble_switch)
    Switch bubble_switch;
    TextView autoNotifictionTv;
    RadioGroup radioGroup;
    RobotoTextView ok;
    View view;
    //
    String newStatus;
    String newStatusTag;
    RadioButton sim1;
    RadioButton sim2;
    Dialog dialog;
    RadioGroup radioGrouplang;
    RobotoTextView okLang;
    View viewLang;
    String newStatusLAng;
    String newStatusTagLang;
    RadioButton arabic;
    RadioButton english;
    Dialog dialogLang;
    TextView autoUpdateTv;
    private String carrierName;
    private String TAG = "ttttttttttt";
    public static final String BUBBLE_SETTING="bubble_settiing";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick(R.id.sim_view)
    public void shooseSimNumber() {

        if (SharedHelper.getKey(getApplicationContext(), "sub_num").equals("1")) {
            return;
        }
        dialog = new Dialog(this);
        //  view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        dialog.setContentView(R.layout.dialog_choose_sim);
        radioGroup = dialog.findViewById(R.id.radio_group);
        sim1 = dialog.findViewById(R.id.sim1);
        sim2 = dialog.findViewById(R.id.sim2);
        if (SharedHelper.getKey(getApplicationContext(), "activated_sub_num").equals("1")) {
            sim1.setChecked(true);
        } else if (SharedHelper.getKey(getApplicationContext(), "activated_sub_num").equals("2")) {
            sim2.setChecked(true);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList().size() == 0 || SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList().size() == 1) {
                simNum.setText(getString(R.string.default_sim_activ));
                return;
            }
            if (SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(0).getCarrierName() != null) {
                sim1.setText(getString(R.string.sim_1) + "  " + SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(0).getCarrierName());

            }
            if (SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(1).getCarrierName() != null) {
                sim2.setText(getString(R.string.sim_2) + "  " + SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(1).getCarrierName());

            }

        }
        ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton selectedRadioButton = dialog.findViewById(radioGroup.getCheckedRadioButtonId());
                newStatus = selectedRadioButton.getText().toString();
                newStatusTag = (String) selectedRadioButton.getTag();

                if (newStatusTag.equals("1")) {
                    simNum.setText(getString(R.string.sim_1) + "  " + SharedHelper.getKey(getApplicationContext(), "activated_sub_net_name"));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    String subIdForSim1 = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        subIdForSim1 = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(0).getIccId());
                        Log.d(TAG, "activated_sub_id: = " + subIdForSim1);
                        SharedHelper.putKey(getApplicationContext(), "activated_sub_num", "1");
                        String netName = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(0).getCarrierName());
                        SharedHelper.putKey(getApplicationContext(), "activated_sub_net_name", netName);
                    }

                } else {
                    simNum.setText(getString(R.string.sim_2) + "  " + SharedHelper.getKey(getApplicationContext(), "activated_sub_net_name"));
                    String subIdForSim2 = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        subIdForSim2 = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(1).getIccId());
                        String netName = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(1).getCarrierName());
                        SharedHelper.putKey(getApplicationContext(), "activated_sub_net_name", netName);
                        SharedHelper.putKey(getApplicationContext(), "activated_sub_id", subIdForSim2);
                        Log.d(TAG, "activated_sub_id: " + subIdForSim2);
                        SharedHelper.putKey(getApplicationContext(), "activated_sub_num", "2");
                    }
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @OnClick(R.id.lang_view)
    public void shooseLang() {
        dialogLang = new Dialog(this);
        //  view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        dialogLang.setContentView(R.layout.dialog_choose_lang);
        radioGrouplang = dialogLang.findViewById(R.id.radio_group);
        arabic = dialogLang.findViewById(R.id.arabic);
        english = dialogLang.findViewById(R.id.english);
        if (Locale.getDefault().getLanguage().equals("ar")) {
            arabic.setChecked(true);
        } else if (Locale.getDefault().getLanguage().equals("en")) {
            english.setChecked(true);
        }
        okLang = dialogLang.findViewById(R.id.ok);
        okLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton selectedRadioButton = dialogLang.findViewById(radioGrouplang.getCheckedRadioButtonId());
                newStatusLAng = selectedRadioButton.getText().toString();
                newStatusTagLang = (String) selectedRadioButton.getTag();
                dialogLang.dismiss();
                if (newStatusTagLang.equals("ar")) {
                    SharedHelper.putKey(getApplicationContext(), "lang", "ar");
                    setLocale("ar");


                } else {
                    SharedHelper.putKey(getApplicationContext(), "lang", "en");
                    setLocale("en");
                }


            }
        });

        dialogLang.show();
    }

    @OnClick({R.id.bubble_container})
    public void bubbleSetting(){

        bubble_switch.setChecked(!bubble_switch.isChecked());
    }

    @OnCheckedChanged(R.id.bubble_switch)
    public void bubbleSwitchStateChanged(){
        boolean state=SharedHelper.getBooleanValue(this,BUBBLE_SETTING);
        Log.d(TAG, "bubbleSetting: "+state);
        SharedHelper.putKey(this,BUBBLE_SETTING,!state);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        MainActivity.mainActivity.finish();
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();

    }

    private void autoUpdateTrue() {
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        pref1.edit().putBoolean("autoUpdate", true).apply();
        autoUpdateTv.setText(getString(R.string.True));
    }

    private void autoUpdateFalse() {
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        pref1.edit().putBoolean("autoUpdate", false).apply();
        autoUpdateTv.setText(getString(R.string.False));
    }

    private boolean getAutoValue() {
        SharedPreferences auto = PreferenceManager.getDefaultSharedPreferences(this);
        return auto.getBoolean("autoUpdate", false);
    }

    void setAutoUpdate() {
        autoUpdateTv = findViewById(R.id.calls_option);
        SharedPreferences auto = PreferenceManager.getDefaultSharedPreferences(this);

        if (auto.getBoolean("autoUpdate", false)) {
            autoUpdateTv.setText(getString(R.string.True));
        } else {
            autoUpdateTv.setText(getString(R.string.False));
        }

    }

    @OnClick(R.id.calls_view)
    void setAutoUpdateTv() {
        if (!getAutoValue()) {
            autoUpdateTrue();
        } else {
            autoUpdateFalse();
        }
    }

    //////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setAutoUpdate();
        setAutoNotifiction();
        if (SharedHelper.getKey(getApplicationContext(), "sub_num").equals("1")) {
            simNum.setText(getString(R.string.default_sim_activ));
            simNum.setClickable(false);
        } else {
            if (SharedHelper.getKey(getApplicationContext(), "activated_sub_num").equals("1")) {
                simNum.setText(getString(R.string.sim_1) + "  " + SharedHelper.getKey(getApplicationContext(), "activated_sub_net_name"));
            } else if (SharedHelper.getKey(getApplicationContext(), "activated_sub_num").equals("2")) {
                simNum.setText(getString(R.string.sim_2) + "  " + SharedHelper.getKey(getApplicationContext(), "activated_sub_net_name"));
            }
        }
        if (SharedHelper.getKey(getApplicationContext(), "lang").equals("en")) {

            langName.setText(getString(R.string.english));

        } else if (SharedHelper.getKey(getApplicationContext(), "lang").equals("ar")) {

            langName.setText(getString(R.string.arabic));

        }
        bubble_switch.setChecked(SharedHelper.getBooleanValue(this,BUBBLE_SETTING));

    }

    private void autoNotifictionTrue() {
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        pref1.edit().putBoolean("autoNotifiction", true).apply();
        autoNotifictionTv.setText(getString(R.string.True));
    }

    private void autoNotifictionFalse() {
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        pref1.edit().putBoolean("autoNotifiction", false).apply();
        autoNotifictionTv.setText(getString(R.string.False));


    }

    private boolean getAutoNotifiction() {
        SharedPreferences auto = PreferenceManager.getDefaultSharedPreferences(this);
        return auto.getBoolean("autoNotifiction", false);
    }

    void setAutoNotifiction() {
        autoNotifictionTv = findViewById(R.id.notifications_option);
        SharedPreferences auto = PreferenceManager.getDefaultSharedPreferences(this);

        if (auto.getBoolean("autoNotifiction", false)) {
            autoNotifictionTv.setText(getString(R.string.True));
        } else {
            autoNotifictionTv.setText(getString(R.string.False));
        }

    }

    @OnClick(R.id.notifications_view)
    void setAutoNotifictionTv() {
        if (!getAutoNotifiction()) {
            autoNotifictionTrue();
        } else {
            autoNotifictionFalse();
            cancelNotification();
        }
    }

    public void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(5);
    }

}
