package com.ilikezhibo.ggzb.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @ClassName: WXShareOkReceiver
 * @Description: TODO
 * @author jack.long
 * @date 2014-12-16 下午2:31:10
 * 
 */
public class WXShareOkReceiver extends BroadcastReceiver {
	public static final String ACTION_SHARE_WX_KEY = "com.qixi.piaoke.share.wx";
	public static final String INTENT_SHARE_TYPE_KEY = "com.qixi.piaoke.share.type";

	private ShareSuccListener listener;

	public void setListener(ShareSuccListener listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_SHARE_WX_KEY)) {
			listener.onWxShareSucc(intent.getStringExtra(INTENT_SHARE_TYPE_KEY));
		}
	}

	public interface ShareSuccListener {
		void onWxShareSucc(String type);
	}

}
