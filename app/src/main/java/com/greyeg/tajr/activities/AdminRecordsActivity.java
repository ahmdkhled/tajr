package com.greyeg.tajr.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greyeg.tajr.R;
import com.greyeg.tajr.adapters.AdminRecordsAdapter;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.models.AdminRecordsResponse;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRecordsActivity extends AppCompatActivity {

    @BindView(R.id.records_recycler_view)
    RecyclerView recordsRecyclerView;

    List<AdminRecordsResponse.Record> records;
    LinearLayoutManager linearLayoutManager;
    AdminRecordsAdapter adapter;
    private String token= SessionManager.getInstance(this).getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_records);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.all_ecords);
        linearLayoutManager = new LinearLayoutManager(this);
        recordsRecyclerView.setLayoutManager(linearLayoutManager);
        BaseClient.getBaseClient().create(Api.class).getRecords(token)
                .enqueue(new Callback<AdminRecordsResponse>() {
                    @Override
                    public void onResponse(Call<AdminRecordsResponse> call, Response<AdminRecordsResponse> response) {
                        recordsRecyclerView.setAdapter(new AdminRecordsAdapter(response.body().getRecords(),getApplicationContext()));
                    }

                    @Override
                    public void onFailure(Call<AdminRecordsResponse> call, Throwable t) {

                    }
                });
    }
}
