package net.suntrans.powerpeace.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.adapter.FragmentAdapter;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.HisEntity;
import net.suntrans.powerpeace.chart.MyMarkerView;
import net.suntrans.powerpeace.databinding.ActivityZhCurHisBinding;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.fragment.ZhCurHisItemFragment;
import net.suntrans.powerpeace.utils.StatusBarCompat;
import net.suntrans.stateview.StateView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.suntrans.powerpeace.R.id.startTime;

/**
 * Created by Looney on 2017/9/7.
 */

public class ZHCurHisActivity extends BasedActivity implements View.OnClickListener {

    private static final String TAG = "AmmeterHisActivity";
    private static final String DISPLAY_WEEK = "WEEK";
    private static final String DISPLAY_DAY = "DAY";
    private static final String DISPLAY_MONTH = "MONTH";

    private ActivityZhCurHisBinding binding;
    private String sno;
    private String mDisplayType = DISPLAY_DAY;
    private HisEntity data;
    //    private MyAdapter adapter;
    private StateView stateView;
    private String datapoint;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int checkedId;
    private CompatDatePickerDialog pickerDialog;
    private String unit;
    private String paramName;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_zh_cur_his);

        StatusBarCompat.compat(binding.headerView);

        initData();


        stateView = StateView.inject(binding.content);
        stateView.setOnRetryClickListener(new StateView.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getData(binding.startTime.getText().toString(), binding.endTime.getText().toString());
            }
        });
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        initChart();


//        adapter = new MyAdapter(R.layout.item_his, datas);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
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
//                setData(data);
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
                long startTimeLong = 0;
                long endTimeLong = 0;
                try {
                    startTimeLong = sdf.parse(startTime).getTime();
                    endTimeLong = sdf.parse(endTime).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (startTimeLong > endTimeLong) {
                    UiUtils.showToast("起始时间必须小于结束时间");
                    return;
                }
                getData(startTime, endTime);
            }
        });

    }

    private boolean threeLine = false;

    private void initData() {

        paramName = getIntent().getStringExtra("paramName");
        unit = getIntent().getStringExtra("unit");
        sno = getIntent().getStringExtra("sno");
        datapoint = getIntent().getStringExtra("datapoint");
        if (paramName != null) {

            if (paramName.contains("A") || paramName.contains("B") || paramName.contains("C")) {
                threeLine = true;
            }
        }
        if (threeLine) {
            binding.toolbar.setTitle("");
        } else {
            binding.toolbar.setTitle(paramName);
        }
        if (paramName != null) {
            String name = paramName;
            if (name.contains("A")) {
                name = name.replace("A", "三");
            }
            if (name.contains("B")) {
                name = name.replace("B", "三");
            }
            if (name.contains("C")) {
                name = name.replace("C", "三");
            }
            binding.unit.setText(name + "(" + unit + ")");
        }


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
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(binding.mChart); // For bounds control
        binding.mChart.setMarker(mv); // Set the marker to the chart

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
        xAxis.setLabelCount(3);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            SimpleDateFormat mFormat = new SimpleDateFormat("dd日HH:mm");//created_at=2017-10-23 15:30:29

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                return mFormat.format(new Date((long) value));
            }
        });
//

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
//        leftAxis.setAxisMinimum(0f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
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
        getData(binding.startTime.getText().toString(), binding.endTime.getText().toString());
//        ArrayList<String> pastDays = DateUtils.getPastDays(10);

