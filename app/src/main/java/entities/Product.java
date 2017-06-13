package entities;

/**
 * Created by uizen on 15.03.2017.
 */

public class Product {
    private String name;
    private double price_excl;
    private double price_incl;
    private String category;
    private int count;
    private boolean drink;
    private String reference;
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product(int id, String name, double price_excl, double price_incl, String reference, String category, int count) {
        this.id = id;

        this.category = category;
        this.price_excl = price_excl;
        this.price_incl = price_incl;
        this.name = name;
        this.reference = reference;
        this.count = count;
        if( this.getName().endsWith("cl") || this.getName().endsWith(" l") || this.getName().endsWith("L)") || this.getName().endsWith("cl)") || this.getCategory().contains("Getränke")){
            this.drink = true;
        }else{
            this.drink = false;
        }
    }

//    public Product(String name, String category) {
//        this.name = name;
//        this.category = category;
//    }

    public boolean getDrink() { return this.drink; }
    public void setDrink(Boolean d) { this.drink= d; }

    public void increaseCount() { this.count = count++; }
    public void decreaseCount() { this.count = count--; }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
         return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getPrice_excl() {
        return price_excl;
    }

    public void setPrice_excl(double price_excl) {
        this.price_excl = price_excl;
    }

    public double getPrice_incl() {
        return price_incl;
    }

    public void setPrice_incl(double price_incl) {
        this.price_incl = price_incl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object obj) {
        Product p = (Product) obj;
        return p.getPrice_incl() == this.getPrice_incl()
                && p.getPrice_excl() == this.getPrice_excl()
                && p.getCategory().equals(this.getCategory())
                && p.getCount() == this.getCount()
                && p.getName().equals(this.getName());


    }


}
