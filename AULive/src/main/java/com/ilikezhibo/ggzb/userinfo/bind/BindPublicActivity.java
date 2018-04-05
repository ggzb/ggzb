package com.ilikezhibo.ggzb.userinfo.bind;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.exchange.ExchangeActivity;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BindPublicActivity extends BaseFragmentActivity implements View.OnClickListener {

    private String code;
    private ImageView mImage_erweima;
    private EditText mEditText_identfy;

    private CustomProgressDialog progressDialog;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_bind_public);
        init();
    }

    private void init() {
        Button rl_back = (Button) this.findViewById(R.id.back);
        rl_back.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setOnClickListener(this);
        title.setText("绑定微信");

        mImage_erweima = (ImageView) findViewById(R.id.erweima);
        mEditText_identfy = (EditText) findViewById(R.id.identify);
        Button button_submit =  (Button) findViewById(R.id.submit);
        button_submit.setOnClickListener(this);

    }

    @Override
    protected void initializeViews() {
        ImageLoader.getInstance().displayImage(UrlHelper.ERWEIMA_URL,mImage_erweima, AULiveApplication.getGlobalImgOptions());

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
            case R.id.submit:
                code = mEditText_identfy.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Utils.showCenterMessage("请输入验证码");
                    return;
                }
                doBind(code);
                break;

        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    startActivity(new Intent(BindPublicActivity.this, ExchangeActivity.class));
                    BindPublicActivity.this.finish();
                    break;
            }
        }
    };
    private void doBind(String code) {
        RequestInformation request = new RequestInformation(UrlHelper.BIND_WX_PUBLIC, RequestInformation.REQUEST_METHOD_POST);
        request.addPostParams("code", code);
        request.setCallback(new JsonCallback<BindWeChatEntity>() {
            @Override
            public void onCallback(BindWeChatEntity callback) {
                startProgressDialog();

                if (callback.getStat() == 200) {
//                    0绑定   1 未绑定
                    SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.BIND_WX_PUBLIC, "0");
                    mHandler.sendEmptyMessageDelayed(1, 400);
                }
                stopProgressDialog();
                Utils.showCenterMessage(callback.getMsg());
            }

            @Override
            public void onFailure(AppException e) {
                Utils.showMessage("获取网络数据失败");
            }
        }.setReturnType(BindWeChatEntity.class));
        request.execute();
    }

    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
            progressDialog.setMessage("正在绑定...");
        }

        progressDialog.show();
    }

    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
