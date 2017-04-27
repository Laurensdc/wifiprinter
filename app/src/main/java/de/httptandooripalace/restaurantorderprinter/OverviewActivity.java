package de.httptandooripalace.restaurantorderprinter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entities.Bill;
import entities.Product;
import helpers.OverviewAdapter;
import helpers.PrintAdapter;
import helpers.SharedPrefHelper;

/**
 * Created by uiz on 26/04/2017.
 */

public class OverviewActivity extends AppCompatActivity {

    private entities.Settings settings;
    private List<Bill> bills;
    private static List<Product> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_activity);

//        SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
//        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshContent();
//            }
//        });
        //TODO : getOpenBills method
//        bills = SharedPrefHelper.getOpenBills(getApplicationContext());
//        settings = SharedPrefHelper.loadSettings(getApplicationContext());
//
//        // Bind products to print overview
//        ListView view = (ListView) findViewById(R.id.listingLayout);
//        OverviewAdapter adapter = new OverviewAdapter(getApplicationContext(), bills);
//        view.setAdapter(adapter);

        //creating 3 bills manually, to test the layout
        Product p1 = new Product("tandori salad", 10, 13, "15", "salads");
        Product p2 = new Product("other salad", 10, 13, "16", "salads");
        Product p3 = new Product("thing salad", 10, 13, "17", "salads");
        Product p4 = new Product("stuff salad", 10, 13, "18", "salads");
        Product p5 = new Product("that salad", 10, 13, "19", "salads");

        products.add(p1);
        products.add(p2);
        products.add(p3);
        products.add(p4);

        Date d = new Date();//system time

        Bill b1 = new Bill(products, true, d, "5", "Bryan", 1);
        Bill b2 = new Bill(products, true,d , "11", "Paul", 2);
        Bill b3 = new Bill(products, true,d , "3", "Jack", 3);

        bills.add(b1);
        bills.add(b2);
        bills.add(b3);

        ListView view = (ListView) findViewById(R.id.list_open_bills);
        OverviewAdapter adapter = new OverviewAdapter(getApplicationContext(), bills);
        view.setAdapter(adapter);

    }

    // something
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.bills_overview:
                Intent i2 = new Intent(this, OverviewActivity.class);
                startActivity(i2);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void refreshContent() {
        finish();
        startActivity(getIntent());
    }

    public void new_bill(View view){
        //TODO : delete the selection of the precedent bill, to start the new one
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void close_bill(View view){
        //TODO : send this bill to the closed ones
    }

    public void delete_bill(View view){
        // TODO : delete this bill of the database ( on its ID)
    }

    public void edit_bill(View view){
        // TODO : open the selected bill using its ID
    }

}
