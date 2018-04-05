package com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Parcel;
import android.view.View;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.FastBlurUtil;
import com.jack.utils.ScreenUtils;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalListEvent;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVEntity;
import com.ilikezhibo.ggzb.avsdk.home.EnterRoomEntity;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by big on 8/25/16.
 */
public class FlipRoomHelper {

   private AvActivity mActivity;

   ArrayList<AVEntity> datas;

   public FlipRoomHelper(AvActivity mActivity1) {
      mActivity = mActivity1;
      init();
   }

   public WatchSwitchViewPager mWatchSwitchViewPager;
   private TouchDelegateView mTouchDelegateView;
   private FlipRelativelayout mTouchDelegateView0;

   private FlipLinearLayout mTouchDelegateView1;
   private FlipLinearLayout mTouchDelegateView2;
   private float[] mNeedHideOriginTx;
   private View[] mNeedHideViews;

   private FlipRelativelayout av_activity_main_view;

   //要移动的view
   private View player_surface;
   private View loading_bg;
   private View body_flipper;
   private View super_manager_ly;
   private View tv_live_uid;
   private View fullscreen_layout;

   protected FlipRoomHelper(Parcel in) {
      mNeedHideOriginTx = in.createFloatArray();
      canTouch = in.readByte() != 0;
   }






   private void init() {
      av_activity_main_view = (FlipRelativelayout) mActivity.findViewById(R.id.av_screen_layout);
      Trace.d("**>>>初始化fliproomhelper");
      player_surface = mActivity.findViewById(R.id.player_surface);
      loading_bg = mActivity.findViewById(R.id.loading_bg);
      body_flipper = mActivity.findViewById(R.id.body_flipper);
      super_manager_ly = mActivity.findViewById(R.id.super_manager_ly);
      tv_live_uid = mActivity.findViewById(R.id.tv_live_uid);
      fullscreen_layout = mActivity.findViewById(R.id.fullscreen_layout);

      //创建ViewPager
      this.mWatchSwitchViewPager =
          (WatchSwitchViewPager) mActivity.findViewById(R.id.flip_viewpager);
      mWatchSwitchViewPager.status_screen_height = getScreenHeight();

      LiveListEvent localLiveListEvent = EventBus.getDefault().getStickyEvent(LiveListEvent.class);
      if (localLiveListEvent != null) {
         datas = new ArrayList<AVEntity>();
         for (AVEntity avEntity : localLiveListEvent.liveList) {
            if (avEntity.uid != null && !avEntity.uid.equals("")) {
               datas.add(avEntity);
            }
         }
      }

      EventBus.getDefault().removeStickyEvent(LiveListEvent.class);

      this.mWatchSwitchViewPager.setData(datas, AULiveApplication.currLiveUid);
      this.mWatchSwitchViewPager.setWatchSwitchListener(
          new WatchSwitchViewPager.WatchSwitchListener() {
             @Override public void switchAnchor(AVEntity paramLiveShow) {
                Trace.d("**>>>传入了下一个房间的参数");
                doPlayerSwitch(paramLiveShow);
             }
          });
      this.mWatchSwitchViewPager.setWatchSwitchViewPagerScrollListener(
          new WatchSwitchViewPager.WatchSwitchViewPagerScrollListener() {
             @Override public void drag() {
                Trace.d("**>滑动");
             }

             @Override public void end() {
                Trace.d("**>滑动结束");
             }

             @Override public void idle() {
                dragScroll(0);
             }

             @Override public void scroll(int paramInt) {
                dragScroll(paramInt);
             }
          });

      mNeedHideViews = new View[1];
      mNeedHideViews[0] = av_activity_main_view;
      mNeedHideOriginTx = new float[this.mNeedHideViews.length];

      for (int j = 0; j < this.mNeedHideViews.length; ++j) {
         View localView = this.mNeedHideViews[j];
         if (localView != null) {
            this.mNeedHideOriginTx[j] = localView.getTranslationX();
         }
      }

      mTouchDelegateView0 = ((FlipRelativelayout) mActivity.findViewById(R.id.av_screen_layout));

      mTouchDelegateView = ((TouchDelegateView) mActivity.findViewById(R.id.touch_delegate_view));
      mTouchDelegateView.setDelegate(this.mWatchSwitchViewPager);
      mTouchDelegateView.setDelegate2(mTouchDelegateView0);

      mTouchDelegateView1 = ((FlipLinearLayout) mActivity.findViewById(R.id.touch_delegate_view1));
      //mTouchDelegateView1.setDelegate(this.mWatchSwitchViewPager);
      //mTouchDelegateView1.setOnTouchListener(mActivity);

      mTouchDelegateView2 = ((FlipLinearLayout) mActivity.findViewById(R.id.touch_delegate_view2));
      //mTouchDelegateView2.setDelegate(this.mWatchSwitchViewPager);
      //mTouchDelegateView.setDelegate2(av_activity_main_view);
   }

