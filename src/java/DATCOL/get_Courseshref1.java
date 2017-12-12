/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DATCOL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

/**
 *
 * @author AhmedShalaby
 */
public class get_Courseshref1 {

    private static Connection con = null;
    private static Statement stmt = null;
    public static String search_language, course_language, search_key, course_startdate, course_field, course_fees, course_feesplan, course_wprovider, uni_address, uni_postal, course_entryrequirements, course_name, course_url, course_desc, course_time, course_title, course_qualification, course_duration, course_location, course_mode, course_assessment, course_requirements, course_fee_int, course_fee_uk;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

//            org.jsoup.nodes.Document doc = Jsoup.connect("https://billing.te.eg/api/Offline/GetVersionNo").post();
            org.jsoup.nodes.Document doc2 = Jsoup.connect("https://billing.te.eg/api/Account/Inquiry")
                    .data("AreaCode", "02")
                    .data("PhoneNumber", "35829487")
                    .data("InquiryBy", "telephone")
                    // and other hidden fields which are being passed in post request.
                    .userAgent("Mozilla")
                    .post();
            System.out.println(doc2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
