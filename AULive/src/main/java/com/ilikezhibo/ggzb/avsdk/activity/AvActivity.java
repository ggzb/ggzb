package com.ilikezhibo.ggzb.avsdk.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.ilikezhibo.ggzb.avsdk.activity.entity.DanmuEntity;
import com.ilikezhibo.ggzb.avsdk.exchange.ExchangeRecordEntity;
import com.ilikezhibo.ggzb.avsdk.home.OperateRecordEntity;
import com.ilikezhibo.ggzb.entity.ChangeLiveType;
import com.ilikezhibo.ggzb.userinfo.MyUserInfoFragment;
import com.ilikezhibo.ggzb.views.UserInfoWebViewActivity;
import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.ilikezhibo.ggzb.views.WebViewFragment;
import com.ilikezhibo.ggzb.views.VariousDialog;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.FastBlurUtil;
import com.jack.utils.JsonParser;
import com.jack.utils.PixelDpHelper;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.util.device.DeviceInfo;
import com.ksyun.media.streamer.util.device.DeviceInfoTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.ChatEntity;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.avsdk.EnterRoomDrive.UserDriveUtil;
import com.ilikezhibo.ggzb.avsdk.MemberInfo;
import com.ilikezhibo.ggzb.avsdk.RoomUserListAdapter;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.CustomizeChatRoomMessage;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.CustomizeChatRoomMessageList;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.CustomizeMsgQueueMessage;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.CustomizeRCTMessage;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.GiftChatRoomMessage;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.SystemChatRoomMessage;
import com.ilikezhibo.ggzb.avsdk.activity.entity.MemberEntity;
import com.ilikezhibo.ggzb.avsdk.activity.entity.MemberListEntity;
import com.ilikezhibo.ggzb.avsdk.activity.entity.RoomNumEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.AttentEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.ChatMsgEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.EnterEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.GagUserEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.LeaveEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.LikeUserEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.RemoveGagEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.RemoveManagerEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SetManagerEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SystemMsgEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.UserBarrageEntity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.UserUpGradeEntity;
import com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper.FlipRoomHelper;
import com.ilikezhibo.ggzb.avsdk.activity.xiaolaba.XiaoLaBaEntity;
import com.ilikezhibo.ggzb.avsdk.activity.xiaolaba.XiaoLaBaHelper;
import com.ilikezhibo.ggzb.avsdk.badwordfilter.FilteredResult;
import com.ilikezhibo.ggzb.avsdk.badwordfilter.WordFilterUtil;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatListActivity;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatHelper;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatListHelper;
import com.ilikezhibo.ggzb.avsdk.gift.ContinueGiftView;
import com.ilikezhibo.ggzb.avsdk.gift.GiftPagerUtil;
import com.ilikezhibo.ggzb.avsdk.gift.RedPacketesUtil;
import com.ilikezhibo.ggzb.avsdk.gift.customized.OperateHelper;
import com.ilikezhibo.ggzb.avsdk.gift.entity.GiftEntity;
import com.ilikezhibo.ggzb.avsdk.gift.luxurygift.LuxuryGiftUtil;
import com.ilikezhibo.ggzb.avsdk.home.EnterRoomEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.ManagerListFragment;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoHelper;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprankfragment.TopRankMainFragment;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.userinfo.buydiamond.BuyDiamondActivity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import dammu.danmu.DanmuControl;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import master.flame.danmaku.controller.IDanmakuView;
import org.json.JSONArray;
import org.json.JSONObject;
import popwindow.PopupWindowUtil;
import tinker.android.util.TinkerManager;
import tyrantgit.widget.HeartLayout;

/**
 * Created by big on 4/7/16.
 */
public class AvActivity extends BaseFragmentActivity
    implements View.OnClickListener, IWeiboHandler.Response, OnTouchListener {

   private boolean video_is_closed;
   //accessKey,secretKey相同
   private String accessKey = "DfC9k+Ohbqm4kbWRjGPhCZtkNjtonKaH841r9nfw";
   private String secretKey = "DfC9k+Ohbqm4kbWRjGPhCZtkNjtonKaH841r9nfw";
   private String appId = "2BlIyk3cMuw7X6Td5lXt";

   //是主播还是看客
   public boolean is_creater = false;
   public final static String IS_PAY = "is_pay";
   public final static int MESSAGE_PAY = 101;
   public final static int NO_MONEY = 201;
   public final static int OVER_TIME = 202;

   public final static String GET_UID_KEY = "GET_UID_KEY";
   public final static String GET_GRADE_KEY = "GET_GRADE_KEY";
   public final static String IS_CREATER_KEY = "IS_CREATER_KEY";
   public final static String GET_VDOID_KEY = "GET_VDOID_KEY";
   public final static String EXTRA_RECORD_TITLE_KEY = "EXTRA_RECORD_TITLE_KEY";
   //public static String root_url = "rtmp://live.uplive.ks-cdn.com/live/";
   public static String live_root_url = "rtmp://live." + UrlHelper.URL_domain + "/live/";
   public static String play_root_url = "rtmp://play." + UrlHelper.URL_domain + "/live/";

   public static String live_root_url_qiniu = "rtmp://yf-push.khlexpo.com/live/";
   public static String play_root_url_qiniu = "rtmp://yf-live.khlexpo.com/live/";

   public String live_url;

   public String play_url;
   public String mHostGrade;
   //金币数
   private TextView gold_count_tv;
   //loading 背景
   public ImageView bg_imageView;
   private Bitmap blur_bg_bitmap;
   private WrapContentLinearLayoutManager recyclerView_layoutManager;
   private int first_get_online_num;
   private ArrayList<View> viewPager_pages;
   private ArrayList<Fragment> pager_fragments;
   //private int lastVisibleItem;

   private ViewFlipper viewFlipper;
   private float touchDownX;  // 手指按下的X坐标
   private float touchUpX;  //手指松开的X坐标
   private float DownX;  // 手指按下的y坐标
   private float DownY;  // 手指按下的y坐标

   public long host_duration_time = 0;

   private Button bt_attend;
   private Timer atten_timer;

   private FlipRoomHelper flipRoomHelper;
   public XiaoLaBaHelper xiaoLaBaHelper;
   private GameOverFragment gameOverFragment;
   private View endView;
   private TopRankMainFragment rankMainFragment;
   private int mWidth;
   private int mHeight;
   private View mPop_view;
   private PopupWindow mWindow;
   private List<OperateRecordEntity.DataBean> current_list;
   private MyAdapter mAdapter;
   private ListView mListView;
   private int currentPage = 1;
   private TextView mCurrent_page;
   private TextView mView_location;
   private Button mOperateRecord;
   private ImageView mIv_type;
   private boolean mIsPay;
   private VariousDialog mVariousDialog;
   private GLSurfaceView mGLSurfaceView;
   protected String mBgImagePath = "assets://bg.jpg";

   public View getGiveGiftView() {
      return giveGiftView;
   }

   private View giveGiftView;

   @Override public void onCreate(Bundle savedInstanceState) {
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      super.onCreate(savedInstanceState);
      Trace.d("AvActivity onCreate");
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
          | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
      setContentView(R.layout.av_activity);

      EventBus.getDefault().register(this);

      //rongcloud 不是在连接以及连接中,做重连处理
      RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus =
          RongIM.getInstance().getCurrentConnectionStatus();
      if (connectionStatus != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED
          && connectionStatus
          != RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) {
         String im_token = AULiveApplication.getUserInfo().getIm_token();
         if (im_token != null) {
            Trace.d("不是在连接以及连接中,做重连处理");
            AULiveHomeActivity.connect(im_token, AvActivity.this);
         }
      }

      initOnCreate();

      initViews();

      //连麦相关

      //切屏幕相关,付费   暂时禁止
      //flipRoomHelper = new FlipRoomHelper(AvActivity.this);

      xiaoLaBaHelper = new XiaoLaBaHelper(AvActivity.this);

      //初始化弹幕
      initDanmu();

      //剩下的view初始化
      initLastViews();

      //私聊相关
      initRoomPrivateChats();

      //提示自己上线了
      onMemberEnter();

      //获取成员列表
      getMemberInfo();

      initPopupWindow();
   }

   private void initPopupWindow() {
      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);
      mWidth = metrics.widthPixels;
      mHeight = metrics.heightPixels;

      mPop_view = View.inflate(this, R.layout.popupwindow_operate, null);
      mWindow = new PopupWindow(mPop_view, ViewGroup.LayoutParams.MATCH_PARENT, mHeight/2, true);
      mWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.empty_white));

      mPop_view.findViewById(R.id.last_page).setOnClickListener(this);
      mPop_view.findViewById(R.id.next_page).setOnClickListener(this);
      TextView title = (TextView) mPop_view.findViewById(R.id.title);
      title.setText("操作记录");
      mCurrent_page = (TextView) mPop_view.findViewById(R.id.current_page);
//      if (mAdapter == null) {
//         mAdapter = new MyAdapter();
//      }
      mListView = (ListView) mPop_view.findViewById(R.id.list_record);
