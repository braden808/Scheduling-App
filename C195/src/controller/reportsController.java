package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controller is used to access all the reports
 */
public class reportsController implements Initializable {
    public Button byMonth;
    public Button goBack;
    public Button byContact;
    public Button byCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onMonth(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/reportByMonthAndType.fxml"));
            root.setStyle("-fx-font-family: 'Times New Roman';");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setX(200);
            stage.setY(50);
            Scene scene = new Scene(root, 700, 500);
            stage.setTitle("Report By Month and Type");
            stage.setScene(scene);
            stage.show();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }


    public void onBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        root.setStyle("-fx-font-family: 'Times New Roman';");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setX(50);
        stage.setY(50);
        Scene scene = new Scene(root, 1300, 800);
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();
    }

    public void onContact(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/reportByContact.fxml"));
            root.setStyle("-fx-font-family: 'Times New Roman';");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setX(200);
            stage.setY(50);
            Scene scene = new Scene(root, 1000, 750);
            stage.setTitle("Report By Contact");
            stage.setScene(scene);
            stage.show();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public void onCustomer(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/reportByCustomer.fxml"));
            root.setStyle("-fx-font-family: 'Times New Roman';");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setX(200);
            stage.setY(50);
            Scene scene = new Scene(root, 400, 500);
            stage.setTitle("Report for Amount of appointments per customer");
            stage.setScene(scene);
            stage.show();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
