package com.greyeg.tajr.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.greyeg.tajr.R;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.font.RobotoTextView;
import com.greyeg.tajr.models.User;
import com.greyeg.tajr.models.UserResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.utils.FloatLabeledEditText;
import com.greyeg.tajr.view.utils.ProgressWheel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.greyeg.tajr.activities.LoginActivity.LOG_IN_FROM;

/**
 * A login screen that offers login via email/password.
 */
public class AdminLoginActivity extends AppCompatActivity {

    public static final String SPLASH_SCREEN_OPTION = "com.csform.android.uiapptemplate.SplashScreensActivity";
    public static final String SPLASH_SCREEN_OPTION_1 = "Fade in + Ken Burns";
    public static final String SPLASH_SCREEN_OPTION_2 = "Down + Ken Burns";
    public static final String SPLASH_SCREEN_OPTION_3 = "Down + fade in + Ken Burns";

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
        setContentView(R.layout.activity_admin_login);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        String k2 = "Ljf6eOd5riWoFuDI7ysGzaSkXU49RAph";
        String s2 = "cZw7P25GncJymMtZEgYOCXGARHrtq9f0DulmDlxU86LIp9ViKWvTsnaoICuHz6Eh";

        email.setText(k2);
        pass.setText(s2);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setVisibility(View.GONE);
                progressLogin.setVisibility(View.VISIBLE);
                progressLogin.spin();
                if (email.getText().toString().equals("") || pass.getText().toString().equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminLoginActivity.this, "برجاء ادخال البريد وكلمة المرور", Toast.LENGTH_SHORT).show();
                } else {
                    api.adminLogin(email.getText().toString(), pass.getText().toString()).enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<UserResponse> call, final Response<UserResponse> response) {

                            Log.d("rrrrrrrrrrrr", "onResponse: response"+response.body().getCode());
                            if (response.body() != null) {
                                if (response.body().getCode().equals("1202") || response.body().getCode().equals("1212")) {
                                    Log.d("rrrrrrrrrrrr", "onResponse: response + 1202");
                                    Log.d("rrrrrrrrrrrr", "onResponse: response+1202+fire");
                                    loginBtn.setVisibility(View.VISIBLE);
                                    progressLogin.setVisibility(View.GONE);
                                    SharedHelper.putKey(getApplicationContext(), LOG_IN_FROM, "admin");
                                    SharedHelper.putKey(getApplicationContext(), IS_LOGIN, "yes");
                                    SharedHelper.putKey(getApplicationContext(), TOKEN, response.body().getToken());
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();



                                    // raniaabdel001@gmail.com
                                }else {
                                    Log.d("rrrrrrrrrrrr", "onResponse: response+ 1202 null");
                                    loginBtn.setVisibility(View.VISIBLE);
                                    progressLogin.setVisibility(View.GONE);

                                }
                            }else {
                                Log.d("rrrrrrrrrrrr", "onResponse: response+body = null");
                                loginBtn.setVisibility(View.VISIBLE);
                                progressLogin.setVisibility(View.GONE);

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
//        getCallLogs();\\n
        //newjob();

        String strJsonBody = "{"
                + "\"app_id\": \"5eb5a37e-b458-11e3-ac11-000c2940e62c\","
                + "\"include_player_ids\": [\"6392d91a-b206-4b7b-a620-cd68e32c3a76\",\"76ece62b-bcfe-468c-8a78-839aeaa8c5fa\",\"8e0f21fa-9a5a-4ae7-a9a6-ca1f24294b86\"],"
                + "\"data\": {\"foo\": \"bar\"},"
                + "\"contents\": {\"en\": \"English Message\"}"
                + "}";

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

    }


}


