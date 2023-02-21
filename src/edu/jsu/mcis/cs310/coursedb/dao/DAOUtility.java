package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.*;
import com.github.cliftonlabs.json_simple.*;

public class DAOUtility {
    
    public static final int TERMID_SP23 = 1;
    
    public static String getResultSetAsJson(ResultSet rs) {
        
        JsonArray records = new JsonArray();
        
        try {
            
            //if there are results add the data for each row to a json object, then add those json objects to the jsonarray
             if (rs != null) {
                ResultSetMetaData metadata = rs.getMetaData();
                int colCount = metadata.getColumnCount();
                
                while (rs.next()) {
                    JsonObject record = new JsonObject();
                    
                    // Loop through the columns and rows and add values values to the JSON object 
                    for (int i = 1; i <= colCount; i++) {
                        String colName = metadata.getColumnLabel(i);
                        Object colValue = rs.getObject(i);
                        record.put(colName, colValue);
                    }
                    
                    records.add(record);
                }
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return Jsoner.serialize(records); //Convert JSON array to a string
        
    }
    
}
