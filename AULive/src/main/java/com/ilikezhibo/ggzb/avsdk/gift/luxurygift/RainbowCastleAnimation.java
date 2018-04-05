package com.ilikezhibo.ggzb.avsdk.gift.luxurygift;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.SendGiftEntity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;


/**
 * 彩虹城堡
 * Created by r on 16/10/24.
 */

public class RainbowCastleAnimation {


    private Activity context;
    ImageView castleFlowerBottom = null;
    ImageView castleCloudTop0 = null;
    ImageView castleCloudTop1 = null;
    ImageView castleHotAirBalloon1 = null;
    ImageView castleHotAirBalloon2 = null;
    ImageView castleHotAirBalloon3 = null;
    ImageView castleCloudMiddle0 = null;
    ImageView castleCloudMiddle1 = null;
    ImageView castleXmlPurple2 = null;
    ImageView castleXmlGreen1 = null;
    ImageView castleXmlGreen3 = null;
    ImageView castle = null;
    ImageView castleCloudMiddle2 = null;
    ImageView castleXmlPurple4 = null;
    ImageView castleCloudBottom0 = null;
    ImageView castleBlue = null;
    ImageView castleCloudBottom1 = null;
    ImageView castlePurple = null;
    ImageView castleRainbow0 = null;
    ImageView castleBalloon0 = null;
    ImageView castleBalloon1 = null;
    ImageView castleBalloon2 = null;
    ImageView castleBalloon3 = null;
    ImageView castleBalloon4 = null;
    ImageView castleHotAirBalloon0 = null;
    ImageView castleRainbow1 = null;
    RelativeLayout rlAnimFind = null;
    RelativeLayout rlAnimContain = null;
    FrameLayout anim_rainbow = null;
    private View view;
    private int windowWidth;
    private int windowHeight;
    private Handler handler = new Handler();
    private long frameDelay = 1500;
    private long frameFlowerDelay = 120 * 15;
    private long middleCloudDuration = 1500;
    private long ANIM_TIME;
    private TranslateAnimation ta_goright;
    private TranslateAnimation ta_goleft;
    private AnimatorSet set;


    public RainbowCastleAnimation(Activity context, View viewById, SendGiftEntity sendGiftEntity1) {
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowWidth = wm.getDefaultDisplay().getWidth();
        windowHeight = wm.getDefaultDisplay().getHeight();
        anim_rainbow = (FrameLayout) viewById.findViewById(R.id.anim_rainbow);
        castleFlowerBottom = (ImageView) viewById.findViewById(R.id.castle_flower_bottom);
        castleCloudTop0 = (ImageView) viewById.findViewById(R.id.castle_cloud_top0);
        castleCloudTop1 = (ImageView) viewById.findViewById(R.id.castle_cloud_top1);
        castleHotAirBalloon1 = (ImageView) viewById.findViewById(R.id.castle_hot_air_balloon1);
        castleHotAirBalloon2 = (ImageView) viewById.findViewById(R.id.castle_hot_air_balloon2);
        castleHotAirBalloon3 = (ImageView) viewById.findViewById(R.id.castle_hot_air_balloon3);
        castleCloudMiddle0 = (ImageView) viewById.findViewById(R.id.castle_cloud_middle0);
        castleCloudMiddle1 = (ImageView) viewById.findViewById(R.id.castle_cloud_middle1);
        castleXmlPurple2 = (ImageView) viewById.findViewById(R.id.castle_xml_purple2);
        castleXmlGreen1 = (ImageView) viewById.findViewById(R.id.castle_xml_green1);
        castleXmlGreen3 = (ImageView) viewById.findViewById(R.id.castle_xml_green3);
        castle = (ImageView) viewById.findViewById(R.id.castle);
        castleCloudMiddle2 = (ImageView) viewById.findViewById(R.id.castle_cloud_middle2);
        castleXmlPurple4 = (ImageView) viewById.findViewById(R.id.castle_xml_purple4);
        castleCloudBottom0 = (ImageView) viewById.findViewById(R.id.castle_cloud_bottom0);
        castleBlue = (ImageView) viewById.findViewById(R.id.castle_blue);
        castleCloudBottom1 = (ImageView) viewById.findViewById(R.id.castle_cloud_bottom1);
        castlePurple = (ImageView) viewById.findViewById(R.id.castle_purple);
        castleRainbow0 = (ImageView) viewById.findViewById(R.id.castle_rainbow0);
        castleBalloon0 = (ImageView) viewById.findViewById(R.id.castle_balloon0);
        castleBalloon1 = (ImageView) viewById.findViewById(R.id.castle_balloon1);
        castleBalloon2 = (ImageView) viewById.findViewById(R.id.castle_balloon2);
        castleBalloon3 = (ImageView) viewById.findViewById(R.id.castle_balloon3);
        castleBalloon4 = (ImageView) viewById.findViewById(R.id.castle_balloon4);
        castleHotAirBalloon0 = (ImageView) viewById.findViewById(R.id.castle_hot_air_balloon0);
        castleRainbow1 = (ImageView) viewById.findViewById(R.id.castle_rainbow1);
        rlAnimFind = (RelativeLayout) viewById.findViewById(R.id.rl_anim_find);
        rlAnimContain = (RelativeLayout) viewById.findViewById(R.id.rl_anim_contain);

        TextView room_gift_car_one_send_person =
                (TextView) viewById.findViewById(R.id.room_gift_car_one_send_person);
        room_gift_car_one_send_person.setText(sendGiftEntity1.nickname + "");

    }

