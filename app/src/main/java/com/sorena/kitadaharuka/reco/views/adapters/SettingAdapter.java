package com.sorena.kitadaharuka.reco.views.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sorena.kitadaharuka.reco.R;
import com.sorena.kitadaharuka.reco.models.Setting;

import java.util.ArrayList;

/**
 * Created by kitadaharuka on 2018/03/31.
 */

public class SettingAdapter extends ArrayAdapter<Setting> {
    private final LayoutInflater _inflater;
    private boolean isFacebook;

    static class ViewHolder {
        TextView title;
        TextView value;
        ImageView icon;
    }

    public SettingAdapter(@NonNull Activity context, ArrayList<Setting> objects) {
        super(context, 0, objects);
        _inflater = LayoutInflater.from(context);
        isFacebook = objects.size() == 4;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder vh = null;

        if(v == null) {
            v = _inflater.inflate(R.layout.setting_list, null);
            vh = new ViewHolder();
            vh.title = (TextView) v.findViewById(R.id.list_title);
            vh.value = (TextView) v.findViewById(R.id.list_value);
            vh.icon = (ImageView) v.findViewById(R.id.icon);
            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }

        Setting data = super.getItem(position);

        if(position == 0) {
            vh.value.setText(data.getValue() + "kg");
        } else if(position == 1) {
            vh.value.setText(data.getValue() + "cm");
        } else {
            vh.value.setText(data.getValue());
        }

        vh.title.setText(data.getTitle());

        if((!isFacebook && position == 5) || (isFacebook && position == 3)) {
            vh.icon.setImageResource(R.drawable.signout);
        } else {
            vh.icon.setImageResource(R.drawable.pen);
        }

        return v;
    }
}
