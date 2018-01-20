/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AhmedShalaby this class for testing the similarity-calculation
 * Algorithms.
 */
public class testingClass {

    static coursesModuler cm = new coursesModuler();
    static contentBasedEngine cb = new contentBasedEngine();
    static db_mysqlops sql = new db_mysqlops();
    static var_env e = new var_env();
    static boolean printComments;
    static API_courseSearch cs = new API_courseSearch();

    static HashMap<String, Double> finalScoreMap = new HashMap<String, Double>();
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, Exception {
        // TODO code application logic here

        try {
            
            
//            cm.moduleUniversityName();
//            cm.moduleUniversityNSS();
//            cb.get_allWeights();

            calculateCourseSimilarities("Computer Science", "Computer Programming", 1000, 4000, "South East England", 1.1, false);
//            double scoring = scoring(173.2, 12.3, 45, 80, 20, 10,true);

        } catch (Exception ex) {
            System.out.print(ex.getCause());
        }

// 
    }

    public static void calculateCourseSimilarities(String searchKey, String userMajor, double minFees, double maxFees, String userLocation, double minNSS, boolean withComments) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException, Exception {
        double totalCourseScore;

        if (withComments == true) {
            cb.printComments = true;
        }

        sql.openmySQLconnection();
        ResultSet r = sql.executeSQLquery_stringRS2("select * from datset.courses_postgrad where uni_nss is not null and course_fees_uk !=''  ", 0);

        while (r.next()) {

            double ct = cb.cal_courseTitleSimilarity(searchKey, r.getString("course_title"));
            double cm = cb.cal_courseMajorSimilarity(userMajor, r.getString("course_field"), r.getString("course_qualification"));
            double cf = cb.cal_courseFees(minFees, maxFees, r.getString("course_fees_uk").replace("£", "").replace(",", ""));
            double cl = cb.cal_locationSimilarity(r.getString("course_location"), userLocation);
            double cn = cb.cal_universityNSS(minNSS, r.getDouble("uni_nss"));
            totalCourseScore = (ct + cm + cf + cl + cn);

            System.out.print(r.getString("course_title") + " total Score is:" + totalCourseScore);
            System.out.print("\n ======= \n");
            finalScoreMap.put(r.getString("id"), totalCourseScore);
            

        }
        sql.closemySQLconnection();
        for (Map.Entry m : finalScoreMap.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());
            
        }

    }

    public static void printComment(String title, double score) {
        if (printComments == true) {
            System.out.print(title + ": " + score + "\n");
        }
    }

}
