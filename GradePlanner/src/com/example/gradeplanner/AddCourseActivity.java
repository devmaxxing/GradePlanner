package com.example.gradeplanner;

import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.gradeplanner.CourseContract.CourseEntry;

public class AddCourseActivity extends ActionBarActivity {

	private ArrayList<String> courseCodes;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_course);
		Intent intent=getIntent();
		courseCodes= intent.getExtras().getStringArrayList(MainActivity.EXTRA_COURSECODES);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void enterCourse(View view){
		if(inputValid()){
			EditText courseCode = (EditText) findViewById(R.id.editText1);
			EditText courseGoal = (EditText) findViewById(R.id.editText2);
			CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
			
			String isSpecial="n";
			if(checkBox.isChecked())//Final exam mark replaces all lower marks
				isSpecial="y";
			
			CourseDBHelper dBHelper= new CourseDBHelper(this);
			SQLiteDatabase db = dBHelper.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(CourseEntry.COLUMN_NAME_COURSE_CODE, courseCode.getText().toString());
			values.put(CourseEntry.COLUMN_NAME_GOAL, 
					Double.parseDouble(courseGoal.getText().toString()));
			values.put(CourseEntry.COLUMN_NAME_SPECIAL, isSpecial);
			
			db.insert(CourseEntry.TABLE_NAME, null, values);
			
			Intent intent= new Intent(this, MainActivity.class);
			finish();
			startActivity(intent);
		}
	}
	
	private boolean inputValid(){
		EditText courseGoal = (EditText) findViewById(R.id.editText2);
		EditText courseCode = (EditText) findViewById(R.id.editText1);
		
		if(!courseCode.getText().toString().equals("")){
			boolean duplicate=false;
			for(String course :courseCodes){
				if(course.toUpperCase(Locale.getDefault()).equals(
						courseCode.getText().toString().toUpperCase(Locale.getDefault()))){
					duplicate=true;
					break;
				}
			}
			if(!duplicate)
				return Utility.checkNumberInput(courseGoal.getText().toString(), this);
			else
				createDialog(3).show();
		}else
			createDialog(2).show();
		return false;
	}
	
	private AlertDialog createDialog(int code){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if(code==2)
			builder.setMessage("Please enter a course code.");
		else
			builder.setMessage("The course code already exists.");
		return builder.create();
	}
}
