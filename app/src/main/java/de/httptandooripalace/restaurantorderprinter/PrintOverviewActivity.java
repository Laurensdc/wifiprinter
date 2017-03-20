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
import java.util.List;

import entities.Product;
import helpers.SharedPrefHelper;

public class PrintOverviewActivity extends AppCompatActivity {

    private List<Product> products;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_overview);

        // Get the layout
        LinearLayout theLayout = (LinearLayout) findViewById(R.id.listingLayout);

        products = SharedPrefHelper.getPrintItems(getApplicationContext());

        Log.d("ya", "ya");

        if(products == null) products = new ArrayList<>();

        // Dynamically add textviews
        for(int i = 0; i < products.size(); i++) {
            TextView t = new TextView(this);
            t.setText(products.get(i).getName() + "x" + products.get(i).getCount()
                    + "\nPrice excl: " + products.get(i).getPrice_excl()
                    + "\nPrice incl: " + products.get(i).getPrice_incl()
                    + "\n\n");
            theLayout.addView(t);

        }


    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
//
//        LinearLayout ll = (LinearLayout) findViewById(R.id.listingLayout);
//        ll.removeAllViews();

    }

    // Delete the print overview and refresh activity
    public void deletePrintOverview(View view) {

        SharedPrefHelper.deleteSharedPrefs(getApplicationContext());
        finish();
        startActivity(getIntent());

    }



    public void printWithPOSPrinterDriverEsc(View view) {
        byte dot = 0x10;
        String tab = "9";
        String tabtest = "▪9▪tabtest▪9▪▪9▪▪9▪▪9▪tabtest▪9▪$intro$▪9▪▪9▪▪9▪▪9▪tabtest3$intro$";
        // ------------------------

        String tableNr = SharedPrefHelper.getString(getApplicationContext(), "tableNr");

        StringBuilder strb = new StringBuilder();

        // Todo: save heading in server settings on load it in dynamically
        strb.append("$intro$$bighw$Restaurant Tandoori$intro$$intro$$intro$"); // Todo: table number and other info
        strb.append("$big$Table nr: " + tableNr + "$intro$$intro$$intro$");

        float totalPriceExcl = 0;
        float totalPriceIncl = 0;

        for(int i = 0; i < products.size(); i++) {
            Float priceEx = products.get(i).getPrice_excl();
            Float priceInc = products.get(i).getPrice_incl();
            strb.append(products.get(i).getName() + "$intro$" +
                    "Price excl: " + priceEx + "$intro$" +
                    "Price incl: " + priceInc + "$intro$$intro$" );

            totalPriceExcl += priceEx;
            totalPriceIncl += priceInc;

        }

        double tax = totalPriceIncl - totalPriceExcl;

        strb.append("--------------------------$intro$");
        strb.append("Total price excl. tax: " + totalPriceExcl + "$intro$");
        strb.append("Tax: " + tax + "$intro$");
        strb.append("==========================");
        strb.append("Total price incl. tax: " + totalPriceIncl + "$intro$");
        strb.append("$intro$$intro$$intro$$intro$$intro$$intro$$intro$$intro$$intro$$cut$");


        String dataToPrint = strb.toString();
        Intent intentPrint = new Intent();
        intentPrint.setAction(Intent.ACTION_SEND);
        intentPrint.putExtra(Intent.EXTRA_TEXT, dataToPrint);
        intentPrint.putExtra("printer_type_id", "1");// For IP
        intentPrint.putExtra("printer_ip", "192.168.178.105");
        intentPrint.putExtra("printer_port", "9100");

        intentPrint.setType("text/plain");

        this.startActivity(intentPrint);
    }


    // Do print job button clicked
    // !! NOT UPDATED
    public void printWithQuickPrinter(View view) {
        StringBuilder strb = new StringBuilder();

        // Todo: save heading in server settings on load it in dynamically
        strb.append("<BIG>Bill<BR><BR>"); // Todo: table number and other info

        for(int i = 0; i < products.size(); i++) {
            strb.append(products.get(i).getName() + "<BR>" + products.get(i).getPrice_incl() + "<BR><BR>");
        }

        strb.append("<BR><BR>");

        Intent intent = new Intent("pe.diegoveloper.printing");
        //intent.setAction(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT,strb.toString());
        startActivity(intent);
    }


}
