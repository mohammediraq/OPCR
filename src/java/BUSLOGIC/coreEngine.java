/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import static BUSLOGIC.coreEngineSubProccess.map_coursesAverageRatings;
import static BUSLOGIC.coreEngineSubProccess.mysql;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AhmedShalaby
 */
public class coreEngine {
//  Classes vars

    static db_mysqlops mysql = new db_mysqlops();
    static FN_toJSON json = new FN_toJSON();
    static var_env env = new var_env();
    static coreEngineSubProccess subprocess = new coreEngineSubProccess();
//    
    boolean KNN_similarityFound = false;
//    
//    HashMaps 
    static HashMap<Integer, Double> map_contentBasedFinalScore = new HashMap<Integer, Double>();
    static HashMap<Integer, Double> map_COBFinalScore;
//

    //   University ranking vars
    static double UniversityRankSimilarity;

//
        //   University NSS vars
    static double UniversityNSS_Similarity;

//
//    main core algorithm weights
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
//    User's profile vars.
    static String userMajor;
    static String userSubclass;
    static String userSearchkey;
    static String userRegion;
    static double minimumNSS;
    static double maxFees;
    static double minFees;
//    

    public void setCoreItemWeight() {

        core_contentBasedWeight = subprocess.readCoreWeights("Content Based", "algorithm_weights");
        core_collaborativeBasedWeight = subprocess.readCoreWeights("Collaborative Based", "algorithm_weights");
        core_universityRankWeight = subprocess.readCoreWeights("University Rank", "algorithm_weights");
        core_universityNSSWeight = subprocess.readCoreWeights("University NSS", "algorithm_weights");
        core_KNNscaleStart = subprocess.readCoreWeights("KNN Scale Start", "algorithm_weights") * 100;
        core_KNNscaleEnd = subprocess.readCoreWeights("KNN Scale End", "algorithm_weights") * 100;
        core_courseFeesRangeStart = subprocess.readCoreWeights("Course Fees Scale Start", "algorithm_weights") * 100;
        core_courseFeesRangeEnd = subprocess.readCoreWeights("Course Fees Scale End", "algorithm_weights") * 100;

        subprocess.core_contentBasedWeight = core_contentBasedWeight;
        subprocess.core_collaborativeBasedWeight = core_collaborativeBasedWeight;
        subprocess.core_universityRankWeight = core_universityRankWeight;
        subprocess.core_universityNSSWeight = core_universityNSSWeight;
        subprocess.core_KNNscaleStart = core_KNNscaleStart;
        subprocess.core_KNNscaleEnd = core_KNNscaleEnd;
        subprocess.core_courseFeesRangeStart = core_courseFeesRangeStart;
        subprocess.core_courseFeesRangeEnd = core_courseFeesRangeEnd;
    }

    public void setContentBasedWeight() {

        subprocess.contentBased_courseTitleWeight = subprocess.readCoreWeights("Course Title", "course_item_weight");
        subprocess.contentBased_courseMajorWeight = subprocess.readCoreWeights("Course Major", "course_item_weight");
        subprocess.contentBased_courseLocationWeight = subprocess.readCoreWeights("Course Location", "course_item_weight");
        subprocess.contentBased_courseFeeWeight = subprocess.readCoreWeights("Course Fees", "course_item_weight");

    }

