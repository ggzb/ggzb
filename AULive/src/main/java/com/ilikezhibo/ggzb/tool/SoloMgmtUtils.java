package com.ilikezhibo.ggzb.tool;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.MemberInfo;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoHelper;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;

import com.jack.utils.Trace;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import popwindow.PopupWindowUtil;

/**
 * Created by r on 16/10/20.
 */

public class SoloMgmtUtils {



    // >发送视频聊天
    public static final int SEND_VIDEO = 1;
    // >需要充值
    public static final int NEED_PREPAID = 3;
    // >对方设置的单价
    public static String price;
    // >未开通
    public static final int NO_OPEN = 0;
    // >正在直播
    public static final int LIVE_TRUE = 10;
    // >可以发送
    public static final int CAN_SEND = 200;
    // >显示发送
    public static final int SHOW_SEND = 20;
    // >显示发送
    public static final int GO_SEND = 21;
    // >需要提示
    public static  final int NEED_PROMPT = 22;


    /**
     * 检查是否拉黑
     */
    public static void checkIsLahei(final String chatId, final SoloRequestListener soloRequestListener) {
        try {
            if (RongIM.getInstance().getRongIMClient() != null) {
                RongIM.getInstance()
                        .getRongIMClient()
                        .getBlacklistStatus(chatId,
                                new RongIMClient.ResultCallback<RongIMClient.BlacklistStatus>() {
                                    @Override
                                    public void onSuccess(
                                            RongIMClient.BlacklistStatus blacklistStatus) {
                                        if (blacklistStatus
                                                == RongIMClient.BlacklistStatus.IN_BLACK_LIST) {
                                            Trace.d("**>在黑名单");
                                            soloRequestListener.onSuccess();
                                        } else {
                                            soloRequestListener.onFailure();
                                        }
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {

                                    }
                                });
            }
        } catch (Exception e) {

        }
    }

    /**
     * 解除拉黑
     *
     * @param uid
     */
    public static void removeLahei(final String uid, final SoloRequestListener soloRequestListener) {
        try {
            RongIM.getInstance()
                    .getRongIMClient()
                    .removeFromBlacklist(uid, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            soloRequestListener.onSuccess();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            soloRequestListener.onFailure();
                        }
                    });
        } catch (Exception e) {

        }
    }

    /**
     * 拉黑
     *
     * @param uid
     */
    public static void addLahei(final String uid, final SoloRequestListener soloRequestListener) {
        if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
            return;
        }
        RongIM.getInstance()
                .getRongIMClient()
                .addToBlacklist(uid, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        soloRequestListener.onSuccess();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        soloRequestListener.onFailure();
                    }
                });
    }

    /**
     * 清空聊天内容
     */
    public static void clearChat(final Activity activity, String uid) {
        if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
            return;
        }
        RongIM.getInstance()
                .getRongIMClient()
                .clearMessages(Conversation.ConversationType.PRIVATE, uid,
                        new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                Utils.showCroutonText(activity, "成功清空");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
    }

    /**
     * 开通1对1
     */

    /**
     * 关闭1v1
     *
     * @param uid
     */

    /**
     * 设置1v1单价
     *
     * @param price
     */


    /**
     * 获得1v1设置
     *
     * @param ed_cond_setprice
     */

    /**
     * 获得主播设置的1v1的单价
     *
     * @param uid
     */

    /**
     * 检查是否可以对当前主播发起1v1
     */

    public static void checkAnchorOhter(String anchor, final Handler handler) {
        StringBuilder sb = new StringBuilder(UrlHelper.user_home_Url + "?u=" + anchor);
        RequestInformation request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
        request.setCallback(new JsonCallback<UserInfo>() {

            @Override
            public void onCallback(UserInfo callback) {
                if (callback != null) {
                    if (callback.getStat() == 200) {
                        int is_onetone = callback.getUserinfo().getOnetone();
                        if (is_onetone == 0) {
                            Message msg = Message.obtain();
                            msg.what = NO_OPEN;
                            msg.obj = callback.getMsg();
                            handler.sendMessage(msg);
                        } else if (callback.getUserinfo().is_live == 1) {
                            Message msg = Message.obtain();
                            msg.what = LIVE_TRUE;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = Message.obtain();
                            msg.what = CAN_SEND;
                            handler.sendMessage(msg);
                        }
                    }
                }
            }

            @Override
            public void onFailure(AppException e) {
                Utils.showMessage(Utils.trans(R.string.get_info_fail));
            }
        }.setReturnType(UserInfo.class));

        request.execute();
    }

    /**
     * 显示对话框
     */
    public static void showDialog(Activity activity, Handler handler, SoloRequestListener listener) {
        String content = "聊天声明: 我们提倡绿色聊天, 封面和聊天内容含吸烟、低俗、引诱、暴漏等都将被封停账号，网警" +
                "24小时在线巡查。\n\u3000\u3000如若选择继续进行视频聊天则认为已阅读以上条例并同意执行。";
        boolean hasTooltip;
        String toolTip = "";
        if(checkIsNewOnetOne()) {
            toolTip =  "您获得免费视频聊天1分钟体验";
            hasTooltip = true;
        }else {
            hasTooltip = false;
        }
        String leftButton = "同意";
        String rightButton = "取消";
        int color = Color.parseColor("#E40A0A");
        showCustomDialog(activity, false, content, hasTooltip, toolTip, color, leftButton, rightButton, listener);
    }

    private static boolean checkIsNewOnetOne() {
        return false;
    }

    /**
     *
     * @param hasTitle 是否包含标题栏
     * @param content 对话框显示内容
     * @param hasToolTip 是否包含提示条
     * @param toolTip 提示条内容
     * @param color 提示条字体颜色
     * @param leftButton 左边按钮内容
     * @param rightButton 右边按钮内容
     */
    public static void showCustomDialog(Activity activity, boolean hasTitle, String content, boolean hasToolTip, String toolTip,
                                        int color, String leftButton, String rightButton, final SoloRequestListener listener) {
        final CustomDialog customDialog = new CustomDialog(
                activity,
                new CustomDialogListener() {
                    @Override
                    public void onDialogClosed(int closeType) {

                        switch (closeType) {

                            case CustomDialogListener.BUTTON_POSITIVE:
                                listener.onSuccess();
                                break;

                            case CustomDialogListener.BUTTON_NEUTRAL:

                                break;
                        }
                    }
                });
        CustomDialog.MyCheckboxListener mCheckBoxListener = new CustomDialog.MyCheckboxListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // >不再显示此对话框
                    SharedPreferenceTool.getInstance().saveBoolean(SharedPreferenceTool.SHOW_SOLO_DIALOG, false);
                } else {
                    // >下次继续显示
                    SharedPreferenceTool.getInstance().saveBoolean(SharedPreferenceTool.SHOW_SOLO_DIALOG, true);
                }
            }
        };
        customDialog.setMyCheckboxListener(mCheckBoxListener);
        customDialog.setHasTitle(hasTitle);
        customDialog.setHasToolTip(hasToolTip);
        customDialog.setToolTipContent(toolTip);
        customDialog.setToolTipColor(color);
        customDialog.setCheckboxContent(true, "下次不再显示");
        customDialog.setButtonText(leftButton, rightButton);
        customDialog.setCustomMessage(content);
        customDialog.setCancelable(true);
        customDialog.setType(CustomDialog.DOUBLE_BTN);
        if(customDialog.isShowing() || activity.isFinishing()) {
            return;
        } else {
            customDialog.show();
        }
    }
    public static void openMemberInfo(final String uid, final View view, final Activity activity) {
        RequestInformation request = null;
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

                if (callback.getStat() == 200) {
                    final LoginUserEntity userinfo = callback.getUserinfo();
                    PopupWindowUtil popupWindow = new PopupWindowUtil(view);
                    popupWindow.setContentView(R.layout.dialog_myroom_userinfo);
                    popupWindow.setOutsideTouchable(true);

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                        }
                    });
                    Trace.d("***" +  "获取到了");
                    MemberInfo memberInfo =
                            new MemberInfo(uid, userinfo.getNickname(),
                                    userinfo.getFace(), userinfo.getGrade());
                    //做View init
                    UserInfoHelper userInfoHelper =
                            UserInfoHelper.getInstance(view, popupWindow, activity, memberInfo);

                } else {
                    Utils.showMessage(Utils.trans(R.string.get_info_fail));
                }
            }

            @Override
            public void onFailure(AppException e) {
                e.printStackTrace();
            }
        }.setReturnType(UserInfo.class));
        request.execute();
    }
}

