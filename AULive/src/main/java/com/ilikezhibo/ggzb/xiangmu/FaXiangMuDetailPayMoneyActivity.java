package com.ilikezhibo.ggzb.xiangmu;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.userinfo.mymoney.MyMoneyActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;

public class FaXiangMuDetailPayMoneyActivity extends BaseFragmentActivity
		implements OnClickListener {

	private EditText pay_money_et;

	@Override
	protected void setContentView() {
		setContentView(R.layout.faxiangmu_accept_and_money_layout);

	}

	@Override
	protected void initializeViews() {
		pay_money_et = (EditText) findViewById(R.id.pay_money);
		pay_money_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						pay_money_et.setText(s);
						pay_money_et.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					pay_money_et.setText(s);
					pay_money_et.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						pay_money_et.setText(s.subSequence(0, 1));
						pay_money_et.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}

		});

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		String nickname = getIntent().getStringExtra(nickname_key);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText(nickname);

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("跳过");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.GONE);

		Button resetPwdBtn = (Button) findViewById(R.id.reg_bt);
		resetPwdBtn.setOnClickListener(this);
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
			Intent intent = new Intent(FaXiangMuDetailPayMoneyActivity.this,
					AULiveHomeActivity.class);
			startActivity(intent);
			break;
		case R.id.reg_bt:
			if (pay_money_et.getText() == null
					|| pay_money_et.getText().toString().equals("")
					|| pay_money_et.getText().toString().equals("0")) {
				Utils.showMessage("请先填写资金数");
				return;
			}

			// 先确认再操作
			doSureJieShou();
			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	private void doSureJieShou() {

		CustomDialog userBlackDialog = null;
		if (userBlackDialog == null) {
			userBlackDialog = new CustomDialog(
					FaXiangMuDetailPayMoneyActivity.this,
					new CustomDialogListener() {
						@Override
						public void onDialogClosed(int closeType) {
							switch (closeType) {
							case CustomDialogListener.BUTTON_POSITIVE:

								String project_id = getIntent().getStringExtra(
										project_id_key);
								String uid = getIntent()
										.getStringExtra(uid_key);
								doJieShou(project_id, uid, pay_money_et
										.getText().toString());
								break;
							case CustomDialogListener.BUTTON_NEUTRAL:

								break;
							}
						}
					});

			userBlackDialog.setCustomMessage("请再次确定项目资金!");
			userBlackDialog.setCancelable(true);
			userBlackDialog.setType(CustomDialog.DOUBLE_BTN);
			// userBlackDialog.setButtonText("同意",
			// "拒绝");
		}

		if (null != userBlackDialog) {
			userBlackDialog.show();
		}

	}

	public static String nickname_key = "nickname_key_key";
	public static String project_id_key = "project_id_key";
	public static String uid_key = "uid_key";

	// 接受
	private void doJieShou(String project_id, String uid, String dev_money) {
		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/choose?project_id=" + project_id + "&uid=" + uid
				+ "&dev_money=" + dev_money,
				RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {

				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("\"接受\"成功");
					FaXiangMuDetailPayMoneyActivity.this.finish();
				} else if (callback.getStat() == 502) {
					Utils.showMessage(callback.getMsg());
					Intent chongzi_Intent = new Intent(
							FaXiangMuDetailPayMoneyActivity.this,
							MyMoneyActivity.class);
					startActivity(chongzi_Intent);
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
