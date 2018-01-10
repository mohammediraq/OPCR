/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author AhmedShalaby
 */
public class coursesModuler {

    static db_mysqlops sql = new db_mysqlops();
    static ArrayList courses = new ArrayList();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, Exception {
        // TODO code application logic here
        moduleUniversityNSS();
        moduleUniversityName();
    
    }
    
    public static void moduleUniversityName() throws SQLException, InstantiationException, IllegalAccessException, Exception 
    {
            sql.openmySQLconnection();
        ResultSet r = sql.executeSQLquery_stringRS2("select id,course_title from datset.courses_postgrad", 0);
        while (r.next()) {
            String splittedWords[] = r.getString("course_title").split("\\)");
            String uniName = splittedWords[1].replace("University", "").replace("university", "").replace("of", "").replace(",", " ");
            courses.add(uniName.trim());
            
            System.out.print(r.getString("id") + ":" + uniName + "\n");
            sql.executeSQL("update datset.courses_postgrad set uni_name = '" + uniName.trim() + "' where id =" + r.getString("id") + "");
            
        }
        sql.closemySQLconnection();
        
    }
        public static void moduleUniversityNSS() throws SQLException, InstantiationException, IllegalAccessException, Exception 
    {
            sql.openmySQLconnection();
        ResultSet r = sql.executeSQLquery_stringRS2("SELECT uni_name,avg_teaching_score FROM DATSET.uni_nss_scoring;", 0);
        while (r.next()) {
            
            Double uniNSS =r.getDouble("avg_teaching_score");
            String uniName = r.getString("uni_name");
            
            
            System.out.print(uniName + ":" + uniNSS + "\n");
            sql.executeSQL("update datset.courses_postgrad set uni_nss = " + uniNSS + " where uni_name ='" + uniName + "'");
            
        }
        sql.closemySQLconnection();
        
    }
    
}
