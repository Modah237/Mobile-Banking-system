/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobile.banking.app.dbconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mobile.banking.app.dbconnect.entityclass.Account_Type;
import mobile.banking.app.dbconnect.entityclass.Accounts;
import mobile.banking.app.dbconnect.entityclass.Log_Report;
import mobile.banking.app.dbconnect.entityclass.Person_Info;
import mobile.banking.app.dbconnect.entityclass.Transactions;
import mobile.banking.app.dbconnect.entityclass.Users;

/**
 *
 * @author User
 */
public class DbConnection {

    @Override
    protected void finalize() throws Throwable {
        this.disconnect();
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }

    private final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private final String username = "modah";
    private final String password = "123";
    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    private Users user;
    private Accounts accData;

    public DbConnection() {
        this.initDb();
    }

    private void initDb() {
        this.user = null;
        this.accData = null;
        try {
            Class.forName(DB_DRIVER);

            con = DriverManager.getConnection(URL, username, password);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
    }

    private void disconnect() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean connect(String u, String p) {
        try {
            stmt = con.createStatement();
            this.rs = this.stmt.executeQuery("SELECT * FROM USERS WHERE USERNAME='" + u + "' AND PASSWORD='" + p + "'");
//            return this.rs.next();
            while (this.rs.next()) {
                this.fetchUser();
                System.out.println(user);
                return this.user.isActive();
            }
            this.stmt.close();
            this.rs.close();
            return false;
        } catch (SQLException ex) {
            System.err.println(ex);
            System.err.println("--> LogEvent:: Wrong Credentials");
        }
        return false;
    }

    public Accounts getAccount(String acc) {
        this.accData = new Accounts();
        try {
            stmt = con.createStatement();
            this.rs = this.stmt.executeQuery("SELECT * FROM ACCOUNTS WHERE ACC_NO=" + acc);
//            return this.rs.next();
            while (this.rs.next()) {
                this.fetchAccount();
                System.out.println(this.accData);
                return this.accData;
            }
            this.stmt.close();
            this.rs.close();
            return null;
        } catch (SQLException ex) {
            System.err.println(ex);
            System.err.println("--> LogEvent:: Fetching Account");
        }
        return null;
    }

    protected void fetchUser() throws SQLException {
        this.user = new Users();
        this.user.setUsername(this.rs.getString(1));
        this.user.setPassword(this.rs.getString(2));
        this.user.setCreation(this.rs.getString(3));
        this.user.setUser_id(this.rs.getInt(4));
        this.user.setUser_acc_no(this.rs.getInt(5));
        if (this.rs.getInt(6) == 1) {
            this.user.setActive(true);
        } else {
            this.user.setActive(false);
        }
    }

    protected void fetchAccount() throws SQLException {
        this.accData = new Accounts();
        this.accData.setAccNo(this.rs.getInt(1));
        this.accData.setCardNo(this.rs.getInt(2));
        this.accData.setCardPin(this.rs.getInt(3));
        this.accData.setBalance(this.rs.getDouble(4));
        this.accData.setDate(this.rs.getTime(5).toString());
        this.accData.setAccType(this.rs.getInt(6));
        this.accData.setPID_Owner(this.rs.getInt(7));
    }

    public Users getUser() {
        return user;
    }

    public Users getUser(String accNo__username) {
        this.user = null;
        String query = "SELECT * FROM USERS WHERE USER_ACC_NO = ?";
        if (Character.isLetter(accNo__username.toCharArray()[0])) {
            query = "SELECT * FROM USERS WHERE USERNAME = ?";
            try {
                PreparedStatement pstmt = this.con.prepareStatement(query);
                pstmt.setString(1, accNo__username);
                this.rs = pstmt.executeQuery();
                while (this.rs.next()) {
                    this.fetchUser();
                }
            } catch (SQLException ex) {
                System.err.println(ex);
                System.err.println("--> LogEvent:: Fetching User");
            }
        } else {
            try {
                PreparedStatement pstmt = this.con.prepareStatement(query);
                pstmt.setInt(1, Integer.parseInt(accNo__username));
                this.rs = pstmt.executeQuery();
                while (this.rs.next()) {
                    this.fetchUser();
                }
            } catch (SQLException ex) {
                System.err.println(ex);
                System.err.println("--> LogEvent:: Fetching User");
            }
        }
        return this.getUser();
    }

    public boolean makeTransaction(Transactions trans, Accounts acc1, Accounts acc2) {
        System.out.println("Inserting records into the table...");
        String sql;
        boolean flag = false;
        switch (trans.getTR_TYPE()) {
            case Transactions.DEPOSIT:
                try {
                this.stmt = this.con.createStatement();
                sql = "UPDATE ACCOUNTS "
                        + " SET BALANCE=" + (acc1.getBalance() + trans.getAmount())
                        + " WHERE ACC_NO=" + acc1.getAccNo();
                this.stmt.executeUpdate(sql);
                sql = "INSERT INTO TRANSACTIONS(TID,FR_ACC,TO_ACC,TR_DATE,NARRATION,TR_TYPE,AMOUNT,REF_CODE,RECIPIENT_NAME) "
                        + "VALUES(SEQ_TRANSACTIONS.nextval, " + acc1.getAccNo() + ", NULL, SYSDATE, '" + trans.getNarration() + "', "
                        + trans.getTR_TYPE() + ", " + trans.getAmount() + ", " + trans.getRef_code() + ", '" + trans.getRepicient_name() + "')";
                this.stmt.executeUpdate(sql);
                flag = true;
            } catch (SQLException ex) {
//                    Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("--> LogEvent:: Inserting Transaction");
            }
            break;
            case Transactions.WITHDRAW:
                try {
                this.stmt = this.con.createStatement();

                sql = "UPDATE ACCOUNTS "
                        + " SET BALANCE=" + (acc1.getBalance() - trans.getAmount())
                        + " WHERE ACC_NO=" + acc1.getAccNo();
                this.stmt.executeUpdate(sql);

                sql = "INSERT INTO TRANSACTIONS(TID,FR_ACC,TO_ACC,TR_DATE,NARRATION,TR_TYPE,AMOUNT,REF_CODE,RECIPIENT_NAME) "
                        + "VALUES(SEQ_TRANSACTIONS.nextval, " + acc1.getAccNo() + ", NULL, SYSDATE, '" + trans.getNarration() + "', "
                        + trans.getTR_TYPE() + ", " + trans.getAmount() + ", " + trans.getRef_code() + ", '" + trans.getRepicient_name() + "')";
                this.stmt.executeUpdate(sql);

                flag = true;
            } catch (SQLException ex) {
//                    Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("--> LogEvent:: Inserting Transaction");
            }
            break;
            case Transactions.PAY_BILL:
                //statement
                try {
                this.stmt = this.con.createStatement();

                sql = "UPDATE ACCOUNTS "
                        + " SET BALANCE=" + (acc1.getBalance() - trans.getAmount())
                        + " WHERE ACC_NO=" + acc1.getAccNo();

                this.stmt.executeUpdate(sql);
                sql = "INSERT INTO TRANSACTIONS(TID,FR_ACC,TO_ACC,TR_DATE,NARRATION,TR_TYPE,AMOUNT,REF_CODE,RECIPIENT_NAME) "
                        + "VALUES(SEQ_TRANSACTIONS.nextval, " + acc1.getAccNo() + ", NULL, SYSDATE, '" + trans.getNarration() + "', "
                        + trans.getTR_TYPE() + ", " + trans.getAmount() + ", " + trans.getRef_code() + ", '" + trans.getRepicient_name() + "')";
                this.stmt.executeUpdate(sql);

                flag = true;
            } catch (SQLException ex) {
//                    Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("--> LogEvent:: Inserting Transaction");
            }
            break;
            case Transactions.TRANSFER:
                //statement
                try {
                this.stmt = this.con.createStatement();

                sql = "UPDATE ACCOUNTS "
                        + " SET BALANCE=" + (acc1.getBalance() - trans.getAmount())
                        + " WHERE ACC_NO=" + acc1.getAccNo();
                this.stmt.executeUpdate(sql);

                sql = "UPDATE ACCOUNTS "
                        + " SET BALANCE=" + (acc2.getBalance() + trans.getAmount())
                        + " WHERE ACC_NO=" + acc2.getAccNo();
                this.stmt.executeUpdate(sql);

                sql = "INSERT INTO TRANSACTIONS(TID,FR_ACC,TO_ACC,TR_DATE,NARRATION,TR_TYPE,AMOUNT,REF_CODE,RECIPIENT_NAME) "
                        + "VALUES(SEQ_TRANSACTIONS.nextval, " + acc1.getAccNo() + ", " + acc2.getAccNo() + ", SYSDATE, '"
                        + trans.getNarration() + "', " + trans.getTR_TYPE() + ", " + trans.getAmount() + ", " + trans.getRef_code() + ", '"
                        + trans.getRepicient_name() + "')";
                this.stmt.executeUpdate(sql);

                flag = true;
            } catch (SQLException ex) {
//                    Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("--> LogEvent:: Inserting Transaction");
            }
            break;
        }
        return flag;
    }

    public List<Log_Report> getAllLogReport() {
        String query = "SELECT * FROM LOG_REPORT";
        List<Log_Report> logList = new ArrayList<>();
        Log_Report record;
        try {
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                record = new Log_Report();
                record.setLog_in(this.rs.getDate(1).toString() + " " + this.rs.getTime(1).toString());
                record.setLog_out(this.rs.getDate(1).toString() + " " + this.rs.getTime(2).toString());
                record.setLog_Id(this.rs.getString(3));
                record.setUser_id(this.rs.getString(4));
                record.setReport(this.rs.getString(5));
                logList.add(record);
            }

        } catch (SQLException ex) {
            System.err.println(ex);
            System.err.println("--> LogEvent:: Fetching All Users Log Report");
        }
        return logList;
    }

