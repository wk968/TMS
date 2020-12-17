/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Materials;

import java.sql.Blob;

/**
 *
 * @author sony
 */
public class Material {
    private String materialID;
    private String courseID;
    private String materialDescription;
    private String fileName;
    private Blob file;

    public Material(String materialID, String courseID, String materialDescription, String fileName, Blob file) {
        this.materialID = materialID;
        this.courseID = courseID;
        this.materialDescription = materialDescription;
        this.fileName = fileName;
        this.file = file;
    }

    public String getMaterialID() {
        return materialID;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }
    
    public String getFileName() {
        return fileName;
    }
}


