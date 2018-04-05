package com.ilikezhibo.ggzb.xiangmu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.step.ProjectTypeEntity;

public class SearchAdapter extends BaseAdapter {
	private Context context;
	public List<ProjectTypeEntity> channelList;
	private TextView item_text;

	public SearchAdapter(Context context, List<ProjectTypeEntity> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public ProjectTypeEntity getItem(int position) {
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.search_item,
				null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		final ProjectTypeEntity channel = getItem(position);
		item_text.setText(channel.getName());

		item_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				setselect(channel);

			}
		});
		if (channel.isIs_pressed()) {
			item_text.setSelected(true);
		} else {
			item_text.setSelected(false);
		}

		return view;
	}

	private void setselect(ProjectTypeEntity pte) {
		for (ProjectTypeEntity projectTypeEntity : channelList) {

			if (projectTypeEntity == pte) {
				projectTypeEntity.setIs_pressed(true);
			} else {
				projectTypeEntity.setIs_pressed(false);
			}

		}
		SearchAdapter.this.notifyDataSetChanged();

	}

}