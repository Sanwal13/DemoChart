package blackriders.demochart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sanwal Singh on 21/4/16.
 */
public class HorizontalBar extends Activity {

    public static final int[] REVIEWS_COLORS = {
            Color.rgb(254, 165, 23), Color.rgb(254, 465, 23), Color.rgb(255, 211, 16),
            Color.rgb(159, 211, 22), Color.rgb(142, 183, 65)};
    protected HorizontalBarChart mChart;
    String url = "http://www.woodenstreet.com/index.php?route=api/api/getProductReviews";
    String product_id = "42";
    ProgressDialog dialog;
    ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<BarEntry> yVals1;
    String TAG = "HorizontalBar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontalbarchart);

        mChart = (HorizontalBarChart) findViewById(R.id.chart1);

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

        xVals.add(0, "1 Star");
        xVals.add(1, "2 Star");
        xVals.add(2, "3 Star");
        xVals.add(3, "4 Star");
        xVals.add(4, "5 Star");


        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(5);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setGridLineWidth(0.3f);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);

        mChart.animateY(2500);

        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getXAxis().setDrawLabels(true);
        mChart.getLegend().setEnabled(false);
        mChart.getXAxis().setTextColor(Color.parseColor("#585858"));
        mChart.getXAxis().setDrawLabels(true);

        userReviews(product_id);
    }

    private void userReviews(final String product_id) {

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait ...");
        dialog.show();

        String strTAG = "userReviews";
        StringRequest strReq_usrRevw = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        Log.d(TAG, "onResponse : " + response);
                        try {
                            yVals1 = new ArrayList<BarEntry>();

                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.getBoolean("status");

                            if (status) {
                                String avg_rating = jsonObject.getString("average_rating");
                                String total_reviews = jsonObject.getString("tatal_reviews");

                                JSONObject jsnObj_rating_cunt = jsonObject.getJSONObject("rating_count");
                                int one = jsnObj_rating_cunt.getInt("1");
                                int two = jsnObj_rating_cunt.getInt("2");
                                int three = jsnObj_rating_cunt.getInt("3");
                                int four = jsnObj_rating_cunt.getInt("4");
                                int five = jsnObj_rating_cunt.getInt("5");

                                yVals1.add(new BarEntry((float) one, 0));
                                yVals1.add(new BarEntry((float) two, 1));
                                yVals1.add(new BarEntry((float) three, 2));
                                yVals1.add(new BarEntry((float) four, 3));
                                yVals1.add(new BarEntry((float) five, 4));

                                JSONArray jsonArray = jsonObject.getJSONArray("reviews");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonOnIndex = jsonArray.getJSONObject(i);
                                    String author = jsonOnIndex.getString("author");
                                    String rating = jsonOnIndex.getString("rating");
                                    String text = jsonOnIndex.getString("text");
                                }
                            }
                            BarDataSet set1 = new BarDataSet(yVals1, "");
                            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                            set1.setColors(REVIEWS_COLORS);
                            set1.setDrawValues(true);
                            dataSets.add(set1);

                            BarData data = new BarData(xVals, dataSets);
                            data.setValueTextSize(10f);
                            data.setDrawValues(true);
                            data.setValueFormatter(new MyValueFormatter());
                            mChart.setData(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error", "Volley Error" + error);
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", product_id);
                return params;
            }
        };
        strReq_usrRevw.setRetryPolicy(new RetryPolicy() {

            @Override
            public void retry(VolleyError error) throws VolleyError {
                // TODO Auto-generated method stub
                Log.e("", "RE-TRY -: " + error);

            }

            @Override
            public int getCurrentTimeout() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getCurrentRetryCount() {
                // TODO Auto-generated method stub
                return 0;
            }
        });
        AppController.getInstance().addToRequestQueue(strReq_usrRevw, strTAG);
    }
}