//        System.out.println(pastDays);
    }


    private void getData(String startTime, String endTime) {
        LogUtil.i("room id is:" + sno);
        binding.query.setEnabled(false);
        stateView.showLoading();
        binding.mainContent.setVisibility(View.INVISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("sno", sno);
        map.put("datapoint", datapoint);
        map.put("beginDate", startTime);
        map.put("endDate", endTime);
        addSubscription(RetrofitHelper.getApi().getZHCurHis(map), new BaseSubscriber<HisEntity>(this) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                e.printStackTrace();
                binding.query.setEnabled(true);
                stateView.showRetry();
                binding.mainContent.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNext(HisEntity hisEntity) {
                super.onNext(hisEntity);
                binding.query.setEnabled(true);
                data = hisEntity;
                try {
                    setData(data);
                } catch (ParseException e) {
                    binding.query.setEnabled(true);
                    stateView.showRetry();
                    binding.mainContent.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void setData(HisEntity hisEntity) throws ParseException {


        if (hisEntity == null) {
            stateView.showEmpty();
            binding.mainContent.setVisibility(View.INVISIBLE);
            return;
        }
        if (!threeLine) {
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
        } else {
            if (hisEntity.AInfo == null) {
                stateView.showEmpty();
                binding.mainContent.setVisibility(View.INVISIBLE);
                return;
            }
            if (hisEntity.AInfo.data.size() == 0) {
                stateView.showEmpty();
                binding.mainContent.setVisibility(View.INVISIBLE);
                return;
            }
        }
        if (hisEntity.title != null)
            binding.toolbar.setTitle(hisEntity.title + "");

        SimpleDateFormat mFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");//created_at=2017-10-23 15:30:29

        if (!threeLine) {
            ArrayList<Entry> values = new ArrayList<Entry>();
            List<String> ls = new ArrayList<>();
            ls.clear();
            values.clear();
            for (int i = hisEntity.info.size() - 1; i >= 0; i--) {
                Date parse = mFormat.parse(hisEntity.info.get(i).created_at);

                float val = 0f;

                val = Float.parseFloat(hisEntity.info.get(i).data);
                values.add(new Entry(parse.getTime(), val));
            }

            LineDataSet set1;

            if (binding.mChart.getData() != null &&
                    binding.mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                set1.setLabel(paramName + "(" + unit + ")");

                binding.mChart.getData().notifyDataChanged();
                binding.mChart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(values, paramName + "(" + unit + ")");

                set1.setDrawIcons(false);

                // set the line to be drawn like this "- - - - - -"
//                set1.enableDashedLine(10f, 5f, 0f);
//                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(getResources().getColor(R.color.colorPrimary));
                set1.setCircleColor(Color.BLACK);
                set1.setLineWidth(1f);
                set1.setDrawCircles(false);
                set1.setCircleRadius(3f);
                set1.setDrawValues(false);
//            set1.setDrawCircleHole(false);
//            set1.setValueTextSize(9f);
                set1.setDrawFilled(false);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

//                if (Utils.getSDKInt() >= 18) {
//                    // fill drawable only supported on api level 18 and above
//                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_chart);
//                    set1.setFillDrawable(drawable);
//                } else {
//                    set1.setFillColor(getResources().getColor(R.color.colorPrimary));
//                }

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1); // add the datasets

                // create a data object with the datasets
                LineData data = new LineData(dataSets);

                // set data
                binding.mChart.setData(data);
            }
            binding.mChart.invalidate();

        } else {
            ArrayList<Entry> yVals1 = new ArrayList<Entry>();


            String aTitle = hisEntity.AInfo.name;
            String bTitle = hisEntity.BInfo.name;
            String cTitle = hisEntity.CInfo.name;
            for (int i = hisEntity.AInfo.data.size() - 1; i >= 0; i--) {
                float val = Float.parseFloat(hisEntity.AInfo.data.get(i).data);
                Date parse = mFormat.parse(hisEntity.AInfo.data.get(i).created_at);
                yVals1.add(new Entry(parse.getTime(), val));
            }

            ArrayList<Entry> yVals2 = new ArrayList<Entry>();

            for (int i = hisEntity.BInfo.data.size() - 1; i >= 0; i--) {
                float val = Float.parseFloat(hisEntity.BInfo.data.get(i).data);
                Date parse = mFormat.parse(hisEntity.BInfo.data.get(i).created_at);

                yVals2.add(new Entry(parse.getTime(), val));
//            if(i == 10) {
//                yVals2.add(new Entry(i, val + 50));
//            }
            }

            ArrayList<Entry> yVals3 = new ArrayList<Entry>();

            for (int i = hisEntity.CInfo.data.size() - 1; i >= 0; i--) {
                float val = Float.parseFloat(hisEntity.CInfo.data.get(i).data);
                Date parse = mFormat.parse(hisEntity.CInfo.data.get(i).created_at);

                yVals3.add(new Entry(parse.getTime(), val));
            }

            LineDataSet set1, set2, set3;

            if (binding.mChart.getData() != null &&
                    binding.mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(0);
                set2 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(1);
                set3 = (LineDataSet) binding.mChart.getData().getDataSetByIndex(2);
                set1.setValues(yVals1);
                set2.setValues(yVals2);
                set3.setValues(yVals3);
                binding.mChart.getData().notifyDataChanged();
                binding.mChart.notifyDataSetChanged();
                binding.mChart.invalidate();
            } else {
                // create a dataset and give it a type
                set1 = new LineDataSet(yVals1, aTitle);

//                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(Color.YELLOW);
                set1.setCircleColor(Color.YELLOW);
                set1.setHighLightColor(Color.YELLOW);
                set1.setLineWidth(2f);
                set1.setDrawCircles(false);
//                set1.setCircleRadius(3f);
//                set1.setFillAlpha(65);
                set1.setDrawCircleHole(false);
                //set1.setFillFormatter(new MyFillFormatter(0f));
                //set1.setDrawHorizontalHighlightIndicator(false);
                //set1.setVisible(false);
                //set1.setCircleHoleColor(Color.WHITE);

                // create a dataset and give it a type
                set2 = new LineDataSet(yVals2, bTitle);
//                set2.setAxisDependency(YAxis.AxisDependency.LEFT);
                set2.setColor(Color.GREEN);
                set2.setCircleColor(Color.GREEN);
                set2.setHighLightColor(Color.GREEN);
                set2.setLineWidth(1f);
//                set2.setCircleRadius(3f);
                set2.setDrawCircles(false);
//                set2.setFillAlpha(65);
                set2.setDrawCircleHole(false);
                //set2.setFillFormatter(new MyFillFormatter(900f));

                set3 = new LineDataSet(yVals3, cTitle);
//                set3.setAxisDependency(YAxis.AxisDependency.LEFT);
                set3.setColor(Color.RED);
                set3.setCircleColor(Color.RED);
                set3.setHighLightColor(Color.RED);
                set3.setDrawCircles(false);
                set3.setLineWidth(1f);
//                set3.setCircleRadius(3f);
//                set3.setFillAlpha(65);
//                set3.setFillColor(ColorTemplate.colorWithAlpha(Color.RED, 200));
                set3.setDrawCircleHole(false);

                // create a data object with the datasets
                LineData data = new LineData(set1, set2, set3);
                data.setValueTextColor(Color.WHITE);
                data.setValueTextSize(9f);

                // set data
                binding.mChart.setData(data);

            }

//            MyAxisValueFormatter formatter = new MyAxisValueFormatter(ls);
//            binding.mChart.getXAxis().setValueFormatter(formatter);
        }
        setRecyclerViewDatas(data);
//        binding.mChart.invalidate();

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

        if (adapter == null && binding.viewpager.getAdapter() == null) {
            adapter = new FragmentAdapter(getSupportFragmentManager());
            if (!threeLine) {
                ZhCurHisItemFragment fragment = ZhCurHisItemFragment.newInstance((ArrayList<HisEntity.EleParmHisItem>) hisEntity.info);
                adapter.addFragment(fragment, paramName);
            } else {
                ZhCurHisItemFragment fragment1 = ZhCurHisItemFragment.newInstance((ArrayList<HisEntity.EleParmHisItem>) hisEntity.AInfo.data);
                ZhCurHisItemFragment fragment2 = ZhCurHisItemFragment.newInstance((ArrayList<HisEntity.EleParmHisItem>) hisEntity.BInfo.data);
                ZhCurHisItemFragment fragment3 = ZhCurHisItemFragment.newInstance((ArrayList<HisEntity.EleParmHisItem>) hisEntity.CInfo.data);
                adapter.addFragment(fragment1, hisEntity.AInfo.name);
                adapter.addFragment(fragment2, hisEntity.BInfo.name);
                adapter.addFragment(fragment3, hisEntity.CInfo.name);
            }
            binding.viewpager.setOffscreenPageLimit(3);
            binding.viewpager.setAdapter(adapter);
            binding.tabLayout.setupWithViewPager(binding.viewpager);
        } else {
            ZhCurHisItemFragment fragment1 = (ZhCurHisItemFragment) adapter.getItem(0);
            ZhCurHisItemFragment fragment2 = (ZhCurHisItemFragment) adapter.getItem(1);
            ZhCurHisItemFragment fragment3 = (ZhCurHisItemFragment) adapter.getItem(2);
            fragment1.setData(hisEntity.AInfo.data);
            fragment2.setData(hisEntity.BInfo.data);
            fragment3.setData(hisEntity.CInfo.data);

        }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_query,menu);
//        return true;
//    }

}
