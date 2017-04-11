package de.httptandooripalace.restaurantorderprinter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entities.Product;
import entities.Settings;
import helpers.HttpHandler;
import helpers.MainAdapter;
import helpers.SharedPrefHelper;

import static android.R.attr.data;
import static android.R.attr.x;


public class MainActivity extends AppCompatActivity {
    private Toast currentToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        try {
            // Todo: Save function in configuration
            new HttpHandler(this.getApplicationContext()).execute("http://print.nepali.mobi/printer/api.php").get();
            // Load data from http request
            final SharedPreferences sharedprefs = getSharedPreferences("cart", 0);
            String apiData = sharedprefs.getString("apiData", "");


            if(apiData.equals("ERROR")) { // No internet or other connection problem with db
                // Todo: This code is so ugly, replace asap
                ArrayList<String> err = new ArrayList<>();
                err.add("Error");
                HashMap<String, List<Product>> msg = new HashMap<>();
                List<Product> durrr = new ArrayList<Product>();
                durrr.add(new Product(getString(R.string.could_not_get_db_info), 0, 0, null, null));
                msg.put("Error", durrr);
                ExpandableListView view = (ExpandableListView) findViewById(R.id.overview_main);
                MainAdapter adapter = new MainAdapter(this, err, msg);
                view.setAdapter(adapter);

            }
            else {

                final JSONArray data = new JSONArray(apiData); // Array of JSONObjects from API

                // Convert json data to arrayList to pass it to gridView
                final ArrayList<String> catlist = new ArrayList<>();
                HashMap<String, List<Product>> prodlist = new HashMap<>();

                if (data != null) {
                    int len = data.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject obj = data.getJSONObject(i);

                        String catname = obj.getString("name_cat");
                        // Actual list view data
                        if (!catlist.contains(catname)) {
                            catlist.add(catname);
                        }

                        List<Product> prods = prodlist.get(catname);
                        if (prods == null) {
                            prods = new ArrayList<>();
                        }

                        prods.add(new Product(
                                obj.getString("name_prod"),
                                Float.parseFloat(obj.getString("price_prod_excl")),
                                Float.parseFloat(obj.getString("price_prod_incl")),
                                stripCommaAtEnd(obj.getString("reference_prod")),
                                catname
                        ));

                        prodlist.put(catname, prods);

                    }
                }

                // Get the grid view and bind array adapter
                ExpandableListView view = (ExpandableListView) findViewById(R.id.overview_main);
                MainAdapter adapter = new MainAdapter(this, catlist, prodlist);
                view.setAdapter(adapter);

                final HashMap<String, List<Product>> prodlist2 = prodlist;

                // Listview on child click listener
                view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        try {
                            // Fetch properties of tapped item
                            String cat = catlist.get(groupPosition);
                            Product prod = prodlist2.get(catlist.get(groupPosition)).get(childPosition);

                            List<Product> products = SharedPrefHelper.getPrintItems(getApplicationContext());
                            if (products == null) products = new ArrayList<>();

                            // If item is already in the list, just increase the count
                            if (products.contains(prod)) {
                                // Todo: check if this is bugging the main refresh count
                                products.remove(prod);
                                prod.increaseCount();
                                products.add(prod);
                            }
                            // Otherwise add the product to print overview list
                            else {
                                products.add(prod);
                            }
                            SharedPrefHelper.setPrintItems(getApplicationContext(), products);

                            // Toast it
                            if (currentToast != null) currentToast.cancel();
                            currentToast = Toast.makeText(getApplicationContext(), getString(R.string.added_products) + " " + prod.getName(),
                                    Toast.LENGTH_SHORT);
                            currentToast.show();

                        } catch (Exception ex) {
                            throw new IllegalArgumentException(ex.getMessage());
                        }

                        return true;
                    }
                });

            }
        }
        catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

//        SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
//        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshContent();
//            }
//        });

    }

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
