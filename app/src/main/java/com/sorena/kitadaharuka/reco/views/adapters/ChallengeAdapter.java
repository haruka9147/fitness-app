package com.sorena.kitadaharuka.reco.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.activities.ChallengeActivity;
import com.sorena.kitadaharuka.reco.models.Challenge;

import java.util.ArrayList;

/**
 * Created by kitadaharuka on 2018/04/01.
 */

public class ChallengeAdapter extends BaseSwipeAdapter{

    private Context mContext;
    private ArrayList<Challenge> mChallenge;
    private TextView mName;
    private TextView mCurrentDay;

    public ChallengeAdapter(Context mContext, ArrayList<Challenge> objects) {
        this.mContext = mContext;
        this.mChallenge = objects;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.challenge_list, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.close();
            }
        }, 50);

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "Clicked!");
                ((ChallengeActivity) mContext).deleteData(mChallenge.get(position).getId(), position);
                swipeLayout.close();
            }
        });

        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChallengeActivity) mContext).editData(mChallenge.get(position).getId());
                swipeLayout.close();
            }
        });

        v.findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChallengeActivity) mContext).goToCheckPage(mChallenge.get(position).getId());
                swipeLayout.close();
            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        mName = (TextView) convertView.findViewById(R.id.challenge_name);
        mCurrentDay = (TextView) convertView.findViewById(R.id.current_day);
        mName.setText(mChallenge.get(position).getName());
        mCurrentDay.setText(mChallenge.get(position).getCurrent_day());
    }

    @Override
    public int getCount() {
        return mChallenge.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setChallenges(ArrayList<Challenge> challenges) {
        mChallenge = challenges;
    }

}
