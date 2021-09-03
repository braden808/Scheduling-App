package controller;

import DBAcess.DBAppointments;
import DBAcess.DBContacts;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Contact;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller is used to get the schedule for each contact
 */
public class reportByContactController implements Initializable {
    public ComboBox contactSelect;
    public TableView appointmentTable;
    public TableColumn apptIDCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn customerIDCol;

    /**
     * populates combo box with all contacts
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactSelect.setItems(DBContacts.getAllContacts());
    }

    /**
     * populates appointments based on customer
     * @param actionEvent select a contact with combo box
     */
    public void onSelect(ActionEvent actionEvent) {
        Contact selectedContact = (Contact) contactSelect.getValue();
        appointmentTable.setItems(DBAppointments.getAppointmentsByContactId(selectedContact.getContactId()));
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }
}
