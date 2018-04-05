package com.jack.lib.net.callback;

import android.text.TextUtils;

import com.jack.lib.AppException;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.utils.IOUtilities;

public abstract class StringCallback extends AbstractCallback<String> {
	@Override
	protected String bindData(String res, IProgressListener task)
			throws AppException {
		super.bindData(res, task);
		if (!TextUtils.isEmpty(path)) {
			res = IOUtilities.readFromFile(res);
		}
		return res;
	}

}

