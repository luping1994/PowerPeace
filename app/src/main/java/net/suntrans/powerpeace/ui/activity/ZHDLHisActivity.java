package net.suntrans.powerpeace.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.CompatDatePickerDialog;
import net.suntrans.looney.widgets.SegmentedGroup;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.Ameter3Entity;
import net.suntrans.powerpeace.bean.HisEntity;
import net.suntrans.powerpeace.chart.DayAxisValueFormatter;
import net.suntrans.powerpeace.chart.MyAxisValueFormatter2;
import net.suntrans.powerpeace.rx.BaseSubscriber;
import net.suntrans.powerpeace.ui.fragment.ZongheFragmentPart1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

/**
 * Created by Looney on 2017/7/13.
 */

public class ZHDLHisActivity extends BasedActivity implements OnChartValueSelectedListener, View.OnClickListener {

    private TextView mChartDes;
    private ProgressBar mProgressBar;
    private TextView errorTx;

    protected BarChart mChart;
    protected Typeface mTfLight;
    protected Typeface mTfRegular;


    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView time;
    private SegmentedGroup group;
    private int currentRaidoId = R.id.radio0;
    private String sno;
    private List<Ameter3Entity.DataBean.HisItem> dayDatas;
    private List<Ameter3Entity.DataBean.HisItem> monthDatas;
    private List<Ameter3Entity.DataBean.HisItem> yearDatas;


    int currentType = DayAxisValueFormatter.DAYS;
    private String requestType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ammeter3_2);
        initToolBar();

        mChartDes = (TextView) findViewById(R.id.chartDes);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorTx = (TextView) findViewById(R.id.error);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        time = (TextView) findViewById(R.id.time);
        time.setText(mYear + "-" + pad(mMonth) + "-" + pad(mDay));

        currentRaidoId = R.id.radio0;
        upDateDes();

        group = (SegmentedGroup) findViewById(R.id.segmented_group);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.radio0) {
                    currentRaidoId = R.id.radio0;
                    initChartBype(DayAxisValueFormatter.DAYS);
                }
                if (checkedId == R.id.radio1) {
                    currentRaidoId = R.id.radio1;
                    initChartBype(DayAxisValueFormatter.MONTHS);
                }
                if (checkedId == R.id.radio2) {
                    currentRaidoId = R.id.radio2;
                    initChartBype(DayAxisValueFormatter.YEARS);
                }
                upDateDes();
            }
        });
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        mChart = (BarChart) findViewById(R.id.chart1);
        findViewById(R.id.rili).setOnClickListener(this);


        initChart();

        sno = getIntent().getStringExtra("sno");
        requestType = getIntent().getStringExtra("requestType");
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(sno, time.getText().toString());
    }


    private void initChart() {


        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.setTouchEnabled(false);
        mChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart, DayAxisValueFormatter.DAYS);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(12);
