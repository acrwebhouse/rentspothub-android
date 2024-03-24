package com.acr.rentspothub.role;

import com.acr.rentspothub.define.Constants;

public class Config {
    private int id;
    private String firebaseToken;
    private String account;
    private String password;
    private String notificationId;
    private String userId;

    public Config(){
        id = 0;
        firebaseToken = Constants.EMPTY_STRING;
        account = Constants.EMPTY_STRING;
        password = Constants.EMPTY_STRING;
        notificationId = Constants.EMPTY_STRING;
        userId = Constants.EMPTY_STRING;
    }

    public Config(int id,String firebaseToken,String account,String password,String notificationId,String userId){
        this.id = id;
        this.firebaseToken = firebaseToken;
        this.account = account;
        this.password = password;
        this.notificationId = notificationId;
        this.userId = userId;
    }

    public Config(String firebaseToken,String account,String password,String notificationId,String userId){
        this.id = 0;
        this.firebaseToken = firebaseToken;
        this.account = account;
        this.password = password;
        this.notificationId = notificationId;
        this.userId = userId;
    }

    public void setAttribute(int type, String attribute) {
        switch (type) {
            case 0:
                this.id = Integer.parseInt(attribute);
                break;
            case 1:
                this.firebaseToken = attribute;
                break;
            case 2:
                this.notificationId = attribute;
                break;
            case 3:
                this.account = attribute;
                break;
            case 4:
                this.password = attribute;
                break;
            case 5:
                this.userId = attribute;
                break;
            default:
        }
    }

    public int getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getUserId(){
        return userId;
    }

}
