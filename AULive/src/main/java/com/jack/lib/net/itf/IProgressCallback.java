package com.jack.lib.net.itf;

public interface IProgressCallback {
	void onProgressChanged(int which,String status,String progress,boolean isProgress,boolean isComplete);
}
