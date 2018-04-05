package com.ilikezhibo.ggzb.xiangmu.adapter;

import com.ilikezhibo.ggzb.AULiveApplication;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.userinfo.ProfileActivity;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.xiangmu.RoundImageView;
import com.ilikezhibo.ggzb.xiangmu.entity.CommentEntity;

/**
 * @ClassName: JingJiaDetailAdapter
 * @Description: 主页适配
 * @author big
 * @date 2014-8-14 下午4:12:57
 * 
 */
public class JingJiaDetailAdapter extends BaseAdapter {
	private Context context;

	private ViewHolder holder;

	private ArrayList<CommentEntity> entities;

	public JingJiaDetailAdapter(Context context) {
		this.context = context;
	}

	public void setEntities(ArrayList<CommentEntity> entities) {
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
					R.layout.forum_review_item, null);
			holder.iv_face = (RoundImageView) convertView
					.findViewById(R.id.iv_face);
			holder.tv_nickname = (TextView) convertView
					.findViewById(R.id.tv_nickname);
			holder.tv_lousu = (TextView) convertView
					.findViewById(R.id.tv_lousu);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_comment = (TextView) convertView
					.findViewById(R.id.tv_comment);

			holder.linearFirstReply = (LinearLayout) convertView
					.findViewById(R.id.linearFirstReply);
			holder.first_line_name = (TextView) convertView
					.findViewById(R.id.first_line_name);
			holder.first_line_content = (TextView) convertView
					.findViewById(R.id.first_line_content);
			holder.first_line_time = (TextView) convertView
					.findViewById(R.id.first_line_time);

			holder.linearSecondReply = (LinearLayout) convertView
					.findViewById(R.id.linearSecondReply);
			holder.second_line_name = (TextView) convertView
					.findViewById(R.id.second_line_name);
			holder.second_line_content = (TextView) convertView
					.findViewById(R.id.second_line_content);
			holder.second_line_time = (TextView) convertView
					.findViewById(R.id.second_line_time);

			holder.linearMoreReply = (LinearLayout) convertView
					.findViewById(R.id.linearMoreReply);
			holder.linearMoreReply.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final CommentEntity entity = entities.get(position);

		ImageLoader.getInstance().displayImage(entity.getFace(),
				holder.iv_face, AULiveApplication.getGlobalImgOptions());
		holder.iv_face.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent profile_intent = new Intent(context,
						ProfileActivity.class);
				profile_intent.putExtra(ProfileActivity.PROFILE_UID,
						entity.getUid());
				context.startActivity(profile_intent);
			}
		});

		holder.tv_nickname.setText(entity.getNickname());
		holder.tv_lousu.setText((position + 1) + "楼");
		holder.tv_time.setText(entity.getAdd_time());
		holder.tv_comment.setText(entity.getCont());
		// 作二级回复处理
		// int huifu_total;
		// try {
		// huifu_total = Integer.parseInt(entity.getHuifu_total());
		// } catch (Exception exception) {
		// huifu_total = 0;
		// }
		//
		// if (huifu_total == 0) {
		// holder.linearMoreReply.setVisibility(View.GONE);
		// }
		// if (huifu_total >= 1) {
		// holder.linearFirstReply.setVisibility(View.VISIBLE);
		// holder.first_line_name.setText(entity.getHuifu().get(0)
		// .getNickname());
		// holder.first_line_content.setText(entity.getHuifu().get(0)
		// .getMemo());
		// holder.first_line_time.setText(entity.getHuifu().get(0).getTime());
		// }
		//
		// if (huifu_total >= 2) {
		// holder.linearSecondReply.setVisibility(View.VISIBLE);
		// holder.second_line_name.setText(entity.getHuifu().get(0)
		// .getNickname());
		// holder.second_line_content.setText(entity.getHuifu().get(0)
		// .getMemo());
		// holder.second_line_time.setText(entity.getHuifu().get(0).getTime());
		// }
		//
		// if (huifu_total > 2) {
		// holder.linearMoreReply.setVisibility(View.VISIBLE);
		// }

		return convertView;
	}

	class ViewHolder {
		RoundImageView iv_face;
		TextView tv_nickname;
		TextView tv_lousu;
		TextView tv_time;
		TextView tv_comment;

		LinearLayout linearFirstReply;
		TextView first_line_name;
		TextView first_line_content;
		TextView first_line_time;

		LinearLayout linearSecondReply;
		TextView second_line_name;
		TextView second_line_content;
		TextView second_line_time;

		LinearLayout linearMoreReply;
	}

	private CustomProgressDialog progressDialog = null;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context);
			progressDialog.setMessage("正在加载中...");
		}

		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

}
