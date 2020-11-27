package com.tajr.tajr.activities;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.tajr.tajr.R;
import com.tajr.tajr.fragments.RequestBalanceFragment;
import com.tajr.tajr.fragments.BalanceRequestHistoryFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BalanceActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

   public static   Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(this.getString(R.string.request_money), RequestBalanceFragment.class)
                .add(this.getString(R.string.balance_history), BalanceRequestHistoryFragment.class).create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }
}
