package com.ilikezhibo.ggzb.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import com.jack.utils.BtnClickUtils;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.R;

/**
 * @ClassName: ForgetPassWord
 * @Description: 忘记密码
 * @author big
 * @date 2014-4-24 下午3:05:43
 * 
 */
public class ForgetPassWord extends BaseActivity implements OnClickListener {
	public static final String USER_ID_KEY = "user_id";
	private static final int REPORT_USER = 1;
	private static final int BLACK_USER = 2;

	private String uid = null;

	private Button phone_way_button;
	private Button mail_way_button;
	private Button cancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uid = getIntent().getStringExtra(USER_ID_KEY);

		setContentView(R.layout.login_forget_password_layout);

		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		LinearLayout layout = (LinearLayout) findViewById(R.id.exit_layout);
		layout.setOnClickListener(this);
		phone_way_button = (Button) findViewById(R.id.phone_way);
		phone_way_button.setOnClickListener(this);

		mail_way_button = (Button) findViewById(R.id.mail_way);
		mail_way_button.setOnClickListener(this);

		findViewById(R.id.findCustomBtn).setOnClickListener(this);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.exit_layout:
			finish();
			overridePendingTransition(R.anim.push_bottom_in,
					R.anim.push_bottom_out);
			break;
		case R.id.phone_way:
			Intent intent1 = new Intent(this, GetPassWordByPhoneActivity.class);
			startActivity(intent1);
			this.finish();
			break;
		case R.id.mail_way:
			Intent intent = new Intent(this, GetPassWordByEmailActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.findCustomBtn:
			Intent intent2 = new Intent(this, FeedBackActivity.class);
			intent2.putExtra(FeedBackActivity.INTENT_IS_FORGET_PASSWORD_KEY,
					true);
			startActivity(intent2);
			this.finish();
			break;
		case R.id.cancelBtn:
			finish();
			overridePendingTransition(R.anim.push_bottom_in, R.anim.push_bottom_out);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// private void reportUser(int position) {
	//
	// RequestInformation request = new RequestInformation(
	// UrlHelper.getReportUserUrl(uid, position),
	// RequestInformation.REQUEST_METHOD_GET);
	//
	// request.setCallback(new JsonCallback<BaseEntity>() {
	//
	// @Override
	// public void onCallback(BaseEntity callback) {
	//
	// if (callback == null) {
	// return;
	// }
	//
	// if (callback.getStat() == 200) {
	// Utils.showMessage("感谢您的举报，我们会尽快处理!");
	// } else {
	// Utils.showMessage(callback.getMsg());
	// }
	// }
	//
	// @Override
	// public void onFailure(AppException e) {
	//
	// Utils.showMessage(Utils.trans(R.string.get_info_fail));
	// ForgetPassWord.this.finish();
	// }
	// }.setReturnType(BaseEntity.class));
	// request.execute();
	// }

}
