package de.httptandooripalace.restaurantorderprinter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import entities.Bill;
import entities.Product;
import entities.Settings;
import helpers.MainAdapter;
import helpers.PrintAdapter;
import helpers.RequestClient;
import helpers.SharedPrefHelper;

import static android.R.attr.x;
import static android.R.attr.y;
import static android.view.View.Y;
import static de.httptandooripalace.restaurantorderprinter.R.layout.main_activity;
import static de.httptandooripalace.restaurantorderprinter.R.string.bill;
import static de.httptandooripalace.restaurantorderprinter.R.string.bill_settings;
import static de.httptandooripalace.restaurantorderprinter.R.string.settings;

public class MainActivity extends AppCompatActivity {
    private Toast currentToast;
    private Context context;
    private ExpandableListView view;
    LinkedHashMap<String, List<Product>> prodlist2 = new LinkedHashMap<>();
    int bill_nr = 0;
    String table_nr = "#";
    private entities.Settings settings;
    Bill b = null;
    Intent intent;
    List<Bill> open_bills = OverviewActivity.bills;

    @Override
    protected void onResume() {

        final Bundle extras = getIntent().getExtras();
        String id_edit;

        if (extras != null) {
            bill_nr = intent.getIntExtra("bill_nr",1);
            table_nr = intent.getStringExtra("tableNr");
            TextView lblTable_number = (TextView) findViewById(R.id.table_number);
            if(table_nr != null) {
                System.out.println("TABLE "+table_nr + "");
                lblTable_number.setHint(table_nr);
            }else{
                lblTable_number.setHint("#");
            }
            System.out.println("EDITING BILL NR : " + bill_nr);
            // and get whatever type user account id is
            // and get whatever type user account id is
        }else{
            System.out.println("NO EDITING BILL NR");
        }

        super.onResume();

        String apiData = SharedPrefHelper.getString(getApplicationContext(), "apiData");
        if(apiData == null || apiData.equals("")) return;

        if(apiData.equals("ERROR")) { // No internet or other connection problem with db
            // Todo: This code is so ugly, replace asap
            ArrayList<String> err = new ArrayList<>();
            err.add("Error");
            LinkedHashMap<String, List<Product>> msg = new LinkedHashMap<>();
            List<Product> durrr = new ArrayList<Product>();
            durrr.add(new Product(-1, getString(R.string.could_not_get_db_info), 0, 0, null, null, 1));
            msg.put("Error", durrr);
            view = (ExpandableListView) findViewById(R.id.overview_main);
            MainAdapter adapter = new MainAdapter(this, err, msg);
            view.setAdapter(adapter);

        }
        else {
            JSONArray data = new JSONArray();

            try {
                data = new JSONArray(apiData); // Array of JSONObjects from API
            } catch(JSONException ex) {
                Log.d("JSONEX", ex.getMessage());
            }

            // Convert json data to arrayList to pass it to gridView
            final ArrayList<String> catlist = new ArrayList<>();
            final LinkedHashMap<String, List<Product>> prodlist = new LinkedHashMap<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = new JSONObject();

                try {
                    obj = data.getJSONObject(i);
                } catch (JSONException ex) {
                    Log.d("JSONEX", ex.getMessage());
                }

                String catname = "";
                String cat_id_json;
                int cat_id =0;
                int selected_cat_id = CategoryActivity.group;
                try {
                    catname = obj.getString("name_cat");
                    cat_id_json = obj.getString("id_group");
                    cat_id = Integer.parseInt(cat_id_json);
                } catch (JSONException ex) {
                    Log.d("JSONEX", ex.getMessage());
                }
                //12.06 added Sergejs
                if (cat_id == selected_cat_id) {
                    //TextView t = (TextView) findViewById(R.id.textView_01);
                    //t.setText(cat_id);
                    // Actual list view data
                    if (!catlist.contains(catname)) {
                        catlist.add(catname);
                    }

                    List<Product> prods = prodlist.get(catname);
                    if (prods == null) {
                        prods = new ArrayList<>();
                    }

                    try {
                        prods.add(new Product(
                                Integer.parseInt(obj.getString("id_prod")),
                                obj.getString("name_prod"),
                                Float.parseFloat(obj.getString("price_prod_excl")),
                                Float.parseFloat(obj.getString("price_prod_incl")),
                                stripCommaAtEnd(obj.getString("reference_prod")),
                                catname,
                                1
                        ));
                    } catch (JSONException ex) {
                        Log.d("JSONEX", "Couldn't get product: " + ex.getMessage());
                    }

                    prodlist.put(catname, prods);
                }
            }
            // Get the grid view and bind array adapter
            view = (ExpandableListView) findViewById(R.id.overview_main);
            final MainAdapter adapter = new MainAdapter(this, catlist, prodlist);
            view.setAdapter(adapter);

            prodlist2 = prodlist;

            // Listview on child click listener
            view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // Fetch properties of tapped item
                    String cat = catlist.get(groupPosition);
                    Product prod = prodlist2.get(catlist.get(groupPosition)).get(childPosition); // Error here !! prodlist2 is trying to get the index of the new adapter data but it doesn't correspond to his data

                    int id2 = prod.getId();
//                List<Product> products = SharedPrefHelper.getPrintItems(getApplicationContext());
//                    if(products == null) products = new ArrayList<>();
//                    // If item is already in the list, just increase the count
//                                   if (products.contains(prod)) {
//                                       // Todo: check if this is bugging the main refresh count
//                                       products.remove(prod);
//                                       prod.increaseCount();
//                                       products.add(prod);
//                                   }
//                    // Otherwise add the product to print overview list
//                                   else {
//                                       products.add(prod);
//                                   }

                    try {
                        StringEntity entity;

                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("bill_id", bill_nr);
                        jsonParams.put("product_id", id2);
                        entity = new StringEntity(jsonParams.toString());
                        RequestClient.put(getApplicationContext(), "bills/product/", entity, "application/json", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                // If the response is JSONObject instead of expected JSONArray
                                try {
                                    System.out.println("Adding product to the bill : ");
                                    Log.d("RESPONSE", response.toString());
                                }
                                catch(Exception e) {
                                    System.out.println("Adding product to the bill : ");
                                    Log.d("Exception HTTP", e.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                try {
                                    Log.d("RESPONSE", errorResponse.toString());
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

                    //TODO : add the prod.getPrice() to the total bill price

                    try {
                        if(extras!=null){
                            for (int y =0; y<=open_bills.size();y++){
                                if(open_bills.get(y).getId()==bill_nr){
                                    b.setTotal_price_excl(open_bills.get(y).getTotal_price_excl());
                                    open_bills.get(y).setTotal_price_excl(open_bills.get(y).getTotal_price_excl()+prod.getPrice_excl());
                                    OverviewActivity.bills=open_bills;
                                    break;
                                }
                            }
                        }

                        b.setTotal_price_excl(b.getTotal_price_excl()+prod.getPrice_excl());
                        StringEntity entity;

                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("bill_id", bill_nr);
                        jsonParams.put("total_price_excl", b.getTotal_price_excl());
                        entity = new StringEntity(jsonParams.toString());
                        RequestClient.put(getApplicationContext(), "bills/price/", entity, "application/json", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                // If the response is JSONObject instead of expected JSONArray
                                try {
                                    System.out.println("Updating price on the bill : ");
                                    Log.d("RESPONSE", response.toString());
                                }
                                catch(Exception e) {
                                    System.out.println("Updating price on the bill : ");
                                    Log.d("Exception HTTP", e.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                try {
                                    Log.d("RESPONSE", errorResponse.toString());
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


                    // SharedPrefHelper.setPrintItems(getApplicationContext(),products);

                    if (currentToast != null) currentToast.cancel();
                    currentToast = Toast.makeText(getApplicationContext(), getString(R.string.added_products) + " " + prod.getName() + " taille de la liste : " + adapter.getChildrenCount(0),
                            Toast.LENGTH_SHORT);
                    currentToast.show();

                    return true;
                }
            });

            /* Text change listener to filter on reference number
            EditText refnr = (EditText) findViewById(R.id.ref_nr);
            refnr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String text = s.toString();
                    Log.d("TEXT CHANGED", "onTextChanged: " + text);
                    view.expandGroup(0);
                    //view.destroyDrawingCache();
                    view.invalidateViews();

                    List<Product> filteredList = new ArrayList<>();
                    ArrayList<String> catlist2 = new ArrayList<>();

                    if(!text.equals(""))
                    {
                        String searchres = "Search result";
                        if(!catlist2.contains(searchres)) {
                            catlist2.add(searchres);
                        }
                        Iterator<List<Product>> it = prodlist.values().iterator();
                        while(it.hasNext())
                        {
                            List<Product> prod = it.next();
                            for(int i = 0; i < prod.size(); i++) {
                                if(prod.get(i).getReference().contains(text)){
                                    filteredList.add(prod.get(i));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        prodlist2.put(searchres,filteredList);
                    }
                    adapter.filter(text);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            */
            // Text change listener to filter on reference number
            EditText table = (EditText) findViewById(R.id.table_number);
            table.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String val = s.toString();
                    table_nr = val;
                    b.setTableNr(table_nr);
                    CategoryActivity.o_table=table_nr;
                    try {
                        StringEntity entity;
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("bill_id", bill_nr);
                        jsonParams.put("table_nr", table_nr);
                        entity = new StringEntity(jsonParams.toString());

                        RequestClient.post(context,"bills/addtable/", entity, "application/json", new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                // If the response is JSONObject instead of expected JSONArray
                                try {
                                    System.out.println("Adding table number on the bill");
                                    Log.d("RESPONSE", response.toString());

                                }
                                catch(Exception e) {
                                    System.out.println("Adding table number on the bill");
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

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(main_activity);
        intent = getIntent();
        Bundle extras;
        String id_edit;
        b = new Bill(null, true,null,null,null,0,0);

        settings = SharedPrefHelper.loadSettings(getApplicationContext());

        if(settings == null) {
            settings = new Settings();
            SharedPrefHelper.saveSettings(getApplicationContext(), settings);
        }


        extras = getIntent().getExtras();
        if (extras == null) {
            System.out.println("CREATING A NEW BILL");
            try {
                StringEntity entity;
                JSONObject bill = new JSONObject();
                bill.put("table_nr", table_nr);
                bill.put("total_price_excl", b.getTotal_price_excl());
                entity = new StringEntity(bill.toString());
                RequestClient.put(context, "bills/", entity, "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // If the response is JSONObject instead of expected JSONArray
                        try {
                            Log.d("RESPONSE", response.toString());//RESPONSE: {"id":"3","success":true}
                            bill_nr = Integer.parseInt(response.get("id").toString());

                        } catch (Exception e) {
                            Log.d("Exception HTTP", e.getMessage());
                        }
                        b.setId(bill_nr);
                        CategoryActivity.o_bill=bill_nr;
                        //START OF SECOND QUERRY
                        try {
                            StringEntity entity;
                            JSONObject jsonParams = new JSONObject();
                            Log.d("RESPONSE", "ADDING THE WAITER ON THE BILL");
                            jsonParams.put("bill_id", bill_nr);
                            jsonParams.put("waiter_id", settings.getWaiter_id());
                            entity = new StringEntity(jsonParams.toString());

                            RequestClient.post(context,"bills/addwaiter/", entity, "application/json", new JsonHttpResponseHandler(){
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
                        //END od second querry

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        try {
                            Log.d("RESPONSE", errorResponse.toString());
                        } catch (Exception e) {
                            Log.d("Exception HTTP", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int c, Header[] h, String r, Throwable t) {
                        try {
                            Log.d("RESPONSE", r.toString());
                        } catch (Exception e) {
                            Log.d("Exception HTTP", e.getMessage());
                        }
                    }
                });
            } catch (Exception e) {
                Log.d("Ex", e.getMessage());

            }


        }else{
            //id_edit = extras.getString("bill_nr");
            //bill_nr = Integer.parseInt(id_edit);
            bill_nr = intent.getIntExtra("bill_nr",1);
            table_nr = intent.getStringExtra("tableNr");
            System.out.println("BILL NR :"+ bill_nr);
        }

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
            case android.R.id.home:
                Intent resultIntent = new Intent(this,CategoryActivity.class);
                //ArrayList<String> bill_info = new ArrayList<String>();
                //bill_info.add(Integer.toString(bill_nr));
                //bill_info.add(table_nr);
                resultIntent.putExtra("bill_nr", bill_nr);
                resultIntent.putExtra("tableNr", table_nr);
                setResult(RESULT_OK, resultIntent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshContent() {
        finish();
        startActivity(getIntent());
    }

    public String stripCommaAtEnd(String s) {
        if(s.equals("") || s == null || s.length() < 1) return "";
        if(s.charAt(s.length() - 1) == ',') {
            return s.substring(0, s.length() - 1);
        }
        else return s;
    }

    public void gotoOverview(View view) {

        EditText e = (EditText) findViewById(R.id.table_number);
        String val = e.getText().toString();
        table_nr = val;
        Intent intent = new Intent(this, PrintActivity.class);
        intent.putExtra("bill_nr", bill_nr);
        intent.putExtra("tableNr", table_nr);
        startActivity(intent);



    }

    public void setFocusToTableNumber(View view) {
        EditText e = (EditText) findViewById(R.id.table_number);
        e.requestFocus();
        // Show keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(e, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideSoftKeyboard(View view) {
        // Hide keyboard
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}
