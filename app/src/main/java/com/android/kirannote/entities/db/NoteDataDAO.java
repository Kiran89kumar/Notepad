package com.android.kirannote.entities.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by kiran.kumar on 8/4/16.
 */
public class NoteDataDAO {

    @Inject
    public NoteDataDAO(DBHelper dbHelper){
        this.appDB = dbHelper;
    }

    void open(){
        database = appDB.loadDB();
    }

    void close(){
        /*if(database!=null){
            database.close();
        }*/
    }

    List<NoteData> getNoteData() {
        List<NoteData> items = new ArrayList<>();
        String[] whereArgs = new String[]{0+""};
        String whereClause=  Columns.COL_REMOVED + "=?";

        Cursor cursor = null;
        try {
            cursor = database.query(TABLE_NAME,
                    projections,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    sortOrder
            );

            while (cursor.moveToNext()) {
                /*NoteData item = new NoteData();
                for (String column : projections) {
                    int columnIndex = cursor.getColumnIndex(column);
                    switch (column) {
                        case Columns.COL_ID:
                            item.setId(cursor.getInt(columnIndex));
                            break;

                        case Columns.COL_HEADING:
                            item.setHeading(cursor.getString(columnIndex));
                            break;

                        case Columns.COL_REMOVED:
                            item.setRemoved(cursor.getInt(columnIndex));
                            break;

                        case Columns.COL_DESC:
                            item.setDescription(cursor.getString(columnIndex));
                            break;

                        default:
                            break;
                    }
                }*/
                NoteData item = getDataFromCursor(cursor);
                items.add(item);
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }

        return items;
    }


    NoteData getNoteData(int id) {
        NoteData data = null;
        String[] whereArgs = new String[]{id+""};

        if(database == null){
            open();
        }

        String whereClause=  Columns.COL_ID + "=?";

        Cursor cursor = null;
        try {
            cursor = database.query(TABLE_NAME,
                    projections,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    sortOrder
            );

            Log.i(TABLE_NAME,"Data:"+cursor+"---"+cursor.getCount());

            while (cursor.moveToNext()) {
                data =  getDataFromCursor(cursor);
            }
        } finally {
            if (cursor!=null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return data;
    }

    public List<NoteData> insertNotes(NoteData noteData){
        Log.i(TABLE_NAME,"insert Notes"+noteData.getHeading());
        ContentValues values = new ContentValues();
        values.put(Columns.COL_HEADING,noteData.getHeading());
        values.put(Columns.COL_DESC,noteData.getDescription());
        values.put(Columns.COL_REMOVED, noteData.getRemoved());
        if(database == null){
            open();
        }

        long inserted = database.insert(TABLE_NAME,null,values);

        Log.i(TABLE_NAME,"inserted:--"+inserted);

        if(inserted>0)
            return getNoteData();

        return getNoteData();
    }

    public NoteData updateNote(NoteData data){
        String[] whereArgs = new String[]{
                data.getId()+""
        };
        String whereClause=  Columns.COL_ID + "=?";

        ContentValues values = new ContentValues();
        values.put(Columns.COL_HEADING,data.getHeading());
        values.put(Columns.COL_DESC, data.getDescription());
        values.put(Columns.COL_REMOVED, data.getRemoved());
        if(database == null){
            open();
        }
        database.update(TABLE_NAME,values, whereClause, whereArgs);

        return getNoteData(data.getId());
    }

    public List<NoteData> deleteNote(int id) {
        String[] whereArgs = new String[]{
                id+""
        };
        String whereClause=  Columns.COL_ID + "=?";

        if(database == null){
            open();
        }

        database.delete(TABLE_NAME, whereClause, whereArgs);

        return getNoteData();
    }

    private NoteData getDataFromCursor(Cursor cursor){
        NoteData item = new NoteData();
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Columns.COL_ID)));
        item.setHeading(cursor.getString(cursor.getColumnIndexOrThrow(Columns.COL_HEADING)));
        item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Columns.COL_DESC)));
        return item;
    }


    private static final String TABLE_NAME = "notebook";

    private abstract class Columns {
        static final String COL_ID     = "_id";
        static final String COL_HEADING = "heading";
        static final String COL_DESC = "description";
        static final String COL_REMOVED = "remove";
    }

    private final String[] projections = {Columns.COL_ID,
            Columns.COL_HEADING,
            Columns.COL_REMOVED,
            Columns.COL_DESC};

    private final String sortOrder = Columns.COL_ID + " ASC";

    private SQLiteDatabase database;
    private final DBHelper appDB;
}
