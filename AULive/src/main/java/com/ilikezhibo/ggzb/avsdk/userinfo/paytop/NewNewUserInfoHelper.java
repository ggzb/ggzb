package com.ilikezhibo.ggzb.avsdk.userinfo.paytop;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.avsdk.MemberInfo;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalListEvent;
import com.ilikezhibo.ggzb.avsdk.chat.PrivateChatActivity;
import com.ilikezhibo.ggzb.avsdk.home.EnterRoomEntity;
import com.ilikezhibo.ggzb.avsdk.search.DialgoListAdapter;
import com.ilikezhibo.ggzb.avsdk.search.UserInfoDialogEntity;
import com.ilikezhibo.ggzb.avsdk.search.UserInfoDialogListEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.CloseAllPopUpDialogEvent;
import com.ilikezhibo.ggzb.avsdk.userinfo.RoomManagerHelper;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.MainActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.tool.SoloMgmtUtils;
import com.ilikezhibo.ggzb.tool.SoloRequestListener;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import popwindow.PopupWindowUtil;

/**
 * Created by hasee on 2017/9/18.
 */

public class NewNewUserInfoHelper implements PullToRefreshView.OnRefreshListener,AdapterView.OnItemClickListener {


    private final static int SHOW_CENTER = 0x002;

    public static final String TAG = "NewNewUserInfoHelper";
    public PopupWindowUtil popupWindow;
    private Activity mActivity;
    private MemberInfo memberInfo;
    private View anchor;
    private CustomProgressDialog progressDialog = null;
    private static NewNewUserInfoHelper mNewNewUserInfoHelper = null;

    private boolean avactiviyflag;

    //刷新有关
    private PullToRefreshView dialog_list;
    private String data_url = UrlHelper.ROOM_ATTEN;
    private DialgoListAdapter dialgoListAdapter;
    private ArrayList<UserInfoDialogEntity> entities;
    private int currPage = 1;

    private String uid;
    private boolean has_lahei;
    TextView btn_home;
    LinearLayout ll_home;

    public NewNewUserInfoHelper() {
        EventBus.getDefault().register(this);
    }
    public static NewNewUserInfoHelper getInstance(View anchor, PopupWindowUtil popupWindow,
                                                   Activity mActivity, MemberInfo memberInfo,boolean avactiviyflag) {
        if (mNewNewUserInfoHelper == null) {
            mNewNewUserInfoHelper = new NewNewUserInfoHelper();
        }
        mNewNewUserInfoHelper.popupWindow = popupWindow;
        mNewNewUserInfoHelper.mActivity = mActivity;
        mNewNewUserInfoHelper.memberInfo = memberInfo;
        mNewNewUserInfoHelper.anchor = anchor;
        mNewNewUserInfoHelper.avactiviyflag = avactiviyflag;
        mNewNewUserInfoHelper.startProgressDialog();
        mNewNewUserInfoHelper.getHomeInfo();

        return mNewNewUserInfoHelper;
    }

