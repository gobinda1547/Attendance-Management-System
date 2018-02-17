package attendance.gobinda.cse.ju.org.attendancemanagementsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectFileActivity extends AppCompatActivity {

    private String nowDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_file_activity);

        nowDirectory = getIntent().getExtras().getString("1");
        showTheFileList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.select_file_activity_menu,menu);
        return true;
    }


    public void showTheFileList(){

        List<String> fileNameList = new ArrayList<>();
        File nowDirectoryFile = new File(nowDirectory);
        File[] filesIntheFolder = nowDirectoryFile.listFiles();
        if(filesIntheFolder == null ){
            Toast.makeText(this,"null",Toast.LENGTH_SHORT);
        }else if(filesIntheFolder.length == 0){
            Toast.makeText(this,"folder empty",Toast.LENGTH_SHORT);
        }else {
            for(File f: filesIntheFolder){
                fileNameList.add(f.getName());
            }
        }

        final ListAdapter listAdapter = new SelectFileActivity.CustomAdapterForShowFileList(this,fileNameList);
        final ListView listView = (ListView) findViewById(R.id.idForSelectFileActivityFileNameListView);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try{
                    String choosenFileName = adapterView.getItemAtPosition(position).toString();
                    String dirHere = nowDirectory+"/"+choosenFileName;
                    if(nowDirectory.equals("/")){
                        dirHere = nowDirectory+choosenFileName;
                    }
                    if(new File(dirHere).isFile()){
                        String message = "This is a file!";
                        Toast.makeText(SelectFileActivity.this,message,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(SelectFileActivity.this,SelectFileActivity.class);
                    intent.putExtra("1",dirHere);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    String message = "Can not open this folder!";
                    Toast.makeText(SelectFileActivity.this,message,Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedOptionId = item.getItemId();

        if(selectedOptionId == R.id.select_file_activity_menu_colseScreen){
            startActivity(new Intent(this,MainActivity.class));
            finish();
            return true;
        }
        if(selectedOptionId == R.id.select_file_activity_menu_goBack){
            try{
                Intent intent = new Intent(this,SelectFileActivity.class);
                File f = new File(nowDirectory);
                intent.putExtra("1",f.getParentFile().getAbsolutePath());
                startActivity(intent);
                finish();
            }catch (Exception e){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
        if(selectedOptionId == R.id.select_file_activity_menu_selectThisLocation){
            String messageReturn = MainActivity.databaseHelper.loadCourseFromThisFileLocation(this,nowDirectory);
            if(messageReturn.length() != 0){
                AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
                a_builder.setMessage(messageReturn);
                a_builder.setCancelable(false);
                a_builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = a_builder.create();
                alert.setTitle("Alert !!!");
                alert.show();
                return true;
            }
            startActivity(new Intent(this,MainActivity.class));
            finish();
            return true;
        }

        return false;
    }

    class CustomAdapterForShowFileList extends ArrayAdapter<String> {

        public CustomAdapterForShowFileList(Context context, List<String> fileList) {
            super(context, R.layout.main_activity_courselist,fileList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View customViewNow = layoutInflater.inflate(R.layout.select_file_activity_filelist,parent,false);
            TextView fileNameTextView = (TextView) customViewNow.findViewById(R.id.idForEnterFileName);
            fileNameTextView.setText(getItem(position));
            return customViewNow;
        }
    }
}
