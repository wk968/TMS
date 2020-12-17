/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Records;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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

/**
 * FXML Controller class
 *
 * @author user
 */
public class ViewScheduleController implements Initializable {

    @FXML
    private AnchorPane rootpane;
    @FXML
    private TableView<ViewSchedule> tvViewSchedule;
    @FXML
    private TableColumn<ViewSchedule, String> colCourseID;
    @FXML
    private TableColumn<ViewSchedule, Date> colSchedule;
    @FXML
    private Button btnBack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showEnrollList();
        } 
        catch (SQLException ex) {
            Logger.getLogger(ViewScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        if (event.getSource() == btnBack){
            back();
        }
    }
    
    @FXML
    private void handleMouseAction(MouseEvent event) {
    }
    
    public void showEnrollList() throws SQLException{
        ObservableList<ViewSchedule> list = getEnrollList();
        
        colCourseID.setCellValueFactory(new PropertyValueFactory<ViewSchedule, String>("courseID"));
        colSchedule.setCellValueFactory(new PropertyValueFactory<ViewSchedule, Date>("courseSche"));
        
        tvViewSchedule.setItems(list);
        System.out.println(list);
    }
    
    public ObservableList<ViewSchedule> getEnrollList() throws SQLException{
        ObservableList<ViewSchedule> enrollList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM testenroll";
        Statement st;
        ResultSet rs;
        
        //to be fixed
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            ViewSchedule sche;
            while(rs.next()){
                sche = new ViewSchedule(rs.getString("CourseID_test"), rs.getDate("Trainee_ID_test"));
                enrollList.add(sche);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return enrollList;
    }
    
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tms","root","");
            return conn;
        }catch(SQLException ex){
            System.out.println("Error: "+ex.getMessage());
            return null;
        }
    }
    
    private void back() throws IOException{
        AnchorPane root = FXMLLoader.load(getClass().getResource("Enrollment.fxml"));
        rootpane.getChildren().setAll(root);
    }
    
}
