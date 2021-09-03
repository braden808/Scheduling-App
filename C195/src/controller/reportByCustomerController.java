package controller;

import DBAcess.DBAppointments;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller is used to display the amount of appointments per customer
 */
public class reportByCustomerController implements Initializable {
    public TableView apptTable;
    public TableColumn customerCol;
    public TableColumn numberValue;

    /**
     * populates table with amount if appointments per customer
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apptTable.setItems(DBAppointments.getAppointmentCountByCustomerId());
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        numberValue.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
}
