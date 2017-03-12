package de.httptandooripalace.restaurantorderprinter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import helpers.SharedPrefHelper;

public class PrintOverviewActivity extends AppCompatActivity {

    private ArrayList<String> ids, names, prices;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_overview);

        // Get the layout
        LinearLayout theLayout = (LinearLayout) findViewById(R.id.listingLayout);

        ids = SharedPrefHelper.loadArrayList("ids", getApplicationContext());
        names = SharedPrefHelper.loadArrayList("names", getApplicationContext());
        prices = SharedPrefHelper.loadArrayList("prices", getApplicationContext());

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

    // Delete the print overview and refresh activity
    public void deletePrintOverview(View view) {
        SharedPrefHelper.deleteSharedPrefs(getApplicationContext());
        finish();
        startActivity(getIntent());

    }

    // Do print job button clicked
    public void doPrintJob(View view) {
        StringBuilder strb = new StringBuilder();

        strb.append("<BIG>Bill<BR><BR>"); // Todo table number and other info

        for(int i = 0; i < ids.size(); i++) {
            strb.append("Product: " + names.get(i) + "<BR>Price: €" + prices.get(i) + "<BR><BR>");
        }

        strb.append("<BR><BR>");

        Intent intent = new Intent("pe.diegoveloper.printing");
        //intent.setAction(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT,strb.toString());
        startActivity(intent);
    }


}
