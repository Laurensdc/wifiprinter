package de.httptandooripalace.restaurantorderprinter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import entities.Product;
import helpers.HttpHandler;
import helpers.MainOverviewAdapter;
import helpers.SharedPrefHelper;

import static android.R.attr.name;


public class MainOverviewActivity extends AppCompatActivity {

    private Toast currentToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_overview);


        try {
            new HttpHandler(this.getApplicationContext()).execute("http://print.nepali.mobi/printer/api.php?function=getcategories").get();


            // Load data from http request
            final SharedPreferences sharedprefs = getSharedPreferences("cart", 0);

            String apiData = sharedprefs.getString("apiData", "");
            final JSONArray data = new JSONArray(apiData); // Array of JSONObjects from API

            // Convert json data to arrayList to pass it to gridView
            final ArrayList<String> catlist = new ArrayList<>();
            final HashMap<String, List<Product>> prodlist = new HashMap<>();

            if (data != null) {
                int len = data.length();
                for (int i=0;i<len;i++){
                    JSONObject obj = data.getJSONObject(i);


                    String catname = obj.getString("name_cat");
                    // Actual list view data
                    if(!catlist.contains(catname)) {
                        catlist.add(catname);
                    }


                    List<Product> prods = prodlist.get(catname);
                    if(prods == null) {
                        prods = new ArrayList<>();
                    }

                    prods.add(new Product(
                            Integer.parseInt(obj.getString("id_prod")),
                            obj.getString("name_prod"),
                            Float.parseFloat(obj.getString("price_prod")),
                            catname
                    ));

                    prodlist.put(catname, prods);

                }
            }

            // Get the grid view and bind array
            ExpandableListView view = (ExpandableListView) findViewById(R.id.overview_main);

            MainOverviewAdapter adapter = new MainOverviewAdapter(this, catlist, prodlist);

            // Bind arrayAdapter to view
            view.setAdapter(adapter);


            // Listview on child click listener
            view.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                    try {
                        // Fetch properties of tapped item
                        String cat = catlist.get(groupPosition);
                        Product prod = prodlist.get(catlist.get(groupPosition)).get(childPosition);

                        String printOverviewItem = cat + " / " + prod.getName() + ": " + prod.getPrice();
                        List<Product> products = SharedPrefHelper.getPrintItems(getApplicationContext());

                        if(products == null) products = new ArrayList<Product>();
                        products.add(prod);
                        SharedPrefHelper.setPrintItems(getApplicationContext(), products);

                        // Toast it
                        if(currentToast != null) currentToast.cancel();
                        currentToast = Toast.makeText(getApplicationContext(), "Added product " + printOverviewItem,
                                Toast.LENGTH_SHORT);
                        currentToast.show();

                    } catch(Exception ex) {
                        throw new IllegalArgumentException(ex.getMessage());
                    }

                    return true;
                }
            });
        }
        catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

        SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

    }

    private void refreshContent() {
        finish();
        startActivity(getIntent());

    }


    public void gotoOverview(View view) {
        Intent intent = new Intent(this, PrintOverviewActivity.class);
        startActivity(intent);
    }

}