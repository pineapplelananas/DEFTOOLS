package model;

import java.util.Date;

public class OrderEntity {

    private int order_num;
    private int customer_id;
    private int product_id;
    private String description;
    private int quantity;
    private float purchase_cost;
    private float total_cost;
    private Date sales_date;
    private Date shipping_date;
    private String shipping_company;

    public OrderEntity(int order_num, int customer_id, int product_id, String description, int quantity, float purchase_cost, float total_cost, Date sales_date, Date shipping_date, String shipping_company) {
        this.order_num = order_num;
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.description = description;
        this.quantity = quantity;
        this.purchase_cost = purchase_cost;
        this.total_cost = total_cost;
        this.sales_date = sales_date;
        this.shipping_date = shipping_date;
        this.shipping_company = shipping_company;
    }

    public float getTotal_cost() {
        return total_cost;
    }

    public int getOrder_num() {
        return order_num;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPurchase_cost() {
        return purchase_cost;
    }

    public Date getSales_date() {
        return sales_date;
    }

    public Date getShipping_date() {
        return shipping_date;
    }

    public String getShipping_company() {
        return shipping_company;
    }

    public void setTotal_cost(float total_cost) {
        this.total_cost = total_cost;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPurchase_cost(float shipping_cost) {
        this.purchase_cost = shipping_cost;
    }

    public void setSales_date(Date sales_date) {
        this.sales_date = sales_date;
    }

    public void setShipping_date(Date shipping_date) {
        this.shipping_date = shipping_date;
    }

    public void setShipping_company(String shipping_company) {
        this.shipping_company = shipping_company;
    }

}
