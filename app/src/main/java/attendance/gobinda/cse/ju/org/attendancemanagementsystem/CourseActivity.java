package attendance.gobinda.cse.ju.org.attendancemanagementsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private Course selectedCourse;
    private ArrayList<Student> selectedCourseStudentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_activity);

        selectedCourseStudentData = new ArrayList<>();
        selectedCourse = MainActivity.databaseHelper.getOneCourseDetailsWhere(getIntent().getExtras().getString("1"),getIntent().getExtras().getInt("2"));
        addToolBarToThisActivity(this);
        showAllTheStudentPresentInThisCourse();
    }

    private void addToolBarToThisActivity(CourseActivity courseActivity) {
        getSupportActionBar().setTitle(  (selectedCourse.getName().length()<25)? selectedCourse.getName():selectedCourse.getName().substring(0,23)+".."    );
        getSupportActionBar().setSubtitle("Session:"+selectedCourse.getSession()+"   Code:"+String.valueOf(selectedCourse.getCode()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.course_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.course_activity_menu_giveAttendance:
                int presentToday = 0;
                for(Student s: selectedCourseStudentData){
                    if(s.isPresentToday()){
                        presentToday++;
                    }
                }
                AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
                a_builder.setMessage("Would You Like To Update Attendance Table Now?");
                a_builder.setCancelable(false);
                a_builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                a_builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.databaseHelper.giveAttendanceForThoseStudent(selectedCourse,selectedCourseStudentData);
                        Toast.makeText(CourseActivity.this,"Successfully Updated!!",Toast.LENGTH_LONG);
                    }
                });
                AlertDialog alert = a_builder.create();
                alert.setTitle(String.valueOf(presentToday)+" Student Present Today!");
                alert.show();
                break;
        }
        return false;
    }

    public void showAllTheStudentPresentInThisCourse(){

        selectedCourseStudentData = MainActivity.databaseHelper.getAllTheStudentWhereCourseId(selectedCourse);

        final ListAdapter listAdapter = new CustomAdapter(this);
        final ListView listView = (ListView) findViewById(R.id.idForCourseActivityStudentListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView studentTodayPresentTextView = (TextView) view.findViewById(R.id.idForStudentTodayPresentTextView);
                if(studentTodayPresentTextView.getText().equals("+1")){
                    studentTodayPresentTextView.setText("");
                    selectedCourseStudentData.get(position).setPresentToday(false);
                }else {
                    studentTodayPresentTextView.setText("+1");
                    selectedCourseStudentData.get(position).setPresentToday(true);
                }
                Log.d("answer:" , "YES come here");
                ((CustomAdapter)listAdapter).notifyDataSetChanged();

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                Intent intent = new Intent(CourseActivity.this,StudentProfileActivity.class);
                intent.putExtra("1",selectedCourse.getSession());
                intent.putExtra("2",selectedCourse.getCode());
                intent.putExtra("3",((Student)adapterView.getItemAtPosition(position)).getRoll());
                startActivity(intent);
                finish();
                return true;
            }
        });

    }

    class CustomAdapter extends ArrayAdapter<Student> {

        public CustomAdapter(Context context) {
            super(context, R.layout.course_activity_studentlist,selectedCourseStudentData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View customViewNow = layoutInflater.inflate(R.layout.course_activity_studentlist,parent,false);

            TextView studentSessionTextView = (TextView) customViewNow.findViewById(R.id.idForStudentSessionTextView);
            TextView studentNameTextView = (TextView) customViewNow.findViewById(R.id.idForStudentNameTextView);
            TextView studentRollTextView = (TextView) customViewNow.findViewById(R.id.idForstudentRollTextView);
            TextView studentTotalPresentTextView = (TextView) customViewNow.findViewById(R.id.idForStudentTotalPresentTextView);
            ImageView studentImageView = (ImageView) customViewNow.findViewById(R.id.idForStudentPictureImageView);
            TextView studentTodayPresent = (TextView) customViewNow.findViewById(R.id.idForStudentTodayPresentTextView);

            Student student = getItem(position);

            studentSessionTextView.setText(student.getSession());
            studentNameTextView.setText(student.getName());
            studentRollTextView.setText(String.valueOf(student.getRoll()));
            studentTotalPresentTextView.setText("Attendance : "+String.valueOf(student.getTotalPresent())+"/"+String.valueOf(selectedCourse.getTotalClass()));
            byte[] im = student.getImage();
            studentImageView.setImageBitmap(BitmapFactory.decodeByteArray(im,0,im.length));
            if(student.isPresentToday()) {
                studentTodayPresent.setText("+1");
            }else {
                studentTodayPresent.setText("");
            }
            return customViewNow;
        }

    }









}
