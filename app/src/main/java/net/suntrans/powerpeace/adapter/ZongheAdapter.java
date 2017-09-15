package net.suntrans.powerpeace.adapter;

import android.content.ContextWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suntrans.powerpeace.R;

import java.util.Map;

/**
 * Created by Looney on 2017/9/15.
 */

public class ZongheAdapter extends RecyclerView.Adapter {
    private Map<String,String> datas;
    private ContextWrapper mContext;

    public ZongheAdapter(ContextWrapper context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = null;
        if (viewType ==1){
            viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_suhe_header,parent,false));
        }else if (viewType ==0){
            viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sushe,parent,false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)
            return 1;
        return 0;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
