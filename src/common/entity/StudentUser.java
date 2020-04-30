/**
 *
 */
package common.entity;

import client.App;
import client.controllers.mainController;
import common.controllers.Message;
import common.controllers.OperationType;
import javafx.application.Platform;

/**
 * Represents a student in the system
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class StudentUser extends User {

    private int userID;
    private String faculty;

    /**
     * Student Constructor
     * @param firstName
     * @param lastName
     * @param email
     * @param userName
     * @param password
     */
    public StudentUser(String firstName, String lastName, String email, String userName, String password, int userID, String faculty) {
        super(firstName, lastName, email, userName, password, "Student");
        this.faculty = faculty;
        this.userID = userID;
    }

    /**
     * Student has no special permissions / roles in the system at this version.
     * this implementation is for potential features.
     */
    @Override
    public void updatePermissions() {
        OperationType ot2 = OperationType.User_getStudentAccess;
        App.client.handleMessageFromClientUI(new Message(ot2, this));
    }

    /**
     * Student has no special permissions / roles in the system at this version.
     * this implementation is for potential features.
     */
    @Override
    public void setStudentPermission(Object object) {
        User student = (User) object;
        if (student == null)
            return;

        if (mainController.instance != null) {
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    mainController.instance.initialize_afterUserUpdate();
                }
            });

        }
    }
}