    public void startAnimation() {
        rlAnimFind.setVisibility(View.VISIBLE);

        // >balloon上升动画
        balloonUpAnim();

        // >四个花环帧动画
        flowerFrameAnim();

        // >中间云朵渐显后短合并动画
        middleCloudAlphaAnim();

        // >中间热气球上升动画
        hotAirBalloon0Anim();

    }

    /**
     * 中部小彩虹半显
     */
    private void middleRainbowAnim() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ta_goright = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f);
                ta_goright.setDuration(1000);
                ta_goleft = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, -1.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f);
                ta_goleft.setDuration(1000);
                ta_goleft.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        castleRainbow1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ViewAnimator.animate(rlAnimContain)
                                .startDelay(1500)
                                .alpha(1f, 0f)
                                .duration(800)
                                .onStop(new AnimationListener.Stop() {
                                    @Override
                                    public void onStop() {
                                        invisible();
                                        rlAnimFind.setVisibility(view.GONE);
                                        LuxuryGiftUtil.is_showing_luxury_gift = false;
                                        if (context instanceof AvActivity) {
                                            AvActivity temp_activity = (AvActivity) context;
                                            //显示礼物动画
                                            temp_activity.hasAnyLuxuryGift();
                                        }
                                    }
                                })
                                .start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                anim_rainbow.startAnimation(ta_goright);
                castleRainbow1.startAnimation(ta_goleft);
               /* ObjectAnimAlpha(castleRainbow1, 1000, 4000, new ObjectAnimListener() {
                    @Override
                    void onAnimStart() {
                        castleRainbow1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    void onAnimStop() {

                    }
                });*/
            }
        };
        handler.postDelayed(runnable, 6000);
    }

    private void invisible() {
        castleFlowerBottom.setVisibility(View.INVISIBLE);
        castleCloudTop0.setVisibility(View.INVISIBLE);
        castleCloudTop1.setVisibility(View.INVISIBLE);
        castleHotAirBalloon1.setVisibility(View.INVISIBLE);
        castleHotAirBalloon2.setVisibility(View.INVISIBLE);
        castleHotAirBalloon3.setVisibility(View.INVISIBLE);
        castleCloudMiddle0.setVisibility(View.INVISIBLE);
        castleCloudMiddle1.setVisibility(View.INVISIBLE);
        castleXmlPurple2.setVisibility(View.INVISIBLE);
        castleXmlGreen1.setVisibility(View.INVISIBLE);
        castleXmlGreen3.setVisibility(View.INVISIBLE);
        castle.setVisibility(View.INVISIBLE);
        castleCloudMiddle2.setVisibility(View.INVISIBLE);
        castleXmlPurple4.setVisibility(View.INVISIBLE);
        castleCloudBottom0.setVisibility(View.INVISIBLE);
        castleBlue.setVisibility(View.INVISIBLE);
        castleCloudBottom1.setVisibility(View.INVISIBLE);
        castlePurple.setVisibility(View.INVISIBLE);
        castleRainbow0.setVisibility(View.INVISIBLE);
        castleBalloon0.setVisibility(View.INVISIBLE);
        castleBalloon1.setVisibility(View.INVISIBLE);
        castleBalloon2.setVisibility(View.INVISIBLE);
        castleBalloon3.setVisibility(View.INVISIBLE);
        castleBalloon4.setVisibility(View.INVISIBLE);
        castleHotAirBalloon0.setVisibility(View.INVISIBLE);
        castleRainbow1.setVisibility(View.INVISIBLE);
        ViewAnimator.animate(castleCloudTop0)
                .dp()
                .translationX(0)
                .duration(100)
                .start();

        ViewAnimator.animate(castleCloudTop1)
                .dp()
                .translationX(0)
                .duration(100)
                .start();

        ViewAnimator.animate(castleCloudMiddle0)
                .dp()
                .translationX(0)
                .interpolator(new LinearInterpolator())
                .duration(100)
                .start();
        ViewAnimator.animate(castleCloudMiddle1)
                .dp()
                .translationX(0)
                .interpolator(new LinearInterpolator())
                .duration(100)
                .start();
        ViewAnimator.animate(castleCloudMiddle2)
                .dp()
                .translationX(0)
                .interpolator(new LinearInterpolator())
                .duration(100)
                .start();

        ta_goleft.cancel();
        ta_goright.cancel();
        set.cancel();
    }

    /**
     * 小热气球飘过
     */
    private void hotAirAnim() {
        final ImageView[] imageViewIds = {castleHotAirBalloon3, castleHotAirBalloon1, castleHotAirBalloon2};
        final Runnable runnable = new Runnable() {
            int count;
            int speed = (int) (windowWidth / 1.7);

            @Override
            public void run() {
                final ImageView imageView = imageViewIds[count];
                imageView.measure(0, 0);
                final int measuredWidth = imageView.getMeasuredWidth();
                Log.e("BNDG", ">>>> " + measuredWidth);
                radian(imageView, speed, measuredWidth, new ObjectAnimListener() {
                    @Override
                    void onAnimStart() {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }, count);
                count++;
                if (count < imageViewIds.length) {
                    handler.postDelayed(this, 2200);
                } else {
                    middleRainbowAnim();
                }
            }
        };
        handler.postDelayed(runnable, 500);
    }

    /**
     * 上方彩虹渐显
     */
    private void topRainbowAnim() {
        ViewAnimator.animate(castleRainbow0)
                .alpha(0f, 1f)
                .duration(1000)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        castleRainbow0.setVisibility(View.VISIBLE);
                    }
                }).start();

    }

    /**
     * 城堡底部莲花推出
     */
    private void castleBottomFlowerAnim() {
        final ImageView[] imageViews = {castlePurple, castleBlue};


        Runnable runnable = new Runnable() {
            int count;

            @Override
            public void run() {
                final ImageView iv = imageViews[count];
                ObjectAnimAlpha(iv, 1500, 0, new ObjectAnimListener());
                ObjectAnimTrans(iv, 0, 0, 0, -10, 1500, new ObjectAnimListener() {
                    @Override
                    void onAnimStart() {
                        iv.setVisibility(View.VISIBLE);
                    }
                });
                count++;
                if (count < imageViews.length) {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnable);
    }

    /**
     * 屏幕底部蓝莲花推出
     */
    private void bottomFlowerAnim() {
        castleFlowerBottom.measure(0, 0);
        int measuredHeight = castleFlowerBottom.getMeasuredHeight();
        ObjectAnimTrans(castleFlowerBottom, 0, 0, measuredHeight, 0, 2500, new ObjectAnimListener() {
            @Override
            void onAnimStart() {
                castleFlowerBottom.setVisibility(View.VISIBLE);
            }

            @Override
            void onAnimStop() {
                // >城堡底部莲花推
                castleBottomFlowerAnim();
                // >上方彩虹渐显
                topRainbowAnim();
                // >小热气球飘过
                hotAirAnim();
            }
        });
    }

    /**
     * 上方云朵显示
     */
    private void topCloudAnim() {
        ViewAnimator.animate(castleCloudTop0, castleCloudTop1)
                .alpha(0f, 1f)
                .duration(2000)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        castleCloudTop0.setVisibility(View.VISIBLE);
                        castleCloudTop1.setVisibility(View.VISIBLE);
                    }
                }).start();
        ViewAnimator.animate(castleCloudTop0)
                .dp()
                .translationX(0, -10)
                .duration(10000)
                .start();

        ViewAnimator.animate(castleCloudTop1)
                .dp()
                .translationX(0, 10)
                .duration(10000)
                .start();


    }

    /**
     * 城堡渐显动画
     */
    private void castleAlphaAnim() {
        ViewAnimator.animate(castle)
                .alpha(0f, 1f)
                .duration(1500)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        castle.setVisibility(View.VISIBLE);
                        // >底部蓝莲花推出
                        bottomFlowerAnim();
                    }
                }).start();
    }

    /**
     * 下方云朵合并动画
     */
    private void bottomCloudAnim() {
        castleCloudBottom0.measure(0, 0);
        castleCloudBottom1.measure(0, 0);
        int bottom0Width = castleCloudBottom0.getMeasuredWidth();
        int bottom1Width = castleCloudBottom1.getMeasuredWidth();
        ObjectAnimTrans(castleCloudBottom0, -bottom0Width, 0, 0, 0, 800, new ObjectAnimListener() {
            @Override
            void onAnimStart() {
                castleCloudBottom0.setVisibility(View.VISIBLE);
            }

            @Override
            void onAnimStop() {
                // >城堡渐显
                castleAlphaAnim();
            }
        });

        ObjectAnimTrans(castleCloudBottom1, bottom1Width, 0, 0, 0, 800, new ObjectAnimListener() {
            @Override
            void onAnimStart() {
                castleCloudBottom1.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 中间云渐显动画
     */
    private void middleCloudAlphaAnim() {
        long delay = 3500;
        ObjectAnimAlpha(castleCloudMiddle0, middleCloudDuration, delay, new ObjectAnimListener() {
            @Override
            void onAnimStart() {
                castleCloudMiddle0.setVisibility(View.VISIBLE);
            }
        });
        ObjectAnimAlpha(castleCloudMiddle1, middleCloudDuration, delay, new ObjectAnimListener() {
            @Override
            void onAnimStart() {
                castleCloudMiddle1.setVisibility(View.VISIBLE);
            }

            @Override
            void onAnimStop() {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        ViewAnimator.animate(castleCloudMiddle0)
                                .dp()
                                .translationX(0, 40)
                                .interpolator(new LinearInterpolator())
                                .duration(7500)
                                .start();
                        ViewAnimator.animate(castleCloudMiddle1)
                                .dp()
                                .translationX(0, 40)
                                .interpolator(new LinearInterpolator())
                                .duration(7500)
                                .start();
                        ViewAnimator.animate(castleCloudMiddle2)
                                .dp()
                                .translationX(0, -40)
                                .interpolator(new LinearInterpolator())
                                .duration(7500)
                                .start();
                    }
                };

                handler.postDelayed(runnable, 500);
            }
        });
        ObjectAnimAlpha(castleCloudMiddle2, middleCloudDuration, delay, new ObjectAnimListener() {
            @Override
            void onAnimStart() {
                castleCloudMiddle2.setVisibility(View.VISIBLE);
            }

            @Override
            void onAnimStop() {

            }
        });

    }

    /**
     * 属性动画透明
     *
     * @param imageView
     * @param duration
     * @param delay
     */
    private void ObjectAnimAlpha(final ImageView imageView, long duration, long delay, Animator.AnimatorListener listener) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        oa.setDuration(duration);
        oa.setRepeatMode(ValueAnimator.RESTART);
        oa.setStartDelay(delay);
        oa.start();
        oa.addListener(listener);
    }

    /**
     * 四个花环顺序帧动画
     */
    private void flowerFrameAnim() {
        final ImageView[] mImageIds = new ImageView[]{castleXmlGreen1, castleXmlPurple2, castleXmlGreen3, castleXmlPurple4};
        Runnable runnable = new Runnable() {
            public int count;

            @Override
            public void run() {
                mImageIds[count].setVisibility(View.VISIBLE);
                AnimationDrawable drawable = (AnimationDrawable) mImageIds[count].getDrawable();
                drawable.start();
                ANIM_TIME = ANIM_TIME + frameFlowerDelay;
                count++;
                if (count < 4) {
                    handler.postDelayed(this, frameFlowerDelay);
                }
            }
        };
        ANIM_TIME = ANIM_TIME + frameDelay;
        handler.postDelayed(runnable, frameDelay);
    }

    /**
     * 气球上升动画
     */
    private void balloonUpAnim() {
        ImageView[] mImageIds = new ImageView[]{castleBalloon0, castleBalloon1, castleBalloon2, castleBalloon3, castleBalloon4};
        Random random = new Random();
        // >在五个小气球随机产生最少2个最多4个
        int balloonNum = random.nextInt(mImageIds.length - 2) + 2;
        HashSet<ImageView> hashSet = new HashSet<ImageView>();
        // >随机选取气球放入set集合
        while (hashSet.size() < balloonNum) {
            hashSet.add(mImageIds[random.nextInt(mImageIds.length)]);
        }
        Iterator<ImageView> iterator = hashSet.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            final ImageView imageView = iterator.next();
            imageView.measure(0, 0);
            final int xStart = random.nextInt(windowWidth - 300) + 100;
            double dEnd = random.nextDouble() * 1000 - random.nextDouble() * 500;
            final int xEnd = (int) dEnd;
            final int height = imageView.getMeasuredHeight();
            final long delay = count * 1400;
            Log.e("BNDG", ">>>> zhunb");
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ObjectAnimTrans(imageView, xStart, xEnd, windowHeight, 0 - height, 2000, new ObjectAnimListener() {
                        @Override
                        void onAnimStart() {
                            imageView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            };
            handler.postDelayed(runnable, delay);
            ANIM_TIME = ANIM_TIME + delay + 2500;
            Log.e("BNDG", ">>>> 动画统计时间" + ANIM_TIME);
            count++;
           /* ViewAnimator.animate(imageView)
                    .translationX(xStart, xEnd)
                    .translationY(windowHeight, 0 - height)
                    .duration(2500)
                    .interpolator(new AccelerateInterpolator())
                    .startDelay(count * 1500)
                    .onStart(new AnimationListener.Start() {
                        @Override
                        public void onStart() {
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }).start();*/
        }

    }


    private void ObjectAnimTrans(final ImageView imageView, int xStart, int xEnd, int yStart, int yEnd, int duration, ObjectAnimListener listener) {
        set = new AnimatorSet();
        ObjectAnimator oaX = ObjectAnimator.ofFloat(imageView, "translationX", new float[]{xStart, xEnd});
        oaX.setRepeatMode(ValueAnimator.RESTART);
        ObjectAnimator oaY = ObjectAnimator.ofFloat(imageView, "translationY", new float[]{yStart, yEnd});
        oaY.setRepeatMode(ValueAnimator.RESTART);
        set.playTogether(oaX, oaY);
        set.setDuration(duration);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(listener);
        Log.e("BNDG", ">>>> daozheli");
        set.start();
    }

    /**
     * 热气球动画
     */
    private void hotAirBalloon0Anim() {
        castleHotAirBalloon0.measure(0, 0);
        final int hotAir0Height = castleHotAirBalloon0.getMeasuredHeight();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                ObjectAnimTrans(castleHotAirBalloon0, 0, 0, windowHeight, hotAir0Height / 5, 2000, new ObjectAnimListener() {
                    @Override
                    void onAnimStart() {
                        castleHotAirBalloon0.setVisibility(View.VISIBLE);
                    }

                    @Override
                    void onAnimStop() {

                        ObjectAnimTrans(castleHotAirBalloon0, 0, 0, hotAir0Height / 5, hotAir0Height / 5 + 20, 2500, new ObjectAnimListener() {
                            @Override
                            void onAnimStop() {
                                ViewAnimator.animate(castleHotAirBalloon0)
                                        .translationY(0 - hotAir0Height)
                                        .duration(1000)
                                        .interpolator(new AccelerateInterpolator())
                                        .onStart(new AnimationListener.Start() {
                                            @Override
                                            public void onStart() {
                                                Runnable runnable1 = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        // >上方云朵显示
                                                        topCloudAnim();
                                                        // >下方云朵合并动画
                                                        bottomCloudAnim();
                                                    }
                                                };

                                                handler.postDelayed(runnable1, 500);

                                            }
                                        }).start();
                            }
                        });

                    }
                });
            }
        };

        handler.postDelayed(runnable, 5000);
    }

    public void radian(final ImageView imageView, final int speed, final int ivWidth, ObjectAnimListener listener, final int tag) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(6000);
        valueAnimator.addListener(listener);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue) {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                if (tag % 2 != 0) {
                    point.x = speed * fraction * 3 - ivWidth;
                } else {
                    point.x = windowWidth - speed * fraction * 3 + ivWidth;
                }
                point.y = 0.1f * speed * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                imageView.setX(point.x);
                imageView.setY(point.y);

            }
        });
    }

    class ObjectAnimListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            onAnimStart();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onAnimStop();
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        void onAnimStart() {

        }

        void onAnimStop() {

        }
    }

}

