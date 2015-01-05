package com.example.gradeplanner;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

public class UpdateGoalListener implements OnClickListener{

	private EditText input;
	private Activity activity;
	
	public UpdateGoalListener(EditText input, Activity activity){
		super();
		this.input = input;
		this.activity = activity;
	}
	public void onClick(DialogInterface dialog, int which) {
		if(Utility.checkNumberInput(input.getText().toString(), activity)){
			CourseDBHelper dbHelper= new CourseDBHelper(activity);
			SQLiteDatabase db=dbHelper.getWritableDatabase();
			ContentValues values =new ContentValues();
			values.put("Goal", Double.parseDouble(input.getText().toString()));
			db.update("Courses", values, "CourseCode =?", new String[]{activity.getTitle().toString()});
			db.close();
			activity.recreate();
		}
	}

}
