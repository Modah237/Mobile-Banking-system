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
public class Transactions {

    public Integer getRef_code() {
        return Ref_code;
    }

    public void setRef_code(Integer Ref_code) {
        this.Ref_code = Ref_code;
    }

    public String getRepicient_name() {
        return Repicient_name;
    }

    public void setRepicient_name(String Repicient_name) {
        this.Repicient_name = Repicient_name;
    }

    public Integer getTID() {
        return TID;
    }

    public void setTID(Integer TID) {
        this.TID = TID;
    }

    public Integer getFR_ACC() {
        return FR_ACC;
    }

    public void setFR_ACC(Integer FR_ACC) {
        this.FR_ACC = FR_ACC;
    }

    public Integer getT0_ACC() {
        return T0_ACC;
    }

    public void setT0_ACC(Integer T0_ACC) {
        this.T0_ACC = T0_ACC;
    }

    public String getTR_DATE() {
        return TR_DATE;
    }

    public void setTR_DATE(String TR_DATE) {
        this.TR_DATE = TR_DATE;
    }

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String Narration) {
        this.Narration = Narration;
    }

    public Integer getTR_TYPE() {
        return TR_TYPE;
    }

    public void setTR_TYPE(Integer TR_TYPE) {
        this.TR_TYPE = TR_TYPE;
    }
    private  Integer TID;
    private Integer FR_ACC;
    private Integer T0_ACC;
    private String TR_DATE;
    private String Narration;
    private Integer TR_TYPE;
    private Double Amount;

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double Amount) {
        this.Amount = Amount;
    }
    private Integer Ref_code;
    private String Repicient_name;
    
    public static final int TRANSFER = 3;
    public static final int WITHDRAW = 2;
    public static final int DEPOSIT = 1;
    public static final int PAY_BILL = 4;
}
