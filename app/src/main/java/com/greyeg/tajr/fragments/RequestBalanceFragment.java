package com.greyeg.tajr.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.BalanceActivity;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.font.RobotoTextView;
import com.greyeg.tajr.models.MoneyRequestResponse;
import com.greyeg.tajr.models.ToalAvailableBalance;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.utils.FloatLabeledEditText;
import com.greyeg.tajr.view.utils.ProgressWheel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestBalanceFragment extends Fragment {

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
    private final String TAG = "RequestBalanceFragment";
    @BindView(R.id.loginbtn)
    RobotoTextView loginBtn;

    @BindView(R.id.money)
    FloatLabeledEditText money;

    @BindView(R.id.notes)
    FloatLabeledEditText notes;

    @BindView(R.id.phone)
    FloatLabeledEditText phone;

    @BindView(R.id.balance_time)
    Spinner balanceTimeSpinner;

    @BindView(R.id.progress_log_in)
    ProgressWheel progressLogin;

    ProgressDialog progressDialog;
    Api api;

    @BindView(R.id.available_balance)
    TextView availableBalance;
    ArrayList<String> timeTypeRequest;

    public RequestBalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setTimeTypeRequest();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.wati_to_log_in));
        api = BaseClient.getBaseClient().create(Api.class);
        initBalanceTimeSpinner();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setVisibility(View.GONE);
                progressLogin.setVisibility(View.VISIBLE);
                progressLogin.spin();
                if (money.getText().equals("") || notes.getText().equals("") || phone.getText().equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "برجاء ادخال البريد وكلمة المرور", Toast.LENGTH_SHORT).show();
                } else {
                    api.requestCash(
                            SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                            SharedHelper.getKey(getActivity(), LoginActivity.USER_ID),
                            phone.getText().toString(),
                            money.getText().toString(),
                            notes.getText().toString()
                    ).enqueue(new Callback<MoneyRequestResponse>() {
                        @Override
                        public void onResponse(Call<MoneyRequestResponse> call, Response<MoneyRequestResponse> response) {
                            Log.d("eeeeeeeeee", "response: " + response.body().getData());
                            if (response.body().getCode().equals("1200") || response.body().getCode().equals("1202")) {
                                Toast.makeText(getActivity(), "تم طلب سحب المال بنجاح", Toast.LENGTH_SHORT).show();

                                BalanceActivity.activity.recreate();

                            }
                            progressLogin.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);

                            showDialog(response.body().getData());
                        }

                        @Override
                        public void onFailure(Call<MoneyRequestResponse> call, Throwable t) {
                            Log.d("eeeeeeeeee", "onFailure: " + t.getMessage());
                            progressLogin.setVisibility(View.GONE);
                            loginBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
//        getCallLogs();
    }

    private void showDialog(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_warning);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    void initBalanceTimeSpinner() {

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.layout_cities_spinner_item, getTimeTypes());
        balanceTimeSpinner.setAdapter(adapter);
        balanceTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position <= 2) {
                    api.getNormalAvailableBalance(
                            SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                            getTypeRequest(position)
                    ).enqueue(new Callback<ToalAvailableBalance>() {
                        @Override
                        public void onResponse(Call<ToalAvailableBalance> call, Response<ToalAvailableBalance> response) {

                            if (response.body().getCode().equals("1408") || response.body().getCode().equals("1407")) {
                                SharedHelper.putKey(getActivity(), IS_LOGIN, "اعادة تسجيل الدخول");
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            } else if (response.body().getCode().equals("1425")) {
                                availableBalance.setText(getString(R.string.notallowed));
                                getActivity().onBackPressed();
                            } else if (response.body().getCode().equals("1400")) {
                                availableBalance.setText("totalAvailableBalance = " + String.valueOf(response.body().getTotalAvailableBalance()) + " ,but " + response.body().getData());
                            } else if (response.body().getCode().equals("1200")) {
                                availableBalance.setText(String.valueOf(response.body().getTotalAvailableBalance()));
                            }

                            Log.d(TAG, "onResponse: " + response.body().toString());
                        }

                        @Override
                        public void onFailure(Call<ToalAvailableBalance> call, Throwable t) {
                            Log.d(TAG, "onFailure: " + t.getMessage());
                            availableBalance.setText(t.getMessage());
                        }
                    });
                } else {
                    showTimeDialog(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<String> getTimeTypes() {
        ArrayList<String> timeType = new ArrayList<>();
        timeType.add(getString(R.string.today));
        timeType.add(getString(R.string.this_month));
        timeType.add(getString(R.string.this_year));
        timeType.add(getString(R.string.specific_year));
        timeType.add(getString(R.string.specific_month));
        timeType.add(getString(R.string.specific_day));
        return timeType;
    }

    private void setTimeTypeRequest() {
        timeTypeRequest = new ArrayList<>();
        timeTypeRequest.add("today");
        timeTypeRequest.add("this_month");
        timeTypeRequest.add("this_year");
        timeTypeRequest.add("specific_year");
        timeTypeRequest.add("specific_month");
        timeTypeRequest.add("specific_day");
    }

    private String getTypeRequest(int position) {
        return timeTypeRequest.get(position);
    }

    private void showTimeDialog(int positin) {
        Dialog timDialog = new Dialog(getActivity());
        timDialog.setContentView(R.layout.dialog_balance_time);
        EditText dayEdt = timDialog.findViewById(R.id.day);
        EditText monthEdt = timDialog.findViewById(R.id.month);
        EditText yearEdt = timDialog.findViewById(R.id.year);

        RobotoTextView ok = timDialog.findViewById(R.id.ok);
        RobotoTextView cancle = timDialog.findViewById(R.id.cancel);
        RobotoTextView message = timDialog.findViewById(R.id.edit_type);

        if (positin == 3) {
            message.setText(getString(R.string.select_year));
            monthEdt.setVisibility(View.GONE);
            dayEdt.setVisibility(View.GONE);
        } else if (positin == 4) {
            message.setText(getString(R.string.select_month));
            dayEdt.setVisibility(View.GONE);
        } else {
            message.setText(getString(R.string.select_day));
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timDialog.dismiss();
                api.getCustomelAvailableBalance(
                        SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                        getTypeRequest(positin),
                        yearEdt.getText().toString(),
                        monthEdt.getText().toString(),
                        dayEdt.getText().toString()
                ).enqueue(new Callback<ToalAvailableBalance>() {
                    @Override
                    public void onResponse(Call<ToalAvailableBalance> call, Response<ToalAvailableBalance> response) {
                        if (response.body().getCode().equals("1200")) {
                            availableBalance.setText("" + response.body().getTotalAvailableBalance());
                        } else if (response.body().getCode().equals("1400")) {
                            availableBalance.setText("" + response.body().getData());
                        } else {
                            availableBalance.setText(R.string.some_thing_went_wrong);
                        }

                    }

                    @Override
                    public void onFailure(Call<ToalAvailableBalance> call, Throwable t) {
                        availableBalance.setText(t.getMessage());
                        availableBalance.setText(t.getMessage());
                    }
                });
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timDialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(timDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        timDialog.show();
        timDialog.getWindow().setAttributes(lp);

    }

//    private void setAnimation(String category) {
//        if (category.equals(SPLASH_SCREEN_OPTION_1)) {
//            mKenBurns.setImageResource(R.drawable.background_media);
//            animation1();
//        } else if (category.equals(SPLASH_SCREEN_OPTION_2)) {
//            //mLogo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color_500));
//            mKenBurns.setImageResource(R.drawable.background_shop);
//            animation2();
//        } else if (category.equals(SPLASH_SCREEN_OPTION_3)) {
//            mKenBurns.setImageResource(R.drawable.ic_traffic);
//            animation2();
//            animation3();
//        }
//    }
//
//    private void animation1() {
//        ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(mLogo, "scaleX", 5.0F, 1.0F);
//        scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        scaleXAnimation.setDuration(1200);
//        ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(mLogo, "scaleY", 5.0F, 1.0F);
//        scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        scaleYAnimation.setDuration(1200);
//        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mLogo, "alpha", 0.0F, 1.0F);
//        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        alphaAnimation.setDuration(1200);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
//        animatorSet.setStartDelay(500);
//        animatorSet.start();
//    }
//
//    private void animation2() {
//        mLogo.setAlpha(1.0F);
//        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_top_to_center);
//        mLogo.startAnimation(anim);
//    }
//
//    private void animation3() {
//
////        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(email, "alpha", 0.0F, 1.0F);
////        alphaAnimation.setStartDelay(1700);
////        alphaAnimation.setDuration(500);
////        alphaAnimation.start();
//
//    }
}