package com.ck.toec.toec_linker.modules.launch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ck.toec.toec_linker.R;

import butterknife.BindView;

/**
 * Created by wm on 2017/11/22.
 */

public class LoginFormView extends LinearLayout {
    @BindView(R.id.login_username)
    EditText login_name;
    @BindView(R.id.login_password)
    EditText login_password;

    public LoginFormView(Context context) {
        super(context);
        loadview();
    }

    public LoginFormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadview();
    }

    public LoginFormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadview();
    }
    private void loadview(){
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.login_form,this);
    }
}
