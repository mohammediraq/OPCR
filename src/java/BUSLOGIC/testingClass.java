/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, Exception {
        // TODO code application logic here

        try {
            cm.moduleUniversityName();
            cm.moduleUniversityNSS();
            calculateCourseSimilarities("English", "Computer Science", "Computer Programming", 1000, 4000, "Jersey", 1.1, false);
//            double scoring = scoring(173.2, 12.3, 45, 80, 20, 10,true);

        } catch (Exception ex) {
            System.out.print(ex.getCause());
        }

// 
    }

    public static void calculateCourseSimilarities(String courseLanguage, String searchKey, String userMajor, double minFees, double maxFees, String userLocation, double minNSS, boolean withComments) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException, Exception {
        double totalCourseScore;

        if (withComments == true) {
            cb.printComments = true;
        }

        sql.openmySQLconnection();
        ResultSet r = sql.executeSQLquery_stringRS2("select * from datset.courses_postgrad where uni_nss is not null and course_fees_uk !=''  ", 0);

        while (r.next()) {

            double ct = cb.cal_courseTitleSimilarity(searchKey, r.getString("course_title"));
            double cm = cb.cal_courseMajorSimilarity(userMajor, r.getString("course_field"), r.getString("course_qualification"));
            double cl = cb.cal_languageSimilarity(r.getString("course_language"), courseLanguage);
            double cf = cb.cal_courseFees(minFees, maxFees, r.getString("course_fees_uk").replace("£", "").replace(",", ""));
            double cs = cb.cal_locationSimilarity(r.getString("course_location"), userLocation);
            double cn = cb.cal_universityNSS(minNSS, r.getDouble("uni_nss"));
            totalCourseScore = (ct + cm + cl + cf + cs + cn);

            System.out.print(r.getString("course_title") + " total Score is:" + totalCourseScore);
            System.out.print("\n ======= \n");

        }
        sql.openmySQLconnection();

    }

    public static double scoring(double ct, double cm, double cl, double cf, double cs, double cn, boolean withComments) throws SQLException, InstantiationException, IllegalAccessException, Exception {
        printComments = withComments;
        double finalScore = 0.0;
        String courseTitle, courseMajor, courseLocation, courseFees, courseNSS, courseLanguage;

        sql.openmySQLconnection();
        ResultSet r = sql.executeSQLquery_stringRS2("SELECT * FROM DATSET.course_item_weight", 0);
        while (r.next()) {

            if (r.getString("item_name").contains("Title")) {
                courseTitle = r.getString("item_name");

                printComment(courseTitle, finalScore);
            } else if (r.getString("item_name").contains("major")) {
                courseMajor = r.getString("item_name");
                finalScore = (cm / r.getDouble("item_weight")) * 100;
                printComment(courseMajor, finalScore);

            } else if (r.getString("item_name").contains("location")) {
                courseLocation = r.getString("item_name");
                finalScore = (cl / r.getDouble("item_weight")) * 100;
                printComment(courseLocation, finalScore);

            } else if (r.getString("item_name").contains("fees")) {
                courseFees = r.getString("item_name");
                finalScore = (cf / r.getDouble("item_weight")) * 100;
                printComment(courseFees, finalScore);

            } else if (r.getString("item_name").contains("nss")) {
                courseNSS = r.getString("item_name");
                finalScore = (cs / r.getDouble("item_weight")) * 100;
                printComment(courseNSS, finalScore);

            } else if (r.getString("item_name").contains("language")) {
                courseLanguage = r.getString("item_name");
                finalScore = (cn / r.getDouble("item_weight")) * 100;
                printComment(courseLanguage, finalScore);

            }

        }

        return finalScore;
    }

    public static void printComment(String title, double score) {
        if (printComments == true) {
            System.out.print(title + ": " + score + "\n");
        }
    }

}
