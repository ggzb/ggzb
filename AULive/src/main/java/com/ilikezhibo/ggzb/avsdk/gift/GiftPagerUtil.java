package com.ilikezhibo.ggzb.avsdk.gift;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.views.UserInfoWebViewActivity;
import com.ilikezhibo.ggzb.views.WebViewActivity;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.callback.StringCallback;
import com.jack.utils.JsonParser;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.gift.entity.GiftAllEntity;
import com.ilikezhibo.ggzb.avsdk.gift.entity.GiftEntity;
import com.ilikezhibo.ggzb.avsdk.gift.entity.SendGiftEntity;
import com.ilikezhibo.ggzb.userinfo.buydiamond.BuyDiamondActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import popwindow.PopupWindowUtil;

public class GiftPagerUtil implements OnPageChangeListener {

   private LayoutInflater inflater;
   private View rootView;
   private BaseFragmentActivity activity;
   private PopupWindowUtil popupWindow;
   private TextView txt_account_balance;

   public GiftPagerUtil(BaseFragmentActivity activity, View rootView, PopupWindowUtil popupWindow) {
      this.activity = activity;
      inflater = LayoutInflater.from(activity);
      initViews(rootView);
      this.popupWindow = popupWindow;
   }




   private LoopViewPager viewPager;
   // private List<View> pageList;
   /** 界面底部的指示圆点容器 */
   private LinearLayout layout_dotView;
   private ImageView[] imgDots;
   /** 统计页卡个数 */
   private int dotCount;
   private View contemView;
   private CheckBox wifi_checkbox;
   private View listHeadView;
   private View listFooterView;
   private View come_everyday;
   private Timer timer;

   private void initViews(View rootView) {
      this.rootView = rootView;
      //更新礼物
      updateGift();
   }

   final public static String GIF_MALL_VERSION_KEY = "GIF_MALL_VERSION_KEY";
   final public static String GIFT_MALL_LOCALE_DATA_KEY = "GIFT_MALL_LOCALE_DATA_KEY";

   // 更新礼物
   public void updateGift() {
      final String oldVersion =
          SharedPreferenceTool.getInstance().getString(GIF_MALL_VERSION_KEY, "0");
      GiftAllEntity.getInstance();
      // 加载礼物内容

      RequestInformation request =
          new RequestInformation(com.jack.utils.UrlHelper.getGiftUrl + "?v=" + oldVersion,
              RequestInformation.REQUEST_METHOD_GET);
      Trace.d("get gift url:" + com.jack.utils.UrlHelper.getGiftUrl + "?v=" + oldVersion);

      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
            e.printStackTrace();
            Utils.showMessage("获取服务器数据失败");
         }

