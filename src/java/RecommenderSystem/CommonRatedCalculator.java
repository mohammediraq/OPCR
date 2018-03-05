/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RecommenderSystem;

import BUSLOGIC.CollaborativeBased.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author
 */
public class CommonRatedCalculator {
    //Related to common rate

    public static GlobalVariablesClass listOfVars = new GlobalVariablesClass();

    static public double ARWc = 0.0;
    static public int Omax = 0;
//    constant
    static public int K = 2;
//   Related to Ratings levels
//    3:2:1 = 100%
//    rate5:rate4:rate3 = 50:30:20
    static public double IfRated3 = (listOfVars.KNNW / 3);
    static public double IfRated4 = (listOfVars.KNNW / 3) * 2;
    static public double IfRated5 = listOfVars.KNNW;
    static public int Oc = 0;

//    
    static ArrayList ContentBased_ListOfID = new ArrayList();
    static ArrayList KNN_ListOfID = new ArrayList();
    static ArrayList ContentBased_ListOfRates = new ArrayList();
    static ArrayList KNN_ListOfIDRates = new ArrayList();

    public static void main(String[] args) {
        TestingFunction();
      getOc("C111");
        
        System.out.format("ARWc: %s",ARWc);
//        checkCommonRated(ContentBased_ListOfID, KNN_ListOfID);

    }

    public static void checkCommonRated(ArrayList ContentBased_ListOfID, ArrayList KNN_List) {
        Map<String, Integer> finalCoursesMap = new HashMap();

        int Oc_Value = 0;
//        loop through CB items
        for (int i = 0; i < ContentBased_ListOfID.size(); i++) {
            for (int j = 0; j < KNN_ListOfID.size(); j++) {
                String courseAid = ContentBased_ListOfID.get(i).toString();
                double courseASimilarity = Double.parseDouble(ContentBased_ListOfRates.get(i).toString());
                String courseBid = KNN_ListOfID.get(j).toString();
                int courseBRate = Integer.parseInt(KNN_ListOfIDRates.get(j).toString());
                if (courseAid.equalsIgnoreCase(courseBid)) {
                    System.out.format("Matched: A:  %s  Rate: %s, b: %s Rate %s ", courseAid, courseASimilarity, courseBid, courseBRate + "\n");
                }
            }
        }

    }

    public static double getSimById(int id) {
        double sim = ContentBased_ListOfRates.indexOf(id);
        return sim;
    }

    public static int getRateById(int id) {
        int rate = KNN_ListOfIDRates.indexOf(id);
        return rate;
    }

    public static int getOc(String courseId) {
        int oc = 0;
        for (int i = 0; i < KNN_ListOfID.size(); i++) {
            String CurrentPos = KNN_ListOfID.get(i).toString();

            if (courseId.equalsIgnoreCase(CurrentPos)) {
                oc += 1;
            }

        }
        Oc = oc;
        System.out.format("OC %s", Oc);
        return Oc;

    }

    public static double getARWc(String courseId) {
        double _ARWc = 0.0;
        int ArrOfIndecies = 0;
        double resolvedRate = 0.0;
        int CourseRate = 0;

        for (int i = 0; i < KNN_ListOfID.size(); i++) {

            String CurrentPos = KNN_ListOfID.get(i).toString();

            if (courseId.equalsIgnoreCase(CurrentPos)) {
                CourseRate = Integer.parseInt(KNN_ListOfIDRates.get(i).toString());

                if (CourseRate == 5) {
                    resolvedRate += IfRated5;
                    
                ArrOfIndecies += 1;
                }

                else if (CourseRate == 4) {
                    resolvedRate += IfRated4;
                    
                ArrOfIndecies += 1;
                }

                else if (CourseRate == 3) {
                    resolvedRate += IfRated3;
                    
                ArrOfIndecies += 1;
                }


            }
//            get the rate
        }

//        calculate the average;
        _ARWc = (resolvedRate / ArrOfIndecies);

        ARWc = _ARWc;
        return ARWc * 100;
    }

    public static void TestingFunction() {
        ContentBased_ListOfID.add("C1030");
        ContentBased_ListOfID.add("C001");
        ContentBased_ListOfID.add("C1031");
        ContentBased_ListOfID.add("C005");
        ContentBased_ListOfID.add("C003");
        ContentBased_ListOfID.add("C1441");
        ContentBased_ListOfID.add("C1341");
        ContentBased_ListOfID.add("C1088");
        ContentBased_ListOfID.add("C1022");
        ContentBased_ListOfID.add("C1222");

        ContentBased_ListOfRates.add(0.5);
        ContentBased_ListOfRates.add(0.49);
        ContentBased_ListOfRates.add(0.47);
        ContentBased_ListOfRates.add(0.4);
        ContentBased_ListOfRates.add(0.39);
        ContentBased_ListOfRates.add(0.37);
        ContentBased_ListOfRates.add(0.35);
        ContentBased_ListOfRates.add(0.29);
        ContentBased_ListOfRates.add(0.27);
        ContentBased_ListOfRates.add(0.25);

        KNN_ListOfID.add("C001");
        KNN_ListOfID.add("C005");
        KNN_ListOfID.add("C010");
        KNN_ListOfID.add("C020");
        KNN_ListOfID.add("C003");
        KNN_ListOfID.add("C030");
        KNN_ListOfID.add("C111");
        KNN_ListOfID.add("C033");
        KNN_ListOfID.add("C003");
        KNN_ListOfID.add("C044");
        KNN_ListOfID.add("C050");
        KNN_ListOfID.add("C003");
        KNN_ListOfID.add("C021");
        KNN_ListOfID.add("C049");
        KNN_ListOfID.add("C111");
        KNN_ListOfID.add("C008");
        KNN_ListOfID.add("C111");
        KNN_ListOfID.add("C010");

        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(2);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(2);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(4);
        KNN_ListOfIDRates.add(5);
        KNN_ListOfIDRates.add(3);
        KNN_ListOfIDRates.add(4);

    }

}
