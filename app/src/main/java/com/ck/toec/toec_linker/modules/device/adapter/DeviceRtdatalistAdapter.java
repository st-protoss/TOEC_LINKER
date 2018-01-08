package com.ck.toec.toec_linker.modules.device.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseViewHolder;
import com.ck.toec.toec_linker.base.onItemClickListener;
import com.ck.toec.toec_linker.common.component.AnimRecyclerViewAdapter;
import com.ck.toec.toec_linker.common.component.ImageLoader;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.ck.toec.toec_linker.modules.device.entity.DeviceRtRule;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;

/**
 * Created by toec on 2017/11/17.
 */

public class DeviceRtdatalistAdapter extends AnimRecyclerViewAdapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private List<DeviceRtRule> rt_data_list;
    private onItemClickListener mlistener;
    public DeviceRtdatalistAdapter(List<DeviceRtRule> rt_data_list){
       this.rt_data_list = rt_data_list;
   }
   public void setOnViewClickedListener(onItemClickListener listener){
       this.mlistener = listener;
   }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_rtdata_card,parent,false);
        v.setOnClickListener(this);
        return new DeviceRtCardHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DeviceRtCardHolder)holder).bind(rt_data_list.get(position));
        //记录view的位置
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return rt_data_list==null?0:rt_data_list.size();
    }

    @Override
    public void onClick(View v) {
        if (mlistener!=null){
            mlistener.onItemClick(v,(int)v.getTag());
        }
    }

    class DeviceRtCardHolder extends BaseViewHolder<DeviceRtRule>{
        @BindView(R.id.device_rt_image)
        ImageView device_rt_image;
        @BindView(R.id.rt_name)
        TextView rt_name;
        @BindView(R.id.rt_data)
        TextView rt_data;
        @BindView(R.id.rt_unit)
        TextView rt_unit;
        DeviceRtCardHolder(View view){
            super(view);
        }


        @Override
        protected void bind(DeviceRtRule deviceRtRule) {
            DeviceRtRule rt = deviceRtRule;
            if(rt!=null){
                rt_name.setText(rt.getRtName());
                rt_unit.setText(rt.getRtUnit());
                rt_data.setText(rt.getRtData());
                ImageLoader.loadNetImage(itemView.getContext(), Constant.ServerAddr+rt.getRtImage(),device_rt_image);
            }

        }
    }
}
