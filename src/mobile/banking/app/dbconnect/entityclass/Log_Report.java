/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.dbconnect.entityclass;

/**
 *
 * @author User
 */
public class Log_Report {

    public String getLog_in() {
        return log_in;
    }

    public void setLog_in(String log_in) {
        this.log_in = log_in;
    }

    public String getLog_out() {
        return log_out;
    }

    public void setLog_out(String log_out) {
        this.log_out = log_out;
    }

    public String getLog_Id() {
        return Log_Id;
    }

    public void setLog_Id(String Log_Id) {
        this.Log_Id = Log_Id;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    private String log_in;
    private String log_out;
    private String Log_Id;
    private String user_id;
    private String report;
}
