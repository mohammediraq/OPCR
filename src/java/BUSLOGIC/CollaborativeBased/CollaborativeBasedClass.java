/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC.CollaborativeBased;

import BUSLOGIC.FN_toJSON;
import BUSLOGIC.db_mysqlops;
import BUSLOGIC.var_env;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author
 */
public class CollaborativeBasedClass {

    //  Class instances 
    static GlobalVariablesClass listOfVars = new GlobalVariablesClass();
    static db_mysqlops mysql = new db_mysqlops();
    static FN_toJSON json = new FN_toJSON();
    static var_env env = new var_env();

    RecommendationHistoryClass RecommendationHistory = new RecommendationHistoryClass();
    OntologySimilarityClass OntologySimilarity = new OntologySimilarityClass();
//  
//for testing
    public ArrayList UserA_props = new ArrayList();
    public ArrayList UserB_props = new ArrayList();
    public double COB_FinalScore;
    public Map<String, Integer> UaCourseList = new HashMap<String, Integer>();
    public Map<String, Integer> UbCourseList = new HashMap<String, Integer>();
//



        public double Calculate_CollaborativeBased() {
//        TestingFunction();

        double FinalCOB_Score = 0.0;
        double ontologyScore = OntologySimilarity.Calculate_OntologySimilarity(UserA_props, UserB_props);
        double recommendationScore = RecommendationHistory.Calculate_RecommendationHistoryScore(UaCourseList, UbCourseList);

        FinalCOB_Score = ontologyScore + recommendationScore;
        System.out.format("\n----------\n Collaborative Score: %s", COB_FinalScore + "\n");
        return FinalCOB_Score;

    }

    //    for testing 
//    public static void main(String[] args) {
//        TestingFunction();
//        double ontologyScore = OntologySimilarity.Calculate_OntologySimilarity(UserA_props, UserB_props);
//        double recommendationScore = RecommendationHistory.Calculate_RecommendationHistoryScore(UaCourseList, UbCourseList);
//
//        double s = ontologyScore + recommendationScore;
//
//        System.out.format("\n----------\n Collaborative Score: %s", s + "\n");
//
//    }
    //    for testing 
    public void TestingFunction() {
        UaCourseList.put("C1002", 4);
        UaCourseList.put("C1004", 2);
        UaCourseList.put("C1003", 5);
        UaCourseList.put("C1001", 4);
        UaCourseList.put("C1005", 3);
//
        UbCourseList.put("C1007", 5);
        UbCourseList.put("C1005", 2);
        UbCourseList.put("C1004", 5);
        UbCourseList.put("C1001", 4);
        UbCourseList.put("C1006", 4);

        UserA_props.add("artificial intelligent");
        UserA_props.add("computer sciences");
        UserA_props.add("information technology");
        UserA_props.add("management");
        UserA_props.add("Portsmouth");
        UserA_props.add("programming");

        UserB_props.add("computer programming");
        UserB_props.add("computer sciences");
        UserB_props.add("information technology");
        UserB_props.add("management");
        UserB_props.add("Southampton");
        UserB_props.add("programming");
    }

}
