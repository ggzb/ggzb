package com.ilikezhibo.ggzb.avsdk.home.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.views.VariousDialog;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.DragViewUtils;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalListEvent;
import com.ilikezhibo.ggzb.avsdk.activity.entity.AVEntity;
import com.ilikezhibo.ggzb.avsdk.activity.roomfliphelper.LiveListEvent;
import com.ilikezhibo.ggzb.avsdk.home.EnterRoomEntity;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;

/**
 * @author big
 * @ClassName: GroupFragmentAdapter
 * @Description: 主页适配
 * @date 2014-7-8 下午4:12:57
 */
public class FenLeiAdapter extends BaseAdapter {
   private Context context;

   private ViewHolder holder;
   private AVEntity mChoseAdvEntity = null;
   private ArrayList<AVEntity> entities;
   private final VariousDialog mDialog;

   public FenLeiAdapter(Context context) {
      this.context = context;
      mDialog = new VariousDialog(context);
   }

   public void setEntities(ArrayList<AVEntity> entities_temp) {

      this.entities = new ArrayList<AVEntity>();

      entities.addAll(entities_temp);

      if (entities.size() <= 0) {
         return;
      }
      // 设置标题
      entities.get(0).setHas_title("");
      // 当最后一行不满三个时，用空白代替
      int size = entities.size();
      int add = size % 3;
      // Trace.d("size % 3:"+add);
      if (add == 1) {
         AVEntity advEntity = new AVEntity();
         advEntity.setEmpty(true);
         entities.add(advEntity);
         AVEntity advEntity2 = new AVEntity();
         advEntity2.setEmpty(true);
         entities.add(advEntity2);
      }
      if (add == 2) {
         AVEntity advEntity = new AVEntity();
         advEntity.setEmpty(true);
         entities.add(advEntity);
      }
   }

   //按行来算
   @Override public int getCount() {
      if (entities != null && entities.size() > 0) {
         int count = entities.size() / 3;
         if (entities.size() % 3 != 0) {
            count++;
         }
         return count;
      }

      return 0;
   }

   @Override public Object getItem(int position) {
      return entities.get(position);
   }

   @Override public long getItemId(int position) {

      return 0;
   }

   DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.loading)
           .showImageForEmptyUri(R.drawable.loading)
           .showImageOnFail(R.drawable.loading)
           .cacheOnDisk(true)
           //.cacheInMemory(true)
           //    // 设置图片以如何的编码方式显示
           //.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
           //    // 设置图片的解码类型
           //.bitmapConfig(Bitmap.Config.RGB_565)
           .build();

   @Override public View getView(final int position, View convertView, ViewGroup parent) {

      if (convertView == null || convertView.getTag() == null) {
         holder = new ViewHolder();
         convertView = LayoutInflater.from(context).inflate(R.layout.layer_square_news_item, null);
         holder.title_layout = (LinearLayout) convertView.findViewById(R.id.title_layout);
         holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);

         holder.layout_one = (LinearLayout) convertView.findViewById(R.id.layout_one);
         holder.layout_two = (LinearLayout) convertView.findViewById(R.id.layout_two);
         holder.layout_three = (LinearLayout) convertView.findViewById(R.id.layout_three);

         holder.iv_deal_pic = (ImageView) convertView.findViewById(R.id.iv_deal_pic);
         holder.tv_title_one = (TextView) convertView.findViewById(R.id.tv_title_one);
         holder.iv_pay_one = (ImageView) convertView.findViewById(R.id.iv_pay_one);



         holder.iv_deal_pic_two = (ImageView) convertView.findViewById(R.id.iv_deal_pic_two);
         holder.tv_title_two = (TextView) convertView.findViewById(R.id.tv_title_two);
         holder.iv_pay_two = (ImageView) convertView.findViewById(R.id.iv_pay_two);

         holder.iv_deal_pic_three = (ImageView) convertView.findViewById(R.id.iv_deal_pic_three);
         holder.tv_title_three = (TextView) convertView.findViewById(R.id.tv_title_three);
         holder.iv_pay_three = (ImageView) convertView.findViewById(R.id.iv_pay_three);

         holder.rlayout_deal_image =
                 (RelativeLayout) convertView.findViewById(R.id.rlayout_deal_image);
         holder.rlayout_deal_image_two =
                 (RelativeLayout) convertView.findViewById(R.id.rlayout_deal_image_two);
         holder.rlayout_deal_image_three =
                 (RelativeLayout) convertView.findViewById(R.id.rlayout_deal_image_three);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }

      // 一行显示两个entity
      final int index = position * 3;
      final int netxt = index + 1;
      final int three = netxt + 1;
      final AVEntity entity = entities.get(index);

      // 还是要使用position处理，不能使用index
      if (entities.size() > index) {
         // 处理第一个
         if (!entity.getHas_title().equals("")) {
            holder.title_layout.setVisibility(View.VISIBLE);
            holder.title_tv.setText(entity.getHas_title());
         }

         if (entity.payliving == 1) {
            holder.iv_pay_one.setImageResource(R.drawable.live_pay);
         } else {
            holder.iv_pay_one.setImageResource(R.drawable.live_free);
         }

         ImageLoader.getInstance().displayImage(entity.face, holder.iv_deal_pic, options);
         holder.tv_title_one.setText(entity.nickname);
         holder.layout_one.setOnClickListener(new OnClickListener() {

            @Override public void onClick(View view) {
               if (BtnClickUtils.isFastDoubleClick()) {
                  return;
               }
               mChoseAdvEntity = entity;
               if (mChoseAdvEntity != null && mChoseAdvEntity.uid.equals(
                       AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                  Utils.showCroutonText((Activity) context, "不能加入自己创建的房间");
                  return;
               }

               if (entity.payliving == 1) {
                  if (AULiveApplication.getUserInfo().diamond > 10) {
                     mDialog.payDialog();
                     mDialog.setDialogListener(new VariousDialog.DialogListener() {
                        @Override
                        public void buttonClick(boolean payResult) {
                           if (payResult) {
                              enterRoom(mChoseAdvEntity.uid,
                                      AULiveApplication.getMyselfUserInfo().getUserPhone(), mChoseAdvEntity.url, mChoseAdvEntity.grade);
                           }
                        }
                     });
                  } else {
                     Utils.showCroutonText((Activity) context, "余额不足");
                  }
               } else {
                  enterRoom(mChoseAdvEntity.uid,
                          AULiveApplication.getMyselfUserInfo().getUserPhone(), mChoseAdvEntity.url, mChoseAdvEntity.grade);
               }
               //预加载
               //LiveFragment.doPreLoad(mChoseAdvEntity.url);
            }
         });

         // 设置大小
         int[] size = DragViewUtils.getScreenSize(context);
         android.view.ViewGroup.LayoutParams lp1 = holder.rlayout_deal_image.getLayoutParams();
         lp1.height = size[0] / 3;
         holder.rlayout_deal_image.setLayoutParams(lp1);

         android.view.ViewGroup.LayoutParams lp2 = holder.rlayout_deal_image_two.getLayoutParams();
         lp2.height = size[0] / 3;
         holder.rlayout_deal_image_two.setLayoutParams(lp2);

         android.view.ViewGroup.LayoutParams lp3 =
                 holder.rlayout_deal_image_three.getLayoutParams();
         lp3.height = size[0] / 3;
         holder.rlayout_deal_image_three.setLayoutParams(lp3);

         // 处理第二个
         if (entities.size() > netxt) {
            // 即还有下一个
            final AVEntity second_entity = entities.get(netxt);
            if (second_entity.isEmpty() == true) {
               holder.layout_two.setVisibility(View.INVISIBLE);
            } else {

               ImageLoader.getInstance()
                       .displayImage(second_entity.face, holder.iv_deal_pic_two, options);

               if (second_entity.payliving == 1) {
                  holder.iv_pay_two.setImageResource(R.drawable.live_pay);
               } else {
                  holder.iv_pay_two.setImageResource(R.drawable.live_free);
               }


               holder.tv_title_two.setText(second_entity.nickname);
               holder.layout_two.setOnClickListener(new OnClickListener() {

                  @Override public void onClick(View view) {
                     if (second_entity == null) {
                        return;
                     }
                     mChoseAdvEntity = second_entity;
                     if (mChoseAdvEntity != null && mChoseAdvEntity.uid.equals(
                             AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                        Utils.showCroutonText((Activity) context, "不能加入自己创建的房间");
                        return;
                     }

                     if (second_entity.payliving == 1) {
                        if (AULiveApplication.getUserInfo().diamond > 10) {
                           mDialog.payDialog();
                           mDialog.setDialogListener(new VariousDialog.DialogListener() {
                              @Override
                              public void buttonClick(boolean payResult) {
                                 if (payResult) {
                                    enterRoom(mChoseAdvEntity.uid,
                                            AULiveApplication.getMyselfUserInfo().getUserPhone(), mChoseAdvEntity.url, mChoseAdvEntity.grade);
                                 }
                              }
                           });
                        } else {
                           Utils.showCroutonText((Activity) context, "余额不足");
                        }
                     } else {
                        enterRoom(mChoseAdvEntity.uid,
                                AULiveApplication.getMyselfUserInfo().getUserPhone(), mChoseAdvEntity.url, mChoseAdvEntity.grade);
                     }

                     //预加载
                     //LiveFragment.doPreLoad(mChoseAdvEntity.url);
                  }
               });
            }
         } else {
            holder.layout_two.setVisibility(View.INVISIBLE);
         }

         // 处理第三个
         if (entities.size() > three) {
            // 即还有下一个
            final AVEntity three_entity = entities.get(three);
            if (three_entity.isEmpty() == true) {
               holder.layout_three.setVisibility(View.INVISIBLE);
            } else {

               ImageLoader.getInstance()
                       .displayImage(three_entity.face, holder.iv_deal_pic_three, options);

               if (three_entity.payliving == 1) {
                  holder.iv_pay_three.setImageResource(R.drawable.live_pay);
               } else {
                  holder.iv_pay_three.setImageResource(R.drawable.live_free);
               }

               holder.tv_title_three.setText(three_entity.nickname);
               holder.layout_three.setOnClickListener(new OnClickListener() {

                  @Override public void onClick(View view) {
                     if (three_entity == null) {
                        return;
                     }
                     mChoseAdvEntity = three_entity;
                     if (mChoseAdvEntity != null && mChoseAdvEntity.uid.equals(
                             AULiveApplication.getMyselfUserInfo().getUserPhone())) {
                        Utils.showCroutonText((Activity) context, "不能加入自己创建的房间");
                        return;
                     }

                     if (three_entity.payliving == 1) {
                        if (AULiveApplication.getUserInfo().diamond > 10) {
                           mDialog.payDialog();
                           mDialog.setDialogListener(new VariousDialog.DialogListener() {
                              @Override
                              public void buttonClick(boolean payResult) {
                                 if (payResult) {
                                    enterRoom(mChoseAdvEntity.uid,
                                            AULiveApplication.getMyselfUserInfo().getUserPhone(), mChoseAdvEntity.url, mChoseAdvEntity.grade);
                                 }
                              }
                           });
                        } else {
                           Utils.showCroutonText((Activity) context, "余额不足");
                        }
                     } else {
                        enterRoom(mChoseAdvEntity.uid,
                                AULiveApplication.getMyselfUserInfo().getUserPhone(), mChoseAdvEntity.url, mChoseAdvEntity.grade);
                     }
                     //预加载
                     //LiveFragment.doPreLoad(mChoseAdvEntity.url);
                  }
               });
            }
         } else {
            holder.layout_three.setVisibility(View.INVISIBLE);
         }
      } else {
         // convertView.setVisibility(View.INVISIBLE);
      }

      return convertView;
   }

   class ViewHolder {
      LinearLayout title_layout;
      TextView title_tv;

      LinearLayout layout_one;
      ImageView iv_deal_pic;
      TextView tv_title_one;
      ImageView iv_pay_one;
      RelativeLayout rlayout_deal_image;

      LinearLayout layout_two;
      ImageView iv_deal_pic_two;
      TextView tv_title_two;
      ImageView iv_pay_two;
      RelativeLayout rlayout_deal_image_two;

      LinearLayout layout_three;
      ImageView iv_deal_pic_three;
      TextView tv_title_three;
      ImageView iv_pay_three;
      RelativeLayout rlayout_deal_image_three;
   }

   //进入房间接口
   private void enterRoom(String liveuid, final String userid, final String url,
                          final String grade) {
      RequestInformation request = null;

      AULiveApplication.getMyselfUserInfo().setIsCreater(false);
      LiveListEvent liveListEvent = new LiveListEvent();
      liveListEvent.liveList = entities;
      EventBus.getDefault().postSticky(liveListEvent);

      try {
         StringBuilder sb = new StringBuilder(
                 UrlHelper.enterRoomUrl + "?liveuid=" + liveuid + "&userid=" + userid);
         request = new RequestInformation(sb.toString(), RequestInformation.REQUEST_METHOD_POST);
         request.addPostParams("roomid", liveuid + "");
         request.addPostParams("userid", userid);
      } catch (Exception e) {
         e.printStackTrace();
      }

      request.setCallback(new JsonCallback<EnterRoomEntity>() {

         @Override public void onCallback(EnterRoomEntity callback) {
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {

               MedalListEvent medalListEvent = new MedalListEvent();
               medalListEvent.anchor_medal = callback.anchor_medal;
               medalListEvent.wanjia_medal = callback.wanjia_medal;
               medalListEvent.act= callback.act;
               EventBus.getDefault().postSticky(medalListEvent);

               context.startActivity(
                       new Intent(context, AvActivity.class).putExtra(AvActivity.GET_UID_KEY,
                               mChoseAdvEntity.uid)
                               .putExtra(AvActivity.IS_CREATER_KEY, false)
                               .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_FACE, mChoseAdvEntity.face)
                               .putExtra(AvActivity.EXTRA_SELF_IDENTIFIER_NICKNAME,
                                       mChoseAdvEntity.nickname)
                               .putExtra(AvActivity.EXTRA_RECIVE_DIAMOND, callback.recv_diamond)
                               .putExtra(AvActivity.EXTRA_SYS_MSG, callback.sys_msg)
                               .putExtra(AvActivity.EXTRA_IS_ON_SHOW, callback.is_live)
                               .putExtra(AvActivity.EXTRA_ONLINE_NUM, callback.total)
                               .putExtra(AvActivity.EXTRA_IS_MANAGER, callback.is_manager)
                               .putExtra(AvActivity.EXTRA_IS_GAG, callback.is_gag)
                               .putExtra(AvActivity.EXTRA_IS_SUPER_MANAGER, callback.show_manager)
                               .putExtra(AvActivity.EXTRA_play_url_KEY, url)
                               .putExtra(AvActivity.EXTRA_MSG_SEND_GRADE_CONTROL, callback.sendmsg_grade)
                               .putExtra(AvActivity.GET_GRADE_KEY, grade)
                               .putExtra(AvActivity.EXTRA_LIVE_TITLE_IN, callback.memo)

               );
            } else {
               Utils.showMessage("" + callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            Utils.showMessage(Utils.trans(R.string.get_info_fail));
         }
      }.setReturnType(EnterRoomEntity.class));
      request.execute();
   }
}
