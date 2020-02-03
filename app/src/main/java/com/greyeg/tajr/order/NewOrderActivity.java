package com.greyeg.tajr.order;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.calc.CalcDialog;
import com.greyeg.tajr.helper.CurrentCallListener;
import com.greyeg.tajr.helper.NetworkUtil;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.TimeCalculator;
import com.greyeg.tajr.models.Activity;
import com.greyeg.tajr.models.LastCallDetails;
import com.greyeg.tajr.models.MainResponse;
import com.greyeg.tajr.models.UploadPhoneResponse;
import com.greyeg.tajr.models.UploadVoiceResponse;
import com.greyeg.tajr.models.UserTimePayload;
import com.greyeg.tajr.models.UserWorkTimeResponse;
import com.greyeg.tajr.order.fragments.CurrentOrderFragment;
import com.greyeg.tajr.order.fragments.MissedCallFragment;
import com.greyeg.tajr.records.CallDetails;
import com.greyeg.tajr.records.CallsReceiver;
import com.greyeg.tajr.records.CommonMethods;
import com.greyeg.tajr.records.DatabaseManager;
import com.greyeg.tajr.repository.WorkTimeRepo;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.viewmodels.NewOrderActivityVM;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewOrderActivity extends AppCompatActivity implements CurrentCallListener {

    private final String TAG = "NewOrderActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    // call controller view
    boolean micMute;
    // work time
    @BindView(R.id.timer)
    TextView timerTv;
    CurrentOrderFragment currentOrderFragment;
    private Menu callControllerMenu;
    private MenuItem micMode;
    private NewOrderActivityVM newOrderActivityVM;
    private static long startTime;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        int id = item.getItemId();
        if (id == R.id.end_call) {
            try {
                endCAll();
            } catch (Exception e) {
                Log.d(TAG, "cannot end the call: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (id == R.id.call_client) {
            if (CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder() != null)
                callClient();
        } else if (id == R.id.mic_mode) {
            modifyMic();
        }
        return false;
    };

    private Timer workTimer;
    private int DIALOG_REQUEST_CODE = 25;
    private CalcDialog calcDialog;
    private MissedCallFragment missedCallFragment;
    private DatabaseManager databaseManager;
    private boolean fromBuble = false;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        CallsReceiver.setCurrentCallListener(this);
        databaseManager = new DatabaseManager(this);
        newOrderActivityVM= ViewModelProviders.of(this).get(NewOrderActivityVM.class);
        openRecords();
        initToolBar();
        initCallController();
        currentOrderFragment = new CurrentOrderFragment();
        missedCallFragment = new MissedCallFragment();
        checkFromWhat();

        showFragment(currentOrderFragment,false);

        startTime=newOrderActivityVM.getStartTime();

        Log.d("WORKTIMEEEEEE", "onCreate: "+startTime);


    }

    private void checkFromWhat() {
        if (getIntent().getStringExtra("fromBuble") != null) {
            Log.d(TAG, "onCreate: intents");
            if (getIntent().getStringExtra("fromBuble").equals("buble")) {
                Bundle bundle = new Bundle();
                bundle.putString("fromBuble", "buble");
                currentOrderFragment.setArguments(bundle);
                fromBuble = true;
            } else fromBuble = false;
        } else fromBuble = false;
    }

    private void initCallController() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        callControllerMenu = navigation.getMenu();

        micMode = callControllerMenu.findItem(R.id.mic_mode);

        if (!micMute) {

            micMode.setIcon(R.drawable.ic_mic_none_black_24dp);
            micMode.setTitle(getString(R.string.mute_mic));

        } else {

            micMode.setIcon(R.drawable.ic_mic_off_black_24dp);
            micMode.setTitle(getString(R.string.turn_on_mic));

        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void showFragment(Fragment fragment,boolean addToBackStack){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager()
                .beginTransaction();
        if (addToBackStack)fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.Handle_Frame,fragment).commit();


    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        timerTv.setTag(0L);
    }

    private void initTimer() {
        workTimer = new Timer();
        workTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long count = (long) timerTv.getTag() + 1;
                runOnUiThread(() -> {
                    timerTv.setText(getDurationBreakdown(count * 1000));
                });
                timerTv.setTag(count);
                Log.d("taskkkkk", "run: "+count);
                TimeCalculator.getInstance(getApplicationContext()).updateActivity(new Activity(count,startTime,"APP"));

                saveWorkedTime();
            }
        }, 0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
    }

    public String getDurationBreakdown(long diff) {
        long millis = diff;
        if (millis < 0) {
            return "00:00:00";
        }
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);


        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);
        //return "${getWithLeadZero(hours)}:${getWithLeadZero(minutes)}:${getWithLeadZero(seconds)}"
    }

    @Override
    protected void onPause() {
        super.onPause();
        workTimer.cancel();
    }

    public static void finishWork(){
        finishWork();
    }


    void endCAll() {
        //stopService(new Intent(this, FloatLayout.class));

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = null;
        try {
            clazz = Class.forName(telephonyManager.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method method = null;
        try {
            method = clazz.getDeclaredMethod("getITelephony");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        ITelephony telephonyService = null;
        try {
            telephonyService = (ITelephony) method.invoke(telephonyManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        telephonyService.endCall();

    }

    private void modifyMic() {

        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        if (audioManager.isMicrophoneMute()) {
            audioManager.setMicrophoneMute(false);
            micMute = false;
            checkMic();
        } else {
            audioManager.setMicrophoneMute(true);
            micMute = true;
            checkMic();
        }
    }

    private void checkMic() {
        if (!micMute) {
            micMode.setIcon(R.drawable.ic_mic_none_black_24dp);
            micMode.setTitle(getString(R.string.mute_mic));

        } else {
            micMode.setIcon(R.drawable.ic_mic_off_black_24dp);
            micMode.setTitle(getString(R.string.turn_on_mic));
        }
    }

    public void callClient() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getPhone1()));
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1232);
            }
        } else {
            startActivity(callIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.calc) {
            showCalculatoe();
        } else if (id == R.id.finish_work) {
            finish();
        } else if (id == R.id.show_missed_call) {
            if (getSupportFragmentManager().findFragmentById(R.id.Handle_Frame) instanceof MissedCallFragment)
                showFragment(currentOrderFragment,true);
            if (getSupportFragmentManager().findFragmentById(R.id.Handle_Frame) instanceof CurrentOrderFragment)
                showFragment(missedCallFragment,true);

        }
        return super.onOptionsItemSelected(item);
    }

    private void showCalculatoe() {
        calcDialog = CalcDialog.newInstance(DIALOG_REQUEST_CODE);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag("calc_dialog") == null) {
            calcDialog.show(fm, "calc_dialog");
        }
    }

    private void openRecords() {
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        pref1.edit().putBoolean("switchOn", true).apply();
    }

    private void closeRecords() {
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        pref1.edit().putBoolean("switchOn", false).apply();
    }

    private void saveWorkedTime() {

//        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
//        long oldWork = pref1.getLong("timeWork", 0);
//        long newWorkedTime = oldWork + 1;
//        Log.d("saveWorkedTime", "saveWorkedTime: " + newWorkedTime);
//        pref1.edit().putLong("timeWork", newWorkedTime).apply();

    }

    private long getNotSavedWrokTime() {
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        return pref1.getLong("timeWork", 0);
    }

    private void setPldTimeWorkZero() {
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        pref1.edit().putLong("timeWork", 0).apply();
        Log.d("saveWorkedTime", "setPldTimeWorkZero: ");
    }

    private void saveWorkTime() {
        long currentWorkTime = Long.valueOf(timerTv.getTag().toString());
        Log.d("userWorkTime", "time before end: " + currentWorkTime);

        if (!NetworkUtil.isConnected(this)){
            TimeCalculator.getInstance(this).setStatus(TimeCalculator.APP,TimeCalculator.FAILED_TO_UPLOAD);
            return;

        }

        TimeCalculator.getInstance(this).setStatus(TimeCalculator.APP,TimeCalculator.UPLOADING);
        String token=SharedHelper.getKey(this, LoginActivity.TOKEN);
        ArrayList<Activity>activityList=TimeCalculator.getInstance(this).getIdleWorkTime();
        UserTimePayload userTimePayload=new UserTimePayload(token,activityList);
        Log.d("TIMERCALCCzz", "before activity : "+activityList.size());

        for (Activity activity:activityList){
            Log.d("TIMERCALCCzz", "before activity : "+activity.toString());
        }

        WorkTimeRepo.getInstance()
                .setUserTime(userTimePayload)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<MainResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<MainResponse> response) {
                        MainResponse mainResponse=response.body();
                        if (response.isSuccessful()&&mainResponse!=null){
                            Log.d("userWorkTime", "time after end: " + currentWorkTime);
                            TimeCalculator.getInstance(getApplicationContext())
                                    .clear(activityList);
                            for (Activity activity:TimeCalculator.getInstance(getApplicationContext()).getWorkTimeActivity()){
                                Log.d("TIMERCALCCzz", "send activity : "+activity);
                            }

                        }else{
                            Log.d("TIMERCALCCzz","error parsing");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        TimeCalculator.getInstance(getApplicationContext())
                                .setStatus(TimeCalculator.APP,TimeCalculator.FAILED_TO_UPLOAD);
                        Log.d("TIMERCALCCzz", "onError: "+e.getMessage());

                    }
                });







    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveWorkTime();

        Log.d("WORKTIMEEEEEE", "onDestroy: ");
        try {
            cancelNotification();
            closeRecords();
        } catch (Exception e) {
            Log.d("userWorkTime", "onDestroy: "+e.getMessage());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("WORKTIMEEEEEE", "onStop: ");
    }

    public void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(5);
    }


    private LastCallDetails getLastCallDetails() {
        StringBuffer sb = new StringBuffer();
        Uri contacts = CallLog.Calls.CONTENT_URI;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        Cursor managedCursor = getContentResolver().query(contacts, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int activeID = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);

        sb.append("Call Details :");
        String phoneNumber = null;
        String callDuration2 = null;
        String activatedNum = null;
        String calType = null;
        while (managedCursor.moveToNext()) {
            phoneNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            callDuration2 = managedCursor.getString(duration);
            activatedNum = managedCursor.getString(activeID);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    dir = "REJECTED";
                    break;
            }

            calType = dir;

        }

        managedCursor.close();
        return new LastCallDetails(phoneNumber, callDuration2, activatedNum, calType);

    }

    @Override
    public void callEnded(int serialNumber, String phoneNumber) {

        Log.d("callEndedcallEnded", "callEnded: ");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LastCallDetails callDetails = getLastCallDetails();

                            Log.d("callDetails", "callDetails: " + callDetails.getType());
                            if (callDetails.getType().equals("MISSED") || callDetails.getType().equals("REJECTED")) {
                                if (callDetails.getActiveId().equals(SharedHelper.getKey(getApplicationContext(),
                                        "activated_sub_id"))) {
                                    BaseClient.getBaseClient().create(Api.class).missedCall(SharedHelper.getKey(getApplicationContext(), LoginActivity.TOKEN), callDetails.getPhone())
                                            .enqueue(new Callback<UploadPhoneResponse>() {
                                                @Override
                                                public void onResponse(Call<UploadPhoneResponse> call, Response<UploadPhoneResponse> response) {
                                                    if (response.body().getResponse().equals("Success")) {
                                                        Toast.makeText(NewOrderActivity.this, "تم ارسال رقم  " + callDetails.getPhone() + " المكالمة الفائتة الى السيرفر", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<UploadPhoneResponse> call, Throwable t) {

                                                }
                                            });

                                }

                            } else if (callDetails.getType().equals("OUTGOING")) {

                                if (!callDetails.getDuration().equals("0")) {
                                    if (!CurrentOrderData.getInstance().getCallTime().equals("Saved")) {

                                        Log.d("eslamfaisalalire", "run: add call to database");
                                        new DatabaseManager(getApplicationContext()).addCallDetails(new CallDetails(serialNumber,
                                                CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getPhone1(),
                                                CurrentOrderData.getInstance().getCallTime(), new CommonMethods().getDate(),
                                                "not_yet", callDetails.getDuration()));
                                        CurrentOrderData.getInstance().setCallTime("Saved");
                                        minutesUsage(callDetails.getDuration());
                                        uploadVoices();
                                    }

                                }
                            }
                        } catch (Exception e) {
                            Log.d("eslamfaisal", "callEnded: " + e.getMessage());
                        }

                    }
                });

            }
        }, 1000);


    }

    private boolean getAutoValue() {
        SharedPreferences auto = PreferenceManager.getDefaultSharedPreferences(this);
        return auto.getBoolean("autoUpdate", false);
    }

    @SuppressLint("ApplySharedPref")
    private void minutesUsage(String seconds) {
        Log.d("minutesUsage", "minutesUsage: call time seconds" + seconds);
        int totalSeconds = 149 * 59;
        int minutes = totalSeconds / 59;
        int remaining = 0;
        if ((totalSeconds % 59) > 0) {
            remaining = 1;
        }
        Log.d("minutesUsage", "minutesUsage: call time minutes" + minutes);
        Log.d("minutesUsage", "minutesUsage: call time remaining" + remaining);
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        float oldUsage = pref1.getFloat("cards_usage", 0f);
        float currentUsage = (float) (minutes + remaining);
        float newUsage = oldUsage + currentUsage;
        Log.d("minutesUsage", "minutesUsage: call time all m" + newUsage);
        pref1.edit().putFloat("cards_usage", newUsage).commit();
        Log.d("minutesUsage", "minutesUsage: " + pref1.getFloat("cards_usage", 0f));

    }

    private void uploadVoices() {
        List<CallDetails> callDetailsList = databaseManager.getAllDetails();
        for (CallDetails call : callDetailsList) {
            if (call.getUploaded().equals("not_yet")) {

                String path = Environment.getExternalStorageDirectory() + "/MyRecords/" + call.getDate() + "/" + call.getNum() + "_" + call.getTime1() + ".mp4";
                File file = new File(path);
                RequestBody surveyBody = RequestBody.create(MediaType.parse("audio/*"), file);
                MultipartBody.Part image = MultipartBody.Part.createFormData("voice_note", file.getName(), surveyBody);
                RequestBody title1 = RequestBody.create(MediaType.parse("text/plain"), CurrentOrderData.getInstance().getCurrentOrderResponse().getOrder().getId());
                RequestBody duration = RequestBody.create(MediaType.parse("text/plain"), call.getDuration());
                RequestBody token = RequestBody.create(MediaType.parse("text/plain"), SharedHelper.getKey(this, LoginActivity.TOKEN));

                BaseClient.getBaseClient().create(Api.class).uploadVoice(token, title1, duration, image).enqueue(new Callback<UploadVoiceResponse>() {
                    @Override
                    public void onResponse(Call<UploadVoiceResponse> call2, Response<UploadVoiceResponse> response) {
                        databaseManager.updateCallDetails(call);

                    }

                    @Override
                    public void onFailure(Call<UploadVoiceResponse> call, Throwable t) {
                        Log.d("caaaaaaaaaaaaaal", "onFailure: " + t.getMessage());

                    }
                });
            }
        }
    }

}