//      mListView.setAdapter(mAdapter);
   }


   class MyAdapter extends BaseAdapter {
      @Override
      public int getCount() {
         return current_list.size();
      }

      @Override
      public Object getItem(int position) {
         return null;
      }

      @Override
      public long getItemId(int position) {
         return 0;
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
         ExchangeRecordEntity.DataBean data;
         long operate_time;
         String time_string;
         ViewHolder viewHolder;
         if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_operate_listview, null);
            viewHolder.uid = (TextView) convertView.findViewById(R.id.live_id);
            viewHolder.operater = (TextView) convertView.findViewById(R.id.operater);
            viewHolder.content = (TextView) convertView.findViewById(R.id.operate_content);
            viewHolder.time = (TextView) convertView.findViewById(R.id.operate_time);
            convertView.setTag(viewHolder);
         } else {
            viewHolder = (ViewHolder) convertView.getTag();
         }
         operate_time = Integer.parseInt(current_list.get(position).getCreated());
         time_string = com.ilikezhibo.ggzb.tool.Utils.timeExchange(operate_time);
         viewHolder.uid.setText("" + current_list.get(position).getUid());
         viewHolder.operater.setText(current_list.get(position).getOperator());
         viewHolder.content.setText(current_list.get(position).getContent());
         viewHolder.time.setText(time_string);
         return convertView;
      }
      class ViewHolder {
         TextView uid;
         TextView operater;
         TextView content;
         TextView time;
      }
   }

   public void initOnCreate() {
      Trace.d("****>>>>进入房间");
      if (qav_top_bar_new != null && qav_top_bar_new.getVisibility() == View.INVISIBLE) {
         qav_top_bar_new.setVisibility(View.VISIBLE);
      }

      if (qav_bottom_bar != null && qav_bottom_bar.getVisibility() == View.INVISIBLE) {
         qav_bottom_bar.setVisibility(View.VISIBLE);
      }
      // ***切换到下一个房间小喇叭状态重置为false
      if (xiaoLaBaHelper != null) {
         xiaoLaBaHelper.is_showing_enter_room_effects = false;
      }
      AULiveApplication.currLiveUid = getIntent().getStringExtra(GET_UID_KEY);
      mHostGrade = getIntent().getStringExtra(GET_GRADE_KEY);

      if (AULiveApplication.currLiveUid == null) {
         this.finish();
         return;
      }
      is_creater = getIntent().getBooleanExtra(IS_CREATER_KEY, false);

      //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
      //    WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
      //|WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS

      //沉浸试statusbar
      // create our manager instance after the content view is set
      //SystemBarTintManager tintManager = new SystemBarTintManager(this);
      //// enable status bar tint
      //tintManager.setStatusBarTintEnabled(true);
      //// enable navigation bar tint
      //tintManager.setNavigationBarTintEnabled(true);
      //
      //tintManager.setTintColor(Color.parseColor("#000000"));

      //用于录像回放
      vdoid_time = getIntent().getLongExtra(GET_VDOID_KEY, System.currentTimeMillis() / 1000);
      Trace.d("vdoid_time:" + vdoid_time);

      //金山云
//      if (AULiveApplication.getUserInfo().getUpLiveUrl() != null) {
//         //初始化推流与播放流
//         live_url =
//             AULiveApplication.getUserInfo().getUpLiveUrl() + "/" + getIntent().getStringExtra(
//                 GET_UID_KEY) + "?vdoid=" + vdoid_time;
//      } else {
//         //初始化推流与播放流
//         live_url =
//             live_root_url + getIntent().getStringExtra(GET_UID_KEY) + "?vdoid=" + vdoid_time;
//      }
      //拉流
      //换成服务器返回
//      play_url = getIntent().getStringExtra(EXTRA_play_url_KEY);
//
//      if (play_url == null || play_url.equals("")) {
//         Trace.d("if (play_url == null || play_url.equals {");
//         play_url = play_root_url + getIntent().getStringExtra(GET_UID_KEY);
//      }

      //qiniuyun
      if (AULiveApplication.getUserInfo().getUpLiveUrl() != null) {
         //初始化推流与播放流
         live_url =
                 AULiveApplication.getUserInfo().getUpLiveUrl() + "/" + getIntent().getStringExtra(
                         GET_UID_KEY) + "?vdoid=" + vdoid_time;
      } else {
//         //初始化推流与播放流
//         live_url =
//                 live_root_url_qiniu + getIntent().getStringExtra(GET_UID_KEY) + "?vdoid=" + vdoid_time;

         Utils.showMessage("参数错误，请重新开播");
         this.finish();
      }

//         拉流
//         换成服务器返回
      play_url = getIntent().getStringExtra(EXTRA_play_url_KEY);

      String url = getIntent().getStringExtra(EXTRA_play_url_KEY);
      if (!is_creater) {
         if (url != null) {
            if(url.startsWith("rtmp")) {
               List<String> list_url = com.jack.utils.Utils.splitURL(url);
               List<String> list_sign = com.jack.utils.Utils.splitSign(list_url.get(1));
               String sign = list_sign.get(1) + list_sign.get(3) + list_sign.get(2) + list_sign.get(0);
               play_url = list_url.get(0) + "sign=" + sign + "&t" + list_url.get(2);
            }
            else {
               play_url = url;
            }
         } else {
            Utils.showCenterMessage(Utils.trans(R.string.data_error));
         }
      }
      Trace.d("play_url:" + play_url);
      mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
      if (is_creater) {
         //自动开始上传流
         startAuto = true;
         initRecord();
      } else {
         mGLSurfaceView.setVisibility(View.GONE);
         initVideoPlayer();
      }
      //设置在线人数
      first_get_online_num = getIntent().getExtras().getInt(EXTRA_ONLINE_NUM);
      php_control_msg_grade = getIntent().getExtras().getInt(EXTRA_MSG_SEND_GRADE_CONTROL);

      //如果是播放回放,隐藏显示一些组件
      is_record_play = getIntent().getExtras().getBoolean(EXTRA_IS_RECORD, false);
      record_id = getIntent().getExtras().getString(EXTRA_IS_RECORD_ID);
   }

   //initviews后面要重复用的，去处来
   public void initLastViews() {
      Trace.d("****>>>>initLastViews");
      //金币数量
      gold_count_tv = (TextView) findViewById(R.id.txt_gold_count);
      recv_diamond = getIntent().getIntExtra(EXTRA_RECIVE_DIAMOND, 0);
      if (is_creater) {
         gold_count_tv.setText(AULiveApplication.getUserInfo().recv_diamond + "");
      } else {
         gold_count_tv.setText(recv_diamond + "");
      }
      //主播uid,封号切图用
      tv_live_uid = (TextView) findViewById(R.id.tv_live_uid);
      tv_live_uid.setText("主播ID:" + AULiveApplication.currLiveUid);

      //在线人数更新
      txt_usernum.setText("" + first_get_online_num);

      //播放
      if (is_record_play) {
         img_chat.setVisibility(View.GONE);
         mPlayerStartBtn.setVisibility(View.VISIBLE);
         mPlayerPanel.setVisibility(View.VISIBLE);
         mPlayerPosition.setVisibility(View.VISIBLE);
      }

      //监听键盘事件
      doKeyBoardListner();
      //显示enter接口传经来的系统信息
      if (!has_show_sys_msg) {
         has_show_sys_msg = true;
         ArrayList<String> sys_msg =
             (ArrayList<String>) getIntent().getExtras().getSerializable(EXTRA_SYS_MSG);
         if (sys_msg != null && sys_msg.size() > 0) {
            for (String msg : sys_msg) {
               ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
               system_ChatMsgEntity.type = "system";
               system_ChatMsgEntity.nickname = "系统消息";
               system_ChatMsgEntity.chat_msg = msg;
               //添加系统消息提醒
               ChatEntity gif_entity = new ChatEntity();
               gif_entity.setChatMsgEntity(system_ChatMsgEntity);
               gif_entity.setTime(System.currentTimeMillis() / 1000);
               mArrayListChatEntity.add(gif_entity);
               updateChatListView();
            }
         }
      }

      //获取储存在服务器上的，是否被禁言,是管理员
      if (!is_creater) {
         if (1 == getIntent().getIntExtra(EXTRA_IS_GAG, 0)) {
            is_gag = true;
         } else {
            is_gag = false;
         }
         if (1 == getIntent().getIntExtra(EXTRA_IS_MANAGER, 0)) {
            is_manager = true;
         } else {
            is_manager = false;
         }
      }
      //超管相关
      if (1 == getIntent().getIntExtra(EXTRA_IS_SUPER_MANAGER, 0)) {
         is_super_manager = true;
         //超管默认是管理，不能禁言
         is_manager = true;
         is_gag = false;
      } else {
         is_super_manager = false;
      }

      super_manager_ly = (RelativeLayout) findViewById(R.id.super_manager_ly);
      if (is_super_manager) {
         super_manager_ly.setVisibility(View.VISIBLE);

         super_manager_ly.findViewById(R.id.yin_chang_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "关闭直播", 1);
                }
             });

         super_manager_ly.findViewById(R.id.jin_bo_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "禁止直播", 2);
                }
             });

         super_manager_ly.findViewById(R.id.tui_jian_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "推荐直播", 3);
                }
             });
         super_manager_ly.findViewById(R.id.zhi_din_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "置顶直播", 4);
                }
             });

         super_manager_ly.findViewById(R.id.tong_guo_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "审核通过", 6);
                }
             });

         super_manager_ly.findViewById(R.id.ju_jue_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "审核拒接", 5);
                }
             });

         super_manager_ly.findViewById(R.id.chong_zhi_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "重置信息", 9);
                }
             });
         super_manager_ly.findViewById(R.id.jing_gao_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "警告主播", 7);
                }
             });
         super_manager_ly.findViewById(R.id.jing_gao_jiechu_bt)
             .setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                   showSuperMangerDialog(AULiveApplication.currLiveUid, "警告主播", 8);
                }
             });

         super_manager_ly.findViewById(R.id.operate_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getOperateData();
//               mWindow.showAtLocation(mView_location, Gravity.RIGHT, 0, 0);
               mWindow.showAsDropDown(mOperateRecord);
            }
         });


      } else {
         super_manager_ly.setVisibility(View.GONE);
      }

      //头像上的关注按钮
      boolean has_follow = false;
      if (MainActivity.atten_uids.contains(AULiveApplication.currLiveUid)) {
         has_follow = true;
      }
      bt_attend = (Button) findViewById(R.id.bt_attend);
      if (has_follow) {
         bt_attend.setVisibility(View.GONE);
      } else {
         bt_attend.setVisibility(View.VISIBLE);
      }

      //主播自己不显示
      if (AULiveApplication.currLiveUid.equals(AULiveApplication.getUserInfo().getUid())) {
         bt_attend.setVisibility(View.GONE);
      }

      bt_attend.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View v) {
            doAttend(AULiveApplication.currLiveUid);
         }
      });

      atten_timer = new Timer();
      atten_timer.schedule(new TimerTask() {
         @Override public void run() {
            bt_attend.post(new Runnable() {
               @Override public void run() {
                  bt_attend.setVisibility(View.GONE);
               }
            });
            atten_timer = null;
         }
      }, 10 * 1000);

      //如果不是主播则用服务端的心跳状态来显示“主播已离开”
      if (!is_creater) {
         if (0 == getIntent().getIntExtra(EXTRA_IS_ON_SHOW, 1)) {
            //loading_bg还在
            if (AvActivity.this.findViewById(R.id.loading_bg).getVisibility() == View.VISIBLE) {
               stopProgressDialog();
               Trace.d("****>>>>在这里");
               videoIsClosed();
            }
         } else {
            Trace.d("****" + "在直播");
            video_is_closed = false;
            if (endView != null && endView.getVisibility() == View.VISIBLE) {
               Trace.d("****" + "直播结束的view不显示");
               endView.setVisibility(View.INVISIBLE);
            }
         }
      }
      //显示活动的medal
      medalListEvent = EventBus.getDefault().getStickyEvent(MedalListEvent.class);
      EventBus.getDefault().removeStickyEvent(MedalListEvent.class);
      if (medalListEvent == null) {
         medalListEvent = new MedalListEvent();
      }
      medalLayoutHelper = new MedalLayoutHelper(this);

      //活动入口
      if (medalListEvent.act != null) {
         ImageView img_act = (ImageView) findViewById(R.id.img_act);
         img_act.setVisibility(View.VISIBLE);
         img_act.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
               WebViewFragment rankMainFragment =
                   WebViewFragment.newInstance(medalListEvent.act.title, medalListEvent.act.url);
               rankMainFragment.showActFragment(AvActivity.this);
            }
         });
      } else {
         ImageView img_act = (ImageView) findViewById(R.id.img_act);
         img_act.setVisibility(View.GONE);
      }
   }

   private void getOperateData() {
      RequestInformation request = new RequestInformation(UrlHelper.OPERATE_RECORD + currentPage + "&liveuid=" + AULiveApplication.currLiveUid, RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<OperateRecordEntity>() {
         @Override
         public void onCallback(OperateRecordEntity callback) {
            if (callback.getStat() == 200) {
               mPop_view.findViewById(R.id.relativelayout).setVisibility(View.VISIBLE);
               current_list = callback.getData();
               if (mAdapter == null) {
                  mAdapter = new MyAdapter();
                  mListView.setAdapter(mAdapter);
               }
               mCurrent_page.setText("第" + currentPage + "页");
               mAdapter.notifyDataSetChanged();
            } else if (callback.getStat() == 201) {
               if (currentPage == 1) {
                  mPop_view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.VISIBLE);
                  return;
               }
               currentPage--;
               com.ilikezhibo.ggzb.tool.Utils.showCenterMessage(callback.getMsg());
            } else {
               com.ilikezhibo.ggzb.tool.Utils.showCenterMessage(callback.getMsg());
            }
         }

         @Override
         public void onFailure(AppException e) {
            Utils.showCenterMessage("获取网络数据失败");
         }
      }.setReturnType(OperateRecordEntity.class));
      request.execute();
   }

   MedalLayoutHelper medalLayoutHelper;
   public MedalListEvent medalListEvent = null;

   //切屏翻页前的清除
   public void doCacheDataClean() {
      currPage = 0;
      hostMember = null;
      headurl = null;
      mArrayListChatEntity.clear();
      mChatMsgListAdapter.notifyDataSetChanged();

      mMemberList.clear();
      roomUserListAdapter.notifyDataSetChanged();
      has_show_sys_msg = false;
      has_send_like = false;

      if (atten_timer != null) {
         atten_timer.cancel();
         atten_timer.purge();
         atten_timer = null;
      }
      //送礼物里的框框里的roomid及live_uid还是缓存上主播的,还会送给上一个主播
      giftPagerUtil = null;
      //清空已经送了多少礼物
      sendgiftList.clear();

      //礼物动画
      cache_luxury_gift_List.clear();
      cache_giftList.clear();
      cache_redbag_List.clear();
      if (continue_gift1.sendGiftList != null) {
         continue_gift1.sendGiftList.clear();
      }
      if (continue_gift2.sendGiftList != null) {
         continue_gift2.sendGiftList.clear();
      }

      cache_enter_room_List.clear();
      enter_room_effects_time.clear();

      UserDriveUtil.getInstance(this).cache_drive_List.clear();
      UserDriveUtil.getInstance(this).enterDriveTimeMap.clear();
   }

   public void setContentView() {
   }

   public void initializeViews() {
   }

   public void initializeData() {
   }

   //是否已经显示过系统信息
   boolean has_show_sys_msg = false;

   //显示有多少条未读信息

   boolean first_time_resume = true;

   @Override public void onResume() {
      super.onResume();

      doResume();
   }

   public void doResume() {
      if(video_is_closed) {
         return;
      }
      Trace.d("AvActivity onResume");
      //设置以下两个属性是为了Crouton在两个activity下显示
      AULiveApplication.mAvActivity = this;
      AULiveApplication.mAuLiveHomeActivity = null;
      AULiveApplication.is_on_home_context = false;

      //进入聊天
      doRoomChatEnter();

      //开始聊天清除
      startChatClean();

      //主播相关
      if (mStreamer != null) {
         //是不是第一次启动,主播返回执行重连
         if (!first_time_resume) {
            sendSystemMsg(ANCHOR_RESTORE, "主播回来啦，视频即将恢复");
         }
         first_time_resume = false;
         mAcitivityResumed = true;
         // 调用KSYStreamer的onResume接口
         mStreamer.onResume();
         // 停止背景图采集
         mStreamer.stopImageCapture();
         // 开启摄像头采集
         mStreamer.startCameraPreview();
         // 如果onPause中切到了DummyAudio模块，可以在此恢复
         mStreamer.setUseDummyAudioCapture(false);
      }
      if (ksyMediaPlayer != null) {
         if (has_onstop) {
            has_onstop = false;
            doPlayerReconnect();
         } else {
            ksyMediaPlayer.start();
         }
      }

      //主播不显示心
      if (!is_creater) {
         startHeartShow();
      }
      if (mDanmuControl != null) {
         mDanmuControl.resume();
      }
      if (giftPagerUtil != null) {
         giftPagerUtil.upDataDiamond(AULiveApplication.getUserInfo().diamond);
      }
   }

   private static long mLeaveLiveRoomTime = 0;

   //主播离开主播界面20秒，则为离开
   public static boolean isTimeToCloseRoom() {
      long time = System.currentTimeMillis();
      long timeD = time - mLeaveLiveRoomTime;
      if (timeD > 20000) {
         return true;
      }

      return false;
   }

   @Override public void onPause() {
      super.onPause();
      Trace.d("AvActivity onPause");
      doPause();
   }

   public void doPause() {
      //主播离开的时间
      mLeaveLiveRoomTime = System.currentTimeMillis();

      //发出主播离开的消息，让看客做相应的提示
      //只在第一次离开界面时调用，第2次调用是onresue里startActivity后销毁旧AvActivity调用的，不发送"主播离开"Msg
      if (is_creater) {
         sendSystemMsg(ANCHOR_LEAVE, "主播离开一下，精彩不断，不要走开");
      }

      if (mStreamer != null) {
         // 调用KSYStreamer的onPause接口
         mStreamer.onPause();
         // 停止摄像头采集，然后开启背景图采集，以实现后台背景图推流功能
         mStreamer.stopCameraPreview();
         mStreamer.startImageCapture(mBgImagePath);
         // 如果希望App切后台后，停止录制主播端的声音，可以在此切换为DummyAudio采集，
         // 该模块会代替mic采集模块产生静音数据，同时释放占用的mic资源
         mStreamer.setUseDummyAudioCapture(true);
         mAcitivityResumed = false;
      }
      if (ksyMediaPlayer != null) {
         ksyMediaPlayer.pause();
      }
      //主播不显示心
      if (!is_creater) {
         closeHeartShow();
      }
      //暂停聊天清除
      closeChatClean();

      //退出聊天
      doRoomChatExit();
      if (mDanmuControl != null) {
         mDanmuControl.pause();
      }
   }

   boolean has_onstop = false;

   @Override public void onStop() {
      super.onStop();
      if (ksyMediaPlayer != null) {
         has_onstop = true;
         Trace.d("AvActivity onStop()");
         ksyMediaPlayer.stop();
      }
   }

   //用于主播退出时的预处理
   public void onRoomLeave() {
      if (mStreamer != null) {
         mStreamer.stopStream();
         mAcitivityResumed = false;
      }

      if (ksyMediaPlayer != null) {
         ksyMediaPlayer.stop();
         ksyMediaPlayer.release();
//         ksyMediaPlayer = null;
      }
   }

   @Override public void onDestroy() {
      super.onDestroy();
      Trace.d("AvActivity onDestroy");
      EventBus.getDefault().unregister(this);
      if (mStreamer != null) {
         //主播的连麦销毁处理
         //从onpause到ondestroy()
         mStreamer.stopStream();
         mStreamer.release();
         executorService.shutdownNow();
      }
      if (ksyMediaPlayer != null) {
         ksyMediaPlayer.release();
         Trace.d("****>>>>ondestroy释放一下ksymediaplayer看看");
         ksyMediaPlayer = null;
      }

      if (mHandler != null) {
         mHandler.removeCallbacksAndMessages(null);
         mHandler = null;
      }

      if (mHandler_playback != null) {
         mHandler_playback.removeCallbacksAndMessages(null);
         mHandler_playback = null;
      }

      if (AULiveApplication.currLiveUid != null && connectionReceiver != null) {
         unregisterReceiver(connectionReceiver);
      }
      if (mDanmuControl != null) {
         mDanmuControl.destroy();
      }

      if (shareDialog != null) {
         this.unregisterReceiver(shareDialog.getShareReceiver());
         shareDialog.dismiss();
         shareDialog = null;
      }

      //很关键，内存清空
      cleanViews();
   }

   //网络监听
   private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
      @Override public void onReceive(Context context, Intent intent) {
         ConnectivityManager connectMgr =
                 (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
         NetworkInfo mobileInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
         NetworkInfo wifiInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

         if (mobileInfo == null || wifiInfo == null) {
            return;
         }

         if (!mobileInfo.isConnected() && !wifiInfo.isConnected()) {
            Trace.d("WL_DEBUG connectionReceiver no network = ");
            com.jack.utils.Utils.showCroutonText(AvActivity.this, "没有网络");
         } else {
            if (!is_creater) {
               // connect network
               //Utils.showCroutonText(AvActivity.this, "网络正常,加载中");
               Trace.d("网络恢复，重连");
               doPlayerReconnect();
            } else {
               //mStreamer.onResume();
            }
         }
      }
   };

   ///////////////////////////////////////////////////////////////////////////////////

   public GLSurfaceView mCameraPreview;
   public KSYStreamer mStreamer;
   private Handler mHandler;

   //生成的视频链接

   private boolean recording = false;
   public boolean startAuto = false;
   private volatile boolean mAcitivityResumed = false;
   ExecutorService executorService = Executors.newSingleThreadExecutor();

   //视频录像相关
   public void initRecord() {
      mHandler = new Handler();
      mStreamer = new KSYStreamer(this);
      config();
      enableBeautyFilter();
      mStreamer.startStream();
   }

   protected void enableBeautyFilter() {
      // 设置美颜滤镜的错误回调，当前机型不支持该滤镜时禁用美颜
      mStreamer.getImgTexFilterMgt().setOnErrorListener(new ImgTexFilterBase.OnErrorListener() {
         @Override
         public void onError(ImgTexFilterBase filter, int errno) {
            Utils.showMessage("当前机型不支持该滤镜");
            mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                    ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
         }
      });
      // 设置美颜滤镜，关于美颜滤镜的具体说明请参见专题说明以及完整版demo
      mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
              ImgTexFilterMgt.KSY_FILTER_BEAUTY_PRO3);
   }

   protected boolean isHw264EncoderSupported() {
      DeviceInfo deviceInfo = DeviceInfoTools.getInstance().getDeviceInfo();
      if (deviceInfo != null) {
         return deviceInfo.encode_h264 == DeviceInfo.ENCODE_HW_SUPPORT;
      }
      return false;
   }

   private KSYStreamer.OnInfoListener mLiveOnInfoListener = new KSYStreamer.OnInfoListener() {
      @Override
      public void onInfo(int what, int msg1, int msg2) {
         onStreamerInfo(what, msg1, msg2);
      }
   };

   protected void onStreamerInfo(int what, int msg1, int msg2) {
      switch (what) {
         case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
            break;
         case StreamerConstants.KSY_STREAMER_CAMERA_FACING_CHANGED:
            break;
         case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
            AvActivity.this.findViewById(R.id.loading_bg).setVisibility(View.GONE);
            break;
         case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
            Utils.showMessage("网络状况不佳");
            break;
         case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
            break;
         case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
            break;
         default:
            break;
      }
   }

   private boolean isHard;
   protected void config() {
      // 设置推流URL地址
      if (live_url == null) {
         Utils.showMessage("参数错误，请重新开播");
         this.finish();
      }
      mStreamer.setUrl(live_url);
      // 设置推流分辨率
      mStreamer.setPreviewResolution(StreamerConstants.VIDEO_RESOLUTION_540P);
      mStreamer.setTargetResolution(StreamerConstants.VIDEO_RESOLUTION_540P);
      // 设置编码方式（硬编、软编）
      if (isHw264EncoderSupported()) {
         mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_HARDWARE);
         isHard = true;
      } else {
         mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
         isHard = false;
      }
      // 硬编模式下默认使用高性能模式(high profile)
      if (isHard) {
         mStreamer.setVideoEncodeProfile(VideoEncodeFormat.ENCODE_PROFILE_HIGH_PERFORMANCE);
      }
      // 设置推流帧率
      mStreamer.setPreviewFps(15);
      mStreamer.setTargetFps(15);

      // 设置推流视频码率，三个参数分别为初始码率、最高码率、最低码率
      mStreamer.setVideoKBitrate(800, 1200, 600);
      // 设置音频码率
      mStreamer.setAudioKBitrate(48);

      // 设置视频方向（横屏、竖屏）
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      mStreamer.setRotateDegrees(0);
      // 选择前后摄像头
      mStreamer.setCameraFacing(CameraCapture.FACING_FRONT);
      // 设置预览View
      mStreamer.setDisplayPreview(mGLSurfaceView);
      // 设置回调处理函数
      mStreamer.setOnInfoListener(mLiveOnInfoListener);
      mStreamer.setOnErrorListener(mLiveOnErrorListener);
      // 禁用后台推流时重复最后一帧的逻辑（这里我们选择切后台使用背景图推流的方式）
      mStreamer.setEnableRepeatLastFrame(false);
   }

   private KSYStreamer.OnErrorListener mLiveOnErrorListener = new KSYStreamer.OnErrorListener() {
      @Override
      public void onError(int what, int msg1, int msg2) {
         onStreamerError(what, msg1, msg2);
      }
   };

   protected void onStreamerError(int what, int msg1, int msg2) {
      switch (what) {
         case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
         case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
            Trace.d("error_one");
            break;
         case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
         case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
         case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
         case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
            mStreamer.stopCameraPreview();
            Trace.d("error_two");
            break;
         case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
         case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
            handleEncodeError();
            Trace.d("error_three");
         default:
            reStreaming();
            break;
      }
   }
   protected boolean mHWEncoderUnsupported;
   protected boolean mSWEncoderUnsupported;
   protected void handleEncodeError() {
      int encodeMethod = mStreamer.getVideoEncodeMethod();
      if (encodeMethod == StreamerConstants.ENCODE_METHOD_HARDWARE) {
         mHWEncoderUnsupported = true;
         if (mSWEncoderUnsupported) {
            mStreamer.setEncodeMethod(
                    StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
         } else {
            mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
         }
      } else if (encodeMethod == StreamerConstants.ENCODE_METHOD_SOFTWARE) {
         mSWEncoderUnsupported = true;
         mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
      }
   }

   protected void reStreaming() {
      mStreamer.stopStream();
      mHandler.postDelayed(new Runnable() {
         @Override
         public void run() {
            mStreamer.startStream();
         }
      }, 3000);
   }

   //处理返回按钮事件
   @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
      switch (keyCode) {
         case KeyEvent.KEYCODE_BACK:

            // >处理弹出的关注界面
            if (video_is_closed == false && PrivateChatListActivity.is_on_chat_context) {
               privateChatListHelper.onClosPrivate();
               return true;
            }
            // >处理夜币打开的界面
            if (video_is_closed == false && rankMainFragment != null && rankMainFragment.getTopRankIsOpen()) {
               rankMainFragment.closeTopRankMainFragment();
               return true;
            }
            // >处理私聊打开的界面
            if (video_is_closed == false && privateChatHelper != null && privateChatHelper.getPrivateChatIsOpen()) {
               privateChatHelper.onClosPrivate();
               return true;
            }

            // >处理绘制个性礼物打开的界面
            if(video_is_closed == false && operateHelper != null && operateHelper.isOpenPadview()) {
               operateHelper.doClose();
               return true;
            }

            if (gameOverFragment != null && gameOverFragment.getIsOpen()) {
               Trace.d("****>>>>直播结束移除fragment");

               getSupportFragmentManager().beginTransaction().remove(gameOverFragment);
               this.finish();
               return true;
            }

            if (is_creater) {
               hostCloseAlertDialog();
            } else {
               memberCloseAlertDialog();
            }
            break;
         case KeyEvent.KEYCODE_VOLUME_DOWN:
            Trace.d("KEYCODE_VOLUME_DOWN");
            AudioManager audioManager =
                (AudioManager) AvActivity.this.getSystemService(Context.AUDIO_SERVICE);
            //降低音量，调出系统音量控制
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                AudioManager.FX_FOCUS_NAVIGATION_UP);

            break;
         case KeyEvent.KEYCODE_VOLUME_UP:
            Trace.d("KEYCODE_VOLUME_UP");
            AudioManager audioManager1 =
                (AudioManager) AvActivity.this.getSystemService(Context.AUDIO_SERVICE);
            //增加音量，调出系统音量控制
            audioManager1.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
                AudioManager.FX_FOCUS_NAVIGATION_UP);
            break;
         case KeyEvent.KEYCODE_VOLUME_MUTE:
            Trace.d("KEYCODE_VOLUME_MUTE");
            break;

         default:
            break;
      }
      return true;
   }

   private void doReconnect() {
      Trace.d("mStreamer doReconnect()");
      mStreamer.updateUrl(live_url);
      if (!executorService.isShutdown()) {
         executorService.submit(new Runnable() {

            @Override public void run() {
               boolean needReconnect = true;
               try {
                  while (needReconnect) {
                     Thread.sleep(1000);
                     //只在Activity对用户可见时重连
                     if (mAcitivityResumed) {
                        if (mStreamer.startStream()) {
                           recording = true;
                           needReconnect = false;
                        }
                     }
                  }
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
         });
      }
   }
   private long lastClickTime = 0;

   //切换摄像头
   private void onSwitchCamClick() {
      long curTime = System.currentTimeMillis();
      if (curTime - lastClickTime < 1000) {
         return;
      }
      lastClickTime = curTime;

      mStreamer.switchCamera();
   }

   //闪光灯

   private boolean isFlashOpened = false;

   private void onFlashClick() {
      if (isFlashOpened) {
         mStreamer.toggleTorch(false);
         isFlashOpened = false;
      } else {
         mStreamer.toggleTorch(true);
         isFlashOpened = true;
      }
   }

   private String md5(String string) {
      byte[] hash;
      try {
         hash = MessageDigest.getInstance("MD5").digest(string.getBytes());
      } catch (NoSuchAlgorithmException e) {
         throw new RuntimeException("Huh, MD5 should be supported?", e);
      }

      StringBuilder hex = new StringBuilder(hash.length * 2);
      for (byte b : hash) {
         if ((b & 0xFF) < 0x10) {
            hex.append("0");
         }
         hex.append(Integer.toHexString(b & 0xFF));
      }

      return hex.toString();
   }

   ////////////////////////////////////////////////////////////
   //看客,视频播放相关

   public KSYMediaPlayer ksyMediaPlayer;

   private Surface mSurface = null;
   public VideoSurfaceView mVideoSurfaceView = null;
   private SurfaceHolder mSurfaceHolder = null;

   private int mVideoWidth = 0;
   private int mVideoHeight = 0;

   public void initVideoPlayer() {

      startProgressDialog();

      mVideoSurfaceView = (VideoSurfaceView) findViewById(R.id.player_surface);
      mVideoSurfaceView.setVisibility(View.VISIBLE);

      mSurfaceHolder = mVideoSurfaceView.getHolder();
      mSurfaceHolder.addCallback(mSurfaceCallback);
      mVideoSurfaceView.setOnTouchListener(mTouchListener);
      mVideoSurfaceView.setKeepScreenOn(true);

      this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

      String timeSec = String.valueOf(System.currentTimeMillis() / 1000);
      String skSign = md5(secretKey + timeSec);
      ksyMediaPlayer = null;
      Trace.d(">>>>ksymediaplayer new初始化");
      ksyMediaPlayer = new KSYMediaPlayer.Builder(this.getApplicationContext()).build();
      //ksyMediaPlayer = new KSYMediaPlayer.Builder(AvActivity.this).build();
      ksyMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
      ksyMediaPlayer.setOnCompletionListener(mOnCompletionListener);
      ksyMediaPlayer.setOnPreparedListener(mOnPreparedListener);
      ksyMediaPlayer.setOnInfoListener(mOnInfoListener);
      ksyMediaPlayer.setOnVideoSizeChangedListener(mOnVideoSizeChangeListener);
      ksyMediaPlayer.setOnErrorListener(mOnErrorListener_player);
      ksyMediaPlayer.setOnSeekCompleteListener(mOnSeekCompletedListener);
      ksyMediaPlayer.setScreenOnWhilePlaying(true);
      ksyMediaPlayer.setBufferTimeMax(5);
      //ksyMediaPlayer.setFastShowMode(1F);
      ksyMediaPlayer.setTimeout(15, 30);
      ksyMediaPlayer.setOnLogEventListener(new IMediaPlayer.OnLogEventListener() {
         @Override public void onLogEvent(IMediaPlayer iMediaPlayer, final String s) {
            Trace.d("ksyPlayer onLogEvent s:" + s);
            //tv_live_uid = (TextView) findViewById(R.id.tv_live_uid);
            //tv_live_uid.post(new Runnable() {
            //   @Override public void run() {
            //      tv_live_uid.setText(s);
            //   }
            //});
         }
      });

      Trace.d("ksyMediaPlayer.getVersion():" + ksyMediaPlayer.getVersion());

      //Trace.d("ksyMediaPlayer Version:"+ksyMediaPlayer.getVersion());
      // set cache dir
      // enable http local cache
//        ksyMediaPlayer.setCachedDir("/mnt/sdcard/");

      try {
         //ksyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
         ksyMediaPlayer.setDataSource(play_url);
         ksyMediaPlayer.prepareAsync();
      } catch (IOException e) {
         Trace.d("****>>>>不需要播放了");
         e.printStackTrace();
      }
      //5秒没有prepare,则是主播没在直播
      noHostTimer = new Timer();
      //noHostTimer.schedule(new TimerTask() {
      //   @Override public void run() {
      //      AvActivity.this.runOnUiThread(new Runnable() {
      //         @Override public void run() {
      //            Utils.showCroutonText(AvActivity.this, "目前没有直播");
      //            videoIsClosed();
      //         }
      //      });
      //   }
      //}, 5000);
   }

   private Timer noHostTimer;

   private IMediaPlayer.OnPreparedListener mOnPreparedListener =
       new IMediaPlayer.OnPreparedListener() {
          @Override public void onPrepared(IMediaPlayer mp) {
             //有主播,取消两秒后关闭
             noHostTimer.cancel();
             stopProgressDialog();
             Trace.d("onPrepared");
             if (ksyMediaPlayer != null) {
                mVideoWidth = ksyMediaPlayer.getVideoWidth();
                mVideoHeight = ksyMediaPlayer.getVideoHeight();

                if (mVideoSurfaceView != null) {
                   mVideoSurfaceView.setVideoDimension(ksyMediaPlayer.getVideoWidth(),
                       ksyMediaPlayer.getVideoHeight());
                   mVideoSurfaceView.requestLayout();
                }
                ksyMediaPlayer.start();

                //回放初始化
                setVideoProgress(0);

//                showUserDrive();
             }

             if (ksyMediaPlayer != null && ksyMediaPlayer.getServerAddress() != null) {
                Trace.d("ServerIP: " + ksyMediaPlayer.getServerAddress());
             }
          }
       };

   private IMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener =
       new IMediaPlayer.OnBufferingUpdateListener() {
          @Override public void onBufferingUpdate(IMediaPlayer mp, int percent) {
             if (ksyMediaPlayer == null) {
                return;
             }
             //Trace.d("onBufferingUpdate");
             long duration = ksyMediaPlayer.getDuration();
             long progress = duration * percent / 100;
             mPlayerSeekbar.setSecondaryProgress((int) progress);
          }
       };

   private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangeListener =
       new IMediaPlayer.OnVideoSizeChangedListener() {
          @Override
          public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sarNum,
              int sarDen) {
             Trace.d("onVideoSizeChanged");
             if (mVideoWidth > 0 && mVideoHeight > 0) {
                if (width != mVideoWidth || height != mVideoHeight) {
                   mVideoWidth = mp.getVideoWidth();
                   mVideoHeight = mp.getVideoHeight();

                   // maybe we could call scaleVideoView here.
                   if (mVideoSurfaceView != null) {
                      mVideoSurfaceView.setVideoDimension(mVideoWidth, mVideoHeight);
                      mVideoSurfaceView.requestLayout();
                   }
                }
             }
          }
       };

   private IMediaPlayer.OnSeekCompleteListener mOnSeekCompletedListener =
       new IMediaPlayer.OnSeekCompleteListener() {
          @Override public void onSeekComplete(IMediaPlayer mp) {
             Trace.d("OnSeekCompleteListener");
          }
       };

   private IMediaPlayer.OnCompletionListener mOnCompletionListener =
       new IMediaPlayer.OnCompletionListener() {
          @Override public void onCompletion(IMediaPlayer mp) {
             Trace.d("OnCompletionListener");
             //用于第一次进来，主播不在线得情况
             //太久，用EXTRA_IS_ON_SHOW状态来马上显示
             //if (AvActivity.this.findViewById(R.id.loading_bg).getVisibility() == View.VISIBLE) {
             //   videoIsClosed();
             //}
          }
       };

   private IMediaPlayer.OnErrorListener mOnErrorListener_player =
       new IMediaPlayer.OnErrorListener() {
          @Override public boolean onError(IMediaPlayer mp, int what, int extra) {
             switch (what) {
                //case KSYMediaPlayer.MEDIA_ERROR_AUTH_FAILED:
                //   Trace.d("OnErrorListener MEDIA_ERROR_AUTH_FAILED");
                //   break;
                case KSYMediaPlayer.MEDIA_ERROR_IO:
                   Trace.d("OnErrorListener MEDIA_ERROR_IO");
                   break;
                case KSYMediaPlayer.MEDIA_ERROR_MALFORMED:
                   Trace.d("OnErrorListener MEDIA_ERROR_MALFORMED");
                   break;
                case KSYMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                   Trace.d("OnErrorListener MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK");
                   break;
                case KSYMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                   Trace.d("OnErrorListener MEDIA_ERROR_SERVER_DIED");
                   break;
                case KSYMediaPlayer.MEDIA_ERROR_TIMED_OUT:
                   Trace.d("OnErrorListener MEDIA_ERROR_TIMED_OUT");
                   break;
                case KSYMediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                   Trace.d("OnErrorListener MEDIA_ERROR_UNSUPPORTED");
                   break;
                case KSYMediaPlayer.MEDIA_ERROR_UNKNOWN:
                   Trace.d("OnErrorListener MEDIA_ERROR_UNKNOWN");
                   break;
                default:
                   Trace.d("OnErrorListener default:" + "Error:" + what + ",extra:" + extra);
                   Utils.showCroutonText(AvActivity.this, "Error:" + what + ",extra:" + extra);
                   //出错，关闭
                   //videoIsClosed();
             }

             return false;
          }
       };

   public IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
      @Override public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
         Trace.d("OnInfoListener i,i1:" + i + "," + i1);
         //去loading图,推流进来了
         if (i == 3) {
            AvActivity.this.findViewById(R.id.loading_bg).setVisibility(View.GONE);
         }

         if (i == 701) {
            //if (!BtnClickUtils.isFastShow()) {
            //   Utils.showCroutonText(AvActivity.this, "网络不佳,请稍后");
            //}
         }
         //主播离开主播页面返回
         if (i == 702) {
            //无用，作废
            //if (ksyMediaPlayer != null) {
            //   ksyMediaPlayer.reload(play_url);
            //   ksyMediaPlayer.start();
            //}

         }
         return false;
      }
   };

   private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
         Trace.d("surfaceChanged");
         if (ksyMediaPlayer != null) {
            final Surface newSurface = holder.getSurface();
            ksyMediaPlayer.setDisplay(holder);
            ksyMediaPlayer.setScreenOnWhilePlaying(true);
            if (mSurface != newSurface) {
               mSurface = newSurface;
               ksyMediaPlayer.setSurface(mSurface);
            }
         }
      }

      @Override public void surfaceCreated(SurfaceHolder holder) {
         Trace.d("surfaceCreated");
      }

      @Override public void surfaceDestroyed(SurfaceHolder holder) {
         Trace.d("surfaceDestroyed");
         if (ksyMediaPlayer != null) {
            mSurface = null;
         }
      }
   };

   @Override public int getChangingConfigurations() {
      return super.getChangingConfigurations();
   }

   @Override public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
   }

   private void scaleVideoView() {
      if (ksyMediaPlayer == null
          || ksyMediaPlayer.getVideoHeight() <= 0
          || mVideoSurfaceView == null) {
         return;
      }

      WindowManager wm = this.getWindowManager();
      int sw = wm.getDefaultDisplay().getWidth();
      int sh = wm.getDefaultDisplay().getHeight();
      int videoWidth = mVideoWidth;
      int videoHeight = mVideoHeight;
      int visibleWidth = 0;
      int visibleHeight = 0;

      if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
         visibleWidth = sw > sh ? sh : sw;
         visibleHeight = (int) Math.ceil(visibleWidth * videoHeight / videoWidth);
      } else if (getResources().getConfiguration().orientation
          == Configuration.ORIENTATION_LANDSCAPE) {

         if (videoHeight * sw > videoWidth * sh) {
            visibleHeight = sh;
            visibleWidth = (int) Math.ceil(videoWidth * visibleHeight / videoHeight);
         } else {
            visibleWidth = sw;
            visibleHeight = (int) Math.ceil(visibleWidth * videoHeight / videoWidth);
         }
      }

      ViewGroup.LayoutParams lp = mVideoSurfaceView.getLayoutParams();
      lp.width = visibleWidth;
      lp.height = visibleHeight;
      mVideoSurfaceView.setLayoutParams(lp);

      mVideoSurfaceView.invalidate();
   }

   private void videoPlayEnd() {
      if (ksyMediaPlayer != null) {
         ksyMediaPlayer.release();
         Trace.d(">>>>结束播放，ksyMediaPlayer置为空");
         ksyMediaPlayer = null;
      }
   }

   private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {

         return false;
      }
   };

   //////////////////////////////////////////////////////////////////////////
   //销毁avactivity之后nullviews
   private void cleanViews() {
      mListViewMsgItems = null;
      mEditTextInputMsg = null;
      danmu_checkbox = null;

      if (mChatTimer != null) {
         mChatTimer.cancel();
         mChatTimer = null;
      }
      if (mChatTimerTask != null) {
         mChatTimerTask.cancel();
         mChatTimerTask = null;
      }
      if (mHeartTimer != null) {
         mHeartTimer.cancel();
         mHeartTimer = null;
      }

      continue_gift1 = null;
      continue_gift2 = null;

      giftpager_popupWindow = null;
      giftPagerUtil = null;

      LuxuryGiftUtil.cleanLuxuryGiftUtil();
      EnterRoomEffectsUtil.is_showing_enter_room_effects = false;
      //清空豪华礼物缓存
      cache_luxury_gift_List.clear();

      user_popupWindow = null;
      share_popupWindow = null;
      guanli_popupWindow = null;

      privateChatListHelper.cleanViews();
      privateChatListHelper = null;
      //聊天界面

      privateChatHelper.cleanViews();
      privateChatHelper = null;

      mHandler = null;

      if (blur_bg_bitmap != null && !blur_bg_bitmap.isRecycled()) {
         blur_bg_bitmap.recycle();  //回收图片所占的内存
      }

      AULiveApplication.mAvActivity = null;

      RelativeLayout root_view = (RelativeLayout) findViewById(R.id.av_screen_layout);
      if (root_view != null) {
         root_view.removeAllViews();
      }
      //清理一下垃圾
      System.gc();

      UserDriveUtil.getInstance(this).cleanUserDriveUtil();
   }
   //初始化views

   public ListView mListViewMsgItems;
   private EditText mEditTextInputMsg;
   private CheckBox danmu_checkbox;
   private Button mButtonSendMsg;
   private InputMethodManager mInputKeyBoard;

   //聊天相关
   private List<ChatEntity> mArrayListChatEntity;
   private ChatMsgListAdapter mChatMsgListAdapter;

   private boolean mIsLoading = false;

   private Timer mChatTimer;
   private ChatTimerTask mChatTimerTask;
   //头部看客列表
   public ArrayList<MemberInfo> mMemberList = new ArrayList<MemberInfo>();

   private UserInfo mSelfUserInfo;

   int heart_beat_count = 0;

   public boolean getVideoIsClosed() {
      return video_is_closed;
   }

   private class ChatTimerTask extends TimerTask {
      @Override public void run() {
         if (mListViewMsgItems == null) {
            return;
         }

         mListViewMsgItems.post(

                 new Runnable() {
            @Override public void run() {
               removeChatItem();
            }
         });

         if (is_creater) {
            //心态 15一次
            heart_beat_count++;
            if (heart_beat_count >= 3) {
               //发送心跳
               heart_beat_count = 0;
               doHeartBeat();
            }
         }
      }
   }

   // 键盘相关
   //屏幕高度
   private int screenHeight = 0;
   //软件盘弹起后所占高度阀值
   private int keyHeight = 0;
   private int old_screen_location_y = 0;
   int[] screen_location = new int[2];

   //自定义组件
   //聊天输入与操作界面互换
   private LinearLayout qav_chat_input_layout;
   private LinearLayout operate_layout;
   private View touch_delegate_view1;
   private RelativeLayout av_screen_layout;
   //底部View
   View qav_bottom_bar;

   //公聊
   private ImageView img_chat;
   private ImageView img_private_chat;
   private ImageView img_shareroom;
   private ImageView img_gift;
   private ImageView img_guanli;
   private ImageView img_shutdown;
   private ImageView img_custom_gift;

   private ImageView img_room_creator;
   private ImageView img_user_type;

   private LinearLayout room_usernum_container;
   private TextView txt_usernum;
   private RecyclerView recyclerView;
   private RoomUserListAdapter roomUserListAdapter;

   private Random mRandom = new Random();
   private Timer mHeartTimer = null;
   private HeartLayout mHeartLayout = null;
   //发送点赞,一个直播间只发送一次
   boolean has_send_like = false;

   private int randomColor() {
      return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
   }

   private ContinueGiftView continue_gift1;
   private ContinueGiftView continue_gift2;
   public LinearLayout qav_top_bar_new;

   private ShareHelper shareDialog = null;

   private LinearLayout gold_count_layout;
   private MemberInfo hostMember;

   //主播的钻石数
   private int recv_diamond;

   //dialog相关
   private GiftPagerUtil giftPagerUtil;
   public PopupWindowUtil giftpager_popupWindow;

   private PopupWindowUtil user_popupWindow;

   private PopupWindowUtil share_popupWindow;

   private PopupWindowUtil guanli_popupWindow;

   public class WrapContentLinearLayoutManager extends LinearLayoutManager {
      public WrapContentLinearLayoutManager(Context context) {
         super(context);
      }

      public WrapContentLinearLayoutManager(Context context, int orientation,
          boolean reverseLayout) {
         super(context, orientation, reverseLayout);
      }

      public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
          int defStyleRes) {
         super(context, attrs, defStyleAttr, defStyleRes);
      }

      @Override
      public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
         try {
            super.onLayoutChildren(recycler, state);
         } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
         }
      }

      //防快划动抛异常
      @Override public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler,
          RecyclerView.State state) {
         try {
            return super.scrollHorizontallyBy(dx, recycler, state);
         } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
         }
         return 0;
      }
   }

   TextView tv_live_uid;

   private void initViews() {
      mView_location = (TextView) findViewById(R.id.title);
      bg_imageView = (ImageView) findViewById(R.id.loading_bg);
      mOperateRecord = (Button) findViewById(R.id.operate_record);

      mIv_type = (ImageView) findViewById(R.id.live_type);

      //try {
      //   blur_bg_bitmap = ChatMsgListAdapter.readBitMap(this, R.drawable.open_show_room);
      //   bg_imageView.setImageBitmap(blur_bg_bitmap);
      //} catch (Exception e) {
      //}

      //设置默认加载背景
      String face = getIntent().getExtras().getString(EXTRA_SELF_IDENTIFIER_FACE);
      if (is_creater) {
         face = AULiveApplication.getUserInfo().getFace();
      }

      ImageLoader.getInstance().loadImage(face, new ImageLoadingListener() {
         @Override public void onLoadingStarted(String s, View view) {
         }

         @Override public void onLoadingFailed(String s, View view, FailReason failReason) {
         }

         @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            if (bitmap == null) {
               return;
            }
            bg_imageView.setImageBitmap(FastBlurUtil.toBlur(bitmap, 10));
         }

         @Override public void onLoadingCancelled(String s, View view) {

         }
      });

      //划屏相关
      viewFlipper = (ViewFlipper) findViewById(R.id.body_flipper);
      viewFlipper.setOnTouchListener(this);
      viewFlipper.setDisplayedChild(1);

      mSelfUserInfo = AULiveApplication.getMyselfUserInfo();
      //注册网络状态监听
      IntentFilter netIntentFilter = new IntentFilter();
      netIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
      registerReceiver(connectionReceiver, netIntentFilter);

      gold_count_layout = (LinearLayout) findViewById(R.id.gold_count_layout);
      gold_count_layout.setOnClickListener(new View.OnClickListener() {

         @Override public void onClick(View view) {
            if (BtnClickUtils.isFastDoubleClick()) {
               return;
            }
            //if (is_creater) {
            //Utils.showCroutonText(AvActivity.this, "主播不能离开主播间");
            rankMainFragment = TopRankMainFragment.newInstance(AULiveApplication.currLiveUid);
            rankMainFragment.showManagersList(AvActivity.this);
            //} else {
            //   //贡献榜
            //   Intent top_rank = new Intent(AvActivity.this, TopRankActivity.class).putExtra(
            //       TopRankActivity.MemberInfo_key, AULiveApplication.currLiveUid);
            //   startActivity(top_rank);
            //}
         }
      });

      qav_top_bar_new = (LinearLayout) findViewById(R.id.qav_top_bar_new);
      //连送礼物动画
      continue_gift1 = (ContinueGiftView) findViewById(R.id.continue_gift1);
      continue_gift2 = (ContinueGiftView) findViewById(R.id.continue_gift2);
      findViewById(R.id.touch_delegate_view2).setOnTouchListener(this);
      //主播头像
      View.OnClickListener live_host_listener = new View.OnClickListener() {
         @Override public void onClick(View view) {
            if (hostMember == null) {
               return;
            }
            //if (user_popupWindow == null) {
            user_popupWindow = new PopupWindowUtil(img_room_creator);
            user_popupWindow.setContentView(R.layout.dialog_myroom_userinfo);
            user_popupWindow.setOutsideTouchable(true);
            user_popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
               @Override public void onDismiss() {

               }
            });
            //做View init
            UserInfoHelper userInfoHelper =
                UserInfoHelper.getInstance(img_room_creator, user_popupWindow, AvActivity.this,
                    hostMember);
            //} else {
            //   user_popupWindow.showCenter();
            //}
         }
      };
      //主播头像点击处理
      room_usernum_container = (LinearLayout) findViewById(R.id.room_usernum_container);
      room_usernum_container.setOnClickListener(live_host_listener);

      img_room_creator = (ImageView) findViewById(R.id.img_room_creator);
      img_user_type = (ImageView) findViewById(R.id.img_user_type);
      img_room_creator.setOnClickListener(live_host_listener);
      //观看人数
      txt_usernum = (TextView) findViewById(R.id.txt_usernum);

      //顶部的用户列表
      recyclerView = (RecyclerView) findViewById(R.id.recyclerView_users);

      recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

         @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //Trace.d("recyclerView onScrollStateChanged isLoading hasMore:" + isLoading+
            //    " " + hasMore);
            if (!isLoading
                && hasMore
                && newState == RecyclerView.SCROLL_STATE_IDLE
                && recyclerView_layoutManager.findLastVisibleItemPosition() + 1
                == mMemberList.size()) {

               getMemberInfo();
            }
         }

         @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //lastVisibleItem = recyclerView_layoutManager.findLastVisibleItemPosition();
         }
      });
      //recyclerView.setOnTouchListener(new View.OnTouchListener() {
      //       @Override public boolean onTouch(View v, MotionEvent event) {
      //          if (isLoading) {
      //             return true;
      //          } else {
      //             return false;
      //          }
      //       }
      //    });

      // 创建一个线性布局管理器
      recyclerView_layoutManager = new WrapContentLinearLayoutManager(this);
      recyclerView_layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
      // 设置布局管理器
      recyclerView.setLayoutManager(recyclerView_layoutManager);
      roomUserListAdapter = new RoomUserListAdapter(mMemberList, AvActivity.this);
      // 设置Adapter
      recyclerView.setAdapter(roomUserListAdapter);

      View chat_list_container = findViewById(R.id.chat_list_container);

      danmu_checkbox = (CheckBox) findViewById(R.id.danmu_checkbox);
      danmu_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               mEditTextInputMsg.setHint("开启弹幕，1钻石/条");
            } else {
               mEditTextInputMsg.setHint("说点什么吧");
            }
         }
      });
      //聊天输入框
      mEditTextInputMsg = (EditText) findViewById(R.id.qav_bottombar_msg_input);
      mEditTextInputMsg.setOnClickListener(this);

      //默认不显示键盘
      mInputKeyBoard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

      //处理点击输入框外，关闭输入法
      av_screen_layout = (RelativeLayout) findViewById(R.id.av_screen_layout);

      touch_delegate_view1 = findViewById(R.id.touch_delegate_view1);
      //touch_delegate_view1.setOnClickListener(new View.OnClickListener() {
      //   @Override public void onClick(View view) {
      //
      //   }
      //});
      //touch_delegate_view1.setOnTouchListener(this);

      //底部组件相关

      qav_chat_input_layout = (LinearLayout) findViewById(R.id.qav_chat_input_layout);
      operate_layout = (LinearLayout) findViewById(R.id.operate_layout);

      img_chat = (ImageView) findViewById(R.id.img_chat);
      img_chat.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            //发言限制先关闭
            int grade = Integer.parseInt(AULiveApplication.getUserInfo().getGrade());
