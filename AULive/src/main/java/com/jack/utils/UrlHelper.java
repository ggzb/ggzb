package com.jack.utils;

import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.home.MainActivity;

/**
 * @author jack.long
 * @ClassName: UrlHelper
 * @Description: 网络请求地址
 * @date 2014-3-18 下午5:20:14
 */
public class UrlHelper {
   public static final int SUCCESS = 200;
   public static final int FAIL = 500;

   public static final String DEFAULT_ENCODING = "UTF-8";

   public static final String URL_domain = SharedPreferenceTool.getInstance()
       .getString(MainActivity.URL_DOMAIN_key, MainActivity.default_URL_DOMAIN_url);

   /** all url */
   public static final String SERVER_URL = "http://mqphone." + URL_domain + "/";
   public static final String URL_HEAD = "http://mqphone." + URL_domain;// 替换了部分 留意拼接的斜线
   public static final String SERVER_URL_TEST = "http://mqphone." + URL_domain + "/";
//   public static final String IMAGE_ROOT_URL = "http://img." + URL_domain + "/face/";
   public static final String IMAGE_ROOT_URL = "http://cdn.khlexpo.com/face/";

//   public static final String IMAGE_ROOT_URL2 = "http://img." + URL_domain + "/";
   public static final String IMAGE_ROOT_URL2 = "http://cdn.khlexpo.com/";
//   public static final String GIFT_ROOT_URL = "http://img." + URL_domain + "/gift/";
//   public static final String GIFT_ROOT_URL = "http://taohua-1253160199.cosgz.myqcloud.com/gift/";
   public static final String GIFT_ROOT_URL = "http://cdn.khlexpo.com/gift/";

   public static final String ERWEIMA_URL = "http://cdn.khlexpo.com/img/qrcode_wx.jpg";

   //   public static final String baidu_webservice_mcode =
//       "DA:84:46:1F:A9:9C:E2:55:66:CF:ED:98:D4:EB:26:F4:75:9D:75:AE;com.qixi.piaoke";
   // 打开应用提交信息
   public static final String SYNC_URL = SERVER_URL + "index/mqsync";

   // 上传百度user_iD
   public static final String getUploadBaiduUser_Id(String userId) {
      return SERVER_URL + "profile/token?id=" + userId + "&udid=" + MobileConfig.getMobileConfig(
          AULiveApplication.mContext).getIemi();
   }

   public static final String REG_FINISH_URL =
       SERVER_URL + "reg/finish?udid=" + MobileConfig.getMobileConfig(AULiveApplication.mContext)
           .getIemi();

   // 注册信息_基本
   public static final String REG_SIGN_URL = SERVER_URL + "reg/sign";
   // 注册信息_详细
   public static final String REG_DETAIL_URL = SERVER_URL + "reg/detail";
   // 注册 上传图片
   //public static final String REG_UP_PHOTO_URL = URL_HEAD + "/reg/upface";
   // 新上传图片
   public static final String REG_UP_PHOTO_URL = "http://upimg." + URL_domain + "/upload.php";

   public static final String UP_VIDEO_IMG_URL = SERVER_URL + "photo/upload";
   // 邮箱找回密码
   public static final String EMAIL_GET_PWD = SERVER_URL + "profile/backpwd";

   // 手机号找回密码
   public static final String getBackpwdPhone(String phone, String udid, String platform) {
      return SERVER_URL + "profile/backpwd?phone=" + phone + "&udid=" + udid + "&udid=" + platform;
   }

   // 获取验证码
   public static final String GET_AUTH_CODE = SERVER_URL + "reg/sendsms";

   // 重置密码
   public static final String RESET_PWD = SERVER_URL + "reg/resetpwd";

   // 检查密码
   public static final String CHECK_PWD_URL = SERVER_URL + "profile/checkpwd";

   // 修改基本信息
   public static final String MY_BASIC_PROFILE_URL = SERVER_URL + "profile/modify";

   public static final String ADD_REG_URL =
       SERVER_URL + "register/start?udid=" + MobileConfig.getMobileConfig(
           AULiveApplication.mContext).getIemi();

   // 用户登录
   public static final String LOGIN_URL = SERVER_URL + "index/login";
   // 建议与反馈
   public static final String FEED_BACK = SERVER_URL + "other/idea";

   public static final String SHARE_CHANNEL = SERVER_URL + "share/success";

   // 上传发项目音频
   public static final String STEP_UPLOAD_VOICE_URL = SERVER_URL + "msg/upvoice";
   // 退出应用
   public static final String EXIT_APP_URL = SERVER_URL + "profile/quit";

   /** msg url start */
   // 同步消息
   public static final String getSyncChatMsg(String time) {
      return SERVER_URL + "msg/sync?time=" + time;
   }

   // 删除会话信息
   public static final String getDelConversationUrl(String mid) {
      return SERVER_URL + "msg/del?mid=" + mid;
   }

   // 清空会话列表
   public static final String getCleanMsgUrl() {
      return SERVER_URL + "msg/clean";
   }

   // 聊天上传图片
   public static final String UPLOAD_PHOTO_MSG = SERVER_URL + "msg/upimg";
   // 聊天上传音频
   public static final String UPLOAD_CHAT_VOICE = SERVER_URL + "msg/upvoice";

   // 消息
   public static final String MSG_ALL_URL = SERVER_URL + "msg?time=";

   // 打开消息信息
   public static final String getMsgDetailInfoUrl(String mid, int comple) {
      return SERVER_URL + "msg/info?mid=" + mid + "&comple=" + comple;
   }

   // 发送消息
   public static final String SEND_MSG_URL = SERVER_URL + "msg/send";

   public static final String GROUP_ADD_FACE_URL = SERVER_URL + "group/info?id=";

