/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.client;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Accounts;
import mobile.banking.app.dbconnect.entityclass.Transactions;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Bills_Pay_FXMLController implements Initializable {

    @FXML
    private Button next;
    @FXML
    private TextField Account_name;
    @FXML
    private TextField amount;
    @FXML
    private TextField reference_code;
    @FXML
    private TextField Student_name;

    private Accounts accData;

    private boolean flag;

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
        this.flag = false;
    }

    @FXML
    private void next_HANDLER(ActionEvent event) {
        DbConnection db = new DbConnection();
        db.recordLog(java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now()), db.getUser(this.Account_name.getText()).getUsername(), "BILL PAY -> ");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.setTitle("TRANSACTION");
        alert.setHeaderText("Bill's Payment Confirmation");
        alert.setContentText("Do you wish to process with this transaction?");
        alert.getButtonTypes().setAll(yes, no);

        // action event
        alert.showAndWait().ifPresent((ButtonType response) -> {
            this.flag = response == yes;
        });

        System.out.print("Transaction Confirmation:: ");
        System.out.println(this.flag);
        if (flag) {
            Transactions trans = new Transactions();
            trans.setFR_ACC(this.accData.getAccNo());
            trans.setNarration("FEES PAY");
            trans.setTR_TYPE(Transactions.PAY_BILL);
            trans.setAmount(Double.parseDouble(this.amount.getText()));
            trans.setRef_code(Integer.parseInt(this.reference_code.getText()));

            if (this.accData.getBalance() - trans.getAmount() < 0) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                ButtonType ok = new ButtonType("Ok");
                a.setHeaderText("TRANSACTION WARNING");
                a.setTitle("MINIMUM BALANCE ALERT");
                a.setContentText("The given Amount of this transaction is GREATER THAN your Account's Balance.\n"
                        + "REMARK: (Transaction.Amount > Accounts." + this.Account_name.getText() + ".Balance)");
                a.getButtonTypes().setAll(ok);
                a.show();
                return;
            }

            db = new DbConnection();
            this.flag = db.makeTransaction(trans, this.accData, null);
            System.out.print("Transaction Done Status:: ");
            System.out.println(this.flag);
            if (this.flag) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("TRANSACTION");
                alert.setHeaderText("SUCCESSFULL TRANSACTION ✅");
                alert.setContentText("The transfer transaction was successfully wired to its recipient. \n"
                        + "We Thank you for Bank with us. \n"
                        + "As-salāmu ʿalaykum, ٱلسَّلَامُ عَلَيْكُمْ ");
                alert.getButtonTypes().setAll(new ButtonType("OK"));
                alert.show();
                this.clearForm();
            }
        } else {
            this.clearForm();
        }
    }

    public void setAccount(Accounts acc) {
        this.accData = acc;
        this.Account_name.setText(this.accData.getAccNo().toString());
        this.Account_name.setDisable(true);
    }

    private void clearForm() {
        this.amount.clear();
        this.Student_name.clear();
        this.reference_code.clear();
    }
}
