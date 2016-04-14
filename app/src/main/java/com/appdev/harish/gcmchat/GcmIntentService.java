package com.appdev.harish.gcmchat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by harish on 14/4/16.
 */
public class GcmIntentService extends IntentService {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    String username, email, token = null;
    RequestQueue requestQueue;

    public GcmIntentService() {
        super(GcmIntentService.class.getSimpleName());
    }

    public void onHandleIntent(Intent intent) {
        requestQueue = Volley.newRequestQueue(this);

        preferences = this.getSharedPreferences("GCM_CHAT", Context.MODE_PRIVATE);
        editor = preferences.edit();

        this.username = intent.getStringExtra("USERNAME");
        this.email = intent.getStringExtra("EMAIL_ID");

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(Constants.senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.e("token", "GCM Registration Token: " + token);

            // sending the registration id to our server
            sendRegistrationToServer();

            // Store and save user details in SharedPreferences
            editor.putString("GCM_TOKEN", token);
            editor.putString("USERNAME", username);
            editor.putString("EMAIL_ID", email);
            editor.putBoolean("SENT_TOKEN_TO_SERVER", true);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRegistrationToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2:3000/users/create", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("xxx", "response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("xxx", "error: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("gcm_token", token);
                params.put("username", username);
                params.put("email", email);
                Log.e("xxx", "params: " + params.toString());
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
