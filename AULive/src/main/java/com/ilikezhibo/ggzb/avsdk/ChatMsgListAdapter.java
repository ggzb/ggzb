package com.ilikezhibo.ggzb.avsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jack.utils.UrlHelper;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.MedalLayoutHelper;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.ChatMsgEntity;
import com.ilikezhibo.ggzb.avsdk.gift.RedPacketesUtil;
import com.ilikezhibo.ggzb.avsdk.userinfo.UserInfoHelper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import popwindow.PopupWindowUtil;
import tinker.android.util.TinkerManager;
import tyrantgit.widget.HeartLayout;

public class ChatMsgListAdapter extends BaseAdapter {

   private static String TAG = ChatMsgListAdapter.class.getSimpleName();
   private static final int ITEMCOUNT = 9;
   private List<ChatEntity> listMessage = null;
   private LayoutInflater inflater;
   private LinearLayout layout;
   public static final int TYPE_TEXT_SEND = 0;
   public static final int TYPE_TEXT_RECV = 1;
   private static AvActivity context;
   private Timer mTimer;
   private TimerTask mTimerTask;
   private ArrayList<MemberInfo> mMemberList;
   private UserInfo myselfInfo;
   private ViewHolder holder;
   private AULiveApplication mAULiveApplication;
   private Animation slide_bottom_to_top;

