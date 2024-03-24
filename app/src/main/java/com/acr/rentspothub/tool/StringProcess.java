package com.acr.rentspothub.tool;

import com.acr.rentspothub.define.Constants;
import com.acr.rentspothub.role.Config;

public class StringProcess {
    public static String getQueryConfigWhereStringByPK(Config config) {
        return Constants.ID_SQL + "='" + config.getId() + "'";
    }

    // getJavascriptFunctionString
    public static String getJavascriptFunctionString(String arg, String functionName) {
        String result = Constants.JAVASCRIPT + ":" + functionName + "('" + arg + "')";
        return result;
    }

    public static String getJavascriptFunctionSetAccountPasswordUrlString(String account, String password) {
        String arg = account + "','" + password;
        return getJavascriptFunctionString(arg, Constants.SET_ACCOUNT_PASSWORD_JAVASCRIPT);
    }

    public static String getReserveHouseUrl(String reserveHouseId) {
        return Constants.SERVER_URL + "/21/" + reserveHouseId;
    }

    public static String getCookieTokenRow(String refreshToken) {
        return "x-refresh-token=" + refreshToken;
    }

}
