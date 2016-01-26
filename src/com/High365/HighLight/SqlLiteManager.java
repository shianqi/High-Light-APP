package com.High365.HighLight;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;

/**
 * 此类用于android端本地数据库的管理
 */
public class SqlLiteManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hl.db";
    private static final int DATABASE_VERSION = 1;
    public SqlLiteManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE Table userEntity (username varchar(20),password varchar(20));");
        sqLiteDatabase.execSQL("CREATE table log (logId integer,text varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists person");
        onCreate(sqLiteDatabase);
    }
}
