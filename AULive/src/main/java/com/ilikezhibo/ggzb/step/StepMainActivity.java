package com.ilikezhibo.ggzb.step;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jack.utils.BtnClickUtils;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import java.util.regex.Pattern;

public class StepMainActivity extends BaseFragmentActivity implements
		TitleListener, OnClickListener {

	@Override
	protected void setContentView() {
		setContentView(R.layout.step_main_layout);
	}

	@Override
	protected void initializeViews() {

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("任务类型");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("下一步");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.GONE);

		LinearLayout step_shihuo_ly = (LinearLayout) this
				.findViewById(R.id.step_shihuo_ly);
		step_shihuo_ly.setOnClickListener(this);

		TextView step_shihuo_tv = (TextView) this
				.findViewById(R.id.step_shihuo_tv);
		step_shihuo_tv.setOnClickListener(this);

		LinearLayout step_xiangmu_ly = (LinearLayout) this
				.findViewById(R.id.step_xiangmu_ly);
		step_xiangmu_ly.setOnClickListener(this);

		TextView step_xiangmu_tv = (TextView) this
				.findViewById(R.id.step_xiangmu_tv);
		step_xiangmu_tv.setOnClickListener(this);

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
			// finishCount();
			// checkField();
			break;
		case R.id.step_xiangmu_tv:
			Intent intent1 = new Intent(this, FaXiangMuStep1Activity.class);
			startActivity(intent1);
			break;
		case R.id.step_xiangmu_ly:
			Intent intent2 = new Intent(this, FaXiangMuStep1Activity.class);
			startActivity(intent2);
			break;
		case R.id.step_shihuo_tv:
			Intent intent3 = new Intent(this, ShiHuoStep1Activity.class);
			startActivity(intent3);
			break;
		case R.id.step_shihuo_ly:
			Intent intent4 = new Intent(this, ShiHuoStep1Activity.class);
			startActivity(intent4);
			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

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

	// private void sendCode() {
	// RequestInformation request = new RequestInformation(
	// UrlHelper.GET_AUTH_CODE, RequestInformation.REQUEST_METHOD_POST);
	// request.addPostParams("phone", editAccount.getText().toString().trim());
	// request.addPostParams("udid", MobileConfig.getMobileConfig(this)
	// .getIemi());
	// request.setCallback(new JsonCallback<BaseEntity>() {
	//
	// @Override
	// public void onCallback(BaseEntity callback) {
	// if (callback == null) {
	// getAuthCodeBtn.setEnabled(true);
	// Utils.showMessage(Utils.trans(R.string.get_info_fail));
	// return;
	// }
	// if (callback.getStat() == 200) {
	// handlerTime = 60;
	// handler.post(timeback);
	// getAuthCodeBtn.setEnabled(false);
	// Utils.showMessage(callback.getMsg());
	// } else {
	// getAuthCodeBtn.setEnabled(true);
	// Utils.showMessage(callback.getMsg());
	// }
	// }
	//
	// @Override
	// public void onFailure(AppException e) {
	// getAuthCodeBtn.setEnabled(true);
	// Utils.showMessage(Utils.trans(R.string.get_info_fail));
	// }
	// }.setReturnType(BaseEntity.class));
	// request.execute();
	// }

}
