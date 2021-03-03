/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Accounts;
import mobile.banking.app.dbconnect.entityclass.Person_Info;
import mobile.banking.app.dbconnect.entityclass.Users;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Admin_DeleteCustomer_FXMLController implements Initializable {

    @FXML
    private Button deleteBtn;
    @FXML
    private TextField accNum;
    @FXML
    private Button returnBtn;
    
    private Accounts accData;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void deleteBtn_Handler(ActionEvent event) {
        DbConnection db = new DbConnection();
        this.accData = db.getAccount(this.accNum.getText());
        if (this.accData == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("The given Account Number IS NOT in Record ☢");
            a.show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do You want to Delete this Account <<AccNo:: " + this.accData.getAccNo()
                + ">> ?");
        alert.getButtonTypes().clear();
        ButtonType yes = new ButtonType("YES"), cancel = new ButtonType("Cancel");
        alert.getButtonTypes().addAll(yes, cancel);
        alert.showAndWait().ifPresent((ButtonType response) -> {
            if (response == yes) {
                Users user = db.getUser(this.accData.getAccNo().toString());
                Person_Info info = db.getPersonInfo(this.accData.getPID_Owner());
                if (db.deleteRecord(user) && db.deleteRecord(accData) && db.deleteRecord(info)) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Customer Record Deleted Successfully ✅ 😊");
                    a.show();
                }
            }
        });
        this.accNum.clear();
    }

    @FXML
    private void Delete_Customer_Handler(ActionEvent event) {
        this.deleteBtn_Handler(event);
    }

    @FXML
    private void returnBtn_Handler(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

}
