package attendance.gobinda.cse.ju.org.attendancemanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by gobinda22 on 2/7/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DATABASE";

    private static final String CTABLE_NAME= "COURSE";
    private static final String CCOL1 = "SESSION";
    private static final String CCOL2 = "NAME";
    private static final String CCOL3 = "CODE";
    private static final String CCOL4 = "SEMESTERIMAGE";
    private static final String CCOL5 = "TOTALCLASS";

    private static final String STABLE_NAME= "STUDENT";
    private static final String SCOL1 = "SESSION";
    private static final String SCOL2 = "COURSECODE";
    private static final String SCOL3 = "NAME";
    private static final String SCOL4 = "ROLL";
    private static final String SCOL5 = "TOTALPRESENT";
    private static final String SCOL6 = "IMAGE";

    private static final String ATABLE_NAME= "ATTENDANCE";
    private static final String ACOL1 = "ID";
    private static final String ACOL2 = "DATE";
    private static final String ACOL3 = "SESSIOIN";
    private static final String ACOL4 = "COURSECODE";
    private static final String ACOL5 = "STUDENTROLL";
    private static final String ACOL6 = "PRESENTORNOT";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try{
            StringBuilder sbForCourse = new StringBuilder();
            sbForCourse.append("CREATE TABLE " + CTABLE_NAME + "(");
            sbForCourse.append(CCOL1 +" TEXT NOT NULL,");
            sbForCourse.append(CCOL2 + " TEXT NOT NULL,");
            sbForCourse.append(CCOL3 + " INTEGER NOT NULL,");
            sbForCourse.append(CCOL4 + " BLOB NOT NULL,");
            sbForCourse.append(CCOL5 + " INTEGER NOT NULL,");
            sbForCourse.append("PRIMARY KEY ("+CCOL1+" , "+CCOL3+"))");
            db.execSQL(sbForCourse.toString());
            Log.d("table creation","Course table created");

            StringBuilder sbForStudent = new StringBuilder();
            sbForStudent.append("CREATE TABLE " + STABLE_NAME + "(");
            sbForStudent.append(SCOL1 +" TEXT NOT NULL,");
            sbForStudent.append(SCOL2 + " INTEGER NOT NULL,");
            sbForStudent.append(SCOL3 + " TEXT NOT NULL,");
            sbForStudent.append(SCOL4 + " INTEGER NOT NULL,");
            sbForStudent.append(SCOL5 + " INTEGER NOT NULL,");
            sbForStudent.append(SCOL6 + " BLOB NOT NULL,");
            sbForStudent.append("PRIMARY KEY (" + SCOL1 + " , " + SCOL2 + "," + SCOL4 + "))");
            db.execSQL(sbForStudent.toString());
            Log.d("table creation","student table created");

            StringBuilder sbForAttendance = new StringBuilder();
            sbForAttendance.append("CREATE TABLE " + ATABLE_NAME + "(");
            sbForAttendance.append(ACOL1 +" INTEGER NOT NULL,");
            sbForAttendance.append(ACOL2 +" TEXT NOT NULL,");
            sbForAttendance.append(ACOL3 + " TEXT NOT NULL,");
            sbForAttendance.append(ACOL4 + " INTEGER NOT NULL,");
            sbForAttendance.append(ACOL5 + " INTEGER NOT NULL,");
            sbForAttendance.append(ACOL6 + " BOOLEAN NOT NULL,");
            sbForAttendance.append("PRIMARY KEY (" + ACOL1 + "))");

            db.execSQL(sbForAttendance.toString());
            Log.d("table creation","attendance table created");

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("on create method","Course table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CTABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ATABLE_NAME);

        onCreate(db);
    }

    public ArrayList<Student> getAllTheStudentWhereCourseId(Course course){
        ArrayList<Student> students = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor rs = db.query(STABLE_NAME, new String[] { SCOL1,SCOL2, SCOL3,SCOL4,SCOL5,SCOL6}, SCOL1 + " = ? and "+SCOL2+" = ?",
                    new String[] { course.getSession(), String.valueOf(course.getCode()) }, null, null, null, null);
            if (rs.moveToFirst()) {
                do {
                    Student st = new Student(rs.getString(2),rs.getInt(3),rs.getString(0),rs.getInt(4), rs.getBlob(5),rs.getInt(1));
                    students.add(st);
                } while (rs.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return students;
    }

    public boolean addStudent(Student s){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SCOL1, s.getSession());
            values.put(SCOL2, s.getCourseCode());
            values.put(SCOL3, s.getName());
            values.put(SCOL4, s.getRoll());
            values.put(SCOL5, s.getTotalPresent());
            values.put(SCOL6, s.getImage());
            db.insert(STABLE_NAME, null, values);
            db.close();
        }catch (Exception e){
            return false;
        }
        return true;
    }


    public Student getOneStudentInformation(String session, int courseCode,int studentRoll){

        Student student = null;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor rs = db.query(STABLE_NAME, new String[] { SCOL1,SCOL2, SCOL3,SCOL4,SCOL5,SCOL6}, SCOL1 + "=? and "+SCOL2+"=? and "+SCOL4+"=?",
                    new String[] { session, String.valueOf(courseCode),String.valueOf(studentRoll) }, null, null, null, null);
            rs.moveToFirst();
            student = new Student(rs.getString(2),rs.getInt(3),rs.getString(0),rs.getInt(4),
                    rs.getBlob(5),rs.getInt(1));
        }catch (Exception e){
            e.printStackTrace();
        }
        return student;
    }


















    public boolean addCourse(Course course){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CCOL1, course.getSession());
            values.put(CCOL2, course.getName());
            values.put(CCOL3, course.getCode());
            values.put(CCOL4, course.getSemesterImage());
            values.put(CCOL5, 0);
            db.insert(CTABLE_NAME, null, values);
            db.close();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean deleteCourse(Course course){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            int ans = db.delete(CTABLE_NAME, CCOL1+"=? and "+CCOL3+"=?" ,new String[]{course.getSession(),String.valueOf(course.getCode())});
            return (ans == 0)? false:true;
        }catch (Exception e){
            return false;
        }
    }

    public Course getOneCourseDetailsWhere(String courseSession, int courseCode){

        Course course = null;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor rs = db.query(CTABLE_NAME, new String[] { CCOL1,CCOL2, CCOL3,CCOL4,CCOL5}, CCOL1 + " = ? and "+CCOL3+" = ? ",
                    new String[] { courseSession, String.valueOf(courseCode) }, null, null, null, null);
            rs.moveToFirst();
            course = new Course(rs.getString(0),rs.getString(1),rs.getInt(2),
                    rs.getBlob(3),rs.getInt(4));
        }catch (Exception e){
            e.printStackTrace();
        }
        return course;
    }

    public ArrayList<Course> getAllTheCourseFromDatabase(){
        ArrayList<Course> courseList = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor rs = db.rawQuery("SELECT  * FROM " + CTABLE_NAME, null);
            if (rs.moveToFirst()) {
                do {
                    Course course = new Course(rs.getString(0),rs.getString(1),rs.getInt(2),
                            rs.getBlob(3),rs.getInt(4));
                    courseList.add(course);
                } while (rs.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("course length",String.valueOf(courseList.size()));
        }
        return courseList;
    }

    public String loadCourseFromThisFileLocation(Context context, String nowDirectory) {

        File selectedFolder  = new File(nowDirectory);
        if(selectedFolder.isDirectory() == false){
            return "Selected Path is not a Folder!!";
        }
        File[] a = selectedFolder.listFiles();
        if(a==null){
            return "Problem occurs while accessing the file!!";
        }

        String regex = ";";
        String selectedFolderName = selectedFolder.getName().trim();
        String[] splitingCourseDetails = selectedFolderName.split(regex);
        if(splitingCourseDetails == null){
            return "Course Details can not load from file! 1 !";
        }
        if(splitingCourseDetails.length != 4){
            Log.d("problem is here \n\n\n\n",String.valueOf(splitingCourseDetails.length));
            return "Course Details can not load from file! 2 !"+String.valueOf(splitingCourseDetails.length);
        }

        String cSession = splitingCourseDetails[0];
        String cName = splitingCourseDetails[1];
        int cCode = Integer.parseInt(splitingCourseDetails[2]);
        byte[] cImage = null;
        try{
            Bitmap bitmap= getImageForCourseSemesterNumber(context,splitingCourseDetails[3]);
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            cImage=bos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
            return "Error with Semester Image!!";
        }

        Course haveToAdd = new Course(cSession,cName,cCode,cImage,0);
        boolean tof = MainActivity.databaseHelper.addCourse(haveToAdd);
        if(tof == false){
            return "Can not add Semester!\nCheck The File Name!";
        }

        ArrayList<File> files = new ArrayList<>();
        for(int i=0;i<a.length;i++){
            if(a[i].isFile()){
                if(a[i].getAbsolutePath().endsWith(".jpg")){
                    try{
                        String fileName = a[i].getName();
                        String[] spliteAns = fileName.split(regex);
                        if(spliteAns != null &&spliteAns.length == 2){
                            String sName = spliteAns[0];
                            int sRoll = Integer.parseInt(spliteAns[1].substring(0,spliteAns[1].length()-4));
                            byte[] sImage = null;
                            try{
                                Bitmap bitmap = BitmapFactory.decodeFile(a[i].getAbsolutePath());
                                ByteArrayOutputStream boss=new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boss);
                                sImage=boss.toByteArray();
                                //FileInputStream fis = new FileInputStream(a[i].getAbsolutePath());
                                //byte[] sImage = new byte[fis.available()];
                                //fis.read(sImage);
                                Student student = new Student(sName,sRoll,cSession,0,sImage,cCode);
                                MainActivity.databaseHelper.addStudent(student);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Log.d("problem","loading file"+a[i].getAbsolutePath());
                }
            }
        }
        return "";
    }

    private Bitmap getImageForCourseSemesterNumber(Context context, String splitingCourseDetail) {
        if(splitingCourseDetail.equals("s11"))
            return   BitmapFactory.decodeResource(context.getResources(), R.drawable.s11);
        if(splitingCourseDetail.equals("s12"))
            return   BitmapFactory.decodeResource(context.getResources(), R.drawable.s12);
        if(splitingCourseDetail.equals("s21"))
            return   BitmapFactory.decodeResource(context.getResources(), R.drawable.s21);
        if(splitingCourseDetail.equals("s22"))
            return   BitmapFactory.decodeResource(context.getResources(), R.drawable.s22);
        if(splitingCourseDetail.equals("s31"))
            return   BitmapFactory.decodeResource(context.getResources(), R.drawable.s31);
        if(splitingCourseDetail.equals("s32"))
            return   BitmapFactory.decodeResource(context.getResources(), R.drawable.s32);
        if(splitingCourseDetail.equals("s41"))
            return   BitmapFactory.decodeResource(context.getResources(), R.drawable.s41);
        if(splitingCourseDetail.equals("s42"))
            return   BitmapFactory.decodeResource(context.getResources(), R.drawable.s42);
        return   BitmapFactory.decodeResource(context.getResources(), R.drawable.a);
    }

    public void giveAttendanceForThoseStudent(Course c, ArrayList<Student> selectedCourseStudentData) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.d("date value:","["+date+"]");
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CCOL1, c.getSession());
            values.put(CCOL2, c.getName());
            values.put(CCOL3, c.getCode());
            values.put(CCOL4, c.getSemesterImage());
            values.put(CCOL5, c.getTotalClass()+1);
            db.update(CTABLE_NAME,values,CCOL1+"=? and "+CCOL3+"=?",new String[]{c.getSession(),String.valueOf(c.getCode())});
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        for(Student s: selectedCourseStudentData){
            giveAttendanceFoStudent(s,c,date);
        }
    }

    public void giveAttendanceFoStudent(Student s, Course c,String date){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ACOL2, date);
            values.put(ACOL3, c.getSession());
            values.put(ACOL4, c.getCode());
            values.put(ACOL5, s.getRoll());
            values.put(ACOL6, s.isPresentToday());
            db.insert(ATABLE_NAME, null, values);
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        if(s.isPresentToday() == false){
            return;
        }

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SCOL1, s.getSession());
            values.put(SCOL2, s.getCourseCode());
            values.put(SCOL3, s.getName());
            values.put(SCOL4, s.getRoll());
            values.put(SCOL5, s.getTotalPresent()+1);
            values.put(SCOL6, s.getImage());
            db.update(STABLE_NAME,values,SCOL1+"=? and "+SCOL2+"=? and "+SCOL4+"=?",new String[]{s.getSession(),String.valueOf(s.getCourseCode()),String.valueOf(s.getRoll())});
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    public ArrayList<Attendance> getAllThePresentDateFromDatabase(Student selectedStudent,Course course) {

        ArrayList<Attendance> attendances = new ArrayList<>();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor rs = db.query(ATABLE_NAME, new String[] { ACOL1,ACOL2, ACOL3,ACOL4,ACOL5,ACOL6}, ACOL3 + "=? and "+ACOL4+"=? and "+ACOL5+"=?",
                    new String[] { course.getSession(), String.valueOf(course.getCode()) , String.valueOf(selectedStudent.getRoll())}, null, null, null, null);
            if (rs.moveToFirst()) {
                do {
                    Attendance at = new Attendance(rs.getInt(0),rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4), Boolean.parseBoolean(rs.getString(5)));
                    attendances.add(at);
                } while (rs.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return attendances;
    }

    public void makeChangeOfStudentAttendance(Attendance a){

        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ACOL1, a.getId());
            values.put(ACOL2, a.getDate());
            values.put(ACOL3, a.getCourseSession());
            values.put(ACOL4, a.getCourseCode());
            values.put(ACOL5, a.getStudentRoll());
            values.put(ACOL6, a.isPresentOrNot());
            db.update(ATABLE_NAME,values,ACOL1+"=?",new String[]{String.valueOf(a.getId())});
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        Student s = getOneStudentInformation(a.getCourseSession(),a.getCourseCode(),a.getStudentRoll());

        if(a.isPresentOrNot()){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SCOL1, s.getSession());
            values.put(SCOL2, s.getCourseCode());
            values.put(SCOL3, s.getName());
            values.put(SCOL4, s.getRoll());
            values.put(SCOL5, s.getTotalPresent()+1);
            values.put(SCOL6, s.getImage());
            db.update(STABLE_NAME,values,SCOL1+"=? and "+SCOL2+"=? and "+SCOL4+"=?",new String[]{s.getSession(),String.valueOf(s.getCourseCode()),String.valueOf(s.getRoll())});
            db.close();
        }else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SCOL1, s.getSession());
            values.put(SCOL2, s.getCourseCode());
            values.put(SCOL3, s.getName());
            values.put(SCOL4, s.getRoll());
            values.put(SCOL5, s.getTotalPresent()-1);
            values.put(SCOL6, s.getImage());
            db.update(STABLE_NAME,values,SCOL1+"=? and "+SCOL2+"=? and "+SCOL4+"=?",new String[]{s.getSession(),String.valueOf(s.getCourseCode()),String.valueOf(s.getRoll())});
            db.close();
        }
    }
}
