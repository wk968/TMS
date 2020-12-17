/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Materials;

import Assignment.Profile.ProfileController;
import Assignment.Records.ViewRecordController;
import Assignment.Trainee.Dashboard.TraineeDashboardController;
import Assignment.Trainee.Enrollment.EnrollmentController;
import Assignment.Trainer.Course.CourseController;
import Assignment.Trainer.Dashboard.Trainer_DashboardController;
import Assignment.loginRegister.Trainer;
import com.mysql.cj.protocol.Resultset;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.sql.rowset.serial.SerialBlob;

/**
 * FXML Controller class
 *
 * @author sony
 */
public class MaterialController implements Initializable {

    @FXML
    private TextField tfMaterialID;
    @FXML
    private TextField tfCourseID;
    @FXML
    private TextArea tfMaterialDescription;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnBack;
    @FXML
    private TableView<Material> tvMaterials;
    @FXML
    private TableColumn<Material, String> colMaterialID;
    @FXML
    private TableColumn<Material, String> colCourseID;
    @FXML
    private TableColumn<Material, String> colDescription;
    @FXML
    private TableColumn<Material, String> colFile;
    @FXML
    private AnchorPane rootpane;

    String courseID = "";
    @FXML
    private Button btnUpload;
    @FXML
    private Button btnDownload;
    @FXML
    private Text btnProfile;
    @FXML
    private Text btnRecords;
    @FXML
    private Text btnCourse;

    Trainer trainer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            // TODO
            @Override
            public void run() {
                try {
                    // TODO
                    showMaterials();
                } catch (SQLException ex) {
                    Logger.getLogger(MaterialController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    ; 

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
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
    
  

    public ObservableList<Material> getMaterials() throws SQLException {
        ObservableList<Material> MaterialList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM training_materials WHERE Trainee_CourseID = '" + courseID + "'";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Material material;
            while (rs.next()) {
                material = new Material(rs.getString("Material_ID"), rs.getString("Trainee_CourseID"), rs.getString("Material_Description"), rs.getString("fileName"), rs.getBlob("file"));
                MaterialList.add(material);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return MaterialList;
    }

    public void showMaterials() throws SQLException {
        ObservableList<Material> list = getMaterials();

        colMaterialID.setCellValueFactory(new PropertyValueFactory<Material, String>("materialID"));
        colCourseID.setCellValueFactory(new PropertyValueFactory<Material, String>("courseID"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Material, String>("materialDescription"));
        colFile.setCellValueFactory(new PropertyValueFactory<Material, String>("fileName"));

        tvMaterials.setItems(list);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException, IOException {
        if (event.getSource() == btnInsert) {
            try {
                insertRecord();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (event.getSource() == btnUpdate) {
            try {
                updateRecord();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (event.getSource() == btnDelete) {
            try {
                deleteRecord();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (event.getSource() == btnBack) {
            try {
                back();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (event.getSource() == btnUpload) {
            try {
                uploadFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (event.getSource() == btnDownload) {
            try {
                downloadFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void insertRecord() throws SQLException {
        System.out.println(tfMaterialID.getText());
        if (tfCourseID.getText().equals(courseID)) {
            String query = "INSERT INTO training_materials (Material_ID, Material_Description, Trainee_CourseID) VALUES('" + tfMaterialID.getText() + "','" + tfMaterialDescription.getText() + "','" + tfCourseID.getText() + "')";
            executeQuery(query);
            showMaterials();
        }
    }

    private void updateRecord() throws SQLException {
        String query = "UPDATE training_materials SET Trainee_CourseID = '" + tfCourseID.getText() + "', Material_Description = '" + tfMaterialDescription.getText() + "' WHERE Material_ID = '" + tfMaterialID.getText() + "'";
        executeQuery(query);
        showMaterials();
    }

    private void deleteRecord() throws SQLException {
        String query = "DELETE FROM training_materials WHERE Material_ID = '" + tfMaterialID.getText() + "'";
        executeQuery(query);
        showMaterials();
    }

    public void viewRecordsPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Records/ViewRecord.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ViewRecordController controller = fxmlLoader.<ViewRecordController>getController();
        controller.setTrainer(trainer);
        rootpane.getChildren().setAll(root);
    }

    public void viewProfilePage() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Profile/Profile.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        ProfileController profilec = fxmlLoader.<ProfileController>getController();
        profilec.setTrainer(trainer);
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
            Material course = tvMaterials.getSelectionModel().getSelectedItem();
            tfMaterialDescription.setText("" + course.getMaterialDescription());
            tfCourseID.setText("" + course.getCourseID());
            tfMaterialID.setText("" + course.getMaterialID());
        }
    }

    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Trainer/Dashboard/TrainerDashboard.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Trainer_DashboardController controller = fxmlLoader.<Trainer_DashboardController>getController();
        controller.setTrainer(trainer);
        rootpane.getChildren().setAll(root);
    }

    public void uploadFile() throws IOException, SQLException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new ExtensionFilter("PDF (*.PDF, *.pdf)", "*.pdf", "*.PDF"),
                new ExtensionFilter("All Files", "*.*"));
        File pdfFile = fileChooser.showOpenDialog(null);

        //File pdfFile = new File(selectedFile);
        byte[] pdfData = new byte[(int) pdfFile.length()];
        try (
                DataInputStream dis = new DataInputStream(new FileInputStream(pdfFile))) {
            dis.readFully(pdfData);  // read from file into byte[] array
        } // read from file into byte[] array

        Connection dbConnection = getConnection();
        PreparedStatement ps = dbConnection.prepareStatement(
                "INSERT INTO training_materials (Material_ID, Material_Description, Trainee_CourseID, file, fileName ) VALUES (?,?,?,?,?)");
        ps.setString(1, tfMaterialID.getText());
        ps.setString(2, tfMaterialDescription.getText());
        ps.setString(3, tfCourseID.getText());
        ps.setBytes(4, pdfData);  // byte[] array
        ps.setString(5, tfCourseID.getText() + " Material");
        ps.executeUpdate();
        showMaterials();
    }

    public void downloadFile() throws IOException, SQLException {

        Connection dbConnection = getConnection();
        ResultSet rs = null;
        PreparedStatement ps = dbConnection.prepareStatement(
                "SELECT file FROM training_materials WHERE Material_ID= '" + tfMaterialID.getText() + "'");
        File file = new File(tfCourseID.getText() + " Material.txt");
        FileOutputStream output = new FileOutputStream(file);

        System.out.println("Writing to file " + file.getAbsolutePath());
        rs = ps.executeQuery();
        while (rs.next()) {
            InputStream input = rs.getBinaryStream("file");
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {
                output.write(buffer);
            }
        }
    }

}
