package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;
import com.ilikezhibo.ggzb.avsdk.gift.customized.SketchHelper;
import com.ilikezhibo.ggzb.avsdk.gift.customized.SketchPadView;

/**
 * Created by big on 3/28/16.
 */
public class LuxuryGiftUtil {
   private LayoutInflater inflater;
   private Activity mContext;
   private static LuxuryGiftUtil redPacketesUtil = null;

   //动画的根layout
   private RelativeLayout full_screen_gift_layout;
   public static boolean is_showing_luxury_gift = false;
   private View fullscreen_rainbow;

   public static LuxuryGiftUtil getInstance(Activity avActivity) {

      if (redPacketesUtil == null) {
         redPacketesUtil = new LuxuryGiftUtil(avActivity);
      }

      return redPacketesUtil;
   }

   public static void cleanLuxuryGiftUtil() {

      if (redPacketesUtil != null) {
         redPacketesUtil.fullscreen_car3_down = null;
         redPacketesUtil.fullscreen_car3_up = null;
         redPacketesUtil.fullscreen_ship = null;
         redPacketesUtil.fullscreen_plane = null;
         redPacketesUtil.fullscreen_firework = null;
         redPacketesUtil.mContext = null;
      }
      redPacketesUtil = null;

      is_showing_luxury_gift = false;
   }

   private LuxuryGiftUtil(Activity mContext) {
      this.mContext = mContext;
      inflater = LayoutInflater.from(mContext);
      full_screen_gift_layout = (RelativeLayout) mContext.findViewById(R.id.fullscreen_layout);
   }

   //用到时才加载，减轻内存
   private View fullscreen_car3_down = null;
   private View fullscreen_car3_up = null;

   //船
   private View fullscreen_ship = null;

   private View fullscreen_plane = null;

   private View fullscreen_firework = null;

   private View fullscreen_propose = null;

   private View fullscreen_glass_shoes = null;

   private View fullscreen_crown = null;

   private View fullscreen_521 = null;

   private View fullscreen_wedding_dress = null;
   private View fullscreen_moon = null;
   private View fullscreen_island = null;

   private View fullscreen_custom = null;

