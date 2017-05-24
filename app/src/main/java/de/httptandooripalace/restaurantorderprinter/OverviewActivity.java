package de.httptandooripalace.restaurantorderprinter;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import entities.Bill;
import entities.Product;
import helpers.OverviewAdapter;
import helpers.PrintAdapter;
import helpers.RequestClient;

/**
 * Created by uiz on 26/04/2017.
 */

public class OverviewActivity extends AppCompatActivity {

    private entities.Settings settings;
    public List<Bill> bills = new ArrayList<>();
    private static  List<Product> products = new ArrayList<>();
    Context context;
    int id =0;
    String boolstr = null;
    boolean is_open = true;
    String datestr = null;
    SimpleDateFormat sdf = null;
    Date date = null;
    String table_nr = null;
    JSONArray jsonarray = null;
    JSONObject jsonobject = null;
    Bill b = null;
    OverviewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_activity);
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
        bills.clear();
        context = this;

        try {
            RequestClient.get("bills/getopen/", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    try {
                        Log.d("RESPONSE", response.toString()); // RESPONSE: {"success":"true","bills":[{"id":"1","is_open":"1","date":"2017-05-09 02:59:18","table_nr":"1"}]}
                        jsonarray = response.getJSONArray("bills");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);
                            id = jsonobject.getInt("id");
                            boolstr = jsonobject.getString("is_open");
                            is_open = true;
                            if(boolstr.equals("1")){
                                is_open = true;
                            }else{
                                is_open = false;
                            }
                            datestr = jsonobject.getString("date");
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            date = sdf.parse(datestr);
                            table_nr = jsonobject.getString("table_nr");
                            b = new Bill(null, is_open, date, table_nr, "", id);
                            bills.add(b);
                            Log.d("RESPONSE", "bills ::::" + bills);
                            ListView view = (ListView) findViewById(R.id.list_open_bills);
                            adapter = new OverviewAdapter(context, bills);
                            view.setAdapter(adapter);
                        }
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
        catch(Exception e){
            Log.d("Ex", e.getMessage());
        }
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
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public void close_bill(View view){
        try {
            StringEntity entity;
            JSONObject jsonParams = new JSONObject();
            Log.d("RESPONSE", "trying to close a bill" + view.getTag());
            jsonParams.put("bill_id", view.getTag());//TODO : get the id of the bill corresponding
            System.out.println("TAGGGGGG : "+ view.getTag());//return the good id !!
            jsonParams.put("open", 0);
            entity = new StringEntity(jsonParams.toString());
            RequestClient.post(context,"bills/", entity, "application/json", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    try {
                        Log.d("RESPONSE", response.toString());
                        onResume();
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
    }

    public void delete_bill(View view){
        // TODO : delete this bill of the database ( on its ID)
    }

    public void edit_bill(View view){
        // TODO : open the main Activity and put the id in the bill_id variable
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("id_edit", view.getTag().toString());
        startActivity(i);
    }

}
