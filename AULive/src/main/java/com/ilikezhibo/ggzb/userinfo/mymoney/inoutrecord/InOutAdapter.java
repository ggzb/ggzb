package com.ilikezhibo.ggzb.userinfo.mymoney.inoutrecord;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;

/**
 * @ClassName: ExperienceFragmentAdapter
 * @Description: 主页适配
 * @author big
 * @date 2014-7-8 下午4:12:57
 * 
 */
public class InOutAdapter extends BaseAdapter {
	private Context context;

	private ViewHolder holder;

	private ArrayList<InOutEntity> entities;

	public InOutAdapter(Context context) {
		this.context = context;
	}

	public void setEntities(ArrayList<InOutEntity> entities) {
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

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null || convertView.getTag() == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.shuozhijilu_item, null);

			holder.tv_memo = (TextView) convertView.findViewById(R.id.tv_memo);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.money_tv = (TextView) convertView
					.findViewById(R.id.money_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final InOutEntity entity = entities.get(position);

		// String pic_url = entity.getPics();
		//
		// ImageLoader.getInstance().displayImage(pic_url, holder.main_image,
		// AULiveApplication.getGlobalImgOptions());

		//Html.fromHtml(
		holder.tv_memo.setText(entity.getMemo());
		holder.tv_time.setText(entity.getAdd_time());
		holder.money_tv.setText(entity.getMoney()+"元");
	 
		return convertView;

	}

	ShareHelper shareDialog = null;

	class ViewHolder {

		TextView tv_memo;
		TextView tv_time;
		TextView money_tv;

	}
}
