package helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by uizen on 3/6/2017.
 */

public class SharedPreferencesArrayHelper {
    public static boolean saveArrayList(ArrayList<String> arr, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("cart", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", arr.size());
        for(int i=0;i<arr.size();i++)
            editor.putString(arrayName + "_" + i, arr.get(i));
        return editor.commit();
    }

    public static String[] loadArray(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("cart", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = prefs.getString(arrayName + "_" + i, null);
        return array;
    }
}
