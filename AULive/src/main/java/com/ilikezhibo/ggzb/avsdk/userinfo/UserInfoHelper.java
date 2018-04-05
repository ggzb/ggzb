package com.ilikezhibo.ggzb.avsdk.userinfo;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.avsdk.MemberInfo;
import com.ilikezhibo.ggzb.avsdk.RoomUserListAdapter;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalLayoutHelper;
import com.ilikezhibo.ggzb.avsdk.userinfo.homepage.HomePageActivity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.tencent.android.tpush.XGPushManager;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import popwindow.PopupWindowUtil;


/**
 * Created by big on 3/22/16.
 */
public class UserInfoHelper
    implements PullToRefreshView.OnRefreshListener, AdapterView.OnItemClickListener {

   public PopupWindowUtil popupWindow;
   private Activity mActivity;
   private MemberInfo memberInfo;
   private View anchor;

   public UserInfoHelper() {
      EventBus.getDefault().register(this);
   }

   private static UserInfoHelper userInfoHelper = null;

   public static UserInfoHelper getInstance(View anchor, PopupWindowUtil popupWindow,
       Activity mActivity, MemberInfo memberInfo) {

      if (userInfoHelper == null) {
         userInfoHelper = new UserInfoHelper();
      }
      userInfoHelper.popupWindow = popupWindow;
      userInfoHelper.mActivity = mActivity;
      userInfoHelper.memberInfo = memberInfo;
      userInfoHelper.anchor = anchor;

      popupWindow.showCenter();

      userInfoHelper.startProgressDialog();
      userInfoHelper.getHomeInfo();

      return userInfoHelper;
   }

   public void onEvent(CloseAllPopUpDialogEvent token_event) {
      if (popupWindow != null) {
         popupWindow.dismiss();
      }
   }

   private void getHomeInfo() {

      RequestInformation request = null;
      String uid = memberInfo.getUserPhone();
      if (uid == null || uid.equals("")) {
         Utils.showMessage("uid为空");
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

               final LoginUserEntity userinfo = callback.getUserinfo();
               final View close_btn = popupWindow.findId(R.id.img_close);
               close_btn.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     popupWindow.dismiss();
                  }
               });
               DisplayImageOptions options =
                   new DisplayImageOptions.Builder().showStubImage(R.drawable.default_head)
                       .showImageForEmptyUri(R.drawable.default_head)
                       .showImageOnFail(R.drawable.default_head)
                       .cacheInMemory()
                       .cacheOnDisc()
                       .build();

               View circle_to_mvp = popupWindow.findId(R.id.circle_to_mvp);
               //贡献第一的头像
               ImageView mvp_portrait = (ImageView) popupWindow.findId(R.id.mvp_portrait);
               if (userinfo.tops != null && userinfo.tops.size() > 0) {
                  mvp_portrait.setVisibility(View.VISIBLE);
                  circle_to_mvp.setVisibility(View.VISIBLE);
                  String top_headurl = userinfo.tops.get(0).getFace();
                  if (!top_headurl.startsWith("http")) {
                     top_headurl = Utils.getImgUrl(top_headurl);
                  }
                  ImageLoader.getInstance().displayImage(top_headurl, mvp_portrait, options);
               } else {
                  mvp_portrait.setVisibility(View.INVISIBLE);
                  circle_to_mvp.setVisibility(View.INVISIBLE);
               }

               //头像
               ImageView user_portrait = (ImageView) popupWindow.findId(R.id.user_portrait);
               String headurl = userinfo.getFace();
               if (!headurl.startsWith("http")) {
                  headurl = UrlHelper.IMAGE_ROOT_URL + headurl;
               }
               ImageLoader.getInstance().displayImage(headurl, user_portrait, options);

               ImageView img_user_type = (ImageView) popupWindow.findId(R.id.img_user_type);
               RoomUserListAdapter.setTopIcon(userinfo.getGrade(), img_user_type);
               //昵称
               TextView txt_username = (TextView) popupWindow.findId(R.id.txt_username);
               txt_username.setText(userinfo.getNickname());

               //性别
               ImageView img_gender = (ImageView) popupWindow.findId(R.id.img_gender);
               if (userinfo.getSex() == 1) {
                  img_gender.setImageDrawable(
                      mActivity.getResources().getDrawable(R.drawable.global_male));
               } else {
                  img_gender.setImageDrawable(
                      mActivity.getResources().getDrawable(R.drawable.global_female));
               }

               //等级
               TextView tv_grade = (TextView) popupWindow.findId(R.id.grade_tv);
               RelativeLayout grade_ly = (RelativeLayout) popupWindow.findId(R.id.grade_ly);
               String grade = userinfo.getGrade();
               ChatMsgListAdapter.setGradeIcon(grade, tv_grade, grade_ly);

               if (userinfo.offical == 1) {
                  popupWindow.findId(R.id.offical_tag_iv).setVisibility(View.VISIBLE);
               } else {
                  popupWindow.findId(R.id.offical_tag_iv).setVisibility(View.GONE);
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
               LinearLayout chat_medal_ly = (LinearLayout) popupWindow.findId(R.id.chat_medal_ly);
               for (int i = 0; i < chat_medal_ly.getChildCount(); i++) {
                  chat_medal_ly.getChildAt(i).setVisibility(View.GONE);
               }

               for (int i = 0; i < chat_medal_ly.getChildCount() && i < tem_urls.size(); i++) {
                  String url = tem_urls.get(i);
                  MedalLayoutHelper.showGifImage(url, mActivity, chat_medal_ly.getChildAt(i));
               }
               ///////////////////////////////////////////////////////////

               //ID
               final TextView txt_uid = (TextView) popupWindow.findId(R.id.txt_uid);
               txt_uid.setText("ID:" + userinfo.getUid());
               if (userinfo.goodid != 0) {
                  txt_uid.setText("ID:" + userinfo.goodid);
                  txt_uid.setTextColor(AULiveApplication.mContext.getResources()
                      .getColor(R.color.btn_bottom_send_msg_c));
               } else {
                  txt_uid.setTextColor(0xff656565);
               }
               txt_uid.setOnClickListener(new View.OnClickListener() {

                  @Override
                  public void onClick(View view) {
                     String id_content = txt_uid.getText().toString().trim();
                     id_content = id_content.substring(3);
                     if (!"".equals(id_content)) {
                        ClipboardManager cmb = (ClipboardManager) AULiveApplication.mContext
                                .getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText(id_content);
                        Utils.showMessage("ID已复制到剪贴板!");
                     }
                  }
               });

               //地址
               TextView txt_location = (TextView) popupWindow.findId(R.id.txt_location);
               txt_location.setText(userinfo.city);

               //认证
               TextView txt_verify_reason = (TextView) popupWindow.findId(R.id.txt_verify_reason);
               if (userinfo.tag == null || userinfo.tag.equals("")) {
                  txt_verify_reason.setVisibility(View.GONE);
               } else {
                  txt_verify_reason.setText("认证:" + userinfo.tag);
               }

               //签名
               TextView txt_desc = (TextView) popupWindow.findId(R.id.txt_desc);
               txt_desc.setText(userinfo.signature);

               //送出钻石
               TextView txt_account_inout = (TextView) popupWindow.findId(R.id.txt_account_inout);
               txt_account_inout.setText(userinfo.send_diamond + "钻");

               entities = new ArrayList<UserInfoDialogEntity>();

               dialog_list = (PullToRefreshView) popupWindow.findId(R.id.dialog_list);
               dialgoListAdapter = new DialgoListAdapter(mActivity);
               dialog_list.setAdapter(dialgoListAdapter);
               dialgoListAdapter.setEntities(entities);
               dialog_list.setOnRefreshListener(UserInfoHelper.this);
               dialog_list.setOnItemClickListener(UserInfoHelper.this);
               dialog_list.setRefreshAble(false);

               //头像layout
               final RelativeLayout user_portrait_container =
                   (RelativeLayout) popupWindow.findId(R.id.user_portrait_container);
               //认证layout
               final LinearLayout info_container =
                   (LinearLayout) popupWindow.findId(R.id.info_container);
               //操作layout
               final LinearLayout oper_container =
                   (LinearLayout) popupWindow.findId(R.id.oper_container);
               //收起list按钮
               final ImageView img_up_up = (ImageView) popupWindow.findId(R.id.img_up_up);

               //箭头
               final ImageView img_follows_pointer =
                   (ImageView) popupWindow.findId(R.id.img_follows_pointer);
               final ImageView img_fans_pointer =
                   (ImageView) popupWindow.findId(R.id.img_fans_pointer);
               final ImageView img_contributors_pointer =
                   (ImageView) popupWindow.findId(R.id.img_contributors_pointer);

               //关注layout
               View btn_follows = popupWindow.findId(R.id.btn_follows);
               btn_follows.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     //显示列表与图标
                     data_url = UrlHelper.ROOM_ATTEN;
                     dialog_list.setVisibility(View.VISIBLE);
                     dialog_list.initRefresh(PullToRefreshView.HEADER);

                     //加载关注列表
                     img_up_up.setVisibility(View.VISIBLE);
                     close_btn.setVisibility(View.GONE);

                     //隐藏
                    // user_portrait_container.setVisibility(View.GONE);
                    // info_container.setVisibility(View.GONE);
                    // oper_container.setVisibility(View.GONE);

                     //jiantou
                     img_follows_pointer.setVisibility(View.INVISIBLE);
                     img_fans_pointer.setVisibility(View.INVISIBLE);
                     img_contributors_pointer.setVisibility(View.INVISIBLE);
                  }
               });

               //粉丝layout
               View btn_fans = popupWindow.findId(R.id.btn_fans);
               btn_fans.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     //显示列表与图标
                     data_url = UrlHelper.ROOM_FANS_LIST;
                     dialog_list.setVisibility(View.VISIBLE);
                     dialog_list.initRefresh(PullToRefreshView.HEADER);

                     //加载关注列表
                     img_up_up.setVisibility(View.VISIBLE);
                     close_btn.setVisibility(View.GONE);

                     //隐藏
                     user_portrait_container.setVisibility(View.GONE);
                     info_container.setVisibility(View.GONE);
                     oper_container.setVisibility(View.GONE);

                     //jiantou
                     img_follows_pointer.setVisibility(View.INVISIBLE);
                     img_fans_pointer.setVisibility(View.INVISIBLE);
                     img_contributors_pointer.setVisibility(View.INVISIBLE);
                  }
               });

               //贡献layout
               View btn_contributors = popupWindow.findId(R.id.btn_contributors);
               btn_contributors.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     //显示列表与图标
                     data_url = UrlHelper.ROOM_IN_GOLD_TOPS;
                     dialog_list.setVisibility(View.VISIBLE);
                     dialog_list.initRefresh(PullToRefreshView.HEADER);

                     //加载关注列表
                     img_up_up.setVisibility(View.VISIBLE);
                     close_btn.setVisibility(View.GONE);

                     //隐藏
                     user_portrait_container.setVisibility(View.GONE);
                     info_container.setVisibility(View.GONE);
                     oper_container.setVisibility(View.GONE);

                     //jiantou
                     img_follows_pointer.setVisibility(View.INVISIBLE);
                     img_fans_pointer.setVisibility(View.INVISIBLE);
                     img_contributors_pointer.setVisibility(View.INVISIBLE);
                  }
               });

               //收起listener
               View.OnClickListener retract_listener = new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     dialog_list.setVisibility(View.GONE);

                     img_up_up.setVisibility(View.GONE);
                     close_btn.setVisibility(View.VISIBLE);

                     //隐藏
                     user_portrait_container.setVisibility(View.VISIBLE);
                     info_container.setVisibility(View.VISIBLE);
                     oper_container.setVisibility(View.VISIBLE);

                     //jiantou
                     img_follows_pointer.setVisibility(View.INVISIBLE);
                     img_fans_pointer.setVisibility(View.INVISIBLE);
                     img_contributors_pointer.setVisibility(View.INVISIBLE);

                     //如果是自己，则不显示操作layout
                     if (memberInfo.getUserPhone() != null && AULiveApplication.getMyselfUserInfo()
                         .isCreater() && memberInfo.getUserPhone()
                         .equals(AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                        oper_container.setVisibility(View.GONE);
                     }
                  }
               };
               //收起ListView
               img_up_up.setOnClickListener(retract_listener);
               //马上收起
               retract_listener.onClick(null);

               //如果是自己，则不显示操作layout
               if (memberInfo.getUserPhone() != null && AULiveApplication.getMyselfUserInfo()
                   .isCreater() && memberInfo.getUserPhone()
                   .equals(AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                  oper_container.setVisibility(View.GONE);
               }

               //关注
               TextView txt_follows = (TextView) popupWindow.findId(R.id.txt_follows);
               //txt_follows.setText(Utils.trans(R.string.userinfo_follows, userinfo.atten_total));
               txt_follows.setText(userinfo.atten_total + "");

               //粉丝
               TextView txt_fans = (TextView) popupWindow.findId(R.id.txt_fans);
               //txt_fans.setText(Utils.trans(R.string.userinfo_fans, userinfo.fans_total));
               txt_fans.setText(userinfo.fans_total + "");

               //新币
               TextView txt_contributors = (TextView) popupWindow.findId(R.id.txt_contributors);
               //txt_contributors.setText(Utils.trans(R.string.userinfo_gain, userinfo.recv_diamond));
               txt_contributors.setText(userinfo.recv_diamond + "");

               //////////////////////////////////////////////////////////
               //管理,标签
               TextView img_report = (TextView) popupWindow.findId(R.id.img_report);
               TextView tv_lianmai = (TextView) popupWindow.findId(R.id.tv_lianmai);
               tv_lianmai.setVisibility(View.GONE);
               if (mActivity instanceof AvActivity) {

                  final AvActivity avActivity = (AvActivity) mActivity;

                  //自己是主播,能连麦,连麦中不显示

                  //自己是不是管理员
                  final boolean is_manager = avActivity.is_manager;

                  //默认状态
                  if (is_manager || AULiveApplication.getMyselfUserInfo().isCreater()) {
                     img_report.setVisibility(View.VISIBLE);
                  } else {
                     img_report.setVisibility(View.INVISIBLE);
                  }
                  //以下是过滤不能显示的状态
                  //是创建者，打开自己
                  if (memberInfo.getUserPhone() != null && AULiveApplication.getMyselfUserInfo()
                      .isCreater() && memberInfo.getUserPhone()
                      .equals(AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                     img_report.setVisibility(View.INVISIBLE);
                  }

                  //是管理员,打开自己
                  if (memberInfo.getUserPhone() != null && memberInfo.getUserPhone()
                      .equals(AULiveApplication.getMyselfUserInfo().getUserPhone()) && is_manager) {
                     img_report.setVisibility(View.INVISIBLE);
                  }

                  //自己是管理员，打开创建者
                  if (memberInfo.getUserPhone() != null
                      && is_manager
                      && AULiveApplication.currLiveUid.equals(memberInfo.getUserPhone())) {
                     img_report.setVisibility(View.INVISIBLE);
                  }
                  //自己是管理员，打开管理员
                  //目前没有记录所有管理员的id,暂废弃
                  //if (memberInfo.getUserPhone() != null
                  //    && is_manager
                  //    && avActivity.manager_uids.contains(memberInfo.getUserPhone())) {
                  //   img_report.setVisibility(View.INVISIBLE);
                  //} else {
                  //   img_report.setVisibility(View.VISIBLE);
                  //}

                  //自己是房间创建者下，与要显示的entity不是这个房间的看客，不显示“管理”
                  //由于加了分页，成员数据可能不在列表，暂去掉
                  //boolean isExist = false;
                  //for (MemberInfo memberInfo : avActivity.mMemberList) {
                  //   String userPhone = memberInfo.getUserPhone();
                  //   if (userPhone.equals(userinfo.getUid())) {
                  //      isExist = true;
                  //      break;
                  //   }
                  //}
                  ////是这房间的用户
                  //if ((isExist && AULiveApplication.getMyselfUserInfo().isCreater()) || (isExist
                  //    && is_manager)) {
                  //   img_report.setVisibility(View.VISIBLE);
                  //} else {
                  //   img_report.setVisibility(View.INVISIBLE);
                  //}

                  img_report.setOnClickListener(new View.OnClickListener() {
                     @Override public void onClick(View view) {
                        final PopupWindowUtil popupWindow = new PopupWindowUtil(anchor);
                        popupWindow.setContentView(R.layout.dialog_roomuser_setting);
                        popupWindow.setOutsideTouchable(true);

                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                           @Override public void onDismiss() {

                           }
                        });

                        //是否是主播
                        if (AULiveApplication.getMyselfUserInfo().isCreater()) {
                           RoomManagerHelper roomManagerHelper =
                               RoomManagerHelper.getInstance(popupWindow, avActivity, userinfo,
                                   false);
                           popupWindow.showBottom();
                           return;
                        }
                        //普通管理员
                        //设置按钮的相应事件
                        RoomManagerHelper roomManagerHelper =
                            RoomManagerHelper.getInstance(popupWindow, avActivity, userinfo,
                                is_manager);
                        popupWindow.showBottom();
                     }
                  });

                  //被主播设为管理员的情况
               } else {
                  //不是在直播界面时
                  img_report.setVisibility(View.INVISIBLE);
               }

               //当不显示管理时即“举报”,并且不是主播
               if (img_report.getVisibility() != View.VISIBLE
                   && !AULiveApplication.getMyselfUserInfo().isCreater()) {
                  img_report.setText("举报");
                  img_report.setOnClickListener(new View.OnClickListener() {
                     @Override public void onClick(View view) {
                        showPromptDialog(userinfo.getUid());
                     }
                  });
                  img_report.setVisibility(View.VISIBLE);
               }

               ///////////////////////////////////////////////////////
               boolean has_follow = false;
               if (MainActivity.atten_uids.contains(memberInfo.getUserPhone())) {
                  has_follow = true;
               }
               //关注
               final Button btn_follow = (Button) popupWindow.findId(R.id.btn_follow);
               btn_follow.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     //判断是不是自己
                     if (memberInfo.getUserPhone()
                         .equals(AULiveApplication.getUserInfo().getUid())) {
                        Utils.showCroutonText(mActivity, "不能关注自己");
                        return;
                     }
                     boolean has_follow = false;
                     if (MainActivity.atten_uids.contains(memberInfo.getUserPhone())) {
                        has_follow = true;
                     }
                     if (has_follow) {
                        //取消关注,只能在个人主页了取消
                        //doDelAttend(memberInfo.getUserPhone());
                     } else {
                        doAttend(memberInfo.getUserPhone());
                     }
                     if (!has_follow) {
                        btn_follow.setText("已关注");
                        btn_follow.setBackgroundResource(R.drawable.bg_room_popup_selected);
                     } else {
                        //btn_follow.setText("关注");
                        //btn_follow.setBackgroundResource(R.drawable.bg_room_popup);
                     }
                  }
               });
               //按是否关注而改变
               if (has_follow) {
                  btn_follow.setText("已关注");
                //  btn_follow.setBackgroundResource(R.drawable.bg_room_popup_selected);
               } else {
                  btn_follow.setText("关注");
               //   btn_follow.setBackgroundResource(R.drawable.bg_room_popup);
               }

               //私聊
               Button btn_private_chat = (Button) popupWindow.findId(R.id.btn_private_chat);
               btn_private_chat.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     if (userinfo.getUid().equals(AULiveApplication.getUserInfo().getUid())) {
                        Utils.showCroutonText(mActivity, "不能跟自己聊天");
                        return;
                     }
                     String len = AULiveApplication.getMyselfUserInfo().getmPrivate_chat_status() + "12";
                     System.out.println("len--" + len);
                     if (len.length() > 4) {
                        Utils.showCroutonText(mActivity, AULiveApplication.getMyselfUserInfo().getmPrivate_chat_status() + "!");
                        return;
                     }
                     if (mActivity instanceof AvActivity) {
                        popupWindow.dismiss();
                        AvActivity avActivity = (AvActivity) mActivity;
                        avActivity.privateChatHelper.startPrivateChat(memberInfo.getUserPhone(),
                            memberInfo.getUserName());
                        avActivity.privateChatListHelper.onClosPrivate();
                     }
                  }
               });

               //回复
               Button btn_reply = (Button) popupWindow.findId(R.id.btn_reply);
               btn_reply.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     if (mActivity instanceof AvActivity) {
                        AvActivity avActivity = (AvActivity) mActivity;
                        avActivity.replyTo(memberInfo.getUserName());
                        popupWindow.dismiss();
                     }
                  }
               });

               //跳到个人主页
               Button btn_home = (Button) popupWindow.findId(R.id.btn_home);
               btn_home.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                     Intent homepage_Intent =
                         new Intent(UserInfoHelper.this.mActivity, HomePageActivity.class);
                     homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID,
                         memberInfo.getUserPhone());
                     mActivity.startActivity(homepage_Intent);
                  }
               });
               LinearLayout ll_btn_home = (LinearLayout)popupWindow.findId(R.id.ll_btn_home);
               //如果是主播，不能离开主播界面,看主页
               if (mActivity instanceof AvActivity) {
                  AvActivity avActivity = (AvActivity) mActivity;
                  if (avActivity.is_creater) {
                     btn_home.setVisibility(View.GONE);
                     ll_btn_home.setVisibility(View.GONE);
                    // popupWindow.findId(R.id.space_home).setVisibility(View.GONE);
                  }
               }

               View click_view = popupWindow.findId(R.id.click_view);
               click_view.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {

                     popupWindow.dismiss();
                  }
               });
               View root_view = popupWindow.findId(R.id.root_view);
               root_view.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {

                  }
               });

               stopProgressDialog();
            } else {

            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(UserInfo.class));
      request.execute();
   }

   /////////刷新相关

   private ArrayList<UserInfoDialogEntity> entities;
   private PullToRefreshView dialog_list;
   private DialgoListAdapter dialgoListAdapter;
   private int currPage = 1;
   private CustomProgressDialog progressDialog = null;
   private String data_url = UrlHelper.ROOM_ATTEN;

   private void startProgressDialog() {
      try {
         if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(mActivity);
            progressDialog.setMessage("加载中");
         }
         progressDialog.show();
      } catch (Exception e) {

      }
   }

   private void stopProgressDialog() {
      try {
         if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
         }
      } catch (Exception e) {

      }
   }

   @Override public void onRefresh(final int mode) {
      startProgressDialog();
      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      RequestInformation request = null;
      StringBuilder sb = new StringBuilder(
          data_url + "?page=" + currPage + "&liveuid=" + memberInfo.getUserPhone());

      Trace.d(sb.toString());
      request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<UserInfoDialogListEntity>() {

         @Override public void onCallback(UserInfoDialogListEntity callback) {
            stopProgressDialog();
            if (callback == null) {
               currPage--;
               dialog_list.setVisibility(View.VISIBLE);
               dialog_list.onRefreshComplete(mode, false);
               dialog_list.enableFooter(false);
               return;
            }

            if (callback.getStat() == 200) {

               if (mode == PullToRefreshView.HEADER) {
                  entities.clear();
               }

               if (callback.getList() != null) {
                  if (data_url.equals(UrlHelper.ROOM_IN_GOLD_TOPS)) {
                     ArrayList<UserInfoDialogEntity> tem_List = callback.getList();
                     for (UserInfoDialogEntity entity : tem_List) {
                        entity.setIs_ranking(true);
                        entities.add(entity);
                     }
                  } else {
                     entities.addAll(callback.getList());
                  }
               }

               dialgoListAdapter.setEntities(entities);
               dialgoListAdapter.notifyDataSetChanged();
               dialog_list.onRefreshComplete(mode, true);

               if (mode == PullToRefreshView.HEADER || (callback.getList() != null
                   && callback.getList().size() > 0)) {
                  dialog_list.enableFooter(true);
               } else {
                  dialog_list.enableFooter(false);
               }
            } else {
               if (callback.getStat() == 500) {
                  //没登录
               }
               stopProgressDialog();
               currPage--;
               // 因为可能网络恢复，success改为true
               dialog_list.onRefreshComplete(mode, false);
               dialog_list.enableFooter(false);
            }
         }

         @Override public void onFailure(AppException e) {
            stopProgressDialog();
            currPage--;
            entities.clear();
            // 因为可能网络恢复，success改为true
            dialog_list.onRefreshComplete(mode, false);
            dialog_list.enableFooter(false);
         }
      }.setReturnType(UserInfoDialogListEntity.class));

      request.execute();
   }

   @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      UserInfoDialogEntity entity = (UserInfoDialogEntity) parent.getAdapter().getItem(position);

      MemberInfo memberInfo =
          new MemberInfo(entity.getUid(), entity.getNickname(), entity.getFace());
      popupWindow.dismiss();

      //PopupWindowUtil popupWindow1 = new PopupWindowUtil(anchor);
      //popupWindow1.setContentView(R.layout.dialog_myroom_userinfo);
      //popupWindow1.setOutsideTouchable(true);
      UserInfoHelper userInfoHelper =
          UserInfoHelper.getInstance(anchor, popupWindow, mActivity, memberInfo);
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
               Utils.showCroutonText(mActivity, "关注成功");

               //信鸽关注
               XGPushManager.setTag(mActivity, uid);

               if (MainActivity.atten_uids.contains(uid)) {
               } else {
                  MainActivity.atten_uids.add(uid);
               }
               //判断是不是关注主播，是则发送消息
               if (mActivity instanceof AvActivity) {
                  AvActivity avActivity = (AvActivity) mActivity;
                  if (uid.equals(AULiveApplication.currLiveUid)) {
                     avActivity.sendAttenMsg();
                  }
               }
               //dialgoListAdapter.notifyDataSetChanged();
            } else {
               Utils.showCroutonText(mActivity, callback.getMsg());
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
               Utils.showCroutonText(mActivity, "取消成功");
               XGPushManager.deleteTag(mActivity, uid);

               if (MainActivity.atten_uids.contains(uid)) {
                  MainActivity.atten_uids.remove(uid);
               } else {

               }

               //dialgoListAdapter.notifyDataSetChanged();
            } else {
               Utils.showCroutonText(mActivity, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void showPromptDialog(final String uid) {
      final CustomDialog customDialog = new CustomDialog(mActivity, new CustomDialogListener() {

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
      customDialog.show();
   }

   String cropPath;

   private void doReport(final String uid) {
      //举报时截图
      //if (mActivity instanceof AvActivity) {
      //
      //   AvActivity avActivity = (AvActivity) mActivity;
      //   //只有主播才截图
      //   if (memberInfo.getUserPhone() != null && avActivity.mHostIdentifier.equals(
      //       memberInfo.getUserPhone())) {
      //
      //      Bitmap bitmap = Bitmap.createBitmap(360, 640, Bitmap.Config.ARGB_8888);
      //      avActivity.ksyMediaPlayer.getCurrentFrame(bitmap);
      //
      //      String imgPath = FileUtil.getGalaryPath(avActivity);
      //      if (imgPath == null) {
      //         Trace.d("imgpath  is null");
      //         return;
      //      }
      //      cropPath = imgPath.replace(".", "_report" + System.currentTimeMillis() + ".").trim();
      //      try {
      //         File file = new File(cropPath);
      //         file.createNewFile();
      //         OutputStream outStream = new FileOutputStream(file);
      //         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
      //         outStream.flush();
      //         outStream.close();
      //      } catch (IOException e) {
      //         //e.printStackTrace();
      //         //用于处理红米note等no permission 错误
      //         try {
      //            cropPath = FileUtil.getImageFolder()
      //                + File.separator
      //                + "report"
      //                + File.separator
      //                + System.currentTimeMillis()
      //                + ".jpg";
      //            File file = new File(cropPath);
      //            file.createNewFile();
      //            OutputStream outStream = new FileOutputStream(file);
      //            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
      //            outStream.flush();
      //            outStream.close();
      //         } catch (IOException e1) {
      //            e1.printStackTrace();
      //         }
      //      }
      //
      //      if (cropPath != null) {
      //         //上传举报图片
      //      }
      //   }
      //}

      RequestInformation request = new RequestInformation(
          UrlHelper.SERVER_URL + "live/report" + "?liveuid=" + uid + "&type=0",
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               Utils.showCroutonText(mActivity, "成功举报");
            } else {
               Utils.showCroutonText(mActivity, callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }
}
