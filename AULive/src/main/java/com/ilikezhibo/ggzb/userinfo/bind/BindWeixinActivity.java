package com.ilikezhibo.ggzb.userinfo.bind;

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
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;

public class BindWeixinActivity extends BaseFragmentActivity implements
		OnClickListener {

	private EditText realname_et;
	private EditText weixin_account;

	@Override
	protected void setContentView() {
		setContentView(R.layout.bind_weixin_layout);

	}

	@Override
	protected void initializeViews() {

		Button resetPwdBtn = (Button) findViewById(R.id.reg_bt);
		resetPwdBtn.setOnClickListener(this);

		realname_et = (EditText) findViewById(R.id.realname_et);
		weixin_account = (EditText) findViewById(R.id.weixin_account);

		LoginUserEntity userEntity = AULiveApplication.getUserInfo();
		if (userEntity != null) {
			if (userEntity.wx_realname != null
					&& !userEntity.wx_realname.equals("")) {
				realname_et.setEnabled(false);
				weixin_account.setEnabled(false);
				resetPwdBtn.setEnabled(false);
				resetPwdBtn.setText("已绑定");
			}
			realname_et.setText(userEntity.wx_realname);
			weixin_account.setText(userEntity.wx_account);
		}

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("绑定微信");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("跳过");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.GONE);

	}

	@Override
	protected void initializeData() {
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			Intent intent = new Intent(BindWeixinActivity.this,
					AULiveHomeActivity.class);
			startActivity(intent);
			break;
		case R.id.reg_bt:
			if (realname_et.getText() == null
					|| realname_et.getText().toString().equals("")
					|| weixin_account.getText() == null
					|| weixin_account.getText().toString().equals("")) {
				Utils.showMessage("请正确填写");
				return;
			}

			checkBind(realname_et.getText().toString(), weixin_account
					.getText().toString());

			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	private void checkBind(final String real_name, final String alipay_account) {
		CustomDialog userBlackDialog = new CustomDialog(this,
				new CustomDialogListener() {

					@Override
					public void onDialogClosed(int closeType) {

						switch (closeType) {
						case CustomDialogListener.BUTTON_POSITIVE:
							doBind(real_name, alipay_account);
							break;
						}
					}
				});

		userBlackDialog.setCustomMessage("请再次确认，绑定后将不能修改");
		userBlackDialog.setCancelable(true);
		userBlackDialog.setType(CustomDialog.DOUBLE_BTN);

		userBlackDialog.show();
	}

	private void doBind(final String real_name, final String weixin_account) {
		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/profile/bind_weixin",
				RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("wx_realname", real_name);
		request.addPostParams("wx_account", weixin_account);

		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("微信\"绑定\"成功");

					LoginUserEntity userEntity = AULiveApplication
							.getUserInfo();
					if (userEntity != null) {
						userEntity.wx_realname = real_name;
						userEntity.wx_account = weixin_account;
					}
					BindWeixinActivity.this.finish();
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
}
