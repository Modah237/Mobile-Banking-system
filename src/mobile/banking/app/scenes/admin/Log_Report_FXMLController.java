/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.scenes.admin;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import mobile.banking.app.dbconnect.DbConnection;
import mobile.banking.app.dbconnect.entityclass.Log_Report;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mobile.banking.app.MobileBankingApp;

/**
 * FXML Controller class
 *
 * @author User
 */
public class Log_Report_FXMLController implements Initializable {

    @FXML
    private MenuBar MenuBar;
    @FXML
    private ListView<Log_Report> ListView;
    @FXML
    private TextArea Item_selected;
    @FXML
    private Button Return;

    @FXML
    private MenuItem export_session;
    @FXML
    private MenuItem import_session;
    @FXML
    private MenuItem exit_app;
    @FXML
    private MenuItem delete_item;
    @FXML
    private MenuItem about_log_report;

    private final ObservableList List = FXCollections.observableArrayList();
    private final DbConnection dbcon = new DbConnection();
    private final Desktop desktop = Desktop.getDesktop();
    private final FileChooser fileChooser = new FileChooser();
    private final String f_ext = ".csv";

    @FXML
    private TextField userID_txtSearch;
    @FXML
    private Button refreshBtn;

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
        this.List.addAll(this.dbcon.getAllLogReport());
        this.ListView.getItems().clear();
        this.ListView.getItems().addAll(this.List);
    }
    
    private void load_list(List e) {
        this.ListView.getItems().clear();
        this.ListView.getItems().addAll(e);
    }

    @FXML
    private void export_session_handler(ActionEvent event) {
        ButtonType save = new ButtonType("Save");
        ButtonType cancel = new ButtonType("Cancel");

        // create an alert
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Export Session");
        a.setContentText("Do you want to save the current to a [.csv] file?");
        a.getButtonTypes().setAll(save, cancel);

        // action event
        a.showAndWait().ifPresent((ButtonType response) -> {
            if (response == save) {
                //////// GO TO LOGIN PAGE//////////////
                System.out.println("Saving File...");
//                String path = MobileBankingApp.class.getResource("").getPath().concat("report/");
//                System.out.println(path);
                String path = "./logs/";
                this.saveListViewItems(path);
            }
        });
    }

    private void createFileChooser(Stage stage) throws IOException {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            this.Item_selected.clear();
            this.Item_selected.setText("List Loaded from <<" + file.getName()
                    + ">>");
            if(file.getName().contains("log_report")){
                this.loadCSV(file);
            } else{
                this.openFile(file);
                this.Item_selected.setText(this.Item_selected.getText() + "\nError:: Wrong File, Not Compatible with data.");
            }
        }
    }

    private void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    Log_Report_FXMLController.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    private void saveListViewItems(String path) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        String date = dtf.format(now);
        File file = new File(path + "log_report_" + date + this.f_ext);
        this.saveCSV(file);
    }

    private void saveCSV(File filePath) {
        System.out.println(filePath.getParent());
        if (new File(filePath.getParent()).mkdirs()) {
            System.out.println("Directory is created!");
        } else {
            System.out.println("Failed to create directory!");
        }
        try {
            FileWriter csvWriter;
            csvWriter = new FileWriter(filePath);
            List<Log_Report> list = this.ListView.getItems();
            for (Log_Report log : list) {
                csvWriter.append(log.getLog_Id());
                csvWriter.append(",");
                csvWriter.append(log.getUser_id());
                csvWriter.append(",");
                csvWriter.append(log.getLog_in());
                csvWriter.append(",");
                csvWriter.append(log.getLog_out());
                csvWriter.append(",");
                csvWriter.append(log.getReport());
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println("File Saved...`");
            this.Item_selected.setText("File <<" + filePath.getName()
                    + ">> Saved...`");
        } catch (IOException ex) {
            Logger.getLogger(Log_Report_FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCSV(File filePath) throws IOException {
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            List<Log_Report> listItem = new ArrayList<>();
            String row;
            Log_Report item;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                // do something with the data
                item = new Log_Report();
                item.setLog_Id(data[0]);
                item.setUser_id(data[1]);
                item.setLog_in(data[2]);
                item.setLog_out(data[3]);
                item.setReport(data[4]);
                listItem.add(item);
            }
            csvReader.close();
            this.load_list(listItem);
        }
    }

    @FXML
    private void import_session_handler(ActionEvent event) {
        try {
            this.createFileChooser(new Stage());
        } catch (IOException ex) {
            Logger.getLogger(Log_Report_FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void exit_app_handler(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void delete_item_handler(ActionEvent event) {
        Log_Report item = this.ListView.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.Item_selected.setText("Nothing Selected Yet!!!"
                    + "\nHence No Deletion can be perform");
        } else {
            Alert al;
            if (this.dbcon.deleteRecord(item)) {
                // create an alert
                al = new Alert(Alert.AlertType.INFORMATION);
                al.setContentText("Record Successfully Deleted");
                al.show();
                this.ListView.getItems().clear();
                this.load_list();
            } else {
                al = new Alert(Alert.AlertType.WARNING);
                al.setContentText("Warning: while record deletion, a bug appeared in the DbConfig");
                al.show();
            }
        }
    }

    @FXML
    private void about_log_report_handler(ActionEvent event) {
        MobileBankingApp.aboutApp();
    }

    @FXML
    private void ListView_Handler_OnMouseClicked(MouseEvent event) {
        Log_Report item = this.ListView.getSelectionModel().getSelectedItem();
        if (item == null) {
            this.Item_selected.setText("Nothing Selected Yet!!!");
        } else {
            String logInfo = "";
//            logInfo += "LOG_ID::\t" + item.getLog_Id() + "\t";
            logInfo += "USER_ID::\t" + item.getUser_id() + "\n";
            logInfo += "IN-TIME:: \t" + item.getLog_in() + "\n";
            logInfo += "OUT-TIME:: \t" + item.getLog_out() + "\n";
            logInfo += item.getReport() + "\n";

            this.Item_selected.setText(logInfo);
        }
    }

    @FXML
    private void return_Handler(ActionEvent event) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("OBS | AdminPanel");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void userID_txtSearch_searchUserLog_Handler(ActionEvent event) {
        String user = this.userID_txtSearch.getText();
        if (user.isBlank() || user.isEmpty()) {
            this.load_list();
            return;
        }
        this.ListView.getItems().clear();
        this.ListView.getItems().addAll(this.dbcon.getUserLogReport(user));
    }

    @FXML
    private void reloadListView(ActionEvent event) {
        this.Item_selected.clear();
        this.Item_selected.setText("List Reloaded From Database...");
        this.load_list();
    }
}
