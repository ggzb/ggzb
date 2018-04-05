package com.ilikezhibo.ggzb.userinfo.mymoney.wxpay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.AppConstants;
import com.jack.utils.MobileConfig;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.R;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

/**
 * @author big
 *
 *         微信支付
 */
public class WXPayUtil {

   private static final String TAG = "big";

   private IWXAPI api;
   private Activity activity;

   // 订单号
   public static String order_id = null;
   private String money_count = null;
   // 充值用户uid
   private String uid = null;

   PayReq req;
   Map<String, String> resultunifiedorder;

   public WXPayUtil(Activity activity, String uid1) {
      this.activity = activity;

      api = WXAPIFactory.createWXAPI(activity, AppConstants.WEI_XIN_ID);
      api.registerApp(Constants.APP_ID);
      req = new PayReq();
      uid = uid1;
      // 先生成订单号，使用使用这个号用在自己后台数据的跟踪上
      order_id = genOutTradNo();
   }

   // money 为分的格式 real_money为元的格式
   public void sendorder(String uid1, String money, String real_money, String diamond) {
      uid = uid1;
      money_count = money;

      boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
      if (isPaySupported) {
         getorder_id(uid1, money, real_money, diamond);
      } else {
         Utils.showMessage("微信版本过低不支持支付");
      }
   }

   // 开始支付前给自己的服务存下订单号
   private void getorder_id(String uid1, String money, String real_money, String diamond) {
      MobileConfig config = MobileConfig.getMobileConfig(activity);

      RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD + "/pay/wxorder",
              RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("out_trade_no", this.order_id);
      request.addPostParams("money", real_money + "");
      request.addPostParams("diamond", diamond + "");
      request.addPostParams("pf", "android");
      request.addPostParams("channel", config.getAppMetaData(activity, "UMENG_CHANNEL"));
      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }

            if (callback.getStat() == 200) {
               Log.e("aaa", "自己服务器下单成功");
               GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
               getPrepayId.execute();
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

   private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

      private ProgressDialog dialog;

      @Override protected void onPreExecute() {
         dialog = ProgressDialog.show(activity, activity.getString(R.string.app_tip),
                 activity.getString(R.string.getting_prepayid));
      }

      @Override protected void onPostExecute(Map<String, String> result) {
         if (dialog != null) {
            dialog.dismiss();
         }

         // resultunifiedorder用于result.get("prepay_id")
         resultunifiedorder = result;
         // 开始第二步
         genPayReq();
      }

      @Override protected void onCancelled() {
         super.onCancelled();
      }

      @Override protected Map<String, String> doInBackground(Void... params) {

         String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
         String entity = genProductArgs();

         Log.e("orion", "A:" + entity);

         byte[] buf = Util.httpPost(url, entity);

         String content = new String(buf);
         Log.e("orion", "B:" + content);
         Map<String, String> xml = decodeXml(content);

         return xml;
      }
   }

   public Map<String, String> decodeXml(String content) {

      try {
         Map<String, String> xml = new HashMap<String, String>();
         XmlPullParser parser = Xml.newPullParser();
         parser.setInput(new StringReader(content));
         int event = parser.getEventType();
         while (event != XmlPullParser.END_DOCUMENT) {

            String nodeName = parser.getName();
            switch (event) {
               case XmlPullParser.START_DOCUMENT:

                  break;
               case XmlPullParser.START_TAG:

                  if ("xml".equals(nodeName) == false) {
                     // 实例化student对象
                     xml.put(nodeName, parser.nextText());
                  }
                  break;
               case XmlPullParser.END_TAG:
                  break;
            }
            event = parser.next();
         }

         return xml;
      } catch (Exception e) {
         Log.e("orion", e.toString());
      }
      return null;
   }

   private String genProductArgs() {
      StringBuffer xml = new StringBuffer();

      try {
         String nonceStr = genNonceStr();

         xml.append("</xml>");
         List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
         packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
         packageParams.add(new BasicNameValuePair("body", Utils.trans(R.string.app_name) + "充值"));
         packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
         packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
         packageParams.add(
                 new BasicNameValuePair("notify_url", UrlHelper.URL_HEAD + "/pay/wxnotify"));
         packageParams.add(new BasicNameValuePair("out_trade_no", order_id));
         packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
         packageParams.add(new BasicNameValuePair("total_fee", money_count));
         packageParams.add(new BasicNameValuePair("trade_type", "APP"));

         String sign = genPackageSign(packageParams);
         packageParams.add(new BasicNameValuePair("sign", sign));

         String xmlstring = toXml(packageParams);

         return new String(xmlstring.toString().getBytes(), "ISO8859-1");
      } catch (Exception e) {
         Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
         return null;
      }
   }

   private String genNonceStr() {
      Random random = new Random();
      return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
   }

   private String genOutTradNo() {
      //Random random = new Random();
      //return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
      //时间＋uid
      long time = System.currentTimeMillis();
      String trade_no=time + uid;
      Trace.d("trade_no:"+trade_no);
      return trade_no;
   }

   /**
    * 生成签名
    */

   private String genPackageSign(List<NameValuePair> params) {
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < params.size(); i++) {
         sb.append(params.get(i).getName());
         sb.append('=');
         sb.append(params.get(i).getValue());
         sb.append('&');
      }
      sb.append("key=");
      sb.append(Constants.API_KEY);

      String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
      Log.e("orion", packageSign);
      return packageSign;
   }

   private String toXml(List<NameValuePair> params) {
      StringBuilder sb = new StringBuilder();
      sb.append("<xml>");
      for (int i = 0; i < params.size(); i++) {
         sb.append("<" + params.get(i).getName() + ">");

         sb.append(params.get(i).getValue());
         sb.append("</" + params.get(i).getName() + ">");
      }
      sb.append("</xml>");

      Log.e("orion", sb.toString());
      return sb.toString();
   }

   // 第二步
   private void genPayReq() {

      req.appId = Constants.APP_ID;
      req.partnerId = Constants.MCH_ID;
      req.prepayId = resultunifiedorder.get("prepay_id");
      req.packageValue = "Sign=WXPay";
      req.nonceStr = genNonceStr();
      req.timeStamp = String.valueOf(genTimeStamp());

      List<NameValuePair> signParams = new LinkedList<NameValuePair>();
      signParams.add(new BasicNameValuePair("appid", req.appId));
      signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
      signParams.add(new BasicNameValuePair("package", req.packageValue));
      signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
      signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
      signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

      req.sign = genAppSign(signParams);

      sendPayReq();
   }

   private long genTimeStamp() {
      return System.currentTimeMillis() / 1000;
   }

   private String genAppSign(List<NameValuePair> params) {
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < params.size(); i++) {
         sb.append(params.get(i).getName());
         sb.append('=');
         sb.append(params.get(i).getValue());
         sb.append('&');
      }
      sb.append("key=");
      sb.append(Constants.API_KEY);
      String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
      Log.e("orion", sb.toString());
      Log.e("orion", "appSign:" + appSign);
      return appSign;
   }

   // 第三步
   private void sendPayReq() {

      api.registerApp(Constants.APP_ID);
      api.sendReq(req);
   }
}
