package edu.jsu.mcis.cs310.coursedb;

import edu.jsu.mcis.cs310.coursedb.dao.*;
import org.junit.*;
import static org.junit.Assert.*;
import com.github.cliftonlabs.json_simple.*;

public class CourseRegistrationDatabaseTest {

    private static final String USERNAME = "jsmith123"; // replace this with your own account name!
    
    private DAOFactory daoFactory;
    private RegistrationDAO registrationDao;
    private SectionDAO sectionDao;
    private int studentid;
    
    private final String s1 = "[{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"21098\"}]";
    private final String s2 = "[{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"20966\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"21016\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"21797\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"21901\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"22170\"}]";
    private final String s3 = "[{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"20966\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"21016\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"21901\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"22170\"}]";
    private final String s4 = "[{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"20966\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"21016\"},{\"studentid\":\"1\",\"termid\":\"1\",\"crn\":\"22170\"}]";
    private final String s5 = "[]";
    
    private final String s6 = "[{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"11:00:00\",\"days\":\"TR\",\"section\":\"001\",\"end\":\"12:30:00\",\"where\":\"Ayers Hall 357\",\"crn\":\"20966\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Shreyashee Ghosh, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"08:45:00\",\"days\":\"MWF\",\"section\":\"002\",\"end\":\"09:45:00\",\"where\":\"Ayers Hall 355\",\"crn\":\"20968\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Shreyashee Ghosh, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"11:15:00\",\"days\":\"MWF\",\"section\":\"003\",\"end\":\"12:15:00\",\"where\":\"Ayers Hall 359\",\"crn\":\"20969\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Brian Christopher Willis, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"004\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"20971\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"12:30:00\",\"days\":\"MWF\",\"section\":\"005\",\"end\":\"13:30:00\",\"where\":\"Ayers Hall 359\",\"crn\":\"20973\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Enrique Navarro, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"12:45:00\",\"days\":\"TR\",\"section\":\"006\",\"end\":\"14:15:00\",\"where\":\"Ayers Hall 357\",\"crn\":\"21003\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Shreyashee Ghosh, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"09:15:00\",\"days\":\"TR\",\"section\":\"007\",\"end\":\"10:45:00\",\"where\":\"Ayers Hall 359\",\"crn\":\"21004\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Thomas D White, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"008\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21005\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"009\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21007\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Brian Christopher Willis, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"010\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21008\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Grant K Greenwood, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"18:15:00\",\"days\":\"MW\",\"section\":\"011\",\"end\":\"19:45:00\",\"where\":\"Ayers Hall 359\",\"crn\":\"21009\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"14:30:00\",\"days\":\"TR\",\"section\":\"012\",\"end\":\"16:00:00\",\"where\":\"Ayers Hall 359\",\"crn\":\"21010\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Enrique Navarro, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"11:00:00\",\"days\":\"TR\",\"section\":\"013\",\"end\":\"12:30:00\",\"where\":\"Ayers Hall 359\",\"crn\":\"21012\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Phillip Bradford Ogden, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"14:30:00\",\"days\":\"TR\",\"section\":\"015\",\"end\":\"16:00:00\",\"where\":\"Ayers Hall 355\",\"crn\":\"21014\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Thomas D White, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"016\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21015\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Brian Christopher Willis, Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"017\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21218\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Cynthia Gunter Jensen\",\"num\":\"201\",\"start\":\"16:30:00\",\"days\":\"MW\",\"section\":\"014\",\"end\":\"18:00:00\",\"where\":\"Ayers Hall 357\",\"crn\":\"21267\",\"subjectid\":\"CS\"}]";
    private final String s7 = "[{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Keith Bradley Foster\",\"num\":\"230\",\"start\":\"10:00:00\",\"days\":\"MWF\",\"section\":\"001\",\"end\":\"11:00:00\",\"where\":\"Ayers Hall 355\",\"crn\":\"21016\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Phillip Bradford Ogden\",\"num\":\"230\",\"start\":\"11:00:00\",\"days\":\"TR\",\"section\":\"002\",\"end\":\"12:30:00\",\"where\":\"Ayers Hall 355\",\"crn\":\"21017\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Arup Kumar Ghosh\",\"num\":\"230\",\"start\":\"13:45:00\",\"days\":\"MWF\",\"section\":\"003\",\"end\":\"14:45:00\",\"where\":\"Ayers Hall 357\",\"crn\":\"21023\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"James Mitchell Jensen\",\"num\":\"230\",\"start\":\"12:30:00\",\"days\":\"MWF\",\"section\":\"004\",\"end\":\"13:30:00\",\"where\":\"Ayers Hall 355\",\"crn\":\"21027\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Keith Bradley Foster\",\"num\":\"230\",\"start\":\"14:30:00\",\"days\":\"TR\",\"section\":\"005\",\"end\":\"16:00:00\",\"where\":\"Ayers Hall 357\",\"crn\":\"21057\",\"subjectid\":\"CS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Keith Bradley Foster\",\"num\":\"230\",\"start\":\"09:15:00\",\"days\":\"TR\",\"section\":\"006\",\"end\":\"10:45:00\",\"where\":\"Ayers Hall 361\",\"crn\":\"22194\",\"subjectid\":\"CS\"}]";
    private final String s8 = "[{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Heather Black McDivitt\",\"num\":\"112\",\"start\":\"08:45:00\",\"days\":\"MWF\",\"section\":\"001\",\"end\":\"09:45:00\",\"where\":\"Ayers Hall 118\",\"crn\":\"21817\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Marcus L Shell\",\"num\":\"112\",\"start\":\"10:00:00\",\"days\":\"MWF\",\"section\":\"002\",\"end\":\"11:00:00\",\"where\":\"Ayers Hall 114\",\"crn\":\"21818\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Marcus L Shell\",\"num\":\"112\",\"start\":\"11:15:00\",\"days\":\"MWF\",\"section\":\"003\",\"end\":\"12:15:00\",\"where\":\"Ayers Hall 114\",\"crn\":\"21819\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Robert L Cochran\",\"num\":\"112\",\"start\":\"12:30:00\",\"days\":\"MWF\",\"section\":\"004\",\"end\":\"13:30:00\",\"where\":\"Ayers Hall 118\",\"crn\":\"21820\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Clint Adam Stanley\",\"num\":\"112\",\"start\":\"13:45:00\",\"days\":\"MW\",\"section\":\"005\",\"end\":\"15:15:00\",\"where\":\"Ayers Hall 218\",\"crn\":\"21821\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Daniel E Smith\",\"num\":\"112\",\"start\":\"16:30:00\",\"days\":\"MW\",\"section\":\"006\",\"end\":\"18:00:00\",\"where\":\"Ayers Hall 214\",\"crn\":\"21822\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Thomas E Leathrum\",\"num\":\"112\",\"start\":\"09:15:00\",\"days\":\"TR\",\"section\":\"007\",\"end\":\"10:45:00\",\"where\":\"Ayers Hall 118\",\"crn\":\"21823\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Darius Williams\",\"num\":\"112\",\"start\":\"09:15:00\",\"days\":\"TR\",\"section\":\"008\",\"end\":\"10:45:00\",\"where\":\"Ayers Hall 220\",\"crn\":\"21824\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Jessica Warren Bentley\",\"num\":\"112\",\"start\":\"11:00:00\",\"days\":\"TR\",\"section\":\"009\",\"end\":\"12:30:00\",\"where\":\"Ayers Hall 118\",\"crn\":\"21825\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Jeffrey J Dodd\",\"num\":\"112\",\"start\":\"12:45:00\",\"days\":\"TR\",\"section\":\"010\",\"end\":\"14:15:00\",\"where\":\"Ayers Hall 220\",\"crn\":\"21826\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Jessica Warren Bentley\",\"num\":\"112\",\"start\":\"12:45:00\",\"days\":\"TR\",\"section\":\"011\",\"end\":\"14:15:00\",\"where\":\"Ayers Hall 218\",\"crn\":\"21827\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Edwin H Smith\",\"num\":\"112\",\"start\":\"14:30:00\",\"days\":\"TR\",\"section\":\"012\",\"end\":\"16:00:00\",\"where\":\"Ayers Hall 214\",\"crn\":\"21828\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Darius Williams\",\"num\":\"112\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"013\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21829\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Sharon Padgett\",\"num\":\"112\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"014\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21830\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Sharon Padgett\",\"num\":\"112\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"015\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21832\",\"subjectid\":\"MS\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Taffy J Cruse\",\"num\":\"112\",\"start\":\"00:00:00\",\"days\":\"TBA\",\"section\":\"200\",\"end\":\"00:00:00\",\"where\":\"Oxford High School\",\"crn\":\"22403\",\"subjectid\":\"MS\"}]";
    private final String s9 = "[{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Richard Allan Dobbs\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"001\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21312\",\"subjectid\":\"HY\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Jennifer L Gross\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"002\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21319\",\"subjectid\":\"HY\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Roger Thomas Zeimet\",\"num\":\"201\",\"start\":\"08:45:00\",\"days\":\"MWF\",\"section\":\"003\",\"end\":\"09:45:00\",\"where\":\"Stone Center 326\",\"crn\":\"21414\",\"subjectid\":\"HY\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Roger Thomas Zeimet\",\"num\":\"201\",\"start\":\"10:00:00\",\"days\":\"MWF\",\"section\":\"004\",\"end\":\"11:00:00\",\"where\":\"Stone Center 326\",\"crn\":\"21419\",\"subjectid\":\"HY\"},{\"termid\":\"1\",\"scheduletypeid\":\"LEC\",\"instructor\":\"Harvey Lynn Edwards\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"TBA\",\"section\":\"201\",\"end\":\"00:00:00\",\"where\":\"Lincoln High School\",\"crn\":\"21684\",\"subjectid\":\"HY\"},{\"termid\":\"1\",\"scheduletypeid\":\"ONL\",\"instructor\":\"Brandon Gilliland\",\"num\":\"201\",\"start\":\"00:00:00\",\"days\":\"\",\"section\":\"202\",\"end\":\"00:00:00\",\"where\":\"Online\",\"crn\":\"21797\",\"subjectid\":\"HY\"}]";
    private final String s10 = "[]";
    
