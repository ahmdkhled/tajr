package com.tajr.tajr.records;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tajr.tajr.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class RecordsActivity extends AppCompatActivity {

    final static String TAGMA = "Main Activity";
    DatabaseHandler db = new DatabaseHandler(this);
    RecordAdapter rAdapter;
    RecyclerView recycler;
    List<CallDetails> callDetailsList;
    boolean checkResume = false;
    DatabaseManager databaseManager;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        databaseManager = new DatabaseManager(this);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putInt("numOfCalls", 0).apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Check", "onResume: ");
        if (checkPermission()) {
            // Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            if (checkResume == false) {
                setUi();
                // this.callDetailsList=new DatabaseManager(this).getAllDetails();
                rAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void onPause() {
        super.onPause();
        SharedPreferences pref3 = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref3.getBoolean("pauseStateVLC", false)) {
            checkResume = true;
            pref3.edit().putBoolean("pauseStateVLC", false).apply();
        } else
            checkResume = false;
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    public void setUi() {
        recycler = findViewById(R.id.recyclerView);
        callDetailsList = databaseManager.getAllDetails();

        for (CallDetails cd : callDetailsList) {
            String log = "Phone num : " + cd.getNum() + " | Time : " + cd.getTime1() + " | Date : " + cd.getDate1();
            Log.d("Database ", log);
        }

        Collections.reverse(callDetailsList);
        rAdapter = new RecordAdapter(callDetailsList, this, databaseManager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(rAdapter);

    }


    private boolean checkPermission() {
        int i = 0;
        String[] perm = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS};
        List<String> reqPerm = new ArrayList<>();

        for (String permis : perm) {
            int resultPhone = ContextCompat.checkSelfPermission(RecordsActivity.this, permis);
            if (resultPhone == PackageManager.PERMISSION_GRANTED)
                i++;
            else {
                reqPerm.add(permis);
            }
        }

        if (i == 5)
            return true;
        else
            return requestPermission(reqPerm);
    }


    private boolean requestPermission(List<String> perm) {
        // String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        String[] listReq = new String[perm.size()];
        listReq = perm.toArray(listReq);
        for (String permissions : listReq) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RecordsActivity.this, permissions)) {
                Toast.makeText(getApplicationContext(), "Phone Permissions needed for " + permissions, Toast.LENGTH_LONG);
            }
        }

        ActivityCompat.requestPermissions(RecordsActivity.this, listReq, 1);

        return false;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(), "Permission Granted to access Phone calls", Toast.LENGTH_LONG);
                else
                    Toast.makeText(getApplicationContext(), "You can't access Phone calls", Toast.LENGTH_LONG);
                break;
        }

    }

}
