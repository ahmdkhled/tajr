package com.greyeg.tajr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.greyeg.tajr.R;
import com.greyeg.tajr.activities.LoginActivity;
import com.greyeg.tajr.helper.SessionManager;
import com.greyeg.tajr.helper.SharedHelper;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdapter extends BaseAdapter {

	private List<DummyModel> mDrawerItems;
	private LayoutInflater mInflater;

	Context context;
	public DrawerAdapter(Context context) {
	    this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDrawerItems =  getTravelDummyList();
	}

	public ArrayList<DummyModel> getTravelDummyList() {
		ArrayList<DummyModel> list = new ArrayList<>();
		list.add(new DummyModel(0, "", context.getString(R.string.settings), R.string.material_icon_settings));
		list.add(new DummyModel(1, "", context.getString(R.string.analytics), R.string.material_icon_settings));
		list.add(new DummyModel(6, "", context.getString(R.string.cards), R.string.material_icon_settings));
		list.add(new DummyModel(3, "", context.getString(R.string.Balance), R.string.material_icon_settings));
		list.add(new DummyModel(4, "", context.getString(R.string.chat), R.string.material_icon_settings));
		list.add(new DummyModel(5, "", context.getString(R.string.recordes), R.string.material_icon_settings));

		list.add(new DummyModel(6, "", context.getString(R.string.logout), R.string.material_icon_settings));
		if (SessionManager.getInstance(context).getLogInFrom().equals("admin")){
			list.add(new DummyModel(7, "", context.getString(R.string.amin_records), R.string.material_icon_settings));
		}




		return list;
	}
	@Override
	public int getCount() {
		return mDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mDrawerItems.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.list_view_item_navigation_drawer_travel, parent,
					false);
			holder = new ViewHolder();
			holder.dividerTop = convertView.findViewById(R.id.divider_top);
			holder.icon = (TextView) convertView.findViewById(R.id.icon);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.dividerBottom = convertView
					.findViewById(R.id.divider_bottom);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DummyModel item = mDrawerItems.get(position);

		holder.icon.setText(item.getIconRes());
		holder.title.setText(item.getText());

		if (position != 0)
			holder.dividerTop.setVisibility(View.GONE);
		return convertView;
	}

	private static class ViewHolder {
		public TextView icon;
		public/* Roboto */TextView title;
		public View dividerTop;
		public View dividerBottom;
	}
}
