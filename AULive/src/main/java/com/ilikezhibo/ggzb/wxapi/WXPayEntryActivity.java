package com.ilikezhibo.ggzb.wxapi;

import android.content.Intent;
import android.os.Bundle;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.userinfo.mymoney.wxpay.Constants;
import com.ilikezhibo.ggzb.userinfo.mymoney.wxpay.WXPayUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.json.JSONException;
import org.json.JSONObject;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

   private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

   private IWXAPI api;

   @Override public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

      api.handleIntent(getIntent(), this);
   }

   @Override protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      setIntent(intent);
      api.handleIntent(intent, this);
   }

   @Override public void onReq(BaseReq req) {
   }

   @Override public void onResp(BaseResp resp) {
      Trace.d("onPayFinish, errCode = " + resp.errCode);
      // 先判断是不是支付
      if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
         Trace.d("WXonResp COMMAND_PAY_BY_WX WXPayUtil.order_id:"+WXPayUtil.order_id);
         if (resp.errCode == 0) {
            paySucc();
         } else if (resp.errCode == -1) {
            Utils.showCenterMessage("支付失败:" + String.valueOf(resp.errCode));
            WXPayUtil.order_id = null;
            finish();
         } else if (resp.errCode == -2) {
            Utils.showCenterMessage("取消支付:" + String.valueOf(resp.errCode));
            WXPayUtil.order_id = null;
            finish();
         } else {
            Utils.showCenterMessage("支付出错:" + String.valueOf(resp.errCode));
            WXPayUtil.order_id = null;
            finish();
         }

         return;
      }
   }

   private void paySucc() {
      RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD + "/pay/success",
          RequestInformation.REQUEST_METHOD_POST);
      request.addPostParams("out_trade_no", WXPayUtil.order_id);
      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
            Trace.d("pay onFailure callback");
            WXPayUtil.order_id = null;
            WXPayEntryActivity.this.finish();
         }

         @Override public void onCallback(String callback) {

            Trace.d("pay succ callback:" + callback);
            WXPayUtil.order_id = null;
            try {
               JSONObject json = new JSONObject(callback);
               int state = json.getInt("stat");
               int diamond = json.getInt("diamond");
               AULiveApplication.getUserInfo().diamond = diamond;
               if (state == 200) {
                  Utils.showCroutonText(WXPayEntryActivity.this, "微信支付成功");
               }
            } catch (JSONException e) {
               e.printStackTrace();
            }
            WXPayEntryActivity.this.finish();
         }
      });
      request.execute();
   }
}