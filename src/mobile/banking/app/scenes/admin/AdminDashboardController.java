/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.admin;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import mobile.banking.app.MobileBankingApp;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AdminDashboardController implements Initializable {

    @FXML
    private Button addNewCustomer;
    @FXML
    private Button deposit;
    @FXML
    private Button deleteCustomer;
    @FXML
    private Button logReport;
    @FXML
    private Button viewUsers;
    @FXML
    private Button transactions;
    @FXML
    private MenuItem clientPage;

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
    private void addNewCustomer_Handler(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PersonInfo.fxml"));
            
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deposit_Handler(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_Deposition_FXML.fxml"));
            
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteCustomer_Handler(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_DeleteCustomer_FXML.fxml"));
            
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void logReport_Handler(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Log_Report_FXML.fxml"));
            
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void viewUsers_Handler(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_ListofUsers_FXML.fxml"));
            
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void transactions_Handler(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_TRANSACTIONS_FXML.fxml"));
            
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clientPage_handler(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../client/Login.fxml"));
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setScene(scene);
            primaryStage.setTitle("ONLINE BANKING SYSTEM");
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void aboutApp(ActionEvent event) {
        MobileBankingApp.aboutApp();
    }

}
