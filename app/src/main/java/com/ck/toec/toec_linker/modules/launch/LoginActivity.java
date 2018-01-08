package com.ck.toec.toec_linker.modules.launch;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.common.utils.SharedPreferenceUtil;
import com.ck.toec.toec_linker.modules.launch.entity.Login;
import com.ck.toec.toec_linker.modules.main.ui.ProjectMainActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wm on 2017/11/13.
 */

public class LoginActivity extends Activity{
    @BindView(R.id.login_status)
    TextView tv;
    @BindView(R.id.login_ctrl)
    Button btn;

    private String userName;
    private String userPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkIsLogin()){
            setContentView(R.layout.login_hold);
            ButterKnife.bind(this);
            Login();
        }else{
            startActivity(new Intent(this,VideoLoginActivity.class));
            finish();
        }


    }

    private Boolean checkIsLogin() {
        userName = SharedPreferenceUtil.getInstance().getString("userName","");
        userPassword = SharedPreferenceUtil.getInstance().getString("userPassword","");
        if (userName.equals("")||userPassword.equals("")){
            return false;
        }else{
            return true;
        }
    }
    private void Login(){
        RetrofitUtil.getInstance().loginCheck(userName,userPassword)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Login>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //播放登录动画 旋转
                        tv.setText("登录中...");
                    }

                    @Override
                    public void onNext(@NonNull Login login) {
                        //登录成功跳转到工程主界面
                        if (login.isAllowed()){
                            tv.setText("登陆成功！");
                            Observable.timer(1,TimeUnit.SECONDS)
                                    .subscribe(a->{
                                        Intent intent = new Intent(LoginActivity.this,
                                                ProjectMainActivity.class);
                                        intent.putExtra("userId",login.getId());
                                        startActivity(intent);
                                        finish();
                                    });

                        }else{
                            tv.setText("登录信息已经失效！请重新登录");
                            SharedPreferenceUtil.getInstance().putString("userName","")
                                    .putString("userPassword","");
                            Observable.timer(1,TimeUnit.SECONDS)
                                    .subscribe(a->{
                                        startActivity(new Intent(LoginActivity.this,VideoLoginActivity.class));
                                        finish();
                                    });
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        //登录失败 跳转到视频登录页面 清空登录信息 重新登录
                        tv.setText("网络连接异常，登录失败！");
                        btn.setVisibility(View.VISIBLE);
                        btn.setText("退出");
                        btn.setOnClickListener(view->{finish();});
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
