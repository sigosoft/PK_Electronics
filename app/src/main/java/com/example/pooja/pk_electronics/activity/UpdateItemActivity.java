package com.example.pooja.pk_electronics.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pooja.pk_electronics.R;
import com.example.pooja.pk_electronics.database.DBHelper;
import com.example.pooja.pk_electronics.service.SendRequestServer;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UpdateItemActivity extends AppCompatActivity {
    EditText pname, price1, price2, price3;
    Button submit;
    String productName, PriceOne, PriceTwo, PriceThree, myId, myprice1, myprice2, myprice3,mypnanme, myupdatedOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        getSupportActionBar().hide();
        Intent p = getIntent();
        myId = p.getStringExtra("idSend");
        mypnanme = p.getStringExtra("pnanme");

        myprice1 = p.getStringExtra("price1");
        myprice2 = p.getStringExtra("price2");
        myprice3 = p.getStringExtra("price3");
        myupdatedOn = p.getStringExtra("updatedOn");


        pname = (EditText) findViewById(R.id.product1);
        price1 = (EditText) findViewById(R.id.price11);
        price2 = (EditText) findViewById(R.id.price21);
        price3 = (EditText) findViewById(R.id.price31);
        submit = (Button) findViewById(R.id.submitProduct1);

        pname.setText(mypnanme);
        price1.setText(myprice1);
        price2.setText(myprice2);
        price3.setText(myprice3);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productName = pname.getText().toString();
                PriceOne = price1.getText().toString();
                PriceTwo = price2.getText().toString();
                PriceThree = price3.getText().toString();
                if (!productName.equals("") && !PriceOne.equals("") && !PriceTwo.equals("") && !PriceThree.equals("")) {
                    new DataUpdateToServer().execute();
                } else {
                    Toast.makeText(UpdateItemActivity.this, "Fill all the field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class DataUpdateToServer extends AsyncTask<Void, Void, Void> {

        String responsefromserver = null;
        JSONArray jArray;
        HashMap<String, String> namevaluePairs = new HashMap<String, String>();

        @Override
        protected Void doInBackground(Void... voids) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            namevaluePairs = new HashMap<String, String>();
            namevaluePairs.put("id", myId);
            namevaluePairs.put("product_name", productName);
            namevaluePairs.put("price1", PriceOne);
            namevaluePairs.put("price2", PriceTwo);
            namevaluePairs.put("price3", PriceThree);
            namevaluePairs.put("updated_on", currentDateandTime);
//TODO current date.
            SendRequestServer req = new SendRequestServer();
            String url1 = "update_product.php";
            responsefromserver = req.requestSender(url1, namevaluePairs, UpdateItemActivity.this);



            return null;
        }

        protected void onPreExecute() {

        }

        protected void onPostExecute(Void unused) {
            String LastInsertedID = responsefromserver;
            if (responsefromserver.equals("")) {
                Toast.makeText(getApplicationContext(), "server error", Toast.LENGTH_LONG).show();

            } else {
                DBHelper bn = new DBHelper(UpdateItemActivity.this);
                bn.updateSingeOrders(Integer.parseInt(myId),productName,PriceOne,PriceTwo,PriceThree);

                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(in);
                finish();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(in);
        finish();
        super.onBackPressed();
    }

    public void backClick1(View v) {
        Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(in);
        finish();
    }
}