    public List<Log_Report> getUserLogReport(String user_id) {
        List<Log_Report> recordList = this.getAllLogReport();
        List<Log_Report> filteredRecordList;
        filteredRecordList = new ArrayList<>();

        recordList.stream().filter(record -> (record.getUser_id().equalsIgnoreCase(user_id))).forEachOrdered(record -> {
            filteredRecordList.add(record);
        });
        return filteredRecordList;
    }

    public boolean recordLog(Date in, Date out, String user, String sysTrack) {
        // "INSERT INTO LOG_REPORT(LOG_IN, LOG_OUT, LOG_ID, USER_ID, REPORT) VALUES(TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'), TO_DATE('2003/05/03 21:02:46', 'yyyy/mm/dd hh24:mi:ss'), SEQ_LOG_REPORT.nextval, 'GHOST', 'TRACK:: LOGIN -> WITHDRAW -> LOGOUT ->')"
        String query;
        query = "INSERT INTO LOG_REPORT VALUES(?, ?, SEQ_LOG_REPORT.nextval, ?, ?)";

        try {
            PreparedStatement pstmt = this.con.prepareStatement(query);
            pstmt.setDate(1, in);
            pstmt.setDate(2, out);
            pstmt.setString(3, user);
            pstmt.setString(4, sysTrack);

            int isSuccess = pstmt.executeUpdate();
            if (isSuccess == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.err.println(ex);
            System.err.println("--> LogEvent:: Inserting LogReport");
        }
        return false;
    }

    public Person_Info getPersonInfo(int pid) {
        try {
            Person_Info data;
            String query = "SELECT * FROM PERSON_INFO WHERE PID = ?";
            PreparedStatement pstmt = this.con.prepareStatement(query);
            pstmt.setInt(1, pid);
            this.rs = pstmt.executeQuery();
            while (this.rs.next()) {
                data = new Person_Info();
                data.setPid(this.rs.getInt(1));
                data.setF_name(this.rs.getString(2));
                data.setL_name(this.rs.getString(3));
                data.setDOB(this.rs.getDate(4).toString());
                data.setPhone(this.rs.getString(5));
                data.setEmail(this.rs.getString(6));
                data.setAddress(this.rs.getString(7));
                data.setProfession(this.rs.getString(7));
                data.setOrgamization(this.rs.getString(7));
                return data;
            }
        } catch (SQLException ex) {
            System.err.println(ex);
            System.err.println("--> LogEvent:: Fetching PersonInfo");
        }
        return null;
    }

    public List<Person_Info> getAllPersonInfos() {
        List<Person_Info> list = new ArrayList<>();
        try {
            Person_Info data;
            String query = "SELECT * FROM PERSON_INFO";
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                data = new Person_Info();
                data.setPid(this.rs.getInt(1));
                data.setF_name(this.rs.getString(2));
                data.setL_name(this.rs.getString(3));
                data.setDOB(this.rs.getDate(4).toString());
                data.setPhone(this.rs.getString(5));
                data.setEmail(this.rs.getString(6));
                data.setAddress(this.rs.getString(7));
                data.setProfession(this.rs.getString(7));
                data.setOrgamization(this.rs.getString(7));
                list.add(data);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
            System.err.println("--> LogEvent:: Fetching ALL PersonInfo");
        }
        return list;
    }

    public List<Users> getAllUsers() {
        List<Users> list = new ArrayList<>();
        try {
            Users data;
            String query = "SELECT * FROM USERS";
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                data = new Users(this.rs.getString(1), this.rs.getString(2), this.rs.getTime(3).toString(),
                        this.rs.getInt(4), this.rs.getInt(5), (this.rs.getInt(6) == 1));
                list.add(data);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
            System.err.println("--> LogEvent:: Fetching ALL USERS");
        }
        return list;
    }

    public List<Account_Type> getAccountTypes() {
        List<Account_Type> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM ACCOUNT_TYPE";
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                Account_Type data = new Account_Type();
                data.setAcc_Type(this.rs.getString(1));
                data.setAT_Id(this.rs.getInt(2));
                list.add(data);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
            System.err.println("--> LogEvent:: Fetching AccountTypes");
        }
        return list;
    }

