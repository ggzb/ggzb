package com.ilikezhibo.ggzb.xiangmuguanli;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.step.ProjectTypeEntity;
import com.ilikezhibo.ggzb.step.view.SubWayDialogListener;
import com.ilikezhibo.ggzb.step.view.SubWayLineDialog;
import com.ilikezhibo.ggzb.userinfo.ProfileActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.xiangmu.FaXiangMuDetailActivity;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuListEntity;
import java.util.ArrayList;

public class XiangMuGuanLiActivity extends BaseFragmentActivity implements
		OnClickListener {

	private String project_id = null;
	private String faxiangmu_id = null;
	public static String project_id_key = "project_id_key";
	public static String faxiangmu_uid_key = "faxiangmu_uid_key";
	public static String project_entity_key = "project_entity_key";

	private XiangMuEntity xiangMuEntity;

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_xiangmuguanli);

	}

	@Override
	protected void initializeViews() {

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("项目管理");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("项目详情");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.VISIBLE);

		bid_main_layout = (LinearLayout) findViewById(R.id.bid_layout0);
		baoming_tv = (TextView) findViewById(R.id.baoming_tv);
		bid_layout = (LinearLayout) findViewById(R.id.bid_layout);

		project_id = getIntent().getStringExtra(project_id_key);
		faxiangmu_id = getIntent().getStringExtra(faxiangmu_uid_key);
		// xiangMuEntity = (XiangMuEntity) getIntent().getSerializableExtra(
		// project_entity_key);
	}

	@Override
	protected void initializeData() {

	}

	@Override
	protected void onResume() {
		super.onResume();
		getBaoMingTask(project_id);
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			Intent intent = new Intent(XiangMuGuanLiActivity.this,
					FaXiangMuDetailActivity.class);
			intent.putExtra(FaXiangMuDetailActivity.FAXIANGMUENTITY_KEY,
					project_id);
			startActivity(intent);
			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	private LinearLayout bid_main_layout;
	private TextView baoming_tv;
	private LinearLayout bid_layout;

	private void getBaoMingTask(String id) {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/lists_status?project_id=" + id,
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<XiangMuListEntity>() {

			@Override
			public void onCallback(XiangMuListEntity callback) {

				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					ArrayList<XiangMuEntity> entities = callback.getList();

					bid_layout.removeAllViews();

					// 第一个为群主
					xiangMuEntity = entities.get(0);
					if (xiangMuEntity == null || xiangMuEntity.getUid() == null
							|| xiangMuEntity.getUid().equals("")) {
						Utils.showMessage("项目组数组出错");
						return;
					}

					// 每一个item一个view
					for (final XiangMuEntity xiangMuEntity : entities) {

						View baoming_item = LayoutInflater.from(
								XiangMuGuanLiActivity.this).inflate(
								R.layout.xiangmuguanli_list_item, null);

						ImageView iv_face = (ImageView) baoming_item
								.findViewById(R.id.iv_face);
						iv_face.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent profile_intent = new Intent(
										XiangMuGuanLiActivity.this,
										ProfileActivity.class);
								profile_intent.putExtra(
										ProfileActivity.PROFILE_UID,
										xiangMuEntity.getUid());
								startActivity(profile_intent);
							}
						});

						ImageLoader.getInstance().displayImage(
								xiangMuEntity.getFace(), iv_face,
								AULiveApplication.getGlobalImgOptions());

						TextView tv_nickname = (TextView) baoming_item
								.findViewById(R.id.tv_nickname);
						tv_nickname.setText(xiangMuEntity.getNickname());

						TextView tv_time = (TextView) baoming_item
								.findViewById(R.id.tv_time);
						tv_time.setText(xiangMuEntity.getAdd_time());

						// 价格显示
						String uid = AULiveApplication.getUserInfo().getUid();
						if (XiangMuGuanLiActivity.this.xiangMuEntity.getUid()
								.equals(uid)) {
							// 发项目者
							String dev_money = xiangMuEntity.getDev_money();
							if (dev_money != null && !dev_money.equals("")) {
								TextView money_tv = (TextView) baoming_item
										.findViewById(R.id.money_tv);
								money_tv.setText(dev_money + "元");
							}

						} else {
							// 只显示自己的钱状态
							if (xiangMuEntity.getUid().equals(uid)) {
								String dev_money = xiangMuEntity.getDev_money();
								if (dev_money != null && !dev_money.equals("")) {
									TextView money_tv = (TextView) baoming_item
											.findViewById(R.id.money_tv);
									money_tv.setText(dev_money + "元");
								}
							}

						}
						Button sure_baoming = (Button) baoming_item
								.findViewById(R.id.sure_baoming);

						// status 状态：0:报名；1:拒绝 2:考虑过了 3:同意 4:接受 5:申请完成 6:完成
						// 7-技术主动或者同意退出项目 8-被T出项目,等待技术人员确认 ( 查询出来状态是4,5,6,8)

						if (xiangMuEntity.getStatus() == null
								|| xiangMuEntity.getStatus().equals("")) {
							sure_baoming.setText("群主");
							sure_baoming.setClickable(false);

						} else if (xiangMuEntity
								.getStatus()
								.equals(FaXiangMuDetailActivity.STATUS_BAOMING_JIESHOU_4)) {
							sure_baoming.setText("项目开始");
							sure_baoming.setClickable(false);

						} else if (xiangMuEntity
								.getStatus()
								.equals(FaXiangMuDetailActivity.STATUS_SHENQING_WANCHENG_5)) {
							sure_baoming.setText("申请项目完成");
							sure_baoming.setClickable(false);

						} else if (xiangMuEntity.getStatus().equals(
								FaXiangMuDetailActivity.STATUS_WANCHENG_6)) {
							sure_baoming.setText("项目已完成");
							sure_baoming.setClickable(false);

						} else if (xiangMuEntity
								.getStatus()
								.equals(FaXiangMuDetailActivity.STATUS_TUICHU_TONGYIN_OR_ZHUDONG_7)) {
							sure_baoming.setText("已退出项目组");
							sure_baoming.setClickable(false);

						} else if (xiangMuEntity.getStatus().equals(
								FaXiangMuDetailActivity.STATUS_TUICHU_BEI_T_8)) {
							sure_baoming.setText("等待确认移出项目组");
							sure_baoming.setClickable(false);

						}

						sure_baoming.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								if (BtnClickUtils.isFastDoubleClick()) {
									return;
								}

								String uid = AULiveApplication.getUserInfo()
										.getUid();
								String nickname = AULiveApplication
										.getUserInfo().getNickname();
								if (xiangMuEntity.getStatus() == null
										|| xiangMuEntity.getStatus().equals("")) {
									Utils.showMessage("无法对群主做操作");
								} else if (XiangMuGuanLiActivity.this.xiangMuEntity
										.getUid().equals(uid)) {

									// 群主的操作，可以修改别的组员的状态

									if (xiangMuEntity
											.getStatus()
											.equals(FaXiangMuDetailActivity.STATUS_WANCHENG_6)) {
										Utils.showMessage("给"
												+ xiangMuEntity.getNickname()
												+ "评论");
										Intent comment_intent = new Intent(
												XiangMuGuanLiActivity.this,
												CommentActivity.class);
										comment_intent
												.putExtra(
														CommentActivity.project_id_key,
														XiangMuGuanLiActivity.this.xiangMuEntity
																.getPid());
										comment_intent.putExtra(
												CommentActivity.uid_key,
												xiangMuEntity.getUid());
										comment_intent.putExtra(
												CommentActivity.nickname_key,
												xiangMuEntity.getNickname());
										startActivity(comment_intent);
									} else {

										showChange(project_id,
												xiangMuEntity.getUid(),
												xiangMuEntity.getNickname());
									}
								} else {
									// 自己的状态
									if (xiangMuEntity.getUid().equals(uid)) {
										// 已完成
										if (xiangMuEntity
												.getStatus()
												.equals(FaXiangMuDetailActivity.STATUS_WANCHENG_6)) {
											Utils.showMessage("给发项目者评论");
											Intent comment_intent = new Intent(
													XiangMuGuanLiActivity.this,
													CommentActivity.class);
											comment_intent
													.putExtra(
															CommentActivity.project_id_key,
															XiangMuGuanLiActivity.this.xiangMuEntity
																	.getPid());
											comment_intent
													.putExtra(
															CommentActivity.nickname_key,
															XiangMuGuanLiActivity.this.xiangMuEntity
																	.getNickname());
											startActivity(comment_intent);
										} else if (xiangMuEntity
												.getStatus()
												.equals(FaXiangMuDetailActivity.STATUS_TUICHU_BEI_T_8)) {
											// 确认被T群，被T的状态只有技术人员确认后才会移出群聊
											CustomDialog userBlackDialog = null;
											if (userBlackDialog == null) {
												userBlackDialog = new CustomDialog(
														XiangMuGuanLiActivity.this,
														new CustomDialogListener() {
															@Override
															public void onDialogClosed(
																	int closeType) {
																switch (closeType) {
																case CustomDialogListener.BUTTON_POSITIVE:
																	// 确认
																	doQueRenTuiChu(
																			project_id,
																			xiangMuEntity
																					.getUid(),
																			"1");
																	break;
																case CustomDialogListener.BUTTON_NEUTRAL:
																	// 否认,将恢复到原来的状态
																	doQueRenTuiChu(
																			project_id,
																			xiangMuEntity
																					.getUid(),
																			"2");
																	break;
																}
															}
														});

												userBlackDialog
														.setCustomMessage("同意或拒绝被T,确认后将失去项目资金");
												userBlackDialog
														.setCancelable(true);
												userBlackDialog
														.setType(CustomDialog.DOUBLE_BTN);
												userBlackDialog.setButtonText(
														"同意", "拒绝");
											}

											if (null != userBlackDialog) {
												userBlackDialog.show();
											}

										} else {
											// 普通组员，只可以修改自己的状态,申请完成或主动退出，主动退出不用双方确认
											showNormalChange(project_id,
													xiangMuEntity.getUid(),
													xiangMuEntity.getNickname());
										}
									} else {
										// 别人的状态
										Utils.showMessage("不可以修改别人的状态");
									}
								}
							}
						});

						bid_layout.addView(baoming_item);

						// 作评论处理
						String faxiangmu_c = xiangMuEntity.getU_comment();
						int faxingmu_score = xiangMuEntity.getU_score();

						String jisu_comment = xiangMuEntity.getP_comment();
						int jisu_score = xiangMuEntity.getP_score();

						//发项目的评论
						View comment_layout = baoming_item
								.findViewById(R.id.comment_layout);

						if ((faxiangmu_c != null && !faxiangmu_c.equals(""))
								|| (jisu_comment != null && !jisu_comment
										.equals(""))) {
							comment_layout.setVisibility(View.VISIBLE);
						}

						View faxiangmu_comment = baoming_item
								.findViewById(R.id.faxiangmu_comment);
						if (faxiangmu_c != null && !faxiangmu_c.equals("")) {
							faxiangmu_comment.setVisibility(View.VISIBLE);

							if (entities.size() > 0) {

								initComment(faxiangmu_comment, entities.get(0)
										.getNickname(), faxingmu_score,
										faxiangmu_c);
							}
						}

						//技术人员的评论
						View jishuren_comment = baoming_item
								.findViewById(R.id.jishuren_comment);
						if (jisu_comment != null && !jisu_comment.equals("")) {
							jishuren_comment.setVisibility(View.VISIBLE);
							if (entities.size() > 0) {

								initComment(jishuren_comment,
										xiangMuEntity.getNickname(),
										jisu_score, jisu_comment);
							}
						}
					}
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
		}.setReturnType(XiangMuListEntity.class));
		request.execute();
	}

	//设置评论的星星等内容
	private void initComment(View view, String nickname, int score,
			String comment) {
		TextView tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
		tv_nickname.setText(nickname);

		TextView comment_tv = (TextView) view.findViewById(R.id.comment_tv);
		comment_tv.setText(comment);

		CheckBox start1_iv = (CheckBox) view.findViewById(R.id.start1_iv);
		CheckBox start2_iv = (CheckBox) view.findViewById(R.id.start2_iv);
		CheckBox start3_iv = (CheckBox) view.findViewById(R.id.start3_iv);
		CheckBox start4_iv = (CheckBox) view.findViewById(R.id.start4_iv);
		CheckBox start5_iv = (CheckBox) view.findViewById(R.id.start5_iv);

		if (score == 0) {
			start1_iv.setSelected(false);
			start2_iv.setSelected(false);
			start3_iv.setSelected(false);
			start4_iv.setSelected(false);
			start5_iv.setSelected(false);
		}

		if (score == 2) {
			start1_iv.setSelected(true);
			start2_iv.setSelected(false);
			start3_iv.setSelected(false);
			start4_iv.setSelected(false);
			start5_iv.setSelected(false);
		}
		if (score == 4) {
			start1_iv.setSelected(true);
			start2_iv.setSelected(true);
			start3_iv.setSelected(false);
			start4_iv.setSelected(false);
			start5_iv.setSelected(false);
		}
		if (score == 6) {
			start1_iv.setSelected(true);
			start2_iv.setSelected(true);
			start3_iv.setSelected(true);
			start4_iv.setSelected(false);
			start5_iv.setSelected(false);
		}
		if (score == 8) {
			start1_iv.setSelected(true);
			start2_iv.setSelected(true);
			start3_iv.setSelected(true);
			start4_iv.setSelected(true);
			start5_iv.setSelected(false);
		}
		if (score == 10) {
			start1_iv.setSelected(true);
			start2_iv.setSelected(true);
			start3_iv.setSelected(true);
			start4_iv.setSelected(true);
			start5_iv.setSelected(true);
		}

	}

	CustomProgressDialog progressDialog = null;

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

	// //////////////////////////////////////////////////////////////////////////////////////////
	// /群主的操作
	// 选择报名的选择
	private ProjectTypeEntity current_type = null;
	private SubWayLineDialog customDialog = null;

	private void showChange(final String project_id, final String uid,
			final String nickname) {
		ArrayList<ProjectTypeEntity> types = new ArrayList<ProjectTypeEntity>();
		ProjectTypeEntity entity1 = new ProjectTypeEntity();
		entity1.setId(DIALIG_INDEX_KEY_1);
		entity1.setName("完成项目");
		types.add(entity1);

		ProjectTypeEntity entity2 = new ProjectTypeEntity();
		entity2.setId(DIALIG_INDEX_KEY_2);
		entity2.setName("移出项目组");
		types.add(entity2);

		ProjectTypeEntity entity3 = new ProjectTypeEntity();
		entity3.setId(DIALIG_INDEX_KEY_3);
		entity3.setName("取消");
		types.add(entity3);

		customDialog = new SubWayLineDialog(XiangMuGuanLiActivity.this,
				new SubWayDialogListener() {

					@Override
					public void onItemClick(ProjectTypeEntity t_type) {
						current_type = t_type;
						if (t_type.getId().equals(DIALIG_INDEX_KEY_1)) {
							CustomDialog userBlackDialog = null;
							if (userBlackDialog == null) {
								userBlackDialog = new CustomDialog(
										XiangMuGuanLiActivity.this,
										new CustomDialogListener() {
											@Override
											public void onDialogClosed(
													int closeType) {
												switch (closeType) {
												case CustomDialogListener.BUTTON_POSITIVE:
													doWanCheng(project_id, uid);
													break;
												}
											}
										});

								userBlackDialog.setCustomMessage("确认此组员完成项目吗?");
								userBlackDialog.setCancelable(true);
								userBlackDialog
										.setType(CustomDialog.DOUBLE_BTN);
							}

							if (null != userBlackDialog) {
								userBlackDialog.show();
							}

						}
						if (t_type.getId().equals(DIALIG_INDEX_KEY_2)) {
							CustomDialog userBlackDialog = null;
							if (userBlackDialog == null) {
								userBlackDialog = new CustomDialog(
										XiangMuGuanLiActivity.this,
										new CustomDialogListener() {
											@Override
											public void onDialogClosed(
													int closeType) {
												switch (closeType) {
												case CustomDialogListener.BUTTON_POSITIVE:
													doTRen(project_id, uid);
													break;
												}
											}
										});

								userBlackDialog
										.setCustomMessage("确认T此组员出项目组吗?");
								userBlackDialog.setCancelable(true);
								userBlackDialog
										.setType(CustomDialog.DOUBLE_BTN);
							}

							if (null != userBlackDialog) {
								userBlackDialog.show();
							}

						}
						if (t_type.getId().equals(DIALIG_INDEX_KEY_3)) {

						}

						customDialog.dismiss();
					}
				}, types);

		customDialog.setCancelable(true);
		// customDialog.getWindow().setType(
		// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		customDialog.show();
	}

	// 确认完成
	private void doWanCheng(String project_id1, String uid) {
		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/finish?project_id=" + project_id1 + "&uid=" + uid,
				RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("确认\"完成\"成功");
					// 刷新
					getBaoMingTask(project_id);
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

	// 踢人
	private void doTRen(String project_id1, String uid) {
		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/passive_exit?project_id=" + project_id1 + "&uid="
				+ uid, RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("确认\"踢人\"成功");
					// 刷新
					getBaoMingTask(project_id);
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

	// /////////////////////////////////////////////////////////////////////////////////////////////////////
	// 普通组员的操作
	// 选择报名的选择
	private SubWayLineDialog customDialog2 = null;

	private static String DIALIG_INDEX_KEY_1 = "1";
	private static String DIALIG_INDEX_KEY_2 = "2";
	private static String DIALIG_INDEX_KEY_3 = "3";
	private static String DIALIG_INDEX_KEY_4 = "4";

	private void showNormalChange(final String project_id, final String uid,
			final String nickname) {
		ArrayList<ProjectTypeEntity> types = new ArrayList<ProjectTypeEntity>();
		ProjectTypeEntity entity1 = new ProjectTypeEntity();
		entity1.setId(DIALIG_INDEX_KEY_1);
		entity1.setName("申请完成项目");
		types.add(entity1);

		ProjectTypeEntity entity2 = new ProjectTypeEntity();
		entity2.setId(DIALIG_INDEX_KEY_2);
		entity2.setName("退出项目组");
		types.add(entity2);

		ProjectTypeEntity entity3 = new ProjectTypeEntity();
		entity3.setId(DIALIG_INDEX_KEY_3);
		entity3.setName("取消");
		types.add(entity3);

		customDialog2 = new SubWayLineDialog(XiangMuGuanLiActivity.this,
				new SubWayDialogListener() {

					@Override
					public void onItemClick(ProjectTypeEntity t_type) {
						if (t_type.getId().equals(DIALIG_INDEX_KEY_1)) {
							CustomDialog userBlackDialog = null;
							if (userBlackDialog == null) {
								userBlackDialog = new CustomDialog(
										XiangMuGuanLiActivity.this,
										new CustomDialogListener() {
											@Override
											public void onDialogClosed(
													int closeType) {
												switch (closeType) {
												case CustomDialogListener.BUTTON_POSITIVE:
													doShenQinWanCheng(
															project_id, uid);
													break;
												}
											}
										});

								userBlackDialog.setCustomMessage("确认申请完成项目吗?");
								userBlackDialog.setCancelable(true);
								userBlackDialog
										.setType(CustomDialog.DOUBLE_BTN);
							}

							if (null != userBlackDialog) {
								userBlackDialog.show();
							}

						}
						if (t_type.getId().equals(DIALIG_INDEX_KEY_2)) {
							CustomDialog userBlackDialog = null;
							if (userBlackDialog == null) {
								userBlackDialog = new CustomDialog(
										XiangMuGuanLiActivity.this,
										new CustomDialogListener() {
											@Override
											public void onDialogClosed(
													int closeType) {
												switch (closeType) {
												case CustomDialogListener.BUTTON_POSITIVE:
													doTuiChuXiangMu(project_id,
															uid);
													break;
												}
											}
										});

								userBlackDialog.setCustomMessage("确认主动退出项目组吗?");
								userBlackDialog.setCancelable(true);
								userBlackDialog
										.setType(CustomDialog.DOUBLE_BTN);
							}

							if (null != userBlackDialog) {
								userBlackDialog.show();
							}

						}
						if (t_type.getId().equals(DIALIG_INDEX_KEY_3)) {

						}

						customDialog2.dismiss();
					}
				}, types);

		customDialog2.setCancelable(true);
		// customDialog.getWindow().setType(
		// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		customDialog2.show();
	}

	// 申请完成
	private void doShenQinWanCheng(String project_id1, String uid) {
		Trace.d("project_id:" + project_id1);
		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/finish_before?project_id=" + project_id1 + "&uid="
				+ uid, RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("确认\"申请完成\"成功");
					// 刷新
					getBaoMingTask(project_id);
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

	// 退出项目组
	private void doTuiChuXiangMu(String project_id1, String uid) {
		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/initiative_exit?project_id=" + project_id1 + "&uid="
				+ uid, RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					Utils.showMessage("确认\"踢人\"成功");
					// 刷新
					getBaoMingTask(project_id);
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

	// 确认退出
	private void doQueRenTuiChu(String project_id1, String uid,
			final String agree) {
		// startProgressDialog();
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/apply/passive_agree?project_id=" + project_id1 + "&uid="
				+ uid + "&agree=" + agree,
				RequestInformation.REQUEST_METHOD_GET);
		request.setCallback(new JsonCallback<BaseEntity>() {

			@Override
			public void onCallback(BaseEntity callback) {
				stopProgressDialog();
				if (callback == null) {
					return;
				}

				if (callback.getStat() == 200) {
					if (agree.equals("1")) {
						Utils.showMessage("\"确认\"成功");
					} else {
						Utils.showMessage("\"否认\"成功");
					}
					// 刷新
					getBaoMingTask(project_id);
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

}
