package attendance.gobinda.cse.ju.org.attendancemanagementsystem;

/**
 * Created by gobinda22 on 2/9/2018.
 */

public class Attendance {

    private int id;
    private String date;
    private String courseSession;
    private int courseCode;
    private int studentRoll;
    private boolean presentOrNot;

    public Attendance(int id, String date, String courseSession, int courseCode, int studentRoll, boolean presentOrNot) {
        this.id = id;
        this.date = date;
        this.courseSession = courseSession;
        this.courseCode = courseCode;
        this.studentRoll = studentRoll;
        this.presentOrNot = presentOrNot;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCourseSession() {
        return courseSession;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public int getStudentRoll() {
        return studentRoll;
    }

    public boolean isPresentOrNot() {
        return presentOrNot;
    }

    public void setPresentOrNot(boolean presentOrNot) {
        this.presentOrNot = presentOrNot;
    }
}

