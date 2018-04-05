package com.ilikezhibo.ggzb;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.jack.utils.SystemStatusManager;
import com.jack.utils.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @ClassName: BaseFragmentActivity
 * @Description: TODO fragment 基类
 * @author jack.long
 * @date 2014-2-12 下午3:18:25
 * 
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.anim.activity_open_enter,
				R.anim.activity_open_exit);
		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= 21) {
			getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.global_main_bg));
		}

		setContentView();
		initializeViews();
		initializeData();

		ActivityStackManager.getInstance().pushActivity(this);
	}

	protected void setContentView(int layoutResID, boolean isShowTitle) {
		setContentView(layoutResID);
	}

	protected void showProgressDialog(String msg) {
       try {
          progressDialog = ProgressDialog.show(this, "提示", msg);
       }catch (Exception e){
          progressDialog=null;
       }
	}

	protected void cancelProgressDialog() {
       try {
		if (progressDialog != null) {
			progressDialog.dismiss();
		} }catch (Exception e){
          progressDialog=null;
       }
	}

	protected abstract void setContentView();

	protected abstract void initializeViews();

	protected abstract void initializeData();

	@Override
	protected void onResume() {
		super.onResume();
//		setTranslucentStatus();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		super.finish();
		exitAnim();
	}

	protected void exitAnim() {
		Utils.exitAnimation(this);
	}

	public interface ErrorListener {
		void onDealAppError();
	}

	/**
	 * 设置状态栏背景状态
	 */
	private void setTranslucentStatus() {

		// // 去掉系统状态栏
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 状态栏系统一体化
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);

			View title_blank = this.findViewById(R.id.title_blank);
			if (title_blank != null)
				title_blank.setVisibility(View.VISIBLE);
		} else {

		}
		SystemStatusManager tintManager = new SystemStatusManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(0);// 状态栏无背景
	}
}
