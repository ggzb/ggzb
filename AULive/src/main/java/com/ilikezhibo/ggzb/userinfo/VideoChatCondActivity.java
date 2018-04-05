package com.ilikezhibo.ggzb.userinfo;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jack.utils.Trace;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.tool.InputFilterMinMax;
import com.ilikezhibo.ggzb.tool.SoloMgmtUtils;
import com.ilikezhibo.ggzb.tool.Utils;

public class VideoChatCondActivity extends BaseFragmentActivity implements View.OnClickListener {

    private EditText ed_cond_setprice;
    private String DRAWABLE = "drawable://";

    @Override
    protected void setContentView() {

        setContentView(R.layout.activity_video_chat_cond);
    }

    @Override
    protected void initializeViews() {
        Button rl_back = (Button) this.findViewById(R.id.back);
        rl_back.setOnClickListener(this);
        rl_back.setVisibility(View.VISIBLE);

        TextView tv_title = (TextView) this.findViewById(R.id.title);
        tv_title.setText("1 对 1视频聊天条件");


        ImageView iv_cond_panda = (ImageView) findViewById(R.id.iv_cond_panda);
        ImageLoader.getInstance().displayImage(DRAWABLE + R.drawable.cond_panda_bg, iv_cond_panda);
        ImageView iv_panda_belly = (ImageView) findViewById(R.id.iv_panda_belly);
        ImageLoader.getInstance().displayImage(DRAWABLE + R.drawable.cond_panda_belly, iv_panda_belly);


        TextView tv_cond_content = (TextView) findViewById(R.id.tv_cond_content);
        tv_cond_content.setText("\u3000\u3000" + "为了防止过多视频聊天邀请对您造成骚扰,请您为视频聊天设置一些条件,同时也能为您带来" + com.jack.utils.Utils.trans(R.string.app_money) + "收益.");
        TextView tv_cond_suggest = (TextView) findViewById(R.id.tv_cond_suggest);
        tv_cond_suggest.setText("建议条件：20～80钻／分钟");
        ed_cond_setprice = (EditText) findViewById(R.id.ed_cond_setprice);
        ed_cond_setprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                String hint;
                if (hasFocus) {
                    hint = ed_cond_setprice.getHint().toString();
                    ed_cond_setprice.setTag(hint);
                    ed_cond_setprice.setHint("");
                } else {
                    hint = ed_cond_setprice.getTag().toString();
                    ed_cond_setprice.setHint(hint);
                }
            }
        });
        ed_cond_setprice.setFilters(new InputFilter[]{new InputFilterMinMax("1", "9999")});
        Button bt_cond_confirm = (Button) findViewById(R.id.bt_cond_confirm);
        bt_cond_confirm.setOnClickListener(this);

    }

    @Override
    protected void initializeData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back:
                this.finish();
                break;

            case R.id.bt_cond_confirm:
                Trace.d("**>>>确认设置价格");
                if (AULiveApplication.getUserInfo().getOnetone() != 1) {
                    Utils.showMessage("您还没有开通1对1,请开通后再来设置条件~");
                    return;
                }
                String price = ed_cond_setprice.getText().toString();
                if (TextUtils.isEmpty(price)) {
                    Utils.showMessage("您没有输入价格哦~");
                } else if (Integer.valueOf(price) < 20) {
                    Utils.showMessage("最低价格不能小于20钻哦~");
                } else {
                }
                break;
        }
    }

}
