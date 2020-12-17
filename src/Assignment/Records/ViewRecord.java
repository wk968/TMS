/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Records;

/**
 *
 * @author user
 */
public class ViewRecord {
    private String courseID;
    private String traineeID;

    public ViewRecord(String traineeID, String courseID) {
        this.traineeID = traineeID;
        this.courseID = courseID; 
    }

    public String getCourseID() {
        return courseID;
    }

    public String getTraineeID() {
        return traineeID;
    }
    
    
}
