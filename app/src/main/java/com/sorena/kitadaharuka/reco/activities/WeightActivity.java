package com.sorena.kitadaharuka.reco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

import com.sorena.kitadaharuka.reco.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kitadaharuka on 2018/03/23.
 */

public class WeightActivity extends AppCompatActivity {

    private final String TAG = "WeightActivity";

    @BindView(R.id.weight_num1) NumberPicker mWeightNum1;
    @BindView(R.id.weight_num2) NumberPicker mWeightNum2;
    @BindView(R.id.weight_num3) NumberPicker mWeightNum3;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, WeightActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        ButterKnife.bind(this);

        // set number
        mWeightNum1.setMaxValue(9);
        mWeightNum1.setMinValue(0);
        mWeightNum1.setValue(5);

        mWeightNum2.setMaxValue(9);
        mWeightNum2.setMinValue(0);

        mWeightNum3.setMaxValue(9);
        mWeightNum3.setMinValue(0);
    }

    @OnClick(R.id.next_step)
    public void goToHeight() {
        int weight1 = mWeightNum1.getValue();
        int weight2 = mWeightNum2.getValue();
        int weight3 = mWeightNum3.getValue();
        String weight = weight1 + "" + weight2 + "." + weight3;
        Intent intent = HeightActivity.newIntent(getApplicationContext(), weight);
        startActivity(intent);
    }
}
