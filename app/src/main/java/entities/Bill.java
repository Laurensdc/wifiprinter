package entities;

import java.util.Date;
import java.util.List;

/**
 * Created by CoredusK on 11-Apr-17.
 */

public class Bill {
    private List<Product> products;
    private boolean isOpen;
    private Date date;
    private String tableNr;
    private String waiter;
    private int id;
    private double total_price_excl;

    public Bill(List<Product> products, boolean isOpen, Date date, String tableNr, String waiter, int id, double total_price_excl) {
        this.products = products;
        this.isOpen = isOpen;
        this.date = date;
        this.tableNr = tableNr;
        this.waiter = waiter;
        this.id = id;
        this.total_price_excl = total_price_excl;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTableNr() {
        return tableNr;
    }

    public void setTableNr(String tableNr) {
        this.tableNr = tableNr;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal_price_excl() {
        return total_price_excl;
    }

    public void setTotal_price_excl(double total_price_excl) {
        this.total_price_excl = total_price_excl;
    }
}
