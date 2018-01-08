package com.ck.toec.toec_linker.modules.main.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseActivity;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.ck.toec.toec_linker.common.utils.BarSizeUtil;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.common.utils.VersionUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wm on 2017/12/18.
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @Override
    protected int layoutId() {
        return R.layout.side_about;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarSizeUtil.setImmersiveStatusBar(this);
        BarSizeUtil.setImmersiveStatusBarToolbar(mToolbar,this);
        initView();
    }

    private void initView() {
        mToolbar.setTitle("关于TOEC_LINKER");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v->{finish();});
    }
    @OnClick({R.id.bt_report,R.id.bt_getGzNum,R.id.bt_shareUrl,R.id.bt_website,R.id.check_version})
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.bt_report: {
                //发送反馈信息邮件
                sendEmail();
                break;
            }
            case R.id.bt_getGzNum: {
                //将公众号复制到剪贴板
                Util.copyToClipboard("信息安全室", this);
                break;
            }
            case R.id.bt_shareUrl: {
                //分享至其他APP 不用sharedp
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "欢迎使用TOEC_LINKER^_^ \n请在应用商店搜索\n[天津光电物联网]\n或访问http://toec_linker.com获取APP");
                startActivity(Intent.createChooser(sharingIntent, "分享TOEC_LINKER"));
                break;
            }
            case R.id.bt_website: {
                //访问公司网站
                goToHtml("http://toec_linker.com");
                break;
            }
            case R.id.check_version: {
                //检测版本更新
                VersionUtil.checkVersion(this,true);
                break;
            }
        }
    }
    private  void sendEmail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(
                "mailto:" + Constant.EmailAddr));
        this.startActivity(Intent.createChooser(intent, "选择邮箱客户端"));
    }
    private void goToHtml(String url) {
        Uri uri = Uri.parse(url);   //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);           //指定Action
        intent.setData(uri);                            //设置Uri
        startActivity(intent);        //启动Activity
    }
    public static void launch(Context context){
        context.startActivity(new Intent(context,AboutActivity.class));
    }
}