    public boolean deleteRecord(Object obj) {
        String query;
        PreparedStatement pstmt;
        int isSuccess;

        if (obj instanceof Log_Report) {
            try {
                Log_Report record = (Log_Report) obj;
                query = "DELETE FROM LOG_REPORT WHERE LOG_ID = ?";
                pstmt = this.con.prepareStatement(query);
                pstmt.setString(1, record.getLog_Id());
                isSuccess = pstmt.executeUpdate();
                if (isSuccess == 1) {
                    return true;
                }
            } catch (SQLException ex) {
                System.err.println(ex);
                System.err.println("--> LogEvent:: Deleting Row in LogReport");
            }
        } else if (obj instanceof Users) {
            try {
                Users record = (Users) obj;
                query = "DELETE FROM USERS WHERE USERNAME = ?";
                pstmt = this.con.prepareStatement(query);
                pstmt.setString(1, record.getUsername());
                isSuccess = pstmt.executeUpdate();
                if (isSuccess == 1) {
                    return true;
                }
            } catch (SQLException ex) {
                System.err.println(ex);
                System.err.println("--> LogEvent:: Deleting Row in User");
            }
        } else if (obj instanceof Accounts) {
            try {
                Accounts record = (Accounts) obj;
                //--Getting and Deleting User Data.
                Users userData = this.getUser(record.getAccNo().toString());
                this.deleteRecord(userData);

                query = "DELETE FROM ACCOUNTS WHERE ACC_NO = ?";
                pstmt = this.con.prepareStatement(query);
                pstmt.setInt(1, record.getAccNo());
                isSuccess = pstmt.executeUpdate();
                if (isSuccess == 1) {
                    //get PersonInfo and Delete it;
                    this.deleteRecord(this.getPersonInfo(record.getPID_Owner()));
                    return true;
                }
            } catch (SQLException ex) {
                System.err.println(ex);
                System.err.println("--> LogEvent:: Deleting Row in ACCOUNTS");
            }
        } else if (obj instanceof Transactions) {
            try {
                Transactions record = (Transactions) obj;
                //--Unfinished
                query = "DELETE FROM TRANSACTIONS WHERE TID = ?";
                pstmt = this.con.prepareStatement(query);
                pstmt.setInt(1, record.getTID());
                isSuccess = pstmt.executeUpdate();
                if (isSuccess == 1) {
                    return true;
                }
            } catch (SQLException ex) {
                System.err.println(ex);
                System.err.println("--> LogEvent:: Deleting Row in TRANSACTIONS");
            }
        } else if (obj instanceof Person_Info) {
            try {
                Person_Info record = (Person_Info) obj;
                //--Unfinished
                query = "DELETE FROM PERSON_INFO WHERE PID = ?";
                pstmt = this.con.prepareStatement(query);
                pstmt.setInt(1, record.getPid());
                isSuccess = pstmt.executeUpdate();
                if (isSuccess == 1) {
                    return true;
                }
            } catch (SQLException ex) {
                System.err.println(ex);
                System.err.println("--> LogEvent:: Deleting Row in PERSON_INFO");
            }
        }
        return false;
    }

