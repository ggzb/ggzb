package com.ilikezhibo.ggzb.news.detail;

import com.ilikezhibo.ggzb.AULiveApplication;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.news.NewsFragment;
import com.ilikezhibo.ggzb.news.entity.NewEntity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;
import com.ilikezhibo.ggzb.xiangmu.entity.CommentEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.CommentListEntity;

@SuppressLint("NewApi")
public class NewsDetailActivity extends BaseFragmentActivity implements
		OnClickListener, OnItemClickListener,
		PullToRefreshView.OnRefreshListener {

	private CustomProgressDialog progressDialog = null;
	private Button send_button;

	// 当前的内容
	private NewDetailEntity findDetailEntity;
	private PullToRefreshView home_listview;

	private View listHeadView;
	private ArrayList<CommentEntity> entities;
	private NewListAdapter listAdapter;

	private String newsId;
	private NewEntity mNewEntity;
	private EditText etSendMsg;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_experience_detail_new);
		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("新闻详情");

		send_button = (Button) this.findViewById(R.id.btnSendMsg);
		send_button.setOnClickListener(this);

		newsId = getIntent().getStringExtra(NewsFragment.NewId);
		mNewEntity = (NewEntity) getIntent().getSerializableExtra(
				NewsFragment.NewEntity);

		etSendMsg = (EditText) this.findViewById(R.id.etSendMsg);

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("分享");
		topRightBtn.setOnClickListener(this);

	}

	@Override
	protected void initializeViews() {

		home_listview = (PullToRefreshView) this
				.findViewById(R.id.pull_refresh_list);
		home_listview.setOnItemClickListener(this);
		home_listview.setOnRefreshListener(this);

		// 设置listView的头部
		listHeadView = LayoutInflater.from(this).inflate(
				R.layout.forum_experience_first_item, null);
		home_listview.addHeaderView(listHeadView);
		home_listview.setHeaderDividersEnabled(false);

		listAdapter = new NewListAdapter(this);
		entities = new ArrayList<CommentEntity>();

		listAdapter.setEntities(entities);
		home_listview.setAdapter(listAdapter);

		// 初始化广告
		RelativeLayout bannerContainer = (RelativeLayout) listHeadView
				.findViewById(R.id.bannercontainer);
		bannerContainer.setVisibility(View.GONE);

		// home_listview.initRefresh(PullToRefreshView.HEADER);
		// startProgressDialog();
	}

	@Override
	protected void initializeData() {

		// 图片与按钮去除
		LinearLayout save_and_buy_bt = (LinearLayout) listHeadView
				.findViewById(R.id.save_and_buy_bt);
		save_and_buy_bt.setVisibility(View.VISIBLE);

		Button save_button = (Button) listHeadView
				.findViewById(R.id.save_button);
		save_button.setVisibility(View.VISIBLE);
		save_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				doShare();
			}
		});

		TextView tv_nickname = (TextView) listHeadView
				.findViewById(R.id.tv_nickname);
		tv_nickname.setText("小编");

		TextView tv_time = (TextView) listHeadView.findViewById(R.id.tv_time);
		tv_time.setText(mNewEntity.getAdd_time());

		TextView tv_title = (TextView) listHeadView.findViewById(R.id.tv_title);
		tv_title.setText(mNewEntity.getTitle());

		listHeadView.findViewById(R.id.forum_review_avatar);

		mWebView = (WebView) listHeadView.findViewById(R.id.tv_content);

		// mWebView.setWebChromeClient(new WebChromeClient());
		// mWebView.setDownloadListener(new MyWebViewDownLoadListener());
		mWebView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 当开启新的页面的时候用webview来进行处理而不是用系统自带的浏览器处理
				// view.loadUrl(url);
				// Trace.d("url:" + url);
				//
				// return true;
				return false;
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// super.onReceivedSslError(view, handler, error);
				// handler.cancel(); // 默认的处理方式，WebView变成空白页
				handler.proceed();// 接受证书
				// handleMessage(Message msg); 其他处理
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}
		});

		WebSettings webSettings = mWebView.getSettings();
		// webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("gb2312");
		webSettings.setAllowFileAccess(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAllowContentAccess(true);

		getDetailTask(newsId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		home_listview.initRefresh(PullToRefreshView.HEADER);

		if (mWebView != null) {
			mWebView.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mWebView != null) {
			mWebView.onPause();
		}
	}

	private ShareHelper shareDialog;

	// 分享相关
	private void doShare() {

		String target_url = null;
		try {
			target_url = "http://phone.qxj.me/news/share?id="
					+ mNewEntity.getId();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (shareDialog == null) {
			shareDialog = new ShareHelper(this);
		}

		shareDialog.setShareUrl(target_url);
		shareDialog.setShareTitle(Utils.trans(R.string.app_name));
		shareDialog
				.setShareContent(mNewEntity.getTitle(), mNewEntity.getPics());

		if (!shareDialog.isShowing()) {
			shareDialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.topRightBtn:
			doShare();
			break;

		case R.id.btnSendMsg:
			// 当还没登陆
			if (AULiveApplication.getUserInfo() == null) {
				Intent login = new Intent(this, LoginActivity.class);
				startActivity(login);
				Utils.showMessage("您还没登陆");
				return;
			}
			if (Utils.isLogin(NewsDetailActivity.this)) {
				if (verify()) {
					doSendReply(etSendMsg.getText().toString());
				}
			}
			break;

		}
	}

	private boolean verify() {

		if (etSendMsg.getText() == null
				|| etSendMsg.getText().toString().equals("")) {
			Utils.showMessage("你输入的内容为空");
			return false;
		}
		if (etSendMsg.getText().toString().length() < 2) {
			Utils.showMessage("请输入2个字符以上");
			return false;
		}
		if (etSendMsg.getText().toString().length() > 40) {
			Utils.showMessage("请输入40个字符以内");
			return false;
		}
		return true;
	}

	private void doSendReply(String content) {
		// startProgressDialog();

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/news/comment_add", RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("news_id", mNewEntity.getId());
		request.addPostParams("cont", content);
		request.addPostParams("type", "0");
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("\"评论\"成功");
					// 重新加载
					
					etSendMsg.setText("");
					
					//收起键盘
					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(NewsDetailActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					home_listview.initRefresh(PullToRefreshView.HEADER);
					
				} else {
					Utils.showMessage(callback.getMsg());
				}

			}

			@Override
			public void onFailure(AppException e) {
				cancelProgressDialog();
				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	// 滚动到最后一行
	private void toTheEndOfListView() {
		if (entities.size() <= 0) {
			return;
		}
		int position = entities.size() - 1;
		home_listview.setSelection(home_listview.getBottom());
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

	private WebView mWebView;

	private void getDetailTask(String id) {
		startProgressDialog();

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/news/findone" + "?news_id=" + id,
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<NewEntity>() {

			@Override
			public void onCallback(NewEntity callback) {

				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					try {
						// 去顶部白条
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					stopProgressDialog();
					mWebView.loadDataWithBaseURL(null, callback.getCont(),
							"text/html", "UTF-8", null);

					// tv_content.loadData(mNewEntity.getCont(),
					// "text/html; charset=UTF-8",
					// null);

				} else {

					Utils.showMessage(callback.getMsg());
				}
				stopProgressDialog();
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage("获取网络数据失败");
				stopProgressDialog();
			}
		}.setReturnType(NewEntity.class));
		request.execute();
	}

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}

	}

	private void doSaveFav(String id) {
		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "?id=" + id, RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("收藏成功");

				} else {
					Utils.showMessage(callback.getMsg());
				}

			}

			@Override
			public void onFailure(AppException e) {
				stopProgressDialog();
				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(BaseEntity.class));
		request.execute();
	}

	public final static String REPLY_ENTITY_KEY = "Reply_entity";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Intent intent = new Intent(this, ReplyDetailActivity.class);
		// CommentEntity entity = (CommentEntity) parent.getAdapter().getItem(
		// position);
		// if (entity == null || entity.getId() == null
		// || entity.getId().equals("")) {
		// return;
		// }
		// intent.putExtra(REPLY_ENTITY_KEY, entity);
		// startActivity(intent);
	}

	private int currPage = 1;

	@Override
	public void onRefresh(final int mode) {

		currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
		RequestInformation request = null;

		String url = UrlHelper.URL_HEAD + "/news/comment_lists?news_id="
				+ mNewEntity.getId() + "&page=" + currPage;
		Trace.d(url);
		request = new RequestInformation(url,
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<CommentListEntity>() {

			@Override
			public void onCallback(CommentListEntity callback) {
				if (callback == null) {
					currPage--;
					home_listview.setVisibility(View.GONE);
					home_listview.onRefreshComplete(mode, true);
					return;
				}

				if (callback.getStat() == 200) {

					if (mode == PullToRefreshView.HEADER) {
						entities.clear();
					}

					ArrayList<CommentEntity> list = callback.getList();
					entities.addAll(list);

					listAdapter.setEntities(entities);

//					if (entities.size() > 0) {
//						home_listview.setSelection(0);
//					}
					home_listview.onRefreshComplete(mode, true);
					toTheEndOfListView();
				} else {
					currPage--;
					// 因为可能网络恢复，success改为true
					home_listview.onRefreshComplete(mode, true);
				}
				stopProgressDialog();
			}

			@Override
			public void onFailure(AppException e) {
				currPage--;
				entities.clear();
				// 因为可能网络恢复，success改为true
				home_listview.onRefreshComplete(mode, true);
				stopProgressDialog();
			}
		}.setReturnType(CommentListEntity.class));

		request.execute();
	}

}