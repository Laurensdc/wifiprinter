package helpers;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.httptandooripalace.restaurantorderprinter.OverviewActivity;
import de.httptandooripalace.restaurantorderprinter.R;
import entities.Bill;
import entities.Settings;

/**
 * Created by uiz on 27/04/2017.
 */

public class HistoryAdapter extends BaseAdapter{

    private Context context;
    private List<Bill> bills;

    public HistoryAdapter(Context c, List<Bill> bills) {
        context = c;
        this.bills = new ArrayList<>();
        this.bills = bills;

    }

    @Override
    public int getCount() {
        if(bills==null){return 0;}
        return bills.size();
    }

    @Override
    public Object getItem(int position) {
        return bills.get(position);
    }

    public int getBillId(int position){
        return bills.get(position).getId();
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
            convertView = inflater.inflate(R.layout.history_list_item, null);
        }

        TextView t1 = (TextView) convertView.findViewById(R.id.id);
        t1.setText(""+bill.getId());

        TextView t2 = (TextView) convertView.findViewById(R.id.date);
        t2.setText(bill.getDate().toString().substring(4,10));

        TextView t3 = (TextView) convertView.findViewById(R.id.table);
        t3.setText(""+ bill.getTableNr());

        TextView t4 = (TextView) convertView.findViewById(R.id.waiter);
        t4.setText(bill.getWaiter());

        TextView t5 = (TextView) convertView.findViewById(R.id.price);
        t5.setText(bill.getTotal_price_excl()+" €");

        return convertView;
    }




}
