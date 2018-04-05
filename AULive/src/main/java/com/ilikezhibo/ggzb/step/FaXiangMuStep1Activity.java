package com.ilikezhibo.ggzb.step;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.gauss.recorder.SpeexPlayer;
import com.gauss.speex.encode.SpeexDecoder;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.callback.PathCallback;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.lib.net.itf.IRequestListener;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.FileUtil;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.cropimage.MyCropActivity;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.step.OnDatingVoiceDialog.UploadVoiceSuccListener;
import com.ilikezhibo.ggzb.step.adapter.FileAddAdapter;
import com.ilikezhibo.ggzb.step.adapter.JingJiaAddAdapter;
import com.ilikezhibo.ggzb.step.view.ListDialog;
import com.ilikezhibo.ggzb.step.view.OnListDialogItemClickListener;
import com.ilikezhibo.ggzb.step.view.SubWayDialogListener;
import com.ilikezhibo.ggzb.step.view.SubWayLineDialog;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.ModifyAvatarDialog;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import tinker.android.util.TinkerManager;

public class FaXiangMuStep1Activity extends BaseFragmentActivity implements
		OnClickListener, OnItemClickListener, UploadVoiceSuccListener,
		OnLongClickListener {

	private EditText title;
	private EditText bugget_money;
	private TextView type;
	private EditText detail_des;
	private EditText need_time;
	private GridView upload_pic_grid;
	private ImageView addphoto_img;
	private Button publishThemeVoiceBtn;
	public static final String IS_MODIFY_KEY = "IS_MODIFY";

	public static final String IS_IGNORD = "IS_IGNORD";

	@Override
	protected void setContentView() {
		setContentView(R.layout.step_faxiangmu_1_layout);
	}

	@Override
	protected void initializeViews() {
		title = (EditText) findViewById(R.id.title_et);
		bugget_money = (EditText) findViewById(R.id.bugget_money);
		type = (TextView) findViewById(R.id.type);
		type.setOnClickListener(this);
		detail_des = (EditText) findViewById(R.id.detail_des);
		need_time = (EditText) findViewById(R.id.need_time);
		upload_pic_grid = (GridView) findViewById(R.id.froum_create_grid);
		upload_file_grid = (GridView) findViewById(R.id.file_grid);
		// 添加相片
		addphoto_img = (ImageView) findViewById(R.id.addphoto_img);
		addphoto_img.setOnClickListener(this);

		addfile_img = (ImageView) findViewById(R.id.addfile_img);
		addfile_img.setOnClickListener(this);

		publishThemeVoiceBtn = (Button) this
				.findViewById(R.id.publishThemeVoiceBtn);
		publishThemeVoiceBtn.setOnClickListener(this);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);

		tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("发布项目");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("跳过");
		topRightBtn.setOnClickListener(this);
		// topRightBtn.setVisibility(View.GONE);

		boolean is_ignore = getIntent().getBooleanExtra(IS_IGNORD, false);

		if (is_ignore) {
			topRightBtn.setVisibility(View.GONE);
		} else {
			topRightBtn.setVisibility(View.VISIBLE);
		}

		next_button = (Button) findViewById(R.id.reg_bt);
		next_button.setOnClickListener(this);
	}

	@Override
	protected void initializeData() {

		// 修改
		xiangMuEntity = (XiangMuEntity) getIntent().getSerializableExtra(
				IS_MODIFY_KEY);
		if (xiangMuEntity != null) {

			title = (EditText) findViewById(R.id.title_et);
			bugget_money = (EditText) findViewById(R.id.bugget_money);
			type = (TextView) findViewById(R.id.type);
			type.setOnClickListener(this);
			detail_des = (EditText) findViewById(R.id.detail_des);
			need_time = (EditText) findViewById(R.id.need_time);
			upload_pic_grid = (GridView) findViewById(R.id.froum_create_grid);
			upload_file_grid = (GridView) findViewById(R.id.file_grid);
			// 添加相片
			addphoto_img = (ImageView) findViewById(R.id.addphoto_img);
			addphoto_img.setOnClickListener(this);

			addfile_img = (ImageView) findViewById(R.id.addfile_img);
			addfile_img.setOnClickListener(this);

			publishThemeVoiceBtn = (Button) this
					.findViewById(R.id.publishThemeVoiceBtn);
			publishThemeVoiceBtn.setOnClickListener(this);

			// 是修改
			tv_title.setText("修改项目");

			title.setText(xiangMuEntity.getTitle());
			bugget_money.setText(xiangMuEntity.getBudget());

			ProjectTypeEntity typeEntity = new ProjectTypeEntity();
			typeEntity.setId(xiangMuEntity.getCategory());
			typeEntity.setName(xiangMuEntity.getCategory_name());
			current_type = typeEntity;
			type.setText(xiangMuEntity.getCategory_name());

			detail_des.setText(xiangMuEntity.getMemo());

			need_time.setText(xiangMuEntity.getDuration());

			String[] urls = xiangMuEntity.getPic().split(",");
			for (String url : urls) {
				if (url != null && !url.equals("") && !url.equals(",")) {
					UpLoadFileEntity loadFileEntity = new UpLoadFileEntity();
					loadFileEntity.setUrl(url);
					pic_urls.add(loadFileEntity);
				}
			}

			urls = xiangMuEntity.getDocument().split(",");
			for (String url : urls) {
				if (url != null && !url.equals("") && !url.equals(",")) {
					UpLoadFileEntity loadFileEntity = new UpLoadFileEntity();
					loadFileEntity.setUrl(url);
					file_urls.add(loadFileEntity);
				}
			}

			modify_has_voice = true;
			publishThemeVoiceBtn.setText("播放语音");
			String voice = xiangMuEntity.getVoice();
			if (voice == null || voice.equals("")) {
				modify_has_voice = false;
			}

			next_button.setText("修改");
		}

		addAdapter = new JingJiaAddAdapter(this);
		addAdapter.setEntities(pic_urls);
		upload_pic_grid.setAdapter(addAdapter);
		upload_pic_grid.setOnItemClickListener(this);

		addFileAdapter = new FileAddAdapter(this);
		addFileAdapter.setEntities(file_urls);
		upload_file_grid.setAdapter(addFileAdapter);
		upload_file_grid.setOnItemClickListener(this);

	}

	private boolean modify_has_voice = false;
	private CustomDialog userBlackDialog;

	@Override
	public void onItemClick(final AdapterView<?> parent, View view,
			int position, long id) {
		final int index = position;
		// if (userBlackDialog == null) {
		userBlackDialog = new CustomDialog(this, new CustomDialogListener() {

			@Override
			public void onDialogClosed(int closeType) {

				switch (closeType) {
				case CustomDialogListener.BUTTON_POSITIVE:
					Trace.d("position :" + index);
					UpLoadFileEntity loadFileEntity = (UpLoadFileEntity) parent
							.getAdapter().getItem(index);
					pic_urls.remove(loadFileEntity);
					addAdapter.notifyDataSetChanged();
					break;
				}
			}
		});

		userBlackDialog.setCustomMessage("确定要删除此附件吗");
		userBlackDialog.setCancelable(true);
		userBlackDialog.setType(CustomDialog.DOUBLE_BTN);
		// }
		//
		// if (null != userBlackDialog) {
		userBlackDialog.show();
		// }

	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:
			Intent intent = new Intent(FaXiangMuStep1Activity.this,
					AULiveHomeActivity.class);
			startActivity(intent);
			break;
		case R.id.reg_bt:
			// 下一步
			if (docheck()) {
				if (xiangMuEntity != null) {
					// 修改
					doModify();
				} else {
					// 添加
					doSend();
				}
			}

			break;
		case R.id.type:
			getType();
			break;
		case R.id.addphoto_img:
			showDialog();
			break;
		case R.id.addfile_img:
			showFileChooser();
			break;

		case R.id.back:
			this.finish();
			break;
		case R.id.publishThemeVoiceBtn:// 主题语音播放

			if (modify_has_voice) {
				modify_has_voice = false;
				downFile(xiangMuEntity.getVoice());
			} else if (voiceLocalPath == null) {
				if (voiceDialog == null) {
					voiceDialog = new OnDatingVoiceDialog(this, this);
				}
				voiceDialog.show();
				voiceDialog.onResetRecordUIState(true);
			} else {
				if (sPlayer == null) {
					playVoice();
				} else if (sPlayer.isPaused()) {
					playVoice();
				} else {
					publishThemeVoiceBtn
							.setCompoundDrawablesWithIntrinsicBounds(
									R.drawable.ondate_voice_play, 0, 0, 0);
					sPlayer.stopPlay();
				}

				publishThemeVoiceBtn.setOnLongClickListener(this);
			}
			break;

		}
	}

	private void downFile(final String voiceUrl) {
		if (voiceUrl == null || voiceUrl.equals("")) {
			return;
		}
		Utils.showMessage("缓冲语音...");
		FileUtil.createDir(FileUtil.RECORD_PATH);
		RequestInformation request = new RequestInformation(voiceUrl,
				RequestInformation.REQUEST_METHOD_GET);
		request.setProgressChangeListener(new IProgressListener() {

			@Override
			public void progressChanged(int status, int progress,
					String operationName) {
				if (progress == 100) {

					publishThemeVoiceBtn
							.setCompoundDrawablesWithIntrinsicBounds(
									R.drawable.ondate_voice_pause, 0, 0, 0);
					String localVoicePath = FileUtil.RECORD_PATH
							+ getVoiceTag(voiceUrl);
					voiceLocalPath = localVoicePath;
					playVoice();
				}
			}
		});
		request.setCallback(new PathCallback() {

			@Override
			public void onFailure(AppException e) {

			}

			@Override
			public void onCallback(String callback) {
			}
		}.setFilePath(FileUtil.RECORD_PATH + getVoiceTag(voiceUrl)));
		request.execute();
	}

	// 获取语音tag
	private String getVoiceTag(String voiceUrl) {
		if (voiceUrl == null) {
			return "";
		}

		return voiceUrl.substring(voiceUrl.lastIndexOf("/") + 1);
	}

	private boolean isValueEt(EditText editText) {
		if (editText == null || editText.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private boolean isValueTV(TextView editText) {
		if (editText == null || editText.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private boolean docheck() {

		if (isValueEt(title) && isValueEt(bugget_money) && isValueTV(type)
				&& isValueEt(detail_des) && isValueEt(need_time)) {

			return true;
		}
		Utils.showMessage("请填写完整后再尝试");
		return false;

	}

	public static String PROJECT_ID = "PROJECT_ID_KEY";

	private void doSend() {

		// title 标题 budget 预算 category分类id
		// voice语音地址 pic 截图可以多个， memo文字描述(注：多余，作废) document 文档说明 duration
		// 项目时长

		// 数组转字符串
		String pics = "";
		if (pic_urls.size() > 0) {
			for (UpLoadFileEntity loadFileEntity : pic_urls) {
				pics += loadFileEntity.getUrl() + ",";
			}
			pics = pics.substring(0, pics.length() - 1);
		}

		Trace.d("/project/reg_send_project pics:" + pics);
		String files = "";
		if (file_urls.size() > 0) {
			for (UpLoadFileEntity loadFileEntity : file_urls) {
				files += loadFileEntity.getUrl() + ",";
			}
			files = files.substring(0, files.length() - 1);
		}
		Trace.d("/project/reg_send_project files:" + files);

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/project/reg_send_project",
				RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("title", title.getText().toString().trim());
		request.addPostParams("budget", bugget_money.getText().toString()
				.trim());
		request.addPostParams("category", current_type.getId());
		
		try {
			request.addPostParams("memo", URLEncoder.encode(detail_des
					.getText().toString().trim(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		request.addPostParams("document", files);
		request.addPostParams("duration", need_time.getText().toString().trim());
		request.addPostParams("pic", pics);

		AULiveApplication application = (AULiveApplication) TinkerManager.getTinkerApplicationLike();
		if (application.getAddress() != null) {
			request.addPostParams("lng", application.getLongitude() + "");
			request.addPostParams("lat", application.getLatitude() + "");
			try {
				request.addPostParams("addr",
						URLEncoder.encode(application.getAddress(), "utf-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		} else {
			request.addPostParams("lng", application.getLongitude() + "");
			request.addPostParams("lat", application.getLatitude() + "");
			request.addPostParams("addr", "");
		}

		if (voiceServerUrl != null) {
			request.addPostParams("voice", voiceServerUrl);
		} else {
			request.addPostParams("voice", "");
		}
		request.setCallback(new JsonCallback<ProjectTypeEntity>() {

			@Override
			public void onCallback(ProjectTypeEntity callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {
					Intent intent = new Intent(FaXiangMuStep1Activity.this,
							FaXiangMuStep2Activity.class);
					intent.putExtra(PROJECT_ID, callback.getId());
					startActivity(intent);
					FaXiangMuStep1Activity.this.finish();
					Utils.showMessage(callback.getMsg());
				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(ProjectTypeEntity.class));
		request.execute();
	}

	private void doModify() {

		// title 标题 budget 预算 category分类id
		// voice语音地址 pic 截图可以多个， memo文字描述(注：多余，作废) document 文档说明 duration
		// 项目时长

		// 数组转字符串
		String pics = "";
		if (pic_urls.size() > 0) {
			for (UpLoadFileEntity loadFileEntity : pic_urls) {
				pics += loadFileEntity.getUrl() + ",";
			}
			pics = pics.substring(0, pics.length() - 1);
		}

		String files = "";
		if (file_urls.size() > 0) {
			for (UpLoadFileEntity loadFileEntity : file_urls) {
				files += loadFileEntity.getUrl() + ",";
			}
			files = files.substring(0, files.length() - 1);
		}
		Trace.d("/project/reg_send_project files:" + files);

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/project/edit_project",
				RequestInformation.REQUEST_METHOD_POST);

		request.addPostParams("id", xiangMuEntity.getId());
		request.addPostParams("skills", "");

		request.addPostParams("title", title.getText().toString().trim());
		request.addPostParams("budget", bugget_money.getText().toString()
				.trim());
		request.addPostParams("category", current_type.getId());
		Trace.d("step faxiangmu memo:"+detail_des
				.getText().toString().trim());
		try {
			request.addPostParams("memo", URLEncoder.encode(detail_des
					.getText().toString().trim(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		request.addPostParams("document", files);
		request.addPostParams("duration", need_time.getText().toString().trim());
		request.addPostParams("pic", pics);

		if (voiceServerUrl != null) {
			request.addPostParams("voice", voiceServerUrl);
		} else {
			request.addPostParams("voice", "");
		}
		request.setCallback(new JsonCallback<ProjectTypeEntity>() {

			@Override
			public void onCallback(ProjectTypeEntity callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {
					Intent intent = new Intent(FaXiangMuStep1Activity.this,
							FaXiangMuStep2Activity.class);
					intent.putExtra(PROJECT_ID, callback.getId());
					startActivity(intent);
					FaXiangMuStep1Activity.this.finish();
					Utils.showMessage(callback.getMsg());
				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(ProjectTypeEntity.class));
		request.execute();
	}

	private void getType() {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/reg/pro_category", RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<ProjectTypeListEntity>() {

			@Override
			public void onCallback(ProjectTypeListEntity callback) {

				if (callback == null) {
					return;
				}
				if (callback.getStat() == 200) {

					ArrayList<ProjectTypeEntity> entities = callback.getList();

					if (entities != null && entities.size() > 0) {
						showPromptDialog(entities);
					}

				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage("获取网络数据失败");
			}
		}.setReturnType(ProjectTypeListEntity.class));
		request.execute();
	}

	SubWayLineDialog customDialog;

	private ProjectTypeEntity current_type = null;

	private void showPromptDialog(ArrayList<ProjectTypeEntity> types) {
		customDialog = new SubWayLineDialog(FaXiangMuStep1Activity.this,
				new SubWayDialogListener() {

					@Override
					public void onItemClick(ProjectTypeEntity t_type) {
						current_type = t_type;
						type.setText(t_type.getName());
						customDialog.dismiss();
					}
				}, types);

		customDialog.setCancelable(true);
		// customDialog.getWindow().setType(
		// WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		customDialog.show();
	}

	// 头像相关
	// 头像相关
	public static final String IMAGE_PATH = "BaoBao";
	private static String localTempImageFileName = "";
	private static final int FLAG_CHOOSE_IMG = 5;
	private static final int FLAG_CHOOSE_PHONE = 6;
	private static final int FLAG_MODIFY_FINISH = 7;
	public static final File FILE_SDCARD = Environment
			.getExternalStorageDirectory();
	public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
	public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL,
			"images/screenshots");

	// 上传的图片地址
	ArrayList<UpLoadFileEntity> pic_urls = new ArrayList<UpLoadFileEntity>();

	private JingJiaAddAdapter addAdapter;

	/**
	 * 显示选择对话框
	 */
	private void showDialog() { // 调用选择那种方式的dialog
		ModifyAvatarDialog modifyAvatarDialog = new ModifyAvatarDialog(
				FaXiangMuStep1Activity.this, false, false) {
			// 选择本地相册
			@Override
			public void doGoToImg() {
				this.dismiss();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, FLAG_CHOOSE_IMG);
			}

			// 选择相机拍照
			@Override
			public void doGoToPhone() {
				this.dismiss();
				String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {
					try {
						localTempImageFileName = "";
						localTempImageFileName = String.valueOf((new Date())
								.getTime()) + ".png";
						File filePath = FILE_PIC_SCREENSHOT;
						if (!filePath.exists()) {
							filePath.mkdirs();
						}
						Intent intent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						File f = new File(filePath, localTempImageFileName);
						// localTempImgDir和localTempImageFileName是自己定义的名字
						Uri u = Uri.fromFile(f);
						intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
						startActivityForResult(intent, FLAG_CHOOSE_PHONE);
					} catch (ActivityNotFoundException e) {
						//
					}
				}
			}
		};
		AlignmentSpan span = new AlignmentSpan.Standard(
				Layout.Alignment.ALIGN_CENTER);
		AbsoluteSizeSpan span_size = new AbsoluteSizeSpan(25, true);
		SpannableStringBuilder spannable = new SpannableStringBuilder();
		String dTitle = "请选择图片";
		spannable.append(dTitle);
		spannable.setSpan(span, 0, dTitle.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannable.setSpan(span_size, 0, dTitle.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		modifyAvatarDialog.setTitle(spannable);
		modifyAvatarDialog.show();
	}

	// 选取图片不再裁剪
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(FaXiangMuStep1Activity.this, "图片没找到", 0)
								.show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();

					Intent intent = new Intent(this, MyCropActivity.class);
					intent.putExtra("path", path);
					// startActivityForResult(intent, FLAG_MODIFY_FINISH);
					// 上传图片到服务器
					uploadFace(path);
				} else {

					Intent intent = new Intent(this, MyCropActivity.class);
					intent.putExtra("path", uri.getPath());
					// startActivityForResult(intent, FLAG_MODIFY_FINISH);
					// 上传图片到服务器
					uploadFace(uri.getPath());
				}
			}
		} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
			File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
			Intent intent = new Intent(this, MyCropActivity.class);
			intent.putExtra("path", f.getAbsolutePath());
			// startActivityForResult(intent, FLAG_MODIFY_FINISH);
			// 上传图片到服务器
			uploadFace(f.getAbsolutePath());
		} else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				// Bitmap b = BitmapFactory.decodeFile(path);
				// faceimagView.setImageBitmap(b);
				// 上传图片到服务器
				// uploadFace(path);
			}
		} else if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				String path = getPath(this, uri);
				// 上传附件word
				uploadFile(path);
				Trace.d("path:" + path);
			}
		}
	}

	public static String getPath(Context context, Uri uri) {

		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		}

		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高,原是320
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			// Bitmap photo = extras.getParcelable("data");
			// Drawable drawable = new BitmapDrawable(photo);
			// faceimagView.setImageDrawable(drawable);
		}
	}

	// 图片上传
	private void uploadFace(final String filePath) {
		RequestInformation request = new RequestInformation(
				UrlHelper.UP_VIDEO_IMG_URL,
				RequestInformation.REQUEST_METHOD_POST);

		request.isHttpClient = false;
		request.addHeader("Connection", "Keep-Alive");
		request.addHeader("Charset", "UTF-8");
		request.addHeader("Content-Type",
				"multipart/form-data;boundary=7d4a6d158c9");

		// request.addPostParams("id", newCreatedId);
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

		request.setCallback(new JsonCallback<UpLoadFileEntity>() {

			@Override
			public void onCallback(UpLoadFileEntity callback) {

				if (callback == null) {
					return;
				}
				if (callback.getStat() == 200) {

					// 保存已上传的图片名称
					pic_urls.add(callback);
					addAdapter.notifyDataSetChanged();
					// 上传成功
					Utils.showMessage("图片上传成功");

				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage("获取网络数据失败");
			}
		}.setReturnType(UpLoadFileEntity.class));
		request.execute();

	}

	// /////////////////////
	// 录音相关
	private int voiceTime;
	private String voiceLocalPath;// 语音路径
	private String voiceServerUrl;// 服务端语音地址
	private OnDatingVoiceDialog voiceDialog;
	private String[] play_voice_options_menu = { "播放", "重新录制", "删除", "取消" };
	private SpeexPlayer sPlayer;
	private static final int PLAY_COMPLETE = 1;

	// listener接收录音完成与上传后接收处理结果
	@Override
	public void onUploadOndateVoiceSucc(String localPath, String serverUrl,
			int time) {
		voiceTime = time;
		voiceLocalPath = localPath;
		voiceServerUrl = serverUrl;
		if (publishThemeVoiceBtn != null) {
			publishThemeVoiceBtn.setText(voiceTime + "s");
			publishThemeVoiceBtn.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.ondate_voice_play, 0, 0, 0);
		}
	}

	private ListDialog listDialog;

	private void setPlayVoiceDialog() {
		if (voiceDialog != null && voiceDialog.isShowing()) {
			return;
		}

		OnListDialogItemClickListener listener = null;

		if (listDialog == null) {
			listDialog = new ListDialog(this, listener);
			listDialog.setIsItemHorizontalCentre(true);
		}
		playVoiceMenuListener(listener, listDialog);
	}

	// 播放录音
	private void playVoiceMenuListener(OnListDialogItemClickListener listener,
			ListDialog listDialog) {
		// "播放", "重新录制", "删除", "取消"
		listener = new OnListDialogItemClickListener() {

			public void onItemClicked(int arg0) {
				switch (arg0) {
				case 0: // 播放
					playVoice();
					break;
				case 1:// 重新录制
					realseVoice();

					if (voiceDialog == null) {
						voiceDialog = new OnDatingVoiceDialog(
								FaXiangMuStep1Activity.this,
								FaXiangMuStep1Activity.this);
					}
					voiceDialog.onResetRecordUIState(true);
					voiceDialog.show();
					break;
				case 2:// 删除
					realseVoice();
					break;

				case 3:// 取消
					break;
				}
			}
		};
		listDialog.setItems(play_voice_options_menu);
		listDialog.setOnListDialogItemClickListener(listener);
		listDialog.show();
	}

	private void playVoice() {
		if (publishThemeVoiceBtn != null) {
			if (voiceLocalPath != null && FileUtil.isFileExist(voiceLocalPath)) {
				publishThemeVoiceBtn.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.ondate_voice_pause, 0, 0, 0);
				if (sPlayer == null) {
					sPlayer = new SpeexPlayer(voiceLocalPath);
				}

				sPlayer.startPlay();
				sPlayer.setSpeexCompletionListener(new SpeexDecoder.SpeexCompletionListener() {

					@Override
					public void onCompletion() {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = PLAY_COMPLETE;
						isRealseVoice = false;
						mHandler.sendMessage(msg);
					}
				});
			}
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PLAY_COMPLETE:// 播放完成
				if (sPlayer != null) {
					sPlayer.stopPlay();
				}

				if (publishThemeVoiceBtn == null) {
					return;
				}

				if (isRealseVoice) {
					return;
				}
				publishThemeVoiceBtn.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.ondate_voice_play, 0, 0, 0);
				break;
			}
			super.handleMessage(msg);
		}
	};

	private boolean isRealseVoice = false;

	private void realseVoice() {
		if (sPlayer != null) {
			if (!sPlayer.isPaused()) {
				sPlayer.stopPlay();
			}
			sPlayer = null;
		}

		isRealseVoice = true;

		if (FileUtil.isFileExist(voiceLocalPath)) {
			FileUtil.deleteFile(voiceLocalPath);
			voiceLocalPath = null;
			voiceServerUrl = null;
		}

		if (publishThemeVoiceBtn != null) {
			publishThemeVoiceBtn.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.ondate_voice_icon, 0, 0, 0);
			publishThemeVoiceBtn.setText("添加语音");
		}
	}

	@Override
	public boolean onLongClick(View v) {
		setPlayVoiceDialog();
		return false;
	}

	// 上传附件相关
	private GridView upload_file_grid;
	private FileAddAdapter addFileAdapter;
	ArrayList<UpLoadFileEntity> file_urls = new ArrayList<UpLoadFileEntity>();
	private static final int FILE_SELECT_CODE = 0;
	private ImageView addfile_img;
	private TextView tv_title;
	private Button next_button;
	private XiangMuEntity xiangMuEntity;

	/** 调用文件选择软件来选择文件 **/
	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
					FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with reset Dialog
			Toast.makeText(FaXiangMuStep1Activity.this, "请安装文件管理器",
					Toast.LENGTH_SHORT).show();
		}
	}

	// 附件上传
	private void uploadFile(final String filePath) {

		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/msg/updocument", RequestInformation.REQUEST_METHOD_POST);

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

		request.setCallback(new JsonCallback<UpLoadFileEntity>() {

			@Override
			public void onCallback(UpLoadFileEntity callback) {

				if (callback == null) {
					return;
				}
				if (callback.getStat() == 200) {

					// 保存已上传的附件名称
					file_urls.add(callback);
					addFileAdapter.notifyDataSetChanged();
					// 上传成功
					Utils.showMessage("附件上传成功");

				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage("获取网络数据失败");
			}
		}.setReturnType(UpLoadFileEntity.class));
		request.execute();

	}

}
