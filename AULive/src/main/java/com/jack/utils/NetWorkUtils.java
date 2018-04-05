package com.jack.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by big on 6/22/16.
 */
public class NetWorkUtils {
   //获取本机WIFI
   public static String getWIFIIpAddress(Context context) {
      WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      WifiInfo wifiInfo = wifiManager.getConnectionInfo();
      // 获取32位整型IP地址
      int ipAddress = wifiInfo.getIpAddress();

      //返回整型地址转换成“*.*.*.*”地址
      return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
          (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
   }

   //3G网络IP
   public static String get3GIpAddress() {
      try {
         for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
             en.hasMoreElements(); ) {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                enumIpAddr.hasMoreElements(); ) {
               InetAddress inetAddress = enumIpAddr.nextElement();
               if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                  // if (!inetAddress.isLoopbackAddress() && inetAddress
                  // instanceof Inet6Address) {
                  return inetAddress.getHostAddress().toString();
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   //3G网络IP 方法2
   public String getLocalIpAddress() {
      String ipAddress = null;
      try {
         List<NetworkInterface> interfaces =
             Collections.list(NetworkInterface.getNetworkInterfaces());
         for (NetworkInterface iface : interfaces) {
            if (iface.getDisplayName().equals("eth0")) {
               List<InetAddress> addresses = Collections.list(iface.getInetAddresses());
               for (InetAddress address : addresses) {
                  if (address instanceof Inet4Address) {
                     ipAddress = address.getHostAddress();
                  }
               }
            } else if (iface.getDisplayName().equals("wlan0")) {
               List<InetAddress> addresses = Collections.list(iface.getInetAddresses());
               for (InetAddress address : addresses) {
                  if (address instanceof Inet4Address) {
                     ipAddress = address.getHostAddress();
                  }
               }
            }
         }
      } catch (SocketException e) {
         e.printStackTrace();
      }
      return ipAddress;
   }

   public static String getIpAddress(Context context) {
      ConnectivityManager cm =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = cm.getActiveNetworkInfo();
      if (info.getType() == ConnectivityManager.TYPE_WIFI) {
         return getWIFIIpAddress(context);
      } else {
         return get3GIpAddress();
      }
   }

   /////////////////////////////////////////////////////////////

   public static boolean isNetworkAvailable(Context context) {
      ConnectivityManager mgr =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo[] info = mgr.getAllNetworkInfo();
      if (info != null) {
         for (int i = 0; i < info.length; ++i) {
            if (info[i].getState() == NetworkInfo.State.CONNECTED) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isNetworkConnected(Context context) {
      if (context != null) {
         ConnectivityManager mConnectivityManager =
             (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
         if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
         }
      }

      return false;
   }

   public static boolean isWifiConnected(Context context) {
      if (context != null) {
         ConnectivityManager mConnectivityManager =
             (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(1);
         if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable();
         }
      }

      return false;
   }

   public static boolean isMobileConnected(Context context) {
      if (context != null) {
         ConnectivityManager mConnectivityManager =
             (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(0);
         if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable();
         }
      }

      return false;
   }
}
