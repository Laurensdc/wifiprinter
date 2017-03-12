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
        //doWebViewPrint(ids, names, prices);
        //String dataToPrint="$big$This is a printer test$intro$posprinterdriver.com$intro$$intro$$cut$$intro$";

//
//
//        //String textToSend="$intro$$big$Test test 123 test$intro$$intro$$intro$";
//        String textToSend="$intro$$intro$$intro$$big$AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA$intro$$intro$$intro$$intro$$intro$$small$BBBBBBBBBBBBBBBBBBBB$intro$$intro$$intro$";
//        Intent intentPrint = new Intent();
//        intentPrint.setAction(Intent.ACTION_SEND);
//        intentPrint.putExtra(Intent.EXTRA_TEXT, textToSend);
//        intentPrint.putExtra("printer_type_id", "1");// For IP
//        intentPrint.putExtra("printer_ip", "192.168.178.105");
//        intentPrint.putExtra("printer_port", "9100");
//
//        intentPrint.setType("text/plain");
//        Log.i("Print job log: ", "sendDataToIPPrinter Start Intent");
//
//        this.startActivity(intentPrint);
//


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


    private void doWebViewPrint(ArrayList<String> ids, ArrayList<String> names, ArrayList<String> prices) {
        // Create a WebView object specifically for printing
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("PAGE FINISHED", "page finished loading " + url);
                createWebPrintJob(view);
                mWebView = null;
            }
        });

        // Generate an HTML document on the fly:

        StringBuilder strb = new StringBuilder();

        strb.append("<html><body>");

        strb.append("<h1>Bill</h1>"); // Todo table number and other info
        strb.append("<ul>");
        for(int i = 0; i < ids.size(); i++) {
            strb.append("<li>" + names.get(i) + " - " + prices.get(i) + "</li>");

        }

        strb.append("</ul>");
        strb.append("</body></html>");

        webView.loadDataWithBaseURL(null, strb.toString(), "text/HTML", "UTF-8", null);

        // Keep a reference to WebView object until you pass the PrintDocumentAdapter
        // to the PrintManager
        mWebView = webView;
    }

    private void createWebPrintJob(WebView webView) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter("printdoc");

        // Create a print job with name and adapter instance
        String jobName = getString(R.string.app_name) + " Document";

        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder()
                        //.setMediaSize(PrintAttributes.MediaSize.ISO_A7)
                        .setMediaSize(new PrintAttributes.MediaSize("customMediaId", "customMediaLabel", 80, 210))
                        .setMinMargins(new PrintAttributes.Margins(5, 10, 5, 20))
                        .setResolution(new PrintAttributes.Resolution("customResId", "customResLabel", 203, 203))
                        .build());

        // Save the job object for later status checking
        //mPrintJobs.add(printJob);
    }
}
