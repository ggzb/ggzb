package com.ilikezhibo.ggzb.cropimage;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.jack.utils.FileUtil;
import com.jack.utils.Trace;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.BaseActivity;
import com.ilikezhibo.ggzb.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * The activity can crop specific region of interest from an image.
 */
public class MyCropActivity extends BaseActivity {

   // private static final String TAG = "CropImage";

   // public final static String ACTION_CROP_IMAGE =
   // "android.intent.action.CROP";
   public final static String IMAGE_URI = "path";
   public final static String CROP_IMAGE_URI = "crop_image_uri";
   public final static String IS_CROP_IMG_KEY = "crop_img_key";

   // private static final boolean RECYCLE_INPUT = true;

   private int mAspectX, mAspectY;
   private final Handler mHandler = new Handler();

   // These options specifiy the output image size and whether we should
   // scale the output to fit it (or just crop it).
   // private int mOutputX, mOutputY;
   // private boolean mScale;
   // private boolean mScaleUp = true;
   private boolean mCircleCrop = false;
   private boolean isCropImg = false;

   boolean mSaving; // Whether the "save" button is already clicked.

   private CropImageView mImageView;

   private Bitmap mBitmap;

   // private RotateBitmap rotateBitmap;
   private Uri targetUri;
   private HighlightView mCrop;
   private HighlightView hv;

   // private int rotation = 0;

   private static final int ONE_K = 1024;
   // private static final int ONE_M = ONE_K * ONE_K;

   private ContentResolver mContentResolver;

   private static final int DEFAULT_WIDTH = 480;
   private static final int DEFAULT_HEIGHT = 800;

   private int width;
   private int height;
   private int sampleSize = 1;

   @Override public void onCreate(Bundle icicle) {
      super.onCreate(icicle);
      try {
         try {
            isCropImg = getIntent().getBooleanExtra(IS_CROP_IMG_KEY, false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            // Make UI fullscreen.
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_crop);

            initViews();

            Intent intent = getIntent();
            String path1 = intent.getStringExtra(IMAGE_URI);
            targetUri = Uri.fromFile(new File(path1));
            mContentResolver = getContentResolver();

            if (targetUri == null) {
               Utils.showMessage("获取照片信息失败！");
               this.finish();
               return;
            }
            boolean isBitmapRotate = false;
            if (mBitmap == null) {
               String path = getFilePath(targetUri);
//					String path = path1;
               isBitmapRotate = isRotateImage(path);
               getBitmapSize();
               getBitmap();
            }

            if (mBitmap == null) {
               finish();
               return;
            }

            startFaceDetection(isBitmapRotate);
         } catch (RuntimeException e) {
            e.printStackTrace();
            System.gc();
            finish();
         }
      } catch (OutOfMemoryError e) {
         Utils.showMessage(Utils.trans(R.string.outofmemory_str));
         System.gc();
         finish();
      }
   }

