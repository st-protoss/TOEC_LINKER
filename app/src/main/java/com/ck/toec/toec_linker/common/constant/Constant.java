package com.ck.toec.toec_linker.common.constant;

import com.ck.toec.toec_linker.base.BaseApp;

import java.io.File;

/**
 * Created by wm on 2017/11/10.
 * save constants  常量类
 */

public class Constant {
    //本地http请求路径
    public static final String NET_CACHE = BaseApp.getAppCacheDir() + File.separator + "ToecNetCache";
    //DEBUG开关
    public static Boolean DEBUG = false;
    //百度地图ak
    public final static String BaiduMapAK = "r7f6YEVcyxrWKWHfpTvhgLdwa0KCs1cF";
    //服务器地址
    public final static String ServerAddr = "http://192.168.1.155:80/";
    public final static  String EmailAddr = "toec@126.com";
}
