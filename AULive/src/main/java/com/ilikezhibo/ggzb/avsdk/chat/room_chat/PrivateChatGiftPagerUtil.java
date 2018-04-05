package com.ilikezhibo.ggzb.avsdk.chat.room_chat;

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
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.gift.LoopViewPager;
import com.ilikezhibo.ggzb.avsdk.gift.entity.GiftAllEntity;
import com.ilikezhibo.ggzb.avsdk.gift.entity.GiftEntity;
import com.ilikezhibo.ggzb.avsdk.gift.entity.SendGiftEntity;
import com.ilikezhibo.ggzb.userinfo.buydiamond.BuyDiamondActivity;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;

import java.util.ArrayList;
import java.util.Timer;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;

public class PrivateChatGiftPagerUtil implements OnPageChangeListener {

   private LayoutInflater inflater;
   private View rootView;
   private Activity activity;

   //送礼的房间号
   public String room_id;
   //收礼的uid
   public String live_uid;
   private TextView txt_account_balance;

   public PrivateChatGiftPagerUtil(Activity activity, View rootView, String room_id,
       String live_uid) {
      this.activity = activity;
      inflater = LayoutInflater.from(activity);
      initViews(rootView);
      this.room_id = room_id;
      this.live_uid = live_uid;
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
      startProgressDialog();
      final String oldVersion =
          SharedPreferenceTool.getInstance().getString(GIF_MALL_VERSION_KEY, "0");
      GiftAllEntity.getInstance();
      // 加载礼物内容
      RequestInformation request = new RequestInformation(UrlHelper.getGiftUrl + "?v=" + oldVersion,
          RequestInformation.REQUEST_METHOD_GET);
      Trace.d("get gift url:" + UrlHelper.getGiftUrl + "?v=" + oldVersion);
      request.setCallback(new StringCallback() {

         @Override public void onFailure(AppException e) {
            stopProgressDialog();
            e.printStackTrace();
            Utils.showMessage("获取服务器数据失败");
         }

         @Override public void onCallback(String callback) {
            stopProgressDialog();
            Trace.d("callback:" + callback);
            if (callback.indexOf("200") > 0) {
               GiftAllEntity.getInstance().initializeGifts(callback);
               ArrayList<GiftEntity> giftEntities = GiftAllEntity.getInstance().getAllGifts();
               //私聊礼物，不能发红包，去除
               ArrayList<GiftEntity> new_giftEntities = new ArrayList<GiftEntity>();
               for (GiftEntity giftEntity : giftEntities) {
                  if (giftEntity.getType() != 2) {
                     new_giftEntities.add(giftEntity);
                  }
               }

               initView(rootView, new_giftEntities);
               initDots();
               setPage(new_giftEntities);
               // 两秒钟换一个
               //startPagerTimer(4000);
            } else {
               Utils.showMessage("获取礼物失败");
            }
         }
      });
      request.execute();
   }

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
      viewPager.setAdapter(new ViewPagerAdapter(giftEntities));
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

