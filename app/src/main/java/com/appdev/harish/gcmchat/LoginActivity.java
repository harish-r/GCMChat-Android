package com.appdev.harish.gcmchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    EditText uname, emailid;
    Button login;

    String username, email;

    // Initialize parameters
    public LoginActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Obtaian preferences from SharedPreferences
        preferences = this.getSharedPreferences("GCM_CHAT", Context.MODE_PRIVATE);
        editor = preferences.edit();

        // Assign UI fields
        uname = (EditText) findViewById(R.id.uname);
        emailid = (EditText) findViewById(R.id.emailid);
        login = (Button) findViewById(R.id.login);

        // Setup Login button listener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = uname.getText().toString();
                email = emailid.getText().toString();
                // Send user details + token to server using registerGCM method
                registerGCM();
                // Show contacts page
                Intent contactsIntent = new Intent(LoginActivity.this, ContactsActivity.class);
                startActivity(contactsIntent);
                finish();
            }
        });
    }

    public void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("EMAIL_ID", email);
        intent.putExtra("USERNAME", username);
        startService(intent);
    }

}
