package controller;

import DBAcess.DBCustomer;
import DBAcess.DBUsers;
import database.DBConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import model.Customer;
import model.User;
import password.Password;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This controller is used to gather the Username and Password credentials
 */
public class loginController implements Initializable {

    public ImageView loginImage;
    public TextField loginUser;
    public PasswordField loginPass;
    public Button loginButton;
    public Label location;
    public Label time;
    public static boolean active;
    public Logger log = Logger.getLogger("login_activity.txt");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    Locale systemLocale = Locale.getDefault();
    ResourceBundle rb = ResourceBundle.getBundle("resourceBundles/Bundle",systemLocale);

    String locationF = rb.getString("Location");
    String timeF = rb.getString("Time");
    String usernameF = rb.getString("Username");
    String passwordF = rb.getString("Password");
    String logF = rb.getString("Log");
    String inF = rb.getString("in");
    String invalidF = rb.getString("Invalid");
    String orF = rb.getString("or");
    String pleaseF = rb.getString("Please");
    String insertF = rb.getString("Insert");
    String valid = rb.getString("valid");
    String userF = rb.getString("user");
    String credentialsF = rb.getString("credentials");
    String errorF = rb.getString("Error");

    /**
     * Instantiates the logger
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //The following four lines write the log text to a file. Otherwise it will print only to the console.
            FileHandler fh = new FileHandler("login_activity.txt", true);
            SimpleFormatter sf = new SimpleFormatter();
            fh.setFormatter(sf);
            log.addHandler(fh);

        } catch (IOException ex) {
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        loginUser.setPromptText(usernameF);
        loginPass.setPromptText(passwordF);
        loginButton.setText(logF+" "+inF);

        active = true;
        timeNow();
        location.setText(locationF + ": " + ZoneId.systemDefault());
    }

    /**
     * Receive time from System default locale
     */
    public void timeNow(){
        Thread thread = new Thread(() ->{
            while (active) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                LocalDateTime currentTime = LocalDateTime.now();
                String timeNow = dtf.format(currentTime);
                Platform.runLater(() -> {
                    time.setText(timeF+": " + timeNow);
                });
            }
        });
        thread.start();
    }

    /**
     * Used to verify user input with database User credentials.
     * @return boolean statement determining valid log-in credentials.
     */
    public boolean checkLogin(){
        String user, password;
        try {
            user = loginUser.getText();
            password = loginPass.getText();
            PreparedStatement ps = DBConnection.getConnection().prepareStatement("SELECT * FROM users WHERE User_Name = ? AND Password = ?");
            ps.setString(1, user);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User.setLoggedUser(user);
                User.setLoggedUserId(rs.getInt("User_ID"));
                active = false;
                return true;
            }
        } catch (Exception e) {
            System.out.println(errorF+" " + e.getMessage());
        }
        return false;
    }

    /**
     * Allows or denies access to the main screen based on checkLogin() return.
     * @param actionEvent the login button being clicked
     * @throws IOException
     */
    public void onLogin(ActionEvent actionEvent) throws IOException {
        if (checkLogin()) {
            log.severe("Successful Login by User: " +loginUser.getText()+ " on " + LocalDate.now()+" at "+ LocalTime.now() +".");
            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            root.setStyle("-fx-font-family: 'Times New Roman';");
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setX(50);
            stage.setY(50);
            Scene scene = new Scene(root, 1300, 800);
            stage.setTitle("Calendar");
            stage.setScene(scene);
            stage.show();
        } else {
            log.severe("Failed Login attempt with Username: " +loginUser.getText()+ " on " + LocalDate.now()+" at "+ LocalTime.now() +".");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().getScene().getRoot().setStyle("-fx-font-family: 'Times New Roman';");
            alert.setTitle(errorF);
            alert.setHeaderText(invalidF+" "+usernameF+" "+orF+" "+passwordF);
            alert.setContentText(pleaseF+" "+insertF+" "+valid+" "+credentialsF);
            alert.showAndWait();
        }
    }


}
