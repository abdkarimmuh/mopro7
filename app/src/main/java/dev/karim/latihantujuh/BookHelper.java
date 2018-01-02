package dev.karim.latihantujuh;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Karim on 11/14/2017.
 */

public class BookHelper extends SQLiteOpenHelper {

    final static String DBNAME = "book.db";
    final static int DBVERSION = 1;

    public BookHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_create = "CREATE TABLE book_entries(" +
                "_id INTEGER PRIMARY KEY autoincrement,"+
                "title TEXT," +
                "author TEXT)";
        db.execSQL(query_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query_drop = "DROP TABLE IF EXIST book_entries";
        db.execSQL(query_drop);
        onCreate(db);
    }
}
