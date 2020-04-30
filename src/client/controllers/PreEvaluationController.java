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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 *  Controller for pre-evaluation phase page
 *  @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class PreEvaluationController extends AppController implements Initializable {

    public static PreEvaluationController instance;
    private ChangeRequest thisRequest;

    @FXML
    private Text requestNumberTXT;

    @FXML
    private Text idText;

    @FXML
    private TextField tfDays;

    @FXML
    private Text requestID;

    @FXML
    private TextArea existingCondition;

    @FXML
    private TextArea descripitionsTextArea;

    @FXML
    private Pane Pane_msg;

    @FXML
    private Text txtMsg;

    @FXML
    private Pane pane_form;

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
    private Button btnSubmit;

    @FXML
    private Button btnAccept;

    @FXML
    private Button btnDeny;

    private common.entity.Stage thisStage;
    /**
     * if pre - evaluation is confirmed by pressing accept, the start time and deadline of evaluation stage for this request
     * is going to be updated om DB, and also changing the status of the request to ACTIVE
     * @param event
     */

    @FXML
    void AcceptPreEval(ActionEvent event) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        Date today = new Date(System.currentTimeMillis());
        c.setTime(today);
        c.add(Calendar.DATE, Integer.parseInt(tfDays.getText()));
        Date deadlineDate = c.getTime();
        System.out.println(dateFormat.format(deadlineDate));

        OperationType ot = OperationType.PreEVAL_SetConfirmationStatus;

        String query = "UPDATE `Stage` SET" +
                " `init_confirmed` = true ," +
                " `StartTime` = '" + dateFormat.format(today) + "'," +
                " `Deadline` = '" + dateFormat.format(deadlineDate) + "'" +
                " where  `StageName` = 'EVALUATION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
        App.client.handleMessageFromClientUI(new Message(ot, query));
        query = "UPDATE Requests SET Status = 'ACTIVE' WHERE RequestID = '" + thisRequest.getRequestID() + "'";
        App.client.handleMessageFromClientUI(new Message(OperationType.updateRequestStatus, query));
    }
    /**
     * if pre - evaluation is denied by pressing deny button, the init set to false
     * @param event
     */

    @FXML
    void DenyPreEval(ActionEvent event) {
        OperationType ot = OperationType.PreEVAL_SetConfirmationStatus;
        String query = "UPDATE `Stage` SET `init` = false where  `StageName` = 'EVALUATION' AND `RequestID` = '"
                + thisRequest.getRequestID() + "'";
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }
    /**
     * requested evaluation days are submitted and uploaded to DB by pressing deny button
     * @param event
     */
    @FXML
    void SubmitDaysRequest(ActionEvent event) {
        int days = 0;
        boolean flag = false;
        try {
            days = Integer.parseInt(tfDays.getText());
            if (days <= 0)
                showAlert(Alert.AlertType.WARNING, "Error", "Number must be greater then zero.", null);
            flag = true;
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Error", "Data must be a number", null);
        }

        if (flag) {
            String query = "UPDATE Requests SET Status = 'WAITING(SUPERVISOR)' WHERE RequestID = '" + thisRequest.getRequestID() + "'";
            App.client.handleMessageFromClientUI(new Message(OperationType.updateRequestStatus, query));
            OperationType ot = OperationType.PreEVAL_SetInitStat;
            query = "UPDATE `Stage` SET `init` = true , `requestedDays` = '" + tfDays.getText()
                    + "' where  `StageName` = 'EVALUATION' AND `RequestID` = '" + thisRequest.getRequestID() + "'";
            App.client.handleMessageFromClientUI(new Message(ot, query));
            showAlert(AlertType.INFORMATION, "Evaluation end time asked",
                    "Evaluation period has been sent to the Supervisor", null);
            loadPage("requestTreatment");
        }
    }

    /**
     * Initialize the pre evaluation screen
     * @param location
     * @param resources
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        tfDays.setEditable(false);
        thisRequest = requestTreatmentController.Instance.getCurrentRequest();
        thisStage = thisRequest.getCurrentStageObject();
        this.requestNumberTXT.setText("Request Number "+thisRequest.getRequestID());
        Tools.fillRequestPanes(requestID, existingCondition, descripitionsTextArea, inchargeTF, departmentID,
                dueDateLabel, requestNameLabel, thisRequest);
        inchargeTF.setText(thisStage.getIncharge());
        getCurrentReqestedDays();

        // GUI Init
        btnAccept.setVisible(false);
        btnDeny.setVisible(false);
        btnSubmit.setVisible(false);
        txtMsg.setVisible(false);

    }

    /**
     * gets the submitted days of evaluation stage
     */
    private void getCurrentReqestedDays() {
        OperationType ot = OperationType.PreEVAL_getData;
        String query = "SELECT `requestedDays`,`init`,`init_confirmed` FROM `Stage` WHERE  `StageName` = 'EVALUATION' AND `RequestID`= '"
                + thisRequest.getRequestID() + "'";
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }


    public void getCurrentReqestedDays_ServerResponse(Object object) {
        List<Integer> res = (List<Integer>) object;
        String query;
        tfDays.setText(res.get(0) + ""); // requestedDays
        // GUI Init by Permission
        if (App.user.isOrganizationRole(OrganizationRole.SUPERVISOR) && res.get(1) == 1) {
            btnAccept.setVisible(true);
            btnDeny.setVisible(true);
            tfDays.setEditable(false);
            return;
        }

        if (App.user.isOrganizationRole(OrganizationRole.SUPERVISOR) && res.get(0) != 0) {
            txtMsg.setText("Waiting for new days evaluation from the Evaluator.");
            txtMsg.setVisible(true);
            tfDays.setVisible(false);

        }

        if (res.get(1) == 1) {
            txtMsg.setVisible(true);
            tfDays.setEditable(false);
        } else {
            if (App.user.isStageRole(thisRequest.getRequestID(), StageRole.EVALUATOR)) {
                btnSubmit.setVisible(true);
                tfDays.setEditable(true);

            }

        }

    }

    /**
     * server response from AcceptPreEval method
     * @param object
     */

    public void updateStatus_serverResponse(Object object) {
        boolean res = (boolean) object;
        if (res) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showAlert(AlertType.INFORMATION, "Update Success", "Supervisor response has been updated", null);
                    loadPage("requestTreatment");
                }
            });
        } else
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Update Failed", null);
    }

}
