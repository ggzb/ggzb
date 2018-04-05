package com.ilikezhibo.ggzb.find;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.TitleNavView;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import com.ilikezhibo.ggzb.home.listener.NavigationListener;

/**
 * @ClassName: FindFragment
 * @Description: 发现的fragment
 * @author big
 * @date 2014-3-19 下午9:49:00
 * 
 */
public class FindFragment extends BaseFragment implements TitleListener {

	private NavigationListener listener;

	public FindFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.jiesihuo_find_layout, null);
		new TitleNavView(view.findViewById(R.id.topLayout), "发现", this, false);

		return view;
	}

	@Override
	public void onBack() {


	}

	@Override
	public void onTopRightEvent() {

	}
}
