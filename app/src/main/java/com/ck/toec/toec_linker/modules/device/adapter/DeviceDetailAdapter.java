package com.ck.toec.toec_linker.modules.device.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseViewHolder;
import com.ck.toec.toec_linker.common.component.AnimRecyclerViewAdapter;
import com.ck.toec.toec_linker.modules.device.entity.AlarmData;
import com.ck.toec.toec_linker.modules.device.entity.DetailPage;
import com.ck.toec.toec_linker.modules.device.entity.DeviceRtRule;
import com.ck.toec.toec_linker.modules.device.entity.HisRealdata;
import com.ck.toec.toec_linker.modules.device.entity.RtHistory;
import com.ck.toec.toec_linker.modules.device.entity.mListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wm on 2017/12/1.
 */

public class DeviceDetailAdapter extends AnimRecyclerViewAdapter<RecyclerView.ViewHolder>{
    private static final int RT_DATA = 0;
    private static final int LINECHART = 1;
    private static final int HISTORY = 2;
    private static final int ALARM = 3;
    private DeviceRtRule rtData;

    private DetailPage<RtHistory> hisPage;
    private DetailPage<AlarmData> alarmPage;
    private List<RtHistory> rtHistories;
    private List<HisRealdata> hisRealdatas;
    private List<AlarmData> alarmDatas;
    //列表的总页数
    private int hisNum=1;
    private int alarmNum;
    //历史数据列表的当前页数
    private int hisCNum = 1;
    private  int alarmCNum = 1;
    //局部刷新接口
    private DetailRefresh detailRefresh;


