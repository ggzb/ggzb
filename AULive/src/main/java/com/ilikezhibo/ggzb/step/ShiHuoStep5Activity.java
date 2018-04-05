package com.ilikezhibo.ggzb.step;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.ilikezhibo.ggzb.userinfo.MyProFileActivity;
import com.ilikezhibo.ggzb.views.BirthDayPickDialog;

public class ShiHuoStep5Activity extends BaseFragmentActivity implements
		OnClickListener {

	private EditText school_et;
	private EditText degree_et;
	private TextView time_start_tv;
	private TextView time_end_tv;
	private EditText major_et;

	private boolean back_home = false;

	@Override
	protected void setContentView() {
		setContentView(R.layout.step_shihuo_5_layout);

		back_home = getIntent().getBooleanExtra(
				MyProFileActivity.back_home_key, false);
	}

	@Override
	protected void initializeViews() {
		school_et = (EditText) findViewById(R.id.school_et);
		degree_et = (EditText) findViewById(R.id.degree_et);
		major_et = (EditText) findViewById(R.id.major_et);
		time_start_tv = (TextView) findViewById(R.id.time_start_tv);
		time_start_tv.setOnClickListener(this);
		time_end_tv = (TextView) findViewById(R.id.time_end_tv);
		time_end_tv.setOnClickListener(this);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("教育经历");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("跳过");
		topRightBtn.setOnClickListener(this);
		// topRightBtn.setVisibility(View.GONE);

		Button resetPwdBtn = (Button) findViewById(R.id.reg_bt);
		resetPwdBtn.setOnClickListener(this);
		// 如果为修改
		if (back_home == true) {
			resetPwdBtn.setText("修改");
		}

	}

	@Override
	protected void initializeData() {
		LoginUserEntity loginUserEntity = AULiveApplication.getUserInfo();
		if (loginUserEntity != null) {
			school_et.setText(loginUserEntity.school);
			major_et.setText(loginUserEntity.specialty);
			degree_et.setText(loginUserEntity.degree);
			time_start_tv.setText(loginUserEntity.begin_school_time);
			time_end_tv.setText(loginUserEntity.end_school_time);
		}
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			if (back_home) {
				ShiHuoStep5Activity.this.finish();
			} else {
				Intent intent = new Intent(ShiHuoStep5Activity.this,
						ShiHuoStep6Activity.class);
				startActivity(intent);
			}
			break;
		case R.id.reg_bt:
			// 下一步
			if (docheck()) {
				sendCode();
			}
			// Intent intent = new Intent(ShiHuoStep3Activity.this,
			// RegisterActivity.class);
			// startActivity(intent);
			break;
		case R.id.time_start_tv:
			showBirthDayDialogStart();
			break;
		case R.id.time_end_tv:
			showBirthDayDialogEnd();
			break;
		case R.id.back:
			this.finish();
			break;
		}
	}

	private boolean isValueEt(EditText editText) {
		if (editText == null || editText.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private boolean isValueTV(TextView editText) {
		if (editText == null || editText.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private boolean docheck() {
		if (isValueEt(school_et) && isValueEt(degree_et)
				&& isValueTV(time_start_tv) && isValueTV(time_end_tv)
				&& isValueEt(major_et)) {

			return true;
		}
		Utils.showMessage("请填写完整后再尝试");
		return false;

	}

	private void sendCode() {
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/reg/edu_experience", RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("school", school_et.getText().toString().trim());
		request.addPostParams("degree", degree_et.getText().toString().trim());
		request.addPostParams("specialty", major_et.getText().toString().trim());
		request.addPostParams("begin_time", time_start_tv.getText().toString()
				.trim());
		request.addPostParams("end_time", time_end_tv.getText().toString()
				.trim());

		if (back_home) {
			request.addPostParams("step", "2");
		}
		request.setCallback(new JsonCallback<UserInfo>() {

			@Override
			public void onCallback(UserInfo callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {
					if (callback.getUserinfo() != null) {
						AULiveApplication.setUserInfo(callback.getUserinfo());
					}
					if (back_home) {
						ShiHuoStep5Activity.this.finish();
					} else {
						Intent intent = new Intent(ShiHuoStep5Activity.this,
								ShiHuoStep6Activity.class);
						startActivity(intent);
					}
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

	private void showBirthDayDialogStart() {
		final BirthDayPickDialog bithdayDialog = new BirthDayPickDialog(
				ShiHuoStep5Activity.this, time_start_tv.getText().toString());
		bithdayDialog.setTitle("选择开始日期");
		bithdayDialog.setButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				bithdayDialog.cancel();
			}
		}, "确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = bithdayDialog.getBirthDay();
				if (text == null) {

				} else {
					time_start_tv.setText(text);
					bithdayDialog.dismiss();
				}
			}
		});
		bithdayDialog.show();

	}

	private void showBirthDayDialogEnd() {
		final BirthDayPickDialog bithdayDialog = new BirthDayPickDialog(
				ShiHuoStep5Activity.this, time_end_tv.getText().toString());
		bithdayDialog.setTitle("选择结束日期");
		bithdayDialog.setButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				bithdayDialog.cancel();
			}
		}, "确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String text = bithdayDialog.getBirthDay();
				if (text == null) {

				} else {
					time_end_tv.setText(text);
					bithdayDialog.dismiss();
				}
			}
		});
		bithdayDialog.show();

	}
}
