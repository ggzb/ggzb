package com.jack.lib.net.itf;

public interface IProgressListener {
	public static final int UPLOADING = 0;
	public static final int DOWNLOADING = 1;
	public static final int STORING = 2;
	public static final int OPTIMIZING = 3;
	public static final int DONE = 4;
	public static final int ERROR = 5;

	void progressChanged(int status, int progress,String operationName);
}
