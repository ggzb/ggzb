package com.gauss.recorder;

import com.gauss.speex.encode.SpeexDecoder;
import com.gauss.speex.encode.SpeexDecoder.SpeexCompletionListener;
import java.io.File;

/**
 * @author Gauss
 * 
 */
public class SpeexPlayer {
	private String fileName = null;
	private SpeexDecoder speexdec = null;

	public SpeexPlayer(String fileName) {

		this.fileName = fileName;
		try {
			speexdec = new SpeexDecoder(new File(this.fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startPlay() {
		RecordPlayThread rpt = new RecordPlayThread();

		Thread th = new Thread(rpt);
		th.start();
	}

	public void stopPlay() {
		if (speexdec != null && !speexdec.isPaused())
			speexdec.setPaused(true);
	}

	boolean isPlay = true;

	public boolean isPaused() {
		if (speexdec != null)
			return speexdec.isPaused();
		return true;
	}

	public void setSpeexCompletionListener(SpeexCompletionListener listener) {
		if (speexdec != null) {
			speexdec.setListener(listener);
		}
	}

	class RecordPlayThread extends Thread {

		public void run() {
			try {
				if (speexdec != null)
					speexdec.decode();

			} catch (Exception t) {
				t.printStackTrace();
			}
		}
	};
}
