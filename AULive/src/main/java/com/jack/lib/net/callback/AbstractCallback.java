package com.jack.lib.net.callback;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

import com.jack.lib.AppException;
import com.jack.lib.AppException.ErrorType;
import com.jack.lib.UserInfo;
import com.jack.lib.net.itf.ICallback;
import com.jack.lib.net.itf.IProgressListener;
import com.jack.utils.IOUtilities;

public abstract class AbstractCallback<T> implements ICallback<T> {
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	protected Class<T> mReturnType;
	private boolean isCancelled = false;
	protected String path;

	protected T bindData(String res, IProgressListener task)
			throws AppException {
		checkIsCancelled();
		return null;
	}

	@Override
	public Object handleResponse(HttpResponse response) throws AppException {
		return handleResponse(response, null);
	}

	@Override
	public Object handleResponse(HttpResponse response, IProgressListener task)
			throws AppException {
		checkIsCancelled();
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity httpEntity = response.getEntity();
			long contentLength = httpEntity != null ? httpEntity
					.getContentLength() : 0;
			if (statusCode == HttpStatus.SC_OK) {
				// if set path , write the data into file
				if (!TextUtils.isEmpty(path)) {
					FileOutputStream fos = new FileOutputStream(path);
					InputStream in = null;
					if (httpEntity.getContentEncoding() != null) {
						String encoding = httpEntity.getContentEncoding()
								.getValue();
						if (encoding != null
								&& "gzip".equalsIgnoreCase(encoding))
							in = new GZIPInputStream(httpEntity.getContent());
						else if (encoding != null
								&& "deflate".equalsIgnoreCase(encoding))
							in = new InflaterInputStream(
									httpEntity.getContent());
					} else {
						in = httpEntity.getContent();
					}
					byte[] b = new byte[IO_BUFFER_SIZE];
					int curPos = 0;
					int read;
					while ((read = in.read(b)) != -1) {
						checkIsCancelled();
						if (task != null) {
							curPos += read;
							if (contentLength != -1) {
								task.progressChanged(
										IProgressListener.DOWNLOADING,
										(int) (curPos * 100 / contentLength),
										null);
							} else {
								task.progressChanged(
										IProgressListener.DOWNLOADING,
										curPos / 1000, null);
							}
						}
						fos.write(b, 0, read);
					}
					fos.flush();
					fos.close();
					in.close();
					if (task != null) {
						task.progressChanged(IProgressListener.DONE, 100, null);
					}
					return bindData(path, task);
				} else {
					return bindData(
							httpEntity == null ? null
									: EntityUtils.toString(httpEntity), task);
				}
			} else {
				UserInfo errorInfo = new UserInfo();
				if (httpEntity != null) {
					errorInfo.putInfo(UserInfo.CLOUD_RESPONSE_BODY,
							EntityUtils.toString(httpEntity));
				}
				errorInfo.putInfo(UserInfo.CLOUD_RESPONSE_STATUS_CODE,
						statusCode);
				return new AppException(ErrorType.CloudException,
						"response code is not 200", response.getStatusLine()
						.getReasonPhrase(), errorInfo);
			}
		} catch (FileNotFoundException e) {
			throw new AppException(ErrorType.ConnectionException,
					"FileNotFoundException", e.getMessage(), null);
		} catch (IllegalStateException e) {
			throw new AppException(ErrorType.ConnectionException,
					"IllegalStateException", e.getMessage(), null);
		} catch (ParseException e) {
			throw new AppException(ErrorType.ConnectionException,
					"ParseException", e.getMessage(), null);
		} catch (IOException e) {
			throw new AppException(ErrorType.ConnectionException,
					"IOException", e.getMessage(), null);
		}
	}

	@Override
	public Object handleConnection(HttpURLConnection response)
			throws AppException {
		return handleConnection(response, null);
	}

	@Override
	public Object handleConnection(HttpURLConnection response,
								   IProgressListener task) throws AppException {
		checkIsCancelled();
		int statusCode = -1;
		try {
			statusCode = response.getResponseCode();
		} catch (IOException e) {
			throw new AppException(ErrorType.ConnectionException,
					"IOException", e.getMessage(), null);
		}
		long contentLength = response.getContentLength();
		InputStream in = null;
		try {
			switch (statusCode) {
				case HttpStatus.SC_OK:
					String encoding = response.getContentEncoding();
					if (encoding != null && "gzip".equalsIgnoreCase(encoding))
						in = new GZIPInputStream(response.getInputStream());
					else if (encoding != null
							&& "deflate".equalsIgnoreCase(encoding))
						in = new InflaterInputStream(response.getInputStream());
					else
						in = response.getInputStream();
					// if set path , write the data into file
					if (!TextUtils.isEmpty(path)) {
						FileOutputStream fos = new FileOutputStream(path);
						byte[] b = new byte[IO_BUFFER_SIZE];
						int curPos = 0;
						int read;
						while ((read = in.read(b)) != -1) {
							checkIsCancelled();
							if (task != null) {
								curPos += read;
								if (contentLength != -1) {
									task.progressChanged(
											IProgressListener.DOWNLOADING,
											(int) (curPos * 100 / contentLength),
											null);
								} else {
									task.progressChanged(
											IProgressListener.DOWNLOADING,
											curPos / 1024, null);
								}
							}
							fos.write(b, 0, read);
						}
						fos.flush();
						fos.close();
						in.close();
						if (task != null) {
							task.progressChanged(IProgressListener.DONE, 100, null);
						}
						return bindData(path, task);
					} else {
						return bindData(IOUtilities.readStreamToMemory(in), task);
					}
				case HttpStatus.SC_CREATED:
					UserInfo errorInfo = new UserInfo();
					errorInfo.putInfo(UserInfo.CLOUD_RESPONSE_STATUS_CODE,
							statusCode);
					errorInfo.putInfo(UserInfo.CLOUD_RESPONSE_BODY,
							response.getResponseMessage());
					throw new AppException(ErrorType.CloudException,
							"HttpStatus.SC_CREATED", response.getResponseMessage(),
							errorInfo);
				default:
					UserInfo errorInfo1 = new UserInfo();
					errorInfo1.putInfo(UserInfo.CLOUD_RESPONSE_STATUS_CODE,
							statusCode);
					errorInfo1.putInfo(UserInfo.CLOUD_RESPONSE_BODY, IOUtilities
							.readStreamToMemory(response.getErrorStream()));
					throw new AppException(ErrorType.CloudException,
							"CloudException", response.getResponseMessage(),
							errorInfo1);
			}
		} catch (FileNotFoundException e) {
			throw new AppException(ErrorType.FileException,
					"FileNotFoundException", e.getMessage(), null);
		} catch (IOException e) {
			throw new AppException(ErrorType.ConnectionException,
					"IOException", e.getMessage(), null);
		}
	}

	public AbstractCallback<T> setReturnType(Class<T> clz) {
		this.mReturnType = clz;
		return this;
	}

	public AbstractCallback<T> setFilePath(String filePath) {
		this.path = filePath;
		return this;
	}

	public void cancel() {
		this.isCancelled = true;
	}

	public void checkIsCancelled() throws AppException {
		if (isCancelled) {
			throw new AppException(ErrorType.CancelException, "task cancel",
					"the request has been cancelled", null);
		}
	}
}
