package com.ilikezhibo.ggzb.register;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.itf.IRequestListener;
import com.jack.utils.BtnClickUtils;
import com.jack.utils.DateUtils;
import com.jack.utils.FileUtil;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.cropimage.MyCropActivity;
import com.ilikezhibo.ggzb.entity.LoginUserEntity;
import com.ilikezhibo.ggzb.entity.UserInfo;
import com.ilikezhibo.ggzb.home.TitleNavView.TitleListener;
import com.ilikezhibo.ggzb.login.LoginActivity;
import com.ilikezhibo.ggzb.register.entity.UpLoadFaceEntity;
import com.ilikezhibo.ggzb.userinfo.MyProFileActivity;
import com.ilikezhibo.ggzb.views.BirthDayPickDialog;
import com.ilikezhibo.ggzb.views.CustomDialog;
import com.ilikezhibo.ggzb.views.CustomDialogListener;
import com.ilikezhibo.ggzb.views.ModifyAvatarDialog;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import tinker.android.util.TinkerManager;

public class RegisterActivity extends BaseFragmentActivity
        implements TitleListener, OnClickListener {
   private static final int FACE_TYPE = 0;
   private static final int PIC_TYPE = 1;
   private static final int SEX_TYPE = 2;

   private String faceUrl = null;

   private double longitude;// 经度
   private double latitude;// 纬度
   private String city;
   // private Bitmap bitmap;
   // private String nickname = null;
   // private String sex = null;
   private String birthday = null;

   private LinearLayout birthdayLayout;
   private TextView birthdayTv;
   private EditText nameEt;
   private EditText signature_et;
   private RadioButton melaRb;
   private RadioButton femaleRb;

   private ImageView faceimagView;
   private ProgressBar uploadProgressTv;

   public static final String INFO_MODIFY_KEY = "INFO_MODIFY_KEY";

   @Override protected void setContentView() {
      setContentView(R.layout.reg_first_layout);

      getLocation();
   }

   private boolean back_home = false;

   @Override protected void initializeViews() {

      back_home = getIntent().getBooleanExtra(MyProFileActivity.back_home_key, false);

      Button rl_back = (Button) this.findViewById(R.id.back);
      rl_back.setOnClickListener(this);
      rl_back.setVisibility(View.VISIBLE);

      TextView tv_title = (TextView) this.findViewById(R.id.title);
      tv_title.setText("注册(2/2)");

      TextView topRightBtn = (TextView) this.findViewById(R.id.topRightBtn);
      topRightBtn.setText("下一步");
      topRightBtn.setOnClickListener(this);

      faceimagView = (ImageView) findViewById(R.id.upFaceImg);
      faceimagView.setOnClickListener(this);

      birthdayLayout = (LinearLayout) findViewById(R.id.birthdayLayout);
      birthdayLayout.setOnClickListener(this);
      birthdayTv = (TextView) findViewById(R.id.birthdayTv);
      nameEt = (EditText) findViewById(R.id.nameEt);
      signature_et = (EditText) findViewById(R.id.signature_et);

      melaRb = (RadioButton) findViewById(R.id.melaRb);
      femaleRb = (RadioButton) findViewById(R.id.femaleRb);

      uploadProgressTv = (ProgressBar) findViewById(R.id.downProgressTv);
      uploadProgressTv.setVisibility(View.GONE);

      melaRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

         @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
               femaleRb.setChecked(false);
            }
         }
      });
      femaleRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

         @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
               melaRb.setChecked(false);
            }
         }
      });
      String regUid =
              SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.REG_UID, "");
      if (regUid != null && regUid.trim().length() > 0) {
         faceUrl = AULiveApplication.getUserInfo().getFace();
         faceUrl = Utils.getImgUrl(faceUrl);
         if (faceUrl == null || faceUrl.trim().length() <= 0) {
            findViewById(R.id.upFaceImg).setOnClickListener(this);
         } else {
            Trace.d("faceUrl:" + faceUrl);
            ImageLoader.getInstance()
                    .displayImage(faceUrl, faceimagView, AULiveApplication.getGlobalImgOptions());
         }
      }

      // 默认生日
      birthday = "1990-01-01";
      birthdayTv.setText(birthday);

      // 作如果是修改的init
      boolean modify = getIntent().getBooleanExtra(INFO_MODIFY_KEY, false);
      if (modify) {
         tv_title.setText("修改资料");
         topRightBtn.setText("修改");
         LoginUserEntity userEntity = AULiveApplication.getUserInfo();
         if (userEntity != null) {
            String f_u = userEntity.getFace();
            f_u = Utils.getImgUrl(f_u);
            ImageLoader.getInstance()
                    .displayImage(f_u, faceimagView, AULiveApplication.getGlobalImgOptions());
            faceUrl = f_u;

            nameEt.setText(userEntity.getNickname());
            signature_et.setText(userEntity.signature);

            String tem_bir = userEntity.getBirthday();
            Trace.d("tem_bir:" + tem_bir);
            Date date = DateUtils.getDate2(tem_bir);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String bir = format.format(date);
            birthday = bir;
            birthdayTv.setText(bir);

            if (userEntity.getSex() == 1) {
               melaRb.setChecked(true);
            } else {
               femaleRb.setChecked(true);
            }
         }
      }
   }

   @Override protected void initializeData() {

   }

   /** 头部左上返回 */
   @Override public void onBack() {
      if (uploadProgressTv.getVisibility() == View.VISIBLE) {
         Utils.showMessage("正在上传头像,请稍候...");
         return;
      }
      this.finish();
   }

   /** 头部右上的事件 */
   @Override public void onTopRightEvent() {
      if (uploadProgressTv.getVisibility() == View.VISIBLE) {
         Utils.showMessage("正在上传头像,请稍候...");
         return;
      }
      doRegister();
   }

   @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK && uploadProgressTv.getVisibility() == View.VISIBLE) {
         Utils.showMessage("正在上传头像,请稍候...");
         return true;
      }

      return super.onKeyDown(keyCode, event);
   }

   @Override public void onClick(View v) {
      if (BtnClickUtils.isFastDoubleClick()) {
         return;
      }

      switch (v.getId()) {
         case R.id.upFaceImg:
            // 头像修改
            showDialog();
            break;
         case R.id.topRightBtn:
            doRegister();
            // Intent intent = new Intent(RegisterActivity.this,
            // StepMainActivity.class);
            // startActivity(intent);
            break;
         case R.id.birthdayLayout:
            showBirthDayDialog();
            break;
         case R.id.back:
            this.finish();
            break;
      }
   }

   private void showPromptDialog(String msg, final int type) {
      CustomDialog customDialog = new CustomDialog(this, new CustomDialogListener() {

         @Override public void onDialogClosed(int closeType) {
            switch (closeType) {
               case CustomDialogListener.BUTTON_POSITIVE:
                  if (type == FACE_TYPE) {
                     // 头像修改
                     showDialog();
                  } else if (type == PIC_TYPE) {
                     showBirthDayDialog();
                  }
                  break;
            }
         }
      });

      customDialog.setCustomMessage(msg);
      customDialog.setCancelable(true);
      customDialog.setType(CustomDialog.SINGLE_BTN);
      customDialog.show();
   }

   // 注册
   private void doRegister() {
      if ((faceUrl == null || faceUrl.trim().length() <= 0)) {
         showPromptDialog("请上传您的个人形象照", FACE_TYPE);
         return;
      }

      if (nameEt.getText().toString().trim().length() <= 0) {
         Utils.showMessage("昵称不能为空！请填写您的昵称！");
         nameEt.requestFocus();
         return;
      }

      if (birthday == null || birthday.trim().length() <= 0) {
         showPromptDialog("请选择您的生日！", PIC_TYPE);
         return;
      }

      if (!femaleRb.isChecked() && !melaRb.isChecked()) {
         Utils.showMessage("请选择性别！");
         return;
      }

      if (signature_et.getText().toString().trim().length() <= 0) {
         Utils.showMessage("签名不能为空！请填写您的签名！");
         signature_et.requestFocus();
         return;
      }

      showProgressDialog("正在处理，请稍候...");

      RequestInformation request =
              new RequestInformation(UrlHelper.REG_DETAIL_URL, RequestInformation.REQUEST_METHOD_POST);

      String name=   nameEt.getText().toString();
      request.addPostParams("nickname", name  );

      //try {
      //   String nickname = URLEncoder.encode(nameEt.getText().toString(), "ISO8859-1");
      //   Trace.d("nickname" + nickname);
      //   request.addPostParams("nickname", nickname);
      //} catch (Exception e) {
      //   e.printStackTrace();
      //}

      if (femaleRb.isChecked()) {
         request.addPostParams("sex", 2 + "");
      } else {
         request.addPostParams("sex", 1 + "");
      }
      request.addPostParams("birthday", birthday);

      request.addPostParams("face", faceUrl);

      request.addPostParams("signature", signature_et.getText().toString());

      if (longitude != 0 || latitude != 0) {
         Trace.d("longitute:" + longitude + " latitude:" + latitude);
         request.addPostParams("longitude", longitude + "");
         request.addPostParams("latitude", latitude + "");
         try {
            if(city==null){
               city="";
            }
            request.addPostParams("city", URLEncoder.encode(city, "utf-8"));

         } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
         }
      }

      request.setCallback(new JsonCallback<UserInfo>() {

         @Override public void onCallback(UserInfo callback) {
            cancelProgressDialog();
            if (callback == null) {
               Utils.showMessage(Utils.trans(R.string.get_info_fail));
               return;
            }

            if (callback.getStat() == 200) {
               int step = callback.getStep();
               Trace.d("reg step:" + step);
               SharedPreferenceTool.getInstance()
                       .saveBoolean(SharedPreferenceTool.REG_FINISH_FIRST, true);
               if (callback.getUserinfo() != null) {
                  AULiveApplication.setUserInfo(callback.getUserinfo());
                  AULiveApplication.setMyselfUserInfo(callback.getUserinfo().getUid(),
                          callback.getUserinfo().getNickname(), callback.getUserinfo().getFace(),
                          callback.getUserSig(), callback.getUserinfo().getMsg_tip(), callback.getUserinfo().getPrivate_chat_status());
               }

               // SocketManager.getInstance(RegisterActivity.this).shutdown();
               // DealSocketMsg dealSocketMsg = new DealSocketMsg();
               // dealSocketMsg.setNotifyContext(RegisterActivity.this);
               // // dealSocketMsg.setListener(this);
               // dealSocketMsg.startSocket();

               // unBindService();
               //bindService();

               if (back_home) {
                  RegisterActivity.this.finish();
               } else {
                  Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                  startActivity(intent);
                  RegisterActivity.this.finish();
               }
               // // 测试
               // Intent intent1 = new Intent(Car1Animation.this,
               // OldLoginActivity.class);
               // intent1.putExtra("first_time", true);
               // startActivity(intent1);
               // Car1Animation.this.finish();
            } else {
               // 当返回不是200时，清空cookie
               //AULiveApplication.removeAllCookie();
               //SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.COOKIE_KEY, "");

               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            cancelProgressDialog();
            Utils.showMessage(Utils.trans(R.string.net_error));
         }
      }.setReturnType(UserInfo.class));
      request.execute();
   }

   private void showBirthDayDialog() {
      final BirthDayPickDialog bithdayDialog =
              new BirthDayPickDialog(RegisterActivity.this, birthday);
      bithdayDialog.setTitle("选择出生日期");
      bithdayDialog.setButton("取消", new DialogInterface.OnClickListener() {

         @Override public void onClick(DialogInterface dialog, int which) {
            bithdayDialog.cancel();
         }
      }, "确认", new DialogInterface.OnClickListener() {

         @Override public void onClick(DialogInterface dialog, int which) {
            String text = bithdayDialog.getBirthDay();
            if (text == null) {

            } else {
               birthday = text;
               birthdayTv.setText(text);
               bithdayDialog.dismiss();
            }
         }
      });
      bithdayDialog.show();
   }

   // 获取当前位置
   private void getLocation() {
      AULiveApplication application = (AULiveApplication) TinkerManager.getTinkerApplicationLike();

      longitude = application.getLongitude();
      latitude = application.getLatitude();
      city = application.getCity();
   }

   ///////////////////////////////

   // 头像相关
   public static final String IMAGE_PATH = "LazyPicture";
   private static String localTempImageFileName = "";
   private static final int FLAG_CHOOSE_IMG = 5;
   private static final int FLAG_CHOOSE_PHONE = 6;
   private static final int FLAG_MODIFY_FINISH = 7;
   public static final File FILE_SDCARD = Environment.getExternalStorageDirectory();
   public static final File FILE_LOCAL = new File(FILE_SDCARD, IMAGE_PATH);
   public static final File FILE_PIC_SCREENSHOT = new File(FILE_LOCAL, "images/screenshots");

   /**
    * 显示选择对话框
    */
   private void showDialog() { // 调用选择那种方式的dialog
      ModifyAvatarDialog modifyAvatarDialog = new ModifyAvatarDialog(this, false, false) {
         // 选择本地相册
         @Override public void doGoToImg() {
            this.dismiss();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, FLAG_CHOOSE_IMG);
         }

         // 选择相机拍照
         @Override public void doGoToPhone() {
            this.dismiss();
            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) {
               try {
                  localTempImageFileName = "";
                  localTempImageFileName = String.valueOf((new Date()).getTime()) + ".png";
                  File filePath = FILE_PIC_SCREENSHOT;
                  if (!filePath.exists()) {
                     filePath.mkdirs();
                  }
                  Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
      AlignmentSpan span = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);
      AbsoluteSizeSpan span_size = new AbsoluteSizeSpan(25, true);
      SpannableStringBuilder spannable = new SpannableStringBuilder();
      String dTitle = "请选择图片";
      spannable.append(dTitle);
      spannable.setSpan(span, 0, dTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      spannable.setSpan(span_size, 0, dTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      modifyAvatarDialog.setTitle(spannable);
      modifyAvatarDialog.show();
   }

   @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
         if (data != null) {
            Uri uri = data.getData();
            if (uri == null) {
               return;
            }
            if (!TextUtils.isEmpty(uri.getAuthority())) {
               Cursor cursor =
                       getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA },
                               null, null, null);
               if (null == cursor) {
                  Toast.makeText(RegisterActivity.this, "图片没找到", Toast.LENGTH_SHORT).show();
                  return;
               }
               cursor.moveToFirst();
               String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
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
//             Bitmap b = BitmapFactory.decodeFile(path);
//             faceimagView.setImageBitmap(b);
            // 上传图片到服务器
            uploadFace(path);
         }
      }
   }

   // 图片上传
   private void uploadFace(final String filePath) {
      Trace.d("filePath:" + filePath);
      if (uploadProgressTv.getVisibility() == View.VISIBLE) {
         return;
      }
      uploadProgressTv.setVisibility(View.VISIBLE);

      RequestInformation request = new RequestInformation(
              UrlHelper.REG_UP_PHOTO_URL + "?uid=" + AULiveApplication.getUserInfo().getUid(),
              RequestInformation.REQUEST_METHOD_POST);

      request.isHttpClient = false;
      request.addHeader("Connection", "Keep-Alive");
      request.addHeader("Charset", "UTF-8");
      request.addHeader("Content-Type", "multipart/form-data;boundary=7d4a6d158c9");

      request.setRequestListener(new IRequestListener() {
         @Override public boolean onPrepareParams(OutputStream out) throws AppException {
            String BOUNDARY = "7d4a6d158c9"; // 数据分隔线
            DataOutputStream outStream = new DataOutputStream(out);
            try {
               outStream.writeBytes("--" + BOUNDARY + "\r\n");
               outStream.writeBytes(
                       "Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
                               + filePath.substring(filePath.lastIndexOf("/") + 1)
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

         @Override public void onPreExecute() {
         }

         @Override public void onPostExecute() {
         }

         @Override public void onCancelled() {
         }

         @Override public void onBeforeDoingBackground() {
         }

         @Override public Object onAfterDoingBackground(Object object) {
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

         @Override public void onCallback(UpLoadFaceEntity callback) {
            uploadProgressTv.setVisibility(View.GONE);
            if (callback == null) {
               return;
            }
            Trace.d("callback:" + callback.getFace());
            if (callback.getStat() == 200) {
               String face = callback.getFace();
               final String face2 = Utils.getImgUrl(face);

               Message msg = new Message();
               msg.what = REFRESH_IMG;
               msg.obj = face2;
               mHandler_timer.sendMessage(msg);

               // 上传成功
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {
            uploadProgressTv.setVisibility(View.GONE);
            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(UpLoadFaceEntity.class));
      request.execute();
   }

   public static final int REFRESH_IMG = 122;
   private Handler mHandler_timer = new Handler(new Handler.Callback() {
      @Override public boolean handleMessage(Message msg) {
         switch (msg.what) {
            case REFRESH_IMG:
               String face2 = (String) msg.obj;
               DisplayImageOptions options =
                       new DisplayImageOptions.Builder().showStubImage(R.drawable.face_male)
                               .showImageForEmptyUri(R.drawable.face_male)
                               .showImageOnFail(R.drawable.face_male)
                               .cacheInMemory()
                               .cacheOnDisc()
                               .build();

               ImageLoader.getInstance().getMemoryCache().remove(face2);
               ImageLoader.getInstance().getDiskCache().remove(face2);
               ImageLoader.getInstance().getDiskCache().get(face2).delete();
               ImageLoader.getInstance().getDiskCache().get(face2).deleteOnExit();
               ImageLoader.getInstance().loadImage(face2, new ImageLoadingListener() {
                  @Override public void onLoadingStarted(String s, View view) {

                  }

                  @Override
                  public void onLoadingFailed(String s, View view, FailReason failReason) {

                  }

                  @Override public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                     faceimagView.setImageBitmap(bitmap);
                  }

                  @Override public void onLoadingCancelled(String s, View view) {

                  }
               });
               //ImageLoader.getInstance().displayImage(face2, faceimagView, options);
               faceUrl = face2;
               LoginUserEntity userinfo = AULiveApplication.getUserInfo();
               if (userinfo != null) {
                  userinfo.setFace(face2);
                  AULiveApplication.setUserInfo(userinfo);
               }

               break;
         }
         return false;
      }
   });

   @Override protected void onDestroy() {
      super.onDestroy();

      // if (FileUtil.isFileExist(FileUtil.PHOTO_TEMP_PATH)) {
      // FileUtil.deleteFile(FileUtil.PHOTO_TEMP_PATH);
      // }
      FileUtil.deleteFileDir(FileUtil.ROOTPATH + File.separator + FileUtil.USER_PHOTO_IMG, true);
   }
}
