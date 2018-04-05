package com.ilikezhibo.ggzb.avsdk.exchange;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.MyListView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;

public class ExchangeDiamondActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private int mDiamond_ratio;
    private int mFlower;
    private int mDiamond;
    private CustomDialog mDialog;
    private TextView mMy_flower;
    private TextView mMy_diamond;
    private MyListView mListview;
    private Button mBtn_diamond;
    private int oldPosition = -1;
    private int[] arr = {100,500,1000,3000,5000};
    private MyAdapter mAdapter;
    private int mExchange_diamond;

    @Override
    protected void setContentView() {
        setContentView(R.layout.exchange_diamond);
        Button rl_back = (Button) this.findViewById(R.id.back);
        rl_back.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("兑换钻石");

        init();

    }

    private void init() {
        TextView textView = (TextView) findViewById(R.id.my_flower);
        textView.setText("我的" + Utils.trans(R.string.app_money) + ":");
        mMy_flower = (TextView) findViewById(R.id.can_exchange);
        mMy_diamond = (TextView) findViewById(R.id.my_diamond);
        mListview = (MyListView) findViewById(R.id.listview);
        mBtn_diamond = (Button) findViewById(R.id.exchange_diamond);

        Intent intent = getIntent();
        mDiamond_ratio = intent.getIntExtra(ExchangeActivity.DIAMOND_RATIO, 0);
        mFlower = intent.getIntExtra(ExchangeActivity.FLOWER, 0);
        mDiamond = intent.getIntExtra(ExchangeActivity.DIAMOND, 0);
        if (mDiamond_ratio == 0) {
            Utils.showCenterMessage("数据错误");
            this.finish();
        }
        mMy_flower.setText("" + mFlower);
        mMy_diamond.setText("" + mDiamond);
        mBtn_diamond.setOnClickListener(this);
        mAdapter = new MyAdapter();
        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(this);
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
            case R.id.exchange_diamond:
                if (oldPosition == -1) {
                    Utils.showCenterMessage("请选择兑换钻石数量");
                    return;
                }
                doDialog();
//                mExchange_diamond = arr[oldPosition];
//                doExchange(mExchange_diamond);
                break;
        }
    }

    private void doDialog() {
        mDialog = new CustomDialog(ExchangeDiamondActivity.this, new CustomDialogListener() {
            @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                    case CustomDialogListener.BUTTON_POSITIVE:
                        doExchange();
                        break;
                }
            }
        });
        mDialog.setCustomMessage("您将消耗 " + arr[oldPosition] * mDiamond_ratio + Utils.trans(R.string.app_money) + " 兑换 " + arr[oldPosition] + " 钻石");
        mDialog.setCancelable(true);
        mDialog.setType(CustomDialog.DOUBLE_BTN);
        mDialog.show();
    }

    private void doExchange() {

        RequestInformation request = new RequestInformation(UrlHelper.EXCHANGE_DIAMOND + "coins=" + arr[oldPosition], RequestInformation.REQUEST_METHOD_GET);
        request.setCallback(new JsonCallback<ExchangeDiamondEntity>() {
            @Override
            public void onCallback(ExchangeDiamondEntity callback) {
                if (callback.getStat() == 200) {
                    mMy_flower.setText("" + callback.getLast_diamond());
                    mMy_diamond.setText("" + callback.getDiamond());
                }
                Utils.showCenterMessage(callback.getMsg());
            }

            @Override
            public void onFailure(AppException e) {
                Utils.showCenterMessage("获取网络数据失败");
            }
        }.setReturnType(ExchangeDiamondEntity.class));
        request.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (oldPosition == position) {
            oldPosition = -1;
        } else {
            oldPosition = position;
        }
        mAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.item_exchangealipay, null);
                viewHolder.money = (TextView) convertView.findViewById(R.id.money);
                viewHolder.flower = (TextView) convertView.findViewById(R.id.flower);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position == oldPosition) {
                convertView.setBackgroundColor(getResources().getColor(R.color.global_main_bg));
            } else {
                convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            viewHolder.money.setText(arr[position] + "钻石");
            viewHolder.flower.setText(arr[position] * mDiamond_ratio + Utils.trans(R.string.app_money));
            return convertView;
        }
        class ViewHolder{
            TextView money;
            TextView flower;
        }

    }

}
