package client.controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
//
import client.App;
import common.Tools;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.EmployeeUser;
import common.entity.InfoSystem;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * controller for manager page
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class ManagerViewPage extends AppController implements Initializable {
    public static ManagerViewPage Instance;
    protected EmployeeUser selectedEmployeeInstance;
    ObservableList<EmployeeUser> o;
    ObservableList<InfoSystem> is;
    @FXML
    private TextField searchBoxTF;

    @FXML
    private Text msg;

    @FXML
    private TableView<EmployeeUser> table;

    @FXML
    private TableColumn<EmployeeUser, String> colId;

    @FXML
    private TableColumn<EmployeeUser, String> colfullname;

    @FXML
    private TableColumn<EmployeeUser, String> colPostion;

    @FXML
    private TableColumn<EmployeeUser, String> colSystemID;


    @FXML
    private TextField EmailTf;

    @FXML
    private Text WorkerID;

    @FXML
    private Button commitee1Btn;

    @FXML
    private Button commitee2Btn;

    @FXML
    private Button SupervisorBtn;

    @FXML
    private Button removeCommiteeBtn;

    @FXML
    private TextField SurenameTf;

    @FXML
    private Button chairmanBtn;

    @FXML
    private TextField nameTf;

    @FXML
    private TextField PositionTf;
    @FXML
    private CheckBox libraryCheckbox;

    @FXML
    private CheckBox labsCheckbox;

    @FXML
    private CheckBox classComputersCheckbox;

    @FXML
    private CheckBox collageCheckbox;

    @FXML
    private CheckBox computerFarmCheckbox;

    @FXML
    private CheckBox moodleCheckbox;

    @FXML
    private CheckBox informationStationCheckbox;

    @FXML
    private Button deleteMember;

    @FXML
    private AnchorPane rightPane;

    /**
     * fetching all the IT-engineers from the DB
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>sends query to get all it employees from server</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void getDatafromServer() {
        App.client.handleMessageFromClientUI(
                new Message(OperationType.getEmployeeData, "SELECT * FROM Employees WHERE Department = 'IT';"));
    }
    /**
     * fetching all the system technician from db
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>sends query to get all it systems technicians from server</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void getSystemsTable() {
        App.client.handleMessageFromClientUI(
                new Message(OperationType.getSystemData, "SELECT `SystemID`, `username` FROM `Systems Techncian` WHERE 1 ;"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Instance = this;
        // request data from server
        showLoading(true);

        getDatafromServer();
        getSystemsTable();
        rightPane.setVisible(false);
        searchBoxTF.setVisible(true);
        // event when user click on a row
        table.setRowFactory(tv -> {
            TableRow<EmployeeUser> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    rightPane.setVisible(true);

//					stageHBox.setVisible(true);
//					progressViewLabel.setVisible(false);
                    selectedEmployeeInstance = row.getItem();
                    SupervisorBtn.setDisable(false);
                    chairmanBtn.setDisable(false);
                    commitee1Btn.setDisable(false);
                    commitee2Btn.setDisable(false);
                    Tools.fillEmployeesPanes(WorkerID, nameTf, SurenameTf, EmailTf, PositionTf, null,
                            selectedEmployeeInstance);
                    enable();
                    setCheckBoxes();
                    deleteMember.setDisable(false);
                }
            });
            return row;
        });

    }

    
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>enables the buttons on row clicked.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    public void enable() {
        libraryCheckbox.setDisable(false);
        classComputersCheckbox.setDisable(false);
        collageCheckbox.setDisable(false);
        computerFarmCheckbox.setDisable(false);
        informationStationCheckbox.setDisable(false);
        labsCheckbox.setDisable(false);
        moodleCheckbox.setDisable(false);
    }

    /**
     * 
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>sets the selected status in the page.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    public void setCheckBoxes() {
        for (InfoSystem infoSystem : is) {
            if (infoSystem.getSystemID().equals("Collage website"))
                collageCheckbox.setSelected(selectedEmployeeInstance.getUserName().equals(infoSystem.getUserName()));
        }
        for (InfoSystem infoSystem : is) {
            if (infoSystem.getSystemID().equals("Library"))
                libraryCheckbox.setSelected(selectedEmployeeInstance.getUserName().equals(infoSystem.getUserName()));
        }
        for (InfoSystem infoSystem : is) {
            if (infoSystem.getSystemID().equals("Class Computers"))
                classComputersCheckbox.setSelected(selectedEmployeeInstance.getUserName().equals(infoSystem.getUserName()));
        }
        for (InfoSystem infoSystem : is) {
            if (infoSystem.getSystemID().equals("Computer Farm"))
                computerFarmCheckbox.setSelected(selectedEmployeeInstance.getUserName().equals(infoSystem.getUserName()));
        }
        for (InfoSystem infoSystem : is) {
            if (infoSystem.getSystemID().equals("Labs"))
                labsCheckbox.setSelected(selectedEmployeeInstance.getUserName().equals(infoSystem.getUserName()));
        }
        for (InfoSystem infoSystem : is) {
            if (infoSystem.getSystemID().equals("Moodle"))
                moodleCheckbox.setSelected(selectedEmployeeInstance.getUserName().equals(infoSystem.getUserName()));
        }
        for (InfoSystem infoSystem : is) {
            if (infoSystem.getSystemID().equals("Information Station"))
                informationStationCheckbox.setSelected(selectedEmployeeInstance.getUserName().equals(infoSystem.getUserName()));
        }
    }

    public void alertMsg(Object object) {
        Boolean queryResult = (Boolean) object;
        FadeTransition ft = new FadeTransition(Duration.millis(1400), msg);
        msg.setText(queryResult ? "Done" : "Failed");
        msg.setFill(queryResult ? Color.BLUE : Color.RED);
        msg.setVisible(true);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setAutoReverse(false);
        ft.play();
        getDatafromServer();
    }

    @FXML
    void refrshBtn(MouseEvent event) {
        getDatafromServer();

        FadeTransition ft = new FadeTransition(Duration.millis(1000), msg);
        msg.setText("Refreshed...");
        msg.setFill(Color.GREEN);
        msg.setVisible(true);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setAutoReverse(false);
        ft.play();
    }

    /**
     * generic role appointment.
     *
     * @param roleInOrg
     */
    void appointment(String roleInOrg) {
        if (selectedEmployeeInstance.getRoleInOrg().equals(roleInOrg))
            return;
        if (!selectedEmployeeInstance.getRoleInOrg().equals("")) {
            showAlert(AlertType.WARNING, "Appointment failure",
                    selectedEmployeeInstance.getRoleInOrg() + " Cannot fill two roles", null);
            return;
        }

        String query1 = "UPDATE Employees SET RoleInOrg = '' WHERE RoleInOrg = '" + roleInOrg + "';";
        OperationType ot1 = OperationType.Manager_updateRoleInOrg;
        for (EmployeeUser e : o) {
            if (e.getRoleInOrg().equals(roleInOrg))
                e.setOrgRoleServerResponse("");
        }

        App.client.handleMessageFromClientUI(new Message(ot1, query1));
        query1 = "UPDATE Employees SET RoleInOrg = '" + roleInOrg + "' WHERE WorkerID = '"
                + selectedEmployeeInstance.getWorkerID() + "';";
        App.client.handleMessageFromClientUI(new Message(ot1, query1));
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
        }

    }

    public void appointment_ServerResponse(Object object) {
        boolean res = (boolean) object;
        if (res) {
            App.ForceAuthorizeAllUsers();
            //PositionTf.setText(roleInOrg);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    refrshBtn(null);
                    rightPane.setVisible(false);
                }
            });
        } else
            showAlert(AlertType.ERROR, "Error", "Role update failed", null);

    }

    @FXML
    void SupervisorBtnClicked(ActionEvent event) {
        appointment("SUPERVISOR");
    }

    @FXML
    void chairmanBtnClicked(ActionEvent event) {
        appointment("COMMITEE_CHAIRMAN");
    }

    @FXML
    void commitee1BtnClicked(ActionEvent event) {
        appointment("COMMITEE_MEMBER1");
    }

    @FXML
    void commitee2BtnClicked(ActionEvent event) {
        appointment("COMMITEE_MEMBER2");
    }

    /**
     * setting the table view of the manager page
     * @param object
     */
    @SuppressWarnings("unchecked")
    public void setDataTable(Object object) {
        // System.out.println("--> setDataTable");
        ArrayList<EmployeeUser> info = ((ArrayList<EmployeeUser>) object);
        o = FXCollections.observableArrayList(info);

        colId.setCellValueFactory(new PropertyValueFactory<>("workerID"));
        colfullname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colPostion.setCellValueFactory(new PropertyValueFactory<>("roleInOrg"));

        FilteredList<EmployeeUser> filteredData = new FilteredList<>(o, b -> true);
        searchBoxTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (employee.getWorkerID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } else if (employee.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (employee.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (employee.getRoleInOrg().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<EmployeeUser> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);
        showLoading(false);

        // table.setItems(o);
    }

    /**
     * remove role of selected Employee
     *
     * @param event
     */
    @FXML
    void deleteMembr(ActionEvent event) {
        String query = "UPDATE `Employees` SET `RoleInOrg` = '' WHERE `Employees`.`WorkerID` = '"
                + selectedEmployeeInstance.getWorkerID() + "';";
        OperationType ot = OperationType.deleteMember;
        App.client.handleMessageFromClientUI(new Message(ot, query));
        for (EmployeeUser employeeUser : o) {
            if (employeeUser.getWorkerID() == selectedEmployeeInstance.getWorkerID()) {
                employeeUser.setOrgRoleServerResponse("");
                PositionTf.setText("");
            }
        }
        table.refresh();
    }

    /**
     * generic update of DB of system data & live update of the present entities.
     *
     * @param event
     */
    @FXML
    void systemSelectionClicked(ActionEvent event) {
        String query = "UPDATE `Systems Techncian` SET `username` = '" + selectedEmployeeInstance.getUserName()
                + "' WHERE `Systems Techncian`.`SystemID` = '" + ((CheckBox) event.getTarget()).getText() + "';";
        OperationType ot = OperationType.updateSystems;
        App.client.handleMessageFromClientUI(new Message(ot, query));
        for (InfoSystem infoSystem : is) {
            if (infoSystem.getSystemID().equals(((CheckBox) event.getTarget()).getText())) {
                infoSystem.setUserName(selectedEmployeeInstance.getUserName());
                showAlert(AlertType.INFORMATION, "Select", selectedEmployeeInstance.getUserName() + " is now incharge of " + ((CheckBox) event.getTarget()).getText(), null);
            }
        }
    }

    public void getquery(Object object) {
//		boolean res = (boolean) object;
//		if (res)
//			showAlert(AlertType.INFORMATION, "selected succsses", "The selected are in the DB", null);
//		else
//			showAlert(AlertType.ERROR, "selected Fail", "Eror in the DB check again", null);
    }

    /**
     * setting of the entities ArrayList from the server.
     *
     * @param object
     */
    @SuppressWarnings("unchecked")
    public void setSystemData(Object object) {
        ArrayList<InfoSystem> infoSystem = ((ArrayList<InfoSystem>) object);
        is = FXCollections.observableArrayList(infoSystem);
    }

    @FXML
    private Pane LoadingPane;

    @FXML
    private HBox MainPane;

    void showLoading(boolean enable){
        LoadingPane.setVisible(false);
        MainPane.setVisible(false);
        LoadingPane.setVisible(enable);
        MainPane.setVisible(!enable);
    }

}
