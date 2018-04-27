package com.sorena.kitadaharuka.reco.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.fragments.CongratulationFragment;
import com.sorena.kitadaharuka.reco.models.Challenge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckChallengeActivity extends BaseActivity implements View.OnClickListener{

    private final String TAG = "CheckChallengeActivity";
    private static final String ID = "id";

    @BindView(R.id.challenge_box) GridLayout mGridLayout;
    @BindView(R.id.challenge_ttl) TextView mTitle;

    private String challengeId;
    private Challenge mChallenge;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    public static Intent newIntent(Context packageContext, String id) {
        Intent intent = new Intent(packageContext, CheckChallengeActivity.class);
        intent.putExtra(ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_challenge);

        ButterKnife.bind(this);

        challengeId = getIntent().getStringExtra(ID);

        // [START connect_firebase]
        String uid = getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(uid).child("challenges").child(challengeId);
        // [END connect_firebase]

    }

    @Override
    protected void onStart() {
        super.onStart();

        getDataFromFirebase();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        for(int i = 0; i < mGridLayout.getChildCount(); i++) {
            LinearLayout linearLayout = (LinearLayout) mGridLayout.getChildAt(i);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = 180;
            params.width = 180;
            linearLayout.setLayoutParams(params);
            linearLayout.setTag(i);
            linearLayout.setOnClickListener(this);
        }

    }

    /**
     * get data from firebase
     */
    private void getDataFromFirebase() {
        showProgressDialog();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mChallenge = dataSnapshot.getValue(Challenge.class);

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

        if(mChallenge != null) {
            mTitle.setText(mChallenge.getName());

            List<String> data_arr = mChallenge.getData();
            List<Integer> check_arr = mChallenge.getCheck_list();
            for (int i = 0; i < mGridLayout.getChildCount(); i++) {
                LinearLayout linearLayout = (LinearLayout) mGridLayout.getChildAt(i);
                linearLayout.removeAllViews();
                if (check_arr.get(i) == 0) {
                    linearLayout.setSelected(false);
                    TextView textView1 = new TextView(this);
                    textView1.setGravity(Gravity.CENTER);
                    if(data_arr.get(i).equals("0")) {
                        textView1.setText("REST");
                    } else {
                        textView1.setText(data_arr.get(i));
                    }

                    textView1.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView1.setTextSize(20f);

                    TextView textView2 = new TextView(this);
                    textView2.setGravity(Gravity.CENTER);
                    textView2.setText("Day" + (i + 1));
                    textView2.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView2.setTextSize(14f);
                    linearLayout.addView(textView1);
                    linearLayout.addView(textView2);
                } else {

                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.check);
                    linearLayout.setSelected(true);
                    linearLayout.addView(imageView);
                }
            }

            if(mChallenge.getCurrent_day().equals("day30")) {
                FragmentManager manager = getFragmentManager();
                CongratulationFragment dialog = CongratulationFragment.newInstance();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(dialog, null);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        Log.d(TAG, "position:" + tag);
        Log.d(TAG, "check:" + mChallenge.getCheck_list().get(tag));
        if(mChallenge.getCheck_list().get(tag) == 0){
            mChallenge.getCheck_list().set(tag, 1);
        } else {
            mChallenge.getCheck_list().set(tag, 0);
        }

        for(int i = 0; i < mChallenge.getCheck_list().size(); i++) {
            if(mChallenge.getCheck_list().get(i) == 1) {
                mChallenge.setCurrent_day("day" + (i + 1));
            }
        }

        ref.setValue(mChallenge);
    }
}
