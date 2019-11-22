package ru.niceaska.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ToDoDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ToDoList.db";

    public ToDoDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TaskDbSchema.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TaskDbSchema.Cols.UUID  + " text, " +
                TaskDbSchema.Cols.IS_DONE + " integer, " +
                TaskDbSchema.Cols.TITLE + " text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
