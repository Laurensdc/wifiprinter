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
            convertView = inflater.inflate(R.layout.overview_list_item, null);
        }

        TextView t1 = (TextView) convertView.findViewById(R.id.table_number);
        t1.setText(bill.getTableNr());

        TextView t2 = (TextView) convertView.findViewById(R.id.waiter_name);
        t2.setText(bill.getWaiter());

        TextView t3 = (TextView) convertView.findViewById(R.id.bill_nr);
        t3.setText(""+ bill.getId());

        TextView t4 = (TextView) convertView.findViewById(R.id.hour);
        t4.setText(bill.getDate().toString().substring(10,16));

        TextView t6 = (TextView) convertView.findViewById(R.id.total_price);
        t6.setText(bill.getTotal_price_excl()+" €");

      /*  TextView t5 = (TextView) convertView.findViewById(R.id.list_products);
        if(bill.getProducts().size() > 2 ) {
            t5.setText(bill.getProducts().get(0).getName() + ", " + bill.getProducts().get(1).getName() + ", " + bill.getProducts().get(2).getName() + " ...");
        }else if(bill.getProducts().size() > 1){
            t5.setText(bill.getProducts().get(0).getName() + ", " + bill.getProducts().get(1).getName() + " ...");
        }else if(bill.getProducts().size() > 0){
            t5.setText(bill.getProducts().get(0).getName() + " ..." );
        }else{
            t5.setText("pas de produits");
        }*/

        FloatingActionButton b1 =  (FloatingActionButton)convertView.findViewById(R.id.close_bill);
        b1.setTag(R.string.id_tag, bill.getId());
        b1.setTag(R.string.table_tag, bill.getTableNr());

        FloatingActionButton b2 =  (FloatingActionButton)convertView.findViewById(R.id.edit_bill);
        b2.setTag(R.string.id_tag, bill.getId());
        b2.setTag(R.string.table_tag, bill.getTableNr());

        FloatingActionButton b3 =  (FloatingActionButton)convertView.findViewById(R.id.print_bill);
        b3.setTag(R.string.id_tag, bill.getId());
        b3.setTag(R.string.table_tag, bill.getTableNr());

        return convertView;
    }




}
