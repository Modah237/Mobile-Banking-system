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
public class Users {

    public Users() {
        this.Username = "";
        this.Password = "";
        this.creation = "";
        this.user_id = 0;
        this.user_acc_no = 0;
        this.active = true;
    }

    public Users(String Username, String Password, String creation, Integer user_id, Integer user_acc_no, boolean active) {
        this.Username = Username;
        this.Password = Password;
        this.creation = creation;
        this.user_id = user_id;
        this.user_acc_no = user_acc_no;
        this.active = active;
    }

    public Users(Users item) {
        this.active = item.isActive();
        this.Username = item.getUsername();
        this.Password = item.getPassword();
        this.creation = item.getCreation();
        this.user_id = item.getUser_id();
        this.user_acc_no = item.getUser_acc_no();
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getUser_acc_no() {
        return user_acc_no;
    }

    public void setUser_acc_no(Integer user_acc_no) {
        this.user_acc_no = user_acc_no;
    }
    private String Username;
    private String Password;
    private String creation;
    private Integer user_id;
    private Integer user_acc_no;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
