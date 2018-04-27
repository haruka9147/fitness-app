package com.sorena.kitadaharuka.reco.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.sorena.kitadaharuka.reco.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeightActivity extends AppCompatActivity {

    private final String TAG = "HeightActivity";
    private static final String WEIGHT = "weight";

    @BindView(R.id.height_num1) NumberPicker mHeightNum1;
    @BindView(R.id.height_num2) NumberPicker mHeightNum2;
    @BindView(R.id.height_num3) NumberPicker mHeightNum3;

    private String weight;

    public static Intent newIntent(Context packageContext, String weight) {
        Intent intent = new Intent(packageContext, HeightActivity.class);
        intent.putExtra(WEIGHT, weight);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        //Log.d(TAG, "Weight: " + getIntent().getStringExtra(WEIGHT));

        weight = getIntent().getStringExtra(WEIGHT);

        ButterKnife.bind(this);

        // set number
        mHeightNum1.setMaxValue(2);
        mHeightNum1.setMinValue(1);

        mHeightNum2.setMaxValue(9);
        mHeightNum2.setMinValue(0);

        mHeightNum3.setMaxValue(9);
        mHeightNum3.setMinValue(0);
    }

    @OnClick(R.id.next_step)
    public void goToBirth() {
        int height1 = mHeightNum1.getValue();
        int height2 = mHeightNum2.getValue();
        int height3 = mHeightNum3.getValue();
        String height = height1 + "" + height2 + "" + height3;
        Intent intent = BirthdayActivity.newIntent(getApplicationContext(), weight, height);
        startActivity(intent);
    }
}
