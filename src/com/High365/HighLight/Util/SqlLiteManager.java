package com.High365.HighLight.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.High365.HighLight.Bean.LoveLogBean;
import com.High365.HighLight.Bean.UserInfoBean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HUPENG
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
        sqLiteDatabase.execSQL("CREATE TABLE UserInfo (UserID varchar(100),UserPWD varchar(20),UserGesturePWD varchar(20),UserName varchar(20),UserEmail varchar(100),UserBirthDay Date,UserPhoto varchar(1000000),UserGender Int,UserSPhysiologicalDay Date,UserEPhysiologicalDay Date,UserPhone varchar(20))");
        sqLiteDatabase.execSQL("CREATE TABLE LoveLog (LogID INTEGER PRIMARY KEY AUTOINCREMENT,UserID varchar(100),SexStartTime timestamp,SexEndTime timestamp,SexTime timestamp,SexHighTime timestamp,SexSubjectiveScore integer,SexObjectiveScore integer,SexFrameState varchar(100000),RecordFileName varchar(100),UpdateFlag integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists person");
        onCreate(sqLiteDatabase);
    }

    /**
     * 以UserID在本地数据库中寻找UserInfoBean对象
     * */
    public UserInfoBean findUserInfoById(String userID){
        //构造要返回的对象,并且赋初值
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUserGender(1);
        userInfoBean.setUserPhone("");
        userInfoBean.setUserEmail("");
        userInfoBean.setUserName("");
        //构造数据库对象
        SQLiteDatabase highLightDB = this.getReadableDatabase();
        //构造数据库查询语句
        String sql = "SELECT * FROM UserInfo WHERE USERID = ?;";
        Cursor cursor = highLightDB.rawQuery(sql,new String[]{userID});
        //判断cursor对象是否为空,为空则返回null
        if (cursor.getCount() == 0){
            return null;
        }
        cursor.moveToFirst();
        //开始赋值
        userInfoBean.setUserId(userID);
        userInfoBean.setUserPwd(cursor.getString(cursor.getColumnIndex("UserPWD")));
        //若可能为NULL,则返回空字符串
        userInfoBean.setUserGesturePwd(cursor.getString(cursor.getColumnIndex("UserGesturePWD"))==null?"":cursor.getString(cursor.getColumnIndex("UserGesturePWD")));
        userInfoBean.setUserName(cursor.getString(cursor.getColumnIndex("UserName"))==null?"":cursor.getString(cursor.getColumnIndex("UserName")));
        userInfoBean.setUserEmail(cursor.getString(cursor.getColumnIndex("UserEmail"))==null?"":cursor.getString(cursor.getColumnIndex("UserEmail")));
        //从字符串构建date对象
        String userBirthDayString = cursor.getString(cursor.getColumnIndex("UserBirthDay"))==null?"":cursor.getString(cursor.getColumnIndex("UserBirthDay"));
        userInfoBean.setUserBirthDay(createDateFromString(userBirthDayString));

        userInfoBean.setUserPhoto(cursor.getString(cursor.getColumnIndex("UserPhoto"))==null?"":cursor.getString(cursor.getColumnIndex("UserPhoto")));
        userInfoBean.setUserGender(cursor.getInt(cursor.getColumnIndex("UserGender")));

        try {
            //从字符串构建date对象
            String userSPhysiologicalDayStr = cursor.getString(cursor.getColumnIndex("UserSPhysiologicalDay"))==null?"":cursor.getString(cursor.getColumnIndex("UserSPhysiologicalDay"));
            userInfoBean.setUserSphysiologicalDay(createDateFromString(userSPhysiologicalDayStr));

            //从字符串构建date对象
            String userEPhysiologicalDayStr = cursor.getString(cursor.getColumnIndex("UserEPhysiologicalDay"))==null?"":cursor.getString(cursor.getColumnIndex("UserEPhysiologicalDay"));
            userInfoBean.setUserEphysiologicalDay(createDateFromString(userEPhysiologicalDayStr));

        }catch (Exception e){}

        userInfoBean.setUserPhone(cursor.getString(cursor.getColumnIndex("UserPhone"))==null?"":cursor.getString(cursor.getColumnIndex("UserPhone")));
        highLightDB.close();
        return userInfoBean;
    }

    /**
     * 从yyyy-MM-dd类型的字符串构造java.util.Date对象
     * */
    private Date createDateFromString(String dateString){
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (dateString !=null || !date.equals("")){
            try {
                date = simpleDateFormat.parse(dateString);
            }catch (Exception e){ e.printStackTrace();}
        }else {
            return null;
        }
        return date;
    }

    /**
     * 从java.util.Date转化为yyyy-MM-dd类型的字符串
     * */

    private String getStringFromDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            return simpleDateFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 进行数据的持久化操作,即将bean里面的数据持久化到数据库中
     * */
    public void saveOrUpdateUserInfo(UserInfoBean userInfoBean){
        if (userInfoBean == null){
            return;
        }
        if (userInfoBean.getUserId() == null || userInfoBean.getUserId().equals("")){
            return;
        }
        SQLiteDatabase highLightDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UserID",userInfoBean.getUserId());
        if (userInfoBean.getUserPwd() != null){
            cv.put("UserPWD",userInfoBean.getUserPwd());
        }
        if (userInfoBean.getUserGesturePwd()!=null){
            cv.put("UserGesturePWD",userInfoBean.getUserGesturePwd());
        }
        if (userInfoBean.getUserName()!=null){
            cv.put("UserName",userInfoBean.getUserName());
        }
        if (userInfoBean.getUserEmail()!=null){
            cv.put("UserEmail",userInfoBean.getUserEmail());
        }
        if (userInfoBean.getUserBirthDay()!=null){
            cv.put("UserBirthDay",getStringFromDate(userInfoBean.getUserBirthDay()));
        }
        if (userInfoBean.getUserPhoto()!=null){
            cv.put("UserPhoto",userInfoBean.getUserPhoto());
        }
        if (userInfoBean.getUserGender()!=-1){
            cv.put("UserGender",userInfoBean.getUserGender());
        }
        if (userInfoBean.getUserSphysiologicalDay()!=null){
            cv.put("UserSPhysiologicalDay",getStringFromDate(userInfoBean.getUserSphysiologicalDay()));
        }
        if (userInfoBean.getUserEphysiologicalDay()!=null){
            cv.put("UserEPhysiologicalDay",getStringFromDate(userInfoBean.getUserEphysiologicalDay()));
        }
        if (userInfoBean.getUserPhone()!=null){
            cv.put("UserPhone",userInfoBean.getUserPhone());
        }
        //判断数据库中是否原有数据,有则update,没有则insert
        String userID = userInfoBean.getUserId();
        String sql = "SELECT * FROM UserInfo WHERE UserId = ?;" ;
        Cursor cursor = highLightDB.rawQuery(sql,new String[]{userID});
        if (cursor.getCount() == 0){
            //插入
            highLightDB.insert("userInfo",null,cv);
        }else {
            highLightDB.update("userInfo",cv,"UserID = ?",new String[]{userID});
        }
        highLightDB.close();
    }
    /**
     * CREATE TABLE LoveLog (LogID INTEGER PRIMARY KEY AUTOINCREMENT,UserID varchar(100),
     * SexStartTime timestamp,SexEndTime timestamp,SexTime timestamp,SexHighTime timestamp,
     * SexSubjectiveScore integer,SexObjectiveScore integer,SexFrameState varchar(100000)),
     * RecordFileName varchar(100),UpdateFlag integer
     * */
    public LoveLogBean getLoveLogByLogID(int logID){
        LoveLogBean loveLogBean = new LoveLogBean();
        String sql = "select * from loveLog where LogID = ?;";
        SQLiteDatabase highLightDB = getReadableDatabase();
        Cursor cursor = highLightDB.rawQuery(sql,new String[]{logID+""});
        if (cursor.getCount() == 0){
            return null;
        }
        cursor.moveToFirst();

        loveLogBean.setLogID(cursor.getInt(cursor.getColumnIndex("LogID")));
        loveLogBean.setUserId(cursor.getString(cursor.getColumnIndex("UserID")));

        if (cursor.getLong(cursor.getColumnIndex("SexStartTime"))!=0){
            loveLogBean.setSexStartTime(new Timestamp(cursor.getLong(cursor.getColumnIndex("SexStartTime"))));
        }
        if (cursor.getLong(cursor.getColumnIndex("SexEndTime"))!=0){
            loveLogBean.setSexEndTime(new Timestamp(cursor.getLong(cursor.getColumnIndex("SexEndTime"))));
        }
        if (cursor.getLong(cursor.getColumnIndex("SexTime"))!=0){
            loveLogBean.setSexTime(new Timestamp(cursor.getLong(cursor.getColumnIndex("SexTime"))));
        }
        if (cursor.getLong(cursor.getColumnIndex("SexHighTime"))!=0){
            loveLogBean.setSexHighTime(new Timestamp(cursor.getLong(cursor.getColumnIndex("SexHighTime"))));
        }
        if (cursor.getInt(cursor.getColumnIndex("SexSubjectiveScore"))!=0){
            loveLogBean.setSexSubjectiveScore(cursor.getInt(cursor.getColumnIndex("SexSubjectiveScore")));
        }
        if (cursor.getInt(cursor.getColumnIndex("SexObjectiveScore"))!=0){
            loveLogBean.setSexObjectiveScore(cursor.getInt(cursor.getColumnIndex("SexObjectiveScore")));
        }
        loveLogBean.setSexFrameState(cursor.getString(cursor.getColumnIndex("SexFrameState")));
        loveLogBean.setRecordFileName(cursor.getString(cursor.getColumnIndex("RecordFileName")));
        loveLogBean.setUpdateFlag(cursor.getInt(cursor.getColumnIndex("UpdateFlag")));
        highLightDB.close();
        return loveLogBean;
    }

    public List<LoveLogBean> getLoveLogsByUserID(String userID){
        List<LoveLogBean> list = new ArrayList<LoveLogBean>();
        SQLiteDatabase highLightDB = getReadableDatabase();
        String sql = "select * from LoveLog where userID = ?;";
        Cursor cursor = highLightDB.rawQuery(sql,new String[]{userID});
        int count = cursor.getCount();
        while (cursor.moveToNext()){
            int logID = cursor.getInt(cursor.getColumnIndex("LogID"));
            list.add(getLoveLogByLogID(logID));
        }
        highLightDB.close();
        return list;
    }

    /**
     * CREATE TABLE LoveLog (LogID INTEGER PRIMARY KEY AUTOINCREMENT,UserID varchar(100),
     * SexStartTime timestamp,SexEndTime timestamp,SexTime timestamp,SexHighTime timestamp,
     * SexSubjectiveScore integer,SexObjectiveScore integer,SexFrameState varchar(100000)),
     * RecordFileName varchar(100),UpdateFlag integer
     * */
    public void updateOrInsertLoveLog(LoveLogBean loveLogBean){
        if (loveLogBean==null)
            return;
        ContentValues cv = new ContentValues();
        if (loveLogBean.getUserId()!=null){
            cv.put("UserID",loveLogBean.getUserId());
        }
        if (loveLogBean.getSexStartTime()!=null){
            cv.put("SexStartTime",loveLogBean.getSexStartTime().getTime());
        }
        if (loveLogBean.getSexEndTime()!=null){
            cv.put("SexEndTime",loveLogBean.getSexEndTime().getTime());
        }
        if (loveLogBean.getSexTime()!=null){
            cv.put("SexTime",loveLogBean.getSexTime().getTime());
        }
        if (loveLogBean.getSexHighTime()!=null){
            cv.put("SexHighTime",loveLogBean.getSexHighTime().getTime());
        }
        if (loveLogBean.getSexObjectiveScore()!=null){
            cv.put("SexObjectiveScore",loveLogBean.getSexObjectiveScore());
        }
        if (loveLogBean.getSexSubjectiveScore()!=null){
            cv.put("SexSubjectiveScore",loveLogBean.getSexSubjectiveScore());
        }
        if (loveLogBean.getSexFrameState()!=null){
            cv.put("SexFrameState",loveLogBean.getSexFrameState());
        }
        if (loveLogBean.getRecordFileName()!=null){
            cv.put("RecordFileName",loveLogBean.getRecordFileName());
        }
        if (loveLogBean.getUpdateFlag()!=null){
            cv.put("UpdateFlag",loveLogBean.getUpdateFlag());
        }
        SQLiteDatabase highLightDB = getWritableDatabase();
        if (loveLogBean.getLogID()!=null){
            //update
            highLightDB.update("LoveLog",cv,"LogID=?",new String[]{loveLogBean.getLogID()+""});
        }else{
            //insert
            long id = highLightDB.insert("LoveLog",null,cv);
            loveLogBean.setLogID((int)id);
        }
        highLightDB.close();
    }
}
