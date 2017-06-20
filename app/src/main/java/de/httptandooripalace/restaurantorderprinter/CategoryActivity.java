package de.httptandooripalace.restaurantorderprinter;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.MenuItem;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import helpers.RequestClient;

import static com.loopj.android.http.AsyncHttpClient.log;
import static de.httptandooripalace.restaurantorderprinter.R.string.bill;
import static de.httptandooripalace.restaurantorderprinter.R.string.table_nr;


public class CategoryActivity extends AppCompatActivity {
    /***TO ADD CATEGORY TO GROUP USE TABLE ps_category_lang***
    //TO EDIT GROUPS USE app_group_category TABLE*/
    public static int group; //variable that is used in MainActivity for displaying group
    public static int o_bill;
    public static String o_table;
    Context context;
    int id =0;
    String name ="";
    JSONArray jsonarray = null;
    JSONObject jsonobject = null;
    LinearLayout lin_layout;
    int bill_nr = 0;
    String tableNr = "#";
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        group= 0;
        extras = getIntent().getExtras();
        if (extras!=null){
            o_bill = extras.getInt("bill_nr");
            o_table = extras.getString("tableNr");
        }

    }
    // something
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.bills_overview:
                Intent i2 = new Intent(this, OverviewActivity.class);
                startActivity(i2);
                return true;
            case R.id.bills_history:
                Intent i3 = new Intent(this, HistoryActivity.class);
                startActivity(i3);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        context = this;
            System.out.println("EDITING BILL NR : " + bill_nr);
            // and get whatever type user account id is
            // and get whatever type user account id is
        try {
            RequestClient.get("products/getgroup/", new JsonHttpResponseHandler(){
                //request api file, to get groups id and names
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                    System.out.println("GETTING GROUPS");
                    try {
                        lin_layout = (LinearLayout)findViewById(R.id.LinearLayout);
                        Log.d("RESPONSE", response.toString());
                        jsonarray = response.getJSONArray("groups");
                        //to avoid duplicates
                        if(lin_layout.getChildCount()>0)lin_layout.removeAllViews();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            jsonobject = jsonarray.getJSONObject(i);
                            id = jsonobject.getInt("id_group");
                            name = jsonobject.getString("name");

                            Button b = new Button(context); //initialize the button
                            b.setId(id); //setting button id to group id
                            b.setText(name);
                            lin_layout.addView(b);
                            b.setOnClickListener(select_category(b));//setting listener for button
                        }
                        Log.d("RESPONSE", "Groups :");
                    }
                    catch(Exception e) {
                        Log.d("Exception HTTP", e.getMessage());
                    }

                }
                @Override
                public void onFailure(int c, Header[] h, String r, Throwable t) {
                    try {
                        Log.d("RESPONSE", r.toString());
                    }
                    catch(Exception e) {
                        Log.d("Exception HTTP", e.getMessage());
                    }
                }
            });
        }
        catch(Exception e){
            Log.d("Ex", e.getMessage());
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    View.OnClickListener select_category(final Button button)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                group = button.getId();
                Intent i = new Intent(context, MainActivity.class);
                if (o_bill!=0){
                    bill_nr = o_bill;
                    tableNr = o_table;
                    i.putExtra("bill_nr",bill_nr);
                    i.putExtra("tableNr", tableNr);
                }
                startActivity(i);
            }
        };
    }

}
