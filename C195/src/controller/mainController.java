package controller;

import DBAcess.DBAppointments;
import DBAcess.DBCountries;
import DBAcess.DBCustomer;
import DBAcess.Validation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Appointment;
import model.Country;
import model.Customer;
import lambdaInterface.Runner;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * THIS CONTROLLER CONTAINS TWO LAMBDA EXPRESSIONS: This controller manages the main screen of the application.  The customers and appointments are viewed here.  Customers and appointments are also added, updated and deleted on this screen.
 */
public class mainController implements Initializable {
    public TableColumn customerId;
    public TableColumn customerName;
    public TableView mainCustomersTable;
    public Button mainCustomerAdd;
    public Button mainCustomerUpdate;
    public Button mainCustomerDelete;
    public AnchorPane mainCalendarView;
    public RadioButton mainWeek;
    public ComboBox<Customer> mainCustomerChoice;
    public RadioButton mainMonth;
    public Button mainAddApt;
    public Button mainUpdateApt;
    public Button mainDeleteApt;
    public Button mainReports;
    public Button allAppointments;
    private static Customer selectedCustomer;
    public ToggleGroup calType;
    public Appointment selectedAppointment;
    public Button monthlyCalendarNext;
    public Button monthlyCalendarPrevious;
    public TableColumn apptIDCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn customerIDCol;
    public Label monthLabel;
    public TableView appointmentTable;
    //Date Variables for Calendar
    private Locale locale = Locale.getDefault();
    private Calendar cal = new GregorianCalendar(locale);
    private DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);
    private String nameOfMonthDisplay = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
    private String nameOfDayDisplay = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
    private String nameOfYearDisplay = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
    private int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
    private int monthNumber = cal.get(Calendar.MONTH) + 1;
    private int yearNumber = cal.get(Calendar.YEAR);
    private LocalDateTime today = LocalDateTime.now();
    private String startRange;
    private String endRange;
    private static boolean firstLog = true;

    /**
     * LAMBDA used for the 15-minute appointment alert. Method also Loads items into all tables and ComboBoxes.  Also sends the fifteen minute appointment alert.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cal.setTime(new Date());
        getAppointments();
        monthLabel.setText(nameOfMonthDisplay);
        ObservableList<Customer> customers = DBCustomer.getAllCustomers();
        mainCustomersTable.setItems(customers);
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        mainCustomerChoice.setItems(customers);
        mainCustomerChoice.setVisibleRowCount(5);
        mainCustomerChoice.setPromptText("Customer Choice");

        try{
            loadMonthlyCal();
        } catch (IOException ioException){
        ioException.printStackTrace();
        }

        fifteenMinuteAlert.runIt();
    }

    /**
     * Brings user to add customer screen
     * @param actionEvent add customer button click
     * @throws IOException
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        root.setStyle("-fx-font-family: 'Times New Roman';");
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setX(200);
        stage.setY(50);
        Scene scene = new Scene(root, 350,350);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Brings user to update customer screen with the values from the selected customer.
     * @param actionEvent update customer button click
     * @throws IOException
     */
    public void onUpdateCustomer(ActionEvent actionEvent) throws IOException{
        Customer selected;
        //Create loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/updateCustomer.fxml"));
        Parent root = loader.load();
        root.setStyle("-fx-font-family: 'Times New Roman';");

        //transfer the data
        selected = (Customer) mainCustomersTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            updateCustomerController mCC = loader.getController();
            mCC.transfer(selected);
            //set the stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 350, 350);
            stage.setX(200);
            stage.setY(50);
            stage.setTitle("Update Customer");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.setHeaderText("No Selection Error");
            alert.setContentText("Please select a Customer");
            alert.showAndWait();
        }
    }

    /**
     * Delete's customer from the customer table of the database.
     * @param actionEvent delete button click
     */
    public void onDeleteCustomer(ActionEvent actionEvent) {
        Customer customer = (Customer) mainCustomersTable.getSelectionModel().getSelectedItem();
        if (customer != null) {
            if (DBAppointments.getNumberOfAppointments(customer.getCustomerId()) == 0) {
                String name = customer.getCustomerName();
                DBCustomer.deleteCustomer(customer);
                mainCustomersTable.setItems(DBCustomer.getAllCustomers());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.setHeaderText("Appointment Deleted");
                alert.setContentText("The customer " + name + " has been deleted.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.setHeaderText("Illegal Delete");
                alert.setContentText("The deletion of every appointment that a customer is involved in is required.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.setHeaderText("No Selection Error");
            alert.setContentText("Please select a Customer");
            alert.showAndWait();
        }

    }

    /**
     * Loads the appointments by month and populates the appointment table.
     * @throws IOException
     */
    public void loadMonthlyCal() throws IOException{
        nameOfMonthDisplay = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
        appointmentTable.setItems(DBAppointments.getAppointmentsByMonth(nameOfMonthDisplay,yearNumber));
        monthLabel.setText(nameOfMonthDisplay + " " + yearNumber);
        selectedCustomer = mainCustomerChoice.getSelectionModel().getSelectedItem();
        getAppointments();
    }

    /**
     * Loads the appointments by week and populates the appointment table.
     */
    public void loadWeeklyCalendar(){
        selectedCustomer = mainCustomerChoice.getSelectionModel().getSelectedItem();
        getThisWeeksDays();
        getAppointments();
    }


    public void onWeek(ActionEvent actionEvent) throws IOException{
        loadWeeklyCalendar();
    }

    public void onMonth(ActionEvent actionEvent) throws IOException{
        loadMonthlyCal();
    }

    /**
     * Bring the user to add appointment screen.
     * @param actionEvent add appointment button clicked
     * @throws IOException
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        root.setStyle("-fx-font-family: 'Times New Roman';");
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        stage.setX(200);
        stage.setY(50);
        Scene scene = new Scene(root, 450,550);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Bring the user to update appointment screen with the transfer of selected appointment data.
     * @param actionEvent upadte appointment button clicked
     * @throws IOException
     */
    public void onUpdateAppointment(ActionEvent actionEvent) throws IOException{
        //Create loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/updateAppointment.fxml"));
        Parent root = loader.load();
        root.setStyle("-fx-font-family: 'Times New Roman';");

        //transfer the data
        selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            updateAppointmentController mCC = loader.getController();
            mCC.transfer(selectedAppointment);
            //set the stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 450, 550);
            stage.setX(200);
            stage.setY(50);
            stage.setTitle("Update Customer");
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.setHeaderText("No Selection Error");
            alert.setContentText("Please select an Appointment");
            alert.showAndWait();
        }
    }


    /**
     * Deletes selected appointment
     * @param actionEvent delete appointment button clicked
     */
    public void onDeleteAppointment(ActionEvent actionEvent) {
        selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if(selectedAppointment != null) {
            int id = selectedAppointment.getAppointmentId();
            if (selectedAppointment != null) {
                DBAppointments.deleteAppointment(selectedAppointment);
                getAppointments();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.setHeaderText("Appointment Deleted");
                alert.setContentText("Appointment with Appointment_ID number: " + id + " with the type: " + selectedAppointment.getType() + " has been deleted.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.setHeaderText("No Selection Error");
                alert.setContentText("Please select an Appointment");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.setHeaderText("No Selection Error");
            alert.setContentText("Please select an Appointment");
            alert.showAndWait();
        }
    }

    /**
     * Opens a side window of the selectable reports
     * @param actionEvent reports button clicked
     * @throws IOException
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
            root.setStyle("-fx-font-family: 'Times New Roman';");
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.setScene(new Scene(root));
            newWindow.show();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    /**
     * populates the appointments table with all appointments by week or month.
     * @param actionEvent show all appointments button clicked
     */
    public void onAllAppt(ActionEvent actionEvent) {
        mainCustomerChoice.getSelectionModel().clearSelection();
    }

    /**
     * Displays the appointments for the selected customer
     * @param actionEvent customer selected in combo box
     * @throws IOException
     */
    public void onCustomerSelect(ActionEvent actionEvent) throws IOException {
        if (mainMonth.isSelected()){
            loadMonthlyCal();
        } else {
            loadWeeklyCalendar();
        }

    }

    /**
     * Increases month or week and LAMBDA used to update the calendar
     * @param actionEvent next button clicked
     */
    public void onNext(ActionEvent actionEvent) {
        if (mainMonth.isSelected()){
            cal.add(Calendar.MONTH,1);
            updateCalendar.runIt();
            getAppointments();
            monthLabel.setText(nameOfMonthDisplay + " " + yearNumber);
        } else {
            cal.add(Calendar.DAY_OF_WEEK,7);
            updateCalendar.runIt();
            getThisWeeksDays();
            getAppointments();
        }
    }
    /**
     * Decreases month or week and LAMBDA used to update the calendar
     * @param actionEvent previous button clicked
     */
    public void onPrevious(ActionEvent actionEvent) {
        if (mainMonth.isSelected()){
            cal.add(Calendar.MONTH,-1);
            updateCalendar.runIt();
            getAppointments();
            monthLabel.setText(nameOfMonthDisplay + " " + yearNumber);
        } else {
            cal.add(Calendar.DAY_OF_WEEK,-7);
            updateCalendar.runIt();
            getThisWeeksDays();
            getAppointments();
        }
    }

    /**
     * Gathers the appointment data by the selected customer and if it is by week or month.
     */
    public void getAppointments(){
        if (mainMonth.isSelected()) {
            if(selectedCustomer == null){
                appointmentTable.setItems(DBAppointments.getAppointmentsByMonth(nameOfMonthDisplay,yearNumber));
            } else {
                appointmentTable.setItems(DBAppointments.getAppointmentsByCustomerIdAndMonth(selectedCustomer.getCustomerId(), nameOfMonthDisplay));
            }
        } else {
            if(selectedCustomer == null){
                appointmentTable.setItems(DBAppointments.getAllAppointmentsByWeek(startRange, endRange));
            } else {
                appointmentTable.setItems(DBAppointments.getAppointmentsByWeekAndCustomer(selectedCustomer.getCustomerId(), startRange,endRange));
            }
        }
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    /**
     * Set's the days of the week for the appointment by week section and LAMBDA used to update the calendar
     */
    public void getThisWeeksDays() {
        // set calendar to the current date
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        updateCalendar.runIt();
        //get start lDT or LD
        int startWeekMonth = monthNumber;
        int startWeekDay = dayOfMonth;
        int startWeekYear = yearNumber;
        LocalDateTime startWeek = LocalDateTime.of(startWeekYear,startWeekMonth,startWeekDay,0,0);
        String firstDay = nameOfMonthDisplay+ " " + dayOfMonth+ " " + yearNumber;
        cal.add(Calendar.DAY_OF_WEEK, 6);
        updateCalendar.runIt();
        int endWeekMonth = monthNumber;
        int endWeekDay = dayOfMonth;
        int endWeekYear = yearNumber;
        //get end LDT to get a range
        LocalDateTime endWeek = LocalDateTime.of(endWeekYear,endWeekMonth,endWeekDay,23,59,59,99);
        startRange = startWeek.toString();
        endRange = endWeek.toString();
        String lastDay = nameOfMonthDisplay+ " " + dayOfMonth + " " + yearNumber;
        // set calendars dOW field to the first dOW (last sunday)
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        monthLabel.setText(firstDay + " - " + lastDay);
    }

    /**
     * LAMBDA: updates instance variables for the calendar
     */
    Runner updateCalendar;
    {
        updateCalendar = () -> {

            df = DateFormat.getDateInstance(DateFormat.FULL, locale);
            nameOfMonthDisplay = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
            nameOfDayDisplay = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
            nameOfYearDisplay = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
            dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            monthNumber = cal.get(Calendar.MONTH) + 1;
            yearNumber = cal.get(Calendar.YEAR);

        };
    }

    /**
     * LAMBDA: Provides an alert if there is or isn't an appointment within the next 15 minutes of local time
     */
    Runner fifteenMinuteAlert;
    {
        fifteenMinuteAlert = () -> {
            Appointment a = Validation.appointmentInFifteenMinutes();
            if (firstLog && a != null) {
                int difference = a.getStart().getMinute() - LocalDateTime.now().getMinute();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.setHeaderText("Appointment with the Appointment_ID: " + a.getAppointmentId() + " starts in " + (difference) + " minutes.  Appointment starts at " + a.getStart().toLocalTime() + " on " + a.getStart().toLocalDate());
                alert.showAndWait();
            } else if (firstLog && a == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.setHeaderText("No appointments within 15 minutes");
                alert.showAndWait();
            }
            firstLog = false;
        };
    }


}