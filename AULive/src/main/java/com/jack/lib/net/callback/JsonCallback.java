package com.jack.lib.net.callback;

import android.text.TextUtils;
import android.util.Log;

import com.jack.lib.AppException;
import com.jack.lib.AppException.ErrorType;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.utils.IOUtilities;
import com.jack.utils.JsonParser;

public abstract class JsonCallback<T> extends AbstractCallback<T> {
	@Override
	protected T bindData(String res, IProgressListener task)
			throws AppException {
		super.bindData(res, task);
		if (res.length() > 10000) {
			// log
			Log.d("JsonCallback", res.substring(res.length() - 1000));
		} else {
			Log.d("JsonCallback", res);
		}

		if (!TextUtils.isEmpty(path)) {
			res = IOUtilities.readFromFile(res);
		}
		if (mReturnType != null) {
			return JsonParser.deserializeByJson(res, mReturnType);
		} else {
			throw new AppException(ErrorType.JsonException, "JsonException",
					"json return type should be set", null);
		}
	}

}
