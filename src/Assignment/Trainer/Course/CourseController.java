/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Trainer.Course;

import Assignment.Materials.MaterialController;
import Assignment.Profile.ProfileController;
import Assignment.Records.ViewRecordController;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.ConditionalFeature.FXML;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author sony
 */
public class CourseController implements Initializable {

    @FXML
    private TextField tfId;
    @FXML
    private TableColumn<Course, String> colId;
    @FXML
    private TableColumn<Course, String> colCourseName;
    @FXML
    private TableColumn<Course, String> colTrainerID;
    @FXML
    private TableColumn<Course, LocalDate> colSchedule;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnViewMaterial;
    @FXML
    private AnchorPane rootpane;
    @FXML
    private TextField tfCourseName;
    @FXML
    private DatePicker tfSchedule;
    @FXML
    private TextField tfTrainerID;
    @FXML
    private TextArea tfCourseInfo;
    @FXML
    private TableColumn<Course, String> colCourseInfo;

    /**
     * Initializes the controller class.
     */
    @FXML
    private Text btnProfile;
    @FXML
    private Text btnRecords;
    @FXML
    private Text btnCourse;

    Trainer trainer;
    @FXML
    private TableView<Course> tvCourses;
    @FXML
    private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            // TODO
            @Override
            public void run() {
                try {
                    // TODO
                    showCourses();
                } catch (SQLException ex) {
                    Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    ;    
    
    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException, IOException {
        System.out.println("You have clicked the button");
        if (event.getSource() == btnInsert) {
            insertRecord();
        } else if (event.getSource() == btnUpdate) {
            updateRecord();
        } else if (event.getSource() == btnDelete) {
            deleteRecord();
        } else if (event.getSource() == btnViewMaterial) {
            ViewMaterial();

        } else if (event.getSource() == btnBack) {
            Back();
        }
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

    public ObservableList<Course> getBooksList() throws SQLException {
        ObservableList<Course> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM course";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
//            Course books;
            while (rs.next()) {
                Course books = new Course(rs.getString("Trainee_CourseID"), rs.getString("Course_name"), rs.getString("Course_info"), rs.getDate("Course_schedule").toLocalDate(), rs.getString("Trainer_ID"));
                bookList.add(books);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bookList;
    }

    public void showCourses() throws SQLException {
        ObservableList<Course> list = getBooksList();

        colId.setCellValueFactory(new PropertyValueFactory<Course, String>("id"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<Course, String>("courseName"));
        colCourseInfo.setCellValueFactory(new PropertyValueFactory<Course, String>("courseInfo"));
        colSchedule.setCellValueFactory(new PropertyValueFactory<Course, LocalDate>("schedule"));
        colTrainerID.setCellValueFactory(new PropertyValueFactory<Course, String>("trainerID"));

        tvCourses.setItems(list);
    }

    private void insertRecord() throws SQLException {
        String query = "INSERT INTO course VALUES('" + tfId.getText() + "','" + tfCourseName.getText() + "','" + java.sql.Date.valueOf(tfSchedule.getValue()) + "','" + tfTrainerID.getText() + "','" + tfCourseInfo.getText() + "')";
        executeQuery(query);
        showCourses();
    }

    private void updateRecord() throws SQLException {
        String query = "UPDATE course SET Course_name = '" + tfCourseName.getText() + "', Trainer_ID = '" + tfTrainerID.getText() + "', Course_schedule = '"
                + java.sql.Date.valueOf(tfSchedule.getValue()) + "', Course_info = '" + tfCourseInfo.getText() + "' WHERE Trainee_CourseID = '" + tfId.getText() + "' AND Trainer_ID = '" + trainer.getUserID() + "'";
        executeQuery(query);
        showCourses();
    }

    private void deleteRecord() throws SQLException {
        String query = "DELETE FROM course WHERE Trainee_CourseID = '" + tfId.getText() + "'";
        executeQuery(query);
        showCourses();
    }

    public void viewRecordsPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Records/ViewRecord.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ViewRecordController controller = fxmlLoader.<ViewRecordController>getController();
        controller.setTrainer(trainer);
        rootpane.getChildren().setAll(root);
    }

    public void viewProfilePage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Profile/Profile.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ProfileController profilec = fxmlLoader.<ProfileController>getController();
        profilec.setTrainer(trainer);
        rootpane.getChildren().setAll(root);

    }

    public void Back() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Dashboard/TrainerDashboard.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Trainer_DashboardController controller = fxmlLoader.<Trainer_DashboardController>getController();
        controller.setTrainer(trainer);
        rootpane.getChildren().setAll(root);

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

    @FXML
    private void handleMouseAction(MouseEvent event) throws IOException {

        if (event.getSource() == btnRecords) {
            viewRecordsPage();
        } else if (event.getSource() == btnProfile) {
            viewProfilePage();
        } else {
            Course book = tvCourses.getSelectionModel().getSelectedItem();
            tfId.setText("" + book.getId());
            tfCourseName.setText("" + book.getCourseName());
            tfCourseInfo.setText("" + book.getCourseInfo());
            tfSchedule.setValue(book.getSchedule());
            tfTrainerID.setText("" + book.getTrainerID());
        }

    }

    public void ViewMaterial() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Materials/Material.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            MaterialController controller = fxmlLoader.<MaterialController>getController();
            controller.setCourseID(tfId.getText());
            controller.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
