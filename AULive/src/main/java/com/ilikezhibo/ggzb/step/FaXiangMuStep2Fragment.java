package com.ilikezhibo.ggzb.step;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import java.util.ArrayList;

/**
 * @ClassName: FenleiFragment
 * @Description: 分类fragment
 * @author big
 * @date 2014-11-13 下午9:49:00
 * 
 */

@SuppressLint("ValidFragment")
public class FaXiangMuStep2Fragment extends BaseFragment implements
		OnClickListener, PullToRefreshView.OnRefreshListener,
		OnItemClickListener {

	private View view;
	private CustomProgressDialog progressDialog = null;
	private PullToRefreshView home_listview;
	private int currPage = 1;
	private TextView msgInfoTv;
	private ArrayList<AdvEntity> entities;
	private FaXiangMuStep2Adapter fenLeiAdapter;

	private String url;
	private String project_id;

	public FaXiangMuStep2Fragment(String id1, String m_url) {
		this.project_id = id1;
		url = m_url;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.step_shihuo_1_layout, null);

		Button rl_back = (Button) view.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) view.findViewById(R.id.title);
		tv_title.setText("所需职业");

		TextView topRightBtn = (TextView) view.findViewById(R.id.topRightBtn);
		topRightBtn.setText("下一步");
		topRightBtn.setOnClickListener(this);

		msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);

		home_listview = (PullToRefreshView) view
				.findViewById(R.id.pull_to_refresh_listview);
		home_listview.setOnRefreshListener(this);
		// home_listview.setOnItemClickListener(this);


		View listHeadView = LayoutInflater.from(this.getActivity()).inflate(
				R.layout.faxiangmu_step2_first_item, null);
		home_listview.addHeaderView(listHeadView);
		home_listview.setHeaderDividersEnabled(false);
		
		startProgressDialog();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		entities = new ArrayList<AdvEntity>();
		fenLeiAdapter = new FaXiangMuStep2Adapter(this.getActivity());
		home_listview.setAdapter(fenLeiAdapter);
		fenLeiAdapter.setEntities(entities);

		// 开始获取数据
		home_listview.initRefresh(PullToRefreshView.HEADER);
	}

	public static String SHIHUOSTEP_JOBS_TYPE = "SHIHUOSTEP_JOBS_TYPE";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.topRightBtn:

			ArrayList<AdvEntity> temp = new ArrayList<AdvEntity>();

			for (AdvEntity advEntity : entities) {
				if (advEntity.isChecked()) {
					temp.add(advEntity);
				}
			}
			
			if(temp.size()<=0)
			{
				Utils.showMessage("至少选择一个职业");
				return;
			}
			Intent intent = new Intent(
					FaXiangMuStep2Fragment.this.getActivity(),
					FaXiangMuStep3Activity.class);
			intent.putExtra(SHIHUOSTEP_JOBS_TYPE, temp);
			intent.putExtra(FaXiangMuStep1Activity.PROJECT_ID, project_id);
			startActivity(intent);
			break;
		case R.id.reg_bt:

			break;

		case R.id.back:
			this.getActivity().finish();
			break;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public final static String ShaiWuId = "ShaiWuId";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Intent authorIntent = new Intent(FenleiFragment.this.getActivity(),
		// ShaiWuDetailActivity.class);
		// ShaiWuEntity entity = (ShaiWuEntity) parent.getAdapter().getItem(
		// position);
		// authorIntent.putExtra(ShaiWuId, entity.getId());
		// startActivity(authorIntent);
	}

	@Override
	public void onRefresh(final int mode) {

		currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
		RequestInformation request = null;
		StringBuilder sb = new StringBuilder(url + "?page=" + currPage);

		request = new RequestInformation(sb.toString(),
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<FenLeiListEntity>() {

			@Override
			public void onCallback(FenLeiListEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					currPage--;
					msgInfoTv.setText(R.string.get_info_fail);
					msgInfoTv.setVisibility(View.VISIBLE);
					home_listview.setVisibility(View.GONE);
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

					fenLeiAdapter.setEntities(entities);
					boolean isShowFooter = false;
					if (mode == PullToRefreshView.HEADER
							|| (callback.getList() != null && callback
									.getList().size() > 0)) {
						isShowFooter = true;
					}
					home_listview.onRefreshComplete(mode, true);
					fenLeiAdapter.notifyDataSetChanged();
				} else {
					stopProgressDialog();
					currPage--;
					msgInfoTv.setText(callback.getMsg());
					msgInfoTv.setVisibility(View.VISIBLE);
					// 因为可能网络恢复，success改为true
					home_listview.onRefreshComplete(mode, false);
				}
			}

			@Override
			public void onFailure(AppException e) {
				stopProgressDialog();
				currPage--;
				entities.clear();
				// 因为可能网络恢复，success改为true
				home_listview.onRefreshComplete(mode, false);
				msgInfoTv.setText(R.string.get_info_fail);
				msgInfoTv.setVisibility(View.VISIBLE);
			}
		}.setReturnType(FenLeiListEntity.class));

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
