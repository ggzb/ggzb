package com.jack.lib.net;

import android.os.AsyncTask;
import com.jack.lib.AppException;
import com.jack.lib.net.itf.ICallback;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.lib.net.itf.IRequestListener;
import com.jack.utils.UrlHelper;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class RequestInformation {
	public String lid;
	public String url;
	public HashMap<String, String> headers;
	public String mRequestMethod = "GET";
	public ArrayList<NameValuePair> urlParameters;
	public boolean isHttpClient = true;
	public static boolean isSetWebViewHttpClient = true;
	public String postContent;
	public ICallback requestCallback;
	public IRequestListener mRequestListener;
	public IProgressListener mProgressChangeListener;
	public String encoding = UrlHelper.DEFAULT_ENCODING;
	private AsyncTask requestTask;
	public byte[] byteParams;
	public final static String REQUEST_METHOD_GET = "GET";
	public final static String REQUEST_METHOD_POST = "POST";
	public static GlobalRequestFilter mGlobalRequestFilter;

	public RequestInformation(String url, String method) {
		this.url = url;
		this.mRequestMethod = method;
	}

	public RequestInformation(String url, String method, ArrayList<NameValuePair> forms) {
		this.url = url;
		this.urlParameters = forms;
		this.mRequestMethod = method;
	}

	public RequestInformation(String url, String method, String params) {
		this.url = url;
		this.postContent = params;
		this.mRequestMethod = method;
	}

	public void addHeader(String key, String value) {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		headers.put(key, value);
	}

	public void addPostParams(String key, String value) {
		if (urlParameters == null) {
			urlParameters = new ArrayList<NameValuePair>();
		}
		urlParameters.add(new BasicNameValuePair(key, value));
	}

	public void execute() {
		if (isHttpClient) {
			requestTask = new RequestTask(this);
			requestTask.execute();
		} else {
			requestTask = new HttpRequestTask(this);
			requestTask.execute();
		}
	}

	public void cancel() {
		if (requestTask != null) {
			requestCallback.cancel();
			requestTask.cancel(true);
		}
	}

	public void setCallback(ICallback callback) {
		this.requestCallback = callback;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setRequestListener(IRequestListener listener) {
		this.mRequestListener = listener;
	}

	public void setProgressChangeListener(IProgressListener mProgressChangeListener) {
		this.mProgressChangeListener = mProgressChangeListener;
	}

	// FIXME: this only for test in unit test , cause in unit test we can't run
	// @RequestTask.
	// public void test(){
	// try {
	// Object object = mCallback.handleResponse(HttpUtil.execute(this),new
	// IProgressListener() {
	// @Override
	// public void onProgressUpdate(int status,int curPos, int totalPos) {
	// Trace.d("status="+ status+"curPos="+curPos+",totalPos="+totalPos);
	// }
	// });
	// if (object instanceof Exception) {
	// mCallback.onFailure((Exception)object);
	// }
	// mCallback.onCallback(object);
	// } catch (Exception e) {
	// mCallback.onFailure(e);
	// }
	// }
	public void test() {
		try {
			Object object =
					requestCallback.handleConnection(HttpUrlUtil.execute(this), new IProgressListener() {
						@Override
						public void progressChanged(int status, int percent, String operationName) {
						}
					});
			if (object instanceof AppException) {
				requestCallback.onFailure((AppException) object);
			}
			requestCallback.onCallback(object);
		} catch (AppException e) {
			requestCallback.onFailure(e);
		}
	}
}
