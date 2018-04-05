package com.ilikezhibo.ggzb.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.jack.utils.Trace;
import com.ilikezhibo.ggzb.AULiveApplication;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
	public final static String JPG = ".jpg";
	public final static String JPEG = ".jpeg";
	public final static String PNG = ".png";

	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h, Matrix matrix) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	public static Bitmap drawableToBitmap(Drawable drawable)
			throws OutOfMemoryError {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
			throws OutOfMemoryError {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap)
			throws OutOfMemoryError {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw reset rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 锟斤拷sd锟斤拷锟斤拷取图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap readBitMap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		Bitmap bm = BitmapFactory.decodeFile(path, options);
		return bm;
	}

	/**
	 * 
	 * @param bmpOriginal
	 * @return
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal)
			throws OutOfMemoryError {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0); // 锟斤拷色
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 淇濆瓨鍥剧墖
	 * 
	 * @param bitName
	 *            鍥剧墖鍚嶇О
	 * @param mBitmap
	 *            鍥剧墖
	 */
	public static void saveBitmapOfPNG(String path, Bitmap mBitmap) {
		Trace.d("saveBitmapOfPNG");
		if (mBitmap == null || mBitmap.isRecycled() || path == null) {
			return;
		}

		FileUtil.createFileDir(path);
		File f = new File(path);
		if (f.exists() && f.isFile()) {
			Trace.d("delete is file");
			FileUtil.deleteFile(path);
		} else {
			Trace.d("file is not");
		}

		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
			mBitmap.compress(CompressFormat.PNG, 100, fOut);
			fOut.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
			FileUtil.deleteFile(path);
		} finally {
			try {
				if (fOut != null)
					fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// /**
	// * 鍒涘缓Drawable
	// *
	// * @param path
	// * @return
	// */
	// public static Drawable createDrawable(String path) {
	// if (path == null) {
	// return null;
	// }
	//
	// File file = new File(path);
	// if (!file.exists() || file.isDirectory()) {
	// return null;
	// }
	//
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inSampleSize = 1;
	// opts.inJustDecodeBounds = false;//
	// 杩欓噷涓�瀹氳灏嗗叾璁剧疆鍥瀎alse锛屽洜涓轰箣鍓嶆垜浠皢鍏惰缃垚浜唗rue
	// opts.inPurgeable = true;
	// opts.inInputShareable = true;
	// opts.inDither = false;
	// opts.inPurgeable = true;
	// FileInputStream is = null;
	// Bitmap bitmap = null;
	// try {
	// is = new FileInputStream(path);
	// bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (is != null) {
	// is.close();
	// is = null;
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// if (bitmap == null) {
	// return null;
	// }
	//
	// Drawable drawable = new BitmapDrawable(bitmap);
	// return drawable;
	// }

	// /**
	// * 鍒涘缓鍥剧墖
	// *
	// * @param path
	// * @return
	// */
	// public static Bitmap createBitmap(String path) {
	// if (path == null) {
	// return null;
	// }
	//
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inSampleSize = 1;
	// opts.inJustDecodeBounds = false;//
	// 杩欓噷涓�瀹氳灏嗗叾璁剧疆鍥瀎alse锛屽洜涓轰箣鍓嶆垜浠皢鍏惰缃垚浜唗rue
	// opts.inPurgeable = true;
	// opts.inInputShareable = true;
	// opts.inDither = false;
	// opts.inPurgeable = true;
	// FileInputStream is = null;
	// Bitmap bitmap = null;
	// try {
	// is = new FileInputStream(path);
	// bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (is != null) {
	// is.close();
	// is = null;
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return bitmap;
	// }

	/**
	 * 缂╂斁鍥剧墖
	 * 
	 * @param srcBitmap
	 * @param scaleW
	 * @param scaleH
	 * @return
	 */
	public static Bitmap scaleBitMap(Bitmap srcBitmap, float scaleW,
			float scaleH) {
		if (srcBitmap == null) {
			return null;
		}

		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createScaledBitmap(srcBitmap,
					(int) (scaleW * srcBitmap.getWidth()),
					(int) (scaleH * srcBitmap.getHeight()), true);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// Utils.recycleBitmap(srcBitmap);
		return bitmap;
	}

	// /**
	// * 鍒涘缓缂╂斁鐨勫浘鐗�(涓昏鏄挱鏀剧晫闈娇鐢�)
	// *
	// * @param resId
	// * 鍥剧墖ID
	// * @return 鍥剧墖
	// * @throws OutOfMeomeryException
	// */
	// public static Bitmap createBitmapOfScale(int resId)
	// throws OutOfMeomeryException {
	// if (ScreenSupport.RES_DENSITY == 0) {
	// return createBitmap(resId);
	// } else {
	// Bitmap dstBitmap = null;
	//
	// try {
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inTargetDensity = ScreenSupport.RES_DENSITY;
	//
	// Bitmap srcBitmap = BitmapFactory.decodeResource(HeroApplication
	// .getInstance().getResources(), resId, opts);
	//
	// if (srcBitmap == null) {
	// return null;
	// }
	//
	// dstBitmap = Bitmap
	// .createScaledBitmap(srcBitmap, (int) (ScreenSupport
	// .getScale() * srcBitmap.getWidth()),
	// (int) (ScreenSupport.getScale() * srcBitmap
	// .getHeight()), true);
	//
	// opts = null;
	// if (SystemInfo.getPhoneSdkVersion() < 16) {
	// // SDK4.1.1(16)浠ヤ笂绯荤粺鍘熸潵鐨勫浘鐗囧拰缂╂斁鐨勫浘鐗囨槸鍚屼竴涓浘鐗囷紝鎵�鏈変笉瑕佸幓鍥炴敹鍘熸潵鐨勫浘鐗�
	// // SDK4.1.1浠ヤ笅鐨勫彲浠ヨ皟鐢ㄦ鏂规硶
	// Utils.recycleBitmap(srcBitmap);
	// }
	// } catch (OutOfMemoryError e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// throw new OutOfMeomeryException();
	// }
	//
	// return dstBitmap;
	// }
	// }

	// /**
	// * 鍒涘缓缂╂斁鐨勫浘鐗�
	// *
	// * @param resId
	// * 鍥剧墖ID
	// * @return 鍥剧墖
	// */
	// public static Bitmap createBitmapOfAlbumBg(byte[] imgBuf,
	// boolean isLandScape) {
	// if (imgBuf == null || imgBuf.length <= 0) {
	// return null;
	// }
	//
	// Bitmap dstBitmap = null;
	// int w = 0;
	// int h = 0;
	// try {
	// if (isLandScape) {
	// w = (int) (296 * ScreenSupport.getScale());
	// h = (int) (203 * ScreenSupport.getScale());
	// } else {
	// w = (int) (465 * ScreenSupport.getScale());
	// h = (int) (320 * ScreenSupport.getScale());
	// }
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inTargetDensity = ScreenSupport.RES_DENSITY;
	//
	// Bitmap srcBitmap = BitmapFactory.decodeByteArray(imgBuf, 0,
	// imgBuf.length, opts);
	//
	// if (srcBitmap == null) {
	// return null;
	// }
	//
	// dstBitmap = Bitmap.createScaledBitmap(srcBitmap, w, h, true);
	//
	// opts = null;
	// Utils.recycleBitmap(srcBitmap);
	// } catch (OutOfMemoryError e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	// return dstBitmap;
	// }

	/**
	 * 鍒涘缓Drawable
	 * 
	 * @param path
	 * @return
	 */
	public static Drawable createDrawable(String path) {
		// Drawable drawable = null;
		// // 鎹曡幏OOM寮傚父
		// try {
		// drawable = Drawable.createFromPath(path);
		// } catch (OutOfMemoryError e) {
		// e.printStackTrace();
		// }
		//
		// if (drawable == null) {
		// // 鍒涘缓鍥剧墖澶辫触锛屾簮鏂囦欢鍙兘鏈夎锛屽垯鍒犻櫎
		// FileUtils.deleteFile(path);
		// }
		//
		// return drawable;

		if (path == null) {
			return null;
		}

		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			return null;
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		opts.inJustDecodeBounds = false;// 杩欓噷涓�瀹氳灏嗗叾璁剧疆鍥瀎alse锛屽洜涓轰箣鍓嶆垜浠皢鍏惰缃垚浜唗rue
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		FileInputStream is = null;
		Bitmap bitmap = null;
		try {
			is = new FileInputStream(path);
			bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (bitmap == null) {
			return null;
		}

		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * 鍒涘缓Drawable
	 * 
	 * @param resId
	 * @return
	 */
	public static Drawable createDrawable(int resId) {
		Drawable drawable = null;
		try {
			drawable = AULiveApplication.mContext.getResources().getDrawable(
					resId);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return drawable;
	}

	/**
	 * 鍒涘缓鍥剧墖
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap createBitmap(String path) {
		// Bitmap bitmap = null;
		//
		// // 鎹曡幏OOM寮傚父
		// try {
		// bitmap = BitmapFactory.decodeFile(path);
		// } catch (OutOfMemoryError e) {
		// e.printStackTrace();
		// }
		//
		// if (bitmap == null) {
		// // 鍒涘缓鍥剧墖澶辫触锛屾簮鏂囦欢鍙兘鏈夎锛屽垯鍒犻櫎
		// FileUtils.deleteFile(path);
		// }
		//
		// return bitmap;
		if (path == null) {
			return null;
		}

		Bitmap bitmap = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 1;
			opts.inJustDecodeBounds = false;// 杩欓噷涓�瀹氳灏嗗叾璁剧疆鍥瀎alse锛屽洜涓轰箣鍓嶆垜浠皢鍏惰缃垚浜唗rue
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			opts.inDither = false;
			opts.inPurgeable = true;
			FileInputStream is = null;
			bitmap = null;
			try {
				is = new FileInputStream(path);
				bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null,
						opts);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 鍒涘缓鍥剧墖
	 * 
	 * @param resId
	 *            鍥剧墖ID
	 * @return 鍥剧墖
	 * @throws OutOfMeomeryException
	 */
	public static Bitmap createBitmap(int resId) throws OutOfMemoryError {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeResource(
					AULiveApplication.mContext.getResources(), resId);
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new OutOfMemoryError();
		}
		return bitmap;
	}

	/**
	 * 鍒涘缓缂╂斁鍥剧墖
	 * 
	 * @param path
	 * @param pixOfWidth
	 * @param pixOfHeight
	 * @return
	 */
	public static Drawable createDrawableOfSampleSize(String path,
			int pixOfWidth, int pixOfHeight) {
		if (path == null) {
			return null;
		}

		if (pixOfWidth * pixOfHeight <= 0) {
			return null;
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 璁剧疆鎴愪簡true,涓嶅崰鐢ㄥ唴瀛橈紝鍙幏鍙朾itmap瀹介珮
		BitmapFactory.decodeFile(path, opts);

		if (opts.outWidth * opts.outHeight <= 0) {
			return null;
		}

		opts.inSampleSize = computeSampleSize(opts, -1, pixOfWidth
				* pixOfHeight);
		opts.inJustDecodeBounds = false;// 杩欓噷涓�瀹氳灏嗗叾璁剧疆鍥瀎alse锛屽洜涓轰箣鍓嶆垜浠皢鍏惰缃垚浜唗rue
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		FileInputStream is = null;
		Bitmap bitmap = null;
		try {
			is = new FileInputStream(path);
			bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (bitmap == null) {
			return null;
		}

		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int roundedSize = 0;
		try {
			int initialSize = computeInitialSampleSize(options, minSideLength,
					maxNumOfPixels);

			if (initialSize <= 8) {
				roundedSize = 1;
				while (roundedSize < initialSize) {
					roundedSize <<= 1;
				}
			} else {
				roundedSize = (initialSize + 7) / 8 * 8;
			}
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 鍒犻櫎鍜屼繚瀛樺浘鐗�
	 * 
	 * @param path
	 * @param mBitmap
	 */
	public static void deleteAndSaveBitmap(String path, Bitmap mBitmap) {
		if (path == null) {
			return;
		}

		// del
		FileUtil.deleteFileOfDir(path, true);

		// save
		saveBitmapOfPNG(path, mBitmap);
	}

	// /**
	// * 淇濆瓨鍥剧墖
	// *
	// * @param bitName
	// * 鍥剧墖鍚嶇О
	// * @param mBitmap
	// * 鍥剧墖
	// */
	// public static void saveBitmapOfPNG(String path, Bitmap mBitmap) {
	// if (mBitmap == null || mBitmap.isRecycled() || path == null) {
	// return;
	// }
	//
	// FileUtils.createFileDir(path);
	// File f = new File(path);
	// if (f.exists()) {
	// return;
	// }
	//
	// FileOutputStream fOut = null;
	// try {
	// f.createNewFile();
	// fOut = new FileOutputStream(f);
	// mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
	// fOut.flush();
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// FileUtils.deleteFile(path);
	// } finally {
	// try {
	// if (fOut != null)
	// fOut.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	/**
	 * 淇濆瓨鍥剧墖
	 * 
	 * @param bitName
	 *            鍥剧墖鍚嶇О
	 * @param mBitmap
	 *            鍥剧墖
	 */
	public static void saveBitmapOfJPG(String path, Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled() || path == null) {
			return;
		}

		FileUtil.createFileDir(path);
		File f = new File(path);
		if (f.exists() && f.isFile()) {
			Trace.d("delete is file");
			FileUtil.deleteFile(path);
		} else {
			Trace.d("file is not");
		}

		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
			bitmap.compress(CompressFormat.JPEG, 75, fOut);
			fOut.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
			FileUtil.deleteFile(path);
		} finally {
			try {
				if (fOut != null)
					fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * bitmap杞崲byteArray
	 * 
	 * @param bmp
	 * @return
	 */
	public static byte[] bitmapToByteArrayOfPNG(final Bitmap bmp) {
		if (bmp == null) {
			return null;
		}

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		bmp.recycle();

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 鑾峰彇鍥剧墖鐨勫楂�
	 * 
	 * @param resId
	 * @return
	 */
	public static int[] getImageWidthAndHeight(int resId) {
		int size[] = new int[2];
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;

			BitmapFactory.decodeResource(
					AULiveApplication.mContext.getResources(), resId, opts);
			size[0] = opts.outWidth;
			size[1] = opts.outHeight;

		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return size;
	}

	/**
	 * 鑾峰彇鍥剧墖鐨勫
	 * 
	 * @param resId
	 * @return
	 */
	public static int getImageWidth(int resId) {
		int width = 0;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;

			BitmapFactory.decodeResource(
					AULiveApplication.mContext.getResources(), resId, opts);
			width = opts.outWidth;
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return width;
	}

	/**
	 * 鑾峰彇鍥剧墖鐨勯珮
	 * 
	 * @param resId
	 * @return
	 */
	public static int getImageHeight(int resId) {
		int height = 0;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;

			BitmapFactory.decodeResource(
					AULiveApplication.mContext.getResources(), resId, opts);
			height = opts.outHeight;
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return height;
	}

	// /**
	// * 璁剧疆鍥剧墖
	// *
	// * @param drawableCatchManager
	// * @param imageViewByTag
	// * @param path
	// */
	// public static void setImageViewBitmap(
	// DrawableCatchManager drawableCatchManager,
	// ImageView imageViewByTag, String path) {
	// if (imageViewByTag == null) {
	// return;
	// }
	//
	// Drawable imageDrawable = BitmapUtils.createDrawable(path);
	// if (imageDrawable == null) {
	// return;
	// }
	//
	// imageViewByTag.setImageDrawable(imageDrawable);
	// // 娣诲姞缂撳瓨
	// if (drawableCatchManager != null) {
	// drawableCatchManager.put(path, imageDrawable);
	// }
	// }

	// /**
	// * 璁剧疆鍥剧墖
	// *
	// * @param drawableCatchManager
	// * @param imageViewByTag
	// * @param path
	// */
	// public static void setImageViewBitmap(
	// DrawableCatchManager drawableCatchManager,
	// ImageView imageViewByTag, String path, Drawable drawable) {
	// if (imageViewByTag == null) {
	// return;
	// }
	//
	// if (drawable == null) {
	// return;
	// }
	//
	// imageViewByTag.setImageDrawable(drawable);
	// // 娣诲姞缂撳瓨
	// if (drawableCatchManager != null) {
	// drawableCatchManager.put(path, drawable);
	// }
	// }

	/**   */
	public static Bitmap createBitmap(byte[] data) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 2;
		options.inPreferredConfig = Config.ARGB_8888; // 默认是Bitmap.Config.ARGB_8888
		options.inPurgeable = true;
		options.inInputShareable = true;

		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}
}
