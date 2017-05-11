package helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import de.httptandooripalace.restaurantorderprinter.R;
import entities.Product;

/**
 * Created by uizen on 14.03.2017.
 */

public class MainAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private LinkedHashMap<String, List<Product>> _listDataChild;

    private ArrayList<String> _originalListDataHeader; // header titles
    // child data in format of header title, child title
    private LinkedHashMap<String, List<Product>> _originalListDataChild;



    public MainAdapter(Context context, ArrayList<String> listDataHeader,
                       LinkedHashMap<String, List<Product>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._originalListDataChild = listChildData;
        this._originalListDataHeader = listDataHeader;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Product p = (Product) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.main_list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.prod_title);
        txtListChild.setText(p.getReference() + " - " + p.getName());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.main_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.cat_title);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }


    public void filter(String f) {
        _listDataHeader = _originalListDataHeader;
        _listDataChild = _originalListDataChild;


        if(f.equals("")) {
            notifyDataSetChanged();
            return;
        }

        Iterator<List<Product>> it = _listDataChild.values().iterator();

        _listDataHeader = new ArrayList<>();
        _listDataChild = new LinkedHashMap<>();


   //     Iterator<String> itCat = _listDataChild.keySet().iterator();
        //String s = null;
        while (it.hasNext())
        {
            List<Product> prodList = it.next();
            //String cat = itCat.next();
            for(int i = 0; i < prodList.size(); i++) {

                if(prodList.get(i).getReference().contains(f) || prodList.get(i).getName().contains(f)) {

                    //Log.d("DOFIEJOFIJ", prodList.get(i).getName());
                    String searchres = "Search result";
                    if(!_listDataHeader.contains(searchres)) {
                        _listDataHeader.add(searchres);
                    }

                    List<Product> filteredList = new ArrayList<>();
                    filteredList.add(prodList.get(i));
                    _listDataChild.put(searchres, filteredList);

                    notifyDataSetChanged();
                }
                else {
//                    prodList.remove(i);
//                    s = prodList.get(i).toString();

//                    String prodname = prodList.get(i).getName();
//                    this._listDataChild.remove(prodname);
//

                }
            }
        }


  /*          List<List<Product>> l = new ArrayList<List<Product>>(_listDataChild.values());
            Log.d("WHAT IS THIS: ", l.get(i).toString());

    _listDataChild.*/

////            _listDataChild.
//
//            if(_listDataChild.get("").)

//
//
//            for (int j = 0; j <  _listDataChild..size(); j++){
//                if(!this._listDataChild.get(j).contains(f)){
//                    String s = this._listDataHeader.get(j);
//                    this._listDataHeader.remove(s);
//                }
            //}

//        }
//        Log.d("TEST", _listDataChild.toString());
//
//        this._listDataChild.get("Beilagen").remove(1);// WORK !! Delete the second products of Beilagen category
//       // this._listDataChild.get("Tandoori Platte").removeAll(Collections.singleton("155 - Tandoori Platte(2)")); // App crash
//
//        String s = this._listDataHeader.get(0);
//        this._listDataHeader.remove(s); // WORK !! Delete the all category
//
//        Log.d("TEST", _listDataChild.toString());


    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
