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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author vlys2
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField inputUsername;
    @FXML
    private PasswordField inputPassword;
    private TextField inputName;
    @FXML
    private TextField inputAge;
//    @FXML
//    private Button btnCancel;
    private TextField inputGender;
    @FXML
    private TextField inputEmail;
    @FXML
    private TextField inputContact;
    @FXML
    Label lblStatus;
    
    int countTrainee = 0, countTrainer = 0, countProfile;
    
    PreparedStatement preparedStatement;
    Connection connection;
    private ObservableList<ObservableList> data;
    String SQL = "SELECT * from user";
    @FXML
    private Button btnTrainee;
    @FXML
    private Button btnTrainer;
    @FXML
    private CheckBox inputMale;
    @FXML
    private CheckBox inputFemale;
    @FXML
    private Label labelID;
    @FXML
    private AnchorPane rootpane;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField name;
    @FXML
    private TextField age;
    @FXML
    private ChoiceBox<?> gender;
    @FXML
    private TextField email;
    @FXML
    private TextField contact;
    @FXML
    private Button btnCancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    Connection conn = null;
    
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
    

    
    private void clearFields()
    {
        inputUsername.clear();
        inputPassword.clear();
        inputName.clear();
        inputAge.clear();
        inputGender.clear();
        inputEmail.clear();
        inputContact.clear();
    }
    
    private String saveDataTrainee()
    {
        String userName = inputUsername.getText();
        String userPassword = inputPassword.getText();
        
        
        try {

            Connection conn = getConnection();
            String ct = "SELECT COUNT(*) FROM trainee";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(ct);
            rs.next();
            countTrainee = rs.getInt(1);
            String newTraineeID = "tre" + countTrainee;
            
            ct = "SELECT COUNT(*) FROM profile";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(ct);
            rs.next();
            countProfile = rs.getInt(1);

            //String st = "INSERT INTO trainee (username, password) and Profile ( User_name, User_age, User_gender, User_email, User_contact) VALUES (?,?,?,?,?)";
            String query = "INSERT INTO trainee(Trainee_ID, Trainee_Name, Trainee_Password) VALUES (?,?,?)";
            preparedStatement = (PreparedStatement) conn.prepareStatement(query);
            preparedStatement.setString(1, newTraineeID);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, userPassword);
            preparedStatement.executeUpdate();

            query = "INSERT INTO Profile(Profile_ID, User_name, User_age, User_gender, User_email, User_contact, Trainee_ID, Trainer_ID) VALUES (?,?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) conn.prepareStatement(query);
            preparedStatement.setString(1, "P" + countProfile);	
            preparedStatement.setString(2, userName);
            preparedStatement.setInt(3, Integer.parseInt(inputAge.getText()));
            if(inputMale.isSelected())
            {
                preparedStatement.setString(4, "M");
            }else if (inputFemale.isSelected())
            {
                 preparedStatement.setString(4, "F");
            }
            preparedStatement.setString(5, inputEmail.getText());
            preparedStatement.setString(6, inputContact.getText());
            preparedStatement.setString(7, newTraineeID);
            preparedStatement.setString(8, null);
            preparedStatement.executeUpdate();
            
//            query = "'INSERT INTO Profile(Trainee_ID) VALUES '" + newTraineeID;
//            preparedStatement = (PreparedStatement) conn.prepareStatement(query);
 //           preparedStatement.executeUpdate();
            lblStatus.setText("Added Successfully. Your user ID is" + newTraineeID);

            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
            ex.printStackTrace();
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }
    
    private String saveDataTrainer()
    {
        try {
            Connection conn = getConnection();
            String ct = "SELECT COUNT(*) FROM trainer";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(ct);
            rs.next();
            countTrainer = rs.getInt(1);
            System.out.println("Number of records in the trainee table: "+countTrainer);
            String newTrainerID = "trr" + countTrainer;
            
            ct = "SELECT COUNT(*) FROM profile";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(ct);
            rs.next();
            countProfile = rs.getInt(1);
            
            String userName = inputUsername.getText();
            String userPassword = inputPassword.getText();
            //String st = "INSERT INTO trainee (username, password) and Profile ( User_name, User_age, User_gender, User_email, User_contact) VALUES (?,?,?,?,?)";
            String query = "INSERT INTO trainer(Trainer_ID, Trainer_Name, Trainer_Password) VALUES (?,?,?)";
            preparedStatement = (PreparedStatement) conn.prepareStatement(query);
            preparedStatement.setString(1, newTrainerID);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, userPassword);
            preparedStatement.executeUpdate();

            query = "INSERT INTO Profile(Profile_ID, User_name, User_age, User_gender, User_email, User_contact, Trainee_ID, Trainer_ID) VALUES (?,?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) conn.prepareStatement(query);
            preparedStatement.setString(1, "P" + countProfile);	
            preparedStatement.setString(2, userName);
            preparedStatement.setInt(3, Integer.parseInt(inputAge.getText()));
            if(inputMale.isSelected())
            {
                preparedStatement.setString(4, "M");
            }else if (inputFemale.isSelected())
            {
                 preparedStatement.setString(4, "F");
            }
            preparedStatement.setString(5, inputEmail.getText());
            preparedStatement.setString(6, inputContact.getText());
            preparedStatement.setString(7, null);
            preparedStatement.setString(8, newTrainerID);
            preparedStatement.executeUpdate();
            lblStatus.setText("Added Successfully. Your user ID is" + newTrainerID);

            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
            ex.printStackTrace();
            lblStatus.setText(ex.getMessage());
            return "Exception";
        }
    }

    @FXML
    private void registerComplete(MouseEvent event) throws IOException {
        boolean validAge;
        boolean validEmail;
        boolean validContact;
        String age;
        String email;
        String contact;
        
        age = inputAge.getText();
        validAge = checkAge(age);
        email = inputEmail.getText();
        validEmail = checkEmail(email);
        contact = inputContact.getText();
        validContact = checkContact(contact);
        
        if(inputUsername.getText().isEmpty() || 
           inputPassword.getText().isEmpty() || 
           inputAge.getText().isEmpty()|| 
           inputEmail.getText().isEmpty() || 
           (!inputMale.isSelected() && !inputFemale.isSelected())|| 
           inputContact.getText().isEmpty())
        {
            lblStatus.setText("Please Enter All details");
        }else if(validAge && validEmail && validContact)
        {
            if(event.getSource() == btnTrainee)
            {
                saveDataTrainee();
                lblStatus.setText("You have successfully registered an account");
            }
            else if(event.getSource() == btnTrainer)
            {
                saveDataTrainer();
                lblStatus.setText("You have successfully registered an account");
            }
        }else if(!validAge)
        {
            lblStatus.setText("Please enter an age between 0-99");
        }else if(!validEmail)
        {
            lblStatus.setText("Please enter a valid Email");
        }else if(!validContact)
        {
            lblStatus.setText("Please enter a valid Contact");
        }
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
    
    private boolean checkEmail(String email)
    {
        boolean result;
        String email_regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String test = email;
        result = test.matches(email_regex);
        
        return result;
    }
    
    private boolean checkContact (String contact)
    {
        if(contact.matches("\\d{10}"))
            return true;
        else if (contact.matches("\\d{11}"))
            return true;
        else
            return false;
    }
    
    private boolean checkAge (String age)
    {
        String expression = "[-+]?[0-9]*\\.?[0-9]+$";
        CharSequence inputStr = age;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if(matcher.matches())
        {
            return true;
        }else
        return false;
    }

    @FXML
    private void handleCloseButtonAction(MouseEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}