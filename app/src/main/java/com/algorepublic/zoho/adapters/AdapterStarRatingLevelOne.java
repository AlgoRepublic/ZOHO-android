package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.Star;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;


/**
 * Created by android on 2/1/16.
 */
public class AdapterStarRatingLevelOne extends BaseAdapter {
    LayoutInflater l_Inflater;
    Context ctx;
    BaseClass baseClass;
    AQuery aq;

    public AdapterStarRatingLevelOne(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
        Log.e("Size","/"+Star.levelOneHead.size());
    }
    @Override
    public int getCount() {
        return Star.levelOneHead.size();
    }

    @Override
    public Object getItem(int position) {
        return Star.levelOneHead.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.drawer_list_level_one, null);
        aq = new AQuery(convertView);

        aq.id(R.id.lblListHeader).text(Star.levelOneHead.get(position).getTitle());
        return convertView;
    }
}