   /**
    * @return void
    * @Title: initViews
    * @date 2012-12-14 ����10:41:23
    */
   private void initViews() {
      mImageView = (CropImageView) findViewById(R.id.image);
      mImageView.mContext = this;

      findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            setResult(RESULT_CANCELED);
            finish();
         }
      });
      findViewById(R.id.rotate).setOnClickListener(new View.OnClickListener() {

         @Override public void onClick(View v) {
            onRotateClicked();
         }
      });

      findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
            onSaveClicked();
         }
      });
   }

   /**
    * ��ȡBitmap�ֱ��ʣ�̫���˾ͽ���ѹ��
    *
    * @return void
    * @Title: getBitmapSize
    * @date 2012-12-14 ����8:32:13
    */
   private void getBitmapSize() {
      InputStream is = null;
      try {

         is = getInputStream(targetUri);

         BitmapFactory.Options options = null;
         try {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, options);
         } catch (OutOfMemoryError e) {
            Utils.showMessage(Utils.trans(R.string.outofmemory_str));
            System.gc();
            finish();
            return;
         }

         width = options.outWidth;
         height = options.outHeight;
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException ignored) {
            }
         }
      }
   }

   /**
    *
    * @return void
    * @Title: getBitmap
    * @date 2012-12-13 ����8:22:23
    */
   private void getBitmap() {
      InputStream is = null;
      try {

         try {
            is = getInputStream(targetUri);
         } catch (IOException e) {
            e.printStackTrace();
         }

         while ((width / sampleSize > DEFAULT_WIDTH * 2) || (height / sampleSize
             > DEFAULT_HEIGHT * 2)) {
            sampleSize *= 2;
            Trace.d("********************sammpleSize:" + sampleSize);
         }

         BitmapFactory.Options options = new BitmapFactory.Options();
         options.inSampleSize = sampleSize;

         try {
            mBitmap = BitmapFactory.decodeStream(is, null, options);
         } catch (OutOfMemoryError e) {
            Utils.showMessage(Utils.trans(R.string.outofmemory_str));
            System.gc();
            finish();
            return;
         }
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException ignored) {
            }
         }
      }
   }

   /**
    *
    * @return void
    * @Title: rotateImage
    * @date 2012-12-14 ����10:58:26
    */
   private boolean isRotateImage(String path) {

      try {
         ExifInterface exifInterface = new ExifInterface(path);

         int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
             ExifInterface.ORIENTATION_NORMAL);

         if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return true;
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
      return false;
   }

   /**
    * ��ȡ������
    *
    * @return InputStream
    * @Title: getInputStream
    * @date 2012-12-14 ����9:00:31
    */
   private InputStream getInputStream(Uri mUri) throws IOException {
      try {
         if (mUri.getScheme().equals("file")) {
            return new java.io.FileInputStream(mUri.getPath());
         } else {
            return mContentResolver.openInputStream(mUri);
         }
      } catch (FileNotFoundException ex) {
         return null;
      }
   }

   /**
    * ���Uri�����ļ�·��
    *
    * @return String
    * @Title: getInputString
    * @date 2012-12-14 ����9:14:19
    */
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

   /**
    * �˴�д��������
    *
    * @return String
    * @Title: getFilePathByUri
    * @date 2012-12-14 ����9:16:33
    */
   private String getFilePathByUri(Uri mUri) throws FileNotFoundException {
      if (mUri == null) {
         return null;
      }
      String imgPath = null;
      try {
         Cursor cursor = mContentResolver.query(mUri, null, null, null, null);
         cursor.moveToFirst();
         imgPath = cursor.getString(1); // ͼƬ�ļ�·��
      } catch (NullPointerException e) {
         e.printStackTrace();
         finish();
      }
      return imgPath;
   }

   /**
    * �˴�д��������
    *
    * @param isRotate �Ƿ���תͼƬ
    * @return void
    * @Title: startFaceDetection
    * @date 2012-12-14 ����10:38:29
    */
   private void startFaceDetection(final boolean isRotate) {
      if (isFinishing()) {
         return;
      }
      if (isRotate) {
         initBitmap();
      }

      mImageView.setImageBitmapResetBase(mBitmap, true);

      startBackgroundJob(null, "正在处理，请稍后...", new Runnable() {
         public void run() {
            final CountDownLatch latch = new CountDownLatch(1);

            mHandler.post(new Runnable() {
               public void run() {

                  final Bitmap b = mBitmap;

                  if (b != mBitmap && b != null) {
                     mImageView.setImageBitmapResetBase(b, true);
                     mBitmap.recycle();
                     mBitmap = b;
                  }

                  if (mImageView.getScale() == 1F) {
                     mImageView.center(true, true);
                  }

                  latch.countDown();
               }
            });
            try {
               latch.await();
            } catch (InterruptedException e) {
               throw new RuntimeException(e);
            }
            mRunFaceDetection.run();
         }
      }, mHandler);
   }

   /**
    * ��תԭͼ
    *
    * @return void
    * @Title: initBitmap
    * @date 2012-12-13 ����5:37:15
    */
   private void initBitmap() {
      Matrix m = new Matrix();
      m.setRotate(90);
      int width = mBitmap.getWidth();
      int height = mBitmap.getHeight();

      try {
         mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, m, true);
      } catch (OutOfMemoryError ooe) {
         Utils.showMessage(Utils.trans(R.string.outofmemory_str));
         System.gc();
         finish();
      }
   }

   private static class BackgroundJob implements Runnable {

      private final ProgressDialog mDialog;
      private final Runnable mJob;
      private final Handler mHandler;
      private final Runnable mCleanupRunner = new Runnable() {
         public void run() {
            if (mDialog.getWindow() != null) {
               mDialog.dismiss();
            }
         }
      };

      public BackgroundJob(Runnable job, ProgressDialog dialog, Handler handler) {
         mDialog = dialog;
         mJob = job;
         mHandler = handler;
      }

      public void run() {
         try {
            mJob.run();
         } finally {
            mHandler.post(mCleanupRunner);
         }
      }
   }

   private void startBackgroundJob(String title, String message, Runnable job, Handler handler) {
      // Make the progress dialog uncancelable, so that we can gurantee
      // the thread will be done before the activity getting destroyed.
      ProgressDialog dialog = ProgressDialog.show(this, title, message, true, false);
      new Thread(new BackgroundJob(job, dialog, handler)).start();
   }

   Runnable mRunFaceDetection = new Runnable() {
      float mScale = 1F;
      Matrix mImageMatrix;

      // Create reset default HightlightView if we found no face in the picture.
      private void makeDefault() {
         if (mBitmap == null) {
            return;
         }
         try {
            // mImageView.re
            if (hv != null) {
               mImageView.remove(hv);
            }
            hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // make the default size about 4/5 of the width or height
            //切图的切框大小在这设置
            int cropWidth = width;
            int cropHeight = cropWidth;
            if (width > height) {
               cropHeight = height;
            }

            if (mAspectX != 0 && mAspectY != 0) {
               if (mAspectX > mAspectY) {
                  cropHeight = cropWidth * mAspectY / mAspectX;
               } else {
                  cropWidth = cropHeight * mAspectX / mAspectY;
               }
            }

            //开始位置
            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            if (isCropImg) {
               cropHeight = height;
            } else {
               cropWidth = cropHeight;
            }

            Trace.d("x:" + x + " y:" + y + " cropWidth:" + cropWidth + " cropHeight:" + cropHeight);
            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
                mAspectX != 0 && mAspectY != 0);
            mImageView.add(hv);
         } catch (OutOfMemoryError e) {
            Utils.showMessage(Utils.trans(R.string.outofmemory_str));
            System.gc();
            finish();
         }
      }

      public void run() {
         mImageMatrix = mImageView.getImageMatrix();

         mScale = 1.0F / mScale;
         mHandler.post(new Runnable() {
            public void run() {
               makeDefault();

               mImageView.invalidate();
               if (mImageView.mHighlightViews.size() == 1) {
                  mCrop = mImageView.mHighlightViews.get(0);
                  mCrop.setFocus(true);
               }
            }
         });
      }
   };

   /**
    * @return void
    * @Title: onRotateClicked
    */
   private void onRotateClicked() {
      startFaceDetection(true);
   }

   /**
    * @return void
    * @Title: onSaveClicked
    */

   String cropPath = null;

   private void onSaveClicked() {
      // TODO this code needs to change to use the decode/crop/encode single
      // step api so that we don't require that the whole (possibly large)
      // bitmap doesn't have to be read into memory
      if (mCrop == null) {
         return;
      }

      if (mSaving) {
         return;
      }
      mSaving = true;

      Bitmap croppedImage = null;

      Rect r = mCrop.getCropRect();

      int width = r.width();
      int height = r.height();

      // If we are circle cropping, we want alpha channel, which is the
      // third param here.

      try {
         croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      } catch (OutOfMemoryError e) {
         // TODO Auto-generated catch block
         Utils.showMessage(Utils.trans(R.string.outofmemory_str));
         finish();
         return;
      }
      if (croppedImage == null) {
         return;
      }
      try {
         Canvas canvas = new Canvas(croppedImage);
         Rect dstRect = new Rect(0, 0, width, height);
         canvas.drawBitmap(mBitmap, r, dstRect, null);

         // Release bitmap memory as soon as possible
         mImageView.clear();
         mBitmap.recycle();
         mBitmap = null;

         mImageView.setImageBitmapResetBase(croppedImage, true);
         mImageView.center(true, true);
         mImageView.mHighlightViews.clear();

         String imgPath = getFilePath(targetUri);
         if (imgPath == null) {
            Trace.d("imgpath  is null");
            this.finish();
            return;
         }
         cropPath = imgPath.replace(".", "_crop_image.").trim();
         // mHandler.post(new Runnable() {
         //
         // @Override
         // public void run() {
         // TODO Auto-generated method stub
         saveDrawableToCache(croppedImage, cropPath);
         // }
         // });

         // Uri cropUri = Uri.fromFile(new File(cropPath));
         //
         // Intent intent = new Intent();
         // intent.putExtra(CROP_IMAGE_URI, cropUri);
         // setResult(RESULT_OK, intent);
         // finish();

         Intent intent = new Intent();
         intent.putExtra(IMAGE_URI, cropPath);
         setResult(RESULT_OK, intent);
         finish();
      } catch (OutOfMemoryError e) {
         // TODO Auto-generated catch block
         Utils.showMessage(Utils.trans(R.string.outofmemory_str));
         System.gc();
         finish();
      }
   }

   /**
    * Bitmap
    *
    * @return void
    * @Title: saveDrawableToCache
    * @date 2012-12-14 9:27:38
    */
   private void saveDrawableToCache(Bitmap bitmap, String filePath) {
      try {
         File file = new File(filePath);
         file.createNewFile();
         OutputStream outStream = new FileOutputStream(file);
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
         outStream.flush();
         outStream.close();
      } catch (IOException e) {
         //e.printStackTrace();
         //用于处理红米note等no permission 错误
         try {
            cropPath = FileUtil.getImageFolder() + "/" + System.currentTimeMillis() + ".jpg";
            File file = new File(cropPath);
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
         } catch (IOException e1) {
            e1.printStackTrace();
            Utils.showCroutonText(MyCropActivity.this,"没有找到内存卡");
         }
      }
   }

   @Override protected void onPause() {
      super.onPause();
   }

   @Override protected void onDestroy() {
      super.onDestroy();
      Trace.d("myCropActivity ondestroy");
      if (mBitmap != null) {
         mBitmap.recycle();
      }
      // 回收LAYOUT
      Utils.unbindLayout(findViewById(R.id.rootLayout));
   }
}
