/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.client;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Users;

/**
 * FXML Controller class
 *
 * @author User
 */
public class LoginController implements Initializable {

    @FXML
    private TextField Username;
    @FXML
    private PasswordField Password;
    @FXML
    private ToggleButton See_password;
    @FXML
    private Button Clear;
    @FXML
    private Button login;

    private Users user;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODOs
        this.Password.setDisable(false);
        this.user = null;
    }

    @FXML
    private void Username_HANDLER(ActionEvent event) throws IOException {
        this.login_HANDLER(event);
    }

    @FXML
    private void Password_HANDLER(ActionEvent event) throws IOException {
        this.login_HANDLER(event);
    }

    @FXML
    private void See_password_HANDLER(ActionEvent event) {

    }

    @FXML
    private void Clear_HANDLER(ActionEvent event) {
        this.Username.clear();
        this.Password.clear();
    }

    @FXML
    private void login_HANDLER(ActionEvent event) throws IOException {

        if (this.fireLogin(this.Username.getText(), this.Password.getText(), event)) {
            //Call Dashboard
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

            DbConnection db = new DbConnection();
            db.recordLog(java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now()), this.user.getUsername(), "LOGIN SUCCESSFUL -> ");
            this.nextPage();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            ButtonType ok = new ButtonType("OK");
            ButtonType exit = new ButtonType("Exit");
            alert.setTitle("Error-Alert");
            alert.setHeaderText("Login Error");
            alert.setContentText("Wrong username/password. Or Account Disabled.\n"
                    + "Please click 'OK' to retry or 'Exit' to quit.");
            alert.getButtonTypes().setAll(ok, exit);

            // action event
            alert.showAndWait().ifPresent((ButtonType response) -> {
                if (response == exit) {
                    Platform.exit();
                } else {
                    this.Clear_HANDLER(event);
                }
            });
        }
    }

    private boolean fireLogin(String u, String p, ActionEvent event) {
        if (u.equalsIgnoreCase("admin")) {
            this.adminPanel(event);
            return false;
        }
        // From Database Connectivity
        DbConnection dbc = new DbConnection();
        boolean flag = dbc.connect(u.toUpperCase(), p);
        this.user = dbc.getUser();
        return flag;
    }

    private void adminPanel(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../admin/AdminDashboard.fxml"));
            
            Parent root = loader.load();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void nextPage() {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard_FXML_interface.fxml"));
            Parent root = loader.load();
            //Get controller of scene2
            Dashboard_FXML_interfaceController dashboard = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            dashboard.setUser(this.user);
            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("MBS - Application");
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex);
            System.err.println("-->Error:: Call Dashboard");
        }
    }

}
