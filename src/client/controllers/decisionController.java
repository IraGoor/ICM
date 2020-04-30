package client.controllers;

import client.App;
import common.Tools;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.*;
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
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 *
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */

/**
 * 
 * <h2>Decision phase page Controller</h2>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class decisionController extends AppController implements Initializable {

    /*
     * this static variable is supposed to hold all the data of the request chosen
     * in request treatment
     */
    // public static ChangeRequest thisRequest;
    public static decisionController instance;

    protected ChangeRequest thisRequest;
    private common.entity.Stage thisStage;
    private Stage prevStage;

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
    private TextArea requiredChangeText;

    @FXML
    private TextArea expectedResultText;

    @FXML
    private TextArea constraintText;

    @FXML
    private Text timeEvaluationText;

    @FXML
    private Button reEvaluateBtn;

    @FXML
    private Button approveBtn;

    @FXML
    private Button declineBtn;

    @FXML
    private TitledPane titledPane;

    @FXML
    private Text titledPane_Text;

    @FXML
    private Button btnRequestExtension;

    /**
     *
     * @param location
     * @param resources
     * @apiNote initialization of the decision screen
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        long estimatedTime = 0;
        instance = this;
        thisRequest = requestTreatmentController.Instance.getCurrentRequest();
        thisStage = thisRequest.getCurrentStageObject();
        this.requestNumberTXT.setText("Request Number "+thisRequest.getRequestID());
        dueDateLabel.setVisible(false);

        titledPane.setCollapsible(false);
        titledPane.setText("Waiting for your action");

        approveBtn.setVisible(false);
        declineBtn.setVisible(false);
        reEvaluateBtn.setVisible(false);
        inchargeTF.setText("Committee Chairman");
        btnRequestExtension.setVisible(false);
        inchargeTF.setText(thisStage.getIncharge());
        Tools.fillRequestPanes(requestID, existingCondition, descripitionsTextArea, inchargeTF, departmentID,
                dueDateLabel, requestNameLabel, thisRequest);

        if (!thisRequest.getCurrentStage().equals("DECISION")) {

            titledPane.setText("This stage is done");
            titledPane_Text.setText("This Report has Been Approved. The stage is done.");
            titledPane.getStyleClass().remove("danger");
            titledPane.getStyleClass().add("success");
            return;
        } else {
            if (App.user.isOrganizationRole(OrganizationRole.COMMITEE_CHAIRMAN)) {
                approveBtn.setVisible(true);
                declineBtn.setVisible(true);
                reEvaluateBtn.setVisible(true);
                setExtensionVisability();
            } else {
                if (thisStage.getExtension_days() != 0)
                    btnRequestExtension.setVisible(true);
            }
        }

        setFieldsData();
        estimatedTime = Duration.between(ZonedDateTime.now(), thisRequest.getCurrentStageObject().getDeadline())
                .toDays();

        Tools.setTitlePane(estimatedTime, titledPane, titledPane_Text);
    }

    /**
     * @apiNote this function is called from the initialization of the page
     * Its loads from the DB the current request evaluation report.
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>
     * Request from  the DB the current request evaluation report.<br/>
     * Called from the initialization of the page.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    private void setFieldsData() {
        OperationType ot = OperationType.DECISION_GetAllReportsByRID;
        String query = "SELECT * FROM `EvaluationReports` WHERE REQUESTID = " + thisRequest.getRequestID()
                + " ORDER BY Report_ID DESC LIMIT 1;";
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }



    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, loads the evaluation report on the screen.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) the evaluation report
     */
    public void setFieldsData_ServerResponse(Object object) {
        ArrayList<EvaluationReport> reports = (ArrayList<EvaluationReport>) object;
        if (reports.size() > 0) {
            // SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
            EvaluationReport individualReport = reports.get(0);
            Date reportDate = individualReport.getTimestamp();
            Calendar reportDateCal = Calendar.getInstance();
            reportDateCal.setTime(reportDate);
            reportDateCal.add(Calendar.DATE, 7);
            reportDateCal.set(Calendar.HOUR_OF_DAY, 0);
            reportDateCal.set(Calendar.MINUTE, 0);
            reportDateCal.set(Calendar.SECOND, 0);
            reportDateCal.set(Calendar.MILLISECOND, 0);

            Date today = new Date(System.currentTimeMillis());
            Calendar todayCal = Calendar.getInstance();
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);

            long diff = reportDateCal.getTime().getTime() - todayCal.getTime().getTime();
            long daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

           if (thisRequest.getCurrentStage().equals("DECISION")) {
                if (daysDiff >= 0) {
                    titledPane_Text.setText(
                            TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " Days left to complete this stage");
                    titledPane.getStyleClass().removeAll();
                    titledPane.getStyleClass().add("info");
                } else {
                    titledPane_Text
                            .setText("Stage in " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " days late!");
                    titledPane.getStyleClass().removeAll();
                    titledPane.getStyleClass().add("danger");
                }

            }
            requiredChangeText.setText(individualReport.getRequired_change());
            expectedResultText.setText(individualReport.getExpected_result());
            constraintText.setText(individualReport.getExpected_risks());
            timeEvaluationText.setText(individualReport.getEstimated_time().toLocalDate().toString());

        }
    }



    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p> Send server a query to update phase to execution phase</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event :approve button on the page was clicked.
     */
    @FXML
    void approveBtnClick(ActionEvent event) {
        c=0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date today = new Date(System.currentTimeMillis());

        String query1 = "UPDATE Requests SET Treatment_Phase = 'EXECUTION' WHERE RequestID = '"
                + thisRequest.getRequestID() + "'";
        String query2 = " UPDATE `Stage` SET  `EndTime` = '" + dateFormat.format(today)
                + "' where  `StageName` = 'DECISION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";

        OperationType ot = OperationType.DECI_UpdateDB;
        App.client.handleMessageFromClientUI(new Message(ot, query1));
        App.client.handleMessageFromClientUI(new Message(ot, query2));
        thisRequest.setReturned(false);
    }

    private static int c = 0;


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check update<br/>
     * if was successful the screen will return to request Treatment screen<br/>
     * else it will show an error alert </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) update result - boolean
     */
    public void queryResult(Object object) {
        c++;
        boolean res = (boolean) object;
        if (c == 2) {
            if (res) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(AlertType.INFORMATION, "Evaluation Approved", "Request moved to execution phase", null);
                        loadPage("requestTreatment");
                    }
                });
            } else
                showAlert(AlertType.ERROR, "Error!", "Can't update approval", null);
        }
    }



    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Send server a query to update phase to closure phase</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event:  decline button on the page was clicked.
     */
    @FXML
    void declineBtnClick(ActionEvent event) {
        c3 = 0;
        thisRequest.setPrevStage("DECISION");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date today = new Date(System.currentTimeMillis());
        ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);
        String tomorrowFormat = tomorrow.getYear() + "/" + tomorrow.getMonthValue() + "/" + tomorrow.getDayOfMonth();
        String query1 = "UPDATE Requests SET Status = 'WAITING(USER)', Treatment_Phase = 'CLOSURE' WHERE RequestID = '"
                + thisRequest.getRequestID() + "'";
        String query2 = " UPDATE `Stage` SET Status = 'WAITING(USER)', `EndTime` = '" + dateFormat.format(today)
                + "' where  `StageName` = 'DECISION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
        String query3 = " UPDATE `Stage` SET  `StartTime` = '" + dateFormat.format(today)
                + "' where  `StageName` = 'CLOSURE' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
        String query4 = " UPDATE `Stage` SET  `Deadline` = '" + tomorrowFormat
                + "' where  `StageName` = 'CLOSURE' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
        String query5 = " UPDATE `Stage` SET  `PrevStage` = 'DECISION' where  `StageName` = 'CLOSURE' AND `RequestID` = '"
                + thisRequest.getRequestID() + "';";
        OperationType ot = OperationType.DECISION_DeclineUpdate;
        App.client.handleMessageFromClientUI(new Message(ot, query1));
        App.client.handleMessageFromClientUI(new Message(ot, query2));
        App.client.handleMessageFromClientUI(new Message(ot, query3));
        App.client.handleMessageFromClientUI(new Message(ot, query4));
        App.client.handleMessageFromClientUI(new Message(ot, query5));

    }

    private static int c3 = 0;

    /**
     *
     * @param object
     * @apiNote
     * client response from Server, this function checks of the update of declineBtnClick was updated successfully
     * if it was updated successfully then the screen will return to request Treatment screen, else it will show an error
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check update<br/>
     * if was successful the screen will return to request Treatment screen<br/>
     * else it will show an error alert </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) update result - boolean
     */
    public void decisionDeclineQueryResult(Object object) {
        c3++;
        boolean res = (boolean) object;
        if (c3 == 5) {
            if (res) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(AlertType.INFORMATION, "Evaluation Declined", "Request moved to closure phase...", null);
                        reEvaluateBtn.setDisable(true);
                        approveBtn.setDisable(true);
                        declineBtn.setDisable(true);
                        loadPage("requestTreatment");
                    }
                });
            } else
                showAlert(AlertType.ERROR, "Error!", "Cannot approve the decline", null);
        }
    }

    /**
     *
     * @param event
     * re-evaluate button on the page was clicked, it tries to update the phase of the request to evaluation phase.
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Send server a query to update phase to evaluation phase</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event : re-evaluate button on the page was clicked.
     */

    @FXML
    void reEvaluateBtnClick(ActionEvent event) {
        OperationType ot = OperationType.DECISION_GetPrevStage;
        String query = "SELECT * FROM `Stage`  WHERE `RequestID` = " + thisRequest.getRequestID()
                + " AND `StageName` = 'EVALUATION' LIMIT 1";
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }

    /**
     * Extension option visible only if day difference is bigger then -3
     */
    
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Check if the Extension is available<br/>
     * if yes make button visable
     *  </p> 
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
     * <p>Load extenstion window</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event : extension button was clicked
     */
    @FXML
    void requestExtension(ActionEvent event) {
        start(new javafx.stage.Stage());
    }

    /**
     *
     * @param primaryStage
     * 
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Loads the extension screen</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param primaryStage
     */
    public void start(javafx.stage.Stage primaryStage) {
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

    /**
     *
     * @param object
     * client response from server, appendPrevStageObject_ServerResponse is the response for the query that search the previous stage
     * if there is any, setStageTable will be called, else error message will be thrown.
     */

    public void appendPrevStageObject_ServerResponse(Object object) {
        this.prevStage = (common.entity.Stage) object;
        if (prevStage == null) {
            showAlert(AlertType.ERROR, "Error", "Could not find prev stage", null);
        } else { // all good
            setStageTable();
        }
    }

    /**
     * resetting all the confirming information of pre- evaluation stage, and trying to send an email to the evaluator
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Sending queries to Reset all the confirming information of pre- evaluation stage and sending mail to evaluator,</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    void setStageTable() {
        c2 = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        Date today = new Date(System.currentTimeMillis());
        // put in echo server

        String query1 = "UPDATE Requests SET Treatment_Phase = 'EVALUATION' WHERE RequestID = '"
                + thisRequest.getRequestID() + "'";
        String query2 = "UPDATE `Stage` SET init = 0, init_confirmed = 0, `EndTime` = '" + dateFormat.format(today)
                + "' where  `StageName` = 'EVALUATION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
        String query3 = "UPDATE `Stage` SET `StartTime` = NULL,`Deadline` = NULL,`EndTime` = NULL where `StageName` = 'EVALUATION' AND `RequestID` = "
                + thisRequest.getRequestID();

        String query4 = "INSERT INTO Repeted (RequestID, StageName, StartTime, EndTime, Deadline, Incharge) VALUES ('"
                + prevStage.getRequestID() + "', '" + prevStage.getStageName() + "', '"
                + prevStage.getStartTime().format(formatter) + "', '" + prevStage.getEndTime().format(formatter)
                + "', '" + prevStage.getDeadline().format(formatter) + "', '" + prevStage.getIncharge() + "');";
        OperationType ot = OperationType.DECISION_updateRequestStatus;

        App.client.handleMessageFromClientUI(new Message(ot, query1));
        App.client.handleMessageFromClientUI(new Message(ot, query2));
        App.client.handleMessageFromClientUI(new Message(ot, query3));
        App.client.handleMessageFromClientUI(new Message(ot, query4));


    }

    private static int c2 = 0;

    /**
     *
     * @param object
     * client response form server, it will check if all the updates from setStageTable were sent correctly
     * returns to request treatment screen of successful and show an success prompt, otherwise, throws an error message.
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check DB tables update (setStageTable)<br/>
     * if successful returns to request treatment screen and show success prompt.<br/>
     * else shows an error alert.   </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void queryResult2(Object object) {
        c2++;
        boolean res = (boolean) object;
        if (c2 == 4) {
            if (res) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(AlertType.INFORMATION, "Return to evaluator", "The request returned to evaluator for further inspection", null);

                        loadPage("requestTreatment");
                    }
                });
            } else
                showAlert(AlertType.ERROR, "Error!", "Cannot re-evaluate", null);
        }
    }

}
