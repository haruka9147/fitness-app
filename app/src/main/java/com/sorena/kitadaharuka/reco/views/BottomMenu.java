package com.sorena.kitadaharuka.reco.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.GridLayout;
import android.widget.ImageButton;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.activities.ChallengeActivity;
import com.sorena.kitadaharuka.reco.activities.EditActivity;
import com.sorena.kitadaharuka.reco.activities.GraphActivity;
import com.sorena.kitadaharuka.reco.activities.SettingActivity;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kitadaharuka on 2018/03/13.
 */

public class BottomMenu extends GridLayout {

    @BindColor(R.color.btnSelected) int btnSelected;

    @BindView(R.id.btn_run) ImageButton mBtnRun;
    @BindView(R.id.btn_edit) ImageButton mBtnEdit;
    @BindView(R.id.btn_graph) ImageButton mBtnGraph;
    @BindView(R.id.btn_cog) ImageButton mBtnCog;
    @BindView(R.id.bottom_menu) GridLayout mLayout;

    private String activityName;

    public BottomMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.menu_bottom, this);
        ButterKnife.bind(this);

        activityName = mLayout.getTag().toString();

        if(activityName.equals("run")) {
            mBtnRun.setBackgroundColor(btnSelected);
            mBtnRun.setEnabled(false);
        } else if(activityName.equals("edit")) {
            mBtnEdit.setBackgroundColor(btnSelected);
            mBtnEdit.setEnabled(false);
        } else if(activityName.equals("graph")) {
            mBtnGraph.setBackgroundColor(btnSelected);
            mBtnGraph.setEnabled(false);
        } else{
            mBtnCog.setBackgroundColor(btnSelected);
            mBtnCog.setEnabled(false);
        }
    }

    @OnClick(R.id.btn_run)
    public void goToRun() {
        Intent intent = ChallengeActivity.newIntent(getContext());
        getContext().startActivity(intent);
    }

    @OnClick(R.id.btn_edit)
    public void goToEdit() {
        Intent intent = EditActivity.newIntent(getContext());
        getContext().startActivity(intent);
    }

    @OnClick(R.id.btn_graph)
    public void goToGraph() {
        Intent intent = GraphActivity.newIntent(getContext());
        getContext().startActivity(intent);
    }

    @OnClick(R.id.btn_cog)
    public void goToCog() {
        Intent intent = SettingActivity.newIntent(getContext());
        getContext().startActivity(intent);
    }

}
