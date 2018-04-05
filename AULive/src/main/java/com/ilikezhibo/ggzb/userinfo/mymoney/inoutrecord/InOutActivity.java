package com.ilikezhibo.ggzb.userinfo.mymoney.inoutrecord;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;

public class InOutActivity extends BaseFragmentActivity {

	private InOutFragment inoutFragement;
	private FragmentManager manager;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_zhaofang);

		inoutFragement = new InOutFragment();
		manager = getSupportFragmentManager();
		manager.beginTransaction().replace(R.id.map, inoutFragement, "map_fragment")
				.commit();
	}

	@Override
	protected void initializeViews() {

	}

	@Override
	protected void initializeData() {

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {

		super.onStop();
	}

	@Override
	public void onDestroy() {
		manager.getFragments().clear();
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}