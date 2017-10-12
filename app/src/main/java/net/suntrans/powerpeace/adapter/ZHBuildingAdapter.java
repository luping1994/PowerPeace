package net.suntrans.powerpeace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.ZHBuildingEntity;

import java.util.List;

/**
 * Created by Looney on 2017/5/23.
 */

public class ZHBuildingAdapter extends BaseExpandableListAdapter {
    private List<ZHBuildingEntity.ZHBuilding> datas;
    private Context mContext;

    public ZHBuildingAdapter(List<ZHBuildingEntity.ZHBuilding> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;

    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).ammeter3.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).ammeter3.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = null;
        GroupHolder groupHolder = null;
        if (convertView != null) {
            view = convertView;
            groupHolder = (GroupHolder) view.getTag(R.id.name);
            view.setTag(R.id.root,groupPosition);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_zh_building_header, parent, false);
            groupHolder = new GroupHolder(view);
            view.setTag(R.id.name,groupHolder);
            view.setTag(R.id.root,groupPosition);
        }
        groupHolder.setData(groupPosition);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        ChildHolder holder = null;
        if (convertView != null) {
            view = convertView;
            holder = (ChildHolder) view.getTag();
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_zh_building, parent, false);
            holder = new ChildHolder(view);
            view.setTag(holder);
        }
        holder.setData(groupPosition, childPosition);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public class GroupHolder {
        TextView mName;
        Button button;

        public GroupHolder(View view) {
            mName = (TextView) view.findViewById(R.id.headerName);
            button = (Button) view.findViewById(R.id.button);
        }

        public void setData(final int groupPosition) {

            mName.setText(datas.get(groupPosition).title);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onChildClickListener!=null)
                        onChildClickListener.onGroupButtonClick(groupPosition);
                }
            });

        }
    }

    public class ChildHolder {
        TextView mText;
        View root;

        public ChildHolder(View view) {
            mText = (TextView) view.findViewById(R.id.name);
            root = view.findViewById(R.id.root);
        }

        public void setData(final int groupPosition, final int childPosition) {
            mText.setText(datas.get(groupPosition).ammeter3.get(childPosition).name);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onChildClickListener!=null)
                        onChildClickListener.onChildClick(groupPosition,childPosition);
                }
            });
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }


    private OnChild$ParentClickListener onChildClickListener;

    public void setOnChildClickListener(OnChild$ParentClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }

    public interface OnChild$ParentClickListener{
        void onChildClick(int grouPposition,int childPosition);
        void onGroupButtonClick(int grouPposition);
    }
}
