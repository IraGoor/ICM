/**
 *
 */
package common.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * ChangeRequest object contains a request data
 * this object is not connected to currentStage object, until the user use this object.
 * in this case, <code>stage</code> object will be updated in this object.
 *
 */
public class ChangeRequest implements Serializable {

    /**
     * initiator: user creating request
     * request details by user: status,existingCondition,suggestedChange,reasonForChange
     * request optional details by user: attachedFiles,remarks
     * request system created details: requestID,submitTime
     * stages default details: allStages
     * stages details given by user:incharge
     * stages details add by system: repeated (a stage that was repeated so it's results were discarded)
     * Auxiliary stage variable: indexOfCurrentStage
     */
    private String initiator;
    private String currentStage;
    private String intiatorType;
    private String status;
    private int requestID;
    private String infoSystem;
    private String existingCondition;
    private String suggestedChange;
    private String reasonForChange;
    private String remarks;
    private ZonedDateTime dueDate;
    private String filesPaths;
    private ZonedDateTime submitTime;
    private String incharges;
    private Stage currentStageObject;
    private boolean returned;
    private String returnedNote;
    private String email;
    private String prevStage;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ChangeRequest{" +
                "initiator='" + initiator + '\'' +
                ", currentStage='" + currentStage + '\'' +
                ", intiatorType='" + intiatorType + '\'' +
                ", status='" + status + '\'' +
                ", requestID=" + requestID +
                ", infoSystem='" + infoSystem + '\'' +
                ", existingCondition='" + existingCondition + '\'' +
                ", suggestedChange='" + suggestedChange + '\'' +
                ", reasonForChange='" + reasonForChange + '\'' +
                ", remarks='" + remarks + '\'' +
                ", dueDate=" + dueDate +
                ", filesPaths='" + filesPaths + '\'' +
                ", submitTime=" + submitTime +
                ", incharges='" + incharges + '\'' +
                ", currentStageObject=" + currentStageObject +
                ", prevStage='" + prevStage + '\'' +
                '}';
    }

    public String getPrevStage() {
        return prevStage;
    }


    public void setPrevStage(String prevStage) {
        this.prevStage = prevStage;
    }


    public String getCurrentStage() {
        return currentStage;
    }


    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }


    public String getFilesPaths() {
        return filesPaths;
    }


    public void setFilesPaths(String filesPaths) {
        this.filesPaths = filesPaths;
    }


    public String getIncharges() {
        return incharges;
    }


    public void setIncharges(String incharges) {
        this.incharges = incharges;
    }


    public void setCurrentStageObject(Stage currentStageObject) {
        this.currentStageObject = currentStageObject;
    }

    /**
     * ChangeRequest constructor, defined by Database fields.
     * @param initiator
     * @param intiatorType
     * @param status
     * @param requestID
     * @param infoSystem
     * @param existingCondition
     * @param suggestedChange
     * @param reasonForChange
     * @param remarks
     * @param dueDate
     * @param submitTime
     * @param currentStage
     * @param filesPaths
     * @param incharges
     * @param currentStageObject
     */
    public ChangeRequest(String initiator, String intiatorType, String status, int requestID, String infoSystem,
                         String existingCondition, String suggestedChange, String reasonForChange, String remarks,
                         ZonedDateTime dueDate, ZonedDateTime submitTime, String currentStage, String filesPaths, String incharges, Stage currentStageObject) {
        this.initiator = initiator;
        this.intiatorType = intiatorType;
        this.status = status;
        this.requestID = requestID;
        this.infoSystem = infoSystem;
        this.existingCondition = existingCondition;
        this.suggestedChange = suggestedChange;
        this.reasonForChange = reasonForChange;
        this.remarks = remarks;
        this.dueDate = dueDate;
        this.submitTime = submitTime;
        this.currentStage = currentStage;
        this.filesPaths = filesPaths;
        this.incharges = incharges;
        this.currentStageObject = currentStageObject;
        this.prevStage = "INIT";
        this.returned = false;
        this.returnedNote = "";

    }

    public Stage getCurrentStageObject() {
        return currentStageObject;
    }

    /**
     * @return the requestID
     */
    public int getRequestID() {
        return requestID;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the infoSysytem
     *
     */
    public String getInfoSysytem() {
        return infoSystem;
    }

    /**
     * @return the existingCondition
     */
    public String getExistingCondition() {
        return existingCondition;
    }

    /**
     * @return the suggestedChange
     */
    public String getSuggestedChange() {
        return suggestedChange;
    }

    /**
     * @return the reasonForChange
     */
    public String getReasonForChange() {
        return reasonForChange;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }


    public String getInitiator() {
        return this.initiator;
    }

    /**
     * @return the infoSystem
     */
    public String getInfoSystem() {
        return infoSystem;
    }

    /**
     * @return the dueDate
     */
    public ZonedDateTime getDueDate() {
        return dueDate;
    }


    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public void setIntiatorType(String intiatorType) {
        this.intiatorType = intiatorType;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public void setInfoSystem(String infoSystem) {
        this.infoSystem = infoSystem;
    }

    public void setExistingCondition(String existingCondition) {
        this.existingCondition = existingCondition;
    }

    public void setSuggestedChange(String suggestedChange) {
        this.suggestedChange = suggestedChange;
    }

    public void setReasonForChange(String reasonForChange) {
        this.reasonForChange = reasonForChange;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setSubmitTime(ZonedDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public ZonedDateTime getSubmitTime() {
        return submitTime;
    }

    public String getIntiatorType() {
        return intiatorType;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public String getReturnedNote() {
        return returnedNote;
    }

    public void setReturnedNote(String returnedNote) {
        this.returnedNote = returnedNote;
    }
}
