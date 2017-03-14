package de.httptandooripalace.restaurantorderprinter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helpers.HttpHandler;
import helpers.MainOverviewAdapter;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.R.id.list;
import static android.media.CamcorderProfile.get;

public class CategoryActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_overview);


        try {
            new HttpHandler(this.getApplicationContext()).execute("http://print.nepali.mobi/printer/api.php?function=getcategories").get();

            // Get the layout
            LinearLayout theLayout = (LinearLayout) findViewById(R.id.activity_main);

            // Load data from http request
            final SharedPreferences sharedprefs = getSharedPreferences("cart", 0);

            String apiData = sharedprefs.getString("apiData", "");

            final JSONArray data = new JSONArray(apiData);

            // Convert json data to arrayList to pass it to gridView
            ArrayList<String> catlist = new ArrayList<>();
            HashMap<String, List<String>> prodlist = new HashMap<>();

            if (data != null) {
                int len = data.length();
                for (int i=0;i<len;i++){
                    JSONObject obj = data.getJSONObject(i);


                    String catname = obj.getString("name_cat");
                    // Actual list view data
                    catlist.add(catname);


                    List<String> prods = prodlist.get(catname);
                    if(prods == null) {
                        prods = new ArrayList<>();
                    }

                    prods.add(obj.getString("name_prod"));

                    prodlist.put(catname, prods);




                }
            }

            // Get the grid view and bind array
            ExpandableListView view = (ExpandableListView) findViewById(R.id.overview_main);

            MainOverviewAdapter adapter = new MainOverviewAdapter(this, catlist, prodlist);

            // ArrayList to ArrayAdapter
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_list_item_1, list);

            // Bind arrayAdapter to view
            view.setAdapter(adapter);

//            // Tap listener on items
//            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> parent, View v,
//                                        int position, long clickedid) {
//
//                    try {
//                        // Fetch properties of tapped item
//                        String id = data.getJSONObject(position).getString("id_cat");
//                        String name = data.getJSONObject(position).getString("name_cat");
//
//                        new HttpHandler(getApplicationContext()).execute("http://print.nepali.mobi/printer/api.php?function=getproducts&catid=" + id).get();
//
//                        Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
//                        startActivity(intent);
//
//                    } catch(Exception ex) {
//                        throw new IllegalArgumentException(ex.getMessage());
//                    }
//                }
//            });

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



}
