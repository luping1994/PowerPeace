package net.suntrans.powerpeace.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.powerpeace.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Looney on 2017/9/13.
 */

public class NavViewAdapter extends BaseQuickAdapter<NavViewItem,BaseViewHolder>{


    public NavViewAdapter(@LayoutRes int layoutResId, @Nullable List<NavViewItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NavViewItem item) {
        helper.setText(R.id.msg,item.getName());
        ImageView view = helper.getView(R.id.image);
        view.setImageResource(item.getImageResID());
    }

    public static List<NavViewItem> getItems(){
        List<NavViewItem> navViewItems = new ArrayList<>();
        NavViewItem item0 = new NavViewItem(R.drawable.ic_home,"主页");
        NavViewItem item1 = new NavViewItem(R.drawable.ic_msg,"消息");
        NavViewItem item2 = new NavViewItem(R.drawable.ic_setting,"设置");
        NavViewItem item3 = new NavViewItem(R.drawable.ic_info_nav,"关于");
        NavViewItem item4 = new NavViewItem(R.drawable.internet,"官网");
        NavViewItem item5= new NavViewItem(R.drawable.ic_jianyi,"帮助与反馈");
        NavViewItem item6 = new NavViewItem(R.drawable.ic_exit,"退出");

        navViewItems.add(item0);
        navViewItems.add(item1);
        navViewItems.add(item4);
        navViewItems.add(item2);
        navViewItems.add(item5);
        navViewItems.add(item3);
        navViewItems.add(item6);
        return navViewItems;
    }

    public static int getLayoutRes(){
        return R.layout.item_nav;
    }
}
