/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import static BUSLOGIC.testingClass.calculateCourseSimilarities;
import static BUSLOGIC.testnewcorefunctions.core;
import BUSLOGIC.*;
import RecommenderSystem.CommonRatedCalculator;
import RecommenderSystem.sortedMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author AhmedShalaby
 */
public class API_courseSearch extends HttpServlet {

    static coreEngine core = new coreEngine();
    static CommonRatedCalculator commonRateEngine = new CommonRatedCalculator();
    static coreEngineSubProccess subprocess = new coreEngineSubProccess();
    static sortedMap sortmap = new sortedMap();
//    contentBasedEngine core = new contentBasedEngine();
    db_mysqlops mysql = new db_mysqlops();
    FN_toJSON json = new FN_toJSON();
    var_env env = new var_env();
    testingColBased test = new testingColBased();

    static appendLog log = new appendLog();
    static String stid;
    int hop = 0;
    HashMap<Integer, Double> CourseList_FinalScore = new HashMap<Integer, Double>();
    Map<Integer, Double> CourseList_FinalScore_Ordered = new HashMap<Integer, Double>();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. //
     * /API_courseSearch?methodName=searchCourse&key=Computer%20Graph&usrid=AHMSH0001&sc=Computer%20Graphics&ur=Jersey&um=Computer%20Programming&nss=55&coursemin=8000&coursemx=10000
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            String methodName, userIdentification, currentCriteria, searchKey, updateCriteria, user_major, user_subclass, user_region;
            int courseScore, courseid;
            double courseMinFees, courseMaxFees, minNSS;
            methodName = request.getParameter("methodName").trim();
// ops1 :
            if (methodName.equals("searchCourse")) {
                courseMinFees = Double.parseDouble(request.getParameter("coursemin").trim());
                courseMaxFees = Double.parseDouble(request.getParameter("coursemx").trim());
                minNSS = Double.parseDouble(request.getParameter("nss").trim());
                user_major = request.getParameter("um").replace("%20", " ").trim();
                user_region = request.getParameter("ur").replace("%20", " ").trim();
                user_subclass = request.getParameter("sc").trim();
                userIdentification = request.getParameter("usrid").trim();
                searchKey = request.getParameter("key").replace("%20", " ").trim();
//    DEBUG:/API_courseSearch?usrid=AHMSH0001&methodName=searchCourse&key=AI
//            log search history

//                core.map_COBFinalScore.clear();
//                core.map_contentBasedFinalScore.clear();
                // log.log_userSearchHistory(userIdentification, searchKey, user_subclass);
//              get gvars of all weights
//              identifying gvars for initial vars calls.
                core.map_contentBasedFinalScore.clear();
                core.map_COBFinalScore.clear();
                commonRateEngine.FinalList_ComRated.clear();
             
                
                core.setSearchProperties(user_major, user_subclass, searchKey, user_region, minNSS, courseMinFees, courseMaxFees);
                
           core.CB_calculateCourseSimilarities();
                
               
                
                core.CollaborativeEngine(userIdentification);
                
                core.CommonRateEngine();
                //CourseList_FinalScore

                core.map_contentBasedFinalScore.forEach((cbk, cbv) -> {

                    System.out.format("cb key %s map_contentBasedFinalScore value %s", cbk, cbv + "\n");

                    commonRateEngine.FinalList_ComRated.forEach((cobk, cobv) -> {

                        if (cbk.equals(cobk)) {
                            try {
                                double score = 0.0;

                                core.UniversityData(cbk);
                                score = (cobv + cbv + core.UniversityNSS_finalScore + core.UniversityRank_finalScore);

                                CourseList_FinalScore.put(cbk, score);

                                CourseList_FinalScore_Ordered = sortmap.sortByValue(CourseList_FinalScore);

                                //get the concated ids of top 10 courses 
                            } catch (SQLException ex) {
                                Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);

                            } catch (Exception ex) {
                                Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });

                });
                System.out.print("=======================");
//                   core.CommonRateEngine().forEach((cobk,cobv)->{
//                       
//                    System.out.format("cob key %s CommonRateEngine value %s",cobk,cobv +"\n");
//                
//                });
                System.out.print("=======================");

                CourseList_FinalScore_Ordered.forEach((k, v) -> {

                    subprocess.list_coursesIds.add(k);
                    System.out.format("IDs with similarity: %s => %s", k,v);

                });
                mysql.closemySQLconnection();
                String ids = "";
                      ids=  subprocess.CourseidGenerator();
                System.out.format("IDs of final recommendation: %s", ids);
                try {
                    mysql.openmySQLconnection();
                    Statement getTop5CoursesData = mysql.con.createStatement();

                    ResultSet top5Courses_Object = getTop5CoursesData.executeQuery("SELECT * FROM DATSET.courses_postgrad where id in (" + ids + ") ORDER BY FIELD(id, "+ids+")");
                    while (top5Courses_Object.next()) {
                        out.print(json.convertToJSON(top5Courses_Object));
                    }
                    mysql.closemySQLconnection();

                } catch (InstantiationException ex) {
                    Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
                }

// ops2 : 
            } else if (methodName.equals("scoreCourse")) {
//    DEBUG:    /API_courseSearch?usrid=AHMSH0001&methodName=scoreCourse&courseid=1&courseScore=3
                userIdentification = request.getParameter("usrid").trim();
                courseid = Integer.parseInt(request.getParameter("courseid"));
                courseScore = Integer.parseInt(request.getParameter("courseScore"));
                String cr, cc, csc, courseCriteria;
//                get course criteria 
                mysql.openmySQLconnection();
                ResultSet rs = mysql.executeSQLquery_stringRS2("select course_rootClass,course_class,course_Subclass"
                        + " from datset.courses_postgrad where id=" + courseid + "", 0);

                while (rs.next()) {
                    cr = rs.getString("course_rootClass");
                    cc = rs.getString("course_class");
                    csc = rs.getString("course_Subclass");
                    courseCriteria = log.concat_generateCourseCriteria(courseid, cr, cc, csc);
                    mysql.closemySQLconnection();
//                    done
//                  log values in course search score 
                    mysql.openmySQLconnection();
                    mysql.executeSQL(env.dq_insertCourseSearchScore(courseid, courseScore, courseCriteria, userIdentification));
                    mysql.closemySQLconnection();

                    out.print("courserid: " + courseid + "</br>" + "courseScore: " + courseScore + "</br>");
                }

            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
                Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(API_courseSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
