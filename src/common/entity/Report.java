package common.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Report Object contains individual report information.
 * type, date of creation, reported date range, data (generated string for CSV Files) and period(for supported types)
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class Report implements Serializable {

    private String type;
    private Date created;
    private Date from;
    private Date to;
    private String data;
    private ArrayList<String> period;


    /**
     * @param type
     * @param created
     * <p>
     * Constructors purpose: non period Report
     */
    public Report(String type, Date created) {
        this.type = type;
        this.created = created;
        this.period = new ArrayList<String>();
        this.period.add("Activity");
    }

    /**
     * @param type
     * @param from
     * @param to
     * <p>
     * Constructors purpose: for a period report
     */
    public Report(String type, Date created, Date from, Date to) {
        this.type = type;
        this.created = created;
        this.from = from;
        this.to = to;
        this.period = new ArrayList<String>();
        period.add("Activity");

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public ArrayList<String> getPeriod() {
        return period;
    }

    public void setPeriod(ArrayList<String> period) {
        this.period = period;
    }

    public boolean isPeriodReport() {
        return period.contains(type);
    }

    public String flipDateformat(String date) {
        StringBuilder s = new StringBuilder();
        s.append(date.substring(8));

        s.append('-');
        s.append(date.substring(5, 7));
        s.append('-');
        s.append(date.substring(0, 4));
        return s.toString();
    }


    @Override
    public String toString() {
        if (isPeriodReport())
            return type + " created " + flipDateformat(created.toString()) + " from " + flipDateformat(from.toString()) + " to " + flipDateformat(to.toString());
        else
            return type + " created " + flipDateformat(created.toString());
    }


}
