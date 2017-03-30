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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import entities.Product;
import helpers.PrintAdapter;
import helpers.Rounder;
import helpers.SharedPrefHelper;

import static android.media.CamcorderProfile.get;
import static de.httptandooripalace.restaurantorderprinter.R.string.total;

public class PrintActivity extends AppCompatActivity {

    private List<Product> products;
    private final int CHARCOUNT_BIG = 48; // Amount of characters fit on one printed line, using $big$ format
    private final int CHARCOUNT_BIGW = 24; // Amount of characters fit on one printed line, using $bigw$ format

    private final String INITIATE = "·27··64·"; // ESC @
    private final String CHAR_TABLE_EURO = "·27··116··19·"; // ESC t 19 -- 19 for euro table
    private final String EURO = "·213·";
    private final String CLEAR_BUFFERS = "··"; // DLE DC4 (=fn8)



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

    // Print bill layout
    public void printWithPOSPrinterDriverEsc(View view) {
        String tableNr = SharedPrefHelper.getString(getApplicationContext(), "tableNr");
        StringBuilder strb = new StringBuilder("");

        strb.append(INITIATE);
        strb.append(CHAR_TABLE_EURO);

        strb.append("$intro$");

        strb.append("$bigw$");
        if(!settings.getNameLine1().equals(""))
            strb.append(alignCenterBigw(settings.getNameLine1()) + "$intro$");
        if(!settings.getNameLine2().equals(""))
            strb.append(alignCenterBigw(settings.getNameLine2()) + "$intro$");
        if(!settings.getAddrLine1().equals(""))
            strb.append(alignCenterBigw(settings.getAddrLine1()) + "$intro$");
        if(!settings.getAddrLine2().equals(""))
            strb.append(alignCenterBigw(settings.getAddrLine2()) + "$intro$");
        if(!settings.getTelLine().equals(""))
            strb.append(alignCenterBigw(settings.getTelLine()) + "$intro$");
        if(!settings.getExtraLine().equals(""))
            strb.append(alignCenterBigw(settings.getExtraLine()) + "$intro$");


        strb.append("$bighw$$intro$" + alignCenterBigw(getString(R.string.bill)) + "$intro$");

        if(!tableNr.equals("")) {
            strb.append("$bighw$");
            strb.append(alignCenterBigw(getString(R.string.table_nr) + tableNr) + "$intro$");
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
        strb.append(getString(R.string.price_excl) +
                    alignRight((EURO + Rounder.round(totalPriceExcl)), (getString(R.string.price_excl)).length()));
        strb.append("$intro$");

        // Tax
        String tax = Rounder.round(totalPriceIncl - totalPriceExcl);
        strb.append(getString(R.string.tax) +
                    alignRight((EURO + tax), (getString(R.string.tax)).length()));


        // Total incl
        strb.append("$intro$$bigw$");

        String totalPriceInc = Rounder.round(totalPriceIncl);


        strb.append(getString(R.string.total) +
                    alignRightBigw((EURO + totalPriceInc), (getString(R.string.total)).length()));


        // Date
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        strb.append("$big$$intro$$intro$");
        strb.append(currentDateTimeString);
        strb.append("$intro$$intro$");

        // Served by waiter
        if(!settings.getWaiter().equals(""))
            strb.append(alignCenter(getString(R.string.served_by) + " " + settings.getWaiter())  + "$intro$");

        strb.append(alignCenter(getString(R.string.tyvm)) + "$intro$");
        strb.append(alignCenter(getString(R.string.visit_again)));

        strb.append("$intro$$intro$$intro$$intro$$intro$$intro$$intro$$intro$$intro$$cut$");

        Print(strb.toString());


    }

    private void Print(String dataToPrint) {
        Intent intentPrint = new Intent();
        intentPrint.setAction(Intent.ACTION_SEND);
        intentPrint.putExtra(Intent.EXTRA_TEXT, dataToPrint);
        intentPrint.putExtra("printer_type_id", "1");// For IP
        intentPrint.putExtra("printer_ip", settings.getPrinterIp());
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
        // EURO length counts as more than 1 character and bugs alignment
        if(s.contains(EURO)) {
            paddingLeft += EURO.length() - 1;
        }
        String newstr = "";
        for(int i = 0; i < paddingLeft - offsetLeft; i++) {
            newstr += " ";
        }
        newstr += s;
        return newstr;
    }

    private String alignRightBigw(String s, int offsetLeft) {
        int length = s.length();
        int paddingLeft = CHARCOUNT_BIGW - length;
        // EURO length counts as more than 1 character and bugs alignment
        if(s.contains(EURO)) {
            paddingLeft += EURO.length() - 1;
        }
        String newstr = "";
        for(int i = 0; i < (paddingLeft - offsetLeft); i++) {
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

    private String alignCenterBigw(String s) {
        int length = s.length();
        int totalSpaceLeft = CHARCOUNT_BIGW - length;
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
