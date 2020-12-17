/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.loginRegister;

/**
 *
 * @author sony
 */
abstract class User {

    String userName;
    int userAge;
    private String userGender;
    String userEmail;
    private String userContact;
    String userID;
    private String profileID;
    boolean isTrainer;

        public User(String profileID, String userName, int userAge, String userGender, String userEmail, String userContact, String userID) {
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.profileID = profileID;
        this.userID = userID;
    }
        
    public void setTrainer(boolean isTrainer){
        this.isTrainer = isTrainer;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }
    
    public String getProfileID() {
        return profileID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserEmail() {
        return userEmail;
    }
    
    public boolean getTrainer(){
        return isTrainer;
    }

    public String getUserContact() {
        return userContact;
    }

}
