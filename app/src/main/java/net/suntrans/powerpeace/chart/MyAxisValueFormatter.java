package net.suntrans.powerpeace.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.List;

public class MyAxisValueFormatter implements IAxisValueFormatter
{


    private List<String> xRealValues;

    public MyAxisValueFormatter(List<String> xRealValues) {
        this.xRealValues = xRealValues;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        return xRealValues.get((int) value);
    }

}
