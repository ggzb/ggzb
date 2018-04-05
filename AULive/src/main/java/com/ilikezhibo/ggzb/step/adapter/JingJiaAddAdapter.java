package com.ilikezhibo.ggzb.step.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.step.UpLoadFileEntity;

/**
 * @ClassName: JingJiaAddAdapter
 * @Description: JingJiaAddAdapter
 * @author big
 * @date 2014-3-27 下午4:12:57
 * 
 */
public class JingJiaAddAdapter extends BaseAdapter {
	private Context context;

	private ViewHolder holder;

	private ArrayList<UpLoadFileEntity> entities;

	public JingJiaAddAdapter(Context context) {
		this.context = context;
	}

	public void setEntities(ArrayList<UpLoadFileEntity> entities) {
		this.entities = entities;
	}

	@Override
	public int getCount() {
		if (entities != null && entities.size() > 0) {
			return entities.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {

		return entities.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.shaiwu_gridview_item, null);
			holder.headImg = (ImageView) convertView
					.findViewById(R.id.content_image);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(entities.get(position).getUrl(),
				holder.headImg, AULiveApplication.getGlobalImgOptions());
		return convertView;
	}

	class ViewHolder {
		ImageView headImg;
	}
}
