/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import static BUSLOGIC.contentBasedEngine.foundSimilarity;
import static BUSLOGIC.contentBasedEngine.mysql;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AhmedShalaby
 */
public class coreEngineSubProccess {

    static db_mysqlops mysql = new db_mysqlops();
    static FN_toJSON json = new FN_toJSON();
    static var_env env = new var_env();
    static ResultSet rs;
    static ResultSet coursesObject;

//   Content based vars
    static double contentBasedScore;
    static double sim_courseTitle;
    static double sim_courseMajor;
    static double sim_courseLocation;
    static double sim_courseFees;
//    
    
//

    static double core_contentBasedWeight;
    static double core_collaborativeBasedWeight;
    static double core_universityRankWeight;
    static double core_universityNSSWeight;
    static double core_KNNscaleStart;
    static double core_KNNscaleEnd;
    static double core_courseFeesRangeStart;
    static double core_courseFeesRangeEnd;
//    

//  
//    
    //   Content-based item weights
    static double contentBased_courseTitleWeight;
    static double contentBased_courseMajorWeight;
    static double contentBased_courseFeeWeight;
    static double contentBased_courseLocationWeight;
//
//    Strings 
    static String string_concatedUsers;
    static String string_concatedIds;
//    
// HashMaps
    static HashMap<Integer, Double> map_coursesAverageRatings = new HashMap<Integer, Double>();
    static HashMap<Integer, Integer> map_KNNcoursesIdandScore = new HashMap<Integer, Integer>();
    static HashMap<Integer,Integer> map_KNNCoursesId = new HashMap<Integer,Integer>();

//    
//    ArrayLists
    static ArrayList list_similarProfiles = new ArrayList();
    static ArrayList list_coursesIds = new ArrayList();
    
    static ArrayList list_KNNcoursesScore = new ArrayList();
    static ArrayList list_KNNcoursesUsers = new ArrayList();
//    
//    String arrays

//    
    //    
    //    reading core weights from the database
    public static double readCoreWeights(String itemName, String destinationTable) {
        double itemWeight = 0.0;

        try {
            mysql.openmySQLconnection();
            rs = mysql.executeSQLquery_stringRS2("select * from datset." + destinationTable.trim() + " where item_name='" + itemName.trim() + "'", 0);

            while (rs.next()) {
                itemWeight = rs.getDouble("item_weight");
            }
            mysql.closemySQLconnection();

        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        }

        return itemWeight / 100;
    }

    public static double cosineSimilarityCalculation(double s, double w) {
        double cosineSimilarity = 0.0;
//        s = similarity calculated from the function.
//        w = the retrieved item weight from database.

        cosineSimilarity = s * w;

        return cosineSimilarity;
    }

    public static double contentBasedScoring() {
//        tip: will be calculated to be 50% of the total score.

        return contentBasedScore;
    }

    public void addScore_contentBased(double s) {
        contentBasedScore += s;
    }

    public double cal_courseTitleSimilarity(String searchKey, String courseTitle) {

        sim_courseTitle = 0.0;
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
            sim_courseTitle = (trueMatches / wc.length);
        } else {
            trueMatches = 0.0;
        }
//        returning the similarity in a double formatted value
        addScore_contentBased(sim_courseTitle * contentBased_courseTitleWeight);
        return sim_courseTitle;
    }

    public double cal_courseMajorSimilarity(String userMajor, String courseField, String courseQual) {
//        this function will point to 2 database columns 
//        1.Course_field: as it has the main course major which been entered by the admin
//        and will calculate the matching percentage with conf_classes;
//        2.Course_qualifications: which has the courses current qualicficaitons been entered
//        by the UCAS website and logged by the Crawler
//        *Seperate functions for future purposes
//         the returned value will be course_field% similarity + course_qualifications% similarity

        sim_courseMajor = 0.0;
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
        sim_courseMajor = (MatchesCount / totalMatchesCount);
        sim_courseMajor += identicalSimilarity;
//     containing the negative value / over 100% case
        if (Math.abs(sim_courseMajor) > 100.0) {
            sim_courseMajor = 100;
        }

        addScore_contentBased(sim_courseMajor * contentBased_courseMajorWeight);
        return Math.abs(sim_courseMajor);
    }

    public double cal_locationSimilarity(String courseLocation, String userLocation) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, Exception {
        sim_courseLocation = 0.0;
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
                    sim_courseLocation = 0.1 * contentBased_courseLocationWeight;
                } else {
                    sim_courseLocation = 0.0;
                }
            }
        }
        mysql.closemySQLconnection();
        addScore_contentBased(sim_courseLocation);
        return sim_courseLocation;
    }

    public double cal_courseFees(double minFees, double maxFees, String courseFees) {
        double feesSimilarity = 0.0;
        double courseFeesValue = Double.parseDouble(courseFees);
//      **check   amount validitaion -null -undefined 
        if (courseFeesValue >= core_courseFeesRangeStart && courseFeesValue <= core_courseFeesRangeEnd) {
//      1.set the scale ( THE SAME KNN SCALE ) 
//      ex. scaled the 25000 (max range ) to 5 scales ( 6250 per each ) ;

            feesSimilarity = (maxFees-Integer.parseInt(courseFees))/((maxFees-(core_courseFeesRangeStart-1)));
            sim_courseFees = feesSimilarity * contentBased_courseFeeWeight;
//        
        }
        else {
//            in this case the expected fees will always less than the actual fees
//            which will be discarded by the user.
            sim_courseFees = 0.0;
        }

        addScore_contentBased(sim_courseFees);
        return sim_courseFees;
    }

