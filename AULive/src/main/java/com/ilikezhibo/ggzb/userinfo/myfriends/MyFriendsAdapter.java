package com.ilikezhibo.ggzb.userinfo.myfriends;

import com.ilikezhibo.ggzb.AULiveApplication;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.userinfo.ProfileActivity;

/**
 * @ClassName: TopRankAdapter
 * @Description: 主页适配
 * @author big
 * @date 2014-7-8 下午4:12:57
 * 
 */
public class MyFriendsAdapter extends BaseAdapter {
	private Context context;

	private ViewHolder holder;

	private ArrayList<FriendEntity> entities;

	public MyFriendsAdapter(Context context) {
		this.context = context;
	}

	public void setEntities(ArrayList<FriendEntity> entities) {
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
					R.layout.myfriends_list_item, null);

			holder.left_image = (ImageView) convertView
					.findViewById(R.id.left_image);
			holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);

			holder.sex_age_tv = (TextView) convertView
					.findViewById(R.id.sex_age_tv);

			holder.bt_yes = (TextView) convertView.findViewById(R.id.bt_yes);
			holder.bt_no = (TextView) convertView.findViewById(R.id.bt_no);

			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.lstUpTimeTv = (TextView) convertView
					.findViewById(R.id.lstUpTimeTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FriendEntity entity = entities.get(position);

		ImageLoader.getInstance().displayImage(entity.getFace(),
				holder.left_image, AULiveApplication.getGlobalImgOptions());

		holder.left_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent profile_intent = new Intent(context,
						ProfileActivity.class);
				profile_intent.putExtra(ProfileActivity.PROFILE_UID,
						entity.getUid());
				context.startActivity(profile_intent);
			}
		});
		holder.name_tv.setText(entity.getNickname());
		holder.sex_age_tv.setText(entity.getAge());
		if (entity.getSex().equals("1")) {
			holder.sex_age_tv.setBackgroundResource(R.drawable.friend_sex_male);
		} else {
			holder.sex_age_tv
					.setBackgroundResource(R.drawable.friend_sex_female);
		}

		holder.distance.setText(entity.getDistance());
		holder.lstUpTimeTv.setText(entity.getTime());
		holder.bt_yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				doAgree(entity);
			}
		});
		holder.bt_no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				doReject(entity);
			}
		});

		if (entity.getStatus().equals("0")) {
			holder.bt_yes.setVisibility(View.VISIBLE);
			holder.bt_no.setVisibility(View.VISIBLE);
		} else {
			holder.bt_yes.setVisibility(View.INVISIBLE);
			holder.bt_no.setVisibility(View.INVISIBLE);
		}
		return convertView;

	}

	class ViewHolder {
		ImageView left_image;
		TextView name_tv;
		TextView sex_age_tv;

		TextView bt_yes;
		TextView bt_no;
		TextView distance;
		TextView lstUpTimeTv;
	}

	private void doAgree(final FriendEntity entity) {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/friend/agree?user=" + entity.getUid(),
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}
				if (callback.getStat() == 200) {

					Utils.showMessage("同意成功");
					entity.setStatus("2");
					MyFriendsAdapter.this.notifyDataSetChanged();

				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage("获取网络数据失败");
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	private void doReject(final FriendEntity entity) {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/friend/reject?user=" + entity.getUid(),
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}
				if (callback.getStat() == 200) {

					Utils.showMessage("拒绝成功");
					entities.remove(entity);
					MyFriendsAdapter.this.notifyDataSetChanged();

				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage("获取网络数据失败");
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

}
