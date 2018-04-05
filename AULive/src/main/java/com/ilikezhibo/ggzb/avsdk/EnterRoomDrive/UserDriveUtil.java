package com.ilikezhibo.ggzb.avsdk.EnterRoomDrive;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.activity.AvActivity;
import com.ilikezhibo.ggzb.avsdk.activity.msgentity.EnterEntity;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 作者：jacklong on 16/8/25 15:10
 * <p>
 * des: 座驾动画
 */
public class UserDriveUtil {
    public static final int DRIVE_MOUSE = 1;// 老鼠
    public static final int DRIVE_BULL  = 2;// 牛
    public static final int DRIVE_TIGER = 3;// 老虎
    public static final int DRIVE_RABBIT = 4;// 兔
    public static final int DRIVE_DRAGON = 5;// 龙
    public static final int DRIVE_SNAKE = 6;// 蛇
    public static final int DRIVE_HORSE = 7;// 马
    public static final int DRIVE_GOAT = 8;// 羊
    public static final int DRIVE_MONKEY = 9;// 猴子
    public static final int DRIVE_ROOSTER = 10;// 鸡
    public static final int DRIVE_DOG = 11;// 狗
    public static final int DRIVE_PIG = 12;// 猪

    public static boolean is_showing_drive = false;

    public LinkedList<EnterEntity> cache_drive_List = new LinkedList<EnterEntity>();
    public HashMap<String, Long> enterDriveTimeMap = new HashMap<String, Long>();

    private static UserDriveUtil userDriveUtil = null;
    private LayoutInflater inflater;

    private AvActivity mContext;
    //动画的根layout
    private RelativeLayout full_screen_drive_layout;
    // 猪的view
    private View mouseAnimView;
    private View bullAnimView;
    private View tigerAnimView;
    private View rabbitAnimView;
    private View dragonAnimView;
    private View snakeAnimView;
    private View horseAnimView;
    private View goatAnimView;
    private View monkeyAnimView;
    private View roosterAnimView;
    private View dogAnimView;
    private View pigAnimView;

    public static UserDriveUtil getInstance(AvActivity avActivity) {

        if (userDriveUtil == null) {
            userDriveUtil = new UserDriveUtil(avActivity);
        }

        return userDriveUtil;
    }

    public void cleanUserDriveUtil() {
        if (userDriveUtil != null) {
            userDriveUtil.mContext = null;
            userDriveUtil.pigAnimView = null;
            userDriveUtil.mouseAnimView = null;
            userDriveUtil.horseAnimView = null;
            userDriveUtil.monkeyAnimView = null;
            userDriveUtil.tigerAnimView = null;
            userDriveUtil.roosterAnimView = null;
            userDriveUtil.rabbitAnimView = null;
            userDriveUtil.dragonAnimView = null;
            userDriveUtil.bullAnimView = null;
            userDriveUtil.snakeAnimView = null;
            userDriveUtil.goatAnimView = null;
            userDriveUtil.dogAnimView = null;
            cache_drive_List.clear();
            enterDriveTimeMap.clear();
        }
        userDriveUtil = null;

        is_showing_drive = false;
    }

    private UserDriveUtil(AvActivity mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        full_screen_drive_layout = (RelativeLayout) mContext.findViewById(R.id.user_drive_layout);
    }

    // 进房动画
    public void showDriveNotify(EnterEntity enterEntity) {
        //10分钟只显示一次
        if (enterDriveTimeMap.containsKey(enterEntity.uid)) {
            long duration = System.currentTimeMillis() - enterDriveTimeMap.get(enterEntity.uid);
            if (duration < 10 * 60 * 1000) {
                return;
            }
        }
        enterDriveTimeMap.put(enterEntity.uid, System.currentTimeMillis());


        if (!is_showing_drive) {
             showDrive(enterEntity);
        } else {
            Trace.d("addCacheDrive:" + enterEntity.nickname);
            addCacheDrive(enterEntity);
        }
    }

    public void hasAnyDrive() {
        if (cache_drive_List.size() > 0) {
            showDriveNotify(cache_drive_List.removeFirst());
        }
    }

    private void addCacheDrive(EnterEntity enterEntity) {
        cache_drive_List.addLast(enterEntity);
    }

