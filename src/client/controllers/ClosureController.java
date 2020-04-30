package client.controllers;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.corba.se.spi.orb.Operation;

import client.App;
import common.Tools;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.ChangeRequest;
import common.entity.OrganizationRole;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import server.controllers.EmailSender;



/**
 * 
 * <h2>Closure phase page controller</h2>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class ClosureController extends AppController implements Initializable {

    /*
     * this static variable is supposed to hold all the data of the request chosen
     * in request treatment
     */
    // public static ChangeRequest thisRequest;
    public static ClosureController instance;

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
    private Pane pane_msg;

    @FXML
    private Text textInMsgPane;

    @FXML
    private AnchorPane pane_form;

    @FXML
    private Text finishedStatus;

    @FXML
    private Button closeProcessBtn;

    @FXML
    private TitledPane titledPane;

    @FXML
    private Text titledPane_Text;

    /**
     *
     * @param location
     * @param resources
     * @apiNote initialization of the Closure screen
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dueDateLabel.setVisible(true);
        instance = this;
        long estimatedTime = 0;
        pane_msg.setVisible(false);
        pane_form.setVisible(false);

        inchargeTF.setText("Supervisor");

        thisRequest = requestTreatmentController.Instance.getCurrentRequest();
        this.requestNumberTXT.setText("Request Number "+thisRequest.getRequestID());
        Tools.fillRequestPanes(requestID, existingCondition, descripitionsTextArea, inchargeTF, departmentID,
                dueDateLabel, requestNameLabel, thisRequest);
        closureInit();
        if (!thisRequest.getCurrentStage().equals("CLOSURE")) {
            pane_msg.setVisible(true);
            return;
        }

        if (!App.user.isOrganizationRole(OrganizationRole.SUPERVISOR)) {
            System.out.println("2");
            textInMsgPane.setFill(Color.BLUE);
            textInMsgPane.setText("Stage in progress");
            pane_msg.setVisible(true);

            return;
        }

        // Otherwise: this is the Supervisor in his stage
        pane_msg.setVisible(true);
        textInMsgPane.setText("The request waiting for Initiator Action (" + thisRequest.getInitiator() + ")");
        estimatedTime = Duration.between(ZonedDateTime.now(), thisRequest.getCurrentStageObject().getDeadline())
                .toDays();

        Tools.setTitlePane(estimatedTime, titledPane, titledPane_Text);
        if (thisRequest.getCurrentStageObject().getPreStage().equals("DECISION")) {
            finishedStatus.setFill(Color.DARKRED);
            finishedStatus.setText("Request Declined");
        } else // else if prevStage == Validation{
        {
            finishedStatus.setFill(Color.FORESTGREEN);
            finishedStatus.setText("Request Processed Correctly");
        }
    }



    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Getting confirmed requests from the request initiator</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *
     */
    void closureInit() {
        String query = "SELECT Request_Confirmed FROM Requests WHERE RequestID = '" + thisRequest.getRequestID()
                + "' LIMIT 1";
        OperationType ot = OperationType.Closure_Init;
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }

    /**
     *
     * @param object
     * @apiNote
     * server response from closureInit function, if there is a confirmed request, the closure process will be available on screen
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check if the request is confirmed. </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) boolean result there is confirmed request.  
     */
    public void checkPreConditions_ServerResponse(Object object) {
        List<Boolean> init_res = (List<Boolean>) object;
        boolean closure_init = init_res.get(0);

        if (closure_init) {
            pane_msg.setVisible(false);
            pane_form.setVisible(true);

            return;
        }
    }

    /**
     * @param event
     * @apiNote need to check if the process succeed or not and send an appropriate
     * 
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p> Check if the process succeed or not and send an appropriate nessage.<br/>
     *message: use finishedStatusTF
     * </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event close button was pressed 
     */
    @FXML
    void closeProcessBtnClicked(ActionEvent event) {
        c = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date today = new Date(System.currentTimeMillis());
        String query;
        if (thisRequest.getCurrentStageObject().getPreStage().equals("DECISION")) {
            query = "UPDATE Requests SET Treatment_Phase = 'DONE' , STATUS = 'CANCELED' WHERE RequestID = '"
                    + thisRequest.getRequestID() + "'";
            thisRequest.setStatus("Canceled");

        } else {
            query = "UPDATE Requests SET Treatment_Phase = 'DONE' , STATUS = 'DONE' WHERE RequestID = '"
                    + thisRequest.getRequestID() + "'";
            thisRequest.setStatus("Done");
        }
        // send email
        String query2 = " UPDATE `Stage` SET  `EndTime` = '" + dateFormat.format(today)
                + "' where  `StageName` = 'CLOSURE' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
        OperationType ot = OperationType.Clousre_UpdateRequestStatus;
        App.client.handleMessageFromClientUI(new Message(ot, query));
        App.client.handleMessageFromClientUI(new Message(ot, query2));
        OperationType ot2 = OperationType.ClousreEmail;
        String query3 = "SELECT Email FROM requests WHERE RequestID='" + thisRequest.getRequestID() + "';";
        App.client.handleMessageFromClientUI(new Message(ot2, query3));
    }

    /**
     * @apiNote
     * client response from server, if request closure Updates were updating the correct tuple on the DB successfully
     * its using the Clousre_UpdateRequestStatus Operation
     */
    
    private static int c = 0;

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server of DB update<br/> if successful shows alert and load request treatment page.
     * <br/>
     * else show error alert  message. </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void closureQueryResult(Object object) {
        c++;
        boolean res = (boolean) object;
        if (c == 2) {
            if (res) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showAlert(AlertType.INFORMATION, "Request Treatment Completed",
                                "Request #" + thisRequest.getRequestID() + " is now " + thisRequest.getStatus()
                                        + "\nnotifying appropriate users via email",
                                null);
                        loadPage("requestTreatment");
                    }
                });
            } else
                showAlert(AlertType.ERROR, "Error!", "Could not close the request", null);
        }
    }

    /**
     * @apiNote
     * client response from server, if request closure mail was sent successfully
     * its using the ClosureEmail Operation
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check if e-mail was sent.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) boolean result of the email sending 
     */
    public void emailResponse(Object object) {
        if (!(boolean) object)
            showAlert(AlertType.ERROR, "Error", "Can't send email please try again", null);
    }
}
