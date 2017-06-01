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

    OverviewAdapter adapter = null;

    /*StringEntity entity;
    JSONObject jsonParams = null;
    RequestParams params = null;
    JSONArray jsonarray2 = null;
    JSONObject jsonobject2 = null;
    String name = null;
    int id2 = 0;
    double price_excl = 0;
    double price_incl = 0;
    String reference = null;
    String category = null;
    Product p = null;
    Bill b = null;*/

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
        /*p = new Product(id2, name, price_excl, price_incl, reference, category);
        b = new Bill(products, is_open, date, table_nr, "", id);*/
        try {
            RequestClient.get("bills/getopen/", new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    System.out.println("GETTING OPEN BILLS");
                    try {
                        Log.d("RESPONSE", response.toString()); // RESPONSE: {"success":"true","bills":[{"id":"1","is_open":"1","date":"2017-05-09 02:59:18","table_nr":"1"}]}
                        jsonarray = response.getJSONArray("bills");
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);
                            id = jsonobject.getInt("id_bill");
                            is_open = true;
                            datestr = jsonobject.getString("date_bill");
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            date = sdf.parse(datestr);
                            table_nr = jsonobject.getString("table_nr");
                            waiter_name = jsonobject.getString("waiter_name");
                            total_price_excl = jsonobject.getDouble("total_price_excl");
                            //TODO : récupérer la liste de produits correspondants à cet id_bill dans la bdd
                           /* products.clear();
                            try {
                                jsonParams = new JSONObject();
                                Log.d("RESPONSE", response.toString());
                                params = new RequestParams();
                                jsonParams.put("bill_id", i);
                                entity = new StringEntity(jsonParams.toString());
                                RequestClient.post(context,"products/getforbill/", entity, "application/json", new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                // If the response is JSONObject instead of expected JSONArray
                                                try {
                                                    Log.d("RESPONSE", response.getJSONArray("products").toString()); // RESPONSE: {"success":"true","products":[{"id_cat":"18","name_cat":" Dienstag","id_prod":"371","name_prod":"Chicken Curry","reference_prod":"512,","price_prod_excl":"4.03","price_prod_incl":"4.32","description_prod":"","bill_id":"1"},
                                                    jsonarray2 = response.getJSONArray("products");
                                                    for (int j = 0; j < jsonarray2.length(); j++) {
                                                        jsonobject2 = jsonarray2.getJSONObject(j);
                                                        name = jsonobject2.getString("name_prod");
                                                        id2 = jsonobject2.getInt("id_prod");
                                                        price_excl = jsonobject2.getDouble("price_prod_incl");
                                                        price_incl = jsonobject2.getDouble("price_prod_excl");
                                                        reference = jsonobject2.getString("reference_prod");
                                                        category = jsonobject2.getString("name_cat");
                                                        //p = new Product(id2, name, price_excl, price_incl, reference, category);
                                                        p.setCategory(category);
                                                        p.setReference(reference);
                                                        p.setPrice_incl(price_incl);
                                                        p.setPrice_excl(price_excl);
                                                        p.setName(name);
                                                        p.setId(id2);
                                                        products.add(p);
                                                        System.out.println(" products list dans FORRRR : "+ products);



                                                    }


                                                } catch (Exception e) {

                                                    System.out.println("Error is in the FOORRR ");
                                                    Log.d("Exception HTTP", e.getMessage());

                                                }
                                                System.out.println(" products list APRES le for : "+ products);

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

                            }*/


                            //Fin de la requete getforbill
                            /*b.setProducts(products);
                            b.setOpen(is_open);
                            b.setDate(date);
                            b.setTableNr(table_nr);
                            b.setWaiter("");
                            b.setId(id);*/

                            Bill b = new Bill(products, is_open, date, table_nr, waiter_name, id, total_price_excl);
                            bills.add(b);

                            ListView view = (ListView) findViewById(R.id.list_open_bills);
                            adapter = new OverviewAdapter(context, bills);
                            view.setAdapter(adapter);

                        }//end of for bills
                        Log.d("RESPONSE", "bills :" + bills);
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
            Log.d("RESPONSE", "CLOSING BILL " + view.getTag(R.string.id_tag));
            jsonParams.put("bill_id", view.getTag(R.string.id_tag));//TODO : get the id of the bill corresponding
            System.out.println("TAGGGGGG : "+ view.getTag(R.string.id_tag));//return the good id !!
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

    public void edit_bill(View view){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("id_edit", view.getTag(R.string.id_tag).toString());
        i.putExtra("table_nr_edit", view.getTag(R.string.table_tag).toString());
        startActivity(i);
    }

    public void print_bill(View view){
        //TODO
        Intent i = new Intent(this, PrintActivity.class);
        i.putExtra("bill_nr", Integer.parseInt(view.getTag(R.string.id_tag).toString()));
        startActivity(i);
    }

}
