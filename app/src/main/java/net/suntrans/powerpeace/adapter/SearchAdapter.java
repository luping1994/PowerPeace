package net.suntrans.powerpeace.adapter;

import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.SearchInfoEntity;
import net.suntrans.powerpeace.ui.activity.StudentInfoActivity;
import net.suntrans.powerpeace.ui.activity.SusheDetailActivity;

import java.util.List;

/**
 * Created by Looney on 2017/9/28.
 */

public class SearchAdapter extends RecyclerView.Adapter {
    public static final int SEARCH_STU = 1;
    public static final int SEARCH_ROOM = 2;
    private List<SearchInfoEntity.SearchInfo> datas;
    private int stuCount;
    private ContextWrapper context;

    public SearchAdapter(List<SearchInfoEntity.SearchInfo> datas, ContextWrapper context) {
        this.datas = datas;
        this.context = context;
    }

    public int getStuCount() {
        return stuCount;
    }

    public void setStuCount(int stuCount) {
        this.stuCount = stuCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_stu, parent, false);
        StuViewHolder holder = new StuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((StuViewHolder) holder).setData(position);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    class StuViewHolder extends RecyclerView.ViewHolder {

        TextView stuName;
        TextView xuehao;
        TextView location;
        TextView header;
        ImageView image;

        public StuViewHolder(View itemView) {
            super(itemView);
            stuName = (TextView) itemView.findViewById(R.id.stuName);
            xuehao = (TextView) itemView.findViewById(R.id.xuehao);
            location = (TextView) itemView.findViewById(R.id.location);
            header = (TextView) itemView.findViewById(R.id.header);
            image = (ImageView) itemView.findViewById(R.id.image);

            itemView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition()==-1){
                        return;
                    }
                    if (datas.get(getAdapterPosition()).type == SEARCH_STU) {
                        Intent intent1 = new Intent(context, StudentInfoActivity.class);
                        intent1.putExtra("name", datas.get(getAdapterPosition()).name);
                        intent1.putExtra("studentID", datas.get(getAdapterPosition()).studentID);
                        context.startActivity(intent1);
                    } else {
                        Intent intent = new Intent(context, SusheDetailActivity.class);
                        intent.putExtra("title", datas.get(getAdapterPosition()).room_sn);
                        intent.putExtra("room_id", datas.get(getAdapterPosition()).room_id);
                        intent.putExtra("whole_name", datas.get(getAdapterPosition()).building_name + "-" + datas.get(getAdapterPosition()).room_id);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void setData(int position) {
            if (datas.get(position).type == SEARCH_STU) {
                header.setText("学生");
                xuehao.setText(datas.get(position).studentID);
                xuehao.setVisibility(View.VISIBLE);
                stuName.setText(datas.get(position).name);
                image.setImageResource(R.drawable.ic_person);
            } else {
                header.setText("宿舍");
                xuehao.setVisibility(View.INVISIBLE);
                stuName.setText(datas.get(position).room_sn);
                image.setImageResource(R.drawable.ic_sushe);

            }
            location.setText(datas.get(position).building_name + "-" + datas.get(position).room_sn);
            if (position == 0 || position == stuCount ) {
                header.setVisibility(View.VISIBLE);
            } else {
                header.setVisibility(View.GONE);
            }
        }
    }


}
