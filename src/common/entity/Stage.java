package common.entity;

import java.io.Serializable;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Structure of a stage in request treatment.
 * fits to ICM Database.
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class Stage implements Serializable {

    private int requestID;
    private String stageName;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZonedDateTime deadline;
    private String incharge;
    private int init;
    private int init_confirmed;
    private int extension_days;
    private String extension_reason;
    private String extension_decision;
    private String preStage;

    public Stage(int requestID, String stageName, ZonedDateTime startTime, ZonedDateTime endTime, ZonedDateTime deadline, String incharge, int init, int init_confirmed, int extension_days, String extension_reason, String extension_decision, String preStage) {
        this.requestID = requestID;
        this.stageName = stageName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deadline = deadline;
        this.incharge = incharge;
        this.init = init;
        this.init_confirmed = init_confirmed;
        this.extension_days = extension_days;
        this.extension_reason = extension_reason;
        this.extension_decision = extension_decision;
        this.preStage = preStage;
    }

    /**
     * Generate a string of object attributes.
     * @return
     */
    @Override
    public String toString() {
        return "Stage{" +
                "requestID=" + requestID +
                ", stageName='" + stageName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", deadline=" + deadline +
                ", incharge='" + incharge + '\'' +
                ", preStage='" + preStage + '\'' +
                ", extension_days=" + extension_days +
                ", extension_reason='" + extension_reason + '\'' +
                ", extension_decision='" + extension_decision + '\'' +
                ", init=" + init +
                ", init_confirmed=" + init_confirmed +
                '}';
    }


    // Getters:

    public int getRequestID() {
        return requestID;
    }

    public int getExtension_days() {
        return extension_days;
    }

    public String getExtension_reason() {
        return extension_reason;
    }

    public String getExtension_decision() {
        return extension_decision;
    }

    public String getStageName() {
        return stageName;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }

    public String getIncharge() {
        return incharge;
    }


    public int getInit() {
        return init;
    }

    public int getInit_confirmed() {
        return init_confirmed;
    }

    public String getPreStage() {
        return preStage;
    }

}
