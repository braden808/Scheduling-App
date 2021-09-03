package main;

import controller.loginController;
import database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * This Class is used to start the connection and launch the application
 */
public class Main extends Application {

    Stage primaryStage;

    /**
     * Loads initial view which is the login view
     * @param window
     * @throws Exception
     */
    @Override
    public void start(Stage window) throws Exception{
        primaryStage = window;
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        root.setStyle("-fx-font-family: 'Times New Roman';");
        primaryStage.setTitle("Login");
        primaryStage.setOnCloseRequest(e -> closeWindow());
        primaryStage.setScene(new Scene(root, 400, 600));
        primaryStage.show();
    }


    /**
     * Starts the connection to the database, launches the start of the program, then closes the connection when ending the program
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
       // Locale.setDefault(new Locale("fr"));
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }


    /**
     * used for the login controller to see if the login window is still open
     */
    public void closeWindow(){
        loginController.active = false;
        primaryStage.close();
    }

}
