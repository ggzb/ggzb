package com.ilikezhibo.ggzb.userinfo.mymoney.inoutrecord;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.UrlHelper;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;

/**
 * @ClassName: InOutFragment
 * @Description: 收支fragment
 * @author big
 * @date 2015-7-16 下午9:49:00
 * 
 */

@SuppressLint("ValidFragment")
public class InOutFragment extends BaseFragment implements OnClickListener,
		PullToRefreshView.OnRefreshListener, OnItemClickListener {

	private CustomProgressDialog progressDialog = null;
	private PullToRefreshView home_listview;
	private int currPage = 1;
	private TextView msgInfoTv;
	private ArrayList<InOutEntity> entities;
	private InOutAdapter experienceListViewAdapter;

	public InOutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.bangmamatao_experience_layout,
				null);

		Button rl_back = (Button) view.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) view.findViewById(R.id.title);
		tv_title.setText("收支列表");

		msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);

		entities = new ArrayList<InOutEntity>();
		home_listview = (PullToRefreshView) view
				.findViewById(R.id.pull_to_refresh_listview);
		home_listview.setOnRefreshListener(this);
		home_listview.setOnItemClickListener(this);

		// 设置listView的头部
		// View listHeadView = LayoutInflater.from(this.getActivity()).inflate(
		// R.layout.layer_experience_header, null);
		// home_listview.addHeaderView(listHeadView);
		// home_listview.setHeaderDividersEnabled(false);

		experienceListViewAdapter = new InOutAdapter(this.getActivity());
		home_listview.setAdapter(experienceListViewAdapter);
		experienceListViewAdapter.setEntities(entities);
		// 开始获取数据
		home_listview.initRefresh(PullToRefreshView.HEADER);
		// startProgressDialog();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			InOutFragment.this.getActivity().finish();
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public final static String NewId = "NewId";
	public final static String InOutEntity = "InOutEntity";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Intent authorIntent = new Intent(InOutFragment.this.getActivity(),
		// NewsDetailActivity.class);
		// InOutEntity entity = (InOutEntity) parent.getAdapter()
		// .getItem(position);
		// authorIntent.putExtra(NewId, entity.getId());
		// authorIntent.putExtra(InOutEntity, entity);
		// startActivity(authorIntent);
	}

	@Override
	public void onRefresh(final int mode) {

		currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
		RequestInformation request = null;
		StringBuilder sb = new StringBuilder(UrlHelper.URL_HEAD
				+ "/pay/logs_list" + "?page=" + currPage);

		request = new RequestInformation(sb.toString(),
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<InOutListEntity>() {

			@Override
			public void onCallback(InOutListEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					currPage--;
					msgInfoTv.setText(R.string.get_info_fail);
					msgInfoTv.setVisibility(View.VISIBLE);
					home_listview.setVisibility(View.GONE);
					home_listview.onRefreshComplete(mode, false);
					home_listview.enableFooter(false);
					return;
				}

				if (callback.getStat() == 200) {
					msgInfoTv.setVisibility(View.GONE);
					if (mode == PullToRefreshView.HEADER) {
						entities.clear();
					}

					if (callback.getList() != null) {
						entities.addAll(callback.getList());
					}

					experienceListViewAdapter.setEntities(entities);
					experienceListViewAdapter.notifyDataSetChanged();
					home_listview.onRefreshComplete(mode, true);

					if (mode == PullToRefreshView.HEADER
							|| (callback.getList() != null && callback
									.getList().size() > 0)) {
						home_listview.enableFooter(true);
					} else {
						home_listview.enableFooter(false);
					}
				} else {
					stopProgressDialog();
					currPage--;
					msgInfoTv.setText(callback.getMsg());
					msgInfoTv.setVisibility(View.VISIBLE);
					// 因为可能网络恢复，success改为true
					home_listview.onRefreshComplete(mode, false);
					home_listview.enableFooter(false);
				}
			}

			@Override
			public void onFailure(AppException e) {
				stopProgressDialog();
				currPage--;
				entities.clear();
				// 因为可能网络恢复，success改为true
				home_listview.onRefreshComplete(mode, true);
				home_listview.enableFooter(false);
				msgInfoTv.setText(R.string.get_info_fail);
				msgInfoTv.setVisibility(View.VISIBLE);
			}
		}.setReturnType(InOutListEntity.class));

		request.execute();
	}

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this
					.getActivity());
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

}
