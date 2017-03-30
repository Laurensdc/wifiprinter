package de.httptandooripalace.restaurantorderprinter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import entities.Settings;
import helpers.SharedPrefHelper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Settings settings = SharedPrefHelper.loadSettings(getApplicationContext());
        if(settings != null) {
            ((TextView) findViewById(R.id.printer_ip)).setText(settings.getPrinterIp());
            ((TextView) findViewById(R.id.name_line_1)).setText(settings.getNameLine1());
            ((TextView) findViewById(R.id.name_line_2)).setText(settings.getNameLine2());
            ((TextView) findViewById(R.id.addr_line_1)).setText(settings.getAddrLine1());
            ((TextView) findViewById(R.id.addr_line_2)).setText(settings.getAddrLine2());
            ((TextView) findViewById(R.id.tel_line)).setText(settings.getTelLine());
            ((TextView) findViewById(R.id.tax_nr)).setText(settings.getTaxLine());
            ((TextView) findViewById(R.id.extra_line)).setText(settings.getExtraLine());
            ((TextView) findViewById(R.id.waitername)).setText(settings.getWaiter());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getText(R.string.save_settings));
        builder.setMessage(getText(R.string.want_to_save));

        builder.setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                entities.Settings settings = new Settings(
                        ((TextView) findViewById(R.id.printer_ip)).getText().toString(),
                        ((TextView) findViewById(R.id.name_line_1)).getText().toString(),
                        ((TextView) findViewById(R.id.name_line_2)).getText().toString(),
                        ((TextView) findViewById(R.id.addr_line_1)).getText().toString(),
                        ((TextView) findViewById(R.id.addr_line_2)).getText().toString(),
                        ((TextView) findViewById(R.id.tel_line)).getText().toString(),
                        ((TextView) findViewById(R.id.tax_nr)).getText().toString(),
                        ((TextView) findViewById(R.id.extra_line)).getText().toString(),
                        ((TextView) findViewById(R.id.waitername)).getText().toString()
                );

                SharedPrefHelper.saveSettings(getApplicationContext(), settings);
                dialog.dismiss();
                onBackPressed();
            }
        });

        builder.setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton(getText(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }



}
