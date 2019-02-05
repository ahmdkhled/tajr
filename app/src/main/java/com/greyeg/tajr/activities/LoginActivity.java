package com.greyeg.tajr.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.greyeg.tajr.MainActivity;
import com.greyeg.tajr.R;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.font.RobotoTextView;
import com.greyeg.tajr.job.JobServicio;
import com.greyeg.tajr.models.SimpleOrderResponse;
import com.greyeg.tajr.models.User;
import com.greyeg.tajr.models.UserResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.FloatLabeledEditText;
import com.greyeg.tajr.view.ProgressWheel;
import com.greyeg.tajr.view.kbv.KenBurnsView;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String SPLASH_SCREEN_OPTION = "com.csform.android.uiapptemplate.SplashScreensActivity";
    public static final String SPLASH_SCREEN_OPTION_1 = "Fade in + Ken Burns";
    public static final String SPLASH_SCREEN_OPTION_2 = "Down + Ken Burns";
    public static final String SPLASH_SCREEN_OPTION_3 = "Down + fade in + Ken Burns";
    public static final String LOG_IN_FROM = "log_in_from";
    public static final String USER_NAME = "username";
    public static final String USER_TYPE = "user_type";
    public static final String USER_ID = "user_id";
    public static final String IS_TAJR = "is_tajr";
    public static final String PARENT_TAJR_ID = "parent_tajr_id";
    public static final String TOKEN = "token";
    public static final String IS_LOGIN = "is_login";

    @BindView(R.id.loginbtn)
    RobotoTextView loginBtn;

    @BindView(R.id.email)
    FloatLabeledEditText email;

    @BindView(R.id.password)
    FloatLabeledEditText pass;

    @BindView(R.id.ken_burns_images)
    KenBurnsView mKenBurns;

    @BindView(R.id.logo)
    ImageView mLogo;

    @BindView(R.id.progress_log_in)
    ProgressWheel progressLogin;

    ProgressDialog progressDialog;
    Api api;

    List<User> users = new ArrayList<>();
    final List<String> ids = new ArrayList<>();
    public static StringBuilder idListString;
    private String TAG = "LoginActivity";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Log.d("sssssssssssssss", "onCreate: ");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.wati_to_log_in));
        api = BaseClient.getBaseClient().create(Api.class);

        setAnimation(SPLASH_SCREEN_OPTION_3);
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }
//        });

        final String k1 = "nour@musa3d.com";
        String s1 = "1234";
        final String k2 = "p5a2rktEVCZhJwAgxbYIWPsOn0uG1XFj";
        String s2 = "jCegey5u2CaPIVRhPoFwp7MbAluNIHwNkzqdgSMp7AcvZYo5QLDUstt9K1z836hZ";

        email.setText(k1);
        pass.setText(s1);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneSignal.sendTag("User_ID", email.getText().toString());
                loginBtn.setVisibility(View.GONE);
                progressLogin.setVisibility(View.VISIBLE);
                progressLogin.spin();
                if (email.getText().equals("") || pass.getText().equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "برجاء ادخال البريد وكلمة المرور", Toast.LENGTH_SHORT).show();
                } else {
                    api.login(email.getText().toString(), pass.getText().toString()).enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {
                            if (response.body() != null) {
                                if (response.body().getCode().equals("1202") || response.body().getCode().equals("1212")) {

                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId())
                                            .setValue(new User(response.body().getData().getLogin_data().getUsername(), OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId()))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        loginBtn.setVisibility(View.VISIBLE);
                                                        progressLogin.setVisibility(View.GONE);
                                                        SharedHelper.putKey(getApplicationContext(), LOG_IN_FROM, "employee");
                                                        SharedHelper.putKey(getApplicationContext(), IS_LOGIN, "yes");
                                                        SharedHelper.putKey(getApplicationContext(), USER_NAME, response.body().getData().getLogin_data().getUsername());
                                                        SharedHelper.putKey(getApplicationContext(), USER_ID, response.body().getData().getLogin_data().getUser_id());
                                                        SharedHelper.putKey(getApplicationContext(), USER_TYPE, response.body().getData().getLogin_data().getUser_type());
                                                        SharedHelper.putKey(getApplicationContext(), PARENT_TAJR_ID, response.body().getData().getLogin_data().getParent_tajr_id());
                                                        SharedHelper.putKey(getApplicationContext(), IS_TAJR, response.body().getData().getLogin_data().getIs_tajr());
                                                        SharedHelper.putKey(getApplicationContext(), TOKEN, response.body().getData().getLogin_data().getToken());
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                }
                                            });


                                    // raniaabdel001@gmail.com
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            loginBtn.setVisibility(View.VISIBLE);
                            progressLogin.setVisibility(View.GONE);
                            Log.d("eeeeeeeeeeeeeeee", "onFailure: " + t.getMessage());
                        }
                    });
                }
            }
        });

    }

    private void minutesUsage(  ){

        int totalSeconds = Integer.parseInt("60");
        int minutes = totalSeconds/59;
        int remaining = 0;
        if ((totalSeconds%59)>0){
            remaining = 1;
        }

        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        float oldUsage = pref1.getFloat("cards_usage",0f);
        float currentUsage = (float) (minutes+remaining);
        float newUsage = oldUsage+currentUsage;
        pref1.edit().putFloat("cards_usage",newUsage).apply();
        Log.d("minutesUsage", "minutesUsage: "+pref1.getFloat("cards_usage",0f));

    }

    private void setAnimation(String category) {
        if (category.equals(SPLASH_SCREEN_OPTION_1)) {
            mKenBurns.setImageResource(R.drawable.background_media);
            animation1();
        }  else if (category.equals(SPLASH_SCREEN_OPTION_3)) {
            mKenBurns.setImageResource(R.drawable.ic_traffic);
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

        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(email, "alpha", 0.0F, 1.0F);
        alphaAnimation.setStartDelay(1700);
        alphaAnimation.setDuration(500);
        alphaAnimation.start();

    }

    void newjob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("some_key", "some_value");

        Job job = dispatcher.newJobBuilder().setService(JobServicio.class)
                .setTag("connectivity-job").setLifetime(Lifetime.FOREVER).setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setRecurring(true).setReplaceCurrent(true).setTrigger(Trigger.executionWindow(0, 0)).build();
        dispatcher.mustSchedule(job);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void adminLogIn(View view) {
        startActivity(new Intent(this,AdminLoginActivity.class));
    }
}

