/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.admin;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Accounts;
import mobile.banking.app.dbconnect.entityclass.Person_Info;
import mobile.banking.app.dbconnect.entityclass.Users;
import mobile.banking.app.utils.RandomPasswordGenerator;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Admin_AddCustomer_USERS_FXMLController implements Initializable {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirm_password;
    @FXML
    private Button create_user;

    private Accounts accountData;
    private Person_Info info;
    private Users credentials;
    @FXML
    private Label pageHeaderText;

    private void setCredentials() {
        this.credentials = new Users();
        this.credentials.setUser_acc_no(this.accountData.getAccNo());
        this.credentials.setUsername(this.username.getText().toUpperCase());
        this.credentials.setPassword(this.password.getText());
        this.credentials.setUser_id(this.info.getPid());
    }

    private void setInfo(Person_Info info) {
        this.info = info;
    }

    private void setAccountData(Accounts accountData) {
        this.accountData = accountData;
    }

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
    
    public void initFrameComponent(Users user, String title){
        this.username.setText(user.getUsername());
        this.password.setText(user.getPassword());
        this.confirm_password.setText(user.getPassword());
        this.pageHeaderText.setText(title);
    }

    public void initParam(Accounts acc, Person_Info data) {
        RandomPasswordGenerator rand_pass = new RandomPasswordGenerator();

        this.setAccountData(acc);
        this.setInfo(data);

        this.username.setText(this.info.getF_name().toUpperCase());
        // create instance of Random class 
        Random rand = new Random();

        // Generate random integers in range 0 to 7 
        int randSeq = rand.nextInt(8);

        switch (randSeq) {
            case 1:
                this.password.setText(rand_pass.generateCommonTextPassword());
                break;
            case 2:
                this.password.setText(rand_pass.generateCommonsLang3Password());
                break;
            case 3:
                this.password.setText(rand_pass.generatePassayPassword());
                break;
            case 4:
                this.password.setText(rand_pass.generateRandomAlphabet(randSeq, true));
                break;
            case 5:
                this.password.setText(rand_pass.generateRandomCharacters(randSeq));
                break;
            case 6:
                this.password.setText(rand_pass.generateRandomNumbers(randSeq));
                break;
            case 7:
                this.password.setText(rand_pass.generateRandomSpecialCharacters(randSeq));
                break;
            default:
                this.password.setText(rand_pass.generateSecureRandomPassword());
        }
        this.confirm_password.setText(this.password.getText());
        this.setCredentials();
    }

    @FXML
    private boolean validatePasswordMatch(ActionEvent event) {
        boolean flag = this.password.getText().equals(this.confirm_password.getText());
        if (!flag) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Mismatched Strings between Password & Confirmation."
                    + "\n\t<<" + this.password.getText() + ">> != <<" + this.confirm_password.getText() + ">>");
            alert.show();
            Platform.runLater(() -> {
                Admin_AddCustomer_USERS_FXMLController.this.password.requestFocus();
            });
        } else {
            Alert b = new Alert(Alert.AlertType.INFORMATION);
            b.setContentText("\t\tMatched(Password & Confirmation) ✅"
                    + "\n\t<<" + this.password.getText() + ">> == <<" + this.confirm_password.getText() + ">>");
            b.show();
        }
        return flag;
    }

    @FXML
    private void create_user_Handler(ActionEvent event) {
        if (!this.username.getText().isEmpty()) {
            if (this.password.getText().equals(this.confirm_password.getText())) {
                DbConnection db = new DbConnection();
                System.out.println(this.credentials.getPassword());
                System.out.println(this.credentials.getUser_acc_no());
                if (db.insertUser(this.credentials)) {
                    Alert b = new Alert(Alert.AlertType.INFORMATION);
                    b.setContentText("User <<Username:: " + this.credentials.getUsername() + ", Password:: " + this.credentials.getPassword() + ">>\n"
                            + "\t\tSuccessfully Created ✅");
                    b.show();

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
                } else {
                    Alert b = new Alert(Alert.AlertType.WARNING);
                    b.setContentText("CONNECTIVITY ERROR");
                    b.show();
                }
            } else {
                this.validatePasswordMatch(event);
            }
        } else {
            Alert b = new Alert(Alert.AlertType.INFORMATION);
            b.setContentText("Username Field is Empty");
            b.show();
        }
    }

}
