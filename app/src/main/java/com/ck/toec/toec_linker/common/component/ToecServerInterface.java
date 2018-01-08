package com.ck.toec.toec_linker.common.component;

import com.ck.toec.toec_linker.modules.device.entity.AlarmData;
import com.ck.toec.toec_linker.modules.device.entity.DetailPage;
import com.ck.toec.toec_linker.modules.device.entity.DeviceRtRule;
import com.ck.toec.toec_linker.modules.device.entity.DeviceWtRule;
import com.ck.toec.toec_linker.modules.device.entity.HisRealdata;
import com.ck.toec.toec_linker.modules.device.entity.RtHistory;
import com.ck.toec.toec_linker.modules.launch.entity.Login;
import com.ck.toec.toec_linker.modules.main.entity.ToecDevice;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by wm on 2017/11/15.
 * 向服务器请求的Retrofit接口类
 */

public interface ToecServerInterface {
    String SERVER_URL = "http://192.168.1.155:80/app/";
    //获取设备列表
    @Headers("Content-Type:application/json;charset=utf-8")
    @GET("deviceList")
    public Observable<DetailPage<ToecDevice>> deviceListAPI(@Query("userID")String userID);
    //获取某设备实时数据列表
    @GET("deviceRtData")
    public Observable<List<DeviceRtRule>> RtruleListAPI(@Query("deviceID")String deviceID);
    //获取某设备开关数据列表
    @GET("deviceWtData")
    public Observable<List<DeviceWtRule>> WtruleListAPI(@Query("deviceID")String deviceID);
    //向服务器写入设备命令
    @GET("writeCommand")
    public Observable<String> WriteCommand(@Query("ID")String ID,@Query("status")int status);
    //登录验证
    @GET("loginCheck")
    public Observable<Login> LoginCheck(@Query("username")String username , @Query("userpassword")String password);
    //获取某个实时属性的实时值
    @GET("getRtData")
    public Observable<DeviceRtRule> GetRtData(@Query("deviceID")String deviceID,@Query("RtID")String RtID);
    //获取历史数据列表
    @GET("historyList")
    public Observable<DetailPage<RtHistory>> GetHistoryList(@Query("RtID")String rtID, @Query("deviceID")String deviceID
    ,@Query("currentPage")int cPage);
    //获取趋势图列表
    @GET("trendList")
    public Observable<List<HisRealdata>> GetTrendList(@Query("RtID")String rtID, @Query("dateType")String datatype);
    //获取报警列表
    @GET("alarmList")
    public Observable<DetailPage<AlarmData>> GetAlarmList(@Query("RtID")String rtID,@Query("currentPAge")int cPage);
 /*   //获取实时报警信息
    @GET("rtAlarmList")
    public Observable<rtAlarmList> GetRtAlarmList(@Query("usertoken")String usertoken);*/
}
