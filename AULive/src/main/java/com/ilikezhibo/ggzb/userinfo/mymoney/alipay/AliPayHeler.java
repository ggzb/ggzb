package com.ilikezhibo.ggzb.userinfo.mymoney.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ilikezhibo.ggzb.R;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.MobileConfig;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.userinfo.buydiamond.BuyDiamondActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class AliPayHeler {

   private Activity contex;
   // 支付宝相关
   // /////////////////////////////////2446397883@qq.com     2088521485091360////////////////////////////////////////////////////////////////////
   // 商户PID
   public static final String PARTNER = "2088031564235232";

   // 商户收款账号
   public static final String SELLER = "3300240017@qq.com";

   private static final int SDK_PAY_FLAG = 1;

   private static final int SDK_CHECK_FLAG = 2;

   private Handler mHandler = new Handler() {
      public void handleMessage(Message msg) {
         switch (msg.what) {
            case SDK_PAY_FLAG: {
               PayResult payResult = new PayResult((String) msg.obj);

               // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
               String resultInfo = payResult.getResult();

               String resultStatus = payResult.getResultStatus();

               // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
               if (TextUtils.equals(resultStatus, "9000")) {
                  Toast.makeText(contex, "支付成功", Toast.LENGTH_SHORT).show();
                  paySucc();
               } else {
                  // 判断resultStatus 为非“9000”则代表可能支付失败
                  // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                  if (TextUtils.equals(resultStatus, "8000")) {
                     Toast.makeText(contex, "支付结果确认中", Toast.LENGTH_SHORT).show();
                  } else {
                     // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                     Toast.makeText(contex, "支付失败", Toast.LENGTH_SHORT).show();
                  }
               }
               break;
            }
            case SDK_CHECK_FLAG: {
               Toast.makeText(contex, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
               break;
            }
            default:
               break;
         }
      }
   };

   // 订单号
   private String order_id = null;
   private String money_count = null;
   // 充值用户uid
   private String uid = null;

   public void sendorder(Activity contex1, String uid1, String money, String diamond) {

      contex = contex1;
      uid = uid1;
      // 获取order id
      order_id = getOutTradeNo();
      money_count = money;

      MobileConfig config = MobileConfig.getMobileConfig(contex);
      RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD + "/alipay/aliorder",
          RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("out_trade_no", this.order_id);
      request.addPostParams("money", money_count + "");
      request.addPostParams("diamond", diamond + "");
      request.addPostParams("pf", "android");
      request.addPostParams("channel", config.getAppMetaData(contex, "UMENG_CHANNEL"));
      // 订单
      final String orderInfo =
              getOrderInfo("用户:" + uid + "充值:" + money_count, Utils.trans(R.string.app_name) + "用户:" + uid + "充值人民币:" + money_count,
                      money_count + "", order_id, UrlHelper.URL_HEAD + "/alipay/alinotify");
      request.addPostParams("order_desc",orderInfo);

      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
            Utils.showMessage("请求数据失败");
         }

         @Override public void onCallback(String callback) {
            Trace.d(callback);
            try {
               JSONObject json = new JSONObject(callback);
               if (json.optInt("stat") == 200) {
                  // 支付接口
                  String signStr = json.optString("sign");
                  if (signStr != null){
                     pay(orderInfo,signStr);
                  }
               } else {
                  Utils.showMessage(json.optString("msg"));
               }
            } catch (JSONException e) {
               e.printStackTrace();
            }
         }
      });

      request.execute();
   }

   private void paySucc() {
      Trace.d("paySucc()");
      RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD + "/alipay/success",
          RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("out_trade_no", this.order_id);
      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
         }

         @Override public void onCallback(String callback) {
            Trace.d("pay succ callback:" + callback);
            try {
               JSONObject json = new JSONObject(callback);
               int state = json.getInt("stat");
               if (state == 200) {
                  Utils.showMessage("支付成功");
                  int diamond = json.getInt("diamond");
                  AULiveApplication.getUserInfo().diamond = diamond;
                  if (contex instanceof BuyDiamondActivity) {
                     BuyDiamondActivity activity = (BuyDiamondActivity) contex;
                     if (activity.buyDiamondFragment != null) {
                        activity.buyDiamondFragment.txt_balance.setText(diamond + "");
                     }
                  }
               }
            } catch (JSONException e) {
               e.printStackTrace();
            }
         }
      });
      request.execute();
   }

   /**
    * call alipay sdk pay. 调用SDK支付
    */
   public void pay(String orderInfo,String signStr) {
//      // 对订单做RSA 签名
//      String sign = sign(orderInfo);
//      String sign = signStr;
//      try {
//         // 仅需对sign 做URL编码
//         sign = URLEncoder.encode(signStr, "UTF-8");
//      } catch (UnsupportedEncodingException e) {
//         e.printStackTrace();
//      }

      // 完整的符合支付宝参数规范的订单信息
      final String payInfo = orderInfo + "&sign=\"" + signStr + "\"&" + getSignType();

      Trace.d(payInfo);

      Runnable payRunnable = new Runnable() {

         @Override public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(contex);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(payInfo);

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
         }
      };

      // 必须异步调用
      Thread payThread = new Thread(payRunnable);
      payThread.start();
   }

   /**
    * check whether the device has authentication alipay account.
    * 查询终端设备是否存在支付宝认证账户
    */
   public void check(View v) {
      Runnable checkRunnable = new Runnable() {

         @Override public void run() {
            // 构造PayTask 对象
            PayTask payTask = new PayTask(contex);
            // 调用查询接口，获取查询结果
            boolean isExist = payTask.checkAccountIfExist();

            Message msg = new Message();
            msg.what = SDK_CHECK_FLAG;
            msg.obj = isExist;
            mHandler.sendMessage(msg);
         }
      };

      Thread checkThread = new Thread(checkRunnable);
      checkThread.start();
   }

   /**
    * get the sdk version. 获取SDK版本号
    */
   public void getSDKVersion() {
      PayTask payTask = new PayTask(contex);
      String version = payTask.getVersion();
      Toast.makeText(contex, version, Toast.LENGTH_SHORT).show();
   }

   /**
    * create the order info. 创建订单信息
    */
   public String getOrderInfo(String subject, String body, String price, String order_id,
       String call_back) {
      // 签约合作者身份ID
      String orderInfo = "partner=" + "\"" + PARTNER + "\"";

      // 签约卖家支付宝账号
      orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

      // 商户网站唯一订单号
      orderInfo += "&out_trade_no=" + "\"" + order_id + "\"";

      // 商品名称
      orderInfo += "&subject=" + "\"" + subject + "\"";

      // 商品详情
      orderInfo += "&body=" + "\"" + body + "\"";

      // 商品金额
      orderInfo += "&total_fee=" + "\"" + price + "\"";

      // 服务器异步通知页面路径
      orderInfo += "&notify_url=" + "\"" + call_back + "\"";

      // 服务接口名称， 固定值
      orderInfo += "&service=\"mobile.securitypay.pay\"";

      // 支付类型， 固定值
      orderInfo += "&payment_type=\"1\"";

      // 参数编码， 固定值
      orderInfo += "&_input_charset=\"utf-8\"";

      // 设置未付款交易的超时时间
      // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
      // 取值范围：1m～15d。
      // Point-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
      // 该参数数值不接受小数点，如1.5h，可转换为90m。
      orderInfo += "&it_b_pay=\"30m\"";

      // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
      // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

      // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
      orderInfo += "&return_url=\"Point.alipay.com\"";

      // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
      // orderInfo += "&paymethod=\"expressGateway\"";

      return orderInfo;
   }

   /**
    * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
    */
   public String getOutTradeNo() {
      //SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
      //Date date = new Date();
      //String key = format.format(date);
      //
      //Random r = new Random();
      //key = key + r.nextInt();
      //key = key.substring(0, 15);
      long time = System.currentTimeMillis();
      String trade_no = time + uid;
      Trace.d("trade_no:" + trade_no);
      return trade_no;
   }

//   /**
//    * sign the order info. 对订单信息进行签名
//    *
//    * @param content 待签名订单信息
//    */
//   public String sign(String content) {
//      return SignUtils.sign(content, RSA_PRIVATE);
//   }

   /**
    * get the sign type we use. 获取签名方式
    */
   public String getSignType() {
      return "sign_type=\"RSA\"";
   }
}
