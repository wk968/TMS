package Assignment.Trainee.Enrollment;

import Assignment.Materials.TraineeMaterialController;
import Assignment.Profile.ProfileController;
import Assignment.loginRegister.Trainee;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
 * @author Wk
 */
public class EnrollmentController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private TextField tfId;
    @FXML
    private Button btnEnroll, btnViewMaterial;
    @FXML
    private TableView<Enrollment> tvCourse;
    @FXML
    private TableColumn<Enrollment, String> colCourseID, colCourseName, colTrainer, colCourseInfo;
    @FXML
    private TableColumn<Enrollment, Date> colSchedule;

    Enrollment enrollment = new Enrollment();
    ArrayList<String> list = new ArrayList<>();
    Trainee trainee;
    boolean state = true;
    @FXML
    private Text btnProfile, btnRecords, btnCourse;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showCourse();
        } catch (SQLException ex) {
            Logger.getLogger(EnrollmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles all the button actions.
     *
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException, IOException {
        try {
            if (event.getSource() == btnEnroll) {
                checkExist();
                System.out.println(list);

            } else if (event.getSource() == btnViewMaterial) {
                viewMaterial();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     *
     * @param event
     */
    @FXML
    private void handleMouseAction(MouseEvent event) throws SQLException, IOException {
        if (event.getSource() == btnProfile) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Profile/Profile.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            ProfileController profilec = fxmlLoader.<ProfileController>getController();
            profilec.setTrainee(trainee);
            rootpane.getChildren().setAll(root);

        } else if (event.getSource() == btnRecords) {

        } else if (event.getSource() == btnCourse) {

        }

    }

    /**
     * Shows available course list
     *
     * @throws SQLException
     */
    public void showCourse() throws SQLException {
        ObservableList<Enrollment> list = getCourseList();

        colCourseID.setCellValueFactory(new PropertyValueFactory<Enrollment, String>("courseID"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<Enrollment, String>("courseName"));
        colTrainer.setCellValueFactory(new PropertyValueFactory<Enrollment, String>("trainerName"));
        colSchedule.setCellValueFactory(new PropertyValueFactory<Enrollment, Date>("courseSche"));
        colCourseInfo.setCellValueFactory(new PropertyValueFactory<Enrollment, String>("courseInfo"));

        tvCourse.setItems(list);

    }

    /**
     * Get the list of courses.
     *
     * @return courseList
     * @throws SQLException
     */
    public ObservableList<Enrollment> getCourseList() throws SQLException {
        ObservableList<Enrollment> courseList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM course";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                enrollment = new Enrollment(rs.getString("Trainee_CourseID"), rs.getString("Course_Name"), rs.getString("Trainer_ID"), rs.getString("Course_info"), rs.getDate("Course_Schedule"));
                courseList.add(enrollment);
                list.add(rs.getString("Trainee_CourseID"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return courseList;
    }

    /**
     *
     * @return connection
     */
    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms", "root", "");
            return conn;
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    /**
     * SQL query to be inserted into database
     *
     * @throws SQLException
     */
    private void enrollCourse() throws SQLException {

        String query = "INSERT INTO enroll VALUES('" + trainee.getUserID() + "', '" + tfId.getText() + " ' )";
        executeQuery(query);
        if (state) {
            notify("success");
        }
    }

    /**
     * Insert the query into database
     *
     * @param query SQL query
     */
    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (SQLIntegrityConstraintViolationException ex) {
            notify("fail2");
            state = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Called when "View Material button is clicked"
     *
     * @throws IOException
     */
    private void viewMaterial() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Materials/TraineeMaterial.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        TraineeMaterialController controller = fxmlLoader.<TraineeMaterialController>getController();
        controller.setCourseID(tfId.getText());
        controller.setTrainee(trainee);
        rootpane.getChildren().setAll(root);
    }

    /**
     * Check if entered course is valid and not enrolled by the current trainee.
     *
     * @throws SQLException
     */
    private void checkExist() throws SQLException {
        state = true;
        if (list.contains(tfId.getText())) {
            enrollCourse();
        } else {
            notify("fail1");
        }
    }

    /**
     * Notify user
     *
     * @param s
     */
    private void notify(String s) {
        Alert alert = new Alert(AlertType.INFORMATION);

        if (s.equals("success")) {
            alert.setContentText("You have successfully enrolled the course " + tfId.getText() + "! ");
        } else if (s.equals("fail1")) {
            alert.setContentText("The course " + tfId.getText() + " doesn't exist. ");
        } else if (s.equals("fail2")) {
            alert.setContentText(" You have already enrolled the course! ");
        }

        alert.showAndWait();
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }
}
