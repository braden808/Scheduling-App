package controller;

import DBAcess.DBCountries;
import DBAcess.DBCustomer;
import DBAcess.DBFirstLevelDivision;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller is used to update a customer in the database
 */
public class updateCustomerController implements Initializable {
    public Label nameLabel;
    public Label phoneLabel;
    public Label addressLabel;
    public Label divisionLabel;
    public Label countryLabel;
    public Label postalLabel;
    public TextField postalField;
    public TextField nameField;
    public TextField phoneField;
    public TextField addressField;
    public Button cancelButton;
    public Button submitButton;
    public ComboBox countrySelection;
    public ComboBox divisionSelection;
    Customer selectedCustomer;
    Country country;
    FirstLevelDivision fLD;
    /**
     * populates the countries and the divisions into combo boxes
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        countrySelection.setItems(DBCountries.getAllCountries());


        if (country != null) {
            divisionSelection.setItems(DBFirstLevelDivision.getByCountryId(country.getCountryId()));
        } else {
            divisionSelection.setItems(DBFirstLevelDivision.getAllDivisions());
        }
    }

    /**
     * @param name user input for customer name
     * @param phone user input for customer phone number
     * @param address user input for customer address
     * @param postal user input for customer postal code
     * @param fLDSelect user input for customer first level division
     * @return
     */
    public boolean fieldCheck(String name,String phone, String address, String postal, FirstLevelDivision fLDSelect){
        if (name == null || phone == null || address == null || postal == null || fLDSelect == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill every field");
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Creates new customer object and stores it into the database
     * Check done of field and phone number
     * @param actionEvent submit button clicked
     */
    public void onSubmit(ActionEvent actionEvent) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String postal = postalField.getText();
        FirstLevelDivision fLDSelect = (FirstLevelDivision)divisionSelection.getValue();

        boolean fieldCheck = fieldCheck(name, phone, address, postal, fLDSelect);
        boolean digitCheck = false;

        //phoneCheck
        if (fieldCheck == true){
            if (phone.length() < 10) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Phone Number");
                alert.setContentText("Phone number must have at least 10 digits");
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.showAndWait();
            } else if (phone.matches("[0-9-]+")) {
                digitCheck = true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Phone Number");
                alert.setContentText("Phone number contains non-digit characters (A-Z, !@#$%*, etc.)\nOnly Numbers and Dashes \"-\" allowed");
                alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
                alert.showAndWait();
            }
        }
        //Add Customer to Database
        if (fieldCheck && digitCheck) {
            selectedCustomer.setCustomerName(name);
            selectedCustomer.setAddress(address);
            selectedCustomer.setPostalCode(postal);
            selectedCustomer.setPhoneNumber(phone);
            selectedCustomer.setDivisionId(fLDSelect.getDivisionId());
            DBCustomer.updateCustomer(selectedCustomer);

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
                root.setStyle("-fx-font-family: 'Times New Roman';");
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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

    }

    /**
     * Returns back to main screen and all actions are canceled
     * @param actionEvent cancel button clicked
     */
    public void onCancel(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            root.setStyle("-fx-font-family: 'Times New Roman';");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setX(50);
            stage.setY(50);
            Scene scene = new Scene(root, 1300, 800);
            stage.setTitle("Calendar");
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void onCountrySelect(ActionEvent actionEvent) {
        Country country = (Country) countrySelection.getValue();
        if (country != null) {
            divisionSelection.setValue(null);
            divisionSelection.setItems(DBFirstLevelDivision.getByCountryId(country.getCountryId()));
        }
    }

    /**
     * Transfers the data from the selected customer from the main screen
     * @param selected the selected customer from the main screen
     */
    public void transfer(Customer selected) {
        selectedCustomer = selected;
        nameField.setText(selectedCustomer.getCustomerName());
        addressField.setText(selectedCustomer.getAddress());
        postalField.setText(selectedCustomer.getPostalCode());
        phoneField.setText(selectedCustomer.getPhoneNumber());
        fLD = DBFirstLevelDivision.getDivisionById(selectedCustomer.getDivisionId());
        country = DBCountries.getCountryById(fLD.getCountryId());
        countrySelection.setValue(country);
        divisionSelection.setItems(DBFirstLevelDivision.getByCountryId(country.getCountryId()));
        divisionSelection.setValue(fLD);


    }



}




