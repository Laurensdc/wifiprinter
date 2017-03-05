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
import java.util.HashSet;
import java.util.Set;

public class ProductSelectionActivity extends AppCompatActivity {

    private Toast currentToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set layout and stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);

        // Get passed data from API request
        //Intent intent = getIntent();
        //String message = intent.getExtras().getString("apiData");

        SharedPreferences settings = getSharedPreferences("cart", 0);

        String message = settings.getString("apiData", "");


        try {
            final JSONArray data = new JSONArray(message);

            // Convert json data to arrayList to pass it to gridView
            ArrayList<String> list = new ArrayList<String>();

            if (data != null) {
                int len = data.length();
                for (int i=0;i<len;i++){
                    JSONObject obj = data.getJSONObject(i);

                    String productId = obj.getString("id_product");
                    String description = obj.getString("description_short");

                    // Cut off strings too long
                    if (description.length() > 30)
                        description = description.substring(0, 30) + "...";

                    // Fix layout glitches with Grid View
                    if(description == "") description = "\n\n\n";

                    // Actual list view data
                    list.add(
                            "ID: " + productId + "\n" +
                            description
                    );

                }
            }

            // Get the grid view and bind array
            GridView view = (GridView) findViewById(R.id.gridview);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);

            view.setAdapter(adapter);

            final Context context = this.getApplicationContext();

            // Tap listener on items
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    try {
                        // Init filename and variables
                        String FILENAME = "cart";

                        // Fetch properties of tapped item
                        String prodId = data.getJSONObject(position).getString("id_product");
                        String description = data.getJSONObject(position).getString("description_short");

                        // Get saved products
                        SharedPreferences settings = getSharedPreferences(FILENAME, 0);
                        Set<String> ids = settings.getStringSet("product_ids", new HashSet<String>());
                        Set<String> descriptions = settings.getStringSet("product_descriptions", new HashSet<String>());


                        // Add the values to the sets
                        ids.add(prodId);
                        descriptions.add(description);


                        // We need an Editor object to make preference changes.
                        SharedPreferences.Editor editor = settings.edit();

                        // Write them back
                        editor.putStringSet("product_ids", ids);
                        editor.putStringSet("product_descriptions", descriptions);


                        // Commit the edits
                        editor.commit();


                        // Toast it
                        if(currentToast != null) currentToast.cancel();
                        currentToast = Toast.makeText(context, "Added product with ID " + prodId,
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
