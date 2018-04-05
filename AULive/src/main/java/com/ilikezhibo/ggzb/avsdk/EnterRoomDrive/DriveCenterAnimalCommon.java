package com.ilikezhibo.ggzb.avsdk.EnterRoomDrive;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;

/**
 * 作者：jacklong on 16/9/20 21:40
 * <p/>
 * des: 中心进入
 */
public class DriveCenterAnimalCommon {


    private int runResId;// 座驾跑的资源id
    private int stopingResId;// 停止中的资源id
    private int stopResId;// 停止资源id
    private int waveResId;// 挥手资源id

    private AvActivity context;
    private ImageView runImgView = null;
    private ImageView lightImg = null;
    private RelativeLayout animRl = null;

    private AnimationDrawable animationDrawable;
    private AnimationDrawable lightAnimDrawable;

    public DriveCenterAnimalCommon(AvActivity context, View rootView, String nickName, int runResId, int stopingResId, int stopResId, int waveResId) {
        this.context = context;

        this.runResId = runResId;
        this.stopingResId = stopingResId;
        this.stopResId = stopResId;
        this.waveResId = waveResId;

        runImgView = (ImageView) rootView.findViewById(R.id.run_img);
        runImgView.setImageResource(this.runResId);
        animationDrawable = (AnimationDrawable) runImgView.getDrawable();
        lightImg = (ImageView) rootView.findViewById(R.id.drive_light_img);
        lightAnimDrawable = (AnimationDrawable)lightImg.getDrawable();
        animRl = (RelativeLayout) rootView.findViewById(R.id.drive_run_rl);

        TextView room_gift_car_one_send_person =
                (TextView) rootView.findViewById(R.id.nicknameTv);
        room_gift_car_one_send_person.setText(nickName);
    }

    public void startAnimalOnAnimation() {
        animationDrawable.start();

        //动画阶段1
        final Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.drive_enter_anim1);
        animation1.getFillAfter();
        final Animation animation2 =
                AnimationUtils.loadAnimation(context, R.anim.drive_enter_anim2);
        animation2.getFillAfter();
        final Animation animation3 =
                AnimationUtils.loadAnimation(context, R.anim.drive_enter_anim3);
        animation3.getFillAfter();
        final Animation animation4 =
                AnimationUtils.loadAnimation(context, R.anim.drive_enter_anim4);
        animation4.getFillAfter();
        final Animation animation5 =
                AnimationUtils.loadAnimation(context, R.anim.drive_enter_anim5);
        animation5.getFillAfter();

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
                animRl.setVisibility(View.VISIBLE);
                runImgView.setVisibility(View.VISIBLE);
            }

            @Override public void onAnimationEnd(Animation animation) {
                animationDrawable.stop();
                if (stopingResId == 0){
                    runImgView.setImageResource(stopResId);
                } else {
                    runImgView.setImageResource(stopingResId);
                }
                animationDrawable = (AnimationDrawable) runImgView.getDrawable();
                animationDrawable.start();
                animRl.startAnimation(animation2);
            }

            @Override public void onAnimationRepeat(Animation animation) {

            }
        });
        animation1.setInterpolator(new LinearInterpolator());
        animRl.startAnimation(animation1);

        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationDrawable.stop();
                runImgView.setImageResource(stopResId);
                animationDrawable = (AnimationDrawable) runImgView.getDrawable();
                animationDrawable.start();
                animRl.startAnimation(animation3);

                lightImg.setVisibility(View.VISIBLE);
                lightAnimDrawable.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationDrawable.stop();
                runImgView.setImageResource(waveResId);
                animationDrawable = (AnimationDrawable) runImgView.getDrawable();
                animationDrawable.start();
                animRl.startAnimation(animation4);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        animation4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runImgView.setImageResource(runResId);
                animationDrawable.stop();
                animationDrawable = (AnimationDrawable) runImgView.getDrawable();
                animationDrawable.start();
                animRl.startAnimation( animation5);
                lightAnimDrawable.stop();
                lightImg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animRl.setVisibility(View.GONE);
                animationDrawable.stop();
                UserDriveUtil.is_showing_drive = false;

                UserDriveUtil.getInstance(context).hasAnyDrive();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
