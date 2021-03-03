/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
public class Admin_ListofUsers_FXMLController implements Initializable {

    @FXML
    private TextField search_key_txt;
    @FXML
    private ListView<Users> listView;
    @FXML
    private TextArea selectedItem;
    @FXML
    private Button activateBtn;
    @FXML
    private Button disableBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button viewmoreBtn;
    @FXML
    private Button returnHomeBtn;

    private final DbConnection dbcon = new DbConnection();
    private final ObservableList List = FXCollections.observableArrayList();
    private Users currSelected;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.load_list();
    }

    private void load_list() {
        this.List.clear();
        this.List.addAll(this.dbcon.getAllUsers());
        this.listView.getItems().clear();
        this.listView.getItems().addAll(this.List);
        this.currSelected = null;
        this.selectedItem.setText("LIST REFRESHED...");
    }

    @FXML
    private void search_key_txt_searchResult_Handler(ActionEvent event) {
        String key = this.search_key_txt.getText();
        if (key.isBlank() || key.isEmpty()) {
            this.selectedItem.setText("List of Entries have been refreshed fully.");
            this.load_list();
            return;
        }
        this.selectedItem.setText("List of Entries refreshed for <<Key:: " + key
                + ">>");
        this.listView.getItems().clear();
        this.listView.getItems().add(this.dbcon.getUser(key));

    }

    @FXML
    private void activateBtn_Handler(ActionEvent event) {
        if (this.currSelected == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Activate <<User:: " + this.currSelected.getUsername()
                + ">>");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait().ifPresent((ButtonType response) -> {
            if (response == ButtonType.YES) {
                this.currSelected.setActive(true);
                this.dbcon.activateOrDisableUser(this.currSelected);
            }
        });
        this.load_list();
    }

    @FXML
    private void disableBtn_Handler(ActionEvent event) {
        if (this.currSelected == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Disactivate <<User:: " + this.currSelected.getUsername()
                + ">>");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.showAndWait().ifPresent((ButtonType response) -> {
            if (response == ButtonType.YES) {
                this.currSelected.setActive(false);
                this.dbcon.activateOrDisableUser(this.currSelected);
            }
        });
        this.load_list();
    }

    @FXML
    private void deleteBtn_Handler(ActionEvent event) {
        if (this.currSelected == null) {
            return;
        }
        Accounts accData = this.dbcon.getAccount(this.currSelected.getUser_acc_no().toString());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do You want to Delete this Account <<AccNo:: " + accData.getAccNo()
                + ">> ?");
        alert.getButtonTypes().clear();
        ButtonType yes = new ButtonType("YES"), cancel = new ButtonType("Cancel");
        alert.getButtonTypes().addAll(yes, cancel);
        alert.showAndWait().ifPresent((ButtonType response) -> {
            if (response == yes) {
                Users user = this.dbcon.getUser(accData.getAccNo().toString());
                Person_Info info = this.dbcon.getPersonInfo(accData.getPID_Owner());
                if (this.dbcon.deleteRecord(user) && this.dbcon.deleteRecord(accData) && this.dbcon.deleteRecord(info)) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("Customer Record Deleted Successfully ✅ 😊");
                    a.show();
                }
            }
        });
        this.load_list();
    }

    @FXML
    private void editBtn_Handler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Feature UnderDevelopment");
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
    }

    @FXML
    private void viewmoreBtn_handler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setContentText("Feature UnderDevelopment");
        alert.getButtonTypes().add(ButtonType.OK);
        alert.show();
    }

    @FXML
    private void returnHomeBtn_Handler(ActionEvent event) {
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

    @FXML
    private void listView_Handler(MouseEvent event) {
        Users item = this.listView.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.selectedItem.setText("Nothing Selected Yet!!!");
        } else {
            String txt = "";
//            txt += "LOG_ID::\t" + item.getLog_Id() + "\t";
            txt += "USERNAME::\t" + item.getUsername() + "\n";
            txt += "ACC-NO:: \t" + item.getUser_acc_no() + "\n";
            txt += "CREATION:: \t" + item.getCreation() + "\n";
            txt += "OWNER --> " + this.dbcon.getPersonInfo(item.getUser_id()).getF_name();
            txt += "\nACTIVATED == " + (item.isActive() ? "true" : "false");

            this.selectedItem.setText(txt);

            this.currSelected = new Users(item);
        }
    }

}
