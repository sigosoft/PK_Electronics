package com.example.pooja.pk_electronics.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pooja.pk_electronics.R;
import com.example.pooja.pk_electronics.database.DBHelper;
import com.example.pooja.pk_electronics.model.DashboardModel;
import com.example.pooja.pk_electronics.service.SendRequestServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    String currentDateandTime;
    String username;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        textView = (TextView) findViewById(R.id.textVieNet);

        DBHelper bn = new DBHelper(SplashActivity.this);
        final String datefromdb = bn.getDate();
        //  final String datefromdb = "2017-05-21";
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (isOnline()) {
                    SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                    username = preferences2.getString("username", "0");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    currentDateandTime = sdf.format(new Date());
                    if (!currentDateandTime.equals(datefromdb)) {
                        getServerData();
                    } else {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                        String value = preferences.getString("login", "0");

                        if (value.equals("1")) {

                            DBHelper bn1 = new DBHelper(SplashActivity.this);
                            bn1.insertDate(username, currentDateandTime);
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivity(intent);


                        } else if (value.equals("0") || value == null) {
                            DBHelper bn2 = new DBHelper(SplashActivity.this);
                            bn2.insertDate("user", currentDateandTime);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);

                        } else {
                            String d = value;
                            String dx = value;
                        }
                    }
                } else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("No Network");
                }
            }
        }, SPLASH_TIME_OUT);
    }

    public void getServerData() {
        DBHelper bn = new DBHelper(SplashActivity.this);
        bn.clearOrders();
        new DataFromServer().execute();
    }

    private class DataFromServer extends AsyncTask<Void, Void, Void> {
        HashMap<String, String> nameValuePairs;
        String responsefromserver = null;
        JSONArray jArray;

        @Override
        protected Void doInBackground(Void... voids) {
            nameValuePairs = new HashMap<String, String>();
            SendRequestServer req = new SendRequestServer();
            String url1 = "select_product.php";
            responsefromserver = req.requestSender(url1, nameValuePairs, SplashActivity.this);
            try {
                jArray = new JSONArray(responsefromserver);
                JSONObject json_data = null;
                for (int i = 0; i < jArray.length(); i++) {
                    json_data = jArray.getJSONObject(i);
                    String product_name = json_data.getString("product_name");
                    int id = json_data.getInt("id");
                    String price1 = json_data.getString("price1");
                    String price2 = json_data.getString("price2");
                    String price3 = json_data.getString("price3");
                    String updated_on = json_data.getString("updated_on");

                    DBHelper bn = new DBHelper(SplashActivity.this);
                    DashboardModel dashboardModel = new DashboardModel(id, product_name, price1, price2, price3, updated_on);
                    bn.insert(dashboardModel);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
                    String value = preferences.getString("login", "0");
                    if (value.equals("1")) {

                        DBHelper bn1 = new DBHelper(SplashActivity.this);
                        bn1.insertDate(username, currentDateandTime);
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);


                    } else if (value.equals("0") || value == null) {
                        DBHelper bn2 = new DBHelper(SplashActivity.this);
                        bn2.insertDate("user", currentDateandTime);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);

                    } else {
                        String d = value;
                        String dx = value;
                    }

                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPreExecute() {

        }

        protected void onPostExecute(Void unused) {

        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
