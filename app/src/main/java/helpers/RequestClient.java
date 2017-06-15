package helpers;


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by CoredusK on 09-May-17.
 */

public class RequestClient {

    private static final String BASE_URL = "http://print.nepali.mobi/api/v0.1/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context c, String url, StringEntity entity, String type, AsyncHttpResponseHandler responseHandler) {
        client.post(c, getAbsoluteUrl(url), entity, type, responseHandler);

    }


    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(Context c, String url, StringEntity entity, String type, AsyncHttpResponseHandler responseHandler) {
        client.put(c, getAbsoluteUrl(url), entity, type, responseHandler);

    }

    public static void delete(Context c, String url, StringEntity entity, String type, AsyncHttpResponseHandler responseHandler) {
        client.delete(c, getAbsoluteUrl(url), entity, type, responseHandler);

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


}