    private void getHomeInfo(){
        RequestInformation request = null;
        uid = memberInfo.getUserPhone();
        Log.e(TAG,"uid = "+uid);
        if (uid == null || uid.equals("")) {
            Log.e(TAG,"uid为空");
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
            @Override
            public void onCallback(UserInfo callback) {
                if (callback == null) {
                    Utils.showMessage(Utils.trans(R.string.get_info_fail));
                    return;
                }
                if(callback.getStat()==200){
                    final LoginUserEntity userinfo = callback.getUserinfo();
                    DisplayImageOptions options =
                            new DisplayImageOptions.Builder().showStubImage(R.drawable.default_head)
                                    .showImageForEmptyUri(R.drawable.default_head)
                                    .showImageOnFail(R.drawable.default_head)
                                    .cacheInMemory()
                                    .cacheOnDisc()
                                    .build();


                    //头像
                    ImageView user_portrait = (ImageView) popupWindow.findId(R.id.image_userhead);
                    String headurl = userinfo.getFace();
                    if (!headurl.startsWith("http")) {
                        headurl = UrlHelper.IMAGE_ROOT_URL + headurl;
                    }
                    ImageLoader.getInstance().displayImage(headurl, user_portrait, options);
                    //用户等级图片设置




                    //昵称
                    TextView txt_username = (TextView) popupWindow.findId(R.id.tv_nickname);
                    txt_username.setText(userinfo.getNickname());



                    //守护
                    ImageView iv_guard = (ImageView) popupWindow.findId(R.id.iv_guard);
                    View bg_guard = popupWindow.findId(R.id.bg_guard);
                    bg_guard.bringToFront();
                    //性别
                    ImageView img_gender = (ImageView) popupWindow.findId(R.id.image_gender);
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



                    //ID
                    final TextView txt_uid = (TextView) popupWindow.findId(R.id.tv_youmeiid);
                    txt_uid.setText("有美ID:" + userinfo.getUid());
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



                    //签名
                    TextView txt_desc = (TextView) popupWindow.findId(R.id.tv_signture);
                    txt_desc.setText(userinfo.signature);



                    //关注
                    boolean has_follow = false;
                    if (MainActivity.atten_uids.contains(memberInfo.getUserPhone())) {
                        has_follow = true;
                    }
                    final TextView btn_atten = (TextView) popupWindow.findId(R.id.btn_atten);
                    btn_atten.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                                doDelAttend(memberInfo.getUserPhone());
                            } else {
                                doAttend(memberInfo.getUserPhone());
                            }
                            if (!has_follow) {
                                btn_atten.setText("已关注");
                                btn_atten.setTextColor(0xff999999);
                                btn_atten.setBackgroundResource(R.drawable.shape_circle_rectangle_gray);
                            } else {
                                btn_atten.setText("关注");
                                btn_atten.setTextColor(0xffe482ec);
                                btn_atten.setBackgroundResource(R.drawable.shape_circle_rectangle_pink);

                            }
                        }
                    });
                    if(has_follow){
                        btn_atten.setText("已关注");
                        btn_atten.setTextColor(0xff999999);
                        btn_atten.setBackgroundResource(R.drawable.shape_circle_rectangle_gray);
                    }
                    else {
                        btn_atten.setText("关注");
                        btn_atten.setTextColor(0xffe482ec);
                        btn_atten.setBackgroundResource(R.drawable.shape_circle_rectangle_pink);
                    }


