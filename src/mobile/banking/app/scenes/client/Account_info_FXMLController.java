/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.client;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Accounts;
import mobile.banking.app.dbconnect.entityclass.Users;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Account_info_FXMLController implements Initializable {

    @FXML
    private Label Username;
    @FXML
    private Label Amount;

    private Users user;

    private Accounts accData;

    @FXML
    private Label Account_number;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.user = null;
    }

    public void setUser(Users u) {
        this.user = u;
        this.Username.setText(this.user.getUsername());
        this.Account_number.setText("ACC-No:: " + this.user.getUser_acc_no().toString());
        DbConnection dbc = new DbConnection();
        this.accData = dbc.getAccount(this.user.getUser_acc_no().toString());
        this.Amount.setText(this.accData.getBalance().toString());
        dbc.recordLog(java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now()), this.user.getUsername(), "ACCOUNT INFO -> ");
    }

}
