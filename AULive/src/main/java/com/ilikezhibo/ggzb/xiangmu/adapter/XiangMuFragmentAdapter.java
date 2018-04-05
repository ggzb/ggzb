package com.ilikezhibo.ggzb.xiangmu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.userinfo.ProfileActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;

/**
 * @ClassName: QiuZuFragmentAdapter
 * @Description: 主页适配
 * @author big
 * @date 2014-7-8 下午4:12:57
 * 
 */
public class XiangMuFragmentAdapter extends BaseAdapter {
	private Context context;

	private ViewHolder holder;

	private ArrayList<XiangMuEntity> entities;

	public XiangMuFragmentAdapter(Context context) {
		this.context = context;
	}

	public void setEntities(ArrayList<XiangMuEntity> entities) {
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
					R.layout.jiesihuo_home_list_item, null);

			holder.left_image = (ImageView) convertView
					.findViewById(R.id.left_image);
			holder.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
			holder.position_tv = (TextView) convertView
					.findViewById(R.id.position_tv);

			holder.title_tv = (TextView) convertView
					.findViewById(R.id.title_tv);
			holder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
			holder.location_tv = (TextView) convertView
					.findViewById(R.id.location_tv);
			holder.pepple_tv = (TextView) convertView
					.findViewById(R.id.pepple_tv);
			holder.memo_tv = (TextView) convertView.findViewById(R.id.memo_tv);
			holder.money_tv = (TextView) convertView
					.findViewById(R.id.money_tv);

			holder.chat_layout = (LinearLayout) convertView
					.findViewById(R.id.chat_layout);
			holder.share_layout = (LinearLayout) convertView
					.findViewById(R.id.share_layout);
			holder.fav_layout = (LinearLayout) convertView
					.findViewById(R.id.fav_layout);
			holder.add_layout = (LinearLayout) convertView
					.findViewById(R.id.add_layout);
			holder.cert_layout = (LinearLayout) convertView
					.findViewById(R.id.cert_layout);

			holder.chat_tv = (TextView) convertView.findViewById(R.id.chat_tv);
			holder.fav_tv = (TextView) convertView.findViewById(R.id.fav_tv);
			holder.add_tv = (TextView) convertView.findViewById(R.id.add_tv);
			holder.iv_fav = (ImageView) convertView.findViewById(R.id.iv_fav);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final XiangMuEntity entity = entities.get(position);

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.face_male)
				.showImageForEmptyUri(R.drawable.face_male)
				.showImageOnFail(R.drawable.face_male).cacheInMemory()
				.cacheOnDisc().build();

		ImageLoader.getInstance().displayImage(entity.getFace(),
				holder.left_image, options);

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
		holder.position_tv.setText("发布时间:" + entity.getAdd_time());

		holder.title_tv.setText(entity.getTitle());
		holder.type_tv.setText(entity.getCategory_name());
		holder.location_tv.setText(entity.getAddr());
		holder.pepple_tv.setText(entity.getSkills());
		holder.money_tv.setText(entity.getBudget() + "元");
		holder.memo_tv.setText(entity.getMemo());

		holder.chat_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		holder.share_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doShare(entity);
			}
		});

		// 0否 1是
		if (entity.getProject_follower().equals("0")) {
			holder.iv_fav.setImageResource(R.drawable.home_list_item_fav);
			holder.fav_layout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 收藏
					doFav(entity.getId(), entity);
				}
			});
		} else {
			holder.iv_fav.setImageResource(R.drawable.home_list_item_fav_press);
			holder.fav_layout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 删除收藏
					doDelFav(entity.getId(), entity);
				}
			});
		}

		holder.add_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
		holder.cert_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		holder.chat_tv.setText(entity.getComm_total());
		holder.fav_tv.setText(entity.getAtten_total());
		holder.add_tv.setText(entity.getApply_total());
		return convertView;

	}

	class ViewHolder {
		// 分享
		ImageView left_image;
		TextView name_tv;
		TextView position_tv;

		TextView title_tv;
		TextView type_tv;
		TextView location_tv;
		TextView pepple_tv;
		TextView memo_tv;
		TextView money_tv;

		LinearLayout chat_layout;
		LinearLayout share_layout;
		LinearLayout fav_layout;
		LinearLayout add_layout;
		LinearLayout cert_layout;

		TextView chat_tv;
		TextView fav_tv;
		TextView add_tv;

		ImageView iv_fav;
	}

	private void doDelete(final XiangMuEntity qiuZuEntity) {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/index/estate_del?charges_id=" + qiuZuEntity.getId(),
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}
				if (callback.getStat() == 200) {

					entities.remove(qiuZuEntity);

					XiangMuFragmentAdapter.this.notifyDataSetChanged();

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

	private void showPromptDialog(final XiangMuEntity qiuZuEntity) {
		final CustomDialog customDialog = new CustomDialog(context,
				new CustomDialogListener() {

					@Override
					public void onDialogClosed(int closeType) {
						switch (closeType) {
						case CustomDialogListener.BUTTON_POSITIVE:
							doDelete(qiuZuEntity);
						}
					}
				});

		customDialog.setCustomMessage("确认要删除吗?");
		customDialog.setCancelable(true);
		customDialog.setType(CustomDialog.DOUBLE_BTN);
		customDialog.show();
	}

	private void doFav(final String project_id,
			final XiangMuEntity xiangMuEntity) {

		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/follower_add?project_id=" + project_id,
				RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("project_id", project_id);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("\"收藏\"成功");
					xiangMuEntity.setProject_follower("1");
					int fav_count = Integer.parseInt(xiangMuEntity
							.getAtten_total());
					fav_count = fav_count + 1;
					xiangMuEntity.setAtten_total(fav_count + "");
					XiangMuFragmentAdapter.this.notifyDataSetChanged();
				} else {
					Utils.showMessage(callback.getMsg());
				}

			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	// 删除收藏
	private void doDelFav(final String project_id,
			final XiangMuEntity xiangMuEntity) {

		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/follower_del?project_id=" + project_id,
				RequestInformation.REQUEST_METHOD_GET);
		request.addPostParams("project_id", project_id);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("取消\"收藏\"成功");
					xiangMuEntity.setProject_follower("0");
					int fav_count = Integer.parseInt(xiangMuEntity
							.getAtten_total());
					fav_count = fav_count - 1;
					xiangMuEntity.setAtten_total(fav_count + "");
					XiangMuFragmentAdapter.this.notifyDataSetChanged();
				} else {
					Utils.showMessage(callback.getMsg());
				}

			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	// 分享相关
	private void doShare(XiangMuEntity xiangMuEntity) {
		ShareHelper shareDialog = null;
		String target_url = "http://wx.qxj.me/p/project.html?pid="
				+ xiangMuEntity.getId();

		if (shareDialog == null) {
			shareDialog = new ShareHelper(context);
		}

		shareDialog.setShareUrl(target_url);
		shareDialog.setShareTitle(Utils.trans(R.string.app_name));
		shareDialog.setShareContent(xiangMuEntity.getTitle(),
				xiangMuEntity.getFace());

		if (!shareDialog.isShowing()) {
			shareDialog.show();
		}
	}
}
