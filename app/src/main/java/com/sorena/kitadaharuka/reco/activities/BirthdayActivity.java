package com.sorena.kitadaharuka.reco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.models.Records;
import com.sorena.kitadaharuka.reco.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BirthdayActivity extends BaseActivity {

    private final String TAG = "BirthdayActivity";
    private static final String WEIGHT = "weight";
    private static final String HEIGHT = "height";
    private final int MAX_DATA_NUMBER = 300;

    @BindView(R.id.birth_year) NumberPicker mBirthYear;
    @BindView(R.id.birth_month) NumberPicker mBirthMonth;
    @BindView(R.id.birth_day) NumberPicker mBirthDay;

    private String weight;
    private String height;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    public static Intent newIntent(Context packageContext, String weight, String height) {
        Intent intent = new Intent(packageContext, BirthdayActivity.class);
        intent.putExtra(WEIGHT, weight);
        intent.putExtra(HEIGHT, height);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        ButterKnife.bind(this);

        //Log.d(TAG, "DATA:" + getIntent().getStringExtra(WEIGHT) + "," + getIntent().getStringExtra(HEIGHT));

        weight = getIntent().getStringExtra(WEIGHT);
        height = getIntent().getStringExtra(HEIGHT);

        Calendar cal = Calendar.getInstance();

        mBirthYear.setMinValue(1800);
        mBirthYear.setMaxValue(cal.get(Calendar.YEAR));
        mBirthYear.setValue(1980);

        mBirthMonth.setMinValue(1);
        mBirthMonth.setMaxValue(12);

        mBirthDay.setMinValue(1);
        mBirthDay.setMaxValue(31);

        String uid = getUid();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(uid);

    }

    @OnClick(R.id.finish)
    public void goToEdit() {
        //String id = ref.push().getKey();
        String birth_month = Integer.toString(mBirthMonth.getValue());
        String birth_day = Integer.toString(mBirthDay.getValue());;
        if(birth_month.length() == 1){
            birth_month = "0" + birth_month;
        }
        if(birth_day.length() == 1) {
            birth_day = "0" + birth_day;
        }
        String birthday = mBirthYear.getValue() + "" + birth_month + "" + birth_day;

        User user = new User(weight, height, birthday);

        //Log.d(TAG, "Data saved!");

        showProgressDialog();

        ref.child("info").setValue(user);

        for(int i = MAX_DATA_NUMBER; 0 <= i; i--) {
            ref.child("records").child(getPositionDate(i - MAX_DATA_NUMBER))
                    .setValue(new Records(getPositionDate(i - MAX_DATA_NUMBER)));
        }

        Intent intent = EditActivity.newIntent(getApplicationContext());
        startActivity(intent);
    }

    /**
     * calculate date
     * @param i
     * @return date
     */
    private static String getPositionDate(int i) {
        Date nowDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        cal.add(Calendar.DAY_OF_MONTH, i);

        DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
        return df.format(cal.getTime());
    }
}
