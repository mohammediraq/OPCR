/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import static BUSLOGIC.testingClass.calculateCourseSimilarities;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author AhmedShalaby
 */
public class API_courseSearch extends HttpServlet {

    contentBasedEngine cb = new contentBasedEngine();
    db_mysqlops mysql = new db_mysqlops();
    FN_toJSON json = new FN_toJSON();
    var_env env = new var_env();

    static appendLog log = new appendLog();
    static String stid;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
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

            String methodName, userIdentification, currentCriteria, searchKey, updateCriteria;
            int courseScore, courseid;
            methodName = request.getParameter("methodName").trim();
// ops1 :
            if (methodName.equals("searchCourse")) {
                userIdentification = request.getParameter("usrid").trim();
                searchKey = request.getParameter("key").replace("%20", " ").trim();
//    DEBUG:            /API_courseSearch?usrid=AHMSH0001&methodName=searchCourse&key=AI
//            log search history
                log.log_userSearchHistory(userIdentification, searchKey);

                cb.calculateCourseSimilarities("Database Management","Computer Science", "Computer Programming", 1000, 4000, "South East England", 1.1, false);

                for (Map.Entry c : cb.ContenetBasedScoreMap.entrySet()) {
                    log.log_courseSearchHistory(c.getKey().toString(), c.getValue().toString());

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
