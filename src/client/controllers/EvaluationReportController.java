package client.controllers;

import client.App;
import common.Tools;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.ChangeRequest;
import common.entity.EvaluationReport;
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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;



/**
 * 
 * <h2>Evaluation Report page Controller</h2>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class EvaluationReportController extends AppController implements Initializable {

    /*
     * this static variable is supposed to hold all the data of the request chosen
     * in request treatment
     */
    // public static ChangeRequest thisRequest;
    public static EvaluationReportController instance;
    private common.entity.Stage thisStage;
    protected ChangeRequest thisRequest;

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
    private Pane Pane_Form;

    @FXML
    private Button SbmtEvlBtn;

    @FXML
    private DatePicker timeEvlBox;

    @FXML
    private TextArea reqChngTXT;

    @FXML
    private TextArea expResTXT;

    @FXML
    private TextArea cnstrntTXT;

    @FXML
    private TitledPane titledPane;

    @FXML
    private Text titledPane_Text;

    @FXML
    private Button btnRequestExtension;

    @FXML
    private Pane Pane_locked;

    @FXML
    private Text txt_locked;

    @FXML
    private Button btnAnswerStageExtensionRequest;
    @FXML
    private Text titledPane_daysLeft;
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>
     * <ul>
     * <li>check submission conditions</li>
     * <li>send server queries to update DB</li>
     * </ul>  </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event : submit button was clicked.
     */
    @FXML
    void SbmtEvlBtnClick(ActionEvent event) {
        c2 = 0;
        if (departmentID.getText().isEmpty() || reqChngTXT.getText().isEmpty() || expResTXT.getText().isEmpty()
                || timeEvlBox.getValue() == null) {
            showAlert(AlertType.WARNING, "EvaluationReport", "You must fill all required fields.", null);
            return;
        }

        String systemID, requiredChange, expectedResult, expectedRisk, estimatedTime;
        systemID = this.departmentID.getText();
        requiredChange = this.reqChngTXT.getText();
        expectedResult = this.expResTXT.getText();
        expectedRisk = this.cnstrntTXT.getText();
        LocalDate date = this.timeEvlBox.getValue();
        estimatedTime = date.toString();


        String query1 = "INSERT INTO `EvaluationReports` (`RequestID`, `System_ID`, `Required_Change`, `Expected_Result`, `Expected_Risks`, `Estimated_Time`) VALUES ("

                + "'" + thisRequest.getRequestID() + "', " + "'" + systemID + "', " + "'" + requiredChange + "', " + "'"
                + expectedResult + "', " + "'" + expectedRisk + "', " + "'" + estimatedTime + "');";

        OperationType ot1 = OperationType.InsertEvaluation;
        App.client.handleMessageFromClientUI(new Message(ot1, query1));

        String query2 = "UPDATE Requests SET Treatment_Phase = 'DECISION', Due_Date = '" + estimatedTime + "' WHERE RequestID = '"
                + thisRequest.getRequestID() + "'";
        OperationType ot2 = OperationType.Eval_updateRequestStatus;
        App.client.handleMessageFromClientUI(new Message(ot2, query2));
        // showAlert(AlertType.INFORMATION, "Evaluation Approved", "Request moved to
        // execution phase...", null);
        // loadPage("requestTreatment");
    }

    private static int c2 = 0;

    /**
     * @param object
     * @apiNote client response from Server, this function checks of the update of SbmtEvlBtnClick was updated successfully
     * if it was updated successfully then the screen will return to request Treatment screen, else it will show an error
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check update (SbmtEvlBtnClick)<br/>
     * if was successsful return to request Treatment screen <br/>
     * else show an error alert.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void evaluationStageUpdateQueryResult(Object object) {
        c2++;
        boolean res = (boolean) object;
        if (c2 == 1) {
            if (res) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadPage("requestTreatment");
                    }
                });
            } else
                showAlert(AlertType.ERROR, "Error!", "Could not update treatment phase", null);
        }
    }

    /**
     * @param location
     * @param resources
     * @apiNote initialization of the Evaluation screen
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        thisRequest = requestTreatmentController.Instance.getCurrentRequest();
        thisStage = thisRequest.getCurrentStageObject();
        this.requestNumberTXT.setText("Request Number " + thisRequest.getRequestID());
        titledPane_daysLeft.setVisible(false);
        SbmtEvlBtn.setVisible(false);
        rightPane.setVisible(false);
        Pane_Form.setVisible(false);
        Pane_locked.setVisible(false);
        dueDateLabel.setDisable(true);
        dueDateLabel.setVisible(false);
        titledPane.setCollapsible(false);
        titledPane.setText("Welcome");
        btnAnswerStageExtensionRequest.setVisible(false);
        btnRequestExtension.setVisible(false);
        if (thisStage.getExtension_reason() != null)
            btnAnswerStageExtensionRequest.setVisible(true);
        inchargeTF.setText(thisStage.getIncharge());
        Tools.fillRequestPanes(requestID, existingCondition, descripitionsTextArea, inchargeTF, departmentID,
                dueDateLabel, requestNameLabel, thisRequest);
        formInit();
        if (!thisRequest.getCurrentStage().equals("EVALUATION")) {
            Pane_Form.setVisible(true);
            rightPane.setVisible(true);
            dueDateLabel.setDisable(true);
            inchargeTF.setText(thisStage.getIncharge());
            return;
        } else { // in Eval Stage
            if (thisStage.getInit_confirmed() == 1 && thisStage.getInit() == 1)
                rightPane.setVisible(true);
            else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadPage("PreEvaluation");
                    }
                });
                return;
            }

            if (App.user.isStageRole(thisRequest.getRequestID(), StageRole.EVALUATOR)) { // EVALUATOR
                SbmtEvlBtn.setVisible(true);
                Pane_Form.setVisible(true);
                titledPane_daysLeft.setVisible(true);
                long estimatedTime = Duration
                        .between(ZonedDateTime.now(), thisRequest.getCurrentStageObject().getDeadline()).toDays();

                Tools.setTitlePane(estimatedTime, titledPane, titledPane_daysLeft);

            } else { // NOT EVALUATOR
                Pane_locked.setVisible(true);
                if (thisStage.getExtension_reason() != null)
                    btnRequestExtension.setVisible(true);

            }
            inchargeTF.setText(thisRequest.getCurrentStageObject().getIncharge() + "");
        }

    }

    /**
     * initialize the titlePane on the screen, it will changed its color to green
     * and finally calls setFieldData method
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>initialize the titlePane on the screen, it will changed its color to green.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void formInit() {
        setFieldsData();
        if (!thisRequest.getCurrentStage().equals("EVALUATION")) { // Watching only
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    titledPane.getStyleClass().remove("danger");
                    titledPane.getStyleClass().add("success");
                    titledPane.setText("This stage is done.");
                    SbmtEvlBtn.setVisible(false);
                    titledPane_Text.setText("You have only a viewing permission.");
                    titledPane_Text.setFill(Color.FORESTGREEN);
                    titledPane_Text.setVisible(true);
                    reqChngTXT.setEditable(false);
                    expResTXT.setEditable(false);
                    cnstrntTXT.setEditable(false);
                    timeEvlBox.setEditable(false);
                }
            });

        } else { // currently doing this stage
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    dueDateLabel.setDisable(false);
                    titledPane.getStyleClass().remove("danger");
                    titledPane.getStyleClass().add("info");

                    titledPane.setText("Fill in Evaluation report.");
                    titledPane_Text.setText("After you submit the form, the evaluation will go to decision stage. ");

                    setExtensionVisability();
                }
            });

        }

    }

    /**
     * loads evaluation report form DB
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>send query to server get evaluation report from DB</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void setFieldsData() {
        OperationType ot = OperationType.EVAL_GetAllReportsByRID;
        String query = "SELECT * FROM `EvaluationReports` WHERE RequestID = " + thisRequest.getRequestID()
                + " ORDER BY Report_ID DESC LIMIT 1";
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }

    /**
     * @param object client response from server for setFieldData, it will load the recent evaluation report on screen
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p></p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void setFieldsData_ServerResponse(Object object) {
        ArrayList<EvaluationReport> reports = (ArrayList<EvaluationReport>) object;
        if (reports.size() > 0) {
            if (thisRequest.getCurrentStage().equals("EVALUATION")) {
//				Pane_Form.setDisable(true);
//				titledPane_Text.setVisible(true);
//
//				titledPane_Text.setText("Evaluation report has been sent. Waiting for supervisor decision.");
//				btnRequestExtension.setVisible(false);
//				btnAnswerStageExtensionRequest.setVisible(false);
//				SbmtEvlBtn.setVisible(false);

            }
            EvaluationReport individualReport = reports.get(0);
            reqChngTXT.setText(individualReport.getRequired_change());
            expResTXT.setText(individualReport.getExpected_result());
            cnstrntTXT.setText(individualReport.getExpected_risks());
            timeEvlBox.setValue(individualReport.getEstimated_time().toLocalDate());
        }

    }

    /**
     * @param object client response from server, submit button was pressed and it is updating the dates and stages of the request
     */
    
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server:<br/>
     * 
     * Check Evaluation report insert into update. 
     * <ul>
     * <li>if successful sends queries to the server, to update stage phase and  save old stage in DB.  </li>
     * <li>else show error alert </li>
     * </ul> </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) boolean result of update in DB
     */

    public void insertNewRequestResult(Object object) {
        boolean res = (boolean) object;

        if (res) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Calendar c = Calendar.getInstance();
            Date today = new Date(System.currentTimeMillis());
            c.setTime(today);
            c.add(Calendar.DATE, 7);
            Date deadlineDate = c.getTime();

            String query1 = " UPDATE `Stage` SET  `EndTime` = '" + dateFormat.format(today)
                    + "' where  `StageName` = 'EVALUATION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
            String query2 = " UPDATE `Stage` SET  `StartTime` = '" + dateFormat.format(today) + "' , `Deadline` = '"
                    + dateFormat.format(deadlineDate) + "' where  `StageName` = 'DECISION' AND `RequestID` = '"
                    + thisRequest.getRequestID() + "';";

            OperationType ot1 = OperationType.EVAL_UpdateDB;
            App.client.handleMessageFromClientUI(new Message(ot1, query1));
            App.client.handleMessageFromClientUI(new Message(ot1, query2));
        } else
            showAlert(AlertType.ERROR, "Error in Evaluation!", "Could not update DB", null);
    }

    private static int c = 0;


    /**
     * 
     *
     * <h3> Method Purpose</h3>
     * <p>Response from server, check stage phase update from insertNewRequestResult <br/>
     * if successful return to request treatment page and show success alert<br/>
     * else show an error alert
     * 
     * </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) boolean result of update in DB
     */
    public void queryResult(Object object) {
        c++;
        boolean res = (boolean) object;
        if (c == 2) {
            if (res) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(AlertType.INFORMATION, "Evaluation Success", "Report updated", null);
                        loadPage("requestTreatment");
                    }
                });
            } else
                showAlert(AlertType.ERROR, "Error!", "Could not update dates and stages", null);
        }
    }

    /**
     * Extension option visible only if day difference is bigger then -3
     */
    // Extensions:
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p></p> 
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
     * <p>open extension window</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event pressed on extension button
     */
    @FXML
    void requestExtension(ActionEvent event) {
        start(new Stage());
    }

    /**
     * @param primaryStage loads the extenstion screen
     */

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
