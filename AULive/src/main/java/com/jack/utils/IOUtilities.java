package com.jack.utils;

import android.text.TextUtils;
import com.jack.lib.AppException;
import com.jack.lib.AppException.ErrorType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IOUtilities {

	public static final int IO_BUFFER_SIZE = 4 * 1024;

	/**
	 * Copy the content of the input stream into the output stream, using reset
	 * temporary byte array buffer whose size is defined by
	 * {@link #IO_BUFFER_SIZE}.
	 * 
	 * @param in
	 *            The input stream to copy from.
	 * @param out
	 *            The output stream to copy to.
	 * 
	 * @throws java.io.IOException
	 *             If any error occurs during the copy.
	 */
	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	/** 返回写入内存的数�? */
	public static String readStreamToMemory(InputStream in) throws AppException {
		if (in == null) {
			return null;
		}
		ByteArrayOutputStream out = null;
		byte[] result = null;
		try {
			byte[] buffer = new byte[1024];
			int len = -1;
			out = new ByteArrayOutputStream();
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			result = out.toByteArray();
		} catch (IOException e) {
			throw new AppException(ErrorType.FileException, "IOException",
					e.getMessage(), null);
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new String(result);
	}

	/** 返回文件想对应的字节数据. */
	public static byte[] getBytesFromFile(String path) {
		if (TextUtils.isEmpty(path) == true) {
			return null;
		}
		try {
			File f = new File(path);
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			Trace.e(e.toString());
		}
		return null;
	}

	/** 返回文件中的内容. */
	public static String readFromFile(String path) throws AppException {
		if (!TextUtil.isValidate(path)) {
			throw new AppException(ErrorType.FileException,
					"FileNotFoundException", "the file(" + path
							+ ") is not exist", null);
		}
		ByteArrayOutputStream out = null;
		FileInputStream in = null;
		try {
			File f = new File(path);
			in = new FileInputStream(f);
			out = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1000];
			int n;
			while ((n = in.read(b)) != -1)
				out.write(b, 0, n);
			out.flush();
			return new String(out.toByteArray());
		} catch (FileNotFoundException e) {
			throw new AppException(ErrorType.FileException,
					"FileNotFoundException", e.getMessage(), null);
		} catch (IOException e) {
			throw new AppException(ErrorType.FileException, "IOException",
					e.getMessage(), null);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