   public void dragScroll(int positionOffsetPixels) {

      if (positionOffsetPixels >= 25) {
         positionOffsetPixels = positionOffsetPixels - 25;
      }

      if (canTouch) {
         //Trace.d("dragScroll:" + positionOffsetPixels);
         //相对其左上角
         av_activity_main_view.setTranslationY(positionOffsetPixels);

         //player_surface.setTranslationY(positionOffsetPixels);
         //  loading_bg.setTranslationY(positionOffsetPixels);
         //body_flipper.setTranslationY(positionOffsetPixels);
         // super_manager_ly.setTranslationY(positionOffsetPixels);
         //   tv_live_uid.setTranslationY(positionOffsetPixels);
         // fullscreen_layout.setTranslationY(positionOffsetPixels);

         //int j = (int)(0.60000002384185791016F * paramInt);
         //this.mPlayerView.setTranslationY(j);
      }
   }

   private int getStatusBarHigh() {
      Rect frame = new Rect();
      mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
      int statusBarHeight = frame.top;
      return statusBarHeight;
   }

   private int getScreenHeight() {
      //int screenWith = mActivity.getWindow().getWindowManager().getDefaultDisplay().getWidth();
      //int screeHeight =
      //    mActivity.getWindow().getWindowManager().getDefaultDisplay().getHeight() - dip2px(
      //        mActivity, 18);
      int screeHeight = ScreenUtils.getScreenHeight(mActivity) - dip2px(mActivity, 12);
      //int screeHeight=TinkerManager.getTinkerApplicationLike().getApplication().getResources().getDisplayMetrics().heightPixels - dip2px(mActivity, 12);
      return screeHeight;
   }

   public static int dip2px(Context context, float dpValue) {
      final float scale = context.getResources().getDisplayMetrics().density;
      return (int) (dpValue * scale + 0.5f);
   }

   //切换主播后处理
   private void doPlayerSwitch(AVEntity paramLiveShow) {
      Trace.d("**>调用切换到下一个房间里面");
      //退出房间php接口，不然每个房间都会有头像
      mActivity.onMemberFlipPage();
      //隐藏一下与暂停
      av_activity_main_view.setVisibility(View.INVISIBLE);
      mActivity.doPause();
      mActivity.doCacheDataClean();
      //设置默认加载背景
      String face = paramLiveShow.face;

      ImageLoader.getInstance().loadImage(face, new ImageLoadingListener() {
         @Override public void onLoadingStarted(String s, View view) {
         }

         @Override public void onLoadingFailed(String s, View view, FailReason failReason) {
         }

         @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            if (bitmap == null) {
               return;
            }
            mActivity.bg_imageView.setImageBitmap(FastBlurUtil.toBlur(bitmap, 10));
            mActivity.bg_imageView.setVisibility(View.VISIBLE);
         }

         @Override public void onLoadingCancelled(String s, View view) {

         }
      });

      mActivity.mVideoSurfaceView.setVisibility(View.GONE);

      enterRoom(paramLiveShow);

