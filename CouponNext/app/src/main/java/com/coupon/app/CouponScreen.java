package com.coupon.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coupon.app.session.AppSession;
import com.coupon.app.utils.AppConstants;

import java.util.ArrayList;

public class CouponScreen extends AppCompatActivity {

    private Context context;

    private AppSession appSession;

    private TextView userName;

    private Button logOutButton;

    private ListView itemList;

    private ArrayList<String> items;

    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_screen);
        context = this;
        appSession = AppSession.getAppSessionInstance();
        if (appSession.isUserLoggedIn())
        {
            Log.w("Coupon ACTIVITY", "User is logged in");
            setUserName();
            setLogOutButton();
        }
        setButtonHandler();

    }



    private void setButtonHandler(){
        logOutButton = getLogOutButton();
        logOutButton.setOnClickListener(new ButtonHandler());
    }

    private void setUserName()
    {
        userName = (TextView)findViewById(R.id.userName);
        Log.w("TODO ACTIVITY", "User name is "+appSession.getUsername());

        userName.setText(AppConstants.SALUTATION + appSession.getUsername());
    }

    private void setLogOutButton()
    {
        logOutButton = getLogOutButton();
        logOutButton.setText(AppConstants.LOGOUT);
        logOutButton.setVisibility(View.VISIBLE);
    }

    private void logOut()
    {
        appSession.clearSession();
        resetUserInfo();
    }

    private void resetUserInfo(){
        userName.setText(AppConstants.EMPTY);
        userName.setVisibility(View.INVISIBLE);
        logOutButton.setVisibility(View.INVISIBLE);
    }

    private Button getLogOutButton(){
        return (Button) findViewById(R.id.logOut);
    }

    class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view){
            Intent intent = null;
            logOut();
            intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }
}
