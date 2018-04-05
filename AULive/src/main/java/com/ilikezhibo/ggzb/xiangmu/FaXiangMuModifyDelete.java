package com.ilikezhibo.ggzb.xiangmu;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.R;

/**
 * @ClassName: SecondHandModifyDelete
 * @Description: 修改晒物
 * @author big
 * @date 2014-4-24 下午3:05:43
 * 
 */
public class FaXiangMuModifyDelete extends BaseActivity implements OnClickListener {
	public static final String USER_ID_KEY = "user_id";

	private Button modify_way_button;
	private Button delete_button;
	private Button cancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.shaiwu_modify_delete_layout);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		LinearLayout layout = (LinearLayout) findViewById(R.id.exit_layout);
		layout.setOnClickListener(this);
		modify_way_button = (Button) findViewById(R.id.modify_way);
		modify_way_button.setText("修改项目");
		modify_way_button.setOnClickListener(this);

		delete_button = (Button) findViewById(R.id.delete_way);
		delete_button.setText("删除项目");
		delete_button.setOnClickListener(this);

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
		switch (v.getId()) {
		case R.id.exit_layout:
			finish();
			overridePendingTransition(R.anim.push_bottom_in,
					R.anim.push_bottom_out);
			break;
		case R.id.modify_way:
			setResult(FaXiangMuDetailActivity.modify_key);
			this.finish();
			break;
		case R.id.delete_way:
			setResult(FaXiangMuDetailActivity.delete_key);
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

}
