package attendance.gobinda.cse.ju.org.attendancemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
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

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        databaseHelper = new DatabaseHelper(this);
        showAllTheCourseList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.main_activity_menu_addnewaccount:
                Intent intent = new Intent(this,SelectFileActivity.class);
                intent.putExtra("1", "/" );
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }

    public void showAllTheCourseList(){

        List<Course> courses = databaseHelper.getAllTheCourseFromDatabase();

        ListAdapter listAdapter = new CustomAdapter(this,courses);
        ListView listView = (ListView) findViewById(R.id.idForMainActivityCourseDetailsListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Course course = (Course) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,CourseActivity.class);
                intent.putExtra("1",course.getSession());
                intent.putExtra("2",course.getCode());
                startActivity(intent);
                finish();
            }
        });


    }

    class CustomAdapter extends ArrayAdapter<Course> {

        public CustomAdapter(Context context, List<Course> courses) {
            super(context, R.layout.main_activity_courselist,courses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View customViewNow = layoutInflater.inflate(R.layout.main_activity_courselist,parent,false);

            TextView courseSessionNumberTextView = (TextView) customViewNow.findViewById(R.id.idForCourseSessionTextView);
            TextView courseNameTextView = (TextView) customViewNow.findViewById(R.id.idForCourseNameTextView);
            TextView courseCodeTextView = (TextView) customViewNow.findViewById(R.id.idForCourseCodeTextView);
            TextView courseTotalClassTextView = (TextView) customViewNow.findViewById(R.id.idForCourseTotalClassTextView);
            ImageView courseImageView = (ImageView) customViewNow.findViewById(R.id.idForCourseSemesterImage);

            Course nowCourse= getItem(position);

            courseSessionNumberTextView.setText(nowCourse.getSession());
            courseNameTextView.setText(nowCourse.getName());
            courseCodeTextView.setText(String.valueOf(nowCourse.getCode()));
            courseTotalClassTextView.setText(String.valueOf(nowCourse.getTotalClass()));
            byte[] imageForSemester = nowCourse.getSemesterImage();
            courseImageView.setImageBitmap(BitmapFactory.decodeByteArray(imageForSemester,0,imageForSemester.length));

            return customViewNow;
        }

    }

}
