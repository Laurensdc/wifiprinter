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

        ArrayList<String> ids = SharedPrefHelper.loadArrayList("ids", getApplicationContext());
        ArrayList<String> names = SharedPrefHelper.loadArrayList("names", getApplicationContext());
        ArrayList<String> prices = SharedPrefHelper.loadArrayList("prices", getApplicationContext());


        String[] idsArr = ids.toArray(new String[ids.size()]);
        String[] namesArr = names.toArray(new String[names.size()]);
        String[] pricesArr = prices.toArray(new String[prices.size()]);

        for(int i = 0; i < idsArr.length; i++) {
            TextView t = new TextView(this);
            t.setText("ID: " + idsArr[i]);
            theLayout.addView(t);

            TextView t2 = new TextView(this);
            t2.setText("Description: " + namesArr[i]);
            theLayout.addView(t2);

            TextView t3 = new TextView(this);
            t3.setText("Price: " + pricesArr[i] + "\n\n");
            theLayout.addView(t3);


        }


    }

    public void deletePrintOverview(View view) {
        SharedPrefHelper.deleteArrayList("ids", getApplicationContext());
        SharedPrefHelper.deleteArrayList("names", getApplicationContext());
        SharedPrefHelper.deleteArrayList("prices", getApplicationContext());


        finish();
        startActivity(getIntent());

    }
}
