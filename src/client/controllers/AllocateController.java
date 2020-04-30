package client.controllers;

import client.App;
import common.Tools;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.ChangeRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


/**
 * 
 * <h2>Controller for Allocate page</h2>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */

public class AllocateController extends AppController implements Initializable {
    public static AllocateController instance;
    private String evaluator; 
    protected ChangeRequest thisRequest;
    @FXML
    private ResourceBundle resources;

    @FXML
    private Text requestNumberTXT;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> cbEvaluator;

    @FXML
    private ComboBox<String> cbExecuter;


    @FXML
    private Text txtWarning;

    @FXML
    private TextField inchargeTF;

    @FXML
    private Text departmentID;

    @FXML
    private Text requestID;

    @FXML
    private TextArea existingCondition;

    @FXML
    private TextArea descripitionsTextArea;


    @FXML
    private Text requestNameLabel;

    @FXML
    private Spinner<Integer> evaluationTime;

    @FXML
    private Spinner<Integer> decisionTime;

    @FXML
    private Spinner<Integer> executionTime;

    @FXML
    private Spinner<Integer> ValidationTime;

    private static int c = 0;



    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Responsible for interpreting supervisor allocation for the request.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event Mouse pressed on submit
     */
    @FXML
    void submitForm(ActionEvent event) {
        c = 0;
        boolean init = thisRequest.getCurrentStage().equals("INIT");
        String query;
        if (cbEvaluator.getValue() == null || cbExecuter.getValue() == null) {
            txtWarning.setVisible(true);
            return;
        }
        OperationType ot2 = OperationType.Allocate_UpdateRoles;

        if (init) {
            OperationType ot = OperationType.Allocate_SetRoles;
            query = "INSERT INTO Stage (RequestID,StageName,Incharge) VALUES " +
                    "('" + thisRequest.getRequestID() + "','EVALUATION','" + cbEvaluator.getValue() + "')," +
                    "('" + thisRequest.getRequestID() + "','DECISION','" + "" + "')," +
                    "('" + thisRequest.getRequestID() + "','EXECUTION','" + cbExecuter.getValue() + "')," +
                    "('" + thisRequest.getRequestID() + "','VALIDATION','" + "" + "')," +
                    "('" + thisRequest.getRequestID() + "','CLOSURE','" + "" + "')";
            App.client.handleMessageFromClientUI(new Message(ot, query));

            c = 1;
            String query2 = "UPDATE Requests SET Treatment_Phase = 'EVALUATION', Status ='ACTIVE' WHERE RequestID = '"
                    + thisRequest.getRequestID() + "'";
            App.client.handleMessageFromClientUI(new Message(ot2, query2));
            App.ForceAuthorizeAllUsers();
        } else {
            c = 2;
            query = "UPDATE Stage SET Incharge = '" + cbEvaluator.getValue() + "' WHERE StageName = 'EVALUATION' AND RequestID = '" + thisRequest.getRequestID() + "';";
            App.client.handleMessageFromClientUI(new Message(ot2, query));
            query = "UPDATE Stage SET Incharge = '" + cbExecuter.getValue() + "' WHERE StageName = 'EXECUTION' AND RequestID = '" + thisRequest.getRequestID() + "';";
            App.client.handleMessageFromClientUI(new Message(ot2, query));

        }
        loadPage("requestTreatment");
    }

    /**
     *
     * @param object
     * @apiNote this function is the response from the server  of the query updates from submitForm function
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>respoond to info for server
     * <ul>
     * <li>Show alert whether the allocation was successful or not </li>
     * <li>if was successful load request treatment page</li>
     * </ul></p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void allocQueryResult(Object object) {
        c--;
        boolean res = (boolean) object;
        if (c == 0 || !res) {
            if (res) {
                showAlert(AlertType.INFORMATION, "Allocation Approved", "The in-charges of the request were assigned", null);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadPage("requestTreatment", "Request Treatment and Management");
                    }
                });
            } else
                showAlert(AlertType.ERROR, "Error!", "Could not assign employees", null);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	txtWarning.setVisible(false);
        instance = this;
        thisRequest = requestTreatmentController.Instance.getCurrentRequest();
        Tools.fillRequestPanes(requestID, existingCondition, descripitionsTextArea, inchargeTF, departmentID, null, requestNameLabel, thisRequest);
        getUsersFromServer();
        this.requestNumberTXT.setText("Request Number "+thisRequest.getRequestID());
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>
     * 
     * Sends Querry to server asks for all the engineers that available on the SQL database, this function is called via the initialize function.
     * 
     * 
     * 
     * </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void getUsersFromServer() {
        OperationType ot = OperationType.Allocate_GetITUsers;
        String query = "SELECT * FROM Employees WHERE Type = 'Engineer' AND USERNAME != '" + App.user.getUserName() + "'";
        String query2 = "SELECT `username` FROM `Systems Techncian` WHERE SystemID='"+thisRequest.getInfoSystem()+"'";

        OperationType ot2 = OperationType.Allocate_System_Incharge;
        App.client.handleMessageFromClientUI(new Message(ot2, query2));
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>client response from server via Allocate_GetITUsers:
     * <ul>
     * <li>Object is converted to "List<String>" which is loaded on the Allocate Incharges page.</li>
     * <li>Loads the data of listOfUsers into the combobox on the screens.</li>
     * </ul>
     * </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object : (Object) data received from query sent to server.
     */
    public void setComboBoxesData(Object object) {
        List<String> listOfUsers = (List<String>) object;
        ObservableList<String> oblist = FXCollections.observableArrayList(listOfUsers);
        int size = oblist.size();
        Random r = new Random();

        cbEvaluator.setItems(oblist);
        cbExecuter.setItems(oblist);
        new AutoCompleteBox<String>(cbEvaluator);
        new AutoCompleteBox<String>(cbExecuter);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cbEvaluator.getSelectionModel().select(evaluator);
            }
        });
    }

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Set from DB evaluator to be the one in charge of the system</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object: (Object) evaluator info
     */
    public void setEvaluuator(Object object)
    {
    	evaluator=(String)object;
    }



    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from the server, shows alert with role appointment result
     *  <ul><li>response from Allocate_SetRoles</li></ul></p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object :Object of a boolean for the result that came from server
     */
    public void showResult(Object object) {
        boolean res = (boolean) object;
        showAlert(Alert.AlertType.INFORMATION, "Role Appointment", res ? "Done!" : "Failed", null);
    }
}

