/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.dbconnect.entityclass;

import java.util.Calendar;
import java.util.Random;
import mobile.banking.app.dbconnect.DbConnection;

/**
 *
 * @author User
 */
public class Accounts {

    public Accounts() {
        this.accNo = 0;
        this.cardNo = 0;
        this.cardPin = 0;
        this.balance = 0.0;
        this.date = null;
        this.accType = 0;
        this.PID_Owner = 0;
    }

    public Accounts(Integer accNo, Integer cardNo, Integer cardPin, Double balance, String date, Integer accType, Integer PID_Owner) {
        this.accNo = accNo;
        this.cardNo = cardNo;
        this.cardPin = cardPin;
        this.balance = balance;
        this.date = date;
        this.accType = accType;
        this.PID_Owner = PID_Owner;
    }

    public Integer getAccNo() {
        return accNo;
    }

    public void setAccNo(Integer accNo) {
        this.accNo = accNo;
    }

    public Integer getCardNo() {
        return cardNo;
    }

    public void setCardNo(Integer cardNo) {
        this.cardNo = cardNo;
    }

    public Integer getCardPin() {
        return cardPin;
    }

    public void setCardPin(Integer cardPin) {
        this.cardPin = cardPin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAccType() {
        return accType;
    }

    public void setAccType(Integer accType) {
        this.accType = accType;
    }

    public Integer getPID_Owner() {
        return PID_Owner;
    }

    public void setPID_Owner(Integer PID_Owner) {
        this.PID_Owner = PID_Owner;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer generateAccountNumber(int accountTypeCode) {
        // create instance of Random class 
        Random rand = new Random();

        // Generate random integers in range 0 to 99 
        int randSeq = rand.nextInt(100);
        System.out.println("Random Integers: " + randSeq);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("Current Year: " + year);

        String accNum = "";

        accNum += year-2000;
        accNum += randSeq;
        accNum += new DbConnection().getSeqAccountNEXT();
        
        accNum += accountTypeCode;
        this.setAccNo(Integer.parseInt(accNum));

        return this.getAccNo();
    }
    
    public Integer generateAccountNumber() {
        // create instance of Random class 
        Random rand = new Random();

        // Generate random integers in range 0 to 999 
        int randSeq = rand.nextInt(100);
        System.out.println("Random Integers: " + randSeq);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("Current Year: " + year);

        String accNum = "";

        accNum += year-2000;
        accNum += randSeq;
        accNum += new DbConnection().getAccountsCount();
        accNum += this.getAccType();
        this.setAccNo(Integer.parseInt(accNum));

        return this.getAccNo();
    }
    
    public Integer generateCardNumber(){
        // create instance of Random class 
        Random rand = new Random();

        // Generate random integers in range 0 to 999 
        int randSeq = rand.nextInt(10000);
        System.out.println("Random Integers: " + randSeq);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        System.out.println("Current Year: " + year);
        
        String cardNum = "";
        
        cardNum += year;
        cardNum += randSeq;
        
        this.setCardNo(Integer.parseInt(cardNum));
        
        return this.getCardNo();
    }
    
    public Integer generateCardPIN(){
        // create instance of Random class 
        Random rand = new Random();

        // Generate random integers in range 0 to 999 
        int randSeq = rand.nextInt(10000);
        System.out.println("Random Integers: " + randSeq);
        
        this.setCardPin(randSeq);
        
        return this.getCardPin();
    }

    private Integer accNo;
    private Integer cardNo;
    private Integer cardPin;
    private Double balance;
    private String date;
    private Integer accType;
    private Integer PID_Owner;

    public static void main(String[] args) {
        Accounts acc = new Accounts();
        System.out.println(acc.generateAccountNumber(100));
        System.out.println(acc.generateCardNumber());
        System.out.println(acc.generateCardPIN());
    }
}
