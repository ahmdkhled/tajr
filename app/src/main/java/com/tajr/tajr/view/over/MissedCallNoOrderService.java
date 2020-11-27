package com.tajr.tajr.view.over;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;

import com.tajr.tajr.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.mattcarroll.hover.HoverMenu;
import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.HoverMenuService;

public class MissedCallNoOrderService extends HoverMenuService {

    private static final String TAG = "MultipleSectionsHoverMenuService";

    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        hoverView.setMenu(createHoverMenu());
        hoverView.collapse();
    }

    @NonNull
    private HoverMenu createHoverMenu() {
        return new MultiSectionHoverMenu(getApplicationContext());
    }

    private static class MultiSectionHoverMenu extends HoverMenu {

        private final Context mContext;
        private final List<Section> mSections;

        public MultiSectionHoverMenu(@NonNull Context context) {
            mContext = context.getApplicationContext();

            mSections = Arrays.asList(
                    new Section(
                            new SectionId("1"),
                            createTabNewOrderView(),
                            new MissedCallScreenNewOrderContent(mContext)
                    )
            );
        }


        private View createTabNewOrderView() {
            Resources resources = mContext.getResources();

            DemoTabView view = new DemoTabView(
                    mContext,
                    resources.getDrawable(R.drawable.tab_background),
                    resources.getDrawable(R.drawable.ic_bags2)
            );
            view.setTabBackgroundColor(0xff9600);
            view.setTabForegroundColor(null);
            return view;
        }


        private View createTabNSearchView() {
            Resources resources = mContext.getResources();

            DemoTabView view = new DemoTabView(
                    mContext,
                    resources.getDrawable(R.drawable.tab_background),
                    resources.getDrawable(R.drawable.ic_search_white)
            );
            view.setTabBackgroundColor(0xff9600);
            view.setTabForegroundColor(null);
            return view;
        }

        @Override
        public String getId() {
            return "multisectionmenu";
        }

        @Override
        public int getSectionCount() {
            return mSections.size();
        }

        @Nullable
        @Override
        public Section getSection(int index) {
            return mSections.get(index);
        }

        @Nullable
        @Override
        public Section getSection(@NonNull SectionId sectionId) {
            for (Section section : mSections) {
                if (section.getId().equals(sectionId)) {
                    return section;
                }
            }
            return null;
        }

        @NonNull
        @Override
        public List<Section> getSections() {
            return new ArrayList<>(mSections);
        }
    }

}

