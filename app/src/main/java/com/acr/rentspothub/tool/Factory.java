package com.acr.rentspothub.tool;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.webkit.WebView;

import com.acr.rentspothub.role.Config;


/**
 * Created by Chris.Wu on 2016/10/20.
 */
public class Factory {

    // change Factory to singleton

    private static final Factory ourInstance = new Factory();

    public static Factory getInstance() {
        return ourInstance;
    }

    private Factory() {
    }

    public HttpClient createHttpClient() {
        return new HttpClient();
    }

    public DBConnection createDBConnection(Context context) {
        return new DBConnection(context);
    }

    public JavaScriptInterface createJavaScriptInterface(Activity activity, WebView webView,Model model) {
        return new JavaScriptInterface(activity, webView,model);
    }

    public Model createModel(Activity activity) {
        return new Model(activity);
    }

    public Model createModel(Application application) {
        return new Model(application);
    }

    public Model createModel(Context context) {
        return new Model(context);
    }

    public Model createModel() {
        return new Model();
    }

    public Config createConfig(){
        return new Config();
    }

    public Config createConfig(int id,String firebaseToken,String account,String password,String notificationId,String userId){
        return new Config(id,firebaseToken,account,password,notificationId,userId);
    }

}