//        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter2();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(mTfLight);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
//        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart

    }

    private void initChartBype(int type) {
//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart, type);
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setValueFormatter(xAxisFormatter);
        currentType = type;
        setData(type);
//        handler.sendMessage(new Message());
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    private void setData(int type) {
        mProgressBar.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.INVISIBLE);
        float start = 1f;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        switch (type) {
            case 1:
                if (dayDatas == null)
                    break;
                for (int i = 0; i <= 23; i++) {
                    float val = 0;
                    for (int j = 0; j < dayDatas.size(); j++) {
                        if ((dayDatas.get(j).x) == i) {
                            val = Float.parseFloat(dayDatas.get(j).data);
//                            System.out.println(i+":"+dayDatas.get(j).data+"");
                        }
                    }
                    yVals1.add(new BarEntry(i, val));
                }
                break;
            case 2:
                if (monthDatas == null)
                    break;
                for (int i = 1; i <= 31; i++) {
                    float val = 0;
                    for (int j = 0; j < monthDatas.size(); j++) {
                        if (monthDatas.get(j).x == i) {
                            val = Float.parseFloat(monthDatas.get(j).data);
                        }
                    }
                    yVals1.add(new BarEntry(i, val));
                }
                break;
            case 3:
                if (yearDatas == null)
                    break;
                for (int i = 1; i <= 12; i++) {
                    float val = 0;
                    for (int j = 0; j < yearDatas.size(); j++) {
                        if (yearDatas.get(j).x == i) {
                            val = Float.parseFloat(yearDatas.get(j).data);
                        }
                    }
                    yVals1.add(new BarEntry(i, val));
                }
                break;
        }


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(MATERIAL_COLORS);
            set1.setDrawValues(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.8f);

            mChart.setData(data);
        }
        mChart.invalidate();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSuccessState();
            }
        }, 1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rili:
                CompatDatePickerDialog pickerDialog = new CompatDatePickerDialog(this, mDateSetListener, mYear, mMonth - 1, mDay);
                DatePicker datePicker = pickerDialog.getDatePicker();
                datePicker.setMaxDate(System.currentTimeMillis());
                if (currentRaidoId == R.id.radio0) {
                    pickerDialog.setTitle(getString(R.string.choose_date));
//                    ((ViewGroup) ((ViewGroup) (datePicker.getChildAt(0))).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
//                    ((ViewGroup) ((ViewGroup) (datePicker.getChildAt(0))).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                } else if (currentRaidoId == R.id.radio1) {
                    pickerDialog.setTitle(getString(R.string.picker_dialog_titlt_choose_month));
                    pickerDialog.hideDay();
//                    ((ViewGroup) ((ViewGroup) (datePicker.getChildAt(0))).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
//                    ((ViewGroup) ((ViewGroup) (datePicker.getChildAt(0))).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);

                } else if (currentRaidoId == R.id.radio2) {
                    pickerDialog.setTitle(getString(R.string.picker_dialog_title_choose_year));
                    pickerDialog.hideMonth();
                    pickerDialog.hideDay();

//                    ((ViewGroup) ((ViewGroup) (datePicker.getChildAt(0))).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
//                    ((ViewGroup) ((ViewGroup) (datePicker.getChildAt(0))).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);

                }
                pickerDialog.show();
                break;

        }
    }

    private CompatDatePickerDialog.OnDateSetListener mDateSetListener =
            new CompatDatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    time.setText(
                            new StringBuilder()
                                    .append(mYear).append("-")
                                    .append(pad(mMonth)).append("-")
                                    .append(pad(mDay))
                    );
                    upDateDes();
                    getData(sno, time.getText().toString());

                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    private Handler mHandler = new Handler();

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }

    public static final int[] MATERIAL_COLORS = {
            rgb("#cc7832")
    };


    private void showErrorState() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mChart.setVisibility(View.INVISIBLE);
        errorTx.setVisibility(View.VISIBLE);
    }

    private void showSuccessState() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mChart.setVisibility(View.VISIBLE);
        errorTx.setVisibility(View.INVISIBLE);
    }

    private void showLoadingState() {
        mProgressBar.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.INVISIBLE);
        errorTx.setVisibility(View.INVISIBLE);
    }

    public void reload(View view) {
        getData(sno, time.getText().toString());
    }

    private void getData(String sno, String date) {
        LogUtil.i(sno + ":" + date);
        showLoadingState();
        if (ZongheFragmentPart1.HIS_REQUEST_TYPE_DL.equals(requestType)) {
            addSubscription(RetrofitHelper.getApi().getZHDLHisData(sno, date), new BaseSubscriber<Ameter3Entity>(this) {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    e.printStackTrace();
                    showErrorState();
                }

                @Override
                public void onNext(Ameter3Entity info) {
                    dayDatas = info.info.electricity_day;
                    monthDatas = info.info.electricity_month;
                    yearDatas = info.info.electricity_year;
                    setData(currentType);
                    showSuccessState();
                }


            });
        } else if (ZongheFragmentPart1.HIS_REQUEST_TYPE_SH.equals(requestType)) {
            addSubscription(RetrofitHelper.getApi().getZHSHHisData(sno, date), new BaseSubscriber<Ameter3Entity>(this) {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    e.printStackTrace();
                    showErrorState();
                }

                @Override
                public void onNext(Ameter3Entity info) {
                    dayDatas = info.info.electricity_day;
                    monthDatas = info.info.electricity_month;
                    yearDatas = info.info.electricity_year;
                    setData(currentType);
                    showSuccessState();
                }


            });
        } else {
            UiUtils.showToast("未知数据源");
        }


    }

    private void upDateDes() {
        if (currentRaidoId == R.id.radio0) {
            if (ZongheFragmentPart1.HIS_REQUEST_TYPE_DL.equals(requestType)) {

                mChartDes.setText(String.format(getString(R.string.ele_his_day_des), mYear + "", mMonth + "", mDay + ""));
            } else if (ZongheFragmentPart1.HIS_REQUEST_TYPE_SH.equals(requestType)) {
                mChartDes.setText(String.format(getString(R.string.sh_his_day_des), mYear + "", mMonth + "", mDay + ""));

            }

        }
        if (currentRaidoId == R.id.radio1) {
            if (ZongheFragmentPart1.HIS_REQUEST_TYPE_DL.equals(requestType)) {
                mChartDes.setText(String.format(getString(R.string.ele_his_month_des), mYear + "", mMonth + ""));

            } else if (ZongheFragmentPart1.HIS_REQUEST_TYPE_SH.equals(requestType)) {
                mChartDes.setText(String.format(getString(R.string.sh_his_month_des), mYear + "", mMonth + ""));

            }

        }
        if (currentRaidoId == R.id.radio2) {
            if (ZongheFragmentPart1.HIS_REQUEST_TYPE_DL.equals(requestType)) {
                mChartDes.setText(String.format(getString(R.string.ele_his_year_des), mYear + ""));
            } else if (ZongheFragmentPart1.HIS_REQUEST_TYPE_SH.equals(requestType)) {
                mChartDes.setText(String.format(getString(R.string.sh_his_year_des), mYear + ""));

            }
        }
    }

}

