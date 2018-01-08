package com.ck.toec.toec_linker.modules.main.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseViewHolder;
import com.ck.toec.toec_linker.base.onItemClickListener;
import com.ck.toec.toec_linker.common.component.AnimRecyclerViewAdapter;
import com.ck.toec.toec_linker.common.component.ImageLoader;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.ck.toec.toec_linker.modules.main.entity.ToecDevice;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wm on 2017/11/14.
 */

public class DeviceListAdapter extends AnimRecyclerViewAdapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<ToecDevice> mdeviceList;
    private onItemClickListener itemListener;
    /**
     *构造时将设备列表传入
     */

    public DeviceListAdapter(List<ToecDevice> deviceList){
        this.mdeviceList = deviceList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_device_card,parent,false);
        v.setOnClickListener(this);
        return new ProjectDeviceListCardHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ProjectDeviceListCardHolder)holder).bind(mdeviceList.get(position));
        //此处可以添加内容的进入场景的动画
        showItemAnim(holder.itemView,position);
        //记录点击位置
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (itemListener!=null){
            itemListener.onItemClick(v,(int)v.getTag());
        }
    }


    //初始化点击接口
    public void setOnItemClickListener(onItemClickListener listener){
        this.itemListener = listener;
    }
    @Override
    public int getItemCount() {
        return mdeviceList==null?0:mdeviceList.size();
    }
    class ProjectDeviceListCardHolder extends BaseViewHolder<ToecDevice>{
        @BindView(R.id.device_image)
        ImageView device_image;
        @BindView(R.id.device_name)
        TextView device_name;
        @BindView(R.id.device_pos)
        TextView device_pos;
        @BindView(R.id.device_parent)
        TextView device_parent;
        @BindView(R.id.online_image)
        ImageButton online_image;
        @BindView(R.id.online_text)
        TextView online_text;

        public ProjectDeviceListCardHolder(View itemView) {
            super(itemView);
        }
        @Override
        protected void bind(ToecDevice toecDevice) {
        //根据是否在线改变按钮颜色和文字
            ToecDevice td = toecDevice;
            if (td!=null){
                if(td.getDeviceState().equalsIgnoreCase("1")){
                    //在线
                    online_image.setBackgroundResource(R.drawable.online);
                    online_text.setText("在线");
                }else{
                    //不在线
                    online_image.setBackgroundResource(R.drawable.offline);
                    online_text.setText("离线");
                }
                //设备名称
                device_name.setText(td.getDeviceName());
                //设备所在地
                device_pos.setText(td.getDevicePos());
                //设备所属项目
                device_parent.setText(td.getDeviceParent());
                //设备图片
                ImageLoader.loadNetImage(itemView.getContext(), Constant.ServerAddr+td.getDeviceImage(),device_image);
            }

        }
    }
}