   public ChatMsgListAdapter(AvActivity context, List<ChatEntity> objects,
       ArrayList<MemberInfo> memberList, UserInfo myself) {
      this.context = context;
      inflater = LayoutInflater.from(context);
      this.listMessage = objects;
      mMemberList = memberList;
      myselfInfo = myself;

      slide_bottom_to_top = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_to_top);
   }

   @Override public int getCount() {
      return listMessage.size();
   }

   @Override public Object getItem(int position) {
      return listMessage.get(position);
   }

   @Override public long getItemId(int position) {
      return position;
   }

   //@Override public int getItemViewType(int position) {
   //   ChatEntity entity = listMessage.get(position);
   //   return entity.getChatMsgEntity().uid.equals(myselfInfo.getUserPhone()) ? TYPE_TEXT_SEND
   //       : TYPE_TEXT_RECV;
   //}

   @Override public View getView(int position, View convertView, ViewGroup parent) {
      if (position > listMessage.size()) {
         position = 0;
      }
      final ChatEntity entity = listMessage.get(position);

      if (convertView == null) {
         convertView = (LinearLayout) inflater.inflate(R.layout.chat_item_left, null);
         convertView.setBackgroundResource(R.drawable.bg_btn_room_chat_bg);
         holder = new ViewHolder();
         holder.text = (TextView) convertView.findViewById(R.id.tv_chatcontent);
         //holder.icon = (CircularImageButton) convertView.findViewById(R.id.tv_chat_head_image);
         holder.user_name = (TextView) convertView.findViewById(R.id.tv_username);
         holder.img_red_packet = (ImageView) convertView.findViewById(R.id.img_red_packet);
         holder.tv_grade = (TextView) convertView.findViewById(R.id.grade_tv);
         holder.grade_ly = (RelativeLayout) convertView.findViewById(R.id.grade_ly);
         holder.offical_tag_iv = (ImageView) convertView.findViewById(R.id.offical_tag_iv);
         holder.chat_medal_ly = (LinearLayout) convertView.findViewById(R.id.chat_medal_ly);
         convertView.setTag(holder);

//         slide_bottom_to_top.setDuration(1000);
//         convertView.setAnimation(slide_bottom_to_top);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }

      mAULiveApplication = (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      if (entity.getChatMsgEntity().type.equals("system")) {
         //处理系统消息显示
         holder.offical_tag_iv.setVisibility(View.GONE);
         holder.grade_ly.setVisibility(View.GONE);
         holder.user_name.setText(entity.getChatMsgEntity().nickname + ":");
         holder.text.setText(entity.getChatMsgEntity().chat_msg);
         holder.text.setTextColor(
             AULiveApplication.mContext.getResources().getColor(R.color.global_main_bg));
         //去红包图片
         holder.text.setVisibility(View.VISIBLE);
         holder.img_red_packet.setVisibility(View.GONE);
      } else if (entity.getChatMsgEntity().type.equals("gift")
          && (entity.getChatMsgEntity().gift_type == 1
          || entity.getChatMsgEntity().gift_type == 3)) {
         //处理普通礼物与豪华礼物消息显示
         holder.grade_ly.setVisibility(View.VISIBLE);
         holder.user_name.setText(entity.getChatMsgEntity().nickname + ":");
         holder.text.setText(entity.getChatMsgEntity().chat_msg);
         holder.text.setTextColor(
             AULiveApplication.mContext.getResources().getColor(R.color.global_main_bg));

         String grade = entity.getChatMsgEntity().grade;
         setGradeIcon(grade, holder.tv_grade, holder.grade_ly);
         //去红包图片
         holder.text.setVisibility(View.VISIBLE);
         holder.img_red_packet.setVisibility(View.GONE);
      } else if (entity.getChatMsgEntity().type.equals("gift")
          && entity.getChatMsgEntity().gift_type == 2) {
         //处理红包消息显示
         holder.grade_ly.setVisibility(View.VISIBLE);
         holder.user_name.setText(entity.getChatMsgEntity().nickname + ":");
         String grade = entity.getChatMsgEntity().grade;
         setGradeIcon(grade, holder.tv_grade, holder.grade_ly);

         //String imgname = "rank_" + grade;
         //int imgid =
         //    context.getResources().getIdentifier(imgname, "drawable", context.getPackageName());
         //holder.tv_grade.setImageResource(imgid);

         holder.text.setText(entity.getChatMsgEntity().chat_msg);
         holder.text.setTextColor(
             AULiveApplication.mContext.getResources().getColor(R.color.global_main_bg));
         holder.text.setVisibility(View.GONE);
         holder.img_red_packet.setVisibility(View.VISIBLE);
         //点击红包
         holder.img_red_packet.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
               RedPacketesUtil.getInstance(context, entity.getChatMsgEntity().uid,
                   entity.getChatMsgEntity().nickname, entity.getChatMsgEntity().face,
                   context.qav_top_bar_new)
                   .grabRedBagPopUpWindows(entity.getChatMsgEntity().packetid);
            }
         });
      } else if (entity.getChatMsgEntity().type.equals("chat")
          || entity.getChatMsgEntity().type.equals("love")|| entity.getChatMsgEntity().type.equals(AvActivity.ENTER_ROOM)) {
         String url = entity.getChatMsgEntity().face;
         if (url != null && !url.startsWith("http")) {
            url = UrlHelper.SERVER_URL + url;
         }
         //ImageLoader.getInstance()
         //    .displayImage(url, holder.icon, AULiveApplication.getGlobalImgOptions());

         holder.grade_ly.setVisibility(View.VISIBLE);
         holder.text.setText(entity.getChatMsgEntity().chat_msg);

         holder.text.setTextColor(
             AULiveApplication.mContext.getResources().getColor(R.color.white));
         holder.user_name.setText(entity.getChatMsgEntity().nickname + ":");
         //设置等级
         //holder.tv_grade.

         //去红包图片
         holder.text.setVisibility(View.VISIBLE);
         holder.img_red_packet.setVisibility(View.GONE);

         String grade = entity.getChatMsgEntity().grade;
         setGradeIcon(grade, holder.tv_grade, holder.grade_ly);

         //如果是点赞，特殊处理一下
         if (entity.getChatMsgEntity().type.equals("love")) {

            Drawable drawable =
                context.getResources().getDrawable(HeartLayout.pics_like[new Random().nextInt(3)]);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2,
                drawable.getIntrinsicHeight() / 2);
            String chat_msg = entity.getChatMsgEntity().chat_msg;
            //需要处理的文本，[smile]是需要被替代的文本
            SpannableString spannable = new SpannableString(chat_msg.toString() + "[smile]");
            //要让图片替代指定的文字就要用ImageSpan
            VerticalImageSpan span = new VerticalImageSpan(drawable);
            //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
            //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
            spannable.setSpan(span, chat_msg.length(), chat_msg.length() + "[smile]".length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            holder.text.setText(spannable);
         }
      }

      //官方图标显示
      if (!entity.getChatMsgEntity().type.equals("system")) {
         if (entity.getChatMsgEntity().offical == 1) {
            holder.offical_tag_iv.setVisibility(View.VISIBLE);
            holder.grade_ly.setVisibility(View.GONE);
         } else {
            holder.offical_tag_iv.setVisibility(View.GONE);
            holder.grade_ly.setVisibility(View.VISIBLE);
         }
      }

      //如果是进房消息,换红色,隐藏等级
      if(entity.getChatMsgEntity().type.equals(AvActivity.ENTER_ROOM)){
         holder.text.setTextColor(
             AULiveApplication.mContext.getResources().getColor(R.color.global_main_bg));
         holder.user_name.setText("直播消息:");
         holder.grade_ly.setVisibility(View.GONE);
      }

      //勋章的显示
      doMedalShow(entity.getChatMsgEntity(), holder.chat_medal_ly);


      View.OnClickListener onClickListener = new View.OnClickListener() {
         @Override public void onClick(View view) {
            if (entity.getChatMsgEntity().uid == null || entity.getChatMsgEntity().uid.equals("")) {
               return;
            }
            if (popupWindow == null) {
               popupWindow = new PopupWindowUtil(holder.text);
               popupWindow.setContentView(R.layout.dialog_myroom_userinfo);
               popupWindow.setOutsideTouchable(true);

               popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                  @Override public void onDismiss() {
                  }
               });
            }
            MemberInfo memberInfo =
                new MemberInfo(entity.getChatMsgEntity().uid, entity.getChatMsgEntity().nickname,
                    entity.getChatMsgEntity().face, entity.getChatMsgEntity().grade);
            //做View init
            UserInfoHelper userInfoHelper =
                UserInfoHelper.getInstance(holder.text, popupWindow, context, memberInfo);
         }
      };
      holder.tv_grade.setOnClickListener(onClickListener);
      holder.text.setOnClickListener(onClickListener);
      holder.user_name.setOnClickListener(onClickListener);
      return convertView;
   }

   private PopupWindowUtil popupWindow;

   static class ViewHolder {
      public TextView text;
      public TextView user_name;
      public TextView tv_grade;
      public RelativeLayout grade_ly;
      //public CircularImageButton icon;
      public ImageView img_red_packet;
      public ImageView offical_tag_iv;
      public LinearLayout chat_medal_ly;
   }

   //垂直居中的ImageSpan
   //@author KenChung
   public class VerticalImageSpan extends ImageSpan {

      public VerticalImageSpan(Drawable drawable) {
         super(drawable);
      }

      public int getSize(Paint paint, CharSequence text, int start, int end,
          Paint.FontMetricsInt fontMetricsInt) {
         Drawable drawable = getDrawable();
         Rect rect = drawable.getBounds();
         if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
         }
         return rect.right;
      }

      @Override
      public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top,
          int y, int bottom, Paint paint) {
         Drawable drawable = getDrawable();
         canvas.save();
         int transY = 0;
         transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
         canvas.translate(x, transY);
         drawable.draw(canvas);
         canvas.restore();
      }
   }

   public static void setGradeIcon(String grade1, TextView tv, RelativeLayout rl) {
      if (grade1 == null || grade1.equals("")) {
         grade1 = "1";
      }
      int grade = Integer.parseInt(grade1);
      int res;
      int each_grade = 16;
      if (grade < each_grade) {
         res = R.drawable.rank_1;
      } else if (grade < 2 * each_grade && grade >= each_grade) {
         res = R.drawable.rank_2;
      } else if (grade < 3 * each_grade && grade >= 2 * each_grade) {
         res = R.drawable.rank_3;
      } else if (grade < 4 * each_grade && grade >= 3 * each_grade) {
         res = R.drawable.rank_4;
      } else if (grade < 5 * each_grade && grade >= 4 * each_grade) {
         res = R.drawable.rank_5;
      } else if (grade < 6 * each_grade && grade >= 5 * each_grade) {
         res = R.drawable.rank_6;
      } else if (grade < 7 * each_grade && grade >= 6 * each_grade) {
         res = R.drawable.rank_7;
      } else if (grade < 8 * each_grade && grade >= 7 * each_grade) {
         res = R.drawable.rank_8;
      } else if (grade < 9 * each_grade && grade >= 8 * each_grade) {
         res = R.drawable.rank_9;
      } else if (grade < 10 * each_grade && grade >= 9 * each_grade) {
         res = R.drawable.rank_10;
      } else if (grade < 11 * each_grade && grade >= 10 * each_grade) {
         res = R.drawable.rank_11;
      } else if (grade < 12 * each_grade && grade >= 11 * each_grade) {
         res = R.drawable.rank_12;
      } else {
         res = R.drawable.rank_13;
      }

      if (grade == -100) {
         res = R.drawable.official_tag_chat;
      }

      rl.setBackgroundResource(res);
      //if (context != null) {
      //   Bitmap bitmap = readBitMap(context, res);
      //   rl.setBackgroundDrawable(new BitmapDrawable(bitmap));
      //}
      tv.setText(grade + "");
   }

   //public static void setOfficalIcon(String grade1, ImageView tv) {
   //   if (grade1 == null || grade1.equals("")) {
   //      grade1 = "1";
   //   }
   //   int grade = Integer.parseInt(grade1);
   //   int res = 0;
   //   if (grade == -100) {
   //      res = R.drawable.official_tag_chat;
   //   }
   //
   //   tv.setBackgroundResource(res);
   //}

   /**
    * 以最省内存的方式读取本地资源的图片
    */
   public static Bitmap readBitMap(Context context, int resId) {
      BitmapFactory.Options opt = new BitmapFactory.Options();
      opt.inPreferredConfig = Bitmap.Config.RGB_565;
      opt.inPurgeable = true;
      opt.inInputShareable = true;
// 获取资源图片
      InputStream is = context.getResources().openRawResource(resId);
      return BitmapFactory.decodeStream(is, null, opt);
   }

   public void doMedalShow(ChatMsgEntity chatMsgEntity, LinearLayout chat_medal_ly) {
      ArrayList<String> tem_urls = new ArrayList<String>();
      ////先添加主播勋章
      //if (chatMsgEntity.anchor_medal != null) {
      //   tem_urls.addAll(chatMsgEntity.anchor_medal);
      //}
      //再添加玩家勋章
      if (chatMsgEntity.wanjia_medal != null) {
         tem_urls.addAll(chatMsgEntity.wanjia_medal);
      }
      //把所有的子view隐藏
      for (int i = 0; i < chat_medal_ly.getChildCount(); i++) {
         chat_medal_ly.getChildAt(i).setVisibility(View.GONE);
      }

      for (int i = 0; i < chat_medal_ly.getChildCount() && i < tem_urls.size(); i++) {
         String url = tem_urls.get(i);
         MedalLayoutHelper.showGifImage(url, context, chat_medal_ly.getChildAt(i));
      }
   }
}
