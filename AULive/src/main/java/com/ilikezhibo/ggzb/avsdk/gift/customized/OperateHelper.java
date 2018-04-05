package com.ilikezhibo.ggzb.avsdk.gift.customized;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.PixelDpHelper;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.gift.entity.GiftEntity;
import com.ilikezhibo.ggzb.avsdk.gift.entity.SendGiftEntity;

import java.util.List;

/**
 * Created by big on 11/21/16.
 */

public class OperateHelper {
   AvActivity avActivity;
   int count;
   private TextView used_balance_prompt;
   private boolean isOpenPadview;
   private View one;
   private View two;
   private View three;
   private View four;
   private RadioGroup rg_gift_list;
   private View iv_icon_diamond;
   private RadioButton[] radioBtns;
   private Drawable[] drawables;

   public boolean isOpenPadview() {
      return isOpenPadview;
   }

   public void setOpenPadview(boolean openPadview) {
      isOpenPadview = openPadview;
   }

   public OperateHelper(AvActivity avActivity1) {
      avActivity = avActivity1;
      init();
   }

   private LinearLayout custom_gift_hint;
   private TextView send_btn;
   private SketchPadView sketchpadview;
   private TextView clear_all;
   private View close_dialog;

   public void init() {
      iv_icon_diamond = avActivity.getGiveGiftView().findViewById(R.id.iv_icon_diamond);
      sketchpadview = (SketchPadView) avActivity.getGiveGiftView().findViewById(R.id.sketchpadview);
      sketchpadview.setOnTouchListener(onTouchListener);

      custom_gift_hint =
          (LinearLayout) avActivity.getGiveGiftView().findViewById(R.id.custom_gift_hint);

      send_btn = (TextView) avActivity.getGiveGiftView().findViewById(R.id.send_btn);
      send_btn.setEnabled(false);
      send_btn.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View v) {
            doSendGift(sketchpadview.sketchHelper.pointDataBeanList.size());
         }
      });
      used_balance_prompt =
          (TextView) avActivity.getGiveGiftView().findViewById(R.id.used_balance_prompt);

      close_dialog = avActivity.getGiveGiftView().findViewById(R.id.close_dialog);
      close_dialog.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View v) {
            doClose();
         }
      });

      rg_gift_list = (RadioGroup) avActivity.getGiveGiftView().findViewById(R.id.rg_gift_list);
      RadioButton rb_gift_one = (RadioButton) rg_gift_list.findViewById(R.id.rb_gift_one);
      RadioButton rb_gift_two = (RadioButton) rg_gift_list.findViewById(R.id.rb_gift_two);
      RadioButton rb_gift_three = (RadioButton) rg_gift_list.findViewById(R.id.rb_gift_three);
      RadioButton rb_gift_four = (RadioButton) rg_gift_list.findViewById(R.id.rb_gift_four);
      radioBtns = new RadioButton[] { rb_gift_one, rb_gift_two, rb_gift_three, rb_gift_four };
      initRadioButton();
