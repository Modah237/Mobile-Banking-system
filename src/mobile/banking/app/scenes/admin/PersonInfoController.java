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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mobile.banking.app.dbconnect.entityclass.Person_Info;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PersonInfoController implements Initializable {

    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private DatePicker DoB;
    @FXML
    private TextField place;
    @FXML
    private TextField country;
    @FXML
    private TextField state;
    @FXML
    private TextField email;
    @FXML
    private TextField phone;
    @FXML
    private TextArea address;
    @FXML
    private Button clearForm;
    @FXML
    private Button nextBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private TextField profession;

    private Person_Info person;
    @FXML
    private TextField organization;
    @FXML
    private MenuItem formFiller_Action;

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
    private void clearForm_Handler(ActionEvent event) {
        this.address.clear();
        this.country.clear();
        this.email.clear();
        this.fname.clear();
        this.lname.clear();
        this.phone.clear();
        this.place.clear();
        this.profession.clear();
        this.state.clear();
        this.DoB.setValue(null);
        this.organization.clear();
    }

    @FXML
    private void nextBtn_Handler(ActionEvent event) {
        if (this.zipData()) {
            //Pass a PersonInfo object to nextPage.
            try {
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin_AddCustomer_ACCOUNTS_FXML.fxml"));

                Parent root = loader.load();
                //Get controller of scene2
                Admin_AddCustomer_ACCOUNTS_FXMLController controller = loader.getController();
                //Pass whatever data you want. You can have multiple method calls here
                controller.setPersonInfo(this.person);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("OBS | AdminPanel");
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    private boolean zipData() {
        if (!this.isFormValidate()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Field <Date of Birth> is Empty... And/Or Other Required Input Fields are also Empty");
            alert.show();
            return false;
        }
        this.person = new Person_Info();
        this.person.setAddress(this.address.getText());
        this.person.setDOB(this.DoB.getValue().toString());
        this.person.setEmail(this.email.getText());
        this.person.setF_name(this.fname.getText());
        this.person.setL_name(this.lname.getText());
        this.person.setOrgamization(this.organization.getText());
        this.person.setPhone(this.phone.getText());
        this.person.setProfession(this.profession.getText());
        this.person.setCountry(this.country.getText());
        this.person.setState(this.state.getText());
        return true;
    }

    private boolean isFormValidate() {
        if (this.DoB.getValue() != null) {
            if (this.fname.getText().equalsIgnoreCase("")) {
                return false;
            } else {
                if (this.country.getText().equalsIgnoreCase("")) {
                    return false;
                } else {
                    return !this.phone.getText().equalsIgnoreCase("");
                }
            }
        }
        return false;
    }

    @FXML
    private void homeBtn_Handler(ActionEvent event) {
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
    private void formFiller_Action_Handler(ActionEvent event) {
        this.fname.setText("LAMENACE");
        this.lname.setText("TOURES");
        this.DoB.setValue(LocalDate.now());
        this.address.setText("iut dhaka");
        this.country.setText("cmr");
        this.email.setText("Aem@iut.edu");
        this.organization.setText("OIC");
        this.phone.setText("89843");
        this.place.setText("douala");
        this.profession.setText("engineer");
        this.state.setText("littoral");
    }

}
