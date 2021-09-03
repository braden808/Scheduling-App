package controller;

import DBAcess.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * This controller is used to update an appointment in the database
 */
public class updateAppointmentController implements Initializable {
    public Button submitButton;
    public Button cancelButton;
    public TextField titleField;
    public DatePicker datePick;
    public ComboBox startTime;
    public ComboBox endTime;
    public TextArea descriptionArea;
    public TextField locationField;
    public ComboBox contactSelect;
    public ComboBox appointmentSelect;
    public ComboBox customerSelect;
    public ComboBox userSelect;

    private Appointment selectedAppointment;
    private final DateTimeFormatter time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
    private ObservableList<String> types = FXCollections.observableArrayList();
    //Make these eastern standard time to local time
    //8am to 10pm
    private LocalTime startEst = LocalTime.of(8,0);
    private LocalTime endEst = LocalTime.of(20,0);
    ZoneId estZoneID = ZoneId.of("America/New_York");
    LocalDate estDate = LocalDate.now();
    LocalTime estTime = LocalTime.now();
    ZonedDateTime estZDTStart = ZonedDateTime.of(estDate, startEst, estZoneID);
    ZonedDateTime estZDTEnd = ZonedDateTime.of(estDate, endEst, estZoneID);
    ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
    ZonedDateTime estToLocalStart = estZDTStart.withZoneSameInstant(localZoneId);
    ZonedDateTime estToLocalEnd = estZDTEnd.withZoneSameInstant(localZoneId);
    LocalTime s = LocalTime.of(estToLocalStart.getHour(),0);
    LocalTime e = LocalTime.of(estToLocalEnd.getHour(),0);
    LocalDateTime sLDT = estToLocalStart.toLocalDateTime();
    LocalDateTime eLDT = estToLocalEnd.toLocalDateTime();


    private boolean completeFields;
    private boolean properDateTime;

