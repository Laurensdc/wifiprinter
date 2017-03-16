package entities;

/**
 * Created by uizen on 15.03.2017.
 */

public class Product {
    private String name;
    private float price;
    private int id;
    private String category;

    public Product(int id, String name, float price, String category) {
        this.category = category;
        this.price = price;
        this.id = id;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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
