package com.acr.rentspothub.tool;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.acr.rentspothub.define.Constants;
import com.acr.rentspothub.role.Config;

public class JavaScriptInterface {
    private final String TAG = "JavaScriptInterface";
    private WebView controlWebView;
    private Model controlModel;
    private Activity controlActivity;

    public JavaScriptInterface(Activity activity ,WebView webView, Model model
    ){
        controlWebView = webView;
        controlModel = model;
        controlActivity = activity;
    }

    @JavascriptInterface
    public void saveUserInfo(final String account,final String password,final String userId) {
        Log.d(TAG, "  saveUserInfo  account " + account);
        Log.d(TAG, "  saveUserInfo  password " + password);
        Log.d(TAG, "  saveUserInfo  userId " + userId);
        controlModel.saveConfig(account,password,userId);
        //controlModel.sendNotificationRequest();
    }

    @JavascriptInterface
    public void setAccountPassword() {
        Log.d(TAG, "  setAccountPassword   " );
        Config config =controlModel.getConfig();
        String account = config.getAccount();
        String password = config.getPassword();
        Log.d(TAG, " setAccountPassword account   "+account );
        Log.d(TAG, " setAccountPassword password   " +password);
        String setAccountPasswordUrl = StringProcess.getJavascriptFunctionSetAccountPasswordUrlString(account,password);
        Log.d(TAG, "  setAccountPassword  setAccountPasswordUrl " +setAccountPasswordUrl);
        controlActivity.runOnUiThread(new Runnable() {
            //  @Override
            public void run() {
                controlWebView.loadUrl(setAccountPasswordUrl);
            }
        });

    }

    @JavascriptInterface
    public void backPage() {
        Log.d(TAG, "  backPage   " );
        controlActivity.runOnUiThread(new Runnable() {
            //  @Override
            public void run() {
                controlWebView.goBack();
            }
        });
    }

    @JavascriptInterface
    public void restartApp() {
        Log.d(TAG, "  restartApp   " );
        controlActivity.runOnUiThread(new Runnable() {
            //  @Override
            public void run() {
                controlWebView.loadUrl(Constants.SERVER_URL);
            }
        });
    }

    @JavascriptInterface
    public void loadUrl(final String url) {
        Log.d(TAG, "  loadUrl  url " +url);
        controlActivity.runOnUiThread(new Runnable() {
            //  @Override
            public void run() {
                controlWebView.loadUrl(Constants.SERVER_URL + url);
            }
        });
    }

    @JavascriptInterface
    public void callUp(final String phoneNumber) {
        Log.d(TAG, "  callUp  phoneNumber " + phoneNumber);
        // 要撥打的電話號碼
        String callNumber = "tel:" + phoneNumber;

        // 建立一個 Intent 物件，指定動作為 ACTION_CALL
        Intent dialIntent = new Intent(Intent.ACTION_CALL);

        // 設定 Uri，表示要執行的動作和資料
        dialIntent.setData(Uri.parse(callNumber));
        try {
            controlActivity.startActivity(dialIntent);
        }catch (Error error){

        }

    }

    @JavascriptInterface
    public void addLineFriend(final String lineId) {
        Log.d(TAG, "  addLineFriend  lineId " + lineId);
        // Line ID，替換成你要加的好友的 Line ID

        // 生成 Line 的 URI
        Uri uri = Uri.parse("line://ti/p/~" + lineId);

        // 建立 Intent 並設定 Action 為 VIEW，Data 為 Line URI
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        try {
            controlActivity.startActivity(intent);
        }catch (Error error){
            openLineInstallPage();
        }
    }

    private void openLineInstallPage() {
        try {
            String linePackageName = Constants.LINE_PACKAGE_NAME;

            // 创建Intent并设置动作为查看Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);

            // 设置URI，包括Google Play上Line应用的链接
            intent.setData(Uri.parse(Constants.GOOGLE_PLAY_LINE_LINK));

            // 设置标志，指定使用Google Play打开链接
            intent.setPackage(Constants.GOOGLE_PLAY_VENDING);

            // 启动Intent
            controlActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void saveNotificationInfo(final String xToken) {
        Log.d(TAG, "  saveNotificationInfo  xToken " + xToken);
        controlModel.sendNotificationRequest(xToken);
    }

    @JavascriptInterface
    public void showInternelErrorPage() {
        Log.d(TAG, "showInternelErrorPage");
        controlActivity.runOnUiThread(new Runnable() {
            //  @Override
            public void run() {
                controlWebView.loadUrl(Constants.NETWORK_ERROR_WEB_URL);
            }
        });
    }


    @JavascriptInterface
    public void autoLogin(String refreshToken, String loadUrl) {
        Log.d(TAG, "autoLogin loadUrl : "+loadUrl);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String cookieStr = StringProcess.getCookieTokenRow(refreshToken);
        cookieManager.setCookie(Constants.SERVER_URL, cookieStr);
        controlActivity.runOnUiThread(new Runnable() {
            //  @Override
            public void run() {
                controlWebView.loadUrl(loadUrl);
            }
        });
    }
}
