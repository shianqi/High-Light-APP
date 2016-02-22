package com.High365.HighLight.Service;

import android.content.Context;
import android.util.Log;
import com.High365.HighLight.Bean.LoveLogBean;
import com.High365.HighLight.Interface.GetRankListener;
import com.High365.HighLight.Interface.Listener;
import com.High365.HighLight.Util.HttpRequest;
import com.High365.HighLight.Util.SharedPreferencesManager;
import com.High365.HighLight.Util.SqlLiteManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hupeng
 * 这个类主要负责LoveLog的业务逻辑的处理和本地数据库的访问
 *
 * 此类继承于线程类
 * url:服务器的url地址的一部分,IP地址与端口号在HttpRequest类中定义,这样做只需要修改那边的HOST的值就可以方便的更换服务器的地址与参数
 * param:参数
 * httpResponseStr:服务器所返回的json字符串
 * 由于Android的网络通信模块必须放在子线程中,若放在主线程中会导致阻塞主线程.
 */
public class LoveLogService extends Thread{
    private Context context;
    private Listener listener;
    private GetRankListener getRankListener;
    private Integer taskId;
    private String url;
    private String param;
    private String httpResponseStr;
    private LoveLogBean loveLogBean;

    public void update(LoveLogBean loveLogBean,Context context){
        this.context = context;
        this.loveLogBean = loveLogBean;

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context);
        String userID = sharedPreferencesManager.readString("UserID");
        String secretKey = sharedPreferencesManager.readString("secretKey");

        url = "updateLoveLog.action";
        param = "userID=" + userID + "&secretKey=" + secretKey + "&jsonString=" + new Gson().toJson(loveLogBean);
        taskId = 0;
        start();
    }

    /**
     * @param oper 获取oper的值不同，获取的数据类别
     * @param context context
     * @param listener 回调是否成功
     * oper=11:个人周排行榜
     * oper=12:个人月排行榜
     * oper=13:个人年排行榜
     * oper=21:全部用户周排行榜
     * oper=22:全部用户月排行榜
     * oper=23:全部用户年排行榜
     */
    public void getRankModelList(int oper,Context context,GetRankListener listener) {
        String userID = new SharedPreferencesManager(context).readString("UserID");
        url = "getRank.action";
        param = "userID=" + userID + "&oper=" + oper;
        taskId = 1;
        this.getRankListener = listener;
        Log.i("向服务端请求",param);
        start();
    }

    /**
     * 根据用户ID获取用户日志
     * @param userID 用户ID
     * @param context context
     * @return 用户的日志列表
     */
    public List<LoveLogBean>getLoveLogListByUserID(String userID, Context context){
        this.context = context;
        SqlLiteManager sqlLiteManager = new SqlLiteManager(context);
        return sqlLiteManager.getLoveLogsByUserID(userID);
    }

    @Override
    public void run() {
        switch (taskId){
            case 0:
                try{
                    httpResponseStr = HttpRequest.sendPost(url,param);
                    UpdateModel updateModel = new Gson().fromJson(httpResponseStr, UpdateModel.class);
                    if (updateModel!=null){
                        if (updateModel.getStatus() == 1){
                            //当更新成功时,更新本地数据库中的updateFlag值,值为1时则已经成功上传到远程服务器
                            loveLogBean.setUpdateFlag(1);
                            SqlLiteManager sqlLiteManager = new SqlLiteManager(context);
                            sqlLiteManager.updateOrInsertLoveLog(loveLogBean);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 1:
                List<RankModel> list = new ArrayList<RankModel>();
                try {
                    httpResponseStr = HttpRequest.sendPost(url,param);
                    Log.d("服务器返回的数据",httpResponseStr);
                    list = new Gson().fromJson(httpResponseStr,new TypeToken<List<RankModel>>() {}.getType());
                    getRankListener.onSuccess(list);
                }catch (Exception e){
                    getRankListener.onFailure("获取信息失败");
                }
                break;
            default:
        }
    }


    /**
     * 排名model
     * */
    public class RankModel{
        private String userID;
        private Integer sexSubjectiveScore;
        private Integer sexObjectiveScore;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public Integer getSexSubjectiveScore() {
            return sexSubjectiveScore;
        }

        public void setSexSubjectiveScore(Integer sexSubjectiveScore) {
            this.sexSubjectiveScore = sexSubjectiveScore;
        }

        public Integer getSexObjectiveScore() {
            return sexObjectiveScore;
        }

        public void setSexObjectiveScore(Integer sexObjectiveScore) {
            this.sexObjectiveScore = sexObjectiveScore;
        }
    }


}
