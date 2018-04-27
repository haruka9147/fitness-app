package com.sorena.kitadaharuka.reco.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.activities.SettingActivity;
import com.sorena.kitadaharuka.reco.models.Setting;

/**
 * Created by kitadaharuka on 2018/03/31.
 */

public class SettingDialogFragment extends DialogFragment {

    private static final String TAG = "SettingDialogFragment";

    private static final String SETTING = "setting";
    private static final String POSITION = "position";
    private static final String ISLOGIN = "islogin";

    public static SettingDialogFragment newInstance(Setting setting, int position, boolean isLogin) {
        Bundle args = new Bundle();
        args.putSerializable(SETTING, setting);
        args.putInt(POSITION, position);
        args.putBoolean(ISLOGIN, isLogin);

        SettingDialogFragment fragment = new SettingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        Setting setting = (Setting) bundle.getSerializable(SETTING);
        final int position = bundle.getInt(POSITION);
        final boolean isLogin = bundle.getBoolean(ISLOGIN);

        String title = null;
        String value = null;
        if(setting != null) {
            title = setting.getTitle();
            value = setting.getValue();
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.9);

        //String message = "Update " + title + ":";

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        TextView textView = (TextView) dialog.findViewById(R.id.title);
        final EditText input = (EditText) dialog.findViewById(R.id.update_value);
        Button cancel = (Button) dialog.findViewById(R.id.cancel_button);
        Button update = (Button) dialog.findViewById(R.id.update_button);

        //Log.d(TAG, "position: " + position);

        String message = null;
        if(position < 2) {
            message = "Update " + title + ":";
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            input.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        }

        if(position == 3 && !isLogin) {
            message = title + ":";
            input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }

        if(position == 4 && !isLogin) {
            message = title + ":";
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        if(isLogin) {
            message = "Enter your password:";
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            update.setText(R.string.login);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = input.getText().toString();
                if(isLogin) {
                    ((SettingActivity) getActivity()).reEnterPassword(position,value);
                } else {
                    ((SettingActivity) getActivity()).updateData(position,value);
                }
                dialog.dismiss();
            }
        });

        input.setText(value);
        textView.setText(message);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dialogWidth;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }
}
