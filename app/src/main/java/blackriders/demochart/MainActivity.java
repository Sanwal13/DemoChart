package blackriders.demochart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    ArrayList<Entry> x;
    ArrayList<String> y;
    String url = "http://www.mysiponline.com/admin-panel/AndroidApi/navDetails";
    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        x = new ArrayList<Entry>();
        y = new ArrayList<String>();


        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setDescription("");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarkerView(mv);

        XAxis xl = mChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setInverted(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);


        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);


        mChart.invalidate();

        rec_SIPTask("K", "K104");
    }


    private void rec_SIPTask(final String amc_code, final String sch_code) {

        String tag_string_req = "req_sip";

        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Login", "Response: " + response.toString());

                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < 10; i++) {
                                JSONObject jsonObject = array.getJSONObject(i);

                                int value = jsonObject.getInt("nav");
                                String date = jsonObject.getString("date");

                                x.add(new Entry(value, i));
                                y.add(date);

                            }
                            Log.d("", "SIZE OF X : " + x.size());
                            Log.d("", "SIZE OF Y : " + y.size());

                            LineDataSet set1 = new LineDataSet(x, "DataSet 1");
                            set1.setLineWidth(1.5f);
                            set1.setCircleRadius(4f);
                            // create a data object with the datasets
                            LineData data = new LineData(y, set1);
                            // set data
                            mChart.setData(data);


                        } catch (Exception e) {
                            // TODO: handle exception
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Login", "Login Error: " + error.getMessage());
                Toast.makeText(MainActivity.this,
                        "Server too busy. Please try after sometime.",
                        Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("amc_code", amc_code);
                params.put("sch_code", sch_code);
                return params;
            }
        };
        strReq.setRetryPolicy(new RetryPolicy() {

            @Override
            public void retry(VolleyError arg0) throws VolleyError {
                // TODO Auto-generated method stub
                Log.e("", "RE-TRY -: " + arg0);
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
