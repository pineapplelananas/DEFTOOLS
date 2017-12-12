package model;

public class Revenue {

    public Revenue() {
    }

    String product_code;
    double revenues;

    public double getRevenues() {
        return revenues;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setRevenues(double revenues) {
        this.revenues = revenues;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public Revenue(String product_code, float revenues) {

        this.product_code = product_code;
        this.revenues = revenues;
    }

}
