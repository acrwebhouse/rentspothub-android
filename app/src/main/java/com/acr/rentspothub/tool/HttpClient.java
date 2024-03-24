package com.acr.rentspothub.tool;

import android.util.Log;

import com.acr.rentspothub.define.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient {
    private OkHttpClient okHttpClient;
    private String serverToken;

    public HttpClient() {
//        okHttpClient = new OkHttpClient();
        serverToken = Constants.EMPTY_STRING;
        createOkHttpClient();
    }

    public HttpClient(String serverToken) {
        okHttpClient = new OkHttpClient();
        this.serverToken = serverToken;
        createOkHttpClient();
    }

    public void setServerToken(String serverToken) {
        this.serverToken = serverToken;
    }

    public String getServerToken() {
        return serverToken;
    }

    public void createOkHttpClient() {
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                        Log.d("okhttp","saveFromResponse");
//                        Log.d("okhttp",""+url.toString());
//                        Log.d("okhttp",""+ cookies.toString());
                        String cookiesString = cookies.toString();
                        if (cookiesString.indexOf(Constants.SERVER_TOKEN_TITLE) > 0) {
                            cookiesString = cookiesString.substring(cookiesString.indexOf(Constants.SERVER_TOKEN_TITLE) + 4, cookiesString.indexOf(Constants.SEMICOLON));
                            serverToken = cookiesString;
                        }
                        Log.d("okhttp", " :::   " + cookiesString);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
//                        Log.d("okhttp","loadForRequest");
//                        Log.d("okhttp",""+ url.toString());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();
    }

    public OkHttpClient getClient() {
        return okHttpClient;
    }

    public Call getCall(Request request) {
        return okHttpClient.newCall(request);
    }

    public void addNotification(final String firebaseToken, final String userId ,Model controlModel, final String xToken){
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.TOKEN, firebaseToken)
                .add(Constants.USER_ID, userId)
                .add(Constants.TYPE, Constants.ANDROID_NOTIFICATION_TYPE)
                .build();
        Request request = new Request.Builder()
                .url(Constants.ADD_NOTIFICATION_REST_API)
                .addHeader(Constants.X_TOKEN,xToken)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("http", "http rest api  addNotification  fail         ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("http", "http rest api  addNotification  success  ");
                String receiveMessage = response.body().string();
                Log.d("http", "http rest api  addNotification  success   receiveMessage   " + receiveMessage);
                JSONObject json = controlModel.getJsonObject(receiveMessage);
                try {
                    if(json.get(Constants.STATUS).toString().equals(Constants.TRUE_STRING)){
                        JSONObject data = (JSONObject) json.get(Constants.DATA);
                        String id = data.getString(Constants._ID);
                        controlModel.saveNotificationId(id);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void editNotification(final String firebaseToken, final String userId , final String notificationId ,final Model controlModel , final String xToken){
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.ID, notificationId)
                .add(Constants.TOKEN, firebaseToken)
                .add(Constants.USER_ID, userId)
                .add(Constants.TYPE, Constants.ANDROID_NOTIFICATION_TYPE)
                .build();
        Request request = new Request.Builder()
                .url(Constants.EDIT_NOTIFICATION_REST_API)
                .addHeader(Constants.X_TOKEN,xToken)
                .put(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("http", "http rest api  editNotification  fail         ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("http", "http rest api  editNotification  success  ");
                String receiveMessage = response.body().string();
                Log.d("http", "http rest api  editNotification  success   receiveMessage   " + receiveMessage);
                JSONObject json = controlModel.getJsonObject(receiveMessage);
                try {
                    if(json.get(Constants.STATUS).toString().equals(Constants.TRUE_STRING)){
                        String data = json.getString(Constants.DATA);
                        if(data.equals((Constants.NOTIFICATION_NO_MATCH_ID))){
                            addNotification(firebaseToken, userId , controlModel ,xToken );
                        }else{
                            JSONObject dataObj = (JSONObject) json.get(Constants.DATA);
                            String id = dataObj.getString(Constants._ID);
                            controlModel.saveNotificationId(id);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void login(final String account, final String password ,final Model controlModel,final JavaScriptInterface javaScriptInterface, final String loadUrl ){
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.ACCOUNT_OR_MAIL, account)
                .add(Constants.PASSWORD, password)
                .build();
        Request request = new Request.Builder()
                .url(Constants.LOGIN_REST_API)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("http", "http rest api  login  fail         ");
                javaScriptInterface.showInternelErrorPage();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("http", "http rest api  login  success  ");
                String receiveMessage = response.body().string();
                Log.d("http", "http rest api  login  success   receiveMessage   " + receiveMessage);
                JSONObject json = controlModel.getJsonObject(receiveMessage);
                try {
                    if(json.get(Constants.STATUS).toString().equals(Constants.TRUE_STRING)){
                        JSONObject data = (JSONObject) json.get(Constants.DATA);
                        String refreshToken = data.getString(Constants.REFRESH_TOKEN);
                        javaScriptInterface.autoLogin(refreshToken,loadUrl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    javaScriptInterface.showInternelErrorPage();
                }
            }
        });
    }


}
