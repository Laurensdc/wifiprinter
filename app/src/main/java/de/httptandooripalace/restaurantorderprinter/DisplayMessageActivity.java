package de.httptandooripalace.restaurantorderprinter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getExtras().getString("apiData");

        TextView changedtv = (TextView) findViewById(R.id.dataview);
        changedtv.setMovementMethod(new ScrollingMovementMethod());
        changedtv.setText(message);


        JSONArray data;

        try {
            data = new JSONArray(message);
        }
        catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

        for(int i = 0; i < data.length(); i++) {

            try {
                JSONObject obj = data.getJSONObject(i);

                Log.d("id product", obj.getString("id_product"));
                Log.d("desscription", obj.getString("description"));
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
