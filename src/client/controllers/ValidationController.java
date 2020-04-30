package client.controllers;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.cj.x.protobuf.MysqlxCrud.ViewSqlSecurity;

import client.App;
import common.Tools;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.ChangeRequest;
import common.entity.OrganizationRole;
import common.entity.Stage;
import common.entity.StageRole;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
/**
 * 
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam.<br>
 * 
 *	This class is the controller of  "Validation" page.
 *	
 */
public class ValidationController extends AppController implements Initializable {

	/*
	 * this static variable is supposed to hold all the data of the request chosen
	 * in request treatment
	 */
	// public static ChangeRequest thisRequest;
	
	/**
	 * This attribute is an implementation of "Singleton" design pattern and it is meant to hold the "live" instance of the class
	 */
	public static ValidationController instance;
	
	/**
	 * This attribute is meant to hold the object of the current stage of the chosen request from the table
	 */
	private Stage thisStage;
	
	/**
	 * This attribute is meant to hold the object of the previous stage of the chosen request from the table
	 */
	private Stage prevStage;
	
	/**
	 * This attribute is meant to hold the object of the current chosen request from the table
	 */
	protected ChangeRequest thisRequest;
	
	/**
	 * This attribute is meant to hold the content of the existing report from the TESTER in validation stage
	 */
	private String reportResult;

	/**
	 * 
	 */
	private boolean responseChairman = false;
	
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
	private Button validateBtn;

	@FXML
	private Button failureReportBtn;

	@FXML
	private TextArea failReportTextArea;

	@FXML
	private Text idText11;

	@FXML
	private Button noBtn;

	@FXML
	private Text failReportLabel;

	@FXML
	private TitledPane titledPane;

	@FXML
	private Text titledPane_Text;
	

	@FXML
	private Button btnRequestExtension;

	@FXML
	private Button btnAnswerStageExtensionRequest;
	
	
	
	/**
	 * <B> initialize</B><BR>
	 *  public void initialize(URL location, ResourceBundle resources)<BR>
	 * Is the first to run when this class is created.
	 * it is meant to give default values for the arguments in the class.
	 * if {@link #thisStage} is not "validation" OR if the user is not "tester" display {@link #pane_msg} with appropriate values.
	 * @param location 
	 * @param resources 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		reportResult = null;
		pane_msg.setVisible(false);
		pane_form.setVisible(false);
		instance = this;
		long estimatedTime = 0;
		thisRequest = requestTreatmentController.Instance.getCurrentRequest();
		this.requestNumberTXT.setText("Request Number "+thisRequest.getRequestID());
		btnAnswerStageExtensionRequest.setVisible(false);
		thisStage = thisRequest.getCurrentStageObject();
		inchargeTF.setText(thisStage.getIncharge());
		Tools.fillRequestPanes(requestID, existingCondition, descripitionsTextArea, inchargeTF, departmentID,
				dueDateLabel, requestNameLabel, thisRequest);

		checkPreConditions();

		if (!thisRequest.getCurrentStage().equals("VALIDATION")) {
			pane_msg.setVisible(true);
			return;
		}

		if (!App.user.isStageRole(thisRequest.getRequestID(), StageRole.TESTER)) {
			textInMsgPane.setFill(Color.BLUE);
			textInMsgPane.setText("Stage in progress");
			pane_msg.setVisible(true);
			inchargeTF.setText(thisStage.getIncharge());
			if (thisStage.getExtension_reason() != null)
				btnAnswerStageExtensionRequest.setVisible(true);
			return;
		}

		// Otherwise: this is the Tester in his stage
		pane_form.setVisible(true);
		setExtensionVisability();

		estimatedTime = Duration.between(ZonedDateTime.now(), thisRequest.getCurrentStageObject().getDeadline())
				.toDays();

		Tools.setTitlePane(estimatedTime, titledPane, titledPane_Text);

		checkReport();
		inchargeTF.setText(thisRequest.getCurrentStageObject().getIncharge() + "");

		titledPane.setCollapsible(false);
		titledPane.setText("Waiting for your action");
		failReportLabel.setVisible(false);
		failReportTextArea.setVisible(false);
		failureReportBtn.setDisable(true);
		failureReportBtn.setVisible(false);
	}

	/**
	 * <PRE><B> checkPreConditions</B><BR>
	 *  private void checkPreConditions()<BR>
	 * sends the server a query to check if the conditions to start the stage are met.
	 * This method is linked with {@link #checkPreConditions_ServerResponse(Object)} method as it is the answer to the query.
	 */
	private void checkPreConditions() {
		OperationType ot = OperationType.VAL_GetInitData;
		String query = "SELECT `init`,`init_confirmed` FROM `Stage` WHERE `RequestID` = '" + thisRequest.getRequestID()
				+ "' AND `StageName` = 'VALIDATION' LIMIT 1";
		App.client.handleMessageFromClientUI(new Message(ot, query));
	}
	
