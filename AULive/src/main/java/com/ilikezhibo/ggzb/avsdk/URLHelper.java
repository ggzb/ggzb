//package com.hrbhzkj.xinxiu.avsdk;
//
//import android.util.Log;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
//public class UrlHelper {
//
//    public static String PostUrl(String Url, List<NameValuePair> pairlist) {
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(Url);
//
//        List<NameValuePair> params = pairlist;
//        UrlEncodedFormEntity entity;
//        int ret = 0;
//        try {
//            entity = new UrlEncodedFormEntity(params, "utf-8");
//            httpPost.setEntity(entity);
//
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            ret = httpResponse.getStatusLine().getStatusCode();
//
//            if (ret > 0) {
//                HttpEntity getEntity = httpResponse.getEntity();
//                String response = EntityUtils.toString(getEntity, "utf-8");
//                return response;
//            } else {
//                Log.e("error", "Httpresponse error");
//            }
//        } catch (UnsupportedEncodingException e) {
//            Log.e("error", e.toString());
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            Log.e("error", e.toString());
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.e("error", e.toString());
//            e.printStackTrace();
//        }
//        return "";
//    }
//}
