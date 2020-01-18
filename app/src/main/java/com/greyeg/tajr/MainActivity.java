package com.greyeg.tajr;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;
import com.greyeg.tajr.activities.BalanceActivity;
import com.greyeg.tajr.activities.CartsActivity;
import com.greyeg.tajr.activities.ChatActivity;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.activities.SettingsActivity;
import com.greyeg.tajr.activities.WorkHistoryActivity;
import com.greyeg.tajr.adapters.DrawerAdapter;
import com.greyeg.tajr.helper.AccessibilityManager;
import com.greyeg.tajr.helper.ScreenHelper;
import com.greyeg.tajr.helper.SharedHelper;

import com.greyeg.tajr.helper.font.RobotoTextView;
import com.greyeg.tajr.order.CurrentOrderData;
import com.greyeg.tajr.order.NewOrderActivity;
import com.greyeg.tajr.order.models.CurrentOrderResponse;
import com.greyeg.tajr.over.MissedCallOrderService;
import com.greyeg.tajr.records.RecordsActivity;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.dialogs.UpdateVersionDialog;
import com.greyeg.tajr.view.kbv.KenBurnsView;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.greyeg.tajr.activities.LoginActivity.idListString;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SPLASH_SCREEN_OPTION = "com.csform.android.uiapptemplate.SplashScreensActivity";
    public static final String SPLASH_SCREEN_OPTION_1 = "Fade in + Ken Burns";
    public static final String SPLASH_SCREEN_OPTION_2 = "Down + Ken Burns";
    public static final String SPLASH_SCREEN_OPTION_3 = "Down + fade in + Ken Burns";
    public static final int PHONE_PERMISSIONS = 123;
    private final static int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 115;
    public static int screenWidth = -1;
    public static int screenHeight = -1;
    public static int calls_count = 0;
    public static Timer timer = new Timer();
    public static long startTime = 0;
    public static Activity mainActivity;
    @BindView(R.id.ken_burns_images)
    KenBurnsView mKenBurns;
    @BindView(R.id.logo)
    ImageView mLogo;
    @BindView(R.id.welcome_text)
    RobotoTextView welcomeText;
    Api api;
    Crashlytics crashlytics;

    //    @BindView(R.id.timer_text)
