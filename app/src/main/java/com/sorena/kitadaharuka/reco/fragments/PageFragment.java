package com.sorena.kitadaharuka.reco.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.activities.GraphActivity;
import com.sorena.kitadaharuka.reco.models.Records;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class PageFragment extends Fragment {

    private final String TAG = "PageFragment";

    private static final String ARG_PARAM = "page";
    private String mParam;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Records> records;

    private LineChart chart;

    public PageFragment() {
    }

    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int page = getArguments().getInt(ARG_PARAM, 0);
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        //((TextView) view.findViewById(R.id.textView)).setText("Page" + page);

        records = new ArrayList<>();
        records = ((GraphActivity) getActivity()).getmRecords();

        //Log.d(TAG, "date: " + records.get(0).getRecordDate());

        chart = (LineChart) view.findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(true);
        chart.setDrawGridBackground(true);
        chart.setEnabled(true);

        chart.setTouchEnabled(true);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setScaleEnabled(true);

        chart.getLegend().setEnabled(true);

        // Value yAxis

        YAxis leftAxis = chart.getAxisLeft();
        float max_value = 0;
        float min_value = 1000;
        String label = null;
        int num = 0;
        final ArrayList<Entry> values = new ArrayList<>();
        if(page == 0) {
            label = "Weight";
            for(int i = records.size() - 8; i < records.size(); i++) {
                float x = records.get(i).getWeight();
                if(x != 0) {
                    values.add(new Entry(num, x));
                } else {
                    values.add(new Entry(num, 0));
                }

                if(max_value < x) {
                    max_value = x;
                }

                if(x < min_value && x != 0) {
                    min_value = x;
                }
                num++;
            }
        } else if (page == 1) {
            label = "Body Fat";
            for(int i = records.size() - 8; i < records.size(); i++) {
                float x = records.get(i).getBodyFat();
                if(x != 0) {
                    values.add(new Entry(num, x));
                } else {
                    values.add(new Entry(num, 0));
                }

                if(max_value < x) {
                    max_value = x;
                }

                if(x < min_value && x != 0) {
                    min_value = x;
                }
                num++;
            }
        } else {
            label = "Muscle";
            for(int i = records.size() - 8; i < records.size(); i++) {
                float x = records.get(i).getMuscle();
                if(x != 0) {
                    values.add(new Entry(num, x));
                } else {
                    values.add(new Entry(num, 0));
                }

                if(max_value < x) {
                    max_value = x;
                }

                if(x < min_value && x != 0) {
                    min_value = x;
                }
                num++;
            }
        }

        leftAxis.setAxisMaximum(max_value + 10);
        leftAxis.setAxisMinimum(min_value - 10);


        // xAxis
        final ArrayList<String> xValues = new ArrayList<>();
        for(int i = records.size() - 8; i < records.size(); i++) {
            String date = records.get(i).getRecordDate().substring(4,6) +
                    "/" + records.get(i).getRecordDate().substring(6,8);
            xValues.add(date);
        }

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xValues.get((int) value);
            }
        });

        LineDataSet valuesDataSet = new LineDataSet(values, label);
        valuesDataSet.setCircleColor(getResources().getColor(R.color.colorAccent));
        valuesDataSet.setColor(getResources().getColor(R.color.colorAccent));
        valuesDataSet.setLineWidth(1f);
        valuesDataSet.setDrawCircleHole(false);
        valuesDataSet.setCircleRadius(3f);
        valuesDataSet.setValueTextSize(9f);

        LineData lineData = new LineData(valuesDataSet);

        chart.setData(lineData);

        chart.invalidate();
        chart.animateY(2000, Easing.EasingOption.EaseInBack);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}