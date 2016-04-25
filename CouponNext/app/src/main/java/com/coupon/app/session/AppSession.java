package com.coupon.app.session;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by shivendrasrivastava on 4/24/16.
 */
public class AppSession {

    private final String SESSION_USER = "session_user";

    private String username;

    private String email;

    private String username_key = "Username:";

    private String password_key = "Password:";

    private SharedPreferences sharedPreferences;

    private static AppSession appSession = null;

    public static AppSession getAppSessionInstance(){
        if (null == appSession )
        {
            appSession = new AppSession();
        }
        return appSession;
    }

    private AppSession()
    {

    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Boolean isUserLoggedIn()
    {
        if (null != getUsername() && null != getEmail())
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setSessionUser(SharedPreferences sharedPreferences, String username, String email)
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Log.w("APP SESSION", "User name being set is "+username);
        Log.w("APP SESSION", "Email being set is "+email);
        if (null == username)
        {
            Set<String> userdetails = sharedPreferences.getStringSet(email, null);
            for (String details : userdetails)
            {
                if (details.contains(username_key))
                {
                    username = details.substring(username_key.length(), details.length());
                }
            }
        }
        setUsername(username);
        setEmail(email);
        setSharedPreferences(sharedPreferences);

        edit.putStringSet(SESSION_USER, new HashSet(Arrays.asList(username, email)));
        edit.apply();
    }

    public void clearSession()
    {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(SESSION_USER);
        edit.apply();
    }

}
