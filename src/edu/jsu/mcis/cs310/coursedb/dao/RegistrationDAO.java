package edu.jsu.mcis.cs310.coursedb.dao;

import com.github.cliftonlabs.json_simple.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class RegistrationDAO {
        
    private static final String QUERY_CREATE = "INSERT INTO registration (studentid, termid, crn) VALUES (?, ?, ?)";
    private static final String QUERY_DELETE = "DELETE FROM registration WHERE studentid = ? AND termid = ? AND crn = ?";
    private static final String QUERY_DELETE2 = "DELETE FROM registration WHERE studentid = ? AND termid = ?";
    private static final String QUERY_LIST = "SELECT * FROM registration WHERE studentid = ? AND termid = ? ORDER BY crn";
    
    private final DAOFactory daoFactory;
    
    RegistrationDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    //Inserts a new registration record
    public boolean create(int studentid, int termid, int crn) {

            boolean result = false;

            PreparedStatement presta = null;
            ResultSet resset = null;

            try {
                
                // Connect to daoFactory
                Connection connection = daoFactory.getConnection();

                if (connection.isValid(0)) {

                    //call our query, set the variables from given input data
                    presta = connection.prepareStatement(QUERY_CREATE, Statement.RETURN_GENERATED_KEYS);
                    presta.setInt(1, studentid);
                    presta.setInt(2, termid);
                    presta.setInt(3, crn);

                    //execute the query, if it executed, set return value result to true
                    int updateCount = presta.executeUpdate();

                    if (updateCount > 0) {
                        result = true;
                    }

                }

            }
               
            // Handles exceptions during execution 
            catch (Exception e) { e.printStackTrace(); }

            finally {

                if (resset != null) { try { resset.close(); } catch (Exception e) { e.printStackTrace(); } }
                if (presta != null) { try { presta.close(); } catch (Exception e) { e.printStackTrace(); } }

            }

        return result;

    }
    
    
    
    //Deletes a registration from the database and returns true if the registration was deleted successfully.
    public boolean delete(int studentid, int termid, int crn) {
        
    // initialize result and prepared statement variable to false and null respectively
        boolean result = false; 
        PreparedStatement presta = null; 
        try {
            // Connect to daoFactory
            Connection conn = daoFactory.getConnection();        
            if (conn.isValid(0)) {

                // prepare the SQL statement and set the variables from given input data
                presta = conn.prepareStatement(QUERY_DELETE);
                presta.setInt(1, studentid);
                presta.setInt(2, termid);
                presta.setInt(3, crn);

                // execute the query and set result to true if successfully
                int updateCount = presta.executeUpdate();

                if (updateCount > 0) {
                    result = true;
                }
            }

        }
        // Handles exceptions during execution 
        catch (Exception e) { e.printStackTrace(); }

        finally {
            if (presta != null) { 
                try{ 
                    presta.close(); 
                } 
                catch (Exception e) {
                    e.printStackTrace(); 
                } 
            }         
        }

        return result; 

    }
    
    // Deletes a student's record 
    public boolean delete(int studentid, int termid) {

        boolean result = false;

        PreparedStatement presta = null;

        try {
            // Connect to daoFactory
            Connection connection = daoFactory.getConnection();

            // Check if the connection is valid
            if (connection.isValid(0)) {

                // Create the prepared statement
                presta = connection.prepareStatement(QUERY_DELETE2);
                presta.setInt(1, studentid); // Set the first parameter to the student id
                presta.setInt(2, termid); // Set the second parameter to the term id

                // Get the number of rows affected
                int updateCount = presta.executeUpdate();

                // Set the result to true if any row is deleted
                if (updateCount > 0) {
                    result = true;
                }
            }
        }

        // Handles exceptions during execution 
        catch (Exception e) {
            e.printStackTrace(); // print the error if any
        }

        finally {
            // Close the prepared statement
            if (presta != null) { 
                try{ 
                    presta.close(); 
                } 
                catch (Exception e) {
                    e.printStackTrace(); 
                } 
            }         
        }        
        return result;      
    }

    // Gets a list of a student's registrations
    public String list(int studentid, int termid) {

        String result = null;        
        PreparedStatement presta = null;
        ResultSet resset = null;
        ResultSetMetaData rsmd = null;

        JsonArray registrations = new JsonArray(); 

        try {
            // Connect to daoFactory
            Connection connection = daoFactory.getConnection();

            // Check if the connection is valid
            if (connection.isValid(0)) {

                // Create the prepared statement
                presta = connection.prepareStatement(QUERY_LIST);
                presta.setInt(1, studentid); // Set the first parameter to the "studentid"
                presta.setInt(2, termid); // Set the second parameter to the "termid"

                // Execute the query
                resset = presta.executeQuery();
                rsmd = resset.getMetaData();

                // Loop the result set
                while (resset.next()) {
                    JsonObject registration = new JsonObject();
                    registration.put("studentid", resset.getString("studentid"));
                    registration.put("termid", resset.getString("termid"));
                    registration.put("crn", resset.getString("crn"));
                    registrations.add(registration);
                }

                // Serialize the JSON array to a string
                result = Jsoner.serialize(registrations);
            }

        }

        // Handles exceptions during execution 
        catch (Exception e) { 
            e.printStackTrace(); // Print the error if any
        }

        finally {
            // Close the result set 
            if (resset != null) { 
                try { 
                    resset.close(); 
                } 
                catch (Exception e) { 
                    e.printStackTrace(); 
                } 
            }
            if (presta != null) { 
                try { 
                    presta.close(); 
                } 
                catch (Exception e) { 
                    e.printStackTrace(); 
                } 
            }
        }

        return result;
    }  
    
}