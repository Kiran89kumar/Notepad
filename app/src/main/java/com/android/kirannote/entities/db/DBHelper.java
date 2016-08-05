package com.android.kirannote.entities.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.android.kirannote.BuildConfig;

import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**
 * Created by kiran.kumar on 8/3/16.
 */

/**
 * DBHelper class is Database helper class for creation of Table and to do crud operations
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context,DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        executeQuery(sqLiteDatabase, SQL_FILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public SQLiteDatabase loadDB() {
        if(database == null)
            database = getWritableDatabase();

        return database;
    }

    private void executeQuery(final SQLiteDatabase db, String filename){

        String[] statements = loadStatements(filename);

        Observable.from(statements)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(s);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG,"DB Loaded with assets data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"Error while inserting:"+e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        db.execSQL(s.trim());
                    }
                });
    }

    private String[] loadStatements(String sqlFile) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(sqlFile);
            return new SQLParser().parseSqlFile(inputStream);

        } catch (Exception ignored) {
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    private SQLiteDatabase database;
    private final Context context;

    private static final String DB_NAME = BuildConfig.DB_NAME;
    private static final int DB_VERSION = BuildConfig.DB_VERSION;
    private static final String SQL_FILE = BuildConfig.SQL_FILE;
    private static final String TAG = DBHelper.class.getSimpleName();

}