      ObjectAnimator objectAnimator1 =
          ObjectAnimator.ofFloat(av_activity_main_view, "alpha", 0f, 1f);
      objectAnimator1.setDuration(200);
      objectAnimator1.setStartDelay(300);
      objectAnimator1.addListener(new Animator.AnimatorListener() {
         @Override public void onAnimationStart(Animator animation) {
            av_activity_main_view.setVisibility(View.VISIBLE);
         }

         @Override public void onAnimationEnd(Animator animation) {

         }

         @Override public void onAnimationCancel(Animator animation) {

         }

         @Override public void onAnimationRepeat(Animator animation) {

         }
      });
      objectAnimator1.start();
   }


   //滑屏后，更换数据以及重连
   public void doPlayerStopAndReplay() {
      if (mActivity.ksyMediaPlayer == null) {
         return;
      }
      //View.GONE 销毁mVideoSurfaceView

      //mActivity.ksyMediaPlayer.pause();
      //mActivity.ksyMediaPlayer.release();
      //mActivity.ksyMediaPlayer.reset();

      mActivity.initOnCreate();
      mActivity.initLastViews();
      mActivity.getMemberInfo();
      // 如果是直播结束的状态，不再进行了
      if(mActivity.getVideoIsClosed()) {
         Trace.d("****发现是直播结束,不再初始avactivity");
         mActivity.stopProgressDialog();
         return;
      }
      mActivity.doResume();
      //加入房间后再发进房消息
      mActivity.onMemberEnter();

      //mActivity.initVideoPlayer();
      //mActivity.ksyMediaPlayer.start();

      mActivity.mVideoSurfaceView.setVisibility(View.GONE);
      mActivity.mVideoSurfaceView.setVisibility(View.VISIBLE);
      Trace.d("**>>>滑屏后更换数据");
   }

   //进入房间接口
   private void enterRoom(final AVEntity paramLiveShow) {
      Trace.d("**>>>调用进入房间接口");

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(UrlHelper.enterRoomUrl
             + "?liveuid="
             + paramLiveShow.uid
             + "&userid="
             + AULiveApplication.getMyselfUserInfo().getUserPhone());
         Trace.d("**>>>房间id号" + sb);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
         //request.addPostParams("roomid", liveuid + "");
         //request.addPostParams("userid", userid);
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
               try {
                  Trace.d("**>>>>得到200ok" + callback.toString());
                  MedalListEvent medalListEvent = new MedalListEvent();
                  medalListEvent.anchor_medal = callback.anchor_medal;
                  medalListEvent.wanjia_medal = callback.wanjia_medal;
                  medalListEvent.act= callback.act;
                  EventBus.getDefault().postSticky(medalListEvent);

                  Intent newIntent =
                      new Intent(mActivity, AvActivity.class).putExtra(AvActivity.GET_UID_KEY,
                          paramLiveShow.uid)
                          .putExtra(AvActivity.IS_CREATER_KEY, false)
                          .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_FACE, paramLiveShow.face)
                          .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_NICKNAME,
                              paramLiveShow.nickname)
                          .putExtra(AvActivity.EXTRA_RECIVE_DIAMOND, callback.recv_diamond)
                          .putExtra(AvActivity.EXTRA_SYS_MSG, callback.sys_msg)
                          .putExtra(AvActivity.EXTRA_IS_ON_SHOW, callback.is_live)
                          .putExtra(AvActivity.EXTRA_ONLINE_NUM, callback.total)
                          .putExtra(AvActivity.EXTRA_IS_MANAGER, callback.is_manager)
                          .putExtra(AvActivity.EXTRA_IS_GAG, callback.is_gag)
                          .putExtra(AvActivity.EXTRA_IS_SUPER_MANAGER, callback.show_manager)
                          .putExtra(AvActivity.EXTRA_play_url_KEY, paramLiveShow.url)
                          .putExtra(AvActivity.EXTRA_MSG_SEND_GRADE_CONTROL, callback.sendmsg_grade)
                          .putExtra(AvActivity.GET_GRADE_KEY, paramLiveShow.grade);

                  mActivity.setIntent(newIntent);
                  //暂停播放以及清理
                  doPlayerStopAndReplay();
               } catch (Exception e) {
                  e.printStackTrace();
                  
               }
            } else {
               Trace.d("**>>>不是200");
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(EnterRoomEntity.class));
      request.execute();
   }

   boolean canTouch = true;
   Timer timer = null;


   //public void setCanTouch(boolean canTouch1) {
   //   dragScroll(0);
   //   if (canTouch1 == true) {
   //      if (timer == null) {
   //         timer = new Timer();
   //         timer.schedule(new TimerTask() {
   //            @Override public void run() {
   //               mTouchDelegateView.setCanTouch(true);
   //            }
   //         }, 700);
   //      }
   //   } else {
   //      mTouchDelegateView.setCanTouch(false);
   //
   //      if (timer != null) {
   //         timer.cancel();
   //         timer.purge();
   //         timer = null;
   //      }
   //   }
   //}
}
