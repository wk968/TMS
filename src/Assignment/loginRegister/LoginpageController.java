/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.loginRegister;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Assignment.Trainee.Dashboard.TraineeDashboardController;
import Assignment.Trainer.Dashboard.Trainer_DashboardController;
/**
 * FXML Controller class
 *
 * @author vlys2
 */
public class LoginpageController implements Initializable {

    @FXML
    private PasswordField inputPassword;
    @FXML
    private Button btnRegister;
    private Button btnLoginTrainee;
    private Button btnLoginTrainer;
    @FXML
    private Label lblError;
    @FXML
    private AnchorPane rootpane;
    @FXML
    private Button btnLogin;
    @FXML
    private TextField inputUserId;
    
    Trainee trainee;
    Trainer trainer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public Connection getConnection() throws SQLException{
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms","root","");
            return conn;
        }catch(Exception ex){
            System.out.println("Error: "+ex.getMessage());
            return null;
        }
    }

    private void handleMouseAction(MouseEvent event) throws IOException, SQLException {
        if(event.getSource() == btnLoginTrainee || event.getSource() == btnLoginTrainer){
            System.out.println("Login");
        if(logInTrainee().equals("Success"))
        {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                
                AnchorPane scene = FXMLLoader.load(getClass().getResource("../Trainee/Dashboard/TraineeDashboard.fxml"));
                rootpane.getChildren().setAll(scene);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
          }
        else if(logInTrainer().equals("Success"))
        {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                
                AnchorPane scene = FXMLLoader.load(getClass().getResource("../Trainee/Dashboard/TraineeDashboard.fxml"));
                rootpane.getChildren().setAll(scene);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
        }
        else if(event.getSource() == btnRegister){
         Parent root = FXMLLoader.load(getClass().getResource("./Register.fxml"));
         Scene Dashboard_Scene = new Scene(root);
         Dashboard_Scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        
         Stage primaryStage = new Stage();
         primaryStage.setScene(Dashboard_Scene);
         primaryStage.setTitle("Software Engineering Fundamental Assignment - Group Garbage Legend - TT03");
         primaryStage.show();
        
        }

    }
    
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    private String logInTrainee() throws SQLException
    {
        String status = "Success";
        String userId = inputUserId.getText();
        String password = inputPassword.getText();
        
        if(userId.isEmpty() || password.isEmpty()) 
        {
            lblError.setText("Empty credentials");
            status = "Error";
        }
        else
        {
            Connection conn = getConnection();
            String sql = "SELECT * FROM trainee Where Trainee_ID = ? and Trainee_Password = ?";

            try {
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,userId);
                preparedStatement.setString(2,password);
                resultSet = preparedStatement.executeQuery();

                if(!resultSet.next())
                {
                    lblError.setText("Please try again");
                    return "Error";
                }
                else
                {
                    String query = "SELECT * FROM profile where Trainee_ID = '" + userId +"'";
                    Statement st;
                    ResultSet rs;
                    st = conn.createStatement();
                    rs = st.executeQuery(query);
                    rs.next();
                    trainee = new Trainee(rs.getString("Profile_ID"), rs.getString("User_name"), rs.getInt("User_age"), rs.getString("User_gender"), rs.getString("User_email"), rs.getString("User_contact"), userId);
                    showDialog("Login Successful", null, "Successful");
                    
                }
            } catch (Exception ex) {
                Logger.getLogger(LoginpageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }
    
    private String logInTrainer() throws SQLException
    {
        String status = "Success";
        String userId = inputUserId.getText();
        String password = inputPassword.getText();
        
        if(userId.isEmpty() || password.isEmpty()) 
        {
            lblError.setText("Empty credentials");
            status = "Error";
        }
        else
        {
            Connection conn = getConnection();
            String sql = "SELECT * FROM trainer Where Trainer_ID = ? and Trainer_Password = ?";

            try {
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,userId);
                preparedStatement.setString(2,password);
                resultSet = preparedStatement.executeQuery();

                if(!resultSet.next())
                {
                    lblError.setText("Please try again");
                    return "Error";
                }
                else
                {
                    String query = "SELECT * FROM profile where Trainer_ID = '" + userId + "'";
                    Statement st;
                    ResultSet rs;
                    st = conn.createStatement();
                    rs = st.executeQuery(query);
                    rs.next();
                    trainer = new Trainer(rs.getString("Profile_ID"), rs.getString("User_name"), rs.getInt("User_age"), rs.getString("User_gender"), rs.getString("User_email"), rs.getString("User_contact"), userId);
                    showDialog("Login Successful", null, "Successful");
                }
            } catch (Exception ex) {
                Logger.getLogger(LoginpageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }
    
    private void showDialog(String info, String header, String title)
    {
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
        alert.setContentText(info);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    @FXML
    private void handleButtonAction(MouseEvent event) throws IOException, SQLException 
    {
        if(event.getSource() == btnLogin)
        {
            if(inputUserId.getText().substring(0, 3).equals("tre")){
            if(logInTrainee().equals("Success"))
            {
                try {
    //                Node node = (Node) event.getSource();
    //                Stage stage = (Stage) node.getScene().getWindow();
    //                stage.close();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainee/Dashboard/TraineeDashboard.fxml"));     

                    Parent root = (Parent)fxmlLoader.load();          
                    TraineeDashboardController controller = fxmlLoader.<TraineeDashboardController>getController();
                    controller.setTrainee(trainee);
                    //AnchorPane scene = FXMLLoader.load(getClass().getResource("../Trainee/Dashboard/TraineeDashboard.fxml"));
                    rootpane.getChildren().setAll(root);
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            else{
                lblError.setText("You had entered a wrong password or id. Please try again");
            }
            }
            else if(inputUserId.getText().substring(0, 3).equals("trr")){
            if(logInTrainer().equals("Success"))
            {
                try {
    //                Node node = (Node) event.getSource();
    //                Stage stage = (Stage) node.getScene().getWindow();
    //                stage.close();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainer/Dashboard/TrainerDashboard.fxml"));     

                    Parent root = (Parent)fxmlLoader.load();          
                    Trainer_DashboardController controller = fxmlLoader.<Trainer_DashboardController>getController();
                    controller.setTrainer(trainer);

                    //AnchorPane scene = FXMLLoader.load(getClass().getResource("../Trainer/Dashboard/TrainerDashboard.fxml"));
                    rootpane.getChildren().setAll(root);
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            }
            else{
            lblError.setText("You may entered a wrong password or id. Please try again");
             }
            
        }
        else if(event.getSource() == btnRegister){
         Parent root = FXMLLoader.load(getClass().getResource("./Register.fxml"));
         Scene Dashboard_Scene = new Scene(root);
         Dashboard_Scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
        
         Stage primaryStage = new Stage();
         primaryStage.setScene(Dashboard_Scene);
         primaryStage.setTitle("Software Engineering Fundamental Assignment - Group Garbage Legend - TT03");
         primaryStage.show();
        }
    }
    
}