      public ViewPagerAdapter(ArrayList<GiftEntity> giftEntities) {
         this.giftEntities = giftEntities;
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
      String oldVersion = SharedPreferenceTool.getInstance().getString(GIF_MALL_VERSION_KEY, "0");
      ImageLoader.getInstance()
          .displayImage(UrlHelper.GIFT_ROOT_URL + giftEntity.getId() + ".png?v=" + oldVersion,
              gift_icon, AULiveApplication.getGlobalImgOptions());

      TextView txt_gift_value = (TextView) gift_item.findViewById(R.id.txt_gift_value);
      txt_gift_value.setText(giftEntity.getPrice() + "");

      TextView txt_experience
              = (TextView) gift_item.findViewById(R.id.txt_experience);
      txt_experience.setText(giftEntity.getName() + "");

      // 礼物活动标记
      ImageView actImg = (ImageView)gift_item.findViewById(R.id.act_img);
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
      RequestInformation request = null;

      try {
         StringBuilder sb = new StringBuilder(UrlHelper.getSendGiftUrl
             + "?roomid="
             + room_id
             + "&liveuid="
             + live_uid
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
               txt_account_balance.setText(callback.getDiamond() + "");
               //当不是红包时为0
               old_chosen_Entity.setPacketid(callback.getPacketid());
               old_chosen_Entity.recv_diamond = callback.getRecv_diamond();
               old_chosen_Entity.setGrade(callback.getGrade());
               String oldVersion =
                   SharedPreferenceTool.getInstance().getString(GIF_MALL_VERSION_KEY, "0");
               //显示礼物到私聊
               sendGiftPicture(live_uid,
                   UrlHelper.GIFT_ROOT_URL + old_chosen_Entity.getId() + ".png?v=" + oldVersion);
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

   public void sendGiftPicture(String targetId, String pic_url) {
      ////发送图片消息
      //File imageFileSource = new File(FileUtil.getImageFolder(), "source.jpg");
      //File imageFileThumb = new File(FileUtil.getImageFolder(), "thumb.jpg");
      //
      //try {
      //   // 读取图片
      //   Bitmap bmpSource = ImageLoader.getInstance()
      //       .loadImageSync(pic_url, new ImageSize(100, 100),
      //           AULiveApplication.getGlobalImgOptions());
      //
      //   imageFileSource.createNewFile();
      //
      //   FileOutputStream fosSource = new FileOutputStream(imageFileSource);
      //
      //   // 保存原图。
      //   bmpSource.compress(Bitmap.CompressFormat.JPEG, 100, fosSource);
      //
      //   // 创建缩略图变换矩阵。
      //   Matrix Point = new Matrix();
      //   Point.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()),
      //       new RectF(0, 0, 160, 160), Matrix.ScaleToFit.CENTER);
      //
      //   // 生成缩略图。
      //   Bitmap bmpThumb =
      //       Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), Point,
      //           true);
      //
      //   imageFileThumb.createNewFile();
      //
      //   FileOutputStream fosThumb = new FileOutputStream(imageFileThumb);
      //
      //   // 保存缩略图。
      //   bmpThumb.compress(Bitmap.CompressFormat.JPEG, 60, fosThumb);
      //} catch (IOException e) {
      //   e.printStackTrace();
      //}
      //
      //ImageMessage imgMsg =
      //    ImageMessage.obtain(Uri.fromFile(imageFileThumb), Uri.fromFile(imageFileSource));
      //
      //RongIMClient.getInstance()
      //    .sendMessage(Conversation.ConversationType.PRIVATE, targetId,
      //        TextMessage.obtain("送你一个" + old_chosen_Entity.getName()),
      //        "送你一个" + old_chosen_Entity.getName(), "送你一个" + old_chosen_Entity.getName(),
      //        new RongIMClient.SendMessageCallback() {
      //           @Override public void onError(Integer messageId, RongIMClient.ErrorCode e) {
      //
      //           }
      //
      //           @Override public void onSuccess(Integer integer) {
      //
      //           }
      //        }, new RongIMClient.ResultCallback<Message>() {
      //           @Override public void onError(RongIMClient.ErrorCode errorCode) {
      //
      //           }
      //
      //           @Override public void onSuccess(Message message) {
      //
      //           }
      //        });

      //
      RichContentMessage mRichContentMessage = new RichContentMessage("赠送礼物",
              "送你一个" + old_chosen_Entity.getName() + ",增加" + old_chosen_Entity.getPrice() + Utils.trans(R.string.app_money),
              pic_url);



/**
 * 发送图片消息。
 *
 * @param conversationType         会话类型。
 * @param targetId                 会话目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
 * @param imgMsg                   消息内容。
 * @param pushContent              接收方离线时需要显示的push消息内容。
 * @param pushData                 接收方离线时需要在push消息中携带的非显示内容。
 * @param SendImageMessageCallback 发送消息的回调。
 */
      RongIMClient.getInstance()
          .sendMessage(Conversation.ConversationType.PRIVATE, targetId, mRichContentMessage, "赠送礼物",
              "送你一个" + old_chosen_Entity.getName(), new RongIMClient.SendImageMessageCallback() {

                 @Override public void onAttached(Message message) {
                    //保存数据库成功
                 }

                 @Override public void onError(Message message, RongIMClient.ErrorCode code) {
                    //发送失败
                    Utils.showCroutonText(activity, "发送失败");
                 }

                 @Override public void onSuccess(Message message) {
                    //发送成功
                    Utils.showCroutonText(activity, "发送成功");
                 }

                 @Override public void onProgress(Message message, int progress) {
                    //发送进度
                 }
              }, new RongIMClient.ResultCallback<Message>() {
                 @Override public void onSuccess(Message message) {
                    Utils.showCroutonText(activity, "发送成功!");
                 }

                 @Override public void onError(RongIMClient.ErrorCode errorCode) {
                    Utils.showCroutonText(activity, "发送失败!");
                 }
              });
   }

   private CustomProgressDialog progressDialog = null;

   public void startProgressDialog() {
      if (progressDialog == null) {
         progressDialog = CustomProgressDialog.createDialog(activity);
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
