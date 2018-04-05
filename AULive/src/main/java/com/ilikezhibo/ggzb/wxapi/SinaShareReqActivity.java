package com.ilikezhibo.ggzb.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.BaseActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by big on 4/27/16.
 */
public class SinaShareReqActivity extends BaseActivity {

   //处理事件传到shareHelper里处理
   @Override protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
      // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
      // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
      // 失败返回 false，不调用上述回调
      SinaReqEvent sinaReqEvent = new SinaReqEvent();
      sinaReqEvent.intent = getIntent();
      EventBus.getDefault().post(sinaReqEvent);
      Trace.d("SinaShareReqActivity onCreate");
      this.finish();
   }

   /**
    * @see {@link Activity#onNewIntent}
    */
   @Override protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);

      // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
      // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
      // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
      if (intent != null) {
         SinaReqEvent sinaReqEvent = new SinaReqEvent();
         sinaReqEvent.intent = intent;
         EventBus.getDefault().post(sinaReqEvent);
      }
      Trace.d("SinaShareReqActivity onNewIntent");
      this.finish();
   }

   @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

   }
}