    public boolean insertPersonInfo(Person_Info info) {
        try {
            String query = "INSERT INTO PERSON_INFO "
                    + "VALUES(SEQ_PERSON_INFO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            java.sql.Date date = Date.valueOf(info.getDOB());
            try (PreparedStatement pstmt = this.con.prepareStatement(query)) {
                pstmt.setString(1, info.getF_name());
                pstmt.setString(2, info.getL_name());
                pstmt.setDate(3, date);
                pstmt.setString(4, info.getPhone());
                pstmt.setString(5, info.getEmail());
                pstmt.setString(6, info.getAddress());
                pstmt.setString(7, info.getProfession());
                pstmt.setString(8, info.getOrgamization());
                pstmt.setString(9, info.getCountry());
                pstmt.setString(10, info.getState());
                int cp = pstmt.executeUpdate();
                if (cp >= 1) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
            System.err.println("--> LogEvent:: Inserting PERSON_INFO");
        }
        return false;
    }

    public boolean insertAccount(Accounts accountData) {
        String query;
        PreparedStatement pstmt;
        int ca = 0;

        try {
            query = "INSERT INTO ACCOUNTS "
                    + "VALUES( ?, ?, ?, ?, SYSDATE, ?, ?)";
            pstmt = this.con.prepareStatement(query);
            pstmt.setLong(1, accountData.getAccNo());
            pstmt.setLong(2, accountData.getCardNo());
            pstmt.setLong(3, accountData.getCardPin());
            pstmt.setDouble(4, accountData.getBalance());
            pstmt.setInt(5, accountData.getAccType());
            pstmt.setInt(6, accountData.getPID_Owner());
            ca = pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
            System.err.println("--> LogEvent:: Inserting ACCOUNT");
        }

        return ca >= 1;
    }

    public boolean insertUser(Users credentials) {
        String query;
        PreparedStatement pstmt;
        int cu = 0;

        try {
            query = "INSERT INTO "
                    + "USERS(USERNAME, PASSWORD, CREATION, USER_PID, USER_ACC_NO) "
                    + "VALUES( ?, ?, SYSDATE, ?, ?)";
            pstmt = this.con.prepareStatement(query);
            pstmt.setString(1, credentials.getUsername());
            pstmt.setString(2, credentials.getPassword());
            pstmt.setInt(3, credentials.getUser_id());
            pstmt.setInt(4, credentials.getUser_acc_no());
            cu = pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex);
            System.err.println("--> LogEvent:: Inserting USER");
        }

        return cu >= 1;
    }

