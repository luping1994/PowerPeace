package net.suntrans.powerpeace.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.suntrans.looney.widgets.SwitchButton;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.ChannelInfo;

import static net.suntrans.looney.utils.UiUtils.getContext;

import android.content.Context;

import java.util.List;

/**
 * Created by Looney on 2017/10/6.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

    private Context context;
    private List<ChannelInfo> datas;

    public ChannelAdapter(Context context, List<ChannelInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public ChannelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_channel_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    public void setListener(onSwitchClickListener listener) {
        this.listener = listener;
    }

    private onSwitchClickListener listener;
    public interface onSwitchClickListener{
        void onSwitchClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private SwitchButton button;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            button = (SwitchButton) itemView.findViewById(R.id.switchButton);
            name = (TextView) itemView.findViewById(R.id.name);
            itemView.findViewById(R.id.switchRl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null)
                        listener.onSwitchClick(getAdapterPosition());
                }
            });
        }

        public void setData(int position) {
            button.setCheckedImmediately(datas.get(position).status.equals("1")?true:false);
            name.setText(datas.get(position).name);
        }
    }
}
