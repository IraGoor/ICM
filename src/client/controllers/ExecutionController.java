package client.controllers;

import client.App;
import common.Tools;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.ChangeRequest;
import common.entity.OrganizationRole;
import common.entity.StageRole;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */

/**
 * 
 * <h2>Execution phase page Controller</h2>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class ExecutionController extends AppController implements Initializable {

    /*
     * this static variable is supposed to hold all the data of the request chosen
     * in request treatment
     */
    // public static ChangeRequest thisRequest;
    public static ExecutionController instance;

    protected ChangeRequest thisRequest;

    @FXML
    private Text titledPane_daysLeft;

    @FXML
    private Text requestNumberTXT;

    @FXML
    private Text idText;

    @FXML
    private Text requestID;

    @FXML
    private TextArea existingCondition;

    @FXML
    private TextArea descripitionsTextArea;

    @FXML
    private Text msg;

    @FXML
    private TextField inchargeTF;

    @FXML
    private Text departmentID;

    @FXML
    private Text idText1;

    @FXML
    private Text requestNameLabel;

    @FXML
    private Text idText2;

    @FXML
    private Text dueDateLabel;

    @FXML
    private AnchorPane rightPane;

    @FXML
    private Pane pane_form;

    @FXML
    private AnchorPane returnedNoteAP;

    @FXML
    private TextArea returnedNotes;

    @FXML
    private TitledPane titledPane;

    @FXML
    private Text titledPane_Text;

    @FXML
    private Button workDone;

    @FXML
    private Pane pane_msg;

    @FXML
    private Text textInMsgPane;

    @FXML
    private Button btnRequestExtension;

    @FXML
    private Button btnAnswerStageExtensionRequest;

    private String reportResult;


    private boolean responseSupervisor = false; // this provide if the supervisor agree or not.

    static LocalDate saveAfterResponse;

    private common.entity.Stage thisStage;
    long estimatedTime = 0;

    /**
     *
     * @param location
     * @param resources
     * @apiNote initialization of the execution screen
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        thisRequest = requestTreatmentController.Instance.getCurrentRequest();
        thisStage = thisRequest.getCurrentStageObject();
        this.requestNumberTXT.setText("Request Number "+thisRequest.getRequestID());
        btnRequestExtension.setVisible(false);
        btnAnswerStageExtensionRequest.setVisible(false);
        if (thisStage.getExtension_reason() != null)
            btnAnswerStageExtensionRequest.setVisible(true);

        pane_msg.setVisible(false);
        pane_form.setVisible(false);
        titledPane.setCollapsible(false);
        titledPane.setText("Waiting for your action");
        inchargeTF.setText(thisStage.getIncharge());
        Tools.fillRequestPanes(requestID, existingCondition, descripitionsTextArea, inchargeTF, departmentID,
                dueDateLabel, requestNameLabel, thisRequest);
        checkPreConditions();
        reportNoteUpdater();
        setExtensionVisability();
        if (!thisRequest.getCurrentStage().equals("EXECUTION")) {
            pane_msg.setVisible(true);
            inchargeTF.setText(thisStage.getIncharge());
            return;
        }

        if (!App.user.isStageRole(thisRequest.getRequestID(), StageRole.EXECUTER)
                && !App.user.isOrganizationRole(OrganizationRole.SUPERVISOR)) {
            textInMsgPane.setFill(Color.BLUE);
            textInMsgPane.setText("Stage in progress");
            pane_msg.setVisible(true);
            return;
        }


        // the supervisor should present the stage in progress after he approved the
        // time
        if (App.user.isOrganizationRole(OrganizationRole.SUPERVISOR)) {
            textInMsgPane.setFill(Color.BLUE);
            textInMsgPane.setText("Stage in progress");
            pane_msg.setVisible(true);
            return;
        }

        // Otherwise: this is the Executer in his stage
        if (thisRequest.isReturned()) {
            returnedNoteAP.setVisible(true);
            returnedNotes.setText(thisRequest.getReturnedNote());
        }

        pane_form.setVisible(true);
        inchargeTF.setText(thisRequest.getCurrentStageObject().getIncharge() + "");

    }



    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Checks if there is any available failure report for this current request on DB.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void reportNoteUpdater() {
        OperationType ot = OperationType.EXECUTION_GetFailReport;
        String query = "SELECT * FROM `Execution Failure Report`  WHERE `RequestID` = " + thisRequest.getRequestID() + " LIMIT 1";
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }



    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, if there is any available failure report for this current request on DB<br/>
     *  then show the report, otherwise don't show the report.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void getReport_ServerResponse(Object object) {
        this.reportResult = (String) object;
        if (reportResult == null) {
            returnedNoteAP.setVisible(false);
        } else { // if the returned result was back
            returnedNoteAP.setVisible(true);
            returnedNotes.setText(reportResult);
        }
    }

    /**
     * 
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Sends query to server to checks if pre - execution step is complete </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void checkPreConditions() {
        OperationType ot = OperationType.EXE_GetInitData;
        String query = "SELECT `init`,`init_confirmed` FROM `Stage` WHERE `RequestID` = '" + thisRequest.getRequestID()
                + "' AND `StageName` = 'EXECUTION'";
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }

    /**
     *
     * @param object
     * client response from server, if the pre execution stage is complete then the execution stage will start
     * otherwise the pre-execution stage is starting.
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check phase pre-conditions (query sent via checkPreConditions)<br/>
     *  if the pre execution stage is complete then the execution stage will start<br/>
     *  else the pre-execution stage is starting.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) List<Boolean> list of pre conditions results
     */
    public void checkPreConditions_ServerResponse(Object object) {
        List<Boolean> init_res = (List<Boolean>) object;
        boolean init = init_res.get(0);
        boolean init_confirmed = init_res.get(1);

        if (init_confirmed && init) {
            init();
            titledPane_daysLeft.setVisible(true);
            estimatedTime = Duration.between(ZonedDateTime.now(), thisRequest.getCurrentStageObject().getDeadline())
                    .toDays();
            Tools.setTitlePane(estimatedTime, titledPane, titledPane_daysLeft);
            rightPane.setVisible(true);
            return;
        }
        // else
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                loadPage("PreExecution");
            }
        });
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Initialize the execution screen</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void init() {

        if (App.user.isOrganizationRole(OrganizationRole.SUPERVISOR)) {
            workDone.setVisible(true);// Change to false once you deal with permissions
            titledPane_daysLeft.setVisible(false);
            if (responseSupervisor) {
                titledPane.getStyleClass().remove("danger");
                titledPane.getStyleClass().add("success");
                titledPane.setCollapsible(false);
                titledPane.setText("This stage is done.");
                titledPane_Text.setText("You have only a viewing permission.");
                titledPane_Text.setFill(Color.FORESTGREEN);
                titledPane_Text.setVisible(true);
                workDone.setVisible(false);

                if (!thisRequest.getCurrentStage().equals("EXECUTION")) { // Watching only
                    titledPane.getStyleClass().remove("danger");
                    titledPane.getStyleClass().add("success");
                    titledPane.setCollapsible(false);
                    titledPane.setText("This stage is done.");
                    titledPane_Text.setText("You have only a viewing permission.");
                    titledPane_Text.setFill(Color.FORESTGREEN);
                    titledPane_Text.setVisible(true);
                    workDone.setVisible(false);
                }
            }
        }
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Execution submitting, sends quries to update stage phase in DB.<br/><br/>
     * note: submit of the executer after the supervisor click agree. </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event approve Button was Clicked
     */
    @FXML
    void submitWorkDone(ActionEvent event) {
        c=0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        Date today = new Date(System.currentTimeMillis());
        c.setTime(today);
        c.add(Calendar.DATE, 7);
        Date deadlineDate = c.getTime();

        String query1 = "UPDATE Requests SET Treatment_Phase = 'VALIDATION' WHERE RequestID = '"
                + thisRequest.getRequestID() + "';";
        String query2 = " UPDATE `Stage` SET init = 0, init_confirmed = 0, `EndTime` = '" + dateFormat.format(today)
                + "' where  `StageName` = 'EXECUTION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";

        OperationType ot1 = OperationType.EXE_UpdateDB;
        App.client.handleMessageFromClientUI(new Message(ot1, query1));
        App.client.handleMessageFromClientUI(new Message(ot1, query2));
        showAlert(AlertType.INFORMATION, "Request #" + thisRequest.getRequestID() + " Execution complete!",
                "The request move forward to Validation Stage\nPlease Notify the committee chairman.", null);
        loadPage("requestTreatment");
    }

    private static int c = 0;

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check phase update (via submitWorkDone)<br/>
     * if successful  the screen will return to request Treatment screen.<br/>
     * else show an error alert  </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void queryResult(Object object) {
        c++;
        boolean res = (boolean) object;
        if (c == 2) {
            if (res) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(AlertType.INFORMATION, "Update Success!", "Execution finished\nNotify the committee-chairman to resume the process", null);
                        loadPage("requestTreatment");
                    }
                });
            } else
                showAlert(AlertType.ERROR, "Error!", "Execution update failed!!!", null);
        }
    }

    /**
     * Extension option visible only if day difference is bigger then -3
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>enable extension button based on conditions</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void setExtensionVisability() {
        btnRequestExtension.setVisible(false);
        long daysDifference = Tools.DaysDifferenceFromToday(thisRequest.getCurrentStageObject().getDeadline());
        if (daysDifference >= -3) {
            btnRequestExtension.setVisible(true);
            if (thisStage.getExtension_days() != 0)
                btnRequestExtension.setDisable(true);
        }

    }

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>open extension window.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event
     */
    @FXML
    void requestExtension(ActionEvent event) {
        start(new Stage());
    }
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>loads extension window</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param primaryStage
     */
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/client/views/Extension.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Extension");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Could not load execution prompt");
            e.printStackTrace();
        }
    }
}

