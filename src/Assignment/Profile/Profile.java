/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment.Profile;

/**
 *
 * @author Adm
 */
public class Profile {
    private String Name;
    private int Age;
    private String Gender;
    private String Email;
    private String Contact;

    public Profile(String Name, int Age, String Gender, String Email, String Contact) {
        this.Name = Name;
        this.Age = Age;
        this.Gender = Gender;
        this.Email = Email;
        this.Contact = Contact;
    }

    public String getName() {
        return Name;
    }

    public int getAge() {
        return Age;
    }

    public String getGender() {
        return Gender;
    }

    public String getEmail() {
        return Email;
    }

    public String getContact() {
        return Contact;
    }

    
}
