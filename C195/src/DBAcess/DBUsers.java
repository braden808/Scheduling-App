package DBAcess;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to access the users of the database
 */
public class DBUsers {


    /**
     * Retrieve all users from database
     * @return list of users
     */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> userList = FXCollections.observableArrayList();

        try {
            String sqlAll = "SELECT * FROM users";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlAll);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                User user = new User();
                user.setUserId(id);
                user.setUserName(userName);
                user.setPassword((password));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Retrieve a single user with the provided user ID
     * @param userId ID of selected user
     * @return the User object with the selected ID
     */
    public static User getUserById(int userId) {
        User user = new User();
        try {
            String sqlGet = "SELECT * FROM users WHERE User_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                user.setUserId(id);
                user.setUserName(userName);
                user.setPassword((password));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return user;
    }
}
