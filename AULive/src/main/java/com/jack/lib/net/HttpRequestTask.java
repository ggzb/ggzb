package com.jack.lib.net;

import android.os.AsyncTask;
import com.jack.lib.AppException;
import com.jack.lib.net.itf.IProgressListener;
import java.net.HttpURLConnection;

public class HttpRequestTask extends AsyncTask<Object, Integer, Object> {
	private String operationName;
	private RequestInformation request;

	public HttpRequestTask(RequestInformation request) {
		this.request = request;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (request.mRequestListener != null) {
			request.mRequestListener.onPreExecute();
		}
	}

	@Override
	protected Object doInBackground(Object... params) {
		HttpURLConnection response = null;
		try {
			Object object = null;
			if (request.mRequestListener != null) {
				request.mRequestListener.onBeforeDoingBackground();
			}
			response = HttpUrlUtil.execute(request);
			if (request.requestCallback != null) {
				if (request.mProgressChangeListener != null) {
					IProgressListener task = new IProgressListener() {
						@Override
						public void progressChanged(int status, int percent,
													String operationName) {
							if (operationName != null) {
								HttpRequestTask.this.operationName = operationName;
							}
							if (percent != -1) {
								publishProgress(status, percent);
							}
						}
					};
					object = request.requestCallback.handleConnection(response,
							task);
				} else {
					object = request.requestCallback.handleConnection(response);
				}
				if (request.mRequestListener != null) {
					return request.mRequestListener
							.onAfterDoingBackground(object);
				}
				return object;
			} else {
				return null;
			}
		} catch (AppException e) {
			return e;
		} finally {
			if (response != null) {
				response.disconnect();
			}
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (request.requestCallback != null) {
			if (result instanceof AppException) {
				request.requestCallback.onFailure((AppException) result);
			} else {
				request.requestCallback.onCallback(result);
			}
		}
		if (request.mRequestListener != null) {
			request.mRequestListener.onPostExecute();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (request.mProgressChangeListener != null) {
			request.mProgressChangeListener.progressChanged(values[0],
					values[1], operationName);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (request.mRequestListener != null) {
			request.mRequestListener.onCancelled();
		}
	}
}
