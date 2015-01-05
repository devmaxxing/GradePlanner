package com.example.gradeplanner;

import com.example.gradeplanner.CourseContract.CourseEntry;
import com.example.gradeplanner.CourseContract.TestEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseDBHelper extends SQLiteOpenHelper{
	public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Courses.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE= " DOUBLE";
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_COURSE_ENTRIES =
        "CREATE TABLE " + CourseEntry.TABLE_NAME + " (" +
        CourseEntry._ID + " INTEGER PRIMARY KEY," +
        CourseEntry.COLUMN_NAME_COURSE_CODE + TEXT_TYPE + COMMA_SEP +
        CourseEntry.COLUMN_NAME_GRADE + DOUBLE_TYPE + COMMA_SEP +
        CourseEntry.COLUMN_NAME_GOAL + DOUBLE_TYPE + COMMA_SEP +
        CourseEntry.COLUMN_NAME_SPECIAL + TEXT_TYPE +
        " )";
    private static final String SQL_DELETE_COURSE_ENTRIES =
        "DROP TABLE IF EXISTS " + CourseEntry.TABLE_NAME;
    
    private static final String SQL_CREATE_TEST_ENTRIES =
            "CREATE TABLE " + TestEntry.TABLE_NAME + " (" +
            TestEntry._ID + " INTEGER PRIMARY KEY," +
            TestEntry.COLUMN_NAME_TEST_NAME + TEXT_TYPE + COMMA_SEP +
            TestEntry.COLUMN_NAME_COURSE_CODE + TEXT_TYPE + COMMA_SEP +
            TestEntry.COLUMN_NAME_GRADE + DOUBLE_TYPE + COMMA_SEP +
            TestEntry.COLUMN_NAME_WEIGHT + DOUBLE_TYPE + COMMA_SEP +
            TestEntry.COLUMN_NAME_TYPE + TEXT_TYPE + 
            " )";
    private static final String SQL_DELETE_TEST_ENTRIES =
        "DROP TABLE IF EXISTS " + TestEntry.TABLE_NAME;
    
    public CourseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COURSE_ENTRIES);
        db.execSQL(SQL_CREATE_TEST_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TEST_ENTRIES);
        db.execSQL(SQL_DELETE_COURSE_ENTRIES);
        onCreate(db);
    }
}
