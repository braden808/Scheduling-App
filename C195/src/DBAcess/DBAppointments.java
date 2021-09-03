package DBAcess;
import database.DBConnection;
import model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This class is used to manage the appointments of the database
 */
public class DBAppointments {
    /**
     * Retrieves all appointments from the database
     * @return Observable list of appointment objects
     */
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, appointments.Create_Date, appointments.Created_By, appointments.Last_Update, appointments.Last_Updated_By, appointments.Customer_ID, appointments.User_ID, appointments.Contact_ID, Customer_Name, User_Name, Contact_Name " +
                    "FROM appointments, customers, users, contacts " +
                    "WHERE appointments.Customer_ID = customers.Customer_ID AND appointments.User_ID = users.User_ID AND appointments.Contact_ID = contacts.Contact_ID;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp apptStart = rs.getTimestamp("Start");
                LocalDateTime start = apptStart.toLocalDateTime();
                Timestamp apptEnd = rs.getTimestamp("End");
                LocalDateTime end = apptEnd.toLocalDateTime();
                Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = apptCreateDate.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = apptLastUpdate.toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(title, description, location, type);
                c.setAppointmentId(appointmentId);
                c.setStart(start);
                c.setEnd(end);
                c.setCreateDate(createDate);
                c.setLastUpdate(lastUpdate);
                c.setCustomerId(customerID);
                c.setUserId(userID);
                c.setContactId(contactID);
                c.setCreatedBy(createdBy);
                c.setLastUpdateBy(lastUpdateBy);
                c.setContact(DBContacts.getContactById(contactID));
                apptList.add(c);
            }

        }catch (SQLException throwable){
            throwable.printStackTrace();
        }

        return apptList;
    }

    /**
     * Add a new appointment to the database
     * @param appointment appointment object that will be added to database
     */
    public static void createAppointment(Appointment appointment) {
        try {
            LocalDateTime localStart = appointment.getStart().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime localEnd = appointment.getEnd().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String sqlIn = "INSERT INTO appointments VALUES(NULL,?,?,?,?,?,?, NOW(), ?, NOW(), ?, ?, ?, ?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlIn);

            ps.setString(1,appointment.getTitle());
            ps.setString(2,appointment.getDescription());
            ps.setString(3,appointment.getLocation());
            ps.setString(4,appointment.getType());
            ps.setTimestamp(5,Timestamp.valueOf(localStart));
            ps.setTimestamp(6,Timestamp.valueOf(localEnd));
            ps.setString(7,appointment.getCreatedBy());
            ps.setString(8,appointment.getLastUpdateBy());
            ps.setInt(9,appointment.getCustomerId());
            ps.setInt(10,appointment.getUserId());
            ps.setInt(11,appointment.getContactId());
            ps.execute();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Updates the information for the selected appointment
     * @param appointment selected appointment
     */
    public static void updateAppointment(Appointment appointment) {
        try {
            LocalDateTime localStart = appointment.getStart().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime localEnd = appointment.getEnd().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String sqlUp = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?,Start = ?, End = ?, Create_Date = NOW(), Created_By = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlUp);

            ps.setString(1,appointment.getTitle());
            ps.setString(2,appointment.getDescription());
            ps.setString(3,appointment.getLocation());
            ps.setString(4,appointment.getType());
            ps.setTimestamp(5,Timestamp.valueOf(localStart));
            ps.setTimestamp(6,Timestamp.valueOf(localEnd));
            ps.setString(7,appointment.getCreatedBy());
            ps.setString(8,appointment.getLastUpdateBy());
            ps.setInt(9,appointment.getCustomerId());
            ps.setInt(10,appointment.getUserId());
            ps.setInt(11,appointment.getContactId());
            ps.setInt(12,appointment.getAppointmentId());
            ps.execute();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Remove selected appointment from the database
     * @param appointment selected appointment
     */
    public static void deleteAppointment(Appointment appointment){
        try {
            String sqlDlt = "DELETE FROM appointments " +
                    "WHERE Appointment_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlDlt);

            ps.setInt(1, appointment.getAppointmentId());

            ps.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Retrieves all appointments with the given customer ID and in the given month
     * @param customerId selected customer ID
     * @param month the month the appointment is in
     * @return all appointments with the given customer ID and in the given month
     */
    public static ObservableList<Appointment> getAppointmentsByCustomerIdAndMonth(int customerId, String month) {
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        try {
            String sqlGet = "SELECT * FROM appointments " +
                    "WHERE Customer_ID = ? AND monthname(start) = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ps.setInt(1, customerId);
            ps.setString(2, month);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp apptStart = rs.getTimestamp("Start");
                LocalDateTime start = apptStart.toLocalDateTime();
                Timestamp apptEnd = rs.getTimestamp("End");
                LocalDateTime end = apptEnd.toLocalDateTime();
                Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = apptCreateDate.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = apptLastUpdate.toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(title, description, location, type);
                c.setAppointmentId(appointmentId);
                c.setStart(start);
                c.setEnd(end);
                c.setCreateDate(createDate);
                c.setLastUpdate(lastUpdate);
                c.setCustomerId(customerID);
                c.setUserId(userID);
                c.setContactId(contactID);
                c.setCreatedBy(createdBy);
                c.setContact(DBContacts.getContactById(contactID));
                c.setLastUpdateBy(lastUpdateBy);
                apptList.add(c);
            }
        }catch(SQLException exception){
            exception.printStackTrace();
        }
        return apptList;
    }

    /**
     * Retrieves all appointments except for the appointment with the given ID
     * @param id given appointment ID
     * @return all appointments except for the appointment with the given ID
     */
    public static ObservableList<Appointment> getAppointmentsExcept(int id) {
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        try {
            String sqlGet = "SELECT * FROM appointments WHERE Appointment_ID != ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp apptStart = rs.getTimestamp("Start");
                LocalDateTime start = apptStart.toLocalDateTime();
                Timestamp apptEnd = rs.getTimestamp("End");
                LocalDateTime end = apptEnd.toLocalDateTime();
                Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = apptCreateDate.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = apptLastUpdate.toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(title, description, location, type);
                c.setAppointmentId(appointmentId);
                c.setStart(start);
                c.setEnd(end);
                c.setCreateDate(createDate);
                c.setLastUpdate(lastUpdate);
                c.setCustomerId(customerID);
                c.setUserId(userID);
                c.setContactId(contactID);
                c.setCreatedBy(createdBy);
                c.setContact(DBContacts.getContactById(contactID));
                c.setLastUpdateBy(lastUpdateBy);
                apptList.add(c);
            }
        }catch(SQLException exception){
            exception.printStackTrace();
        }
        return apptList;
    }


    /**
     * Retrieves all appointments with the given month and year
     * @param month selected year
     * @param year the month the appointment is in
     * @return all appointments with the given month and year
     */
    public static ObservableList<Appointment> getAppointmentsByMonth(String month, int year) {
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM appointments WHERE (monthname(Start) = ?) AND (Year(Start) = ?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1,month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp apptStart = rs.getTimestamp("Start");
                LocalDateTime start = apptStart.toLocalDateTime();
                Timestamp apptEnd = rs.getTimestamp("End");
                LocalDateTime end = apptEnd.toLocalDateTime();
                Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = apptCreateDate.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = apptLastUpdate.toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(title, description, location, type);
                c.setAppointmentId(appointmentId);
                c.setStart(start);
                c.setEnd(end);
                c.setCreateDate(createDate);
                c.setLastUpdate(lastUpdate);
                c.setCustomerId(customerID);
                c.setUserId(userID);
                c.setContactId(contactID);
                c.setCreatedBy(createdBy);
                c.setLastUpdateBy(lastUpdateBy);
                c.setContact(DBContacts.getContactById(contactID));
                apptList.add(c);
            }

        }catch (SQLException throwable){
            throwable.printStackTrace();
        }

        return apptList;
    }

    /**
     * Retrieves all appointments with the given start and end of the week
     * @param weekStart start of selected week
     * @param weekEnd end of selected week
     * @return all appointments with the given start and end of the week
     */
    public static ObservableList<Appointment> getAllAppointmentsByWeek(String weekStart, String weekEnd) {
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM appointments WHERE Start Between ? AND ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1,weekStart);
            ps.setString(2,weekEnd);


            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp apptStart = rs.getTimestamp("Start");
                LocalDateTime start = apptStart.toLocalDateTime();
                Timestamp apptEnd = rs.getTimestamp("End");
                LocalDateTime end = apptEnd.toLocalDateTime();
                Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = apptCreateDate.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = apptLastUpdate.toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(title, description, location, type);
                c.setAppointmentId(appointmentId);
                c.setStart(start);
                c.setEnd(end);
                c.setCreateDate(createDate);
                c.setLastUpdate(lastUpdate);
                c.setCustomerId(customerID);
                c.setUserId(userID);
                c.setContactId(contactID);
                c.setCreatedBy(createdBy);
                c.setLastUpdateBy(lastUpdateBy);
                c.setContact(DBContacts.getContactById(contactID));
                apptList.add(c);
            }

        }catch (SQLException throwable){
            throwable.printStackTrace();
        }

        return apptList;
    }

    /**
     * Retrieves all appointments with the given start and end of the week as well as customer
     * @param customer selected customer ID
     * @param weekStart start of selected week
     * @param weekEnd end of selected week
     * @return all appointments with the given start and end of the week as well as customer
     */
    public static ObservableList<Appointment> getAppointmentsByWeekAndCustomer(int customer, String weekStart, String weekEnd) {
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM appointments WHERE (Start Between ? AND ?) AND (Customer_ID = ?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1,weekStart);
            ps.setString(2,weekEnd);
            ps.setInt(3,customer);


            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp apptStart = rs.getTimestamp("Start");
                LocalDateTime start = apptStart.toLocalDateTime();
                Timestamp apptEnd = rs.getTimestamp("End");
                LocalDateTime end = apptEnd.toLocalDateTime();
                Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = apptCreateDate.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = apptLastUpdate.toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(title, description, location, type);
                c.setAppointmentId(appointmentId);
                c.setStart(start);
                c.setEnd(end);
                c.setCreateDate(createDate);
                c.setLastUpdate(lastUpdate);
                c.setCustomerId(customerID);
                c.setUserId(userID);
                c.setContactId(contactID);
                c.setCreatedBy(createdBy);
                c.setLastUpdateBy(lastUpdateBy);
                c.setContact(DBContacts.getContactById(contactID));
                apptList.add(c);
            }

        }catch (SQLException throwable){
            throwable.printStackTrace();
        }

        return apptList;
    }


    /**
     * Retrieves the amount of appointments by customer ID
     * @param id customer id
     * @return int of number of appointments
     */
    public static int getNumberOfAppointments(int id){
        int value = 0;
        try{
            String sql = "SELECT * FROM appointments WHERE (Customer_ID = ?)";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                value++;
            }

        }catch (SQLException throwable){
            throwable.printStackTrace();
        }

        return value;

    }

    /**
     * Retrieves the count of appointments by type and month
     * @return all reportByMonthAndType objects by the appointment month
     */
    public static ObservableList<reportByMonthAndType> getAppointmentsByMonth() {
        ObservableList<reportByMonthAndType> apptList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT DATE_FORMAT(Start, '%M') AS Month, Type , COUNT(Start) AS Count FROM appointments GROUP BY Month, Type";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String month = rs.getString("month");
                String type = rs.getString("type");
                int count = rs.getInt("count");

                reportByMonthAndType rbm = new reportByMonthAndType();
                rbm.setMonth(month);
                rbm.setType(type);
                rbm.setCount(count);

                apptList.add(rbm);
            }

        }catch (SQLException throwable){
            throwable.printStackTrace();
        }

        return apptList;
    }

    /**
     * Retrieves the amount of appointments by contact ID
     * @param contactId contact id
     * @return all appointments by contract ID
     */
    public static ObservableList<Appointment> getAppointmentsByContactId(int contactId) {
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        try {
            String sqlGet = "SELECT * FROM appointments " +
                    "WHERE Contact_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGet);
            ps.setInt(1, contactId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp apptStart = rs.getTimestamp("Start");
                LocalDateTime start = apptStart.toLocalDateTime();
                Timestamp apptEnd = rs.getTimestamp("End");
                LocalDateTime end = apptEnd.toLocalDateTime();
                Timestamp apptCreateDate = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = apptCreateDate.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp apptLastUpdate = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = apptLastUpdate.toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Appointment c = new Appointment(title, description, location, type);
                c.setAppointmentId(appointmentId);
                c.setStart(start);
                c.setEnd(end);
                c.setCreateDate(createDate);
                c.setLastUpdate(lastUpdate);
                c.setCustomerId(customerID);
                c.setUserId(userID);
                c.setContactId(contactID);
                c.setCreatedBy(createdBy);
                c.setContact(DBContacts.getContactById(contactID));
                c.setLastUpdateBy(lastUpdateBy);
                apptList.add(c);
            }
        }catch(SQLException exception){
            exception.printStackTrace();
        }
        return apptList;
    }

    /**
     * Retrieves the number of appointments for each customer
     * @return all appointmentCountByCustomer
     */
    public static ObservableList<appointmentCountByCustomer> getAppointmentCountByCustomerId() {
        ObservableList<appointmentCountByCustomer> apptList = FXCollections.observableArrayList();

        try{
            String sql = " SELECT Customer_Name as Customer, COUNT(appointments.Customer_ID) as count FROM appointments, customers WHERE appointments.Customer_ID = customers.Customer_ID GROUP BY Customer";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String customer = rs.getString("customer");
                int count = rs.getInt("count");

                appointmentCountByCustomer ac = new appointmentCountByCustomer();
                ac.setCustomer(customer);
                ac.setCount(count);
                apptList.add(ac);
            }

        }catch (SQLException throwable){
            throwable.printStackTrace();
        }

        return apptList;
    }

}