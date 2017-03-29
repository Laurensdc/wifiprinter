package de.httptandooripalace.restaurantorderprinter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import entities.Product;
import helpers.PrintAdapter;
import helpers.Rounder;
import helpers.SharedPrefHelper;

import static android.media.CamcorderProfile.get;

public class PrintActivity extends AppCompatActivity {

    private List<Product> products;
    private final int CHARCOUNT_BIG = 48; // Amount of characters fit on one printed line, using $big$ format

    private final String INITIATE = "·27··64·"; // ESC @
    private final String CHAR_TABLE_EURO = "·27··116··19·"; // ESC t 19 -- 19 for euro table
    private final String EURO = "·213·";


    private TextView totalPriceTextView;

    private entities.Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_activity);

        // Get products
        products = SharedPrefHelper.getPrintItems(getApplicationContext());
        settings = SharedPrefHelper.loadSettings(getApplicationContext());

        // Bind products to print overview
        ListView view = (ListView) findViewById(R.id.listingLayout);
        PrintAdapter adapter = new PrintAdapter(getApplicationContext(), products);
        view.setAdapter(adapter);

        totalPriceTextView = (TextView) findViewById(R.id.print_table_number);


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    // Add settings menu icon to toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    // Open settings menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Delete the print overview and refresh activity
    public void deletePrintOverview(View view) {
        SharedPrefHelper.deleteSharedPrefs(getApplicationContext());
        finish();
        startActivity(getIntent());

    }


    public void printWithPOSPrinterDriverEsc(View view) {
        String tableNr = SharedPrefHelper.getString(getApplicationContext(), "tableNr");
        StringBuilder strb = new StringBuilder("$intro$");

        strb.append(INITIATE);
        strb.append(CHAR_TABLE_EURO);

        strb.append("$bigw$");
        if(!settings.getNameLine1().equals(""))
            strb.append(alignCenter(settings.getNameLine1()) + "$intro$");
        if(!settings.getNameLine2().equals(""))
            strb.append(alignCenter(settings.getNameLine2()) + "$intro$");
        if(!settings.getAddrLine1().equals(""))
            strb.append(alignCenter(settings.getAddrLine1()) + "$intro$");
        if(!settings.getAddrLine2().equals(""))
            strb.append(alignCenter(settings.getAddrLine2()) + "$intro$");
        if(!settings.getTelLine().equals(""))
            strb.append(alignCenter(settings.getTelLine()) + "$intro$");
        if(!settings.getExtraLine().equals(""))
            strb.append(alignCenter(settings.getExtraLine()) + "$intro$");


        if(!tableNr.equals("")) {
            strb.append("$bighw$");
            strb.append("$intro$");
            strb.append(alignCenter("Rechnung") + "$intro$" + alignCenter("TISH NR.: " + tableNr) + "$intro$$intro$$intro$");
        }

        strb.append("$intro$$big$$intro$");


        double totalPriceExcl = 0;
        double totalPriceIncl = 0;

        for(int i = 0; i < products.size(); i++) {
            if(products.get(i).getCount() < 1) continue;

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
        intentPrint.putExtra("printer_ip", settings.getPrinterIp());
//        intentPrint.putExtra("printer_ip", "192.168.178.105");
        intentPrint.putExtra("printer_port", "9100");
        intentPrint.setType("text/plain");
        this.startActivity(intentPrint);
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

    private String alignCenter(String s) {
        int length = s.length();
        int totalSpaceLeft = CHARCOUNT_BIG - length;
        int spaceOnBothSides = totalSpaceLeft / 2;
        String newstr = "";
        for(int i = 0; i < spaceOnBothSides; i++) {
            newstr += " ";
        }
        newstr += s;
        for(int i = 0; i < spaceOnBothSides; i++) {
            newstr += " ";
        }

        return newstr;
    }

    // Do print job button clicked
    // !! NOT UPDATED
    public void printWithQuickPrinter(View view) {
        StringBuilder strb = new StringBuilder();


        strb.append("<BIG>Bill<BR><BR>");

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
