package de.httptandooripalace.restaurantorderprinter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import entities.Product;
import helpers.MainAdapter;
import helpers.RequestClient;
import helpers.SharedPrefHelper;

public class MainActivity extends AppCompatActivity {
    private Toast currentToast;
    private Context context;
    private ExpandableListView view;

    @Override
    protected void onResume() {
        super.onResume();

        String apiData = SharedPrefHelper.getString(getApplicationContext(), "apiData");
        if(apiData == null || apiData.equals("")) return;

        if(apiData.equals("ERROR")) { // No internet or other connection problem with db
            // Todo: This code is so ugly, replace asap
            ArrayList<String> err = new ArrayList<>();
            err.add("Error");
            LinkedHashMap<String, List<Product>> msg = new LinkedHashMap<>();
            List<Product> durrr = new ArrayList<Product>();
            durrr.add(new Product(-1, getString(R.string.could_not_get_db_info), 0, 0, null, null));
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
            LinkedHashMap<String, List<Product>> prodlist = new LinkedHashMap<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = new JSONObject();

                try {
                    obj = data.getJSONObject(i);
                } catch(JSONException ex) {
                    Log.d("JSONEX", ex.getMessage());
                }

                String catname = "";

                try {
                    catname = obj.getString("name_cat");
                } catch(JSONException ex) {
                    Log.d("JSONEX", ex.getMessage());
                }

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
                            catname
                    ));
                } catch(JSONException ex) {
                    Log.d("JSONEX", "Couldn't get product: " + ex.getMessage());
                }

                prodlist.put(catname, prods);

            }

            // Get the grid view and bind array adapter
            view = (ExpandableListView) findViewById(R.id.overview_main);
            final MainAdapter adapter = new MainAdapter(this, catlist, prodlist);
            view.setAdapter(adapter);

            final HashMap<String, List<Product>> prodlist2 = prodlist;


            // Listview on child click listener
            view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                // Fetch properties of tapped item
                String cat = catlist.get(groupPosition);
                Product prod = prodlist2.get(catlist.get(groupPosition)).get(childPosition);

                int id2 = prod.getId();

                try {
                    StringEntity entity;

                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("bill_id", "1");
                    jsonParams.put("product_id", id2);
                    entity = new StringEntity(jsonParams.toString());

                    RequestClient.put(getApplicationContext(), "bills/product/", entity, "application/json", new JsonHttpResponseHandler() {
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
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            try {
                                Log.d("RESPONSE", errorResponse.toString());
                            }
                            catch(Exception e) {
                                Log.d("Exception HTTP", e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
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

                if (currentToast != null) currentToast.cancel();
                currentToast = Toast.makeText(getApplicationContext(), getString(R.string.added_products) + " " + prod.getName(),
                        Toast.LENGTH_SHORT);
                currentToast.show();

                return true;
                }
            });

            // Text change listener to filter on reference number
            EditText refnr = (EditText) findViewById(R.id.ref_nr);
            refnr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String text = s.toString();
                    Log.d("TEXT CHANGED", "onTextChanged: " + text);

                    adapter.filter(text);
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
        setContentView(R.layout.main_activity);



//        SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
//        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshContent();
//            }
//        });

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
        // Going to overview without products added
//        if(SharedPrefHelper.getPrintItems(getApplicationContext()) == null) { // No Products added
//            Toast.makeText(getApplicationContext(), "Please add products to bill",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }

        EditText e = (EditText) findViewById(R.id.table_number);
        String val = e.getText().toString();

        SharedPrefHelper.putString(getApplicationContext(), "tableNr", val);
        Intent intent = new Intent(this, PrintActivity.class);
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
