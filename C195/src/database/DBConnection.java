package database;
import password.Password;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * This class is used to start and end the connection to the database
 */
public class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String IpAdress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ07wuo";

    private static final String jdbcURL = protocol + vendorName +IpAdress + dbName;
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";

    private static final String username = "U07wuo";
    private static Connection conn = null;

    /**
     * Creates a new connection to my database
     * @return the creation of connection object
     */
    public static Connection startConnection(){
        try{
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, Password.getPassword());

            System.out.println("Successful Connection");
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * @return connection to my database
     */
    public static Connection getConnection(){
        return conn;
    }

    /**
     * Ends the connection to my database
     */
    public static void closeConnection(){
        try{
            conn.close();
        } catch (Exception e) {

        }
    }
}
