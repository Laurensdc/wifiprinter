package de.httptandooripalace.restaurantorderprinter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import entities.Bill;
import helpers.OverviewAdapter;
import helpers.SharedPrefHelper;

/**
 * Created by uiz on 26/04/2017.
 */

public class OverviewActivity extends AppCompatActivity {

    private entities.Settings settings;
    private List<Bill> bills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_activity);

//        SwipeRefreshLayout swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
//        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshContent();
//            }
//        });
        //TODO : getOpenBills method
//        bills = SharedPrefHelper.getOpenBills(getApplicationContext());
//        settings = SharedPrefHelper.loadSettings(getApplicationContext());
//
//        // Bind products to print overview
//        ListView view = (ListView) findViewById(R.id.listingLayout);
//        OverviewAdapter adapter = new OverviewAdapter(getApplicationContext(), bills);
//        view.setAdapter(adapter);

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
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void refreshContent() {
        finish();
        startActivity(getIntent());
    }

    public void new_bill(View view){
        //TODO : delete the selection of the precedent bill, to start the new one
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void close_bill(View view){
        //TODO : send this bill to the closed ones
    }

    public void delete_bill(View view){
        // TODO : delete this bill of the database ( on its ID)
    }

    public void edit_bill(View view){
        // TODO : open the selected bill using its ID
    }

}
