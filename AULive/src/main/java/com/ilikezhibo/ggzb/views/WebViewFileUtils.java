package com.ilikezhibo.ggzb.views;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.jack.lib.net.HttpUrlUtil;
import com.jack.lib.net.RequestInformation;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;


/**
 * Created by big on 7/2/16.
 */
public class WebViewFileUtils {
   /**
    * @param uri The Uri to check.
    * @return Whether the Uri authority is ExternalStorageProvider.
    */
   public static boolean isExternalStorageDocument(Uri uri) {
      return "com.android.externalstorage.documents".equals(uri.getAuthority());
   }

   /**
    * @param uri The Uri to check.
    * @return Whether the Uri authority is DownloadsProvider.
    */
   public static boolean isDownloadsDocument(Uri uri) {
      return "com.android.providers.downloads.documents".equals(uri.getAuthority());
   }

   /**
    * @param uri The Uri to check.
    * @return Whether the Uri authority is MediaProvider.
    */
   public static boolean isMediaDocument(Uri uri) {
      return "com.android.providers.media.documents".equals(uri.getAuthority());
   }

   /**
    * Get the value of the data column for this Uri. This is useful for
    * MediaStore Uris, and other file-based ContentProviders.
    *
    * @param context The context.
    * @param uri The Uri to query.
    * @param selection (Optional) Filter used in the query.
    * @param selectionArgs (Optional) Selection arguments used in the query.
    * @return The value of the _data column, which is typically reset file path.
    */
   public static String getDataColumn(Context context, Uri uri, String selection,
                                      String[] selectionArgs) {

      Cursor cursor = null;
      final String column = "_data";
      final String[] projection = {
              column
      };

      try {
         cursor =
                 context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
         if (cursor != null && cursor.moveToFirst()) {
            final int column_index = cursor.getColumnIndexOrThrow(column);
            return cursor.getString(column_index);
         }
      } finally {
         if (cursor != null) {
            cursor.close();
         }
      }
      return null;
   }

   /**
    * Get reset file path from reset Uri. This will get the the path for Storage Access
    * Framework Documents, as well as the _data field for the MediaStore and
    * other file-based ContentProviders.
    *
    * @param context The context.
    * @param uri The Uri to query.
    * @author paulburke
    */
   @SuppressLint("NewApi") public static String getPath(final Context context, final Uri uri) {

      final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

      // DocumentProvider
      if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
         // ExternalStorageProvider
         if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
               return Environment.getExternalStorageDirectory() + "/" + split[1];
            }

            // TODO handle non-primary volumes
         }
         // DownloadsProvider
         else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri =
                    ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
         }
         // MediaProvider
         else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
               contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
               contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
               contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {
                    split[1]
            };

            return getDataColumn(context, contentUri, selection, selectionArgs);
         }
      }
      // MediaStore (and general)
      else if ("content".equalsIgnoreCase(uri.getScheme())) {
         return getDataColumn(context, uri, null, null);
      }
      // File
      else if ("file".equalsIgnoreCase(uri.getScheme())) {
         return uri.getPath();
      }

      return null;
   }

   /**
    * 将cookie同步到WebView
    *
    * @param url WebView要加载的url
    * @param cookie 要同步的cookie
    * @return true 同步cookie成功，false同步cookie失败
    */
   public static boolean syncCookie(String url, String cookie,Context context) {
      //兼容久版本 4.0
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
         CookieSyncManager.createInstance(context);
      }
      CookieManager cookieManager = CookieManager.getInstance();

      if (RequestInformation.isSetWebViewHttpClient) {
         //方法1 如果使用httpClient必须手动加domain
         //RequestInformation.isSetWebViewHttpClient必须等于true
         String[] cookies = cookie.split(";");
         for (String cookie1 : cookies) {
            String tem_cookie =
                    cookie1 + ";Max-Age=2592000" + ";Domain=." + UrlHelper.URL_domain + ";Path=/";
            cookieManager.setCookie(url, tem_cookie);
            Trace.d("webview eachone:" + tem_cookie);
         }
      } else {
         //方法2,使用urlConnection
         //RequestInformation.isSetWebViewHttpClient
         //"Max-Age=2592000" + ";Domain=." + UrlHelper.URL_domain + ";Path=/"
         String[] cookies = cookie.split(HttpUrlUtil.cookie_split_sign);
         for (String cookie1 : cookies) {
            String tem_cookie = cookie1;
            cookieManager.setCookie(url, tem_cookie);
            Trace.d("webview eachone:" + tem_cookie);
         }
      }

      String newCookie = cookieManager.getCookie(url);
      Trace.d("webview newCookie:" + newCookie);

      return TextUtils.isEmpty(newCookie) ? false : true;
   }
}