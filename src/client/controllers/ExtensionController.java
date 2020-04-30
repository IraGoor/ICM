package client.controllers;

import client.App;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.ChangeRequest;
import common.entity.OrganizationRole;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;



/**
 * 
 * <h2>Extension prompt controller</h2>
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class ExtensionController extends AppController implements Initializable {
    public static ExtensionController instance;
    protected ChangeRequest thisRequest;
    private common.entity.Stage thisStage;

    @FXML
    private Pane defualtPane;

    @FXML
    private TextArea taReason;

    @FXML
    private Button btnAccept;

    @FXML
    private Button btnDeny;

    @FXML
    private Button btnSubmit;

    @FXML
    private TextField tfDays;

    @FXML
    private Pane msgPane;

    @FXML
    private Text currentStageText;


    @FXML
    private Text warning;

    /**
     *
     * @param event
     * submission of time extension send to DB
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Sends extension request query to DB</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event submit button was pressed
     */
    @FXML
    void submitForm(ActionEvent event) {
        this.event = event;
        warning.setVisible(false);

        if (tfDays.getText().isEmpty() || taReason.getText().isEmpty()) {
            warning.setVisible(true);
            return;
        }
        OperationType ot = OperationType.Extension_submit;
        String query = "UPDATE `Stage` SET " +
                "`extension_days`= '" + tfDays.getText() + "', " +
                "`extension_reason`= '" + taReason.getText() + "' " +
                "WHERE`RequestID`= '" + thisRequest.getRequestID() + "' AND `StageName` = '" + thisRequest.getCurrentStage() + "'";
        App.client.handleMessageFromClientUI(new Message(ot, query));
    }

    /**
     *
     * @param event
     * supervisor accepted the extension, deadLine is updated on database
     */

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Supervisor screen:Sends extension stage phase update  </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event supervisor pressed on accept  button
     */
    @FXML
    void accept(ActionEvent event) {
        this.event = event;
        OperationType ot = OperationType.Extension_submit;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        String query = "UPDATE `Stage` SET " +
                "`extension_decision`= 'ACCEPT' , " +
                "`Deadline`= '" + thisStage.getDeadline().plusDays(thisStage.getExtension_days()).format(formatter) + "' " +
                " WHERE`RequestID`= '" + thisRequest.getRequestID() + "' AND `StageName` = '" + thisRequest.getCurrentStage() + "'";
        App.client.handleMessageFromClientUI(new Message(ot, query));
        OperationType ot2= OperationType.mailToDirectorExtension;
        App.client.handleMessageFromClientUI(new Message(ot2,thisRequest.getRequestID()));
        loadPage("requestTreatment");
        
    }
    /**
     *
     * @param event
     * 
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Supervisor screen:Sends extension stage phase update </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event supervisor press on deny button. 
     */
    @FXML
    void deny(ActionEvent event) {
        this.event = event;
        OperationType ot = OperationType.Extension_submit;
        String query = "UPDATE `Stage` SET " +
                "`extension_decision`= 'DENIED' " +
                " WHERE`RequestID`= '" + thisRequest.getRequestID() + "' AND `StageName` = '" + thisRequest.getCurrentStage() + "'";
        App.client.handleMessageFromClientUI(new Message(ot, query));

    }


    private Event event;

    /**
     *
     * @param location
     * @param resources
     * initialization of the extension screen
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        thisRequest = requestTreatmentController.Instance.selectedRequested;
        thisStage = thisRequest.getCurrentStageObject();

        this.currentStageText.setText(thisRequest.getCurrentStage());
        warning.setVisible(false);
        defualtPane.setVisible(false);
        msgPane.setVisible(false);
        btnDeny.setVisible(false);
        btnAccept.setVisible(false);
        initScreen();
    }


    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>initialization of the extension screen with the validation of the extension data.</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     */
    public void initScreen() {
        if (thisStage.getExtension_reason() != null) { // if request sent
            defualtPane.setVisible(true);
            tfDays.setEditable(false);
            taReason.setEditable(false);
            btnSubmit.setVisible(false);

            taReason.setText(thisStage.getExtension_reason());
            tfDays.setText(thisStage.getExtension_days() + "");

            if (thisStage.getExtension_decision() != null)
                warning.setText("Extension request " + thisStage.getExtension_decision().toLowerCase());
            else
                warning.setText("You have to answer this request");

            warning.setVisible(true);

            if (thisStage.getExtension_decision() != null) {
                btnAccept.setVisible(false);
                btnDeny.setVisible(false);
                return;
            }

            if (!App.user.isOrganizationRole(OrganizationRole.SUPERVISOR)) {
                btnAccept.setVisible(false);
                btnDeny.setVisible(false);
                return;
            }
            // supervisor response
            btnSubmit.setVisible(false);
            btnAccept.setVisible(true);
            btnDeny.setVisible(true);

        } else { // == null -> user can send a form.

            defualtPane.setVisible(true);
            btnSubmit.setVisible(true);

        }
    }

    /**
     *
     * @param object
     * client response from server, its a response from the submitForm method, it will show an alert if the update is a success or not
     */
    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server, check update result (via submitForm)<br/>
     * if successful it will show success alert and close window<br/>
     * else will show error alert </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object
     */
    public void InsertOrUpdate_ServerResponse(Object object) {
        Boolean res = (Boolean) object;
        if (res) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    showAlert(Alert.AlertType.INFORMATION, "Operation Done!", "Operation done successfully!", null);
                    ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
                }
            });

        } else {
            showAlert(Alert.AlertType.ERROR, "Operation Failed!", "Operation failed. Try again.", null);
        }

    }

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>close extension window </p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param event close button was pressed
     */
    @FXML
    void close(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
            }
        });
    }
   

    /**
     * 
     *
     * <h3> Method Purpose:</h3>
     * <p>Response from server from sending an email to director</p> 
     * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
     *
     *@param object (Object) boolean e-mail sent result
     */
	public void emailResponse(Object object) {
		if((boolean)object)
			showAlert(AlertType.ERROR, "ERROR", "Could not send mail", null);
		
	}

}
