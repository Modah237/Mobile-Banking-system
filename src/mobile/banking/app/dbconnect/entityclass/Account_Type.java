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
public class Account_Type {

    public String getAcc_Type() {
        return acc_Type;
    }

    public void setAcc_Type(String acc_Type) {
        this.acc_Type = acc_Type;
    }

    public Integer getAT_Id() {
        return AT_Id;
    }

    public void setAT_Id(Integer AT_Id) {
        this.AT_Id = AT_Id;
    }
    private String acc_Type;
    private Integer AT_Id;
}