         @Override public void onCallback(String callback) {
            Trace.d("callback:" + callback);
            if (callback.indexOf("200") > 0) {
               GiftAllEntity.getInstance().initializeGifts(callback);
               ArrayList<GiftEntity> global_giftEntities =
                   GiftAllEntity.getInstance().getAllGifts();
               setGiftEntityMap(global_giftEntities);
               initView(rootView, global_giftEntities);
               initDots();
               setPage(global_giftEntities);
               // 两秒钟换一个
               //startPagerTimer(4000);
            } else {
               Utils.showMessage("获取礼物失败");
            }
         }
      });
      request.execute();
   }
   //作废
   //public void startPagerTimer(long time) {
   //   if (timer != null) {
   //      return;
   //   }
   //   timer = new Timer();
   //   TimerTask task = new TimerTask() {
   //      public void run() {
   //         handler.obtainMessage().sendToTarget();
   //      }
   //   };
   //   timer.schedule(task, time, time);
   //}
   //
   //private int pos = 0;
   //private Handler handler = new Handler() {
   //
   //   @Override public void handleMessage(Message msg) {
   //
   //      // int pos = viewPager.getCurrentItem();
   //      viewPager.setCurrentItem(pos++);
   //      // Log.d("big", "te hui handler pos=" + pos);
   //   }
   //};

   private void initView(View root_view, ArrayList<GiftEntity> giftEntities) {
      layout_dotView = (LinearLayout) root_view.findViewById(R.id.layout_dotView);
      viewPager = (LoopViewPager) root_view.findViewById(R.id.pager);
      viewPager.setOnPageChangeListener(this);

      //充值按钮
      LinearLayout btn_charge = (LinearLayout) root_view.findViewById(R.id.btn_charge);
      btn_charge.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            Intent moeny_intent = new Intent(activity, BuyDiamondActivity.class);
            activity.startActivity(moeny_intent);
//            Intent intent4 =
//                    new Intent(activity, UserInfoWebViewActivity.class);
//            intent4.putExtra(WebViewActivity.input_url,
//                    UrlHelper.SERVER_URL + "profile/h5charge");
//            intent4.putExtra(WebViewActivity.back_home_key, false);
//            intent4.putExtra(WebViewActivity.actity_name, "充值");
//            activity.startActivity(intent4);
         }
      });
      //设置当前钻石数
      txt_account_balance = (TextView) root_view.findViewById(R.id.txt_account_balance);
      int diamond = AULiveApplication.getUserInfo().diamond;
      txt_account_balance.setText(diamond + "");

      //处理送礼物事件
      btn_send = (Button) root_view.findViewById(R.id.btn_send);
      btn_send.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            doSendGift();
         }
      });

      //连击送礼
      btn_continue_send = (Button) root_view.findViewById(R.id.btn_continue_send);
      btn_continue_send.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {
            doSendGift();
         }
      });

      dotCount = giftEntities.size() / 8;
      if (giftEntities.size() % 8 != 0) {
         dotCount = dotCount + 1;
      }
   }

   //连击送礼物
   private Timer mTimer;
   private Button btn_send;
   private Button btn_continue_send;
   private int handlerTime = 10000;

   /** 设置底部圆点 */
   private void initDots() {
      imgDots = new ImageView[dotCount];
      layout_dotView.removeAllViews();
      for (int i = 0; i < dotCount; i++) {
         ImageView dotView = new ImageView(activity);
         if (i == 0) {
            dotView.setBackgroundResource(R.drawable.dot_white);
         } else {
            dotView.setBackgroundResource(R.drawable.dot_gray);
         }
         imgDots[i] = dotView;
         // 设置圆点布局参数
         LinearLayout.LayoutParams params =
             new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT);
         params.setMargins(7, 0, 7, 0);
         dotView.setLayoutParams(params);
         layout_dotView.addView(dotView);
      }
   }

   private void setPage(ArrayList<GiftEntity> giftEntities) {
      viewPager.setAdapter(new ViewPagerAdapter(activity, giftEntities));
      viewPager.setVisibility(View.VISIBLE);
      if (isCycle) {
            /*
             * 此处设置当前页的显示位置,设置在100(随便什么数,稍微大点就行)就 可以实现向左循环,当然是有限制的,不过一般情况下没啥问题
			 */
         // viewPager.setCurrentItem(100);
      }
   }

   @Override public void onPageSelected(int position) {
      // Trace.d("onPageSelected:" + position);
      if (isCycle) {
         position = position % dotCount;
      }

      for (int i = 0; i < dotCount; i++) {
         if (i == position) {
            imgDots[i].setBackgroundResource(R.drawable.dot_white);
         } else {
            imgDots[i].setBackgroundResource(R.drawable.dot_gray);
         }
      }
   }

   @Override public void onPageScrollStateChanged(int arg0) {

   }

   @Override public void onPageScrolled(int arg0, float arg1, int arg2) {

   }

   //PageUtil
   /** 是否循环 */
   public static final boolean isCycle = false;

   public class ViewPagerAdapter extends PagerAdapter {
      //存放每一页的view
      private ArrayList<GiftEntity> giftEntities;
      private Activity activity;

      public ViewPagerAdapter(Activity activity, ArrayList<GiftEntity> giftEntities) {
         this.giftEntities = giftEntities;
         this.activity = activity;
      }

      @Override public int getCount() {
         int page_num = giftEntities.size() / 8;
         if (giftEntities.size() % 8 != 0) {
            return page_num = page_num + 1;
         }

         return page_num;
      }

      @Override public boolean isViewFromObject(View view, Object object) {
         return view == object;
      }

      public void destroyItem(ViewGroup container, int position, Object object) {
         ((ViewPager) container).removeView((View) object);
      }

      //每次滑动的时候生成的组件
      @Override public Object instantiateItem(ViewGroup container, int position) {

         View pager_page = inflater.inflate(R.layout.room_gift_pager_page, null);
         //做页面初始化
         View gift1 = pager_page.findViewById(R.id.gift1);
         View gift2 = pager_page.findViewById(R.id.gift2);
         View gift3 = pager_page.findViewById(R.id.gift3);
         View gift4 = pager_page.findViewById(R.id.gift4);
         View gift5 = pager_page.findViewById(R.id.gift5);
         View gift6 = pager_page.findViewById(R.id.gift6);
         View gift7 = pager_page.findViewById(R.id.gift7);
         View gift8 = pager_page.findViewById(R.id.gift8);

         int gift_count = giftEntities.size();
         if (gift_count >= position * 8 + 1) {
            initPagerPage(gift1, giftEntities.get(position * 8 + 0));
         }
         if (gift_count >= position * 8 + 2) {
            initPagerPage(gift2, giftEntities.get(position * 8 + 1));
         }
         if (gift_count >= position * 8 + 3) {
            initPagerPage(gift3, giftEntities.get(position * 8 + 2));
         }
         if (gift_count >= position * 8 + 4) {
            initPagerPage(gift4, giftEntities.get(position * 8 + 3));
         }
         if (gift_count >= position * 8 + 5) {
            initPagerPage(gift5, giftEntities.get(position * 8 + 4));
         }
         if (gift_count >= position * 8 + 6) {
            initPagerPage(gift6, giftEntities.get(position * 8 + 5));
         }
         if (gift_count >= position * 8 + 7) {
            initPagerPage(gift7, giftEntities.get(position * 8 + 6));
         }
         if (gift_count >= position * 8 + 8) {
            initPagerPage(gift8, giftEntities.get(position * 8 + 7));
         }

         ((ViewPager) container).addView(pager_page);
         return pager_page;
      }
   }

   public void initPagerPage(View gift_item, final GiftEntity giftEntity) {
      gift_item.setVisibility(View.VISIBLE);
      ImageView gift_icon = (ImageView) gift_item.findViewById(R.id.img_gift_icon);
      //用于及时更新图片
      String oldVersion = SharedPreferenceTool.getInstance().getString(GIF_MALL_VERSION_KEY, "0");
      ImageLoader.getInstance()
          .displayImage(
              com.jack.utils.UrlHelper.GIFT_ROOT_URL + giftEntity.getId() + ".png?v=" + oldVersion,
              gift_icon, AULiveApplication.getGlobalImgOptions());

      TextView txt_gift_value = (TextView) gift_item.findViewById(R.id.txt_gift_value);
      txt_gift_value.setText(giftEntity.getPrice() + "");

      TextView txt_experience = (TextView) gift_item.findViewById(R.id.txt_experience);
      txt_experience.setText(giftEntity.getName() + "");

      // 礼物活动标记
      ImageView actImg = (ImageView) gift_item.findViewById(R.id.act_img);
      if (actImg != null) {
         if (giftEntity.getAct() == 1) {
            actImg.setVisibility(View.VISIBLE);
         } else {
            actImg.setVisibility(View.GONE);
         }
      }

      final ImageView img_type = (ImageView) gift_item.findViewById(R.id.img_type);
      //连击
      if (giftEntity.getType() == 1) {
         img_type.setImageDrawable(
             activity.getResources().getDrawable(R.drawable.icon_continue_gift));
      } else if (giftEntity.getType() == 2) {
         //红包什么也不做
      }
      if (giftEntity.getType() == 3) {
         //大礼物 什么也不做
      }

      gift_item.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View view) {

            //先把已选的取消，在赋值
            if (old_chosen_Entity != null) {
               //连击
               if (old_chosen_Entity.getType() == 1) {
                  old_chosen_iv.setImageDrawable(
                      activity.getResources().getDrawable(R.drawable.icon_continue_gift));
               } else if (old_chosen_Entity.getType() == 2) {
                  //红包什么也不做
                  old_chosen_iv.setImageDrawable(null);
               }
               if (old_chosen_Entity.getType() == 3) {
                  //大礼物 什么也不做
                  old_chosen_iv.setImageDrawable(null);
               }
            }

            //两次连点击取消
            if (old_chosen_Entity == giftEntity) {
               old_chosen_Entity = null;
               old_chosen_iv = null;
               return;
            } else {
               old_chosen_Entity = giftEntity;
               old_chosen_iv = img_type;
               //改变图标
               img_type.setImageDrawable(
                   activity.getResources().getDrawable(R.drawable.icon_continue_gift_chosen));
            }
         }
      });
   }

   //记录已经点击的礼物
   ImageView old_chosen_iv;
   GiftEntity old_chosen_Entity;

   public void doSendGift() {

      if (old_chosen_Entity == null) {
         Utils.showMessage("请先选择礼物后在尝试");
         return;
      }

      if (AULiveApplication.currLiveUid == null && AULiveApplication.oneToOneUid == null) {
         return;
      }
      RequestInformation request = null;
      StringBuilder sb;
      try {
         sb = new StringBuilder(UrlHelper.getSendGiftUrl
             + "?roomid="
             + AULiveApplication.currLiveUid
             + "&liveuid="
             + AULiveApplication.currLiveUid
             + "&giftid="
             + old_chosen_Entity.getId());

         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<SendGiftEntity>() {

         @Override public void onCallback(SendGiftEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               //Utils.showMessage("送礼成功");
               if (old_chosen_Entity == null) {
                  Utils.showMessage("你还没选择礼物");
                  return;
               }
               AULiveApplication.getUserInfo().diamond = callback.getDiamond();
               AULiveApplication.getUserInfo().setGrade(callback.getGrade() + "");
               txt_account_balance.setText(callback.getDiamond() + "");

               //克隆entity
               String chose_entity = JsonParser.serializeToJson(old_chosen_Entity);
               GiftEntity temp_GiftEntity =
                   JsonParser.deserializeByJson(chose_entity, GiftEntity.class);
               //当不是红包时为0
               temp_GiftEntity.setId(callback.gift_id);
               temp_GiftEntity.setType(callback.gift_type);
               temp_GiftEntity.setPacketid(callback.getPacketid());
               temp_GiftEntity.recv_diamond = callback.getRecv_diamond();
               temp_GiftEntity.setGrade(callback.getGrade());

               String gift_name = getGiftName(callback.gift_id);
               if (gift_name == null || gift_name.equals("")) {
               } else {
                  temp_GiftEntity.setName(gift_name);
               }

               if (activity instanceof AvActivity) {
                  AvActivity temp_activity = (AvActivity) activity;
                  //显示礼物动画
                  temp_activity.sendGiftMsg(temp_GiftEntity);
               }
               //处理10秒没有再点击消失
               if (old_chosen_Entity.getType() == 1) {
                  btn_send.setVisibility(View.GONE);
                  btn_continue_send.setVisibility(View.VISIBLE);
                  viewPager.setClickable(false);

                  if (mTimer != null) {
                     mTimer.cancel();
                     mTimer = null;
                  }
                  handlerTime = 6000;
                  mTimer = new Timer();
                  mTimer.schedule(new TimerTask() {
                     @Override public void run() {

                        if (handlerTime > 0) {
                           handlerTime = handlerTime - 100;
                           activity.runOnUiThread(new Runnable() {
                              @Override public void run() {
                                 btn_continue_send.setText("(" + handlerTime / 100 + ")");
                              }
                           });
                        } else {
                           activity.runOnUiThread(new Runnable() {
                              @Override public void run() {
                                 btn_continue_send.setVisibility(View.GONE);
                                 btn_send.setVisibility(View.VISIBLE);
                                 btn_continue_send.setText("连发5S");
                                 viewPager.setClickable(true);
                              }
                           });

                           mTimer.cancel();
                           mTimer = null;
                        }
                     }
                  }, 100, 100);
               }
            } else {
               Utils.showMessage(callback.getMsg());
               Intent moeny_intent = new Intent(activity, BuyDiamondActivity.class);
               activity.startActivity(moeny_intent);
//                Intent intent4 =
//                        new Intent(activity, UserInfoWebViewActivity.class);
//                intent4.putExtra(WebViewActivity.input_url,
//                        UrlHelper.SERVER_URL + "profile/h5charge");
//                intent4.putExtra(WebViewActivity.back_home_key, false);
//                intent4.putExtra(WebViewActivity.actity_name, "充值");
//                activity.startActivity(intent4);
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(SendGiftEntity.class));
      request.execute();
   }

   //获取礼物昵称，防止乱点昵称错了现象
   private String getGiftName(int gift_id) {
      if (gift_map == null) {
         return "";
      }
      return gift_map.get(gift_id).getName();
   }

   private HashMap<Integer, GiftEntity> gift_map = new HashMap<Integer, GiftEntity>();

   private void setGiftEntityMap(ArrayList<GiftEntity> global_giftEntities) {
      gift_map.clear();
      for (GiftEntity giftEntity : global_giftEntities) {
         gift_map.put(giftEntity.getId(), giftEntity);
      }
   }

   ///主播发礼物
   public static void doHostSendRedbag(final AvActivity activity, String giftid) {

      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(UrlHelper.getSendGiftUrl
             + "?roomid="
             + AULiveApplication.currLiveUid
             + "&liveuid="
             + AULiveApplication.currLiveUid
             + "&giftid="
             + giftid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<SendGiftEntity>() {

         @Override public void onCallback(SendGiftEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {

               AULiveApplication.getUserInfo().diamond = callback.getDiamond();
               AULiveApplication.getUserInfo().setGrade(callback.getGrade() + "");

               GiftEntity temp_GiftEntity = new GiftEntity();
               //当不是红包时为0
               temp_GiftEntity.setId(callback.gift_id);
               temp_GiftEntity.setType(callback.gift_type);
               temp_GiftEntity.setPacketid(callback.getPacketid());
               temp_GiftEntity.recv_diamond = callback.getRecv_diamond();
               temp_GiftEntity.setGrade(callback.getGrade());
               String gift_name = "红包";
               temp_GiftEntity.setName(gift_name);
               temp_GiftEntity.setPrice(200);

               //显示礼物动画
               activity.sendGiftMsg(temp_GiftEntity);
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(SendGiftEntity.class));
      request.execute();
   }

   public void upDataDiamond(int diamond) {
      txt_account_balance.setText("" + diamond);
   }
}