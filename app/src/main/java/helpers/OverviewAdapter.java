package helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.httptandooripalace.restaurantorderprinter.R;
import entities.Bill;
import entities.Product;

/**
 * Created by uiz on 27/04/2017.
 */

public class OverviewAdapter extends BaseAdapter{

    private Context context;
    private List<Bill> bills;

    public OverviewAdapter(Context c, List<Bill> bills) {
        context = c;
        this.bills = new ArrayList<>();
        this.bills = bills;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Bill bill = bills.get(position);


        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.overview_list_item, null);
        }

            TextView t1 = (TextView) convertView.findViewById(R.id.table_number);
            t1.setText(bills.get(position).getTableNr());

            TextView t2 = (TextView) convertView.findViewById(R.id.waiter_name);
            t2.setText(bills.get(position).getWaiter());




        return convertView;
    }



}
