/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import static BUSLOGIC.testingClass.cb;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author AhmedShalaby
 */
public class contentBasedEngine {

    static db_mysqlops mysql = new db_mysqlops();
    static FN_toJSON json = new FN_toJSON();
    static var_env env = new var_env();
    static testingColBased testingCOB = new testingColBased();

    ArrayList coursesWithSimilarity = new ArrayList();
//    Weights
    static double iwCourseTitle;
    static double iwCourseMajor;
    static double iwCourseLocation;
    static double iwCourseNSS;
    static double iwCourseFees;
//   Given Similarities 
    double similarityCourseTitle;
    double similarityCourseMajor;
    double similarityCourseLocation;
    double similarityNSS;
    double similarityUNIRanking;
    double similarityCourseFees;
    double similarityCourseLanguage;
//    

    static boolean foundSimilarity = false;
    static boolean printComments = false;
//    
    static HashMap<String, Double> ContenetBasedScoreMap = new HashMap<String, Double>();
    static HashMap<Integer, Double> collaborativeBasedScoreMap = new HashMap<Integer, Double>();

    public static void calculateCourseSimilarities(String subclass, String searchKey, String userMajor, double minFees, double maxFees, String userLocation, double minNSS, boolean withComments) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException, Exception {
        double totalCourseScore;
        get_allWeights();

        if (withComments == true) {
            cb.printComments = true;
        }

        mysql.openmySQLconnection();
        ResultSet r = mysql.executeSQLquery_stringRS2(env.dq_CB_getCoursesBySubClass(subclass), 0);

        while (r.next()) {

            double ct = cb.cal_courseTitleSimilarity(searchKey, r.getString("course_title"));
            double cm = cb.cal_courseMajorSimilarity(userMajor, r.getString("course_field"), r.getString("course_qualification"));
            double cf = cb.cal_courseFees(minFees, maxFees, r.getString("course_fees_uk").replace("£", "").replace(",", ""));
            double cl = cb.cal_locationSimilarity(r.getString("course_location"), userLocation);
            double cn = cb.cal_universityNSS(minNSS, r.getDouble("uni_nss"));
            totalCourseScore = (ct + cm + cf + cl + cn);

            System.out.print(r.getString("course_title") + " total Score is:" + totalCourseScore);
            System.out.print("\n ======= \n");
            ContenetBasedScoreMap.put(r.getString("id"), totalCourseScore);
//==>
//            testingCOB.appendRandomScores(r.getString("id").toString());
        }
        mysql.closemySQLconnection();
        for (Map.Entry m : ContenetBasedScoreMap.entrySet()) {
            try {
                COB_getAverageCourseScore(m.getKey().toString());
            } catch (Exception ex) {
                System.out.print("Error Exception: " + ex.getCause());
            }
        }

        printResults();

    }

//    Print two algroithms results 
    public static void printResults() {
        System.out.print("Content-Based \n");
        for (Map.Entry m : ContenetBasedScoreMap.entrySet()) {

            System.out.println(m.getKey() + " = " + m.getValue());

        }
        System.out.print("collaborative-Based \n");
        for (Map.Entry n : collaborativeBasedScoreMap.entrySet()) {
            System.out.println(n.getKey() + " = " + n.getValue());

        }

    }

    public static void printComment(String title, double score) {
        if (printComments == true) {
            System.out.print(title + ": " + score + "\n");
        }
    }

