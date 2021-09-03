package DBAcess;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Contact;
import model.Customer;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to manage the customer portion of the database
 */
public class DBCustomer {
    /**
     * Add a new customer to the database
     * @param customer customer object that will be added to database
     */
    public static void insertCustomer(Customer customer){
        try{
            String sqlIn = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone ,Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                    "VALUES (?, ?, ?, ?,NOW(),?,NOW(),?,?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlIn);

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhoneNumber());
            ps.setString(5, customer.getCreatedBy());
            ps.setString(6, customer.getLastUpdateBy());
            ps.setInt(7,customer.getDivisionId());
            ps.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Retrieves all customers from the database
     * @return Observable list of customer objects
     */
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            String sqlGet = "SELECT * FROM customers, first_level_divisions WHERE customers.Division_ID = first_level_divisions.Division_ID"; //select statement
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ResultSet rs = ps.executeQuery(sqlGet);

            while (rs.next()) {
                int id = rs.getInt("Customer_Id");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                Customer c = new Customer(name,address,postalCode,phoneNumber);
                c.setCustomerId(id);
                c.setDivisionId(divisionId);

                customerList.add(c);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    /**
     * Updates the information for the selected customer
     * @param customer selected customer
     */
    public static void updateCustomer(Customer customer){
        try{
            String sqlIn = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlIn);

            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhoneNumber());
            ps.setString(5, User.getLoggedUser());
            ps.setInt(6, customer.getDivisionId());
            ps.setInt(7, customer.getCustomerId());
            ps.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Remove selected customer from the database
     * @param customer selected customer
     */
    public static void deleteCustomer(Customer customer){
        try {
            String sqlDlt = "DELETE FROM customers " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlDlt);

            ps.setInt(1, customer.getCustomerId());

            ps.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Retrieves the customer with the selected customer ID
     * @param customerId selected customer ID
     * @return customer with the selected customer ID
     */
    public static Customer getCustomerById(int customerId) {
        Customer customer = new Customer();
        try {
            String sqlGet = "SELECT * FROM customers " +
                    "WHERE Customer_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Customer_Id");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                customer.setCustomerName(name);
                customer.setAddress(address);
                customer.setPostalCode(postalCode);
                customer.setPhoneNumber(phoneNumber);
                customer.setCustomerId(id);
                customer.setDivisionId(divisionId);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return customer;
    }

}
