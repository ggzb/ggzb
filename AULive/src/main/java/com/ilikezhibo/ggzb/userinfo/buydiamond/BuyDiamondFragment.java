package com.ilikezhibo.ggzb.userinfo.buydiamond;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.UrlHelper;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.userinfo.mymoney.alipay.AliPayHeler;
import com.ilikezhibo.ggzb.userinfo.mymoney.wxpay.WXPayUtil;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;

import java.util.ArrayList;

/**
 * @author big
 * @ClassName: InOutFragment
 * @Description: 收支fragment
 * @date 2015-7-16 下午9:49:00
 */

@SuppressLint("ValidFragment")
public class BuyDiamondFragment extends BaseFragment
        implements OnClickListener {

    private CustomProgressDialog progressDialog = null;
    private ArrayList<BuyDiamondEntity> entities = new ArrayList<BuyDiamondEntity>();
    //还有多少钻石
    public TextView txt_balance;
    private View weixin_pay;
    private View ali_pay;
    private ImageView img_chosen_weixin;
    private ImageView img_chosen_ali;
    private GridView chose_pay_ly;
    private TextView txt_choose_chargeinfo_tip;
    private MyAdapter myGridViewAdapter;
    private LinearLayout pay_choose;
    private LinearLayout ll_gridview;

    public BuyDiamondFragment() {
    }

    private TextView payname_weixin;
    private TextView payname_ali;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_charge, null);

        pay_choose = (LinearLayout) view.findViewById(R.id.ll_pay_choose);
        ll_gridview = (LinearLayout) view.findViewById(R.id.ll_gridview);
        Button rl_back = (Button) view.findViewById(R.id.back);
        rl_back.setOnClickListener(this);

        Activity activity = BuyDiamondFragment.this.getActivity();
        if (activity instanceof AULiveHomeActivity) {
            rl_back.setVisibility(View.INVISIBLE);
        } else {
            rl_back.setVisibility(View.VISIBLE);
        }

        TextView tv_title = (TextView) view.findViewById(R.id.title);
        tv_title.setText("我的钻石");

        //钻石数
        txt_balance = (TextView) view.findViewById(R.id.txt_balance);
        entities = new ArrayList<BuyDiamondEntity>();

        chose_pay_ly = (GridView) view.findViewById(R.id.chose_item_ly);
        chose_pay_ly.setSelector(new ColorDrawable(Color.TRANSPARENT));
        myGridViewAdapter = new MyAdapter(entities);
        chose_pay_ly.setAdapter(myGridViewAdapter);

        //支付方式
//        weixin_pay = view.findViewById(R.id.weixin_type);
//        payname_weixin = (TextView) weixin_pay.findViewById(R.id.txt_paymethod_name);
//        payname_weixin.setTextColor(getResources().getColor(R.color.white));
//
//        payname_weixin.setText("微信");
//        img_chosen_weixin = (ImageView) weixin_pay.findViewById(R.id.img_chosen);
//        weixin_pay.setBackgroundColor(
//                BuyDiamondFragment.this.getResources().getColor(R.color.global_main_bg));
        //img_chosen_weixin.setVisibility(View.VISIBLE);

        ali_pay = view.findViewById(R.id.alipay_type);
        payname_ali = (TextView) ali_pay.findViewById(R.id.txt_paymethod_name);
        payname_ali.setText("支付宝");
        img_chosen_ali = (ImageView) ali_pay.findViewById(R.id.img_chosen);
        //img_chosen_ali.setVisibility(View.INVISIBLE);

        txt_choose_chargeinfo_tip = (TextView) view.findViewById(R.id.txt_choose_chargeinfo_tip);

//        weixin_pay.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                is_weixin_pay = true;
//
//                weixin_pay.setBackgroundColor(
//                        BuyDiamondFragment.this.getResources().getColor(R.color.global_main_bg));
//                ali_pay.setBackgroundColor(
//                        BuyDiamondFragment.this.getResources().getColor(R.color.white));
//
//                payname_weixin.setTextColor(getResources().getColor(R.color.white));
//                payname_ali.setTextColor(getResources().getColor(R.color.text_paymethod_name));
//                txt_choose_chargeinfo_tip.setText("支付方式:微信支付");
//            }
//        });
        ali_pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                is_weixin_pay = false;

//                weixin_pay.setBackgroundColor(BuyDiamondFragment.this.getResources().getColor(R.color.white));
                ali_pay.setBackgroundColor(
                        BuyDiamondFragment.this.getResources().getColor(R.color.global_main_bg));

