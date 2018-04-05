package com.ilikezhibo.ggzb.avsdk.userinfo.homepage;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.views.UserInfoWebViewActivity;
import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.FastBlurUtil;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.ActivityStackManager;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalLayoutHelper;
import com.ilikezhibo.ggzb.avsdk.activity.MedalListEvent;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatActivity;
import com.ilikezhibo.ggzb.avsdk.chat.room_chat.PrivateChatListAdapter;
import com.ilikezhibo.ggzb.avsdk.home.EnterRoomEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.AttenListFragment;
import com.ilikezhibo.ggzb.avsdk.userinfo.FansListFragment;
import com.ilikezhibo.ggzb.avsdk.userinfo.LivesListFragment;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.TopRankActivity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import com.ilikezhibo.ggzb.home.listener.NavigationListener;
import com.ilikezhibo.ggzb.photos.photobrowser.PicBrowseActivity;
import com.ilikezhibo.ggzb.tool.SoloMgmtUtils;
import com.ilikezhibo.ggzb.tool.SoloRequestListener;
import com.ilikezhibo.ggzb.userinfo.buydiamond.BuyDiamondActivity;
import com.ilikezhibo.ggzb.views.BadgeView;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.tencent.android.tpush.XGPushManager;

import de.greenrobot.event.EventBus;

import java.util.ArrayList;

import popwindow.PopupWindowUtil;
import pulltozoomview.PullToZoomScrollViewEx;

/**
 * @author big
 * @ClassName: HomePageFragment
 * @Description: 我的资料
 * @date 2015-3-12 下午9:49:56
 */
