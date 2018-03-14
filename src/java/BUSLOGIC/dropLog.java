/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.servlet.ServletException;
import org.json.JSONArray;

/**
 *
 * @author AhmedShalaby
 */
public class dropLog {

    static JSONArray ja;
    static boolean foundRelatedSearches = false;
    static String tid;
//    
    db_mysqlops mysql = new db_mysqlops();
    FN_toJSON json = new FN_toJSON();
    var_env env = new var_env();

//    log user's search history
    public void log_userSearchHistory(String userIdentification, String searchKey, String usubclass) throws ServletException, IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, Exception {
        tid = getTrackingKey().toString();
//
        mysql.openmySQLconnection();
        mysql.executeSQLquery_stringRS2(env.dq_getCurrentCriteria(userIdentification), 0);
        mysql.rs.next();
        String currentCriteria = mysql.rs.getString(1);
        mysql.closemySQLconnection();
        String updateCriteria = currentCriteria;
//                update currentCriteria with the search key 
        log_checkRelatedSearches(updateCriteria);
        mysql.openmySQLconnection();
        mysql.executeSQL(env.dq_updateCurrentCriteria(userIdentification, updateCriteria, searchKey, tid, usubclass));
        mysql.closemySQLconnection();
    }

    public void log_checkRelatedSearches(String updateCriteria) throws ServletException, IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, Exception {
//        called in log_userSearchHistory
//            1.get tid for the same searches 
        String foundTrackingid;
        foundTrackingid = tidForSameCriteria(updateCriteria);
        ja = getHistoryBytid(foundTrackingid);
//        Q:001
        foundRelatedSearches = true;

    }

    public String tidForSameCriteria(String userCriteria) throws ServletException, IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, Exception {
        String searchtid = "";
        mysql.openmySQLconnection();
        ResultSet rtid = mysql.executeSQLquery_stringRS2("select transaction_id from  DATSET.usr_search_history "
                + "where usr_criteria = '" + userCriteria + "'  order by time_stamp desc limit 1", 0);

        while (rtid.next()) {

            searchtid = rtid.getString(1);
        }
        mysql.closemySQLconnection();

        return searchtid;
    }

    public JSONArray getHistoryBytid(String tid) throws ServletException, IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, Exception {

        mysql.openmySQLconnection();
        ResultSet rs = mysql.executeSQLquery_stringRS2(env.dq_getRelatedSearchHistory(tid), 0);
        JSONArray ja = json.convertToJSON(rs);

        mysql.closemySQLconnection();

        return ja;
    }

    public void log_courseSearchHistory(String id, String sm) throws ServletException, IOException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, Exception {
        {
            try {
                mysql.openmySQLconnection();
                mysql.executeSQL(env.dq_insertCourseSearchHistory(id, sm, tid));
                mysql.closemySQLconnection();
            } catch (Exception ex) {
                System.out.print(ex.getCause());
            }
        }
    }

    public String concat_generateCourseCriteria(int courseId, String root, String className, String subClass) {
        String courseCriteria;
        courseCriteria = courseId + "-" + root + "-" + className + "-" + subClass;

        return courseCriteria;
    }

    public UUID getTrackingKey() {
        UUID uniqueKey = UUID.randomUUID();
        return uniqueKey;
    }

}
