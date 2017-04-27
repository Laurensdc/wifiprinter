package helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.Bill;
import entities.Product;
import entities.Settings;

/**
 * Created by uizen on 3/6/2017.
 */

public class SharedPrefHelper {
    public static void deleteSharedPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("printItems");
        editor.apply();
    }


    public static List<Product> getPrintItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        String productstr = prefs.getString("printItems", null);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Product>>(){}.getType();
        List<Product> prods = (List<Product>) gson.fromJson(productstr, listType);
        return prods;
    }

    public static void setPrintItems(Context context, List<Product> products) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String prodsstr = gson.toJson(products);
        editor.putString("printItems", prodsstr);
        editor.commit();

    }

    public static void putString(Context context, String name, String value) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }


    public static String getString(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        return prefs.getString(name, null);

    }

    public static void saveSettings(Context context, Settings settings) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String prodsstr = gson.toJson(settings);
        editor.putString("settings", prodsstr);
        editor.apply();
    }

    public static Settings loadSettings(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        String settingStr = prefs.getString("settings", null);
        Gson gson = new Gson();
        Settings settings = gson.fromJson(settingStr, Settings.class);

        return settings;
    }
}
