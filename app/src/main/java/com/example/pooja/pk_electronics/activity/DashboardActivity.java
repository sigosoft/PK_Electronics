package com.example.pooja.pk_electronics.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pooja.pk_electronics.R;
import com.example.pooja.pk_electronics.adapter.DashboardCustomAdapter;
import com.example.pooja.pk_electronics.database.DBHelper;
import com.example.pooja.pk_electronics.model.DashboardModel;
import com.example.pooja.pk_electronics.service.SendRequestServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
    ArrayList<DashboardModel> arrayList;
    ListView listView;
    DashboardCustomAdapter adapter;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();
        ImageButton search = (ImageButton) findViewById(R.id.search);
        listView = (ListView) findViewById(R.id.listview);
        imageView2 = (ImageView) findViewById(R.id.imageView2);

        final ImageView imageButtonAdd = (ImageView) findViewById(R.id.imageButtonAdd);
        final EditText searchED = (EditText) findViewById(R.id.searchED);
        ImageButton sync = (ImageButton) findViewById(R.id.sync);
        arrayList = new ArrayList<>();
        DBHelper bn = new DBHelper(DashboardActivity.this);
        arrayList = bn.getOrders();
        adapter = new DashboardCustomAdapter(DashboardActivity.this, arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //getServerData();
        //     registerForContextMenu(listView);
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), AddItemActivity.class);
                startActivity(in);

            }
        });
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    getServerData();
                    Toast.makeText(DashboardActivity.this, "Syncing..", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DashboardActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchED.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
            }
        });
        searchED.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = searchED.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }

    public void getServerData() {
        DBHelper bn = new DBHelper(DashboardActivity.this);
        bn.clearOrders();
        new DataFromServer().execute();
        adapter.notifyDataSetChanged();
    }

    private class DataFromServer extends AsyncTask<Void, Void, Void> {
        HashMap<String, String> nameValuePairs;
        String responsefromserver = null;
        JSONArray jArray;

        @Override
        protected Void doInBackground(Void... voids) {
            nameValuePairs = new HashMap<String, String>();

            SendRequestServer req = new SendRequestServer();
            //selecting the outlets from outlet table
            String url1 = "select_product.php";

            responsefromserver = req.requestSender(url1, nameValuePairs, DashboardActivity.this);
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

                    DBHelper bn = new DBHelper(DashboardActivity.this);
                    DashboardModel dashboardModel = new DashboardModel(id, product_name, price1, price2, price3, updated_on);
                    bn.insert(dashboardModel);


                    //  adapter.notifyDataSetChanged();
                    Intent in = new Intent(DashboardActivity.this, DashboardActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //  finish();
                    startActivity(in);
                    //       overridePendingTransition(0, 0);
                    //  arrayList.add(dashboardModel);

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
