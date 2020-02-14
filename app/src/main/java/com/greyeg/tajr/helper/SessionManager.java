package com.greyeg.tajr.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.greyeg.tajr.models.UserResponse;

public class SessionManager  {

    private static SessionManager sessionManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String LOG_IN_FROM = "log_in_from";
    public static final String USER_NAME = "username";
    public static final String USER_TYPE = "user_type";
    public static final String USER_ID = "user_id";
    public static final String IS_TAJR = "is_tajr";
    public static final String PARENT_TAJR_ID = "parent_tajr_id";
    public static final String TOKEN = "token";
    public static final String IS_LOGIN = "is_login";
    public static final String PARENT_ID = "PARENT_ID";
    public static final String REMEMBER_PASS = "REMEMBER_PASS";
    public static final String REMEMBERED_PASS = "REMEMBERED_PASS";
    public static final String REMEMBERED_EMAIL = "REMEMBERED_EMAIL";
    public static final String EMPLOYEE = "employee";

    public static SessionManager getInstance(Context context) {
        return sessionManager == null ? sessionManager = new SessionManager(context) : sessionManager;
    }

    @SuppressLint("CommitPrefEdits")
    private SessionManager(Context context){
        sharedPreferences=context.getSharedPreferences("Cache",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

    }

    public void saveSession(UserResponse userResponse,String is_login,String login_from){
        editor.putString(TOKEN,userResponse.getToken());
        editor.putString(LOG_IN_FROM,login_from);
        if (userResponse.getClients()!=null&&!userResponse.getClients().isEmpty())
            editor.putString(PARENT_ID,userResponse.getClients().get(0).getId());
        else
            editor.putString(PARENT_ID,null);

        editor.putString(IS_LOGIN,is_login);
        editor.putString(USER_NAME,userResponse.getData().getLogin_data().getUsername());
        editor.putString(USER_ID,userResponse.getData().getLogin_data().getUser_id());
        editor.putString(USER_TYPE,userResponse.getData().getLogin_data().getUser_type());
        editor.putString(PARENT_TAJR_ID,userResponse.getData().getLogin_data().getParent_tajr_id());
        editor.putString(IS_TAJR,userResponse.getData().getLogin_data().getIs_tajr());
        editor.commit();
    }

    public void SaveEmailAndPassword(String email,String pass){
        editor.putString(REMEMBERED_EMAIL,email);
        editor.putString(REMEMBERED_PASS,pass);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString(REMEMBERED_EMAIL,"");
    }
    public String getPass(){
        return sharedPreferences.getString(REMEMBERED_PASS,"");
    }


    public void setIsRemembered(boolean isRemembered){
        editor.putBoolean(REMEMBER_PASS,isRemembered);
    }
    public boolean isRemembered(){
        return sharedPreferences.getBoolean(REMEMBER_PASS,false);
    }



}
