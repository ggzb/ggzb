package com.gauss.recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import com.gauss.speex.encode.SpeexEncoder;

public class SpeexRecorder implements Runnable {

	private volatile boolean isRecording;
	private final Object mutex = new Object();
	private static final int frequency = 8000;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	public static int packagesize = 160;
	private String fileName = null;

	private VolumeListener volumeListener;

	public SpeexRecorder(String fileName) {
		super();
		this.fileName = fileName;
	}

	public void run() {
		// 启动编码线程
		SpeexEncoder encoder = null;
		try {
			encoder = new SpeexEncoder(this.fileName);

			Thread encodeThread = new Thread(encoder);
			encoder.setRecording(true);
			encodeThread.start();
			synchronized (mutex) {
				while (!this.isRecording) {
					try {
						mutex.wait();
					} catch (InterruptedException e) {
						throw new IllegalStateException("Wait() interrupted!",
								e);
					}
				}
			}
			android.os.Process
					.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

			int bufferRead = 0;
			int bufferSize = AudioRecord.getMinBufferSize(frequency,
					AudioFormat.CHANNEL_IN_MONO, audioEncoding);

			short[] tempBuffer = new short[packagesize];

			AudioRecord recordInstance = new AudioRecord(
					MediaRecorder.AudioSource.MIC, frequency,
					AudioFormat.CHANNEL_IN_MONO, audioEncoding, bufferSize);

			recordInstance.startRecording();

			while (this.isRecording) {
				bufferRead = recordInstance.read(tempBuffer, 0, packagesize);
				// bufferRead = recordInstance.read(tempBuffer, 0, 320);
				if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
					throw new IllegalStateException(
							"read() returned AudioRecord.ERROR_INVALID_OPERATION");
				} else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
					throw new IllegalStateException(
							"read() returned AudioRecord.ERROR_BAD_VALUE");
				} else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
					throw new IllegalStateException(
							"read() returned AudioRecord.ERROR_INVALID_OPERATION");
				}

				long v = 0;
				// 将 buffer 内容取出，进行平方和运算
				for (int i = 0; i < tempBuffer.length; i++) {
					v += tempBuffer[i] * tempBuffer[i];
				}
				// 平方和除以数据总长度，得到音量大小。
				double mean = v / (double) bufferRead;
				double volume = 10 * Math.log10(mean);
				if (this.volumeListener != null) {
					volumeListener.onRecordVolume(volume);
				}

				encoder.putData(tempBuffer, bufferRead);
			}

			recordInstance.stop();
			recordInstance.release();
			recordInstance = null;
			// tell encoder to stop.
			encoder.setRecording(false);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			if (encoder != null) {
				encoder.setRecording(false);
			}
		}
	}

	public void setVolumeListener(VolumeListener listener) {
		this.volumeListener = listener;
	}

	public VolumeListener getVolumeListener() {
		return volumeListener;
	}

	public interface VolumeListener {
		void onRecordVolume(double volume);
	}

	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}
}
