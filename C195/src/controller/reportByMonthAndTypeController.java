package controller;

import DBAcess.DBAppointments;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller displays appointments by type and month
 */
public class reportByMonthAndTypeController implements Initializable {
    public TableView monthTable;
    public TableColumn monthCol;
    public TableColumn appointmentType;
    public TableColumn numberCol;

    /**
     * populate table with appointments by month,type, and the count.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthTable.setItems(DBAppointments.getAppointmentsByMonth());
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        numberCol.setCellValueFactory(new PropertyValueFactory<>("count"));
    }
}
