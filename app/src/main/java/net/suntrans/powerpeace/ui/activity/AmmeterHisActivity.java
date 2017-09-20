package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.HisEntity;
import net.suntrans.powerpeace.chart.MyAxisValueFormatter;
import net.suntrans.powerpeace.databinding.ActivityAmmeterHisBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.utils.DateUtils;
import net.suntrans.powerpeace.utils.StatusBarCompat;
import net.suntrans.stateview.StateView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static net.suntrans.powerpeace.R.id.segmented_group;

/**
 * Created by Looney on 2017/9/7.
 */

public class AmmeterHisActivity extends BasedActivity {

    private static final java.lang.String TAG = "AmmeterHisActivity";
    private static final String DISPLAY_WEEK = "WEEK";
    private static final String DISPLAY_DAY = "DAY";
    private static final String DISPLAY_MONTH = "MONTH";

    private ActivityAmmeterHisBinding binding;
    private String room_id;
    private String paramName;
    private Map<String, String> dictionaries;
    private String mDisplayType = DISPLAY_DAY;
    private HisEntity data;
    private MyAdapter adapter;
    private StateView stateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ammeter_his);

        StatusBarCompat.compat(binding.headerView);


        room_id = getIntent().getStringExtra("room_id");
        paramName = getIntent().getStringExtra("paramName");
        binding.toolbar.setTitle(getIntent().getStringExtra("title") + paramName);
        stateView = StateView.inject(binding.content);

        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        initData();
        initChart();
        adapter = new MyAdapter(R.layout.item_his, datas);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DefaultDecoration());
        binding.headerTitle.setText(paramName + dictionaries.get(paramName + "Unit"));
        binding.radio0.setChecked(true);
        binding.segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        mDisplayType = DISPLAY_DAY;
                        break;
                    case R.id.radio1:
                        mDisplayType = DISPLAY_WEEK;
                        break;
                    case R.id.radio2:
                        mDisplayType = DISPLAY_MONTH;
                        break;
                }
                setData(data);
            }
        });
    }

    private void initData() {
        dictionaries = new HashMap<>();
        dictionaries.put("电压", "1");
        dictionaries.put("电压Unit", "(V)");

        dictionaries.put("电流Unit", "(A)");
        dictionaries.put("电流", "2");

        dictionaries.put("功率", "3");
        dictionaries.put("功率Unit", "(W)");

        dictionaries.put("功率因数", "4");
        dictionaries.put("功率因数Unit", "");

        dictionaries.put("电表值", "5");
        dictionaries.put("电表值Unit", "(度)");

        dictionaries.put("用电量", "6");
        dictionaries.put("用电量Unit", "(度)");


        dictionaries.put(DISPLAY_DAY, "最近24小时");
        dictionaries.put(DISPLAY_MONTH, "最近一月");
        dictionaries.put(DISPLAY_WEEK, "最近一周");
    }

    private void initChart() {

//        mChart.setOnChartGestureListener(this);
//        binding.mChart.setOnChartValueSelectedListener(this);
        binding.mChart.setDrawGridBackground(false);
        binding.mChart.setNoDataText("暂无数据...");

        // no description text
        binding.mChart.getDescription().setEnabled(false);

        // enable touch gestures
        binding.mChart.setTouchEnabled(true);

        // enable scaling and dragging
        binding.mChart.setDragEnabled(true);
        binding.mChart.setScaleEnabled(true);

        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        binding.mChart.setPinchZoom(true);
        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
//        LimitLine llXAxis = new LimitLine(10f, "Index 10");
//        llXAxis.setLineWidth(4f);
//        llXAxis.enableDashedLine(10f, 10f, 0f);
//        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        llXAxis.setTextSize(10f);

        /**
         * 设置x轴
         */
        XAxis xAxis = binding.mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
//        xAxis.setAxisMaximum(30);
//        xAxis.setAxisMinimum(0);
        xAxis.setDrawGridLines(true);
        xAxis.setGridLineWidth(1f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

//        LimitLine ll1 = new LimitLine(150f, "Upper Limit");
//        ll1.setLineWidth(4f);
//        ll1.enableDashedLine(10f, 10f, 0f);
//        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll1.setTextSize(10f);
//        ll1.setTypeface(tf);

//        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        ll2.setTextSize(10f);
//        ll2.setTypeface(tf);

        YAxis leftAxis = binding.mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
//        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        binding.mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
//        setData(data);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

//        binding.mChart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = binding.mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
//        ArrayList<String> pastDays = DateUtils.getPastDays(10);

//        System.out.println(pastDays);
    }


    private void getData() {
        LogUtil.i("room id is:"+room_id);
        stateView.showLoading();
        binding.content.setVisibility(View.INVISIBLE);
        addSubscription(api.getMeterHis(room_id, dictionaries.get(paramName)), new BaseSubscriber<HisEntity>(this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                stateView.showRetry();
            }

            @Override
            public void onNext(HisEntity hisEntity) {
                super.onNext(hisEntity);
                data = hisEntity;
                setData(data);

            }
        });
    }


    private void setData(HisEntity hisEntity) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        List<String> ls = new ArrayList<>();
        ls.clear();
        values.clear();
        if (mDisplayType == DISPLAY_WEEK) {
            if (hisEntity.week_data == null || hisEntity.week_data.size() == 0) {
                stateView.showEmpty();
                return;
            }
            for (int i = 0; i < hisEntity.week_data.size(); i++) {
                float val = 0f;
                if (hisEntity.week_data.get(hisEntity.week_data.size()-1-i).data != null)
                    val = Float.parseFloat(hisEntity.week_data.get(hisEntity.week_data.size()-1-i).data);
                values.add(new Entry(i, val));
                String substring = hisEntity.week_data.get(hisEntity.week_data.size()-1-i).update_time.substring(8, 10);
                if (substring.substring(0, 1).equals("0"))
                    substring = substring.substring(1, 2);
                ls.add(substring+"日");
            }

        }
        if (mDisplayType == DISPLAY_MONTH) {
            if (hisEntity.month_data == null || hisEntity.month_data.size() == 0) {
                stateView.showEmpty();
                return;
            }
            for (int i = 0; i < hisEntity.month_data.size(); i++) {
                float val = 0f;
                if (hisEntity.month_data.get(hisEntity.month_data.size() - 1 - i).data != null)
                    val = Float.parseFloat(hisEntity.month_data.get(hisEntity.month_data.size() - 1 - i).data);
                values.add(new Entry(i, val));
                String substring = hisEntity.month_data.get(hisEntity.month_data.size() - 1 - i).update_time.substring(8, 10);
                if (substring.substring(0, 1).equals("0"))
                    substring = substring.substring(1, 2);
                ls.add(substring + "日");
            }

        }

        if (mDisplayType == DISPLAY_DAY) {
            if (hisEntity.day_data == null || hisEntity.day_data.size() == 0) {
                stateView.showEmpty();
                return;
            }
            int size = hisEntity.day_data.size();
            for (int i = 0; i < size; i++) {
                float val = 0f;
                if (hisEntity.day_data.get(size-1-i).data != null)
                    val = Float.parseFloat(hisEntity.day_data.get(size-1-i).data);
                values.add(new Entry(i, val));
                String substring = hisEntity.day_data.get(size-1-i).update_time.substring(11, 13);
//                if (substring.substring(0, 1).equals("0"))
//                    substring = substring.substring(1, 2);
                ls.add(substring+"时");
            }
        }

        MyAxisValueFormatter formatter = new MyAxisValueFormatter(ls);
        binding.mChart.getXAxis().setValueFormatter(formatter);

        LineDataSet set1;

        if (binding.mChart.getData() != null &&
                binding.mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setLabel(dictionaries.get(mDisplayType)+paramName + dictionaries.get(paramName + "Unit"));
            List<Entry> values1 = set1.getValues();
//            for (Entry s :
//                    values1) {
//                System.out.println(s.toString());
//            }
            binding.mChart.getData().notifyDataChanged();
            binding.mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, dictionaries.get(mDisplayType)+paramName + dictionaries.get(paramName + "Unit"));

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setDrawValues(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_chart);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(getResources().getColor(R.color.colorPrimary));
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            binding.mChart.setData(data);
        }
        binding.mChart.invalidate();

        setRecyclerViewDatas(data);
        stateView.showContent();
        binding.content.setVisibility(View.VISIBLE);
    }

    private List<HisEntity.HisItem> datas = new ArrayList<>();

    class MyAdapter extends BaseQuickAdapter<HisEntity.HisItem, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<HisEntity.HisItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HisEntity.HisItem item) {
            helper.setText(R.id.value, item.data == null ? "0.00" : item.data);
            helper.setText(R.id.time, item.update_time == null ? "0.00" : item.update_time);
        }
    }

    private void setRecyclerViewDatas(HisEntity hisEntity) {
        if (hisEntity == null) {
            return;
        }
        datas.clear();
        if (mDisplayType == DISPLAY_WEEK) {
            datas.addAll(hisEntity.week_data);
        } else if (mDisplayType == DISPLAY_MONTH) {
            datas.addAll(hisEntity.month_data);
        } else if (mDisplayType == DISPLAY_DAY) {
            datas.addAll(hisEntity.day_data);
        }
        adapter.notifyDataSetChanged();
    }

}
