package model;

/**
 * This model class creates the reportByMonthAndType object used in the month and type report
 */
public class reportByMonthAndType {
    private String month;
    private int count;
    private String type;

    public reportByMonthAndType(){

    }

    public String getType() {
        return type;
    }

    public String getMonth() {
        return month;
    }

    public int getCount() {
        return count;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setType(String type) {
        this.type = type;
    }
}
