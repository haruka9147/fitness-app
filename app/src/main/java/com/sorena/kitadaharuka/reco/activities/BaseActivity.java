package com.sorena.kitadaharuka.reco.activities;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import com.sorena.kitadaharuka.reco.R;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    /**
     * show progress dialog
     */
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    /**
     * hide progress dialog
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * return firebase uid
     * @return String uid
     */
    public String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * return user email
     * @return String email
     */
    public String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    /**
     * format date string
     * @param date
     * @return String date
     */
    public String showDateFormat(String date) {
        return date.substring(0,4) + "." + date.substring(4,6) + "." + date.substring(6,8);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}
