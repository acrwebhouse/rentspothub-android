package com.acr.rentspothub.define;

/**
 * Created by Chris.Wu on 2016/10/21.
 */
public class Constants {
    public static String SERVER_URL = "https://matchrentdev.com";
//    public static String SERVER_URL = "https://rentspothub.com";
//      public static String SERVER_URL = "http://192.168.0.100:3000";
//      public static String SERVER_URL = "http://192.168.0.11:3000";
    //web url
    public static final String NETWORK_ERROR_WEB_URL = "file:///android_asset/networkErrorWeb.html";

    //javascript
    public static final String ANDROID_PARAMETER_FOR_JAVASCRIPT = "jsToAndroidInterface";
    public static final String JAVASCRIPT_PARAMETER_FOR_ANDROID = "jsAppInterface";
    public static final String SET_ACCOUNT_PASSWORD_JAVASCRIPT = "setDefaultAccountPassword";


    //permissions
    public static final int SYSTEM_ALERT_WINDOW_PERMISSIONS_REQUEST_CODE = 0;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST_CODE = 1;

    //sqlite
    public static final String DATABASE_NAME = "ACRDB";
    public static final int DATABASE_VERSION = 1;
    public static final String ID_SQL = "id";
    //table user
    public static final String TABLE_USER_SQL = "User";
    public static final String USER_ID_SQL = "UserId";
    public static final String USER_USERNAME_SQL = "username";
    public static final String USER_PASSWORD_SQL = "password";
    public static final String USER_PROJECT_NAME_SQL = "projectName";
    public static final String USER_SERVER_TOKEN_SQL = "serverToken";
    public static final String USER_SERVER_IP_SQL = "ip";
    public static final String USER_TABLE_CREATE_SQL = "CREATE TABLE "
            + TABLE_USER_SQL + " ( "
            + USER_USERNAME_SQL + " text not null ," + USER_PASSWORD_SQL + " text not null ,"
            + USER_PROJECT_NAME_SQL + " text not null ,"+ USER_SERVER_TOKEN_SQL + " text not null ,"
            + USER_SERVER_IP_SQL + " text not null ,"
            + "CONSTRAINT "+USER_ID_SQL+" PRIMARY KEY ("+USER_USERNAME_SQL+","+USER_PROJECT_NAME_SQL+","+USER_SERVER_IP_SQL+")); ";


    //table config
    public static final String TABLE_CONFIG_SQL = "Config";
    public static final String CONFIG_FIREBASE_TOKEN_SQL = "firebaseToken";
    public static final String CONFIG_NOTIFICATION_ID_SQL = "notificationId";
    public static final String CONFIG_ACCOUNT_SQL = "account";
    public static final String CONFIG_PASSWORD_SQL = "password";
    public static final String CONFIG_USER_ID_SQL = "userId";

    public static final String CONFIG_TABLE_CREATE_SQL = "CREATE TABLE "
            + TABLE_CONFIG_SQL + " ( " + ID_SQL + "  INTEGER primary key autoincrement, "
            + CONFIG_FIREBASE_TOKEN_SQL + " text not null,"
            + CONFIG_NOTIFICATION_ID_SQL + " text not null,"
            + CONFIG_ACCOUNT_SQL + " text not null,"
            + CONFIG_PASSWORD_SQL + " text not null,"
            + CONFIG_USER_ID_SQL + " text not null"
            +"); ";


    //util
    public static final String OPEN_BRACE = "{";
    public static final String CLOSE_BRACE = "}";
    public static final String USER_ID = "userId";
    public static final String TOKEN = "token";
    public static final String ANDROID_NOTIFICATION_TYPE = "1";
    public static final String RESULT_REST_API = "resStatus";
    public static final String RES_STRING_REST_API = "resString";
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String HTTP = "http:";
    public static final String HTTPS = "https:";
    public static final String SLASH = "//";
    public static final String EMPTY_STRING = "";
    public static final String IP = "ip";
    public static final String ACCOUNT_OR_MAIL = "accountOrMail";
    public static final String USERNAME = "username";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String REMEMBER = "remember";
    public static final String TYPE = "type";
    public static final String PROJECT_NAME = "projectName";
    public static final String PROJECT_NAME_1 = "projectName1";
    public static final String PROJECT = "project";
    public static final String TRUE_STRING = "true";
    public static final String FALSE_STRING = "false";
    public static final String FIREBASE_TOKEN = "firebaseToken";
    public static final String DOUBLE_QUOTES="\"";
    public static final String JAVASCRIPT="javascript";
    public static final String IP_LIST="ipList";
    public static final String PROJECT_LIST="projectList";
    public static final String ACCOUNT_LIST="accountList";
    public static final String FUNCTION_LIST="funcList";
    public static final String HTTP_FAIL="{"+DOUBLE_QUOTES+RESULT_REST_API+DOUBLE_QUOTES+":"+ONE+"}";
    public static final String URL="url";
    public static final String START="start";
    public static final String COUNT="count";
    public static final String FILTERS="filters";
    public static final String SORT="sort";
    public static final String COOKIE="Cookie";
    public static final String STATUS = "status";
    public static final String DATA = "data";
    public static final String SERVER_TOKEN_TITLE="WDT=";
    public static final String SEMICOLON=";";
    public static final String _ID = "_id";
    public static final String ID = "id";
    public static final String NOTIFICATION_NO_MATCH_ID = "no match id";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String X_TOKEN = "x-token";
    public static final String LINE_PACKAGE_NAME = "jp.naver.line.android";
    public static final String GOOGLE_PLAY_LINE_LINK = "market://details?id=" + LINE_PACKAGE_NAME;
    public static final String GOOGLE_PLAY_VENDING = "com.android.vending";

    //rest api
    public static String ADD_NOTIFICATION_REST_API = SERVER_URL + "/user-service/notification/addNotification";
    public static String EDIT_NOTIFICATION_REST_API = SERVER_URL + "/user-service/notification/editNotification";
    public static String LOGIN_REST_API = SERVER_URL + "/auth-service/auth/login";

    //notification
    public static final String NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_CONTENT = "body";
    public static final String NOTIFICATION_RESERVE_HOUSE_ID = "reserveHouseId";
    public static final int NOTIFICATION_TYPE_RESERVE_HOUSE = 1;
    public static final int NOTIFICATION_TYPE_SYSTEM = 0;
}
