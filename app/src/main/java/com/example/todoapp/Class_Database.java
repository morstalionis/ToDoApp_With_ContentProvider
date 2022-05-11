package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Class_Database extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ToDoList.db";
    private static final int  DATABASE_VERSION = 1;

    private static final String C_TABLE_NAME = "CompletedItems";
    private static final String C_COLUMN_ID = "_cid";
    private static final String C_COLUMN_TASK = "c_task";
    private static final String C_COLUMN_DATE = "c_date";
    private static final String C_COLUMN_MODIFIED = "c_modified";
    private static final String C_COLUMN_DETAILS = "c_details";

    private static final String P_TABLE_NAME = "PendingItems";
    private static final String P_COLUMN_ID = "_pid";
    private static final String P_COLUMN_TASK = "p_task";
    private static final String P_COLUMN_DATE = "p_date";
    private static final String P_COLUMN_MODIFIED = "p_modified";
    private static final String P_COLUMN_DETAILS = "p_details";

    Class_Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String complete_query = "CREATE TABLE " + C_TABLE_NAME + " (" + C_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_COLUMN_TASK + " TEXT, " +
                C_COLUMN_DATE + " TEXT, " +
                C_COLUMN_MODIFIED + " TEXT, " +
                C_COLUMN_DETAILS + " TEXT);";

        String pending_query = "CREATE TABLE " + P_TABLE_NAME + " (" + P_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                P_COLUMN_TASK + " TEXT, " +
                P_COLUMN_DATE + " TEXT, " +
                P_COLUMN_MODIFIED + " TEXT, " +
                P_COLUMN_DETAILS + " TEXT);";

        sqLiteDatabase.execSQL(complete_query);
        sqLiteDatabase.execSQL(pending_query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + C_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + P_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //COMPLETED ITEMS DATABASE FUNCTIONS

    void c_add_task(String task_title, String task_date, String task_modified, String task_details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(C_COLUMN_TASK, task_title);
        cv.put(C_COLUMN_DATE, task_date);
        cv.put(C_COLUMN_MODIFIED, task_modified);
        cv.put(C_COLUMN_DETAILS, task_details);

        long result = db.insert(C_TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Marked as Complete", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor c_readAllData(){
        String query = "SELECT * FROM " + C_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void c_updateData(String row_id, String task, String modified, String details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv_update = new ContentValues();
        cv_update.put(C_COLUMN_TASK, task);
        cv_update.put(C_COLUMN_MODIFIED, modified);
        cv_update.put(C_COLUMN_DETAILS, details);

        long result = db.update(C_TABLE_NAME, cv_update, "_cid=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void c_deleteItem(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(C_TABLE_NAME, "_cid=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
        }else{

        }
    }

    //PENDING ITEMS DATABASE FUNCTIONS

    void p_add_task(String task_title, String task_date, String task_modified, String task_details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(P_COLUMN_TASK, task_title);
        cv.put(P_COLUMN_DATE, task_date);
        cv.put(P_COLUMN_MODIFIED, task_modified);
        cv.put(P_COLUMN_DETAILS, task_details);

        long result = db.insert(P_TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Add Entry Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor p_readAllData(){
        String query = "SELECT * FROM " + P_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void p_updateData(String row_id, String task, String modified, String details){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv_update = new ContentValues();
        cv_update.put(P_COLUMN_TASK, task);
        cv_update.put(P_COLUMN_MODIFIED, modified);
        cv_update.put(P_COLUMN_DETAILS, details);

        long result = db.update(P_TABLE_NAME, cv_update, "_pid=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to Update!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show();
        }
    }

    void p_deleteItem(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(P_TABLE_NAME, "_pid=?", new String[]{String.valueOf(row_id)});
        if (result == -1){
            Toast.makeText(context, "Failed to Delete!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
        }
    }

}
