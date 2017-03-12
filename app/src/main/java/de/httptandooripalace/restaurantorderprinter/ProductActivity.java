package de.httptandooripalace.restaurantorderprinter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import helpers.SharedPrefHelper;

public class ProductActivity extends AppCompatActivity {

    private Toast currentToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set layout and stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_overview);

        // Get passed data from API request
        SharedPreferences sharedprefs = getSharedPreferences("cart", 0);

        String apiData = sharedprefs.getString("apiData", "");


        try {
            final JSONArray data = new JSONArray(apiData);

            // Convert json data to arrayList to pass it to gridView
            ArrayList<String> list = new ArrayList<>();

            if (data != null) {
                int len = data.length();
                for (int i=0;i<len;i++){
                    JSONObject obj = data.getJSONObject(i);

                    String id = obj.getString("id_prod");
                    String name = obj.getString("name_prod");
                    String price = obj.getString("price_prod");

                    // Cut off strings too long
                    if (name.length() > 30)
                        name = name.substring(0, 30) + "...";

                    // Fix layout glitches with Grid View
                    if(name == "") name = "\n\n\n";

                    // Actual list view data
                    list.add(
                            "ID: " + id + "\n" +
                            name + "\n" +
                            price
                    );

                }
            }

            // Get the grid view and bind array
            GridView view = (GridView) findViewById(R.id.product_gridview);

            // ArrayList to ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);

            // Bind arrayAdapter to view
            view.setAdapter(adapter);

            final Context context = this.getApplicationContext();

            // Tap listener on items
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long clickedid) {

                try {
                    // Fetch properties of tapped item
                    String id = data.getJSONObject(position).getString("id_prod");
                    String name = data.getJSONObject(position).getString("name_prod");
                    String price = data.getJSONObject(position).getString("price_prod");

                    // Get saved products
                    ArrayList<String> ids = SharedPrefHelper.loadArrayList("ids", getApplicationContext());
                    ArrayList<String> names = SharedPrefHelper.loadArrayList("names", getApplicationContext());
                    ArrayList<String> prices = SharedPrefHelper.loadArrayList("prices", getApplicationContext());

                    // Add the values to the lists
                    ids.add(id);
                    names.add(name);
                    prices.add(price);

                    SharedPrefHelper.saveArrayList(ids, "ids", getApplicationContext());
                    SharedPrefHelper.saveArrayList(names, "names", getApplicationContext());
                    SharedPrefHelper.saveArrayList(prices, "prices", getApplicationContext());

                    // Toast it
                    if(currentToast != null) currentToast.cancel();
                    currentToast = Toast.makeText(context, "Added product " + name,
                            Toast.LENGTH_SHORT);
                    currentToast.show();

                } catch(Exception ex) {
                    throw new IllegalArgumentException(ex.getMessage());
                }
                }
            });
        }
        catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

    }

    public void gotoOverview(View view) {
        Intent intent = new Intent(this, PrintOverviewActivity.class);
        startActivity(intent);
    }

}
