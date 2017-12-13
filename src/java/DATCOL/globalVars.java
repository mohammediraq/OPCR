/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DATCOL;

import java.sql.Connection;
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
public class globalVars {

    //Date time vars
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date CurrentDate = new Date();
    // Database vars
    public static Connection con;
    public static Statement stmt;
    // Database connection String
    public static String dbname = "DATSET";
    public static String dbpass = "root";
    public static String dbuser = "root";
    public static String dbtable = "pulldata_workers1";
    public String logTable = "Log_Performance";
  
}
