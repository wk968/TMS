/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Records;

import Assignment.Profile.ProfileController;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import Assignment.Trainer.Course.CourseController;
import Assignment.Trainer.Dashboard.Trainer_DashboardController;
import Assignment.loginRegister.Trainer;
import javafx.scene.Parent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ViewRecordController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private TableView<ViewRecord> tvViewRecord;
    @FXML
    private Button btnBack;
    @FXML
    private TableColumn<ViewRecord, String> colCourseID;
    @FXML
    private TableColumn<ViewRecord, String> colTraineeID;

    /**
     * Initializes the controller class.
     */
    Trainer trainer;
    public boolean showtrainer;
    @FXML
    private Text btnProfile;
    @FXML
    private Text btnCourse;

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        showtrainer = true;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showEnrollList();
        } catch (SQLException ex) {
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == btnBack) {
            back();
        }
    }

    @FXML
    private void handleMouseAction(MouseEvent event) throws IOException {
        if (event.getSource() == btnCourse) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainer/Course/CoursePage.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            CourseController viewc = fxmlLoader.<CourseController>getController();
            viewc.setTrainer(trainer);
            rootpane.getChildren().setAll(root);
        } else if (event.getSource() == btnProfile) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Profile/Profile.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            ProfileController profilec = fxmlLoader.<ProfileController>getController();
            profilec.setTrainer(trainer);
            rootpane.getChildren().setAll(root);

        }
    }

    public void showEnrollList() throws SQLException {
        ObservableList<ViewRecord> list = getEnrollList();

        colCourseID.setCellValueFactory(new PropertyValueFactory<ViewRecord, String>("courseID"));
        colTraineeID.setCellValueFactory(new PropertyValueFactory<ViewRecord, String>("traineeID"));

        tvViewRecord.setItems(list);
        System.out.println(list);
    }

    public ObservableList<ViewRecord> getEnrollList() throws SQLException {
        ObservableList<ViewRecord> enrollList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM enroll";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            ViewRecord record;
            while (rs.next()) {
                record = new ViewRecord(rs.getString("Trainee_ID"), rs.getString("Trainee_CourseID"));
                enrollList.add(record);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return enrollList;
    }

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

    private void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainer/DashBoard/TrainerDashBoard.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Trainer_DashboardController ownprofilec = fxmlLoader.<Trainer_DashboardController>getController();
        ownprofilec.setTrainer(trainer);
        rootpane.getChildren().setAll(root);
    }

}
