package com.jack.lib.net;

import android.os.AsyncTask;
import com.jack.lib.AppException;
import com.jack.lib.net.itf.IProgressListener;
import org.apache.http.HttpResponse;

public class RequestTask extends AsyncTask<Object, Integer, Object> {

	private RequestInformation request;
	private String operationName;

	public RequestTask(RequestInformation request) {
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
		try {
			Object object = null;
			if (request.mRequestListener != null) {
				request.mRequestListener.onBeforeDoingBackground();
			}
			HttpResponse response = HttpUtil.execute(request);
			if (request.requestCallback != null) {
				if (request.mProgressChangeListener != null) {
					IProgressListener task = new IProgressListener() {
						@Override
						public void progressChanged(int status, int percent,
													String operationName) {
							if (operationName != null) {
								RequestTask.this.operationName = operationName;
							}
							if (percent != -1) {
								publishProgress(status, percent);
							}
						}
					};
					object = request.requestCallback.handleResponse(response,
							task);
				} else {
					object = request.requestCallback.handleResponse(response);
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
