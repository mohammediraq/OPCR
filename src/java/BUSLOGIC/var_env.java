/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.sql.SQLException;

/**
 *
 * @author AhmedShalaby
 */
public class var_env {

    static db_mysqlops mysql = new db_mysqlops();
    public String dbhost = "localhost";
    public String dbport = "3306";
    public String dbname = "DATSET";
    public String dbuser = "root";
    public String dbpassword = "root";
    public String dtusercontact = "usr_contact_dat";
    public String dtuseredu = "usr_edu_dat";
    public String dtcounties = "uk_countries";
    public String dtlanguages = "conf_languages";
//    Get all Locations 
    public String dq_getAllCounties = "SELECT * FROM DATSET.uk_countries";
//  User's mail validiation
    public String dq_getUserEmail = "SELECT count(usr_email) 'found' FROM DATSET.usr_contact_dat where usr_email=";
//    Classes
    public String dq_getedubackground = "SELECT distinct(subclass_name) from datset.conf_subclasses";
//    SubClasses
    public String dq_getedufields = "SELECT * FROM DATSET.conf_subclasses";
// Languages 
    public String dq_getalllanguages = "SELECT distinct(language_name),recid FROM DATSET.conf_languages";
//    course level
    public String dq_getallcourselevel = "SELECT * FROM DATSET.conf_course_lvl order by level_order asc";
// nss
    public String dq_getnss = "SELECT * FROM DATSET.uni_nss_scoring";
//    item weight
    public String dq_itemweight = "SELECT * FROM DATSET.usr_item_weight";

    public String dq_Courseitemweight = "SELECT * FROM DATSET.course_item_weight";
//  user profile data
    public String dq_usrProfile = "SELECT * FROM DATSET.usr_contact_dat ";
//   

    public String dq_getAllClasses = "SELECT * FROM datset.conf_classes a right join datset.conf_subclasses b on a.recid= b.recid";
    public String dq_getAllInterestAreas = "Select * from DATSET.conf_interest_areas";

//adding classes 
    public String subClassOrder = null;
    public String classOrder = null;
    public String q_insertClass = null;
    public String q_insertSubClass = null;

    public String dq_insertUserContactInfo(String uid, String ufn, String umn, String uln, String uad, String ucn, String uem, String umo,String uedbck, String eduqual, String edulang, String eduski, String usrcrit) {
        String q_contact = "insert into DATSET.usr_contact_dat (usr_id,usr_first_name,usr_last_name,usr_mid_name,usr_address,usr_city,usr_region,usr_email,usr_mobile,usr_education_background,usr_current_qualification,usr_language,usr_skills,usr_criteria) values ('" + uid + "','" + ufn + "','" + uln + "','" + umn + "','" + uad + "','" + ucn + "',(select distinct(region) from DATSET.uk_countries where city ='" + ucn + "'),'" + uem + "','" + umo + "','" + uedbck + "','" + eduqual + "','" + edulang + "','" + eduski + "','" + usrcrit + "')";

        return q_contact;

    }

  

