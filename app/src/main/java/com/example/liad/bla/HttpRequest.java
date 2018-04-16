package com.example.liad.bla;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpRequest extends Thread {


    public static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/weather";

    private Context context;
    private HttpMethod httpMethod;
    private String data;
    private Handler handler;
    private boolean isJsonParams;
    private OnServerResponseListener listener;

    private HttpRequest(Builder builder) {
        context = builder.context;
        isJsonParams = builder.paramsType == ParamsType.JSON;
        httpMethod = builder.httpMethod;
        data = builder.data;
        handler = new Handler();
        listener = builder.listener;
    }

    @Override
    public void run() {
        if (InternetCheck.isNetworkAvailable(context)) {
            try {
                URL url = new URL(SERVER_URL  + (httpMethod == HttpMethod.GET ? "?" + data : ""));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (isJsonParams) {
                    connection.setRequestProperty("Content-Type", "application/json");
                }
                connection.setRequestMethod(httpMethod.toString());
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                if (httpMethod == HttpMethod.POST) {
                    connection.getOutputStream().write(data.getBytes());
                }

                int responseCode = connection.getResponseCode();
                Log.d("TAG", "response code: " + responseCode);

                InputStream in = null;
                if (responseCode == 200) {
                    in = connection.getInputStream();
                } else {
                    in = connection.getErrorStream();
                }
                StringBuilder sb = new StringBuilder();
                int actuallyRead;
                byte[] buffer = new byte[1024];
                while ((actuallyRead = in.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, actuallyRead));
                }
                JSONObject data = new JSONObject(sb.toString());
                if (responseCode == 200) {
                    publishSuccess(data);
                } else {
                    publishFailure(data);
                }
            } catch (Exception e) {
                JSONObject object = new JSONObject();
                try {
                    object.put("error", e.toString());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                publishFailure(object);
            }
        } else {
            JSONObject object = new JSONObject();
            try {
                object.put("error", "No internet connection");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            publishFailure(object);
        }
    }

    private void publishSuccess(final JSONObject data) {
        if (listener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onSuccess(data);
                }
            });
        }
    }

    private void publishFailure(final JSONObject error) {
        if (listener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onFailure(error);
                }
            });
        }
    }


    public static class Builder {
        private Context context;
        private String serverMethod;
        private String data;
        private StringBuilder sb = new StringBuilder();
        private ParamsType paramsType = ParamsType.UNDEFINED;
        private HttpMethod httpMethod = HttpMethod.GET;
        private OnServerResponseListener listener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setParams(String params) {
            if (paramsType != ParamsType.JSON) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(params);
            }
            return this;
        }

        public Builder setHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder setServerMethod(String serverMethod) {
            this.serverMethod = serverMethod;
            return this;
        }

        public HttpRequest start(OnServerResponseListener listener) {
            this.listener = listener;
            if (!(paramsType == ParamsType.JSON)) {
                data = sb.toString();
            }
            HttpRequest request = new HttpRequest(this);
            request.start();
            return request;
        }

    }

    private enum ParamsType {
        UNDEFINED, JSON
    }

}
