package client.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.TreeMap;

import client.App;
import common.Tools;
import common.controllers.Message;
import common.controllers.OperationType;
import common.entity.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class ViewReportsController extends AppController implements Initializable {


	/**
	 * @author Ira Goor
	 * static fields: 
	 * file: holds report file selected, selectedReport: the report selected by user
	 * datesAndData:holds a map of values of report , static for graphics page
	 * columns:holds columns name and statistics info such as average,media and SD ,static for graphics
	 * instance: singleton implementation for page
	 * non static fields:
	 * obserevableList of reports act as filters for the list (allReports,reportsDelays,reportsPerformences,reportsActivty)
	 * path:string to set path to reports folder
	 * filterButtons: array list used to hold all filter buttons to help change color when pressed 
	 */
	private static File file;
	public static Report selectedReport;
	public static ViewReportsController instance;
	public static TreeMap<String, ArrayList<Integer>> datesAndData;
	public static TreeMap<String, ArrayList<Double>> columns;
	ObservableList<Report> allReports,reportsDelays,reportsPerformences,reportsActivty;

	private String path = System.getProperty("user.dir") + "\\ReportsFiles\\";
	ArrayList<Button> filterButtons;
	@FXML
	private Button showAllBtn;

	@FXML
	private Button activitybtn;

	@FXML
	private Button performencesbtn;

	@FXML
	private Button delaysBtn;
	


	@FXML
	private ListView<Report> reportList;

	@FXML
	private Button csvBtn;

	@FXML
	private Button viewReportBtn;

	@FXML
	private Button generateReportBtn;

	@FXML
	private VBox createGraphicsPane;

	@FXML
	private Label lblReportName;

	@FXML
	private TextArea reportDescArea;

	@FXML
	private Button btnViewGraphics;

	@FXML
	private AnchorPane paneList;
	@FXML
	private ImageView buffer;

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose:when pressed on to open report as a file ,open the file 
	 * 
	 * @param event
	 */
	@FXML
	void openAsFile(ActionEvent event) {
		selectedReport = reportList.getSelectionModel().getSelectedItem();
		if (selectedReport == null) {
			showAlert(AlertType.ERROR, "No report was chosen", "you must choose a report to view it", null);
			return;
		}
		file = new File(path + selectedReport.toString() + ".csv");
		if (!file.exists())
			try {
				createInPC();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
			}

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: load report generator  page 
	 *
	 * @param event:pressed on back to Report Generator button
	 */
	@FXML
	void screenGenerateReport(ActionEvent event) {
		loadPage("Reports");
	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: show in list all activity reports, and change filter button color
	 *
	 * @param event:press on show activity button filter
	 */
	@FXML
	void showActivity(ActionEvent event) {
		reportList.setItems(reportsActivty);
		setAllfilterBtnToSameStyle();
		activitybtn.setStyle("-fx-background-color:#337ab7 ;-fx-text-fill: white ;");

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: show in list all reports, and change filter button color
	 *
	 * @param event:press on show all button filter
	 */
	@FXML
	void showAllReports(ActionEvent event) {
		reportList.setItems(allReports);
		setAllfilterBtnToSameStyle();
		showAllBtn.setStyle("-fx-background-color:#337ab7 ;-fx-text-fill: white ;");

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: show in list all delays reports, and change filter button color
	 *
	 * @param event:press on show delays button filter
	 */
	@FXML
	void showDelays(ActionEvent event) {
		reportList.setItems(reportsDelays);
		setAllfilterBtnToSameStyle();
		delaysBtn.setStyle("-fx-background-color:#337ab7 ;-fx-text-fill: white ;");

	}
	
	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: show in list all  performances reports, and change filter button color
	 *
	 * @param event:press on show performances button filter
	 */

	@FXML
	void showPerformences(ActionEvent event) {
		reportList.setItems(reportsPerformences);
		setAllfilterBtnToSameStyle();
		performencesbtn.setStyle("-fx-background-color:#337ab7 ;-fx-text-fill: white ;");

	}

	@FXML
	void openGraphView(ActionEvent event) {

		loadPage("ReportGraphics");

	}
	
	
	
	 
/**
 * 
 * @author Ira Goor 
 * method purpose:  when click on view Report open Description and option for Graphics
 *
 * @param event
 */
	@FXML
	void showReport(ActionEvent event) {
		selectedReport = reportList.getSelectionModel().getSelectedItem();
		if (selectedReport == null) {
			showAlert(AlertType.ERROR, "No report was chosen", "you must choose a report to view it", null);
			return;
		}
		breakReportData();
		Report report = selectedReport;
		disablePane(createGraphicsPane, 0.1);

		
		if (selectedReport.isPeriodReport()) {
			currentReport(1);

		} else {
			if (selectedReport.getType().equals("Delays"))
				currentReport(3);
			else
				currentReport(2);

		}

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose:open show graphic description pane,set report title and set report description 
	 * 
	 * @param i:report description by value (1 is Activity,2 is Performances, 3 is Delays)
	 * we used value make it easier to add new report types
	 */
	private void currentReport(int i) {
		String info = "";
		switch (i) {
		case 1:
			info = "Activity:\r\nhas 4 bar Charts\r\nthe lower ones represents the frequency\r\n"
					+ "through times period of Days\r\n"
					+ "in this Report each period is about "+calcPeriodTime()+" days\r\n"
							+ "Lower Bar Chart:\r\n"
							+ "left bar chart  is for requests types.\r\n"
							+ "right bar Chart is for WorkDays spent on each request\r\n"
							+ "Upper Bar Charts: represents there stats\r\n"
							+ "such as median,standered deviation (SD) and average (AVG)\r\n"
							+ "left bar chart  is for requests types,\r\n"
							+ "right bar Chart is for WorkDays spent on each request";
			break;
		case 2:
			info = "Performences:\r\n"
					+ "Lower Bar Chart:\r\n\n"
					+ "repeated Days:days spent in reapting a stage\r\n"
					+ "Extensions Days:actual work days as extension on a reauest\r\n"
					+ "Upper Bar:\r\n\n"
					+ "the upper bar charts represents there stats\r\n"
					+ "such as median,standered deviation (SD) and average (AVG)";
			break;
		case 3:
			info = "Delays:\r\n"
					+ "Lower Bar Chart:\r\n"
					+ "left bar chart  is for amount of delays.\r\n"
					+ "right bar Chart is for amount of dealy days\r\n"
					+ "Upper Bar Chart: represents there stats\r\n"
					+ "such as median,standered deviation (SD) and average (AVG)\r\n"
					+ "left bar chart  is stats for amount of delays.\r\n"
					+ "right bar Chart is stats for amount of dealy days\r\n";
			break;
		}
		openPane(createGraphicsPane);
		lblReportName.setText(labelFormat());
		reportDescArea.setText(info);
	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: set title of graphic pane title 
	 *
	 * @return
	 */
	private String labelFormat() {
		if (!selectedReport.isPeriodReport())
			return "Report type:" + selectedReport.getType() + "\r\n" + "Created:"
					+ selectedReport.flipDateformat(selectedReport.getCreated().toString()) + "\r\n";
		else
			return "Report type: " + selectedReport.getType() + "\r\n" + "Created: "
					+ selectedReport.flipDateformat(selectedReport.getFrom().toString()) + "\r\n" + "From: "
					+ selectedReport.flipDateformat(selectedReport.getFrom().toString()) + "\r\n" + "To: "
					+ selectedReport.flipDateformat(selectedReport.getTo().toString());

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose:set pane to be visable and enable all of her components
	 *
	 * @param pane
	 */
	void openPane(VBox pane) {
		pane.setOpacity(1);
		for (Node n : pane.getChildren())
			n.setDisable(false);

	}

	
	/**
	 * 
	 * @author Ira Goor 
	 * method purpose:dim pane visibility by chosen value and disable all of her components
	 *
	 * @param pane
	 * @param opacity: amount to dim by (0-1)
	 */
	void disablePane(VBox pane, double opacity) {
		pane.setOpacity(opacity);
		for (Node n : pane.getChildren())
			n.setDisable(true);
	}

	/**
	 * @author Ira Goor
	 * method purposes:
	 * set singleton,open loading buffer while page is loading, disable Graphic report info pane,
	 * set filter buttons color by default state and call all reports from DB
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		buffer.setVisible(true);
		disablePane(createGraphicsPane, 0);
		filterButtons=new ArrayList<Button>();
		
		filterButtons.add(activitybtn);
		filterButtons.add(performencesbtn);
		filterButtons.add(delaysBtn);
		for(Button btn: filterButtons)
			btn.setStyle("-fx-background-color:#f0ad4e ;-fx-text-fill: white ;");
		filterButtons.add(showAllBtn);
		showAllBtn.setStyle("-fx-background-color:#337ab7 ;-fx-text-fill: white ;");
		
		getReports();

		
	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: call server for all of the reports in DB
	 *
	 */
	public void getReports() {
		App.client.handleMessageFromClientUI(new Message(OperationType.GetReports, "Select * From Reports"));

	}
	
	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: reset all buttons to unclicked color docomention 
	 *
	 */
	private void setAllfilterBtnToSameStyle()
	{
		for(Button btn: filterButtons )
			btn.setStyle("-fx-background-color:#f0ad4e ;-fx-text-fill: white ;");

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose:retrive all reports from database and set filter lists
	 * also close loading buffer
	 *
	 * @param obj:Array list of All reports from DB
	 */
	public void setReportsToList(Object obj) {
		ArrayList<Report> info = (ArrayList<Report>) obj;
		allReports = FXCollections.observableArrayList();
		reportsActivty = FXCollections.observableArrayList();
		reportsPerformences = FXCollections.observableArrayList();
		reportsDelays = FXCollections.observableArrayList();

		for (Report repo : info) {
			allReports.add(repo);
			if (repo.getType().equals("Activity"))
				reportsActivty.add(repo);
			if (repo.getType().equals("Performences"))
				reportsPerformences.add(repo);
			if (repo.getType().equals("Delays"))
				reportsDelays.add(repo);
		}

		buffer.setVisible(false);
		reportList.setItems(allReports);

	}
	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: if object is not in PC create it  
	 *
	 * @param obj
	 * @throws FileNotFoundException
	 */

	public void createInPC(Object obj) throws FileNotFoundException {

		PrintWriter csvFile;
		Report report = (Report) obj;

		new File(path).mkdirs();

		csvFile = new PrintWriter(file);
		csvFile.write(report.getData());
		csvFile.close();
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
		}

	}
	public void createInPC() throws FileNotFoundException {

		PrintWriter csvFile;
		

		new File(path).mkdirs();

		csvFile = new PrintWriter(file);
		csvFile.write(selectedReport.getData());
		csvFile.close();
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
		}

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: break report data from string to maps to make it possible to create graphics
	 * and calculate average 
	 * (ask after for the first time after we finish the report so it's not in original file)
	 */
	private void breakReportData() {
		breakColumns();
		breakDatesAndData();
		calcAvg();
	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: break every report by frequency amounts and mapped by frequency groups name
	 * generic function to make it easier to add report types
	 */
	private void breakDatesAndData() {
		datesAndData = new TreeMap<String, ArrayList<Integer>>();
		String[][] rows = breakString();

		for (int i = 1; i < rows[0].length - 2; i++) {
			String key = rows[0][i];
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for (int j = 1; j < rows.length; j++) {
				temp.add(Integer.valueOf(rows[j][i]));
			}
			datesAndData.put(key, temp);
		}

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose:break every reports by parameters ask for this reports and map statistics info to them
	 *
	 */
	private void breakColumns() {
		columns = new TreeMap<String, ArrayList<Double>>();

		String[][] rows = breakString();

		int medianSpot = rows[0].length - 2;
		int sdSpot = rows[0].length - 1;
		for (int i = 1; i < rows.length; i++) {
			ArrayList<Double> temp = new ArrayList<Double>();
			temp.add(Double.valueOf(rows[i][medianSpot]));
			temp.add(Double.valueOf(rows[i][sdSpot]));
			columns.put(rows[i][0], temp);
		}
	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose:claculate average every parameter in report
	 *
	 */
	private void calcAvg() {
		int sum = 0;
		int i = 0;
		for (String c : columns.keySet()) {
			for (String s : datesAndData.keySet())
				sum += datesAndData.get(s).get(i);
			double avg = sum;

			avg = avg / datesAndData.keySet().size();

			columns.get(c).add(avg);
			i++;
			sum = 0;
		}

	}

	/**
	 * 
	 * @author Ira Goor 
	 * method purpose:split string to a String matrix of values
	 *
	 * @return
	 */
	private String[][] breakString() {
		String[] firstSplit = selectedReport.getData().split("\r\n");
		String[][] rows = new String[firstSplit.length][];
		for (int i = 0; i < rows.length; i++)
			rows[i] = firstSplit[i].split(",");
		return rows;
	}
	/**
	 * 
	 * @author Ira Goor 
	 * method purpose: calc for period report period time for group 
	 *
	 * @return long periodTime
	 */
	private long calcPeriodTime()
	{
        ZonedDateTime from = Tools.convertDateSQLToZoned(selectedReport.getFrom());

        ZonedDateTime to = Tools.convertDateSQLToZoned(selectedReport.getTo());
        long totalTime = Duration.between(from, to).toDays();
        boolean check = totalTime < 12 ? false : true;
        long peirodTime = totalTime < 12 ? totalTime : totalTime % 12 == 0 ? totalTime / 12 : (totalTime / 12) + 1;
        return peirodTime;
	}
}