@SuppressLint("ValidFragment") public class HomePageFragment extends BaseFragment
    implements TitleListener, OnClickListener {
   private View root_view;
   private ImageView my_image;
   private RelativeLayout imageLayout;
   private ViewPager viewPager;
   private NavigationListener listener;

   private String uid;

   private ImageView blur_bg_iv;
   private View headView;
   private Button more_btn;
   private boolean isReported;

   private Handler handler = new Handler() {

      private String rightButton;
      private String leftButton;
      private String content;
      private String title;
      private ViewGroup toastRoot;

      @Override
      public void handleMessage(Message msg) {

          switch (msg.what) {
              case SoloMgmtUtils.NO_OPEN:
                  // >提示主播未开通1v1
                  Trace.d("**>主播没开通1v1");
                  toastRoot =
                          (ViewGroup) HomePageFragment.this.getActivity()
                                  .getLayoutInflater()
                                  .inflate(R.layout.my_toast, null);
                 if (msg.obj != null) {
                    String str = (String) msg.obj;
                    Utils.setCustomViewToast(toastRoot, false, str);
                 } else {
                    Utils.setCustomViewToast(toastRoot, false, "当前主播还没有开通1v1功能~");
                 }
                  break;

              case SoloMgmtUtils.LIVE_TRUE:
                  // >提示主播正忙
                  Trace.d("**>主播忙");
                  toastRoot = (ViewGroup) HomePageFragment.this.getActivity()
                          .getLayoutInflater()
                          .inflate(R.layout.my_toast, null);
                 if (msg.obj != null) {
                    String str = (String) msg.obj;
                    Utils.setCustomViewToast(toastRoot, false, str);
                 } else {
                    Utils.setCustomViewToast(toastRoot, false, "当前主播正在直播哦~");
                 }
                 break;

              case SoloMgmtUtils.CAN_SEND:
                  sendOneToOne();
                  break;

              case SoloMgmtUtils.NEED_PREPAID:
                 // >提示需要充值
                 title = "钻石余额不足哦";
                 content = "钻石余额不足以支持本次聊天哦, 您要前往充值吗? ";
                 leftButton = "充值";
                 rightButton = "取消";
                 showCustomDialog(title, content, leftButton, rightButton,
                         SoloMgmtUtils.NEED_PREPAID);
                  break;

              case SoloMgmtUtils.SHOW_SEND:
                  content = (String) msg.obj;
                  title = "发送视频聊天";
                  leftButton = "继续";
                  rightButton = "取消";
                 if ("520".equals(content)) {
                    content = "本次发起的 1 对 1 视频聊天将消耗您 " + SoloMgmtUtils.price + " 钻/分钟, 您确定继续发送视频聊天么?";
                    showCustomDialog(title, content, leftButton, rightButton, SoloMgmtUtils.NEED_PROMPT);
                 } else {
                    showCustomDialog(title, content, leftButton, rightButton, SoloMgmtUtils.SEND_VIDEO);
                 }
                 break;
          }
      }
   };

   public HomePageFragment() {
   }

   public HomePageFragment(String uid1) {
      uid = uid1;
   }

   public HomePageFragment(NavigationListener listener) {
      this.listener = listener;
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
      ActivityStackManager.getInstance().pushActivity(getActivity());
      root_view = inflater.inflate(R.layout.qav_homepage_layout, null);

      PullToZoomScrollViewEx scrollView =
          (PullToZoomScrollViewEx) root_view.findViewById(R.id.scroll_view);
      headView = LayoutInflater.from(HomePageFragment.this.getActivity())
          .inflate(R.layout.qav_homepage_layout_top, null, false);
      View zoomView = LayoutInflater.from(HomePageFragment.this.getActivity())
          .inflate(R.layout.jiesihuo_my_info_layout_zoom_view, null, false);
      View contentView = LayoutInflater.from(HomePageFragment.this.getActivity())
          .inflate(R.layout.qav_homepage_layout_content, null, false);
      scrollView.setHeaderView(headView);
      scrollView.setZoomView(zoomView);
      scrollView.setScrollContentView(contentView);
      DisplayMetrics localDisplayMetrics = new DisplayMetrics();
      HomePageFragment.this.getActivity()
          .getWindowManager()
          .getDefaultDisplay()
          .getMetrics(localDisplayMetrics);
      int mScreenHeight = localDisplayMetrics.heightPixels;
      int mScreenWidth = localDisplayMetrics.widthPixels;
      LinearLayout.LayoutParams localObject =
          new LinearLayout.LayoutParams(mScreenWidth, (int) (13.5F * (mScreenWidth / 16.0F)));
      scrollView.setHeaderLayoutParams(localObject);
      scrollView.setZoomEnabled(false);

      Button rl_back = (Button) root_view.findViewById(R.id.back_btn);
      rl_back.setOnClickListener(this);

      more_btn = (Button) root_view.findViewById(R.id.more_btn);
      more_btn.setOnClickListener(this);
      checkIsMe(more_btn);

      TextView tv_title = (TextView) root_view.findViewById(R.id.title);
      tv_title.setText("主页");

      TextView topRightBtn = (TextView) root_view.findViewById(R.id.topRightBtn);
      topRightBtn.setText("注册");
      topRightBtn.setOnClickListener(this);
      topRightBtn.setVisibility(View.INVISIBLE);

      initializeData();
      return root_view;
   }

   protected void initializeData() {
      imageLayout = (RelativeLayout) root_view.findViewById(R.id.imageLayout);
      imageLayout.setOnClickListener(this);
      my_image = (ImageView) root_view.findViewById(R.id.my_image);

      ImageButton ib_chat_enter = (ImageButton) root_view.findViewById(R.id.ib_chat_enter);
      BadgeView badgeView = new BadgeView(this.getContext());
      if (badgeView != null) {
         badgeView.setTargetView(ib_chat_enter);
         badgeView.setBadgeCount(3);
      }

      String my_uid = AULiveApplication.getUserInfo().getUid();
      if (uid != null && my_uid != null && uid.equals(my_uid)) {
         root_view.findViewById(R.id.bottom_ly).setVisibility(View.GONE);
         root_view.findViewById(R.id.is_live_bt).setVisibility(View.GONE);
      }
      //如果是自己，最新列表不显示
      getHomeInfo();
   }

   @Override public void onBack() {

   }

   @Override public void onTopRightEvent() {

   }

   @Override public void onClick(View v) {

      switch (v.getId()) {
         case R.id.back_btn:
            HomePageFragment.this.getActivity().finish();
            break;
         case R.id.more_btn:
            // >显示popwindow
            final PopupWindowUtil popupWindow = new PopupWindowUtil(more_btn);
            popupWindow.setContentView(R.layout.dialog_more);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
               @Override public void onDismiss() {

               }
            });
            // >显示popwindow
            popupWindow.showAlignParentRight(-10, 0);
            // >做View init
            final TextView more_lahei = (TextView) popupWindow.findId(R.id.more_lahei);
            SoloMgmtUtils.checkIsLahei(uid, new SoloRequestListener() {
               @Override
               public void onSuccess() {
                  // >在黑名单
                  more_lahei.setText("解除");
                  has_lahei = true;
                  setTextViewListener();
               }

               @Override
               public void onFailure() {
                  // >不在
                  more_lahei.setText("拉黑");
                  has_lahei = false;
                  setTextViewListener();
               }

               private void setTextViewListener() {
                  more_lahei.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View view) {
                        // >在这里拉黑
                        if (uid == null) {
                           return;
                        }
                        if (has_lahei) {
                           SoloMgmtUtils.removeLahei(uid, new SoloRequestListener() {
                              @Override
                              public void onSuccess() {
                                 Utils.showCroutonText(HomePageFragment.this.getActivity(), "解除拉黑");
                                 more_lahei.setText("拉黑");
                                 has_lahei = false;
                              }

                              @Override
                              public void onFailure() {

                              }
                           });
                        } else {
                           SoloMgmtUtils.addLahei(uid, new SoloRequestListener() {
                              @Override
                              public void onSuccess() {
                                 Utils.showCroutonText(HomePageFragment.this.getActivity(), "成功拉黑");
                                 more_lahei.setText("解除");
                                 has_lahei = true;
                              }

                              @Override
                              public void onFailure() {

                              }
                           });
                        }
                        popupWindow.dismiss();
                     }
                  });
               }
            });

            final TextView more_report = (TextView) popupWindow.findId(R.id.more_report);
            more_report.setOnClickListener(new OnClickListener() {
               @Override public void onClick(View view) {
                  // >在这里举报
                  if (uid == null) {
                     return;
                  }
                  if (isReported) {
                     Utils.showMessage("已经举报过了哦");
                  } else {
                     showPromptDialog(uid);
                  }

                  popupWindow.dismiss();
               }
            });

            TextView more_cancel = (TextView) popupWindow.findId(R.id.more_cancel);
            more_cancel.setOnClickListener(new OnClickListener() {
               @Override public void onClick(View view) {
                  popupWindow.dismiss();
               }
            });

            break;
      }
   }

   // >确认举报的对话框
   private void showPromptDialog(final String uid) {
      final CustomDialog customDialog =
          new CustomDialog(HomePageFragment.this.getActivity(), new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                      doReport(uid);
                }
             }
          });

      customDialog.setCustomMessage("确认要举报此用户吗?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      if(customDialog.isShowing() || HomePageFragment.this.getActivity().isFinishing()) {
         return;
      } else {
         customDialog.show();
      }
   }

   // >举报
   private void doReport(String uid) {
      RequestInformation request = new RequestInformation(
          UrlHelper.SERVER_URL + "live/report" + "?liveuid=" + uid + "&type=0",
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               Utils.showCroutonText(HomePageFragment.this.getActivity(), "成功举报");
               isReported = true;
            } else {
               Utils.showCroutonText(HomePageFragment.this.getActivity(), callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   @Override public void onResume() {
      super.onResume();
   }

   private boolean has_lahei;
   private LoginUserEntity userinfo;

   private void getHomeInfo() {
      RequestInformation request = null;
      if (uid == null || uid.equals("")) {
         //Utils.showMessage("uid为空");
         return;
      }
      try {
         StringBuilder sb = new StringBuilder(UrlHelper.user_home_Url + "?u=" + uid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<UserInfo>() {

         @Override public void onCallback(UserInfo callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {

               userinfo = callback.getUserinfo();

               DisplayImageOptions options =
                   new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
                       .showImageForEmptyUri(R.drawable.face_male)
                       .showImageOnFail(R.drawable.face_male)
                       .cacheInMemory()
                       .cacheOnDisc()
                       .build();

               if (userinfo != null) {
                  // http://img.yuanphone.com/http://img.yuanphone.com/face/238/12526574.jpg
                  String face = userinfo.getFace();
                  face = Utils.getImgUrl(face);
                  ImageLoader.getInstance().displayImage(face, my_image, options);

                  blur_bg_iv = (ImageView) root_view.findViewById(R.id.iv_zoom);

                  //模糊背景
                  ImageLoader.getInstance()
                      .displayImage(face, blur_bg_iv, new ImageLoadingListener() {
                         @Override public void onLoadingStarted(String s, View view) {

                         }

                         @Override
                         public void onLoadingFailed(String s, View view, FailReason failReason) {

                         }

                         @Override
                         public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            if (bitmap == null) {
                               return;
                            }
                            blur_bg_iv.setImageBitmap(FastBlurUtil.toBlur(bitmap, 10));
                         }

                         @Override public void onLoadingCancelled(String s, View view) {

                         }
                      });

                  if (face != null) {
                     my_image.setOnClickListener(new OnClickListener() {
                        @Override public void onClick(View view) {
                           String face = userinfo.getFace();
                           face = Utils.getImgUrl(face);
                           String[] urls = { face };
                           Intent intent = new Intent(HomePageFragment.this.getActivity(),
                               PicBrowseActivity.class);
                           intent.putExtra(PicBrowseActivity.INTENT_BROWSE_POS_KEY, 0);
                           intent.putExtra(PicBrowseActivity.INTENT_BROWSE_LST_KEY, urls);
                           HomePageFragment.this.getActivity().startActivity(intent);
                        }
                     });
                  }

                  TextView name = (TextView) root_view.findViewById(R.id.id_name);
                  name.setText(userinfo.getNickname());
                  final TextView id = (TextView) root_view.findViewById(R.id.id_value);
                  id.setText("ID:" + userinfo.getUid());

                  if (userinfo.goodid != 0) {
                     id.setText("ID:" + userinfo.goodid);
                     id.setTextColor(AULiveApplication.mContext.getResources()
                         .getColor(R.color.btn_bottom_send_msg_c));
                  } else {
                     id.setTextColor(
                         AULiveApplication.mContext.getResources().getColor(R.color.white));
                  }

                  id.setOnClickListener(new OnClickListener() {

                     @Override
                     public void onClick(View view) {
                        String id_content = id.getText().toString().trim();
                        id_content = id_content.substring(3);
                        if (!"".equals(id_content)) {
                           ClipboardManager cmb = (ClipboardManager) AULiveApplication.mContext
                                   .getSystemService(Context.CLIPBOARD_SERVICE);
                           cmb.setText(id_content);
                           Utils.showMessage("ID已复制到剪贴板!");
                        }
                     }
                  });

                  //等级
                  TextView tv_grade = (TextView) root_view.findViewById(R.id.grade_tv);
                  RelativeLayout grade_ly = (RelativeLayout) root_view.findViewById(R.id.grade_ly);
                  String grade = userinfo.getGrade();
                  ChatMsgListAdapter.setGradeIcon(grade, tv_grade, grade_ly);

                  if (userinfo.offical == 1) {
                     root_view.findViewById(R.id.offical_tag_iv).setVisibility(View.VISIBLE);
                  } else {
                     root_view.findViewById(R.id.offical_tag_iv).setVisibility(View.GONE);
                  }

                  ///////////////////////////////////////////////////////////
                  ArrayList<String> tem_urls = new ArrayList<String>();
                  //先添加主播勋章
                  if (userinfo.anchor_medal != null) {
                     tem_urls.addAll(userinfo.anchor_medal);
                  }
                  //再添加玩家勋章
                  if (userinfo.wanjia_medal != null) {
                     tem_urls.addAll(userinfo.wanjia_medal);
                  }
                  //把所有的子view隐藏
                  LinearLayout chat_medal_ly =
                      (LinearLayout) root_view.findViewById(R.id.chat_medal_ly);
                  for (int i = 0; i < chat_medal_ly.getChildCount(); i++) {
                     chat_medal_ly.getChildAt(i).setVisibility(View.GONE);
                  }

                  for (int i = 0; i < chat_medal_ly.getChildCount() && i < tem_urls.size(); i++) {
                     String url = tem_urls.get(i);
                     MedalLayoutHelper.showGifImage(url, HomePageFragment.this.getActivity(),
                         chat_medal_ly.getChildAt(i));
                  }
                  ///////////////////////////////////////////////////////////

                  // 设置性别
                  ImageView sex_iv = (ImageView) root_view.findViewById(R.id.sex_iv);
                  int sex = userinfo.getSex();
                  if (sex == 1) {
                     sex_iv.setImageResource(R.drawable.userinfo_male);
                  } else {
                     sex_iv.setImageResource(R.drawable.userinfo_female);
                  }
                  //认证
                  TextView txt_verify_reason =
                      (TextView) root_view.findViewById(R.id.txt_verify_reason);
                  if (userinfo.tag == null || userinfo.tag.equals("")) {
                     txt_verify_reason.setVisibility(View.GONE);
                  } else {
                     txt_verify_reason.setText("认证:" + userinfo.tag);
                  }

                  //签名
                  TextView txt_desc = (TextView) root_view.findViewById(R.id.txt_desc);

                  if (userinfo.signature == null || userinfo.signature.equals("")) {
                     txt_desc.setText("Ta好像忘记写签名了...");
                  } else {
                     txt_desc.setText(userinfo.signature);
                  }
                  //送出金币
                  TextView txt_account_inout =
                      (TextView) root_view.findViewById(R.id.txt_account_inout);
                  txt_account_inout.setText("送出:" + userinfo.send_diamond + "");

                  //是否在直播

                  Button is_live_bt = (Button) root_view.findViewById(R.id.is_live_bt);
                  if (userinfo.is_live == 1) {
                     is_live_bt.setText("在直播");
                     is_live_bt.setOnClickListener(new OnClickListener() {
                        @Override public void onClick(View view) {
                           enterRoom(userinfo.getUid(),
                               AULiveApplication.getMyselfUserInfo().getUserPhone(),
                               userinfo.getUid(), userinfo.getFace(), userinfo.getNickname());
                        }
                     });
                  } else {
                     is_live_bt.setText("未直播");
                     is_live_bt.setOnClickListener(new OnClickListener() {
                        @Override public void onClick(View view) {

                        }
                     });
                  }

                  //贡献榜处理
                  int size = 0;
                  if (userinfo.tops != null) {
                     ImageView piaopiap_top1 =
                         (ImageView) root_view.findViewById(R.id.piaopiap_top1);

                     if (userinfo.tops.size() > 0) {
                        ImageLoader.getInstance()
                            .displayImage(userinfo.tops.get(0).getFace(), piaopiap_top1, options);
                        piaopiap_top1.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View view) {
                              //MemberInfo memberInfo = new MemberInfo(userinfo.tops.get(0).getUid(),
                              //    userinfo.tops.get(0).getNickname(),
                              //    userinfo.tops.get(0).getFace());
                              //final PopupWindowUtil popupWindow = new PopupWindowUtil(root_view);
                              //popupWindow.setContentView(R.layout.dialog_myroom_userinfo);
                              //popupWindow.setOutsideTouchable(true);
                              //
                              //UserInfoHelper userInfoHelper =
                              //    new UserInfoHelper(root_view, popupWindow,
                              //        HomePageFragment.this.getActivity(), memberInfo);
                              Intent homepage_Intent =
                                  new Intent(HomePageFragment.this.getActivity(),
                                      HomePageActivity.class);
                              homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID,
                                  userinfo.tops.get(0).getUid());
                              HomePageFragment.this.getActivity().startActivity(homepage_Intent);
                           }
                        });
                     }
                     ImageView piaopiap_top2 =
                         (ImageView) root_view.findViewById(R.id.piaopiap_top2);
                     if (userinfo.tops.size() > 1) {
                        ImageLoader.getInstance()
                            .displayImage(userinfo.tops.get(1).getFace(), piaopiap_top2, options);
                        piaopiap_top2.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View view) {
                              Intent homepage_Intent =
                                  new Intent(HomePageFragment.this.getActivity(),
                                      HomePageActivity.class);
                              homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID,
                                  userinfo.tops.get(1).getUid());
                              HomePageFragment.this.getActivity().startActivity(homepage_Intent);
                           }
                        });
                     }

                     ImageView piaopiap_top3 =
                         (ImageView) root_view.findViewById(R.id.piaopiap_top3);
                     if (userinfo.tops.size() > 2) {
                        ImageLoader.getInstance()
                            .displayImage(userinfo.tops.get(2).getFace(), piaopiap_top3, options);
                        piaopiap_top3.setOnClickListener(new OnClickListener() {
                           @Override public void onClick(View view) {
                              Intent homepage_Intent =
                                  new Intent(HomePageFragment.this.getActivity(),
                                      HomePageActivity.class);
                              homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID,
                                  userinfo.tops.get(2).getUid());
                              HomePageFragment.this.getActivity().startActivity(homepage_Intent);
                           }
                        });
                     }

                     TextView tv_piaopiapTop =
                         (TextView) root_view.findViewById(R.id.tv_piaopiapTop);
                     tv_piaopiapTop.setOnClickListener(new OnClickListener() {
                        @Override public void onClick(View view) {
                           //贡献榜
                           Intent top_rank = new Intent(HomePageFragment.this.getActivity(),
                               TopRankActivity.class).putExtra(TopRankActivity.MemberInfo_key,
                               userinfo.getUid());
                           startActivity(top_rank);
                        }
                     });
                  }

                  //列表相关
                  viewPager = (ViewPager) root_view.findViewById(R.id.viewpager);

                  String[] tabs = { "关注", "粉丝", "直播" };

                  AttenListFragment attenFragment = new AttenListFragment(userinfo.getUid());
                  FansListFragment fansListFragment = new FansListFragment(userinfo.getUid());
                  LivesListFragment livesListFragment = new LivesListFragment(userinfo.getUid());

                  ArrayList fragments = new ArrayList<Fragment>();
                  fragments.add(attenFragment);
                  fragments.add(fansListFragment);
                  fragments.add(livesListFragment);

                  PrivateChatListAdapter viewpager_adapter =
                      new PrivateChatListAdapter(HomePageFragment.this.getChildFragmentManager(),
                          fragments, tabs);
                  viewPager.setOffscreenPageLimit(2);
                  viewPager.setAdapter(viewpager_adapter);

                  viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                     @Override public void onPageScrolled(int position, float positionOffset,
                         int positionOffsetPixels) {

                     }

                     @Override public void onPageSelected(int position) {
                        setSelectLine(position);
                     }

                     @Override public void onPageScrollStateChanged(int state) {

                     }
                  });

                  //关注
                  root_view.findViewById(R.id.btn_followings)
                      .setOnClickListener(new OnClickListener() {
                         @Override public void onClick(View view) {
                            //显示列表与图标
                            viewPager.setCurrentItem(0);
                         }
                      });

                  //粉丝
                  root_view.findViewById(R.id.btn_fans).setOnClickListener(new OnClickListener() {
                     @Override public void onClick(View view) {
                        //显示列表与图标
                        viewPager.setCurrentItem(1);
                     }
                  });

                  //直播
                  root_view.findViewById(R.id.btn_lives).setOnClickListener(new OnClickListener() {
                     @Override public void onClick(View view) {
                        //显示列表与图标
                        viewPager.setCurrentItem(2);
                     }
                  });

                  //关注
                  boolean has_follow = false;
                  if (MainActivity.atten_uids.contains(uid)) {
                     has_follow = true;
                  }
                  final Button btn_follow = (Button) root_view.findViewById(R.id.txt_followinlahei);
                  btn_follow.setOnClickListener(new OnClickListener() {
                     @Override public void onClick(View view) {

                        //判断是不是自己
                        if (uid.equals(AULiveApplication.getUserInfo().getUid())) {
                           Utils.showCroutonText(HomePageFragment.this.getActivity(), "不能关注自己");
                           return;
                        }

                        boolean has_follow = false;
                        if (MainActivity.atten_uids.contains(uid)) {
                           has_follow = true;
                        }
                        if (has_follow) {
                           //取消关注
                           doDelAttend(uid);
                        } else {
                           doAttend(uid);
                        }
                        if (!has_follow) {
                           btn_follow.setText("已关注");
                           //btn_follow.setTextColor(HomePageFragment.this.getActivity()
                           //    .getResources()
                           //    .getColor(R.color.red));
                           //btn_follow.setBackgroundResource(R.drawable.bg_room_popup_selected);
                        } else {
                           btn_follow.setText("关注");
                           btn_follow.setTextColor(HomePageFragment.this.getActivity()
                               .getResources()
                               .getColor(R.color.white));
                           //btn_follow.setBackgroundResource(R.drawable.bg_room_popup);
                        }
                     }
                  });
                  //按是否关注而改变
                  if (has_follow) {
                     btn_follow.setText("已关注");
                     //btn_follow.setTextColor(
                     //    HomePageFragment.this.getActivity().getResources().getColor(R.color.red));
                     //btn_follow.setBackgroundResource(R.drawable.bg_room_popup_selected);
                  } else {
                     btn_follow.setText("关注");
                     btn_follow.setTextColor(HomePageFragment.this.getActivity()
                         .getResources()
                         .getColor(R.color.white));
                     //btn_follow.setBackgroundResource(R.drawable.bg_room_popup);
                  }

                  //私聊
                  final Button txt_chat = (Button) root_view.findViewById(R.id.txt_chat);
                  txt_chat.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View view) {

//                        System.out.println("我是状态--" + AULiveApplication.getUserInfo().getPrivate_chat_status());
//                        System.out.println("是否相等" + AULiveApplication.getUserInfo().getPrivate_chat_status() == null);
//                        AULiveApplication.getUserInfo().getPrivate_chat_status();
                        if (uid.equals(AULiveApplication.getUserInfo().getUid())) {
                           Utils.showCroutonText(HomePageFragment.this.getActivity(), "不能跟自己聊天");
                           return;
                        }
                        String len = AULiveApplication.getMyselfUserInfo().getmPrivate_chat_status() + "12";
                        System.out.println("len--" + len);
                        if (len.length() > 4) {
                           Utils.showCroutonText(HomePageFragment.this.getActivity(), AULiveApplication.getMyselfUserInfo().getmPrivate_chat_status() + "!");
                           return;
                        }
                        startActivityForResult(new Intent(HomePageFragment.this.getActivity(),
                                PrivateChatActivity.class).putExtra(
                                PrivateChatActivity.STAR_PRIVATE_CHAT_BY_CODE_UID_KEY, uid), 0);
                     }
                  });
                  //如果是自己则不能私聊

                  //1对1
                  final View lahei_divider_line = root_view.findViewById(R.id.lahei_divider_line);
                  final Button txt_solo = (Button) root_view.findViewById(R.id.txt_solo);
                  checkIsMe(lahei_divider_line, txt_solo);
                  txt_solo.setOnClickListener(new OnClickListener() {

                     @Override public void onClick(View view) {
                        if (BtnClickUtils.isFastDoubleClick()) {
                           return;
                        }
                        // >是否需要显示提示对话框
                        if (SharedPreferenceTool.getInstance()
                                .getBoolean(SharedPreferenceTool.SHOW_SOLO_DIALOG, true)) {
                           SoloMgmtUtils.showDialog(HomePageFragment.this.getActivity(), handler, new SoloRequestListener() {
                              @Override
                              public void onSuccess() {
                              }

                              @Override
                              public void onFailure() {

                              }
                           });
                        } else {
                        }
                     }
                  });
               } else {
                  ImageLoader.getInstance().displayImage(null, my_image, options);
                  TextView name = (TextView) root_view.findViewById(R.id.id_name);
                  name.setText("");
                  TextView id = (TextView) root_view.findViewById(R.id.id_value);
                  id.setText("ID:" + "");
               }
            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(UserInfo.class));
      request.execute();
   }

   /**
    * 对话框
    *
    * @param title 标题内容
    * @param content 对话框内容
    * @param leftButton 左按钮
    * @param rightButton 右按钮
    * @param action 动作
    */
   private void showCustomDialog(String title, String content, String leftButton,
       String rightButton, final int action) {
      final CustomDialog customDialog =
          new CustomDialog(HomePageFragment.this.getActivity(), new CustomDialogListener() {
             @Override public void onDialogClosed(int closeType) {

                switch (closeType) {

                   case CustomDialogListener.BUTTON_POSITIVE:
                      switch (action) {

                         case SoloMgmtUtils.SEND_VIDEO:
                            // **检查其他状态
                            SoloMgmtUtils.checkAnchorOhter(uid, handler);
                            break;

                         // >需要更新
                         case SoloMgmtUtils.NEED_PROMPT:
                            Message msg = Message.obtain();
                            msg.what = SoloMgmtUtils.NEED_PREPAID;
                            handler.sendMessage(msg);
                            break;

                         // >需要充值
                         case SoloMgmtUtils.NEED_PREPAID:
                            Intent moeny_intent =
                                    new Intent(HomePageFragment.this.getActivity(), BuyDiamondActivity.class);
                            HomePageFragment.this.getActivity().startActivity(moeny_intent);
//                            Intent intent4 =
//                                    new Intent(HomePageFragment.this.getActivity(), UserInfoWebViewActivity.class);
//                            intent4.putExtra(WebViewActivity.input_url,
//                                    UrlHelper.SERVER_URL + "profile/h5charge");
//                            intent4.putExtra(WebViewActivity.back_home_key, false);
//                            intent4.putExtra(WebViewActivity.actity_name, "充值");
//                            HomePageFragment.this.getActivity().startActivity(intent4);
                            break;
                      }
                      break;

                   case CustomDialogListener.BUTTON_NEUTRAL:

                      break;
                }
             }
          });
      customDialog.setButtonText(leftButton, rightButton);
      customDialog.setCustomMessage(content);
      customDialog.setCancelable(true);
      customDialog.setAndFormatTitle(title, true, Color.parseColor("#FFFFFF"));
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      if(customDialog.isShowing() || HomePageFragment.this.getActivity().isFinishing()) {
         return;
      } else {
         customDialog.show();
      }
   }


   /**
    * 发起1v1
    */
   private void sendOneToOne() {
      io.rong.imlib.model.UserInfo userInfo =
          new io.rong.imlib.model.UserInfo(userinfo.getUid(),
              userinfo.getNickname(), Uri.parse(userinfo.getFace()));
   }

   private void checkIsMe(View view, Button button) {
      if (AULiveApplication.getUserInfo().getUid().equals(uid)) {
         //自己的主页，不显示
         button.setVisibility(View.GONE);
         view.setVisibility(View.GONE);
      } else {
         button.setVisibility(View.VISIBLE);
         view.setVisibility(View.VISIBLE);
      }
   }

   private void checkIsMe(Button button) {
      if (AULiveApplication.getUserInfo().getUid().equals(uid)) {
         //自己的主页，不显示
         button.setVisibility(View.GONE);
      } else {
         button.setVisibility(View.VISIBLE);
      }
   }

   private void doAttend(final String uid) {

      RequestInformation request = new RequestInformation(UrlHelper.ROOM_ADD_ATTEN + "?u=" + uid,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               if (MainActivity.atten_uids.contains(uid)) {
               } else {
                  MainActivity.atten_uids.add(uid);
               }
               XGPushManager.setTag(HomePageFragment.this.getActivity(), uid);

               Utils.showCroutonText(HomePageFragment.this.getActivity(), "成功关注");
            } else {
               Utils.showCroutonText(HomePageFragment.this.getActivity(), callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void doDelAttend(final String uid) {

      RequestInformation request = new RequestInformation(UrlHelper.ROOM_DEL_ATTEN + "?u=" + uid,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               if (MainActivity.atten_uids.contains(uid)) {
                  MainActivity.atten_uids.remove(uid);
               } else {

               }
               XGPushManager.deleteTag(HomePageFragment.this.getActivity(), uid);

               Utils.showCroutonText(HomePageFragment.this.getActivity(), "取消关注");
            } else {
               Utils.showCroutonText(HomePageFragment.this.getActivity(), callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   //进入房间接口
   private void enterRoom(String liveuid, final String userid, final String uid, final String face,
       final String nickname) {
      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(
             UrlHelper.enterRoomUrl + "?liveuid=" + liveuid + "&userid=" + userid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
         request.addPostParams("roomid", liveuid + "");
         request.addPostParams("userid", userid);
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

               MedalListEvent medalListEvent = new MedalListEvent();
               medalListEvent.anchor_medal = callback.anchor_medal;
               medalListEvent.wanjia_medal = callback.wanjia_medal;
               medalListEvent.act = callback.act;
               EventBus.getDefault().postSticky(medalListEvent);

               HomePageFragment.this.getActivity()
                   .startActivity(
                       new Intent(HomePageFragment.this.getActivity(), AvActivity.class).putExtra(
                           AvActivity.GET_UID_KEY, uid)
                           .putExtra(AvActivity.IS_CREATER_KEY, false)
                           .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_FACE, face)
                           .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_NICKNAME, nickname)
                           .putExtra(AvActivity.EXTRA_RECIVE_DIAMOND, callback.recv_diamond)
                           .putExtra(AvActivity.EXTRA_SYS_MSG, callback.sys_msg)
                           .putExtra(AvActivity.EXTRA_IS_ON_SHOW, callback.is_live)
                           .putExtra(AvActivity.EXTRA_ONLINE_NUM, callback.total)
                           .putExtra(AvActivity.EXTRA_IS_MANAGER, callback.is_manager)
                           .putExtra(AvActivity.EXTRA_IS_GAG, callback.is_gag)
                           .putExtra(AvActivity.EXTRA_IS_SUPER_MANAGER, callback.show_manager)

                   );
            } else {
               Utils.showMessage("" + callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(EnterRoomEntity.class));
      request.execute();
   }

   public void setSelectLine(int pos) {
      root_view.findViewById(R.id.fllow_line).setVisibility(View.INVISIBLE);
      root_view.findViewById(R.id.fans_line).setVisibility(View.INVISIBLE);
      root_view.findViewById(R.id.lives_line).setVisibility(View.INVISIBLE);

      if (pos == 0) {
         root_view.findViewById(R.id.fllow_line).setVisibility(View.VISIBLE);
      }
      if (pos == 1) {
         root_view.findViewById(R.id.fans_line).setVisibility(View.VISIBLE);
      }
      if (pos == 2) {
         root_view.findViewById(R.id.lives_line).setVisibility(View.VISIBLE);
      }
   }
}
