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
public class Trainee extends User {

    public Trainee(String profileID, String userName, int userAge, String userGender, String userEmail, String userContact, String userID) {
        super(profileID, userName, userAge, userGender, userEmail, userContact, userID);//To change body of generated methods, choose Tools | Templates.
        super.setTrainer(false);
    }

    
    @Override
    public void setUserID(String userID){
        super.setUserID(userID);
    }
    
     public boolean getTrainer(boolean isTrainer){
        return super.getTrainer();
    }
    
    public String getUserID() {
        return super.getUserID();
    }

    public void setUserName(String userName) {
        super.setUserName(userName);
    }

    public void setUserAge(int userAge) {
        super.setUserAge(userAge);
    }

    public void setUserGender(String userGender) {
        super.setUserGender(userGender);
    }

    public void setUserEmail(String userEmail) {
        super.setUserEmail(userEmail);
    }

    public void setUserContact(String userContact) {
        super.setUserEmail(userContact);
    }

    public String getUserName() {
        return super.getUserName();
    }

    public int getUserAge() {
        return super.getUserAge();
    }

    public String getUserGender() {
        return super.getUserGender();
    }

    public String getUserEmail() {
        return super.getUserEmail();
    }

    public String getUserContact() {
        return super.getUserContact();
    }
    
}
