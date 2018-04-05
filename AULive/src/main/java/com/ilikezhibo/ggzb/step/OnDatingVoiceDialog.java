package com.ilikezhibo.ggzb.step;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gauss.recorder.SpeexPlayer;
import com.gauss.recorder.SpeexRecorder;
import com.gauss.recorder.SpeexRecorder.VolumeListener;
import com.gauss.speex.encode.SpeexDecoder;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.StringCallback;
import com.jack.lib.net.itf.IRequestListener;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.FileUtil;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.R;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: OnDatingVoiceDialog
 * @Description: 约会语音录制
 * @author jack.long
 * @date 2014-12-27 下午5:31:00
 * 
 */
public class OnDatingVoiceDialog extends Dialog implements OnClickListener,
		VolumeListener {

	// 录音分贝最小与最大值
	private static final int MIN_VOLUME = 40;
	private static final int MAX_VOLUME = 90;

	private boolean btn_vocie = false;// 录音touch
	// private boolean isShosrt = false;

	private int flag = 1;
	// /** 语音删除的范围 */
	// private static final int VOLUME_DELETE_AREA = 1;

	/** 执行Timer标识 **/
	private static final int LOAD_PROGRESS = 0;
	/** 关闭Timer标识 **/
	private static final int CLOSE_PROGRESS = 1;
	/** 语音分贝 */
	private static final int VOLUME_VALUE = 2;
	/** 播放完成 */
	private static final int PLAY_COMPLETE = 3;

	/** TimerId **/
	private int mTimerId = 0;

	private TextView ondate_AudioTimeTv;
	private RelativeLayout ondate_voice_size_layout;
	private ImageView ondate_record_image_full;
	private ImageView ondate_record_image_empty;

	private ImageView ondate_voice_play_img;// 播放
	private ImageView ondate_voice_playing_img;// 播放中

	private LinearLayout ondate_voice_layout;
	private LinearLayout ondate_state_layout;
	private Button ondate_again_voice_btn;
	private Button ondate_send_voice_btn;

	// volume
	private int recordEmptyHeight;
	private int initEmptyHeight;

	/** Timer对象 **/
	private Timer timer = null;
	/** TimerTask对象 **/
	private TimerTask timerTask = null;

	private long startVoiceT, endVoiceT;
	// private String voiceName = null;

	private int voiceTime;
	private String speexVoicePath = null;
	private SpeexRecorder recorderInstance = null;

	private UploadVoiceSuccListener uploadSuccListener;

	private String voiceServerUrl;

	private SpeexPlayer sPlayer;

	public OnDatingVoiceDialog(Context context,
			UploadVoiceSuccListener uploadSuccListener) {
		super(context, R.style.Theme_dialog);
		this.uploadSuccListener = uploadSuccListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_voice_btn_layout);

		ondate_AudioTimeTv = (TextView) findViewById(R.id.ondate_AudioTimeTv);

		ondate_voice_size_layout = (RelativeLayout) findViewById(R.id.ondate_voice_size_layout);
		ondate_voice_size_layout.setVisibility(View.VISIBLE);
		ondate_record_image_full = (ImageView) findViewById(R.id.ondate_record_image_full);
		ondate_record_image_empty = (ImageView) findViewById(R.id.ondate_record_image_empty);

		ondate_voice_play_img = (ImageView) findViewById(R.id.ondate_voice_play_img);
		ondate_voice_play_img.setVisibility(View.GONE);
		ondate_voice_play_img.setOnClickListener(this);
		ondate_voice_playing_img = (ImageView) findViewById(R.id.ondate_voice_playing_img);
		ondate_voice_playing_img
				.setBackgroundResource(R.drawable.ondate_voice_playing);
		ondate_voice_playing_img.setVisibility(View.GONE);
		ondate_voice_playing_img.setOnClickListener(this);

		ondate_voice_layout = (LinearLayout) findViewById(R.id.ondate_voice_layout);
		ondate_voice_layout.setVisibility(View.VISIBLE);

		ondate_state_layout = (LinearLayout) findViewById(R.id.ondate_state_layout);
		ondate_state_layout.setVisibility(View.GONE);
		ondate_again_voice_btn = (Button) findViewById(R.id.ondate_again_voice_btn);
		ondate_again_voice_btn.setOnClickListener(this);
		ondate_send_voice_btn = (Button) findViewById(R.id.ondate_send_voice_btn);
		ondate_send_voice_btn.setOnClickListener(this);

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		ondate_record_image_empty.measure(w, h);
		int height = ondate_record_image_empty.getMeasuredHeight();
		initEmptyHeight = height;
		recordEmptyHeight = height;

		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				// 如果不发送则销毁当前录音
				onResetRecordUIState(voiceServerUrl != null ? false : true);
			}
		});

		ondate_voice_layout.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// 按下语音录制按钮时返回false执行父类OnTouch
				if (!Environment.getExternalStorageDirectory().exists()) {
					Utils.showMessage("No SDCard");
					return false;
				}

				Trace.d("dialog ontouch event:" + event.getAction()
						+ " btn_voice:" + btn_vocie);

				Trace.d("1");
				int[] location = new int[2];
				ondate_voice_layout.getLocationInWindow(location);
				// 获取在当前窗口内的绝对坐标
				int btn_rc_Y = location[1];
				int btn_rc_X = location[0];
				//
				// int[] del_location = new int[2];
				// ondate_voice_size_layout.getLocationOnScreen(del_location);
				// int del_Y = del_location[1];
				// int del_x = del_location[0];

				Trace.d("ondate_voice_layout btn_x:" + btn_rc_X + " btn_y:"
						+ btn_rc_Y);

				if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
					if (!Environment.getExternalStorageDirectory().exists()) {
						Utils.showMessage("No SDCard");
						return false;
					}
					Trace.d("2");
					// if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X)
					// {// 判断手势按下的位置是否是语音录制按钮的范围内
					Trace.d("3");
					// chatVoiceLayout
					// .setBackgroundResource(R.drawable.chat_bar_send_voice_bg_press);
					// sendTitle.setText(R.string.chat_audio_send);
					// record_image_delet.setVisibility(View.GONE);
					// record_action_text.setVisibility(View.VISIBLE);
					// record_image_empty.setVisibility(View.VISIBLE);
					// record_image_full.setVisibility(View.VISIBLE);
					// audioTimeTv.setVisibility(View.VISIBLE);
					// rcChat_popup.setVisibility(View.VISIBLE);
					// voice_rcd_hint_loading.setVisibility(View.VISIBLE);
					// voice_rcd_hint_rcding.setVisibility(View.GONE);
					// voice_rcd_hint_tooshort.setVisibility(View.GONE);
					// mHandler.postDelayed(new Runnable() {
					// public void run() {
					// if (!isShosrt) {
					// // voice_rcd_hint_loading.setVisibility(View.GONE);
					// // voice_rcd_hint_rcding
					// // .setVisibility(View.VISIBLE);
					// Utils.showCenterMessage("录制时间太短");
					// }
					// }
					// }, 300);
					// img1.setVisibility(View.VISIBLE);
					// del_re.setVisibility(View.GONE);
					startVoiceT = System.currentTimeMillis();
					Trace.d("start startVoiceT:" + startVoiceT);
					// 开始录音
					FileUtil.createDir(FileUtil.RECORD_PATH);
					// voiceName = startVoiceT + ".amr";
					// start(voiceName);
					// 开始录音
					speexVoicePath = FileUtil.RECORD_PATH + File.separator
							+ System.currentTimeMillis() + ".spx";
					recorderInstance = new SpeexRecorder(speexVoicePath);
					recorderInstance
							.setVolumeListener(OnDatingVoiceDialog.this);

					Thread recordThread = new Thread(recorderInstance);
					recordThread.start();
					recorderInstance.setRecording(true);

					flag = 2;
					startTimer();
					// }
				} else if (event.getAction() == MotionEvent.ACTION_UP
						&& flag == 2) {// 松开手势时执行录制完成
					Trace.d("4");
					// closeTimer();
					// chatVoiceLayout
					// .setBackgroundResource(R.drawable.chat_bar_send_voice_bg);
					// sendTitle.setText(R.string.chat_press_voice);
					// recorderInstance.setRecording(false);
					// File file = new File(
					// android.os.Environment.getExternalStorageDirectory()
					// + "/" + tempVoiceName);
					// if (file.exists()) {
					// file.delete();
					// }
					// if (event.getY() >= del_Y
					// && event.getY() <= del_Y
					// + ondate_voice_size_layout.getHeight()
					// + VOLUME_DELETE_AREA
					// && event.getX() >= del_x
					// && event.getX() <= del_x
					// + ondate_voice_size_layout.getWidth()) {
					// // img1.setVisibility(View.VISIBLE);
					// // del_re.setVisibility(View.GONE);
					// // chatVoiceLayout
					// //
					// .setBackgroundResource(R.drawable.chat_bar_send_voice_bg);
					// // sendTitle.setText(R.string.chat_press_voice);
					// btn_vocie = false;
					// stopVoiceRecord();
					// // chatVoiceLayout
					// //
					// .setBackgroundResource(R.drawable.chat_bar_send_voice_bg_cancel);
					// // sendTitle.setText(R.string.chat_audio_send_cancel);
					// // record_image_delet.setVisibility(View.VISIBLE);
					// // record_action_text.setVisibility(View.GONE);
					// // record_image_empty.setVisibility(View.GONE);
					// // record_image_full.setVisibility(View.GONE);
					// // audioTimeTv.setVisibility(View.GONE);
					// // if (new File(speexVoicePath).exists()) {
					// // new File(speexVoicePath).delete();
					// // }
					// // flag = 1;
					// } else {
					// voice_rcd_hint_rcding.setVisibility(View.GONE);
					endVoiceT = System.currentTimeMillis();
					flag = 1;
					Trace.d("stop endVoiceT:" + endVoiceT + " startVoiceT:"
							+ startVoiceT);
					// int time = (int) ((endVoiceT - startVoiceT) / 1000);
					// if (time < 2) {
					// isShosrt = true;
					// // voice_rcd_hint_loading.setVisibility(View.GONE);
					// // voice_rcd_hint_rcding.setVisibility(View.GONE);
					// // voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
					// mHandler.postDelayed(new Runnable() {
					// public void run() {
					// // voice_rcd_hint_tooshort
					// // .setVisibility(View.GONE);
					// // rcChat_popup.setVisibility(View.GONE);
					// isShosrt = false;
					// btn_vocie = false;
					// stopVoiceRecord();
					// if (new File(speexVoicePath).exists()) {
					// new File(speexVoicePath).delete();
					// }
					// }
					// }, 500);
					// return false;
					// }
					btn_vocie = false;
					stopVoiceRecord();
				}
				if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
					Trace.d("5");

				} else {
					Trace.d("在按钮范围内");
					if (flag != 1) {
					}
				}
				return true;
			}
		});

		ondate_AudioTimeTv.setText("0/60\"");
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.ondate_again_voice_btn:
			if (isUploadVoice) {
				return;
			}
			onResetRecordUIState(true);
			break;
		case R.id.ondate_send_voice_btn:
			if (FileUtil.isFileExist(speexVoicePath)) {
				uploadFace(speexVoicePath);
			} else {
				speexVoicePath = null;
				Utils.showCenterMessage("没找到您上传的语音文件");
			}
			break;

		case R.id.ondate_voice_play_img:
			if (speexVoicePath != null) {
				if (sPlayer == null) {
					sPlayer = new SpeexPlayer(speexVoicePath);
				}

				sPlayer.startPlay();
				sPlayer.setSpeexCompletionListener(new SpeexDecoder.SpeexCompletionListener() {

					@Override
					public void onCompletion() {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = PLAY_COMPLETE;
						mHandler.sendMessage(msg);
					}
				});
			}

			ondate_voice_play_img.setVisibility(View.GONE);
			ondate_voice_playing_img.setVisibility(View.VISIBLE);
			break;
		case R.id.ondate_voice_playing_img:// 播放中
			if (ondate_voice_layout.getVisibility() == View.VISIBLE) {
				ondate_voice_play_img.setVisibility(View.GONE);
			} else {
				ondate_voice_play_img.setVisibility(View.VISIBLE);
			}
			ondate_voice_playing_img.setVisibility(View.GONE);
			if (sPlayer != null) {
				sPlayer.stopPlay();
			}
			break;
		default:
			break;
		}
	}

	// // 按下语音录制按钮时
	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// if (!Environment.getExternalStorageDirectory().exists()) {
	// Utils.showMessage("No SDCard");
	// return false;
	// }
	//
	// Trace.d("dialog ontouch event:" + event.getAction() + " btn_voice:"
	// + btn_vocie);
	//
	// if (btn_vocie) {
	// Trace.d("1");
	// int[] location = new int[2];
	// ondate_voice_layout.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
	// int btn_rc_Y = location[1];
	// int btn_rc_X = location[0];
	//
	// int[] del_location = new int[2];
	// ondate_voice_size_layout.getLocationInWindow(del_location);
	// int del_Y = del_location[1];
	// int del_x = del_location[0];
	//
	// Trace.d("ondate_voice_layout btn_x:" + btn_rc_X + " btn_y:"
	// + btn_rc_Y + " ondate_voice_size_layout del_x:" + del_x
	// + " del_y:" + del_Y);
	//
	// if (event.getAction() == MotionEvent.ACTION_DOWN && flag == 1) {
	// if (!Environment.getExternalStorageDirectory().exists()) {
	// Utils.showMessage("No SDCard");
	// return false;
	// }
	// Trace.d("2");
	// if (event.getY() > btn_rc_Y && event.getX() > btn_rc_X) {//
	// 判断手势按下的位置是否是语音录制按钮的范围内
	// Trace.d("3");
	// // chatVoiceLayout
	// // .setBackgroundResource(R.drawable.chat_bar_send_voice_bg_press);
	// // sendTitle.setText(R.string.chat_audio_send);
	// // record_image_delet.setVisibility(View.GONE);
	// // record_action_text.setVisibility(View.VISIBLE);
	// // record_image_empty.setVisibility(View.VISIBLE);
	// // record_image_full.setVisibility(View.VISIBLE);
	// // audioTimeTv.setVisibility(View.VISIBLE);
	// // rcChat_popup.setVisibility(View.VISIBLE);
	// // voice_rcd_hint_loading.setVisibility(View.VISIBLE);
	// // voice_rcd_hint_rcding.setVisibility(View.GONE);
	// // voice_rcd_hint_tooshort.setVisibility(View.GONE);
	// mHandler.postDelayed(new Runnable() {
	// public void run() {
	// if (!isShosrt) {
	// // voice_rcd_hint_loading.setVisibility(View.GONE);
	// // voice_rcd_hint_rcding
	// // .setVisibility(View.VISIBLE);
	// Utils.showCenterMessage("录制时间太短");
	// }
	// }
	// }, 300);
	// // img1.setVisibility(View.VISIBLE);
	// // del_re.setVisibility(View.GONE);
	// startVoiceT = System.currentTimeMillis();
	// Trace.d("start startVoiceT:" + startVoiceT);
	// // 开始录音
	// FileUtil.createDir(FileUtil.RECORD_PATH);
	// // voiceName = startVoiceT + ".amr";
	// // start(voiceName);
	// // 开始录音
	// speexVoicePath = FileUtil.RECORD_PATH + File.separator
	// + System.currentTimeMillis() + ".spx";
	// recorderInstance = new SpeexRecorder(speexVoicePath);
	// recorderInstance
	// .setVolumeListener(OnDatingVoiceDialog.this);
	//
	// Thread recordThread = new Thread(recorderInstance);
	// recordThread.start();
	// recorderInstance.setRecording(true);
	//
	// flag = 2;
	// startTimer();
	// }
	// } else if (event.getAction() == MotionEvent.ACTION_UP && flag == 2) {//
	// 松开手势时执行录制完成
	// Trace.d("4");
	// // closeTimer();
	// // chatVoiceLayout
	// // .setBackgroundResource(R.drawable.chat_bar_send_voice_bg);
	// // sendTitle.setText(R.string.chat_press_voice);
	// // recorderInstance.setRecording(false);
	// // File file = new File(
	// // android.os.Environment.getExternalStorageDirectory()
	// // + "/" + tempVoiceName);
	// // if (file.exists()) {
	// // file.delete();
	// // }
	// if (event.getY() >= del_Y
	// && event.getY() <= del_Y
	// + ondate_voice_size_layout.getHeight()
	// + VOLUME_DELETE_AREA
	// && event.getX() >= del_x
	// && event.getX() <= del_x
	// + ondate_voice_size_layout.getWidth()) {
	// // img1.setVisibility(View.VISIBLE);
	// // del_re.setVisibility(View.GONE);
	// // chatVoiceLayout
	// // .setBackgroundResource(R.drawable.chat_bar_send_voice_bg);
	// // sendTitle.setText(R.string.chat_press_voice);
	// btn_vocie = false;
	// stopVoiceRecord();
	// // chatVoiceLayout
	// // .setBackgroundResource(R.drawable.chat_bar_send_voice_bg_cancel);
	// // sendTitle.setText(R.string.chat_audio_send_cancel);
	// // record_image_delet.setVisibility(View.VISIBLE);
	// // record_action_text.setVisibility(View.GONE);
	// // record_image_empty.setVisibility(View.GONE);
	// // record_image_full.setVisibility(View.GONE);
	// // audioTimeTv.setVisibility(View.GONE);
	// // if (new File(speexVoicePath).exists()) {
	// // new File(speexVoicePath).delete();
	// // }
	// // flag = 1;
	// } else {
	// // voice_rcd_hint_rcding.setVisibility(View.GONE);
	// endVoiceT = System.currentTimeMillis();
	// flag = 1;
	// Trace.d("stop endVoiceT:" + endVoiceT + " startVoiceT:"
	// + startVoiceT);
	// int time = (int) ((endVoiceT - startVoiceT) / 1000);
	// if (time < 2) {
	// isShosrt = true;
	// // voice_rcd_hint_loading.setVisibility(View.GONE);
	// // voice_rcd_hint_rcding.setVisibility(View.GONE);
	// // voice_rcd_hint_tooshort.setVisibility(View.VISIBLE);
	// mHandler.postDelayed(new Runnable() {
	// public void run() {
	// // voice_rcd_hint_tooshort
	// // .setVisibility(View.GONE);
	// // rcChat_popup.setVisibility(View.GONE);
	// isShosrt = false;
	// btn_vocie = false;
	// stopVoiceRecord();
	// if (new File(speexVoicePath).exists()) {
	// new File(speexVoicePath).delete();
	// }
	// }
	// }, 500);
	// return false;
	// }
	// btn_vocie = false;
	// stopVoiceRecord();
	// // uploadChatVoice(speexVoicePath, time, null);
	//
	// // ChatMsgEntity entity = new ChatMsgEntity();
	// // entity.setDate(getDate());
	// // entity.setName("高富帅");
	// // entity.setMsgType(false);
	// // entity.setTime(time + "\"");
	// // entity.setText(voiceName);
	// // mDataArrays.add(entity);
	// // mAdapter.notifyDataSetChanged();
	// // mListView.setSelection(mListView.getCount() - 1);
	// }
	// }
	// if (event.getY() < btn_rc_Y) {// 手势按下的位置不在语音录制按钮的范围内
	// Trace.d("5");
	//
	// // Animation mLitteAnimation =
	// // AnimationUtils.loadAnimation(this,
	// // R.anim.cancel_rc);
	// // Animation mBigAnimation =
	// // AnimationUtils.loadAnimation(this,
	// // R.anim.cancel_rc2);
	// // // img1.setVisibility(View.GONE);
	// // del_re.setVisibility(View.VISIBLE);
	// // del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg);
	// if (event.getY() >= del_Y
	// && event.getY() <= del_Y
	// + ondate_voice_size_layout.getHeight()
	// + VOLUME_DELETE_AREA
	// && event.getX() >= del_x
	// && event.getX() <= del_x
	// + ondate_voice_size_layout.getWidth()) {
	// // voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
	// // rcChat_popup.setVisibility(View.GONE);
	// if (flag != 1) {
	// Trace.d("在删除按钮范围内 value:" + flag);
	// // chatVoiceLayout
	// // .setBackgroundResource(R.drawable.chat_bar_send_voice_bg_cancel);
	// // sendTitle.setText(R.string.chat_audio_send_cancel);
	// // record_image_delet.setVisibility(View.VISIBLE);
	// // record_action_text.setVisibility(View.GONE);
	// // record_image_empty.setVisibility(View.GONE);
	// // record_image_full.setVisibility(View.GONE);
	// // audioTimeTv.setVisibility(View.GONE);
	// }
	// // if (new File(speexVoicePath).exists()) {
	// // new File(speexVoicePath).delete();
	// // }
	// // flag = 1;
	// Trace.d("在删除按钮范围内");
	// // del_re.setBackgroundResource(R.drawable.voice_rcd_cancel_bg_focused);
	// // sc_img1.startAnimation(mLitteAnimation);
	// // sc_img1.startAnimation(mBigAnimation);
	// } else {
	// Trace.d("不再按钮范围内");
	// if (flag != 1) {
	// // voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
	// // chatVoiceLayout
	// // .setBackgroundResource(R.drawable.chat_bar_send_voice_bg_press);
	// // sendTitle.setText(R.string.chat_audio_send);
	// // record_image_delet.setVisibility(View.GONE);
	// // record_action_text.setVisibility(View.VISIBLE);
	// // record_image_empty.setVisibility(View.VISIBLE);
	// // record_image_full.setVisibility(View.VISIBLE);
	// // audioTimeTv.setVisibility(View.VISIBLE);
	// }
	// }
	// } else {
	// // img1.setVisibility(View.VISIBLE);
	// // del_re.setVisibility(View.GONE);
	// // del_re.setBackgroundResource(0);
	// Trace.d("在按钮范围内");
	// if (flag != 1) {
	// // voice_rcd_hint_rcding.setVisibility(View.VISIBLE);
	// // chatVoiceLayout
	// // .setBackgroundResource(R.drawable.chat_bar_send_voice_bg_press);
	// // sendTitle.setText(R.string.chat_audio_send);
	// // record_image_delet.setVisibility(View.GONE);
	// // record_action_text.setVisibility(View.VISIBLE);
	// // record_image_empty.setVisibility(View.VISIBLE);
	// // record_image_full.setVisibility(View.VISIBLE);
	// // audioTimeTv.setVisibility(View.VISIBLE);
	// }
	// }
	// }
	// return true;
	// }

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// // 省略了部分细节
	// // ListenerInfo li = mListenerInfo;
	// // if (li != null && li.mOnTouchListener != null && (mViewFlags &
	// // ENABLED_MASK) == ENABLED
	// // && li.mOnTouchListener.onTouch(this, event)) {
	// // return true;
	// // }
	// if (onTouchEvent(ev)) {
	// return true;
	// }
	// return false;
	// }

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// // TODO Auto-generated method stub
	// Trace.d("dialog event:" + event.getAction());
	// return super.onTouchEvent(event);
	// }

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PLAY_COMPLETE:// 播放完成
				if (ondate_voice_playing_img == null) {
					return;
				}
				if (ondate_voice_layout.getVisibility() == View.VISIBLE) {
					ondate_voice_play_img.setVisibility(View.GONE);
				} else {
					ondate_voice_play_img.setVisibility(View.VISIBLE);
				}
				ondate_voice_playing_img.setVisibility(View.GONE);
				if (sPlayer != null) {
					sPlayer.stopPlay();
				}
				break;
			case LOAD_PROGRESS:
				Trace.d("当前TimerId为" + msg.arg1);
				int time = msg.arg1;
				if (time > 0) {
					voiceTime = time;
					if (ondate_AudioTimeTv != null) {
						ondate_AudioTimeTv.setText(time + "/60\"");
					}
				}
				if (time == 60) {
					voiceTime = time;
					btn_vocie = false;
					stopVoiceRecord();
					onReadySendVoiceUIState();
					// uploadChatVoice(speexVoicePath, 60, null);

					// endVoiceT = System.currentTimeMillis();
					// System.out.println("stop endVoiceT:" + endVoiceT
					// + " startVoiceT:" + startVoiceT);
					// int time = (int) ((endVoiceT - startVoiceT) / 1000);
					// ChatMsgEntity entity = new ChatMsgEntity();
					// entity.setDate(getDate());
					// entity.setName("高富帅");
					// entity.setMsgType(false);
					// entity.setTime(time + "\"");
					// entity.setText(voiceName);
					// mDataArrays.add(entity);
					// mAdapter.notifyDataSetChanged();
					// mListView.setSelection(mListView.getCount() - 1);
				}
				break;
			case CLOSE_PROGRESS:
				mTimerId = 0;
				Trace.d("当前Timer已经关闭请重新开启");
				break;
			case VOLUME_VALUE:
				updateDisplay(msg.arg1);
				break;
			}
			super.handleMessage(msg);
		}
	};

	// 准备发送语音的状态
	private void onReadySendVoiceUIState() {
		if (ondate_state_layout == null || ondate_voice_layout == null) {
			return;
		}
		btn_vocie = false;
		ondate_state_layout.setVisibility(View.VISIBLE);
		ondate_voice_layout.setVisibility(View.GONE);
		ondate_record_image_full.setVisibility(View.GONE);
		ondate_record_image_empty.setVisibility(View.GONE);
		ondate_voice_play_img.setVisibility(View.VISIBLE);
		ondate_voice_playing_img.setVisibility(View.GONE);
	}

	// 开始录音 重置状态
	public void onResetRecordUIState(boolean isDelLocalPath) {
		if (ondate_state_layout == null || ondate_voice_layout == null) {
			return;
		}

		btn_vocie = true;

		voiceTime = 0;
		ondate_AudioTimeTv.setText("0/60\"");
		ondate_voice_play_img.setVisibility(View.GONE);
		ondate_voice_playing_img.setVisibility(View.GONE);
		ondate_state_layout.setVisibility(View.GONE);
		ondate_voice_layout.setVisibility(View.VISIBLE);
		ondate_record_image_full.setVisibility(View.VISIBLE);
		ondate_record_image_empty.setVisibility(View.VISIBLE);

		if (sPlayer != null) {
			sPlayer.stopPlay();
		}

		if (isDelLocalPath) {
			if (speexVoicePath != null && FileUtil.isFileExist(speexVoicePath)) {
				FileUtil.deleteFile(speexVoicePath);
				speexVoicePath = null;
			}

			if (sPlayer != null) {
				sPlayer = null;
			}
		}

		if (ondate_record_image_empty != null) {
			android.view.ViewGroup.LayoutParams param = ondate_record_image_empty
					.getLayoutParams();
			param.height = initEmptyHeight;
			ondate_record_image_empty.setLayoutParams(param);
		}
	}

	private void stopVoiceRecord() {
		recorderInstance.setRecording(false);
		closeTimer();
		flag = 1;

		onReadySendVoiceUIState();
		// sendTitle.setText(R.string.chat_press_voice);
		// voice_rcd_hint_rcding.setVisibility(View.GONE);
		// rcChat_popup.setVisibility(View.GONE);
	}

	private void startTimer() {
		if (timerTask == null) {
			mTimerId = 0;
			timerTask = new TimerTask() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = LOAD_PROGRESS;
					Trace.d("当前Timer为:" + mTimerId);
					int time = ++mTimerId;
					msg.arg1 = time;
					mHandler.sendMessage(msg);
				}
			};
			timer = new Timer();
			// schedule(TimerTask task, long delay, long period)
			// 安排指定的任务从指定的延迟后开始进行重复的固定延迟执行。
			// task - 所要安排的任务。
			// delay - 执行任务前的延迟时间，单位是毫秒。
			// period - 执行各后续任务之间的时间间隔，单位是毫秒。
			timer.schedule(timerTask, 1000, 1000);
		}
	}

	private void closeTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask = null;
		}
		mTimerId = 0;
		// audioTimeTv.setText("");
		Trace.d("mTimerid:" + mTimerId);
		mHandler.sendEmptyMessage(CLOSE_PROGRESS);
	}

	@Override
	public void onRecordVolume(double volume) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = VOLUME_VALUE;
		msg.arg1 = (int) volume;
		this.mHandler.sendMessage(msg);
	}

	private void updateDisplay(double signalEMA) {
		int recordHeight = 0;
		if (signalEMA >= MAX_VOLUME) {
			recordHeight = 0;
		} else if (signalEMA < MAX_VOLUME || signalEMA > MIN_VOLUME) {
			recordHeight = recordEmptyHeight
					- (int) (recordEmptyHeight * (signalEMA - MIN_VOLUME) / (MAX_VOLUME - MIN_VOLUME));
		} else {
			recordHeight = recordEmptyHeight;
		}

		if (ondate_record_image_empty != null) {
			android.view.ViewGroup.LayoutParams param = ondate_record_image_empty
					.getLayoutParams();
			param.height = recordHeight;
			ondate_record_image_empty.setLayoutParams(param);
		}
	}

	private boolean isUploadVoice = false;

	// 图片上传
	private void uploadFace(final String filePath) {
		if (isUploadVoice) {
			return;
		}
		isUploadVoice = true;

		RequestInformation request = new RequestInformation(
				UrlHelper.STEP_UPLOAD_VOICE_URL,
				RequestInformation.REQUEST_METHOD_POST);

		request.isHttpClient = false;
		request.addHeader("Connection", "Keep-Alive");
		request.addHeader("Charset", "UTF-8");
		request.addHeader("Content-Type",
				"multipart/form-data;boundary=7d4a6d158c9");
		request.setRequestListener(new IRequestListener() {
			@Override
			public boolean onPrepareParams(OutputStream out)
					throws AppException {
				String BOUNDARY = "7d4a6d158c9"; // 数据分隔线
				DataOutputStream outStream = new DataOutputStream(out);
				try {
					outStream.writeBytes("--" + BOUNDARY + "\r\n");
					outStream
							.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
									+ filePath.substring(filePath
											.lastIndexOf("/") + 1)
									+ "\""
									+ "\r\n");
					outStream.writeBytes("\r\n");
					byte[] buffer = new byte[1024];
					FileInputStream fis = new FileInputStream(filePath);
					while (fis.read(buffer, 0, 1024) != -1) {
						outStream.write(buffer, 0, buffer.length);
					}
					fis.close();
					outStream.write("\r\n".getBytes());
					byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 数据结束标志
					outStream.write(end_data);
					outStream.flush();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public void onPreExecute() {
			}

			@Override
			public void onPostExecute() {
			}

			@Override
			public void onCancelled() {
			}

			@Override
			public void onBeforeDoingBackground() {
			}

			@Override
			public Object onAfterDoingBackground(Object object) {
				return object;
			}
		});
		request.setCallback(new StringCallback() {

			@Override
			public void onFailure(AppException e) {
				e.printStackTrace();
				isUploadVoice = false;
				voiceServerUrl = null;
				Utils.showCenterMessage(Utils.trans(R.string.get_info_fail));
			}

			@Override
			public void onCallback(String callback) {
				Trace.d(callback);
				isUploadVoice = false;
				try {
					JSONObject obj = new JSONObject(callback);
					if (callback.indexOf("200") > 0) {
						// 上传成功
						voiceServerUrl = obj.optString("url");
						if (uploadSuccListener != null) {
							uploadSuccListener.onUploadOndateVoiceSucc(
									filePath, voiceServerUrl, voiceTime);
						}
						OnDatingVoiceDialog.this.dismiss();
					} else {
						voiceServerUrl = null;
						Utils.showCenterMessage(obj.optString("msg"));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		request.execute();
	}

	public interface UploadVoiceSuccListener {
		void onUploadOndateVoiceSucc(String localPath, String serverUrl,
				int time);
	}
}