    public static void get_allWeights() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException, Exception {
        mysql.openmySQLconnection();
        ResultSet rs = mysql.executeSQLquery_stringRS2("SELECT * FROM DATSET.course_item_weight ", 0);
        while (rs.next()) {

            String itemName;
            double itemWeight;
            itemName = rs.getString("item_name");
            itemWeight = Double.parseDouble(rs.getString("item_weight"));

            if (itemName.toLowerCase().contains("title")) {

                iwCourseTitle = itemWeight;
            } else if (itemName.toLowerCase().contains("major")) {

                iwCourseMajor = itemWeight;
            } else if (itemName.toLowerCase().contains("fees")) {

                iwCourseFees = itemWeight;
            } else if (itemName.toLowerCase().contains("location")) {

                iwCourseLocation = itemWeight;
            } else if (itemName.toLowerCase().contains("nss")) {

                iwCourseNSS = itemWeight;
            }

        }
        mysql.closemySQLconnection();

    }

//    Calcuate Course title similarity 
    public double cal_courseTitleSimilarity(String searchKey, String courseTitle) {
        String wc[] = courseTitle.split(" ");
        String searchKeyWords[] = searchKey.split(" ");

        double trueMatches = 0.0;

        if (searchKey.length() > 1) {

            for (int i = 0; i < searchKeyWords.length; i++) {
                if (courseTitle.replace("(", "").replace(")", "").contains(searchKeyWords[i])) {
                    foundSimilarity = true;
                    break;
                }
            }
        }

//      check the similarity between the give keyword (search key) and the current course title.
        if (foundSimilarity == true) {
//            get the course title's count of words
            wc = courseTitle.split(" ");
//            check similarity * case sensitive 
            for (int skw = 0; skw < searchKeyWords.length; skw++) {

                for (int i = 0; i < wc.length; i++) {
                    if ((searchKeyWords[skw].trim()).equalsIgnoreCase(wc[i].trim())) {
                        trueMatches += 1;
                    }

                }
            }
//            end of check matches loop
//        output the final percentage 
//         trueMatches / total course title's words * 100 
            similarityCourseTitle = (trueMatches / wc.length);
        } else {
            trueMatches = 0.0;
        }
//        returning the similarity in a double formatted value
        printResults(similarityCourseTitle, "Course Title Similarity");
        return similarityCourseTitle;
    }

//    Calculate course major similarity
    public double cal_courseMajorSimilarity(String userMajor, String courseField, String courseQual) {
//        this function will point to 2 database columns 
//        1.Course_field: as it has the main course major which been entered by the admin
//        and will calculate the matching percentage with conf_classes;
//        2.Course_qualifications: which has the courses current qualicficaitons been entered
//        by the UCAS website and logged by the Crawler
//        *Seperate functions for future purposes
//         the returned value will be course_field% similarity + course_qualifications% similarity
        String userMajorWords[] = userMajor.split(" ");
        String courseFieldWords[] = courseField.split(" ");
        String courseQualificaitonsWords[] = courseQual.split(" ");
        double MatchesCount = 0;
        double totalMatchesCount = 0;
        double identicalSimilarity = 0;
        double total_firstScenarioWords = courseFieldWords.length;
        double total_secondScenarioWords = courseQualificaitonsWords.length;

//        1.Similarity for course field.
//          userMajorWords and courseFieldWords;
        if (courseField.trim().equalsIgnoreCase(userMajor.trim())) {
            identicalSimilarity += 50.0;
        } else {

            for (String userMajorWord : userMajorWords) {
                for (String courseFieldWord : courseFieldWords) {
                    if (userMajorWord.trim().equalsIgnoreCase(courseFieldWord.trim())) {
//                        System.out.print("Comparing " + courseFieldWord.trim() + " with " + userMajorWord.trim() + "\n");
                        MatchesCount += 1;
                    }

                }
            }

        }

//        2.Similarity for course qualifications.
//          userMajorWords and courseQualificaitonsWords;
        if (courseQual.contains("-")) {
            courseQualificaitonsWords = courseQual.split("-");
        } else {
            courseQualificaitonsWords = courseQual.split(" ");
        }

        if (courseQual.trim().equalsIgnoreCase(userMajor.trim())) {
            identicalSimilarity += 50.0;
        } else {

            for (String userMajorWord : userMajorWords) {
                String courseQualitifationsWordp1[] = courseQualificaitonsWords[0].split(" ");
                for (String courseQualWord : courseQualitifationsWordp1) {

                    if (userMajorWord.trim().equalsIgnoreCase(courseQualWord.trim())) {
//                        System.out.print("Comparing " + courseQualWord.trim() + " with " + userMajorWord.trim() + "\n");
                        MatchesCount += 1;
                    }

                }
            }

        }
//        finializing/producing the course major percentage ...

        totalMatchesCount = total_firstScenarioWords + total_secondScenarioWords;
//        System.out.print("Matches Count= " + MatchesCount + "\n" + "Total Words= " + totalMatchesCount + "\n");
        similarityCourseMajor = (MatchesCount / totalMatchesCount);
        similarityCourseMajor += identicalSimilarity;
//     containing the negative value / over 100% case
        if (Math.abs(similarityCourseMajor) > 100.0) {
            similarityCourseMajor = 100;
        }

        printResults(similarityCourseMajor, "Major Similarities");
        return Math.abs(similarityCourseMajor);
    }