//            if (grade < 1) {
//               Utils.showCenterMessage("对不起，一级以上才可以发言");
//               return;
//            }
            if (qav_chat_input_layout.getVisibility() == View.GONE) {
               operate_layout.setVisibility(View.GONE);
               qav_chat_input_layout.setVisibility(View.VISIBLE);
               findViewById(R.id.place_temp).setVisibility(View.GONE);
               qav_top_bar_new.setVisibility(View.GONE);

               mEditTextInputMsg.setFocusable(true);
               mEditTextInputMsg.setFocusableInTouchMode(true);
               mEditTextInputMsg.requestFocus();
               //延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
               InputMethodManager imm =
                   (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.showSoftInput(mEditTextInputMsg, 0);
            }
         }
      });
      img_private_chat = (ImageView) findViewById(R.id.img_private_chat);
      img_private_chat.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            //测试
            SendGiftEntity sendGiftEntity = new SendGiftEntity();
            sendGiftEntity.nickname = "ABC";
            sendGiftEntity.face = AULiveApplication.getUserInfo().getFace();

            privateChatListHelper.onOpenPrivateChat();

            //弹幕test
            //UserBarrageEntity userBarrageEntity = new UserBarrageEntity();
            //userBarrageEntity.chat_msg = "bigbigbigabc";
            //userBarrageEntity.face = AULiveApplication.getUserInfo().getFace();
            //userBarrageEntity.nickname = "yuyuid";
            //userBarrageEntity.uid = "123";
            //addDanMu(userBarrageEntity);

            //连发豪华礼物测试
            //for (int i = 21; i > 9; i--) {
            //   SendGiftEntity sendGiftEntity1 = new SendGiftEntity();
            //   sendGiftEntity1.gift_type = 3;
            //   sendGiftEntity1.nickname = "ABC";
            //   sendGiftEntity1.gift_id = i;
            //   sendGiftEntity1.face = AULiveApplication.getUserInfo().getFace();
            //   addReciveGiftEntity(sendGiftEntity1);
            //}

/*            for (int i = 34; i < 35; i++) {
               if (i != 18 && i != 19 && i != 17 && i != 16 && i != 15 && i != 14) {
                  GiftEntity temp_GiftEntity = new GiftEntity();
                  //当不是红包时为0
                  temp_GiftEntity.setId(401);
                  temp_GiftEntity.setType(3);
                  temp_GiftEntity.setPacketid(0);
                  temp_GiftEntity.recv_diamond = 1111;
                  temp_GiftEntity.setGrade(3);
                  temp_GiftEntity.setName("礼物" + i);
                  temp_GiftEntity.setPrice(1111);
                  try {
                     Thread.sleep(200);
                  } catch (InterruptedException e) {
                     e.printStackTrace();
                  }
                  //显示礼物动画
                  sendGiftMsg(temp_GiftEntity);
               }
            }*/
//            XiaoLaBaEntity xiaoLaBaEntity = new XiaoLaBaEntity();
//            xiaoLaBaEntity.target = "bigbig777";
//            xiaoLaBaEntity.sender = "bigbig666";
//            xiaoLaBaEntity.system_content = "x-man送了一个豪华礼物给了superman蜘蛛侠将他顶到了热门第一!";
//            xiaoLaBaHelper.doXiaoLaBaEffects(xiaoLaBaEntity);
         }
      });

      img_shareroom = (ImageView) findViewById(R.id.img_shareroom);
      if (is_creater) {
         img_shareroom.setVisibility(View.GONE);
      } else {
         img_shareroom.setVisibility(View.GONE);
      }
      img_shareroom.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {

            if (shareDialog == null) {
               shareDialog = new ShareHelper(AvActivity.this);
               shareDialog.setShareLiveuid(AULiveApplication.currLiveUid);
            }
            if (share_popupWindow == null) {
               share_popupWindow = new PopupWindowUtil(img_shareroom);
               share_popupWindow.setContentView(R.layout.room_share);
               share_popupWindow.setOutsideTouchable(false);
               //share_popupWindow.showBottomWithAlpha();
               share_popupWindow.showLikeQuickAction(PixelDpHelper.dip2px(AvActivity.this, 65),
                   PixelDpHelper.dip2px(AvActivity.this, -5));
               share_popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                  @Override public void onDismiss() {

                  }
               });
               //分享的内容

               //if (!shareDialog.isShowing()) {
               //   shareDialog.show();
               //}

               View btn_qqzone = share_popupWindow.findId(R.id.btn_qqzone);
               btn_qqzone.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     if (hostMember == null) {
                        return;
                     }
                     AULiveApplication auLiveApplication =
                         (AULiveApplication) TinkerManager.getTinkerApplicationLike();
                     ReleaseLiveActivity.doSharePrepare(AULiveApplication.currLiveUid,
                         auLiveApplication.getCity(), "qzone");
                     String getuid = mSelfUserInfo.getUserPhone();
                     //分享的内容
                     String share_target_url = "http://web."
                         + UrlHelper.URL_domain
                         + "/play?uid="
                         + AULiveApplication.currLiveUid
                         + "&liveid="
                         + getuid
                         + "&share_uid="
                         + getuid
                         + "&share_from=qzone";

                     if (is_record_play && record_id != null && !record_id.equals("")) {
                        share_target_url = share_target_url.replace("/play?uid", "/play/vdoid?uid");
                        share_target_url = share_target_url + "&vdoid=" + record_id;
                     }

                     shareDialog.setShareUrl(share_target_url);
                     shareDialog.setShareTitle(Utils.trans(R.string.app_name));
                     //内容文字，头像
                     shareDialog.setShareContent(
                             "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + "！" + share_target_url,
                             hostMember.getHeadImagePath());

                     shareDialog.doShareToQQZone();
                  }
               });

               View btn_qq = share_popupWindow.findId(R.id.btn_qq);
               btn_qq.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     if (hostMember == null) {
                        return;
                     }
                     AULiveApplication auLiveApplication =
                         (AULiveApplication) TinkerManager.getTinkerApplicationLike();
                     ReleaseLiveActivity.doSharePrepare(AULiveApplication.currLiveUid,
                         auLiveApplication.getCity(), "qq");

                     String getuid = mSelfUserInfo.getUserPhone();
                     String nickname = mSelfUserInfo.getUserName();
                     //分享的内容
                     String share_target_url = "http://web."
                         + UrlHelper.URL_domain
                         + "/play?uid="
                         + AULiveApplication.currLiveUid
                         + "&liveid="
                         + getuid
                         + "&share_uid="
                         + getuid
                         + "&share_from=qq";

                     if (is_record_play && record_id != null && !record_id.equals("")) {
                        share_target_url = share_target_url.replace("/play?uid", "/play/vdoid?uid");
                        share_target_url = share_target_url + "&vdoid=" + record_id;
                     }

                     shareDialog.setShareUrl(share_target_url);
                     shareDialog.setShareTitle(Utils.trans(R.string.app_name));
                     //内容文字，头像
                     shareDialog.setShareContent(
                             "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + share_target_url,
                             hostMember.getHeadImagePath());
                     shareDialog.doShareToQQ();
                  }
               });

               View btn_sina = share_popupWindow.findId(R.id.btn_sina);
               btn_sina.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     if (hostMember == null) {
                        return;
                     }
                     AULiveApplication auLiveApplication =
                         (AULiveApplication) TinkerManager.getTinkerApplicationLike();
                     ReleaseLiveActivity.doSharePrepare(AULiveApplication.currLiveUid,
                         auLiveApplication.getCity(), "weibo");

                     String getuid = mSelfUserInfo.getUserPhone();
                     String nickname = mSelfUserInfo.getUserName();
                     //分享的内容
                     String share_target_url = "http://web."
                         + UrlHelper.URL_domain
                         + "/play?uid="
                         + AULiveApplication.currLiveUid
                         + "&liveid="
                         + getuid
                         + "&share_uid="
                         + getuid
                         + "&share_from=weibo";

                     if (is_record_play && record_id != null && !record_id.equals("")) {
                        share_target_url = share_target_url.replace("/play?uid", "/play/vdoid?uid");
                        share_target_url = share_target_url + "&vdoid=" + record_id;
                     }

                     shareDialog.setShareUrl(share_target_url);
                     shareDialog.setShareTitle(Utils.trans(R.string.app_name));
                     //内容文字，头像
                     shareDialog.setShareContent(
                             "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + share_target_url,
                             hostMember.getHeadImagePath());

                     shareDialog.doShareToWeiBo();
                  }
               });

               View btn_friendcircle = share_popupWindow.findId(R.id.btn_friendcircle);
               btn_friendcircle.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     if (hostMember == null) {
                        return;
                     }
                     AULiveApplication auLiveApplication =
                         (AULiveApplication) TinkerManager.getTinkerApplicationLike();
                     ReleaseLiveActivity.doSharePrepare(AULiveApplication.currLiveUid,
                         auLiveApplication.getCity(), "friend_circle");
                     String getuid = mSelfUserInfo.getUserPhone();
                     String nickname = mSelfUserInfo.getUserName();
                     //分享的内容
                     String share_target_url = "http://web."
                         + UrlHelper.URL_domain
                         + "/play?uid="
                         + AULiveApplication.currLiveUid
                         + "&liveid="
                         + getuid
                         + "&share_uid="
                         + getuid
                         + "&share_from=friend_circle";

                     if (is_record_play && record_id != null && !record_id.equals("")) {
                        share_target_url = share_target_url.replace("/play?uid", "/play/vdoid?uid");
                        share_target_url = share_target_url + "&vdoid=" + record_id;
                     }

                     shareDialog.setShareUrl(share_target_url);
                     shareDialog.setShareTitle(Utils.trans(R.string.app_name));
                     //内容文字，头像
                     shareDialog.setShareContent(
                             "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + share_target_url,
                             hostMember.getHeadImagePath());
                     shareDialog.doShareToWeiXinFriend();
                  }
               });

               View btn_wechat = share_popupWindow.findId(R.id.btn_wechat);
               btn_wechat.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     if (hostMember == null) {
                        return;
                     }
                     AULiveApplication auLiveApplication =
                         (AULiveApplication) TinkerManager.getTinkerApplicationLike();
                     ReleaseLiveActivity.doSharePrepare(AULiveApplication.currLiveUid,
                         auLiveApplication.getCity(), "friend_circle");

                     String getuid = mSelfUserInfo.getUserPhone();
                     String nickname = mSelfUserInfo.getUserName();
                     //分享的内容
                     String share_target_url = "http://web."
                         + UrlHelper.URL_domain
                         + "/play?uid="
                         + AULiveApplication.currLiveUid
                         + "&liveid="
                         + getuid
                         + "&share_uid="
                         + getuid
                         + "&share_from=weixin";

                     if (is_record_play && record_id != null && !record_id.equals("")) {
                        share_target_url = share_target_url.replace("/play?uid", "/play/vdoid?uid");
                        share_target_url = share_target_url + "&vdoid=" + record_id;
                     }

                     shareDialog.setShareUrl(share_target_url);
                     shareDialog.setShareTitle(Utils.trans(R.string.app_name));
                     //内容文字，头像
                     shareDialog.setShareContent(
                             "我正在看" + hostMember.getUserName() + ",想看精彩的现场秀，就来" + Utils.trans(R.string.app_name) + share_target_url,
                             hostMember.getHeadImagePath());
                     shareDialog.doShareToWeiXin();
                  }
               });
            } else {
               share_popupWindow.showLikeQuickAction(PixelDpHelper.dip2px(AvActivity.this, 65),
                   PixelDpHelper.dip2px(AvActivity.this, -5));
            }
         }
      });

      //底部View
      qav_bottom_bar = findViewById(R.id.qav_bottom_bar);
      img_gift = (ImageView) findViewById(R.id.img_gift);
      img_gift.setOnClickListener(new View.OnClickListener() {

         @Override public void onClick(View view) {
            //当打开礼物时，关闭聊天与底部聊天，礼物等
            //qav_bottom_bar.setVisibility(View.INVISIBLE);

            findViewById(R.id.qav_bottom_input_bar).setVisibility(View.INVISIBLE);
            findViewById(R.id.chat_list_container).setVisibility(View.INVISIBLE);

            if (giftPagerUtil == null) {
               giftpager_popupWindow = new PopupWindowUtil(img_gift);
               giftpager_popupWindow.setContentView(R.layout.room_gift_pager);
               giftpager_popupWindow.setOutsideTouchable(true);
               giftpager_popupWindow.showBottom();
               giftpager_popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                  @Override public void onDismiss() {
                     //qav_bottom_bar.setVisibility(View.VISIBLE);
                     findViewById(R.id.qav_bottom_input_bar).setVisibility(View.VISIBLE);
                     findViewById(R.id.chat_list_container).setVisibility(View.VISIBLE);
                  }
               });

               giftPagerUtil =
                   new GiftPagerUtil(AvActivity.this, giftpager_popupWindow.findId(R.id.root_view),
                       giftpager_popupWindow);
            } else {
               giftPagerUtil.upDataDiamond(AULiveApplication.getUserInfo().diamond);
               giftpager_popupWindow.showBottom();
            }
         }
      });

      //只有是主播时才有送礼物
      if (!is_creater) {
         img_gift.setVisibility(View.VISIBLE);
      } else {
         img_gift.setVisibility(View.GONE);
      }
      //只有是主播时才有管理，切换摄像头
      img_guanli = (ImageView) findViewById(R.id.img_guanli);

      if (is_creater) {
         img_guanli.setVisibility(View.VISIBLE);
         mIv_type.setVisibility(View.VISIBLE);
         if (mIsPay) {
            mIv_type.setBackgroundResource(R.drawable.live_pay);
         } else {
            mIv_type.setBackgroundResource(R.drawable.live_free);
         }
      } else {
         img_guanli.setVisibility(View.GONE);
         mIv_type.setVisibility(View.GONE);
      }

      mIv_type.setOnClickListener(this);

      img_guanli.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {

            if (guanli_popupWindow == null) {
               guanli_popupWindow = new PopupWindowUtil(img_shutdown);
               guanli_popupWindow.setContentView(R.layout.room_setting);
               guanli_popupWindow.setOutsideTouchable(false);
               //guanli_popupWindow.showBottomWithAlpha();

               guanli_popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                  @Override public void onDismiss() {

                  }
               });
               guanli_popupWindow.findId(R.id.btn_switchcamera)
                   .setOnClickListener(new View.OnClickListener() {
                      @Override public void onClick(View view) {
                         onSwitchCamClick();
                      }
                   });
               //更新语音图标
               TextView flash_light = (TextView) guanli_popupWindow.findId(R.id.txt_flashlight);
               ImageView voice_image = (ImageView) guanli_popupWindow.findId(R.id.img_flashlight);
               setSettingPopupIcon(voice_image, flash_light);

               guanli_popupWindow.findId(R.id.btn_flashlight)
                   .setOnClickListener(new View.OnClickListener() {
                      @Override public void onClick(View view) {
                         onFlashClick();
                         mChecked = !mChecked;
                         //更新语音图标
                         TextView flash_light =
                             (TextView) guanli_popupWindow.findId(R.id.txt_flashlight);
                         ImageView voice_image =
                             (ImageView) guanli_popupWindow.findId(R.id.img_flashlight);
                         setSettingPopupIcon(voice_image, flash_light);
                      }
                   });

               guanli_popupWindow.showLikeQuickAction(PixelDpHelper.dip2px(AvActivity.this, 110),
                   PixelDpHelper.dip2px(AvActivity.this, -5));
            } else {
               guanli_popupWindow.showLikeQuickAction(PixelDpHelper.dip2px(AvActivity.this, 110),
                   PixelDpHelper.dip2px(AvActivity.this, -5));
            }
         }
      });

      img_shutdown = (ImageView) findViewById(R.id.img_shutdown);
      img_shutdown.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            //分主播与看客
            if (!is_creater) {
               onMemberExit();
            } else {
               onCloseVideo();
            }
         }
      });

      //聊天相关
      mButtonSendMsg = (Button) findViewById(R.id.qav_bottombar_send_msg);
      mButtonSendMsg.setOnClickListener(this);
      mListViewMsgItems = (ListView) findViewById(R.id.im_msg_items);
      mArrayListChatEntity = new ArrayList<ChatEntity>();
      mChatMsgListAdapter =
          new ChatMsgListAdapter(this, mArrayListChatEntity, mMemberList, mSelfUserInfo);
      mListViewMsgItems.setAdapter(mChatMsgListAdapter);
      if (mListViewMsgItems.getCount() > 1) {
         mListViewMsgItems.setSelection(mListViewMsgItems.getCount() - 1);
      }

      mListViewMsgItems.setOnTouchListener(new View.OnTouchListener() {
         @Override public boolean onTouch(View v, MotionEvent event) {

            //if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //   //接触到ListView移动时
            //   flipRoomHelper.setCanTouch(false);
            //} else if (event.getAction() == MotionEvent.ACTION_UP) {
            //   //离开ListView时
            //   flipRoomHelper.setCanTouch(true);
            //
            //} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //   //接触到ListView时
            //   flipRoomHelper.setCanTouch(false);
            //}

            InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            //做onlyaoutChangeListener处理
            old_screen_location_y = 0;

            if (operate_layout == null) {
               return false;
            }

            operate_layout.setVisibility(View.VISIBLE);
            qav_chat_input_layout.setVisibility(View.GONE);
            findViewById(R.id.place_temp).setVisibility(View.VISIBLE);
            qav_top_bar_new.setVisibility(View.VISIBLE);

            mEditTextInputMsg.setVisibility(View.VISIBLE);

            //抛异常
            //mChatMsgListAdapter.notifyDataSetChanged();
            return false;
         }
      });

      mListViewMsgItems.setOnScrollListener(new AbsListView.OnScrollListener() {
         @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
               case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                  // 手指触屏拉动准备滚动，只触发一次        顺序: 1
                  break;
               case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                  // 持续滚动开始，只触发一次                顺序: 2
                  break;
               case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                  // 整个滚动事件结束，只触发一次            顺序: 4
                  //view.getFirstVisiblePosition() == 0 &&
                  if (!mIsLoading) {
                     //划到顶部不动
                     mIsLoading = true;
                  }
                  break;
            }
         }

         @Override
         public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
             int totalItemCount) {
            // 一直在滚动中，多次触发                          顺序: 3
         }
      });

      //主播发红包
