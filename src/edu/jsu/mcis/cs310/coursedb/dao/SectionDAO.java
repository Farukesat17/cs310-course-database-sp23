package edu.jsu.mcis.cs310.coursedb.dao;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class SectionDAO {
    
    // The SQL query to retrieve a section with a given termid, subjectid, and num
    private static final String QUERY_FIND = "SELECT * FROM section WHERE termid = ? AND subjectid = ? AND num = ? ORDER BY crn";
    
    private final DAOFactory daoFactory;
    
    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public String find(int termid, String subjectid, String num) {
        
        String result = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                // Create a prepared statement for the query
                ps = conn.prepareStatement(QUERY_FIND);
                
                // Set the parameters in the query
                ps.setInt(1, termid);
                ps.setString(2, subjectid);
                ps.setString(3, num);
                
                // Execute the query
                rs = ps.executeQuery();
                
                // Get the metadata of the result set
                rsmd = rs.getMetaData();
                
                // Create a JSON array to hold the rows of the result set
                JsonArray sections = new JsonArray();
                
                // Iterate over the rows of the result set
                while (rs.next()) {
                    
                    // Create a JSON object to hold the row data
                    JsonObject section = new JsonObject();
                    
                    // Add the columns of the row to the JSON object
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        section.put(rsmd.getColumnName(i), rs.getString(i));
                    }
                    
                    // Add the JSON object to the JSON array
                    sections.add(section);
                }
                
                // Serialize the JSON array to a string
                result = Jsoner.serialize(sections);
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            // Close the result set, statement, and connection
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
}
