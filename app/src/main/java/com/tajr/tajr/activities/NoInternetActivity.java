package com.tajr.tajr.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.tajr.tajr.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoInternetActivity extends AppCompatActivity {

    @BindView(R.id.ken_burns_images)
    KenBurnsView mKenBurns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        getSupportActionBar().setTitle(R.string.no_internet);
        ButterKnife.bind(this);
        registerBroadCastReceiver();
        setAnimation();
    }

    private void setAnimation() {
        mKenBurns.setImageResource(R.drawable.ic_no_internet);
    }

    BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (!noConnectivity) {
                onConnectionFound();
            } else {
                onConnectionLost();
            }
        }
    };

    public void onConnectionLost() {
//        Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivityForResult(intent, 8523);
//        Toast.makeText(this, "مفيش نت", Toast.LENGTH_SHORT).show();
    }

    public void onConnectionFound() {
       setResult(8523);
       finish();
    }

    private void registerBroadCastReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }
}