//      Button host_redbag_bt = (Button) findViewById(R.id.host_redbag_bt);
//      if (is_creater) {
//         host_redbag_bt.setVisibility(View.VISIBLE);
//         host_redbag_bt.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//               GiftPagerUtil.doHostSendRedbag(AvActivity.this, 8 + "");
//            }
//         });
//      } else {
//         host_redbag_bt.setVisibility(View.GONE);
//      }

      //个性礼物
      img_custom_gift = (ImageView) findViewById(R.id.img_custom_gift);
      //只有不是主播时才有绘制礼物
      if (!is_creater) {
     //    img_custom_gift.setVisibility(View.VISIBLE);
      } else {
         img_custom_gift.setVisibility(View.GONE);
      }
      img_custom_gift.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View v) {

            if(giveGiftView == null) {
               giveGiftView = View.inflate(AvActivity.this, R.layout.dialog_give_gift, null);
               RelativeLayout root_view = (RelativeLayout) findViewById(R.id.av_screen_layout);
               root_view.addView(giveGiftView);
            }

            if (operateHelper == null) {
               operateHelper = new OperateHelper(AvActivity.this);
            }
            /*<include
                    layout="@layout/dialog_give_gift"
            android:id="@+id/custom_gift_layout"
            android:visibility="gone"
                    />*/

            operateHelper.setOpenPadview(true);
//            View custom_gift_layout = findViewById(R.id.custom_gift_layout);
            giveGiftView.setVisibility(View.VISIBLE);
            ViewAnimator.animate(giveGiftView)
                .translationY(PixelDpHelper.dip2px(AvActivity.this, 1200), 0)
                .duration(600)

                .onStop(new AnimationListener.Stop() {
                   @Override public void onStop() {

                   }
                })
                .start();
         }
      });

      //视频回放相关
      mPlayerPanel = (LinearLayout) findViewById(R.id.player_panel);
      mPlayerStartBtn = (ImageView) findViewById(R.id.player_start);
      mPlayerSeekbar = (SeekBar) findViewById(R.id.player_seekbar);
      mPlayerPosition = (TextView) findViewById(R.id.player_time);

      mHandler_playback = new UIHandler(AvActivity.this);

      mPlayerStartBtn.setOnClickListener(mStartBtnListener);
      mPlayerSeekbar.setOnSeekBarChangeListener(mSeekBarListener);
      mPlayerSeekbar.setEnabled(true);
   }

   private OperateHelper operateHelper;

   //关闭键盘
   private void doCloseKeyBoard(View v) {

      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
      //做onlyaoutChangeListener处理
      old_screen_location_y = 0;

      operate_layout.setVisibility(View.VISIBLE);
      qav_chat_input_layout.setVisibility(View.GONE);
      findViewById(R.id.place_temp).setVisibility(View.VISIBLE);
      qav_top_bar_new.setVisibility(View.VISIBLE);

      mEditTextInputMsg.setVisibility(View.VISIBLE);

      //抛异常
      //mChatMsgListAdapter.notifyDataSetChanged();
   }

   @Override public boolean onTouch(View v, MotionEvent event) {
      if (mListViewMsgItems == null) {
         return false;
      }
      //抖动的原因 getX getY获取的是相对于child 左上角点的 x y
      // 正确的做法是event.getRawX getRawY 获取相对于屏幕的x y
      MotionEvent eventClone = MotionEvent.obtain(event);
      eventClone.setLocation(event.getRawX(), event.getRawY());

      //连麦时不能划屏
//      if (!rtcHelper.is_linkingRtc) {
//         flipRoomHelper.mWatchSwitchViewPager.dispatchTouchEvent(eventClone);
//      }
      try {
         if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 取得左右滑动时手指按下的X坐标
            touchDownX = event.getX();
            DownX = event.getRawX();
            DownY = event.getRawY();
            return true;
         } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // 取得左右滑动时手指松开的X坐标
            touchUpX = event.getX();
            float UpX = event.getRawX();
            float UpY = event.getRawY();
            // 从左往右，看前一个View
            float dx = UpX - DownX;
            float dy = UpY - DownY;
            if (Math.abs(dy) >= Math.abs(dx)) {
               doLikeClick();
               return true;
            } else {
               Trace.d("****>>>>是左右滑动");
            }
            if (touchUpX - touchDownX > 200) {
               if (viewFlipper.getDisplayedChild() != 0) {
                  if (!is_creater) {
                     closeHeartShow();
                  }
                  // 显示上一屏动画
                  viewFlipper.setInAnimation(
                      AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                  viewFlipper.setOutAnimation(
                      AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                  // 显示上一屏的View
                  //viewFlipper.showPrevious();
                  viewFlipper.setDisplayedChild(0);
               }
               // 从右往左，看后一个View
            } else if (touchDownX - touchUpX > 200) {
               if (viewFlipper.getDisplayedChild() != 1) {
                  //显示前开启心动态
                  if (!is_creater) {
                     startHeartShow();
                  }
                  //显示下一屏的动画
                  viewFlipper.setInAnimation(
                      AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                  viewFlipper.setOutAnimation(
                      AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                  // 显示下一屏的View
                  //viewFlipper.showNext();
                  viewFlipper.setDisplayedChild(1);
               }
            } else {
               //普通点击 代替setonclickListener
               doLikeClick();
               //取消，不需要隐藏回放按钮
               //dealTouchEvent(v, event);
            }
            return true;
         }
      } catch (Exception e) {
      }
      return true;
   }

   //点亮与关闭输入法xgx
   public void doLikeClick() {

      //当不是聊天状态时
      if (operate_layout.getVisibility() == View.VISIBLE) {
         if (mHeartLayout != null) {
            //mHeartLayout.addHeart(randomColor());
            mHeartLayout.addHeart2(HeartLayout.pics_like[new Random().nextInt(3)]);
            try {
               int mygrade = Integer.parseInt(AULiveApplication.getUserInfo().getGrade());
               //发送点赞,一个直播间只发送一次
               if (!has_send_like && mygrade >= php_control_msg_grade) {
                  LikeUserEntity barrageEntity = new LikeUserEntity();
                  barrageEntity.uid = AULiveApplication.getUserInfo().getUid();
                  barrageEntity.nickname = AULiveApplication.getUserInfo().getNickname();
                  barrageEntity.face = AULiveApplication.getUserInfo().getFace();
                  barrageEntity.type = LOVE_ANCHOR;
                  barrageEntity.grade = AULiveApplication.getUserInfo().getGrade();
                  barrageEntity.love_pos = "1";
                  barrageEntity.offical = getIntent().getIntExtra(EXTRA_IS_SUPER_MANAGER, 0);
                  //barrageEntity.anchor_medal = medalListEvent.anchor_medal;
                  barrageEntity.wanjia_medal = medalListEvent.wanjia_medal;
                  final String msg1 = JsonParser.serializeToJson(barrageEntity);

                  CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
                  custom_msg.type = LOVE_ANCHOR;
                  custom_msg.data = msg1;
                  doSendContent(custom_msg);
                  has_send_like = true;
               }
            } catch (Exception e) {
            }
         }
      }

      operate_layout.setVisibility(View.VISIBLE);
      qav_chat_input_layout.setVisibility(View.GONE);
      findViewById(R.id.place_temp).setVisibility(View.VISIBLE);
      qav_top_bar_new.setVisibility(View.VISIBLE);
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(qav_top_bar_new.getWindowToken(), 0);
      //做onlyaoutChangeListener处理
      old_screen_location_y = 0;
   }

   private boolean mChecked = false;

   public void setSettingPopupIcon(ImageView voice_image, TextView flash_light) {
      if (mChecked) {
         voice_image.setImageDrawable(
             AvActivity.this.getResources().getDrawable(R.drawable.room_pop_up_lamp_p));
         flash_light.setText("开");
      } else {
         //guan
         flash_light.setText("关");
         voice_image.setImageDrawable(
             AvActivity.this.getResources().getDrawable(R.drawable.room_pop_up_lamp));
      }
   }

   //当键盘收起或隐藏时的处理
   private void  doKeyBoardListner() {
      if (mEditTextInputMsg == null) {
         return;
      }
      //做键盘监听
      screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
      //阀值设置为屏幕高度的1/3
      keyHeight = screenHeight / 3;
      mEditTextInputMsg.getLocationOnScreen(screen_location);
      old_screen_location_y = screen_location[1];

      mEditTextInputMsg.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
         @Override
         public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
             int oldTop, int oldRight, int oldBottom) {
            mEditTextInputMsg.getLocationOnScreen(screen_location);
            int new_screen_location_y = screen_location[1];

            //Trace.d("new_screen_location_y:old_screen_location_y:y-oy,"
            //    + new_screen_location_y
            //    + ":"
            //    + old_screen_location_y
            //    + ":"
            //    + (new_screen_location_y - old_screen_location_y));
            //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
            if (new_screen_location_y != 0 && old_screen_location_y != 0 && (old_screen_location_y
                - new_screen_location_y > keyHeight)) {
               //监听到软键盘弹起

            } else if (new_screen_location_y != 0 && old_screen_location_y != 0 &&
                new_screen_location_y > old_screen_location_y && (new_screen_location_y
                - old_screen_location_y > keyHeight)) {
               //监听到软件盘关闭...

               operate_layout.setVisibility(View.VISIBLE);
               qav_chat_input_layout.setVisibility(View.GONE);
               findViewById(R.id.place_temp).setVisibility(View.VISIBLE);

               qav_top_bar_new.setVisibility(View.VISIBLE);
            }
            //更新old_screen_location_y
            old_screen_location_y = new_screen_location_y;
         }
      });
   }

   private void startHeartShow() {
      Trace.d("startHeartShow");
      if (mHeartLayout == null && mHeartLayout == null) {
         mHeartLayout = (HeartLayout) findViewById(R.id.heart_layout);
         mHeartTimer = new Timer();
         mHeartTimer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
               if (mHeartLayout != null) {
                  mHeartLayout.post(new Runnable() {
                     @Override public void run() {
                        if (mHeartLayout != null) {
                           //mHeartLayout.addHeart(randomColor());
                           //新的图片方法
                           mHeartLayout.addHeart2(HeartLayout.pics_other[new Random().nextInt(7)]);
                        }
                     }
                  });
               }
            }
         }, 700, 500);
      }
   }

   private void closeHeartShow() {
      Trace.d("closeHeartShow");
      if (mHeartTimer != null && mHeartLayout != null) {
         mHeartTimer.cancel();
         mHeartTimer.purge();
         mHeartTimer = null;
         mHeartLayout.clearAnimation();
         mHeartLayout = null;
      }
   }

   private void startChatClean() {
      mChatTimer = new Timer(true);
      mChatTimerTask = new ChatTimerTask();
      mChatTimer.schedule(mChatTimerTask, 8000, 5000);
   }

   private void closeChatClean() {
      if (mChatTimer != null) {
         mChatTimer.cancel();
         mChatTimer = null;
      }
   }

   @Override public void onClick(View v) {
      switch (v.getId()) {
         case R.id.qav_bottombar_send_msg:
            onSendInputMsg();
            break;
         case R.id.last_page:
            currentPage--;
            if (currentPage == 0) {
               currPage = 1;
               com.ilikezhibo.ggzb.tool.Utils.showCenterMessage("没有更多数据了");
               return;
            }
            getOperateData();
            break;
         case R.id.next_page:
            currentPage++;
            getOperateData();
            break;
         case R.id.live_type:
            changeLiveType();
            break;
         default:
            break;
      }
   }

   private void changeLiveType() {
      RequestInformation request = null;
      StringBuilder sb = null;
      if (mIsPay) {
         sb = new StringBuilder(UrlHelper.PAY_CLOSE + AULiveApplication.currLiveUid);
      } else {
         sb = new StringBuilder(UrlHelper.PAY_OPEN + AULiveApplication.currLiveUid);
      }
      request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      request.setCallback(new JsonCallback<ChangeLiveType>() {
         @Override
         public void onCallback(ChangeLiveType callback) {
            if (callback.getStat() == 200) {
               com.jack.utils.Utils.showCroutonText(AvActivity.this, "修改成功");
               mIsPay = !mIsPay;
               if (mIsPay) {
                  mIv_type.setBackgroundResource(R.drawable.live_pay);
               } else {
                  mIv_type.setBackgroundResource(R.drawable.live_free);
               }
            } else {
               Utils.showCenterMessage(Utils.trans(R.string.system_busy));
            }
         }
         @Override
         public void onFailure(AppException e) {
            com.jack.utils.Utils.showCenterMessage(Utils.trans(R.string.net_fail));
         }
      }.setReturnType(ChangeLiveType.class));
      request.execute();
   }

   //回复
   public void replyTo(String nickname) {
      if (qav_chat_input_layout.getVisibility() == View.GONE) {
         operate_layout.setVisibility(View.GONE);
         qav_chat_input_layout.setVisibility(View.VISIBLE);
         findViewById(R.id.place_temp).setVisibility(View.GONE);
         qav_top_bar_new.setVisibility(View.GONE);

         mEditTextInputMsg.setFocusable(true);
         mEditTextInputMsg.setFocusableInTouchMode(true);
         mEditTextInputMsg.requestFocus();
         mEditTextInputMsg.setText("@" + nickname + " ");
         InputMethodManager imm =
             (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
         imm.showSoftInput(mEditTextInputMsg, 0);
         //光标最后
         mEditTextInputMsg.setSelection(mEditTextInputMsg.getText().length());
      }
   }

   private long vdoid_time;

   //主播关闭房间
   private void closeLive() {
      String title = getIntent().getExtras().getString(EXTRA_RECORD_TITLE_KEY, "");
      RequestInformation request = null;
      //秒
      long duration_time = (System.currentTimeMillis() - host_duration_time) / 1000;
      try {
         StringBuilder sb = new StringBuilder(UrlHelper.liveCloseUrl
             + "?roomid="
             + AULiveApplication.currLiveUid
             + "&userid="
             + mSelfUserInfo.getUserPhone()
             + "&liveuid="
             + AULiveApplication.currLiveUid
             + "&visitor="
             + view_count
             + "&zan="
             + praise_count
             + "&time="
             + duration_time
             + "&vdoid="
             + vdoid_time
             + "&title="
             + title);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            if (callback == null) {
               Utils.showCroutonText(AvActivity.this, Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {

            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showCroutonText(AvActivity.this, Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   //退出房间
   private void leaveLive() {
      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(UrlHelper.closeLiveUrl
             + "?roomid="
             + AULiveApplication.currLiveUid
             + "&userid="
             + mSelfUserInfo.getUserPhone()
             + "&liveuid="
             + AULiveApplication.currLiveUid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {
            if (callback == null) {
               Utils.showCroutonText(AvActivity.this, Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               //Utils.showMessage("成功退出房间");
            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showCroutonText(AvActivity.this, Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   //心跳
   public void doHeartBeat() {

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(UrlHelper.SERVER_URL + "live/ping");
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<RoomNumEntity>() {

         @Override public void onCallback(RoomNumEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               Trace.d("heart beat ok");
            } else {
               Utils.showCroutonText(AvActivity.this, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(RoomNumEntity.class));
      request.execute();
   }

   //弹幕
   public void doDanmu(final String memo) {

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(UrlHelper.liveAddPraiseUrl
             + "?memo="
             + URLEncoder.encode(memo, "utf-8")
             + "&liveuid="
             + AULiveApplication.currLiveUid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<DanmuEntity>() {

         @Override public void onCallback(DanmuEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               UserBarrageEntity barrageEntity = new UserBarrageEntity();
               barrageEntity.uid = AULiveApplication.getUserInfo().getUid();
               barrageEntity.nickname = AULiveApplication.getUserInfo().getNickname();
               barrageEntity.face = AULiveApplication.getUserInfo().getFace();
               barrageEntity.chat_msg = memo;
               barrageEntity.grade = AULiveApplication.getUserInfo().getGrade();
               barrageEntity.type = BARRAGE_USER;
               barrageEntity.offical = is_super_manager ? 1 : 0;
               //barrageEntity.anchor_medal = medalListEvent.anchor_medal;
               barrageEntity.wanjia_medal = medalListEvent.wanjia_medal;

               final String msg1 = JsonParser.serializeToJson(barrageEntity);

               CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
               custom_msg.type = BARRAGE_USER;
               custom_msg.data = msg1;
               doSendContent(custom_msg);

               //实时更新钻石数量
               AULiveApplication.getUserInfo().diamond = callback.getDiamond();

            } else if (callback.getStat() == 520) {
               //关闭前先关输入法，不然主播会出现半屏现象
               doCloseKeyBoard(mListViewMsgItems);

               mListViewMsgItems.postDelayed(new Runnable() {
                  @Override public void run() {
                     Intent moeny_intent = new Intent(AvActivity.this, BuyDiamondActivity.class);
                     AvActivity.this.startActivity(moeny_intent);
//                     Intent intent4 =
//                             new Intent(AvActivity.this, UserInfoWebViewActivity.class);
//                     intent4.putExtra(WebViewActivity.input_url,
//                             UrlHelper.SERVER_URL + "profile/h5charge");
//                     intent4.putExtra(WebViewActivity.back_home_key, false);
//                     intent4.putExtra(WebViewActivity.actity_name, "充值");
//                     AvActivity.this.startActivity(intent4);
                  }
               }, 200);
            } else {
               Utils.showCroutonText(AvActivity.this, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(DanmuEntity.class));
      request.execute();
   }

   //脏词过滤表
   String[] bad_words = {
       "漏点", "脱光", "奶子", "脱了", "大奶", "揉起来", "咪咪", "大秀", "掏出来", "jb", "乳沟", "看腿", "漏奶", "漏胸", "看胸",
       "看逼", "乳头", "内内", "内库", "套路", "闪现", "脱吗", "脱掉", "操你", "艹你", "漏奶", "内裤", "鸡巴", "鸡吧", "幼女"
   };

   //5秒只能发一次
   private static long mLastShowTime = 0;

   //填写的内容
   private void onSendInputMsg() {

      if (mEditTextInputMsg == null) {
         return;
      }
      String msg = mEditTextInputMsg.getText().toString();
      if (msg == null || msg.equals("")) {
         return;
      }
      if (msg.length() > 40) {
         Utils.showCroutonText(AvActivity.this, "你输入的内容过长");
         return;
      }

      //5秒只能发一次
      long time = System.currentTimeMillis();
      long timeD = time - mLastShowTime;
      if (0 < timeD && timeD < 5000) {
         Utils.showMessage("你发信息有点快噢");
         return;
      }

      //过滤脏词
      for (int bw = 0; bw < bad_words.length; bw++) {
         String bad = bad_words[bw];
         msg = msg.replace(bad, "**");
      }

      //第2重过滤
      FilteredResult res= WordFilterUtil.filterText(msg, '*');
      msg=res.getFilteredContent().toString();
      //清空
      mEditTextInputMsg.setText("");

      //else
      {
         //普通信息

         if (msg.length() > 0) {
            //判断是否被禁言
            if (is_gag) {
               Utils.showCroutonText(AvActivity.this, "你已被禁言,不能发送消息");
               return;
            }

            try {
               byte[] byte_num = msg.getBytes("utf8");
               if (byte_num.length > 1600) {
                  Utils.showCroutonText(AvActivity.this, "你输入的内容过长");
                  return;
               }
            } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
               return;
            }

            /////////////////////////////////////////////////////////////////////////
            try {
               //判断广告
               String clipBoardText = getClipboardText();
               if (clipBoardText != null && !clipBoardText.equals("")) {
                  if (clipBoardText.equals(msg)) {
                     Trace.d("clipBoardText.equals(msg)");
                     AULiveApplication.adv_count++;
                     //3次以上是广告及5级以下
                     int grade = Integer.parseInt(AULiveApplication.getUserInfo().getGrade());
                     if (AULiveApplication.adv_count > 3 && grade < 6) {
                        doSendLocalMsg(msg);
                        return;
                     }
                  }
               }
            } catch (Exception e) {
            }

            if (isAdv(msg)) {
               Trace.d("发现微信号广告");
               doSendLocalMsg(msg);
               //给服务器发送一次验证及禁言
               sendAdvWordsToSever(msg);
               return;
            }

/////////////////////////////////////////////////////////////////////////
            ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
            chatMsgEntity.chat_msg = msg;
            chatMsgEntity.face = mSelfUserInfo.getHeadImagePath();
            chatMsgEntity.nickname = mSelfUserInfo.getUserName();
            chatMsgEntity.type = MSG_COETENT_ROOM;
            chatMsgEntity.grade = AULiveApplication.getUserInfo().getGrade();
            chatMsgEntity.uid = mSelfUserInfo.getUserPhone();
            chatMsgEntity.offical = getIntent().getIntExtra(EXTRA_IS_SUPER_MANAGER, 0);
            //chatMsgEntity.anchor_medal=medalListEvent.anchor_medal;
            chatMsgEntity.wanjia_medal = medalListEvent.wanjia_medal;
            final String msg1 = JsonParser.serializeToJson(chatMsgEntity);

            CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
            custom_msg.type = MSG_COETENT_ROOM;
            custom_msg.data = msg1;
            doSendContent(custom_msg);
         }
      }

      if (danmu_checkbox.isChecked()) {
         //弹幕消息

         doDanmu(msg);
      }

      mLastShowTime = time;
   }

   private void doSendLocalMsg(String msg) {
      //Utils.showMessage("别发广告了");
      //只更新给自己看，不广播出去
      //更新公聊
      ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
      system_ChatMsgEntity.type = "chat";
      system_ChatMsgEntity.grade = AULiveApplication.getUserInfo().getGrade();
      system_ChatMsgEntity.nickname = AULiveApplication.getUserInfo().getNickname();
      system_ChatMsgEntity.uid = AULiveApplication.getUserInfo().getUid();
      system_ChatMsgEntity.face = AULiveApplication.getUserInfo().getFace();
      system_ChatMsgEntity.chat_msg = msg;
      system_ChatMsgEntity.offical = getIntent().getIntExtra(EXTRA_IS_SUPER_MANAGER, 0);
      //system_ChatMsgEntity.anchor_medal=medalListEvent.anchor_medal;
      system_ChatMsgEntity.wanjia_medal = medalListEvent.wanjia_medal;
      //添加系统消息提醒
      ChatEntity gif_entity = new ChatEntity();
      gif_entity.setChatMsgEntity(system_ChatMsgEntity);
      gif_entity.setTime(System.currentTimeMillis() / 1000);
      mArrayListChatEntity.add(gif_entity);
      updateChatListView();
   }

   //判断是否you微信号
   public static boolean isAdv(String stradv) {
      String strPattern = "^.{0,}[a-zA-Z\\d_]{5,}.{0,}$";
      Pattern p = Pattern.compile(strPattern);
      Matcher m = p.matcher(stradv);
      return m.matches();
   }

   //弹幕
   public void sendAdvWordsToSever(final String memo) {

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(
             UrlHelper.SERVER_URL + "other/filtermsg?word=" + URLEncoder.encode(memo, "utf-8"));
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<RoomNumEntity>() {

         @Override public void onCallback(RoomNumEntity callback) {
            if (callback == null) {
               //Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               Trace.d("sendAdvWordsToSever 200");
            }
         }

         @Override public void onFailure(AppException e) {
            //Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(RoomNumEntity.class));
      request.execute();
   }

   private String getClipboardText() {
      try {
         ClipboardManager clipboardManager =
             (ClipboardManager) AULiveApplication.mContext.getSystemService(
                 Context.CLIPBOARD_SERVICE);
         if (clipboardManager.hasPrimaryClip()) {
            return clipboardManager.getPrimaryClip().getItemAt(0).getText() + "";
         }
      } catch (Exception e) {
      }
      return null;
   }

   private long last_send_msg_time;
   //处理所有的发送内容
   ArrayList<CustomizeChatRoomMessage> messageList_arraylist;

   private void doSendContent(CustomizeChatRoomMessage msg) {
      //礼物,分开发,优先级比较高
      if (msg.type != null && msg.type.equals(MSG_GIFT_ROOM)) {
         if (RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance()
                .getRongIMClient()
                .sendMessage(Conversation.ConversationType.CHATROOM, AULiveApplication.currLiveUid,
                    msg, "", "", new RongIMClient.SendMessageCallback() {
                       @Override public void onError(Integer messageId, RongIMClient.ErrorCode e) {

                       }

                       @Override public void onSuccess(Integer integer) {
                          //System.gc();
                       }
                    }, new RongIMClient.ResultCallback<Message>() {
                       @Override public void onError(RongIMClient.ErrorCode errorCode) {

                       }

                       @Override public void onSuccess(Message message) {
                          //加入到自己的chat listview

                       }
                    });
         }

         return;
      }
      //新数据
      if (System.currentTimeMillis() - last_send_msg_time > 200) {
         //有新数据进来要创建新的数列
         last_send_msg_time = System.currentTimeMillis();
         messageList_arraylist = new ArrayList<CustomizeChatRoomMessage>();
         messageList_arraylist.add(msg);
         new Timer().schedule(new TimerTask() {
            @Override public void run() {
               /**
                * 发送普通消息
                *
                * @param conversationType      会话类型
                * @param targetId              会话ID
                * @param content               消息的内容，一般是MessageContent的子类对象
                * @param pushContent           接收方离线时需要显示的push消息内容
                * @param pushData              接收方离线时需要在push消息中携带的非显示内容
                * @param SendMessageCallback   发送消息的回调
                * @param ResultCallback        消息存库的回调，可用于获取消息实体
                *
                */
               if (messageList_arraylist == null || messageList_arraylist.size() < 1) {
                  return;
               }

               //创临时数据，避免serializeToJson时改变messageList_arraylist,抛异常
               ArrayList<CustomizeChatRoomMessage> tem_list =
                   new ArrayList<CustomizeChatRoomMessage>();
               tem_list.addAll(messageList_arraylist);

               CustomizeChatRoomMessageList tem_messageList = new CustomizeChatRoomMessageList();
               tem_messageList.setList(tem_list);

               CustomizeMsgQueueMessage tem_msg_queue = new CustomizeMsgQueueMessage();
               tem_msg_queue.type = "MsgQueue";
               tem_msg_queue.data = JsonParser.serializeToJson(tem_messageList);

               if (RongIM.getInstance().getRongIMClient() != null) {
                  RongIM.getInstance()
                      .getRongIMClient()
                      .sendMessage(Conversation.ConversationType.CHATROOM,
                          AULiveApplication.currLiveUid, tem_msg_queue, "", "",
                          new RongIMClient.SendMessageCallback() {
                             @Override
                             public void onError(Integer messageId, RongIMClient.ErrorCode e) {

                             }

                             @Override public void onSuccess(Integer integer) {
                                //System.gc();
                             }
                          }, new RongIMClient.ResultCallback<Message>() {
                             @Override public void onError(RongIMClient.ErrorCode errorCode) {

                             }

                             @Override public void onSuccess(Message message) {
                                //加入到自己的chat listview

                             }
                          });
               }
            }
         }, 200);
      } else {
         //添加到已有消息队列
         messageList_arraylist.add(msg);
      }
   }

   /////////////////////////
   //管理员相关
   public boolean is_gag = false;
   public boolean is_manager = false;

   //缓存被禁言的用户uid
   public ArrayList<String> gag_list = new ArrayList<String>();

   public void sendManagerAndGagMsg(final LoginUserEntity userinfo, String type) {
      GagUserEntity gagUserEntity = new GagUserEntity();
      if (userinfo == null || userinfo.getUid() == null || userinfo.getUid().equals("")) {
         return;
      }
      gagUserEntity.type = type;
      gagUserEntity.uid = userinfo.getUid();
      gagUserEntity.nickname = userinfo.getNickname();
      gagUserEntity.send_name = AULiveApplication.getUserInfo().getNickname();
      String tem_msg = "";
      String tem_system_msg = "";
      if (type.equals("gag")) {
         tem_msg = JsonParser.serializeToJson(gagUserEntity);
         tem_system_msg = userinfo.getNickname() + "被管理员禁言";
         if (!gag_list.contains(userinfo.getUid())) {
            gag_list.add(userinfo.getUid());
         }
      } else if (type.equals("manager")) {
         tem_msg = JsonParser.serializeToJson(gagUserEntity);
         tem_system_msg = userinfo.getNickname() + "被设置为管理员身份";
      } else if (type.equals("remove_manager")) {
         tem_msg = JsonParser.serializeToJson(gagUserEntity);
         tem_system_msg = userinfo.getNickname() + "被取消了管理员身份";
      } else if (type.equals("remove_gag")) {
         tem_msg = JsonParser.serializeToJson(gagUserEntity);
         tem_system_msg = userinfo.getNickname() + "被管理员解除禁言";
         if (gag_list.contains(userinfo.getUid())) {
            gag_list.remove(userinfo.getUid());
         }
      }

      CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
      custom_msg.type = type;
      custom_msg.data = tem_msg;
      doSendContent(custom_msg);

      ////chatlistview的内容
      //final String system_msg = tem_system_msg;
      ////发系统广播消息
      //sendSystemMsg(system_msg);
   }

   //发系统消息
   public void sendSystemMsg(String msg_type, String msg1) {
      SystemMsgEntity systemMsgEntity = new SystemMsgEntity();

      if (msg1 == null || msg1.equals("")) {
         return;
      }

      systemMsgEntity.type = msg_type;
      systemMsgEntity.msg = msg1;
      String tem_msg = "";
      tem_msg = JsonParser.serializeToJson(systemMsgEntity);

      CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
      custom_msg.type = msg_type;
      custom_msg.data = tem_msg;

      doSendContent(custom_msg);
   }

   public void sendSystemMsg(String msg_type, String msg1, int alter) {
      SystemMsgEntity systemMsgEntity = new SystemMsgEntity();

      if (msg1 == null || msg1.equals("")) {
         return;
      }

      systemMsgEntity.type = msg_type;
      systemMsgEntity.system_content = msg1;
      systemMsgEntity.alert = alter;
      String tem_msg = "";
      tem_msg = JsonParser.serializeToJson(systemMsgEntity);

      CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
      custom_msg.type = msg_type;
      custom_msg.data = tem_msg;

      doSendContent(custom_msg);
   }

   //发关注消息
   public void sendAttenMsg() {
      LikeUserEntity attentEntity = new LikeUserEntity();
      attentEntity.uid = AULiveApplication.getUserInfo().getUid();
      attentEntity.nickname = AULiveApplication.getUserInfo().getNickname();
      attentEntity.face = AULiveApplication.getUserInfo().getFace();
      attentEntity.type = ATTENT_USER_ROOM;
      attentEntity.offical = getIntent().getIntExtra(EXTRA_IS_SUPER_MANAGER, 0);
      //barrageEntity.anchor_medal = medalListEvent.anchor_medal;
      attentEntity.wanjia_medal = medalListEvent.wanjia_medal;
      attentEntity.grade = AULiveApplication.getUserInfo().getGrade();
      final String msg1 = JsonParser.serializeToJson(attentEntity);

      CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
      custom_msg.type = ATTENT_USER_ROOM;
      custom_msg.data = msg1;
      doSendContent(custom_msg);
   }

   //提示消息Dialog
   private void showPromptDialog(String msg) {
      final CustomDialog customDialog =
          new CustomDialog(AvActivity.this, new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                }
             }
          });

      customDialog.setCustomMessage("" + msg);
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.SINGLE_BTN);
      customDialog.show();
   }

   //看客重连
   public void doPlayerReconnect() {
      if (ksyMediaPlayer == null || video_is_closed) {
         return;
      }
      //View.GONE 销毁mVideoSurfaceView
      AvActivity.this.findViewById(R.id.loading_bg).setVisibility(View.VISIBLE);
      Trace.d(">>>>ksymediaplayer.pause-release-reset");
      ksyMediaPlayer.pause();
      ksyMediaPlayer.release();
      ksyMediaPlayer.reset();
      initVideoPlayer();
      ksyMediaPlayer.start();
      mVideoSurfaceView.setVisibility(View.GONE);
      mVideoSurfaceView.setVisibility(View.VISIBLE);
   }

   ////////////////////////////////////////////////

   //新自定义消息类型
   public static final String LIVE_FEE = "live_fee"; // 观看欠费
   public static final String DIAMOND_NOTIFY = "live_notify";//付费房间钻石更新
   public static final String PAY_OPEN = "live_notify_payopen";//开启付费
   public static final String PAY_CLOSE = "live_notify_payclose";//关闭付费
   public static final String SYSTEM_NOTICE_ROOM = "system"; // 系统消息
   public static final String ENTER_ROOM = "enter";// 进入房间
   private static final String EXIT_ROOM = "exit";// 退出房间
   private static final String MSG_COETENT_ROOM = "chat"; // 聊天内容
   private static final String MSG_GIFT_ROOM = "gift"; // 送礼物
   private static final String ATTENT_USER_ROOM = "attent"; // 关注用户
   private static final String UPGRADE_USER_ROOM = "upgrade"; // 用户升级
   private static final String LOVE_ANCHOR = "love"; // 点亮
   private static final String BARRAGE_USER = "barrage";// 用户弹幕
   private static final String GAG_USER = "gag";// 禁言用户
   private static final String REMOVE_GAG_USER = "remove_gag"; // 解除禁言
   private static final String MANAGER_ROOM = "manager"; // 设置管理员
   private static final String REMOVE_MANAGER_ROOM = "remove_manager"; // 移除管理员
   //private static final int START_LIVE = 116;// 开始直播
   //private static final int SUPER_CLOSE_LIVE = 117;// 管理员关闭房间
   public static final String SHARE_ROOM = "share";//分享成功消息

   private static final String ANCHOR_LEAVE = "anchor_leave";//主播离开
   private static final String ANCHOR_RESTORE = "anchor_restore";//主播回来
   private static final String ANCHOR_EXIT = "anchor_exit";//主播关闭

   private int view_count = 0;
   private int praise_count = 0;
   private int gift_price_count = 0;

   private void handleCustomMsg(CustomizeChatRoomMessage message) {
      if (mListViewMsgItems == null
          || AvActivity.this == null
          || AvActivity.this.findViewById(R.id.host_is_leave_ly) == null) {
         return;
      }
      try {
         //消息类型
         String type = message.type;
         String customText = message.data;
         Trace.d("handleCustomMsg type:" + type + " content:" + customText);

         if (type.equals(ANCHOR_EXIT)) {
            LeaveEntity leaveEntity = JsonParser.deserializeByJson(customText, LeaveEntity.class);
            view_count = leaveEntity.audience_count;
            praise_count = leaveEntity.praise_count;
            Trace.d("****>>>>在这里发现anchor_exit");
            videoIsClosed();
         } else if (type.equals(SHARE_ROOM)) {
            SystemMsgEntity smeEntity =
                JsonParser.deserializeByJson(customText, SystemMsgEntity.class);
            //分享成功消息
            ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
            system_ChatMsgEntity.type = "system";
            system_ChatMsgEntity.nickname = "系统消息";
            system_ChatMsgEntity.chat_msg = smeEntity.msg;
            //添加系统消息提醒
            ChatEntity gif_entity = new ChatEntity();
            gif_entity.setChatMsgEntity(system_ChatMsgEntity);
            gif_entity.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(gif_entity);
            updateChatListView();
         } else if (type.equals(ANCHOR_LEAVE)) {
            //主播离开
            SystemMsgEntity smeEntity =
                JsonParser.deserializeByJson(customText, SystemMsgEntity.class);

            //不是主播，做界面显示修改，提示主播离开了
            if (!is_creater) {
               //处理，主播关闭后，还传来ANCHOR_LEAVE，
               // 主播关闭主播间后，不显示host_is_leave_ly，qav_bottom_bar==visible即还在看直播。
//               if (findViewById(R.id.qav_bottom_bar).getVisibility() == View.VISIBLE) {
//                  AvActivity.this.findViewById(R.id.host_is_leave_ly).setVisibility(View.VISIBLE);
//                  //防，可以乱的后面
//                  AvActivity.this.findViewById(R.id.host_is_leave_ly)
//                      .setOnClickListener(new View.OnClickListener() {
//                         @Override public void onClick(View view) {
//
//                         }
//                      });
//                  Button host_is_leave_bt =
//                      (Button) AvActivity.this.findViewById(R.id.bt_host_is_leave);
//                  host_is_leave_bt.setText("主播离开一下，马上回来");
//               }

//               ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
//               system_ChatMsgEntity.type = "system";
//               system_ChatMsgEntity.nickname = "系统消息";
//               system_ChatMsgEntity.chat_msg = "主播离开一下，马上回来";
//               //添加系统消息提醒
//               ChatEntity gif_entity = new ChatEntity();
//               gif_entity.setChatMsgEntity(system_ChatMsgEntity);
//               gif_entity.setTime(System.currentTimeMillis() / 1000);
//               mArrayListChatEntity.add(gif_entity);
//               updateChatListView();
            }

            ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
            system_ChatMsgEntity.type = "system";
            system_ChatMsgEntity.nickname = "系统消息";
            system_ChatMsgEntity.chat_msg = smeEntity.msg;
            //添加系统消息提醒
            ChatEntity gif_entity = new ChatEntity();
            gif_entity.setChatMsgEntity(system_ChatMsgEntity);
            gif_entity.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(gif_entity);
            updateChatListView();
         } else if (type.equals(ANCHOR_RESTORE)) {
            //主播回来
            SystemMsgEntity smeEntity =
                JsonParser.deserializeByJson(customText, SystemMsgEntity.class);

            //做界面显示修改，做重连
            if (!is_creater) {
               //自己不时主播，而主播进来了，重连palyer
               Trace.d("主播进来了，重连");

               //加入连麦,主播即使home键,还是会继续推流的,不需要重连
               doPlayerReconnect();
//               Button host_is_leave_bt =
//                   (Button) AvActivity.this.findViewById(R.id.bt_host_is_leave);
//               host_is_leave_bt.setText("主播回来了，加载中");
//
//               host_is_leave_bt.postDelayed(new Runnable() {
//                  @Override public void run() {
//                     try {
//                        AvActivity.this.findViewById(R.id.host_is_leave_ly)
//                            .setVisibility(View.GONE);
//                     } catch (Exception e) {
//
//                     }
//                  }
//               }, 2000);

//               ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
//               system_ChatMsgEntity.type = "system";
//               system_ChatMsgEntity.nickname = "系统消息";
//               system_ChatMsgEntity.chat_msg = "主播回来了！正在重联...";
//               //添加系统消息提醒
//               ChatEntity gif_entity = new ChatEntity();
//               gif_entity.setChatMsgEntity(system_ChatMsgEntity);
//               gif_entity.setTime(System.currentTimeMillis() / 1000);
//               mArrayListChatEntity.add(gif_entity);
//               updateChatListView();
               //return;
            }

            ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
            system_ChatMsgEntity.type = "system";
            system_ChatMsgEntity.nickname = "系统消息";
            system_ChatMsgEntity.chat_msg = smeEntity.msg;
            //添加系统消息提醒
            ChatEntity gif_entity = new ChatEntity();
            gif_entity.setChatMsgEntity(system_ChatMsgEntity);
            gif_entity.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(gif_entity);
            updateChatListView();
         } else if (type.equals(SYSTEM_NOTICE_ROOM)) {
            // 系统消息
            SystemMsgEntity smeEntity =
                JsonParser.deserializeByJson(customText, SystemMsgEntity.class);

            if (smeEntity.type != null
                && smeEntity.system_content != null
                && smeEntity.target != null
                && smeEntity.sender != null) {
               //小喇叭
               XiaoLaBaEntity xiaoLaBaEntity = new XiaoLaBaEntity();
               xiaoLaBaEntity.system_content = smeEntity.system_content;
               xiaoLaBaEntity.sender = smeEntity.sender;
               xiaoLaBaEntity.target = smeEntity.target;

               xiaoLaBaHelper.doXiaoLaBaEffects(xiaoLaBaEntity);
            } else if (smeEntity.type != null && smeEntity.type.equals(LIVE_FEE) && smeEntity.uids != null) {
               List<String> list = smeEntity.uids;
               String uid = AULiveApplication.getUserInfo().getUid();
               boolean contains = list.contains(uid);
               if (contains) {
                  com.jack.utils.Utils.showCroutonText(AvActivity.this,"余额不足");
                  AULiveApplication.getUserInfo().diamond = 0;
                  if (mHandler_playback != null) {
                     mHandler_playback.sendEmptyMessageDelayed(NO_MONEY, 3000);
                  }
               }
            } else if (smeEntity.type != null && smeEntity.type.equals(DIAMOND_NOTIFY) && smeEntity.msg == null) {
               if (is_creater) {
                  AULiveApplication.getUserInfo().recv_diamond = smeEntity.recv_diamond;
               }
               gold_count_tv.setText("" + smeEntity.recv_diamond);
            } else if (smeEntity.type != null && smeEntity.type.equals(PAY_OPEN) && smeEntity.msg == null && !is_creater) {
               if (mVariousDialog == null) {
                  mVariousDialog = new VariousDialog(AvActivity.this);
               }
               mVariousDialog.free2pay();
               mHandler_playback.sendEmptyMessageDelayed(OVER_TIME, 10000);
               mVariousDialog.setDialogListener(new VariousDialog.DialogListener() {
                  @Override
                  public void buttonClick(boolean payResult) {
                     mHandler_playback.removeMessages(OVER_TIME);
                     if (!payResult) {
                        onMemberExit();
                     }
                  }
               });
            } else if (smeEntity.type != null && smeEntity.type.equals(PAY_CLOSE) && smeEntity.msg == null && !is_creater) {
               if (mVariousDialog == null) {
                  mVariousDialog = new VariousDialog(AvActivity.this);
               }
               mVariousDialog.pay2free();
            } else if (smeEntity.msg != null && !smeEntity.msg.equals("") && !smeEntity.type.equals(PAY_CLOSE)
                    && !smeEntity.type.equals(PAY_OPEN)) {
               ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
               system_ChatMsgEntity.type = "system";
               //普通提示消息
               system_ChatMsgEntity.nickname = Utils.trans(R.string.news_system);
               system_ChatMsgEntity.chat_msg = smeEntity.msg;
               //添加系统消息提醒
               ChatEntity gif_entity = new ChatEntity();
               gif_entity.setChatMsgEntity(system_ChatMsgEntity);
               gif_entity.setTime(System.currentTimeMillis() / 1000);
               mArrayListChatEntity.add(gif_entity);
               updateChatListView();
            } else if (smeEntity.type != null && smeEntity.type.equals("live_ban")) {
               //超管关闭主播房间
               ChatMsgEntity chatMsgEntity =
                   JsonParser.deserializeByJson(customText, ChatMsgEntity.class);

               //显示提示消息
               chatMsgEntity.chat_msg = chatMsgEntity.system_content;
               ChatEntity entity = new ChatEntity();
               entity.setChatMsgEntity(chatMsgEntity);
               entity.setTime(System.currentTimeMillis() / 1000);
               mArrayListChatEntity.add(entity);
               updateChatListView();

               //关闭房间
               if (!is_creater) {
                  //普通用户只显示消息，什么都不做
                  //onMemberExit();
               } else {
                  onCloseVideo();
               }
            } else if (smeEntity.type != null && smeEntity.system_content != null) {
               //超管关闭主播房间
               ChatMsgEntity chatMsgEntity =
                   JsonParser.deserializeByJson(customText, ChatMsgEntity.class);
               chatMsgEntity.type = "system";
               //普通提示消息
               chatMsgEntity.nickname = "系统消息";
               //显示提示消息
               chatMsgEntity.chat_msg = chatMsgEntity.system_content;
               ChatEntity entity = new ChatEntity();
               entity.setChatMsgEntity(chatMsgEntity);
               entity.setTime(System.currentTimeMillis() / 1000);
               mArrayListChatEntity.add(entity);
               updateChatListView();

               View host_is_leave_ly = AvActivity.this.findViewById(R.id.host_is_warning_ly);

               Button host_is_leave_bt =
                   (Button) AvActivity.this.findViewById(R.id.bt_host_is_warning);
               if (smeEntity.alert == 1) {
                  host_is_leave_bt.setText(chatMsgEntity.system_content);
                  host_is_leave_ly.setVisibility(View.VISIBLE);
               } else {
                  host_is_leave_ly.setVisibility(View.GONE);
               }
            }
         } else if (type.equals(ENTER_ROOM)) {
            // 进入房间
            EnterEntity enterEntity = JsonParser.deserializeByJson(customText, EnterEntity.class);

            int enter_grade = 0;
            try {
               enter_grade = Integer.parseInt(enterEntity.grade);
            } catch (Exception e) {

            }

            //高等级进出房特效,主播不显示
            if (enter_grade > 1 && !enterEntity.uid.equals(AULiveApplication.currLiveUid)) {
               doEnterRoomEffects(enterEntity);
            }

            // 显示座驾
            if (enterEntity.getZuojia() > 0) {
               UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
            } else {
               // test
               //Random random = new Random();
               //int r = random.nextInt(12);
               //if (r == 0) {
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_MOUSE);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 1) {
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_BULL);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 2) {
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_TIGER);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 3) {
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_RABBIT);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 4){
               //
               //} else if (r == 5){
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_SNAKE);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 6){
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_MOUSE);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 7){
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_GOAT);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 8){
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_MONKEY);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 9){
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_ROOSTER);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 10){
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_DOG);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //} else if (r == 11){
               //   enterEntity.setZuojia(UserDriveUtil.DRIVE_PIG);
               //   UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
               //}
            }

            //做人数及时更新
            if (enter_grade > php_control_msg_grade) {
               if (enterEntity.total > view_count) {
                  view_count = enterEntity.total;
               } else {

               }
               if (enterEntity.total != 0) {
                  first_get_online_num = enterEntity.total;
               }
               txt_usernum.setText(first_get_online_num + "");
            }
            boolean isExist = false;
            //判断是否已经群组存在
            for (int i = 0; i < mMemberList.size(); ++i) {
               String userPhone = mMemberList.get(i).getUserPhone();
               if (userPhone.equals(enterEntity.uid)) {
                  isExist = true;
                  break;
               }
            }
            //不存在增加
            if (!isExist) {
               MemberInfo member = null;
               //包含完整信息
               member = new MemberInfo(enterEntity.uid, enterEntity.nickname, enterEntity.face,
                   enterEntity.grade);
               //自己不加入列表
               if (!member.getUserPhone().equals(mSelfUserInfo.getUserPhone())) {
                  //处理按等级排序
                  boolean has_add = false;
                  for (int i = 0; i < mMemberList.size(); ++i) {
                     if (mMemberList.get(i).grade == null) {
                        mMemberList.get(i).grade = "0";
                     }
                     if (enterEntity.grade == null) {
                        enterEntity.grade = "0";
                     }
                     int grade = Integer.parseInt(mMemberList.get(i).grade);
                     if (grade < Integer.parseInt(enterEntity.grade)) {
                        mMemberList.add(i, member);
                        has_add = true;
                        break;
                     }
                  }
                  //最小加到最后
                  if (!has_add && mMemberList.size() > 0) {
                     mMemberList.add(mMemberList.size() - 1, member);
                     has_add = true;
                  }
                  //空列表
                  if (!has_add) {
                     mMemberList.add(0, member);
                     has_add = true;
                  }
               }
            }
            updateMemberView();
            //五级以上才更新公聊
            if (Integer.parseInt(enterEntity.grade) > php_control_msg_grade) {
               //更新公聊
               ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
               system_ChatMsgEntity.type = ENTER_ROOM;
               system_ChatMsgEntity.grade = enterEntity.grade;
               system_ChatMsgEntity.nickname = enterEntity.nickname;
               system_ChatMsgEntity.uid = enterEntity.uid;
               system_ChatMsgEntity.face = enterEntity.face;
               system_ChatMsgEntity.chat_msg = enterEntity.welcome_msg;
               system_ChatMsgEntity.offical = enterEntity.offical;
               //system_ChatMsgEntity.anchor_medal=enterEntity.anchor_medal;
               system_ChatMsgEntity.wanjia_medal = enterEntity.wanjia_medal;
               //添加系统消息提醒
               ChatEntity gif_entity = new ChatEntity();
               gif_entity.setChatMsgEntity(system_ChatMsgEntity);
               gif_entity.setTime(System.currentTimeMillis() / 1000);
               mArrayListChatEntity.add(gif_entity);
               updateChatListView();
            }
         } else if (type.equals(EXIT_ROOM)) {
            // 退出房间
            LeaveEntity leaveEntity = JsonParser.deserializeByJson(customText, LeaveEntity.class);
            //用不上
            //if (first_get_online_num > 20) {
            //   txt_usernum.setText((--first_get_online_num) + "");
            //}

            if (leaveEntity.uid.equals(AULiveApplication.currLiveUid) && is_record_play == false) {
               //if (mHostIdentifier.equals(AULiveApplication.getUserInfo().getUid())) {
               //   //自己是主播不处理
               //} else {
               //主播退出
               view_count = leaveEntity.audience_count;
               praise_count = leaveEntity.praise_count;
               Trace.d("****>>>>在这里leave");
               videoIsClosed();
               //}
            } else {
               //普通用户只是去图标
               for (int i = 0; i < mMemberList.size(); ++i) {
                  String userPhone = mMemberList.get(i).getUserPhone();
                  if (userPhone.equals(leaveEntity.uid)) {
                     mMemberList.remove(i);
                  }
               }
               updateMemberView();
            }
         } else if (type.equals(MSG_COETENT_ROOM)) {
            // 聊天内容
            ChatMsgEntity chatMsgEntity =
                JsonParser.deserializeByJson(customText, ChatMsgEntity.class);

            ChatEntity entity = new ChatEntity();
            entity.setChatMsgEntity(chatMsgEntity);
            entity.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(entity);
            updateChatListView();
         } else if (type.equals(MSG_GIFT_ROOM)) {
            // 送礼物
            SendGiftEntity sendGiftEntity =
                JsonParser.deserializeByJson(customText, SendGiftEntity.class);
            //android是有独立的packetid,由于ios没有，而是把packetid赋值在gift_id上,接收后必须做相应处理

            if (sendGiftEntity.gift_type == 2) {
               sendGiftEntity.packetid = sendGiftEntity.gift_id;
            }
            //累加收到的礼物钻石数
            if (sendGiftEntity.gift_type != 2) {
               gift_price_count = gift_price_count + sendGiftEntity.price;
            }
            gold_count_tv.setText(sendGiftEntity.recv_diamond + "");
            //更新礼物
            addReciveGiftEntity(sendGiftEntity);

            ChatMsgEntity gift_ChatMsgEntity = new ChatMsgEntity();
            gift_ChatMsgEntity.grade = sendGiftEntity.grade;
            gift_ChatMsgEntity.type = "gift";
            gift_ChatMsgEntity.nickname = sendGiftEntity.nickname;
            gift_ChatMsgEntity.uid = sendGiftEntity.uid;
            gift_ChatMsgEntity.face = sendGiftEntity.face;
            gift_ChatMsgEntity.chat_msg = "我送了1个" + sendGiftEntity.gift_name;
            gift_ChatMsgEntity.gift_type = sendGiftEntity.gift_type;
            gift_ChatMsgEntity.offical = sendGiftEntity.offical;
            //gift_ChatMsgEntity.anchor_medal=sendGiftEntity.anchor_medal;
            gift_ChatMsgEntity.wanjia_medal = sendGiftEntity.wanjia_medal;
            //android是有独立的packetid,由于ios没有，而是把packetid赋值在gift_id上,接收后必须做相应处理
            if (sendGiftEntity.gift_type == 2) {
               gift_ChatMsgEntity.packetid = sendGiftEntity.gift_id;
            }

            //添加礼物消息提醒
            ChatEntity gif_entity1 = new ChatEntity();
            gif_entity1.setChatMsgEntity(gift_ChatMsgEntity);
            gif_entity1.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(gif_entity1);
            updateChatListView();
         } else if (type.equals(ATTENT_USER_ROOM)) {
            // 关注用户
            AttentEntity attentEntity =
                JsonParser.deserializeByJson(customText, AttentEntity.class);
            //更新公聊
            ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
            system_ChatMsgEntity.type = "system";
            system_ChatMsgEntity.nickname = attentEntity.nickname + "";
            system_ChatMsgEntity.chat_msg = "我关注了主播";
            system_ChatMsgEntity.offical = attentEntity.offical;
            //system_ChatMsgEntity.anchor_medal=attentEntity.anchor_medal;
            system_ChatMsgEntity.wanjia_medal = attentEntity.wanjia_medal;
            //添加系统消息提醒
            ChatEntity gif_entity = new ChatEntity();
            gif_entity.setChatMsgEntity(system_ChatMsgEntity);
            gif_entity.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(gif_entity);
            updateChatListView();
         } else if (type.equals(UPGRADE_USER_ROOM)) {
            // 用户升级
            UserUpGradeEntity userUpGradeEntity =
                JsonParser.deserializeByJson(customText, UserUpGradeEntity.class);
         } else if (type.equals(LOVE_ANCHOR)) {
            // 点亮
            LikeUserEntity likeUserEntity =
                JsonParser.deserializeByJson(customText, LikeUserEntity.class);
            //点赞的人数加一
            praise_count++;

            //更新公聊
            ChatMsgEntity like_ChatMsgEntity = new ChatMsgEntity();
            like_ChatMsgEntity.type = LOVE_ANCHOR;
            like_ChatMsgEntity.grade = likeUserEntity.grade;
            like_ChatMsgEntity.nickname = likeUserEntity.nickname;
            like_ChatMsgEntity.uid = likeUserEntity.uid;
            like_ChatMsgEntity.face = likeUserEntity.face;
            like_ChatMsgEntity.chat_msg = "我点亮了";
            like_ChatMsgEntity.offical = likeUserEntity.offical;
            //like_ChatMsgEntity.anchor_medal=likeUserEntity.anchor_medal;
            like_ChatMsgEntity.wanjia_medal = likeUserEntity.wanjia_medal;
            //添加系统消息提醒
            ChatEntity gif_entity = new ChatEntity();
            gif_entity.setChatMsgEntity(like_ChatMsgEntity);
            gif_entity.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(gif_entity);
            updateChatListView();
         } else if (type.equals(BARRAGE_USER)) {
            // 用户弹幕
            UserBarrageEntity userBarrageEntity =
                JsonParser.deserializeByJson(customText, UserBarrageEntity.class);

            //更新公聊
            //ChatMsgEntity like_ChatMsgEntity = new ChatMsgEntity();
            //like_ChatMsgEntity.type = "chat";
            //like_ChatMsgEntity.grade = userBarrageEntity.grade;
            //like_ChatMsgEntity.nickname = userBarrageEntity.nickname;
            //like_ChatMsgEntity.uid = userBarrageEntity.uid;
            //like_ChatMsgEntity.face = userBarrageEntity.face;
            //like_ChatMsgEntity.chat_msg = userBarrageEntity.chat_msg;
            ////添加系统消息提醒
            //ChatEntity gif_entity = new ChatEntity();
            //gif_entity.setChatMsgEntity(like_ChatMsgEntity);
            //gif_entity.setTime(System.currentTimeMillis() / 1000);
            //mArrayListChatEntity.add(gif_entity);
            //updateChatListView();

            addDanMu(userBarrageEntity);
         } else if (type.equals(GAG_USER)) {

            // 禁言用户
            GagUserEntity gagUserEntity =
                JsonParser.deserializeByJson(customText, GagUserEntity.class);
            String gagUid = gagUserEntity.uid;
            String my_uid = AULiveApplication.getUserInfo().getUid();
            if (gagUserEntity.send_name == null) {
               gagUserEntity.send_name = "管理员";
            }
            //与我uid相同
            if (gagUid.equals(my_uid)) {
               if (is_super_manager) {
                  return;
               }
               //判断是否被禁言
               is_gag = true;
               showPromptDialog("你已被" + gagUserEntity.send_name + "禁言了");
            }
            //更新公聊
            ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
            system_ChatMsgEntity.type = "system";
            system_ChatMsgEntity.nickname = "系统消息";
            system_ChatMsgEntity.chat_msg =
                gagUserEntity.nickname + "被" + gagUserEntity.send_name + "禁言";
            //添加系统消息提醒
            ChatEntity gif_entity = new ChatEntity();
            gif_entity.setChatMsgEntity(system_ChatMsgEntity);
            gif_entity.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(gif_entity);
            updateChatListView();
         } else if (type.equals(REMOVE_GAG_USER)) {

            // 解除禁言
            RemoveGagEntity removeGagEntity =
                JsonParser.deserializeByJson(customText, RemoveGagEntity.class);

            String gagUid = removeGagEntity.uid;
            String my_uid = AULiveApplication.getUserInfo().getUid();
            if (removeGagEntity.send_name == null) {
               removeGagEntity.send_name = "管理员";
            }
            //与我uid相同
            if (gagUid.equals(my_uid)) {
               if (is_super_manager) {
                  return;
               }
               //判断是否被禁言
               is_gag = false;
               showPromptDialog("你已被" + removeGagEntity.send_name + "解除禁言了");
            }
            //更新公聊
            ChatMsgEntity system_ChatMsgEntity = new ChatMsgEntity();
            system_ChatMsgEntity.type = "system";
            system_ChatMsgEntity.nickname = "系统消息";
            system_ChatMsgEntity.chat_msg =
                removeGagEntity.send_name + "把" + removeGagEntity.nickname + "解除禁言";
            //添加系统消息提醒
            ChatEntity gif_entity = new ChatEntity();
            gif_entity.setChatMsgEntity(system_ChatMsgEntity);
            gif_entity.setTime(System.currentTimeMillis() / 1000);
            mArrayListChatEntity.add(gif_entity);
            updateChatListView();
         } else if (type.equals(MANAGER_ROOM)) {
            // 设置管理员
            SetManagerEntity setManagerEntity =
                JsonParser.deserializeByJson(customText, SetManagerEntity.class);
            String managerUid = setManagerEntity.uid;
            String my_uid = AULiveApplication.getUserInfo().getUid();
            //与我uid相同
            if (managerUid.equals(my_uid)) {
               //判断是否已经为管理员
               is_manager = true;
               showPromptDialog("你已被设置为管理员");
            }
         } else if (type.equals(REMOVE_MANAGER_ROOM)) {
            // 移除管理员
            RemoveManagerEntity removeManagerEntity =
                JsonParser.deserializeByJson(customText, RemoveManagerEntity.class);
            String remove_managerUid = removeManagerEntity.uid;
            String my_uid = AULiveApplication.getUserInfo().getUid();
            //与我uid相同
            Trace.d("remove_managerUid:my_uid=" + remove_managerUid + ":" + my_uid);
            if (remove_managerUid.equals(my_uid)) {
               //判断是否被禁言
               is_manager = false;
               showPromptDialog("你被取消了管理员身份");
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private int updateChatListView_count = 0;
   //0.5秒更新一次
   private long last_update_chat_listview = 0;

   private void updateChatListView() {
      try {
         if (mListViewMsgItems == null) {
            return;
         }
         //0.5秒更新一次
         //if (System.currentTimeMillis() - last_update_chat_listview > 500) {
         //   last_update_chat_listview = System.currentTimeMillis();
         //   mListViewMsgItems.postDelayed(new Runnable() {
         //      @Override public void run() {

         //Trace.d("updateChatListView_count:" + updateChatListView_count++);
         if (mListViewMsgItems == null) {
            return;
         }
         //处理完之后，更新
         mChatMsgListAdapter.notifyDataSetChanged();
         mListViewMsgItems.setVisibility(View.VISIBLE);
         if (mListViewMsgItems.getCount() > 1) {
            if (mIsLoading) {
               //有滑动后，位置不变
               //mListViewMsgItems.setSelection(0);
            } else {
               mListViewMsgItems.setSelection(mListViewMsgItems.getCount() - 1);
            }
         }
         mIsLoading = false;
         //}
         //   }, 500);
         //} else {
         //
         //}
      } catch (Exception e) {

      }
   }

   private String headurl;

   private int updateMemberView_count = 0;

   private long last_update_member_view = 0;

   //1秒更新一次
   private void updateMemberView() {
      if (mListViewMsgItems == null) {
         return;
      }
      if (System.currentTimeMillis() - last_update_member_view > 1000) {
         last_update_member_view = System.currentTimeMillis();
         mListViewMsgItems.postDelayed(new Runnable() {
            @Override public void run() {
               if (mListViewMsgItems == null) {
                  return;
               }
               //Trace.d("updateMemberView_count:" + updateMemberView_count++);
               //txt_usernum.setText(getIntent().getExtras().getInt(EXTRA_ONLINE_NUM) + mMemberList.size());
               //更新最后添加的用户
               //RecycleView的adapter
               roomUserListAdapter.notifyDataSetChanged();

               if (hostMember != null && headurl == null) {
                  headurl = hostMember.getHeadImagePath();
                  headurl = Utils.getImgUrl(headurl);
                  DisplayImageOptions options =
                      new DisplayImageOptions.Builder().showStubImage(R.drawable.default_head)
                          .showImageForEmptyUri(R.drawable.default_head)
                          .showImageOnFail(R.drawable.default_head)
                          .cacheInMemory()
                          .cacheOnDisc()
                          .build();
                  ImageLoader.getInstance().displayImage(headurl, img_room_creator, options);

                  RoomUserListAdapter.setTopIcon(hostMember.grade, img_user_type);
               }

               //如果第一次人数为0,则更新人数
               int num = 0;
               try {
                  num = Integer.parseInt(txt_usernum.getText().toString());
               } catch (Exception e) {
                  num = 0;
               }
               if (num == 0) {
                  txt_usernum.setText(mMemberList.size() + "");
               }
            }
         }, 1000);
      } else {

      }
   }

   //从php接口获取的消息发送等级控制
   private int php_control_msg_grade = 0;

   /**
    * 发一条消息告诉大家 自己上线了
    */
   public void onMemberEnter() {
      //当主播已经离开了，就不进房与获取列表
      if (!is_creater) {
         if (0 == getIntent().getIntExtra(EXTRA_IS_ON_SHOW, 1)) {
            return;
         }
      }
      if (mSelfUserInfo.getUserName() == null) {
         mSelfUserInfo.setUserName("");
      }
      if (mSelfUserInfo.getHeadImagePath() == null) {
         mSelfUserInfo.setHeadImagePath("");
      }
      //0级以上才有提示
      int grade = 0;
      try {
         grade = Integer.parseInt(AULiveApplication.getUserInfo().getGrade());
      } catch (Exception e) {

      }
      if (grade > php_control_msg_grade) {
         EnterEntity enterEntity = new EnterEntity();
         enterEntity.uid = mSelfUserInfo.getUserPhone();
         enterEntity.face = mSelfUserInfo.getHeadImagePath();
         enterEntity.type = ENTER_ROOM;
         enterEntity.grade = AULiveApplication.getUserInfo().getGrade();
         if (mSelfUserInfo.getmMsg_tip() == null  || mSelfUserInfo.getmMsg_tip().equals("")) {
            enterEntity.nickname = mSelfUserInfo.getUserName();
         } else {
            enterEntity.nickname = mSelfUserInfo.getUserName() + "<" + mSelfUserInfo.getmMsg_tip() + ">";
         }

         enterEntity.offical = getIntent().getIntExtra(EXTRA_IS_SUPER_MANAGER, 0);
         //enterEntity.anchor_medal=medalListEvent.anchor_medal;
         enterEntity.wanjia_medal = medalListEvent.wanjia_medal;
         if (grade > 16) {
            if (mSelfUserInfo.getmMsg_tip() == null || mSelfUserInfo.getmMsg_tip().equals("")) {
               enterEntity.welcome_msg = "一道金光闪过 '" + mSelfUserInfo.getUserName()+ "'进入直播间";
            } else {
               enterEntity.welcome_msg = "一道金光闪过 '" + mSelfUserInfo.getUserName() +"<" + mSelfUserInfo.getmMsg_tip() + ">"+ "'进入直播间";
            }
         } else {
            if (mSelfUserInfo.getmMsg_tip() == null || mSelfUserInfo.getmMsg_tip().equals("")) {
               enterEntity.welcome_msg = "欢迎'" + mSelfUserInfo.getUserName()+ "'进入直播间";
            } else {
               enterEntity.welcome_msg = "欢迎'" + mSelfUserInfo.getUserName() +"<" + mSelfUserInfo.getmMsg_tip() + ">"+ "'进入直播间";
            }
         }

         //由于只有0级别才发ENTER_ROOM，不能及时更新人数，所以在大于0级别的ENTER_ROOM中加入total来做人数更新
         enterEntity.total = first_get_online_num;
         if (AULiveApplication.getUserInfo().zuojia > 0) {
            enterEntity.setZuojia(AULiveApplication.getUserInfo().zuojia);

            UserDriveUtil.getInstance(this).showDriveNotify(enterEntity);
         }

         String message = JsonParser.serializeToJson(enterEntity);

         CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
         custom_msg.type = ENTER_ROOM;
         custom_msg.data = message;
         doSendContent(custom_msg);
      }
   }

   //主播头像
   public final static String EXTRA_SELF_IDENTIFIER_FACE = "EXTRA_SELF_IDENTIFIER_FACE";
   //主播nickname
   public final static String EXTRA_SELF_IDENTIFIER_NICKNAME = "EXTRA_SELF_IDENTIFIER_NICKNAME";
   //主播一共收到的钻石
   public final static String EXTRA_RECIVE_DIAMOND = "EXTRA_RECIVE_DIAMOND";
   //主播上次关闭直播间的观看人数与点赞人数
   public final static String EXTRA_LAST_VIEW_COUNT = "EXTRA_LAST_VIEW_COUNT";
   public final static String EXTRA_LAST_PRAISE_COUNT = "EXTRA_LAST_PRAISE_COUNT";
   //主播是否在直播
   public final static String EXTRA_IS_ON_SHOW = "EXTRA_IS_ON_SHOW";
   //是否在是回放
   public final static String EXTRA_IS_RECORD = "EXTRA_IS_RECORD";
   //是否在是回放ID
   public final static String EXTRA_IS_RECORD_ID = "EXTRA_IS_RECORD_ID";
   //系统信息
   public final static String EXTRA_SYS_MSG = "EXTRA_SYS_MSG";
   //在线人数
   public final static String EXTRA_ONLINE_NUM = "EXTRA_ONLINE_NUM";
   //是否是管理员
   public final static String EXTRA_IS_MANAGER = "EXTRA_IS_MANAGER";
   //是否是被禁言
   public final static String EXTRA_IS_GAG = "EXTRA_IS_GAG";
   //是不是超管
   public final static String EXTRA_IS_SUPER_MANAGER = "EXTRA_IS_SUPER_MANAGER";
   //播放的url
   public final static String EXTRA_play_url_KEY = "EXTRA_play_url_KEY";
   //消息发送等级控制
   public final static String EXTRA_MSG_SEND_GRADE_CONTROL = "EXTRA_MSG_SEND_GRADE_CONTROL";
   //直播间标题展示
   public final static String EXTRA_LIVE_TITLE_IN = "EXTRA_LIVE_TITLE_IN";

   //用户头像分页处理
   private int currPage = 0;
   private boolean isLoading = false;
   private boolean hasMore = true;

   public void getMemberInfo() {
      currPage++;

      isLoading = true;
      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(UrlHelper.getUserListUrl
             + "?roomid="
             + AULiveApplication.currLiveUid
             + "&liveuid="
             + AULiveApplication.currLiveUid
             + "&page="
             + currPage);
         Trace.d("getMemberInfo url:" + sb.toString());
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<MemberListEntity>() {

         @Override public void onCallback(MemberListEntity callback) {
            if (callback == null) {
               currPage--;
               hasMore = false;
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {

               //从login或syn中获取
               //如果自己是主播，拉下的成员列表过滤自己
               if (is_creater) {
                  hostMember = new MemberInfo(AULiveApplication.getUserInfo().getUid(),
                      AULiveApplication.getUserInfo().getNickname(),
                      AULiveApplication.getUserInfo().getFace(),
                      AULiveApplication.getUserInfo().getGrade());
               } else {
                  String face = getIntent().getExtras().getString(EXTRA_SELF_IDENTIFIER_FACE);
                  String nick_name =
                      getIntent().getExtras().getString(EXTRA_SELF_IDENTIFIER_NICKNAME);
                  int recive_diamond = getIntent().getExtras().getInt(EXTRA_RECIVE_DIAMOND, 0);

                  if (mHostGrade == null || mHostGrade.equals("")) {
                     mHostGrade = "0";
                  }
                  Trace.d("****>>>>在这里赋值");
                  hostMember =
                      new MemberInfo(AULiveApplication.currLiveUid, nick_name, face, mHostGrade);
                  EventBus.getDefault().post(hostMember);
               }

               ArrayList<MemberEntity> memberEntities = callback.getList();

               for (MemberEntity memberEntity : memberEntities) {

                  if (memberEntity.uid.equals(AULiveApplication.currLiveUid)) {
                     hostMember =
                         new MemberInfo(memberEntity.uid, memberEntity.nickname, memberEntity.face,
                             memberEntity.grade);
                     hostMember.offical = memberEntity.offical;
                     continue;
                  }

                  MemberInfo member =
                      new MemberInfo(memberEntity.uid, memberEntity.nickname, memberEntity.face,
                          memberEntity.grade);
                  member.offical = memberEntity.offical;
                  if (member.getUserPhone().equals(mSelfUserInfo.getUserPhone())) {
                     mSelfUserInfo.setHeadImagePath(member.getHeadImagePath());
                  }
                  mMemberList.add(member);
               }

               updateMemberView();
            } else {
               Utils.showMessage("获取房间成员出错");
            }
            //是否有更多
            if ((callback.getList() != null && callback.getList().size() > 0)) {
               hasMore = true;
            } else {
               currPage--;
               hasMore = false;
            }
            isLoading = false;
         }

         @Override public void onFailure(AppException e) {
            currPage--;
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
            isLoading = false;
         }
      }.setReturnType(MemberListEntity.class));
      request.execute();
   }

   //五秒一删
   private void removeChatItem() {
      if (mListViewMsgItems == null) {
         return;
      }

      long time = System.currentTimeMillis() / 1000;
      //Trace.d("removeChatItem 五秒一删");
      int num = mListViewMsgItems.getCount();
      //别删完，留60条信息
      // 加载中不能删除
      if (num > 200) {
         //要反着删，不然index会变
         for (int i = num - 1; i >= 0; i--) {
            if (mArrayListChatEntity.size() == 0) {
               return;
            }
            if (time - mArrayListChatEntity.get(i).getTime() > 10) {
               mArrayListChatEntity.remove(i);
            }
         }
         mChatMsgListAdapter.notifyDataSetChanged();
         mListViewMsgItems.setVisibility(View.VISIBLE);
      }
   }

   //按关闭按的看客离开
   public void onMemberExit() {
      EventBus.getDefault().post(new CloseAvActivityEvent());
      if (mChatTimer != null) {
         mChatTimer.cancel();
      } else {
         return;
      }

      onMemberFlipPage();

      if (mStreamer != null) {
         mStreamer.stopStream();
         recording = false;
         //结束视频播放
         Trace.d(">>在这里onmember");
         videoPlayEnd();
      }
      mMemberList.clear();
      AvActivity.this.finish();
   }

   //看客划屏离开
   public void onMemberFlipPage() {
      leaveLive();

      //0级以上才有提示
      int grade = 0;
      try {
         grade = Integer.parseInt(AULiveApplication.getUserInfo().getGrade());
      } catch (Exception e) {

      }
      if (grade > php_control_msg_grade) {
         //提示房间所有人
         LeaveEntity leaveEntity = new LeaveEntity();
         leaveEntity.type = EXIT_ROOM;
         leaveEntity.uid = mSelfUserInfo.getUserPhone();
         String message = JsonParser.serializeToJson(leaveEntity);

         CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
         custom_msg.type = EXIT_ROOM;
         custom_msg.data = message;
         doSendContent(custom_msg);
      }
   }

   //主播离开
   private void onCloseVideo() {
      if (mChatTimer != null) {
         mChatTimer.cancel();
      } else {
         return;
      }

      closeLive();

      LeaveEntity leaveEntity = new LeaveEntity();
      leaveEntity.type = EXIT_ROOM;
      leaveEntity.uid = mSelfUserInfo.getUserPhone();
      leaveEntity.audience_count = view_count;
      leaveEntity.praise_count = praise_count;

      String message = JsonParser.serializeToJson(leaveEntity);

      CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
      custom_msg.type = EXIT_ROOM;
      custom_msg.data = message;
      doSendContent(custom_msg);

      if (mStreamer != null) {
         mStreamer.stopStream();
         recording = false;
         //结束视频播放
         Trace.d("****>>在这里发现");
         videoPlayEnd();
      }
   }

   private Dialog dialog;

   private void hostCloseAlertDialog() {
      dialog = new Dialog(this, R.style.dialog);
      dialog.setContentView(R.layout.exit_dialog);
      TextView messageTextView = (TextView) dialog.findViewById(R.id.message);
      Button exitOk = (Button) dialog.findViewById(R.id.btn_exit_ok);
      Button exitCancel = (Button) dialog.findViewById(R.id.btn_exit_cancel);
      messageTextView.setText("有" + mMemberList.size() + "人正在看您的直播\n确定结束直播吗？");
      exitOk.setText("结束直播");
      exitCancel.setText("继续直播");
      exitOk.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            onCloseVideo();
            dialog.dismiss();
         }
      });

      exitCancel.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            dialog.dismiss();
         }
      });
      dialog.show();
   }

   private void memberCloseAlertDialog() {
      dialog = new Dialog(this, R.style.dialog);
      dialog.setContentView(R.layout.exit_dialog);
      TextView messageTextView = (TextView) dialog.findViewById(R.id.message);
      Button exitOk = (Button) dialog.findViewById(R.id.btn_exit_ok);
      Button exitCancel = (Button) dialog.findViewById(R.id.btn_exit_cancel);
      messageTextView.setText("确认退出吗？");
      exitOk.setText("结束观看");
      exitCancel.setText("继续观看");
      exitOk.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            onMemberExit();
            dialog.dismiss();
         }
      });

      exitCancel.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            dialog.dismiss();
         }
      });
      dialog.show();
   }

   private CustomDialog promptDialog;

   //防多次弹出GameOverActivity界面

   public void setVideoState(boolean isClosed) {
      video_is_closed = isClosed;
   }

   public boolean getVideoState() {
      return video_is_closed;
   }

   //提示此直播已结束
   public void videoIsClosed() {
//      if (video_is_closed) {
//         return;
//      }
      video_is_closed = true;
      //mEditTextInputMsg.setVisibility(View.GONE);
      //mButtonSendMsg.setVisibility(View.GONE);
      //
      //dialog = new Dialog(this, R.style.dialog);
      //dialog.setContentView(R.layout.alert_dialog);
      //((TextView) dialog.findViewById(R.id.dialog_title)).setText("温馨提示");
      //((TextView) dialog.findViewById(R.id.dialog_message)).setText("此直播已结束，请观看其他直播！");
      //((Button) dialog.findViewById(R.id.close_dialog)).setOnClickListener(
      //    new View.OnClickListener() {
      //       @Override public void onClick(View view) {
      //          //onCloseVideo();
      //          AvActivity.this.finish();
      //          dialog.dismiss();
      //       }
      //    });
      //dialog.setCanceledOnTouchOutside(false);
      //dialog.show();

      //去“主播暂时离开”
      AvActivity.this.findViewById(R.id.host_is_leave_ly).setVisibility(View.GONE);

      //隐藏所有操作界面
      findViewById(R.id.qav_bottom_bar).setVisibility(View.INVISIBLE);
      findViewById(R.id.qav_top_bar_new).setVisibility(View.INVISIBLE);
      //关闭流与播放器
      onRoomLeave();
      Trace.d("****>>>>直播已经结束");
      gameOverFragment = new GameOverFragment();
      Bundle bundle = new Bundle();
      bundle.putString(GameOverActivity.EXTRA_ROOM_NUM, AULiveApplication.currLiveUid);
      bundle.putBoolean(GameOverActivity.EXTRA_LEAVE_MODE, is_creater);
      bundle.putInt(GameOverActivity.EXTRA_PRAISE_NUM, praise_count);
      bundle.putInt(GameOverActivity.EXTRA_VIEWS_NUM, view_count);
      bundle.putSerializable(GameOverActivity.EXTRA_HOST_INFO, hostMember);
      bundle.putInt(GameOverActivity.EXTRA_PRICE_COUNT, gift_price_count);
      gameOverFragment.setArguments(bundle);
      if (endView == null) {
         endView = View.inflate(this, R.layout.game_over_activity, null);
         RelativeLayout root_view = (RelativeLayout) findViewById(R.id.av_screen_layout);
         root_view.addView(endView);
         getSupportFragmentManager().beginTransaction()
             .replace(R.id.fl_gameover, gameOverFragment)
             .commit();
      }
      endView.setVisibility(View.VISIBLE);
      doPause();
      //第二种方式，跳到GameOverActivity界面
//      Intent intent = new Intent(AvActivity.this, GameOverActivity.class);
//      intent.
//              putExtra(
//                      GameOverActivity.EXTRA_ROOM_NUM, AULiveApplication.currLiveUid)
//              .putExtra(GameOverActivity.EXTRA_LEAVE_MODE, is_creater)
//              .putExtra(GameOverActivity.EXTRA_PRAISE_NUM, praise_count)
//              .putExtra(GameOverActivity.EXTRA_VIEWS_NUM, view_count)
//              .putExtra(GameOverActivity.EXTRA_HOST_INFO, hostMember)
//              .putExtra(GameOverActivity.EXTRA_PRICE_COUNT, gift_price_count);
//
//      startActivity(intent);
      //AvActivity.this.finish();

   }

   public void onEvent(CloseAvActivityEvent close_event) {
      AvActivity.this.finish();
   }

   //根据用户id在用户列表里找
   public MemberInfo findMemberInfo(ArrayList<MemberInfo> list, String id) {

      String identifier = "";
      identifier = id;
      for (MemberInfo member : list) {

         if (member.getUserPhone().equals(identifier)) {
            return member;
         }
      }
      return null;
   }

   //送礼物相关
   ////////////////////////////////////////////////
   //自己送出去的礼物列表缓存,不删除
   private LinkedList<SendGiftEntity> sendgiftList = new LinkedList<SendGiftEntity>();
   //接收到是要显示的礼物，显示玩删除

   private HashMap<String, HashMap<String, LinkedList<SendGiftEntity>>> recive_gift_List =
       new HashMap<String, HashMap<String, LinkedList<SendGiftEntity>>>();

   //发礼物
   public void sendGiftMsg(GiftEntity giftEntity) {
      SendGiftEntity sendGiftEntity = new SendGiftEntity();

      if (giftEntity == null) {
         return;
      }
      sendGiftEntity.face = AULiveApplication.getUserInfo().getFace();
      sendGiftEntity.gift_id = giftEntity.getId();
      sendGiftEntity.nickname = AULiveApplication.getUserInfo().getNickname();
      //gift_type 1=普通礼物 2=红包 3=豪华礼物
      sendGiftEntity.gift_type = giftEntity.getType();
      sendGiftEntity.type = "gift";
      sendGiftEntity.gift_name = giftEntity.getName();
      sendGiftEntity.uid = AULiveApplication.getUserInfo().getUid();
      sendGiftEntity.grade = giftEntity.getGrade() + "";
      //只有红包有，否则为0
      sendGiftEntity.packetid = giftEntity.getPacketid();
      sendGiftEntity.recv_diamond = giftEntity.recv_diamond;
      sendGiftEntity.price = giftEntity.getPrice();
      sendGiftEntity.offical = getIntent().getIntExtra(EXTRA_IS_SUPER_MANAGER, 0);
      sendGiftEntity.paint = giftEntity.paint;

      //sendGiftEntity.anchor_medal = medalListEvent.anchor_medal;
      sendGiftEntity.wanjia_medal = medalListEvent.wanjia_medal;
      boolean is_contians = false;
      for (SendGiftEntity entity : sendgiftList) {
         if (entity.gift_id == sendGiftEntity.gift_id) {
            entity.gift_nums++;
            sendGiftEntity.gift_nums = entity.gift_nums;
            is_contians = true;
         }
      }
      if (is_contians) {

      } else {
         //第一次添加相应礼物时
         sendGiftEntity.gift_nums = 1;
         sendgiftList.add(sendGiftEntity);
      }
      //android是有独立的packetid,由于ios没有，而是把packetid赋值在gift_id上,发出前必须做相应处理
      if (sendGiftEntity.gift_type == 2) {
         sendGiftEntity.gift_id = giftEntity.getPacketid();
      }

      final SendGiftEntity tem_SendGiftEntity = sendGiftEntity;
      //自己的要优先显示,已经送礼后handlecustom显示了，再加就重复了
      //addReciveGiftEntity(tem_SendGiftEntity);

      final String msg = JsonParser.serializeToJson(sendGiftEntity);
      CustomizeChatRoomMessage custom_msg = new CustomizeChatRoomMessage();
      custom_msg.type = MSG_GIFT_ROOM;
      custom_msg.data = msg;
      doSendContent(custom_msg);
   }

   public void addReciveGiftEntity(final SendGiftEntity sendGiftEntity) {
      if (sendGiftEntity.gift_type == 1) {
         if (recive_gift_List.get(sendGiftEntity.uid) == null) {
            HashMap<String, LinkedList<SendGiftEntity>> tem_hashmpa =
                new HashMap<String, LinkedList<SendGiftEntity>>();
            recive_gift_List.put(sendGiftEntity.uid, tem_hashmpa);

            LinkedList<SendGiftEntity> tem_linkedList = new LinkedList<SendGiftEntity>();
            tem_hashmpa.put(sendGiftEntity.gift_id + "", tem_linkedList);

            tem_linkedList.addLast(sendGiftEntity);
         } else if (recive_gift_List.get(sendGiftEntity.uid).get(sendGiftEntity.gift_id + "")
             == null) {
            LinkedList<SendGiftEntity> tem_linkedList = new LinkedList<SendGiftEntity>();
            tem_linkedList.addLast(sendGiftEntity);

            recive_gift_List.get(sendGiftEntity.uid)
                .put(sendGiftEntity.gift_id + "", tem_linkedList);
         } else {
            recive_gift_List.get(sendGiftEntity.uid)
                .get(sendGiftEntity.gift_id + "")
                .addLast(sendGiftEntity);
         }

         //连送礼物处理
         showGiftNotify(sendGiftEntity);
      } else if (sendGiftEntity.gift_type == 2) {
         //红包
         showRedBagNotify(sendGiftEntity);
      } else if (sendGiftEntity.gift_type == 3) {
         //豪华礼物
         showLuxuryGiftNotify(sendGiftEntity);
      }
   }

   ///豪华礼物动画
   public synchronized void showLuxuryGiftNotify(SendGiftEntity sendGiftEntity) {
      if (!LuxuryGiftUtil.is_showing_luxury_gift) {
         LuxuryGiftUtil.getInstance(AvActivity.this).showLuxuryGift(sendGiftEntity);
      } else {
         Trace.d("addCacheLuxuryGift:" + sendGiftEntity.gift_name);
         addCacheLuxuryGift(sendGiftEntity);
      }
   }

   private LinkedList<SendGiftEntity> cache_luxury_gift_List = new LinkedList<SendGiftEntity>();

   public void hasAnyLuxuryGift() {

      if (cache_luxury_gift_List.size() > 0) {
         showLuxuryGiftNotify(cache_luxury_gift_List.removeFirst());
      }
   }

   public void addCacheLuxuryGift(SendGiftEntity giftEntity) {
      cache_luxury_gift_List.addLast(giftEntity);
   }

   /////////////////////////////////////////////////////////////
   //红包动画
   public synchronized void showRedBagNotify(SendGiftEntity sendGiftEntity) {
      if (!RedPacketesUtil.is_show_redbag) {
         //显示红包
         RedPacketesUtil.getInstance(AvActivity.this, sendGiftEntity.uid, sendGiftEntity.nickname,
             sendGiftEntity.face, qav_top_bar_new).grabRedBagPopUpWindows(sendGiftEntity.packetid);
      } else {
         addCacheRedBag(sendGiftEntity);
      }
   }

   private LinkedList<SendGiftEntity> cache_redbag_List = new LinkedList<SendGiftEntity>();

   public void hasAnyRedBagGift() {

      if (cache_redbag_List.size() > 0) {
         showRedBagNotify(cache_redbag_List.removeFirst());
      }
   }

   public void addCacheRedBag(SendGiftEntity giftEntity) {
      for (SendGiftEntity tem : cache_redbag_List) {
         if (giftEntity.packetid == tem.packetid) {
            //已经存在相同类型
            return;
         }
      }
      cache_redbag_List.addLast(giftEntity);
   }

   ////////////////////////////////////////////////////////////////////
   public String playingGifID1;
   public String playingGifID2;

   //连送动画
   public void showGiftNotify(SendGiftEntity sendGiftEntity) {

      if (sendGiftEntity == null) {
         return;
      }

      //连送礼物处理

      LinkedList<SendGiftEntity> now_SendGiftList =
          recive_gift_List.get(sendGiftEntity.uid).get(sendGiftEntity.gift_id + "");

      //当没有更新时
      if (now_SendGiftList.size() == 0) {
         return;
      }
      /////////////////////////////////////////////////////
      //去除特殊不显示现象
      //判断存在送很多类型的礼物后，出现playingGifID1＝＝playingGifID2，而continue_gift1，continue_gift2没处于使用状态
      if (continue_gift1.doingGiftShow == false
          && continue_gift2.doingGiftShow == false
          && playingGifID1 != null
          && !playingGifID1.equals("")
          && playingGifID1.equals(playingGifID2)) {
         playingGifID1 = null;
         playingGifID2 = null;
      }
      //去卡死现象
      //continue_gift1.cleanBlockState();
      //continue_gift2.cleanBlockState();
      //////////////////////////////////////////////////////

      if (continue_gift1.doingGiftShow == false) {
         SendGiftEntity tem = now_SendGiftList.getFirst();
         playingGifID1 = tem.uid + tem.gift_id;

         if (playingGifID1.equals(playingGifID2)) {
            //动画2在播放同一个用户的同一个gid动画，返回
            playingGifID1 = "";
            return;
         }

         continue_gift1.setmGiftID(1);
         continue_gift1.setSendGiftEntity(now_SendGiftList, AvActivity.this);
         continue_gift1.showGiftView();

         return;
      } else if (continue_gift2.doingGiftShow == false) {
         SendGiftEntity tem = now_SendGiftList.getFirst();
         playingGifID2 = tem.uid + tem.gift_id;

         if (playingGifID1.equals(playingGifID2)) {
            //动画1在播放同一个用户的同一个gid动画，返回
            playingGifID2 = "";
            return;
         }
         continue_gift2.setmGiftID(2);
         continue_gift2.setSendGiftEntity(now_SendGiftList, AvActivity.this);
         continue_gift2.showGiftView();

         return;
      }

      //两个动画都在使用中，缓存起来
      String tem_id = sendGiftEntity.uid + sendGiftEntity.gift_id;
      if (tem_id.equals(playingGifID1) || tem_id.equals(playingGifID2)) {
         //都在播放中，不缓存，因为此entity会被消费掉
         return;
      }
      addCacheGift(sendGiftEntity);
   }

   private LinkedList<SendGiftEntity> cache_giftList = new LinkedList<SendGiftEntity>();

   //显示缓存的连动画
   public void hasAnyCacheGift() {
      if (cache_giftList.size() > 0) {
         showGiftNotify(cache_giftList.removeFirst());
      }
   }

   public void addCacheGift(SendGiftEntity giftEntity) {
      for (SendGiftEntity tem : cache_giftList) {
         if (giftEntity.gift_id == tem.gift_id && giftEntity.uid.equals(tem.uid)) {
            //已经存在相同类型
            return;
         }
      }
      cache_giftList.addLast(giftEntity);
   }
