package edu.univ.erp.data;


// making a new class as it combines data from multiple tables

public class CourseSectionAPI {
    private int sectionID;
    private String courseCode;
    private String courseName;
    private String instructor;
    private String schedule;
    private String room;
    private int enrolled;
    private int capacity;

    public CourseSectionAPI(int sectionID, String courseCode, String courseName, String instructor, String schedule, String room, int enrolled, int capacity) {
        this.sectionID = sectionID;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.schedule = schedule;
        this.room = room;
        this.enrolled = enrolled;
        this.capacity = capacity;
    }
    public int getSectionID() {
        return sectionID;
    }
    public String getCourseCode() {
        return courseCode;
    }
    public String getCourseName(){
        return courseName;
    }
    public String getInstructor(){
        return instructor;
    }
    public String getSchedule(){
        return schedule;
    }
    public String getRoom(){
        return room;
    }
    public int getEnrolled(){
        return enrolled;
    }
    public int getCapacity(){
        return capacity;
    }

    public String getSeatsInfo(){
        return enrolled + " / " + capacity;
    }
}
