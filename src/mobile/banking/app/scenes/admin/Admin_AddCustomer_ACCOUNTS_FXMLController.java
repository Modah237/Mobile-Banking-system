/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.admin;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Account_Type;
import mobile.banking.app.dbconnect.entityclass.Accounts;
import mobile.banking.app.dbconnect.entityclass.Person_Info;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Admin_AddCustomer_ACCOUNTS_FXMLController implements Initializable {

    @FXML
    private TextField account_name;
    @FXML
    private TextField card_number;
    @FXML
    private TextField card_pin;
    @FXML
    private Button create_account;
    @FXML
    private ComboBox<String> accTypesList;

    private Person_Info infoData;
    private Accounts accData;
    private DbConnection db;
    private List<Account_Type> types;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.accData = null;
    }

    private void loadBox() {
        this.db = new DbConnection();
        this.types = db.getAccountTypes();
        this.accTypesList.getItems().clear();
        this.types.forEach(type -> {
            this.accTypesList.getItems().add(type.getAcc_Type());
        });
        System.out.println(this.infoData.getDOB());
    }

    public void setPersonInfo(Person_Info data) {
        this.infoData = data;
        this.loadBox();
        System.out.println(this.infoData.getDOB());
    }

    @FXML
    private void create_account_Handler(ActionEvent event) {
        Alert alert;
        if (this.account_name.getText().isEmpty() || this.accData == null) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("First Select an Account Type to Generate the relevant account information for the given customer <<" + this.infoData.getF_name() + ">>");
            alert.show();
            return;
        }
        ButtonType ok = new ButtonType("Yes"), cancel = new ButtonType("Cancel");
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do You Want to create this Account <<AccNo:: " + this.account_name.getText() + ">> ?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ok, cancel);
        alert.showAndWait().ifPresent((ButtonType response) -> {
            if (response == ok) {
                if (this.db.insertPersonInfo(this.infoData)) {
                    int pid = this.db.getSeqPersonCURR();
                    System.out.println(pid);
                    this.accData.setPID_Owner(pid);
                    this.infoData.setPid(pid);

                    this.db.insertAccount(this.accData);

                    Alert b = new Alert(Alert.AlertType.INFORMATION);
                    b.setContentText("Account <<AccNo:: " + this.accData.getAccNo() + ">>"
                            + " Successfully Created ✅");
                    b.showAndWait();

                    //Pass a PersonInfo object to nextPage.
                    try {
                        Node source = (Node) event.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
                        
                        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("Admin_AddCustomer_USERS_FXML.fxml"));

                        Parent root = loader.load();
                        //Get controller of scene2
                        Admin_AddCustomer_USERS_FXMLController controller = loader.getController();
                        //Pass whatever data you want. You can have multiple method calls here
                        controller.initParam(this.accData, this.infoData);

                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("OBS | AdminPanel");
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
                        System.err.println(ex);
                    }
                }
            }
        });
    }

    @FXML
    private void accTypesList_Handler(ActionEvent event) {
        this.accData = new Accounts();
        String item = this.accTypesList.getSelectionModel().getSelectedItem();
        if (item == null || item.isEmpty() || item.isBlank()) {
            System.out.println("Nothing Selected");
        } else {
            this.types.forEach(type -> {
                if (type.getAcc_Type().equalsIgnoreCase(item)) {
                    this.accData.setAccType(type.getAT_Id());
                }
            });
        }
        this.account_name.setText(this.accData.generateAccountNumber().toString());
        this.card_number.setText(this.accData.generateCardNumber().toString());
        this.card_pin.setText(this.accData.generateCardPIN().toString());
        System.out.println(this.infoData.getDOB());
    }

}