    public void setSearchProperties(String um, String usc, String usk, String ur, double mnss, double mnf, double mxf) {
        userMajor = um;
        userSubclass = usc;
        userSearchkey = usk;
        userRegion = ur;
        minimumNSS = mnss;
        minFees = mnf;
        maxFees = mxf;
    }

//    THE CORE FUNCTION.
    public HashMap CB_calculateCourseSimilarities() throws SQLException {
//        preloaded functions.
        setContentBasedWeight();
        setCoreItemWeight();
//              

//        1. Get courses by a subclass.
        subprocess.readCurrentCourses(userSubclass);
//        2. Calculate foreach course similarity and fill the hashmap with 
//           the course id and course score.
//        2.1: while loop with the courses object

        while (subprocess.coursesObject.next()) {
            try {
                //          !important: reset score counter.

//        2.2: calculate course features similarity.
                subprocess.cal_courseTitleSimilarity(userSearchkey, subprocess.coursesObject.getString("course_title"));
                subprocess.cal_courseMajorSimilarity(userMajor, subprocess.coursesObject.getString("course_field"), subprocess.coursesObject.getString("course_qualification"));
                subprocess.cal_locationSimilarity(subprocess.coursesObject.getString("course_location"), userRegion);
                subprocess.cal_courseFees(minFees, maxFees, subprocess.coursesObject.getString("course_fees_uk").replace("£", "").replace(",", ""));
//        2.3   Hashmapping the output.
                addContentBasedScore(subprocess.coursesObject.getInt("id"), subprocess.contentBasedScore);

            } catch (InstantiationException ex) {
                Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        mysql.closemySQLconnection();
        return map_contentBasedFinalScore;
    }

    public void COB_checkKNNSimilarity(String userid) throws Exception {
        try {
            //        query for similar users with user profile.
            mysql.openmySQLconnection();
            ResultSet t = mysql.executeSQLquery_stringRS2(env.dq_COB_getSimilarProfilesRatings(userid), 0);
            if (t.first()) {
                while (t.next()) {
                    subprocess.map_KNNCoursesId.put(t.getInt("course_id"), t.getInt("course_score"));

                }
            } else {
                mysql.closemySQLconnection();
                return;
            }

            for (Map.Entry m : subprocess.map_KNNCoursesId.entrySet()) {
//                !important passing vars from hashmap to the array list.
                subprocess.list_coursesIds.add(m.getKey());
            }
//            get the average ratings for each course.
            String coursesList = subprocess.idGenerator();

            ResultSet t2 = mysql.executeSQLquery_stringRS2(env.dq_COB_getAverageCoursesScorePerCourse(coursesList), 0);
            while (t2.next()) {
//                 prepare a hashmap of average course scores per similar users
                subprocess.map_KNNcoursesIdandScore.put(t2.getInt("course_id"), t2.getInt("course_score"));
            }
            mysql.closemySQLconnection();

            subprocess.getAverageRatingsForCourse();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void COB_calculateKNN(String userId, String userMajor, String SearchKey) {
//    id and user generators to be called by CB  and call getAverageRatingsForCourse()
//        returns a hashmap of 30% of COB
        subprocess.getSimilarProfiles(userId, SearchKey);
        subprocess.usersGenerator();
        //        
        for (Map.Entry v : map_contentBasedFinalScore.entrySet()) {
            subprocess.list_coursesIds.add(v.getKey());
        }
        subprocess.idGenerator();
        subprocess.getAverageRatingsForCourse();

        map_COBFinalScore = subprocess.map_coursesAverageRatings;

    }

    public void addContentBasedScore(int course_id, double course_score) throws InstantiationException, SQLException, IllegalAccessException, Exception {
//       adding the output from the algorith to the courses hashmap.
        map_contentBasedFinalScore.put(course_id, (course_score * core_contentBasedWeight));

//        !! testing purpose only 
//          appends random rating to these course ids.
//        testingColBased.appendRandomScores(course_id);
//        System.out.print("ID: " + course_id + " - " + course_score + "\n");
        subprocess.contentBasedScore = 0.0;
//

//       
    }
    // calculate the university rank similarity

    public double cal_UniversityRank(int universityRank) {
        int Rmax = 0;
        int Rmin = 1;
        if (universityRank > 0) {
            try {
                //            get the Rmax
                mysql.openmySQLconnection();
                ResultSet RankObject = mysql.executeSQLquery_stringRS2((env.dq_UniRank_getMaxRanks()), 0);

                while (RankObject.next()) {

                    Rmax = RankObject.getInt("max(recid)");

                }
                mysql.closemySQLconnection();
//   Implement the equation

                double sim = (Rmax - universityRank) / (Rmax - Rmin);
                UniversityRankSimilarity = sim;
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

        }

        return UniversityRankSimilarity * subprocess.core_universityRankWeight;
    }
    
    public double cal_UniversityNSS (double scoreOfNSS)
    {
        double minNSS = 0.0;
        double maxNSS = 0.0;
        
        if (scoreOfNSS >0)
        {
            try {
                
                mysql.openmySQLconnection();
//            get  min and max score.
             ResultSet MinMaxObject = mysql.executeSQLquery_stringRS2(env.dq_NSS_getMinAndMaxScores(), 0);
             
             while (MinMaxObject.next())
             {
                 minNSS =   MinMaxObject.getDouble("min nss");
                 maxNSS =  MinMaxObject.getDouble("max nss");
                 
             }
                mysql.closemySQLconnection();
//                implement the equation.

          double sim = (scoreOfNSS-(minNSS-1))/(maxNSS-(minNSS-1));
                UniversityNSS_Similarity = sim;
                  
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(coreEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        return UniversityNSS_Similarity * subprocess.core_universityNSSWeight;
    }
}
