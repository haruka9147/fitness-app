package com.sorena.kitadaharuka.reco.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.models.Records;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditActivity extends BaseActivity {

    private static final String TAG = "EditActivity";

    private ViewPager viewPager;
    public static ArrayList<Records> mRecords;
    private EditAdapter adapter;

    @BindView(R.id.edit_date) TextView editDate;
    @BindView(R.id.next) ImageButton mNextBtn;
    @BindView(R.id.prev) ImageButton mPrevBtn;

    public static FirebaseDatabase database;
    public static DatabaseReference ref;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, EditActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ButterKnife.bind(this);

        // [START connect_firebase]
        String uid = getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(uid).child("records");
        // [END connect_firebase]

        mRecords = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        showProgressDialog();

        // get data
        getDataFromFirebase();

        String date = getNowDate(0,false);
        editDate.setText(date);

        // set alpha to next button
        mNextBtn.setImageResource(R.drawable.next60);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

    }

    /**
     * update view pager
     */
    private void updateUI() {

        if(adapter == null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            adapter = new EditAdapter(fragmentManager);

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(mRecords.size(), false);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    editDate.setText(showDateFormat(mRecords.get(position).getRecordDate()));
                    if(position != mRecords.size() - 1) {
                        mNextBtn.setImageResource(R.drawable.next);
                        mNextBtn.setEnabled(true);
                    } else {
                        mNextBtn.setImageResource(R.drawable.next60);
                        mNextBtn.setEnabled(false);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            viewPager.getAdapter().notifyDataSetChanged();
        }

    }


    /**
     * get data from firebase
     */
    private void getDataFromFirebase() {
        //get data from firebase
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRecords.clear();
                for(DataSnapshot recordSnapshot : dataSnapshot.getChildren()){
                    //Log.d(TAG, "data: " + recordSnapshot.getValue());
                    Records records = recordSnapshot.getValue(Records.class);
                    mRecords.add(records);
                }

                if(mRecords.size() == 0) {
                    startActivity(WeightActivity.newIntent(EditActivity.this));
                } else {

                    int arr_num = 1;
                    int date_num = 0;
                    while (!mRecords.get(mRecords.size() - arr_num).getRecordDate().equals(getNowDate(date_num, true))) {
                        if (arr_num == 1) {
                            mRecords.add(new Records(getNowDate(date_num, true)));
                            ref.child(getNowDate(date_num, true)).setValue(new Records(getNowDate(date_num, true)));
                        } else {
                            mRecords.add(mRecords.size() - 1, new Records(getNowDate(date_num, true)));
                            ref.child(getNowDate(date_num, true)).setValue(new Records(getNowDate(date_num, true)));
                        }
                        arr_num++;
                        date_num--;
                    }

                    // set view pager
                    updateUI();
                    //viewPager.getAdapter().notifyDataSetChanged();
                    hideProgressDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * get now date
     * @return today's date
     */
    private String getNowDate(int i, boolean database){
        Date nowDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        cal.add(Calendar.DAY_OF_MONTH, i);
        DateFormat df;
        if(database) {
            df = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
        } else {
            df = new SimpleDateFormat("yyyy.MM.dd", Locale.CANADA);
        }
        return df.format(cal.getTime());
    }

    public static class EditAdapter extends FragmentPagerAdapter {

        public EditAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mRecords.size();
        }

        @Override
        public Fragment getItem(int position) {
            return EditFragment.newInstance(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    public static class EditFragment extends Fragment implements View.OnClickListener{

        private EditText mWeightNum;
        private EditText mBodyFatNum;
        private EditText mMuscleNum;
        private EditText mNote;

        private Button saveBtn;

        static final String POSITION = "position";
        int position;

        public EditFragment() {
            // Required empty public constructor
        }

        static EditFragment newInstance(int position) {
            EditFragment fragment = new EditFragment();
            Bundle args = new Bundle();
            args.putInt(POSITION, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                position = getArguments().getInt(POSITION);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            View view = inflater.inflate(R.layout.fragment_edit, container, false);

            mWeightNum = view.findViewById(R.id.weight_num);
            mBodyFatNum = view.findViewById(R.id.bodyFat_num);
            mMuscleNum = view.findViewById(R.id.muscle_num);
            mNote = view.findViewById(R.id.note);
            saveBtn = view.findViewById(R.id.save);

            if(mRecords.get(position).getWeight() != 0) {
                mWeightNum.setText(String.valueOf(mRecords.get(position).getWeight()));

            }

            if(mRecords.get(position).getBodyFat() != 0) {
                mBodyFatNum.setText(String.valueOf(mRecords.get(position).getBodyFat()));

            }

            if(mRecords.get(position).getMuscle() != 0) {
                mMuscleNum.setText(String.valueOf(mRecords.get(position).getMuscle()));
            }

            mNote.setText(mRecords.get(position).getNote());

            saveBtn.setOnClickListener(this);

            return view;
        }

        @Override
        public void onClick(View v) {
            float weight = 0;
            if(!TextUtils.isEmpty(mWeightNum.getText().toString())) {
                weight = Float.valueOf(mWeightNum.getText().toString());
            }

            float bodyFat = 0;
            if(!TextUtils.isEmpty(mBodyFatNum.getText().toString())) {
                bodyFat = Float.valueOf(mBodyFatNum.getText().toString());
            }

            float muscle = 0;
            if(!TextUtils.isEmpty(mMuscleNum.getText().toString())) {
                muscle = Float.valueOf(mMuscleNum.getText().toString());
            }

            String note = mNote.getText().toString();
            String date = mRecords.get(position).getRecordDate();

            Records records = new Records(date, weight, bodyFat, muscle, note);
            ref.child(date).setValue(records);

            Toast.makeText(getContext(), "Data saved!", Toast.LENGTH_SHORT).show();
        }
    }
}
