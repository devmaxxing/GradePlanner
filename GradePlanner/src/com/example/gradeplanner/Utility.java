package com.example.gradeplanner;

import java.util.ArrayList;

import com.example.gradeplanner.CourseContract.CourseEntry;
import com.example.gradeplanner.CourseContract.TestEntry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Utility {
	
	public static final int GRADES = 1;
	public static final int WEIGHTS = 2;
	
	public static boolean checkNumberInput(String number, Activity activity){
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("ERROR");
		builder.setMessage("Please enter a number from 0 to 100.");
		
		try{
			if(Double.parseDouble(number)<=100)
				return true;
			builder.show();
			
		}catch(Exception e){
			builder.show();
		}
		return false;
	}
	
	public static ArrayList<Double> getArray(String courseCode, Activity activity, int selection){
		ArrayList<Double> grades =new ArrayList<Double>();
		ArrayList<Double> weights=new ArrayList<Double>();
		
		CourseDBHelper dbHelper = new CourseDBHelper(activity);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		String selectQuery="select * from Tests where CourseCode like '"+courseCode+"'";
		
		Cursor c = db.rawQuery(selectQuery,null);
		int numTest=c.getCount();
        c.moveToFirst();
        
        for(int i=0;i<numTest;i++){
        	double grade = Double.parseDouble(c.getString(
        			c.getColumnIndex(TestEntry.COLUMN_NAME_GRADE)));
        	double weight = Double.parseDouble(c.getString(
        			c.getColumnIndex(TestEntry.COLUMN_NAME_WEIGHT)));
        	if(c.getString(c.getColumnIndex(TestEntry.COLUMN_NAME_TYPE)).contains("SPECIAL"))
        		grades.add(i*1.0);//store the index of the final exam score at end of list
        	
        	grades.add(i,grade);
        	weights.add(weight);
        	c.moveToNext();
        }
        db.close();
        
        if(selection==GRADES)
        	return grades;
        else if(selection==WEIGHTS)
        	return weights;
        
        return null;
	}
	
	public static void computeGrade(String courseCode, Activity activity){
		double finalGrade=0;
		double totalWeight=0;
		ArrayList<Double> grades = getArray(courseCode, activity, GRADES);
		ArrayList<Double> weights= getArray(courseCode, activity, WEIGHTS);
        
        for(int i=0;i<weights.size();i++){
        	double grade = grades.get(i);
        	double weight = weights.get(i);
        	
        	if(weights.size()<grades.size())//grades contains a final exam
        		for(int j=0;j<grades.size();j++)
        			if(grade>grades.get(j)){
        				finalGrade-=grades.get(j)*weights.get(j);
        				finalGrade+=grades.get((int) Math.round(
        						grades.get(grades.size()-1)));//index of final exam score
        				grades.set(j, grade);
        			}
        	finalGrade+=grade*weight;
        	totalWeight+=weight;
        }
        finalGrade/=totalWeight;
        
        CourseDBHelper dbHelper = new CourseDBHelper(activity);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values= new ContentValues();
		values.put(CourseEntry.COLUMN_NAME_GRADE, finalGrade);
		String selection = CourseEntry.COLUMN_NAME_COURSE_CODE + " LIKE ?";
		String[] selectionArgs = { courseCode };
		db.update(
			    CourseEntry.TABLE_NAME,
			    values,
			    selection,
			    selectionArgs);
	}
}
