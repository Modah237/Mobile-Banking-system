/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.client;

import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
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
public class Fund_Transfer_FXMLController implements Initializable {

    @FXML
    private Button Next_fund_t;
    @FXML
    private TextField account_name;
    @FXML
    private TextField amount;
    @FXML
    private TextField narration_text;
    @FXML
    private TextField sender_account;
    @FXML
    private TextField tel_number;

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
    private void Next_fund_t_HANDLER(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");
        alert.setTitle("TRANSACTION");
        alert.setHeaderText("Fund Transfer Confirmation");
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
            trans.setT0_ACC(Integer.parseInt(this.sender_account.getText()));
            trans.setNarration(this.narration_text.getText());
            trans.setTR_TYPE(Transactions.TRANSFER);
            trans.setAmount(Double.parseDouble(this.amount.getText()));
            // create instance of Random class 
            Random rand = new Random();
            // Generate random integers in range 0 to 99999 
            Integer ref_code = rand.nextInt(100000);
            trans.setRef_code(ref_code);
            trans.setRepicient_name(this.tel_number.getText());

            if (this.accData.getBalance() - trans.getAmount() < 0) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                ButtonType ok = new ButtonType("Ok");
                a.setHeaderText("TRANSACTION WARNING");
                a.setTitle("MINIMUM BALANCE ALERT");
                a.setContentText("The given Amount of this transaction is GREATER THAN your Account's Balance.\n"
                        + "REMARK: (Transaction.Amount > Accounts." + this.account_name.getText() + ".Balance)");
                a.getButtonTypes().setAll(ok);
                a.show();
                return;
            }

            DbConnection db = new DbConnection();
            this.flag = db.makeTransaction(trans, this.accData, db.getAccount(trans.getT0_ACC().toString()));
            System.out.print("Transaction Done Status:: ");
            System.out.println(this.flag);
            if (this.flag) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("TRANSACTION");
                alert.setTitle("SUCCESSFULL TRANSACTION ✅");
                alert.setContentText("The transfer transaction was successfully wired to its recipient. \n"
                        + "We Thank you for Bank with us. as-salāmu ʿalaykum, ٱلسَّلَامُ عَلَيْكُمْ ");
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
        this.account_name.setText(this.accData.getAccNo().toString());
        this.account_name.setDisable(true);
        DbConnection db = new DbConnection();
        db.recordLog(java.sql.Date.valueOf(LocalDate.now()), java.sql.Date.valueOf(LocalDate.now()), db.getUser(this.accData.getAccNo().toString()).getUsername(), "FUND TRANSFER -> ");
    }

    private void clearForm() {
        this.amount.clear();
        this.narration_text.clear();
        this.sender_account.clear();
        this.tel_number.clear();
    }

}
