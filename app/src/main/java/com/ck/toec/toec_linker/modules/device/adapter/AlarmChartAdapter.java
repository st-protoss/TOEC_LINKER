package com.ck.toec.toec_linker.modules.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.modules.device.entity.AlarmData;
import com.ck.toec.toec_linker.modules.device.entity.RtHistory;

import java.util.List;

/**
 * Created by wm on 2017/12/5.
 * 警报List的适配器
 */

public class AlarmChartAdapter extends BaseAdapter{
    private List<AlarmData> mList;
    private Context mContext;
    public AlarmChartAdapter(List<AlarmData> list, Context context){
        this.mList = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mList==null||mList.size()==1?2:mList.size();
    }

    @Override
    public AlarmData getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_history_chart,parent,false);
        TextView data = (TextView)convertView.findViewById(R.id.history_data);
        TextView time = (TextView)convertView.findViewById(R.id.history_date);
        if (position==0){
            data.setText("报警数值");
            data.setTextColor(convertView.getResources().getColor(R.color.alarm_chart_title));
            time.setText("报警时间");
            time.setTextColor(convertView.getResources().getColor(R.color.alarm_chart_title));
        }else{
            AlarmData alarmData = mList.get(position-1);
            data.setText(alarmData.getAlData());
            time.setText(alarmData.getAlDate());
        }
        return null;
    }
}
