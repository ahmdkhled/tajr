package com.tajr.tajr.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.tajr.tajr.R;
import com.tajr.tajr.helper.SessionManager;
import com.tajr.tajr.helper.font.RobotoTextView;
import com.tajr.tajr.models.User;
import com.tajr.tajr.models.UserResponse;
import com.tajr.tajr.server.Api;
import com.tajr.tajr.server.BaseClient;
import com.tajr.tajr.view.utils.FloatLabeledEditText;
import com.tajr.tajr.view.utils.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    public static final String REMEMBER_PASS = "REMEMBER_PASS";
    public static StringBuilder idListString;
    public static String TOKEN="token";
    public static final String IS_LOGIN = "is_login";
    public static final String PARENT_ID = "PARENT_ID";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "username";
    public static final String LOG_IN_FROM = "log_in_from";



    final List<String> ids = new ArrayList<>();
    RobotoTextView loginBtn;
    FloatLabeledEditText email;
    FloatLabeledEditText pass;
    KenBurnsView mKenBurns;
    ImageView mLogo;
    ProgressWheel progressLogin;
    CheckBox rememberPass;
    ProgressDialog progressDialog;
    Api api;
    List<User> users = new ArrayList<>();
    SharedPreferences sharedPreferences;
    private String TAG = "LoginActivityyy";
    SessionManager sessionManager;

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
        mKenBurns=findViewById(R.id.ken_burns_images);
        mLogo=findViewById(R.id.logo);
        progressLogin=findViewById(R.id.progress_log_in);
        loginBtn=findViewById(R.id.loginbtn);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        rememberPass=findViewById(R.id.remember_pass);
        Log.d("OVERLAYYY", "login activity: ");
        sessionManager=SessionManager.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.wati_to_log_in));
        api = BaseClient.getBaseClient().create(Api.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean rem = sessionManager.isRemembered();
        Log.d(TAG, "onCreate: "+sessionManager.getEmail());
        if (rem) {
            email.setText(sessionManager.getEmail());
            pass.setText(sessionManager.getPass());
            rememberPass.setChecked(true);
        }
        setAnimation(SPLASH_SCREEN_OPTION_3);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("") || pass.getText().toString().equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "برجاء ادخال البريد وكلمة المرور", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginBtn.setVisibility(View.GONE);
                progressLogin.setVisibility(View.VISIBLE);
                progressLogin.spin();
                api.login(email.getText().toString(), pass.getText().toString()).enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, final Response<UserResponse> response) {

                            UserResponse userResponse=response.body();
                            if (userResponse != null) {
                                Log.d(TAG, "onResponse: "+response.body().toString());
                                if (userResponse.getCode().equals("1202") || userResponse.getCode().equals("1212")) {
                                    //todo solve onesignal error
                                    loginBtn.setVisibility(View.VISIBLE);
                                    progressLogin.setVisibility(View.GONE);
                                    SessionManager.getInstance(getApplicationContext())
                                            .saveSession(userResponse,"yes",SessionManager.EMPLOYEE);
                                    sessionManager.setIsRemembered(rememberPass.isChecked());
                                    if (rememberPass.isChecked()) {
                                        sessionManager
                                                .SaveEmailAndPassword(email.getText().toString(),pass.getText().toString());
                                    } else {
                                        sessionManager
                                                .SaveEmailAndPassword("","");

                                    }
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();


                                } else {
                                    Snackbar.make(v, R.string.wrong_email_pass, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            Snackbar.make(v, R.string.wrong_email_pass, Snackbar.LENGTH_LONG).show();
                            loginBtn.setVisibility(View.VISIBLE);
                            progressLogin.setVisibility(View.GONE);
                            Log.d("eeeeeeeeeeeeeeee", "onFailure: " + t.getMessage());
                        }
                    });

            }
        });

    }

    private void minutesUsage() {

        int totalSeconds = Integer.parseInt("60");
        int minutes = totalSeconds / 59;
        int remaining = 0;
        if ((totalSeconds % 59) > 0) {
            remaining = 1;
        }

        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(this);
        float oldUsage = pref1.getFloat("cards_usage", 0f);
        float currentUsage = (float) (minutes + remaining);
        float newUsage = oldUsage + currentUsage;
        pref1.edit().putFloat("cards_usage", newUsage).apply();
        Log.d("minutesUsage", "minutesUsage: " + pref1.getFloat("cards_usage", 0f));

    }

    private void setAnimation(String category) {
        if (category.equals(SPLASH_SCREEN_OPTION_1)) {
            mKenBurns.setImageResource(R.drawable.background_media);
            animation1();
        } else if (category.equals(SPLASH_SCREEN_OPTION_3)) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("OVERLAYYY", " login onDestroy: ");

    }

    public void adminLogIn(View view) {
        startActivity(new Intent(this, AdminLoginActivity.class));
    }
}

