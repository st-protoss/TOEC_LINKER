package com.ck.toec.toec_linker.modules.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.modules.device.entity.RtHistory;

import java.util.List;

/**
 * Created by wm on 2017/12/5.
 * 历史表格List的适配器
 */

public class HisChartAdapter extends BaseAdapter{
    private List<RtHistory> mList;
    private Context mContext;
    public HisChartAdapter(List<RtHistory> list, Context context){
        this.mList = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mList==null||mList.size()==1?2:mList.size();
    }

    @Override
    public RtHistory getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_history_chart,parent,false);

        }
        TextView data = (TextView)convertView.findViewById(R.id.history_data);
        TextView time = (TextView)convertView.findViewById(R.id.history_date);
        if (position==0){
            data.setText("数值记录");
            data.setTextColor(convertView.getResources().getColor(R.color.history_chart_title));
            time.setText("记录时间");
            time.setTextColor(convertView.getResources().getColor(R.color.history_chart_title));
        }else{
            if (mList==null){
                data.setText("暂无数据");
                time.setText("暂无数据");
                return convertView;
            }
            RtHistory rtHistory = mList.get(position-1);
            data.setText(rtHistory.getRtData());
            time.setText(rtHistory.getRtDate());
        }
        return convertView;
    }

}
