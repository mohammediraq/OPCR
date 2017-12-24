/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;

/**
 *
 * @author AhmedShalaby
 */
public class API_insertNewUser extends HttpServlet {

    db_mysqlops mysql = new db_mysqlops();
    FN_toJSON json = new FN_toJSON();
    var_env env = new var_env();
    JSONArray m;

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
            throws ServletException, IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String userEduInfo, userConInfo, usr_id = null;
            String usr_first_name = request.getParameter("ufn").replace("%20", " ");
            String usr_last_name = request.getParameter("uln").replace("%20", " ");
            String usr_mid_name = request.getParameter("umn").replace("%20", " ");
            String usr_address = request.getParameter("uad").replace("%20", " ");
            String usr_city = request.getParameter("ucit").replace("%20", " ");

            String usr_email = request.getParameter("uem").replace("%20", " ");
            String usr_mobile = request.getParameter("umo").replace("%20", " ");
//          end of contact data
            String usr_education_background = request.getParameter("uedb").replace("%20", " ");
            String usr_current_qualification = request.getParameter("ucq").replace("%20", " ");
            String usr_language = request.getParameter("ulang").replace("%20", " ");
            String usr_skills = request.getParameter("uski").replace("%20", " ");

            if (!"undefined".equals(usr_first_name) && !"undefined".equals(usr_last_name) && !"undefined".equals(usr_mid_name) && !"undefined".equals(usr_address) && !"undefined".equals(usr_city) && !"undefined".equals(usr_email) && !"undefined".equals(usr_mobile) && !"undefined".equals(usr_education_background) && !"undefined".equals(usr_current_qualification) && !"undefined".equals(usr_language) && !"undefined".equals(usr_skills)) {
//            Create UID
                usr_id = env.createUID(usr_first_name, usr_last_name);
//          Generating queries for adding new user
                userConInfo = env.dq_insertUserContactInfo(usr_id, usr_first_name, usr_mid_name, usr_last_name, usr_address, usr_city, usr_email, usr_mobile);
                userEduInfo = env.dq_insertUserEduInfo(usr_id, usr_education_background, usr_current_qualification, usr_language, usr_skills);
                mysql.openmySQLconnection();
                mysql.executeSQL(userConInfo);
                mysql.executeSQL(userEduInfo);

                mysql.closemySQLconnection();
                m = json.printJson("addingUserResponse", "Thanks " + usr_first_name + " for registeration!");
                out.print(m);
            } else {
                m = json.printJson("addingUserResponse", "Registeration Error: Null data is not allowed.");
                out.print(m);
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
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(API_insertNewUser.class.getName()).log(Level.SEVERE, null, ex);
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
