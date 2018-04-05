package com.jack.lib.net.itf;

import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;

import com.jack.lib.AppException;

public interface ICallback<T> {
	void onCallback(T callback);

	void onFailure(AppException e);

	Object handleResponse(HttpResponse response) throws AppException;

	Object handleResponse(HttpResponse response, IProgressListener task)
			throws AppException;

	Object handleConnection(HttpURLConnection response) throws AppException;

	Object handleConnection(HttpURLConnection response, IProgressListener task)
			throws AppException;

	void cancel();

	void checkIsCancelled() throws AppException;
}