//                payname_weixin.setTextColor(getResources().getColor(R.color.text_paymethod_name));
                payname_ali.setTextColor(getResources().getColor(R.color.white));
                txt_choose_chargeinfo_tip.setText("支付方式:支付宝支付");
            }
        });
        // startProgressDialog();

        return view;
    }

    //true为微信，false为支付宝
    private boolean is_weixin_pay = false;

    @Override
    public void onResume() {
        super.onResume();
        getPayData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                BuyDiamondFragment.this.getActivity().finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getPayData() {

        RequestInformation request = null;
        StringBuilder sb = new StringBuilder(UrlHelper.URL_HEAD + "/pay");

        request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

        request.setCallback(new JsonCallback<BuyDiamondListEntity>() {

            @Override
            public void onCallback(BuyDiamondListEntity callback) {
                startProgressDialog();

                if (callback.getStat() == 200) {
                    entities.clear();
                    myGridViewAdapter.notifyDataSetChanged();
//               chose_pay_ly.removeAllViews();
                    txt_balance.setText(callback.diamond + "");

                    if (callback.getList() != null) {
                        entities.addAll(callback.getList());
                        myGridViewAdapter.notifyDataSetChanged();
                        ll_gridview.setVisibility(View.VISIBLE);
                        pay_choose.setVisibility(View.VISIBLE);
                    }
                    stopProgressDialog();
                }
            }

            @Override
            public void onFailure(AppException e) {
                stopProgressDialog();
                entities.clear();
                myGridViewAdapter.notifyDataSetChanged();
            }
        }.setReturnType(BuyDiamondListEntity.class));

        request.execute();
    }

    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this.getActivity());
            progressDialog.setMessage("正在加载中...");
        }

        progressDialog.show();
    }

    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private class MyAdapter extends BaseAdapter {
        private ArrayList<BuyDiamondEntity> entities;

        public MyAdapter(ArrayList<BuyDiamondEntity> entities) {
            this.entities = entities;
        }

        @Override
        public int getCount() {
            return entities.size();
        }

        @Override
        public Object getItem(int position) {
            return entities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(BuyDiamondFragment.this.getActivity())
                        .inflate(R.layout.cell_charge_info, null);
                viewHolder = new ViewHolder();

                viewHolder.txt_charge_desc = (TextView) convertView.findViewById(R.id.txt_charge_desc);
                viewHolder.bt_pay = (TextView) convertView.findViewById(R.id.bt_pay);
                viewHolder.txt_charge_value = (TextView) convertView.findViewById(R.id.txt_charge_value);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final BuyDiamondEntity entity = entities.get(position);
            if (entity.getMemo() != null && !entity.getMemo().equals("")) {
                viewHolder.txt_charge_desc.setText(entity.getMemo());
                viewHolder.txt_charge_desc.setVisibility(View.VISIBLE);
            }
            viewHolder.bt_pay.setText(entity.getMoney() + "元");
            viewHolder.txt_charge_value.setText(entity.getDiamond() + "");
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (BtnClickUtils.isFastClean()) {
                        return;
                    }
                    String uid = null;
                    String nickname = null;
                    if (AULiveApplication.getUserInfo() != null) {
                        uid = AULiveApplication.getUserInfo().getUid();
                        nickname = AULiveApplication.getUserInfo().getNickname();
                    }
                    if (AULiveApplication.getUserInfo() == null
                            || uid == null
                            || uid.equals("")
                            || uid.equals("0")) {
                        Intent login_intent = new Intent(BuyDiamondFragment.this.getActivity(),
                                LoginActivity.class);
                        login_intent.putExtra(LoginActivity.back_home_key, false);
                        startActivity(login_intent);
                        return;
                    }

                    if (is_weixin_pay) {
                        //微信支付
                        WXPayUtil wxPayUtil =
                                new WXPayUtil(BuyDiamondFragment.this.getActivity(), uid);
                        // 微信支付单位为分所以乘以100
                        int t_money = (int) (entity.getMoney() * 100);
                        wxPayUtil.sendorder(uid, t_money + "", entity.getMoney() + "",
                                entity.getDiamond() + "");

                        //必须为整型,分不能有小数点
//                        wxPayUtil.sendorder(uid, ((int)(0.1 * 100)) + "", 0.1 + "", 1 + "");
                    } else {
                        //支付宝支付
                        AliPayHeler aliPayHeler = new AliPayHeler();
                        aliPayHeler.sendorder(BuyDiamondFragment.this.getActivity(), uid,
                                entity.getMoney() + "", entity.getDiamond() + "");
//                        aliPayHeler.sendorder(BuyDiamondFragment.this.getActivity(), uid,
//                           0.1 + "",1 + "");
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView txt_charge_desc;
            public TextView bt_pay;
            public TextView txt_charge_value;
        }
    }
}
