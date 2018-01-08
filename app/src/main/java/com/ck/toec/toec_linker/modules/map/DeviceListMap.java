package com.ck.toec.toec_linker.modules.map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseActivity;
import com.ck.toec.toec_linker.base.BaseApp;
import com.ck.toec.toec_linker.common.component.ImageLoader;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.modules.device.entity.DetailPage;
import com.ck.toec.toec_linker.modules.device.entity.mListView;
import com.ck.toec.toec_linker.modules.device.ui.DeviceDetail;
import com.ck.toec.toec_linker.modules.device.ui.DeviceMainActivity;
import com.ck.toec.toec_linker.modules.main.entity.ToecDevice;
import com.ck.toec.toec_linker.modules.map.adapter.MapListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by wm on 2017/11/18.
 * 设备列表地图展示
 */

public class DeviceListMap extends Activity {
    private MapView mp;
    private BaiduMap mMap;

    //uikongjian
    @BindView(R.id.map_back)
    ImageView map_back;
    @BindView(R.id.map_search_content)
    EditText search_content;
    @BindView(R.id.map_search)
    ImageView map_search;
    @BindView(R.id.map_image)
    ImageView map_image;
    @BindView(R.id.map_name)
    TextView map_name;
    @BindView(R.id.map_pos)
    TextView map_pos;
    @BindView(R.id.map_open)
    ImageView map_open;
    @BindView(R.id.map_list)
    ImageView map_list;
    @BindView(R.id.map_refresh)
    ImageView map_refresh;
    //设备列表
    private List<ToecDevice> deviceList;
    //页数
    int cPage = 1;
    // 定位相关
    LocationClient mLocClient;
    public mLocationListenner myListener = new mLocationListenner();
    private LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private Double lastX = 0.0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    private MyLocationData locData;
    boolean isFirstLoc = true; // 是否首次定位
    //覆盖物相关
    private List<Marker> markerList = new ArrayList<>();
    BitmapDescriptor marker_online = BitmapDescriptorFactory
            .fromResource(R.mipmap.online);
    BitmapDescriptor marker_offline = BitmapDescriptorFactory
            .fromResource(R.mipmap.offline);
    private List<LatLng> latlng;
    //记录当前点击的设备
    int currentDevicePos = -1;
    //测试数据
    String b = "[{\"deviceID\":\"574102e42cc342b790f353f3e7ea5f8e\",\"deviceImage\":\"uploadFiles/uploadImgs/20171211/dbbbdad8cec64e25a57966a91d560e7f.png\",\"deviceName\":\"温湿度传感器No.02\",\"deviceParent\":\"城北水利监控\",\"devicePos\":\"\",\"deviceState\":\"0\",\"jd\":39.071,\"wd\":117.224},{\"deviceID\":\"cce57f7f938e4d1a9e5fd980be50dbed\",\"deviceImage\":\"uploadFiles/uploadImgs/20171211/6fc6ee622f0744698bca23ac0a44b259.png\",\"deviceName\":\"pm2.5传感器No,02\",\"deviceParent\":\"城北水利监控\",\"devicePos\":\"\",\"deviceState\":\"0\",\"jd\":39.068,\"wd\":117.218},{\"deviceID\":\"4fcb72e9f9b843febc4075f7fbc20d5d\",\"deviceImage\":\"uploadFiles/uploadImgs/20171211/4784707f72354e5f8262af1739d86d0a.png\",\"deviceName\":\"pm2.5传感器No.01\",\"deviceParent\":\"城北水利监控\",\"devicePos\":\"\",\"deviceState\":\"0\",\"jd\":39.069,\"wd\":117.228},{\"deviceID\":\"40ae8bfb883d4d1b9781cf410987b025\",\"deviceImage\":\"uploadFiles/uploadImgs/20171211/03241149ad804856b4349dfcdec1a226.png\",\"deviceName\":\"温湿度传感器No.01\",\"deviceParent\":\"城北水利监控\",\"devicePos\":\"\",\"deviceState\":\"0\",\"jd\":39.067,\"wd\":117.223},{\"deviceID\":\"dbbe286bd2f54c56801ed8792224069e\",\"deviceImage\":\"1\",\"deviceName\":\"温湿度\",\"deviceParent\":\"蓝川城市排水系统\",\"devicePos\":\"1\",\"deviceState\":\"0\",\"jd\":0.0,\"wd\":0.0},{\"deviceID\":\"5ca0d4f7d1884c59ad1214b9d07448b7\",\"deviceImage\":\"13\",\"deviceName\":\"12354123\",\"deviceParent\":\"蓝川城市排水系统\",\"devicePos\":\"1\",\"deviceState\":\"0\",\"jd\":0.0,\"wd\":0.0},{\"deviceID\":\"139f683d26e5454db3e5994d2a4b79d4\",\"deviceImage\":\"uploadFiles/uploadUserPhoto/images/%E4%BC%A0%E6%84%9F%E5%99%A8/qjcgq2.jpg\",\"deviceName\":\"SVT626双轴数字型倾角传感器-中浩\",\"deviceParent\":\"天津光电测试项目\",\"devicePos\":\"1\",\"deviceState\":\"0\",\"jd\":0.0,\"wd\":0.0},{\"deviceID\":\"14177212c15643b895e4b80bdf5d8029\",\"deviceImage\":\"uploadFiles/uploadUserPhoto/images/PLC%E8%AE%BE%E5%A4%87/%E9%A3%8E%E6%89%872.png\",\"deviceName\":\"1号遥控设备\",\"deviceParent\":\"华电天津市东丽区线路\",\"devicePos\":\"1\",\"deviceState\":\"0\",\"jd\":0.0,\"wd\":0.0},{\"deviceID\":\"634ba53f8d5d4da4a812eff1280d73f2\",\"deviceImage\":\"uploadFiles/uploadUserPhoto/images/PLC%E8%AE%BE%E5%A4%87/selftimer.png\",\"deviceName\":\"1号遥信设备\",\"deviceParent\":\"华电天津市东丽区线路\",\"devicePos\":\"1\",\"deviceState\":\"0\",\"jd\":0.0,\"wd\":0.0},{\"deviceID\":\"b67bbcb3b4c94e208775fae0ce1be456\",\"deviceImage\":\"uploadFiles/uploadUserPhoto/images/%E7%94%B5%E6%9C%BA/1%20(9).jpg\",\"deviceName\":\"1号软压板\",\"deviceParent\":\"华电天津市东丽区线路\",\"devicePos\":\"1\",\"deviceState\":\"0\",\"jd\":0.0,\"wd\":0.0}]";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_devicelist);
        ButterKnife.bind(this);
        mp = (MapView)findViewById(R.id.devicelist_map);
        mMap = mp.getMap();
        /// /获取第一页设备坐标
       // getDeviceList();

        //初始化底部菜单
        initFrag();
        //定位初始化地图 以用户当前位置为原点
        initLocation();
        getTestData(b);
        //添加设备标志物
        setMarkers();
    }
    private void getTestData(String c){
        Gson b = new Gson();
        deviceList = b.fromJson(c,new TypeToken<List<ToecDevice>>(){}.getType());
        for (ToecDevice a:deviceList) {
            LatLng latLng = new LatLng(a.getJd(),a.getWd());
            MarkerOptions markerOptions = new MarkerOptions();
            if(a.getDeviceState().equals("1")){
                markerOptions.position(latLng).icon(marker_online).zIndex(10).draggable(false);
            }else if(a.getDeviceState().equals("0")){
                markerOptions.position(latLng).icon(marker_offline).zIndex(10).draggable(false);
            }
            markerOptions.animateType(MarkerOptions.MarkerAnimateType.grow);
            markerOptions.title(a.getDeviceName());
            Marker marker = (Marker)mMap.addOverlay(markerOptions);
            markerList.add(marker);
        }
    }

    private void getDeviceList() {
        RetrofitUtil.getInstance()
                .getDeviceList("1")
                .subscribe(new Observer<DetailPage<ToecDevice>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull DetailPage<ToecDevice> toecDeviceDetailPage) {
                        if (toecDeviceDetailPage!=null){
                            deviceList = toecDeviceDetailPage.getList();
                            for (ToecDevice a:deviceList) {
                                LatLng latLng = new LatLng(a.getJd(),a.getWd());
                                MarkerOptions markerOptions = new MarkerOptions();
                                if(a.getDeviceState().equals("1")){
                                    markerOptions.position(latLng).icon(marker_online).zIndex(10).draggable(false);
                                }else if(a.getDeviceState().equals("0")){
                                    markerOptions.position(latLng).icon(marker_offline).zIndex(10).draggable(false);
                                }
                                markerOptions.animateType(MarkerOptions.MarkerAnimateType.grow);
                                markerOptions.title(a.getDeviceName());
                                Marker marker = (Marker)mMap.addOverlay(markerOptions);
                                markerList.add(marker);
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Util.showShort("设备列表获取失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    //添加覆盖物
    private void setMarkers() {
        mMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (markerList.size()>0){
                    for(int i=0;i<markerList.size();i++){
                        if(markerList.get(i)==marker){
                            ToecDevice tempDevice = deviceList.get(i);
                            //记录当前位置
                            currentDevicePos = i;
                            ImageLoader.loadNetImage(DeviceListMap.this,
                                    Constant.ServerAddr+tempDevice.getDeviceImage(),map_image);
                            map_name.setText(tempDevice.getDeviceName());
                            map_pos.setText(tempDevice.getDevicePos());

                        }
                    }
                }
                return false;
            }
        });

    }

    private void initLocation() {
        mCurrentMode = LocationMode.NORMAL;
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_room);
        mMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        MapStatus.Builder builder1 = new MapStatus.Builder();
        builder1.overlook(0);
        mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));

        // 开启定位图层
        mMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(100);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    private void initFrag() {
        //list按键监听
        map_list.setOnClickListener(v->{
            //自底部弹出设备列表
            Dialog bottomDialog = new Dialog(this,R.style.bottomDialog);
            View contentView = LayoutInflater.from(this).inflate(R.layout.map_bottom_menu,null);
            bottomDialog.setContentView(contentView);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            //layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            //layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            //layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            DisplayMetrics metrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            layoutParams.width = metrics.widthPixels;
            contentView.setLayoutParams(layoutParams);
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.bottomDialog_Animation);

            mListView list = (mListView) contentView.findViewById(R.id.dialog_list);
            MapListAdapter mAapter = new MapListAdapter(deviceList,this);
            mAapter.setCurrentPos(currentDevicePos);
            list.setAdapter(mAapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentDevicePos = position;
                    //自动转到该坐标
                    ToecDevice td = deviceList.get(position);
                    LatLng ll = new LatLng(td.getJd(), td.getWd());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(16.0f);
                    mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    //填充底部菜单
                    ImageLoader.loadNetImage(DeviceListMap.this,
                            Constant.ServerAddr+td.getDeviceImage(),map_image);
                    map_name.setText(td.getDeviceName());
                    map_pos.setText(td.getDevicePos());
                    //弹窗消失
                    bottomDialog.dismiss();
                }
            });
            bottomDialog.setCanceledOnTouchOutside(true);
            bottomDialog.show();
        });
        //刷新按钮监听
        map_refresh.setOnClickListener(v->{
            mMap.clear();
            //释放之前所有marker
            if(markerList!=null){
                for(int i=0;i<markerList.size();i++){
                    markerList.get(i).remove();
                }
            }
            //清除list
            markerList.clear();
            deviceList.clear();
            //getDeviceList();
            //恢复到未选择设备状态
            currentDevicePos = -1;
            ImageLoader.loadNetImage(DeviceListMap.this,
                    null,map_image);
            map_name.setText("暂无内容");
            map_pos.setText("请选择设备进行查看");
            //重新加载数据
            getTestData(b);
            //加载动画(后续添加)
        });
        //open按钮监听
        map_open.setOnClickListener(v->{
            //跳转到该设备详情界面
            if(currentDevicePos>=0){
                Intent intent = new Intent();
                intent.putExtra("deviceId",deviceList.get(currentDevicePos).getDeviceID());
                intent.setClass(this, DeviceMainActivity.class);
                startActivity(intent);
            }

        });
        //后退按钮监听
        map_back.setOnClickListener(v->{
            this.finish();
        });
        //搜索按钮监听
        map_search.setOnClickListener(v->{
            //动画弹出搜索框
        });
    }
    //定位监听
    private class mLocationListenner implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mp == null) {
                return;
            }
            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            mCurrentAccracy = bdLocation.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mMap.setMyLocationData(locData);
            //首次定位定位到用户当前位置
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(15.0f);
                mMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mMap.setMyLocationEnabled(false);
        mp.onDestroy();
        mp = null;
        //清除marker图
        marker_offline.recycle();
        marker_online.recycle();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mp.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mp.onPause();
        super.onPause();
    }
}
