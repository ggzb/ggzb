package com.jack.lib.net.callback;

import com.jack.lib.AppException;
import com.jack.lib.net.itf.IProgressListener;

public abstract class PathCallback extends AbstractCallback<String> {
	@Override
	protected String bindData(String path, IProgressListener task)
			throws AppException {
		super.bindData(path, task);
		return path;
	}

}
