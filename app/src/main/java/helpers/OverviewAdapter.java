package helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.httptandooripalace.restaurantorderprinter.R;
import entities.Bill;

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
        if(bills==null){return 0;}
        return bills.size();
    }

    public Object getAdapter(){
        return this;
    }

    @Override
    public Object getItem(int position) {
        return bills.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Bill bill = bills.get(position);

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.overview_list_item, null);
        }

        TextView t1 = (TextView) convertView.findViewById(R.id.table_number);
        t1.setText(bill.getTableNr());

        TextView t2 = (TextView) convertView.findViewById(R.id.waiter_name);
        t2.setText(bill.getWaiter());

        TextView t3 = (TextView) convertView.findViewById(R.id.bill_nr);
        t3.setText(""+ bill.getId());
        t3.setTag(position);

        TextView t4 = (TextView) convertView.findViewById(R.id.hour);
        t4.setText(bill.getDate().toString().substring(10,19));

        Button b = (Button) convertView.findViewById(R.id.close_bill);
        b.setTag(position);

        return convertView;
    }




}
