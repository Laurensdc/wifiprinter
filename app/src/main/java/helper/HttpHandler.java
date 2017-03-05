package helper;

/**
 * Created by uizen on 3/2/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import de.httptandooripalace.restaurantorderprinter.DisplayMessageActivity;

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


        Intent intent = new Intent(context, DisplayMessageActivity.class);

        intent.putExtra("apiData", result.toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}