//    TextView timer_text;
    WorkTimer workTimer;
    SwitchCompat callsSwitch;
    Toolbar toolbar;
    SwipeButton enableButton;
    String idid;
    String namename;
    String callDuration2 = null;
    String phonNumber = null;
    int subID;
    ImageView start;
    private boolean isCanceled = false;
    private android.os.Handler handleCheckStatus;

    private String TAG = "tttttttttt";
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        crashlytics=Crashlytics.getInstance();
        mainActivity = this;
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        checkAppVersion();
        initDrawer();
        requestPermissions();

        initViews();











        setAnimation(SPLASH_SCREEN_OPTION_3);
        api = BaseClient.getBaseClient().create(Api.class);

        enableButton =findViewById(R.id.swipe_btn);
        enableButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (active) {
                    mKenBurns.pause();
                    Intent intent = new Intent(getApplicationContext(), NewOrderActivity.class);
                    startActivity(intent);


                }

            }
        });

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notification").child("seen");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//              //  Notification message = dataSnapshot.getValue(Notification.class);
//                //Toast.makeText(MainActivity.this, message.getUserName(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        // LoginActivity.sendNotification();
        // newjob();


    }

    void showAccessibilityPermissionDialog(){
        AlertDialog alertDialog=new AlertDialog.Builder(this).create();
        View dialogView= LayoutInflater.from(this)
                .inflate(R.layout.accessibility_dialog,null);
        alertDialog.setView(dialogView);
        alertDialog.show();




        dialogView.findViewById(R.id.enable)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        alertDialog.dismiss();


                    }
                });

        dialogView.findViewById(R.id.cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
    }



    public static void sendNotification(final String message) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    send_email = "user2@gmail.com";

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic NTQ1YzI4YzYtZTE4Zi00OWQ3LWE1ZWQtZGRkNWNiMmVkMjI5");
                        con.setRequestMethod("POST");



                        String strJsonBody = "{"
                                + "\"app_id\": \"c1c60918-4009-4213-b070-36296afc47b7\"," +

                                //  "'include_player_ids': ['" + userId + "'], "

                                // "'include_player_ids': ["+idListString.toString()+"], "

                                "\"include_player_ids\": [" + idListString.toString() + "],"
                                // + "\"excluded_segments\": [\"" + userId + "\"],"
                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"" + message + "\"}," +
                                "\"headings\": {\"en\": \"" + SharedHelper.getKey(mainActivity, LoginActivity.USER_NAME) + "\"}"
                                + "}";

                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    public static void sendNotificationImage() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    send_email = "user2@gmail.com";

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic NTQ1YzI4YzYtZTE4Zi00OWQ3LWE1ZWQtZGRkNWNiMmVkMjI5");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"c1c60918-4009-4213-b070-36296afc47b7\"," +

                                //  "'include_player_ids': ['" + userId + "'], "

                                // "'include_player_ids': ["+idListString.toString()+"], "

                                "\"include_player_ids\": [" + idListString.toString() + "],"
                                // + "\"excluded_segments\": [\"" + userId + "\"],"
                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"" + "صورة" + "\"}," +
                                "\"headings\": {\"en\": \"" + SharedHelper.getKey(mainActivity, LoginActivity.USER_NAME) + "\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }

    public static int getSimIdColumn(final Cursor c) {

        for (String s : new String[]{"sim_id", "simid", "sub_id"}) {
            int id = c.getColumnIndex(s);
            if (id >= 0) {
                Log.d("kkkkkkkkkk", "sim_id column found: " + s);
                return id;
            }
        }
        Log.d("kkkkkkkkkk", "no sim_id column found");
        return -1;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void checkAppVersion(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig
                .fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()){
                            String latestAppVersion=mFirebaseRemoteConfig.getString("latest_app_version");
                            try {
                                latestAppVersion=latestAppVersion.replace(".","");
                                int latestVersion=Integer.valueOf(latestAppVersion);
                                int currentVersion=Integer.valueOf(BuildConfig.VERSION_NAME.replace(".",""));

                                if (currentVersion<latestVersion){
                                    UpdateVersionDialog updateVersionDialog=new UpdateVersionDialog();
                                    updateVersionDialog.show(getSupportFragmentManager(),"");
                                }

                            }catch (Exception e){
                                Log.d("REMOTEECONFIGG","exception "+ e.getMessage());
                            }

                        }

                    }
                });

    }


    private void initViews() {
        ScreenHelper.saveScreenDimensions(this,this);
        if (!AccessibilityManager.isAccessibilityEnabled(this)){
            showAccessibilityPermissionDialog();
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;


        setAnimation(SPLASH_SCREEN_OPTION_3);
        api = BaseClient.getBaseClient().create(Api.class);


    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                    checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                            checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission has not been granted, therefore prompt the user to grant permission
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.MODIFY_PHONE_STATE,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.PROCESS_OUTGOING_CALLS,
                                Manifest.permission.SYSTEM_ALERT_WINDOW

                        },
                        56);
            } else {
                try {
                    checkDauleSim();
                } catch (Exception e) {
                    Log.d(TAG, "onCreate: ");
                }

            }

            Log.d("OVERLAYYY", "onCreate: ");
            //grant permission for drawing bubble over screen
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }

        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    void checkDauleSim() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoCountMax() == 2) {
            SharedHelper.putKey(getApplicationContext(), "sub_num", "2");
            Log.d(TAG, "sub_num: = 2");
            if (SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoCount() == 2) {
                SharedHelper.putKey(getApplicationContext(), "active_sub", "2");
                Log.d(TAG, "active_sub: = 2");
                String subIdForSim1 = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(0).getIccId());
                String subIdForSim2 = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(1).getIccId());
                SharedHelper.putKey(getApplicationContext(), "sub_id_sim1", subIdForSim1);
                SharedHelper.putKey(getApplicationContext(), "sub_id_sim2", subIdForSim2);
                Log.d(TAG, "checkDauleSim: " + subIdForSim1);
                Log.d(TAG, "checkDauleSim: " + subIdForSim2);
            } else {
                SharedHelper.putKey(getApplicationContext(), "active_sub", "1");
                Log.d(TAG, "active_sub: = 1");
                if (SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(0) != null) {
                    String subIdForSim1 = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(0).getIccId());
                    SharedHelper.putKey(getApplicationContext(), "activated_sub_id", subIdForSim1);
                    Log.d(TAG, "activated_sub_id: = " + subIdForSim1);
                } else {
                    String subIdForSim1 = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(1).getIccId());
                    SharedHelper.putKey(getApplicationContext(), "activated_sub_id", subIdForSim1);
                    Log.d(TAG, "activated_sub_id: = " + subIdForSim1);
                }

            }

        } else {
            SharedHelper.putKey(getApplicationContext(), "sub_num", "1");
            Log.d(TAG, "sub_num: = 1");
            SubscriptionManager sm = SubscriptionManager.from(getApplicationContext());
            List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();
            SubscriptionInfo si = sis.get(0);
            String iccId = si.getIccId();
            Log.d(TAG, "activated_sub_id: = " + iccId);

        }
    }

    void getSubListInfo() {
        SubscriptionManager sm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            sm = SubscriptionManager.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();
            SubscriptionInfo si = sis.get(0);
            String iccId = si.getIccId();
        }
    }

    void getSimIds() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //Toast.makeText(getApplicationContext(), "duration "+getget()+" sim= "+SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoCount(), Toast.LENGTH_SHORT).show();
            Toast.makeText(mainActivity, "" + subID, Toast.LENGTH_SHORT).show();
            String subscriptionId = String.valueOf(SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(1).getIccId());

            SubscriptionInfo info = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoForSimSlotIndex(subID);

            Toast.makeText(this, "" + info.getNumber(), Toast.LENGTH_SHORT).show();

            // Log.d("getSubscriptionId", "first sub: "+subID);
            Log.d("icicicicicicic", "second sub: " + subscriptionId);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 56) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission has not been granted, therefore prompt the user to grant permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.READ_CALL_LOG,
                                    Manifest.permission.MODIFY_AUDIO_SETTINGS,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_CONTACTS
                            },
                            56);
                }
                {
                    checkDauleSim();
                }
            } else {
                checkDauleSim();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
//            Log.d("OVERLAYYYY", "onActivityResult: result code "+resultCode);
//
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(mainActivity, "thanks :)", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.d("OVERLAYYY", "onActivityResult: permission denies");
//                Toast.makeText(MainActivity.this,
//                        "you must give permission foe drawing on screen",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }

    }

    @SuppressLint("NewApi")
    private String getMyPhoneNumber() {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "545645";
        }

        return String.valueOf(mTelephonyMgr.getSimState(0));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nave_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nave_analytics) {
            startActivity(new Intent(this, WorkHistoryActivity.class));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        SharedHelper.putKey(getApplicationContext(), "state", "off");
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    String getget() {
//        String callDuration2 = null;
//        String phonNumber = null;
//        String simId = null;
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
        Cursor managedCursor = getContentResolver().query(contacts, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");

        while (managedCursor.moveToNext()) {

            HashMap rowDataCall = new HashMap<String, String>();

            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            String callDayTime = new Date(Long.valueOf(callDate)).toString();
            // long timestamp = convertDateToTimestamp(callDayTime);
            String callDuration = managedCursor.getString(duration);
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
            }
            callDuration2 = callDuration;
            phonNumber = phNumber;
            // simId = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);
            int subid = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID);
            int subname = managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_COMPONENT_NAME);

            idid = managedCursor.getString(subid);
            namename = managedCursor.getString(subname);

            //   simId = managedCursor.getString(managedCursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));

//            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
//            sb.append("\n----------------------------------");


        }
        Log.d("iiiiiiiiiiiii", "getget: " + idid);
        Log.d("iiiiiiiiiiiii", "getget: " + namename);
        managedCursor.close();
        return callDuration2;

    }

    void test() {
    }

    void initDrawer() {
        start = findViewById(R.id.startDrawer);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerList = findViewById(R.id.list_view);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        View headerView = getLayoutInflater().inflate(
                R.layout.header_navigation_drawer_travel, mDrawerList, false);
        TextView userName = headerView.findViewById(R.id.user_name);
        userName.setText(SharedHelper.getKey(getApplicationContext(), LoginActivity.USER_NAME));

        mDrawerList.addHeaderView(headerView);// Add header before adapter (for
        // pre-KitKat)
        mDrawerList.setAdapter(new DrawerAdapter(this));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        // mDrawerList.setBackgroundResource(R.drawable.ic_home_for_list);
        mDrawerList.getLayoutParams().width = (int) getResources()
                .getDimension(R.dimen.drawer_width_travel);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        });