   public static final String getUserInfo(int friendUid) {
      return SERVER_URL + "msg/userinfo?uid=" + friendUid;
   }

   /** msg url end */

//   public static final String tencent_server = "http://203.195.167.34/";
//   //腾讯 上传头像
//   public static final String requestUrl = tencent_server + "image_post.php";
//   ////腾讯 登录
//   public static final String loginUrl = tencent_server + "login.php";
//   public static final String UserInfoUrl = tencent_server + "user_getinfo.php";
//   public static final String registerUrl = tencent_server + "register.php";
//   public static final String replaycreateUrl = tencent_server + "replay_create.php";
//   //已换成live_begin
//   public static final String liveImageUrl = tencent_server + "live_create.php";
//   public static final String testliveImageUrl = tencent_server + "test_live_create.php";
   //创建房间号
   public static final String createRoomNumUrl = SERVER_URL + "live/createroomid";
   //创建房间
   public static final String createRoom = SERVER_URL + "live/begin";
   //关闭房间
   public static final String liveCloseUrl = SERVER_URL + "live/close";
   //进入房间
   public static final String enterRoomUrl = SERVER_URL + "live/enter";
   //退出房间
   public static final String closeLiveUrl = SERVER_URL + "live/quit";
   //视频最热列表
   public static final String getLiveListUrl = SERVER_URL + "live/mqhot";
   //主页关注列表
   public static final String getLiveAttenListUrl = SERVER_URL + "live/mqatten";
   //主页最新列表
   public static final String getLiveNewListUrl = SERVER_URL + "live/mqnews";
   //获取房间用户列表
   public static final String getUserListUrl = SERVER_URL + "live/userlist";
   //点赞
   public static final String liveAddPraiseUrl = SERVER_URL + "live/barrage";
   public static final String user_home_Url = SERVER_URL + "home";
   //礼物相关
   public static final String getGiftUrl = SERVER_URL + "gift/mall";
   public static final String getSendGiftUrl = SERVER_URL + "gift/send";
   //红包相关
   public static final String getSendRedBag = SERVER_URL + "gift/sendredbag";
   //抢红包
   public static final String getGrabRedBag = SERVER_URL + "gift/grabredbag";
   //红包列表
   public static final String getRedbaglist = SERVER_URL + "gift/redbaglist";
   //设置管理员
   public static final String SET_MANAGER_ROOM = SERVER_URL + "live/manage";
   //禁言
   public static final String SET_GAG_ROOM = SERVER_URL + "live/gag";
   //管理员列表
   public static final String SET_MANAGER_LIST_ROOM = SERVER_URL + "live/managelist";
   //   public static final String getRecordListUrl = tencent_server + "replay_getbytime.php";
//   public static final String saveUserInfoUrl = tencent_server + "saveuserinfo.php";
//   public static final String modifyFieldUrl = tencent_server + "user_modifyfields.php";
//   //心跳
//   public static final String heartClickUrl = tencent_server + "update_heart.php";
//   //腾讯的图片地址
//   public static final String rootUrl = tencent_server + "image_get.php";
   //房间相关
   // 关注列表
   public static final String ROOM_ATTEN = SERVER_URL + "atten";
   // 添加关注
   public static final String ROOM_ADD_ATTEN = SERVER_URL + "atten/add";
   // 取消关注
   public static final String ROOM_DEL_ATTEN = SERVER_URL + "atten/del";
   // 粉丝列表
   public static final String ROOM_FANS_LIST = SERVER_URL + "fans";
   // 收入榜
   public static final String ROOM_IN_GOLD_TOPS = SERVER_URL + "live/tops";
   // 关注用户id集合
   public static final String ROOM_ATTEN_UID_LIST = SERVER_URL + "atten/sync";
   //搜索
   public static final String SEARCH_USER = SERVER_URL + "live/search";
   //开通1对1
   public static final String OPEN_ONETONE = SERVER_URL + "profile/onetone";
   //关闭1对1
   public static final String CLOSE_ONETONE = SERVER_URL + "profile/onetone/close";
   //设置1对1单价
   public static final String SET_SOLO_PRICE = SERVER_URL + "onetone/set";
   //获得1对1设置信息
   public static final String GET_SOLO_SETTING = SERVER_URL + "onetone/get";

   //   绑定微信公众号
   public static final String BIND_WX_PUBLIC = SERVER_URL + "profile/bindwx";
   //用户兑换信息
   public static final String EXCHANGE_INFO = SERVER_URL + "exchange/info";
   //兑换支付宝
   public static final String ALIPAY_EXCHANGE = SERVER_URL + "exchange/alipay?";
   //兑换银行卡
   public static final String BANK_EXCHANGE = SERVER_URL + "exchange/lianlianpay?";
   //   兑换钻石
   public static final String EXCHANGE_DIAMOND = SERVER_URL + "exchange/diamond?";
   //   兑换记录
   public static final String EXCHANGE_RECORD = SERVER_URL + "exchange/log?page=";
   //查看超管的操作记录
   public static final String OPERATE_RECORD = SERVER_URL + "manage/log?page=";
   //附近的人
   public static final String NEARBY_PEOPLE = SERVER_URL + "nearby?page=";
   //付费房间
   public static final String ROOM_PAY = SERVER_URL + "live/deduct?liveuid=";
   //开启付费
   public static final String PAY_OPEN = SERVER_URL + "live/payopen?liveuid=";
   //关闭付费
   public static final String PAY_CLOSE = SERVER_URL + "live/payclose?liveuid=";
   //更新钻石数
   public static final String UPDATE_DIAMOND = SERVER_URL + "profile/diamond";
   //临时购买
   public static final String TEMP_DIAMOND = SERVER_URL + "pay/manual?price=";

}
