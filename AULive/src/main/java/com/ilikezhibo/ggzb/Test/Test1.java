package com.ilikezhibo.ggzb.Test;

import com.jack.lib.AppException;
import com.jack.lib.net.itf.ICallback;
import com.jack.lib.net.itf.IProgressListener;

import org.apache.http.HttpResponse;

import java.net.HttpURLConnection;


/**
 * Created by stephensun on 2017/1/5.
 */
public abstract class Test1 implements ICallback {



    @Override
    public Object handleResponse(HttpResponse response) throws AppException {
        return null;
    }

    @Override
    public Object handleResponse(HttpResponse response, IProgressListener task) throws AppException {
        return null;
    }

    @Override
    public Object handleConnection(HttpURLConnection response) throws AppException {
        return null;
    }

    @Override
    public Object handleConnection(HttpURLConnection response, IProgressListener task) throws AppException {
        return null;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void checkIsCancelled() throws AppException {

    }
}
