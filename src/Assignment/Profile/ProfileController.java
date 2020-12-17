/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Profile;

import Assignment.Records.ViewRecordController;
import Assignment.Trainee.Dashboard.TraineeDashboardController;
import Assignment.Trainee.Enrollment.EnrollmentController;
import Assignment.Trainer.Course.CourseController;
import Assignment.Trainer.Dashboard.Trainer_DashboardController;
import Assignment.loginRegister.Trainee;
import Assignment.loginRegister.Trainer;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Adm
 */
public class ProfileController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private TableView<Profile> tvProfile;
    @FXML
    private TableColumn<Profile, String> colName;
    @FXML
    private TableColumn<Profile, Integer> colAge;
    @FXML
    private TableColumn<Profile, String> colGender;
    @FXML
    private TableColumn<Profile, String> colEmail;
    @FXML
    private TableColumn<Profile, String> colContact;
    @FXML
    private Button btnTrainer;
    @FXML
    private Button btnTrainee;
    @FXML
    private Button btnOwn;
    @FXML
    private Button btnBack;
    
    public boolean showtrainer;
  
    public String query;
   
    Trainer trainer;
    Trainee trainee;
    @FXML
    private Text btnRecords;
    @FXML
    private Text btnCourse;

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
        showtrainer = false;
    }
    
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        showtrainer = true;
    }
    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    
    public ObservableList<Profile> getProfile() throws SQLException{
        ObservableList<Profile> profile = FXCollections.observableArrayList();
        Connection conn = getConnection();
        
        
            query = "SELECT * FROM profile Where Trainer_ID IS NOT NULL";
          
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Profile p;
            while(rs.next()){
                p = new Profile(rs.getString("User_name"), rs.getInt("User_age"), rs.getString("User_gender"),rs.getString("User_email"),rs.getString("User_contact"));
                profile.add(p);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return profile;
    }
    
    public ObservableList<Profile> getTraineeProfile() throws SQLException{
        ObservableList<Profile> profile = FXCollections.observableArrayList();
        Connection conn = getConnection();
        
        
         query = "SELECT * FROM profile Where Trainer_ID IS NULL" ;
        
           
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Profile p;
            while(rs.next()){
                p = new Profile(rs.getString("User_name"), rs.getInt("User_age"), rs.getString("User_gender"),rs.getString("User_email"),rs.getString("User_contact"));
                profile.add(p);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return profile;
    }
    
    public void showProfile() throws SQLException{
        

        ObservableList<Profile> list = getProfile();
        colName.setCellValueFactory(new PropertyValueFactory<Profile, String>("Name"));
        colAge.setCellValueFactory(new PropertyValueFactory<Profile, Integer>("Age"));
        colGender.setCellValueFactory(new PropertyValueFactory<Profile, String>("Gender"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Profile, String>("Email"));
        colContact.setCellValueFactory(new PropertyValueFactory<Profile, String>("Contact"));

        tvProfile.setItems(list);
        System.out.println(list);
       
           
        
    }
    
    public void showTraineeProfile() throws SQLException{
        

        ObservableList<Profile> list = getTraineeProfile();
        colName.setCellValueFactory(new PropertyValueFactory<Profile, String>("Name"));
        colAge.setCellValueFactory(new PropertyValueFactory<Profile, Integer>("Age"));
        colGender.setCellValueFactory(new PropertyValueFactory<Profile, String>("Gender"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Profile, String>("Email"));
        colContact.setCellValueFactory(new PropertyValueFactory<Profile, String>("Contact"));

        tvProfile.setItems(list);
        System.out.println(list);
       
           
        
    }
    
    
    private void executeQuery(String query) throws SQLException{
        Connection conn = getConnection();
        Statement st;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    @FXML
    private void handleButtonAction(ActionEvent event)throws SQLException, IOException {
        
        if(event.getSource() == btnTrainer){
           
            showProfile();
        }
        else if(event.getSource() == btnTrainee){
            
            showTraineeProfile();
        }
        else if(event.getSource() == btnOwn){
              
                viewOwnProfile();
        }
        
        else if(event.getSource() == btnBack){
              
                viewDashBoard();
        }
        
    }
    
    @FXML
    private void handleMouseAction(MouseEvent event) throws SQLException, IOException {
        
        if(event.getSource() == btnRecords){
           
            viewRecordsPage();
        }
        else if(event.getSource() == btnCourse){
           
            viewCoursePage();
        }
        
    }
    
    public void viewRecordsPage() throws IOException {
        
        if (showtrainer == true){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Records/ViewRecord.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            ViewRecordController viewr = fxmlLoader.<ViewRecordController>getController();
            viewr.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        }
    }
    
    public void viewCoursePage() throws IOException {
        
        if (showtrainer == true){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainer/Course/CoursePage.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            CourseController viewc = fxmlLoader.<CourseController>getController();
            viewc.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        }
        else if (showtrainer==false){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainee/Enrollment/Enrollment.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            EnrollmentController viewe = fxmlLoader.<EnrollmentController>getController();
            viewe.setTrainee(trainee);
            rootpane.getChildren().setAll(root);
        }
    }
    
    public void viewOwnProfile() throws IOException{
        
        if (showtrainer == false){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./OwnProfile.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            OwnProfileController ownprofilec = fxmlLoader.<OwnProfileController>getController();
            ownprofilec.setTrainee(trainee);
            rootpane.getChildren().setAll(root);
        }
        else if (showtrainer == true){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./OwnProfile.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            OwnProfileController ownprofilec = fxmlLoader.<OwnProfileController>getController();
            ownprofilec.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        }
                    
    }
    public void viewDashBoard() throws IOException{
        
        if (showtrainer == false){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainee/DashBoard/TraineeDashBoard.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            TraineeDashboardController ownprofilec = fxmlLoader.<TraineeDashboardController>getController();
            ownprofilec.setTrainee(trainee);
            rootpane.getChildren().setAll(root);
        }
        else if (showtrainer == true){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainer/DashBoard/TrainerDashBoard.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            Trainer_DashboardController ownprofilec = fxmlLoader.<Trainer_DashboardController>getController();
            ownprofilec.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        }
                    
    }
    
    
    
}