   public void showLuxuryGift(SendGiftEntity sendGiftEntity) {

      if (mContext == null) {
         return;
      }
      //是否在做豪华礼物动画
      is_showing_luxury_gift = true;
      //烟花
      if (sendGiftEntity.gift_id == 10) {
         if (fullscreen_firework == null) {
            fullscreen_firework = inflater.inflate(R.layout.fullscreen_firework, null);
            full_screen_gift_layout.addView(fullscreen_firework,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         //烟花动画
         FireWorkAnimation planeDownAnimation =
             new FireWorkAnimation(mContext, mContext.findViewById(R.id.firework_ly),
                 sendGiftEntity);
         planeDownAnimation.startFireWorkAnimation();
      } else if (sendGiftEntity.gift_id == 11) {
         if (fullscreen_car3_down == null) {
            fullscreen_car3_down = inflater.inflate(R.layout.fullscreen_car3_down, null);
            fullscreen_car3_up = inflater.inflate(R.layout.fullscreen_car3_up, null);
            full_screen_gift_layout.addView(fullscreen_car3_down,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            full_screen_gift_layout.addView(fullscreen_car3_up,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         //跑车动画
         Car3DownAnimation car3DownAnimation =
             new Car3DownAnimation(mContext, mContext.findViewById(R.id.car_3_down),
                 sendGiftEntity);
         car3DownAnimation.startCarOnAnimation();
      } else if (sendGiftEntity.gift_id == 12) {
         if (fullscreen_ship == null) {
            fullscreen_ship = inflater.inflate(R.layout.fullscreen_ship, null);
            full_screen_gift_layout.addView(fullscreen_ship,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }

         //游艇
         ShipAnimation shipAnimation =
             new ShipAnimation(mContext, mContext.findViewById(R.id.ship_ly), sendGiftEntity);
         shipAnimation.startCarOnAnimation();
      } else if (sendGiftEntity.gift_id == 13) {
         if (fullscreen_plane == null) {
            fullscreen_plane = inflater.inflate(R.layout.fullscreen_battle_plane, null);
            full_screen_gift_layout.addView(fullscreen_plane,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         //飞机动画
         //PlaneDownAnimation planeDownAnimation =
         //    new PlaneDownAnimation(mContext, mContext.findViewById(R.id.plane_ly), sendGiftEntity);
         //planeDownAnimation.startAnimation();

         BattlePlaneAnimation planeDownAnimation =
             new BattlePlaneAnimation(mContext, mContext.findViewById(R.id.battleplane_ly),
                 sendGiftEntity);
         planeDownAnimation.startBattlePlaneAnimation();
      } else if (sendGiftEntity.gift_id == 14) {
         //城堡
         is_showing_luxury_gift = false;
         if (mContext instanceof AvActivity) {
            AvActivity temp_activity = (AvActivity) mContext;
            //显示礼物动画
            temp_activity.hasAnyLuxuryGift();
         }
      } else if (sendGiftEntity.gift_id == 20) {
         if (fullscreen_propose == null) {
            fullscreen_propose = inflater.inflate(R.layout.fullscreen_propose, null);
            full_screen_gift_layout.addView(fullscreen_propose,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         ProposeAnimation proposeAnimation =
             new ProposeAnimation(mContext, mContext.findViewById(R.id.propose_ly), sendGiftEntity);
         proposeAnimation.startAnimation();
      } else if (sendGiftEntity.gift_id == 27) {
         //水晶鞋
         if (fullscreen_glass_shoes == null) {
            fullscreen_glass_shoes = inflater.inflate(R.layout.fullscreen_glass_shoes, null);
            full_screen_gift_layout.addView(fullscreen_glass_shoes,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         GlassShoseAnimation glassShoseAnimation =
             new GlassShoseAnimation(mContext, mContext.findViewById(R.id.glass_shoes_ly),
                 sendGiftEntity);
         glassShoseAnimation.startAnimation();
      } else if (sendGiftEntity.gift_id == 28) {
         //皇冠
         if (fullscreen_crown == null) {
            fullscreen_crown = inflater.inflate(R.layout.fullscreen_crown, null);
            full_screen_gift_layout.addView(fullscreen_crown,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         CrownAnimation glassShoseAnimation =
             new CrownAnimation(mContext, mContext.findViewById(R.id.crown_ly), sendGiftEntity);
         glassShoseAnimation.startAnimation();
      } else if (sendGiftEntity.gift_id == 29) {
         //512
         if (fullscreen_521 == null) {
            fullscreen_521 = inflater.inflate(R.layout.fullscreen_521, null);
            full_screen_gift_layout.addView(fullscreen_521,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         Five521Animation glassShoseAnimation =
             new Five521Animation(mContext, mContext.findViewById(R.id.f521_ly), sendGiftEntity);
         glassShoseAnimation.startAnimation();
      } else if (sendGiftEntity.gift_id == 30) {
         //wedding dress
         if (fullscreen_wedding_dress == null) {
            fullscreen_wedding_dress = inflater.inflate(R.layout.fullscreen_wedding_dress, null);
            full_screen_gift_layout.addView(fullscreen_wedding_dress,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         WeddingDressAnimation glassShoseAnimation =
             new WeddingDressAnimation(mContext, mContext.findViewById(R.id.wedding_dress_ly),
                 sendGiftEntity);
         glassShoseAnimation.startAnimation();
      } else if (sendGiftEntity.gift_id == 33) {
         //wedding dress
         if (fullscreen_moon == null) {
            fullscreen_moon = inflater.inflate(R.layout.fullscreen_moon, null);
            full_screen_gift_layout.addView(fullscreen_moon,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         MoonAnimation moonAnimation =
             new MoonAnimation(mContext, mContext.findViewById(R.id.moon_ly), sendGiftEntity);
         moonAnimation.startAnimation();
      } else if (sendGiftEntity.gift_id == 340) {
         //fullscreen_island
         if (fullscreen_island == null) {
            fullscreen_island = inflater.inflate(R.layout.fullscreen_island, null);
            full_screen_gift_layout.addView(fullscreen_island,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         IslandAnimation islandAnimation =
             new IslandAnimation(mContext, mContext.findViewById(R.id.island_ly), sendGiftEntity);
         islandAnimation.startAnimation();
      } else if (sendGiftEntity.gift_id == 401) {
         // 梦幻城堡礼物
         if (fullscreen_rainbow == null) {
            fullscreen_rainbow = inflater.inflate(R.layout.fullscreen_rainbow, null);
            full_screen_gift_layout.addView(fullscreen_rainbow,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
         }
         RainbowCastleAnimation rainbowCastleAnimation =
             new RainbowCastleAnimation(mContext, mContext.findViewById(R.id.rl_anim_find),
                 sendGiftEntity);
         rainbowCastleAnimation.startAnimation();
      } else if (sendGiftEntity.gift_id == 1000) {
         // 个性礼物
         if (sendGiftEntity.paint != null) {

            if (fullscreen_custom == null) {
               fullscreen_custom = inflater.inflate(R.layout.custom_gift_show_view_layout, null);
               full_screen_gift_layout.addView(fullscreen_custom,
                   RelativeLayout.LayoutParams.MATCH_PARENT,
                   RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            fullscreen_custom.setVisibility(View.VISIBLE);

            TextView room_gift_car_one_send_person =
                (TextView) fullscreen_custom.findViewById(R.id.room_gift_car_one_send_person);
            room_gift_car_one_send_person.setText(sendGiftEntity.nickname + "");

            final SketchPadView sketchPadGiftView =
                (SketchPadView) fullscreen_custom.findViewById(R.id.sketchpadview_show);
            sketchPadGiftView.sketchHelper.refleshInterface = new SketchHelper.RefleshInterface() {
               @Override
               public void onreflsesh() {
                  sketchPadGiftView.invalidate();
               }
            };
            sketchPadGiftView.getPathPointsAndDraw(sendGiftEntity.paint, fullscreen_custom,
                mContext);
         }
      } else {
         //没找到的
         is_showing_luxury_gift = false;
         if (mContext instanceof AvActivity) {
            AvActivity temp_activity = (AvActivity) mContext;
            //显示礼物动画
            temp_activity.hasAnyLuxuryGift();
         }
      }
   }
}
