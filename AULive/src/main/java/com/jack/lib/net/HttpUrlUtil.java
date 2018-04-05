package com.jack.lib.net;

import android.text.TextUtils;

import com.jack.lib.AppException;
import com.jack.lib.AppException.ErrorType;
import com.jack.utils.SharedPreferenceTool;
import com.jack.utils.TextUtil;
import com.jack.utils.Trace;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.NameValuePair;

public class HttpUrlUtil {
	private static final int TIMEOUT_CONNECTION = 15 * 1000;
	private static final int TIMEOUT_READ = 15 * 1000;

	public static HttpURLConnection execute(RequestInformation request) throws AppException {
		setCookie(null);

		if (request.mRequestMethod.equals(RequestInformation.REQUEST_METHOD_GET)) {
			return get(request);
		} else if (request.mRequestMethod.equals(RequestInformation.REQUEST_METHOD_POST)) {
			return post(request);
		} else {
			throw new RuntimeException(
					"request method:" + request.mRequestMethod + " is not supported");
		}
	}

	private static HttpURLConnection get(RequestInformation request) throws AppException {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(request.url);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(request.mRequestMethod);
			connection.setConnectTimeout(TIMEOUT_CONNECTION);
			connection.setReadTimeout(TIMEOUT_READ);

			addHeader(connection, request);
			saveCookie(connection.getHeaderFields(), connection.getURL().getHost(), connection);
		} catch (MalformedURLException e) {
			throw new AppException(ErrorType.ConnectionException, "MalformedURLException",
					e.getMessage(), null);
		} catch (ProtocolException e) {
			throw new AppException(ErrorType.ConnectionException, "ProtocolException", e.getMessage(),
					null);
		} catch (IOException e) {
			throw new AppException(ErrorType.ConnectionException, "IOException", e.getMessage(), null);
		}
		return connection;
	}

	private static HttpURLConnection post(RequestInformation request) throws AppException {
		HttpURLConnection connection = null;
		OutputStream out = null;
		boolean isClosed = false;
		try {
			URL url = new URL(request.url);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(request.mRequestMethod);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(TIMEOUT_CONNECTION);
			connection.setReadTimeout(TIMEOUT_READ);

			addHeader(connection, request);

			out = connection.getOutputStream();
			if (TextUtil.isValidate(request.urlParameters)) {
				out.write(getParams(request.urlParameters).getBytes());
			}
			if (TextUtil.isValidate(request.postContent)) {
				out.write(request.postContent.getBytes());
			}
			if (request.byteParams != null) {
				out.write(request.byteParams);
			}
			if (request.mRequestListener != null) {
				isClosed = request.mRequestListener.onPrepareParams(out);
			}
			saveCookie(connection.getHeaderFields(), connection.getURL().getHost(), connection);
		} catch (MalformedURLException e) {
			throw new AppException(ErrorType.ConnectionException, "MalformedURLException",
					e.getMessage(), null);
		} catch (ProtocolException e) {
			throw new AppException(ErrorType.ConnectionException, "ProtocolException", e.getMessage(),
					null);
		} catch (IOException e) {
			throw new AppException(ErrorType.ConnectionException, "IOException", e.getMessage(), null);
		} finally {
			if (out != null && !isClosed) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return connection;
	}

	private static void saveCookie(Map<String, List<String>> map, String host,
								   HttpURLConnection connection) {

		if(RequestInformation.isSetWebViewHttpClient==true){
			return;
		}
		if (map == null) {
			return;
		}

		//String cookieStr = connection.getHeaderField("Set-Cookie");
		//Trace.d("saveCookie cookieStr:" + cookieStr);

		//List<String> cookieList = map.get("cookie");
		//if (cookieList != null) {
		//   Trace.d("saveCookie cookieList:" + cookieList.toString());
		//}

		//需要获取umd uid phpsessid
		List<String> setCookieList = map.get("set-cookie");
		if (setCookieList != null) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < setCookieList.size(); i++) {
				String cookie = setCookieList.get(i);
				if (!TextUtils.isEmpty(cookie)) {
					sb.append(cookie + cookie_split_sign);
				}
			}
			Trace.d("saveCookie cookieList:" + sb.toString());

			if (sb.toString() != null
					&& sb.toString().indexOf("umd") > 0
					&& sb.toString().trim().indexOf("uid") > 0) {
				// Trace.d("save cookie:" + sb.toString());
				SharedPreferenceTool.getInstance()
						.saveString(SharedPreferenceTool.COOKIE_KEY, sb.toString());

				//CookieSyncManager.createInstance(AULiveApplication.mContext);
				//CookieManager cookieManager = CookieManager.getInstance();
				//cookieManager.setCookie(host, sb.toString());
				///** 及时同步Cookie数据.默认是利用Timer间隔5分钟同步一次. */
				//CookieSyncManager.getInstance().sync();

			} else {
				// Trace.d(" no save cookie:" + sb.toString());
			}
		}
	}

	private static void addHeader(HttpURLConnection mHttpCon, RequestInformation request) {
		if (request.headers != null && request.headers.size() > 0) {
			for (Map.Entry<String, String> header : request.headers.entrySet()) {
				mHttpCon.addRequestProperty(header.getKey(), header.getValue());
			}
		}
		if (request.mGlobalRequestFilter != null) {
			HashMap<String, String> global = request.mGlobalRequestFilter.filterHeader();
			if (TextUtil.isValidate(global)) {
				for (Map.Entry<String, String> header : global.entrySet()) {
					mHttpCon.addRequestProperty(header.getKey(), header.getValue());
				}
			}
		}
	}

	private static String getParams(ArrayList<NameValuePair> lists) {
		String params = "";
		NameValuePair pair = null;
		for (int j = 0; j < lists.size(); j++) {
			pair = lists.get(j);
			params += pair.getName() + "=" + pair.getValue();
			if (j != lists.size() - 1) {
				params += "&";
			}
		}
		return params;
	}

	public static String cookie_split_sign = "!!";
	private static java.net.CookieManager msCookieManager = new java.net.CookieManager();

	//HttpURLConnection不会维护cookie,只能自己维护
	private static void setCookie(HttpURLConnection mHttpCon) {

		if(RequestInformation.isSetWebViewHttpClient==true){
			return;
		}
		msCookieManager.getCookieStore().removeAll();
		msCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

		String mCookie =
				SharedPreferenceTool.getInstance().getString(SharedPreferenceTool.COOKIE_KEY, "");
		//如果不包含"!!"则之前不是HttpURLConnection,清空
		if (!mCookie.contains(cookie_split_sign)) {
			SharedPreferenceTool.getInstance().saveString(SharedPreferenceTool.COOKIE_KEY, "");
			mCookie = "";
		}

		String[] cookies = mCookie.split(cookie_split_sign);
		for (String cookie1 : cookies) {
			String tem_cookie = cookie1;
			if (tem_cookie.trim().equals("")) {
				continue;
			}
			msCookieManager.getCookieStore().add(null, HttpCookie.parse(tem_cookie).get(0));
		}

		if (msCookieManager.getCookieStore().getCookies().size() > 0) {
			// While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
			//失效
			//String request_cookie = TextUtils.join(";", msCookieManager.getCookieStore().getCookies());
			//Trace.d("request_cookie:"+request_cookie);
			//mHttpCon.setRequestProperty("Cookie", request_cookie);

			CookieHandler.setDefault(msCookieManager);
		}
	}
}
