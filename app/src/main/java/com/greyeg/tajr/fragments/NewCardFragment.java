package com.greyeg.tajr.fragments;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.helper.font.RobotoTextView;
import com.greyeg.tajr.models.CardsResponse;
import com.greyeg.tajr.models.CartResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.utils.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCardFragment extends Fragment {

    private static final String TAG = "NewCardFragment";
    @BindView(R.id.type)
    Spinner typeSpinner;

    @BindView(R.id.amount)
    Spinner amountSpinner;

    @BindView(R.id.request_cart)
    RobotoTextView requestCart;

    @BindView(R.id.copy)
    RobotoTextView copyCart;


    @BindView(R.id.request_view)
    LinearLayout requestView;

    @BindView(R.id.copy_view)
    LinearLayout copy_view;

    @BindView(R.id.progress_bar)
    ProgressWheel proress;

    @BindView(R.id.cart_num)
    TextView cardText;
    List<String> types = new ArrayList<>();

    List<String> etisalats = new ArrayList<>();
    List<String> vodafone = new ArrayList<>();
    List<String> we = new ArrayList<>();
    List<String> orange = new ArrayList<>();

    List<String> amount = new ArrayList<>();

    private String TYPE = "";
    private String AMOUNT = "";

    String cardNum;

    public NewCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);


        Api api = BaseClient.getBaseClient().create(Api.class);
        api.getCards(SharedHelper.getKey(getActivity(), LoginActivity.TOKEN)).enqueue(new Callback<CardsResponse>() {
            @Override
            public void onResponse(Call<CardsResponse> call, Response<CardsResponse> response) {

                if (response.body() != null && response.body().getData() != null) {
                    etisalats.addAll(response.body().getData().getEtisalat());
                    we.addAll(response.body().getData().getWe());
                    orange.addAll(response.body().getData().getOrange());
                    vodafone.addAll(response.body().getData().getVodafone());

                    types.add("vodafone");
                    types.add("we");
                    types.add("orange");
                    types.add("etisalat");

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, types);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeSpinner.setAdapter(adapter);
                    typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            TYPE = String.valueOf(types.get(position));
                            if (position == 0) {
                                amount = vodafone;
                            } else if (position == 1) {
                                amount = we;
                            } else if (position == 2) {
                                amount = orange;
                            } else if (position == 3) {
                                amount = etisalats;
                            }

                            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, amount);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            amountSpinner.setAdapter(adapter);
                            amountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    AMOUNT = String.valueOf(amount.get(position));

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<CardsResponse> call, Throwable t) {

                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

        requestCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proress.setVisibility(View.VISIBLE);
                proress.spin();
                requestCart.setVisibility(View.GONE);
                if (TYPE.equals("") || AMOUNT.equals("")) {
                    proress.setVisibility(View.GONE);
                    requestCart.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "برجاء اختيار كل القيم", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    BaseClient.getBaseClient().create(Api.class).getCartDetails(
                            SharedHelper.getKey(getActivity(), LoginActivity.TOKEN), AMOUNT, TYPE)
                            .enqueue(new Callback<CartResponse>() {
                                @SuppressLint("ApplySharedPref")
                                @Override
                                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                                    if(response.body().getCode().equals("1510")){
                                        Toast.makeText(getActivity(), ""+response.body().getResponse(), Toast.LENGTH_LONG).show();
                                    }
                                    if (response.body().getResponse().equals("success")) {
                                        copy_view.setVisibility(View.VISIBLE);
                                        requestView.setVisibility(View.GONE);
                                        cardNum = response.body().getData().getNumber();
                                        cardText.setText(cardNum);
                                        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        pref1.edit().putFloat("cards_amount", Float.parseFloat(response.body().getData().getMinutes())).apply();
                                        pref1.edit().putFloat("cards_usage",0f).apply();

                                    }

                                }

                                @Override
                                public void onFailure(Call<CartResponse> call, Throwable t) {
                                    proress.setVisibility(View.GONE);
                                    requestCart.setVisibility(View.VISIBLE);
                                }
                            });

                }
            }
        });

        copyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "تم النسخ", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("card_num", cardNum);
                clipboard.setPrimaryClip(clip);

            }
        });
    }


    void copy(String number) {

    }

}
