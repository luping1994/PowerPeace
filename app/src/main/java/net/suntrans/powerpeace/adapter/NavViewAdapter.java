package net.suntrans.powerpeace.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.suntrans.powerpeace.R;

import java.util.ArrayList;
import java.util.List;

import static com.pgyersdk.c.a.n;

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
        NavViewItem item0 = new NavViewItem(R.drawable.ic_message,"我的消息");
        NavViewItem item1 = new NavViewItem(R.drawable.ic_help,"帮助中心");
        NavViewItem item2 = new NavViewItem(R.drawable.ic_feedback,"问题反馈");
        NavViewItem item3 = new NavViewItem(R.drawable.ic_about,"关于");
        navViewItems.add(item0);
        navViewItems.add(item1);
        navViewItems.add(item2);
        navViewItems.add(item3);
        return navViewItems;
    }

    public static int getLayoutRes(){
        return R.layout.item_nav;
    }
}
