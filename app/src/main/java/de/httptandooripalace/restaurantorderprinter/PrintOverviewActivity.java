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

        names = SharedPrefHelper.loadArrayList("printItems", getApplicationContext());

        for(int i = 0; i < names.size(); i++) {
            TextView t = new TextView(this);
            t.setText(names.get(i));
            theLayout.addView(t);



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

        for(int i = 0; i < names.size(); i++) {
            strb.append(names.get(i) + "<BR><BR>");
        }

        strb.append("<BR><BR>");

        Intent intent = new Intent("pe.diegoveloper.printing");
        //intent.setAction(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT,strb.toString());
        startActivity(intent);
    }


}
