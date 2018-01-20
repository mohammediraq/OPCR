/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.sql.SQLException;
import java.util.Random;

/**
 *
 * @author AhmedShalaby
 */
public class testingColBased {

    static db_mysqlops mysql = new db_mysqlops();
    static FN_toJSON json = new FN_toJSON();
    static var_env env = new var_env();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException, Exception {
        // TODO code application logic here
   
    }

    public static void appendRandomScores(String courseId) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException, Exception {
        mysql.openmySQLconnection();
        mysql.executeSQL("insert into DATSET.course_search_score (course_id,course_score) values ("+courseId+","+generateRandomScore()+")");
        mysql.closemySQLconnection();

    }

    public static int generateRandomScore() {
        int max = 5;
        int min = 1;
        int diff = max - min;
        Random rn = new Random();
        int i = rn.nextInt(diff + 1);
        i += min;

        System.out.print("The Random Number is " + i);
        return i;
    }

}