//      final ImageButton one = (ImageButton) avActivity.getGiveGiftView().findViewById(R.id.iv_gift_one);
      one = avActivity.getGiveGiftView().findViewById(R.id.iv_gift_one);
      two = avActivity.getGiveGiftView().findViewById(R.id.iv_gift_two);
      three = avActivity.getGiveGiftView().findViewById(R.id.iv_gift_three);
      four = avActivity.getGiveGiftView().findViewById(R.id.iv_gift_four);
      // 默认选中第一个
      rg_gift_list.check(R.id.iv_gift_one);
      sketchpadview.setIconBitmap(0);
      setIvEnableId(0);

      rg_gift_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
            padViewReset();
            switch (checkId) {
               case R.id.rb_gift_one:
                  sketchpadview.sketchHelper.point_type = 0;
                  sketchpadview.setIconBitmap(0);
                  setIvEnableId(0);
                  break;
               case R.id.rb_gift_two:
                  sketchpadview.sketchHelper.point_type = 1;
                  sketchpadview.setIconBitmap(1);
                  setIvEnableId(1);
                  break;
               case R.id.rb_gift_three:
                  sketchpadview.sketchHelper.point_type = 2;
                  sketchpadview.setIconBitmap(2);
                  setIvEnableId(2);
                  break;
               case R.id.rb_gift_four:
                  sketchpadview.sketchHelper.point_type = 3;
                  sketchpadview.setIconBitmap(3);
                  setIvEnableId(3);
                  break;
            }
         }
      });

      clear_all = (TextView) avActivity.getGiveGiftView().findViewById(R.id.clear_all);
      clear_all.setOnClickListener(new View.OnClickListener() {
         @Override public void onClick(View v) {
            padViewReset();
         }
      });

      sketchpadview.sketchHelper.refleshInterface = new SketchHelper.RefleshInterface() {
         public final void onreflsesh() {
            count++;
            if (count < 10) {
               used_balance_prompt.setText("至少要画10个小礼物,才能送出哦");
            } else {
               send_btn.setEnabled(true);
               used_balance_prompt.setText("画了" + count + "个小礼物, 共计消耗" + count * 10);
               iv_icon_diamond.setVisibility(View.VISIBLE);
            }
            sketchpadview.invalidate();
         }
      };
   }

   private void initRadioButton() {

      for (int i = 0; i < radioBtns.length; i++) {//循环

         drawables =
             radioBtns[i].getCompoundDrawables();//通过RadioButton的getCompoundDrawables()方法，拿到图片的drawables,分别是左上右下的图片

         drawables[1].setBounds(0, 0,
             avActivity.getResources().getDimensionPixelSize(R.dimen.room_share_icon_width),
             avActivity.getResources().getDimensionPixelSize(R.dimen.room_share_icon_width));

         radioBtns[i].setCompoundDrawables(drawables[0], drawables[1], drawables[2],
             drawables[3]);//将改变了属性的drawable再重新设置回去
      }
   }

   private void setIvEnableId(int id) {
      one.setEnabled(id == 0);
      two.setEnabled(id == 1);
      three.setEnabled(id == 2);
      four.setEnabled(id == 3);
   }

   public void doClose() {
      isOpenPadview = false;
//      View custom_gift_layout = avActivity.getGiveGiftView().findViewById(R.id.custom_gift_layout);
      ViewAnimator.animate(avActivity.getGiveGiftView())
          .translationY(0, PixelDpHelper.dip2px(avActivity, 1200))
          .duration(600)

          .onStop(new AnimationListener.Stop() {
             @Override public void onStop() {
                padViewReset();
                // 还原默认选中第一个图形
                rg_gift_list.check(R.id.rb_gift_one);
                avActivity.getGiveGiftView().setVisibility(View.INVISIBLE);
             }
          })
          .start();
   }

   /**
    * 还原为初始状态
    */
   private void padViewReset() {
      sketchpadview.clean();
      count = 0;
      used_balance_prompt.setText("在上方选择想要绘制的小礼物");
      iv_icon_diamond.setVisibility(View.INVISIBLE);
      custom_gift_hint.setVisibility(View.VISIBLE);
      send_btn.setEnabled(false);
   }

   private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
      public final boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {

         switch (paramAnonymousMotionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
               custom_gift_hint.setVisibility(View.INVISIBLE);
               Trace.d("SketchPadView onTouch ACTION_DOWN");
               sketchpadview.sketchHelper.drawIcon(paramAnonymousMotionEvent.getX(),
                   paramAnonymousMotionEvent.getY());
               return true;
            case MotionEvent.ACTION_MOVE:
               Trace.d("SketchPadView onTouch ACTION_MOVE");
               sketchpadview.sketchHelper.drawIcon(paramAnonymousMotionEvent.getX(),
                   paramAnonymousMotionEvent.getY());
               return true;

            case MotionEvent.ACTION_UP:
               Trace.d("SketchPadView onTouch ACTION_UP");
               sketchpadview.sketchHelper.start_point = null;
               return true;
            default:
               return true;
         }
      }
   };

   //手画礼物接口/gift/drawsend?liveuid=121&giftid=121&num=10
   public void doSendGift(int num) {
//      // 测试
      if (AULiveApplication.currLiveUid == null) {
         return;
      }
      RequestInformation request = null;
      StringBuilder sb;
      try {
         sb = new StringBuilder(UrlHelper.SERVER_URL
             + "gift/drawsend"
             + "?num="
             + num
             + "&liveuid="
             + AULiveApplication.currLiveUid
             + "&giftid=1000");

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

               List<PointEntity.PointDataBean> pointDataBeens =
                   sketchpadview.sketchHelper.pointDataBeanList;
               PointEntity pointEntity = new PointEntity();
               pointEntity.point_count = pointDataBeens.size();
               pointEntity.point_type = sketchpadview.sketchHelper.point_type;
               pointEntity.square_size = sketchpadview.getMeasuredWidth();
               pointEntity.point_data = pointDataBeens;

               GiftEntity temp_GiftEntity = new GiftEntity();
               //当不是红包时为0
               temp_GiftEntity.setId(callback.gift_id);
               temp_GiftEntity.setType(3);
               temp_GiftEntity.setPacketid(callback.getPacketid());
               temp_GiftEntity.recv_diamond = callback.getRecv_diamond();
               temp_GiftEntity.setGrade(callback.getGrade());
               temp_GiftEntity.setName("个性礼物:" + pointEntity.point_count);
               temp_GiftEntity.setPrice(10);
               temp_GiftEntity.paint = pointEntity;

               //显示礼物动画
               avActivity.sendGiftMsg(temp_GiftEntity);
               doClose();
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
}
