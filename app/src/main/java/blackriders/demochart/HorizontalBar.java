package blackriders.demochart;

import android.app.Activity;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * Created by Sanwal Singh on 21/4/16.
 */
public class HorizontalBar extends Activity {

    protected com.github.mikephil.charting.charts.HorizontalBarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_horizontalbarchart);

        mChart = (com.github.mikephil.charting.charts.HorizontalBarChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                if (e == null)
                    return;

                RectF bounds = mChart.getBarBounds((BarEntry) e);
                PointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(dataSetIndex)
                        .getAxisDependency());

                Log.i("bounds", bounds.toString());
                Log.i("position", position.toString());

            }

            @Override
            public void onNothingSelected() {

            }
        });


        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(5);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);

        setData(5, 50);
        mChart.animateY(2500);
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }

    private void setData(int count, float range) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add(0, "1 Star");
        xVals.add(1, "2 Star");
        xVals.add(2, "3 Star");
        xVals.add(3, "4 Star");
        xVals.add(4, "5 Star");

        for (int i = 0; i < count; i++) {
            yVals1.add(new BarEntry((float) (Math.random() * range), i));
        }
        BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        mChart.setData(data);
    }

}