//    read all courses and returns a ResultSet Object.
    public ResultSet readCurrentCourses(String subclass) {

        try {
            mysql.openmySQLconnection();
          coursesObject = mysql.executeSQLquery_stringRS2(env.dq_CB_getCoursesBySubClass(subclass), 0);
            coursesObject.next();
//            mysql.closemySQLconnection();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        }

        return coursesObject;

    }

//       COB Algorithm
//Functions.
//1. get top 5 similar users.
//2. get rounded average for each course.
//3. scale generator.
//4. percentage calculator
//5.append to hashmap.
//6.users and course list generators.
    public ArrayList getSimilarProfiles(String userid, String searchKey) {
        try {
//        this function takes the current user id to get the
//        similar profiles based on (region,user major,user class)
//        from the courses search history.

            mysql.openmySQLconnection();
            ResultSet rs = mysql.executeSQLquery_stringRS2(env.dq_COB_getTopSimilarProfiles(searchKey, userid, 5), 0);
            while (rs.next()) {
                list_similarProfiles.add(rs.getString("usr_id"));
            }

            mysql.closemySQLconnection();

//            then calling the get average score according to users
//
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(coreEngineSubProccess.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list_similarProfiles;
    }

    public HashMap getAverageRatingsForCourse() {

        for (Map.Entry m : map_KNNcoursesIdandScore.entrySet()) {
            int courseScore= Integer.parseInt(m.getValue().toString());
            int courseid= Integer.parseInt(m.getKey().toString());
            double scale;
           scale = getscaledPercentage(courseScore);
            map_coursesAverageRatings.put(courseid, scale);
        }

        return map_coursesAverageRatings;
    }

    public String idGenerator() {
        ArrayList ar = list_coursesIds;
//        called by CB engine in a while loop.
        string_concatedIds = "'" + ar.get(0).toString() + "',";
        for (int i = 1; i < ar.size(); i++) {
            if (i == ar.size() - 1) {
                string_concatedIds += "'" + ar.get(i).toString() + "'";
            } else {
                string_concatedIds += "'" + ar.get(i).toString() + "',";
            }
        }

        return string_concatedIds;
    }

    public String usersGenerator() {
        ArrayList ar = list_similarProfiles;

        string_concatedUsers = "'" + ar.get(0).toString() + "',";
        for (int i = 1; i < ar.size(); i++) {
            if (i == ar.size() - 1) {
                string_concatedUsers += "'" + ar.get(i).toString() + "'";
            } else {
                string_concatedUsers += "'" + ar.get(i).toString() + "',";
            }
        }
        return string_concatedUsers;
    }

    public double getscaledPercentage( int rate) {
        double valueOfscaledPercentage = 0.0;
        double valueOfScale = 0.0;
        valueOfScale = core_collaborativeBasedWeight / (core_KNNscaleEnd - core_KNNscaleStart);
        valueOfscaledPercentage = valueOfScale * (rate - 1);

        return valueOfscaledPercentage;
    }

    public double getFeesScale(double maxRange, double minRange, double courseFees, int rangeEnd) {
        int scale = 0;
        double score = 0.0;
        double f = courseFees;
        double r = 0.0;
        double v = 0.0;
//    get the scale-level variance.
        v = maxRange / (rangeEnd - 1);
//      locate the course fees position.

        for (int i = 0; i < 4; i++) {
            if (r >= f) {
                scale = i + 1;
                break;
            }
            r += v;

        }
        score = (((v * scale) - f) * (rangeEnd - 1)) / (maxRange - minRange);

        return score;
    }

   
    
    
}