//
    }



    /**
     * Animation depends on category.
     */
    private void setAnimation(String category) {
        if (category.equals(SPLASH_SCREEN_OPTION_1)) {
            mKenBurns.setImageResource(R.drawable.background_media);
            animation1();
        } else if (category.equals(SPLASH_SCREEN_OPTION_3)) {
            mKenBurns.setImageResource(R.drawable.ic_cars);
            animation2();
            animation3();
        }
    }

    private void animation1() {
        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(mLogo, "scaleX", 5.0F, 1.0F);
        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimation.setDuration(1200);
        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(mLogo, "scaleY", 5.0F, 1.0F);
        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimation.setDuration(1200);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mLogo, "alpha", 0.0F, 1.0F);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setDuration(1200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
        animatorSet.setStartDelay(500);
        animatorSet.start();
    }

    private void animation2() {
        mLogo.setAlpha(1.0F);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
        mLogo.startAnimation(anim);
    }

    private void animation3() {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(welcomeText, "alpha", 0.0F, 1.0F);
        alphaAnimation.setStartDelay(1500);
        alphaAnimation.setDuration(100);
        alphaAnimation.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (enableButton.isActive()) {
            enableButton.toggleState();
        }
        new Handler().postDelayed(() -> {
            mKenBurns.resume();
        },200);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.records_menu, menu);
        MenuItem item = menu.findItem(R.id.mySwitch);

        View view = getLayoutInflater().inflate(R.layout.switch_layout, null, false);

        final SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);

        SwitchCompat switchCompat = view.findViewById(R.id.switchCheck);
        switchCompat.setChecked(pref1.getBoolean("switchOn", true));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Switch", "onCheckedChanged: " + isChecked);
                    Toast.makeText(getApplicationContext(), getString(R.string.call_listener_on), Toast.LENGTH_LONG).show();
                    pref1.edit().putBoolean("switchOn", isChecked).apply();
                } else {
                    Log.d("Switch", "onCheckedChanged: " + isChecked);
                    Toast.makeText(getApplicationContext(), getString(R.string.call_listener_off), Toast.LENGTH_LONG).show();
                    pref1.edit().putBoolean("switchOn", isChecked).apply();
                }
            }
        });
        item.setActionView(view);
        return true;
    }

    private void sendNotification() {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NGEwMGZmMjItY2NkNy0xMWUzLTk5ZDUtMDAwYzI5NDBlNjJj");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    + "\"app_id\": \"5eb5a37e-b458-11e3-ac11-000c2940e62c\","
                    + "\"included_segments\": [\"All\"],"
                    + "\"data\": {\"foo\": \"bar\"},"
                    + "\"contents\": {\"en\": \"English Message\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            } else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    public interface WorkTimer {
        void getTime(String time);
    }


//    private void scheduleAdvancedJob() {
//        PersistableBundleCompat extras = new PersistableBundleCompat();
//        extras.putString("key", "Hello world");
//
//        int jobId = new JobRequest.Builder(DemoSyncJob.TAG)
//                .setExecutionWindow(1_000L, 4_000L)
//                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
//                .build()
//                .schedule();
//        SharedHelper.putKey(this,"job_key", String.valueOf(jobId));
//    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = null;
            if (position == 1) {
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
            } else if (position == 2) {
                intent = new Intent(getApplicationContext(), WorkHistoryActivity.class);
            } else if (position == 3) {
                intent = new Intent(getApplicationContext(), CartsActivity.class);
            } else if (position == 4) {
                intent = new Intent(getApplicationContext(), BalanceActivity.class);
            } else if (position == 5) {
                intent = new Intent(getApplicationContext(), ChatActivity.class);
            } else if (position == 6) {
                intent = new Intent(getApplicationContext(), RecordsActivity.class);
            }
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            if (position == 7) {
                SharedHelper.putKey(getApplicationContext(), LoginActivity.IS_LOGIN, "no");
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }



}
