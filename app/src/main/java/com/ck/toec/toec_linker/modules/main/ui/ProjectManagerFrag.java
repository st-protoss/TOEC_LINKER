package com.ck.toec.toec_linker.modules.main.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wm on 2017/11/13.
 * 项目管理的fragment
 */

public class ProjectManagerFrag extends BaseFragment {
    View view;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.project_manager_frag,container,false);
            ButterKnife.bind(this,view);
        }
        mIsCreateView = true;
        return view;
    }

    @Override
    protected void lazyLoad() {
        //zzz
    }
}
