package helpers;

/**
 * Created by uizen on 3/2/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.httptandooripalace.restaurantorderprinter.ProductSelectionActivity;

/**
 * Created by Ravi Tamada on 01/09/16.
 * www.androidhive.info
 */
public class HttpHandler extends AsyncTask<String, JSONArray, JSONArray> {

    Context context;

    public HttpHandler(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected JSONArray doInBackground(String... urlString) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;
        try {
            url = new URL(urlString[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000 /* milliseconds */);
            urlConnection.setConnectTimeout(8000 /* milliseconds */);
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

            String temp, response = "";
            while ((temp = br.readLine()) != null) {
                response += temp;
            }
            br.close();

            return new JSONArray(response);

        } catch (Exception e) {
            Log.d("JSON ERROR", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                    Log.d("IOException thrown", ignored.getMessage());
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    protected void onPostExecute(JSONArray result) {

        //super.onPostExecute(result);
        SharedPreferences settings = context.getSharedPreferences("cart", 0);
        // We need an Editor object to make preference changes.
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("apiData", result.toString());
        editor.commit();

    }
}