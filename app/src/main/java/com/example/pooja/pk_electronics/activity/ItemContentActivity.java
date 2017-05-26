package com.example.pooja.pk_electronics.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pooja.pk_electronics.R;

public class ItemContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_content);
        getSupportActionBar().hide();

        Intent p = getIntent();
        String pname = p.getStringExtra("pname");
        String price1 = p.getStringExtra("price1");
        String price2 = p.getStringExtra("price2");
        String price3 = p.getStringExtra("price3");
        String updatedOn = p.getStringExtra("updatedOn");
        String updatedBy = p.getStringExtra("updatedBy");

        TextView priceTwo = (TextView) findViewById(R.id.priceTwo);
        TextView priceThree = (TextView) findViewById(R.id.priceThree);
        TextView updatedon = (TextView) findViewById(R.id.updatedOn);
        TextView priceOne = (TextView) findViewById(R.id.priceOne);
        TextView pNAME = (TextView) findViewById(R.id.pNAME);
        TextView updatedby = (TextView) findViewById(R.id.updatedBy);

        priceTwo.setText(price2);
        priceThree.setText(price3);
        updatedon.setText(updatedOn);
        priceOne.setText(price1);
        pNAME.setText(pname);
        updatedby.setText(updatedBy);
    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(in);
        finish();
        super.onBackPressed();
    }

    public void backClick(View v) {
        Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(in);
        finish();
    }
}
