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
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.bean.HisEntity;
import net.suntrans.powerpeace.chart.MyAxisValueFormatter;
import net.suntrans.powerpeace.databinding.ActivityAmmeterHisBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.decoration.DefaultDecoration;
import net.suntrans.powerpeace.utils.DateUtils;
import net.suntrans.powerpeace.utils.StatusBarCompat;
import net.suntrans.stateview.StateView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static net.suntrans.powerpeace.R.id.endTime;
import static net.suntrans.powerpeace.R.id.pin;
import static net.suntrans.powerpeace.R.id.segmented_group;
import static net.suntrans.powerpeace.R.id.startTime;

/**
 * Created by Looney on 2017/9/7.
 */

public class AmmeterHisActivity extends BasedActivity implements View.OnClickListener {

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
    private String code;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int checkedId;
    private CompatDatePickerDialog pickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ammeter_his);

        StatusBarCompat.compat(binding.headerView);


        room_id = getIntent().getStringExtra("room_id");
        paramName = getIntent().getStringExtra("paramName");
        code = getIntent().getStringExtra("code");
        String title = getIntent().getStringExtra("title");
        if (title == null)
            binding.toolbar.setTitle(paramName + "历史记录");
        else
            binding.toolbar.setTitle(title + "宿舍" + paramName + "历史记录");
        stateView = StateView.inject(binding.content);
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getData(binding.startTime.getText().toString(),binding.endTime.getText().toString());
            }
        });
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

        binding.startTime.setOnClickListener(this);
        binding.endTime.setOnClickListener(this);


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        binding.startTime.setText(mYear + "-" + pad(mMonth) + "-" + pad(mDay));
        binding.endTime.setText(mYear + "-" + pad(mMonth) + "-" + pad(mDay));
        binding.query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startTime = binding.startTime.getText().toString();
                String endTime = binding.endTime.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                long startTimeLong=0;
                long endTimeLong=0;
                try {
                    startTimeLong  = sdf.parse(startTime).getTime();
                    endTimeLong  = sdf.parse(endTime).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startTimeLong>endTimeLong){
                    UiUtils.showToast("起始时间必须小于结束时间");
                    return;
                }
                getData(startTime,endTime);
            }
        });
    }

    private void initData() {
        dictionaries = new HashMap<>();
        dictionaries.put("电压", "100001");
        dictionaries.put("电压Unit", "(V)");

        dictionaries.put("电流Unit", "(A)");
        dictionaries.put("电流", "100002");

        dictionaries.put("功率", "100003");
        dictionaries.put("功率Unit", "(W)");

        dictionaries.put("功率因数", "100004");
        dictionaries.put("功率因数Unit", "");

        dictionaries.put("电表值", "100005");
        dictionaries.put("电表值Unit", "(度)");

        dictionaries.put("用电量", "100007");
        dictionaries.put("用电量Unit", "(度)");


        dictionaries.put(DISPLAY_DAY, "");
        dictionaries.put(DISPLAY_MONTH, "");
        dictionaries.put(DISPLAY_WEEK, "");
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
        xAxis.setDrawLabels(false);
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
        getData(binding.startTime.getText().toString(),binding.endTime.getText().toString());
//        ArrayList<String> pastDays = DateUtils.getPastDays(10);

//        System.out.println(pastDays);
    }


    private void getData(String startTime,String endTime) {
        LogUtil.i("room id is:" + room_id);
        stateView.showLoading();
        binding.mainContent.setVisibility(View.INVISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("room_id", room_id);
        map.put("datapoint", code);
        map.put("beginDate", startTime);
        map.put("endDate", endTime);
        addSubscription(api.getMeterHis(map), new BaseSubscriber<HisEntity>(this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                stateView.showRetry();
                binding.mainContent.setVisibility(View.INVISIBLE);
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


        if (hisEntity == null) {
            stateView.showEmpty();
            binding.mainContent.setVisibility(View.INVISIBLE);
            return;
        }
        if (hisEntity.info == null) {
            stateView.showEmpty();
            binding.mainContent.setVisibility(View.INVISIBLE);
            return;
        }
        if (hisEntity.info.size() == 0) {
            stateView.showEmpty();
            binding.mainContent.setVisibility(View.INVISIBLE);
            return;
        }
        ArrayList<Entry> values = new ArrayList<Entry>();
        List<String> ls = new ArrayList<>();
        ls.clear();
        values.clear();
        for (int i = 0; i < hisEntity.info.size(); i++) {
            float val = 0f;
            val = Float.parseFloat(hisEntity.info.get(i).data);
            values.add(new Entry(i, val));
        }
//        MyAxisValueFormatter formatter = new MyAxisValueFormatter(ls);
//        binding.mChart.getXAxis().setValueFormatter(formatter);

        LineDataSet set1;

        if (binding.mChart.getData() != null &&
                binding.mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setLabel(dictionaries.get(mDisplayType) + paramName + dictionaries.get(paramName + "Unit"));

            binding.mChart.getData().notifyDataChanged();
            binding.mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, dictionaries.get(mDisplayType) + paramName + dictionaries.get(paramName + "Unit"));

            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setDrawCircles(false);
            set1.setCircleRadius(3f);
            set1.setDrawValues(false);
//            set1.setDrawCircleHole(false);
//            set1.setValueTextSize(9f);
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
        binding.mainContent.setVisibility(View.VISIBLE);
    }

    private List<HisEntity.EleParmHisItem> datas = new ArrayList<>();


    class MyAdapter extends BaseQuickAdapter<HisEntity.EleParmHisItem, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<HisEntity.EleParmHisItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HisEntity.EleParmHisItem item) {
            helper.setText(R.id.value, item.data == null ? "0.00" : item.data);
            helper.setText(R.id.time, item.created_at == null ? "0.00" : item.created_at);
        }
    }

    private void setRecyclerViewDatas(HisEntity hisEntity) {
        if (hisEntity == null) {
            return;
        }
        datas.clear();
//        if (mDisplayType == DISPLAY_WEEK) {
//            datas.addAll(hisEntity.week_data);
//        } else if (mDisplayType == DISPLAY_MONTH) {
//            datas.addAll(hisEntity.month_data);
//        } else if (mDisplayType == DISPLAY_DAY) {
//            datas.addAll(hisEntity.day_data);
//        }
        datas.addAll(hisEntity.info);
        adapter.notifyDataSetChanged();
    }

    private CompatDatePickerDialog.OnDateSetListener mDateSetListener =
            new CompatDatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    if (checkedId == startTime) {
                        binding.startTime.setText(
                                new StringBuilder()
                                        .append(mYear).append("-")
                                        .append(pad(mMonth)).append("-")
                                        .append(pad(mDay))
                        );
                    } else {
                        binding.endTime.setText(
                                new StringBuilder()
                                        .append(mYear).append("-")
                                        .append(pad(mMonth)).append("-")
                                        .append(pad(mDay))
                        );
                    }


                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onClick(View v) {
        checkedId = v.getId();
        pickerDialog = new CompatDatePickerDialog(this, mDateSetListener, mYear, mMonth - 1, mDay);
        DatePicker datePicker = pickerDialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());

        pickerDialog.show();

    }

}
