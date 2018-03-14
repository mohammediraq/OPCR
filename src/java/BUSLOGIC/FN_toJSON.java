/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUSLOGIC;

import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author AhmedShalaby
 */
public class FN_toJSON {
        public static JSONArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_rows = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_rows; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;

    }
        
        
        
        public static JSONArray printJson( String label,String message)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
     
            JSONObject obj = new JSONObject();
            
                obj.put(label,message);
            
            jsonArray.put(obj);
        
        return jsonArray;

    }

}
