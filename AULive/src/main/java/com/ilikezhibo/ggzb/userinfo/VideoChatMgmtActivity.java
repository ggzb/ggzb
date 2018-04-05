package com.ilikezhibo.ggzb.userinfo;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jack.utils.BtnClickUtils;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.tool.SoloMgmtUtils;
import com.ilikezhibo.ggzb.tool.SoloRequestListener;

public class VideoChatMgmtActivity extends BaseFragmentActivity implements View.OnClickListener {

    private CheckBox cb_accept_video;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_video_chat_mgmt);
    }

    @Override
    protected void initializeViews() {
        Button rl_back = (Button) this.findViewById(R.id.back);
        rl_back.setOnClickListener(this);
        rl_back.setVisibility(View.VISIBLE);

        final TextView tv_title = (TextView) this.findViewById(R.id.title);
        tv_title.setText("视频聊天管理");

        cb_accept_video = (CheckBox) findViewById(R.id.cb_accept_video);
        cb_accept_video.setOnClickListener(this);
        // >这里应该获取字段 确定checkbox状态
        setCheckboxState();
        cb_accept_video.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean checked) {
                if (checked) {
                    // >开通
                    Trace.d("**>开通");
                } else {
                    // >取消
                    Trace.d("**>>关闭");
                }
            }
        });
        RelativeLayout rl_vchat_cert = (RelativeLayout) findViewById(R.id.rl_vchat_cert);
        rl_vchat_cert.setOnClickListener(this);

        RelativeLayout rl_vchat_cond = (RelativeLayout) findViewById(R.id.rl_vchat_cond);
        rl_vchat_cond.setOnClickListener(this);

        RelativeLayout rl_appt = (RelativeLayout) findViewById(R.id.rl_appt);
        rl_appt.setOnClickListener(this);
    }

    private void setCheckboxState() {
        // >1为开通
        cb_accept_video.setChecked(SharedPreferenceTool.getInstance().getBoolean(
                SharedPreferenceTool.OPEN_ONETONE + AULiveApplication.getUserInfo().getUid(), false));
    }

    @Override
    protected void initializeData() {

    }

    @Override
    public void onClick(View v) {
        if (BtnClickUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {

            case R.id.back:
                this.finish();
                break;

            // >认证
            case R.id.rl_vchat_cert:
                break;

            // >设置条件
            case R.id.rl_vchat_cond:
                Intent intent_videochat = new Intent(this, VideoChatCondActivity.class);
                startActivity(intent_videochat);
                break;

            // >预约聊天
            case R.id.rl_appt:
                break;
        }
    }
}
