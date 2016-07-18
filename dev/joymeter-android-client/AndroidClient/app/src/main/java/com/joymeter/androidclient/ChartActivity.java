package com.joymeter.androidclient;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.joymeter.dto.LevelOfJoyRow;
import com.joymeter.events.bus.EventsBus;
import com.joymeter.events.bus.LevelOfJoyLoadedEvent;
import com.joymeter.events.bus.LoadLevelOfJoyEvent;
import com.joymeter.events.bus.SuggestActivityEvent;
import com.joymeter.events.bus.SuggestActivityLoaded;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class ChartActivity extends Activity {

    private LineChart mChart;
    private Typeface mTf;
    private Button suggestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart);

        EventsBus.getInstance().register(this);

        mChart = (LineChart) findViewById(R.id.chart);

        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");

        suggestButton = (Button) findViewById(R.id.suggestButton);

        suggestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestButton.setEnabled(Boolean.FALSE);

                EventsBus.getInstance().post(new SuggestActivityEvent());
            }
        });

        EventsBus.getInstance().post(new LoadLevelOfJoyEvent());
    }

    private void setupChart(LineChart chart, LineData data, int color) {

        // no description text
        chart.setDescription("");
        chart.setNoDataTextDescription("Es necesarion poseer actividades para visualizar el gráfico.");

        // mChart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        //chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(true);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(true);

        // animate calls invalidate()...
        chart.animateX(2500);
    }

    private LineData setData(int count, float range, List<LevelOfJoyRow> history) {

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        int i=0;
        for (LevelOfJoyRow row: history){
            xVals.add(row.getDateString().substring(0,5));
            yVals.add(new Entry((float)row.getLevel(), i));
            i++;
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Nivel de Felicidad");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleSize(3f);
        set1.setColor(Color.rgb(0, 160, 198));
        set1.setCircleColor(Color.rgb(0, 160, 198));
        set1.setHighLightColor(Color.rgb(0, 160, 198));
        set1.setDrawValues(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        return data;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        EventsBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onLevelOfJoyLoaded(LevelOfJoyLoadedEvent event){
        List<LevelOfJoyRow> history = event.getHistory();
        LineData data = setData(JoymeterPreferences.LOJ_WINDOW_SIZE, 5, history);
        data.setValueTypeface(mTf);

        setupChart(mChart, data, Color.rgb(239, 239, 239));
    }

    @Subscribe
    public void onSuggestActivityLoaded(SuggestActivityLoaded event){
        suggestButton.setEnabled(Boolean.TRUE);
    }
}
