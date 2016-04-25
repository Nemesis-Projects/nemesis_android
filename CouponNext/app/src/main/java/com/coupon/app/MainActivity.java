package com.coupon.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coupon.app.session.AppSession;
import com.coupon.app.utils.AppConstants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private Button loginRegisterButton;

    private EditText nameTextBox;

    private EditText emailTextBox;

    private EditText passwordTextBox;

    private SharedPreferences sharedPreferences;

    private AppSession appSession;

    private Set<String> user_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing all text inputs.
        nameTextBox = (EditText)findViewById(R.id.name);
        emailTextBox = (EditText)findViewById(R.id.email);
        passwordTextBox = (EditText)findViewById(R.id.password);
        loginRegisterButton = (Button)findViewById(R.id.button);
        hideTextOnScreenLoad();
        setButtonTextOnScreenLoad();
        //hideButton(loginRegisterButton);
        context = this;
        setTextWatcher();
        setButtonHandler(loginRegisterButton);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //clearSharedPreferences();
    }

    private void hideButton(Button button)
    {
        button.setVisibility(View.INVISIBLE);
    }

    private void setButtonHandler(Button button)
    {
        button.setOnClickListener(new ButtonHandler());
    }

    private void setTextWatcher()
    {
        nameTextBox.addTextChangedListener(new LoginInfoTextWatcher());
        emailTextBox.addTextChangedListener(new LoginInfoTextWatcher());
        passwordTextBox.addTextChangedListener(new LoginInfoTextWatcher());
    }

    private void checkIfUserExists(String eventType)
    {
        if(null != sharedPreferences){
            if(AppConstants.NEXT_BUTTON.equalsIgnoreCase(eventType))
            {
                String email_id = emailTextBox.getText().toString();
                user_details = sharedPreferences.getStringSet(email_id, null);
                if (user_details == null)
                {
                    enableFieldsForRegister();
                }
                else
                {
                    enableFieldsForLogin();
                }
            }

        }
    }

    private void enableFieldsForRegister()
    {
        nameTextBox.setVisibility(View.VISIBLE);
        passwordTextBox.setVisibility(View.VISIBLE);
        loginRegisterButton.setText(AppConstants.REGISTER_BUTTON);
    }

    private void enableFieldsForLogin()
    {
        loginRegisterButton.setText(AppConstants.LOGIN_BUTTON);
        passwordTextBox.setVisibility(View.VISIBLE);
    }

    private void addUserToLocalDB()
    {
        String username = nameTextBox.getText().toString();
        username = "Username:"+username;
        String email = emailTextBox.getText().toString();
        String password = passwordTextBox.getText().toString();
        password = "Password:"+password;
        if (null != sharedPreferences)
        {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putStringSet(email, new HashSet<String>(Arrays.asList(username, password)));
            edit.apply();
            appSession = AppSession.getAppSessionInstance();
            appSession.setSessionUser(sharedPreferences, username, email);
        }

    }

    /**private void checkIfUserExists(String userInfo){
     if(null != sharedPreferences){
     String user_name = sharedPreferences.getString(userInfo, "NA");
     Log.w("MAIN_ACTIVITY.CHECK", userInfo);
     SharedPreferences.Editor edit = sharedPreferences.edit();
     if (user_name.equalsIgnoreCase("NA"))
     {
     Log.w("MAIN_ACTIVITY.CHECK", "Adding user name to shared preferences"+userInfo);
     edit.putString(userInfo, "");
     edit.apply();
     }
     }

     }**/

    private void hideTextOnScreenLoad(){
        nameTextBox.setVisibility(View.INVISIBLE);
        passwordTextBox.setVisibility(View.INVISIBLE);
        loginRegisterButton.setVisibility(View.INVISIBLE);
    }

    private void setButtonTextOnScreenLoad(){
        loginRegisterButton.setText(AppConstants.NEXT_BUTTON);
    }

    private void navigateUserToNextActivity(Intent intent)
    {
        intent = new Intent(context, CouponScreen.class);
        startActivity(intent);
    }

    private boolean validateUserInfo()
    {
        String password = passwordTextBox.getText().toString();
        String email = emailTextBox.getText().toString();
        if(null != user_details)
        {
            for (String details : user_details)
            {
                if (details.contains(AppConstants.password_key))
                {
                    if (password.equalsIgnoreCase(details.substring(AppConstants.password_key.length(), details.length())))
                    {
                        appSession = AppSession.getAppSessionInstance();
                        appSession.setSessionUser(sharedPreferences, null, email);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void clearSharedPreferences()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view){
            Intent intent = null;
            if (view.getId() == R.id.button){
                Button button = (Button)findViewById(R.id.button);
                if(AppConstants.NEXT_BUTTON.equalsIgnoreCase(button.getText().toString()))
                {
                    checkIfUserExists(AppConstants.NEXT_BUTTON);
                }
                else if(AppConstants.LOGIN_BUTTON.equalsIgnoreCase(button.getText().toString()))
                {
                    if (validateUserInfo())
                    {
                        navigateUserToNextActivity(intent);
                    }
                }
                else if(AppConstants.REGISTER_BUTTON.equalsIgnoreCase(button.getText().toString()))
                {
                    addUserToLocalDB();
                    navigateUserToNextActivity(intent);
                }
            }
        }
    }

    class LoginInfoTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after){

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){

        }

        @Override
        public void afterTextChanged(Editable editable){
            if (null != editable.toString()){
                if (!editable.toString().isEmpty()){
                    loginRegisterButton.setVisibility(View.VISIBLE);
                } else
                {
                    loginRegisterButton.setVisibility(View.INVISIBLE);
                }

            }
        }
    }
}
