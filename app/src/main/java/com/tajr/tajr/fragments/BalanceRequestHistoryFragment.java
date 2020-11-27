package com.tajr.tajr.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tajr.tajr.R;
import com.tajr.tajr.activities.LoginActivity;
import com.tajr.tajr.adapters.CashHistoryAdapter;
import com.tajr.tajr.helper.SharedHelper;
import com.tajr.tajr.models.CashRequestHistory;
import com.tajr.tajr.server.Api;
import com.tajr.tajr.server.BaseClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BalanceRequestHistoryFragment extends Fragment {

    @BindView(R.id.cashHistoryRecyclerView)
    RecyclerView cashRecyclerView;
    CashHistoryAdapter adapter ;
    LinearLayoutManager linearLayoutManager;

    public BalanceRequestHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_balance_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        cashRecyclerView.setLayoutManager(linearLayoutManager);
        Api api = BaseClient.getBaseClient().create(Api.class);
        api.getAvailableBalance(
                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN)).enqueue(new Callback<CashRequestHistory>() {
            @Override
            public void onResponse(Call<CashRequestHistory> call, Response<CashRequestHistory> response) {

                if (response.body().getCode().equals("1200")||response.body().getCode().equals("1202")){

                    adapter = new CashHistoryAdapter(response.body().getData(),getActivity());
                    cashRecyclerView.setAdapter(adapter);

                }
                Log.d("uuuuuuuuuu", "onResponse: "+response.body().getData().get(0).getCash());

            }

            @Override
            public void onFailure(Call<CashRequestHistory> call, Throwable t) {
                Log.d("uuuuuuuuuu", "onFailure: " + t.getMessage());
            }
        });
    }
}
