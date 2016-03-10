package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.StarRatingFragments.StarRatingBaseFragment;
import com.algorepublic.zoho.StarRatingFragments.StarRatingLevelQuestionsFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by android on 2/24/16.
 */
public class AdapterStarRatingLevelThree extends BaseAdapter {
    LayoutInflater l_Inflater;
    Context ctx;
    BaseClass baseClass;
    AQuery aq;
    static ArrayList<StarRatingHeadsLevelThree> levelThrees =new ArrayList<>();

    public AdapterStarRatingLevelThree(Context context, ArrayList<StarRatingHeadsLevelThree> threes) {
        levelThrees.addAll(threes);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }
    @Override
    public int getCount() {
        return levelThrees.size();
    }

    @Override
    public Object getItem(int position) {
        return levelThrees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.drawer_list_level_one, null);
        aq = new AQuery(convertView);
        aq.id(R.id.lblListHeader).text(levelThrees.get(position).getTitle());
        aq.id(R.id.listClick).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StarRatingBaseFragment.textLevel3= levelThrees.get(position).getTitle();
                callFragmentWithBackStack(R.id.starContainer, StarRatingLevelQuestionsFragment.
                        newInstance(levelThrees.get(position).getID()), "StarRatingLevelQuestionsFragment");
            }
        });
        return convertView;
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)ctx).getSupportFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
}