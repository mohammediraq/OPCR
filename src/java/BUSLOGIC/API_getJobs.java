/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import static DATCOL.get_allJobsIndeed.j;
import static DATCOL.get_allJobsIndeed.pageNum;
import static DATCOL.get_allJobsIndeed.sql;
import DATCOL.globalVars;
import DATCOL.ops_mysql;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

/**
 *
 * @author AhmedShalaby
 */
public class API_getJobs extends HttpServlet {

    public static ops_mysql sql = new ops_mysql();
    static globalVars gv = new globalVars();
    public static Elements j;
    public static int pageNum;
 FN_toJSON json = new FN_toJSON();
  
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
            throws ServletException, IOException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String subclass = request.getParameter("sc");
            String KeyWord = request.getParameter("k");

            for (pageNum = 0; pageNum < 999990000; pageNum += 10) {

                // 1000 * 10 loops * 10 ids per page = 100 job.
                String jobsURL = "https://www.indeed.co.uk/jobs?q=" + KeyWord.replace(" ", "+") + "&l=london&start=" + Integer.toString(pageNum);
                org.jsoup.nodes.Document doc1 = Jsoup.connect(jobsURL).get();
                String[] ids = new String[10];
                ids = doc1.getElementsByAttributeValue("data-tn-component", "organicJob").eachAttr("id").toArray(ids);

                for (String div_id : ids) {

                    //h2 jtitle 
                    //.company jcompany
                    //.slNoUnderline jreviews
                    //.location jlocation
                    //.no-wrap jsalary
                    //.summary jrequirements
                    j = doc1.select("div#" + div_id);
                    showJobs();
                    String cols = "job_title," + "job_company," + "job_reviews," + "job_location," + "job_salary," + "job_desc," + "job_subclass";
                    String vals = "'" + j.select("h2").text() + "',"
                            + "'" + j.select(".company").text() + "',"
                            + "'" + j.select("> a > span.slNoUnderline").text() + "',"
                            + "'" + j.select("#"+div_id+" > span.location").text() + "',"
                            + "'" + j.select("> table > tbody > tr > td > div:nth-child(1) > span.no-wrap").text() + "',"
                            + "'" + j.select(".summary").text() + "',"
                            + "'" + subclass + "'";

                    gv.dbname = "DATSET";
                    gv.dbuser = "root";
                    gv.dbpass = "root";
                    gv.dbtable = "Jobs_Indeed_info";
                    sql._insertCol = cols;
                    sql._insertVal = vals;
                    sql.connectMYSQL();

                    showJobs();
                }

            }
        }
            json.printJson("Status", "OK");
    }

    public static void showJobs() {
        System.out.print("Title: " + j.select("h2").text() + "---- page :" + pageNum / 1000 + "\n");
        System.out.print("Company: " + j.select(".company").text() + "\n");
        System.out.print("Reviews: " + j.select("> a > span.slNoUnderline").text() + "\n");
        System.out.print("Location: " + j.select("> span.company > span").text() + "\n");
        System.out.print("Salary: " + j.select("> table > tbody > tr > td > div:nth-child(1) > span.no-wrap").text() + "\n");
        System.out.print("Requirements: " + j.select(".summary").text() + "\n");
        System.out.print("\n \n \n");
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
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(API_getJobs.class.getName()).log(Level.SEVERE, null, ex);
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
