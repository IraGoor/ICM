package common.entity;

import client.App;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents the general user of our system
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private String type;


    /**
     * @param firstName
     * @param lastName
     * @param email
     * @param userName
     * @param password
     */
    public User(String firstName, String lastName, String email, String userName, String password, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.type = type;
    }

    public void setStudentPermission(Object object) {}

    public boolean isEngineer() {
        if (type.equals("Engineer"))
            return true;
        return false;
    }

    public boolean isStudent() {
        if (type.equals("Student"))
            return true;
        return false;
    }

    public boolean isOrganizationRole(OrganizationRole role) {
        return false;
    }

    public boolean isStageRole(int requestID, StageRole role) {
        return false;
    }

    public void setStageRole(int requestID, StageRole role) {
    }

    public void setStageRoleServerResponse(Object object) {
    }

    public void updatePermissions() {
    }

    public void updatePermissionsServerResponse(Object object) {
    }

    public void setOrgRoleServerResponse(Object object) {
    }


//    public void updateUserFromDB(){
//    }
//
//    public void showUpdateUserResult(Object res){
//        switch (res){
//            case (res instanceof boolean):
//
//        }
//
//        User resUser = (EmployeeUser)res;
//
//        if(!(boolean)res)
//            mainController.instance.showAlertAtMainController(Alert.AlertType.ERROR,"User Update", "User Update Failed", null);
//    }

    public String getOrgRole() {
        return "";
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return position of User
     */
    public String getPosition() {
        if (App.user instanceof EmployeeUser) {
            return "Employee";
        } else if (App.user instanceof StudentUser) {
            return "Student";
        } else
            return "User";

    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }
}
