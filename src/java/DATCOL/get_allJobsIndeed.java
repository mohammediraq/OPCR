/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DATCOL;

import DATCOL.globalVars;
import DATCOL.ops_mysql;
import java.io.IOException;
import java.sql.SQLException;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;


/**
 *
 * @author AhmedShalaby
 */
public class get_allJobsIndeed {

    public static ops_mysql sql = new ops_mysql();
    static globalVars gv = new globalVars();
    public static Elements j;
    public static int pageNum;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        for (pageNum = 0; pageNum < 999990000; pageNum += 10) {

            // 1000 * 10 loops * 10 ids per page = 100 job.
            String jobsURL = "https://www.indeed.co.uk/jobs?q=computer+science&l=london&start=" + Integer.toString(pageNum);
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
                String cols = "job_title," + "job_company," + "job_reviews," + "job_location," + "job_salary," + "job_desc";
                String vals = "'" + j.select("h2").text() + "',"
                        + "'" + j.select(".company").text() + "',"
                        + "'" + j.select("> a > span.slNoUnderline").text() + "',"
                        + "'" + j.select("> span.company > span").text() + "',"
                        + "'" + j.select("> table > tbody > tr > td > div:nth-child(1) > span.no-wrap").text() + "',"
                        + "'" + j.select(".summary").text() + "'";

                gv.dbname = "DATSET";
                gv.dbuser = "root";
                gv.dbpass = "root";
                gv.dbtable = "Jobs_Indeed_info";
                sql._insertCol = cols;
                sql._insertVal = vals;
                sql.connectMYSQL();

            }

        }
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

}
