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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import entities.Product;
import helpers.Rounder;
import helpers.SharedPrefHelper;

import static android.media.CamcorderProfile.get;

public class PrintOverviewActivity extends AppCompatActivity {

    private List<Product> products;
    private WebView mWebView;
    private final int CHARCOUNT_BIG = 48;

    private final String INITIATE = "·27··64·"; // ESC @
    private final String CHAR_TABLE_EURO = "·27··116··19·"; // ESC t 19 -- 19 for euro table
    private final String EURO = "·213·";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_overview);

        // Get the layout
        LinearLayout theLayout = (LinearLayout) findViewById(R.id.listingLayout);

        products = SharedPrefHelper.getPrintItems(getApplicationContext());

        Log.d("ya", "ya");

        if(products == null) products = new ArrayList<>();

        // Adding all dynamically created textviews to an arrayList, so we can edit the text later
        // When pressing + or - buttons
        final ArrayList<TextView> textviews = new ArrayList<>();

        // Dynamically add textviews
        for(int i = 0; i < products.size(); i++) {
            // Create new horizontal linear layout
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView t = new TextView(this);
            t.setText(printOverviewItemText(products.get(i)));

            row.addView(t);
            textviews.add(t);

            final int i2 = i;
            Button buttonPlus = new Button(this);
            buttonPlus.setText("+");

            buttonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product p = products.get(i2);

                    // Todo: Find a better way to do this

                    p.increaseCount();
                    products.set(i2, p);
                    Log.d("Button plus", textviews.get(i2).getText().toString());

                    textviews.get(i2).setText(printOverviewItemText(p));

                }
            });
            row.addView(buttonPlus);

            Button buttonMinus = new Button(this);
            buttonMinus.setText("-");

            buttonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product p = products.get(i2);

                    p.decreaseCount();
                    products.set(i2, p);

                    textviews.get(i2).setText(printOverviewItemText(p));

                }
            });
            row.addView(buttonMinus);
            theLayout.addView(row);
        }
    }

    private String printOverviewItemText(Product p) {
        return (p.getName() + " x " + p.getCount()
                + "\nPrice incl: €" + Rounder.round(p.getPrice_incl())
                + "\nReference nr: " + p.getReference()
                + "\n\n");
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


    private String alignRight(String s) {
        int length = s.length();
        int paddingLeft = CHARCOUNT_BIG - length;
        String newstr = "";
        for(int i = 0; i < paddingLeft; i++) {
            newstr += " ";
        }
        newstr += s;
        return newstr;
    }

    private String alignRight(String s, int offsetLeft) {
        int length = s.length();
        int paddingLeft = CHARCOUNT_BIG - length;
        String newstr = "";
        for(int i = 0; i < paddingLeft - offsetLeft; i++) {
            newstr += " ";
        }
        newstr += s;
        return newstr;
    }

    public void printWithPOSPrinterDriverEsc(View view) {
        String tableNr = SharedPrefHelper.getString(getApplicationContext(), "tableNr");

        StringBuilder strb = new StringBuilder(" $intro$");

        strb.append(INITIATE);
        strb.append(CHAR_TABLE_EURO);

        // Todo: save heading in server settings on load it in dynamically
        strb.append("$bighw$Restaurant Tandoori$intro$$intro$$intro$"); // Todo: table number and other info
        strb.append("$big$Table nr: " + tableNr + "$intro$$intro$$intro$");

        double totalPriceExcl = 0;
        double totalPriceIncl = 0;

        for(int i = 0; i < products.size(); i++) {
            double priceEx = products.get(i).getPrice_excl();
            double priceInc = products.get(i).getPrice_incl();

            for(int j = 0; j < CHARCOUNT_BIG; j++) {
                strb.append("-");
            }
            strb.append("$intro$");

            // 2 x 2.15
            strb.append(products.get(i).getCount() + " x " + EURO + Rounder.round(products.get(i).getPrice_excl()));
            strb.append("$intro$");

            // All Star Product                 4.30
            String totalPriceForThisProduct = Rounder.round(products.get(i).getCount() * products.get(i).getPrice_excl());
            strb.append(products.get(i).getName() + alignRight((EURO + totalPriceForThisProduct), products.get(i).getName().length()) );
            strb.append("$intro$");

            totalPriceExcl += (priceEx * products.get(i).getCount());
            totalPriceIncl += (priceInc * products.get(i).getCount());

        }

        for(int j = 0; j < CHARCOUNT_BIG; j++) {
            strb.append("-");
        }
        strb.append("$intro$");

        // Total excl
        strb.append("Nettoumsatz" + alignRight((EURO + Rounder.round(totalPriceExcl)), ("Nettoumsatz").length()));
        strb.append("$intro$");

        // Tax
        String tax = Rounder.round(totalPriceIncl - totalPriceExcl);
        strb.append("Mwst." + alignRight((EURO + tax), ("Mwst.").length()));
        strb.append("$intro$");


        // Total incl
        strb.append("Total" + alignRight((EURO + Rounder.round(totalPriceIncl)), ("Total").length()));
        strb.append("$intro$");


        strb.append("$intro$$intro$$intro$$intro$$intro$$intro$$intro$$intro$$intro$$cut$");

        Print(strb.toString());

    }

    private void Print(String dataToPrint) {
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
