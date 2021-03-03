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
public class Person_Info {

    public Person_Info() {
        this.Pid = 0;
        this.F_name = "";
        this.L_name = "";
        this.DOB = "";
        this.phone = "";
        this.email = "";
        this.address = "";
        this.profession = "";
        this.orgamization = "";
        this.country = "";
        this.state = "";
    }

    public Person_Info(Integer Pid, String F_name, String L_name, String DOB, String phone, String email, String address, String profession, String orgamization, String country, String state) {
        this.Pid = Pid;
        this.F_name = F_name;
        this.L_name = L_name;
        this.DOB = DOB;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.profession = profession;
        this.orgamization = orgamization;
        this.country = country;
        this.state = state;
    }

    public Integer getPid() {
        return Pid;
    }

    public void setPid(Integer Pid) {
        this.Pid = Pid;
    }

    public String getF_name() {
        return F_name;
    }

    public void setF_name(String F_name) {
        this.F_name = F_name;
    }

    public String getL_name() {
        return L_name;
    }

    public void setL_name(String L_name) {
        this.L_name = L_name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOrgamization() {
        return orgamization;
    }

    public void setOrgamization(String orgamization) {
        this.orgamization = orgamization;
    }
    private Integer Pid;
    private String F_name;
    private String L_name;
    private String DOB;
    private String phone;
    private String email;
    private String address;
    private String profession;
    private String orgamization;
    private String country;
    private String state;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
