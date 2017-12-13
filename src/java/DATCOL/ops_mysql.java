/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

1. String cols without ' ;
2. String vals.
3. Call connectMYSQL;




 */
package DATCOL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author AhmedShalaby
 */
public class ops_mysql {

    static globalVars gv = new globalVars();
    static ops_mysql mysql = new ops_mysql();

    public String q;
    public static String _insertCol;
    public static String _insertVal;
    

    

    public void connectMYSQL() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        gv.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + gv.dbname + "?useUnicode=yes&characterEncoding=UTF-8&"
                + "user=" + gv.dbuser + "&password=" + gv.dbpass + "&useSSL=false");
        gv.con.getSchema();

        q = "insert into `" + gv.dbname + "`.`" + gv.dbtable + "`"
                + " (" + _insertCol + ") "
                + "values "
                + "(" + _insertVal + ");";
        try {
            Statement st = gv.con.createStatement();
            st.execute(q);
            gv.con.close();
            System.out.print(_insertVal+"\n");
        } catch (SQLException ex) {
            System.out.print(ex.getCause());
            gv.con.close();
        }

    }

    public void log(String logLevel, String className, Long ElapsedTime, String desc) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        _insertCol = "Log_level," + "Class_Name," + "Elapsed_Time," + "Action_Description";

        _insertVal = "'" + logLevel.trim() + "'," + "'" + className.trim() + "'," + ElapsedTime + "," + "'" + desc + "'";
       
        
        gv.dbtable = gv.logTable;
        mysql.connectMYSQL();

    }

  
}
