package com.ilikezhibo.ggzb.avsdk.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.lib.net.callback.StringCallback;
import com.jack.lib.net.itf.IRequestListener;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.avsdk.ChatMsgListAdapter;
import com.ilikezhibo.ggzb.avsdk.UserInfo;
import com.ilikezhibo.ggzb.cropimage.MyCropActivity;
import com.ilikezhibo.ggzb.register.entity.UpLoadFaceEntity;
import com.ilikezhibo.ggzb.views.CustomProgressDialog;
import com.ilikezhibo.ggzb.views.ModifyAvatarDialog;
import com.ilikezhibo.ggzb.wxapi.ShareHelper;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import tinker.android.util.TinkerManager;

/**
 * 发布直播的直播
 */
public class ReleaseLiveActivity extends BaseActivity
    implements TextWatcher, View.OnClickListener, IWeiboHandler.Response {

   private UserInfo mSelfUserInfo;
   private EditText mEditTextLiveTitle;
   private ImageButton mImageButtonLiveCover;
   private ImageButton mImageButtonCloseLiveCover;
   private Button mButtonShow;
   private String mLiveTitleString = "";
   private String coverPath;
   private String userPhone = "123";
   private ImageButton mImageButtonCloseRelease;
   private CheckBox weibo_cb;
   private CheckBox weixin_cb;
   private CheckBox friend_circle_cb;
   private CheckBox qq_cb;
   private CheckBox qzone_cb;
   private static final int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;

   private boolean is_doing_share = false;
   //因为sina登录后分享会回两次onresum,is_doing_sina_login用于过滤第一次onresum
   public static boolean is_doing_sina_login = false;
   private Bitmap background_bitmap;

   @Override public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.release_live_activity);
      initViewUI();
   }

   @Override public void onDestroy() {
      super.onDestroy();
      if (shareDialog != null) {
         ReleaseLiveActivity.this.unregisterReceiver(shareDialog.getShareReceiver());
         shareDialog.dismiss();
         shareDialog = null;
      }

      if (background_bitmap != null && !background_bitmap.isRecycled()) {
         background_bitmap.recycle();  //回收图片所占的内存
      }
   }

   private void initViewUI() {

      //设置背景图
      ImageView bg_img = (ImageView) findViewById(R.id.bg_img);
      background_bitmap = ChatMsgListAdapter.readBitMap(this, R.drawable.open_show_room);
      bg_img.setImageBitmap(background_bitmap);
      //if (!bitmap.isRecycled()) {
      //   bitmap.recycle();  //回收图片所占的内存
      //}

      mImageButtonCloseRelease = (ImageButton) findViewById(R.id.close_release);
      mImageButtonCloseRelease.setOnClickListener(this);
      mEditTextLiveTitle = (EditText) findViewById(R.id.live_title);
      mButtonShow = (Button) findViewById(R.id.btn_show);
      mImageButtonLiveCover = (ImageButton) findViewById(R.id.live_cover);
      mImageButtonCloseLiveCover = (ImageButton) findViewById(R.id.close_live_cover);
      mButtonShow.setOnClickListener(this);
      mEditTextLiveTitle.setOnClickListener(this);
      mEditTextLiveTitle.addTextChangedListener(this);
      mImageButtonLiveCover.setOnClickListener(this);
      mImageButtonCloseLiveCover.setOnClickListener(this);

      AULiveApplication mAULiveApplication =
          (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      mSelfUserInfo = mAULiveApplication.getMyselfUserInfo();
      userPhone = mSelfUserInfo.getUserPhone();
      coverPath = null;
      uploadProgressTv = (ProgressBar) findViewById(R.id.downProgressTv);

      //分享相关

      weibo_cb = (CheckBox) findViewById(R.id.weibo_cb);
      weibo_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               weixin_cb.setChecked(false);
               friend_circle_cb.setChecked(false);
               qq_cb.setChecked(false);
               qzone_cb.setChecked(false);
            }
         }
      });
      weixin_cb = (CheckBox) findViewById(R.id.weixin_cb);
      weixin_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               weibo_cb.setChecked(false);
               friend_circle_cb.setChecked(false);
               qq_cb.setChecked(false);
               qzone_cb.setChecked(false);
            }
         }
      });
      friend_circle_cb = (CheckBox) findViewById(R.id.friend_circle_cb);
      friend_circle_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               weibo_cb.setChecked(false);
               weixin_cb.setChecked(false);
               qq_cb.setChecked(false);
               qzone_cb.setChecked(false);
            }
         }
      });
      qq_cb = (CheckBox) findViewById(R.id.qq_cb);
      qq_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               weibo_cb.setChecked(false);
               weixin_cb.setChecked(false);
               friend_circle_cb.setChecked(false);
               qzone_cb.setChecked(false);
            }
         }
      });
      qzone_cb = (CheckBox) findViewById(R.id.qzone_cb);
      qzone_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
               weibo_cb.setChecked(false);
               weixin_cb.setChecked(false);
               friend_circle_cb.setChecked(false);
               qq_cb.setChecked(false);
            }
         }
      });
   }

   @Override public void onClick(View v) {
      switch (v.getId()) {
         case R.id.live_cover:
            //裁切图片
            // 头像修改
            showDialog();
            break;
         case R.id.btn_show:
//            doShowSelected();
            startCameraPreviewWithPermCheck();
            break;
         case R.id.close_live_cover:
            //删除图片
            mImageButtonCloseLiveCover.setVisibility(View.GONE);
            Drawable drawable = getResources().getDrawable(R.drawable.release_cover);
            mImageButtonLiveCover.setImageDrawable(drawable);
            coverPath = null;
            break;
         case R.id.close_release:
            finish();
            break;
         default:
            break;
      }
   }

   @Override protected void onResume() {
      super.onResume();
      //用来判断是否从分享界面回来
      if (is_doing_share) {
         if (is_doing_sina_login) {
            is_doing_sina_login = false;
         } else {
            doFinalShow();
         }
      }
   }

   //按选分享类型跳转，分享结束，不管成功或取消都开始直播
   public void doShowSelected() {
      startProgressDialog();

      is_doing_share = true;

      if (shareDialog == null) {
         shareDialog = new ShareHelper(ReleaseLiveActivity.this);
         shareDialog.setShareLiveuid(mSelfUserInfo.getUserPhone());
      }

      String getuid = mSelfUserInfo.getUserPhone();
      String nickname = mSelfUserInfo.getUserName();
      //分享的内容
      String share_target_url = "http://web."
          + UrlHelper.URL_domain
          + "/play?uid="
          + getuid
          + "&liveid="
          + getuid
          + "&share_uid="
          + getuid
          + "&share_from=";

      AULiveApplication auLiveApplication =
          (AULiveApplication) TinkerManager.getTinkerApplicationLike();
      if (weibo_cb.isChecked()) {
         doSharePrepare(getuid, auLiveApplication.getCity(), "weibo");
         share_target_url = share_target_url + "weibo";
      } else if (weixin_cb.isChecked()) {
         doSharePrepare(getuid, auLiveApplication.getCity(), "weixin");
         share_target_url = share_target_url + "weixin";
      } else if (friend_circle_cb.isChecked()) {
         doSharePrepare(getuid, auLiveApplication.getCity(), "friend_circle");
         share_target_url = share_target_url + "friend_circle";
      } else if (qq_cb.isChecked()) {
         doSharePrepare(getuid, auLiveApplication.getCity(), "qq");
         share_target_url = share_target_url + "qq";
      } else if (qzone_cb.isChecked()) {
         doSharePrepare(getuid, auLiveApplication.getCity(), "qzone");
         share_target_url = share_target_url + "qzone";
      } else {

      }

      shareDialog.setShareUrl(share_target_url);
      shareDialog.setShareTitle(Utils.trans(R.string.app_name));
      //内容文字，头像
      shareDialog.setShareContent("我是" + nickname + ",想看我现场秀，就来" + Utils.trans(R.string.app_name)+ "！" + share_target_url,
          mSelfUserInfo.getHeadImagePath());

      if (weibo_cb.isChecked()) {
         shareDialog.doShareToWeiBo();
      } else if (weixin_cb.isChecked()) {
         shareDialog.doShareToWeiXin();
      } else if (friend_circle_cb.isChecked()) {
         shareDialog.doShareToWeiXinFriend();
      } else if (qq_cb.isChecked()) {
         shareDialog.doShareToQQ();
      } else if (qzone_cb.isChecked()) {
         shareDialog.doShareToQQZone();
      } else {
         doFinalShow();
      }
   }


   private void startCameraPreviewWithPermCheck() {
      int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
      int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
      if (cameraPerm != PackageManager.PERMISSION_GRANTED
              || audioPerm != PackageManager.PERMISSION_GRANTED) {
         if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Trace.d("No CAMERA or AudioRecord permission, please check");
            Utils.showCroutonText(ReleaseLiveActivity.this, "没有视频权限,请打开再尝试");
         } else {
            String[] permissions = {
                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(this, permissions,
                    PERMISSION_REQUEST_CAMERA_AUDIOREC);
         }
      } else {
         doShowSelected();
      }
   }

   @Override
   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
         Utils.showCroutonText(ReleaseLiveActivity.this, "没有麦克风或者视频权限,请打开再尝试");
         return;
      } else {
         doShowSelected();
      }
   }

   public void doFinalShow() {
      //标题可选
      //if (mLiveTitleString == null || mLiveTitleString.equals("")) {
      //   Utils.showMessage("请先填写标题");
      //   return;
      //}
      //封面可选
      //if (coverPath == null || coverPath.equals("")) {
      //   Utils.showMessage("请先上传封面");
      //   return;
      //}
      mSelfUserInfo.setIsCreater(true);
      createLive();
   }

   private ShareHelper shareDialog = null;

   /**
    * 同步个人信息到后台
    */
   // 图片与创建房间
   private void createLive() {
      Trace.d("**>>创建了房间");
      uploadProgressTv.setVisibility(View.VISIBLE);

      final long vdoid = System.currentTimeMillis() / 1000;
      Trace.d("live begin vdoid_time:" + vdoid);
      final String filePath = coverPath;
      Trace.d("filePath:" + filePath);
      RequestInformation request = null;
      try {
         AULiveApplication application =
             (AULiveApplication) TinkerManager.getTinkerApplicationLike();
         String address;
         if (application.getAddress() != null) {

            address = application.getCity();
         } else {
            address = "";
         }
         request = new RequestInformation(UrlHelper.createRoom
             + "?livetitle="
             + URLEncoder.encode(mLiveTitleString, "utf-8")
             + "&addr="
             + URLEncoder.encode(address, "utf-8")
             + "&vdoid="
             + vdoid, RequestInformation.REQUEST_METHOD_POST);
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      try {
         //request.addPostParams("roomid", roomNum + "");
         //request.addPostParams("livetitle", URLEncoder.encode(mLiveTitleString, "utf-8"));
         //request.addPostParams("userid", userPhone);
         //request.addPostParams("groupid", groupid);
         //request.addPostParams("addr", "abc");
         request.isHttpClient = false;
         request.addHeader("Connection", "Keep-Alive");
         request.addHeader("Charset", "UTF-8");
         request.addHeader("Content-Type", "multipart/form-data;boundary=7d4a6d158c9");
      } catch (Exception e) {
      }

      if (filePath != null) {
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
      }

      request.setCallback(new JsonCallback<UpLoadFaceEntity>() {

         @Override public void onCallback(UpLoadFaceEntity callback) {

            if (callback == null) {
               return;
            }
            Trace.d("callback:" + callback.getFace());
            stopProgressDialog();
            if (callback.getStat() == 200) {
               String face = callback.getFace();
               uploadProgressTv.setVisibility(View.GONE);
               startActivity(new Intent(ReleaseLiveActivity.this, AvActivity.class).putExtra(
                   AvActivity.GET_UID_KEY, userPhone)
                   .putExtra(AvActivity.IS_CREATER_KEY, true)
                   .putExtra(AvActivity.EXTRA_SYS_MSG, callback.sys_msg)
                   .putExtra(AvActivity.EXTRA_RECORD_TITLE_KEY, mLiveTitleString)
                   .putExtra(AvActivity.GET_VDOID_KEY, vdoid));
               finish();
            } else {
               Utils.showMessage(callback.getMsg());
            }
         }

         @Override public void onFailure(AppException e) {

            Utils.showMessage("获取网络数据失败");
         }
      }.setReturnType(UpLoadFaceEntity.class));
      request.execute();
   }

   @Override public void afterTextChanged(Editable s) {
      mLiveTitleString = mEditTextLiveTitle.getText().toString();
      //mButtonShow.setEnabled(mLiveTitleString != null && mLiveTitleString.length() > 0);
   }

   @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
   }

   @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
   }

   private void refreshWaitingDialog() {

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

   private ProgressBar uploadProgressTv;

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
                  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
      try {

         super.onActivityResult(requestCode, resultCode, data);
         Trace.d("requestCode:" + requestCode + " resultcode:" + resultCode + " data:" + data);
         if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareDialog.qq_IUiListener);
         }
         if (requestCode == Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data,
                shareDialog.qzone_IUiListener);
         }
         // SSO 授权回调
         // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
         if (shareDialog != null && shareDialog.mSsoHandler != null) {
            shareDialog.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
         }

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
                     Toast.makeText(ReleaseLiveActivity.this, "图片没找到", Toast.LENGTH_SHORT).show();
                     return;
                  }
                  cursor.moveToFirst();
                  String path =
                      cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
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

            //不切图
            //coverPath = f.getAbsolutePath();
            //try {
            //   FileInputStream fis = new FileInputStream(coverPath);
            //   Bitmap bitmap = BitmapFactory.decodeStream(fis);
            //   mImageButtonLiveCover.setImageBitmap(bitmap);
            //} catch (Exception e) {
            //
            //}

         } else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
            if (data != null) {
               String path = data.getStringExtra("path");
               Trace.d("ReleaseLiveActivity path:" + path);

               // Bitmap b = BitmapFactory.decodeFile(path);
               // faceimagView.setImageBitmap(b);
               // 上传图片到服务器

               try {
                  Uri targetUri = Uri.fromFile(new File(path));
                  coverPath = getFilePath(targetUri);
                  Trace.d("ReleaseLiveActivity coverPath:" + coverPath);
                  FileInputStream fis = new FileInputStream(coverPath);
                  Bitmap bitmap = BitmapFactory.decodeStream(fis);
                  mImageButtonLiveCover.setImageBitmap(bitmap);

                  mImageButtonCloseLiveCover.setVisibility(View.VISIBLE);
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      } catch (Exception e) {
      }
   }

   //转换路径类型
   private String getFilePath(Uri mUri) {
      try {
         if (mUri.getScheme().equals("file")) {
            return mUri.getPath();
         } else {
            return getFilePathByUri(mUri);
         }
      } catch (FileNotFoundException ex) {
         return null;
      }
   }

   private String getFilePathByUri(Uri mUri) throws FileNotFoundException {
      ContentResolver mContentResolver = getContentResolver();
      if (mUri == null) {
         return null;
      }
      String imgPath = null;
      try {
         Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
         cursor.moveToFirst();
         imgPath = cursor.getString(1);
      } catch (NullPointerException e) {
         e.printStackTrace();
         finish();
      }
      return imgPath;
   }

   @Override public void onResponse(BaseResponse baseResp) {

      Trace.d("ReleaseLiveActivity onResponse");
      switch (baseResp.errCode) {
         case WBConstants.ErrorCode.ERR_OK:
            Utils.showCroutonText(ReleaseLiveActivity.this,
                Utils.trans(R.string.weibosdk_share_success));
            shareDialog.doWeiboShareCallback();
            break;
         case WBConstants.ErrorCode.ERR_CANCEL:
            Utils.showCroutonText(ReleaseLiveActivity.this,
                Utils.trans(R.string.weibosdk_share_canceled));
            break;
         case WBConstants.ErrorCode.ERR_FAIL:
            Utils.showCroutonText(ReleaseLiveActivity.this,
                Utils.trans(R.string.weibosdk_share_failed));
            break;
      }
   }

   private CustomProgressDialog progressDialog = null;

   public void startProgressDialog() {
      if (progressDialog == null) {
         progressDialog = CustomProgressDialog.createDialog(this);
         progressDialog.setMessage("加载中");
      }

      progressDialog.show();
   }

   public void stopProgressDialog() {
      if (progressDialog != null) {
         progressDialog.dismiss();
         progressDialog = null;
      }
   }

   public static void doSharePrepare(String liveuid, String city, String pf) {
      //{
      //   "stat": 200,
      //    "msg": "",
      //    "title": "恩行在上海市直播",
      //    "desc": "我在看恩行直播,有823个人在观看,颜值高不高,直播看真相",
      //    "img": "http://img.hrbhzkj.com/face/80/17686096.jpg?v=49",
      //    "url": "http://web.hrbhzkj.com/play?uid=17686096&liveid=17686096&share_uid=17686096&share_from=friend_circle"
      //}
      RequestInformation request = new RequestInformation(
          UrlHelper.SERVER_URL + "share?liveuid=" + liveuid + "&city=" + city + "&pf=" + pf,
          RequestInformation.REQUEST_METHOD_GET);

      request.setCallback(new StringCallback() {
         @Override public void onFailure(AppException e) {
         }

         @Override public void onCallback(String callback) {
            Trace.d("doSharePrepare:" + callback);
            if (callback == null) {
               return;
            } else {

            }
         }
      });
      request.execute();
   }
}