    @Before
    public void setUp() {
        
        daoFactory = new DAOFactory("coursedb");
        
        registrationDao = daoFactory.getRegistrationDAO();
        sectionDao = daoFactory.getSectionDAO();
        
        studentid = daoFactory.getStudentDAO().find(USERNAME);
        
    }
    
    @Test
    public void testRegisterSingle() {
        
        try {
        
            JsonArray r1 = (JsonArray)Jsoner.deserialize(s1);

            // clear schedule (withdraw all classes)

            registrationDao.delete(studentid, DAOUtility.TERMID_SP23);

            // register for one course

            boolean result = registrationDao.create(studentid, DAOUtility.TERMID_SP23, 21098);

            // compare number of updated records

            assertTrue(result);

            // compare schedule

            assertEquals(r1, (JsonArray)Jsoner.deserialize(registrationDao.list(studentid, DAOUtility.TERMID_SP23)));
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }
    
    @Test
    public void testRegisterMultiple() {
        
        try {
        
            JsonArray r2 = (JsonArray)Jsoner.deserialize(s2);

            int[] crn = {20966, 21016, 21901, 22170, 21797};

            // clear schedule (withdraw all classes)

            registrationDao.delete(studentid, DAOUtility.TERMID_SP23);

            // register for multiple courses

            for (int i = 0; i < crn.length; ++i) {

                // add next course

                boolean result = registrationDao.create(studentid, DAOUtility.TERMID_SP23, crn[i]);

                // compare number of updated records

                assertTrue(result);

            }

            // compare schedule

            JsonArray t1 = (JsonArray)Jsoner.deserialize(registrationDao.list(studentid, DAOUtility.TERMID_SP23));
            assertEquals(5, t1.size());
            assertEquals(r2, t1);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }
    
    @Test
    public void testDropSingle() {
        
        try {
        
            JsonArray r3 = (JsonArray)Jsoner.deserialize(s3);

            int[] crn = {20966, 21016, 21901, 22170, 21797};

            // clear schedule (withdraw all classes)

            registrationDao.delete(studentid, DAOUtility.TERMID_SP23);

            // register for multiple courses

            for (int i = 0; i < crn.length; ++i) {

                // add next course

                boolean result = registrationDao.create(studentid, DAOUtility.TERMID_SP23, crn[i]);

                // compare number of updated records

                assertTrue(result);

            }
            
            // drop individual course
            
            boolean result = registrationDao.delete(studentid, DAOUtility.TERMID_SP23, 21797);
            
            assertTrue(result);

            // compare schedule

            JsonArray t1 = (JsonArray)Jsoner.deserialize(registrationDao.list(studentid, DAOUtility.TERMID_SP23));
            assertEquals(4, t1.size());
            assertEquals(r3, t1);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }
    
    @Test
    public void testDropMultiple() {
        
        try {
        
            JsonArray r4 = (JsonArray)Jsoner.deserialize(s4);

            int[] crn = {20966, 21016, 21901, 22170, 21797};

            // clear schedule (withdraw all classes)

            registrationDao.delete(studentid, DAOUtility.TERMID_SP23);

            // register for multiple courses

            for (int i = 0; i < crn.length; ++i) {

                // add next course

                boolean result = registrationDao.create(studentid, DAOUtility.TERMID_SP23, crn[i]);

                // compare number of updated records

                assertTrue(result);

            }
            
            // drop multiple courses
            
            boolean result = registrationDao.delete(studentid, DAOUtility.TERMID_SP23, 21797);
            
            assertTrue(result);
            
            result = registrationDao.delete(studentid, DAOUtility.TERMID_SP23, 21901);
            
            assertTrue(result);

            // compare schedule

            JsonArray t1 = (JsonArray)Jsoner.deserialize(registrationDao.list(studentid, DAOUtility.TERMID_SP23));
            assertEquals(3, t1.size());
            assertEquals(r4, t1);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }
    
    @Test
    public void testWithdraw() {
        
        try {
        
            JsonArray r5 = (JsonArray)Jsoner.deserialize(s5);

            int[] crn = {20966, 21016, 21901, 22170, 21797};

            // clear schedule (withdraw all classes)

            registrationDao.delete(studentid, DAOUtility.TERMID_SP23);

            // register for multiple courses

            for (int i = 0; i < crn.length; ++i) {

                // add next course

                boolean result = registrationDao.create(studentid, DAOUtility.TERMID_SP23, crn[i]);

                // compare number of updated records

                assertTrue(result);

            }

            // clear schedule (withdraw all classes)

            boolean result = registrationDao.delete(studentid, DAOUtility.TERMID_SP23);

            // check withdrawal

            assertTrue(result);

            // compare schedule

            JsonArray t1 = (JsonArray)Jsoner.deserialize(registrationDao.list(studentid, DAOUtility.TERMID_SP23));
            assertEquals(0, t1.size());
            assertEquals(r5, t1);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }
    
    @Test
    public void testGetSections() {
        
        try {
        
            JsonArray r6 = (JsonArray)Jsoner.deserialize(s6);
            JsonArray r7 = (JsonArray)Jsoner.deserialize(s7);
            JsonArray r8 = (JsonArray)Jsoner.deserialize(s8);
            JsonArray r9 = (JsonArray)Jsoner.deserialize(s9);
            JsonArray r10 = (JsonArray)Jsoner.deserialize(s10);

            // get sections; compare number of registered sections and JSON data

            // CS 201

            JsonArray t1 = (JsonArray)Jsoner.deserialize(sectionDao.find(1, "CS", "201"));
            assertEquals(17, t1.size());
            assertEquals(r6, t1);

            // CS 230

            JsonArray t2 = (JsonArray)Jsoner.deserialize(sectionDao.find(1, "CS", "230"));
            assertEquals(6, t2.size());
            assertEquals(r7, t2);

            // MS 112

            JsonArray t3 = (JsonArray)Jsoner.deserialize(sectionDao.find(1, "MS", "112"));
            assertEquals(16, t3.size());
            assertEquals(r8, t3);

            // HY 201

            JsonArray t4 = (JsonArray)Jsoner.deserialize(sectionDao.find(1, "HY", "201"));
            assertEquals(6, t4.size());
            assertEquals(r9, t4);

            // PHS 212 (should be empty)

            JsonArray t5 = (JsonArray)Jsoner.deserialize(sectionDao.find(1, "PHS", "212"));
            assertEquals(0, t5.size());
            assertEquals(r10, t5);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }
    
}