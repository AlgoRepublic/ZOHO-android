package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.StarRatingFragments.StarRatingLevelThreeFragment;
import com.algorepublic.zoho.StarRatingFragments.StarRatingLevelTwoFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 2/24/16.
 */
public class AdapterStarRatingLevelTwo extends BaseAdapter {
    LayoutInflater l_Inflater;
    Context ctx;
    BaseClass baseClass;
    AQuery aq;
    static ArrayList<StarRatingHeadsLevelTwo> levelTwos =new ArrayList<>();

    public AdapterStarRatingLevelTwo(Context context, ArrayList<StarRatingHeadsLevelTwo> twos) {
        levelTwos.addAll(twos);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }
    @Override
    public int getCount() {
        return levelTwos.size();
    }

    @Override
    public Object getItem(int position) {
        return levelTwos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.drawer_list_level_one, null);
        aq = new AQuery(convertView);
        aq.id(R.id.lblListHeader).text(levelTwos.get(position).getTitle());
        aq.id(R.id.listClick).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.starContainer, StarRatingLevelThreeFragment.
                        newInstance(levelTwos.get(position).getLevelThrees()), "StarRatingLevelThreeFragment");
            }
        });
        return convertView;
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
}