package common;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import common.entity.ChangeRequest;
import common.entity.EmployeeUser;
import common.entity.Report;
import common.entity.StageName;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Tools {

//	public static List<String> convertMaptoStringsList(Map<String, List<Object>> map) {
//
//		   List<String>  listOfValue = new ArrayList<String>();
//		   for(Map m: map){ // loop through the maps
//		       listOfValue.addAll(map.values()); // append the values in listOfValue
//		   }
//	}

    public static void setTitlePane(long estimatedTime, TitledPane titledPane, Text titledPane_Text) {
        if (estimatedTime >= 0) {
            titledPane_Text.setText(String.valueOf(estimatedTime) + " Days left to complete this stage");
            titledPane.getStyleClass().removeAll();
            titledPane.getStyleClass().add("info");
        }
        //need to send this to time exceptions..
        else {
            titledPane_Text.setText(String.valueOf(Math.abs(estimatedTime)) + " You are Late");
            titledPane.getStyleClass().removeAll();
            titledPane.getStyleClass().add("danger");
        }
    }

    public static ArrayList<String> disassemble(ResultSet rs, int NumOfCol) {
        ArrayList<String> returnString = new ArrayList<String>();
        try {
            while (rs.next()) {
                // Attention ! must run from 1 to NumOfCol (Not from zero)
                for (int i = 1; i <= NumOfCol; i++)
                    returnString.add(rs.getString(i));
            }
            rs.close();
            return returnString;
        } catch (SQLException e) {
            System.out.println("Tools.disassemble() - int NumOfCol Error");
            e.printStackTrace();
        }
        return returnString;
    }

    // Idan : I changed it from Map<String , List<Object>>
    public static Map<Object, List<Object>> resultSetToMap(ResultSet rs) throws SQLException {
        ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
        int columns = md.getColumnCount();
        Map<Object, List<Object>> map = new HashMap<>(columns);
        for (int i = 1; i <= columns; ++i) {
            map.put(md.getColumnName(i), new ArrayList<>());
        }
        while (rs.next()) {
            for (int i = 1; i <= columns; ++i) {
                map.get(md.getColumnName(i)).add(rs.getObject(i));
            }
        }

        return map;
    }

    /**
     * @param str
     * @return
     * @author Ira Goor
     * method purpose:convert String from DB to Enum StageName
     */
    public static StageName convertStringToStageName(String str) {
        String name = str.toUpperCase();
        if (name.equals("EVALUATION"))
            return StageName.EVALUATION;
        else if (name.equals("DECISION"))
            return StageName.DECISION;
        else if (name.equals("EXECUTION"))
            return StageName.EXECUTION;
        else if (name.equals("VALIDATION"))
            return StageName.VALIDATION;
        else if (name.equals("CLOUSRE"))
            return StageName.CLOUSRE;
        else
            return StageName.INIT;

    }

    public static ZonedDateTime convertDateSQLToZoned(Date sqlDate) {
        if (sqlDate == null)
            return null;
        ZonedDateTime t = ZonedDateTime.of(sqlDate.toLocalDate().atStartOfDay(), ZoneId.systemDefault());
        return t;
    }

    public static int convertStageNameToInt(StageName name) {
        switch (name) {
            case EVALUATION:
                return 0;
            case DECISION:
                return 1;
            case EXECUTION:
                return 2;
            case VALIDATION:
                return 3;
            case CLOUSRE:
                return 4;
            default:
                return -1;

        }
    }

    public static TreeMap<String, ArrayList<Integer>> frequncyDistrbution(Report report) {
        /**
         * frequency: map Key Holds period of time as a string,arraylist holds value of data wanted(active/canceled/frozen/done)
         * from: report start time
         * to: report end time
         * totalTime: total days in report
         * periodTime: Duration of frequency
         *
         */

        TreeMap<String, ArrayList<Integer>> frequency = new TreeMap<String, ArrayList<Integer>>();
        ZonedDateTime from = Tools.convertDateSQLToZoned(report.getFrom());

        ZonedDateTime to = Tools.convertDateSQLToZoned(report.getTo());
        long totalTime = Duration.between(from, to).toDays();
        boolean check = totalTime < 12 ? false : true;
        long peirodTime = totalTime < 12 ? totalTime : totalTime % 12 == 0 ? totalTime / 12 : (totalTime / 12) + 1;
        int groups = (int) (totalTime / peirodTime);
        System.out.println(groups);
        ZonedDateTime startTime = from;
        ZonedDateTime endPeriod = null;
        String period;

        if (check) {
            for (int i = 1; i < groups; i++) {
                endPeriod = startTime.plusDays(peirodTime - 1);
                period = DateTimeFormatter.ofPattern("yyyy--MM--dd").format(startTime) + " "
                        + DateTimeFormatter.ofPattern("yyyy--MM--dd").format(endPeriod);
                frequency.put(period, new ArrayList<Integer>());
                startTime = startTime.plusDays(peirodTime);

            }

            period = DateTimeFormatter.ofPattern("yyyy--MM--dd").format(startTime) + " "
                    + DateTimeFormatter.ofPattern("yyyy--MM--dd").format(to);
            frequency.put(period, new ArrayList<Integer>());

        } else {
            for (int i = 0; i <= (int) totalTime; i++) {
                frequency.put(DateTimeFormatter.ofPattern("yyyy--MM--dd").format(startTime), new ArrayList<Integer>());
                startTime = startTime.plusDays(1);
            }

        }
        return frequency;

    }

    public static double calculateSD(ArrayList<Integer> numArray) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.size();
        for(double num : numArray) {
            sum += num;
        }
        double mean = sum/length;
        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }

    public static int calcMedian(ArrayList<Integer> numArray) {
        Collections.sort(numArray);
        int middle = (numArray.size() / 2);
        return numArray.get(middle);

    }

    /**
     * @param oldDeadLineTime - gets the old deadline of each stage
     * @param freezeTime      - gets the suspension time from the DB via the request treatment controller
     * @return new extended deadline
     */
    public static ZonedDateTime calculatedAddedDaysAfterSuspence(ZonedDateTime oldDeadLineTime, ZonedDateTime freezeTime) {
        ZonedDateTime newDeadLineTime = ZonedDateTime.now();

        return newDeadLineTime;
    }


    public static void fillRequestPanes(Text requestID, TextArea existingCondition, TextArea descripitionsTextArea,
                                        TextField inchargeTF, Text departmentID, Text dueDateLabel, Text requestNameLabel,
                                        ChangeRequest selectedRequestInstance) {
        requestID.setText("" + selectedRequestInstance.getRequestID());
        existingCondition.setText(selectedRequestInstance.getExistingCondition());
        descripitionsTextArea.setText(selectedRequestInstance.getRemarks());
        departmentID.setText(selectedRequestInstance.getInfoSystem());
        requestNameLabel.setText(selectedRequestInstance.getInitiator());
        if (dueDateLabel != null)
            dueDateLabel
                    .setText(selectedRequestInstance.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

    }

    public static void fillEmployeesPanes(Text WorkerID, TextField nameTf, TextField SurenameTf, TextField EmailTf,
                                          TextField PositionTf, TextField expertiseTf, EmployeeUser selectedEmployeeInstance) {
        WorkerID.setText("" + selectedEmployeeInstance.getWorkerID());
        nameTf.setText(selectedEmployeeInstance.getFirstName());
        SurenameTf.setText(selectedEmployeeInstance.getLastName());
        EmailTf.setText(selectedEmployeeInstance.getEmail());
        PositionTf.setText(selectedEmployeeInstance.getRoleInOrg());
//		if (expertiseTf != null)
//			expertiseTf.setText(selectedEmployeeInstance.getSystemID());

    }

    public static void highlightProgressBar(ImageView stage1, ImageView stage2, ImageView stage3, ImageView stage4, ImageView stage5, ChangeRequest currentRequest) {
        switch (currentRequest.getCurrentStage()) {

            case "EVALUATION":
                imgStage_setAsCurrent(stage1);
                imgStage_setAsBlocked(stage2);
                imgStage_setAsBlocked(stage3);
                imgStage_setAsBlocked(stage4);
                imgStage_setAsBlocked(stage5);
                break;
            case "DECISION":
                imgStage_setAsPassed(stage1);
                imgStage_setAsCurrent(stage2);
                imgStage_setAsBlocked(stage3);
                imgStage_setAsBlocked(stage4);
                imgStage_setAsBlocked(stage5);
                break;
            case "EXECUTION":
                imgStage_setAsPassed(stage1);
                imgStage_setAsPassed(stage2);
                imgStage_setAsCurrent(stage3);
                imgStage_setAsBlocked(stage4);
                imgStage_setAsBlocked(stage5);
                break;
            case "VALIDATION":
                imgStage_setAsPassed(stage1);
                imgStage_setAsPassed(stage2);
                imgStage_setAsPassed(stage3);
                imgStage_setAsCurrent(stage4);
                imgStage_setAsBlocked(stage5);
                break;
            case "CLOSURE":
                imgStage_setAsPassed(stage1);
                imgStage_setAsPassed(stage2);
                imgStage_setAsPassed(stage3);
                imgStage_setAsPassed(stage4);
                imgStage_setAsCurrent(stage5);
                break;
            default:
                imgStage_setAsBlocked(stage1);
                imgStage_setAsBlocked(stage2);
                imgStage_setAsBlocked(stage3);
                imgStage_setAsBlocked(stage4);
                imgStage_setAsBlocked(stage5);
                break;
        }
    }

    private static void imgStage_setAsBlocked(ImageView img) {
        img.getStyleClass().add("img_stage_blocked");
        img.setOnMouseClicked(null);
    }

    private static void imgStage_setAsPassed(ImageView img) {
        img.getStyleClass().add("img_stage_passed");
    }

    private static void imgStage_setAsCurrent(ImageView img) {
        img.getStyleClass().add("img_stage_current");
    }

    public static long DaysDifferenceFromToday(ZonedDateTime dateToCompare) {
        if (dateToCompare == null)
            return -999999999;
        //return Duration.between(ZonedDateTime.now(ZoneId.of( "Asia/Jerusalem")), dateToCompare).toDays();
        return Period.between(dateToCompare.toLocalDate(), ZonedDateTime.now(ZoneId.of("Asia/Jerusalem")).plusDays(1).toLocalDate()).getDays();

    }
}
