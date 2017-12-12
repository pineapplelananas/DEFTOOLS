
package model;


public class ProductEntity {

    private int prod_id;
    private float purchase_cost;
    private String description;

    public ProductEntity(int prod_id, float purchase_cost, String description) {
        this.prod_id = prod_id;
        this.purchase_cost = purchase_cost;
        this.description = description;
    }

    public int getProd_id() {
        return prod_id;
    }

    public float getPurchase_cost() {
        return purchase_cost;
    }

    public String getDescription() {
        return description;
    }

    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    public void setPurchase_cost(float purchase_cost) {
        this.purchase_cost = purchase_cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
