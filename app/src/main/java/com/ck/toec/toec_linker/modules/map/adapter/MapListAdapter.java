package com.ck.toec.toec_linker.modules.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.modules.main.entity.ToecDevice;

import java.util.List;

/**
 * Created by wm on 2017/12/14.
 * 地图界面弹出设备列表dialog的列表适配器
 */

public class MapListAdapter extends BaseAdapter {
    private List<ToecDevice> toecDevices;
    private Context context;
    private int currentPos = -1;

    public MapListAdapter (List<ToecDevice> toecDevices , Context context){
        this.toecDevices = toecDevices;
        this.context = context;
    }
    @Override
    public int getCount() {
        return toecDevices==null?1:toecDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return toecDevices==null?1:toecDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.map_bottom_list,null);
        TextView tv = (TextView)convertView.findViewById(R.id.device_name);
        ImageView iv = (ImageView)convertView.findViewById(R.id.checked);
        if (toecDevices==null&&position==0){
            tv.setText("暂无数据");
            return convertView;
        }
        tv.setText(toecDevices.get(position).getDeviceName());
        if(currentPos>=0){
            if(position==currentPos){
                iv.setVisibility(View.VISIBLE);
                tv.setTextColor(context.getResources().getColor(R.color.alarm_chart_title));
            }
        }
        return convertView;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }
}
