package com.ck.toec.toec_linker.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TableLayout;


import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseActivity;
import com.ck.toec.toec_linker.common.utils.CheckDoubleTouch;
import com.ck.toec.toec_linker.common.utils.TestInterface;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.modules.main.adapter.DeviceListAdapter;
import com.ck.toec.toec_linker.modules.main.adapter.ProjectFragAdapter;
import com.ck.toec.toec_linker.modules.map.DeviceListMap;

import butterknife.BindView;

/**
 * Created by wm on 2017/11/13.
 */

public class ProjectMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private ProjectMainFrag projectMainFrag;
    private ProjectManagerFrag projectManagerFrag;

    /**
     * 侧面抽屉菜单的回调
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.nav_about:
                AboutActivity.launch(this);break;
            case R.id.nav_manage:
                SettingActivity.launch(this);break;
            case R.id.nav_quit:
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("确认退出?")
                        .setPositiveButton("确认",(dialog,which)->{this.finish();})
                        .setNegativeButton("返回",((dialog, which) ->{dialog.dismiss();}))
                        .show();
        }
        return false;
    }

    @Override
    protected int layoutId() {
        return R.layout.project_main;
    }
    public static void launch(Context context){
        context.startActivity(new Intent(context,ProjectMainActivity.class));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载设备列表和设置界面
        initView();
        //加载左侧侧滑菜单
        initdrawer();
        //开启自动更新服务
        //startService(new Intent(this, XXX.class));

    }
    /**
     * 加载设备列表fragment 设置界面面fragment
     */
    private void initView(){
        mToolbar.setTitle("TOEC_LINKER");
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(v -> turnToMap(this));
        ProjectFragAdapter projectFragAdapter = new ProjectFragAdapter(getSupportFragmentManager());
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        projectMainFrag = new ProjectMainFrag();
        projectMainFrag.setUserId(userId);
        projectManagerFrag = new ProjectManagerFrag();
        projectFragAdapter.addTab(projectMainFrag,"设备检视");
        projectFragAdapter.addTab(projectManagerFrag,"设备管理");
        mViewPager.setAdapter(projectFragAdapter);
        mTablayout.setupWithViewPager(mViewPager,false);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
               if(position == 1){
                   mFab.hide();
               }
               if(position == 0){
                   mFab.show();
               }

            }
        });

    }
    /**
     * 加载左侧侧滑菜单
     */
    private void initdrawer(){
        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
            mNavView.inflateHeaderView(R.layout.nav_header_project);
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                            R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }
    /**
     * 转到地图界面 MapDeviceList
     */
    private void turnToMap(Context context){
        Intent intent = new Intent(context,DeviceListMap.class);
        context.startActivity(intent);
        //切换动画
        overridePendingTransition(R.anim.map_fade_in,R.anim.map_fade_out);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (!CheckDoubleTouch.check()) {
                Util.showShort("再按一次退出");
            } else {
                finish();
            }
        }
    }
}
