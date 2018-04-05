package com.ilikezhibo.ggzb.xiangmu;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gauss.recorder.SpeexPlayer;
import com.gauss.speex.encode.SpeexDecoder;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.callback.PathCallback;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.FileUtil;
import com.jack.utils.OpenFileUtils;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.photos.photobrowser.PicBrowseActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.step.FaXiangMuStep1Activity;
import com.ilikezhibo.ggzb.step.ProjectTypeEntity;
import com.ilikezhibo.ggzb.step.UpLoadFileEntity;
import com.ilikezhibo.ggzb.step.adapter.FileAddAdapter;
import com.ilikezhibo.ggzb.step.adapter.JingJiaAddAdapter;
import com.ilikezhibo.ggzb.step.view.SubWayDialogListener;
import com.ilikezhibo.ggzb.step.view.SubWayLineDialog;
import com.ilikezhibo.ggzb.userinfo.ProfileActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;
import com.ilikezhibo.ggzb.xiangmu.adapter.JingJiaDetailAdapter;
import com.ilikezhibo.ggzb.xiangmu.entity.CeateGroupEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.CommentEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.CommentListEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuListEntity;
import com.ilikezhibo.ggzb.xiangmuguanli.XiangMuGuanLiActivity;
import java.util.ArrayList;

