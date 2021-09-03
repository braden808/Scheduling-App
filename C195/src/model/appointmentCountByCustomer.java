package model;

/**
 * This model class creates appointmentCountByCustomer object for the appointments per customer report
 */
public class appointmentCountByCustomer {
    private String customer;
    private int count;

    public appointmentCountByCustomer(String customer, int count){
        this.customer = customer;
        this.count = count;
    }

    public appointmentCountByCustomer() {

    }

    public String getCustomer() {
        return customer;
    }

    public int getCount() {
        return count;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
