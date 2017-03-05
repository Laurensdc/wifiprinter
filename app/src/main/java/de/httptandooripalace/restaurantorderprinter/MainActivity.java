package de.httptandooripalace.restaurantorderprinter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import helper.HttpHandler;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void sendMessage(View view) {

        try {
            new HttpHandler(this.getApplicationContext()).execute("http://tandooripalace.de/billprinter/api.php");

        }
        catch(Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }

    }


}
