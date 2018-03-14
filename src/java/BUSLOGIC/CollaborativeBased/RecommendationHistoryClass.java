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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * In our system each user U has a recommendation history which is built after
 * given the user recommendations and his rate the recommendations. cosine
 * similarity approach *
 */
public class RecommendationHistoryClass {
    //  Class instances 

    static GlobalVariablesClass listOfVars = new GlobalVariablesClass();
    static db_mysqlops mysql = new db_mysqlops();
    static FN_toJSON json = new FN_toJSON();
    static var_env env = new var_env();
     double score = 0.0;
     int Numerator = 0;
     double Denominator = 0.0;

    public static HashMap<Integer, Double> RecommendationHistory_Map = new HashMap<Integer, Double>();
//    
//   - will get all of Ua rating history and iterates through all users profiles
//    - returns the Recommendation history's score for users' history to be added to Ontology sim.
//     => listof Ua 5 courses , listOf Ub 5 coureses.
//      <= Recommendation similarity score..

  

    public  double Calculate_RecommendationHistoryScore(Map UaCourseList, Map UbCourseList) {
        CompareCourse(UaCourseList, UbCourseList);
        score =0.0;
        score = (Numerator / Denominator) * listOfVars.RecommendationHistory_Similarity;
        
        System.out.format("\n---------\n Final Score:%s", score+"\n");
        return score;
    }

    public  void  CompareCourse(Map UaCourseList, Map UbCourseList) {

        GetNumerator(UaCourseList, UbCourseList);
        GetDenominator(UaCourseList, UbCourseList);

        

    }

    public  void GetDenominator(Map UaCourseList, Map UbCourseList) {
        ArrayList ListA = new ArrayList();
        ArrayList ListB = new ArrayList();
        double DenominatorA = 0.0;
        double DenominatorB = 0.0;

        double ListA_sqrt = 0.0;
        double ListB_sqrt = 0.0;

        UaCourseList.forEach((ka, va) -> {
            double List_a = 0.0;

            List_a = Double.parseDouble(va.toString()) * Double.parseDouble(va.toString());
            ListA.add(List_a);
        });

        UbCourseList.forEach((kb, vb) -> {
            double List_b = 0.0;

            List_b = Double.parseDouble(vb.toString()) * Double.parseDouble(vb.toString());
            ListB.add(List_b);
        });
        for (int i = 0; i < ListA.size(); i++) {
            DenominatorA += Double.parseDouble(ListA.get(i).toString());
        }
        for (int i = 0; i < ListB.size(); i++) {
            DenominatorB += Double.parseDouble(ListB.get(i).toString());
        }

        ListA_sqrt = Math.sqrt(DenominatorA);
        ListB_sqrt = Math.sqrt(DenominatorB);

        Denominator = ListA_sqrt * ListB_sqrt;
        System.out.format("\n-------------\nDenominator: %s", Denominator+"\n");

    
    }

    public  void GetNumerator(Map UaCourseList, Map UbCourseList) {
        Map<String, Integer> numeratorMap = new HashMap<String, Integer>();
        ArrayList NumeratorList = new ArrayList();
        Iterator it = UaCourseList.entrySet().iterator();
        Iterator it2 = UbCourseList.entrySet().iterator();

        Map.Entry EntryA = (Map.Entry) it.next();
        Map.Entry EntryB = (Map.Entry) it2.next();

        UaCourseList.forEach((ka, va) -> {
            if (ka != null) {
                UbCourseList.forEach((kb, vb) -> {

                    if (ka.equals(kb)) {
                        String eqution = ka.toString() + "+" + kb.toString();
                        if (!numeratorMap.containsKey(eqution)) {

                            System.out.format("keyA: %s , Key:B : %s, valueA: %d%n, valueB:%d%n", ka, kb, va, vb);
                            int s = Integer.parseInt(va.toString()) * Integer.parseInt(vb.toString());
                            NumeratorList.add(s);

                        }
                    }
                });
            }
        });
        for (int i = 0; i < NumeratorList.size(); i++) {
            Numerator += Integer.parseInt(NumeratorList.get(i).toString());

        }
        System.out.format("\n-------------\n Numerator: %s", Numerator + "\n");
  
    }

}
