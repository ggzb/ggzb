package com.ilikezhibo.ggzb;

import android.app.Activity;
import java.util.Stack;

/**
 * @ClassName: ActivityManager
 * @Description: activity 栈管理类
 * @author jack.long
 * @date 2013-9-12 下午2:57:38
 * 
 */
public class ActivityStackManager {

	private static Stack<Activity> activityStack = null;
	private static ActivityStackManager instance = null;

	private ActivityStackManager() {
	}

	public static ActivityStackManager getInstance() {
		if (instance == null) {
			instance = new ActivityStackManager();
		}

		return instance;
	}

	/**
	 * 出栈
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			if (!activity.isFinishing()) {
				activity.finish();
			}

			if (activityStack != null) {
				activityStack.remove(activity);
			}

			activity = null;
		}
	}

	/**
	 * activity入栈
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}

		activityStack.push(activity);
	}

	/**
	 * 最先入栈的activity
	 * 
	 * @return
	 */
	public Activity getFirstActivity() {
		Activity activity = null;
		if (activityStack == null) {
			return activity;
		}

		if (!activityStack.isEmpty()) {
			activity = activityStack.firstElement();
		}

		return activity;
	}

	/**
	 * 当前activity
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		Activity activity = null;
		if (activityStack == null) {
			return activity;
		}

		if (!activityStack.isEmpty()) {
			activity = activityStack.lastElement();
		}

		return activity;
	}

	/**
	 * 退出应用程序
	 */
	public void exitActivity() {
		while (true) {
			Activity activity = getFirstActivity();
			if (activity == null) {
				break;
			}
			popActivity(activity);
		}

		//android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 退出栈中所有Activity
	 * 
	 * @param cls
	 */
	public void popAllActivityExceptOne(Class<?> cls) {
		while (true) {
			Activity activity = getFirstActivity();
			if (activity == null) {
				break;
			}

			if (activity.getClass().equals(cls)) {
				if (activity != null) {
					activityStack.remove(activity);
					break;
				}
			} else {
				if (activity != null) {
					if (!activity.isFinishing()) {
						activity.finish();
					}
					activityStack.remove(activity);
					activity = null;
				}
			}
		}
	}
}
