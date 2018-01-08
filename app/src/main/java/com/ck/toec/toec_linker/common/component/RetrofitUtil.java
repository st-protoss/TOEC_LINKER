package com.ck.toec.toec_linker.common.component;

import com.ck.toec.toec_linker.base.BaseApp;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.ck.toec.toec_linker.common.utils.RxUtil;
import com.ck.toec.toec_linker.common.utils.SharedPreferenceUtil;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.modules.device.entity.AlarmData;
import com.ck.toec.toec_linker.modules.device.entity.DetailPage;
import com.ck.toec.toec_linker.modules.device.entity.DeviceRtRule;
import com.ck.toec.toec_linker.modules.device.entity.DeviceWtRule;
import com.ck.toec.toec_linker.modules.device.entity.HisRealdata;
import com.ck.toec.toec_linker.modules.device.entity.RtHistory;
import com.ck.toec.toec_linker.modules.launch.entity.Login;
import com.ck.toec.toec_linker.modules.main.entity.ToecDevice;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.observable.ObservableError;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wm on 2017/11/15.
 * 返回retrofit的单例实例
 */

public class RetrofitUtil {
    private static ToecServerInterface mServerApi = null;
    private static Retrofit mRetrofit = null;
    private static OkHttpClient mOkHttpClient = null;

    private static class RetrofitHolder{
        private static RetrofitUtil instance = new RetrofitUtil();
    }
    private RetrofitUtil(){
    init();
    }

    private void init() {
        initOkHttp();
        initRetrofit();
        mServerApi = mRetrofit.create(ToecServerInterface.class);
    }

    /**
     * 创建retrofit实例
     */
    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ToecServerInterface.SERVER_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 初始化并创建okhttp实例
     */
    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 缓存 http://www.jianshu.com/p/93153b34310e
        if(SharedPreferenceUtil.getInstance().getBoolean("openCache",true)){
            File cacheFile = new File(Constant.NET_CACHE);
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            Interceptor cacheInterceptor = chain -> {
                Request request = chain.request();
                if (!Util.isNetworkConnected(BaseApp.getAppContext())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                Response.Builder newBuilder = response.newBuilder();
                if (Util.isNetworkConnected(BaseApp.getAppContext())) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    newBuilder.header("Cache-Control", "public, max-age=" + maxAge);

                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                }
                return newBuilder.build();
            };
            builder.cache(cache).addInterceptor(cacheInterceptor);
        }

        if (Constant.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }
        //设置超时
        builder.connectTimeout(2, TimeUnit.SECONDS);
        builder.readTimeout(2, TimeUnit.SECONDS);
        builder.writeTimeout(2, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        mOkHttpClient = builder.build();
    }

    /**
     * 获取单例的RetrofitUtil对象
     * @return
     */

    public static RetrofitUtil getInstance(){
        return RetrofitHolder.instance;
    }

    /**
     * 处理向服务器失败的操作
     * @param t
     */
    private static Consumer<Throwable> disposeFailureInfo(Throwable t) {
        return throwable -> {
            if (t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
                    t.toString().contains("UnknownHostException")) {
                Util.showShort("网络问题");
            }
            Log.w(t.getMessage());
        };
    }
    /**
     * 获取设备列表
     */
    public Observable<DetailPage<ToecDevice>> getDeviceList(String userID){
        return mServerApi.deviceListAPI(userID)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());
    }
    /**
     * 获取某设备的实时数据属性列表
     */
    public Observable<List<DeviceRtRule>> getRtdataList(String deviceID){
        return mServerApi.RtruleListAPI(deviceID)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());
    }
    /**
     * 获取某设备的开关属性列表
     */
    public Observable<List<DeviceWtRule>> getWtdataList(String deviceID){
        return mServerApi.WtruleListAPI(deviceID)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());
    }
    /**
     * 将开关属性下发到服务器
     */
    public Observable<String> writeCommand(String ID , int status){
        return  mServerApi.WriteCommand(ID,status)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());

    }
    /**
     * 登录
     */
    public Observable<Login> loginCheck(String username, String userpassword){
        return mServerApi.LoginCheck(username,userpassword)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());
    }
    /**
     * 获取某个实时属性的实时值
     */
    public Observable<DeviceRtRule> GetRtData(String deviceID,String RtID){
        return mServerApi.GetRtData(deviceID,RtID)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());
    }
    /**
     * 获取历史数据列表
     */
    public Observable<DetailPage<RtHistory>> GetHistoryList(String rtID, String deviceID,int cPage){
        return mServerApi.GetHistoryList(rtID,deviceID,cPage)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());
    }
    /**
     * 获取趋势图数据列表
     */
    public Observable<List<HisRealdata>> GetTrendList(String rtID, String dataType){
        return mServerApi.GetTrendList(rtID,dataType)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());
    }
    /**
     * 获取报警数据列表
     */
    public Observable<DetailPage<AlarmData>> GetAlarmList(String rtID ,int cPage ){
        return mServerApi.GetAlarmList(rtID,cPage)
                .doOnError(RetrofitUtil::disposeFailureInfo)
                .compose(RxUtil.io());
    }
}
