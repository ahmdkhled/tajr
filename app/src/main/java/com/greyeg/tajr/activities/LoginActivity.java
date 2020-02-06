package com.greyeg.tajr.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.greyeg.tajr.MainActivity;
import com.greyeg.tajr.R;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.font.RobotoTextView;
import com.greyeg.tajr.models.User;
import com.greyeg.tajr.models.UserResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.FloatLabeledEditText;
import com.greyeg.tajr.view.ProgressWheel;

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
    public static final String LOG_IN_FROM = "log_in_from";
    public static final String USER_NAME = "username";
    public static final String USER_TYPE = "user_type";
    public static final String USER_ID = "user_id";
    public static final String IS_TAJR = "is_tajr";
    public static final String PARENT_TAJR_ID = "parent_tajr_id";
    public static final String TOKEN = "token";
    public static final String IS_LOGIN = "is_login";
    public static final String PARENT_ID = "PARENT_ID";
    public static final String REMEMBER_PASS = "REMEMBER_PASS";
    public static final String REMEMBERED_PASS = "REMEMBERED_PASS";
    public static final String REMEMBERED_EMAIL = "REMEMBERED_EMAIL";
    public static StringBuilder idListString;

    final List<String> ids = new ArrayList<>();
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
    @BindView(R.id.remember_pass)
    CheckBox rememberPass;
    ProgressDialog progressDialog;
    Api api;
    List<User> users = new ArrayList<>();
    SharedPreferences sharedPreferences;
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

        Log.d("OVERLAYYY", "login activity: ");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.wati_to_log_in));
        api = BaseClient.getBaseClient().create(Api.class);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean rem = sharedPreferences.getBoolean(REMEMBER_PASS, false);
        if (rem) {
            email.setText(SharedHelper.getKey(this, REMEMBERED_EMAIL));
            pass.setText(SharedHelper.getKey(this, REMEMBERED_PASS));
            rememberPass.setChecked(true);
        }
        setAnimation(SPLASH_SCREEN_OPTION_3);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setVisibility(View.GONE);
                sharedPreferences.edit().putBoolean(REMEMBER_PASS, rememberPass.isChecked()).apply();
                if (rememberPass.isChecked()) {
                    SharedHelper.putKey(getApplicationContext(), REMEMBERED_PASS, pass.getText().toString());
                    SharedHelper.putKey(getApplicationContext(), REMEMBERED_EMAIL, email.getText().toString());
                } else {
                    SharedHelper.putKey(getApplicationContext(), REMEMBERED_PASS, "");
                    SharedHelper.putKey(getApplicationContext(), REMEMBERED_EMAIL, "");
                }

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
                                Log.d(TAG, "onResponse: "+response.body().toString());
                                if (response.body().getCode().equals("1202") || response.body().getCode().equals("1212")) {
                                    //todo solve onesignal error
                                    loginBtn.setVisibility(View.VISIBLE);
                                    progressLogin.setVisibility(View.GONE);
                                    SharedHelper.putKey(getApplicationContext(), LOG_IN_FROM, "employee");
                                    SharedHelper.putKey(getApplicationContext(), PARENT_ID, response.body().getClients().get(0).getId());
                                    SharedHelper.putKey(getApplicationContext(), IS_LOGIN, "yes");
                                    SharedHelper.putKey(getApplicationContext(), USER_NAME, response.body().getData().getLogin_data().getUsername());
                                    SharedHelper.putKey(getApplicationContext(), USER_ID, response.body().getData().getLogin_data().getUser_id());
                                    SharedHelper.putKey(getApplicationContext(), USER_TYPE, response.body().getData().getLogin_data().getUser_type());
                                    SharedHelper.putKey(getApplicationContext(), PARENT_TAJR_ID, response.body().getData().getLogin_data().getParent_tajr_id());
                                    SharedHelper.putKey(getApplicationContext(), IS_TAJR, response.body().getData().getLogin_data().getIs_tajr());
                                    SharedHelper.putKey(getApplicationContext(), TOKEN, response.body().getData().getLogin_data().getToken());
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