    public String createUID(String fn, String ln) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
        String uid = null, recOrder = null, fnCode, lnCode;
        String q = "select max(recid) 'rec' from DATSET.usr_contact_dat";
        mysql.openmySQLconnection();
        try {
            recOrder = mysql.executeSQLquery_stringRS(q, 1);
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }
        mysql.closemySQLconnection();
        fnCode = fn.substring(0, 3).toUpperCase();
        lnCode = ln.substring(0, 2).toUpperCase();
        if (null == recOrder) {
            recOrder = "1";
            uid = fnCode + lnCode + "000" + recOrder;
        } else {
            uid = fnCode + lnCode + "000" + recOrder;
        }
        return uid;
    }

    public String getProfileByID (String id )
    {
        String q ="SELECT * FROM DATSET.usr_contact_dat where usr_id= '"+id+"'";
        return q;
    }
    
    public void dq_insertClassConf(String careerName, String className, String subClassName) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException, Exception {

//set an order for the class 
        setOrder(className, subClassName, careerName);

//        Create a serial
        String checkResult_career = null;
        String checkResult_class = null;
        try {
//check if a serial is already assigned to careers
            mysql.openmySQLconnection();
            checkResult_career = mysql.executeSQLquery_stringRS("select career_id from DATSET.conf_classes where career_name = '" + careerName + "'", 1);
            mysql.closemySQLconnection();
// check if a serial is already assigned to class

            mysql.openmySQLconnection();
            checkResult_class = mysql.executeSQLquery_stringRS("select class_id from DATSET.conf_classes where class_name = '" + className + "'", 1);
            mysql.closemySQLconnection();

        } catch (Exception ex) {

        }
        String recOrder = null, careerCode[], classCode[], subClassCode[], careerId, classId, subClassId;
        String q = "select max(recid) 'rec' from DATSET.conf_classes";
        mysql.openmySQLconnection();
        try {
            recOrder = mysql.executeSQLquery_stringRS(q, 1);
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }
        mysql.closemySQLconnection();

//      for the first insertion
        if (recOrder == null) {
            recOrder = "1";
        }

        careerCode = careerName.split(" ");
        classCode = className.split(" ");
        subClassCode = subClassName.split(" ");

//        get if there's a current code for this class/subclass/career
        if (checkResult_career != null) {
            careerId = checkResult_career;
        } else {

            if (careerCode.length == 1) {
                careerId = careerCode[0].substring(0, 3).toUpperCase() + "000" + classOrder;
            } else {
                careerId = careerCode[0].substring(0, 2).toUpperCase() + careerCode[1].substring(0, 2).toUpperCase() + "000" + classOrder;
            }

        }

        if (checkResult_class != null) {
            classId = checkResult_class;
        } else {
            if (classCode.length == 1) {
                classId = classCode[0].substring(0, 3).toUpperCase() + "000" + classOrder;
            } else {
                classId = classCode[0].substring(0, 2).toUpperCase() + classCode[1].substring(0, 2).toUpperCase() + "000" + classOrder;
            }
        }

        String q_Insertcareer = "insert into DATSET.conf_classes (field_of_study,field_id,class_name,class_id) values ('" + careerName + "','" + careerId + "','" + className + "','" + classId + "')";

        q_insertClass = q_Insertcareer;

//        Create a serial
        String checkResult_subClass = null;

//check if a serial is already assigned to careers
        try {
            mysql.openmySQLconnection();
            checkResult_subClass = mysql.executeSQLquery_stringRS("select subclass_id from DATSET.conf_subclasses where subclass_name = '" + subClassName + "'", 1);
            mysql.closemySQLconnection();

            mysql.openmySQLconnection();
            checkResult_class = mysql.executeSQLquery_stringRS("select class_id from DATSET.conf_classes where class_name = '" + className + "'", 1);
            mysql.closemySQLconnection();
        } catch (Exception ex) {

        }

        classCode = className.split(" ");
        subClassCode = subClassName.split(" ");

        if (checkResult_subClass != null) {
            subClassId = checkResult_subClass;
        } else {

            if (subClassCode.length == 1) {
                subClassId = subClassCode[0].substring(0, 3).toUpperCase() + "000" + subClassOrder;
            } else {
                subClassId = subClassCode[0].substring(0, 2).toUpperCase() + subClassCode[1].substring(0, 2).toUpperCase() + "000" + subClassOrder;
            }
        }

        if (checkResult_class != null) {
            classId = checkResult_class;
        } else {
            if (classCode.length == 1) {
                classId = classCode[0].substring(0, 3).toUpperCase() + "000" + classOrder;
            } else {
                classId = classCode[0].substring(0, 2).toUpperCase() + classCode[1].substring(0, 2).toUpperCase() + "000" + classOrder;
            }
        }

        String q_InserSubClass = "insert into DATSET.conf_subclasses (subclass_name,subclass_id,class_id) values ('" + subClassName + "','" + subClassId + "','" + classId + "')";
        q_insertSubClass = q_InserSubClass;

    }

    public void setOrder(String cn, String scn, String can) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {

        String q = "select max(recid) 'rec' from DATSET.conf_subclasses";
        mysql.openmySQLconnection();
        try {
            subClassOrder = mysql.executeSQLquery_stringRS(q, 1);
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }
        mysql.closemySQLconnection();

//      for the first insertion
        if (subClassOrder == null) {
            subClassOrder = "1";
        }

        String q2 = "select count(distinct(class_name)) 'rec' from DATSET.conf_classes";
        mysql.openmySQLconnection();
        try {
            classOrder = mysql.executeSQLquery_stringRS(q2, 1);
        } catch (Exception ex) {
            System.out.println(ex.getCause());
        }
        mysql.closemySQLconnection();

//      for the first insertion
        if (classOrder == null) {
            classOrder = "1";
        }
    }

