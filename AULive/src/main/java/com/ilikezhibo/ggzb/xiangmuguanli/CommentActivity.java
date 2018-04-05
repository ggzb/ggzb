package com.ilikezhibo.ggzb.xiangmuguanli;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;

public class CommentActivity extends BaseFragmentActivity implements
		OnClickListener {

	private EditText desc_et;
	private String project_id;
	private String uid;

	private String nickname;
	// private String comment;
	private int score = 0;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_comment_layout);

	}

	private CheckBox start1_iv;
	private CheckBox start2_iv;
	private CheckBox start3_iv;
	private CheckBox start4_iv;
	private CheckBox start5_iv;

	@Override
	protected void initializeViews() {
		desc_et = (EditText) findViewById(R.id.desc_et);

		start1_iv = (CheckBox) findViewById(R.id.start1_iv);
		start1_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (start1_iv.isChecked()) {
					start1_iv.setChecked(true);
					start2_iv.setChecked(false);
					start3_iv.setChecked(false);
					start4_iv.setChecked(false);
					start5_iv.setChecked(false);
					score = 2;
				} else {
					start1_iv.setChecked(false);
					start2_iv.setChecked(false);
					start3_iv.setChecked(false);
					start4_iv.setChecked(false);
					start5_iv.setChecked(false);
					score = 0;
				}

			}
		});
		start2_iv = (CheckBox) findViewById(R.id.start2_iv);
		start2_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (start2_iv.isChecked()) {
					start1_iv.setChecked(true);
					start2_iv.setChecked(true);
					start3_iv.setChecked(false);
					start4_iv.setChecked(false);
					start5_iv.setChecked(false);
					score = 4;
				} else {
					start1_iv.setChecked(true);
					start2_iv.setChecked(false);
					start3_iv.setChecked(false);
					start4_iv.setChecked(false);
					start5_iv.setChecked(false);
					score = 2;
				}

			}
		});

		start3_iv = (CheckBox) findViewById(R.id.start3_iv);
		start3_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (start3_iv.isChecked()) {
					start1_iv.setChecked(true);
					start2_iv.setChecked(true);
					start3_iv.setChecked(true);
					start4_iv.setChecked(false);
					start5_iv.setChecked(false);
					score = 6;
				} else {
					start1_iv.setChecked(true);
					start2_iv.setChecked(true);
					start3_iv.setChecked(false);
					start4_iv.setChecked(false);
					start5_iv.setChecked(false);
					score = 4;
				}

			}
		});

		start4_iv = (CheckBox) findViewById(R.id.start4_iv);
		start4_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (start4_iv.isChecked()) {
					start1_iv.setChecked(true);
					start2_iv.setChecked(true);
					start3_iv.setChecked(true);
					start4_iv.setChecked(true);
					start5_iv.setChecked(false);
					score = 8;
				} else {
					start1_iv.setChecked(true);
					start2_iv.setChecked(true);
					start3_iv.setChecked(true);
					start4_iv.setChecked(false);
					start5_iv.setChecked(false);
					score = 6;
				}

			}
		});

		start5_iv = (CheckBox) findViewById(R.id.start5_iv);
		start5_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (start5_iv.isChecked()) {
					start1_iv.setChecked(true);
					start2_iv.setChecked(true);
					start3_iv.setChecked(true);
					start4_iv.setChecked(true);
					start5_iv.setChecked(true);
					score = 10;
				} else {
					start1_iv.setChecked(true);
					start2_iv.setChecked(true);
					start3_iv.setChecked(true);
					start4_iv.setChecked(true);
					start5_iv.setChecked(false);
					score = 8;
				}

			}
		});

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("评论");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("跳过");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.GONE);

		Button resetPwdBtn = (Button) findViewById(R.id.reg_bt);
		resetPwdBtn.setOnClickListener(this);

		project_id = getIntent().getStringExtra(project_id_key);
		uid = getIntent().getStringExtra(uid_key);
		nickname = getIntent().getStringExtra(nickname_key);

		tv_title.setText("评论" + nickname);
		// score = getIntent().getStringExtra(score_key);

	}

	@Override
	protected void initializeData() {
		LoginUserEntity loginUserEntity = AULiveApplication.getUserInfo();
		if (loginUserEntity != null) {
			desc_et.setText(loginUserEntity.intro);
		}
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			Intent intent = new Intent(CommentActivity.this,
					AULiveHomeActivity.class);
			startActivity(intent);
			break;
		case R.id.reg_bt:
			if (desc_et.getText() == null || desc_et.getText().equals("")) {
				Utils.showMessage("你还没有填写评论");
				return;
			}
			if (uid == null || uid.equals("")) {
				jishuyuanPinglun(project_id, desc_et.getText().toString(),
						score + "");
			} else {
				faxiangmuPinglun(project_id, uid, desc_et.getText().toString(),
						score + "");
			}
			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	public static String project_id_key = "project_id_key";
	public static String uid_key = "uid_key";
	public static String nickname_key = "nickname_key";

	// public static String score_key = "score_key";

	// 发项目的人给技术人员评论
	private void faxiangmuPinglun(String project_id, String uid,
			String comment, String score) {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/p_comment?project_id=" + project_id + "&uid=" + uid
				+ "&comment=" + comment + "&score=" + score,
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<UserInfo>() {

			@Override
			public void onCallback(UserInfo callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {

					CommentActivity.this.finish();

					Utils.showMessage(callback.getMsg());
				} else {

					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(UserInfo.class));
		request.execute();
	}

	// 技术人员给发项目的人评论
	private void jishuyuanPinglun(String project_id, String comment,
			String score) {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/u_comment?project_id=" + project_id + "&comment="
				+ comment + "&score=" + score,
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<UserInfo>() {

			@Override
			public void onCallback(UserInfo callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {

					CommentActivity.this.finish();

					Utils.showMessage(callback.getMsg());
				} else {

					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(UserInfo.class));
		request.execute();
	}

}
