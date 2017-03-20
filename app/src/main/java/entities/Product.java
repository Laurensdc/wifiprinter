package entities;

/**
 * Created by uizen on 15.03.2017.
 */

public class Product {
    private String name;
    private float price_excl;
    private float price_incl;

    private int id;
    private String category;

    public Product(String name, float price_excl, float price_incl, String category) {
        this.category = category;
        this.price_excl = price_excl;
        this.price_incl = price_incl;
        this.name = name;
    }

    public Product(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getPrice_excl() {
        return price_excl;
    }

    public void setPrice_excl(float price_excl) {
        this.price_excl = price_excl;
    }

    public float getPrice_incl() {
        return price_incl;
    }

    public void setPrice_incl(float price_incl) {
        this.price_incl = price_incl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
