package com.ilikezhibo.ggzb.avsdk.exchange;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.tool.SharedPreferenceTool;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.MyListView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;

public class ExchangeBankActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private int mMoney_ratio;
    private int mFlower;
    private CustomDialog mDialog;

    private int[] arr_money = {100, 200, 500};
    private TextView mText_flower;
    private EditText mEdit_alipy;
    private EditText mEdit_name;
    private MyListView mListView;
    private int oldPosition = -1;
    private MyAdapter mAdapter;
    private Button mBtn_money;
    private String mText_account;
    private String mText_name;
    private String savedAccount;
    private String savedName;
    private int mCoins;
    private String mWay;

    @Override
    protected void setContentView() {
        setContentView(R.layout.exchange_bank);

        Button rl_back = (Button) this.findViewById(R.id.back);
        rl_back.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("银行卡兑换");

        init();
    }

    private void init() {
        Intent intent = getIntent();
        mMoney_ratio = intent.getIntExtra(ExchangeActivity.MONEY_RATIO, 0);
        mFlower = intent.getIntExtra(ExchangeActivity.FLOWER, 0);
        mWay = intent.getStringExtra(ExchangeActivity.MONEY_WAY);
        if (mMoney_ratio == 0) {
            Utils.showCenterMessage("数据错误");
            this.finish();
        }
        TextView textView = (TextView) findViewById(R.id.my_flower);
        textView.setText("我的" + Utils.trans(R.string.app_money));
        mBtn_money = (Button) findViewById(R.id.exchange_money);
        mBtn_money.setOnClickListener(this);
        mText_flower = (TextView) findViewById(R.id.can_exchange);
        mText_flower.setText("" + mFlower);
        mEdit_alipy = (EditText) findViewById(R.id.account_alipay);
        mEdit_name = (EditText) findViewById(R.id.true_name);
        mListView = (MyListView) findViewById(R.id.list_alipay);
        if (mAdapter == null) {
            mAdapter = new MyAdapter();
        }
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        savedAccount = SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.BANK_NUM,"空");
        savedName = SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.BANK_REALNAME, "空");
        if (!savedName.equals("空") && !savedAccount.equals("空")) {
            mEdit_alipy.setText(savedAccount);
            mEdit_name.setText(savedName);
        }
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
                finish();
                break;
            case R.id.exchange_money:
                mText_account = mEdit_alipy.getText().toString();
                mText_name = mEdit_name.getText().toString();
                if (TextUtils.isEmpty(mText_account) || TextUtils.isEmpty(mText_name)) {
                    Utils.showCenterMessage("银行卡号或者真实姓名不能为空");
                    return;
                }
                if (oldPosition == -1) {
                    Utils.showCenterMessage("请选择兑换金额");
                    return;
                }
                doDialog();
//                doExchange(mText_account, mText_name, oldPosition);
//                doExchange();
                break;
        }

    }

    private void doDialog() {
        mDialog = new CustomDialog(ExchangeBankActivity.this, new CustomDialogListener() {
            @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                    case CustomDialogListener.BUTTON_POSITIVE:
                        doExchange();
                        break;
                }
            }
        });
        mDialog.setCustomMessage("您将在 " + mText_account + " 的银行卡号中兑换 " + arr_money[oldPosition] + " 元");
        mDialog.setCancelable(true);
        mDialog.setType(CustomDialog.DOUBLE_BTN);
        mDialog.show();
    }


    private void doExchange() {
        mCoins = arr_money[oldPosition];
        StringBuilder sb = new StringBuilder(UrlHelper.BANK_EXCHANGE + "coins=" + mCoins + "&account=" + mText_account + "&realname=" + mText_name);
        RequestInformation request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);
        request.setCallback(new JsonCallback<ExchangeBankEntity>() {
            @Override
            public void onCallback(ExchangeBankEntity callback) {
                if (callback.getStat() == 200) {
                    Utils.showCenterMessage(callback.getMsg());
                    mText_flower.setText("" + callback.getLast_diamond());
                    SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.BANK_NUM, mText_account);
                    SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.BANK_REALNAME, mText_name);
//                    mEdit_alipy.setText(mText_account);
//                    mEdit_name.setText(mText_name);
                } else {
                    Utils.showCenterMessage(callback.getMsg());
                }
            }

            @Override
            public void onFailure(AppException e) {
                Utils.showCenterMessage("获取网络数据失败");
            }
        }.setReturnType(ExchangeBankEntity.class));
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
            return arr_money.length;
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
            viewHolder.money.setText(arr_money[position] + "元");
            viewHolder.flower.setText(arr_money[position] * mMoney_ratio + Utils.trans(R.string.app_money));
            return convertView;
        }
    }

    class ViewHolder{
        TextView money;
        TextView flower;
    }
}
