package com.ck.toec.toec_linker.modules.launch;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseActivity;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.common.utils.SharedPreferenceUtil;
import com.ck.toec.toec_linker.modules.launch.entity.Login;
import com.ck.toec.toec_linker.modules.main.ui.ProjectMainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2017/11/21.
 */

public class VideoLoginActivity extends BaseActivity {
    @BindView(R.id.login_video)
    LoginVideoView mVideoView;
    @BindView(R.id.login_title)
    TextView tv;
    @BindView(R.id.start_login)
    Button start_login;
    @BindView(R.id.btn_login)
    Button leftBtn;
    @BindView(R.id.btn_exit)
    Button rightBtn;
    @BindView(R.id.login_form)
    LoginFormView lf;
    @BindView(R.id.login_btn)
    LinearLayout login_btn;
    @BindView(R.id.login_username)
    EditText userName;
    @BindView(R.id.login_password)
    EditText userPass;
    @BindView(R.id.login_status)
    TextView tv1;
public String VIDEO_NAME = "login.mp4";
    @Override
    protected int layoutId() {
        return R.layout.login_video;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        File videoFile = copyVideoFile();
        //播放动画
        mVideoView.setVideoPath(videoFile.getPath());
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
        //使隐藏的按钮变得不可用 添加监听
        leftBtn.setEnabled(false);
        rightBtn.setEnabled(false);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tUserName = String.valueOf(userName.getText());
                String tUserPass = String.valueOf(userPass.getText());
                if (tUserName.equals("")||tUserPass.equals("")){
                    tv1.setText("用户名和密码不能为空！");
                }else{
                    RetrofitUtil.getInstance().loginCheck(tUserName,tUserPass)
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<Login>() {
                                @Override
                                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                }

                                @Override
                                public void onNext(@io.reactivex.annotations.NonNull Login login) {
                                    if (login.isAllowed()){
                                        tv1.setText("登陆成功！");
                                        SharedPreferenceUtil.getInstance().putString("userName",tUserName)
                                                .putString("userPassword",tUserPass);
                                        Observable.timer(1, TimeUnit.SECONDS)
                                                .subscribe(a->{
                                                    Intent intent = new Intent(VideoLoginActivity.this,
                                                            ProjectMainActivity.class);
                                                    intent.putExtra("userId",login.getId());
                                                    startActivity(intent);
                                                    finish();
                                                });

                                    }else{
                                        tv1.setText("用户名密码错误！");
                                    }
                                }

                                @Override
                                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                    tv1.setText("登录失败！");
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                }
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击开始使用
        start_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //其本身消失
                start_login.animate().alpha(0).setDuration(500).start();
                start_login.setVisibility(View.GONE);
               /* int Ydis_lf = lf.getHeight()+lf.getTop();
                int Xdis_lb = leftBtn.getWidth()+leftBtn.getLeft();
                leftBtn.animate().alpha(1).setDuration(500).start();
                int Xdis_rb = rightBtn.getWidth()+rightBtn.getRight();
                rightBtn.animate().alpha(1).setDuration(500).start();*/
                login_btn.animate().alpha(1).setDuration(500).start();
                lf.animate().alpha(1).setDuration(500).start();

                rightBtn.setEnabled(true);
                leftBtn.setEnabled(true);

            }
        });

    }
    @NonNull
    private File copyVideoFile() {
        File videoFile;
        try {
            FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
            InputStream in = getResources().openRawResource(R.raw.login_video);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists())
            throw new RuntimeException("video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
        return videoFile;
    }

    @Override
    protected void onDestroy() {
        if (mVideoView.isPlaying())
        mVideoView.stopPlayback();
        super.onDestroy();
    }
}
