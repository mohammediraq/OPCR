/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC.CollaborativeBased;

/**
 *
 * @author AhmedShalaby
 */
public class GlobalVariablesClass {

    //Collaborative based 
    public double CollaborativeBased_Share = .30;

    //
    public double Ontology_Similarity = .50;
    public double RecommendationHistory_Similarity = .50;
    //
    public double KNNW = 0.30;
    //
    //Ontology_Similarity
//    Level 1 
    public double MainClass_Similarity = .10;
    public double SubClass_Similarity = .15;
    public double FieldOfStudy_Similarity = .05;
    public double Level1_OntologySimilarity = MainClass_Similarity + SubClass_Similarity + FieldOfStudy_Similarity;
//    
//   Level 2
    public double InterestArea_Similarity = .10;
    public double Level2_OntologySimilarity = InterestArea_Similarity;
//    
//    Level 3 
    public double Location_Similarity = .05;
    public double Level3_OntologySimilarity = Location_Similarity;
//    Level 4
    public double Skills_Similarity = .05;
    public double Level4_OntologySimilarity = Skills_Similarity;
    //


    //

//
}
