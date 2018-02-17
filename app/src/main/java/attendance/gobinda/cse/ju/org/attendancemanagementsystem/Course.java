package attendance.gobinda.cse.ju.org.attendancemanagementsystem;

/**
 * Created by gobinda22 on 2/7/2018.
 */

public class Course {

    private String session;
    private int code;
    private String name;
    private int totalClass;
    private byte[] semesterImage;

    public Course(String session,String name, int code, byte[] semesterImage, int totalClass ) {
        this.session = session;
        this.code = code;
        this.name = name;
        this.totalClass = totalClass;
        this.semesterImage = semesterImage;
    }

    public String getSession() {
        return session;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getTotalClass() {
        return totalClass;
    }

    public byte[] getSemesterImage() {
        return semesterImage;
    }
}
