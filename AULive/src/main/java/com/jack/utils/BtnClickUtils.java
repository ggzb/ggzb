package com.jack.utils;

/**
 * @author jack.long
 * @ClassName: BtnClickUtils
 * @Description: TODO
 * @date 2015-2-4 下午5:50:38
 */
public class BtnClickUtils {
   private BtnClickUtils() {

   }

   public static boolean isFastDoubleClick() {
      long time = System.currentTimeMillis();
      long timeD = time - mLastClickTime;
      if (0 < timeD && timeD < 500) {
         return true;
      }

      mLastClickTime = time;

      return false;
   }

   private static long mLastClickTime = 0;
/////////////////////////////////////////

   public static boolean isFastClean() {
      long time = System.currentTimeMillis();
      long timeD = time - mLastCleanTime;
      if (0 < timeD && timeD < 1000) {
         return true;
      }

      mLastCleanTime = time;

      return false;
   }

   private static long mLastCleanTime = 0;

   ///////////////////
   public static boolean isFastSendMsg() {
      long time = System.currentTimeMillis();
      long timeD = time - mLastShowTime;
      if (0 < timeD && timeD < 5000) {
         return true;
      }

      mLastShowTime = time;

      return false;
   }

   private static long mLastShowTime = 0;
}