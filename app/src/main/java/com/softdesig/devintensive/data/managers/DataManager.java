package com.softdesig.devintensive.data.managers;

import android.content.Context;

import com.softdesig.devintensive.data.network.RestService;
import com.softdesig.devintensive.data.network.ServiceGenerator;
import com.softdesig.devintensive.data.network.request.UserLoginReq;
import com.softdesig.devintensive.data.network.responce.UserModelRes;
import com.softdesig.devintensive.utils.DevintensiveApplication;

import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANCE = null;
    private PreferenceManager mPreferenceManager;
    private RestService mRestService;

    private Context mContext;

    public DataManager() {
        this.mPreferenceManager = new PreferenceManager();
        //this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    public static DataManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }

    //region ============ Network ======================
    public Call<UserModelRes> loginUser(UserLoginReq userLoginReq){
        return mRestService.loginUser(userLoginReq);
    }
    //endregion
}