//////////////////////////////////////////////////////////////////////////////////////////
   //Weibo分享授权回调

   /**
    * 当 SSO 授权 Activity 退出时，该函数被调用。
    *
    * @see {@link Activity#onActivityResult}
    */
   @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      try {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareDialog.qq_IUiListener);
         }
         if (requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data,
                shareDialog.qzone_IUiListener);
         }
         // SSO 授权回调
         // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
         if (shareDialog != null && shareDialog.mSsoHandler != null) {
            shareDialog.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
         }
      } catch (Exception e) {
      }
   }

   ////////////////////////////////////////////////////////////////////////////
   //公聊相关
   public void doRoomChatEnter() {
      /**
       * 加入聊天室。
       *
       * @param chatroomId      聊天室 Id。
       * @param defMessageCount 进入聊天室拉取消息数目，为 -1 时不拉取任何消息，默认拉取 10 条消息。
       * @param callback        状态回调。
       */

      RongIMClient.getInstance()
          .joinChatRoom("" + AULiveApplication.currLiveUid, -1,
              new RongIMClient.OperationCallback() {
                 @Override public void onSuccess() {
                    Trace.d("joinChatRoom onSuccess");
                 }

                 @Override public void onError(RongIMClient.ErrorCode errorCode) {
                    Trace.d("joinChatRoom onError");
                    //Utils.showCroutonText(AvActivity.this, "加入房间失败");
                 }
              });
      //自己发送的内容监听
      RongIM.getInstance().setSendMessageListener(mySendMessageListener);
      //RongIM.getInstance().setOnReceiveMessageListener(myReceiveMessageListener);
      //获取在线人数
      //RongIMClient.getInstance()
      //    .getChatRoomInfo(mHostIdentifier, 1,
      //        ChatRoomInfo.ChatRoomMemberOrder.RC_CHAT_ROOM_MEMBER_DESC,
      //        new RongIMClient.ResultCallback<ChatRoomInfo>() {
      //           @Override public void onSuccess(ChatRoomInfo chatRoomInfo) {
      //              Trace.d(
      //                  "chatRoomInfo.getTotalMemberCount():" + chatRoomInfo.getTotalMemberCount());
      //              first_get_online_num = chatRoomInfo.getTotalMemberCount();
      //              txt_usernum.setText("" + first_get_online_num);
      //           }
      //
      //           @Override public void onError(RongIMClient.ErrorCode errorCode) {
      //
      //           }
      //        });
   }

   public void onEvent(RongReceiveEvent rongReceiveEvent) {
      myReceiveMessageListener.onReceived(rongReceiveEvent.message, rongReceiveEvent.left);
   }

   public MySendMessageListener mySendMessageListener = new MySendMessageListener();
   private MyReceiveMessageListener myReceiveMessageListener = new MyReceiveMessageListener();

   public void doRoomChatExit() {
      try {
         if (RongIM.getInstance().getRongIMClient() == null) {
            return;
         }
         RongIM.getInstance()
             .getRongIMClient()
             .quitChatRoom(AULiveApplication.currLiveUid, new RongIMClient.OperationCallback() {
                @Override public void onSuccess() {

                }

                @Override public void onError(RongIMClient.ErrorCode errorCode) {

                }
             });

         //不能为空，必挂
         //RongContext.getInstance().setOnSendMessageListener(null);
         //RongIMClientWrapper.setOnReceiveMessageListener(null);

      } catch (Exception e) {
      }
   }

   private NotificationManager mNotifyManager;
   private NotificationCompat.Builder mBuilder;

   private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

      /**
       * 收到消息的处理。
       *
       * @param message 收到的消息实体。
       * @param left 剩余未拉取消息数目。
       * @return 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
       */
      @Override public boolean onReceived(final Message message, int left) {

         //系统消息,推送
         if (message.getConversationType() == Conversation.ConversationType.SYSTEM
                 && message.getSenderUserId().equals("10000")) {
            if (message.getContent() instanceof CustomizeChatRoomMessage) {
               CustomizeChatRoomMessage push_ccrm = (CustomizeChatRoomMessage) message.getContent();
               String customText = push_ccrm.data;
               ChatMsgEntity chatMsgEntity =
                       JsonParser.deserializeByJson(customText, ChatMsgEntity.class);
               if (chatMsgEntity.type.equals("tips")) {
                  //PushNotificationMessage pushMsg =
                  //    PushNotificationMessage.obtain(chatMsgEntity.content,
                  //        Conversation.ConversationType.PRIVATE, chatMsgEntity.uid,
                  //        chatMsgEntity.nickname);
                  //PushNotificationManager.getInstance().onReceiveMessage(pushMsg, false);

                  mNotifyManager =
                          (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                  mBuilder = new NotificationCompat.Builder(AULiveApplication.mContext);

                  String appName = getString(getApplicationInfo().labelRes);
                  int icon = getApplicationInfo().icon;
                  mBuilder.setContentTitle(appName).setSmallIcon(icon);
                  mBuilder.setTicker(chatMsgEntity.content);
                  mBuilder.setContentText(chatMsgEntity.content);
                  //| Notification.DEFAULT_VIBRATE
                  mBuilder.setDefaults(Notification.DEFAULT_SOUND);

                  Intent homePageIntent =
                          new Intent(AULiveApplication.mContext, HomePageActivity.class);
                  homePageIntent.putExtra(HomePageActivity.HOMEPAGE_UID, chatMsgEntity.uid);
                  homePageIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                  homePageIntent.setFlags(
                          Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                  PendingIntent pendingIntent =
                          PendingIntent.getActivity(AULiveApplication.mContext, 0, homePageIntent,
                                  PendingIntent.FLAG_UPDATE_CURRENT);
                  mBuilder.setContentIntent(pendingIntent);
                  Notification noti = mBuilder.build();
                  noti.flags = Notification.FLAG_AUTO_CANCEL;
                  int notify_id = 0;
                  try {
                     notify_id = Integer.parseInt(chatMsgEntity.uid);
                  } catch (Exception e) {

                  }
                  mNotifyManager.notify((int) notify_id, noti);

                  return true;
               }
            }
            return false;
         }

         //开发者根据自己需求自行处理
         //不是这房间的消息不接收
         if (message.getConversationType() != Conversation.ConversationType.CHATROOM
                 || !message.getTargetId().equals(AULiveApplication.currLiveUid)
                 || message.getTargetId().equals("10000")) {
            return false;
         }

         //显示收到的内容
         if (message.getContent() instanceof CustomizeChatRoomMessage) {
            if (mListViewMsgItems == null) {
               return false;
            }
            mListViewMsgItems.post(new Runnable() {
               @Override public void run() {
                  handleCustomMsg((CustomizeChatRoomMessage) message.getContent());
               }
            });
         }

         //消息分级的礼物
         if (message.getContent() instanceof GiftChatRoomMessage) {
            if (mListViewMsgItems == null) {
               return false;
            }
            mListViewMsgItems.post(new Runnable() {
               @Override public void run() {
                  GiftChatRoomMessage giftChatRoomMessage =
                          (GiftChatRoomMessage) message.getContent();
                  CustomizeChatRoomMessage customizeChatRoomMessage =
                          new CustomizeChatRoomMessage();
                  customizeChatRoomMessage.type = giftChatRoomMessage.type;
                  customizeChatRoomMessage.data = giftChatRoomMessage.data;
                  handleCustomMsg(customizeChatRoomMessage);
               }
            });
         }
         //消息分级的系统消息
         if (message.getContent() instanceof SystemChatRoomMessage) {
            if (mListViewMsgItems == null) {
               return false;
            }
            mListViewMsgItems.post(new Runnable() {
               @Override public void run() {
                  SystemChatRoomMessage systemChatRoomMessage =
                          (SystemChatRoomMessage) message.getContent();
                  CustomizeChatRoomMessage customizeChatRoomMessage =
                          new CustomizeChatRoomMessage();
                  customizeChatRoomMessage.type = systemChatRoomMessage.type;
                  customizeChatRoomMessage.data = systemChatRoomMessage.data;
                  handleCustomMsg(customizeChatRoomMessage);
               }
            });
         }

         if (message.getContent() instanceof CustomizeMsgQueueMessage) {
            if (mListViewMsgItems == null) {
               return false;
            }

            CustomizeMsgQueueMessage msg_queue = (CustomizeMsgQueueMessage) message.getContent();
            try {

               //Trace.d("msg_queue.data=" + msg_queue.data);
               JSONObject dataObject = new JSONObject(msg_queue.data);
               JSONArray list = dataObject.optJSONArray("list");
               JSONArray newList = new JSONArray();
               for (int i = 0; i < list.length(); ++i) {
                  JSONObject obj = list.getJSONObject(i);
                  obj.putOpt("data", obj.optJSONObject("data").toString());
                  newList.put(obj);
               }
               dataObject.putOpt("list", newList);

               //String dataString = dataObject.toString();
               //Trace.d(list.toString());
               CustomizeChatRoomMessageList custom_msg_list =
                       JsonParser.deserializeByJson(dataObject.toString(),
                               CustomizeChatRoomMessageList.class);
               //Trace.d("=++++" + custom_msg_list.toString());
               //Trace.d("=======" + custom_msg_list.getList().toString());
               for (final CustomizeChatRoomMessage msg : custom_msg_list.getList()) {
                  mListViewMsgItems.post(new Runnable() {
                     @Override public void run() {
                        long time = System.currentTimeMillis();
                        handleCustomMsg(msg);
                        Trace.d("handleCustomMsg time:" + (System.currentTimeMillis() - time));
                     }
                  });
               }
            } catch (Exception e) {
               e.printStackTrace();
            }
         }

         //上麦相关消息
         if (message.getContent() instanceof CustomizeRCTMessage) {
            if (mListViewMsgItems == null) {
               return false;
            }
         }

         return false;
      }
   }

   //判断微信号正则
   private boolean matchPhone(String text) {
      if (Pattern.compile("^[a-zA-Z0-9_]{5,}$").matcher(text).matches()) {
         return true;
      }
      return false;
   }

   public class UIDAndTime {
      public UIDAndTime(String uid1, long time1) {
         uid = uid1;
         time = time1;
      }

      public String uid;
      public long time;
   }

   private ArrayList<UIDAndTime> adv_filter_arraylist = new ArrayList<UIDAndTime>();
   private ArrayList<String> adv_filter_uidlist = new ArrayList<String>();

   //去广告
   private void doWeixinGG(String memo) {

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(
             UrlHelper.SERVER_URL + "other/adfilter?memo=" + URLEncoder.encode(memo, "utf-8"));
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<EnterRoomEntity>() {

         @Override public void onCallback(EnterRoomEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showCroutonText(AvActivity.this, callback.getMsg());
            } else {
               Utils.showCroutonText(AvActivity.this, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(EnterRoomEntity.class));
      request.execute();
   }

   //自己的发送监听器
   public class MySendMessageListener implements RongIM.OnSendMessageListener {

      /**
       * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
       *
       * @param message 发送的消息实例。
       * @return 处理后的消息实例。
       */
      @Override public Message onSend(final Message message) {

         if (message == null || message.getContent() == null) {
            return message;
         }
         ////////////////////////////////////////////////////////
         //   广告处理
         if (message.getContent() instanceof TextMessage) {//文本消息
            if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {
               TextMessage textMessage = (TextMessage) message.getContent();
               try {
                  //判断广告
                  int grade = Integer.parseInt(AULiveApplication.getUserInfo().getGrade());
                  String msg = textMessage.getContent();
                  if (msg == null) {
                     return null;
                  }
                  Trace.d("****" + "接收到了信息,准备进行过滤" + msg);
                  FilteredResult res = WordFilterUtil.filterText(msg, '*');
                  msg = res.getFilteredContent().toString();
                  Trace.d("****" +  "过滤后" + msg);
                  textMessage.setContent(msg);
                  //////////////////
                  if (grade < 5) {
                     //去微信
                     adv_filter_arraylist.add(
                         new UIDAndTime(message.getTargetId(), System.currentTimeMillis()));
                     //不存在才加入，加入时判断，算法最简单,方法1
                     if (!adv_filter_uidlist.contains(message.getTargetId())) {
                        adv_filter_uidlist.add(message.getTargetId());
                     }

                     //1分钟发6次不同用户，包含微信，等级小于5
                     //Trace.d("adv_filter_uidlist:"+adv_filter_uidlist.size());
                     if (adv_filter_arraylist.size() > 5) {
                        long last_time =
                            adv_filter_arraylist.get(adv_filter_arraylist.size() - 1).time;
                        long first_time =
                            adv_filter_arraylist.get(adv_filter_arraylist.size() - 6).time;
                        //1分钟之内
                        if (last_time - first_time < 60 * 1000) {
                           //包含6次不同用户
                           if (adv_filter_uidlist.size() > 5) {
                              Trace.d("有广告嫌疑");
                              //有广告嫌疑
                              doWeixinGG(msg);
                           }
                        }
                     }
                     //清空
                     if (adv_filter_arraylist.size() > 8) {
                        adv_filter_arraylist.clear();
                        adv_filter_uidlist.clear();
                     }
                  }

                  String clipBoardText = getClipboardText();
                  if (clipBoardText != null && !clipBoardText.equals("")) {

                     //剪切版内容发3次以上
                     {
                        if (clipBoardText.equals(msg)) {
                           AULiveApplication.adv_count++;
                           //3次以上是广告及5级以下
                           if (AULiveApplication.adv_count > 3 && grade < 6) {
                              return null;
                           }
                        }
                     }
                  }
               } catch (Exception e) {
               }
            }
         }
         /////////////////////////////////////////////////////////////
         if (mListViewMsgItems == null) {
            return message;
         }
         //开发者根据自己需求自行处理逻辑
         //String json_s=  JsonParser.serializeToJson(message);
         //  Trace.d(json_s);
         //发送前把自己发送的内容也在本地显示一遍
         if (message.getContent() instanceof CustomizeChatRoomMessage) {
            mListViewMsgItems.post(new Runnable() {
               @Override public void run() {
                  handleCustomMsg((CustomizeChatRoomMessage) message.getContent());
               }
            });
         }
         if (message.getContent() instanceof CustomizeMsgQueueMessage) {
            mListViewMsgItems.post(new Runnable() {
               @Override public void run() {
                  CustomizeMsgQueueMessage msg_queue =
                      (CustomizeMsgQueueMessage) message.getContent();
                  try {
                     CustomizeChatRoomMessageList custom_msg_list =
                         JsonParser.deserializeByJson(msg_queue.data,
                             CustomizeChatRoomMessageList.class);

                     for (CustomizeChatRoomMessage msg : custom_msg_list.getList()) {
                        handleCustomMsg(msg);
                     }
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
               }
            });
         }
         return message;
      }

      /**
       * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
       *
       * @param message 消息实例。
       * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
       * @return true 表示走自已的处理方式，false 走融云默认处理方式。
       */
      @Override public boolean onSent(Message message,
          RongIM.SentMessageErrorCode sentMessageErrorCode) {

         if (message.getSentStatus() == Message.SentStatus.FAILED) {
            if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
               //不在聊天室
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
               //不在讨论组
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
               //不在群组
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
               //你在他的黑名单中
            }
         }

         MessageContent messageContent = message.getContent();

         if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Trace.d("onSent-TextMessage:" + textMessage.getContent());
         } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Trace.d("onSent-ImageMessage:" + imageMessage.getRemoteUri());
         } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Trace.d("onSent-voiceMessage:" + voiceMessage.getUri().toString());
         } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Trace.d("onSent-RichContentMessage:" + richContentMessage.getContent());
         } else {
            Trace.d("onSent-其他消息，自己来判断处理");
         }

         return false;
      }
   }

   //////////////////////////////////////
   // 私聊相关
   //聊天对话list
   public PrivateChatListHelper privateChatListHelper;
   //聊天界面
   public PrivateChatHelper privateChatHelper;

   private void initRoomPrivateChats() {
      privateChatHelper = new PrivateChatHelper(AvActivity.this);
      privateChatListHelper = new PrivateChatListHelper(AvActivity.this, privateChatHelper);
   }

   //管理员列表fragment
   public ManagerListFragment managerListFragment;

   //弹幕相关
   private IDanmakuView mDanmakuView;
   private DanmuControl mDanmuControl;

   private void initDanmu() {
      mDanmuControl = new DanmuControl(this);
      mDanmakuView = (IDanmakuView) findViewById(R.id.danmakuView);
      mDanmuControl.setDanmakuView(mDanmakuView);
   }

   ///views重复用
   //没有使用
   private LinkedList<View> imageViews_cache_no_use = new LinkedList<View>();
   //使用中
   private LinkedList<View> imageViews_cache_in_use = new LinkedList<View>();

   public View getDanMuView() {
      if (imageViews_cache_no_use.size() < 1) {
         View danmuView = LayoutInflater.from(AvActivity.this).inflate(R.layout.danmu_layout, null);
         imageViews_cache_in_use.addLast(danmuView);
         return danmuView;
      }
      View no_use_iv = imageViews_cache_no_use.removeFirst();
      imageViews_cache_in_use.addLast(no_use_iv);
      return no_use_iv;
   }

   public void addDanMu(final UserBarrageEntity entity) {

      //你改一个地方就行了，把DanmakuView的private boolean
      // mEnableDanmakuDrwaingCache ;改为false，就不用加载那个so库了
      final View danMuView = getDanMuView();
      Trace.d("imageViews_cache_no_use.size():" + imageViews_cache_no_use.size());
      Trace.d("imageViews_cache_in_use.size():" + imageViews_cache_in_use.size());
      TextView txt_tip = (TextView) danMuView.findViewById(R.id.txt_tip);
      txt_tip.setText(entity.chat_msg);

      TextView txt_username = (TextView) danMuView.findViewById(R.id.txt_username);
      txt_username.setText(entity.nickname);

      ImageView user_portrait = (ImageView) danMuView.findViewById(R.id.user_portrait);

      if (entity.offical == 1) {
         danMuView.findViewById(R.id.offical_tag_iv).setVisibility(View.VISIBLE);
      } else {
         danMuView.findViewById(R.id.offical_tag_iv).setVisibility(View.INVISIBLE);
      }
      ImageLoader.getInstance()
          .displayImage(entity.face, user_portrait, AULiveApplication.getGlobalImgOptions(),
              new ImageLoadingListener() {
                 @Override public void onLoadingStarted(String s, View view) {

                 }

                 @Override public void onLoadingFailed(String s, View view, FailReason failReason) {

                 }

                 @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    if (danMuView == null) {
                       return;
                    }

                    new Thread(new Runnable() {
                       @Override public void run() {
                          try {
                             // 获取bitmap
                             danMuView.setDrawingCacheEnabled(true);

                             //if (localView.getLayoutParams() == null) {
                             //   localView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
                             //}
                             danMuView.measure(
                                 View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                 View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                             danMuView.layout(0, 0, danMuView.getMeasuredWidth(),
                                 danMuView.getMeasuredHeight());
                             danMuView.destroyDrawingCache();
                             danMuView.buildDrawingCache();
                             Bitmap danmu_bitmap = danMuView.getDrawingCache();
                             mDanmuControl.addDanmu(danmu_bitmap, entity);

                             imageViews_cache_in_use.remove(danMuView);
                             imageViews_cache_no_use.addLast(danMuView);
                          } catch (Exception e) {
                          }
                       }
                    }).start();
                 }

                 @Override public void onLoadingCancelled(String s, View view) {

                 }
              });
   }

   @Override public void onResponse(BaseResponse baseResp) {
      Trace.d("AvActivity onResponse");
      switch (baseResp.errCode) {
         case WBConstants.ErrorCode.ERR_OK:
            Utils.showCroutonText(AvActivity.this, Utils.trans(R.string.weibosdk_share_success));
            shareDialog.doWeiboShareCallback();
            break;
         case WBConstants.ErrorCode.ERR_CANCEL:
            Utils.showCroutonText(AvActivity.this, Utils.trans(R.string.weibosdk_share_canceled));
            break;
         case WBConstants.ErrorCode.ERR_FAIL:
            Utils.showCroutonText(AvActivity.this, Utils.trans(R.string.weibosdk_share_failed));
            break;
      }
   }

   private CustomProgressDialog progressDialog = null;

   public void startProgressDialog() {
      if (progressDialog == null) {
         progressDialog = CustomProgressDialog.createDialog(this);
         progressDialog.setMessage("加载中");
      }

      progressDialog.show();
   }

   public void stopProgressDialog() {
      if (progressDialog != null) {
         progressDialog.dismiss();
         progressDialog = null;
      }
   }

   @Override public void finish() {
      super.finish();
      Trace.d("AvActivity finsish()");
   }

   //超管相关
   public boolean is_super_manager = false;
   RelativeLayout super_manager_ly;

   public void showSuperMangerDialog(final String mHostIdentifier, String msg, final int type) {

      CustomDialog userBlackDialog = new CustomDialog(this, new CustomDialogListener() {

         @Override public void onDialogClosed(int closeType) {

            switch (closeType) {
               case CustomDialogListener.BUTTON_POSITIVE:
                  doSuperManager(mHostIdentifier, type);
                  break;
               case CustomDialogListener.BUTTON_NEUTRAL:

                  break;
            }
         }
      });

      userBlackDialog.setCustomMessage("确认操作:" + msg);
      userBlackDialog.setCancelable(true);
      userBlackDialog.setType(CustomDialog.DOUBLE_BTN);
      userBlackDialog.setCanceledOnTouchOutside(true);
      userBlackDialog.show();
   }

   public void doSuperManager(String mHostIdentifier, final int type) {
      if (mHostIdentifier == null) {
         return;
      }
      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(
             UrlHelper.SERVER_URL + "manage/forbid?liveuid=" + mHostIdentifier + "&type=" + type);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<EnterRoomEntity>() {

         @Override public void onCallback(EnterRoomEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               Utils.showCroutonText(AvActivity.this, "成功处理");
               if (type == 7) {
                  sendSystemMsg(SYSTEM_NOTICE_ROOM, "警告！直播时请遵守社区文明公约，裸露、挂机、黑屏、不露脸等违规内容。", 1);
               }
               if (type == 8) {
                  sendSystemMsg(SYSTEM_NOTICE_ROOM, "警告！直播时请遵守社区文明公约，裸露、挂机、黑屏、不露脸等违规内容。", 0);
               }
            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(EnterRoomEntity.class));
      request.execute();
   }

   ////////////////////////////////////
   //uid time
   private HashMap<String, Long> enter_room_effects_time = new HashMap<String, Long>();
   private LinkedList<EnterEntity> cache_enter_room_List = new LinkedList<EnterEntity>();

   ///进房动画
   public synchronized void doEnterRoomEffects(EnterEntity enterEntity) {
      //10分钟只显示一次
      if (enter_room_effects_time.containsKey(enterEntity.uid)) {
         long duration = System.currentTimeMillis() - enter_room_effects_time.get(enterEntity.uid);
         if (duration < 10 * 60 * 1000) {
            return;
         }
      }
      enter_room_effects_time.put(enterEntity.uid, System.currentTimeMillis());

      if (!EnterRoomEffectsUtil.is_showing_enter_room_effects) {
         EnterRoomEffectsUtil.getInstance(AvActivity.this).showEnterRoomEfct(enterEntity);
      } else {
         addCacheEnterRoomEffect(enterEntity);
      }
   }

   public void hasAnyEnterRoomEffect() {
      if (cache_enter_room_List.size() > 0) {
         EnterEntity enterEntity = cache_enter_room_List.removeFirst();
         //先把enter_room_effects_time记录去掉，因为还没显示过， enter_room_effects_time是缓存已经显示过的，去掉不然同时进入会有问题
         if (enter_room_effects_time.containsKey(enterEntity.uid)) {
            enter_room_effects_time.remove(enterEntity.uid);
         }
         doEnterRoomEffects(enterEntity);
      }
   }

   public void addCacheEnterRoomEffect(EnterEntity enterEntity) {
      cache_enter_room_List.addLast(enterEntity);
   }

   /////////////
   //关注接口
   private void doAttend(final String uid) {
      RequestInformation request = new RequestInformation(UrlHelper.ROOM_ADD_ATTEN + "?u=" + uid,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               Utils.showCroutonText(AvActivity.this, "关注成功");

               XGPushManager.setTag(AvActivity.this, uid);

               if (MainActivity.atten_uids.contains(uid)) {
               } else {
                  MainActivity.atten_uids.add(uid);
               }
               //判断是不是关注主播，是则发送消息
               //if (uid.equals(mHostIdentifier)) {
               //   AvActivity.this.sendAttenMsg();
               //}
               //dialgoListAdapter.notifyDataSetChanged();

               sendAttenMsg();

               if (bt_attend != null) {
                  bt_attend.setVisibility(View.GONE);
               }

               if (atten_timer != null) {
                  atten_timer.cancel();
                  atten_timer = null;
               }
            } else {
               Utils.showCroutonText(AvActivity.this, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   /////////////////////////////////////////////////////////////
   //视屏回放相关
   private LinearLayout mPlayerPanel;
   private ImageView mPlayerStartBtn;
   private SeekBar mPlayerSeekbar;
   private TextView mPlayerPosition;

   private boolean mPlayerPanelShow = true;
   private boolean mPause = false;

   private long mStartTime = 0;
   private long mPauseStartTime = 0;
   private long mPausedTime = 0;

   public static final int UPDATE_SEEKBAR = 0;
   public static final int HIDDEN_SEEKBAR = 1;
   //硬件信息相关
   public static final int UPDATE_QOS = 2;
   boolean is_record_play = false;
   private String record_id;
   private UIHandler mHandler_playback;

   private class UIHandler extends Handler {

      AvActivity mActivtiy;

      public UIHandler(AvActivity activty) {
         mActivtiy = activty;
      }

      @Override public void handleMessage(android.os.Message msg) {
         switch (msg.what) {
            case UPDATE_SEEKBAR:
               if (mActivtiy != null) {
                  mActivtiy.setVideoProgress(0);
               }
               break;
            case HIDDEN_SEEKBAR:
               if (mActivtiy != null) {
                  mActivtiy.mPlayerPanelShow = false;
                  mActivtiy.mPlayerPanel.setVisibility(View.GONE);
               }
               break;
            case UPDATE_QOS:
               //更新硬件信息
               //if(mActivtiy != null && msg.obj instanceof QosObject) {
               //   mActivtiy.updateQosInfo((QosObject)msg.obj);
               //}
               break;
            case NO_MONEY:
               onMemberExit();
               break;
            case OVER_TIME:
               onMemberExit();
               break;
         }
      }
   }

   //更新进度条
   public int setVideoProgress(int currentProgress) {

      if (ksyMediaPlayer == null) {
         return -1;
      }

      long time = currentProgress > 0 ? currentProgress : ksyMediaPlayer.getCurrentPosition();
      long length = ksyMediaPlayer.getDuration();

      // Update all view elements
      mPlayerSeekbar.setMax((int) length);
      mPlayerSeekbar.setProgress((int) time);

      if (time >= 0) {
         String progress = Strings.millisToString(time) + "/" + Strings.millisToString(length);
         mPlayerPosition.setText(progress);
      }

      android.os.Message msg = new android.os.Message();
      msg.what = UPDATE_SEEKBAR;

      if (mHandler_playback != null) {
         mHandler_playback.sendMessageDelayed(msg, 1000);
      }
      return (int) time;
   }

   private View.OnClickListener mStartBtnListener = new View.OnClickListener() {
      @Override public void onClick(View v) {
         mPause = !mPause;
         if (mPause) {
            mPlayerStartBtn.setBackgroundResource(R.drawable.room_btn_zanting);
            ksyMediaPlayer.pause();
            mPauseStartTime = System.currentTimeMillis();
         } else {
            mPlayerStartBtn.setBackgroundResource(R.drawable.room_btn_bofang);
            ksyMediaPlayer.start();
            mPausedTime += System.currentTimeMillis() - mPauseStartTime;
            mPauseStartTime = 0;
         }
      }
   };

   private int mVideoProgress = 0;
   private SeekBar.OnSeekBarChangeListener mSeekBarListener =
       new SeekBar.OnSeekBarChangeListener() {
          @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
             if (fromUser) {
                mVideoProgress = progress;
             }
          }

          @Override public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override public void onStopTrackingTouch(SeekBar seekBar) {
             ksyMediaPlayer.seekTo(mVideoProgress);
             setVideoProgress(mVideoProgress);
          }
       };

   // Maybe we could support gesture detect
   //点击隐藏回放按钮
   private void dealTouchEvent(View view, MotionEvent event) {
      mPlayerPanelShow = !mPlayerPanelShow;

      if (mPlayerPanelShow) {
         mPlayerPanel.setVisibility(View.VISIBLE);

         android.os.Message msg = new android.os.Message();
         msg.what = HIDDEN_SEEKBAR;
         mHandler_playback.sendMessageDelayed(msg, 3000);
      } else {
         mPlayerPanel.setVisibility(View.GONE);
         mHandler_playback.removeMessages(HIDDEN_SEEKBAR);
      }
   }
   /////////////////////////////////////////////////////////
   //重连相关
}