    /**
     * Populates the start and end times
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        while (s.isBefore(e.plusSeconds(1))){
            startTime.getItems().add(s);
            s = s.plusMinutes(30);
            if (s.getHour() == 20 && s.getMinute() > 1){
                break;
            }
            endTime.getItems().add(s);

        }

        //add appointment types to the list
        customerSelect.setItems(DBCustomer.getAllCustomers());
        contactSelect.setItems(DBContacts.getAllContacts());
        userSelect.setItems(DBUsers.getAllUsers());
        types.addAll("Meeting", "Phone call", "Video conference");
        appointmentSelect.setItems(types);

        /**
         * LAMBDA: Returns to the main menu and cancels all actions
         * @param set action event
         */
        cancelButton.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
                root.setStyle("-fx-font-family: 'Times New Roman';");
                Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                stage.setX(50);
                stage.setY(50);
                Scene scene = new Scene(root, 1300, 800);
                stage.setTitle("Calendar");
                stage.setScene(scene);
                stage.show();
            } catch (IOException exception){
                exception.printStackTrace();
            }
        });
        /**
         * LAMBDA: Updates selected appointment with user input, adds it to the database, then returns to main screen
         * @param e set action event
         */
        submitButton.setOnAction(e -> {
            completeFields = false;
            properDateTime = false;
            String title = titleField.getText();
            String description = descriptionArea.getText();
            Customer customer = (Customer)customerSelect.getValue();
            String type = (String) appointmentSelect.getValue();
            String locationAppointment = locationField.getText();
            Contact contact = (Contact) contactSelect.getValue();
            LocalDate date = datePick.getValue();
            LocalTime startT = (LocalTime) startTime.getValue();
            LocalTime endT = (LocalTime) endTime.getValue();
            User u = (User) userSelect.getValue();


//            System.out.println();
//            System.out.println("title = "+title);
//            System.out.println("des = "+description);
//            System.out.println("customer = "+customer);
//            System.out.println("type = "+type);
//            System.out.println("location = "+locationAppointment);
//            System.out.println("date = "+date);
//            System.out.println("start = "+startT);
//            System.out.println("end = "+endT);

            //completeFields check
            if (title.isEmpty() || description.isEmpty() || u == null || customer == null || type == null || locationAppointment.isEmpty() || date == null || startT == null || endT == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Empty Fields");
                alert.setContentText("Please fill every field");
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.showAndWait();
            } else {
                completeFields = true;
            }

            //properTime check
            try {

                LocalDate today = LocalDateTime.now().toLocalDate();
                int compareDate = date.compareTo(today);
                LocalDateTime startFinal = LocalDateTime.of(date, startT);
                LocalDateTime endFinal = LocalDateTime.of(date, endT);
                LocalDateTime todayLDT = LocalDateTime.now();
                int compareTime = startFinal.compareTo(endFinal);
                int compareToToday = startFinal.compareTo(LocalDateTime.now());

//            System.out.println("Today = " + today);
//            System.out.println("compare Date = " + compareDate);
//            System.out.println("TodayLDT = " + todatLDT);
//                System.out.println("startFinal = " + startFinal);
//                System.out.println("endFinal = " + endFinal);
//            System.out.println("compareTime = " + compareTime);

                if (endT.equals(startT)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Invalid Start and End Time");
                    alert.setContentText("Appointment Start and End Time must not be the same value");
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                    alert.showAndWait();
                } else if (compareDate < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Invalid Date Selection");
                    alert.setContentText("Please select a future Date");
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                    alert.showAndWait();
                } else if (compareTime > 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Invalid time Selection");
                    alert.setContentText("Please select an end time later than the start time.");
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                    alert.showAndWait();
                } else if (compareToToday < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Invalid time Selection");
                    alert.setContentText("Please select a future Time");
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                    alert.showAndWait();
                }else if (Validation.appointmentExistsUpdate(selectedAppointment.getAppointmentId(), startFinal, endFinal)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Overlapping appointments");
                    alert.setContentText("The time and date selected is overlapping an existing appointment.");
                    alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                    alert.showAndWait();
                } else {
                    properDateTime = true;
                }

                if (completeFields && properDateTime) {
                    selectedAppointment.setUserId(u.getUserId());
                    selectedAppointment.setTitle(title);
                    selectedAppointment.setDescription(description);
                    selectedAppointment.setLocation(locationAppointment);
                    selectedAppointment.setType(type);
                    selectedAppointment.setStart(startFinal);
                    selectedAppointment.setEnd(endFinal);
                    selectedAppointment.setCustomerId(customer.getCustomerId());
                    selectedAppointment.setContact(contact);
                    selectedAppointment.setContactId(contact.getContactId());
                    DBAppointments.updateAppointment(selectedAppointment);
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
                        root.setStyle("-fx-font-family: 'Times New Roman';");
                        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        stage.setX(50);
                        stage.setY(50);
                        Scene scene = new Scene(root, 1300, 800);
                        stage.setTitle("Calendar");
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }catch (NullPointerException exception){
                System.out.println("Null Pointer Catch");
            }
        });
    }

    /**
     * Transfers the data from the selected Appointment from the main screen
     * @param selectedAppt the selected Appointment from the main screen
     */
    public void transfer(Appointment selectedAppt){
        selectedAppointment = selectedAppt;
        titleField.setText(selectedAppointment.getTitle());
        String description = selectedAppointment.getDescription();
        String location = selectedAppointment.getLocation();
        String type = selectedAppointment.getType();
        descriptionArea.setText(description);
        locationField.setText(location);
        appointmentSelect.setValue(type);
        userSelect.setValue(DBUsers.getUserById(selectedAppt.getUserId()));
        LocalDateTime start = selectedAppointment.getStart();
        LocalDateTime end = selectedAppointment.getEnd();
        LocalDate sD = LocalDate.of(start.getYear(),start.getMonth(), start.getDayOfMonth());
        LocalTime  sT = LocalTime.of(start.getHour(), start.getMinute());
        LocalTime eT = LocalTime.of(end.getHour(), end.getMinute());
        datePick.setValue(sD);
        startTime.setValue(sT);
        endTime.setValue(eT);
        contactSelect.setValue(DBContacts.getContactById(selectedAppointment.getContactId()));
        customerSelect.setValue(DBCustomer.getCustomerById(selectedAppointment.getCustomerId()));



    }


}
