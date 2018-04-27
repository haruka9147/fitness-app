package com.sorena.kitadaharuka.reco.activities;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.fragments.SettingDialogFragment;
import com.sorena.kitadaharuka.reco.models.Setting;
import com.sorena.kitadaharuka.reco.models.User;
import com.sorena.kitadaharuka.reco.views.adapters.SettingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    private final String TAG = "SettingActivity";

    private SettingAdapter mAdapter;
    private ArrayList<Setting> mSetList;
    private User mUser;
    private Calendar calendar;
    private String password;

    @BindView(R.id.set_list) ListView mListView;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private boolean isFacebook;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        isFacebook = mAuth.getCurrentUser().getProviders().get(0).equals("facebook.com");

        // [START connect_firebase]
        String uid = getUid();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users").child(uid).child("info");
        // [END connect_firebase]

        mSetList = new ArrayList<>();

        calendar = Calendar.getInstance();

        //Log.d(TAG, "id: " + mAuth.getCurrentUser().getProviders().get(0));

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
                mUser = dataSnapshot.getValue(User.class);

                if(mSetList.size() == 0) {
                    mSetList.add(new Setting("Target weight", mUser.getTargetWeight()));
                    mSetList.add(new Setting("Height", mUser.getHeight() ));
                    mSetList.add(new Setting("Birthday", showDateFormat(mUser.getBirthday())));
                    if(!isFacebook) {
                        mSetList.add(new Setting("Change your email", ""));
                        mSetList.add(new Setting("Change your password", ""));
                    }
                    mSetList.add(new Setting("Sign out", ""));
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
        if(mAdapter == null) {
            mAdapter = new SettingAdapter(SettingActivity.this, mSetList);
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if((isFacebook && position == 3) || (!isFacebook && position == 5)) {
                        signOut();
                    } else if(position == 2) {
                        final DatePickerDialog datePickerDialog = new DatePickerDialog(SettingActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mUser.setBirthday(String.format(Locale.CANADA, "%d%02d%02d", year, month+1, dayOfMonth));
                                mSetList.get(position).setValue(showDateFormat(mUser.getBirthday()));
                                //Log.d(TAG, "Birthday: " + mUser.getBirthday());
                                ref.setValue(mUser);
                            }
                        },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                        datePickerDialog.show();
                    } else if(position == 3 || position == 4) {
                        FragmentManager manager = getFragmentManager();
                        SettingDialogFragment.newInstance(mSetList.get(position), position, true).show(manager, "dialog");
                    } else {
                        FragmentManager manager = getFragmentManager();
                        SettingDialogFragment.newInstance(mSetList.get(position), position, false).show(manager, "dialog");
                    }
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * sign out
     */
    private void signOut() {
        mAuth.signOut();
        startActivity(LoginActivity.newIntent(this));
    }

    /**
     * update data
     * @param position
     * @param value
     */
    public void updateData(int position, final String value) {
        switch(position) {
            case 0:
                mUser.setTargetWeight(value);
                mSetList.get(position).setValue(value);
                break;
            case 1:
                mUser.setHeight(value);
                mSetList.get(position).setValue(value);
                break;
            case 3:
                if(!isFacebook) {
                    showProgressDialog();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(getUserEmail(), password);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   Log.d(TAG, "User re-authenticated.");
                                   if(task.isSuccessful()) {
                                       user.updateEmail(value)
                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       if (task.isSuccessful()) {
                                                           Toast.makeText(SettingActivity.this, "User email address updated.", Toast.LENGTH_SHORT).show();
                                                       } else {
                                                           Log.w(TAG, "updateEmail:failure", task.getException());
                                                           Toast.makeText(SettingActivity.this, "Authentication failed.",
                                                                   Toast.LENGTH_SHORT).show();

                                                       }
                                                       hideProgressDialog();
                                                   }
                                               });
                                   } else {
                                       Log.w(TAG, "Re-authenticated:failure", task.getException());
                                       Toast.makeText(SettingActivity.this, task.getException().getMessage(),
                                               Toast.LENGTH_LONG).show();
                                   }

                                }
                            });
                    mSetList.get(position).setValue("");
                }
                break;
            case 4:
                if(!isFacebook) {
                    showProgressDialog();
                    final FirebaseUser user = mAuth.getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(getUserEmail(), password);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");
                                    if(task.isSuccessful()) {
                                        user.updatePassword(value)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(SettingActivity.this, "User password updated.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Log.w(TAG, "updatePassword:failure", task.getException());
                                                            Toast.makeText(SettingActivity.this, task.getException().getMessage(),
                                                                    Toast.LENGTH_LONG).show();

                                                        }
                                                        hideProgressDialog();
                                                    }
                                                });
                                    } else {
                                        Log.w(TAG, "Re-authenticated:failure", task.getException());
                                        Toast.makeText(SettingActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                    mSetList.get(position).setValue("");
                }
        }
        ref.setValue(mUser);
    }

    /**
     * check password
     * @param position
     * @param password
     */
    public void reEnterPassword(int position, String password) {
        this.password = password;
        FragmentManager manager = getFragmentManager();
        SettingDialogFragment.newInstance(mSetList.get(position), position, false).show(manager, "dialog");
    }

}
