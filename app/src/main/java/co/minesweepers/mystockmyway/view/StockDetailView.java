package co.minesweepers.mystockmyway.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import co.minesweepers.mystockmyway.R;
import co.minesweepers.mystockmyway.model.Stock;
import co.minesweepers.mystockmyway.model.StockData;

public class StockDetailView implements OnChartValueSelectedListener {
    private LineChart mChart;
    private TextView mTextViewX;
    private TextView mTextViewY;
    private TextView mTextViewStockSymbol;

    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stock_details_fragment, container, false);
        mTextViewStockSymbol = (TextView) rootView.findViewById(R.id.stock_name);
        mTextViewX = (TextView) rootView.findViewById(R.id.x);
        mTextViewY = (TextView) rootView.findViewById(R.id.y);
        mChart = (LineChart) rootView.findViewById(R.id.chart1);
        init();
        return rootView;
    }

    private void init() {
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        mChart.setOnChartValueSelectedListener(this);
    }

    private void setupChart(LineChart chart, LineData data, int color) {
        chart.setDescription("");
        chart.setDrawGridBackground(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setExtraLeftOffset(10);
	    chart.setExtraRightOffset(10);

        // add data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        // animate calls invalidate()...
        chart.animateX(2000);
    }

	private LineData getData(Stock stock) {
		ArrayList<StockData> stockData = stock.getData();
		ArrayList<String> xAxisData = new ArrayList<>();
		ArrayList<Entry> highData = new ArrayList<>();
		ArrayList<Entry> lowData = new ArrayList<>();
		ArrayList<Entry> closeData = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			StockData data = stockData.get(i);
			xAxisData.add(data.getDate().toString());
			highData.add(new Entry(data.getHigh().floatValue(), i));
			lowData.add(new Entry(data.getLow().floatValue(), i));
			closeData.add(new Entry(data.getClose().floatValue(), i));
		}

		LineDataSet high = new LineDataSet(highData, "High");
		LineDataSet low = new LineDataSet(lowData, "Low");
		LineDataSet close = new LineDataSet(closeData, "Close");

		high.setLineWidth(1.5f);
		high.setCircleSize(2f);
		high.setColor(Color.GREEN);
		high.setDrawValues(false);

		low.setLineWidth(1.5f);
		low.setCircleSize(2f);
		low.setColor(Color.RED);
		low.setDrawValues(false);

		close.setLineWidth(1.5f);
		close.setCircleSize(2f);
		close.setColor(Color.YELLOW);
		close.setDrawValues(false);

		ArrayList<LineDataSet> dataSets = new ArrayList<>();
		dataSets.add(high);
		dataSets.add(low);
		dataSets.add(close);

		return new LineData(xAxisData, dataSets);
	}

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        String xVal = String.valueOf(e.getXIndex());
        String yVal = String.valueOf(e.getVal());
        mTextViewY.setText(yVal);
        mTextViewX.setText(xVal);
    }

    @Override
    public void onNothingSelected() {
        mTextViewY.setText("");
        mTextViewX.setText("");
    }

    public void setStock(Stock stock) {
        mTextViewStockSymbol.setText(stock.getSymbol());
        LineData data = getData(stock);
        setupChart(mChart, data, Color.BLACK);
    }
}
