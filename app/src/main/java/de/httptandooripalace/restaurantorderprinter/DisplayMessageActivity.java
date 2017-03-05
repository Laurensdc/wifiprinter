package de.httptandooripalace.restaurantorderprinter;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Display back button
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set layout and stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get passed data from API request
        Intent intent = getIntent();
        String message = intent.getExtras().getString("apiData");


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
                        // Fetch id_product of tapped item
                        String prodId = data.getJSONObject(position).getString("id_product");

                        // Toast it
                        Toast.makeText(context, "" + prodId,
                                Toast.LENGTH_SHORT).show();

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

}
