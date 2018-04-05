package com.ilikezhibo.ggzb.avsdk.exchange;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.userinfo.bind.BindWeixinActivity;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.views.UserInfoWebViewActivity;
import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;

public class ExchangeActivity extends BaseFragmentActivity implements View.OnClickListener {
    private TextView mText_exchange_flower;
    private TextView mText_exchange_diamond;
    private RelativeLayout mLinearLayout;
    private int money_ratio;
    private int diamomd_ratio;
    private int flower;
    private int diamond;
    private String uid;
    private CustomProgressDialog progressDialog = null;
    public static final String MONEY_RATIO = "money_ratio";
    public static final String DIAMOND_RATIO = "diamond_ratio";
    public static final String FLOWER = "flower";
    public static final String DIAMOND = "diamond";
    public static final String MONEY_WAY = "money_way";

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_exchange);
        init();
    }

    private void init() {
        Button rl_back = (Button) this.findViewById(R.id.back);
        rl_back.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setOnClickListener(this);
        title.setText("收益");

//        TextView title_right = (TextView) this.findViewById(R.id.topRightBtn);
//        title_right.setVisibility(View.VISIBLE);
//        title_right.setOnClickListener(this);
//        title_right.setText("兑换记录");

        TextView textView = (TextView) findViewById(R.id.my_flower);
        textView.setText("我的" + Utils.trans(R.string.app_money) + ":");
        findViewById(R.id.common_problem).setOnClickListener(this);
        mText_exchange_flower = (TextView) findViewById(R.id.text_exchange);
        mText_exchange_diamond = (TextView) findViewById(R.id.text_exchange_diamond);
        mLinearLayout = (RelativeLayout) findViewById(R.id.family);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startProgressDialog();
        getData();
    }

    @Override
    protected void initializeViews() {

    }

    @Override
    protected void initializeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.topRightBtn:
                startActivity(new Intent(this,ExchangeRecordActivity.class));
                break;
//            case R.id.exchange_wx:
//                Intent intent_wx = new Intent(this, UserInfoWebViewActivity.class);
//                intent_wx.putExtra(WebViewActivity.input_url, UrlHelper.SERVER_URL + "weixin/bind" + "?uid=" + uid);
//                intent_wx.putExtra(WebViewActivity.back_home_key, false);
//                intent_wx.putExtra(WebViewActivity.actity_name, "微信提现");
//                startActivity(intent_wx);
//                break;
            case R.id.exchange_zfb:
                Intent intent = new Intent();
                intent.putExtra(MONEY_RATIO, money_ratio);
                intent.putExtra(FLOWER, flower);
                intent.setClass(this, ExchangeAlipayActivity.class);
                startActivity(intent);
                break;
            case R.id.exchange_lianlian:
                Intent intent_lianlian = new Intent();
                intent_lianlian.putExtra(MONEY_RATIO, money_ratio);
                intent_lianlian.putExtra(FLOWER, flower);
                intent_lianlian.setClass(this, ExchangeBankActivity.class);
                startActivity(intent_lianlian);
                break;
            case R.id.exchange_diamond:
                Intent intent_diamond = new Intent();
                intent_diamond.putExtra(DIAMOND_RATIO, diamomd_ratio);
                intent_diamond.putExtra(DIAMOND, diamond);
                intent_diamond.putExtra(FLOWER, flower);
                intent_diamond.setClass(this, ExchangeDiamondActivity.class);
                startActivity(intent_diamond);
                break;
            case R.id.family:
                Intent intent_family = new Intent(this, UserInfoWebViewActivity.class);
                intent_family.putExtra(WebViewActivity.input_url, UrlHelper.SERVER_URL + "family");
                intent_family.putExtra(WebViewActivity.back_home_key, false);
                intent_family.putExtra(WebViewActivity.actity_name, "家族管理");
                startActivity(intent_family);
                break;
            case R.id.common_problem:
                Intent intent_problem = new Intent(this, UserInfoWebViewActivity.class);
                intent_problem.putExtra(WebViewActivity.input_url, UrlHelper.SERVER_URL + "faq/index");
                intent_problem.putExtra(WebViewActivity.back_home_key, false);
                intent_problem.putExtra(WebViewActivity.actity_name, "常见问题");
                startActivity(intent_problem);
                break;
        }
    }

    private void getData() {
        RequestInformation request = new RequestInformation(UrlHelper.EXCHANGE_INFO, RequestInformation.REQUEST_METHOD_GET);
        request.setCallback(new JsonCallback<ExchangeEntity>() {
            @Override
            public void onCallback(ExchangeEntity callback) {
                if (callback.getStat() == 200) {
                    mText_exchange_flower.setText("" + callback.getRecv_diamond());
                    mText_exchange_diamond.setText("" + callback.getDiamond());
                    uid = callback.getUid() + "";
                    money_ratio = callback.getExchange_money_rate();
                    diamomd_ratio = callback.getExchange_diamond_rate();
                    flower = callback.getRecv_diamond();
                    diamond = Integer.parseInt(callback.getDiamond());
                    if ("5".equals(callback.getAnchor_type())) {
                        mLinearLayout.setVisibility(View.VISIBLE);
                        mLinearLayout.setOnClickListener(ExchangeActivity.this);
                    }
                    if (1 == callback.getAlipay_status()) {
                        findViewById(R.id.exchange_zfb).setVisibility(View.VISIBLE);
                        findViewById(R.id.exchange_zfb).setOnClickListener(ExchangeActivity.this);
                    }
                    if (1 == callback.getDiamond_status()) {
                        findViewById(R.id.exchange_diamond).setVisibility(View.VISIBLE);
                        findViewById(R.id.exchange_diamond).setOnClickListener(ExchangeActivity.this);
                    }
                    if (1 == callback.getLianlianpay_status()) {
                        findViewById(R.id.exchange_lianlian).setVisibility(View.VISIBLE);
                        findViewById(R.id.exchange_lianlian).setOnClickListener(ExchangeActivity.this);
                    }
//                    findViewById(R.id.exchange_wx).setOnClickListener(ExchangeActivity.this);
                    findViewById(R.id.linearlayout).setVisibility(View.VISIBLE);
                    stopProgressDialog();
                } else if (callback.getStat() == 502) {
                    Utils.showCenterMessage(callback.getMsg());
                    startActivity(new Intent(ExchangeActivity.this, BindWeixinActivity.class));
                }else {
                    Utils.showCenterMessage(callback.getMsg());
                }
            }

            @Override
            public void onFailure(AppException e) {
                Utils.showMessage("获取网络数据失败");
            }
        }.setReturnType(ExchangeEntity.class));
        request.execute();
    }
    public void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("加载中");
        }
        progressDialog.show();
    }
    public void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
