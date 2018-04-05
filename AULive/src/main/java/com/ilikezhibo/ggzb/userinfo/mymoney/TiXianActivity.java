package com.ilikezhibo.ggzb.userinfo.mymoney;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.userinfo.bind.BindAlipayActivity;
import com.ilikezhibo.ggzb.userinfo.bind.BindWeixinActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;

/**
 * 
 * @author big
 * 
 *         选择支付方式
 */
public class TiXianActivity extends BaseFragmentActivity implements
		OnClickListener {
	public static final String MONEY_COUNT = "MONEY_COUNT";

	private double money_cont;// 购买的钱

	private TextView shopContentTv;

	@Override
	protected void setContentView() {
		money_cont = getIntent().getDoubleExtra(MONEY_COUNT, 0);

		setContentView(R.layout.duihuan_layout);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("提示方式");

	}

	@Override
	protected void initializeViews() {

	}

	@Override
	protected void initializeData() {

		shopContentTv = (TextView) findViewById(R.id.buyContentTv);

		shopContentTv.setText(Html.fromHtml(Utils.trans(
				R.string.tixian_content_str, money_cont)));
		findViewById(R.id.payAliPayLayout).setOnClickListener(this);
		findViewById(R.id.payWeiXinLayout).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.back:

			this.finish();

			break;
		case R.id.payAliPayLayout:
			doTixianAlipay(money_cont);
			break;
		case R.id.payWeiXinLayout:

			doTixianWeixin(money_cont);
			break;
		default:
			break;
		}
	}

	private void doTixianAlipay(final double money) {
		String uid = AULiveApplication.getUserInfo().getUid();
		String nickname = AULiveApplication.getUserInfo().getNickname();

		if (AULiveApplication.getUserInfo() == null || uid == null
				|| uid.equals("") || uid.equals("0")) {
			Intent login_intent = new Intent(this, LoginActivity.class);
			login_intent.putExtra(LoginActivity.back_home_key, true);
			startActivity(login_intent);
			return;
		}

		LoginUserEntity userEntity = AULiveApplication.getUserInfo();
		if (userEntity != null) {
			if (userEntity.ali_realname == null
					|| userEntity.ali_realname.equals("")) {

				Intent alipay_intent = new Intent(this,
						BindAlipayActivity.class);
				startActivity(alipay_intent);
				return;
			}
		}

		CustomDialog userBlackDialog = null;
		if (userBlackDialog == null) {
			userBlackDialog = new CustomDialog(TiXianActivity.this,
					new CustomDialogListener() {
						@Override
						public void onDialogClosed(int closeType) {
							switch (closeType) {
							case CustomDialogListener.BUTTON_POSITIVE:
								// 确认
								tiXian(money, "1");
								break;
							case CustomDialogListener.BUTTON_NEUTRAL:
								// 否认

								break;
							}
						}
					});

			userBlackDialog.setCustomMessage("确实提现" + money + "到支付宝");
			userBlackDialog.setCancelable(true);
			userBlackDialog.setType(CustomDialog.DOUBLE_BTN);

		}

		if (null != userBlackDialog) {
			userBlackDialog.show();
		}
	}

	private void doTixianWeixin(final double money) {
		String uid = AULiveApplication.getUserInfo().getUid();
		String nickname = AULiveApplication.getUserInfo().getNickname();

		if (AULiveApplication.getUserInfo() == null || uid == null
				|| uid.equals("") || uid.equals("0")) {
			Intent login_intent = new Intent(this, LoginActivity.class);
			login_intent.putExtra(LoginActivity.back_home_key, true);
			startActivity(login_intent);
			return;
		}
		LoginUserEntity userEntity1 = AULiveApplication.getUserInfo();
		if (userEntity1 != null) {
			if (userEntity1.wx_realname == null
					|| userEntity1.wx_realname.equals("")) {

				Intent wx_intent = new Intent(this, BindWeixinActivity.class);
				startActivity(wx_intent);
				return;
			}
		}

		CustomDialog userBlackDialog = null;
		if (userBlackDialog == null) {
			userBlackDialog = new CustomDialog(TiXianActivity.this,
					new CustomDialogListener() {
						@Override
						public void onDialogClosed(int closeType) {
							switch (closeType) {
							case CustomDialogListener.BUTTON_POSITIVE:
								// 确认
								tiXian(money, "2");
								break;
							case CustomDialogListener.BUTTON_NEUTRAL:
								// 否认

								break;
							}
						}
					});

			userBlackDialog.setCustomMessage("确实提现" + money + "到微信");
			userBlackDialog.setCancelable(true);
			userBlackDialog.setType(CustomDialog.DOUBLE_BTN);

		}

		if (null != userBlackDialog) {
			userBlackDialog.show();
		}
	}

	private void tiXian(double money, String type) {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/pay/exchange?money=" + money + "&type=" + type,
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<UserInfo>() {

			@Override
			public void onCallback(UserInfo callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {
					TiXianActivity.this.finish();
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
