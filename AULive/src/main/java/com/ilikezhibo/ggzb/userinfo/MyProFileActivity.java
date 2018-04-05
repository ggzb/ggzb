package com.ilikezhibo.ggzb.userinfo;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import com.ilikezhibo.ggzb.photos.photobrowser.PicBrowseActivity;
import com.ilikezhibo.ggzb.register.RegisterActivity;
import com.ilikezhibo.ggzb.step.ShiHuoStep1Activity;
import com.ilikezhibo.ggzb.step.ShiHuoStep3Activity;
import com.ilikezhibo.ggzb.step.ShiHuoStep4Activity;
import com.ilikezhibo.ggzb.step.ShiHuoStep5Activity;
import com.ilikezhibo.ggzb.step.ShiHuoStep6Activity;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuListEntity;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MyProFileActivity extends BaseFragmentActivity implements
		TitleListener, OnClickListener {
	private LinearLayout basic_layout;
	private LinearLayout an_li_layout;
	private LinearLayout skill_layout;
	private LinearLayout company_layout;
	private LinearLayout edu_layout;
	private LinearLayout personal_intro_layout;

	@Override
	protected void setContentView() {
		setContentView(R.layout.userinfo_myprofile);
	}

	@Override
	protected void initializeViews() {

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("我的简历");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("预览");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.VISIBLE);

	}

	@Override
	protected void initializeData() {
		basic_layout = (LinearLayout) this.findViewById(R.id.basic_layout);
		basic_layout.setOnClickListener(this);
		an_li_layout = (LinearLayout) this.findViewById(R.id.main_layout);
		an_li_layout.setOnClickListener(this);
		skill_layout = (LinearLayout) this.findViewById(R.id.skill_layout);
		skill_layout.setOnClickListener(this);
		company_layout = (LinearLayout) this.findViewById(R.id.company_layout);
		company_layout.setOnClickListener(this);
		edu_layout = (LinearLayout) this.findViewById(R.id.edu_layout);
		edu_layout.setOnClickListener(this);
		personal_intro_layout = (LinearLayout) this
				.findViewById(R.id.personal_intro_layout);
		personal_intro_layout.setOnClickListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		init();
	}

	private void init() {
		getMyProducs();

		ImageView face_img = (ImageView) this.findViewById(R.id.face_img);
		TextView real_name_tv = (TextView) this.findViewById(R.id.real_name_tv);
		TextView sex_tv = (TextView) this.findViewById(R.id.ssex_tv);
		TextView birthday_tv = (TextView) this.findViewById(R.id.birthday_tv);

		TextView skill_tv = (TextView) this.findViewById(R.id.skill_tv);
		TextView major_tv = (TextView) this.findViewById(R.id.major_tv);

		TextView company_tv = (TextView) this.findViewById(R.id.company_tv);
		TextView position_tv = (TextView) this.findViewById(R.id.position_tv);
		TextView company_time_tv = (TextView) this
				.findViewById(R.id.company_time_tv);
		TextView content_tv = (TextView) this.findViewById(R.id.work_desc_tv);

		TextView school_tv = (TextView) this.findViewById(R.id.school_tv);
		TextView school_major_tv = (TextView) this
				.findViewById(R.id.school_major_tv);
		TextView degree_tv = (TextView) this.findViewById(R.id.degree_tv);
		TextView school_time_tv = (TextView) this
				.findViewById(R.id.school_time_tv);

		TextView short_intro_myselft = (TextView) this
				.findViewById(R.id.short_intro_myselft);

		// 初始化数据
		LoginUserEntity loginUserEntity = AULiveApplication.getUserInfo();


		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.face_male)
		.showImageForEmptyUri(R.drawable.face_male)
		.showImageOnFail(R.drawable.face_male).cacheInMemory()
		.cacheOnDisc().build();
		
		ImageLoader.getInstance().displayImage(loginUserEntity.getFace(),
				face_img, options);
		real_name_tv.setText(loginUserEntity.getNickname());

		int sex = loginUserEntity.getSex();
		if (sex == 1) {
			sex_tv.setText("男");
		} else {
			sex_tv.setText("女");
		}
		birthday_tv.setText(loginUserEntity.getBirthday());

		skill_tv.setText(loginUserEntity.skill);
		major_tv.setText(loginUserEntity.job);

		company_tv.setText(loginUserEntity.company);
		position_tv.setText(loginUserEntity.post);
		company_time_tv.setText(loginUserEntity.begin_work_time + "至"
				+ loginUserEntity.end_work_time);
		content_tv.setText(loginUserEntity.job_desc);

		school_tv.setText(loginUserEntity.school);
		school_major_tv.setText(loginUserEntity.specialty);
		degree_tv.setText(loginUserEntity.degree);
		school_time_tv.setText(loginUserEntity.begin_school_time + "至"
				+ loginUserEntity.end_school_time);

		short_intro_myselft.setText(loginUserEntity.intro);
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			Intent profile_intent = new Intent(this, ProfileActivity.class);
			profile_intent.putExtra(ProfileActivity.PROFILE_UID,
					AULiveApplication.getUserInfo().getUid());
			startActivity(profile_intent);
			break;

		case R.id.back:
			this.finish();
			break;

		case R.id.basic_layout:
			Intent basic_layout = new Intent(this, RegisterActivity.class);
			basic_layout.putExtra(back_home_key, true);
			// 修改状态的key
			basic_layout.putExtra(RegisterActivity.INFO_MODIFY_KEY, true);
			startActivity(basic_layout);
			break;
		case R.id.main_layout:
			Intent an_li_layout = new Intent(this, ShiHuoStep3Activity.class);
			an_li_layout.putExtra(back_home_key, true);
			startActivity(an_li_layout);
			break;

		case R.id.skill_layout:
			Intent skill_layout = new Intent(this, ShiHuoStep1Activity.class);
			skill_layout.putExtra(back_home_key, true);
			startActivity(skill_layout);
			break;
		case R.id.company_layout:
			Intent company_layout = new Intent(this, ShiHuoStep4Activity.class);
			company_layout.putExtra(back_home_key, true);
			startActivity(company_layout);
			break;
		case R.id.edu_layout:
			Intent edu_layout = new Intent(this, ShiHuoStep5Activity.class);
			edu_layout.putExtra(back_home_key, true);
			startActivity(edu_layout);
			break;
		case R.id.personal_intro_layout:
			Intent personal_intro_layout = new Intent(this,
					ShiHuoStep6Activity.class);
			personal_intro_layout.putExtra(back_home_key, true);
			startActivity(personal_intro_layout);
			break;
		}

	}

	public static String back_home_key = "back_home_key";

	protected boolean matchPhone(String text) {
		if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
			return true;
		}
		return false;
	}

	/** 左边事件 */
	@Override
	public void onBack() {
		this.finish();
	}

	/** 右边事件 */
	@Override
	public void onTopRightEvent() {

	}

	private void getMyProducs() {
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/project/my_list?example=1",
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<XiangMuListEntity>() {

			@Override
			public void onCallback(XiangMuListEntity callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {

					ArrayList<XiangMuEntity> entities = callback.getList();
					an_li_layout.removeAllViews();

					if (entities.size() > 0) {
						for (final XiangMuEntity xiangMuEntity : entities) {
							final View edu_item = LayoutInflater.from(
									MyProFileActivity.this).inflate(
									R.layout.userinfo_myproduct_item, null);

							TextView app_name = (TextView) edu_item
									.findViewById(R.id.app_name_tv);
							app_name.setText(xiangMuEntity.getTitle());

							ImageView addphoto_img = (ImageView) edu_item
									.findViewById(R.id.addphoto_img);
							ImageLoader.getInstance().displayImage(
									xiangMuEntity.getPic(), addphoto_img,
									AULiveApplication.getGlobalImgOptions());
							addphoto_img
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											String url = xiangMuEntity.getPic();
											if (url == null || url.equals("")) {
												return;
											}
											String[] urls = { url };
											Intent intent = new Intent(
													MyProFileActivity.this,
													PicBrowseActivity.class);
											intent.putExtra(
													PicBrowseActivity.INTENT_BROWSE_POS_KEY,
													0);
											intent.putExtra(
													PicBrowseActivity.INTENT_BROWSE_LST_KEY,
													urls);
											startActivity(intent);
										}
									});

							TextView app_url_tv = (TextView) edu_item
									.findViewById(R.id.app_url_tv);
							app_url_tv.setText(xiangMuEntity.getUrl());
							app_url_tv
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											Intent intent = new Intent(
													MyProFileActivity.this,
													WebViewActivity.class);
											if (xiangMuEntity.getUrl() == null
													|| xiangMuEntity.getUrl()
															.equals("")) {
												return;
											}

											intent.putExtra(
													WebViewActivity.input_url,
													xiangMuEntity.getUrl());
											intent.putExtra(
													WebViewActivity.back_home_key,
													false);
											intent.putExtra(
													WebViewActivity.actity_name,
													"网页详情");
											startActivity(intent);

										}
									});

							TextView content_tv = (TextView) edu_item
									.findViewById(R.id.content_tv);
							content_tv.setText(xiangMuEntity.getMemo());

							an_li_layout.addView(edu_item);
						}
					} else {
						// 当没有时，加一个空的
						final View edu_item = LayoutInflater.from(
								MyProFileActivity.this).inflate(
								R.layout.userinfo_myproduct_item, null);
						an_li_layout.addView(edu_item);
					}
				} else {

					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(XiangMuListEntity.class));
		request.execute();
	}

}
