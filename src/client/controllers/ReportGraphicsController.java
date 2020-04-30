package client.controllers;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

import common.Tools;
import common.entity.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ReportGraphicsController extends AppController implements Initializable {

	@FXML
	private HBox stsPane;
    @FXML
    private HBox freqPane;

    @FXML
    private Label reportInfo;
    
    
    @FXML
    private Button generateReportBtn;

    @FXML
    private Button viewReportsBtn;

    @FXML
    void BackToGenereteReport(ActionEvent event) {
    	loadPage("Reports");
    }

    @FXML
    void backToViewReports(ActionEvent event) {
    	loadPage("ViewReports");
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Report report=ViewReportsController.selectedReport;
		reportInfo.setText(labelFormat(report));
		if(report.getType().equals("Activity"))
		{
		CategoryAxis xAxis=new CategoryAxis();
		xAxis.tickLabelFontProperty().set(Font.font(10));
		NumberAxis yAxis= new NumberAxis();
		BarChart amount =new BarChart(xAxis, yAxis, getDataActivtiys1());
		amount.setTitle("Requests");
		CategoryAxis xAxis2=new CategoryAxis();
		NumberAxis yAxis2= new NumberAxis();
		yAxis2.setLabel("Days");
		xAxis2.tickLabelFontProperty().set(Font.font(10));
		BarChart days =new BarChart(xAxis2, yAxis2, getDataActivtiys2());
		days.setTitle("Work Days");
		CategoryAxis xAxis3=new CategoryAxis();
		NumberAxis yAxis3= new NumberAxis();
		BarChart avner =new BarChart(xAxis3, yAxis3, getDataActivtiys3());
		avner.setTitle("Statistics: Request");
		freqPane.getChildren().addAll(amount,days);
		//stsPane.getChildren().add(avner);
		CategoryAxis xAxis4=new CategoryAxis();
		NumberAxis yAxis4= new NumberAxis();
		BarChart avner2 =new BarChart(xAxis4, yAxis4, getDataActivtiys4());
		avner2.setTitle("Statistics: WorkDays");
		stsPane.getChildren().addAll(avner,avner2);
		}
		else if(report.getType().equals("Performences"))
		{
			CategoryAxis xAxis=new CategoryAxis();
			NumberAxis yAxis= new NumberAxis();
			yAxis.setLabel("Days");
			BarChart amount =new BarChart(xAxis, yAxis, getDataPerfomencesFreq());
			amount.setTitle("Work days");
			freqPane.getChildren().add(amount);
			CategoryAxis xAxis2=new CategoryAxis();
			NumberAxis yAxis2= new NumberAxis();
			BarChart statsChart =new BarChart(xAxis2, yAxis2,getDataPerfomencesStats());
			statsChart.setTitle("Statistics: Days");
			stsPane.getChildren().addAll(statsChart);
		}
		else 
		{
			
			CategoryAxis xAxis=new CategoryAxis();
			NumberAxis yAxis= new NumberAxis();
			yAxis.setLabel("Days");
			BarChart days =new BarChart(xAxis, yAxis, getDataDelaysDaysFreq());
			CategoryAxis xAxis0=new CategoryAxis();
			NumberAxis yAxis0= new NumberAxis();
			yAxis.setLabel("Times");
			BarChart amount =new BarChart(xAxis0, yAxis0, getDataDelaysDTimesFreq());
			freqPane.getChildren().addAll(amount,days);
			CategoryAxis xAxis2=new CategoryAxis();
			NumberAxis yAxis2= new NumberAxis();
			BarChart statsChart =new BarChart(xAxis2, yAxis2,getDataDelaysStatsDays());
			statsChart.setTitle("Statistics: Days");
			yAxis2.setLabel("Days");
			CategoryAxis xAxis3=new CategoryAxis();
			NumberAxis yAxis3= new NumberAxis();
			BarChart statsChartTimes =new BarChart(xAxis3, yAxis3,getDataDelaysStatsTomes());
			statsChartTimes.setTitle("Statistics: Times");
			stsPane.getChildren().addAll(statsChartTimes,statsChart);
		}
		
	}
	
	
    private ObservableList<XYChart.Series<String, Integer>> getDataActivtiys1()
    {
    	
    	ObservableList<XYChart.Series<String, Integer>> data=FXCollections.observableArrayList();
    	Series<String,Integer> active=new Series<String, Integer>();
    	Series<String,Integer> canceld=new Series<String, Integer>();
    	Series<String,Integer> frozen=new Series<String, Integer>();
    	Series<String,Integer> done=new Series<String, Integer>();
    	active.setName("Active");
    	canceld.setName("Canceld");
    	frozen.setName("Frozen");
    	done.setName("Done");
    	for(String s:ViewReportsController.datesAndData.keySet())
    	{
    		String col;
    		if(timePeriod())
    			col=fixColumnPeriod(s);
    		else
    			col=fixColumnDay(s);
    
    		active.getData().add(new XYChart.Data(col,ViewReportsController.datesAndData.get(s).get(0)));
    		canceld.getData().add(new XYChart.Data(col,ViewReportsController.datesAndData.get(s).get(1)));
    		frozen.getData().add(new XYChart.Data(col,ViewReportsController.datesAndData.get(s).get(2)));
    		done.getData().add(new XYChart.Data(col,ViewReportsController.datesAndData.get(s).get(3)));
    	}
    	
    	data.addAll(active,canceld,frozen,done);

    	return data;
    		
    }
    private boolean timePeriod()
    {
    	return Duration.between(Tools.convertDateSQLToZoned(ViewReportsController.selectedReport.getFrom()), Tools.convertDateSQLToZoned(ViewReportsController.selectedReport.getTo())).toDays()<12?false:true;
    			
    }
    private ObservableList<XYChart.Series<String, Integer>> getDataPerfomencesFreq()
    {
    	
    	ObservableList<XYChart.Series<String, Integer>> data=FXCollections.observableArrayList();
    	Series<String,Integer> repeated=new Series<String, Integer>();
    	Series<String,Integer> extensions=new Series<String, Integer>();
    	extensions.setName("Extensions");
    	repeated.setName("Repeated");
    	for(String s:ViewReportsController.datesAndData.keySet())
    	{
    		extensions.getData().add(new XYChart.Data(s,ViewReportsController.datesAndData.get(s).get(0)));
    		repeated.getData().add(new XYChart.Data(s,ViewReportsController.datesAndData.get(s).get(1)));
    	}
    	
    	data.addAll(extensions,repeated);

    	return data;
    		
    }
    
    private ObservableList<XYChart.Series<String, Integer>> getDataDelaysDaysFreq()
    {
    	
    	ObservableList<XYChart.Series<String, Integer>> data=FXCollections.observableArrayList();
    	Series<String,Integer> delays=new Series<String, Integer>();

    	delays.setName("Delayed Days");
    	for(String s:ViewReportsController.datesAndData.keySet())
    	{
    		delays.getData().add(new XYChart.Data(s,ViewReportsController.datesAndData.get(s).get(0)));
    	}
    	
    	data.add(delays);

    	return data;
    		
    }
    private ObservableList<XYChart.Series<String, Integer>> getDataDelaysDTimesFreq()
    {
    	
    	ObservableList<XYChart.Series<String, Integer>> data=FXCollections.observableArrayList();
    	Series<String,Integer> delays=new Series<String, Integer>();

    	delays.setName("Delayed Times");
    	for(String s:ViewReportsController.datesAndData.keySet())
    	{
    		delays.getData().add(new XYChart.Data(s,ViewReportsController.datesAndData.get(s).get(1)));
    	}
    	
    	data.add(delays);

    	return data;
    		
    }
    private ObservableList<XYChart.Series<String, Integer>> getDataActivtiys2()
    {
    	
    	ObservableList<XYChart.Series<String, Integer>> data=FXCollections.observableArrayList();
    	Series<String,Integer> workdays=new Series<String, Integer>();

    	workdays.setName("Work Days");

    	for(String s:ViewReportsController.datesAndData.keySet())
    	{
    		String col;
    		if(timePeriod())
    			col=fixColumnPeriod(s);
    		else
    			col=fixColumnDay(s);
    		workdays.getData().add(new XYChart.Data(col,ViewReportsController.datesAndData.get(s).get(4)));

    	}
    	
    	data.add(workdays);

    	return data;
    		
    }
    private ObservableList<XYChart.Series<String, Double>> getDataDelaysStatsDays()
    {
    	
    	ObservableList<XYChart.Series<String, Double>> data=FXCollections.observableArrayList();
    	Series<String,Double> median=new Series<String, Double>();
    	Series<String,Double> SD=new Series<String, Double>();
    	Series<String,Double> avg=new Series<String, Double>();
    	
    	median.setName("Median");
    	SD.setName("SD");
    	avg.setName("AVG");
    	for(String s:ViewReportsController.columns.keySet())
    	{ 		
    		if(s.equals("Delayed Days"))
    		{
    			median.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(0)));
   				SD.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(1)));
   				avg.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(2)));
    		}
    	}
    	
       	data.addAll(median,SD,avg);

    	return data;
    		
    }
    
    
    private ObservableList<XYChart.Series<String, Double>> getDataDelaysStatsTomes()
    {
    	
    	ObservableList<XYChart.Series<String, Double>> data=FXCollections.observableArrayList();
    	Series<String,Double> median=new Series<String, Double>();
    	Series<String,Double> SD=new Series<String, Double>();
    	Series<String,Double> avg=new Series<String, Double>();
    	
    	median.setName("Median");
    	SD.setName("SD");
    	avg.setName("AVG");
    	for(String s:ViewReportsController.columns.keySet())
    	{ 		
    		if(!s.equals("Delayed Days"))
    		{
    			median.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(0)));
   				SD.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(1)));
   				avg.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(2)));
    		}
    	}
    	
       	data.addAll(median,SD,avg);

    	return data;
    		
    }
    
    
    private ObservableList<XYChart.Series<String, Double>> getDataPerfomencesStats()
    {
    	
    	ObservableList<XYChart.Series<String, Double>> data=FXCollections.observableArrayList();
    	Series<String,Double> median=new Series<String, Double>();
    	Series<String,Double> SD=new Series<String, Double>();
    	Series<String,Double> avg=new Series<String, Double>();
    	
    	median.setName("Median");
    	SD.setName("SD");
    	avg.setName("AVG");
    	for(String s:ViewReportsController.columns.keySet())
    	{ 		
    		median.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(0)));
   			SD.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(1)));
   			avg.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(2)));
    	}
    	
       	data.addAll(median,SD,avg);

    	return data;
    		
    }
    
    private ObservableList<XYChart.Series<String, Double>> getDataActivtiys3()
    {
    	
    	ObservableList<XYChart.Series<String, Double>> data=FXCollections.observableArrayList();
    	Series<String,Double> median=new Series<String, Double>();
    	Series<String,Double> SD=new Series<String, Double>();
    	Series<String,Double> avg=new Series<String, Double>();
    	
    	median.setName("Median");
    	SD.setName("SD");
    	avg.setName("AVG");
    	for(String s:ViewReportsController.columns.keySet())
    	{
    		if(!s.equals("WorkDays"))
    		{
    			median.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(0)));
    			SD.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(1)));
    			avg.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(2)));
    		}
    	}
    	
       	data.addAll(median,SD,avg);

    	return data;
    		
    }
    private ObservableList<XYChart.Series<String, Double>> getDataActivtiys4()
    {
    	
    	ObservableList<XYChart.Series<String, Double>> data=FXCollections.observableArrayList();
    	Series<String,Double> median=new Series<String, Double>();
    	Series<String,Double> SD=new Series<String, Double>();
    	Series<String,Double> avg=new Series<String, Double>();
    	
    	median.setName("Median");
    	SD.setName("SD");
    	avg.setName("AVG");
    	for(String s:ViewReportsController.columns.keySet())
    	{
    		if(s.equals("WorkDays"))
    		{
    			median.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(0)));
    			SD.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(1)));
    			avg.getData().add(new XYChart.Data(s,ViewReportsController.columns.get(s).get(2)));
    		}
    	}
    	
       	data.addAll(median,SD,avg);

    	return data;
    		
    }
    private String fixColumnPeriod(String s)
    {
    	String ret=s.substring(10,12)+"/"+s.substring(6,8)+"/"+s.substring(2,4)+"-\r\n"+s.substring(23,25)+"/"+s.substring(19,21)+"/"+s.substring(15,17);
		return ret;
    	
    }
    private String fixColumnDay(String s)
    {
    	String ret=s.substring(10,12)+"/"+s.substring(6,8)+"/"+s.substring(2,4);
		return ret;
    	
    }
    
    private String labelFormat(Report report)
    {
    	
    	if(!report.isPeriodReport())
    		return "Report type:"+report.getType()+"\t"
    		+ "Created:"+report.flipDateformat(report.getCreated().toString())+"\t";
    	else
    		return "Report type: "+report.getType()+"\t\t"
    		+ "Created: "+report.flipDateformat(report.getFrom().toString())+"\t\t"
    				+ "From: "+report.flipDateformat(report.getFrom().toString())+"\t\t"
    						+ "To: "+report.flipDateformat(report.getTo().toString());
    				
    }

}
