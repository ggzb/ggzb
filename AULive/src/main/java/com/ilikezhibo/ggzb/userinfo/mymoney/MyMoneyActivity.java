package com.ilikezhibo.ggzb.userinfo.mymoney;

import com.ilikezhibo.ggzb.AULiveApplication;
import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.userinfo.mymoney.inoutrecord.InOutActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.xiangmu.adapter.JingJiaDetailAdapter;
import com.ilikezhibo.ggzb.xiangmu.entity.CommentEntity;

public class MyMoneyActivity extends BaseFragmentActivity implements
		OnItemClickListener, PullToRefreshView.OnRefreshListener,
		OnClickListener {

	private CustomProgressDialog progressDialog = null;
	// 当前的内容

	private PullToRefreshView home_listview;
	private View listHeadView;
	private ArrayList<CommentEntity> entities;
	private JingJiaDetailAdapter listAdapter;

	private int currPage = 1;
	private boolean backh_home;

	private TextView tv_edit;

	private Button button_chongzhi;
	private Button button_tixian;
	private EditText pay_money_et;

	private MyMoneyEntity moneyEntity;

	private TextView my_money_left;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_mymoney_layout);
		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("我的钱包");

		tv_edit = (TextView) MyMoneyActivity.this
				.findViewById(R.id.topRightBtn);
		tv_edit.setOnClickListener(MyMoneyActivity.this);
		tv_edit.setText("收支记录");
		// tv_edit.setVisibility(View.GONE);

	}

	@Override
	protected void initializeViews() {
		home_listview = (PullToRefreshView) this
				.findViewById(R.id.pull_to_refresh_listview);
		// home_listview.setOnRefreshListener(this);
		home_listview.setOnItemClickListener(this);

		// 设置listView的头部
		listHeadView = LayoutInflater.from(this).inflate(
				R.layout.mymoney_first_item, null);
		home_listview.addHeaderView(listHeadView);
		home_listview.setHeaderDividersEnabled(false);

		listAdapter = new JingJiaDetailAdapter(this);

		entities = new ArrayList<CommentEntity>();
		listAdapter.setEntities(entities);
		home_listview.setAdapter(listAdapter);

		button_chongzhi = (Button) this.findViewById(R.id.button_chongzhi);
		button_chongzhi.setOnClickListener(this);

		button_tixian = (Button) this.findViewById(R.id.button_tixian);
		button_tixian.setOnClickListener(this);

		pay_money_et = (EditText) findViewById(R.id.pay_money);

		my_money_left = (TextView) findViewById(R.id.my_money_left);

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

		// startProgressDialog();
	}

	@Override
	protected void initializeData() {

	}

	@Override
	protected void onResume() {
		super.onResume();

		getMyMoney();
		// home_listview.initRefresh(PullToRefreshView.HEADER);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.back:
			if (backh_home) {
				Intent intent = new Intent(this, AULiveApplication.class);
				startActivity(intent);
				this.finish();
			} else {
				this.finish();
			}
			break;

		case R.id.topRightBtn:
			Intent inout = new Intent(this, InOutActivity.class);
			startActivity(inout);
			break;

		case R.id.button_chongzhi:
			Trace.d("pay_money_et.getText():" + pay_money_et.getText());
			if (pay_money_et.getText() == null
					|| pay_money_et.getText().toString().equals("")
					|| pay_money_et.getText().toString().equals("0")) {
				Utils.showMessage("请先填写充值资金数");
				return;
			}

			Intent button_chongzhi = new Intent(this, ChoosePayActivity.class);
			button_chongzhi.putExtra(ChoosePayActivity.MONEY_COUNT,
					Double.parseDouble(pay_money_et.getText().toString()));
			startActivity(button_chongzhi);
			
			pay_money_et.setText("");
			break;
		case R.id.button_tixian:
			if (pay_money_et.getText() == null
					|| pay_money_et.getText().toString().equals("")
					|| pay_money_et.getText().toString().equals("0")) {
				Utils.showMessage("请先填写提现资金数");
				return;
			}
			Intent button_tixian = new Intent(this, TiXianActivity.class);
			button_tixian.putExtra(ChoosePayActivity.MONEY_COUNT,
					Double.parseDouble(pay_money_et.getText().toString()));
			startActivity(button_tixian);
			
			pay_money_et.setText("");
			break;
		}
	}

	// 按下返回键时
	@Override
	public void onBackPressed() {

		if (backh_home) {
			Intent intent = new Intent(this, AULiveApplication.class);
			startActivity(intent);
			this.finish();
		} else {
			this.finish();
		}

	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage("正在加载中...");
		}

		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	// 滚动到最后一行
	private void toTheEndOfListView() {
		if (entities.size() <= 0) {
			return;
		}
		int position = entities.size() - 1;
		home_listview.setSelection(position);
	}

	public final static String REPLY_ENTITY_KEY = "Reply_entity";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Intent intent = new Intent(this, JingJiaReplyDetailActivity.class);
		// CommentEntity entity = (CommentEntity) parent.getAdapter().getItem(
		// position);
		// if (entity == null || entity.getId() == null
		// || entity.getId().equals("")) {
		// return;
		// }
		// intent.putExtra(REPLY_ENTITY_KEY, entity);
		// startActivity(intent);
	}

	private CustomDialog userBlackDialog;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// if (resultCode == delete_key) {
		//
		// if (userBlackDialog == null) {
		// userBlackDialog = new CustomDialog(this,
		// new CustomDialogListener() {
		// @Override
		// public void onDialogClosed(int closeType) {
		// switch (closeType) {
		// case CustomDialogListener.BUTTON_POSITIVE:
		// doDelet(xiangMuEntity.getId());
		// break;
		// }
		// }
		// });
		//
		// userBlackDialog.setCustomMessage("确定要删除晒物吗?");
		// userBlackDialog.setCancelable(true);
		// userBlackDialog.setType(CustomDialog.DOUBLE_BTN);
		// }
		//
		// if (null != userBlackDialog) {
		// userBlackDialog.show();
		// }
		//
		// }
	}

	private void getMyMoney() {
		startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/pay/get_curr_money", RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new JsonCallback<MyMoneyEntity>() {

			@Override
			public void onCallback(MyMoneyEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					moneyEntity = callback;
					my_money_left.setText(moneyEntity.getMoney());
				} else {
					Utils.showMessage(callback.getMsg());
				}

			}

			@Override
			public void onFailure(AppException e) {
				cancelProgressDialog();
				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(MyMoneyEntity.class));
		request.execute();
	}

	@Override
	public void onRefresh(final int mode) {
		//
		// currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
		// RequestInformation request = null;
		//
		// String url = UrlHelper.URL_HEAD + "/comment/lists?project_id="
		// + xiangMuEntity.getId() + "&page=" + currPage;
		// Trace.d(url);
		// request = new RequestInformation(url,
		// RequestInformation.REQUEST_METHOD_GET);
		//
		// request.setCallback(new JsonCallback<CommentListEntity>() {
		//
		// @Override
		// public void onCallback(CommentListEntity callback) {
		// if (callback == null) {
		// currPage--;
		// home_listview.setVisibility(View.GONE);
		// home_listview.onRefreshComplete(mode, true);
		// return;
		// }
		//
		// if (callback.getStat() == 200) {
		//
		// if (mode == PullToRefreshView.HEADER) {
		// entities.clear();
		// }
		//
		// ArrayList<CommentEntity> list = callback.getList();
		// entities.addAll(list);
		//
		// listAdapter.setEntities(entities);
		//
		// if (entities.size() > 0) {
		// home_listview.setSelection(0);
		// }
		// home_listview.onRefreshComplete(mode, true);
		// } else {
		// currPage--;
		// // 因为可能网络恢复，success改为true
		// home_listview.onRefreshComplete(mode, true);
		// }
		// stopProgressDialog();
		// }
		//
		// @Override
		// public void onFailure(AppException e) {
		// currPage--;
		// entities.clear();
		// // 因为可能网络恢复，success改为true
		// home_listview.onRefreshComplete(mode, true);
		// stopProgressDialog();
		// }
		// }.setReturnType(CommentListEntity.class));
		//
		// request.execute();
	}

}