	/**
	 * <B> checkPreConditions_ServerResponse</B><BR>
	 *  public void checkPreConditions_ServerResponse(Object object)<BR>
	 * This method is called after the server runs the query from {@link #checkPreConditions()} and set values for the arguments of the class.
	 * if the conditions are not met it loads the {@link PreValidationController} page.
	 * if the conditions are met it runs the {@link #init()} method.
	 * @param object holds the answer from the server after running the query
	 */
	public void checkPreConditions_ServerResponse(Object object) {
		List<Boolean> init_res = (List<Boolean>) object;
		boolean init = init_res.get(0);
		boolean init_confirmed = init_res.get(1);

		if (init_confirmed && init) {
			init();
			return;
		}
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				loadPage("PreValidation");
			}
		});
	}
	
	/**
	 * <PRE><B> init</B><BR>
	 *  private void init()<BR>
	 * if the user is "COMMITEE_CHAIRMAN" set the page accordingly
	 */
	private void init() {

		if (App.user.isOrganizationRole(OrganizationRole.COMMITEE_CHAIRMAN)) {

			if (responseChairman) {
				titledPane.getStyleClass().remove("danger");
				titledPane.getStyleClass().add("success");
				titledPane.setCollapsible(false);
				titledPane.setText("This stage is done.");
				titledPane_Text.setText("You have only a viewing permission.");
				titledPane_Text.setFill(Color.FORESTGREEN);
				titledPane_Text.setVisible(true);

				if (!thisRequest.getCurrentStage().equals("VALIDATION")) { // Watching only
					titledPane.getStyleClass().remove("danger");
					titledPane.getStyleClass().add("success");
					titledPane.setCollapsible(false);
					titledPane.setText("This stage is done.");
					titledPane_Text.setText("You have only a viewing permission.");
					titledPane_Text.setFill(Color.FORESTGREEN);
					titledPane_Text.setVisible(true);
				}
			}
		}
	}
	
	/**
	 * <PRE><B> failureReportBtnClicked</B><BR>
	 *  void failureReportBtnClicked(ActionEvent event)<BR>
	 * Runs when "Submit Failure Report" is pressed and send the server a query and calls {@link #setValidationTable()}.
	 * @param event
	 */
	@FXML
	void failureReportBtnClicked(ActionEvent event) {
		OperationType ot = OperationType.VALID_GetPrevStage;
		String query = "SELECT * FROM `Stage`  WHERE `RequestID` = " + thisRequest.getRequestID()
				+ " AND `StageName` = 'EXECUTION' LIMIT 1";
		String query2 = "UPDATE Stage SET EndTime = NULL WHERE `StageName` = 'EXECUTION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
		App.client.handleMessageFromClientUI(new Message(ot, query));
		App.client.handleMessageFromClientUI(new Message(OperationType.VALID_UpdatePrevStage, query2));
		setValidationTable();
	}



	/**
	 * <PRE><B> setStageTable</B><BR>
	 *  void setStageTable()<BR>
	 * is called on {@link #appendPrevStageObject_ServerResponse(Object)}.
	 * send the server a query to return the process to "EXECUTION" stage.
	 * This method is linked with {@link #queryResult2(Object)} method as it is the answer to the query.
	 */
	void setStageTable() {
		c2 = 0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		Calendar c = Calendar.getInstance();
		Date today = new Date(System.currentTimeMillis());
		c.setTime(today);
		c.add(Calendar.DATE, 7);
		Date deadlineDate = c.getTime();
		String query1 = "UPDATE Requests SET Treatment_Phase = 'EXECUTION' WHERE RequestID = '"
				+ thisRequest.getRequestID() + "'";
		String query2 = " UPDATE `Stage` SET init = 0, init_confirmed = 0, `EndTime` = '" + dateFormat.format(today)
				+ "' where  `StageName` = 'VALIDATION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
		String query3 = "INSERT INTO Repeted (RequestID, StageName, StartTime, EndTime, Deadline, Incharge) VALUES ('"
				+ prevStage.getRequestID() + "', '" + prevStage.getStageName() + "', '"
				+ prevStage.getStartTime().format(formatter) + "', '" + prevStage.getEndTime().format(formatter)
				+ "', '" + prevStage.getDeadline().format(formatter) + "', '" + prevStage.getIncharge() + "');";

		OperationType ot = OperationType.VALID_UpdateRepeated;
		App.client.handleMessageFromClientUI(new Message(ot, query1));
		App.client.handleMessageFromClientUI(new Message(ot, query2));
		App.client.handleMessageFromClientUI(new Message(ot, query3));

	}
	
	/**
	 * 
	 * @param object
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
	 * 
	 */
	private static int c2 = 0;
	
	/**
	 *<B> queryResult2</B><BR>
	 *  public void queryResult2(Object object)
	 *  This method is called after server runs a query from this class
	 *  On success pop an alert of return to "execution" and load "requestTreatment" page
	 *  On failure pop an alert of failure
	 * @param object holds the answer from the server after running the query
	 */
	public void queryResult2(Object object) {
		c2++;
		boolean res = (boolean) object;
		if (c2 == 3) {
			if (res) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showAlert(AlertType.INFORMATION, "Return to Executer",
								"The report was sent to the execution stage", null);
						loadPage("requestTreatment");
					}
				});
			} else
				showAlert(AlertType.ERROR, "Error!", "Cannot re-execute", null);
		}
	}
	
	/**
	 * <PRE><B> checkReport</B><BR>
	 *  void checkReport()<BR>
	 * Is called on {@link #initialize(URL, ResourceBundle)}.
	 * Sends the server a query to get relevant report.
	 * This method is linked with {@link #getReport_ServerResponse(Object)} method as it is the answer to the query.
	 */
	void checkReport() {
		OperationType ot = OperationType.VALID_GetReport;
		String query = "SELECT * FROM `Execution Failure Report` WHERE RequestID = " + thisRequest.getRequestID();
		App.client.handleMessageFromClientUI(new Message(ot, query));
	}
	
	/**
	 * <B> setValidationTable</B><br>
	 *  public void setValidationTable()<br>
	 * Is called on {@link #failureReportBtnClicked(ActionEvent)} and send a query to the server in order to insert a new row in "Execution Failure Report" table.
	 * This method is linked with {@link #setValidationTable_ServerResponse(Object)} method as it is the answer to the query.
	 */
	public void setValidationTable() {
		c4=2;
		String query1 = " INSERT INTO `Execution Failure Report` (RequestID, Report) VALUES ( '"
				+ thisRequest.getRequestID() + "', '" + failReportTextArea.getText() + "')";
		OperationType ot = OperationType.VALID_updateRequestStatus;
		App.client.handleMessageFromClientUI(new Message(ot, query1));
		String query2 = "UPDATE Stage SET EndTime = NULL WHERE `StageName` = 'EXECUTION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
		App.client.handleMessageFromClientUI(new Message(ot, query2));
	}

	private int c4;
	
	/**
	 * <PRE><B> setValidationTable_ServerResponse</B><BR>
	 *  public void setValidationTable_ServerResponse(Object object)<BR>
	 * This method is called after the server runs the query from {@link #setValidationTable()}.
	 * if the answer is true load "requestTreatment" page.
	 * @param object holds the answer from the server after running the query
	 */
	public void setValidationTable_ServerResponse(Object object) {

		boolean res = (boolean) object;
		if (res&&(--c4==0)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					showAlert(AlertType.INFORMATION, "", "", null);
					loadPage("requestTreatment");
				}
			});
		}
	}
	
	/**
	 * <B> getReport_ServerResponse</B><BR>
	 *  public void getReport_ServerResponse(Object object)<BR>
	 * This method is called after the server runs the query from {@link #checkReport()}.
	 * If a report already exist it pops an appropriate alert and hide to "no" button.
	 * @param object
	 */
	public void getReport_ServerResponse(Object object) {
		this.reportResult = (String) object;
		if (reportResult != null) { // if the returned result was back
			showAlert(AlertType.INFORMATION, "Report already exists", "The report already been sent to the executer",null);
			noBtn.setVisible(false);
		}
	}
	
	/**
	 * <PRE><B> noBtnClick</B><BR>
	 *  void noBtnClick(ActionEvent event)<BR>
	 * Runs when "no" is pressed and set the appropriate arguments as visible
	 * @param event
	 */
	@FXML
	void noBtnClick(ActionEvent event) {
		failReportLabel.setVisible(true);
		failReportTextArea.setVisible(true);
		failureReportBtn.setVisible(true);
		failureReportBtn.setDisable(false);
		validateBtn.setDisable(true);
		noBtn.setDisable(true);
	}
	
	/**
	 * <PRE><B> validateBtnClicked</B><BR>
	 *  void noBtnClick(ActionEvent event)<BR>
	 * Runs when "yes" is pressed and sends the server appropriate queries to change values in DB
	 * This method is linked with {@link #queryResult(Object)} method as it is the answer to the query.
	 * @param event
	 */
	@FXML
	void validateBtnClicked(ActionEvent event) {
		c = 0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date today = new Date(System.currentTimeMillis());
		ZonedDateTime tomorrow = ZonedDateTime.now().plusDays(1);
		String tomorrowFormat = tomorrow.getYear() + "/" + tomorrow.getMonthValue() + "/" + tomorrow.getDayOfMonth();
		String query = "UPDATE Requests SET Status = 'WAITING(USER)', Treatment_Phase = 'CLOSURE' WHERE RequestID = '"
				+ thisRequest.getRequestID() + "'";
		String query2 = " UPDATE `Stage` SET  `EndTime` = '" + dateFormat.format(today)
				+ "' where  `StageName` = 'VALIDATION' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
		String query3 = " UPDATE `Stage` SET  `StartTime` = '" + dateFormat.format(today)
				+ "' where  `StageName` = 'CLOSURE' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
		String query4 = " UPDATE `Stage` SET  `Deadline` = '" + tomorrowFormat
				+ "' where  `StageName` = 'CLOSURE' AND `RequestID` = '" + thisRequest.getRequestID() + "';";
		String query5 = " UPDATE `Stage` SET  `PrevStage` = 'VALIDATION' where  `StageName` = 'CLOSURE' AND `RequestID` = '"
				+ thisRequest.getRequestID() + "';";
		OperationType ot = OperationType.VALID_UpdateDB;
		App.client.handleMessageFromClientUI(new Message(ot, query));
		App.client.handleMessageFromClientUI(new Message(ot, query2));
		App.client.handleMessageFromClientUI(new Message(ot, query3));
		App.client.handleMessageFromClientUI(new Message(ot, query4));
		App.client.handleMessageFromClientUI(new Message(ot, query5));
		thisRequest.setPrevStage("VALIDATION");

	}
	
	/**
	 * 
	 */
	private static int c = 0;
	
	/**
	 * <B> queryResult</B><br>
	 *  public void queryResult(Object object)<br>
	 *  This method is called after server runs a query from this class
	 *  On success pop an alert of success and load "requestTreatment" page
	 *  On failure pop an alert of failure
	 * @param object holds the answer from the server after running the query
	 */
	public void queryResult(Object object) {
		c++;
		boolean res = (boolean) object;
		if (c == 5) {
			if (res) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						showAlert(AlertType.INFORMATION, "Validation Complete",
								"The request moved to "+thisRequest.getInitiator()+" for closure.", null);
						loadPage("requestTreatment");
					}
				});
			} else
				showAlert(AlertType.ERROR, "Error!", "Could not update the closure update", null);
		}
	}
	
	/**
	 * <PRE><B> setExtensionVisability</B><BR>
	 *  private void setExtensionVisability()<BR>
	 * This method is called on {@link #initialize(URL, ResourceBundle)}.
	 * This method set the visibility of extension button according to the rules
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
	 * <PRE><B> requestExtension</B><BR>
	 *  void requestExtension(ActionEvent event)<BR>
	 * Runs when "Request Extension" is pressed and calls {@link #start(javafx.stage.Stage)} 
	 * @param event
	 */
	@FXML
	void requestExtension(ActionEvent event) {
		start(new javafx.stage.Stage());
	}
	
	/**
	 * <B> requestExtension</B><BR>
	 *  public void start(javafx.stage.Stage primaryStage)
	 * This method is called on {@link #requestExtension(ActionEvent)} and opens a new "Extension" windows.
	 * 
	 * @param primaryStage
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

}
