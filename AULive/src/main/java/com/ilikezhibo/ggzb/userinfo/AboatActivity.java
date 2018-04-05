package com.ilikezhibo.ggzb.userinfo;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;

public class AboatActivity extends BaseFragmentActivity implements
		OnClickListener {

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_about);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setVisibility(View.VISIBLE);
		rl_back.setOnClickListener(this);

		TextView title = (TextView) this.findViewById(R.id.title);
		title.setOnClickListener(this);
		title.setText("关于");

//		TextView website = (TextView) this.findViewById(R.id.website);
//		website.setText("官网: www.hainanhuanle.com");
	}

	@Override
	protected void initializeViews() {

	}

	@Override
	protected void initializeData() {

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back:
			this.finish();
			break;

		}
	}

}
