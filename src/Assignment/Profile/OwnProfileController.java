/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Profile;

import Assignment.Records.ViewRecordController;
import Assignment.Trainee.Enrollment.EnrollmentController;
import Assignment.Trainer.Course.CourseController;
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
import javafx.application.Platform;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author sony
 */
public class OwnProfileController implements Initializable {
    
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfGender;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfContact;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnBack;
    @FXML
    private TableView<OwnProfile> tvProfile;
    @FXML
    private TableColumn<OwnProfile, String> colName;
    @FXML
    private TableColumn<OwnProfile, Integer> colAge;
    @FXML
    private TableColumn<OwnProfile, String> colGender;
    @FXML
    private TableColumn<OwnProfile, String> colEmail;
    @FXML
    private TableColumn<OwnProfile, String> colContact;
    @FXML
    private AnchorPane rootpane;
    
    public boolean showtrainer;
    
    Trainer trainer;
    Trainee trainee;
    String id;
    String query;
    @FXML
    private Text btnProfile;
    @FXML
    private Text btnRecords;
    @FXML
    private Text btnCourse;
    
    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
        id = trainee.getUserID();
        showtrainer = false;
    }
    
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        id = trainer.getUserID();
        showtrainer = true;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Platform.runLater(new Runnable() {
            public void run(){
        
                try {
                    // TODO
                    showOwnProfile();
                    OwnProfile own = tvProfile.getSelectionModel().getSelectedItem();

                } catch (SQLException ex) {
                    Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });    
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
    
    
    public ObservableList<OwnProfile> getOwnProfile() throws SQLException{
        ObservableList<OwnProfile> ownP = FXCollections.observableArrayList();
        Connection conn = getConnection();
        
        if (showtrainer==false){
             query = "SELECT * FROM profile Where Trainee_ID = "
                    + "(SELECT Trainee_ID FROM trainee Where Trainee_ID ='"+id+"')";
        }
        else if (showtrainer == true){
             query = "SELECT * FROM profile Where Trainer_ID = "
                    + "(SELECT Trainer_ID FROM trainer Where Trainer_ID ='"+id+"')";
        }
               
 
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            OwnProfile op;
            while(rs.next()){
                op = new OwnProfile(rs.getString("User_name"), rs.getInt("User_age"), rs.getString("User_gender"),rs.getString("User_email"),rs.getString("User_contact"));
                ownP.add(op);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return ownP;
    }
    
    
    public void showOwnProfile() throws SQLException{

        ObservableList<OwnProfile> list = getOwnProfile();

        colName.setCellValueFactory(new PropertyValueFactory<OwnProfile, String>("Name"));
        colAge.setCellValueFactory(new PropertyValueFactory<OwnProfile, Integer>("Age"));
        colGender.setCellValueFactory(new PropertyValueFactory<OwnProfile, String>("Gender"));
        colEmail.setCellValueFactory(new PropertyValueFactory<OwnProfile, String>("Email"));
        colContact.setCellValueFactory(new PropertyValueFactory<OwnProfile, String>("Contact"));

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
    
     private void editProfile() throws SQLException{
         
        if (showtrainer == false){ 
            String query = "UPDATE profile SET User_name = '" + tfName.getText() + "', User_age = " + tfAge.getText() + ", User_gender = '" + 
                    tfGender.getText() + "', User_email = '" + tfEmail.getText() + "', User_contact = '" + tfContact.getText() + 
                    "' Where Trainee_ID = '"+id+"'";
            executeQuery(query);
            showOwnProfile();
        }
        else if (showtrainer == true){
           String query = "UPDATE profile SET User_name = '" + tfName.getText() + "', User_age = " + tfAge.getText() + ", User_gender = '" + 
                    tfGender.getText() + "', User_email = '" + tfEmail.getText() + "', User_contact = '" + tfContact.getText() + 
                    "' Where Trainer_ID = '"+id+"'";
            executeQuery(query);
            showOwnProfile(); 
        }
    }
     
  
     
    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException, IOException{
        System.out.println("You have clicked the button");
        if(event.getSource() == btnEdit){
                editProfile();
           
        }
        if(event.getSource() == btnBack){
                viewProfile();
      
        }
        
    }

    public void viewProfile() throws IOException{
        
        if (showtrainer == false){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./Profile.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            ProfileController profilec = fxmlLoader.<ProfileController>getController();
            profilec.setTrainee(trainee);
            rootpane.getChildren().setAll(root);
        }
        else if(showtrainer ==true){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("./Profile.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            ProfileController profilec = fxmlLoader.<ProfileController>getController();
            profilec.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
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

    @FXML
    private void handleMouseAction(MouseEvent event) throws SQLException, IOException{
        
        if(event.getSource() == btnProfile){
                viewProfile();
      
        }
        else if(event.getSource() == btnRecords){
                
                viewRecordsPage();
        }
        else if(event.getSource() == btnCourse){
                viewCoursePage();
      
        }
        else{
            OwnProfile own = tvProfile.getSelectionModel().getSelectedItem();
            tfName.setText(own.getName());
            tfAge.setText(""+own.getAge());
            tfGender.setText(own.getGender());
            tfEmail.setText(own.getEmail());
            tfContact.setText(own.getContact());
        }
        
        
    }
    
    
    
    
}


