package DBAcess;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to manage the contacts in the database
 */
public class DBContacts {

    /**
     * Retrieves all contacts from the database
     * @return Observable list of contact objects
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try {
            String sqlCon = "SELECT * FROM contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlCon);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact contact = new Contact(id, contactName, email);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public static void insertContact(Contact contact) {
        try {
            String sqlIn = "INSERT INTO contacts (Contact_Name, Email) VALUES(?,?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlIn);
            ps.setString(1, contact.getContactName());
            ps.setString(2, contact.getEmail());
            ps.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void updateContact(Contact contact) {
        try {
            String sqlIn = "UPDATE contacts SET Contact_Name = ?, Email = ? " +
                    "WHERE Contact_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlIn);

            ps.setString(1, contact.getContactName());
            ps.setString(2, contact.getEmail());
            ps.setInt(3, contact.getContactId());
            ps.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteContact(Contact contact) {
        try {
            String sqlDlt = "DELETE FROM Contacts " +
                    "WHERE Contact_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlDlt);

            ps.setInt(1, contact.getContactId());

            ps.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Retrieves the contact with the selected contact ID
     * @param contactId selected customer ID
     * @return contact with the selected contact ID
     */
    public static Contact getContactById(int contactId) {
        Contact contact = new Contact();
        try {
            String sqlGet = "SELECT * FROM contacts " +
                    "WHERE Contact_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ps.setInt(1, contactId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                contact.setContactId(id);
                contact.setContactName(name);
                contact.setEmail(email);
            }


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return contact;
    }

}