    public int getSeqAccountNEXT() {
        int accSeq = 0;
        try {
            String query = "SELECT SEQ_ACCOUNTS.NEXTVAL FROM DUAL";
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                accSeq = this.rs.getInt(1);
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accSeq;
    }

    public int getSeqAccountCURR() {
        int accSeq = 0;
        try {
            String query = "SELECT SEQ_ACCOUNTS.CURRVAL FROM DUAL";
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                accSeq = this.rs.getInt(1);
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accSeq;
    }

    public int getSeqPersonCURR() {
        int pid = 0;
        try {
            String query = "SELECT SEQ_PERSON_INFO.CURRVAL FROM DUAL";
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                pid = this.rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pid;
    }

    public int getSeqPersonNEXT() {
        int pid = 0;
        try {
            String query = "SELECT SEQ_PERSON_INFO.NEXTVAL FROM DUAL";
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                pid = this.rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pid;
    }

    public int getAccountsCount() {
        int count = 0;
        try {
            String query = "SELECT COUNT(*) AS ACC_COUNT FROM ACCOUNTS";
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            while (this.rs.next()) {
                count = this.rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public boolean isUserActivated(Users credential) {
        try {
            String query = "SELECT ACTIVE FROM USERS WHERE USERNAME = ? AND USER_ACC_NO = ?";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setString(1, credential.getUsername());
            ps.setInt(2, credential.getUser_acc_no());
            this.rs = ps.executeQuery();
            while (this.rs.next()) {
                return this.rs.getInt(1) == 1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void activateOrDisableUser(Users credential) {
        try {
            String query = "UPDATE USERS "
                    + "SET ACTIVE = ? "
                    + "WHERE USERNAME = ?";
            PreparedStatement ps = this.con.prepareStatement(query);
            ps.setInt(1, (credential.isActive() ? 1 : 0));
            ps.setString(2, credential.getUsername());
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        DbConnection db = new DbConnection();
    }
}
