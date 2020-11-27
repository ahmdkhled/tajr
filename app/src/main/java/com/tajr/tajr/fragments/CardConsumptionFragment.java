package com.tajr.tajr.fragments;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tajr.tajr.R;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardConsumptionFragment extends Fragment {

    private int mSeries1Index;
    private int mSeries2Index;


    public CardConsumptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_consumption, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DecoView decoView = (DecoView) view.findViewById(R.id.dynamicArcView);
        decoView.deleteAll();
        decoView.configureAngles(280, 0);

        final float seriesMax = 100f;
        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(32f))
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(32f))
                .setSeriesLabel(new SeriesLabel.Builder(getString(R.string.expired) + " %.0f%%")
                        .setColorText(Color.argb(255, 64, 196, 0))
                        .build())

                .build();

        mSeries1Index = decoView.addSeries(seriesItem1);
        float max = 0;
        SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getActivity());
        max = pref1.getFloat("cards_amount",100f);
        float usage = pref1.getFloat("cards_usage",0f);
        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 64, 0, 196))
                .setRange(0, max, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(22f))
                .setSpinDuration(3000)
                .setSeriesLabel(new SeriesLabel.Builder(getString(R.string.used) +" "+(int)usage+" "+getString(R.string.minute))
                        .build())
                .build();

        mSeries2Index = decoView.addSeries(seriesItem2);

        final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        textPercent.setVisibility(View.VISIBLE);
        decoView.executeReset();

        final View[] linkedViews = {textPercent};

        decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(500)
                .setDuration(2000)
                .setLinkedViews(linkedViews)
                .build());

        decoView.addEvent(new DecoEvent.Builder(25).setIndex(mSeries1Index).setDelay(3300).build());
        Log.d("minutesUsage", "minutesUsage: "+pref1.getFloat("cards_usage",0f));
        decoView.addEvent(new DecoEvent.Builder((int)usage).setIndex(mSeries2Index).setDelay(4250).setDuration(2000).build());


    }

    /**
     * Convert base dip into pixels based on the display metrics of the current device
     *
     * @param base dip value
     * @return pixels from base dip
     */
    protected float getDimension(float base) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, base, getResources().getDisplayMetrics());
    }

}
