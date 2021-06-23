package com.example.todolistsqlite.DatabaseHelper;

import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolistsqlite.Model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME="TODO_TABLE";
    private static final String COL_1="ID";
    private static final String COL_2="TASK";
    private static final String COL_3="STATUS";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not EXISTS "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TASK TEXT,STATUS INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(TodoModel model){
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,model.getTask());
        values.put(COL_3,0);
        db.insert(TABLE_NAME,null,values);
    }
    public void updateTask(int id , String task){
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,task);
        db.update(TABLE_NAME,values,"ID=?",new String[]{String.valueOf(id)});
    }
    public void updateStatus(int id , int status){
        db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3,status);
        db.update(TABLE_NAME,values,"ID=?",new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id){
        db=this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(id)});
    }
    public List<TodoModel> getAllTAsks(){
        db=this.getWritableDatabase();
        Cursor cursor=null;
        List<TodoModel> modelList= new ArrayList<>();
        db.beginTransaction();
        try {
            cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
          if (cursor != null){
              if (cursor.moveToFirst()){
                  do {
                      TodoModel t1=new TodoModel();
                      t1.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                      t1.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                      t1.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                      modelList.add(t1);
                  }while (cursor.moveToNext());
              }
          }
        }finally {
            db.endTransaction();
        }
        return modelList;
    }
}
