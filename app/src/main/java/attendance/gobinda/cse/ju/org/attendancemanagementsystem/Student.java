package attendance.gobinda.cse.ju.org.attendancemanagementsystem;

/**
 * Created by gobinda22 on 2/7/2018.
 */

public class Student {

    private String name;
    private int roll;
    private String session;
    private int totalPresent;
    private byte[] image;
    private int courseCode;
    private boolean presentToday;

    public Student(String name, int roll, String session, int totalPresent, byte[] image,int courseCode) {
        this.name = name;
        this.roll = roll;
        this.session = session;
        this.totalPresent = totalPresent;
        this.image = image;
        this.courseCode=courseCode;
    }

    public String getName() {
        return name;
    }

    public int getRoll() {
        return roll;
    }

    public String getSession() {
        return session;
    }

    public int getTotalPresent() {
        return totalPresent;
    }

    public byte[] getImage() {
        return image;
    }

    public int getCourseCode() { return courseCode; }

    public boolean isPresentToday() {
        return presentToday;
    }

    public void setPresentToday(boolean presentToday) {
        this.presentToday = presentToday;
    }
}
