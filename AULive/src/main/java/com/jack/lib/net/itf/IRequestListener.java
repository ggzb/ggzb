package com.jack.lib.net.itf;

import java.io.OutputStream;

import com.jack.lib.AppException;

public interface IRequestListener {
	void onPreExecute();

	void onCancelled();

	void onPostExecute();

	Object onAfterDoingBackground(Object object);

	void onBeforeDoingBackground();

	boolean onPrepareParams(OutputStream out) throws AppException;
}
