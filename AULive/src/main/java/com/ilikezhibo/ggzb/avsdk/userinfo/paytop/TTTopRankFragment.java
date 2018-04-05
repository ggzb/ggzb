package com.ilikezhibo.ggzb.avsdk.userinfo.paytop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.MemberInfo;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoHelper;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.TopRankAdapter;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.TopRankEntity;
import com.ilikezhibo.ggzb.avsdk.userinfo.toprank.TopRankListEntity;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;

import java.util.ArrayList;

import popwindow.PopupWindowUtil;

/**
 * Created by hasee on 2017/9/4.
 */

public class TTTopRankFragment extends BaseFragment
        implements View.OnClickListener,PullToRefreshView.OnRefreshListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{
    private Activity mActivity;
    public static final int TYPE_DAY = 0;
    public static final int TYPE_WEEK = 1;
    public static final int TYPE_TOTAL = 2;

    private View view;
    private CustomProgressDialog progressDialog = null;
    private PullToRefreshView home_listview;
    private int currPage = 1;
    private TextView msgInfoTv;
    private ArrayList<TopRankEntity> entities;
    private TopRankAdapter groupFragmentAdapter_1;
    private TopRankAdapter groupFragmentAdapter_2;

    private String uid;
    private TextView txt_total_coin;

    private int type1,type2;//分辨是什么榜
    private String urlhead,urltype;
    private PopupWindowUtil user_popupWindow;

    // 只提示一次登录
    private boolean has_ask_login = false;
    public TTTopRankFragment(){

    }
    @SuppressLint("ValidFragment")
    public TTTopRankFragment(int type1, int type2){
        this.type1 = type1;
        this.type2 = type2;
        initurl();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_toprank_layout, null);
        view.findViewById(R.id.topLayout).setVisibility(View.GONE);

        msgInfoTv = (TextView) view.findViewById(R.id.msgInfoTv);
        msgInfoTv.setOnClickListener(this);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(Utils.trans(R.string.app_money) + "贡献榜");

        Button rl_back = (Button) view.findViewById(R.id.back);
        rl_back.setOnClickListener(this);
        rl_back.setVisibility(View.GONE);
        entities = new ArrayList<TopRankEntity>();

        home_listview = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_listview);
        home_listview.setOnRefreshListener(this);
        home_listview.setOnItemClickListener(this);
        home_listview.setOnItemLongClickListener(this);
        home_listview.setDividerHeight(0);

        if(type1 == TotalRankFragment.TYPE_ANCHOR){
            groupFragmentAdapter_1 = new TopRankAdapter(mActivity);
            home_listview.setAdapter(groupFragmentAdapter_1);
            groupFragmentAdapter_1.setEntities(entities);
        }else if(type1 == TotalRankFragment.TYPE_USER){
            groupFragmentAdapter_2 = new TopRankAdapter(mActivity);
            home_listview.setAdapter(groupFragmentAdapter_2);
            groupFragmentAdapter_2.setEntities(entities);
        }



