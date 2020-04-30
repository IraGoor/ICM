package client.controllers;

import client.App;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

//import common.entity.Reports;

public class ReportGenerateController extends AppController implements Initializable {

	public static ReportGenerateController instance;
	private ArrayList<RadioButton> types;

	// private Reports newReport;

	private static File file;

	@FXML
	private ImageView buffer;
	@FXML
	private AnchorPane reportTypePane;

	@FXML
	private AnchorPane datesPane;

	@FXML
	private AnchorPane submitPane;

	@FXML
	private RadioButton radioActivty;

	@FXML
	private ToggleGroup ReportType;

	@FXML
	private RadioButton radioPerformences;

	@FXML
	private RadioButton radioDelays;

	@FXML
	private Button btnSelectType;

	@FXML
	private DatePicker fromDatePic;

	@FXML
	private DatePicker toDatePic;

	@FXML
	private Button btnSelectDates;

	@FXML
	private Button btnChangeType1;

	@FXML
	private TextArea reportInfo;

	@FXML
	private Button btnSubmit;

	@FXML
	private Button btnChangeDates;

	@FXML
	private Button btnViewReports;

	@FXML
	private ImageView imgArrow;

	/*
	 * all need to go into report entity
	 *
	 */
	private String reportName;

	private LocalDate from;

	private LocalDate to;

	private String path = System.getProperty("user.dir") + "\\ReportsFiles\\";

	@FXML
	void ChooseDates(ActionEvent event) {
		if (isDatelegal()) {
			currentStep(3);
			reportInfo.setText("report type " + reportName + "\nfrom:" + fromDatePic.getValue().toString() + "\nto:"
					+ toDatePic.getValue().toString());
		}

	}

	@FXML
	void changeDates(ActionEvent event) {
		if (isReportType())
			currentStep(2);
		else
			currentStep(1);
		imgArrow.setVisible(false);

	}

	@FXML
	void changeType(ActionEvent event) {
		currentStep(1);
		imgArrow.setVisible(false);
	}

	@FXML
	void chooseReportType(ActionEvent event) {

		if (isReportType())
			currentStep(2);
		else {
			currentStep(3);
			reportInfo.setText("report type " + reportName);
			reportInfo.setDisable(true);
		}
	}

	@FXML
	void submit(ActionEvent event) {
		Report report = new Report(reportName, Date.valueOf(LocalDate.now()));

		if (report.isPeriodReport()) {
			report.setFrom(Date.valueOf(from));
			report.setTo(Date.valueOf(to));
			if (!(file = new File(path + report.toString() + ".csv")).exists()) {
				App.client.handleMessageFromClientUI(new Message(OperationType.InsertReport, report));
				App.client.handleMessageFromClientUI(new Message(OperationType.GenreateReport,
						"Select * From Reports WHERE ReportType='" + report.getType() + "' And Since ='"
								+ report.getFrom().toString() + "' AND Till ='" + report.getTo().toString() + "'"));
				buffer.setVisible(true);

			} else {
				showAlert(AlertType.WARNING, "Report already exist",
						"this file Exist at " + path + report.toString() + ".csv", null);

				try {
					Desktop desktop = Desktop.getDesktop();

					desktop.open(file);
				} catch (IOException e) {}

			}

		} else {
			file = new File(path + report.toString() + ".csv");
			App.client.handleMessageFromClientUI(new Message(OperationType.InsertReport, report));
			App.client.handleMessageFromClientUI(
					new Message(OperationType.GenreateReport, "Select * From Reports WHERE ReportType='"
							+ report.getType() + "' And Created ='" + report.getCreated().toString() + "'"));
			buffer.setVisible(true);

		}
	}

	private boolean isReportType() {

		for (RadioButton rb : types)
			if (rb.isSelected()) {
				/*
				 * here we supposed to save report type
				 */
				reportName = identifyReportType(rb);
				if (reportName.equals("Activity"))
					return true;
				// System.out.println("you chose " + reportName);

				return false;

			}
		showAlert(AlertType.ERROR, "Type input failure", "didn't choose a type of report ", null);
		return false;
	}

	private boolean isDatelegal() {
		from = fromDatePic.getValue();
		to = toDatePic.getValue();

		if (from == null || to == null)
			if (from != null) {
				showAlert(AlertType.ERROR, "Dates input failure", "didn't choose a to date ", null);
				return false;
			} else if (to != null) {
				showAlert(AlertType.ERROR, "Dates input failure", "didn't choose a from date ", null);
				return false;
			} else {
				showAlert(AlertType.ERROR, "Dates input failure", "didn't choose any dates ", null);
				return false;
			}
		else if (from.isAfter(to) || from.isEqual(to)) {
			showAlert(AlertType.ERROR, "Dates input failure", "date 'from' needs to be before 'to' ", null);
			return false;
		} else if (from.isAfter(LocalDate.now()) || to.isAfter(LocalDate.now())) {
			showAlert(AlertType.ERROR, "Dates input failure", "report period can't contain time after today' ", null);
			return false;
		} else {
			System.out.println("you chose Dates from: " + from.toString() + "and to:" + to.toString());
			return true;
		}

	}

	public void openNewReport(Object obj) {
		PrintWriter csvFile;
		Report report = (Report) obj;

		File f = new File(path);
		f.mkdirs();

		try {
			csvFile = new PrintWriter(file);
			csvFile.write(report.getData());
			csvFile.close();
			buffer.setVisible(false);
			showAlert(AlertType.INFORMATION, "Success", "new file", "Report Generated");

			// open the downloaded file using operation system

			Desktop desktop = Desktop.getDesktop();

			desktop.open(file);
		} catch (IOException e) {
		}

	}

	@FXML
	void viewReports(ActionEvent event) {
		loadPage("ViewReports");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		setupReportType();
		currentStep(1);
	}

	void currentStep(int step) {
		switch (step) {
		case 1:
			openPane(reportTypePane);
			disablePane(datesPane, 0.7);
			disablePane(submitPane, 0.5);
			break;
		case 2:
			disablePane(reportTypePane, 0.8);
			openPane(datesPane);
			disablePane(submitPane, 0.7);
			break;
		case 3:
			disablePane(reportTypePane, 0.6);
			disablePane(datesPane, 0.8);
			openPane(submitPane);
			break;
		}
	}

	void openPane(AnchorPane pane) {
		pane.setOpacity(1);
		for (Node n : pane.getChildren())
			n.setDisable(false);

	}

	void disablePane(AnchorPane pane, double opacity) {
		pane.setOpacity(opacity);
		for (Node n : pane.getChildren())
			n.setDisable(true);
	}

	public void setupReportType() {
		types = new ArrayList<RadioButton>();
		types.add(radioActivty);
		types.add(radioPerformences);
		types.add(radioDelays);

	}

	private String identifyReportType(RadioButton rb) {

		if (rb.equals(radioActivty))
			return "Activity";
		else if (rb.equals(radioDelays))
			return "Delays";
		else if (rb.equals(radioPerformences))
			return "Performences";
		else
			return null;
	}

}
