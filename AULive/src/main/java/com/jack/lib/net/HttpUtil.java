package com.jack.lib.net;

import android.text.TextUtils;
import com.jack.lib.AppException;
import com.jack.lib.AppException.ErrorType;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.TextUtil;
import com.jack.utils.Trace;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

public class HttpUtil {
   private static final int REQUEST_TIMEOUT = 10 * 1000;// 设置请求超时10秒钟
   private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟

   public static HttpResponse execute(RequestInformation request) throws AppException {
      if (request.mRequestMethod.equals(RequestInformation.REQUEST_METHOD_GET)) {
         return get(request);
      } else if (request.mRequestMethod.equals(RequestInformation.REQUEST_METHOD_POST)) {
         return post(request);
      } else {
         throw new RuntimeException(
                 "request method:" + request.mRequestMethod + " is not supported");
      }
   }

   private static HttpResponse get(RequestInformation request) throws AppException {
      HttpClient httpClient = getHttpClient();
      HttpGet httpGet = new HttpGet(request.url);
      fillHeader(request);
      if (request.headers != null && request.headers.size() > 0) {
         for (Map.Entry<String, String> header : request.headers.entrySet()) {
            httpGet.addHeader(header.getKey(), header.getValue());
         }
      }
      request.requestCallback.checkIsCancelled();
      HttpResponse httpResponse = null;
      try {
         httpResponse = httpClient.execute(httpGet);
         saveCookie((DefaultHttpClient) httpClient, httpGet.getURI().getHost());
      } catch (ClientProtocolException e) {
         throw new AppException(ErrorType.ConnectionException, "ClientProtocolException",
                 e.getMessage(), null);
      } catch (IOException e) {
         throw new AppException(ErrorType.ConnectionException, "IOException", e.getMessage(), null);
      }
      return httpResponse;
   }

   public static HttpResponse getWithOutCallBack(RequestInformation request) throws AppException {
      HttpClient httpClient = getHttpClient();
      HttpGet httpGet = new HttpGet(request.url);
      fillHeader(request);
      if (request.headers != null && request.headers.size() > 0) {
         for (Map.Entry<String, String> header : request.headers.entrySet()) {
            httpGet.addHeader(header.getKey(), header.getValue());
         }
      }
      HttpResponse httpResponse = null;
      try {
         httpResponse = httpClient.execute(httpGet);
         saveCookie((DefaultHttpClient) httpClient, httpGet.getURI().getHost());
      } catch (ClientProtocolException e) {
         throw new AppException(ErrorType.ConnectionException, "ClientProtocolException",
                 e.getMessage(), null);
      } catch (IOException e) {
         throw new AppException(ErrorType.ConnectionException, "IOException", e.getMessage(), null);
      }
      return httpResponse;
   }

   private static void fillHeader(RequestInformation request) {
      HashMap<String, String> global = RequestInformation.mGlobalRequestFilter.filterHeader();
      if (TextUtil.isValidate(global)) {
         if (TextUtil.isValidate(request.headers)) {
            request.headers.putAll(global);
         } else {
            request.headers = new HashMap<String, String>();
            request.headers.putAll(global);
         }
      }
   }

   /**
    * 添加请求超时时间和等待时间
    *
    * @return HttpClient对象
    */
   public static HttpClient getHttpClient() {
      BasicHttpParams httpParams = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
      HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
      HttpClient client = new DefaultHttpClient(httpParams);
      return client;
   }

   private static HttpResponse post(RequestInformation request) throws AppException {
      HttpResponse httpResponse = null;
      HttpEntity entity = null;
      try {
         HttpClient httpClient = getHttpClient();
         if (request.urlParameters != null && request.urlParameters.size() > 0) {
            entity = new UrlEncodedFormEntity(request.urlParameters, request.encoding);
         }
         if (request.postContent != null && !"".equals(request.postContent.trim())) {
            entity = new StringEntity(request.postContent, request.encoding);
         }
         if (request.byteParams != null) {
            entity = new ByteArrayEntity(request.byteParams);
         }
         HttpPost httpPost = new HttpPost(request.url);
         fillHeader(request);
         if (request.headers != null && request.headers.size() > 0) {
            for (Map.Entry<String, String> header : request.headers.entrySet()) {
               httpPost.addHeader(header.getKey(), header.getValue());
            }
         }
         httpPost.setEntity(entity);
         request.requestCallback.checkIsCancelled();
         httpResponse = httpClient.execute(httpPost);
         saveCookie((DefaultHttpClient) httpClient, httpPost.getURI().getHost());
      } catch (UnsupportedEncodingException e) {
         throw new AppException(ErrorType.ConnectionException, "UnsupportedEncodingException",
                 e.getMessage(), null);
      } catch (ClientProtocolException e) {
         throw new AppException(ErrorType.ConnectionException, "ClientProtocolException",
                 e.getMessage(), null);
      } catch (IOException e) {
         throw new AppException(ErrorType.ConnectionException, "IOException", e.getMessage(), null);
      }
      return httpResponse;
   }

   private static void saveCookie(DefaultHttpClient client, String host) {
      List<Cookie> cookies = client.getCookieStore().getCookies();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < cookies.size(); i++) {
         Cookie cookie = cookies.get(i);
         Trace.d(cookie.toString());
         String cookieName = cookie.getName();
         String cookieValue = cookie.getValue();
         if (!TextUtils.isEmpty(cookieName) && !TextUtils.isEmpty(cookieValue)) {
            sb.append(cookieName + "=");
            sb.append(cookieValue + ";");
         }
      }

      if (sb.toString() != null
              && sb.toString().indexOf("umd") > 0
              && sb.toString().trim().indexOf("uid") > 0) {
         // Trace.d("save cookie:" + sb.toString());
         SharedPreferenceTool.getInstance()
                 .saveString(SharedPreferenceTool.COOKIE_KEY, sb.toString());
      } else {
         // Trace.d(" no save cookie:" + sb.toString());
      }
   }
}
