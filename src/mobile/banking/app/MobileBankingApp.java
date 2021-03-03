/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author amado
 */
public class MobileBankingApp extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        this.adminPanelTest();
        this.clientPanelTest(primaryStage);
    }
    
    private void adminPanelTest() throws IOException{
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(this.getClass().getResource("scenes/admin/AdminDashboard.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("OBS | AdminPanel");
        primaryStage.show();
    }
    
    private void clientPanelTest(Stage primaryStage) throws IOException{
        Parent root = FXMLLoader.load(this.getClass().getResource("scenes/client/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ONLINE BANKING SYSTEM");
        primaryStage.show();
    }
    
    public static void aboutApp() {
        String message = "OBS - Online Banking System, is a Java SE App. It allows the users to manage the Banking and Transactional Information, transfer Funds via our platform, and pay Bills."
                + "\n\n \tApp-Version:: v.1.0"
                + "\n \tAlias:: GHOST ☢"
                + "\n \thttp:://ghostx.io/apps";
        Alert bl = new Alert(Alert.AlertType.INFORMATION);
        bl.setTitle("OBS - About App");
        bl.setContentText(message);
        bl.show();
    }

}
