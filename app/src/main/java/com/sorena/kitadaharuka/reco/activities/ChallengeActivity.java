package com.sorena.kitadaharuka.reco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.models.Challenge;
import com.sorena.kitadaharuka.reco.views.adapters.ChallengeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChallengeActivity extends BaseActivity {

    private final String TAG = "ChallengeActivity";

    private ChallengeAdapter mAdapter;
    private ArrayList<Challenge> mChallengeList;

    @BindView(R.id.challenge_list) ListView mListView;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, ChallengeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        ButterKnife.bind(this);

        // [START connect_firebase]
        String uid = getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(uid).child("challenges");
        // [END connect_firebase]

        mChallengeList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getDataFromFireBase();
    }

    private void getDataFromFireBase() {
        showProgressDialog();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mChallengeList.clear();
                for(DataSnapshot challengeSnapshot : dataSnapshot.getChildren()) {
                    Challenge challenge = challengeSnapshot.getValue(Challenge.class);
                    mChallengeList.add(challenge);
                }
                updateUI();
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI() {
        if(mAdapter == null) {
            mAdapter = new ChallengeAdapter(ChallengeActivity.this, mChallengeList);
            mListView.setAdapter(mAdapter);
            mAdapter.setMode(Attributes.Mode.Single);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void deleteData(String id, int position) {
        ref.child(id).removeValue();
        mChallengeList.remove(position);
    }

    public void editData(String id) {
        startActivity(NewChallengeActivity.newIntent(ChallengeActivity.this, id));
    }

    public void goToCheckPage(String id) {
        startActivity(CheckChallengeActivity.newIntent(ChallengeActivity.this, id));
    }


    @OnClick(R.id.add)
    public void goToNewChallenge() {
        startActivity(NewChallengeActivity.newIntent(ChallengeActivity.this));
    }

}
