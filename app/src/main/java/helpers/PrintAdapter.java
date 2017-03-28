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
import entities.Product;

/**
 * Created by uizen on 3/27/2017.
 */

public class PrintAdapter extends BaseAdapter {

    private Context context;
    private List<Product> products;

    public PrintAdapter(Context c, List<Product> products) {
        context = c;
        this.products = new ArrayList<>();
        this.products = products;
    }

    @Override
    public int getCount() {
        if(products == null) return 0;
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Product product = products.get(position);

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.print_list_item, null);
        }

        final TextView tv = (TextView) convertView.findViewById(R.id.product_description);
        tv.setText(productText(product));

        final int pos = position;
        Button btnplus = (Button) convertView.findViewById(R.id.btnplus);
        btnplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product p = products.get(pos);
                p.increaseCount();
                products.set(pos, p);
                tv.setText(productText(p));
            }
        });

        Button btnminus = (Button) convertView.findViewById(R.id.btnminus);
        btnminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product p = products.get(pos);
                if(p.getCount() < 1) return;
                p.decreaseCount();
                products.set(pos, p);
                tv.setText(productText(p));
            }
        });

        return convertView;
    }

    private String productText(Product p) {
        return (p.getName() + " x " + p.getCount()
                + "\nPrice incl: €" + Rounder.round(p.getPrice_incl() * p.getCount())
                + "\nRef: " + p.getReference()
                + "\n\n");
    }

    private float totalPrice(List<Product> prodlist) {
        float total = 0;
        for(int i = 0; i < prodlist.size(); i++) {
            total += prodlist.get(i).getPrice_incl();
        }
        return total;
    }
}
