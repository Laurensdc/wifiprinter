package de.httptandooripalace.restaurantorderprinter;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import helpers.SharedPrefHelper;

public class PrintOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_overview);

        // Get the layout
        LinearLayout theLayout = (LinearLayout) findViewById(R.id.listingLayout);

        // Get saved products
//        SharedPreferences settings = getSharedPreferences("cart", 0);
//        Set<String> ids = settings.getStringSet("product_ids", new HashSet<String>());
//        Set<String> descriptions = settings.getStringSet("product_descriptions", new HashSet<String>());


        ArrayList<String> ids = SharedPrefHelper.loadArrayList("product_ids", getApplicationContext());
        ArrayList<String> descriptions = SharedPrefHelper.loadArrayList("descriptions", getApplicationContext());


        String[] idsArr = ids.toArray(new String[ids.size()]);
        String[] descriptionsArr = descriptions.toArray(new String[descriptions.size()]);

        for(int i = 0; i < idsArr.length; i++) {
            TextView t = new TextView(this);
            t.setText("ID: " + idsArr[i]);
            theLayout.addView(t);

            TextView t2 = new TextView(this);
            t2.setText("Description: " + descriptionsArr[i] + "\n\n");
            theLayout.addView(t2);
        }


    }

    public void deletePrintOverview(View view) {
//        SharedPreferences settings = getSharedPreferences("cart", 0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.remove("product_ids");
//        editor.remove("product_descriptions");
//        editor.commit();

        SharedPrefHelper.deleteArrayList("product_ids", getApplicationContext());
        SharedPrefHelper.deleteArrayList("descriptions", getApplicationContext());

        finish();
        startActivity(getIntent());

    }
}