    @Override
    public int getItemViewType(int position) {
       if(position==DeviceDetailAdapter.RT_DATA){
           return DeviceDetailAdapter.RT_DATA;
       }
       if(position==DeviceDetailAdapter.LINECHART){
           return DeviceDetailAdapter.LINECHART;
       }
       if (position==DeviceDetailAdapter.HISTORY){
           return DeviceDetailAdapter.HISTORY;
       }
       if(position==DeviceDetailAdapter.ALARM){
           return  DeviceDetailAdapter.ALARM;
       }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch(position){
            case DeviceDetailAdapter.RT_DATA:
                ((RtHolder)holder).bind(rtData);
                break;
            case DeviceDetailAdapter.LINECHART:
                ((LineChartHolder)holder).bind(hisRealdatas);
                break;
            case DeviceDetailAdapter.HISTORY:
                ((HistoryHolder)holder).bind(rtHistories);
                break;
            case DeviceDetailAdapter.ALARM:
                ((AlarmHolder)holder).bind(alarmDatas);
                break;
        }
        //列表出现动画
        showItemAnim(holder.itemView,position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType){
            case DeviceDetailAdapter.RT_DATA:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_rtdata_card,parent,false);
                return new RtHolder(v);
            case DeviceDetailAdapter.LINECHART:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_linechart_card,parent,false);
                return new LineChartHolder(v);
            case DeviceDetailAdapter.HISTORY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_history_card,parent,false);
                return new HistoryHolder(v);
            case DeviceDetailAdapter.ALARM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_alarm_card,parent,false);
                return new AlarmHolder(v);
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return 4;
    }

    /**
     * 停止局部刷新动画
     * @param rtData
     */


    public void setRtData(DeviceRtRule rtData) {
        this.rtData = rtData;
    }

    public void setRtHistories(List<HisRealdata> hisRealdatas) {
        this.hisRealdatas = hisRealdatas;
    }

    public void setAlarmDatas(List<AlarmData> alarmDatas) {
        this.alarmDatas = alarmDatas;
    }

    public void setDetailRefresh(DetailRefresh detailRefresh) {
        this.detailRefresh = detailRefresh;
    }

    public void setHisPage(DetailPage<RtHistory> hisPage) {
        this.hisPage = hisPage;
        this.rtHistories = hisPage.getList();
        this.hisNum = hisPage.getTotalPage();
        if (hisNum==0){
            hisNum=1;
        }
    }

    public void setAlarmPage(DetailPage<AlarmData> alarmPage) {
        this.alarmPage = alarmPage;
        this.alarmDatas = alarmPage.getList();
        this.alarmNum = alarmPage.getTotalPage();
        if (alarmNum==0){
            alarmNum=1;
        }
    }

    public void setHisNum(int hisNum) {
        this.hisNum = hisNum;
    }

    public void setAlarmNum(int alarmNum) {
        this.alarmNum = alarmNum;
    }

    /**
     * 实时数据卡片
     */
    class RtHolder extends BaseViewHolder<DeviceRtRule>{

        @BindView(R.id.rt_data)
        TextView rt_data;
        @BindView(R.id.rt_unit)
        TextView rt_unit;
        @BindView(R.id.rt_refresh)
        ImageView rt_refresh;
        public RtHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(DeviceRtRule deviceRtRule) {
            if (deviceRtRule!=null){
                rt_data.setText(deviceRtRule.getRtData());
                rt_unit.setText(deviceRtRule.getRtUnit());
            }else{
                rt_data.setText("暂无数据");
            }

            rt_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  detailRefresh.rtRefresh();
                }
            });
        }
    }
    /**
     * 折线图卡片
     */
    class LineChartHolder extends  BaseViewHolder<List<HisRealdata>>{
        @BindView(R.id.web_linechart)
        WebView wv;
        @OnClick({R.id.recent_day,R.id.recent_week,R.id.recent_month})
        public void OnClick(View v){
            switch (v.getId()){
                case R.id.recent_day:
                    detailRefresh.lineRefresh("1");
                    break;
                case R.id.recent_week:
                    detailRefresh.lineRefresh("2");
                    break;
                case R.id.recent_month:
                    detailRefresh.lineRefresh("3");
                    break;
            }
        }

        public LineChartHolder(View itemView) {
            super(itemView);
        }

        private class mWebViewClient extends WebViewClient{
            ArrayList<String> timeLine ;
            ArrayList<String> dataLine ;
            String jsonData;
            String a ="1";
            String b ="2";
            public mWebViewClient(ArrayList<String> timeLine ,ArrayList<String>dataline){
                this.dataLine = dataline;
                this.timeLine = timeLine;
            }
            public mWebViewClient(String jsonData){
                this.jsonData = jsonData;
            }
            @SuppressLint("JavascriptInterface")
            @Override
            public void onPageFinished(WebView view, String url) {
               // view.loadUrl("javascript: test()");
               // view.loadUrl("javascript:setOptions(" + timeLine + ","+ dataLine + ")");
                //view.loadUrl("javascript:setOptions("+ a + ","+b+ ")");
                view.loadUrl("javascript: showChart("+jsonData+")");
            }
        }
        @SuppressLint("JavascriptInterface")
        @Override
        protected void bind(List<HisRealdata> rtHistories) {
            ArrayList<String> timeLine = new ArrayList<>();
            ArrayList<String> dataLine = new ArrayList<>();
            String jsonData="";
            if (rtHistories!=null){
                for (HisRealdata a:rtHistories) {
                    timeLine.add(a.getcREATEDT());
                    dataLine.add(a.getdATA1());
                }
                Gson a = new Gson();
                jsonData = a.toJson(rtHistories);
                //wv.loadUrl("javascript:setOptions("+timeLine+","+dataLine+")");
                // wv.loadUrl("javascript: test()");
            }else{
            }
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setUseWideViewPort(true);
            wv.getSettings().setSupportZoom(false);
            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setLoadWithOverviewMode(true);
            wv.loadUrl("file:///android_asset/echarts.html");
            wv.setWebViewClient(new mWebViewClient(jsonData));


        }
    }
    /**
     * 历史数据卡片
     */
    class HistoryHolder extends  BaseViewHolder<List<RtHistory>>{
        @BindView(R.id.history_chart)
        mListView history_chart;
        @OnClick({R.id.previous_page,R.id.next_page})
        public void onClickPage(View v){
            switch (v.getId()){
                case R.id.previous_page:
                    if(hisCNum>=2){
                        hisCNum-=1;
                    }else {
                        hisCNum=1;
                    }
                    break;
                case R.id.next_page:
                    if(hisCNum+1<=hisNum)
                    hisCNum++;
                    break;
            }
            //请求翻页数据并刷新列表
            detailRefresh.chartRefresh(hisCNum);
        }
        @BindView(R.id.page_num)
        TextView page_num;
        @BindView(R.id.total_num)
        TextView total_num;
        HisChartAdapter madapter;

        public HistoryHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(List<RtHistory> rtHistories) {
            madapter = new HisChartAdapter(rtHistories,itemView.getContext());
            history_chart.setAdapter(madapter);
            //显示当前页数
            page_num.setText(String.valueOf(hisCNum));
            //显示总页数
            total_num.setText("/"+String.valueOf(hisNum));

        }
    }
    /**
     * 报警列表卡片
     */
    class AlarmHolder extends BaseViewHolder<List<AlarmData>>{
        @BindView(R.id.history_chart)
        mListView history_chart;
        @OnClick({R.id.previous_page,R.id.next_page})
        public void onClickPage(View v){
            switch (v.getId()){
                case R.id.previous_page:
                    if(alarmCNum>=2){
                        alarmCNum-=1;
                    }else {
                        alarmCNum=1;
                    }
                    break;
                case R.id.next_page:
                    if(alarmCNum+1<=alarmNum)
                        alarmCNum++;
                    break;
            }
            //请求翻页数据并刷新列表
            detailRefresh.alarmRefresh(alarmCNum);
        }
        @BindView(R.id.page_num)
        TextView page_num;
        @BindView(R.id.total_num)
        TextView total_num;
        AlarmChartAdapter madapter;
        public AlarmHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bind(List<AlarmData> alarmDatas) {
            madapter = new AlarmChartAdapter(alarmDatas,itemView.getContext());
            history_chart.setAdapter(madapter);
            //显示当前页数
            page_num.setText(String.valueOf(alarmCNum));
            //显示总页数
            total_num.setText("/"+String.valueOf(alarmNum));
        }
    }
}
