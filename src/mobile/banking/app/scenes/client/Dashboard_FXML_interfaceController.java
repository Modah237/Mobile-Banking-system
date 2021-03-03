/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.client;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Users;

/**
 * FXML Controller class
 *
 * @author Modah
 */
public class Dashboard_FXML_interfaceController implements Initializable {

    @FXML
    private AnchorPane MINI_MENU;
    @FXML
    private Button Account_info;
    @FXML
    private Button Fund_transfer;
    @FXML
    private Button Bill_pay;
    @FXML
    private Button Log_out;
    @FXML
    private Label USER_name;
    @FXML
    private AnchorPane Stage_pane;

    private AnchorPane Account_info_page, Fund_transfer_page, Bill_pay_page;

    private Users usr;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.usr = null;
        try {
            // TODO
            this.Account_info_page = FXMLLoader.load(this.getClass().getResource("Account_info_FXML.fxml"));
            this.Fund_transfer_page = FXMLLoader.load(this.getClass().getResource("Fund_Transfer_FXML.fxml"));
            this.Bill_pay_page = FXMLLoader.load(this.getClass().getResource("Bills_Pay_FXML.fxml"));
            this.setPage(this.Account_info_page);
            Platform.runLater(() -> this.Account_info.requestFocus());
//            this.Account_info.requestFocus();
        } catch (IOException ex) {
            Logger.getLogger(Dashboard_FXML_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setPage(Node page) {
        this.Stage_pane.getChildren().clear();
        this.Stage_pane.getChildren().add((Node) page);

        FadeTransition trans = new FadeTransition(Duration.millis(1500));
        trans.setNode(page);
        trans.setFromValue(0.1);
        trans.setToValue(1);
        trans.setCycleCount(1);
        trans.setAutoReverse(false);
        trans.play();
    }

    @FXML
    private void Account_info_Handler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Account_info_FXML.fxml"));
            Parent root = loader.load();
            Account_info_FXMLController aif = loader.getController();
            aif.setUser(this.usr);
            this.setPage(root);
        } catch (IOException ex) {
            Logger.getLogger(Dashboard_FXML_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Fund_transfer_HANDLER(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Fund_Transfer_FXML.fxml"));
            Parent root = loader.load();
            Fund_Transfer_FXMLController ft = loader.getController();
            DbConnection db = new DbConnection();
            ft.setAccount(db.getAccount(this.usr.getUser_acc_no().toString()));
            this.setPage(root);
        } catch (IOException ex) {
            Logger.getLogger(Dashboard_FXML_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.setPage(this.Fund_transfer_page);
    }

    @FXML
    private void Bill_pay_HANDLER(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Bills_Pay_FXML.fxml"));
            Parent root = loader.load();
            Bills_Pay_FXMLController bp = loader.getController();
            DbConnection db = new DbConnection();
            bp.setAccount(db.getAccount(this.usr.getUser_acc_no().toString()));
            this.setPage(root);
        } catch (IOException ex) {
            Logger.getLogger(Dashboard_FXML_interfaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
//        this.setPage(this.Bill_pay_page);
    }

    @FXML
    private void log_out_HANDLER(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        // Buttons Yes && No
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");

        // create an alert
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("SIGN OUT ");
        a.setContentText("Do you want to sign out?");
        a.getButtonTypes().setAll(yes, no);

        // action event
        a.showAndWait().ifPresent((ButtonType response) -> {
            if (response == yes) {
                //////// GO TO LOGIN PAGE//////////////-=
                stage.close();
                this.goToLoginPage();
            }
        });
    }

    private void goToLoginPage() {
        try {
            //Load second scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            //Get controller of scene2
            LoginController dashboard = loader.getController();
            //Pass whatever data you want. You can have multiple method calls here
            //

            //Show scene 2 in new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("OBS - Application");
            stage.show();
            DbConnection db = new DbConnection();
        db.recordLog(java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now()), this.usr.getUsername(), "LOGOUT -> ");
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public void setUser(Users user) {
        this.usr = user;
        this.USER_name.setText(this.usr.getUsername());
        DbConnection db = new DbConnection();
        db.recordLog(java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now()), this.usr.getUsername(), "DASHBORAD -> ");
    }

}
