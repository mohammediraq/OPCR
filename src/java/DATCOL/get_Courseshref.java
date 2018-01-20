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

/**
 *
 * @author AhmedShalaby
 */
public class get_Courseshref {

    static BUSLOGIC.coursesModuler cm = new BUSLOGIC.coursesModuler();
    private static Connection con = null;
    private static Statement stmt = null;
    public static String courseRoot ,courseClass,courseSubClass,search_language, course_language, search_key, course_startdate, course_field, course_fees, course_feesplan, course_wprovider, uni_address, uni_postal, course_entryrequirements, course_name, course_url, course_desc, course_time, course_title, course_qualification, course_duration, course_location, course_mode, course_assessment, course_requirements, course_fee_int, course_fee_uk;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, Exception {

        try {
            for (int pageNum = 1; pageNum < 30; pageNum++) {

                search_language = "English";
                search_key = "Database Management";
                courseRoot= "Engineering";
                courseClass = "Computer Science";
                courseSubClass="Database Management";
                
                
                //get course url
                String URL = "https://digital.ucas.com/search/results?SearchText=" + search_key.replace(" ", "+") + "&AutoSuggestType=&SearchType=searchbarbutton&PreviouslyAppliedFilters=D_0_Postgraduate__&filters=Destination_Postgraduate&ProviderText=&SubjectText=&DistanceFromPostcode=1mi&RegionDistancePostcode=&SortOrder=0&CurrentView=List&pageNumber=" + pageNum;
                org.jsoup.nodes.Document doc2 = Jsoup.connect(URL).get();
                int urlCount = doc2.getElementsByClass("button button--small js-searchResults-view").eachAttr("href").size();
                String[] urlList = new String[urlCount];
                urlList = doc2.getElementsByClass("button button--small js-searchResults-view").eachAttr("href").toArray(urlList);

                //get course details * url as an input *
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DATSET?"
                        + "user=root&password=root&useSSL=false");
                con.getSchema();
                for (int i = 0; i < urlList.length; i++) {
                    try {
                        String detailsURL = "https://digital.ucas.com" + urlList[i];
                        org.jsoup.nodes.Document doc1 = Jsoup.connect(detailsURL).get();

                        String courseTitle = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > h1").text().replace("'", "\'");
                        String courseDesc = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > p:nth-child(3)").text().replace("'", "\'");
                        String courseTime = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > div:nth-child(5) > div > ul > li:nth-child(2) > p.impact.impact--medium").text().replace("'", "\'");
                        String courseQualification = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > div:nth-child(5) > div > ul > li:nth-child(1) > p.impact.impact--medium").text().replace("'", "\'");
                        String courseDuration = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > div:nth-child(5) > div > ul > li:nth-child(5) > p.impact.impact--medium").text();
                        String courseStartDate = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > div:nth-child(5) > div > ul > li:nth-child(4) > p.impact.impact--medium").text();
                        String courseEntryRequirements = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > section > section > p").text().replace("'", "\'");
                        String courseLocation = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > div:nth-child(5) > div > ul > li:nth-child(3) > p.impact.impact--medium").text();
                        String uniAddress = doc1.select("#section--content > div > div > div.grid__column-4 > aside > div:nth-child(2) > div > div > div.vcard > div > span.adr > span:nth-child(1)").text();
                        String uniPostalCode = doc1.select("#section--content > div > div > div.grid__column-4 > aside > div:nth-child(2) > div > div > div.vcard > div > span.adr > span.postal-code").text();
                        String providerWebSite = doc1.select("#ProviderWebsite").text();
                        String courseFees = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > div:nth-child(9) > div > section > div > table > tbody > tr:nth-child(2) > td.column-width--20pc").text().trim();
                        String courseFeesPlan = doc1.select("#section--content > div > div > div.grid__column-8 > div > div:nth-child(1) > div > div:nth-child(9) > div > section > div > table > tbody > tr:nth-child(1) > td.column-width--50pc").text().trim();

//                      set a clean data model
                        course_url = urlList[i];
                        course_desc = courseDesc.replace("'", "\'");
                        course_time = courseTime;
                        course_title = courseTitle.replace("'", "\'");
                        course_qualification = courseQualification.replace("'", "\'");
                        course_duration = courseDuration;
                        course_location = courseLocation;
                        course_entryrequirements = courseEntryRequirements.replace("'", "\'");
                        course_fees = courseFees;
                        course_startdate = courseStartDate;
                        course_wprovider = providerWebSite;
                        course_feesplan = courseFeesPlan;
                        course_field = search_key;
                        course_language = search_language;
                        uni_address = uniAddress;
                        uni_postal = uniPostalCode;
                        if ("".equals(course_fees))
                        {
                            course_fees ="0";
                        }
                        String query = "insert into `DATSET`.`courses_postgrad` ("
                                + "`course_URL`,\n"
                                + "`course_desc`,\n"
                                + "`course_field`,\n"
                                + "`course_language`,\n"
                                + "`course_title`,\n"
                                + "`course_qualification`,\n"
                                + "`course_entry_requirements`,\n"
                                + "`course_fees_uk`,\n"
                                + "`course_feesplan`,\n"
                                + "`course_time`,\n"
                                + "`course_startdate`,\n"
                                + "`course_duration`,\n"
                                + "`course_wprovider`,\n"
                                + "`course_location`,\n"
                                + "`uni_address`,\n"
                                + "`uni_postal`,\n"
                                + "`course_rootClass`,\n"
                                + "`course_class`,\n"
                                + "`course_subclass`)\n"
                                + " values ('"
                                + "" + course_url + "',"
                                + "'" + course_desc + "',"
                                + "'" + course_field + "',"
                                + "'" + course_language + "',"
                                + "'" + course_title + "',"
                                + "'" + course_qualification + "',"
                                + "'" + course_entryrequirements + "',"
                                + "'" + course_fees + "',"
                                + "'" + course_feesplan + "',"
                                + "'" + course_time + "',"
                                + "'" + course_startdate + "',"
                                + "'" + course_duration + "',"
                                + "'" + course_wprovider + "',"
                                + "'" + course_location + "',"
                                + "'" + uni_address + "',"
                                + "'" + uni_postal + "',"
                                + "'" + courseRoot + "',"
                                + "'" + courseClass + "',"
                                + "'" + courseSubClass + "');";

//                        System.out.print(i + ": " + course_name + "- Page# " + pageNum + "- Courses# " + urlList.length + " \n" + course_url + "\n");
                        Statement st = con.createStatement();
                        st.execute(query);
                    } catch (Exception ex) {
                        System.out.print("Error: " + ex.getMessage() + "\n" + "Page# " + pageNum + "- Courses# " + urlList.length + " \n" + course_url + "\n");

                    }

                }

                con.close();

            }
         

        } catch (Exception e) {
            e.printStackTrace();
        
        }
        cm.moduleUniversityName();
        cm.moduleUniversityNSS();
    }

}
