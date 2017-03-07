package de.httptandooripalace.restaurantorderprinter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import helpers.HttpHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void sendMessage(View view) {

        try {
            new HttpHandler(this.getApplicationContext()).execute("http://print.nepali.mobi/printer/api.php?function=getproducts").get();



            Intent intent = new Intent(this.getApplicationContext(), ProductSelectionActivity.class);

            //intent.putExtra("apiData", result.toString());
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

    }


}
