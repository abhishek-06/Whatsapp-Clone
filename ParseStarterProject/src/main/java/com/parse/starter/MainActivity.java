/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

  Boolean loginModeActive = false;

  public void redirectIfLoggedIn(){

    if(ParseUser.getCurrentUser() != null){
      Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
      startActivity(intent);
    }
  }

  public void toggleLoginMode(View view){

    Button loginSignUpButton = findViewById(R.id.loginSignupButton);

    TextView toggleLoginModeTextView = (TextView) findViewById(R.id.toggleLoginModeTextView);

    Log.i("Info","Mode toggled");
    if (loginModeActive){
      loginModeActive = true;
      loginSignUpButton.setText("SignUp");
      toggleLoginModeTextView.setText("Already have an account?  LogIn");

    } else {
      loginModeActive = true;
      loginSignUpButton.setText("Log In");
      toggleLoginModeTextView.setText("Don't Have an account?  SignUp");
    }
  }

  public void signupLogin(View view){

    EditText usernameEditText = findViewById(R.id.usernameEditText);

    EditText passwordEditText = findViewById(R.id.passwordEditText);

    if (loginModeActive){

      ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {

          if (e == null) {
            Log.i("Info","user logged in");
            Toast.makeText(MainActivity.this, "Log In successful", Toast.LENGTH_SHORT).show();
            redirectIfLoggedIn();
          } else {

            String message = e.getMessage();

            if (message.toLowerCase().contains("java")){
              message = e.getMessage().substring(e.getMessage().indexOf(" "));
            }
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
          }
        }
      });

    } else {

      ParseUser user = new ParseUser();

      user.setUsername(usernameEditText.getText().toString());

      user.setPassword(passwordEditText.getText().toString());

      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {

          if (e == null) {

            Log.i("Info", "User SignedUp");
            Toast.makeText(MainActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
            redirectIfLoggedIn();
          } else {
            String message = e.getMessage();

            if (message.toLowerCase().contains("java")){
              message = e.getMessage().substring(e.getMessage().indexOf(" "));
            }
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
          }
        }
      });
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("WhatsApp: Login");

    redirectIfLoggedIn();

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}