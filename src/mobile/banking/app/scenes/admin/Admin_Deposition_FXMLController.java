/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.admin;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Accounts;
import mobile.banking.app.dbconnect.entityclass.Transactions;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Admin_Deposition_FXMLController implements Initializable {

    @FXML
    private TextField Account_number;
    @FXML
    private TextField Account_name;
    @FXML
    private TextField Amount;
    @FXML
    private Button Recharge;
    @FXML
    private Label info;

    private Accounts accData;
    private DbConnection db;
    private boolean flag;
    
    @FXML
    private CheckBox edit_AccName_CheckBox;
    @FXML
    private Button returnHome;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.db = new DbConnection();
    }

    @FXML
    private void Account_number_Handler(ActionEvent event) {
        this.accData = db.getAccount(this.Account_number.getText());
        this.Account_name.setText(db.getPersonInfo(this.accData.getPID_Owner()).getF_name());
        this.info.setText("--> Owner Mr. " + this.Account_name.getText()
                + "\n--> AccNo: " + this.accData.getAccNo()
                + "\n--> CardNo: " + this.accData.getCardNo()
                + "\n--> Balance: " + this.accData.getBalance());
        Platform.runLater(() -> {
            this.Amount.requestFocus();
        });
    }

    @FXML
    private void Amount_Handler(ActionEvent event) {
        this.Recharge_Handler(event);
    }

    private boolean makeDeposit() {
        Double amt = Double.parseDouble(this.Amount.getText());
        if (amt < 0) {
            return false;
        }
        Transactions trans = new Transactions();
        trans.setAmount(amt);
        trans.setFR_ACC(Integer.parseInt(this.Account_number.getText()));
        trans.setNarration("DEPOSIT TO MYSELF FROM " + this.Account_name.getText());
        trans.setRef_code(this.accData.generateCardPIN());
        trans.setRepicient_name(this.Account_name.getText());
        trans.setT0_ACC(null);
        trans.setTR_DATE(LocalDate.now().toString());
        trans.setTR_TYPE(Transactions.DEPOSIT);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do You want to proceed with this Transaction?");
        ButtonType yes = new ButtonType("Yes"), no = new ButtonType("No");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(yes, no);
        alert.showAndWait().ifPresent((ButtonType response) -> {
            if (response == yes) {
                Admin_Deposition_FXMLController.this.flag = Admin_Deposition_FXMLController.this.db.makeTransaction(trans, Admin_Deposition_FXMLController.this.accData, null);
            }
        });
        return this.flag;
    }

    @FXML
    private void Recharge_Handler(ActionEvent event) {
        Alert alert;
        if (this.makeDeposit()) {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("DEPOSIT TRANSACTION DONE SUCCESSFULLY ✅");
            alert.show();
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("ISSUE WITH DEPOSIT TRANSACTION ❌"
                    + "\nPlease try rechecking carefully the following field:: <<Account Number>> & <<Amount>> they might be some errors.");
            alert.show();
        }
    }

    @FXML
    private void checkBox_Handler(ActionEvent event) {
        if (this.edit_AccName_CheckBox.isSelected()) {
            this.Account_name.setEditable(true);
        } else {
            this.Account_name.setEditable(false);
        }
    }

    @FXML
    private void checkBox_Handler(MouseEvent event) {
    }

    private void clearForm(){
        this.Account_name.clear();
        this.Amount.clear();
        this.Account_number.clear();
        
    }

    @FXML
    private void returnHome_Handler(ActionEvent event) {
        //Goto AdminPanel.
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
