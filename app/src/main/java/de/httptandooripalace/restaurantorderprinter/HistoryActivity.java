package de.httptandooripalace.restaurantorderprinter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import entities.Bill;
import entities.Product;
import helpers.HistoryAdapter;
import helpers.OverviewAdapter;
import helpers.RequestClient;

/**
 * Created by uiz on 30/05/2017.
 */

public class HistoryActivity extends AppCompatActivity {

    private entities.Settings settings;
    public List<Bill> bills = new ArrayList<>();
    public  List<Product> products = new ArrayList<>();
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
    String waiter_name = "";
    Double total_price_excl = 0.0;

    HistoryAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
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
            case R.id.bills_history:
                Intent i3 = new Intent(this, HistoryActivity.class);
                startActivity(i3);
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
            RequestClient.get("bills/getclosed/", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    try {
                        Log.d("RESPONSE", response.toString()); // RESPONSE: {"success":"true","bills":[{"id":"1","is_open":"1","date":"2017-05-09 02:59:18","table_nr":"1"}]}
                        jsonarray = response.getJSONArray("bills");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);
                            id = jsonobject.getInt("id_bill");
                            is_open = false;
                            datestr = jsonobject.getString("date_bill");
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            date = sdf.parse(datestr);
                            table_nr = jsonobject.getString("table_nr");
                            waiter_name = jsonobject.getString("waiter_name");
                            total_price_excl = jsonobject.getDouble("total_price_excl");
                            //TODO : récupérer la liste de produits correspondants à cet id_bill dans la bdd


                            Bill b = new Bill(products, is_open, date, table_nr, waiter_name, id, total_price_excl);
                            bills.add(b);

                            Log.d("RESPONSE", "bills ::::" + bills);
                            ListView view = (ListView) findViewById(R.id.list_closed_bills);
                            adapter = new HistoryAdapter(context, bills);
                            view.setAdapter(adapter);

                        }//end of for bills

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void refreshContent() {
        finish();
        startActivity(getIntent());
    }

}
