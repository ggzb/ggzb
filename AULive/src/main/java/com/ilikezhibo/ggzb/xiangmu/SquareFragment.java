package com.ilikezhibo.ggzb.xiangmu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseEntity;
import com.ilikezhibo.ggzb.BaseFragment;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.NavigationManager;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.pull.widget.PullToRefreshView;
import com.ilikezhibo.ggzb.step.ProjectTypeEntity;
import com.ilikezhibo.ggzb.step.ProjectTypeListEntity;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.views.OtherGridView;
import com.ilikezhibo.ggzb.xiangmu.adapter.SearchAdapter;
import com.ilikezhibo.ggzb.xiangmu.adapter.XiangMuFragmentAdapter;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuListEntity;
import java.util.ArrayList;
import tinker.android.util.TinkerManager;

/**
 * @author big
 * @ClassName: QiuZuFragment
 * @Description:消息的fragment
 * @date 2014-3-19 下午9:49:31
 */

@SuppressLint("ValidFragment") public class SquareFragment extends BaseFragment
    implements OnClickListener, PullToRefreshView.OnRefreshListener, OnItemClickListener {

   private View root_view;
   private CustomProgressDialog progressDialog = null;
   private PullToRefreshView home_listview;
   private int currPage = 1;
   private TextView msgInfoTv;
   private ArrayList<XiangMuEntity> entities;
   private XiangMuFragmentAdapter groupFragmentAdapter;

   public SquareFragment() {
   }

   @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {

      root_view = inflater.inflate(R.layout.jiesihuo_square_layout, null);

      msgInfoTv = (TextView) root_view.findViewById(R.id.msgInfoTv);
      msgInfoTv.setOnClickListener(this);

      TextView title = (TextView) root_view.findViewById(R.id.title);
      title.setText("项目");

      search_bt = (Button) root_view.findViewById(R.id.search_bt);
      search_bt.setOnClickListener(this);

      serachLayout = (LinearLayout) root_view.findViewById(R.id.serachLayout);
      serachLayout.setOnClickListener(this);
      serachLayout.setVisibility(View.GONE);

      serachLayout_main = (LinearLayout) root_view.findViewById(R.id.serachLayout_main);
      serachLayout_main.setOnClickListener(this);
      serachLayout_main.setVisibility(View.GONE);

      Button righ_bt = (Button) root_view.findViewById(R.id.righ_bt);
      righ_bt.setOnClickListener(this);

      entities = new ArrayList<XiangMuEntity>();

      // 测试
      // for (int i = 0; i < 10; i++) {
      // XiangMuEntity entity = new XiangMuEntity();
      // entities.add(entity);
      // }
      home_listview = (PullToRefreshView) root_view.findViewById(R.id.pull_to_refresh_listview);
      home_listview.setOnRefreshListener(this);
      home_listview.setOnItemClickListener(this);

      groupFragmentAdapter = new XiangMuFragmentAdapter(this.getActivity());
      home_listview.setAdapter(groupFragmentAdapter);
      groupFragmentAdapter.setEntities(entities);

      // 加载搜索列表的数据
      getSearchParama();
      return root_view;
   }

   @Override public void onResume() {
      super.onResume();

      Trace.d("SquareFragment OnResume");
      // 只有是在当前所选项时才刷新
      if (NavigationManager.getCurrType() == NavigationManager.TYPE_BANGMAMATAO_ZHOUFANGZI) {
         // 开始获取数据
         home_listview.initRefresh(PullToRefreshView.HEADER);
      }
   }

   @Override public void onClick(View v) {

      switch (v.getId()) {
         case R.id.search_bt:
            // 搜索开
            if (search_open == false) {
               onOpenSearch();
            } else {
               onCloseSearch();
            }
            break;
         case R.id.serachLayout:
            // // 关闭
            // if (search_open == true) {
            // onCloseSearch();
            // }
            break;
         case R.id.serachLayout_main:
            // 关闭
            if (search_open == true) {
               onCloseSearch();
            }
            break;

         case R.id.righ_bt:

            // Intent mypro_intent = new
            // Intent(SquareFragment.this.getActivity(),
            // MyProjectActivity.class);
            // startActivity(mypro_intent);
            break;
         case R.id.msgInfoTv:
            Intent login_intent =
                new Intent(SquareFragment.this.getActivity(), LoginActivity.class);
            startActivity(login_intent);
            break;
      }
   }

   @Override public void onDetach() {
      super.onDetach();
   }

   @Override public void onDestroy() {
      super.onDestroy();
      Trace.d("SquareFragment onDestroy");
   }

   @Override public void onPause() {
      super.onPause();
      Trace.d("SquareFragment onPause");
   }

   @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

      Intent authorIntent =
          new Intent(SquareFragment.this.getActivity(), FaXiangMuDetailActivity.class);
      XiangMuEntity entity = (XiangMuEntity) parent.getAdapter().getItem(position);
      authorIntent.putExtra(FaXiangMuDetailActivity.FAXIANGMUENTITY_KEY, entity.getId());
      startActivity(authorIntent);
   }

   @Override public void onRefresh(final int mode) {
      startProgressDialog();
      currPage = mode == PullToRefreshView.HEADER ? 1 : ++currPage;
      RequestInformation request = null;
      StringBuilder sb = new StringBuilder(UrlHelper.URL_HEAD + "/project/lists?page=" + currPage);

      ProjectTypeEntity type = getSearchCurrentType();
      if (type != null) {
         sb.append("&category=" + type.getId());
      }

      ProjectTypeEntity distance = getSearchCurrentDistance();
      AULiveApplication application = (AULiveApplication) TinkerManager.getTinkerApplicationLike();

      if (distance != null && application.getLongitude() != 0 && application.getLatitude() != 0) {

         sb.append("&geolen=" + distance.getId());
         sb.append("&lng=" + application.getLongitude());
         sb.append("&lat=" + application.getLatitude());
      }

      Trace.d(sb.toString());
      request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<XiangMuListEntity>() {

         @Override public void onCallback(XiangMuListEntity callback) {
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
               msgInfoTv.setVisibility(View.GONE);
               if (mode == PullToRefreshView.HEADER) {
                  entities.clear();
               }

               if (callback.getList() != null) {
                  entities.addAll(callback.getList());
               }

               groupFragmentAdapter.setEntities(entities);
               groupFragmentAdapter.notifyDataSetChanged();
               home_listview.onRefreshComplete(mode, true);

               if (mode == PullToRefreshView.HEADER || (callback.getList() != null
                   && callback.getList().size() > 0)) {
                  home_listview.enableFooter(true);
               } else {
                  home_listview.enableFooter(false);
               }
            } else {
               if (callback.getStat() == 500 && has_ask_login == false) {
                  has_ask_login = true;
                  Intent login_intent =
                      new Intent(SquareFragment.this.getActivity(), LoginActivity.class);
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
      }.setReturnType(XiangMuListEntity.class));

      request.execute();
   }

   // 只提示一次登录
   private boolean has_ask_login = false;
   private LinearLayout serachLayout_main;

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

   // /删除

   private void doDelete(final XiangMuEntity qiuZuEntity) {

      RequestInformation request = new RequestInformation(
          UrlHelper.URL_HEAD + "/index/estate_del?charges_id=" + qiuZuEntity.getId(),
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<BaseEntity>() {

         @Override public void onCallback(BaseEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {

               entities.remove(qiuZuEntity);
               groupFragmentAdapter.notifyDataSetChanged();
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(BaseEntity.class));
      request.execute();
   }

   private void showPromptDialog(final XiangMuEntity qiuZuEntity) {
      final CustomDialog customDialog =
          new CustomDialog(SquareFragment.this.getActivity(), new CustomDialogListener() {

             @Override public void onDialogClosed(int closeType) {
                switch (closeType) {
                   case CustomDialogListener.BUTTON_POSITIVE:
                      doDelete(qiuZuEntity);
                }
             }
          });

      customDialog.setCustomMessage("确认要删除吗?");
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.DOUBLE_BTN);
      customDialog.show();
   }

   // ////////////////////////////////////
   // 搜索相关
   private Button search_bt;
   private boolean search_open = false;
   private LinearLayout serachLayout;

   private ProjectTypeEntity search_cur_type;
   private ProjectTypeEntity search_cur_distance;

   private ArrayList<ProjectTypeEntity> pro_category;
   private ArrayList<ProjectTypeEntity> geo_range;

   private OtherGridView distance_gv;
   private SearchAdapter distance_gvAdapter;
   private OtherGridView type_gv;
   private SearchAdapter type_gvAdapter;

   // 搜索数据
   private void getSearchParama() {

      RequestInformation request =
          new RequestInformation(UrlHelper.URL_HEAD + "/project/merge_geo_category",
              RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new JsonCallback<ProjectTypeListEntity>() {

         @Override public void onCallback(ProjectTypeListEntity callback) {

            if (callback == null) {
               return;
            }
            if (callback.getStat() == 200) {
               geo_range = callback.getGeo_range();
               geo_range.get(0).setIs_pressed(true);

               pro_category = callback.getPro_category();
               pro_category.get(0).setIs_pressed(true);
               distance_gv = (OtherGridView) serachLayout.findViewById(R.id.distance_gv);

               distance_gvAdapter = new SearchAdapter(SquareFragment.this.getActivity(), geo_range);
               distance_gv.setAdapter(distance_gvAdapter);
               // ////////////////////////////////////////////////////////////////////////////
               type_gv = (OtherGridView) serachLayout.findViewById(R.id.type_gv);

               type_gvAdapter = new SearchAdapter(SquareFragment.this.getActivity(), pro_category);
               type_gv.setAdapter(type_gvAdapter);

               Button button_search = (Button) serachLayout.findViewById(R.id.button_search);
               button_search.setOnClickListener(new OnClickListener() {

                  @Override public void onClick(View view) {

                     // 刷新
                     home_listview.initRefresh(PullToRefreshView.HEADER);
                     onCloseSearch();
                     // 缓存标签结果
                     ProjectTypeEntity type = getSearchCurrentType();
                     search_cur_type = type;

                     ProjectTypeEntity distance = getSearchCurrentDistance();
                     search_cur_distance = distance;
                  }
               });
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(ProjectTypeListEntity.class));
      request.execute();
   }

   private void onOpenSearch() {

      // 打开时，如果没有点击过确定，恢复之前状态
      if (search_cur_type != null) {
         setSearchCurrentType(search_cur_type);
         type_gvAdapter.notifyDataSetChanged();
      } else {
         setSearchCurrentType(pro_category.get(0));
         type_gvAdapter.notifyDataSetChanged();
      }
      if (search_cur_distance != null) {
         setSearchCurrentDistance(search_cur_distance);
         distance_gvAdapter.notifyDataSetChanged();
      } else {
         setSearchCurrentDistance(geo_range.get(0));
         distance_gvAdapter.notifyDataSetChanged();
      }

      // 向左打开
      Animation anim_open =
          AnimationUtils.loadAnimation(SquareFragment.this.getActivity(), R.anim.push_right_in);
      anim_open.setDuration(300);
      // anim_open.setFillAfter(true);
      anim_open.setAnimationListener(new AnimationListener() {

         @Override public void onAnimationStart(Animation arg0) {

         }

         @Override public void onAnimationRepeat(Animation arg0) {

         }

         @Override public void onAnimationEnd(Animation arg0) {
         }
      });
      search_open = true;
      serachLayout.setVisibility(View.VISIBLE);
      serachLayout_main.setVisibility(View.VISIBLE);
      serachLayout_main.startAnimation(anim_open);
   }

   private void onCloseSearch() {
      // 向右关闭
      Animation anim_close =
          AnimationUtils.loadAnimation(SquareFragment.this.getActivity(), R.anim.push_right_out);
      anim_close.setDuration(300);
      // anim_close.setFillAfter(true);
      anim_close.setAnimationListener(new AnimationListener() {

         @Override public void onAnimationStart(Animation arg0) {
         }

         @Override public void onAnimationRepeat(Animation arg0) {

         }

         @Override public void onAnimationEnd(Animation arg0) {
            serachLayout.setVisibility(View.GONE);
            serachLayout_main.setVisibility(View.GONE);
         }
      });
      search_open = false;
      serachLayout_main.startAnimation(anim_close);
   }

   private ProjectTypeEntity getSearchCurrentType() {
      if (pro_category != null) {
         for (ProjectTypeEntity pte : pro_category) {
            if (pte.isIs_pressed()) {
               return pte;
            }
         }
         return null;
      } else {
         return null;
      }
   }

   private void setSearchCurrentType(ProjectTypeEntity pte) {
      for (ProjectTypeEntity projectTypeEntity : pro_category) {

         if (projectTypeEntity == pte) {
            projectTypeEntity.setIs_pressed(true);
         } else {
            projectTypeEntity.setIs_pressed(false);
         }
      }
   }

   private ProjectTypeEntity getSearchCurrentDistance() {
      if (geo_range != null) {
         for (ProjectTypeEntity pte : geo_range) {
            if (pte.isIs_pressed()) {
               return pte;
            }
         }
         return null;
      } else {
         return null;
      }
   }

   private void setSearchCurrentDistance(ProjectTypeEntity pte) {
      for (ProjectTypeEntity projectTypeEntity : geo_range) {

         if (projectTypeEntity == pte) {
            projectTypeEntity.setIs_pressed(true);
         } else {
            projectTypeEntity.setIs_pressed(false);
         }
      }
   }
}
