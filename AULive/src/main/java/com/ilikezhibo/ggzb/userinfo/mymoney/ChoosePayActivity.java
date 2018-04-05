package com.ilikezhibo.ggzb.userinfo.mymoney;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.userinfo.mymoney.alipay.AliPayHeler;
import com.ilikezhibo.ggzb.userinfo.mymoney.wxpay.WXPayUtil;

/**
 * @author big
 *
 *         选择支付方式
 */
public class ChoosePayActivity extends BaseFragmentActivity implements OnClickListener {
   public static final String MONEY_COUNT = "MONEY_COUNT";

   private double money_cont;// 购买的钱

   private TextView shopContentTv;

   @Override protected void setContentView() {
      money_cont = getIntent().getDoubleExtra(MONEY_COUNT, 0);

      setContentView(R.layout.pay_choose_layout);

      Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      TextView tv_title = (TextView) this.findViewById(R.id.title);
      tv_title.setText("充值方式");
   }

   @Override protected void initializeViews() {

   }

   @Override protected void initializeData() {

      shopContentTv = (TextView) findViewById(R.id.buyContentTv);

      shopContentTv.setText(Html.fromHtml(Utils.trans(R.string.pay_content_str, money_cont)));
      findViewById(R.id.payAliPayLayout).setOnClickListener(this);
      findViewById(R.id.payWeiXinLayout).setOnClickListener(this);
   }

   @Override public void onClick(View v) {
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }

      String uid = AULiveApplication.getUserInfo().getUid();
      String nickname = AULiveApplication.getUserInfo().getNickname();
      switch (v.getId()) {
         case R.id.back:

            this.finish();

            break;
         case R.id.payAliPayLayout:

            if (AULiveApplication.getUserInfo() == null
                    || uid == null
                    || uid.equals("")
                    || uid.equals("0")) {
               Intent login_intent = new Intent(this, LoginActivity.class);
               login_intent.putExtra(LoginActivity.back_home_key, true);
               startActivity(login_intent);
               return;
            }

//			// 构造PayTask 对象
//			PayTask payTask = new PayTask(ChoosePayActivity.this);
//			// 调用查询接口，获取查询结果
//			boolean isExist = payTask.checkAccountIfExist();
//
//			if (!isExist) {
//				Utils.showMessage("你没安装支付宝");
//				return;
//			}

            if (money_cont < 1) {
               Utils.showMessage("支付宝充值不能小于1元");
               return;
            }
            AliPayHeler aliPayHeler = new AliPayHeler();
            aliPayHeler.sendorder(this, uid, money_cont + "", "");
            break;
         case R.id.payWeiXinLayout:

            if (AULiveApplication.getUserInfo() == null
                    || uid == null
                    || uid.equals("")
                    || uid.equals("0")) {
               Intent login_intent = new Intent(this, LoginActivity.class);
               login_intent.putExtra(LoginActivity.back_home_key, true);
               startActivity(login_intent);
               return;
            }

            if (money_cont < 1) {
               Utils.showMessage("微信充值不能小于1元");
               return;
            }

            WXPayUtil wxPayUtil = new WXPayUtil(this,uid);
            // 微信支付单位为分所以乘以100
            int t_money = (int) (money_cont * 100);
            wxPayUtil.sendorder(uid, t_money + "", money_cont + "", "");

            break;
         default:
            break;
      }
   }
}