//    insert interest areas
    public String dq_insertArea(String areaName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InstantiationException, Exception {
        String q, areaId, recOrder;
//        get max records

        mysql.openmySQLconnection();
        recOrder = mysql.executeSQLquery_stringRS("select count(distinct(area_name)) from conf_interest_areas", 1);
        mysql.closemySQLconnection();

//        generate an id 
        areaId = areaName.substring(0, 3).toUpperCase() + "000" + recOrder;
        q = "insert into DATSET.conf_interest_areas (area_name,area_id) values ('" + areaName + "','" + areaId + "')";

        return q;
    }

    //    insert language areas
    public String dq_insertLanguage(String languageName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InstantiationException, Exception {
        String q, languageId, recOrder;
//        get max records

        mysql.openmySQLconnection();
        recOrder = mysql.executeSQLquery_stringRS("select count(distinct(language_name)) from DATSET.conf_languages", 1);
        mysql.closemySQLconnection();

//        generate an id 
        languageId = languageName.substring(0, 2).toUpperCase() + "000" + recOrder;
        q = "insert into DATSET.conf_languages (language_name,language_id) values ('" + languageName + "','" + languageId + "')";

        return q;
    }

    public double getAvailablePercentage() throws Exception {
        double p, t;
        mysql.openmySQLconnection();
        String qureyResult = mysql.executeSQLquery_stringRS("select sum(item_weight) from DATSET.usr_item_weight", 1);

        if (qureyResult == null) {
            qureyResult = "0";
            t = Double.parseDouble(qureyResult);
        } else {
            t = Double.parseDouble(qureyResult);
        }
        mysql.closemySQLconnection();

//        handling first recod is null
        p = 100 - t;
        if (p < 0) {
            p = 0.0;
        }
        return p;
    }

    public double getAvailablePercentage_course() throws Exception {
        double p, t;
        mysql.openmySQLconnection();
        String qureyResult = mysql.executeSQLquery_stringRS("select sum(item_weight) from DATSET.course_item_weight", 1);

        if (qureyResult == null) {
            qureyResult = "0";
            t = Double.parseDouble(qureyResult);
        } else {
            t = Double.parseDouble(qureyResult);
        }
        mysql.closemySQLconnection();

//        handling first recod is null
        p = 100 - t;
        if (p < 0) {
            p = 0.0;
        }
        return p;
    }

    public String dq_insertItem(String itemName, double itemWeight) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InstantiationException, Exception {
        String q;

//        generate an id 
        q = "insert into DATSET.usr_item_weight (item_name,item_weight) values ('" + itemName + "'," + itemWeight + ")";

        return q;
    }

    public String dq_insertItemWeight_course(String itemName, double itemWeight) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InstantiationException, Exception {
        String q;

//        generate an id 
        q = "insert into DATSET.course_item_weight (item_name,item_weight) values ('" + itemName + "'," + itemWeight + ")";

        return q;
    }

    public String dq_updateItem(int recNumber, String itemName, double itemWeight) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InstantiationException, Exception {
        String q;

//        generate an id 
        q = "update DATSET.usr_item_weight set item_name  = '" + itemName + "' ,item_weight =" + itemWeight + "  where recid = " + recNumber + "";

        return q;
    }

    public String dq_updateCourseItem(int recNumber, String itemName, double itemWeight) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, InstantiationException, Exception {
        String q;

//        generate an id 
        q = "update DATSET.course_item_weight set item_name  = '" + itemName + "' ,item_weight =" + itemWeight + "  where recid = " + recNumber + "";

        return q;
    }

    public String dq_insertCourselevel(String levelName, String levelOrder) {
        String q;
        q = "insert into conf_course_lvl (level_name,level_order) values ('" + levelName + "','" + levelOrder + "')";

        return q;
    }

    public String dq_deleteRecord(String tableName, int recid) {
        String q;
        q = "delete from DATSET." + tableName + " where recid =" + recid + "";

        return q;
    }

    public String dq_getCurrentCriteria(String usrid) {
        String q_contact = "SELECT usr_criteria FROM DATSET.usr_edu_dat where usr_id = '" + usrid + "'";

        return q_contact;

    }

    public String dq_updateCurrentCriteria(String usrid, String usrcrit, String sk, String tid, String usubclass) {
        String q_contact = "insert into DATSET.usr_search_history (usr_id,usr_criteria,usr_searchkey,transaction_id,usr_class,usr_region,usr_background) values ('" + usrid + "','" + usrcrit + "','" + sk + "','" + tid + "',(SELECT a.class_name \n"
                + "FROM DATSET.conf_classes a inner join conf_subclasses b \n"
                + "on a.class_id = b.class_id\n"
                + "where b.subclass_name = '" + usubclass + "'),(SELECT usr_region from datset.usr_contact_dat where usr_id='" + usrid + "'),"
                + "(SELECT usr_education_background from datset.usr_edu_dat where usr_id='" + usrid + "'));";

        return q_contact;

    }

    public String dq_getRelatedSearchHistory(String criteria) {
        String q = "select * from datset.courses_search_history a inner join DATSET.course_search_score b\n"
                + "on a.transaction_id = b.transaction_id\n"
                + "where a.transaction_id = '" + criteria + "'\n"
                + "order by b.course_score desc;";
        return q;

    }

    public String dq_insertCourseSearchHistory(String id, String sim, String tid) {
        String q = "insert into DATSET.courses_search_history (course_id,course_similarity,transaction_id)\n"
                + "values ('" + id + "','" + sim + "','" + tid + "')";

        return q;
    }

    public String dq_insertCourseSearchScore(int cid, int cscore, String ccriteria, String cuid) {
        String q = "insert into DATSET.course_search_score (course_id,course_score,course_criteria,user_id)\n"
                + "values\n"
                + "(" + cid + "," + cscore + ",'" + ccriteria + "','" + cuid + "')";
        return q;

    }

//   
    public String dq_COB_getAverageCourseScore(String courseId) {
        String q = " select avg(course_score) \"avg\" from datset.course_search_score where course_id =" + courseId;
        return q;
    }
//   
    
    
    public String dq_NSS_getMinAndMaxScores()
    {
        
        String q = "SELECT min(nss_overall)'min nss',max(nss_overall)'max nss'  FROM DATSET.uni_nss_scoring";
        return q;
    }

    public String dq_CB_getCoursesBySubClass(String subclass) {
        String q = "select * from datset.courses_postgrad where uni_nss is not null and course_fees_uk !='' "
                + " and course_subclass = '" + subclass + "' limit 1000";
        return q;
    }
    
    
    public String dq_UniRank_getMaxRanks() {
        String q = "SELECT max(recid) FROM DATSET.uni_nss_scoring ";
        return q;
    }

    public String dq_COB_getTopSimilarProfiles(String searchKey, String userid, int resultLimit) {
        String q = "select count(usr_searchkey) 'search_key',usr_id \n"
                + "from  datset.usr_search_history\n"
                + "where \n"
                + "usr_searchkey =  '" + searchKey + "'\n"
                + "and usr_region = (select usr_region from datset.usr_search_history where usr_id ='" + userid + "' limit 1  )\n"
                + "and usr_class = (select usr_class from datset.usr_search_history where usr_id ='" + userid + "' limit 1  )\n"
                + "and usr_background = (select usr_background from datset.usr_search_history where usr_id ='" + userid + "' limit 1  )\n"
                + "and usr_id != '" + userid + "'\n"
                + "group by 2 \n"
                + "order by 1 desc\n"
                + "limit " + resultLimit + "";

        return q;
    }

    public String dq_COB_getAverageCoursesScore(String courseidList, String useridList) {
        String q = "select round(avg(course_score)) 'course_score',course_id,user_id \n"
                + "from datset.course_search_score\n"
                + "where\n"
                + "course_id in (" + courseidList + ")\n"
                + "and \n"
                + "user_id in (" + useridList + ") \n"
                + "group by 3,2\n"
                + "order by 1 desc";
        return q;
    }

    public String dq_COB_getAverageCoursesScorePerCourse(String courseidList) {
        String q = "select round(avg(course_score)) 'course_score',course_id \n"
                + "from datset.course_search_score\n"
                + "where\n"
                + "course_id in (" + courseidList + ")\n"
                + "group by 2\n"
                + "order by 1 desc";
        return q;
    }



public String dq_COB_getSimilarProfilesRatings(String userid) {
//        matching users by class,location and user major.
        String q = "select a.course_id,a.course_score,a.user_id,b.usr_class,b.usr_background,b.usr_region\n"
                + "from \n"
                + "course_search_score a \n"
                + "inner join usr_search_history b\n"
                + "on a.user_id = b.usr_id\n"
                + "where \n"
                + "b.usr_region = (select usr_region from usr_search_history where usr_id = '" + userid + "' limit 1)\n"
                + "and\n"
                + "b.usr_class = (select usr_class from usr_search_history where usr_id ='" + userid + "' limit 1)\n"
                + "and\n"
                + "b.usr_background = (select usr_background from usr_search_history where usr_id ='" + userid + "' limit 1) \n"
                + " and b.usr_id != '" + userid + "'\n"
                + "order by 1 asc,2 desc, 3 asc";

        return q;
    }
}
