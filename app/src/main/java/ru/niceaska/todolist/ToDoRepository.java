package ru.niceaska.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.niceaska.todolist.model.Task;

public class ToDoRepository {

    private static SQLiteDatabase INSTANCE;

    public static SQLiteDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE  = new ToDoDbHelper(context).getReadableDatabase();

        }
        return INSTANCE;
    }


    public List<Task> getData(Context context) {
        SQLiteDatabase db = getDatabase(context);

        Cursor cursor = db.query(
                TaskDbSchema.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<Task> tasks = new ArrayList<>();
        try {
            while(cursor.moveToNext()) {
                String title = cursor.getString(
                        cursor.getColumnIndex(TaskDbSchema.Cols.TITLE));
                int id = cursor.getInt(
                        cursor.getColumnIndex(BaseColumns._ID));
                boolean isDone = cursor.getInt(
                        cursor.getColumnIndex(TaskDbSchema.Cols.IS_DONE)) == 1;

                tasks.add(new Task(id, title, isDone));
            }
        } finally {
            cursor.close();
        }
        return tasks;
    }

    public void addNewTask(Context context, String task) {
        SQLiteDatabase sqLiteDatabase = getDatabase(context);
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.Cols.UUID, UUID.randomUUID().variant());
        values.put(TaskDbSchema.Cols.IS_DONE, false);
        values.put(TaskDbSchema.Cols.TITLE, task);

        long newRowId = sqLiteDatabase.insert(TaskDbSchema.NAME, null, values);
    }

    public void updateTask(Context context, Task task) {
        SQLiteDatabase sqLiteDatabase = getDatabase(context);
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.Cols.UUID, UUID.randomUUID().variant());
        values.put(TaskDbSchema.Cols.IS_DONE, task.isDone());
        values.put(TaskDbSchema.Cols.TITLE, task.getTaskTitle());

        long newRowId = sqLiteDatabase.update(TaskDbSchema.NAME, values, "_id="+ task.getId(), null);
    }

    public void deleteTask(Context context, Task task) {
        SQLiteDatabase db = getDatabase(context);
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { String.valueOf(task.getId()) };
        int count = db.delete(
                TaskDbSchema.NAME,
                selection,
                selectionArgs);

    }
}
