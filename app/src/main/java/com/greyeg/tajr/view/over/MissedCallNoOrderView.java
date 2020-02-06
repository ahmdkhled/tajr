package com.greyeg.tajr.view.over;

import android.content.Context;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.greyeg.tajr.R;

import java.util.Collections;
import java.util.List;

import io.mattcarroll.hover.HoverMenu;

public class MissedCallNoOrderView extends HoverMenu {

    private final Context mContext;
    private final String mMenuId;
    private final Section mSection;

    public MissedCallNoOrderView(Context mContext, String mMenuId) {
        this.mContext = mContext.getApplicationContext();
        this.mMenuId = mMenuId;
        this.mSection = new Section(
                new SectionId("0"),
                createTabView(),
                new MissedCallScreenNewOrderContent(mContext)
        );
    }

    private View createTabView() {
        Resources resources = mContext.getResources();

        DemoTabView view = new DemoTabView(
                mContext,
                resources.getDrawable(R.drawable.tab_background),
                resources.getDrawable(R.drawable.ic_launcher)
        );
        view.setTabBackgroundColor(0xff9600);
        view.setTabForegroundColor(null);
        return view;
    }


    @Override
    public String getId() {
        return mMenuId;
    }

    @Override
    public int getSectionCount() {
        return 1;
    }

    @Nullable
    @Override
    public Section getSection(int index) {
        return mSection;
    }

    @Nullable
    @Override
    public Section getSection(@NonNull SectionId sectionId) {
        return mSection;
    }

    @NonNull
    @Override
    public List<Section> getSections() {
        return Collections.singletonList(mSection);
    }
}