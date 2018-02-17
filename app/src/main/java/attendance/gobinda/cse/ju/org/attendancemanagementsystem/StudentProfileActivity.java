package attendance.gobinda.cse.ju.org.attendancemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StudentProfileActivity extends AppCompatActivity {

    private ArrayList<Attendance> attendances;
    private Student selectedStudent;
    private Course selectedCourse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_activity);

        Bundle b = getIntent().getExtras();
        selectedStudent = MainActivity.databaseHelper.getOneStudentInformation(b.getString("1"),b.getInt("2"),b.getInt("3"));
        selectedCourse = MainActivity.databaseHelper.getOneCourseDetailsWhere(b.getString("1"),b.getInt("2"));
        attendances = new ArrayList<>();

        showAllTheStudentPresentList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.student_profile_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.idForStudentProfileActivityMenuCloseActivity:
                Intent intent = new Intent(this,CourseActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }


    public void showAllTheStudentPresentList(){

        attendances = MainActivity.databaseHelper.getAllThePresentDateFromDatabase(selectedStudent,selectedCourse);

        final ListAdapter listAdapter = new StudentProfileActivity.CustomAdapter(this);
        ListView listView = (ListView) findViewById(R.id.idForStudentProfileListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                TextView dateTextView = (TextView) view.findViewById(R.id.idForStudentProfileDateShowTextView);
                TextView presentOrNotTextView = (TextView) view.findViewById(R.id.idForStudentProfilePresentOrNotTextView);

                if(presentOrNotTextView.getText().equals("Absent")){
                    attendances.get(position).setPresentOrNot(true);
                    MainActivity.databaseHelper.makeChangeOfStudentAttendance((Attendance) adapterView.getItemAtPosition(position));
                    presentOrNotTextView.setText("Present");
                    presentOrNotTextView.setTextColor(Color.GREEN);

                }else {
                    attendances.get(position).setPresentOrNot(false);
                    MainActivity.databaseHelper.makeChangeOfStudentAttendance((Attendance) adapterView.getItemAtPosition(position));
                    presentOrNotTextView.setText("Absent");
                    presentOrNotTextView.setTextColor(Color.RED);
                }
                Log.d("answer:" , "Value changed for updating present ");
                ((StudentProfileActivity.CustomAdapter)listAdapter).notifyDataSetChanged();
            }
        });


    }

    class CustomAdapter extends ArrayAdapter<Attendance> {

        public CustomAdapter(Context context) {
            super(context, R.layout.main_activity_courselist,attendances);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View customViewNow = layoutInflater.inflate(R.layout.student_profile_activity_presentlist,parent,false);

            TextView dateTextView = (TextView) customViewNow.findViewById(R.id.idForStudentProfileDateShowTextView);
            TextView presentOrNotTextView = (TextView) customViewNow.findViewById(R.id.idForStudentProfilePresentOrNotTextView);

            Attendance nowAttendance = getItem(position);

            dateTextView.setText(nowAttendance.getDate());
            if(nowAttendance.isPresentOrNot()){
                presentOrNotTextView.setText("Present");
                presentOrNotTextView.setTextColor(Color.GREEN);
            }else {
                presentOrNotTextView.setText("Absent");
                presentOrNotTextView.setTextColor(Color.RED);
            }

            return customViewNow;
        }

    }
}
