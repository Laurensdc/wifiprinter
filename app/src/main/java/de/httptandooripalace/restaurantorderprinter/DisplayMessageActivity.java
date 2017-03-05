package de.httptandooripalace.restaurantorderprinter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String message = intent.getExtras().getString("apiData");
//        JSONObject jsonData;
//        try {
//            jsonData = new JSONObject(message);
//        }
//        catch(Exception ex) {
//            throw new IllegalArgumentException(ex.getMessage());
//        }

        TextView changedtv = (TextView) findViewById(R.id.dataview);
        changedtv.setMovementMethod(new ScrollingMovementMethod());
        changedtv.setText(message);

//        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
//        layout.addView(textView);
    }
}
