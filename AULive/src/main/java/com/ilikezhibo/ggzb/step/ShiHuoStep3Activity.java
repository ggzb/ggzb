package com.ilikezhibo.ggzb.step;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.itf.IRequestListener;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.JsonParser;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.cropimage.MyCropActivity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.register.entity.UpLoadFaceEntity;
import com.ilikezhibo.ggzb.userinfo.MyProFileActivity;
import com.ilikezhibo.ggzb.views.ModifyAvatarDialog;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuEntity;
import com.ilikezhibo.ggzb.xiangmu.entity.XiangMuListEntity;
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
import java.util.HashMap;

public class ShiHuoStep3Activity extends BaseFragmentActivity implements
		OnClickListener {

	private LinearLayout main_layout;

	@Override
	protected void setContentView() {
		setContentView(R.layout.step_shihuo_3_layout);
	}

	private boolean back_home = false;

	@Override
	protected void initializeViews() {

		back_home = getIntent().getBooleanExtra(
				MyProFileActivity.back_home_key, false);

		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);
		rl_back.setVisibility(View.VISIBLE);
		main_layout = (LinearLayout) this.findViewById(R.id.main_layout);


		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("项目案例");

		TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
		topRightBtn.setText("下一步");
		topRightBtn.setOnClickListener(this);
		topRightBtn.setVisibility(View.GONE);

		Button resetPwdBtn = (Button) findViewById(R.id.reg_bt);
		resetPwdBtn.setOnClickListener(this);

		//如果为修改
				if (back_home == true) {
					resetPwdBtn.setText("修改");
				}

				
		Button add_more_bt = (Button) findViewById(R.id.add_more_bt);
		add_more_bt.setOnClickListener(this);

	}

	@Override
	protected void initializeData() {
		getMyProducs();
	}

	@Override
	public void onClick(View v) {
		if (BtnClickUtils.isFastDoubleClick()) {
			return;
		}

		switch (v.getId()) {
		case R.id.topRightBtn:

			break;
		case R.id.reg_bt:
			dosend();
			// Intent intent = new Intent(ShiHuoStep3Activity.this,
			// RegisterActivity.class);
			// startActivity(intent);
			break;
		case R.id.add_more_bt:

			add_more();

			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	private void add_more() {
		final View edu_item = LayoutInflater.from(this).inflate(
				R.layout.step_shihuo_edu_item, null);
		edu_item.findViewById(R.id.city_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						main_layout.removeView(edu_item);
						Utils.showMessage("删除成功");
					}
				});
		final ImageView addphoto_img = (ImageView) edu_item
				.findViewById(R.id.addphoto_img);
		final EditText app_icon = (EditText) edu_item
				.findViewById(R.id.app_icon);
		addphoto_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				showDialog(addphoto_img, app_icon);
			}
		});

		main_layout.addView(edu_item);
	}

	private boolean isValueEt(EditText editText) {
		if (editText == null || editText.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	private void dosend() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		int count = main_layout.getChildCount();
		if (count <= 0) {
			Utils.showMessage("最少有一个项目案例");
			return;
		}
		for (int i = 0; i < count; i++) {
			View view = main_layout.getChildAt(i);
			EditText app_name = (EditText) view.findViewById(R.id.app_name);
			EditText app_icon = (EditText) view.findViewById(R.id.app_icon);
			EditText app_url = (EditText) view.findViewById(R.id.app_url);
			EditText memo = (EditText) view.findViewById(R.id.memo);

			if (isValueEt(app_name) && isValueEt(app_icon)
					&& isValueEt(app_url) && isValueEt(memo)) {
				// do something
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("app_name", app_name.getText().toString());
				map.put("app_icon", app_icon.getText().toString());
				map.put("app_url", app_url.getText().toString());
				map.put("memo", memo.getText().toString());
				list.add(map);
			} else {
				Utils.showMessage("请填写完整后再尝试");
				return;
			}
		}
		String json = null;

		try {
			json = JsonParser.serializeToJson(list);
			json = URLEncoder.encode(json, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		Trace.d("json:" + json);
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/project/example_project",
				RequestInformation.REQUEST_METHOD_POST);
		request.addPostParams("list", json);
		if (back_home) {
			request.addPostParams("step", "2");
		}
		request.setCallback(new JsonCallback<UserInfo>() {

			@Override
			public void onCallback(UserInfo callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {

					if (callback.getUserinfo() != null) {
						AULiveApplication.setUserInfo(callback.getUserinfo());
					}
					if (back_home) {
						ShiHuoStep3Activity.this.finish();
					} else {
						Intent intent = new Intent(ShiHuoStep3Activity.this,
								ShiHuoStep4Activity.class);
						startActivity(intent);
						Utils.showMessage(callback.getMsg());
					}
				} else {

					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(UserInfo.class));
		request.execute();
	}

	// /////////////////////////////

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

	private ImageView current_imageView = null;
	private EditText current_editText = null;

	/**
	 * 显示选择对话框
	 */
	private void showDialog(final ImageView imageView, final EditText editText) { // 调用选择那种方式的dialog
		ModifyAvatarDialog modifyAvatarDialog = new ModifyAvatarDialog(this,
				false, false) {
			// 选择本地相册
			@Override
			public void doGoToImg() {
				this.dismiss();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, FLAG_CHOOSE_IMG);

				current_imageView = imageView;
				current_editText = editText;
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

						current_imageView = imageView;
						current_editText = editText;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (uri == null) {
					return;
				}
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(ShiHuoStep3Activity.this, "图片没找到", 0)
								.show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();

					Intent intent = new Intent(this, MyCropActivity.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {

					Intent intent = new Intent(this, MyCropActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
			File f = new File(FILE_PIC_SCREENSHOT, localTempImageFileName);
			Intent intent = new Intent(this, MyCropActivity.class);
			intent.putExtra("path", f.getAbsolutePath());
			startActivityForResult(intent, FLAG_MODIFY_FINISH);
		} else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				final String path = data.getStringExtra("path");
				// Bitmap b = BitmapFactory.decodeFile(path);
				// faceimagView.setImageBitmap(b);
				// 上传图片到服务器
				uploadFace(path);
			}
		}
	}

	// 图片上传
	private void uploadFace(final String filePath) {
		Trace.d("filePath:" + filePath);

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
		// request.setProgressChangeListener(new IProgressListener() {
		//
		// @Override
		// public void progressChanged(int status, int progress,
		// String operationName) {
		// Trace.d("progress:" + progress);
		// if (progress == 100) {
		// downProgressTv.setVisibility(View.GONE);
		// } else {
		// downProgressTv.setProgress(progress);
		// downProgressTv.setVisibility(View.VISIBLE);
		// }
		// }
		// });
		request.setCallback(new JsonCallback<UpLoadFaceEntity>() {

			@Override
			public void onCallback(UpLoadFaceEntity callback) {

				if (callback == null) {
					return;
				}
				Trace.d("callback:" + callback.getUrl());
				if (callback.getStat() == 200) {
					String face = callback.getUrl();

					if (current_editText == null || current_imageView == null)
						return;
					current_editText.setText(face);

					ImageLoader.getInstance().displayImage(
							Utils.getImgUrl(face), current_imageView,
							AULiveApplication.getGlobalImgOptions());

					// 上传成功
				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				Utils.showMessage("获取网络数据失败");
			}
		}.setReturnType(UpLoadFaceEntity.class));
		request.execute();
	}

	private void getMyProducs() {
		RequestInformation request = new RequestInformation(UrlHelper.URL_HEAD
				+ "/project/my_list?example=1",
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<XiangMuListEntity>() {

			@Override
			public void onCallback(XiangMuListEntity callback) {
				if (callback == null) {

					Utils.showMessage(Utils.trans(R.string.get_info_fail));
					return;
				}
				if (callback.getStat() == 200) {

					ArrayList<XiangMuEntity> entities = callback.getList();

					if (entities.size() > 0) {
						main_layout.removeAllViews();
						for (XiangMuEntity xiangMuEntity : entities) {
							final View edu_item = LayoutInflater.from(
									ShiHuoStep3Activity.this).inflate(
									R.layout.step_shihuo_edu_item, null);
							edu_item.findViewById(R.id.city_close)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View view) {
											main_layout.removeView(edu_item);
											Utils.showMessage("删除成功");
										}
									});
							final ImageView addphoto_img = (ImageView) edu_item
									.findViewById(R.id.addphoto_img);
							final EditText app_icon = (EditText) edu_item
									.findViewById(R.id.app_icon);
							addphoto_img
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View view) {
											showDialog(addphoto_img, app_icon);
										}
									});

							main_layout.addView(edu_item);

							ImageLoader.getInstance().displayImage(
									xiangMuEntity.getPic(), addphoto_img,
									AULiveApplication.getGlobalImgOptions());
							app_icon.setText(xiangMuEntity.getPic());

							EditText app_name = (EditText) edu_item
									.findViewById(R.id.app_name);
							app_name.setText(xiangMuEntity.getTitle());

							EditText app_url = (EditText) edu_item
									.findViewById(R.id.app_url);
							app_url.setText(xiangMuEntity.getUrl());

							EditText memo = (EditText) edu_item
									.findViewById(R.id.memo);
							memo.setText(xiangMuEntity.getMemo());
						}
					} else {
						// 当没有时，初始化默认的
						View first_item = (View) findViewById(R.id.first_item);
						first_item.findViewById(R.id.city_close).setVisibility(
								View.INVISIBLE);
						// first_item.findViewById(R.id.city_close).setOnClickListener(
						// new OnClickListener() {
						//
						// @Override
						// public void onClick(View view) {
						// main_layout.removeView(first_item);
						// Utils.showMessage("删除成功");
						// }
						// });
						final ImageView addphoto_img = (ImageView) first_item
								.findViewById(R.id.addphoto_img);
						final EditText app_icon = (EditText) first_item
								.findViewById(R.id.app_icon);
						addphoto_img.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								showDialog(addphoto_img, app_icon);
							}
						});
					}
				} else {

					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {

				Utils.showMessage(Utils.trans(R.string.get_info_fail));
			}
		}.setReturnType(XiangMuListEntity.class));
		request.execute();
	}

}
