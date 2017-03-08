package de.httptandooripalace.restaurantorderprinter;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import helpers.SharedPrefHelper;

public class PrintOverviewActivity extends AppCompatActivity {

    ArrayList<String> ids, names, prices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_overview);

        // Get the layout
        LinearLayout theLayout = (LinearLayout) findViewById(R.id.listingLayout);

        ArrayList<String> ids = SharedPrefHelper.loadArrayList("ids", getApplicationContext());
        ArrayList<String> names = SharedPrefHelper.loadArrayList("names", getApplicationContext());
        ArrayList<String> prices = SharedPrefHelper.loadArrayList("prices", getApplicationContext());


//        idsArr = ids.toArray(new String[ids.size()]);
//        namesArr = names.toArray(new String[names.size()]);
//        pricesArr = prices.toArray(new String[prices.size()]);

        for(int i = 0; i < ids.size(); i++) {
            TextView t = new TextView(this);
            t.setText("ID: " + ids.get(i));
            theLayout.addView(t);

            TextView t2 = new TextView(this);
            t2.setText("Description: " + names.get(i));
            theLayout.addView(t2);

            TextView t3 = new TextView(this);
            t3.setText("Price: " + prices.get(i) + "\n\n");
            theLayout.addView(t3);


        }


    }

    public void deletePrintOverview(View view) {
        SharedPrefHelper.deleteSharedPrefs(getApplicationContext());

        finish();
        startActivity(getIntent());

    }
}
