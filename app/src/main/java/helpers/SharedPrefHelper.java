package helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entities.Product;

/**
 * Created by uizen on 3/6/2017.
 */

public class SharedPrefHelper {
//    public static boolean saveArrayList(ArrayList<String> arr, String arrayName, Context mContext) {
//        SharedPreferences prefs = mContext.getSharedPreferences("cart", 0);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(arrayName +"_size", arr.size());
//        for(int i=0;i<arr.size();i++)
//            editor.putString(arrayName + "_" + i, arr.get(i));
//        return editor.commit();
//    }
//
//    public static ArrayList<String> loadArrayList(String arrayName, Context mContext) {
//        SharedPreferences prefs = mContext.getSharedPreferences("cart", 0);
//        int size = prefs.getInt(arrayName + "_size", 0);
//        String array[] = new String[size];
//        for(int i=0;i<size;i++)
//            array[i] = prefs.getString(arrayName + "_" + i, null);
//        return new ArrayList<String>(Arrays.asList(array));
//    }
//
//    public static void deleteArrayList(String arrayName, Context mContext) {
//        SharedPreferences prefs = mContext.getSharedPreferences("cart", 0);
//        int size = prefs.getInt(arrayName + "_size", 0);
//        SharedPreferences.Editor editor = prefs.edit();
//        for(int i = 0; i < size; i ++) {
//            editor.remove(arrayName + "_" + i);
//        }
//        editor.remove(arrayName + "_size");
//        editor.commit();
//    }
//
    public static void deleteSharedPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("printItems");
        editor.commit();
    }


    public static List<Product> getPrintItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("cart", 0);
        SharedPreferences.Editor editor = prefs.edit();

        String productstr = prefs.getString("printItems", null);


        Gson gson = new Gson();
//        Product product = gson.fromJson(productstr, Product.class);

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

}
