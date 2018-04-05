package com.ilikezhibo.ggzb;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author Stay
 * @version create time：Sep 1, 2013 2:05:27 PM
 */
public class BaseFragment extends Fragment {
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * flagment 销毁时调用
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	protected void showProgressDialog(String msg) {
		progressDialog = ProgressDialog.show(getActivity(), "提示", msg);
	}

	protected void cancelProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}
