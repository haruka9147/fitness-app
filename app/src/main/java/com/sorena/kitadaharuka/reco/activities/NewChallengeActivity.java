package com.sorena.kitadaharuka.reco.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.models.Challenge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewChallengeActivity extends BaseActivity {

    private final String TAG = "NewChallengeActivity";
    private static final String ID = "id";

    private String[] mData;
    private List<Integer> mCheckList;
    private String challengeId;
    private Challenge mChallenge;

    @BindView(R.id.challenge_box) GridLayout mgridLayout;
    @BindView(R.id.challenge_name) EditText mChallengeName;
    @BindView(R.id.challenge_ttl) TextView mTitle;
    @BindView(R.id.save) Button mButton;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    public static Intent newIntent(Context packageContext, String id) {
        Intent intent = new Intent(packageContext, NewChallengeActivity.class);
        intent.putExtra(ID, id);
        return intent;
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, NewChallengeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_challenge);

        ButterKnife.bind(this);

        challengeId = getIntent().getStringExtra(ID);

        // [START connect_firebase]
        String uid = getUid();
        database = FirebaseDatabase.getInstance();
        if(challengeId != null) {
            ref = database.getReference("users").child(uid).child("challenges").child(challengeId);
            mTitle.setText("Update challenge");
        } else {
            ref = database.getReference("users").child(uid).child("challenges");
        }

        // [END connect_firebase]

        mData = new String[30];
        mCheckList = new ArrayList<>();

        for(int i = 0; i < mgridLayout.getChildCount(); i++) {
            EditText editText = (EditText) mgridLayout.getChildAt(i);
            editText.setHint("day" + (i + 1));
            editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(challengeId != null) {
                    updateData();
                } else {
                    saveData();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(challengeId != null) {
            getDataFromFirebase();
        }

    }

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

    private void updateUI() {
        mChallengeName.setText(mChallenge.getName());
        for(int i = 0; i < mgridLayout.getChildCount(); i++) {
            EditText editText = (EditText) mgridLayout.getChildAt(i);
            editText.setText(mChallenge.getData().get(i));
        }
    }

    public void saveData() {
        String id = ref.push().getKey();
        String challenge_name = mChallengeName.getText().toString();
        if (TextUtils.isEmpty(challenge_name)) {
            mChallengeName.setError("Required.");
        } else {
            mChallengeName.setError(null);
        }

        mData = new String[30];
        for(int i = 0; i < mgridLayout.getChildCount(); i++) {
            EditText editText = (EditText) mgridLayout.getChildAt(i);
            String text = editText.getText().toString();
            if(TextUtils.isEmpty(text)) {
                mData[i] = "0";
            } else {
                mData[i] = text;
            }
            mCheckList.add(0);
        }

        Challenge challenge = new Challenge(id, challenge_name, "day1", Arrays.asList(mData), mCheckList);
        ref.child(id).setValue(challenge);

        Toast.makeText(NewChallengeActivity.this, "Data saved!", Toast.LENGTH_SHORT).show();

        startActivity(ChallengeActivity.newIntent(NewChallengeActivity.this));

    }

    public void updateData() {
        String challenge_name = mChallengeName.getText().toString();
        if (TextUtils.isEmpty(challenge_name)) {
            mChallengeName.setError("Required.");
        } else {
            mChallengeName.setError(null);
        }

        mData = new String[30];
        for(int i = 0; i < mgridLayout.getChildCount(); i++) {
            EditText editText = (EditText) mgridLayout.getChildAt(i);
            String text = editText.getText().toString();
            if(TextUtils.isEmpty(text)) {
                mData[i] = "0";
            } else {
                mData[i] = text;
            }
            //String data = String.join("," , mData);
        }

        mChallenge.setName(challenge_name);
        mChallenge.setData(Arrays.asList(mData));
        ref.setValue(mChallenge);

        Toast.makeText(NewChallengeActivity.this, "Data update!", Toast.LENGTH_SHORT).show();

        startActivity(ChallengeActivity.newIntent(NewChallengeActivity.this));
    }

}
