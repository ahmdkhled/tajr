package com.tajr.tajr.activities;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.tajr.tajr.R;
import com.tajr.tajr.fragments.HoursFragment;
import com.tajr.tajr.fragments.MoneyFragment;
import com.tajr.tajr.fragments.PointsFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WorkHistoryActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_history);
        getSupportActionBar().setTitle(getString(R.string.analytics));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(this.getString(R.string.money), MoneyFragment.class)
                .add(this.getString(R.string.points), PointsFragment.class)
                .add(this.getString(R.string.hours), HoursFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

}