                    //私聊
                    TextView btn_private_chat = (TextView) popupWindow.findId(R.id.btn_private_chat);
                    btn_private_chat.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View view) {
                            if (userinfo.getUid().equals(AULiveApplication.getUserInfo().getUid())) {
                                Utils.showCroutonText(mActivity, "不能跟自己聊天");
                                return;
                            }
                            String len = AULiveApplication.getMyselfUserInfo().getmPrivate_chat_status() + "12";
                            if (len.length() > 4) {
                                Utils.showCroutonText(mActivity, AULiveApplication.getMyselfUserInfo().getmPrivate_chat_status() + "!");
                                return;
                            }
                            String string_grade = AULiveApplication.getUserInfo().getGrade();
                            int int_grade = Integer.parseInt(string_grade);
                            if (int_grade < 8) {
                                Utils.showCroutonText(mActivity, "八级才可以发私信哦");
                                return;
                            }

                            if (mActivity instanceof AvActivity) {
                                popupWindow.dismiss();
                                AvActivity avActivity = (AvActivity) mActivity;
                                avActivity.privateChatHelper.startPrivateChat(memberInfo.getUserPhone(),
                                        memberInfo.getUserName());
                                avActivity.privateChatListHelper.onClosPrivate();
                            }
                            else {
                                popupWindow.dismiss();
                                mActivity.startActivityForResult(new Intent(NewNewUserInfoHelper.this.mActivity,
                                        PrivateChatActivity.class).putExtra(
                                        PrivateChatActivity.STAR_PRIVATE_CHAT_BY_CODE_UID_KEY, uid), 0);
                            }
                        }
                    });
                    //关注
                    TextView txt_send = (TextView) popupWindow.findId(R.id.tv_atten);
                    txt_send.setText(userinfo.atten_total + "");


                    //粉丝
                    TextView txt_fans = (TextView) popupWindow.findId(R.id.tv_fans);
                    txt_fans.setText(userinfo.fans_total + "");

                    //有美币
                    TextView txt_recv = (TextView) popupWindow.findId(R.id.tv_recv_diamond);
                    txt_recv.setText(userinfo.recv_diamond + "");


                    //主页
                    //点击头像跳到主页
                    LinearLayout ll_head = (LinearLayout) popupWindow.findId(R.id.ll_head);
                    ll_head.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                                }
                    });



                    LinearLayout ll_report = (LinearLayout) popupWindow.findId(R.id.ll_report);
                    TextView btn_report = (TextView) popupWindow.findId(R.id.btn_report);

                    ll_home = (LinearLayout) popupWindow.findId(R.id.ll_home);
                    btn_home = (TextView) popupWindow.findId(R.id.btn_home);


                    if(mActivity instanceof AvActivity&&avactiviyflag){


                        final  AvActivity avActivity = (AvActivity)mActivity;

                        //自己是不是管理员
                        final boolean is_manager = avActivity.is_manager;



                        if(avActivity.is_creater){
                            //管理


                            btn_report.setText("管理");
                            //是创建者，打开自己
                            if (memberInfo.getUserPhone() != null && AULiveApplication.getMyselfUserInfo()
                                    .isCreater() && memberInfo.getUserPhone()
                                    .equals(AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                                ll_report.setVisibility(View.INVISIBLE);
                            }
                            //连麦
                            btn_home.setText("连麦");
                            ll_home.setVisibility(View.INVISIBLE);
                        }
                        else if(is_manager){
                            btn_report.setText("管理");
                            //管理员,打开自己
                            if (memberInfo.getUserPhone() != null && memberInfo.getUserPhone()
                                    .equals(AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                                ll_report.setVisibility(View.INVISIBLE);
                                ll_home.setVisibility(View.INVISIBLE);
                            }
                            //管理员，打开创建者
                            if (memberInfo.getUserPhone() != null
                                    && AULiveApplication.currLiveUid.equals(memberInfo.getUserPhone())) {
                                ll_report.setVisibility(View.INVISIBLE);
                            }
                            btn_home.setText("拉黑");

                        }
                        else {
                            btn_report.setText("举报");

                            btn_home.setText("拉黑");
                            if (memberInfo.getUserPhone() != null && memberInfo.getUserPhone()
                                    .equals(AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                                ll_report.setVisibility(View.INVISIBLE);
                            }
                            if (memberInfo.getUserPhone() != null && memberInfo.getUserPhone()
                                    .equals(AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                                ll_home.setVisibility(View.INVISIBLE);
                            }
                        }

                        //管理的实现
                        if(is_manager||avActivity.is_creater){

                            ll_report.setOnClickListener(new View.OnClickListener() {
                                private int mRoom_manager;
                                //是否是直播间的管理员
                                private boolean isManager = false;
                                @Override
                                public void onClick(View v) {

                                    final PopupWindowUtil popupWindow = new PopupWindowUtil(anchor);
                                    popupWindow.setContentView(R.layout.dialog_roomuser_setting);
                                    popupWindow.setOutsideTouchable(true);

                                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override public void onDismiss() {

                                        }
                                    });

//                                    if (mRoom_manager == 1) {
//                                        isManager = true;
//                                    }
//                                    else {
//                                        isManager = false;
//                                    }
//                                    //是否是主播
//                                    if (AULiveApplication.getMyselfUserInfo().isCreater()) {
//                                        RoomManagerHelper roomManagerHelper =
//                                                RoomManagerHelper.getInstance(popupWindow, avActivity, userinfo,
//                                                        isManager, true);
//                                        popupWindow.showBottom();
//                                        return;
//                                    }
//                                    //普通管理员
//                                    //设置按钮的相应事件
//                                    RoomManagerHelper roomManagerHelper =
//                                            RoomManagerHelper.getInstance(popupWindow, avActivity, userinfo,
//                                                    isManager,false);
//                                    popupWindow.showBottom();
                                }
                            });
                        }

                        //举报
                        if(!avActivity.is_creater&&!is_manager){
                            ll_report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showPromptDialog(userinfo.getUid());
                                }
                            });
                        }
                        //拉黑
                        if(!avActivity.is_creater){
                            LaHei();
                        }
                    }
                    else {
                        btn_home.setText("拉黑");
                        btn_report.setText("直播");
                        LaHei();
                        if(userinfo.is_live==1){
                            ll_report.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                    enterRoom(userinfo.getUid(),
                                            AULiveApplication.getMyselfUserInfo().getUserPhone(),
                                            userinfo.getUid(), userinfo.getFace(), userinfo.getNickname());
                                }
                            });
                        }
                        else {
                            ll_report.setVisibility(View.GONE);
                        }
                    }


                    //列表
                    entities = new ArrayList<UserInfoDialogEntity>();
                    data_url = UrlHelper.ROOM_ATTEN;

                    dialog_list = (PullToRefreshView) popupWindow.findId(R.id.dialog_list);
                    dialgoListAdapter = new DialgoListAdapter(mActivity);
                    dialog_list.setAdapter(dialgoListAdapter);
                    dialgoListAdapter.setEntities(entities);
                    dialog_list.setOnRefreshListener(NewNewUserInfoHelper.this);
                    dialog_list.setOnItemClickListener(NewNewUserInfoHelper.this);
                    dialog_list.setRefreshAble(false);
                    dialog_list.initRefresh(PullToRefreshView.HEADER);

                    //箭头
                    final ImageView img_follows_pointer =
                            (ImageView) popupWindow.findId(R.id.img_follows_pointer);
                    final ImageView img_fans_pointer =
                            (ImageView) popupWindow.findId(R.id.img_fans_pointer);
                    final ImageView img_contributors_pointer =
                            (ImageView) popupWindow.findId(R.id.img_contributors_pointer);

                    final LinearLayout layout_atten = (LinearLayout) popupWindow.findId(R.id.layout_atten);
                    layout_atten.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            data_url = UrlHelper.ROOM_ATTEN;
                            dialog_list.initRefresh(PullToRefreshView.HEADER);

                            img_follows_pointer.setVisibility(View.VISIBLE);
                            img_fans_pointer.setVisibility(View.INVISIBLE);
                            img_contributors_pointer.setVisibility(View.INVISIBLE);
                        }
                    });
                    final LinearLayout layout_fans = (LinearLayout) popupWindow.findId(R.id.layout_fans);
                    layout_fans.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            data_url = UrlHelper.ROOM_FANS_LIST;
                            dialog_list.initRefresh(PullToRefreshView.HEADER);

                            img_follows_pointer.setVisibility(View.INVISIBLE);
                            img_fans_pointer.setVisibility(View.VISIBLE);
                            img_contributors_pointer.setVisibility(View.INVISIBLE);
                        }
                    });
                    final LinearLayout layout_recv = (LinearLayout) popupWindow.findId(R.id.layout_recv);
                    layout_recv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            data_url = UrlHelper.ROOM_IN_GOLD_TOPS;
                            dialog_list.initRefresh(PullToRefreshView.HEADER);

                            img_follows_pointer.setVisibility(View.INVISIBLE);
                            img_fans_pointer.setVisibility(View.INVISIBLE);
                            img_contributors_pointer.setVisibility(View.VISIBLE);
                        }
                    });



                    View root_view = popupWindow.findId(R.id.root_view);
                    root_view.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View view) {
                        }
                    });
                    //dialog 消失
                    //点击框框外面消失
                    View click_view = popupWindow.findId(R.id.click_view);
                    click_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                    popupWindow.showBottom();
                    stopProgressDialog();

                }
                else{
                    stopProgressDialog();
                }
            }

            @Override
            public void onFailure(AppException e) {

            }
        }.setReturnType(UserInfo.class));
        request.execute();
    }
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
    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_CENTER:
                    popupWindow.showBottom();
                    break;
            }
        }
    };
    //关注
    public void onEvent(CloseAllPopUpDialogEvent token_event) {
        if (popupWindow != null) {
            popupWindow.dismiss();
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

    //取消关注
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
    private void doReport(final String uid) {

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh(final int mode) {
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
    //拉黑
    private void LaHei(){

        SoloMgmtUtils.checkIsLahei(uid, new SoloRequestListener() {
            @Override
            public void onSuccess() {
                // >在黑名单
                btn_home.setText("解除");
                has_lahei = true;
                ll_home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTextViewListener();
                    }
                });
            }

            @Override
            public void onFailure() {
                // >不在
                btn_home.setText("拉黑");
                has_lahei = false;
                ll_home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setTextViewListener();
                    }
                });
            }
        });
    }
    private void setTextViewListener() {
        if (has_lahei) {
            SoloMgmtUtils.removeLahei(uid, new SoloRequestListener() {
                @Override
                public void onSuccess() {
                    Utils.showCroutonText(mActivity, "解除拉黑");
                    btn_home.setText("拉黑");
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
                    Utils.showCroutonText(mActivity, "成功拉黑");
                    btn_home.setText("解除");
                    has_lahei = true;
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }
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

                    mActivity
                            .startActivity(
                                    new Intent(NewNewUserInfoHelper.this.mActivity, AvActivity.class).putExtra(
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
                                            .putExtra(AvActivity.EXTRA_play_url_KEY,callback.url)

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
}
