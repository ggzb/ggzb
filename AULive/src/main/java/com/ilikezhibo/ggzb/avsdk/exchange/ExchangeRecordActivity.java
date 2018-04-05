package com.ilikezhibo.ggzb.avsdk.exchange;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.tool.Utils;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.UrlHelper;

import java.util.List;

public class ExchangeRecordActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView mCurrent_page;
    private ListView mListView;
    private MyAdapter mAdapter;
    private int currentPosition = 1;
    private List<ExchangeRecordEntity.DataBean> current_list;
    private CustomProgressDialog progressDialog;

    @Override
    protected void setContentView() {
        setContentView(R.layout.exchange_record);
    }

    @Override
    protected void initializeViews() {
        Button rl_back = (Button) this.findViewById(R.id.back);
        rl_back.setVisibility(View.VISIBLE);
        rl_back.setOnClickListener(this);
        TextView title = (TextView) this.findViewById(R.id.title);
        title.setText("兑换记录");

        findViewById(R.id.last_page).setOnClickListener(this);
        findViewById(R.id.next_page).setOnClickListener(this);
        mCurrent_page = (TextView) findViewById(R.id.current_page);
        mListView = (ListView) findViewById(R.id.list_record);
    }

    @Override
    protected void initializeData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startProgressDialog();
        getRecordData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.last_page:
                currentPosition--;
                if (currentPosition == 0) {
                    currentPosition = 1;
                    Utils.showCenterMessage("没有更多数据了");
                    return;
                }
                getRecordData();
                break;
            case R.id.next_page:
                currentPosition++;
                getRecordData();
                break;
        }
    }

    public void getRecordData() {
        RequestInformation request = new RequestInformation(UrlHelper.EXCHANGE_RECORD + currentPosition, RequestInformation.REQUEST_METHOD_GET);
        request.setCallback(new JsonCallback<ExchangeRecordEntity>() {
            @Override
            public void onCallback(ExchangeRecordEntity callback) {
                stopProgressDialog();
              if (callback.getStat() == 200) {
                  findViewById(R.id.relativelayout).setVisibility(View.VISIBLE);
                    current_list = callback.getData();
                    if (current_list.size() == 0) {
                        Utils.showCenterMessage("没有更多数据了");
                        currentPosition--;
                        return;
                    }
                        if (mAdapter == null) {
                            mAdapter = new MyAdapter();
                            mListView.setAdapter(mAdapter);
                        }
                        mCurrent_page.setText("第" + currentPosition + "页");
                        mAdapter.notifyDataSetChanged();
                }else if (callback.getStat() == 201) {
                  if (currentPosition == 1) {
                      findViewById(R.id.ll_fav_nocontent).setVisibility(View.VISIBLE);
                      return;
                  }
                    currentPosition--;
                    Utils.showCenterMessage(callback.getMsg());
                } else {
                    Utils.showCenterMessage(callback.getMsg());
                }
            }

            @Override
            public void onFailure(AppException e) {

            }
        }.setReturnType(ExchangeRecordEntity.class));
        request.execute();

    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return current_list.size();
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
            ExchangeRecordEntity.DataBean data;
            int type;
            int exchange_state;
            long exchange_time;
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.item_exchange_record, null);
                viewHolder.record = (TextView) convertView.findViewById(R.id.record);
                viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                viewHolder.state = (TextView) convertView.findViewById(R.id.state);
                viewHolder.way = (TextView) convertView.findViewById(R.id.way);
                viewHolder.image_state = (ImageView) convertView.findViewById(R.id.exchange_state);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            data = current_list.get(position);
            type = Integer.parseInt(data.getType());
            exchange_state = Integer.parseInt(data.getStatus());
            exchange_time = Integer.parseInt(data.getAdd_time());
            switch (type) {
                //1钻石,2支付宝,3微信
                case 1:
                    viewHolder.record.setText(data.getUcoins() + com.jack.utils.Utils.trans(R.string.app_money)+ " 兑换 " + data.getMoney() + "钻石");
                    viewHolder.way.setText("兑换钻石");
                    break;
                case 2:
                    viewHolder.record.setText(data.getUcoins() + com.jack.utils.Utils.trans(R.string.app_money) + " 兑换 " + data.getMoney() + "元");
                    viewHolder.way.setText("支付宝:" + data.getAli_account());
                    break;
                case 3:
                    viewHolder.record.setText(data.getUcoins() + com.jack.utils.Utils.trans(R.string.app_money) + " 兑换 " + data.getMoney() + "元");
                    viewHolder.way.setText("微信");
                    break;
                case 4:
                    viewHolder.record.setText(data.getUcoins() + com.jack.utils.Utils.trans(R.string.app_money) + " 兑换 " + data.getMoney() + "元");
                    viewHolder.way.setText("银行卡");
                    break;
            }
//            0进行中  2完成
            switch (exchange_state) {
                case 0:
                    viewHolder.state.setText("进行中");
                    break;
                case 2:
                    viewHolder.state.setText("已完成");
//                    viewHolder.image_state.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    viewHolder.state.setText("兑换失败");
                    break;
            }
            viewHolder.time.setText(Utils.timeExchange(exchange_time));
            return convertView;
        }
        class ViewHolder {
            TextView record;
            TextView time;
            TextView way;
            TextView state;
            ImageView image_state;
        }
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
