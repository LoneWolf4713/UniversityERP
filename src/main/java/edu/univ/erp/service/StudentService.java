package edu.univ.erp.service;

import edu.univ.erp.data.CourseSectionStructure;
import edu.univ.erp.util.DBConnection;
import edu.univ.erp.data.GradesStructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class StudentService {

    // Some Helper Functions
    private boolean isMaintenanceModeOn(Connection conn) throws Exception{
        String sqlStmt = "SELECT settingValue FROM settings WHERE settingKey = 'maintenance_on'";
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(sqlStmt);
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                if("true".equalsIgnoreCase(rs.getString("settingValue"))){
                    return true;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    private boolean isAlreadyEnrolled(Connection conn, int studentID, int sectionID) throws Exception{
        String sql = "SELECT enrollmentID FROM enrollments WHERE studentID = ? AND sectionID = ?";
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setInt(1, studentID);
            preparedStmt.setInt(2, sectionID);

            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                return true;
            }


        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    private boolean isSectionFull(Connection conn, int sectionID) throws Exception{
        String sqlStmt = "SELECT COUNT(*) FROM enrollments WHERE sectionID = ?";
        int current = 0;
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(sqlStmt);
            preparedStmt.setInt(1, sectionID);
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                current = rs.getInt(1);
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }

        String sqlStmt2 = "SELECT capacity FROM sections WHERE sectionID = ?";
        int max = 0;
        try{
            PreparedStatement preparedStmt = conn.prepareStatement(sqlStmt);
            preparedStmt.setInt(1, sectionID);
            ResultSet rs = preparedStmt.executeQuery();
            if(rs.next()){
                max = rs.getInt(1);
            }

        }
        catch (Exception e) {
            System.out.println(e);
        }

        if(current >= max){
            return true;
        }
        return false;

    }

    // Function to get all the available sections with courses
    public List<CourseSectionStructure> getAvailableSections(){
        List<CourseSectionStructure> list = new ArrayList<>();

        String sqlStmt = "SELECT s.sectionID, c.courseID,c.courseCode, c.courseName, i.fullName, s.schedule, s.room, s.capacity, (SELECT COUNT(*) FROM enrollments e WHERE e.sectionID = s.sectionID AND status='ENROLLED') as enrolledCount FROM sections s JOIN courses c ON s.courseID = c.courseID JOIN instructors i on s.instructorID = i.userID ORDER BY c.courseCode";
        try{
            Connection conn = DBConnection.getErpConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(sqlStmt);

            ResultSet rs = preparedStmt.executeQuery();
            while(rs.next()){
                list.add(new CourseSectionStructure(
                        rs.getInt("sectionID"),
                        rs.getString("courseCode"),
                        rs.getString("courseName"),
                        rs.getString("fullName"),
                        rs.getString("schedule"),
                        rs.getString("room"),
                        rs.getInt("enrolledCount"),
                        rs.getInt("capacity")

                        ));
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
        return list;
    }

    public void registerStudent(int studentID, int sectionID) throws Exception{

        try{
            Connection conn = DBConnection.getErpConnection();
            if(isMaintenanceModeOn(conn)){
                System.out.println("Maintenance mode is enabled, Registrations Disabled");
            }
            if(isAlreadyEnrolled(conn, studentID, sectionID)){
                System.out.println("Student is already enrolled");
            }
            if(isSectionFull(conn, sectionID)){
                System.out.println("Section is already full");
            }

            String sqlStmt = "INSERT INTO enrollments (studentID, sectionID, status) VALUES (?, ?, 'ENROLLED')";
            try{
                PreparedStatement preparedStmt = conn.prepareStatement(sqlStmt);
                preparedStmt.setInt(1, studentID);
                preparedStmt.setInt(2, sectionID);
                preparedStmt.executeUpdate();
            }
            catch (Exception e) {
                System.out.println("ERROR: " + e);
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
    }

    // Code for 2nd Tab
    public List<CourseSectionStructure> getStudentRegistrations(int studentID){
        List<CourseSectionStructure> list = new ArrayList<>();

        String sqlStmt = """
            SELECT s.sectionID, c.courseCode, c.courseName, i.fullName, s.schedule, s.room,(SELECT COUNT(*) FROM enrollments e WHERE e.sectionID = s.sectionID AND status='ENROLLED') AS enrolledCount, s.capacity
            FROM enrollments en
            JOIN sections s ON en.sectionID = s.sectionID
            JOIN courses c ON s.courseID = c.courseID
            JOIN instructors i ON  s.instructorID = i.userID
            WHERE en.studentID = ? AND en.status='ENROLLED'
            ORDER BY c.courseCode
            """;

        try{
            Connection conn = DBConnection.getErpConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(sqlStmt);
            preparedStmt.setInt(1, studentID);
            ResultSet rs = preparedStmt.executeQuery();

            while(rs.next()){
                list.add(new CourseSectionStructure(
                        rs.getInt("sectionID"),
                        rs.getString("courseCode"),
                        rs.getString("courseName"),
                        rs.getString("fullName"),
                        rs.getString("schedule"),
                        rs.getString("room"),
                        rs.getInt("enrolledCount"),
                        rs.getInt("capacity")
                ));
            }
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e);
        }


    return list;
    }

    public boolean dropSection(int studentID, int sectionID) throws Exception{
        try{
            Connection conn = DBConnection.getErpConnection();
            if(isMaintenanceModeOn(conn)){
                System.out.println("Maintenance mode is enabled, Dropping Sections");
            }

            String sqlStmt = "UPDATE enrollments SET status='DROPPED' WHERE studentID = ? AND sectionID = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(sqlStmt);
            preparedStmt.setInt(1, studentID);
            preparedStmt.setInt(2, sectionID);

            if(preparedStmt.executeUpdate() == 1){
                return true;
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e);
        }
        return false;
    }

    // Code for 3rd Tab
    public List<GradesStructure> getStudentGrades(int studentID){
        List<GradesStructure> list = new ArrayList<>();

        String sqlStmt = """
                SELECT s.sectionID, c.courseCode, c.courseName, i.fullname AS instructor, g.componentName, g.score, g.maxScore
                FROM enrollments en
                JOIN grades g ON en.enrollmentID = g.enrollmentID
                JOIN sections s ON  en.sectionID = s.sectionID
                JOIN courses c ON s.courseID = c.courseID
                JOIN instructors i ON  s.instructorID = i.userID
                WHERE en.studentID = ? AND en.status='ENROLLED'
                ORDER BY c.courseCode, g.componentName
                """;
        try{
            Connection conn = DBConnection.getErpConnection();
            PreparedStatement preparedStmt = conn.prepareStatement(sqlStmt);
            preparedStmt.setInt(1, studentID);

            ResultSet rs = preparedStmt.executeQuery();
            while(rs.next()){
                list.add(new GradesStructure(
                        rs.getInt("sectionID"),
                        rs.getString("courseCode"),
                        rs.getString("courseName"),
                        rs.getString("instructor"),
                        rs.getString("componentName"),
                        rs.getDouble("score"),
                        rs.getDouble("maxScore")
                ));
            }
        }
        catch (Exception ex) {
            System.out.println("ERROR getStudentGrades: " + ex);
        }
        return list;
    }
}


