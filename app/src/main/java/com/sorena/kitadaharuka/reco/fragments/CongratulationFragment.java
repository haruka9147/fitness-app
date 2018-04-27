package com.sorena.kitadaharuka.reco.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.sorena.kitadaharuka.reco.R;

/**
 * Created by kitadaharuka on 2018/04/02.
 */

public class CongratulationFragment extends DialogFragment {

    private static final String TAG = "CongratulationFragment";

    public static CongratulationFragment newInstance() {
        return new CongratulationFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.9);

        //String message = "Update " + title + ":";

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.congratulation_dialog);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dialogWidth;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // nothing
    }
}