    public double cal_locationSimilarity(String courseLocation, String userLocation) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, Exception {
        similarityCourseLocation = 0.0;
        if (userLocation.isEmpty() == false && courseLocation.isEmpty() == false) {

//            course location is the uni location matching city => uk countries
//            ==> region compare to user's region;
//             true == item weight , else = 0 ;
            String c = courseLocation.replace("University of", " ").replace("Campus", " ").toLowerCase();
//              get uni region 
            mysql.openmySQLconnection();
            ResultSet rs = mysql.executeSQLquery_stringRS2("select * from uk_countries where city like ('%" + c + "%')", 0);
            while (rs.next()) {
                String uniRegion = rs.getString("Region");

                if (userLocation.equals(uniRegion)) {
                    similarityCourseLocation = iwCourseLocation / 100;
                } else {
                    similarityCourseLocation = 0.0;
                }
            }
        }
        mysql.closemySQLconnection();
        printResults(similarityCourseLocation, "Location");
        return similarityCourseLocation;
    }

//    public double cal_languageSimilarity(String courseLanguage, String userLanguage) {
//
////          calculates the user's language matching 
////  true = full score.
////  false = score is 0.
//        if (userLanguage.isEmpty() == false && courseLanguage.isEmpty() == false) {
//            if (courseLanguage.equalsIgnoreCase(userLanguage)) {
//                similarityCourseLanguage = 100.0;
//            } else {
//                similarityCourseLanguage = 0.0;
//            }
//
//        }
//
//        printResults(similarityCourseLanguage, "Language");
//        return similarityCourseLanguage;
//    }
    public double cal_courseFees(double minFees, double maxFees, String courseFees) {
        double feesSimilarity = 0.0;
        double courseFeesValue = Double.parseDouble(courseFees);
//      **check   amount validitaion -null -undefined 
        if (courseFeesValue >= minFees && courseFeesValue <= maxFees) {
//            fees within the expected range 
//            relating the minimum fees with the actual course fees will
//            get the percentage of the user's acceptence for this fees
//            the higher percentage is the more acceptence .
            feesSimilarity = (minFees / courseFeesValue);
            similarityCourseFees = feesSimilarity;

        }
        if (courseFeesValue > maxFees) {
//            in this case the expected fees will always less than the actual fees
//            which will be discarded by the user.
            similarityCourseFees = 0.0;
        }

        printResults(similarityCourseFees, "course Fees");
        return similarityCourseFees;
    }

    public double cal_universityNSS(double userInputMinScore, double universityNSS) {
//        ex.userInputMinScore = 90 ==> will get all Ranks >= 90%
//        calculates the similarity 

        if (userInputMinScore <= universityNSS) {
            similarityNSS = (userInputMinScore / universityNSS);
        } else {
            similarityNSS = 0.0;
        }
        printResults(similarityNSS, "University NSS");
        return similarityNSS;
    }

    public static void COB_getAverageCourseScore(String CourseId) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException, Exception {
        mysql.openmySQLconnection();
        ResultSet rs = mysql.executeSQLquery_stringRS2(env.dq_COB_getAverageCourseScore(CourseId), 0);
        while (rs.next()) {
            collaborativeBasedScoreMap.put(Integer.parseInt(CourseId), Double.parseDouble(rs.getString("avg")));
        }

        mysql.closemySQLconnection();

    }

    public void printResults(double s, String similarityName) {
        if (printComments == true) {
            System.out.print(similarityName + " Similarity is: " + s + "\n");
        }
    }

}
