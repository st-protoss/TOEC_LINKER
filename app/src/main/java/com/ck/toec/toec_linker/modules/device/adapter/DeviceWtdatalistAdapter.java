package com.ck.toec.toec_linker.modules.device.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseViewHolder;
import com.ck.toec.toec_linker.common.component.AnimRecyclerViewAdapter;
import com.ck.toec.toec_linker.common.component.ImageLoader;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.ck.toec.toec_linker.modules.device.entity.DeviceWtRule;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by toec on 2017/11/17.
 */

public class DeviceWtdatalistAdapter extends AnimRecyclerViewAdapter<RecyclerView.ViewHolder>{
    private List<DeviceWtRule> deviceWtRules;
    public DeviceWtdatalistAdapter(List<DeviceWtRule> deviceWtRules){
        this.deviceWtRules = deviceWtRules;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeviceWtCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.device_wtdata_card,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DeviceWtCardHolder)holder).bind(deviceWtRules.get(position));
    }

    @Override
    public int getItemCount() {
        return deviceWtRules==null?0:deviceWtRules.size();
    }
    class DeviceWtCardHolder extends BaseViewHolder<DeviceWtRule>{
        @BindView(R.id.wt_on_text)
        TextView wt_on;
        @BindView(R.id.wt_off_text)
        TextView wt_off;
        @BindView(R.id.wt_btn_write)
        ImageView wt_btn_write;
        @BindView(R.id.wt_name)
        TextView wt_name;
        @BindView(R.id.wt_on)
        RadioButton wt_btn_on;
        @BindView(R.id.wt_off)
        RadioButton wt_btn_off;
        @BindView(R.id.wt_image)
        ImageView wt_image;

        private String wtId;

        public DeviceWtCardHolder(View itemView) {
            super(itemView);
        }


        @Override
        protected void bind(DeviceWtRule deviceWtRule) {
            DeviceWtRule wt = deviceWtRule;
            if(wt!=null){
                wt_name.setText(wt.getWtName());
                wt_on.setText(wt.getContent0());
                wt_off.setText(wt.getContent1());
                ImageLoader.loadNetImage(itemView.getContext(),Constant.ServerAddr+wt.getWtImage(),wt_image);
                setWtId(wt.getWtID());
            }
        }
        @OnClick({R.id.wt_btn_write})
        public void onWrite(View v){
            if(wt_btn_on.isChecked()){
                writeData(wtId,1);
            }
            if(wt_btn_off.isChecked()){
                writeData(wtId,0);
            }
        }
        /**
         * 下发数据方法
         */
        private void writeData(String ID , int status){
            RetrofitUtil.getInstance()
                    .writeCommand(ID,status)
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            //写入数据时界面 按钮开始转动 显示正在写入
                        }

                        @Override
                        public void onNext(@NonNull String s) {
                            //写入数据 服务器响应时界面 按钮停止转动 显示命令下发成功/失败
                            if (s.equals("1")){
                                Snackbar.make(itemView,"下发指令成功",Snackbar.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            //写入失败  按钮停止转动 显示网络错误
                            Snackbar.make(itemView,"指令下发失败",Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                           //doNothing
                        }
                    });

        }


        public void setWtId(String wtId) {
            this.wtId = wtId;
        }
    }
}
