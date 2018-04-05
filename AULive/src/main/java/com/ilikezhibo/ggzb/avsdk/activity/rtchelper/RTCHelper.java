package com.ilikezhibo.ggzb.avsdk.activity.rtchelper;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.JsonParser;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.custommsg.CustomizeRCTMessage;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by big on 10/12/16.
 */
//public class RTCHelper implements RTCEngineEventListener {
//   private AvActivity mActivity;
//   private Handler mHandler;
//   public boolean is_register = false;
//
//   public RTCHelper(AvActivity mActivity1) {
//      mActivity = mActivity1;
//      init();
//   }
//
//   private void init() {
//      //注册rtc监听
//      mHandler = new Handler() {
//
//         @Override public void handleMessage(android.os.Message msg) {
//            if (msg != null) {
//               String content = null;
//               if (msg.obj != null) {
//                  content = msg.obj.toString();
//               }
//               switch (msg.what) {
//
//                  case MSG_MAIN_REGISTER: {
//                     int status = msg.arg1;
//                     if (status == 200) {
//                        //已经注册过
//                        if (is_register == true) {
//                           break;
//                        }
//                        is_register = true;
//                        doHandleRegister(status);
//                     } else {
//                        //注册失败
//                        doHandleRegister(-1);
//                     }
//                  }
//                  break;
//                  case MSG_MAIN_UNREGISTER: {
//                     int status = msg.arg1;
//                     if (status == 200) {
//
//                     }
//                  }
//                  break;
//                  case MSG_MAIN_START_CALL: {
//                     int status = msg.arg1;
//                     if (status == 200) {
//                        is_linkingRtc = true;
//
//                        //主播调用上麦接口
//                        if (mActivity.is_creater) {
//                           getUpMike(AULiveApplication.currLiveUid, linking_uid);
//                           is_waiting_agree=false;
//                           if (cancel_linkchat != null) {
//                              cancel_linkchat.setVisibility(View.GONE);
//                           }
//                        }
//                        ImageView img_linkchat =
//                            (ImageView) mActivity.findViewById(R.id.img_linkchat);
//                        img_linkchat.setVisibility(View.VISIBLE);
//                        img_linkchat.setOnClickListener(new View.OnClickListener() {
//                           @Override public void onClick(View v) {
//
//                              showIsCloseRTC();
//                           }
//                        });
//                        RTCMsgEntity rejectMsg = new RTCMsgEntity();
//                        rejectMsg.type = LIVE_RTCLIVE_TYPE;
//                        rejectMsg.upmike_type = RTCLIVE_CALL_SUCC;
//                        rejectMsg.uid = AULiveApplication.getUserInfo().getUid();
//                        rejectMsg.nickname = AULiveApplication.getUserInfo().getNickname();
//                        doSendRTCMsg(rejectMsg);
//                     } else if (status < 200) {
//
//                     } else {
//                        RTCMsgEntity rejectMsg = new RTCMsgEntity();
//                        rejectMsg.type = LIVE_RTCLIVE_TYPE;
//                        rejectMsg.upmike_type = RTCLIVE_CALL_FAIL;
//                        rejectMsg.uid = AULiveApplication.getUserInfo().getUid();
//                        rejectMsg.nickname = AULiveApplication.getUserInfo().getNickname();
//                        doSendRTCMsg(rejectMsg);
//                     }
//                  }
//                  break;
//                  case MSG_MAIN_STOP_CALL: {
//                     int status = msg.arg1;
//                     is_linkingRtc = false;
//                     is_waiting_agree=false;
//                     if (status == 200) {
//
//                     }
//                     if (!mActivity.is_creater) {
//                        backToPlayer();
//                     }
//
//                     //主播调用下麦接口
//                     if (mActivity.is_creater) {
//                        //调用下麦接口
//                        getDownMike(AULiveApplication.currLiveUid, linking_uid);
//                     }
//
//                     ImageView img_linkchat = (ImageView) mActivity.findViewById(R.id.img_linkchat);
//                     img_linkchat.setVisibility(View.GONE);
//                  }
//                  break;
//                  case MSG_MAIN_RTC_BREAK: {
//                     //if (is_linkingRtc) {
//                     //   mActivity.mStreamer.stopCall();
//                     //}
//                     is_linkingRtc = false;
//                     Utils.showCroutonText(mActivity, "对方用户已断网");
//                     if (!mActivity.is_creater) {
//                        backToPlayer();
//                     }
//                  }
//                  break;
//                  case MSG_MAIN_INCOMMING_CALL: {
//                     String remoteURI = (String) msg.obj;
//
//                     is_linkingRtc = true;
//                     //if (is_linkingRtc == true) {
//                     //   //如果已经在连麦中
//                     //   mActivity.mStreamer.rejectCall();
//                     //   //辅播给主播发拒绝消息
//                     //   RTCMsgEntity rejectMsg = new RTCMsgEntity();
//                     //   rejectMsg.type = LIVE_RTCLIVE_TYPE;
//                     //   rejectMsg.upmike_type = RTCLIVE_REJECT_INVITE;
//                     //   rejectMsg.uid = AULiveApplication.getUserInfo().getUid();
//                     //   rejectMsg.nickname = AULiveApplication.getUserInfo().getNickname();
//                     //   doSendRTCMsg(rejectMsg);
//                     //} else {
//                     //有连接进来,默认接受
//
//                     streamerFragment.mStreamer.startStream();
//                     if (streamerFragment.mStreamer.answerCall() < 0) {
//                        Utils.showCroutonText(mActivity, "接麦出错");
//                        Trace.d("answerCall() < 0");
//                        if (!mActivity.is_creater) {
//                           streamerFragment.mStreamer.unregisterRTC();
//                           is_register = false;
//                           backToPlayer();
//                        }
//                     } else {
//                        Trace.d("answerCall() startStream");
//                        //mActivity.mStreamer.startStream();
//                     }
//                     //}
//
//                  }
//                  break;
//                  case RTC_GET_AUTH_INFO_SUCC: {
//
//                  }
//                  break;
//                  case RTC_GET_AUTH_INFO_FAILED:
//                  case MSG_MAIN_AUTH_FAILED: {
//                     is_linkingRtc = false;
//                     is_register = false;
//                     Utils.showCroutonText(mActivity, "鉴权失败");
//                  }
//                  break;
//                  case MSG_MAIN_RTC_AGREE: {
//
//                  }
//                  break;
//
//                  default:
//                     if (msg.obj != null) {
//                        Toast.makeText(mActivity, content, Toast.LENGTH_SHORT).show();
//                     }
//               }
//            }
//         }
//      };
//   }
//
//   private void backToPlayer() {
//      //辅播下麦要把推流器换回播发器
//      if (streamerFragment != null) {
//         //streamerFragment.mStreamer.onPause();
//         streamerFragment.mStreamer.stopStream();
//         //streamerFragment.mStreamer.onDestroy();
//         //streamerFragment.mCameraPreview.setVisibility(View.GONE);
//         //streamerFragment.mCameraPreview = null;
//         streamerFragment.closeRTCFragment(mActivity);
//      }
//      mActivity.doPlayerReconnect();
//   }
//
//   private void doHandleRegister(int status) {
//      //主播不用发,辅播发给主播看
//      if (!mActivity.is_creater) {
//         if (status == 200) {
//            //辅播给主播发送同意上麦消息
//            RTCMsgEntity regMsg = new RTCMsgEntity();
//            regMsg.type = LIVE_RTCLIVE_TYPE;
//            regMsg.upmike_type = RTCLIVE_REGISTER_SUCC;
//            regMsg.uid = AULiveApplication.getUserInfo().getUid();
//            regMsg.nickname = AULiveApplication.getUserInfo().getNickname();
//            doSendRTCMsg(regMsg);
//
//            //RTCMsgEntity ctcMsg = new RTCMsgEntity();
//            //ctcMsg.type = LIVE_RTCLIVE_TYPE;
//            //ctcMsg.upmike_type = RTCLIVE_AGREE_INVITE;
//            //ctcMsg.uid = AULiveApplication.getUserInfo().getUid();
//            //ctcMsg.nickname = AULiveApplication.getUserInfo().getNickname();
//            //doSendRTCMsg(ctcMsg);
//         } else {
//            RTCMsgEntity rejectMsg = new RTCMsgEntity();
//            rejectMsg.type = LIVE_RTCLIVE_TYPE;
//            rejectMsg.upmike_type = RTCLIVE_REGISTER_FAIL;
//            rejectMsg.uid = AULiveApplication.getUserInfo().getUid();
//            rejectMsg.nickname = AULiveApplication.getUserInfo().getNickname();
//            doSendRTCMsg(rejectMsg);
//         }
//      }
//
//      //主播
//      if (mActivity.is_creater) {
//         if (status == 200) {
//            //发送邀请消息
//            Trace.d("发送邀请消息");
//            RTCMsgEntity ctcMsg = new RTCMsgEntity();
//            ctcMsg.type = LIVE_RTCLIVE_TYPE;
//            ctcMsg.url = rtc_callback.user_url;
//            ctcMsg.domain_url = rtc_callback.domain_url;
//            ctcMsg.upmike_type = RTCLIVE_INVITE_USER;
//            ctcMsg.uid = linking_uid;
//            ctcMsg.nickname = linking_nickname;
//
//            doSendRTCMsg(ctcMsg);
//         }
//      }
//   }
//
//   ////////////////////////////////////////////////////////////
//
//   private final static int MSG_MAIN_REGISTER = 8001;
//   private final static int MSG_MAIN_START_CALL = 8002;
//   private final static int MSG_MAIN_STOP_CALL = 8003;
//   private final static int MSG_MAIN_INCOMMING_CALL = 8004;
//   private final static int MSG_MAIN_UNREGISTER = 8005;
//   private final static int MSG_MAIN_AUTH_FAILED = -8001;
//   private final static int MSG_MAIN_RTC_BREAK = -8002;
//   private final static int MSG_MAIN_RTC_AGREE = 8006;
//
//   public static final int RTC_GET_AUTH_INFO_SUCC = 6000;
//   public static final int RTC_GET_AUTH_INFO_FAILED = -6000;
//
//   //RTC监听接口
//   @Override public void onRegister(int status) {
//      Trace.d("onRegister status:" + status);
//      mHandler.obtainMessage(MSG_MAIN_REGISTER, status, 0).sendToTarget();
//   }
//
//   @Override public void onUnregister(int status) {
//      Trace.d("onUnregister status:" + status);
//      mHandler.obtainMessage(MSG_MAIN_UNREGISTER, status, 0).sendToTarget();
//   }
//
//   @Override public void onCallStart(int status) {
//      Trace.d("onCallStart status:" + status);
//
//      mHandler.obtainMessage(MSG_MAIN_START_CALL, status, 0).sendToTarget();
//   }
//
//   @Override public void onCallStop(int status) {
//      Trace.d("onCallStop status:" + status);
//      mHandler.obtainMessage(MSG_MAIN_STOP_CALL, status, 0).sendToTarget();
//   }
//
//   @Override public void onIncomingCall(String remoteURI) {
//      Trace.d("onIncomingCall remoteURI:" + remoteURI);
//      mHandler.obtainMessage(MSG_MAIN_INCOMMING_CALL, remoteURI).sendToTarget();
//   }
//
//   @Override public void onServiceError() {
//      Trace.d("onServiceError()");
//      mHandler.obtainMessage(MSG_MAIN_RTC_BREAK, 0).sendToTarget();
//   }
//
//   @Override public void onDataChannelConnectChange(int status) {
//      Trace.d("--------onDataChannelConnectChange status = " + status);
//   }
//
//   @Override public void onAuthFailed() {
//      Trace.d("onAuthFailed()");
//      if (mHandler != null) {
//         mHandler.obtainMessage(MSG_MAIN_AUTH_FAILED).sendToTarget();
//      }
//   }
//
//   //////////////////////////////////////////////////////////////
//   //当前在连麦中的uid,主播缓存用
//   public String linking_uid = null;
//   public String linking_nickname = null;
//
//   //是否在连麦中
//   public boolean is_linkingRtc = false;
//
//   //主播连麦时返回的鉴权消息
//   public MikeEntity rtc_callback = null;
//
//   public boolean is_waiting_agree = false;
//   TextView cancel_linkchat;
//
//   //主播上麦签权
//   public void hostRTCInfo(String live, final String zhubo_uid, final String fubo_uid,
//       final String fubo_nickname) {
//
//      is_waiting_agree = true;
//      cancel_linkchat = (TextView) mActivity.findViewById(R.id.cancel_linkchat);
//      cancel_linkchat.setVisibility(View.VISIBLE);
//      cancel_linkchat.setOnClickListener(new View.OnClickListener() {
//         @Override public void onClick(View v) {
//            showIsCloseWaiting();
//         }
//      });
//
//      RequestInformation request = null;
//      try {
//         StringBuilder sb = new StringBuilder(UrlHelper.SERVER_URL
//             + "live/mikesign?live="
//             + live
//             + "&liveuid="
//             + zhubo_uid
//             + "&user="
//             + fubo_uid);
//         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
//      } catch (Exception e) {
//         e.printStackTrace();
//      }
//
//      request.setCallback(new JsonCallback<MikeEntity>() {
//
//         @Override public void onCallback(MikeEntity callback) {
//            if (callback == null) {
//               Utils.showCroutonText(mActivity, Utils.trans(R.string.get_info_fail));
//               return;
//            }
//
//            if (callback.getStat() == 200) {
//
//               rtc_callback = callback;
//
//               linking_uid = fubo_uid;
//               linking_nickname = fubo_nickname;
//
//               //先自己注册
//               //String authString =
//               //    callback.live_url.substring(callback.live_url.indexOf("accesskey="));
//
//               String authString = callback.live_url;
//
//               //已经鉴权注册过
//               if (is_register == true) {
//                  doHandleRegister(200);
//               } else {
//                  //mActivity.mStreamer.unregisterRTC();
//
//                  Trace.d("authString:" + authString + " authstring:" + authString.substring(
//                      authString.indexOf("accesskey=")));
//                  KSYStreamerConfig mStreamerBuilder = mActivity.mStreamer.getConfig();
//                  mStreamerBuilder.setRTCAuthURI(callback.domain_url)
//                      .setRTCAuthString(authString.substring(authString.indexOf("accesskey=")))
//                      .setRTCUID(zhubo_uid)
//                      .setRTCUniqueName("aulive")
//                      .setRTCPipLeft(0.7f)
//                      .setRTCPipTop(0.35f)
//                      .setRTCPipWidth(0.3f)
//                      .setRTCPipHeight(0.3f)
//                      .setRTCFps(15)
//                      .setRTCVideoBitrate(256)
//                      .setEnableTLS(true)
//                      .setCalleeMode(false);
//                  mActivity.mStreamer.updateConfig(mStreamerBuilder);
//
//                  //反注册拒绝消息中处理
//                  //mActivity.mStreamer.unregisterAllEventListeners();
//                  mActivity.mStreamer.registerRTCEventListener(RTCHelper.this);
//
//                  //注册
//                  int result = mActivity.mStreamer.registerRTC();
//                  if (result == RecorderConstants.RTC_ILLEGAL_STATE) {
//                     Utils.showCroutonText(mActivity, "RTC鉴权注册失败");
//                  } else if (result == RecorderConstants.RTC_MISSING_CONFIG) {
//                     Utils.showCroutonText(mActivity, "无效的鉴权配置");
//                  }
//               }
//            } else {
//               Utils.showCroutonText(mActivity, "" + callback.getMsg());
//            }
//         }
//
//         @Override public void onFailure(AppException e) {
//            Utils.showCroutonText(mActivity, Utils.trans(R.string.get_info_fail));
//         }
//      }.setReturnType(MikeEntity.class));
//      request.execute();
//   }
//
//   //上麦
//   public void getUpMike(String zhubo_uid, String fubo_uid) {
//      RequestInformation request = null;
//
//      try {
//         StringBuilder sb = new StringBuilder(
//             UrlHelper.SERVER_URL + "live/upmike?liveuid=" + zhubo_uid + "&user=" + fubo_uid);
//         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
//      } catch (Exception e) {
//         e.printStackTrace();
//      }
//
//      request.setCallback(new JsonCallback<MikeEntity>() {
//
//         @Override public void onCallback(MikeEntity callback) {
//            if (callback == null) {
//               Utils.showCroutonText(mActivity, Utils.trans(R.string.get_info_fail));
//               return;
//            }
//
//            if (callback.getStat() == 200) {
//
//            } else {
//
//            }
//         }
//
//         @Override public void onFailure(AppException e) {
//            Utils.showCroutonText(mActivity, Utils.trans(R.string.get_info_fail));
//         }
//      }.setReturnType(MikeEntity.class));
//      request.execute();
//   }
//
//   //下麦
//   public void getDownMike(String zhubo_uid, String fubo_uid) {
//      RequestInformation request = null;
//
//      try {
//         StringBuilder sb = new StringBuilder(
//             UrlHelper.SERVER_URL + "live/downmike?liveuid=" + zhubo_uid + "&user=" + fubo_uid);
//         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
//      } catch (Exception e) {
//         e.printStackTrace();
//      }
//
//      request.setCallback(new JsonCallback<MikeEntity>() {
//
//         @Override public void onCallback(MikeEntity callback) {
//            if (callback == null) {
//               Utils.showCroutonText(mActivity, Utils.trans(R.string.get_info_fail));
//               return;
//            }
//
//            if (callback.getStat() == 200) {
//
//            } else {
//
//            }
//         }
//
//         @Override public void onFailure(AppException e) {
//            Utils.showCroutonText(mActivity, Utils.trans(R.string.get_info_fail));
//         }
//      }.setReturnType(MikeEntity.class));
//      request.execute();
//   }
//
//   ///自定义消息类型相关
//   // 上麦
//   public final static String LIVE_RTCLIVE_TYPE = "live_rtc";                 // 上麦类型
//
//   public final static int RTCLIVE_INVITE_USER = 1;           // 主播邀请玩家上麦 (主播先自己注册成功后发邀请)
//   public final static int RTCLIVE_AGREE_INVITE = 2;          // 玩家同意主播邀请
//   public final static int RTCLIVE_REJECT_INVITE = 3;         // 玩家拒绝主播邀请
//
//   //可能用不到,RTCEngineEventListener有相关的回调
//   public final static int RTCLIVE_REGISTER_FAIL = 4;        // 上麦注册失败
//   public final static int RTCLIVE_REGISTER_SUCC = 5;       // 上麦注册成功
//   public final static int RTCLIVE_CALL_SUCC = 6;      // 成功（连麦成功）呼叫
//   public final static int RTCLIVE_CALL_FAIL = 7;     // 呼叫失败 (连麦失败)
//   public final static int RTCLIVE_EXIT = 8;     // 下麦
//
//   public void doSendRTCMsg(RTCMsgEntity rtc_msg) {
//      String message = JsonParser.serializeToJson(rtc_msg);
//
//      CustomizeRCTMessage msg = new CustomizeRCTMessage();
//      msg.type = rtc_msg.type;
//      msg.data = message;
//
//      //礼物,分开发,优先级比较高
//      if (msg.type != null && msg.type.equals(LIVE_RTCLIVE_TYPE)) {
//         if (RongIMClient.getInstance() != null) {
//            RongIMClient.getInstance()
//                .sendMessage(Conversation.ConversationType.CHATROOM, AULiveApplication.currLiveUid,
//                    msg, "", "", new RongIMClient.SendMessageCallback() {
//                       @Override public void onError(Integer messageId, RongIMClient.ErrorCode e) {
//
//                       }
//
//                       @Override public void onSuccess(Integer integer) {
//                          //System.gc();
//                       }
//                    }, new RongIMClient.ResultCallback<Message>() {
//                       @Override public void onError(RongIMClient.ErrorCode errorCode) {
//
//                       }
//
//                       @Override public void onSuccess(Message message) {
//                          //加入到自己的chat listview
//
//                       }
//                    });
//         }
//      }
//   }
//
//   //是主播时,主播要处理的连麦消息
//   public void handleHostTCTMsg(CustomizeRCTMessage message) {
//      //消息类型
//      String type = message.type;
//      String customText = message.data;
//      RTCMsgEntity msg = JsonParser.deserializeByJson(customText, RTCMsgEntity.class);
//
//      // 玩家同意主播邀请
//      if (msg.upmike_type == RTCLIVE_REGISTER_SUCC) {
//         //同意后,
//         Utils.showCroutonText(mActivity, msg.nickname + "同意了你的连麦");
//         //直接call,之前缓存起来的uid
//         if (mActivity.mStreamer.startCall(linking_uid) < 0) {
//            // startcall in illegal state
//            Utils.showCroutonText(mActivity, "呼叫视频连麦失败");
//            //给辅播反呼叫失败消息,返回播放器
//
//            RTCMsgEntity rejectMsg = new RTCMsgEntity();
//            rejectMsg.type = LIVE_RTCLIVE_TYPE;
//            rejectMsg.upmike_type = RTCLIVE_CALL_FAIL;
//            rejectMsg.uid = linking_uid;
//            rejectMsg.nickname = linking_nickname;
//            doSendRTCMsg(rejectMsg);
//         } else {
//            Utils.showCroutonText(mActivity, "呼叫连麦中。。。");
//         }
//      }
//
//      // 玩家拒绝主播邀请
//      if (msg.upmike_type == RTCLIVE_REJECT_INVITE) {
//         //对方拒绝后,主播直接反注册
//         //is_linkingRtc = false;
//         //linking_uid = null;
//
//         if (cancel_linkchat != null) {
//            cancel_linkchat.setVisibility(View.GONE);
//            is_waiting_agree = false;
//         }
//
//         Utils.showCroutonText(mActivity, msg.nickname + "拒绝了你的连麦");
//      }
//
//      // 上麦注册失败
//      if (msg.upmike_type == RTCLIVE_REGISTER_FAIL) {
//         is_linkingRtc = false;
//         Utils.showCroutonText(mActivity, "对方上麦注册失败");
//      }
//
//      // 上麦注册成功
//      if (msg.upmike_type == RTCLIVE_AGREE_INVITE) {
//         //Utils.showCroutonText(mActivity,"上麦注册成功");
//      }
//
//      // 呼叫成功（连麦成功）
//      if (msg.upmike_type == RTCLIVE_CALL_SUCC) {
//         //Utils.showCroutonText(mActivity, "呼叫连麦成功");
//      }
//
//      // 下麦
//      //在回调接口oncallstop实现
//      if (msg.upmike_type == RTCLIVE_EXIT) {
//         Utils.showCroutonText(mActivity, "对方关闭了连麦");
//      }
//   }
//
//   //是辅播端时,需要处理连麦消息
//   public void handlePlayerTCTMsg(CustomizeRCTMessage message) {
//      //消息类型
//      String type = message.type;
//      String customText = message.data;
//      RTCMsgEntity msg = JsonParser.deserializeByJson(customText, RTCMsgEntity.class);
//
//      //等于自己的uid才处理
//      if (msg.uid != null && msg.uid.equals(AULiveApplication.getUserInfo().getUid())) {
//
//         // 主播邀请玩家上麦 (主播先自己注册成功后发邀请)
//         if (msg.upmike_type == RTCLIVE_INVITE_USER) {
//            // 1.对方接受后,对方鉴权接受后"辅播鉴权及注册",返回"同意消息",主播直接call以及发call消息;辅播有call来时默认接受call
//            if (!is_linkingRtc) {
//               //显示是否连麦的dailog
//               showIsRTCDialog(msg.url, msg.domain_url);
//            }
//         }
//         // 主播发过来的取消连接,在回调接口oncallstop实现
//         if (msg.upmike_type == RTCLIVE_EXIT) {
//            Utils.showCroutonText(mActivity, "主播取消了连麦");
//            if (agreeOrNotcustomDialog != null) {
//               agreeOrNotcustomDialog.dismiss();
//            }
//            //如果已经在连麦,关闭流,装播放
//            if (streamerFragment != null && streamerFragment.mStreamer != null) {
//               if (streamerFragment.mCameraPreview.getVisibility() == View.VISIBLE) {
//                  backToPlayer();
//               }
//            }
//            is_linkingRtc = false;
//         }
//
//         // 呼叫失败 (连麦失败)
//         if (msg.upmike_type == RTCLIVE_CALL_FAIL) {
//            Utils.showCroutonText(mActivity, "主播呼叫连麦失败");
//            //主播呼叫失败与辅播无关
//            //mActivity.mStreamer.unregisterRTC();
//            //is_register = false;
//            backToPlayer();
//         }
//      }
//   }
//
//   ////////////////////////////////////////////////////////////
//   private CustomDialog agreeOrNotcustomDialog;
//
//   //是否同意连麦
//   private void showIsRTCDialog(final String authString_temp, final String domain_url_temp) {
//      //把上一个dialog取消掉
//      if (agreeOrNotcustomDialog != null) {
//         agreeOrNotcustomDialog.dismiss();
//      }
//      agreeOrNotcustomDialog = new CustomDialog(mActivity, new CustomDialogListener() {
//
//         @Override public void onDialogClosed(int closeType) {
//            switch (closeType) {
//               case CustomDialogListener.BUTTON_POSITIVE:
//
//                  //同意后1.停止播放器,启动推流录像
//                  // 2.鉴权与注册,然后发"同意连麦"消息
//                  if (mActivity.ksyMediaPlayer != null) {
//                     mActivity.ksyMediaPlayer.pause();
//                     mActivity.ksyMediaPlayer.stop();
//                     mActivity.ksyMediaPlayer.release();
//                     //mActivity.ksyMediaPlayer.reset();
//                     //mActivity.ksyMediaPlayer=null;
//                  }
//
//                  //已经鉴权注册过,mStream已经new过的必须重新注册
//                  if (is_register == true) {
//                     mActivity.mVideoSurfaceView.setVisibility(View.GONE);
//                     if (streamerFragment != null) {
//                        streamerFragment.openRTCFragment(mActivity);
//                     }
//                     doHandleRegister(200);
//                  } else {
//                     //辅播推流不能先推流,必须在ansercall之后才startStream();
//
//                     //整个activity生命周期只有一个mStreamer
//                     if (streamerFragment == null) {
//                        streamerFragment = StreamerFragment.newInstance();
//                        streamerFragment.openRTCFragment(mActivity);
//                     }
//
//                     authString1 = authString_temp;
//                     domain_url = domain_url_temp;
//                     //等streamerFragment,初始化后回调doFuboRegister()
//                  }
//                  break;
//               case CustomDialogListener.BUTTON_NEUTRAL:
//
//                  //辅播给主播发拒绝消息
//                  RTCMsgEntity rejectMsg = new RTCMsgEntity();
//                  rejectMsg.type = LIVE_RTCLIVE_TYPE;
//                  rejectMsg.upmike_type = RTCLIVE_REJECT_INVITE;
//                  rejectMsg.uid = AULiveApplication.getUserInfo().getUid();
//                  rejectMsg.nickname = AULiveApplication.getUserInfo().getNickname();
//                  doSendRTCMsg(rejectMsg);
//
//                  break;
//            }
//         }
//      });
//
//      agreeOrNotcustomDialog.setCustomMessage("主播邀请你进行连麦");
//      agreeOrNotcustomDialog.setType(CustomDialog.DOUBLE_BTN);
//      agreeOrNotcustomDialog.show();
//      //一定要在show之后
//      agreeOrNotcustomDialog.setCustomTitle("是否连麦");
//      agreeOrNotcustomDialog.setContentTextSize(14);
//      agreeOrNotcustomDialog.setCancelable(false);
//      agreeOrNotcustomDialog.setCanceledOnTouchOutside(false);
//   }
//
//   public String authString1;
//   public String domain_url;
//
//   public void doFuboRegister() {
//      String authString = authString1;
//      KSYStreamerConfig mStreamerBuilder = streamerFragment.mStreamer.getConfig();
//      mStreamerBuilder.setRTCAuthURI(domain_url)
//          .setRTCAuthString(authString.substring(authString.indexOf("accesskey=")))
//          .setRTCUID(AULiveApplication.getUserInfo().getUid())
//          .setRTCUniqueName("aulive")
//          .setRTCPipLeft(0.7f)
//          .setRTCPipTop(0.35f)
//          .setRTCPipWidth(0.3f)
//          .setRTCPipHeight(0.3f)
//          .setRTCFps(15)
//          .setRTCVideoBitrate(256)
//          .setEnableTLS(true)
//          .setCalleeMode(true);
//
//      streamerFragment.mStreamer.updateConfig(mStreamerBuilder);
//
//      //mActivity.mStreamer.unregisterRTC();
//      //mActivity.mStreamer.unregisterAllEventListeners();
//
//      streamerFragment.mStreamer.registerRTCEventListener(RTCHelper.this);
//
//      //注册
//      int result = streamerFragment.mStreamer.registerRTC();
//      if (result == RecorderConstants.RTC_ILLEGAL_STATE) {
//         Utils.showCroutonText(mActivity, "RTC鉴权注册失败");
//      } else if (result == RecorderConstants.RTC_MISSING_CONFIG) {
//         Utils.showCroutonText(mActivity, "无效的鉴权配置");
//      }
//   }
//
//   //关闭连麦
//   private void showIsCloseRTC() {
//      final CustomDialog customDialog = new CustomDialog(mActivity, new CustomDialogListener() {
//
//         @Override public void onDialogClosed(int closeType) {
//            switch (closeType) {
//               case CustomDialogListener.BUTTON_POSITIVE:
//                  if (mActivity.is_creater) {
//                     //关闭连麦
//                     if (mActivity.mStreamer != null && mActivity.mStreamer.stopCall() < 0) {
//                        Utils.showCroutonText(mActivity, "关闭连麦出错");
//                        return;
//                     }
//                  } else {
//                     if (streamerFragment.mStreamer != null
//                         && streamerFragment.mStreamer.stopCall() < 0) {
//                        Utils.showCroutonText(mActivity, "关闭连麦出错");
//                        return;
//                     }
//                  }
//
//                  ImageView img_linkchat = (ImageView) mActivity.findViewById(R.id.img_linkchat);
//                  img_linkchat.setVisibility(View.GONE);
//
//                  //发给主播看,我已经关闭连麦
//                  RTCMsgEntity rejectMsg = new RTCMsgEntity();
//                  rejectMsg.type = LIVE_RTCLIVE_TYPE;
//                  rejectMsg.upmike_type = RTCLIVE_EXIT;
//                  rejectMsg.uid = AULiveApplication.getUserInfo().getUid();
//                  rejectMsg.nickname = AULiveApplication.getUserInfo().getNickname();
//                  doSendRTCMsg(rejectMsg);
//
//                  break;
//               case CustomDialogListener.BUTTON_NEUTRAL:
//
//                  break;
//            }
//         }
//      });
//
//      customDialog.setCustomMessage("是否确定关闭连麦");
//      customDialog.setType(CustomDialog.DOUBLE_BTN);
//      customDialog.show();
//      //一定要在show之后
//      customDialog.setCustomTitle("是否关闭连麦");
//      customDialog.setContentTextSize(14);
//      customDialog.setCancelable(false);
//      customDialog.setCanceledOnTouchOutside(false);
//   }
//
//   //主播关闭等待
//   private void showIsCloseWaiting() {
//      final CustomDialog customDialog = new CustomDialog(mActivity, new CustomDialogListener() {
//
//         @Override public void onDialogClosed(int closeType) {
//            switch (closeType) {
//               case CustomDialogListener.BUTTON_POSITIVE:
//
//                  is_waiting_agree = false;
//                  cancel_linkchat.setVisibility(View.GONE);
//
//                  //发给辅播看,我取消等待连麦
//                  RTCMsgEntity rejectMsg = new RTCMsgEntity();
//                  rejectMsg.type = LIVE_RTCLIVE_TYPE;
//                  rejectMsg.upmike_type = RTCLIVE_EXIT;
//                  rejectMsg.uid = linking_uid;
//                  rejectMsg.nickname = linking_nickname;
//                  doSendRTCMsg(rejectMsg);
//
//                  break;
//               case CustomDialogListener.BUTTON_NEUTRAL:
//
//                  break;
//            }
//         }
//      });
//
//      customDialog.setCustomMessage("是否取消等待连麦");
//      customDialog.setType(CustomDialog.DOUBLE_BTN);
//      customDialog.show();
//      //一定要在show之后
//      customDialog.setCustomTitle("是否取消连麦");
//      customDialog.setContentTextSize(14);
//      //customDialog.setCancelable(false);
//      //customDialog.setCanceledOnTouchOutside(false);
//   }
//
//   public StreamerFragment streamerFragment;
//}
