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
        sqLiteDatabase.execSQL("CREATE TABLE UserInfo (UserID varchar(100),UserPWD varchar(20),UserGesturePWD varchar(20),UserName varchar(20),UserEmail varchar(100),UserBirthDay Date,UserPhoto Blob,UserGender Int,UserSPhysiologicalDay Date,UserEPhysiologicalDay Date,UserPhone varchar(20))");
        sqLiteDatabase.execSQL("CREATE TABLE LoveLog (LogID INTEGER PRIMARY KEY AUTOINCREMENT,UserID varchar(100),SexStartTime timestamp,SexEndTime timestamp,SexTime timestamp,SexHighTime timestamp,SexSubjectiveScore integer,SexObjectiveScore integer,SexFrameState varchar(100000))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists person");
        onCreate(sqLiteDatabase);
    }
}
