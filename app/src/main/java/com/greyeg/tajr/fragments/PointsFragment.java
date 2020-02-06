package com.greyeg.tajr.fragments;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.SharedHelper;
import com.greyeg.tajr.models.PointsHistory;
import com.greyeg.tajr.server.Api;
import com.greyeg.tajr.server.BaseClient;
import com.greyeg.tajr.view.utils.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PointsFragment extends Fragment {

    // private AnimatedExpandableListView listView;
    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;

    private ImageView mImage;
    private TextView mName;
    private TextView mPlace;

    public PointsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_points, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Api api = BaseClient.getBaseClient().create(Api.class);

        adapter = new ExampleAdapter(getActivity());
        listView = view.findViewById(R.id.expandable_lv_social_list_view);


        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        // Set indicator (arrow) to the right
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            listView.setIndicatorBounds(width - px, width);
        } else {
            listView.setIndicatorBoundsRelative(width - px, width);
        }

        api.getPointsHistory(
                SharedHelper.getKey(getActivity(), LoginActivity.TOKEN),
                SharedHelper.getKey(getActivity(), LoginActivity.USER_ID)
        ).enqueue(new Callback<PointsHistory>() {
            @Override
            public void onResponse(Call<PointsHistory> call, Response<PointsHistory> response) {

                if (response.body() != null&&!response.body().getCode().equals("1400")) {
                    // if (response.body().getHistory().size()>0){
                    List<PointsHistory.Year> years = response.body().getHistory();
                    List<GroupItem> items = new ArrayList<GroupItem>();
                    GroupItem item = null;
                    for (PointsHistory.Year year : years) {
                        item = new GroupItem();
                        item.year = year.getYear();
                        ChildItem child;
                        for (PointsHistory.Year.Month month : year.getMonth()) {
                            child = new ChildItem();
                            child.month = month.getName_ar();
                            child.hours = month.getPoints();
                            item.items.add(child);
                        }
                    }
                    items.add(item);
                    //items = fillData(items);

                    adapter.setData(items);
                    listView.setAdapter((ExpandableListAdapter) adapter);
                    // }
                }

            }

            @Override
            public void onFailure(Call<PointsHistory> call, Throwable t) {
                Log.d("uuuuuuuuuu", "onFailure: " + t.getMessage());
            }
        });

    }

    private static class GroupItem {
        String year;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String monthEn;
        String month;
        String hours;
    }

    private static class ChildHolder {
        TextView type;
        TextView month;
        TextView hours;
    }

    private static class GroupHolder {
        TextView year;
    }


    private List<GroupItem> fillData(List<GroupItem> items) {
        GroupItem item = new GroupItem();
        item.year = "2018";
        ChildItem child;

        child = new ChildItem();
        child.month = "يناير";
        child.hours = "03:05:5";
        item.items.add(child);


        child = new ChildItem();
        child.month = "فبراير";
        child.hours = "03:05:5";
        item.items.add(child);

        items.add(item);

        return items;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(
                        R.layout.list_item_expandable_social_child, parent,
                        false);
                holder.month = convertView
                        .findViewById(R.id.month);
                holder.hours = convertView
                        .findViewById(R.id.hours);
                holder.type = convertView
                        .findViewById(R.id.type);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.type.setText(getString(R.string.points));
            holder.month.setText(item.month);
            holder.hours.setText(item.hours);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(
                        R.layout.list_item_expandable_social, parent, false);

                holder.year = (TextView) convertView
                        .findViewById(R.id.year);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.year.setText(item.year);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }
    }
}
