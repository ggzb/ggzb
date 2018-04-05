package com.ilikezhibo.ggzb.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.news.entity.NewEntity;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;
import java.util.ArrayList;

/**
 * @ClassName: ExperienceFragmentAdapter
 * @Description: 主页适配
 * @author big
 * @date 2014-7-8 下午4:12:57
 * 
 */
public class NewsFragmentAdapter extends BaseAdapter {
	private Context context;

	private ViewHolder holder;

	private ArrayList<NewEntity> entities;

	public NewsFragmentAdapter(Context context) {
		this.context = context;
	}

	public void setEntities(ArrayList<NewEntity> entities) {
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
					R.layout.news_listview_item, null);

			holder.main_image = (ImageView) convertView
					.findViewById(R.id.main_image);
			holder.shere_iv = (ImageView) convertView
					.findViewById(R.id.shere_iv);

			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.tv_nickname = (TextView) convertView
					.findViewById(R.id.tv_nickname);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.comment_count = (TextView) convertView
					.findViewById(R.id.comment_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final NewEntity entity = entities.get(position);

		String pic_url = entity.getPics();
		// final String[] pic_urls = pic_url.split(",");

		ImageLoader.getInstance().displayImage(pic_url, holder.main_image,
				AULiveApplication.getGlobalImgOptions());
		holder.tv_title.setText(entity.getIntro());
		// Html.fromHtml(
		holder.content.setText(entity.getTitle());
		holder.tv_nickname.setText(entity.getNickname());
		holder.tv_time.setText(entity.getAdd_time());
		holder.comment_count.setText(entity.getComm_total());

		holder.shere_iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				doShare(entity);
			}
		});

		return convertView;

	}

   ShareHelper shareDialog = null;

	// 分享相关
	private void doShare(NewEntity mNewEntity) {

		String target_url = null;
		try {
			target_url = "http://phone.qxj.me/news/share?id="
					+ mNewEntity.getId();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (shareDialog == null) {
			shareDialog = new ShareHelper(context);
		}

		shareDialog.setShareUrl(target_url);
		shareDialog.setShareTitle(Utils.trans(R.string.app_name));
		shareDialog
				.setShareContent(mNewEntity.getTitle(), mNewEntity.getPics());

		if (!shareDialog.isShowing()) {
			shareDialog.show();
		}
	}

	class ViewHolder {
		// 分享
		ImageView main_image;
		ImageView shere_iv;
		TextView tv_title;
		TextView content;
		TextView tv_nickname;
		TextView tv_time;
		TextView comment_count;

	}
}
