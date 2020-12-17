/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Trainer.Dashboard;

import Assignment.Profile.ProfileController;
import Assignment.Records.ViewRecordController;
import Assignment.loginRegister.Trainee;
import Assignment.Trainee.Dashboard.TraineeDashboardController;
import Assignment.Trainer.Course.Course;
import Assignment.loginRegister.Trainer;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Assignment.Trainer.Course.Course;
import Assignment.Trainer.Course.CourseController;
import Assignment.loginRegister.Trainee;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author sony
 */
public class Trainer_DashboardController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private Text btnProfile;
    @FXML
    private Text btnRecords;
    @FXML
    private Text btnCourse;
    
    Course course;
    Trainer trainer;
    @FXML
    private AnchorPane subpane;
    @FXML
    private Label labelID;
    @FXML
    private Label labelName;
    @FXML
    private TableColumn<Trainee, String> colTraineeID;
    @FXML
    private TableColumn<Trainee, String> colTraineeName;
    @FXML
    private TableColumn<Trainee, String> colTraineeEmail;
    @FXML
    private TableColumn<Course, String> colCourseID;
    @FXML
    private TableColumn<Course, String> colCourseName;
    @FXML
    private TableColumn<Course, LocalDate> colCourseSchedule;
    @FXML
    private TableView<Course> tvCourse;
    @FXML
    private TableView<Trainee> tvTrainee;
    @FXML
    private Button btnUploadPic;
    @FXML
    private ImageView imgProfilePic;


    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(new Runnable() {
            // TODO
            @Override
            public void run() {
                try {
                    showTrainee();
                    showCourses();
                    try {
                        refreshProfilePic();
                    } catch (IOException ex) {
                        Logger.getLogger(Trainer_DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Trainer_DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
                labelID.setText(trainer.getUserID());
                labelName.setText(trainer.getUserName());
//                ex.printStackTrace();
//                Logger.getLogger(TraineeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

    public void showCourses() throws SQLException {
        ObservableList<Course> list = getCourseList();
        
        colCourseID.setCellValueFactory(new PropertyValueFactory<Course, String>("id"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<Course, String>("courseName"));
        colCourseSchedule.setCellValueFactory(new PropertyValueFactory<Course, LocalDate>("schedule"));

        tvCourse.setItems(list);
    }
    
    public void showTrainee() throws SQLException {
        ObservableList<Trainee> traineelist = getTraineeList();

        colTraineeID.setCellValueFactory(new PropertyValueFactory<Trainee, String>("userID"));
        colTraineeName.setCellValueFactory(new PropertyValueFactory<Trainee, String>("userName"));
        colTraineeEmail.setCellValueFactory(new PropertyValueFactory<Trainee, String>("userEmail"));

        tvTrainee.setItems(traineelist);
    }

    public Connection getConnection() throws SQLException {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "");
            return conn;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    private void executeQuery(String query) throws SQLException {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ObservableList<Course> getCourseList() throws SQLException {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM course WHERE Trainer_ID = '" + trainer.getUserID() + "'";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
//            Course books;
            while (rs.next()) {
                Assignment.Trainer.Course.Course course = new Assignment.Trainer.Course.Course(rs.getString("Trainee_CourseID"), rs.getString("Course_name"), rs.getString("Course_info"), rs.getDate("Course_schedule").toLocalDate(), rs.getString("Trainer_ID"));
                courseList.add(course);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return courseList;
    }
    
    public ObservableList<Trainee> getTraineeList() throws SQLException {
        ObservableList<Trainee> traineeList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM profile where Trainee_ID IS NOT NULL";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
//            Course books;
            while (rs.next()) {
                Trainee trainee = new Trainee(rs.getString("Profile_ID"), rs.getString("User_name"), rs.getInt("User_age"), rs.getString("User_gender"), rs.getString("User_email"), rs.getString("User_contact"), rs.getString("Trainee_ID"));
                traineeList.add(trainee);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return traineeList;
    }

    @FXML
    private void handleButtonAction(MouseEvent event) throws IOException, SQLException {
        if (event.getSource() == btnCourse) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Course/CoursePage.fxml"));
            Parent root = (Parent)fxmlLoader.load(); 
            CourseController profilec = fxmlLoader.<CourseController>getController();
            profilec.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        } else if (event.getSource() == btnRecords) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Records/ViewRecord.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            ViewRecordController viewr = fxmlLoader.<ViewRecordController>getController();
            viewr.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        } else if (event.getSource() == btnProfile) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Profile/Profile.fxml"));   
            Parent root = (Parent)fxmlLoader.load(); 
            ProfileController profilec = fxmlLoader.<ProfileController>getController();
            profilec.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        } else if (event.getSource() == btnUploadPic){
            try{
             uploadProfilePic();   
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    public void uploadProfilePic() throws SQLException, FileNotFoundException{
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE profile SET profileImage = ? WHERE Trainer_ID = '" + trainer.getUserID() + "'");
        //PreparedStatement pstmt = conn.prepareStatement("INSERT INTO profile (profileImage) VALUES(?) WHERE Trainer_ID = '" + trainer.getUserID() + "'");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.PNG", "*.png","*.JPEG", "*.jpg", "*.gif")
        );
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(null);
        InputStream inputStream = new FileInputStream(file);
        
        pstmt.setBinaryStream(1, inputStream);
        pstmt.execute();
        if(file != null)
        {
            Image image = new Image(file.toURI().toString(), 140, 212, true, true);
            imgProfilePic.setImage(image);
            imgProfilePic.setPreserveRatio(true);
        }
    }
    
    public void refreshProfilePic() throws SQLException, IOException{
        Statement st;
        ResultSet rs = null;
        Connection conn = getConnection();
        String query = "Select * FROM profile WHERE Trainer_ID = '" + trainer.getUserID() + "'";
        st = conn.createStatement();
        rs = st.executeQuery(query);
        if (rs.next()) {
            Blob blob = rs.getBlob("profileImage");
            if (blob != null) {
                InputStream is = blob.getBinaryStream(1, blob.length());
                BufferedImage buffImage = ImageIO.read(is);
                Image image = SwingFXUtils.toFXImage(buffImage, null);
                imgProfilePic.setImage(image);
                imgProfilePic.setPreserveRatio(true);
            }
        }
    }
}
