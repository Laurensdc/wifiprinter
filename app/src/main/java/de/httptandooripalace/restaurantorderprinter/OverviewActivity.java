package de.httptandooripalace.restaurantorderprinter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import entities.Bill;
import entities.Product;
import helpers.OverviewAdapter;
import helpers.RequestClient;

/**
 * Created by uiz on 26/04/2017.
 */

public class OverviewActivity extends AppCompatActivity {

    private entities.Settings settings;
    private static  List<Bill> bills = new ArrayList<>();;
    private static  List<Product> products = new ArrayList<>();;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_activity);

        try {

            RequestClient.get("bills/getopen/", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    try {
                        Log.d("RESPONSE", response.toString()); // RESPONSE: {"success":"true","bills":[{"id":"1","is_open":"1","date":"2017-05-09 02:59:18","table_nr":"1"}]}
                        //TODO : inserts those data into bills arraylist
                    }
                    catch(Exception e) {
                        Log.d("Exception HTTP", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int c, Header[] h, String r, Throwable t) {
                    try {
                        Log.d("RESPONSE", r.toString());
                    }
                    catch(Exception e) {
                        Log.d("Exception HTTP", e.getMessage());
                    }
                }
            });
        }
        catch(Exception e) {
            Log.d("Ex", e.getMessage());

        }

//        //creating 3 bills manually, to test the layout
//        System.out.print("start of the manual phase !!!!!!!!!!!!!!!!!!!!!");
//        Product p1 = new Product(0, "tandori salad", 10, 13, "15", "salads");
//        Product p2 = new Product(0, "other salad", 10, 13, "16", "salads");
//        Product p3 = new Product(0, "thing salad", 10, 13, "17", "salads");
//        Product p4 = new Product(0, "stuff salad", 10, 13, "18", "salads");
//        Product p5 = new Product(0, "that salad", 10, 13, "19", "salads");
//
//        products.add(p1);
//        products.add(p2);
//        products.add(p3);
//        products.add(p4);
//
//        Date d = new Date();//system time
//
//        Bill b1 = new Bill(products, true, d, "5", "Bryan", 1);
//        Bill b2 = new Bill(products, true,d , "11", "Paul", 2);
//        Bill b3 = new Bill(products, true,d , "3", "Jack", 3);
//
//        bills.add(b1);
//        bills.add(b2);
//        bills.add(b3);

        ListView view = (ListView) findViewById(R.id.list_open_bills);
        OverviewAdapter adapter = new OverviewAdapter(this, bills);
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

//        RequestParams params = new RequestParams();
//        params.put("key", "value");
//        params.put("more", "data");

        RequestClient.get("products/get", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                try {
                    Log.d("RESPONSE", response.toString());
                }
                catch(Exception e) {
                    Log.d("Exception HTTP", e.getMessage());
                }
            }

        });

        // TODO : open the selected bill using its ID
    }

}
