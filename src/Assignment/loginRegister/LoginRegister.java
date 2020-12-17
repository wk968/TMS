/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.loginRegister;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author vlys2
 */
public class LoginRegister extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent login = FXMLLoader.load(getClass().getResource("./loginpage.fxml"));
        Scene login_Scene = new Scene(login);
        login_Scene.getStylesheets().add(getClass().getResource("./application.css").toExternalForm());
        primaryStage.setScene(login_Scene);
        primaryStage.setTitle("Software Engineering Fundamental Assignment - Group Garbage Legend - TT03");
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
