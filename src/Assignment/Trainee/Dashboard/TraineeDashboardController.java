/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Trainee.Dashboard;

import Assignment.Profile.OwnProfileController;
import Assignment.Profile.ProfileController;
import Assignment.Records.ViewRecordController;
import Assignment.Trainee.Enrollment.Enrollment;
import Assignment.Trainee.Enrollment.EnrollmentController;
import Assignment.Trainer.Course.Course;
import Assignment.loginRegister.Trainee;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author sony
 */
public class TraineeDashboardController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private Text btnProfile;
    @FXML
    private Text btnRecords;
    @FXML
    private Text btnCourse;
    @FXML
    private AnchorPane subpane;
    @FXML
    private PieChart progressChart;
    @FXML
    private Label labelID;
    @FXML
    private Label labelName;

    Trainee trainee;
    @FXML
    private TableView<Enrollment> tvCourses;
    @FXML
    private TableColumn<Enrollment, String> colCourseID;
    @FXML
    private TableColumn<Enrollment, String> colCourseName;
    @FXML
    private TableColumn<Enrollment, Date> colSchedule;
    @FXML
    private ImageView imgProfilePic;
    @FXML
    private Button btnUploadPic;

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     */
    public void initialize(URL url, ResourceBundle rb) {
        //Thread t = new Thread(new Runnable(){
        Platform.runLater(new Runnable() {
            // TODO
            @Override
            public void run() {
                try {
                    labelID.setText(trainee.getUserID());
                    labelName.setText(trainee.getUserName());
                    try {
                        System.out.println("Show Course");
                        showCourses();
                        viewPieChart();
                        refreshProfilePic();
                    } catch (SQLException ex) {
                        Logger.getLogger(TraineeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TraineeDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        //t.start();

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

    public ObservableList<Enrollment> getCoursesList() throws SQLException {
        ObservableList<Enrollment> courseList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        Statement st;
        ResultSet rs;
        String joinQuery = "SELECT enroll.Trainee_CourseID, course.Course_name, course.Course_schedule FROM enroll INNER JOIN course "
                + "on enroll.Trainee_CourseID = course.Trainee_CourseID WHERE enroll.Trainee_ID = '" + trainee.getUserID() + "'";

        //String query = "SELECT * FROM enroll WHERE Trainee_ID = '" + trainee.getUserID() + "'";
        try {
            st = conn.createStatement();
            rs = st.executeQuery(joinQuery);
//            Course books;
            while (rs.next()) {
                Enrollment enrollment = new Enrollment(rs.getString("Trainee_CourseID"), rs.getString("Course_name"), rs.getDate("Course_schedule"));
                courseList.add(enrollment);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return courseList;
    }

    @FXML
    private void handleButtonAction(MouseEvent event) throws IOException {
        System.out.println("You have clicked the button");
        if (event.getSource() == btnCourse) {
            viewCoursePage();
        } else if (event.getSource() == btnRecords) {
            viewRecordsPage();
        } else if (event.getSource() == btnProfile) {
            viewProfilePage();
        } else if (event.getSource() == btnUploadPic) {
            try {
                uploadProfilePic();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void showCourses() throws SQLException {
        ObservableList<Enrollment> list = getCoursesList();

        colCourseID.setCellValueFactory(new PropertyValueFactory<Enrollment, String>("courseID"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<Enrollment, String>("courseName"));
        colSchedule.setCellValueFactory(new PropertyValueFactory<Enrollment, Date>("courseSche"));

        tvCourses.setItems(list);
    }

    public void viewRecordsPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Records/ViewRecord.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ViewRecordController controller = fxmlLoader.<ViewRecordController>getController();
        rootpane.getChildren().setAll(root);
    }

    public void viewCoursePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Enrollment/Enrollment.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        EnrollmentController controller = fxmlLoader.<EnrollmentController>getController();
        controller.setTrainee(trainee);
        rootpane.getChildren().setAll(root);
    }

    public void viewProfilePage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Profile/Profile.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ProfileController profilec = fxmlLoader.<ProfileController>getController();
        profilec.setTrainee(trainee);
        rootpane.getChildren().setAll(root);

    }

    public void viewPieChart() throws IOException, SQLException {
        int courseCount = 0;
        int enrollCount = 0;
        Connection conn = getConnection();
        String ct = "SELECT COUNT(*) FROM course";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(ct);
        rs.next();
        courseCount = rs.getInt(1);

        ct = "SELECT COUNT(*) FROM enroll WHERE trainee_ID = '" + trainee.getUserID() + "'";
        rs = stmt.executeQuery(ct);
        rs.next();
        enrollCount = rs.getInt(1);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("The course you enrolled", enrollCount),
                new PieChart.Data("The course you haven't enrolled", courseCount - enrollCount)
        );

        progressChart = new PieChart(pieChartData);
        progressChart.setTitle("Training Progress");
        progressChart.setClockwise(true);
        progressChart.setLabelLineLength(20);
        progressChart.setLabelsVisible(false);
        progressChart.setStartAngle(30);

        subpane.getChildren().add(progressChart);
    }

    public void uploadProfilePic() throws SQLException, FileNotFoundException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE profile SET profileImage = ? WHERE Trainee_ID = '" + trainee.getUserID() + "'");
        //PreparedStatement pstmt = conn.prepareStatement("INSERT INTO profile (profileImage) VALUES(?) WHERE Trainer_ID = '" + trainer.getUserID() + "'");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.PNG", "*.png", "*.JPEG", "*.jpg", "*.gif")
        );
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(null);
        InputStream inputStream = new FileInputStream(file);

        pstmt.setBinaryStream(1, inputStream);
        pstmt.execute();
        if (file != null) {
            Image image = new Image(file.toURI().toString(), 140, 212, true, true);
            imgProfilePic.setImage(image);
            imgProfilePic.setPreserveRatio(true);
        }
    }

    public void refreshProfilePic() throws SQLException, IOException {
        Statement st;
        ResultSet rs = null;
        Connection conn = getConnection();
        String query = "Select * FROM profile WHERE Trainee_ID = '" + trainee.getUserID() + "'";
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