public class FaXiangMuDetailActivity extends BaseFragmentActivity
    implements OnItemClickListener, PullToRefreshView.OnRefreshListener, OnClickListener {

   private CustomProgressDialog progressDialog = null;
   // 当前的内容

   private PullToRefreshView home_listview;
   private View listHeadView;
   private ArrayList<CommentEntity> entities;
   private JingJiaDetailAdapter listAdapter;

   private int currPage = 1;
   private boolean backh_home;

   private TextView tv_edit;

   private String xiangMu_id = null;
   private XiangMuEntity xiangMuEntity = null;

   private EditText etSendMsg;
   private Button send_button;
   public static String FAXIANGMUENTITY_KEY = "FAXIANGMUENTITY_KEY";

   public static String STATUS_BAOMING_NO_01 = "-1";
   public static String STATUS_BAOMING_ING_0 = "0";
   public static String STATUS_BAOMING_JUJUE_1 = "1";
   public static String STATUS_BAOMING_KAOLU_2 = "2";
   public static String STATUS_BAOMING_TONGYI_3 = "3";
   public static String STATUS_BAOMING_JIESHOU_4 = "4";
   public static String STATUS_SHENQING_WANCHENG_5 = "5";
   public static String STATUS_WANCHENG_6 = "6";
   public static String STATUS_TUICHU_TONGYIN_OR_ZHUDONG_7 = "7";
   public static String STATUS_TUICHU_BEI_T_8 = "8";
   public static String STATUS_BAOMING_JUJUE_JIAGE_9 = "9";

   @Override protected void setContentView() {
      setContentView(R.layout.jiesihuo_detail);
      Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      TextView tv_title = (TextView) this.findViewById(R.id.title);
      tv_title.setText("项目详情");

      tv_edit = (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.topRightBtn);
      tv_edit.setOnClickListener(FaXiangMuDetailActivity.this);
      tv_edit.setText("修改");
      tv_edit.setVisibility(View.GONE);

      // backh_home = getIntent().getBooleanExtra(
      // PushMessageReceiver.backh_home_key, false);
      xiangMu_id = getIntent().getStringExtra(FAXIANGMUENTITY_KEY);

      send_button = (Button) this.findViewById(R.id.btnSendMsg);
      send_button.setOnClickListener(this);
      etSendMsg = (EditText) this.findViewById(R.id.etSendMsg);
   }

   @Override protected void initializeViews() {
      initListViews();

      fav_layout = (LinearLayout) this.findViewById(R.id.fav_layout);

      LinearLayout share_layout = (LinearLayout) this.findViewById(R.id.share_layout);
      share_layout.setOnClickListener(this);

      LinearLayout add_layout = (LinearLayout) this.findViewById(R.id.add_layout);
      add_layout.setOnClickListener(this);

      bid_main_layout = (LinearLayout) FaXiangMuDetailActivity.this.findViewById(R.id.bid_layout0);
      baoming_tv = (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.baoming_tv);
      bid_layout = (LinearLayout) FaXiangMuDetailActivity.this.findViewById(R.id.bid_layout);

      // 图片列表
      upload_pic_grid = (GridView) findViewById(R.id.froum_create_grid);
      upload_pic_grid.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (pic_urls == null || pic_urls.size() == 0) {
               return;
            }
            if (urls == null || urls.length == 0) {
               return;
            }

            Intent intent = new Intent(FaXiangMuDetailActivity.this, PicBrowseActivity.class);
            intent.putExtra(PicBrowseActivity.INTENT_BROWSE_POS_KEY, position);
            intent.putExtra(PicBrowseActivity.INTENT_BROWSE_LST_KEY, urls);
            startActivity(intent);
         }
      });

      // 附件列表
      file_grid = (GridView) findViewById(R.id.file_grid);
      file_grid.setOnItemClickListener(new OnItemClickListener() {

         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (file_urls == null || file_urls.size() == 0) {
               return;
            }
            if (f_urls == null || f_urls.length == 0) {
               return;
            }

            try {
               // 下载与打开文件
               OpenFile(f_urls[position]);
            } catch (Exception e) {
               Utils.showMessage("打开文件失败,正确安装相关应用后再试");
            }
         }
      });

      // 图片
      addAdapter = new JingJiaAddAdapter(this);
      addAdapter.setEntities(pic_urls);
      upload_pic_grid.setAdapter(addAdapter);

      // 语音
      publishThemeVoiceBtn = (Button) this.findViewById(R.id.publishThemeVoiceBtn);
      publishThemeVoiceBtn.setOnClickListener(this);

      // 附件
      fileAddAdapter = new FileAddAdapter(this);
      fileAddAdapter.setEntities(file_urls);
      file_grid.setAdapter(fileAddAdapter);
   }

   @Override protected void initializeData() {

   }

   private void initListViews() {

      home_listview = (PullToRefreshView) this.findViewById(R.id.pull_refresh_list);
      home_listview.setOnRefreshListener(this);
      home_listview.setOnItemClickListener(this);

      // 设置listView的头部
      listHeadView =
          LayoutInflater.from(this).inflate(R.layout.faxingmu_detail_first_item_new, null);
      home_listview.addHeaderView(listHeadView);
      home_listview.setHeaderDividersEnabled(false);

      listAdapter = new JingJiaDetailAdapter(this);

      entities = new ArrayList<CommentEntity>();
      listAdapter.setEntities(entities);
      home_listview.setAdapter(listAdapter);

      // startProgressDialog();
   }

   @Override protected void onResume() {
      super.onResume();
      // getDetailTask(xiangMu_id);
      home_listview.initRefresh(PullToRefreshView.HEADER);
   }

   public static String LOCATION_KEY = "LOCATION_KEY";

   @Override public void onClick(View v) {
      switch (v.getId()) {

         case R.id.share_layout:
            doShare();
            break;

         case R.id.fav_layout:
            doFav(xiangMuEntity.getId());
            break;
         case R.id.add_layout:

            break;
         case R.id.back:
            if (backh_home) {
               Intent intent = new Intent(this, AULiveHomeActivity.class);
               startActivity(intent);
               this.finish();
            } else {
               this.finish();
            }
            break;
         case R.id.publishThemeVoiceBtn:// 主题语音播放
            if (voiceLocalPath == null) {
               Utils.showMessage("没有语音");
            } else {

               if (sPlayer == null) {
                  playCurrVoice(voiceLocalPath, publishThemeVoiceBtn);
               } else if (sPlayer.isPaused()) {
                  playCurrVoice(voiceLocalPath, publishThemeVoiceBtn);
               } else {
                  publishThemeVoiceBtn.setCompoundDrawablesWithIntrinsicBounds(
                      R.drawable.ondate_voice_play, 0, 0, 0);
                  sPlayer.stopPlay();
               }
            }
            break;

         case R.id.btnSendMsg:
            // 当还没登陆
            if (AULiveApplication.getUserInfo() == null) {
               Intent login = new Intent(this, LoginActivity.class);
               startActivity(login);
               Utils.showMessage("您还没登陆");
               return;
            }
            if (Utils.isLogin(FaXiangMuDetailActivity.this)) {
               if (verify()) {
                  doSendReply(etSendMsg.getText().toString());
               }
            }
            break;
         case R.id.topRightBtn:
            Intent intent = new Intent(this, FaXiangMuModifyDelete.class);
            startActivityForResult(intent, 100);
            break;
      }
   }

   private String voiceLocalPath;// 语音路径
   private SpeexPlayer sPlayer;
   private Button publishThemeVoiceBtn;

   /**
    * playCurrVoice:
    *
    * @Description: 播放当前录音
    * @author jack.long
    */
   private void playCurrVoice(String voiceUrl, Button btn) {
      if (FileUtil.isFileExist(FileUtil.RECORD_PATH + getVoiceTag(voiceUrl))) {
         Trace.d("本地录音文件存在");
         btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ondate_voice_pause, 0, 0, 0);
         startPlayVoice(FileUtil.RECORD_PATH + getVoiceTag(voiceUrl), btn);
      } else {
         downFile(voiceUrl, btn);
      }
   }

   // 下载文件
   private void downFile(final String voiceUrl, final Button playBtn) {
      FileUtil.createDir(FileUtil.RECORD_PATH);
      RequestInformation request =
          new RequestInformation(voiceUrl, RequestInformation.REQUEST_METHOD_GET);
      request.setProgressChangeListener(new IProgressListener() {

         @Override public void progressChanged(int status, int progress, String operationName) {
            if (progress == 100) {
               Trace.d("fileName:" + getVoiceTag(voiceUrl));
               // final ImageView playImg = (ImageView) listView
               // .findViewWithTag(getFileName(entity) + VOICE_SINGAL);
               playBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ondate_voice_pause, 0, 0,
                   0);
               String localVoicePath = FileUtil.RECORD_PATH + getVoiceTag(voiceUrl);
               startPlayVoice(localVoicePath, playBtn);

               // if (entity.getUrl().endsWith("spx")) {
               // } else {
               // playVoice(playUrl, playImg, entity.isComing());
               // }
            }
         }
      });
      request.setCallback(new PathCallback() {

         @Override public void onFailure(AppException e) {

         }

         @Override public void onCallback(String callback) {
         }
      }.setFilePath(FileUtil.RECORD_PATH + getVoiceTag(voiceUrl)));
      request.execute();
   }

   // 获取语音tag
   private String getVoiceTag(String voiceUrl) {
      if (voiceUrl == null) {
         return "";
      }

      return voiceUrl.substring(voiceUrl.lastIndexOf("/") + 1);
   }

   // 播放语音
   private void startPlayVoice(final String localVoicePath, final Button btn) {
      sPlayer = new SpeexPlayer(localVoicePath);
      sPlayer.startPlay();
      sPlayer.setSpeexCompletionListener(new SpeexDecoder.SpeexCompletionListener() {

         @Override public void onCompletion() {
            // TODO Auto-generated method stub
            Message msg = new Message();
            msg.what = VOICE_COMPLETION;
            msg.obj = btn;
            mHandler.sendMessage(msg);
         }
      });
   }

   private static final int VOICE_COMPLETION = 0;
   private Handler mHandler = new Handler() {
      @Override public void handleMessage(Message msg) {
         switch (msg.what) {
            case VOICE_COMPLETION:
               if (msg.obj != null) {
                  Button btn = (Button) msg.obj;
                  btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ondate_voice_play, 0, 0,
                      0);
               }
               if (sPlayer != null) {
                  sPlayer.stopPlay();
               }
               sPlayer = null;
               break;
         }
         super.handleMessage(msg);
      }
   };

   // 按下返回键时
   @Override public void onBackPressed() {

      if (backh_home) {
         Intent intent = new Intent(this, AULiveApplication.class);
         startActivity(intent);
         this.finish();
      } else {
         this.finish();
      }
   }

   private boolean verify() {

      if (etSendMsg.getText() == null || etSendMsg.getText().toString().equals("")) {
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

   private void doSendReply(String content) {
      // startProgressDialog();
      RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD + "/comment/add",
          RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("project_id", xiangMuEntity.getId());
      request.addPostParams("cont", content);
      request.addPostParams("type", "0");
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("\"评论\"成功");

               etSendMsg.setText("");
               // 收起键盘
               ((InputMethodManager) getSystemService(
                   Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                   FaXiangMuDetailActivity.this.getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
               // 重新加载
               home_listview.initRefresh(PullToRefreshView.HEADER);
               toTheEndOfListView();
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
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

   // 跳转到项目管理
   private void jumpToGroupChat() {
      // 跳到项目管理
      // Intent guanli_Intent = new Intent(this, XiangMuGuanLiActivity.class);
      // guanli_Intent.putExtra(XiangMuGuanLiActivity.project_id_key,
      // xiangMuEntity.getId());
      // guanli_Intent.putExtra(XiangMuGuanLiActivity.project_entity_key,
      // xiangMuEntity);
      // startActivity(guanli_Intent);

      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }
      // 发起群聊
      createGroup();
   }

   private void addJumpGroupChatLisener(View view) {
      view.setOnClickListener(new OnClickListener() {

         @Override public void onClick(View arg0) {
            jumpToGroupChat();
            Utils.showMessage("请到\"项目管理\"中操作");
         }
      });
   }

   // 上传的图片地址
   private ArrayList<UpLoadFileEntity> pic_urls = new ArrayList<UpLoadFileEntity>();
   private JingJiaAddAdapter addAdapter;
   private GridView upload_pic_grid;
   String[] urls = null;

   // 附件相关
   private ArrayList<UpLoadFileEntity> file_urls = new ArrayList<UpLoadFileEntity>();
   private FileAddAdapter fileAddAdapter;
   private GridView file_grid;
   String[] f_urls = null;

   private void getDetailTask(String id) {
      Trace.d("FAXIANGMUENTITY_id:" + id);
      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/project/findone?project_id=" + id,
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<XiangMuEntity>() {

         @Override public void onCallback(XiangMuEntity callback) {

            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {

               xiangMuEntity = callback;
               // 获取与项目关联的报名列表
               getBaoMingTask(xiangMuEntity.getId());

               // 报名按钮
               Button button_baoming =
                   (Button) FaXiangMuDetailActivity.this.findViewById(R.id.button_baoming);
               button_baoming.setOnClickListener(FaXiangMuDetailActivity.this);

               // 设置头像默认图标
               DisplayImageOptions options =
                   new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
                       .showImageForEmptyUri(R.drawable.face_male)
                       .showImageOnFail(R.drawable.face_male)
                       .cacheInMemory()
                       .cacheOnDisc()
                       .build();

               ImageView left_image =
                   (ImageView) FaXiangMuDetailActivity.this.findViewById(R.id.left_image);
               ImageLoader.getInstance().displayImage(callback.getFace(), left_image, options);

               left_image.setOnClickListener(new OnClickListener() {

                  @Override public void onClick(View arg0) {
                     Intent profile_intent =
                         new Intent(FaXiangMuDetailActivity.this, ProfileActivity.class);
                     profile_intent.putExtra(ProfileActivity.PROFILE_UID, xiangMuEntity.getUid());
                     startActivity(profile_intent);
                  }
               });

               TextView name_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.name_tv);
               name_tv.setText(callback.getNickname());

               TextView position_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.position_tv);
               position_tv.setText(callback.getAdd_time());

               TextView title_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.title_tv);
               title_tv.setText(callback.getTitle());

               TextView type_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.type_tv);
               type_tv.setText(callback.getCategory_name());

               TextView location_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.location_tv);
               location_tv.setText(callback.getAddr());

               TextView job_tv = (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.job_tv);
               job_tv.setText(callback.getJob());

               TextView skills_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.skills_tv);
               skills_tv.setText(callback.getSkills());

               TextView money_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.money_tv);
               money_tv.setText(callback.getBudget() + "元");

               TextView memo_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.memo_tv);
               memo_tv.setText(callback.getMemo());

               TextView duaration_time_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.duaration_time_tv);
               duaration_time_tv.setText(callback.getDuration() + "个月");

               TextView chat_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.chat_tv);
               chat_tv.setText(callback.getComm_total());

               TextView fav_tv = (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.fav_tv);
               fav_tv.setText(callback.getAtten_total());

               ImageView iv_fav =
                   (ImageView) FaXiangMuDetailActivity.this.findViewById(R.id.fav_iv);
               // 0 -1否 1是
               if (callback.getProject_follower().equals("0") || callback.getProject_follower()
                   .equals("-1")) {
                  iv_fav.setImageResource(R.drawable.home_list_item_fav);
                  fav_layout.setOnClickListener(new View.OnClickListener() {

                     @Override public void onClick(View arg0) {
                        // 收藏
                        doFav(xiangMuEntity.getId());
                     }
                  });
               } else {
                  iv_fav.setImageResource(R.drawable.home_list_item_fav_press);
                  fav_layout.setOnClickListener(new View.OnClickListener() {

                     @Override public void onClick(View arg0) {
                        // 删除收藏
                        doDelFav(xiangMuEntity.getId());
                     }
                  });
               }

               TextView apply_tv =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.apply_tv);
               apply_tv.setText(callback.getApply_total());
               // 处理图片
               urls = callback.getPic().split(",");
               pic_urls.clear();
               for (String url : urls) {
                  if (url != null && !url.equals("") && !url.equals(",")) {
                     UpLoadFileEntity loadFileEntity = new UpLoadFileEntity();
                     loadFileEntity.setUrl(url);
                     pic_urls.add(loadFileEntity);
                  }
               }
               addAdapter.notifyDataSetChanged();

               voiceLocalPath = callback.getVoice();

               // 处理附件
               f_urls = callback.getDocument().split(",");
               file_urls.clear();
               for (String url : f_urls) {
                  if (url != null && !url.equals("") && !url.equals(",")) {
                     UpLoadFileEntity loadFileEntity = new UpLoadFileEntity();
                     loadFileEntity.setUrl(url);
                     file_urls.add(loadFileEntity);
                  }
               }
               fileAddAdapter.notifyDataSetChanged();

               if (voiceLocalPath == null || voiceLocalPath.equals("")) {
                  publishThemeVoiceBtn.setVisibility(View.GONE);
               } else {
                  publishThemeVoiceBtn.setVisibility(View.VISIBLE);
               }

               // //////////////////////////////////////////////////////////////////////////////////////////////////////
               // 报名状态处理
               // status 默认-1 状态：0:报名；1:拒绝 2:考虑过了 3:自己同意 4:发项目者接受 5:申请完成
               // 6：完成 7：已退出项目
               // project_status;
               // project_dev_money;
               if (callback.getProject_status().equals(STATUS_BAOMING_NO_01)) {
                  button_baoming.setOnClickListener(new OnClickListener() {

                     @Override public void onClick(View arg0) {
                        showBaoMing();
                     }
                  });
               } else if (callback.getProject_status().equals(STATUS_BAOMING_ING_0)) {
                  button_baoming.setText("报名中...");
               } else if (callback.getProject_status().equals(STATUS_BAOMING_JUJUE_1)) {
                  button_baoming.setText("你被拒绝了");
               } else if (callback.getProject_status().equals(STATUS_BAOMING_KAOLU_2)) {
                  button_baoming.setText("考虑中...");
               } else if (callback.getProject_status().equals(STATUS_BAOMING_TONGYI_3)) {
                  button_baoming.setText("同意(" + callback.getProject_dev_money() + "元)");
                  button_baoming.setClickable(true);
                  button_baoming.setOnClickListener(new OnClickListener() {

                     @Override public void onClick(View view) {

                        CustomDialog userBlackDialog = null;
                        if (userBlackDialog == null) {
                           userBlackDialog = new CustomDialog(FaXiangMuDetailActivity.this,
                               new CustomDialogListener() {
                                  @Override public void onDialogClosed(int closeType) {
                                     switch (closeType) {
                                        case CustomDialogListener.BUTTON_POSITIVE:
                                           // 同意资金
                                           doAccepteMoney(xiangMuEntity.getId());
                                           break;
                                        case CustomDialogListener.BUTTON_NEUTRAL:
                                           // 拒绝资金
                                           doJuJueMoney(xiangMuEntity.getId());
                                           break;
                                     }
                                  }
                               });

                           userBlackDialog.setCustomMessage("确定要接受此项目资金吗?");
                           userBlackDialog.setCancelable(true);
                           userBlackDialog.setType(CustomDialog.DOUBLE_BTN);
                           userBlackDialog.setButtonText("同意", "拒绝");
                        }

                        if (null != userBlackDialog) {
                           userBlackDialog.show();
                        }
                     }
                  });
               } else if (callback.getProject_status().equals(STATUS_BAOMING_JIESHOU_4)) {
                  button_baoming.setText("已加入项目组");
                  addJumpGroupChatLisener(button_baoming);
               } else if (callback.getProject_status().equals(STATUS_SHENQING_WANCHENG_5)) {
                  button_baoming.setText("申请项目完成");
                  addJumpGroupChatLisener(button_baoming);
               } else if (callback.getProject_status().equals(STATUS_WANCHENG_6)) {
                  button_baoming.setText("项目已完成");
                  addJumpGroupChatLisener(button_baoming);
               } else if (callback.getProject_status().equals(STATUS_TUICHU_TONGYIN_OR_ZHUDONG_7)) {
                  button_baoming.setText("已退出项目组");
                  button_baoming.setOnClickListener(new OnClickListener() {

                     @Override public void onClick(View arg0) {
                        Utils.showMessage("不能再操作");
                     }
                  });
               } else if (callback.getProject_status().equals(STATUS_TUICHU_BEI_T_8)) {
                  button_baoming.setText("待确认-被T出项目组");
                  addJumpGroupChatLisener(button_baoming);
               } else if (callback.getProject_status().equals(STATUS_BAOMING_JUJUE_JIAGE_9)) {
                  button_baoming.setText("已拒绝(" + callback.getProject_dev_money() + "元)");
                  button_baoming.setClickable(true);
               }

               TextView tv_money_pay =
                   (TextView) FaXiangMuDetailActivity.this.findViewById(R.id.tv_money_pay);
               // 显示提示信息
               if (callback.getProject_status().equals(STATUS_BAOMING_TONGYI_3)) {
                  tv_money_pay.setVisibility(View.VISIBLE);
               } else {
                  tv_money_pay.setVisibility(View.GONE);
               }

               if (AULiveApplication.getUserInfo() != null
                   && AULiveApplication.getUserInfo().getUid() != null
                   && AULiveApplication.getUserInfo().getUid().equals(xiangMuEntity.getUid())) {
                  // Trace.d(xiangMuEntity.getUid()+":"+AULiveApplication.getUserInfo().getUid());
                  // 如果是发项目者
                  tv_edit.setVisibility(View.VISIBLE);
                  button_baoming.setVisibility(View.VISIBLE);
                  button_baoming.setText("群聊");
                  button_baoming.setOnClickListener(new OnClickListener() {

                     @Override public void onClick(View view) {
                        if (BtnClickUtils.isFastDoubleClick()) {
                           return;
                        }
                        createGroup();
                     }
                  });
               } else {
                  // 报名与未报名者
                  tv_edit.setVisibility(View.INVISIBLE);
                  button_baoming.setVisibility(View.VISIBLE);
               }
            } else {

               Utils.showMessage(callback.getMsg());
            }
            stopProgressDialog();
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
            stopProgressDialog();
         }
      }.setReturnType(XiangMuEntity.class));
      request.execute();
   }

   private void getBaoMingTask(String id) {

      if (AULiveApplication.getUserInfo() != null
          && AULiveApplication.getUserInfo().getUid() != null
          && AULiveApplication.getUserInfo().getUid().equals(xiangMuEntity.getUid())) {

         bid_main_layout.setVisibility(View.VISIBLE);
         bid_layout.setVisibility(View.VISIBLE);
      } else {
         bid_main_layout.setVisibility(View.GONE);
         bid_layout.setVisibility(View.GONE);
         return;
      }

      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/apply/lists?project_id=" + id,
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<XiangMuListEntity>() {

         @Override public void onCallback(XiangMuListEntity callback) {

            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               ArrayList<XiangMuEntity> entities = callback.getList();

               bid_layout.removeAllViews();
               for (final XiangMuEntity xiangMuEntity : entities) {

                  View baoming_item = LayoutInflater.from(FaXiangMuDetailActivity.this)
                      .inflate(R.layout.faxiangmu_baoming_item, null);

                  ImageView iv_face = (ImageView) baoming_item.findViewById(R.id.iv_face);
                  iv_face.setOnClickListener(new OnClickListener() {

                     @Override public void onClick(View arg0) {
                        Intent profile_intent =
                            new Intent(FaXiangMuDetailActivity.this, ProfileActivity.class);
                        profile_intent.putExtra(ProfileActivity.PROFILE_UID,
                            xiangMuEntity.getUid());
                        startActivity(profile_intent);
                     }
                  });

                  ImageLoader.getInstance()
                      .displayImage(xiangMuEntity.getFace(), iv_face,
                          AULiveApplication.getGlobalImgOptions());

                  TextView tv_nickname = (TextView) baoming_item.findViewById(R.id.tv_nickname);
                  tv_nickname.setText(xiangMuEntity.getNickname());

                  TextView tv_time = (TextView) baoming_item.findViewById(R.id.tv_time);
                  tv_time.setText(xiangMuEntity.getAdd_time());

                  Button sure_baoming = (Button) baoming_item.findViewById(R.id.sure_baoming);

                  // status 默认-1 状态：0:报名；1:拒绝 2:考虑过了 3:自己同意 4:发项目者接受
                  // 5:申请完成 6：完成 7：已退出项目

                  if (xiangMuEntity.getStatus().equals(STATUS_BAOMING_JUJUE_1)) {
                     sure_baoming.setText("拒绝");
                  }
                  if (xiangMuEntity.getStatus().equals(STATUS_BAOMING_KAOLU_2)) {
                     sure_baoming.setText("考虑中");
                  }

                  if (xiangMuEntity.getStatus().equals(STATUS_BAOMING_TONGYI_3)) {
                     sure_baoming.setText("等待对方同意");
                  }

                  if (xiangMuEntity.getStatus().equals(STATUS_BAOMING_JIESHOU_4)) {
                     sure_baoming.setText("已加入项目组");
                     sure_baoming.setClickable(false);
                  }
                  if (xiangMuEntity.getStatus().equals(STATUS_SHENQING_WANCHENG_5)) {
                     sure_baoming.setText("申请项目完成");
                     sure_baoming.setClickable(false);
                  }
                  if (xiangMuEntity.getStatus().equals(STATUS_WANCHENG_6)) {
                     sure_baoming.setText("项目已完成");
                     sure_baoming.setClickable(false);
                  }
                  if (xiangMuEntity.getStatus().equals(STATUS_TUICHU_TONGYIN_OR_ZHUDONG_7)) {
                     sure_baoming.setText("已退出项目组");
                     sure_baoming.setClickable(false);
                  }
                  if (xiangMuEntity.getStatus().equals(STATUS_BAOMING_JUJUE_JIAGE_9)) {
                     sure_baoming.setText("已拒绝价格");
                     sure_baoming.setClickable(true);
                  }

                  sure_baoming.setOnClickListener(new OnClickListener() {

                     @Override public void onClick(View view) {

                        // 当已经做资金托管后不再改变
                        if (xiangMuEntity.getStatus().equals(STATUS_BAOMING_TONGYI_3)) {
                           Utils.showMessage("资金已托管,无法修改");
                        } else if (xiangMuEntity.getStatus().equals(STATUS_BAOMING_JIESHOU_4)) {
                           Utils.showMessage("项目进行中");
                           dojumpXiangMuGuanli();
                        } else if (xiangMuEntity.getStatus().equals(STATUS_SHENQING_WANCHENG_5)) {
                           Utils.showMessage("申请完成");
                           dojumpXiangMuGuanli();
                        } else if (xiangMuEntity.getStatus().equals(STATUS_WANCHENG_6)) {
                           Utils.showMessage("项目已经完成");
                           dojumpXiangMuGuanli();
                        } else if (xiangMuEntity.getStatus()
                            .equals(STATUS_TUICHU_TONGYIN_OR_ZHUDONG_7)) {
                           Utils.showMessage("已退出项目组,无法修改");
                        } else {

                           showBaoMingDialog(FaXiangMuDetailActivity.this.xiangMuEntity.getId(),
                               xiangMuEntity.getUid(), xiangMuEntity.getNickname());
                        }
                     }
                  });

                  bid_layout.addView(baoming_item);
               }
            } else {

               Utils.showMessage(callback.getMsg());
            }
            stopProgressDialog();
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
            stopProgressDialog();
         }
      }.setReturnType(XiangMuListEntity.class));
      request.execute();
   }

   // 选择报名的选择
   private ProjectTypeEntity current_type = null;

   private static String DIALIG_INDEX_KEY_1 = "1";
   private static String DIALIG_INDEX_KEY_2 = "2";
   private static String DIALIG_INDEX_KEY_3 = "3";
   private static String DIALIG_INDEX_KEY_4 = "4";

   private void showBaoMingDialog(final String project_id, final String uid,
       final String nickname) {
      ArrayList<ProjectTypeEntity> types = new ArrayList<ProjectTypeEntity>();
      ProjectTypeEntity entity1 = new ProjectTypeEntity();
      entity1.setId(DIALIG_INDEX_KEY_1);
      entity1.setName("接受");
      types.add(entity1);

      ProjectTypeEntity entity2 = new ProjectTypeEntity();
      entity2.setId(DIALIG_INDEX_KEY_2);
      entity2.setName("考虑");
      types.add(entity2);

      ProjectTypeEntity entity3 = new ProjectTypeEntity();
      entity3.setId(DIALIG_INDEX_KEY_3);
      entity3.setName("拒绝");
      types.add(entity3);

      ProjectTypeEntity entity4 = new ProjectTypeEntity();
      entity4.setId(DIALIG_INDEX_KEY_4);
      entity4.setName("取消");
      types.add(entity4);

      customDialog = new SubWayLineDialog(FaXiangMuDetailActivity.this, new SubWayDialogListener() {

         @Override public void onItemClick(ProjectTypeEntity t_type) {
            current_type = t_type;
            if (t_type.getId().equals(DIALIG_INDEX_KEY_1)) {
               Intent acceppted_intent =
                   new Intent(FaXiangMuDetailActivity.this, FaXiangMuDetailPayMoneyActivity.class);
               acceppted_intent.putExtra(FaXiangMuDetailPayMoneyActivity.project_id_key,
                   project_id);
               acceppted_intent.putExtra(FaXiangMuDetailPayMoneyActivity.uid_key, uid);
               acceppted_intent.putExtra(FaXiangMuDetailPayMoneyActivity.nickname_key, nickname);
               startActivity(acceppted_intent);
            }
            if (t_type.getId().equals(DIALIG_INDEX_KEY_2)) {
               doKaoLu(project_id, uid);
            }
            if (t_type.getId().equals(DIALIG_INDEX_KEY_3)) {
               doJuJue(project_id, uid);
            }
            if (t_type.getId().equals(DIALIG_INDEX_KEY_4)) {

            }
            customDialog.dismiss();
         }
      }, types);

      customDialog.setCancelable(true);
      // customDialog.getWindow().setType(
      // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
      customDialog.show();
   }

   // 考虑
   private void doKaoLu(String project_id, String uid) {
      // startProgressDialog();
      RequestInformation request = new RequestInformation(
          UrlHelper.URL_HEAD + "/apply/considered?project_id=" + project_id + "&uid=" + uid,
          RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("\"考虑\"成功");
               // 刷新
               getDetailTask(xiangMuEntity.getId());
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   // 拒绝报名
   private void doJuJue(String project_id, String uid) {
      // startProgressDialog();
      RequestInformation request = new RequestInformation(
          UrlHelper.URL_HEAD + "/apply/refuse?project_id=" + project_id + "&uid=" + uid,
          RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("\"拒绝\"成功");
               // 刷新
               getDetailTask(xiangMuEntity.getId());
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   // 同意发项目者提出的资金，加入项目组
   private void doAccepteMoney(String project_id) {
      // startProgressDialog();
      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/apply/accept?project_id=" + project_id,
              RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("接受资金,成功加入项目组");
               // 刷新
               getDetailTask(xiangMuEntity.getId());
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   // 拒绝发项目者提出的资金
   private void doJuJueMoney(String project_id) {
      // startProgressDialog();
      RequestInformation request = new RequestInformation(
          UrlHelper.URL_HEAD + "/apply/programmer_refuse?project_id=" + project_id,
          RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("\"拒绝\"成功");
               // 刷新
               getDetailTask(xiangMuEntity.getId());
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   public final static String REPLY_ENTITY_KEY = "Reply_entity";

   @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

   public static final int modify_key = 1;
   public static final int delete_key = 2;
   private CustomDialog userBlackDialog;

   @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == modify_key) {
         Intent modify_intent = new Intent(this, FaXiangMuStep1Activity.class);
         modify_intent.putExtra(FaXiangMuStep1Activity.IS_MODIFY_KEY, xiangMuEntity);
         modify_intent.putExtra(FaXiangMuStep1Activity.IS_IGNORD, true);
         startActivity(modify_intent);
      }
      if (resultCode == delete_key) {

         if (userBlackDialog == null) {
            userBlackDialog = new CustomDialog(this, new CustomDialogListener() {
               @Override public void onDialogClosed(int closeType) {
                  switch (closeType) {
                     case CustomDialogListener.BUTTON_POSITIVE:
                        doDelet(xiangMuEntity.getId());
                        break;
                  }
               }
            });

            userBlackDialog.setCustomMessage("确定要删除晒物吗?");
            userBlackDialog.setCancelable(true);
            userBlackDialog.setType(CustomDialog.DOUBLE_BTN);
         }

         if (null != userBlackDialog) {
            userBlackDialog.show();
         }
      }
   }

   private void doDelet(String project_id) {
      // startProgressDialog();
      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/project/my_del?project_id=" + project_id,
              RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("\"删除\"成功");
               FaXiangMuDetailActivity.this.finish();
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void showBaoMing() {
      if (userBlackDialog == null) {
         userBlackDialog = new CustomDialog(this, new CustomDialogListener() {
            @Override public void onDialogClosed(int closeType) {
               switch (closeType) {
                  case CustomDialogListener.BUTTON_POSITIVE:
                     doBaoMing(xiangMuEntity.getId());
                     break;
               }
            }
         });

         userBlackDialog.setCustomMessage("确定要报名吗?");
         userBlackDialog.setCancelable(true);
         userBlackDialog.setType(CustomDialog.DOUBLE_BTN);
      }

      if (null != userBlackDialog) {
         userBlackDialog.show();
      }
   }

   private void doBaoMing(final String project_id) {

      Trace.d("project_id:" + project_id);
      // startProgressDialog();
      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/apply/bid?project_id=" + project_id,
              RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("project_id", project_id);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("\"报名\"成功");
               getDetailTask(xiangMu_id);
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void doFav(final String project_id) {

      // startProgressDialog();
      RequestInformation request = new RequestInformation(
          UrlHelper.URL_HEAD + "/apply/follower_add?project_id=" + project_id,
          RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("project_id", project_id);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("\"收藏\"成功");
               // 刷新
               getDetailTask(xiangMuEntity.getId());
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   // 删除收藏
   private void doDelFav(final String project_id) {

      // startProgressDialog();
      RequestInformation request = new RequestInformation(
          UrlHelper.URL_HEAD + "/apply/follower_del?project_id=" + project_id,
          RequestInformation.REQUEST_METHOD_GET);
      request.addPostParams("project_id", project_id);
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showMessage("取消\"收藏\"成功");
               // 刷新
               getDetailTask(xiangMuEntity.getId());
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {

            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   @Override public void onRefresh(final int mode) {

      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      RequestInformation request = null;

      String url =
          UrlHelper.URL_HEAD + "/comment/lists?project_id=" + xiangMu_id + "&page=" + currPage;
      Trace.d(url);
      request = new RequestInformation(url, RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<CommentListEntity>() {

         @Override public void onCallback(CommentListEntity callback) {
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

               // if (entities.size() > 0) {
               // home_listview.setSelection(0);
               // }
               home_listview.onRefreshComplete(mode, true);
               getDetailTask(xiangMu_id);
            } else {
               currPage--;
               // 因为可能网络恢复，success改为true
               home_listview.onRefreshComplete(mode, true);
            }
            stopProgressDialog();
         }

         @Override public void onFailure(AppException e) {
            currPage--;
            entities.clear();
            // 因为可能网络恢复，success改为true
            home_listview.onRefreshComplete(mode, true);
            stopProgressDialog();
         }
      }.setReturnType(CommentListEntity.class));

      request.execute();
   }

   // 下载与打开附件
   private void OpenFile(String voiceUrl) {
      if (voiceUrl == null || voiceUrl.equals("")) {
         Trace.d("voiceUrl==null");
         return;
      }
      if (FileUtil.isFileExist(FileUtil.DOWNLOAD_PATH + getFileTag(voiceUrl))) {
         Trace.d("本地附件存在");

         Intent intent = OpenFileUtils.openFile(FileUtil.DOWNLOAD_PATH + getFileTag(voiceUrl));
         startActivity(intent);
      } else {
         downFile2(voiceUrl);
      }
   }

   // 下载附件
   private void downFile2(final String voiceUrl) {
      FileUtil.createDir(FileUtil.DOWNLOAD_PATH);
      RequestInformation request =
          new RequestInformation(voiceUrl, RequestInformation.REQUEST_METHOD_GET);
      request.setProgressChangeListener(new IProgressListener() {

         @Override public void progressChanged(int status, int progress, String operationName) {
            if (progress == 100) {
               Trace.d("fileName:" + getFileTag(voiceUrl));

               String localVoicePath = FileUtil.DOWNLOAD_PATH + getFileTag(voiceUrl);
               // 第三方打开文件
               Intent intent = OpenFileUtils.openFile(localVoicePath);
               startActivity(intent);
            }
         }
      });
      request.setCallback(new PathCallback() {

         @Override public void onFailure(AppException e) {

         }

         @Override public void onCallback(String callback) {
         }
      }.setFilePath(FileUtil.DOWNLOAD_PATH + getFileTag(voiceUrl)));
      request.execute();
   }

   // 获取附件tag
   private String getFileTag(String fileUrl) {
      if (fileUrl == null) {
         return "";
      }

      return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
   }

   private ShareHelper shareDialog;
   private LinearLayout bid_main_layout;

   private TextView baoming_tv;

   private LinearLayout bid_layout;

   private SubWayLineDialog customDialog;

   private LinearLayout fav_layout;

   // 分享相关
   private void doShare() {

      String target_url = null;
      try {
         target_url = "http://wx.qxj.me/p/project.html?pid=" + xiangMuEntity.getId();
      } catch (Exception e1) {
         e1.printStackTrace();
      }
      if (shareDialog == null) {
         shareDialog = new ShareHelper(this);
      }

      shareDialog.setShareUrl(target_url);
      shareDialog.setShareTitle(Utils.trans(R.string.app_name));
      shareDialog.setShareContent(xiangMuEntity.getTitle(), xiangMuEntity.getFace());

      if (!shareDialog.isShowing()) {
         shareDialog.show();
      }
   }

   // 创建群聊
   private void createGroup() {

      // startProgressDialog();
      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/group?id=" + xiangMuEntity.getId(),
              RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<CeateGroupEntity>() {

         @Override public void onCallback(CeateGroupEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               //doGroupChat(callback.getFace(), callback.getName());
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(CeateGroupEntity.class));
      request.execute();
   }

   private void dojumpXiangMuGuanli() {
      String project_id_key = xiangMu_id;
      Intent guanli_Intent = new Intent(FaXiangMuDetailActivity.this, XiangMuGuanLiActivity.class);
      guanli_Intent.putExtra(XiangMuGuanLiActivity.project_id_key, project_id_key);
      startActivity(guanli_Intent);
   }
}