    private void showDrive(EnterEntity enterEntity) {
        if (mContext == null) {
            return;
        }
        //是否在做豪华礼物动画
        is_showing_drive = true;
        //烟花
        if (enterEntity.getZuojia() == DRIVE_PIG) {
            if (pigAnimView == null) {
                pigAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(pigAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑猪的动画
            DriveAnimalCommon drivePigAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_pig_run,R.drawable.drive_pig_stoping,R.drawable.drive_pig_stop,R.drawable.drive_pig_wave);
            drivePigAnim.startAnimalOnAnimation();
        } else  if (enterEntity.getZuojia() == DRIVE_SNAKE) {
            if (snakeAnimView == null) {
                snakeAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(snakeAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑蛇的动画
            DriveAnimalCommon driveSnakeAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_snake_run,R.drawable.drive_snake_stoping,R.drawable.drive_snake_stop,R.drawable.drive_snake_wave);
            driveSnakeAnim.startAnimalOnAnimation();
        } else  if (enterEntity.getZuojia() == DRIVE_RABBIT) {
            if (rabbitAnimView == null) {
                rabbitAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(rabbitAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑兔子的动画
            DriveAnimalCommon driveRabbitAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_rabbit_run,0,R.drawable.drive_rabbit_stop,R.drawable.drive_rabbit_wave);
            driveRabbitAnim.startAnimalOnAnimation();
        } else if (enterEntity.getZuojia() == DRIVE_MOUSE) {
            if (mouseAnimView == null) {
                mouseAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(mouseAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑老鼠的动画
            DriveAnimalCommon driveMouseAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_mouse_run,R.drawable.drive_mouse_stoping,R.drawable.drive_mouse_stop,R.drawable.drive_mouse_wave);
            driveMouseAnim.startAnimalOnAnimation();
        } else if (enterEntity.getZuojia() == DRIVE_HORSE) {
            if (horseAnimView == null) {
                horseAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(horseAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑马的动画
            DriveAnimalCommon driveHorseAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_horse_run,R.drawable.drive_horse_stoping,R.drawable.drive_horse_stop,R.drawable.drive_horse_wave);
            driveHorseAnim.startAnimalOnAnimation();
        } else if (enterEntity.getZuojia() == DRIVE_MONKEY) {
            if (monkeyAnimView == null) {
                monkeyAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(monkeyAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑猴子的动画
            DriveAnimalCommon driveMonkeyAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_monkey_run,0,R.drawable.drive_monkey_stop,R.drawable.drive_monkey_wave);
            driveMonkeyAnim.startAnimalOnAnimation();
        } else if (enterEntity.getZuojia() == DRIVE_TIGER) {
            if (tigerAnimView == null) {
                tigerAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(tigerAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑老虎的动画
            DriveCenterAnimalCommon driveTigerAnim =
                    new DriveCenterAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_tiger_run,R.drawable.drive_tiger_stoping,R.drawable.drive_tiger_stop,R.drawable.drive_tiger_wave);
            driveTigerAnim.startAnimalOnAnimation();
        } else if (enterEntity.getZuojia() == DRIVE_BULL) {
            if (bullAnimView == null) {
                bullAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(bullAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑牛的动画
            DriveCenterAnimalCommon driveBullAnim =
                    new DriveCenterAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_bull_run,0,R.drawable.drive_bull_stop,R.drawable.drive_bull_wave);
            driveBullAnim.startAnimalOnAnimation();
        } else if (enterEntity.getZuojia() == DRIVE_ROOSTER) {
            if (roosterAnimView == null) {
                roosterAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(roosterAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑鸡的动画
            DriveAnimalCommon driveRoosterAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_rooster_run,0,R.drawable.drive_rooster_stop,R.drawable.drive_rooster_wave);
            driveRoosterAnim.startAnimalOnAnimation();
        } else if (enterEntity.getZuojia() == DRIVE_GOAT) {
            if (goatAnimView == null) {
                goatAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(goatAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑洋的动画
            DriveAnimalCommon driveGoatAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_goat_run,0,R.drawable.drive_goat_stop,R.drawable.drive_goat_wave);
            driveGoatAnim.startAnimalOnAnimation();
        }  else if (enterEntity.getZuojia() == DRIVE_DOG) {
            if (dogAnimView == null) {
                dogAnimView = inflater.inflate(R.layout.drive_animal, null);
                full_screen_drive_layout.addView(dogAnimView,
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            }
            // 骑狗的动画
            DriveAnimalCommon driveDogAnim =
                    new DriveAnimalCommon(mContext, mContext.findViewById(R.id.drive_run_rl),
                            enterEntity.nickname,R.drawable.drive_dog_run,R.drawable.drive_dog_stoping,R.drawable.drive_dog_stop,R.drawable.drive_dog_wave);
            driveDogAnim.startAnimalOnAnimation();
        } else {
            //没找到的
            is_showing_drive = false;
            // 查找数组下一个对象
            hasAnyDrive();
        }
    }
}
