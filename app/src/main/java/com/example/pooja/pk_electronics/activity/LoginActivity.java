package com.example.pooja.pk_electronics.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pooja.pk_electronics.R;
import com.example.pooja.pk_electronics.service.SendRequestServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    String ph, pn;
    int pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        final EditText phone = (EditText) findViewById(R.id.phone);
        final EditText pin = (EditText) findViewById(R.id.pin);
        Button loginSubmit = (Button) findViewById(R.id.loginSubmit);
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ph = phone.getText().toString();
                pn = pin.getText().toString();

                new LoginToServer().execute();

            }
        });
    }

    private class LoginToServer extends AsyncTask<Object, Object, Integer> {

        HashMap<String, String> namevaluePairs;
        String responsefromserver = null;
        JSONArray jArray;

        @Override
        protected Integer doInBackground(Object... voids) {
            namevaluePairs = new HashMap<String, String>();
            //   namevaluePairs.put("name", "myName");
            namevaluePairs.put("password", pn);
            namevaluePairs.put("phone", ph);
            namevaluePairs.put("status", "1");
            SendRequestServer req = new SendRequestServer();
            String url1 = "create_login.php";

            responsefromserver = req.requestSender(url1, namevaluePairs, LoginActivity.this);
            try {
                JSONObject json_data = new JSONObject(responsefromserver);
                String result = json_data.getString("success");
                String message = json_data.getString("message");
                String username = json_data.getString("name");
                String mobile = json_data.getString("mobile");

                if (message.equals("successfully")) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", username);
                    editor.putString("mobile", mobile);
                    editor.putString("login", "1");
                    editor.commit();
                    pass = 1;
                    Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(in);
                    finish();
                } else if (message.equals("user incorrect")) {
                    pass = 2;
                } else if (message.equals("phone number not valid")) {
                    pass = 3;
                } else {
                    pass = 4;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return pass;
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(Integer unused) {
            if (unused==1){
                Toast.makeText(LoginActivity.this, "login successfuly", Toast.LENGTH_SHORT).show();
            }else if (unused==2){
                Toast.makeText(LoginActivity.this, "user incorrect", Toast.LENGTH_SHORT).show();
            }
            else if (unused==3){
                Toast.makeText(LoginActivity.this, "phone number not valid", Toast.LENGTH_SHORT).show();
            }
            else if (unused==4){
                Toast.makeText(LoginActivity.this, "server error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
