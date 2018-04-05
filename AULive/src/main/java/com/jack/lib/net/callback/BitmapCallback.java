package com.jack.lib.net.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jack.lib.AppException;
import com.jack.lib.net.itf.IProgressListener;

public abstract class BitmapCallback extends AbstractCallback<Bitmap> {
	@Override
	protected Bitmap bindData(String res, IProgressListener task)
			throws AppException {
		super.bindData(res, task);
		if (!TextUtils.isEmpty(path)) {
			return BitmapFactory.decodeFile(path);
		}
		return null;
	}

}

