/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AhmedShalaby
 */
public class db_mysqlops {

    FN_toJSON json = new FN_toJSON();
    var_env env = new var_env();
    public static Connection con;
    public static Statement stmt;
    public static ResultSet rs;
    public String jsonObject;

    public void openmySQLconnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection("jdbc:mysql://" + env.dbhost + ":" + env.dbport + "/" + env.dbname + "?"
                + "user=" + env.dbuser + "&password=" + env.dbpassword + "&useSSL=false");
        con.getSchema();
    }

    public void closemySQLconnection() throws SQLException {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(db_mysqlops.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void executeSQLquery(String q) throws SQLException, Exception {

        try {

            Statement st = con.createStatement();
            st.executeQuery(q);

        } catch (Exception ex) {
            System.out.print(ex);

        }

    }

    public void executeSQL(String q) throws SQLException, Exception {

        try {

            Statement st = con.createStatement();
            st.execute(q);

        } catch (Exception ex) {
            System.out.print(ex);
        }

    }

    public String executeSQLquery_stringRS(String q, int col) throws SQLException, Exception {

        try {

            Statement st = con.createStatement();
            rs = st.executeQuery(q);
            rs.next();

        } catch (Exception ex) {

        }

        return rs.getString(col);
    }

    public ResultSet executeSQLquery_WithRS(String q) throws SQLException, Exception {
        ResultSet rset = null;
        try {

            Statement st = con.createStatement();
            rset = st.executeQuery(q);
            rset.next();

        } catch (Exception ex) {

        }

        return rset;
    }

    public ResultSet executeSQLquery_stringRS2(String q, int col) throws SQLException, Exception {

        try {
            

            Statement st = con.createStatement();
            rs = st.executeQuery(q);
      
        } catch (Exception ex) {

        }

        return rs;
    }

}
