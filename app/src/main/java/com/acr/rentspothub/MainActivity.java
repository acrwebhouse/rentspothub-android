package com.acr.rentspothub;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.util.Log;
import android.view.KeyEvent;
import android.net.Uri;
import android.webkit.WebChromeClient;

import android.os.Bundle;

import com.acr.rentspothub.tool.Factory;
import com.acr.rentspothub.tool.JavaScriptInterface;
import com.acr.rentspothub.tool.Model;
import com.acr.rentspothub.tool.StringProcess;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.acr.rentspothub.define.Constants;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String url = Constants.SERVER_URL;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    JavaScriptInterface controlJavaScriptInterface;
    private Factory factory = Factory.getInstance();
    private Model controlModel;
    private static final String TAG = "MyFirebaseMsgService";

    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate : " );
        resolveIntent();
        setContentView(R.layout.activity_main);
        createObj();
        checkPhonePermission();
    }

    private void checkPhonePermission(){
        // 檢查是否已經授予 CALL_PHONE 權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
            // 如果沒有權限，請求權限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_PERMISSION_REQUEST_CODE);
        } else {
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 使用者同意權限，執行撥打電話的程式碼
                //makePhoneCall();
            } else {
                // 使用者拒絕權限，你可以提供一些提示或處理拒絕的情況
            }
        }
    }

    private void resolveIntent(){
        Intent intent = getIntent();
        int type = intent.getIntExtra(Constants.NOTIFICATION_TYPE,0);
        switch (type){
            case Constants.NOTIFICATION_TYPE_SYSTEM:
                break;
            case Constants.NOTIFICATION_TYPE_RESERVE_HOUSE:
                String reserveHouseId = intent.getStringExtra(Constants.NOTIFICATION_RESERVE_HOUSE_ID);
                url = StringProcess.getReserveHouseUrl(reserveHouseId);
                break;
            default:
        }
    }

    private void createObj() {
        controlModel = factory.createModel(this);
        controlModel.initDB();
        initWebView();
        initJavaScriptInterface();
        initFirebase();
        controlModel.setJavaScriptInterface(controlJavaScriptInterface);
        controlModel.autoLogin(url);
    }

    private void initJavaScriptInterface(){
        controlJavaScriptInterface = factory.createJavaScriptInterface(this,webView,controlModel);
        webView.addJavascriptInterface(controlJavaScriptInterface, Constants.ANDROID_PARAMETER_FOR_JAVASCRIPT);
    }

    private void initFirebase(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String updatedToken = instanceIdResult.getToken();
                Log.d("initFirebase",updatedToken);
                controlModel.saveFirebaseToken(updatedToken);

            }
        });
    }

    private void initWebView(){
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(webViewClient);
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description,String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.loadUrl(Constants.NETWORK_ERROR_WEB_URL);
            }

        });

        webView.setWebChromeClient(new WebChromeClient()
        {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    intent = fileChooserParams.createIntent();
                }
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
            {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });

        webSettings.setDisplayZoomControls(false);

//        controlModel.autoLogin();

        //webViewLoadUrl("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYzZTY0ZjM5ZWJmMjAzMDAxZjZiMjAyMCIsInJvbGVzIjpbNCwzXSwiZXhwaXJlZCI6MzAwMDAwLCJpYXQiOiIyMDIzLTA0LTEyVDEyOjI2OjAzLjM5N1oifQ.HOY-UqcgVqffEwPBBWcG5DycWWI-Ul40B7AoFFuhFOg");
    }

    public void webViewLoadUrl(String token){
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //cookieManager.setCookie(url, StringProcess.getCookieTokenRow(token));
        webView.loadUrl(url);//加载url
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }


    }


    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ansen", "是否有上一个页面:" + webView.canGoBack());
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}