package com.sorena.kitadaharuka.reco.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.sorena.kitadaharuka.reco.models.Records;
import com.sorena.kitadaharuka.reco.views.HoldableViewPager;
import com.sorena.kitadaharuka.reco.fragments.PageFragment;
import com.sorena.kitadaharuka.reco.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GraphActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        PageFragment.OnFragmentInteractionListener {

    private final String TAG = "GraphActivity";

    private TabLayout tabLayout;
    private HoldableViewPager viewPager;
    private final String[] pageTitle = {"Weight", "Body fat", "Muscle"};

    private ArrayList<Records> mRecords;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, GraphActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // [START connect_firebase]
        String uid = getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(uid).child("records");
        // [END connect_firebase]

        mRecords = new ArrayList<>();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (HoldableViewPager) findViewById(R.id.pager);
        viewPager.setSwipeHold(true);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onStart() {
        super.onStart();

        getDataFromFirebase();
    }

    /**
     * get data from firebase
     */
    private void getDataFromFirebase() {
        showProgressDialog();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mRecords.clear();
                for(DataSnapshot challengeSnapshot : dataSnapshot.getChildren()) {
                    Records records = challengeSnapshot.getValue(Records.class);
                    mRecords.add(records);
                }

                updateUI();

                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * update UI
     */
    private void updateUI() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PageFragment.newInstance(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pageTitle[position];
            }

            @Override
            public int getCount() {
                return pageTitle.length;
            }
        };

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setupWithViewPager(viewPager);
    }

    public ArrayList<Records> getmRecords() {
        return mRecords;
    }

}
