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
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Users’ similarity (collaborative based filtering) In order get the
 * top N similar users to the current user we need to define the scoring
 * function for the user similarity. Similarity scoring function between users
 * (U1, U2) = ontology similarity (Ua, Ub) + Recommendation history similarity
 * (Ua, Ub) ------------------------- (equation 1) The similarity scoring range
 * will be between (0-1) and the weight for ontology similarity will be 50% and
 * for the recommendation history similarity will be 50 %. *
 */
public class OntologySimilarityClass {
//  Class instances 

    static GlobalVariablesClass listOfVars = new GlobalVariablesClass();
    static db_mysqlops mysql = new db_mysqlops();
    static FN_toJSON json = new FN_toJSON();
    static var_env env = new var_env();
//    
//    class vars

    public static double Ontology_Similarity_Coursescore = 0;
    public ArrayList UserA = new ArrayList();
    public ArrayList UserB = new ArrayList();

//
//  1.get Ua profile props. => 1 function
//  2.get Ub profile props. => 1 function.
//  3.Calculate level of similarties. => 5 functions.
//      
//    public static void main(String[] args) {
//        Calculate_OntologySimilarity();
//
//    }

    public static double Calculate_OntologySimilarity(ArrayList UserA_props,ArrayList UserB_props) {
//
//        ArrayList UserA_props = new ArrayList();
//        ArrayList UserB_props = new ArrayList();
//   
//         => list of Ua profile features.
//         => list of Uxx profiles and features.
//         <= list of similarity scores for Ua and other users.
//
//        UserA_props.add("Computer Science1");
//        UserA_props.add("Computer Programming");
//        UserA_props.add("Java Programming");
//        UserA_props.add("Tech Leading");
//        UserA_props.add("London111");
//        UserA_props.add("Coding");
//
//        UserB_props.add("Computer Science");
//        UserB_props.add("Computer Programming");
//        UserB_props.add("Java Programming");
//        UserB_props.add("Tech Leading");
//        UserB_props.add("London12");
//        UserB_props.add("Coding1");


        double score= CompareProfilesProps(UserA_props, UserB_props);
        return score;
    }

        public static double CompareProfilesProps(ArrayList UserA_props, ArrayList UserB_props) {

        for (int i = 0; i < 6; i++) {
            int propOrder = i;
            String el1 = UserA_props.get(i).toString();
            String el2 = UserB_props.get(i).toString();

            switch (propOrder) {
                case 0:
                    if (el1 == null ? el2 == null : el1.equals(el2)) {
                        Ontology_Similarity_Coursescore += listOfVars.SubClass_Similarity;
                        break;
                    }

                case 1:
                    if (el1 == null ? el2 == null : el1.equals(el2)) {
                        if (el1.equals(el2)) {
                            Ontology_Similarity_Coursescore += listOfVars.MainClass_Similarity;
                        break;
                        }
                    }

                case 2:
                    if (el1 == null ? el2 == null : el1.equals(el2)) {
                        if (el1.equals(el2)) {
                            Ontology_Similarity_Coursescore += listOfVars.FieldOfStudy_Similarity;
                        break;
                        }
                    }

                case 3:
                    if (el1 == null ? el2 == null : el1.equals(el2)) {
                        if (el1.equals(el2)) {
                            Ontology_Similarity_Coursescore += listOfVars.InterestArea_Similarity;
                        break;
                        }
                    }

                case 4:
                    if (el1 == null ? el2 == null : el1.equals(el2)) {
                        if (el1.equals(el2)) {
                            Ontology_Similarity_Coursescore += listOfVars.Location_Similarity;
                        break;
                        }
                    }

                case 5:
                    if (el1 == null ? el2 == null : el1.equals(el2)) {
                        if (el1.equals(el2)) {
                            Ontology_Similarity_Coursescore += listOfVars.Skills_Similarity;
                        break;
                        }
                    }
                    break;

            }
        }
        System.out.format("\n-----------\n Ontology Similarity: %s",Ontology_Similarity_Coursescore +"\n---------\n");
        return Ontology_Similarity_Coursescore ;
    }

}