/*        home_listview.setAdapter(groupFragmentAdapter);
        groupFragmentAdapter.setEntities(entities);*/

        View header =
                LayoutInflater.from(this.getContext()).inflate(R.layout.gift_contributor_header, null);
        txt_total_coin = (TextView) header.findViewById(R.id.txt_total_coin);
        //txt_total_coin.setText(AULiveApplication.getUserInfo().recv_diamond + "");
        //home_listview.addHeaderView(header);

        startProgressDialog();
        return view;
    }

    private void initurl(){
        if(type1 == TotalRankFragment.TYPE_USER) urlhead = "rank/user";
        else if(type1 == TotalRankFragment.TYPE_ANCHOR) urlhead = "rank/anchor";
        if(type2 == TYPE_DAY) urltype = "day";
        else if(type2 == TYPE_WEEK) urltype = "week";
        else if(type2 == TYPE_TOTAL) urltype = "all";
    }
    @Override
    public void onResume() {
        super.onResume();
        home_listview.initRefresh(PullToRefreshView.HEADER);
    }
    @Override public void onDetach() {
        super.onDetach();
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(mActivity);
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

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TopRankEntity entity = (TopRankEntity) parent.getAdapter().getItem(position);
/*        Intent homepage_Intent =
                new Intent(TTTopRankFragment.this.getActivity(), HomePageActivity.class);
        if (homepage_Intent == null || entity == null || entity.getUid() == null) {
            return;
        }
        homepage_Intent.putExtra(HomePageActivity.HOMEPAGE_UID, entity.getUid());
        TTTopRankFragment.this.getActivity().startActivity(homepage_Intent);*/


//        MemberInfo memberInfo =
//                new MemberInfo(entity.getUid(), entity.getNickname(), entity.getFace());
//
//        PopupWindowUtil popupWindow1 = new PopupWindowUtil(view);
//        popupWindow1.setContentView(R.layout.dialog_myroom_userinfo_new_new);
//        popupWindow1.setOutsideTouchable(true);
//        NewNewUserInfoHelper userInfoHelper =
//                NewNewUserInfoHelper.getInstance(view, popupWindow1, mActivity, memberInfo,false);

        MemberInfo memberInfo = new MemberInfo(entity.getUid(), entity.getNickname(), entity.getFace());
        user_popupWindow = new PopupWindowUtil(view);
        user_popupWindow.setContentView(R.layout.dialog_myroom_userinfo);
        user_popupWindow.setOutsideTouchable(true);
        user_popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {

            }
        });
        //做View init
        UserInfoHelper userInfoHelper =
                UserInfoHelper.getInstance(view, user_popupWindow, TTTopRankFragment.this.getActivity(), memberInfo);
    }

    /**
     * Callback method to be invoked when an item in this view has been
     * clicked and held.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need to access
     * the data associated with the selected item.
     *
     * @param parent   The AbsListView where the click happened
     * @param view     The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onRefresh(final int mode) {
        currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
        RequestInformation request = null;
        StringBuilder sb = new StringBuilder(
                UrlHelper.SERVER_URL + urlhead + "?page=" + currPage + "&liveuid=" + uid + "&type="+urltype);
        Trace.d(sb.toString());
        request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

        request.setCallback(new JsonCallback<TopRankListEntity>() {

            @Override public void onCallback(TopRankListEntity callback) {
                stopProgressDialog();
                if (callback == null) {
                    currPage--;
                    msgInfoTv.setText(R.string.get_info_fail);
                    msgInfoTv.setVisibility(View.VISIBLE);
                    home_listview.setVisibility(View.VISIBLE);
                    home_listview.onRefreshComplete(mode, false);
                    home_listview.enableFooter(false);

                    return;
                }

                if (callback.getStat() == 200) {
                    if (currPage == 1) {
                        txt_total_coin.setText(callback.total + "");
                    }
                    msgInfoTv.setVisibility(View.GONE);
                    if (mode == PullToRefreshView.HEADER) {
                        entities.clear();
                    }

                    if (callback.getList() != null) {
                        //测试，加4次
                        entities.addAll(callback.getList());
                        //entities.addAll(callback.getList());
                        //entities.addAll(callback.getList());
                        //entities.addAll(callback.getList());
                    }

                    if(type1 == TotalRankFragment.TYPE_ANCHOR){
                        groupFragmentAdapter_1.setEntities(entities);
                        groupFragmentAdapter_1.notifyDataSetChanged();
                    }else if(type1 == TotalRankFragment.TYPE_USER){
                        groupFragmentAdapter_2.setEntities(entities);
                        groupFragmentAdapter_2.notifyDataSetChanged();
                    }
/*                    groupFragmentAdapter.setEntities(entities);
                    groupFragmentAdapter.notifyDataSetChanged();*/
                    home_listview.onRefreshComplete(mode, true);

                    if (mode == PullToRefreshView.HEADER || (callback.getList() != null
                            && callback.getList().size() > 0)) {
                        home_listview.enableFooter(true);
                    } else {
                        home_listview.enableFooter(false);
                    }

                    //显示内容为空
                    if (mode == PullToRefreshView.HEADER && (callback.getList() == null
                            || callback.getList().size() == 0)) {
                        view.findViewById(R.id.ll_fav_nocontent).setVisibility(View.VISIBLE);
                        home_listview.setVisibility(View.GONE);
                    }
                } else {
                    if (callback.getStat() == 500 && has_ask_login == false) {
                        has_ask_login = true;
                        Intent login_intent =
                                new Intent(mActivity, LoginActivity.class);
                        startActivity(login_intent);
                    }
                    stopProgressDialog();
                    currPage--;
                    msgInfoTv.setText(callback.getMsg());
                    msgInfoTv.setVisibility(View.VISIBLE);
                    // 因为可能网络恢复，success改为true
                    home_listview.onRefreshComplete(mode, false);
                    home_listview.enableFooter(false);
                }
            }

            @Override public void onFailure(AppException e) {
                stopProgressDialog();
                currPage--;
                entities.clear();
                // 因为可能网络恢复，success改为true
                home_listview.onRefreshComplete(mode, false);
                home_listview.enableFooter(false);

                msgInfoTv.setText(R.string.get_info_fail);
                msgInfoTv.setVisibility(View.VISIBLE);
            }
        }.setReturnType(TopRankListEntity.class));

        request.execute();
    }
}
