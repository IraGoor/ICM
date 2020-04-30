package common.entity;

import java.io.Serializable;
import java.sql.Date;


import java.sql.*;

/**
 * Evaluation Report is a data structure for client-server communication.
 */
public class EvaluationReport implements Serializable {
    private int reportID;
    private String requestID;
    private String system_id;
    private String required_change;
    private String expected_result;
    private String expected_risks;
    private Date estimated_time;
    private Timestamp timestamp;

    /**
     * EvaluationReport constructor, defined by Database fields.
     * @param reportID
     * @param requestID
     * @param system_id
     * @param required_change
     * @param expected_result
     * @param expected_risks
     * @param estimated_time
     * @param timestamp
     */
    public EvaluationReport(int reportID, String requestID, String system_id, String required_change, String expected_result, String expected_risks, Date estimated_time, Timestamp timestamp) {
        this.reportID = reportID;
        this.requestID = requestID;
        this.system_id = system_id;
        this.required_change = required_change;
        this.expected_result = expected_result;
        this.expected_risks = expected_risks;
        this.estimated_time = estimated_time;
        this.timestamp = timestamp;
    }

    /**
     * @return All Evaluation Report object data to String.
     */
    @Override
    public String toString() {
        return "EvaluationReport{" +
                "reportID=" + reportID +
                ", requestID='" + requestID + '\'' +
                ", system_id='" + system_id + '\'' +
                ", required_change='" + required_change + '\'' +
                ", expected_result='" + expected_result + '\'' +
                ", expected_risks='" + expected_risks + '\'' +
                ", estimated_time='" + estimated_time + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    // Setters:

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public void setRequired_change(String required_change) {
        this.required_change = required_change;
    }

    public void setExpected_result(String expected_result) {
        this.expected_result = expected_result;
    }

    public void setExpected_risks(String expected_risks) {
        this.expected_risks = expected_risks;
    }

    public void setEstimated_time(Date estimated_time) {
        this.estimated_time = estimated_time;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    // Getters:

    public int getReportID() {
        return reportID;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getSystem_id() {
        return system_id;
    }

    public String getRequired_change() {
        return required_change;
    }

    public String getExpected_result() {
        return expected_result;
    }

    public String getExpected_risks() {
        return expected_risks;
    }

    public Date getEstimated_time() {
        return estimated_time;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
